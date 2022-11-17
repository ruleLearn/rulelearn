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

package org.rulelearn.measures;

import static org.rulelearn.core.OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets;
import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionRemovalEvaluator;
import org.rulelearn.rules.ConditionReplacementEvaluator;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleConditionsEvaluator;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Coverage outside approximated set measure, calculated to check the number of objects that neither belong to the considered approximated set nor are neutral with respect to that set
 * but are covered by rule conditions. This measure relate to measure {@link EpsilonConsistencyMeasure}, but does not divide by the size of the complement of considered approximated set.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CoverageOutsideApproximatedSetMeasure implements CostTypeMeasure, MonotonicConditionAdditionEvaluator, RuleConditionsEvaluator, ConditionRemovalEvaluator, ConditionReplacementEvaluator {
	
	/**
	 * The only instance of this measure (singleton).
	 */
	private static final CoverageOutsideApproximatedSetMeasure INSTANCE = new CoverageOutsideApproximatedSetMeasure();
	
	/**
	 * Sole constructor. Ensures adherence to singleton design pattern.
	 */
	private CoverageOutsideApproximatedSetMeasure () {
	}
	
	/**
	 * Returns reference to singleton instance of coverage outside approximated set measure.
	 * 
	 * @return reference to singleton instance of coverage outside approximated set measure
	 */
	public static CoverageOutsideApproximatedSetMeasure getInstance() {
		return INSTANCE; 
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
				ruleConditions.getIndicesOfPositiveObjects(), ruleConditions.getIndicesOfNeutralObjects());
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
					ruleConditions.getIndicesOfPositiveObjects(), ruleConditions.getIndicesOfNeutralObjects());
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
				ruleConditions.getIndicesOfPositiveObjects(), ruleConditions.getIndicesOfNeutralObjects());
	}
	
	/** 
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param conditionIndex {@inheritDoc}
	 * @param newCondition {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public double evaluateWhenReplacingCondition(RuleConditions ruleConditions, int conditionIndex, Condition<? extends EvaluationField> newCondition) {
		notNull(ruleConditions, "Rule conditions for which evaluation is made are null.");
		notNull(newCondition, "Replacing condition is null.");
		
		return calculateConsistency(ruleConditions.getIndicesOfCoveredObjectsWhenReplacingCondition(conditionIndex, newCondition),
				ruleConditions.getIndicesOfPositiveObjects(), ruleConditions.getIndicesOfNeutralObjects());
	}
	
	/**
	 * Calculates number of objects covered by rule conditions that are neither positive nor neutral (i.e., are negative).
	 * 
	 * @param coveredObjects list of objects covered by rule conditions
	 * @param positiveObjects set of positive objects
	 * @param neutralObjects set of neutral objects
	 * 
	 * @return number of objects covered by rule conditions that are neither positive nor neutral (i.e., are negative)
	 */
	int calculateConsistency(IntList coveredObjects, IntSet positiveObjects, IntSet neutralObjects) {
		return getNumberOfElementsFromListNotPresentInSets(coveredObjects, positiveObjects, neutralObjects);
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
