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

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.Table;
import org.rulelearn.measures.Measure.MeasureType;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.SimpleField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Condition generator taking advantage of the assumption that for any gain/cost-type condition attribute having {@link SimpleField} evaluations (i.e., evaluations which can be completely ordered),
 * the order of elementary conditions involving that attribute, implied by each considered condition addition evaluator, is consistent with the preference order in the value set of that attribute.<br>
 * <br>
 * For example, given attribute q with integer values, the following monotonic relationships are assumed:
 * <ul>
 *   <li>the better the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the better the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each cost-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each cost-type condition addition evaluator,</li>
 * </ul>
 * where q(x) denotes value (evaluation) of object x with respect to attribute q.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class M4OptimizedConditionGenerator extends AbstractConditionGeneratorWithEvaluators {
	
	/**
	 * Constructor for this condition generator. Stores given condition addition evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionaAdditionEvaluators array with condition addition evaluators used lexicographically
	 * 
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 * @throws NullPointerException if type of any condition addition evaluator is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 */
	public M4OptimizedConditionGenerator(ConditionAdditionEvaluator[] conditionaAdditionEvaluators) {
		super(conditionaAdditionEvaluators);
	}
	
	/**
	 * Tells if attributes already present in rule conditions can be skipped when generating next best condition.
	 * 
	 * @return {@code false}
	 */
	boolean skipUsedAttributes() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * During search for the best condition, scans all active condition attributes. For each such an attribute (for one column of considered learning information able),
	 * optimizes scanning of values of considered objects by skipping not relevant values.
	 * During scanning of values in one column, elementary conditions are lexicographically evaluated by the condition addition evaluators that are set in constructor.
	 * Moreover, it is assumed that evaluations of elementary conditions are monotonically dependent on the preference order of an attribute.
	 * This dependency enables to skip checking some conditions and speed up search for the best condition.
	 * 
	 * @param consideredObjects {@inheritDoc}
	 * @param ruleConditions {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws ElementaryConditionNotFoundException when it is impossible to find any new condition that could be added to given rule conditions
	 */
	@Override
	public Condition<EvaluationField> getBestCondition(IntList consideredObjects, RuleConditions ruleConditions) {
		Precondition.notNull(consideredObjects, "List of objects considered in m4-optimized condition generator is null.");
		Precondition.notNull(ruleConditions, "Rule conditions considered in m4-optimized condition generator are null.");
		
		Condition<EvaluationField> bestCondition = null;
		Condition<EvaluationField> candidateCondition = null;
		
		InformationTable learningInformationTable = ruleConditions.getLearningInformationTable();
		Table<EvaluationAttribute, EvaluationField> data = learningInformationTable.getActiveConditionAttributeFields();
		EvaluationAttribute[] activeConditionAttributes = data.getAttributes(true);
		
		EvaluationField objectEvaluation;
		KnownSimpleField extremeLimitingEvaluation; //least/most (depending on the type of the first condition addition evaluator) restrictive limiting evaluation found so far
		KnownSimpleField candidateLimitingEvaluation; //current limiting evaluation to be compared with the most restrictive one
		
		int consideredObjectsCount = consideredObjects.size();
		int consideredObjectsSuccessfullIndex;
		int globalAttributeIndex;
		int compareToMultiplier;
		
		//TODO: handle PairField evaluations (decomposition!)
		//TODO: skip evaluation if missing

		//go through active condition attributes
		for (int localActiveConditionAttributeIndex = 0; localActiveConditionAttributeIndex < activeConditionAttributes.length; localActiveConditionAttributeIndex++) {
			globalAttributeIndex = learningInformationTable.translateLocalActiveConditionAttributeIndex2GlobalAttributeIndex(localActiveConditionAttributeIndex);
			//current attribute should be considered
			if (!(ruleConditions.hasConditionForAttribute(globalAttributeIndex) && this.skipUsedAttributes())) {
				//optimization is possible for current attribute - it is a criterion whose evaluations can be linearly ordered
				if (activeConditionAttributes[localActiveConditionAttributeIndex].getPreferenceType() != AttributePreferenceType.NONE &&
						activeConditionAttributes[localActiveConditionAttributeIndex].getValueType() instanceof SimpleField) { //or KnownSimpleField
					
					extremeLimitingEvaluation = null;
					consideredObjectsSuccessfullIndex = -1; //index of consideredObjects corresponding to first non-missing evaluation
					
					for (int i = 0; i < consideredObjectsCount; i++) {
						objectEvaluation = data.getField(consideredObjects.getInt(i), localActiveConditionAttributeIndex);
						if (objectEvaluation instanceof KnownSimpleField) { //non-missing evaluation found
							extremeLimitingEvaluation = (KnownSimpleField)objectEvaluation;
							consideredObjectsSuccessfullIndex = i; //remember last considered index, so next search can start from the following index
							break;
						}
					}
					
					if (consideredObjectsSuccessfullIndex >= 0) {
						//first, calculate multiplier used to compare two evaluations; if candidateEval.compareTo(referenceEval) * compareToMultiplier > 0, 
						//then candidateEval is more restrictive limiting evaluation than referenceEval, w.r.t. the constructed condition;
						//multiplier takes into account both rule's semantics and attribute's preference type
						compareToMultiplier = (activeConditionAttributes[localActiveConditionAttributeIndex].getPreferenceType() == AttributePreferenceType.GAIN ? 1 : -1) *
								(ruleConditions.getRuleSemantics() == RuleSemantics.AT_LEAST ? 1 : -1);
						//second, iterate through all considered objects to calculate least/most restrictive limiting evaluation of a condition,
						//taking into account rule's semantics and attribute's preference type
						for (int i = consideredObjectsSuccessfullIndex + 1; i < consideredObjectsCount; i++) { //continue loop at next index
							objectEvaluation = data.getField(consideredObjects.getInt(i), localActiveConditionAttributeIndex);
							if (objectEvaluation instanceof KnownSimpleField) { //non-missing evaluation found
								candidateLimitingEvaluation = (KnownSimpleField)objectEvaluation;
								switch(this.conditionAdditionEvaluators[0].getType()) {
								case GAIN:
									if (candidateLimitingEvaluation.compareTo(extremeLimitingEvaluation) * compareToMultiplier < 0) { //less restrictive limiting evaluation found
										extremeLimitingEvaluation = candidateLimitingEvaluation;
									}
									break;
								case COST:
									if (candidateLimitingEvaluation.compareTo(extremeLimitingEvaluation) * compareToMultiplier > 0) { //more restrictive limiting evaluation found
										extremeLimitingEvaluation = candidateLimitingEvaluation;
									}
									break;
								}
							}
						}
						
						//set limits
						//TODO: implement
						
						//at this point, least/most restrictive limiting evaluation among considered objects, for current criterion, has been calculated, so one can construct candidate condition
						candidateCondition = constructCondition(ruleConditions.getRuleType(), ruleConditions.getRuleSemantics(),
								activeConditionAttributes[localActiveConditionAttributeIndex], extremeLimitingEvaluation, globalAttributeIndex);
						
						bestCondition = getBetterCondition(ruleConditions, candidateCondition, bestCondition);
						//TODO: check if can continue at this point, going to next attribute
						
						//TODO: iterate through considered objects
					} else {
						continue; //go to next active condition attribute as for the current attribute all considered objects miss an evaluation (have missing value)
					}
				} else { //proceed without optimization
					//TODO: iterate through considered objects
				}
			}
		}
		
		return bestCondition;
	}
		
	private Condition<EvaluationField> getBetterCondition(RuleConditions ruleConditions, Condition<EvaluationField> candidateCondition, Condition<EvaluationField> bestCondition) {
		double bestConditionEvaluation;
		try {
			bestConditionEvaluation = conditionAdditionEvaluators[0].evaluateWithCondition(ruleConditions, bestCondition);
		} catch (NullPointerException exception) {
			return candidateCondition; //best condition is null
		}
		double candidateConditionEvaluation = conditionAdditionEvaluators[0].evaluateWithCondition(ruleConditions, candidateCondition);
		
		if (candidateConditionEvaluation > bestConditionEvaluation) {
			return (conditionAdditionEvaluators[0].getType() == MeasureType.GAIN ? candidateCondition : bestCondition);
		}
		if (candidateConditionEvaluation < bestConditionEvaluation) {
			return (conditionAdditionEvaluators[0].getType() == MeasureType.GAIN ? bestCondition: candidateCondition);
		}
		
		for (int i = 1; i < conditionAdditionEvaluators.length; i++) {
			bestConditionEvaluation = conditionAdditionEvaluators[i].evaluateWithCondition(ruleConditions, bestCondition);
			candidateConditionEvaluation = conditionAdditionEvaluators[i].evaluateWithCondition(ruleConditions, candidateCondition);
			
			if (candidateConditionEvaluation > bestConditionEvaluation) {
				return (conditionAdditionEvaluators[i].getType() == MeasureType.GAIN ? candidateCondition : bestCondition);
			}
			if (candidateConditionEvaluation < bestConditionEvaluation) {
				return (conditionAdditionEvaluators[i].getType() == MeasureType.GAIN ? bestCondition: candidateCondition);
			}
		}
		
		return bestCondition; //return best condition if both conditions evaluate the same
	}
	
	private Condition<EvaluationField> constructCondition(RuleType ruleType, RuleSemantics ruleSemantics, EvaluationAttribute evaluationAttribute, 
			EvaluationField limitingEvaluation, int globalAttributeIndex) {
		switch (ruleType) {
		case CERTAIN:
			switch (ruleSemantics) {
			case AT_LEAST:
				switch (evaluationAttribute.getPreferenceType()) {
				case GAIN:
					return new ConditionAtLeastThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case COST:
					return new ConditionAtMostThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case NONE:
					return new ConditionEqualThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				default:
					throw new NullPointerException("Attribute preference type is null.");
				}
			case AT_MOST:
				switch (evaluationAttribute.getPreferenceType()) {
				case GAIN:
					return new ConditionAtMostThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case COST:
					return new ConditionAtLeastThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case NONE:
					return new ConditionEqualThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				default:
					throw new NullPointerException("Attribute preference type is null.");
				}
			case EQUAL:
				return new ConditionEqualThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new NullPointerException("Rule semantics is null.");
			}
		case POSSIBLE:
			switch (ruleSemantics) {
			case AT_LEAST:
				switch (evaluationAttribute.getPreferenceType()) {
				case GAIN:
					return new ConditionAtLeastObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case COST:
					return new ConditionAtMostObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case NONE:
					return new ConditionEqualObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				default:
					throw new NullPointerException("Attribute preference type is null.");
				}
			case AT_MOST:
				switch (evaluationAttribute.getPreferenceType()) {
				case GAIN:
					return new ConditionAtMostObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case COST:
					return new ConditionAtLeastObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				case NONE:
					return new ConditionEqualObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
				default:
					throw new NullPointerException("Attribute preference type is null.");
				}
			case EQUAL:
				return new ConditionEqualObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new NullPointerException("Rule semantics is null.");
			}
		default:
			throw new InvalidValueException("Cannot construct condition if rule type is neither certain nor possible.");
		}
	}

}
