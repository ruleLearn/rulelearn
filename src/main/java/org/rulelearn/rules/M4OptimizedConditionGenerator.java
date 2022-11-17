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

import org.rulelearn.core.AttributeNotFoundException;
import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.Table;
import org.rulelearn.measures.Measure.MeasureType;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator.MonotonicityType;
import org.rulelearn.types.CompositeField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.SimpleField;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

/**
 * Condition generator taking advantage of the assumption that for any gain/cost-type condition attribute having {@link SimpleField} evaluations
 * (i.e., evaluations which can be completely ordered),
 * the order of elementary conditions involving that attribute, implied by each considered condition addition evaluator,
 * is consistent with the preference order in the value set of that attribute.
 * It assumes that each considered condition addition evaluator has monotonicity property (m4),
 * which enables to speed up search for conditions related to gain/cost-type condition attributes having {@link SimpleField} evaluations.<br>
 * <br>
 * For example, given attribute q with integer values, the following monotonic relationships are assumed:
 * <ul>
 *   <li>the better the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the better the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each cost-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each cost-type condition addition evaluator,</li>
 * </ul>
 * where q(x) denotes value (evaluation) of object x with respect to attribute q.<br>
 * <br>
 * This condition generator assumes that given a list (array) of monotonic condition addition evaluators {@link MonotonicConditionAdditionEvaluator},
 * that should be considered lexicographically,
 * if that list (array) contains evaluators for which monotonicity type is equal to {@link MonotonicConditionAdditionEvaluator.MonotonicityType#DETERIORATES_WITH_NUMBER_OF_COVERED_OBJECTS}
 * and also evaluators for which monotonicity type is equal to {@link MonotonicConditionAdditionEvaluator.MonotonicityType#IMPROVES_WITH_NUMBER_OF_COVERED_OBJECTS},
 * then that list is composed of exactly two disjoint sublists of evaluators, each containing evaluators of the same monotonicity type.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class M4OptimizedConditionGenerator extends AbstractConditionGeneratorWithEvaluators {

	enum ConditionComparisonResult {
		CANDIDATE_CONDITION_IS_BETTER,
		CANDIDATE_CONDITION_IS_EQUAL,
		/**
		 * Indicates that candidate condition is lexicographically worse than the best condition already with respect to the k first (leading) evaluators having the same monotonicity type. 
		 */
		CANDIDATE_CONDITION_IS_WORSE_WRT_FIRST_EVALUATORS,
		/**
		 * Indicates that candidate condition is lexicographically worse than the best condition only with respect to the n-k last (trailing) evaluators having the same monotonicity type,
		 * different than k first (leading) evaluators. With respect to these k first evaluators, both conditions are equal.
		 * Such situation may occur only if considered evaluators have more than one monotonicity type. 
		 */
		CANDIDATE_CONDITION_IS_WORSE_WRT_SECOND_EVALUATORS
	}
	
	abstract class ConditionLimitingEvaluationInterval {
		KnownSimpleField sufficientEvaluation = null;
		KnownSimpleField insufficientEvaluation = null;
		
		/**
		 * Tells if given limiting evaluation of an elementary condition is inside this interval.
		 * 
		 * @param evaluation evaluation for elementary condition
		 * @param compareToMultiplier see {@link M4OptimizedConditionGenerator#calculateCompareToMultiplier(EvaluationAttribute, RuleConditions)}
		 * @return {@code true} if this interval contains given evaluation, {@code false} otherwise
		 */
		abstract boolean includes(KnownSimpleField evaluation, int compareToMultiplier);
		
		/**
		 * Tells if given candidate condition limiting evaluation is more extreme than given so far most extreme evaluation.
		 * The meaning of the notion "extreme" depends on the evaluation measure used for choosing elementary conditions.
		 * If more specific conditions are preferred, then more extreme evaluation is the one that corresponds to more specific condition.
		 * If more general conditions are preferred, then more extreme evaluation is the one that corresponds to more general condition.
		 * 
		 * @param candidateLimitingEvaluation new evaluation to be tested
		 * @param extremeLimitingEvaluation the most extreme evaluation found so far
		 * @param compareToMultiplier see {@link M4OptimizedConditionGenerator#calculateCompareToMultiplier(EvaluationAttribute, RuleConditions)}
		 * @return {@code true} if given candidate condition limiting evaluation is more extreme than given so far most extreme evaluation,
		 *         {@code false} otherwise
		 */
		abstract boolean isMoreExtremeEvaluation(KnownSimpleField candidateLimitingEvaluation, KnownSimpleField extremeLimitingEvaluation, int compareToMultiplier);
		
		//initializes this interval
		void initialize(KnownSimpleField sufficientEvaluation, KnownSimpleField insufficientEvaluation) {
			this.sufficientEvaluation = sufficientEvaluation;
			this.insufficientEvaluation = insufficientEvaluation;
		}

		//updates this interval using result of comparison of candidate condition and currently best condition
		void update(ConditionComparisonResult candidateVSBestConditionComparisonResult, KnownSimpleField candidateLimitingEvaluation) {
			if (candidateVSBestConditionComparisonResult == ConditionComparisonResult.CANDIDATE_CONDITION_IS_BETTER ||
					candidateVSBestConditionComparisonResult == ConditionComparisonResult.CANDIDATE_CONDITION_IS_EQUAL) {
				sufficientEvaluation = candidateLimitingEvaluation;
			} else {
				if (candidateVSBestConditionComparisonResult == ConditionComparisonResult.CANDIDATE_CONDITION_IS_WORSE_WRT_FIRST_EVALUATORS) {
					insufficientEvaluation = candidateLimitingEvaluation;
				}
			}
		}
		
	}
	
	final class RestrictingConditionLimitingEvaluationInterval extends ConditionLimitingEvaluationInterval {
		//sufficientEvaluation denotes sufficientlyRestrictiveEvaluation
		//insufficientEvaluation denotes insufficientlyRestrictiveEvaluation
		
		/**
		 * Checks if given evaluation is inside open interval<br>
		 * {@code (insufficientEvaluation, sufficientEvaluation)}, for condition of type >=, or<br>
		 * inside interval {@code (sufficientEvaluation, insufficientEvaluation)}, for condition of type <=.
		 * Assumes sufficientEvaluation is not {@code null}.
		 * 
		 * @param evaluation evaluation to be checked for its inclusion inside this interval
		 * @param compareToMultiplier multiplier taking into account both rule's semantics and attribute's preference type;
		 *        enables to compare given evaluation to limits of this interval just as if >= condition would be searched for 
		 * @return {@code true} if given evaluation is inside this interval,
		 *         {@code false} otherwise
		 */
		boolean includes(KnownSimpleField evaluation, int compareToMultiplier) {
			return (insufficientEvaluation == null || evaluation.compareTo(insufficientEvaluation) * compareToMultiplier > 0) &&
					(evaluation.compareTo(sufficientEvaluation) * compareToMultiplier < 0);
		}
		
		//if candidateEval.compareTo(extremeEval) * compareToMultiplier > 0, 
		//then candidateEval is more restrictive limiting evaluation than extremeEval
		boolean isMoreExtremeEvaluation(KnownSimpleField candidateLimitingEvaluation, KnownSimpleField extremeLimitingEvaluation, int compareToMultiplier) {
			return candidateLimitingEvaluation.compareTo(extremeLimitingEvaluation) * compareToMultiplier > 0; //more restrictive limiting evaluation found
		}
	}
	
	final class GeneralizingConditionLimitingEvaluationInterval extends ConditionLimitingEvaluationInterval {
		//sufficientEvaluation denotes sufficientlyGeneralEvaluation
		//insufficientEvaluation denotes insufficientlyGeneralEvaluation
		
		/**
		 * Checks if given evaluation is inside open interval<br>
		 * {@code (sufficientEvaluation, insufficientEvaluation)}, for condition of type >=, or<br>
		 * inside interval {@code (insufficientEvaluation, sufficientEvaluation)}, for condition of type <=.
		 * Assumes sufficientEvaluation is not {@code null}.
		 * 
		 * @param evaluation evaluation to be checked for its inclusion inside this interval
		 * @param compareToMultiplier multiplier taking into account both rule's semantics and attribute's preference type;
		 *        enables to compare given evaluation to limits of this interval just as if >= condition would be searched for 
		 * @return {@code true} if given evaluation is inside this interval,
		 *         {@code false} otherwise
		 */
		boolean includes(KnownSimpleField evaluation, int compareToMultiplier) {
			return (evaluation.compareTo(sufficientEvaluation) * compareToMultiplier > 0) &&
					(insufficientEvaluation == null || evaluation.compareTo(insufficientEvaluation) * compareToMultiplier < 0);
		}
		
		//if candidateEval.compareTo(extremeEval) * compareToMultiplier < 0, 
		//then candidateEval is more general limiting evaluation than extremeEval
		boolean isMoreExtremeEvaluation(KnownSimpleField candidateLimitingEvaluation, KnownSimpleField extremeLimitingEvaluation, int compareToMultiplier) {
			return candidateLimitingEvaluation.compareTo(extremeLimitingEvaluation) * compareToMultiplier < 0; //less restrictive limiting evaluation found
		}
	}
	
	final class ConditionWithEvaluations {
		Condition<EvaluationField> condition;
		double[] evaluations;
		int validEvaluationsCount;
		RuleConditions ruleConditions;
		
		ConditionWithEvaluations(RuleConditions ruleConditions) {
			condition = null;
			evaluations = new double[conditionAdditionEvaluators.length];
			validEvaluationsCount = 0;
			this.ruleConditions = ruleConditions;
		}
		
		void setCondition(Condition<EvaluationField> condition) {
			this.condition = condition;
			this.validEvaluationsCount = 0;
		}
		
		/**
		 * Copies fields from given condition with evaluations to this object.
		 * 
		 * @param conditionWithEvaluations other condition with evaluations that contains fields that should be copied to this object
		 */
		void copy(ConditionWithEvaluations conditionWithEvaluations) {
			this.condition = conditionWithEvaluations.condition;
			for (int i = 0; i < conditionWithEvaluations.validEvaluationsCount; i++) {
				this.evaluations[i] = conditionWithEvaluations.evaluations[i];
			}
			this.validEvaluationsCount = conditionWithEvaluations.validEvaluationsCount;
			//this.ruleConditions = conditionWithEvaluations.ruleConditions; //not necessary as both objects are defined for the same rule conditions
		}
		
		double getEvaluation(int evaluationIndex) {
			if (evaluationIndex < validEvaluationsCount) {
				return evaluations[evaluationIndex];
			}
			if (evaluationIndex == validEvaluationsCount) {
				evaluations[evaluationIndex] = conditionAdditionEvaluators[evaluationIndex].evaluateWithCondition(ruleConditions, condition);
				validEvaluationsCount++;
				return evaluations[evaluationIndex];
			} else { //not subsequent evaluation is retrieved
				throw new InvalidValueException("Not subsequent evaluation of condition by condition addition evaluator is being retrieved.");
			}
		}
	}
	
	/**
	 * Tells if among considered monotonic condition addition evaluators there is at least one evaluator with monotonicity type different than the other evaluators.
	 */
	boolean containsEvaluatorsOfDifferentMonotonicityType;
	
	/**
	 * Count of the condition addition evaluators having the same monotonicity type {@link MonotonicConditionAdditionEvaluator.MonotonicityType} as the
	 * first condition addition evaluator (indexed by 0) in the array of all these evaluators.
	 */
	int firstMonotonicityTypeEvaluatorsCount;
	
	/**
	 * Constructor for this condition generator. Stores given monotonic condition addition evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionAdditionEvaluators array with monotonic condition addition evaluators used lexicographically
	 * 
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 * @throws NullPointerException if type of any condition addition evaluator is {@code null}
	 * @throws NullPointerException if monotonicity type of any condition addition evaluator is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 * @throws InvalidValueException if more than one switch of monotonicity type occurred when iterating from the first to the last of given monotonic condition addition evaluators
	 */
	public M4OptimizedConditionGenerator(MonotonicConditionAdditionEvaluator[] conditionAdditionEvaluators) {
		super(conditionAdditionEvaluators);
		for (MonotonicConditionAdditionEvaluator conditionEvaluator : conditionAdditionEvaluators) {
			Precondition.notNull(conditionEvaluator.getMonotonictyType(), "Monotonicty type of a monotonic condition addition evaluator is null.");
		}
		
		this.containsEvaluatorsOfDifferentMonotonicityType = false;
		this.firstMonotonicityTypeEvaluatorsCount = -1;
		int monotonicityTypeSwitchCount = 0; //tells how many times monotonicity type is switched when iterating from the first to the last evaluator
		
		for (int i = 1; i < conditionAdditionEvaluators.length; i++) {
			if (conditionAdditionEvaluators[i].getMonotonictyType() != conditionAdditionEvaluators[i-1].getMonotonictyType()) {
				this.containsEvaluatorsOfDifferentMonotonicityType = true;
				this.firstMonotonicityTypeEvaluatorsCount = i;
				monotonicityTypeSwitchCount++;
				//break;
			}
		}
		
		if (monotonicityTypeSwitchCount > 1) {
			throw new InvalidValueException("More than one switch of monotonicity type occurred when iterating from the first to the last monotonic condition addition evaluator.");
		}
		
		if (this.firstMonotonicityTypeEvaluatorsCount == -1) {
			this.firstMonotonicityTypeEvaluatorsCount = conditionAdditionEvaluators.length; //all evaluators are of the same monotonicity type
		}
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
	 * @throws AttributeNotFoundException if the learning information table referenced from given rule conditions does not contain
	 *         any active condition evaluation attribute, for which a condition could be constructed
	 */
	@Override
	public Condition<EvaluationField> getBestCondition(IntList consideredObjects, RuleConditions ruleConditions) {
		Precondition.notNull(consideredObjects, "List of objects considered in m4-optimized condition generator is null.");
		Precondition.notNull(ruleConditions, "Rule conditions considered in m4-optimized condition generator are null.");
		
		ConditionWithEvaluations bestConditionWithEvaluations = new ConditionWithEvaluations(ruleConditions);
		ConditionWithEvaluations candidateConditionWithEvaluations = new ConditionWithEvaluations(ruleConditions);
		
		InformationTable learningInformationTable = ruleConditions.getLearningInformationTable();
		Table<EvaluationAttribute, EvaluationField> activeConditionAttributeFields = learningInformationTable.getActiveConditionAttributeFields();
		
		//
		if (activeConditionAttributeFields == null) {
			throw new AttributeNotFoundException("Learning data does not contain any active condition evaluation attribute.");
		}
		//
		
		EvaluationAttribute[] activeConditionAttributes = activeConditionAttributeFields.getAttributes(true);
		
		int globalAttributeIndex;
		
		//go through active condition attributes
		for (int localActiveConditionAttributeIndex = 0; localActiveConditionAttributeIndex < activeConditionAttributes.length; localActiveConditionAttributeIndex++) {
			globalAttributeIndex = learningInformationTable.translateLocalActiveConditionAttributeIndex2GlobalAttributeIndex(localActiveConditionAttributeIndex);
			//current attribute should be considered
			if (!ruleConditions.containsConditionForAttribute(globalAttributeIndex)) { //this should be safe for all attributes that do not have weak order in their value set
				//optimization is possible for current attribute - it is a criterion whose evaluations can be linearly ordered
				if (activeConditionAttributes[localActiveConditionAttributeIndex].getPreferenceType() != AttributePreferenceType.NONE && activeConditionAttributes[localActiveConditionAttributeIndex].getValueType() instanceof SimpleField) { //or KnownSimpleField
					searchForBestConditionForOptimizableAttribute(consideredObjects, ruleConditions, localActiveConditionAttributeIndex, globalAttributeIndex, bestConditionWithEvaluations, candidateConditionWithEvaluations); //check criterion, possibly updating bestCondition
				} else { //proceed without optimization
					searchForBestConditionForNonOptimizableAttribute(consideredObjects, ruleConditions, localActiveConditionAttributeIndex, globalAttributeIndex, bestConditionWithEvaluations, candidateConditionWithEvaluations); //check criterion, possibly updating bestCondition
				}
			} //if
		} //for
		
		if (bestConditionWithEvaluations.condition == null) {
			throw new ElementaryConditionNotFoundException("Could not find any new elementary conditon to be added to constructed rule conditions: "+ruleConditions);
		} else {
			return bestConditionWithEvaluations.condition;
		}
	}
	
	/**
	 * Calculates multiplier used to compare two evaluations on the same condition attribute;
	 * it takes into account both attribute's preference type and rule's semantics.
	 * 
	 * @param attribute considered condition attribute
	 * @param ruleConditions rule conditions for which next best conditions is search for
	 * @return multiplier used to compare two evaluations on the same condition attribute
	 */
	int calculateCompareToMultiplier(EvaluationAttribute attribute, RuleConditions ruleConditions) {
		return (attribute.getPreferenceType() == AttributePreferenceType.GAIN ? 1 : -1) * (ruleConditions.getRuleSemantics() == RuleSemantics.AT_LEAST ? 1 : -1);
	}
	
	//informs if missing value should be considered for current attribute (i.e., if condition thresholding that value can restrict set of covered objects)
	boolean shouldSkipMissingValue(EvaluationAttribute evaluationAttribute, RuleType ruleType) {
		switch (ruleType) {
		case CERTAIN:
			return evaluationAttribute.getMissingValueType().equalWhenComparedToAnyEvaluation();
		case POSSIBLE:
			return evaluationAttribute.getMissingValueType().equalWhenReverseComparedToAnyEvaluation();
		default:
			throw new InvalidValueException("Cannot assess if missing value should be skipped if rule type is neither certain nor possible.");
		}
	}
	
	//can update bestConditionWithEvaluations
	void searchForBestConditionForOptimizableAttribute(IntList consideredObjects, RuleConditions ruleConditions, int localActiveConditionAttributeIndex, int globalAttributeIndex,
			ConditionWithEvaluations bestConditionWithEvaluations, ConditionWithEvaluations candidateConditionWithEvaluations) {
		MonotonicityType firstEvaluatorMonotonicityType = ((MonotonicConditionAdditionEvaluator[])this.conditionAdditionEvaluators)[0].getMonotonictyType(); //casting should work, as constructor parameter is of type MonotonicConditionAdditionEvaluator[])
		
		Table<EvaluationAttribute, EvaluationField> data = ruleConditions.getLearningInformationTable().getActiveConditionAttributeFields();
		EvaluationAttribute activeConditionAttribute = data.getAttributes(true)[localActiveConditionAttributeIndex];
		
		EvaluationField objectEvaluation;
		KnownSimpleField extremeLimitingEvaluation = null; //least/most (depending on the type of the first condition addition evaluator) restrictive limiting evaluation found so far
		KnownSimpleField candidateLimitingEvaluation; //current limiting evaluation to be compared with the extreme one
		ConditionLimitingEvaluationInterval conditionLimitingEvaluationInterval;
		Condition<EvaluationField> candidateCondition = null;

		int consideredObjectsCount = consideredObjects.size();
		int consideredObjectsSuccessfullIndex = -1; //index of consideredObjects corresponding to the first non-missing evaluation
		int compareToMultiplier;
		
		ConditionComparisonResult candidateVSBestConditionComparisonResult;
		boolean checkLessExtremeEvaluations;
		
		boolean missingValueConditionTested = shouldSkipMissingValue(activeConditionAttribute, ruleConditions.getRuleType());
		
		//establish if there is at least one non-missing evaluation for considered criterion, among considered objects
		for (int i = 0; i < consideredObjectsCount; i++) {
			objectEvaluation = data.getField(consideredObjects.getInt(i), localActiveConditionAttributeIndex);
			if (objectEvaluation instanceof KnownSimpleField) { //non-missing evaluation found
				extremeLimitingEvaluation = (KnownSimpleField)objectEvaluation;
				consideredObjectsSuccessfullIndex = i; //remember last considered index, so next search can start from the following index
				break;
			} else {
				if (!missingValueConditionTested) {
					testBestCondition(ruleConditions, activeConditionAttribute, objectEvaluation, globalAttributeIndex, bestConditionWithEvaluations, candidateConditionWithEvaluations); //update best condition, if necessary
					missingValueConditionTested = true;
				}
			}
		}
		
		if (consideredObjectsSuccessfullIndex >= 0) {
			conditionLimitingEvaluationInterval = (firstEvaluatorMonotonicityType == MonotonicityType.IMPROVES_WITH_NUMBER_OF_COVERED_OBJECTS ?
					new GeneralizingConditionLimitingEvaluationInterval() :
					new RestrictingConditionLimitingEvaluationInterval());
			
			//first, calculate multiplier used to compare two evaluations on the considered attribute; it takes into account both rule's semantics and attribute's preference type
			compareToMultiplier = calculateCompareToMultiplier(activeConditionAttribute, ruleConditions); 
			
			//second, iterate through all considered objects to calculate least/most restrictive limiting evaluation of a condition
			//taking into account rule's semantics and attribute's preference type
			for (int i = consideredObjectsSuccessfullIndex + 1; i < consideredObjectsCount; i++) { //continue loop at next index
				objectEvaluation = data.getField(consideredObjects.getInt(i), localActiveConditionAttributeIndex);
				if (objectEvaluation instanceof KnownSimpleField) { //non-missing evaluation found
					candidateLimitingEvaluation = (KnownSimpleField)objectEvaluation;
					
					if (conditionLimitingEvaluationInterval.isMoreExtremeEvaluation(candidateLimitingEvaluation, extremeLimitingEvaluation, compareToMultiplier)) {
						extremeLimitingEvaluation = candidateLimitingEvaluation;
					}
				} else {
					if (!missingValueConditionTested) {
						testBestCondition(ruleConditions, activeConditionAttribute, objectEvaluation, globalAttributeIndex, bestConditionWithEvaluations, candidateConditionWithEvaluations); //update best condition, if necessary
						missingValueConditionTested = true;
					}
				}
			}
			
			//initialize limits
			conditionLimitingEvaluationInterval.initialize(extremeLimitingEvaluation, null);
			
			//at this point, least/most restrictive limiting evaluation among considered objects, for considered criterion, has been calculated, so one can construct candidate condition employing that limiting evaluation
			candidateCondition = ConditionGenerator.constructCondition(ruleConditions.getRuleType(), ruleConditions.getRuleSemantics(), activeConditionAttribute, extremeLimitingEvaluation, globalAttributeIndex);
			candidateConditionWithEvaluations.setCondition(candidateCondition); //set extreme condition
			candidateVSBestConditionComparisonResult = compareCandidateAndBestCondition(candidateConditionWithEvaluations, bestConditionWithEvaluations, this.firstMonotonicityTypeEvaluatorsCount); //compare candidate and best condition w.r.t. the first evaluator(s) only, having the same monotonicity type

			checkLessExtremeEvaluations = (containsEvaluatorsOfDifferentMonotonicityType ? true : false);
			
			//check comparison result and update best condition if needed
			switch (candidateVSBestConditionComparisonResult) {
			case CANDIDATE_CONDITION_IS_BETTER: //candidate condition is better already w.r.t. the first evaluator(s)
				bestConditionWithEvaluations.copy(candidateConditionWithEvaluations); //update best condition
				break;
			case CANDIDATE_CONDITION_IS_EQUAL:
				candidateVSBestConditionComparisonResult = compareCandidateAndBestCondition(candidateConditionWithEvaluations, bestConditionWithEvaluations, conditionAdditionEvaluators.length); //compare conditions w.r.t. all evaluators (using already stored evaluations for the first evaluator(s))
				if (candidateVSBestConditionComparisonResult == ConditionComparisonResult.CANDIDATE_CONDITION_IS_BETTER) {
					bestConditionWithEvaluations.copy(candidateConditionWithEvaluations); //update best condition
				}
				break;
			case CANDIDATE_CONDITION_IS_WORSE_WRT_FIRST_EVALUATORS: //candidate condition is worse already w.r.t. the first evaluator(s)
				checkLessExtremeEvaluations = false; //go to next active condition attribute as for the current attribute best condition cannot be improved
				break;
			case CANDIDATE_CONDITION_IS_WORSE_WRT_SECOND_EVALUATORS:
				throw new InvalidValueException("Unexpected result of comparison of candidate and best condition w.r.t. the first monotonicity type evaluators."); //this should not happen
			}
			
			if (checkLessExtremeEvaluations) {
				for (int consideredObjectIndex : consideredObjects) {
					objectEvaluation = data.getField(consideredObjectIndex, localActiveConditionAttributeIndex);
					
					if (objectEvaluation instanceof KnownSimpleField) { //non-missing evaluation found
						candidateLimitingEvaluation = (KnownSimpleField)objectEvaluation;
						
						//check if current evaluation is strictly inside current range of interest
						if (conditionLimitingEvaluationInterval.includes(candidateLimitingEvaluation, compareToMultiplier)) {
							conditionLimitingEvaluationInterval.update(
									testBestCondition(ruleConditions, activeConditionAttribute, candidateLimitingEvaluation, globalAttributeIndex, bestConditionWithEvaluations, candidateConditionWithEvaluations), //update best condition, if necessary
									candidateLimitingEvaluation); //update interval
						}
					} //if
					//at this point it is not necessary to handle missing value, as it had to be already handled above
				} //for
			} //if
		} //if
	}
	
	//can update bestConditionWithEvaluations
	void searchForBestConditionForNonOptimizableAttribute(IntList consideredObjects, RuleConditions ruleConditions, int localActiveConditionAttributeIndex, int globalAttributeIndex,
			ConditionWithEvaluations bestConditionWithEvaluations, ConditionWithEvaluations candidateConditionWithEvaluations) {
		Table<EvaluationAttribute, EvaluationField> data = ruleConditions.getLearningInformationTable().getActiveConditionAttributeFields();
		EvaluationAttribute activeConditionAttribute = data.getAttributes(true)[localActiveConditionAttributeIndex];
		EvaluationField objectEvaluation;
		ObjectSet<EvaluationField> alreadyTestedObjectEvaluations = new ObjectOpenHashSet<>();
		
		boolean missingValueConditionTested = shouldSkipMissingValue(activeConditionAttribute, ruleConditions.getRuleType());
		
		for (int consideredObjectIndex : consideredObjects) {
			objectEvaluation = data.getField(consideredObjectIndex, localActiveConditionAttributeIndex);
			
			if ( objectEvaluation instanceof KnownSimpleField || ((objectEvaluation instanceof CompositeField) && !((CompositeField)objectEvaluation).isUnknown()) ) { //non-missing evaluation found
				//this handles PairField evaluations with both evaluations known or partially known pairs of evaluations (with only one evaluation known)
				if (!alreadyTestedObjectEvaluations.contains(objectEvaluation)) { //new (i.e., not seen for any previously considered object) evaluation found
					testBestCondition(ruleConditions, activeConditionAttribute, objectEvaluation, globalAttributeIndex, bestConditionWithEvaluations, candidateConditionWithEvaluations);
					alreadyTestedObjectEvaluations.add(objectEvaluation); //remember already tested evaluation on considered attribute, not to test it again if occurs for another considered object
				}
			} else {
				if (!missingValueConditionTested) {
					testBestCondition(ruleConditions, activeConditionAttribute, objectEvaluation, globalAttributeIndex, bestConditionWithEvaluations, candidateConditionWithEvaluations); //update best condition, if necessary
					missingValueConditionTested = true;
				}
			}
		}
	}
	
	//constructs candidate conditions for given evaluation, compares is with currently best condition (w.r.t. all evaluators), and replaces best condition if candidate condition prevails;
	//returns result of comparison of candidate vs best condition (for post mortem handling)
	ConditionComparisonResult testBestCondition(RuleConditions ruleConditions, EvaluationAttribute activeConditionAttribute, EvaluationField objectEvaluation, int globalAttributeIndex,
			ConditionWithEvaluations bestConditionWithEvaluations, ConditionWithEvaluations candidateConditionWithEvaluations) {
		Condition<EvaluationField> candidateCondition = ConditionGenerator.constructCondition(ruleConditions.getRuleType(), ruleConditions.getRuleSemantics(), activeConditionAttribute, objectEvaluation, globalAttributeIndex);
		candidateConditionWithEvaluations.setCondition(candidateCondition); //reset candidate condition with evaluations
		ConditionComparisonResult candidateVSBestConditionComparisonResult = compareCandidateAndBestCondition(candidateConditionWithEvaluations, bestConditionWithEvaluations, conditionAdditionEvaluators.length); //compare conditions w.r.t. all evaluators
		
		if (candidateVSBestConditionComparisonResult == ConditionComparisonResult.CANDIDATE_CONDITION_IS_BETTER) {
			bestConditionWithEvaluations.copy(candidateConditionWithEvaluations); //update best condition
		}
		
		return candidateVSBestConditionComparisonResult;
	}
	
	/**
	 * Compares candidate versus best condition found so far to establish which one of the two is better.
	 * Lexicographically employs considered condition addition evaluators.
	 * 
	 * @param candidateConditionWithEvaluations candidate condition (with its already calculated evaluations), to be compared with currently best condition
	 * @param bestConditionWithEvaluations currently best condition (with its already calculated evaluations)
	 * @param usedEvaluatorsCount number of consecutive condition addition evaluators used in the comparison; has to be {@code >= 0} and {@code <= conditionAdditionEvaluators.length}
	 * 
	 * @return {@link ConditionComparisonResult#CANDIDATE_CONDITION_IS_BETTER} if given candidate condition is better than given best condition,
	 *         {@link ConditionComparisonResult#CANDIDATE_CONDITION_IS_WORSE_WRT_FIRST_EVALUATORS} if given candidate condition is lexicographically worse than given best condition
	 *         already w.r.t. the first sublist of evaluators,
	 *         {@link ConditionComparisonResult#CANDIDATE_CONDITION_IS_WORSE_WRT_SECOND_EVALUATORS} if given candidate condition is lexicographically worse than given best condition
	 *         only w.r.t. the second sublist of evaluators,
	 *         {@link ConditionComparisonResult#CANDIDATE_CONDITION_IS_EQUAL} otherwise
	 */
	ConditionComparisonResult compareCandidateAndBestCondition(ConditionWithEvaluations candidateConditionWithEvaluations, ConditionWithEvaluations bestConditionWithEvaluations, int usedEvaluatorsCount) {
		double candidateConditionEvaluation;
		double bestConditionEvaluation;
		
		for (int i = 0; i < usedEvaluatorsCount; i++) {
			candidateConditionEvaluation = candidateConditionWithEvaluations.getEvaluation(i);
			bestConditionEvaluation = bestConditionWithEvaluations.getEvaluation(i);
			
			if (candidateConditionEvaluation > bestConditionEvaluation) {
				if (conditionAdditionEvaluators[i].getType() == MeasureType.GAIN) { //candidate condition is better at i-th evaluation
					return ConditionComparisonResult.CANDIDATE_CONDITION_IS_BETTER;
				} else { //COST
					return (i < this.firstMonotonicityTypeEvaluatorsCount ?
								ConditionComparisonResult.CANDIDATE_CONDITION_IS_WORSE_WRT_FIRST_EVALUATORS :
								ConditionComparisonResult.CANDIDATE_CONDITION_IS_WORSE_WRT_SECOND_EVALUATORS);
				}
			}
			
			if (candidateConditionEvaluation < bestConditionEvaluation) {
				if (conditionAdditionEvaluators[i].getType() == MeasureType.GAIN) { //candidate condition is worse at i-th evaluation
					return (i < this.firstMonotonicityTypeEvaluatorsCount ?
								ConditionComparisonResult.CANDIDATE_CONDITION_IS_WORSE_WRT_FIRST_EVALUATORS :
								ConditionComparisonResult.CANDIDATE_CONDITION_IS_WORSE_WRT_SECOND_EVALUATORS);
				} else { //COST
					return ConditionComparisonResult.CANDIDATE_CONDITION_IS_BETTER;
				}
			}
		} //for
		
		return ConditionComparisonResult.CANDIDATE_CONDITION_IS_EQUAL; //neither condition is better
	}
	
	//TODO: overwrite getter for conditionAdditionEvaluators to return monotonic ones

}
