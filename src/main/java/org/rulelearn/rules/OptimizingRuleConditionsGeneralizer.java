/**
 * Copyright (C) Jerzy Błaszczyński, Marcin Szeląg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rulelearn.rules;

import org.rulelearn.core.ComparisonResult;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.UnknownSimpleField;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Rule conditions generalizer that for each considered elementary condition of type "at least" and "at most"
 * keeps record of the interval in which more general evaluations are to be looked for.
 * For instance, if the value set of criterion f1 is {0,1,...,10}, an elementary condition in a certain rule is "f1 &gt;= 5",
 * and one establishes that threshold 2 is too general (i.e., generalized condition "f1 &gt;= 2" is too general, meaning it does not meet rule induction stopping conditions),
 * than this generalizer would internally keep interval (2,5). Then, when iterating over values of f1 for objects
 * indicated by rule conditions' {@link RuleConditions#getIndicesOfApproximationObjects()
 * set of indices of approximation objects} (i.e., objects for which elementary conditions can be generated),
 * only the values falling into interval (2,5) are checked, which speeds up the process.<br>
 * <br>
 * This generalizer should be used only when for all criteria (condition attributes with gain/cost preference type)
 * elementary conditions added to rule conditions were evaluated using {@link MonotonicConditionAdditionEvaluator monotonic condition addition evaluators},
 * which is the case, e.g., when {@link M4OptimizedConditionGenerator} was used.<br>
 * <br>
 * This generalizer assumes that given an elementary condition for a gain/cost criterion, originally present in rule conditions,
 * if some tested more general elementary condition does not meet rule induction stopping conditions (as verified by the used {@link RuleInductionStoppingConditionChecker checker}),
 * then even more general elementary conditions with respect to the same criterion also do not meet these stopping conditions,
 * so checking of these even more general elementary conditions is redundant and can be safely skipped.<br>
 * <br>
 * This generalizer is capable of generalizing elementary conditions only for criteria with {@link SimpleField} evaluations.<br>
 * <br>
 * This generalizer relies strongly on the assumption that there are no redundant elementary conditions (e.g., a {@link RuleConditionsPruner rule conditions pruner} have been applied before
 * to the {@link RuleConditions rule conditions} that are expected to be generalized here). This is due to desired execution time optimization.<br>
 * <br>
 * Different scenarios considered in this generalizer are presented in the following graphics:
 * <img src="./doc-files/OptimizingRuleConditionsGeneralizer.png" alt="Condition generalization scenarios"> and in this file:
 * <a href="./doc-files/OptimizingRuleConditionsGeneralizer-generalization-possibilities.xlsx" alt="Condition generalization scenarios">OptimizingRuleConditionsGeneralizer-generalization-possibilities.xlsx</a>
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class OptimizingRuleConditionsGeneralizer extends AbstractRuleConditionsGeneralizer {
	
	/**
	 * Type of elementary condition in rule conditions.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	enum ConditionType {
		/**
		 * Concerns condition with relation &gt;=.
		 */
		GE, //>=
		/**
		 * Concerns condition with relation &lt;=.
		 */
		LE //<=
	}
	
	/**
	 * Exception thrown when elementary condition cannot be generalized.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	class NonGeneralizableConditionException extends RuntimeException {
		/**
		 * Generated serial version UID.
		 */
		private static final long serialVersionUID = -2834581221295535426L;

		/**
		 * Constructs this exception with message of failure reason.
		 * 
		 * @param message message of this exception
		 */
		public NonGeneralizableConditionException(String message) {
			super(message);
		}
	}
	
	/**
	 * Interval for limiting evaluation of a generalizing condition. Used to ensure minimality of rule conditions (i.e., situation when each elementary condition is as general as possible).
	 * This interval is to be used only for a single evaluation attribute with linearly ordered domain (of type {@link SimpleField}) for which there exists a condition of type &gt;= or &lt;=.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	class GeneralizingConditionLimitingEvaluationInterval {
		/**
		 * Acceptably general limiting evaluation initialized with original limiting evaluation present in the elementary condition to be generalized. 
		 */
		SimpleField acceptablyGeneralEvaluation;
		/**
		 * Too general limiting evaluation. Can be initialized with {@code null}, and stay {@code null}.
		 */
		KnownSimpleField tooGeneralEvaluation;
		/**
		 * Type of rule. Accepted values are: {@link RuleType#CERTAIN} and {@link RuleType#POSSIBLE}.
		 */
		RuleType ruleType;
		/**
		 * Type of elementary condition - see {@link ConditionType}.
		 */
		ConditionType conditionType;
		/**
		 * Tells if attribute's missing value compares to a concrete value. 
		 */
		boolean mvEqualWhenComparedToAnyEvaluation;
		/**
		 * Tells if a concrete attribute's value compares to attribute's missing value. 
		 */
		boolean mvEqualWhenReverseComparedToAnyEvaluation;
		/**
		 * Type of attribute's missing value.
		 */
		UnknownSimpleField missingValueType;
		
		/**
		 * Constructor. Assumes all parameters are not {@code null}. Initializes {@link #acceptablyGeneralEvaluation} with condition's limiting evaluation.
		 * 
		 * @param ruleConditions rule conditions that this generalizer is trying to generalize
		 * @param conditionIndex index of elementary condition that this generalizer is trying to generalize
		 * 
		 * @throws NonGeneralizableConditionException if considered condition cannot be potentially generalized
		 */
		GeneralizingConditionLimitingEvaluationInterval(RuleConditions ruleConditions, int conditionIndex) {
			Condition<EvaluationField> condition = ruleConditions.getCondition(conditionIndex);
			this.missingValueType = condition.getAttributeWithContext().getAttribute().getMissingValueType();
			
			this.mvEqualWhenComparedToAnyEvaluation = this.missingValueType.equalWhenComparedToAnyEvaluation();
			this.mvEqualWhenReverseComparedToAnyEvaluation = this.missingValueType.equalWhenReverseComparedToAnyEvaluation();
			this.acceptablyGeneralEvaluation = (SimpleField)condition.getLimitingEvaluation();
			this.tooGeneralEvaluation = null;
			this.ruleType = ruleConditions.getRuleType();
			this.conditionType = condition instanceof ConditionAtLeast ? ConditionType.GE : ConditionType.LE;
			
			if (!conditionCanPotentiallyBeGeneralized()) {
				throw new NonGeneralizableConditionException("Condition cannot be generalized.");
			}
		}
		
		/**
		 * Tells if condition for which this interval has been created can potentially be generalized.
		 * Assumes {@link #acceptablyGeneralEvaluation} stores original condition's limiting evaluation.
		 * 
		 * @return {@code true} if condition for which this interval has been created can potentially be generalized,
		 *         {@code false} otherwise
		 */
		private boolean conditionCanPotentiallyBeGeneralized() {
			switch (ruleType) {
			case CERTAIN:
				if (mvEqualWhenComparedToAnyEvaluation) {
					if (acceptablyGeneralEvaluation.equals(missingValueType)) {
						return false; //this should not happen (condition with considered type of mv should not be induced, as it does not reduce the number of covered objects) 
					}
				} else { //!mvEqualWhenComparedToAnyEvaluation
					if (acceptablyGeneralEvaluation.equals(missingValueType)) {
						return mvEqualWhenReverseComparedToAnyEvaluation;
					}
				}
				break;
			case POSSIBLE:
				if (mvEqualWhenReverseComparedToAnyEvaluation) {
					if (acceptablyGeneralEvaluation.equals(missingValueType)) {
						return false; //this should not happen (condition with considered type of mv should not be induced, as it does not reduce the number of covered objects) 
					}
				} else { //!mvEqualWhenReverseComparedToAnyEvaluation
					if (acceptablyGeneralEvaluation.equals(missingValueType)) {
						return mvEqualWhenComparedToAnyEvaluation;
					}
				}
				break;
			default:
				throw new InvalidValueException("Rule type is neither CERTAIN not POSSIBLE.");
			}
			return true;
		}
		
		/**
		 * Tells if given limiting evaluation of an elementary condition is strictly inside this interval.
		 * Note that missing value as limiting evaluation is not allowed as it should never lead to a more general condition (assuming prior pruning of rule conditions!).
		 * 
		 * @param limitingEvaluation limiting evaluation of an elementary condition
		 * @return {@code true} if this interval contains given limiting evaluation,
		 *         {@code false} otherwise
		 */
		boolean includes(KnownSimpleField limitingEvaluation) {
			switch (ruleType) {
			case CERTAIN:
				if (mvEqualWhenComparedToAnyEvaluation) { //acceptablyGeneralEvaluation cannot be a missing value
					switch (conditionType) {
					case GE:
						return (tooGeneralEvaluation == null || limitingEvaluation.compareTo(tooGeneralEvaluation) > 0)
								&& limitingEvaluation.compareToEnum(acceptablyGeneralEvaluation) == ComparisonResult.SMALLER_THAN;
					case LE:
						return limitingEvaluation.compareToEnum(acceptablyGeneralEvaluation) == ComparisonResult.GREATER_THAN 
								&& (tooGeneralEvaluation == null || limitingEvaluation.compareTo(tooGeneralEvaluation) < 0);
					default:
						throw new InvalidValueException("Condition type is neither GE nor LE.");
					}
				}
				else { //!mvEqualWhenComparedToAnyEvaluation - acceptablyGeneralEvaluation can be a missing value
					switch (conditionType) {
					case GE:
						if (!acceptablyGeneralEvaluation.equals(missingValueType)) {
							return (tooGeneralEvaluation == null || limitingEvaluation.compareTo(tooGeneralEvaluation) > 0)
									&& limitingEvaluation.compareToEnum(acceptablyGeneralEvaluation) == ComparisonResult.SMALLER_THAN;
						} else { //mv
							return (tooGeneralEvaluation == null || limitingEvaluation.compareTo(tooGeneralEvaluation) > 0)
									/*&& limitingEvaluation.compareToEnum(acceptablyGeneralEvaluation) == ComparisonResult.EQUAL*/;
						}
					case LE:
						if (!acceptablyGeneralEvaluation.equals(missingValueType)) {
							return limitingEvaluation.compareToEnum(acceptablyGeneralEvaluation) == ComparisonResult.GREATER_THAN
									&& (tooGeneralEvaluation == null || limitingEvaluation.compareTo(tooGeneralEvaluation) < 0);
						} else { //mv
							return /*limitingEvaluation.compareToEnum(acceptablyGeneralEvaluation) == ComparisonResult.EQUAL &&*/
									(tooGeneralEvaluation == null || limitingEvaluation.compareTo(tooGeneralEvaluation) < 0);
						}
					default:
						throw new InvalidValueException("Condition type is neither GE nor LE.");
					}
				}
				//break; //removed - no fall through
			case POSSIBLE:
				if (mvEqualWhenReverseComparedToAnyEvaluation) { //acceptablyGeneralEvaluation cannot be a missing value
					switch (conditionType) {
					case GE:
						return (tooGeneralEvaluation == null || tooGeneralEvaluation.compareTo(limitingEvaluation) < 0)
								&& acceptablyGeneralEvaluation.compareToEnum(limitingEvaluation) == ComparisonResult.GREATER_THAN;
					case LE:
						return acceptablyGeneralEvaluation.compareToEnum(limitingEvaluation) == ComparisonResult.SMALLER_THAN
								&& (tooGeneralEvaluation == null || tooGeneralEvaluation.compareTo(limitingEvaluation) > 0);
					default:
						throw new InvalidValueException("Condition type is neither GE nor LE.");
					}
				}
				else { //!mvEqualWhenReverseComparedToAnyEvaluation - acceptablyGeneralEvaluation can be a missing value
					switch (conditionType) {
					case GE:
						if (!acceptablyGeneralEvaluation.equals(missingValueType)) {
							return (tooGeneralEvaluation == null || tooGeneralEvaluation.compareTo(limitingEvaluation) < 0)
									&& acceptablyGeneralEvaluation.compareToEnum(limitingEvaluation) == ComparisonResult.GREATER_THAN;
						} else { //mv
							return (tooGeneralEvaluation == null || tooGeneralEvaluation.compareTo(limitingEvaluation) < 0)
									/*&& acceptablyGeneralEvaluation.compareToEnum(limitingEvaluation) == ComparisonResult.GREATER_THAN*/;
						}
					case LE:
						if (!acceptablyGeneralEvaluation.equals(missingValueType)) {
							return acceptablyGeneralEvaluation.compareToEnum(limitingEvaluation) == ComparisonResult.SMALLER_THAN
									&& (tooGeneralEvaluation == null || tooGeneralEvaluation.compareTo(limitingEvaluation) > 0);
						} else { //mv
							return /*acceptablyGeneralEvaluation.compareToEnum(limitingEvaluation) == ComparisonResult.SMALLER_THAN &&*/
									(tooGeneralEvaluation == null || tooGeneralEvaluation.compareTo(limitingEvaluation) > 0);
						}
					default:
						throw new InvalidValueException("Condition type is neither GE nor LE.");
					}
				}
				//break; //removed - no fall through
			default:
				throw new InvalidValueException("Rule type is neither CERTAIN not POSSIBLE.");
			}
		}
		
		/**
		 * Updates one of the borders of this interval, assuming that given limiting evaluation is inside this interval.
		 * 
		 * @param limitingEvaluation limiting evaluation inside this interval
		 * @param conditionIsAcceptablyGeneral tells if condition featuring given limiting evaluation is acceptably general (this has to be established outside of this object)
		 */
		void update(KnownSimpleField limitingEvaluation, boolean conditionIsAcceptablyGeneral) { //limitingEvaluation is inside this interval
			if (conditionIsAcceptablyGeneral) {
				acceptablyGeneralEvaluation = limitingEvaluation;
			} else {
				tooGeneralEvaluation = limitingEvaluation;
			}
		}
		
		/**
		 * Gets acceptably general limiting evaluation.
		 * 
		 * @return acceptably general limiting evaluation
		 */
		SimpleField getAcceptablyGeneralEvaluation() {
			return acceptablyGeneralEvaluation;
		}

	}

	/**
	 * Constructor storing given stopping condition checker.
	 * 
	 * @param stoppingConditionChecker stopping condition checker used to assure that generalized rule conditions satisfy stopping condition(s)
	 * @throws NullPointerException if given stopping condition checker is {@code null}
	 */
	public OptimizingRuleConditionsGeneralizer(RuleInductionStoppingConditionChecker stoppingConditionChecker) {
		super(stoppingConditionChecker);
	}

	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * Iterates from the oldest condition (added at the beginning) to the newest (added in the end).
	 * This should offer a good chance to generalize most specific conditions first.
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */	
	@Override
	public int generalize(RuleConditions ruleConditions) {
		Precondition.notNull(ruleConditions, "Rule conditions for optimizing rule conditions generalizer are null.");
		int numberOfGeneralizedConditions = 0;
		
		IntSet indicesOfApproximationObjects = ruleConditions.getIndicesOfApproximationObjects(); //get indices of objects for which elementary conditions may be created
		InformationTable informationTable = ruleConditions.getLearningInformationTable();
		
		int conditionIndex = 0;
		EvaluationAttribute evaluationAttribute;
		GeneralizingConditionLimitingEvaluationInterval limitingEvaluationInterval;
		int attributeIndex;
		IntIterator iterator;
		int approximationObjectIndex;
		SimpleField approximationObjectEvaluation;
		Condition<EvaluationField> oldCondition;
		Condition<EvaluationField> newCondition;
		UnknownSimpleField missingValueType;
		
		while (conditionIndex < ruleConditions.size()) {
			oldCondition = ruleConditions.getCondition(conditionIndex);
			evaluationAttribute = oldCondition.getAttributeWithContext().getAttribute();
			attributeIndex = oldCondition.getAttributeWithContext().getAttributeIndex();
			missingValueType = evaluationAttribute.getMissingValueType();
			
			if (evaluationAttribute.getPreferenceType() != AttributePreferenceType.NONE && evaluationAttribute.getValueType() instanceof SimpleField) { //simple field criterion
				try {
					limitingEvaluationInterval = new GeneralizingConditionLimitingEvaluationInterval(ruleConditions, conditionIndex);
				} catch (NonGeneralizableConditionException exception) {
					continue; //skip condition as it cannot be generalized
				}
				
				iterator = indicesOfApproximationObjects.iterator();
				while (iterator.hasNext()) {
					approximationObjectIndex = iterator.nextInt();
					approximationObjectEvaluation = (SimpleField)informationTable.getField(approximationObjectIndex, attributeIndex);
					
					//<assumed-prior-pruning>
					if (!approximationObjectEvaluation.equals(missingValueType) //approximation object does not have missing value on current attribute
					//</assumed-prior-pruning>
							&& limitingEvaluationInterval.includes((KnownSimpleField)approximationObjectEvaluation)) { //it makes sense to test new condition
						newCondition = ConditionGenerator.constructCondition(ruleConditions.getRuleType(), ruleConditions.getRuleSemantics(), evaluationAttribute, approximationObjectEvaluation, attributeIndex);
						limitingEvaluationInterval.update((KnownSimpleField)approximationObjectEvaluation,
								stoppingConditionChecker.isStoppingConditionSatisifiedWhenReplacingCondition(ruleConditions, conditionIndex, newCondition));
					}
				}
				
				//check if acceptably general limiting evaluation has changed from default (old) value, and if so - replace condition
				if (!limitingEvaluationInterval.getAcceptablyGeneralEvaluation().equals(oldCondition.getLimitingEvaluation())) {
					ruleConditions.replaceCondition(conditionIndex, ConditionGenerator.constructCondition(ruleConditions.getRuleType(), ruleConditions.getRuleSemantics(),
							evaluationAttribute, limitingEvaluationInterval.getAcceptablyGeneralEvaluation(), attributeIndex));
					numberOfGeneralizedConditions++;
				}
				
				
			} //if
			else {
				if (evaluationAttribute.getPreferenceType() == AttributePreferenceType.NONE && evaluationAttribute.getValueType() instanceof SimpleField) { //simple field regular attribute
					if (oldCondition.getLimitingEvaluation().equals(missingValueType)) { //old equality condition concerns missing value, so maybe it can be generalized
						if (   (ruleConditions.getRuleType() == RuleType.CERTAIN &&
								!missingValueType.equalWhenComparedToAnyEvaluation() &&
								missingValueType.equalWhenReverseComparedToAnyEvaluation())
								||
							   (ruleConditions.getRuleType() == RuleType.POSSIBLE) &&
							   !missingValueType.equalWhenReverseComparedToAnyEvaluation() &&
							   missingValueType.equalWhenComparedToAnyEvaluation()) { //condition with missing value can potentially be generalized by substituting a concrete value
						
							iterator = indicesOfApproximationObjects.iterator();
							while (iterator.hasNext()) {
								approximationObjectIndex = iterator.nextInt();
								approximationObjectEvaluation = (SimpleField)informationTable.getField(approximationObjectIndex, attributeIndex);
								
								if (!approximationObjectEvaluation.equals(missingValueType)) { //approximation object does not have missing value - it makes sense to test new condition
									newCondition = ConditionGenerator.constructCondition(ruleConditions.getRuleType(), ruleConditions.getRuleSemantics(), evaluationAttribute, approximationObjectEvaluation, attributeIndex);
									if (stoppingConditionChecker.isStoppingConditionSatisifiedWhenReplacingCondition(ruleConditions, conditionIndex, newCondition)) { //first generalizing condition is found
										ruleConditions.replaceCondition(conditionIndex, newCondition);
										numberOfGeneralizedConditions++;
										break; //stop searching for generalizing condition
									}
								}
							} //while
						} //if
					} //if
				} //if
			} //else
			
			conditionIndex++;
		} //while
		
		return numberOfGeneralizedConditions;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param stoppingConditionChecker {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public OptimizingRuleConditionsGeneralizer copyWithNewStoppingConditionChecker(RuleInductionStoppingConditionChecker stoppingConditionChecker) {
		return new OptimizingRuleConditionsGeneralizer(stoppingConditionChecker);
	}

}
