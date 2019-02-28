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

import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionRemovalEvaluator;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleConditionsEvaluator;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Relative coverage outside approximation measure. It calculates the ratio of
 * the number of objects which are covered by rule conditions but neither belong to the considered approximation nor are neutral 
 * and the number of objects that neither belong to the considered approximation nor are neutral.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RelativeCoverageOutsideApproximationMeasure implements CostTypeMeasure, RuleConditionsEvaluator, MonotonicConditionAdditionEvaluator, ConditionRemovalEvaluator {
	/**
	 * The only instance of this measure (singleton).
	 */
	private static final RelativeCoverageOutsideApproximationMeasure INSTANCE = new RelativeCoverageOutsideApproximationMeasure();
	
	/**
	 * Sole constructor. Ensures adherence to singleton design pattern.
	 */
	private RelativeCoverageOutsideApproximationMeasure () {
	}
	
	/**
	 * Returns reference to singleton instance of relative coverage outside approximation measure.
	 * 
	 * @return reference to singleton instance of relative coverage outside approximation measure
	 */
	public static RelativeCoverageOutsideApproximationMeasure getInstance() { 
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
		IntList coveredObjects = ruleConditions.getIndicesOfCoveredObjects();
		IntSet approximationObjects = ruleConditions.getIndicesOfApproximationObjects();
		IntSet positiveObjects = ruleConditions.getIndicesOfPositiveObjects();
		IntSet neutralObjects = ruleConditions.getIndicesOfNeutralObjects();
		
		return (double)getNumberOfElementsFromListNotPresentInSets(coveredObjects, approximationObjects, neutralObjects) / 
				(ruleConditions.getLearningInformationTable().getNumberOfObjects() - positiveObjects.size() - neutralObjects.size());
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
			IntList coveredObjects = ruleConditions.getIndicesOfCoveredObjectsWithCondition(condition);
			IntSet approximationObjects = ruleConditions.getIndicesOfApproximationObjects();
			IntSet positiveObjects = ruleConditions.getIndicesOfPositiveObjects();
			IntSet neutralObjects = ruleConditions.getIndicesOfNeutralObjects();
			
			return (double)getNumberOfElementsFromListNotPresentInSets(coveredObjects, approximationObjects, neutralObjects) /
					(ruleConditions.getLearningInformationTable().getNumberOfObjects() - positiveObjects.size() - neutralObjects.size());
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
		IntList coveredObjects = ruleConditions.getIndicesOfCoveredObjectsWithoutCondition(conditionIndex);
		IntSet approximationObjects = ruleConditions.getIndicesOfApproximationObjects();
		IntSet positiveObjects = ruleConditions.getIndicesOfPositiveObjects();
		IntSet neutralObjects = ruleConditions.getIndicesOfNeutralObjects();
		
		return (double)getNumberOfElementsFromListNotPresentInSets(coveredObjects, approximationObjects, neutralObjects) / 
				(ruleConditions.getLearningInformationTable().getNumberOfObjects() - positiveObjects.size() - neutralObjects.size());
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
