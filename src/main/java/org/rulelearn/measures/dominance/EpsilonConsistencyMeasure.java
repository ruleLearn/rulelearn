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

package org.rulelearn.measures.dominance;

import static org.rulelearn.core.OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets;
import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.data.Decision;
import org.rulelearn.dominance.DominanceConesDecisionDistributions;
import org.rulelearn.measures.ConsistencyMeasure;
import org.rulelearn.measures.CostTypeMeasure;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionRemovalEvaluator;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleConditionsEvaluator;
import org.rulelearn.rules.RuleCoverageInformation;
import org.rulelearn.rules.RuleEvaluator;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Epsilon consistency measure defined with respect to union of decision classes in Błaszczyński, J., Greco, S., Słowiński, R., Szeląg, M.: 
 * Monotonic variable consistency rough set approaches. International Journal of Approximate Reasoning 50(7), 979--999 (2009).
 * 
 * The exact definition implemented here is more general and allows proper handling of missing values. It involves standard and inverted
 * dominance relations as defined in M. Szeląg, J. Błaszczyński, R. Słowiński, Rough Set Analysis of Classification Data with Missing Values. [In]:
 * L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017, Proceedings, Part I.
 * Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.   
 *	
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EpsilonConsistencyMeasure implements CostTypeMeasure, ConsistencyMeasure<Union>, MonotonicConditionAdditionEvaluator, RuleConditionsEvaluator, ConditionRemovalEvaluator, RuleEvaluator {

	/**
	 * Best possible value of this measure.
	 */
	protected final static double BEST_VALUE = 0.0;
	/**
	 * Worst possible value of this measure.
	 */
	protected final static double WORST_VALUE = 1.0;
	
	/**
	 * The only instance of this measure (singleton).
	 */
	private static final EpsilonConsistencyMeasure INSTANCE = new EpsilonConsistencyMeasure();
	
	/**
	 * Sole constructor. Ensures adherence to singleton design pattern.
	 */
	private EpsilonConsistencyMeasure () {
	}
	
	/**
	 * Returns reference to singleton instance of epsilon consistency measure.
	 * 
	 * @return reference to singleton instance of epsilon consistency measure
	 */
	public static EpsilonConsistencyMeasure getInstance() { 
		return INSTANCE; 
	}
	
	/**
	 * Calculates value of epsilon consistency of the given object with respect to the given union of decision classes.
	 * 
	 * @param objectIndex index of an object in the information table for which the given union is defined
	 * @param union approximated union
	 * @return value of epsilon consistency measure for the given object with respect to the given union
	 */
	@Override
	public double calculateConsistency(int objectIndex, Union union) {
		DominanceConesDecisionDistributions dominanceCDD = union.getInformationTable().getDominanceConesDecisionDistributions();
		int negativeCount = 0;
		
		if (union.getUnionType() == UnionType.AT_LEAST) {
			for (Decision decision : dominanceCDD.getPositiveInvDConeDecisionClassDistribution(objectIndex).getDecisions()) {
				// check how many objects in a positive inverted dominance cone based on the object are not concordant with the union (i.e., not in the union and not uncomparable) 
				if (union.isDecisionNegative(decision)) {
					negativeCount += dominanceCDD.getPositiveInvDConeDecisionClassDistribution(objectIndex).getCount(decision);
				}
			}
		
		}
		else if (union.getUnionType() == UnionType.AT_MOST) {
			for (Decision decision : dominanceCDD.getNegativeDConeDecisionClassDistribution(objectIndex).getDecisions()) {
				// check how many objects in a negative dominance cone based on the object are not concordant with the union (i.e., not in the union and not uncomparable) 
				if (union.isDecisionNegative(decision)) {
					negativeCount += dominanceCDD.getNegativeDConeDecisionClassDistribution(objectIndex).getCount(decision);
				}
			}
		}
		
		if (negativeCount == 0) { //no negative object is covered
			return 0;
		} else {
			int complementarySetSize = union.getComplementarySetSize();
			
			if (complementarySetSize == 0) { //prevent division by zero
				return 0;
			} else {
				return ((double)negativeCount) / ((double)complementarySetSize);
			}
		}
	}
	
	/** 
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc} 
	 */
	@Override
	public double evaluate(RuleConditions ruleConditions) {
		notNull(ruleConditions, "Rule conditions for which evaluation is made are null.");
		
		return calculateConsistency(ruleConditions.getIndicesOfCoveredObjects(),
				ruleConditions.getIndicesOfPositiveObjects(), ruleConditions.getIndicesOfNeutralObjects(),
				ruleConditions.getLearningInformationTable().getNumberOfObjects());
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param condition {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public double evaluateWithCondition(RuleConditions ruleConditions, Condition<EvaluationField> condition) {
		notNull(ruleConditions, "Rule conditions for which evaluation is made are null.");
		
		if (condition != null) {
			return calculateConsistency(ruleConditions.getIndicesOfCoveredObjectsWithCondition(condition),
					ruleConditions.getIndicesOfPositiveObjects(), ruleConditions.getIndicesOfNeutralObjects(),
					ruleConditions.getLearningInformationTable().getNumberOfObjects());
		}
		else {
			return Double.MAX_VALUE;
		}
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param conditionIndex {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public double evaluateWithoutCondition(RuleConditions ruleConditions, int conditionIndex) {
		notNull(ruleConditions, "Rule conditions for which evaluation is made are null.");
		
		return calculateConsistency(ruleConditions.getIndicesOfCoveredObjectsWithoutCondition(conditionIndex),
				ruleConditions.getIndicesOfPositiveObjects(), ruleConditions.getIndicesOfNeutralObjects(),
				ruleConditions.getLearningInformationTable().getNumberOfObjects());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleCoverageInfo {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public double evaluate(RuleCoverageInformation ruleCoverageInfo) {
		notNull(ruleCoverageInfo, "Rule coverage information for which evaluation is made is null.");
		
		return calculateConsistency(ruleCoverageInfo.getIndicesOfCoveredObjects(),
				ruleCoverageInfo.getIndicesOfPositiveObjects(), ruleCoverageInfo.getIndicesOfNeutralObjects(),
				ruleCoverageInfo.getAllObjectsCount());
	}
	
	/**
	 * Calculates value of epsilon measure, avoiding unnecessary calculations and division by zero.
	 * 
	 * @param coveredObjects list of objects covered by rule conditions
	 * @param positiveObjects set of positive objects
	 * @param neutralObjects set of neutral objects
	 * @param allObjectsCount number of all objects
	 * 
	 * @return value of epsilon measure
	 */
	double calculateConsistency(IntList coveredObjects, IntSet positiveObjects, IntSet neutralObjects, int allObjectsCount) {
		int negativeCoverage = getNumberOfElementsFromListNotPresentInSets(coveredObjects, positiveObjects, neutralObjects);
		
		if (negativeCoverage == 0) { //no negative object is covered
			return 0.0;
		} else {
			int negativeObjectsCount = allObjectsCount - positiveObjects.size() - neutralObjects.size();
			
			if (negativeObjectsCount == 0) { //prevent division by zero
				return 0.0;
			} else {
				return ((double)negativeCoverage) / ((double)negativeObjectsCount);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public MonotonicityType getMonotonictyType() {
		return MonotonicityType.DETERIORATES_WITH_NUMBER_OF_COVERED_OBJECTS;
	}

}
