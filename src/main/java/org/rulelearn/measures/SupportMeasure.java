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

import static org.rulelearn.core.OperationsOnCollections.getNumberOfElementsFromListInSet;
import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionRemovalEvaluator;
import org.rulelearn.rules.ConditionReplacementEvaluator;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleConditionsEvaluator;
import org.rulelearn.rules.RuleCoverageInformation;
import org.rulelearn.rules.RuleEvaluator;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Rule support measure reflecting the amount of objects which support a decision rule (i.e., objects which match elementary conditions on the LHS, and decisions on the RHS).  
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SupportMeasure implements GainTypeMeasure, MonotonicConditionAdditionEvaluator, ConditionRemovalEvaluator, ConditionReplacementEvaluator, RuleConditionsEvaluator, RuleEvaluator {
	
	/**
	 * The only instance of this measure (singleton).
	 */
	private static final SupportMeasure INSTANCE = new SupportMeasure();
	
	/**
	 * Sole constructor. Ensures adherence to singleton design pattern.
	 */
	private SupportMeasure () {
	}
	
	/**
	 * Returns reference to singleton instance of support measure.
	 * 
	 * @return reference to singleton instance of support measure
	 */
	public static SupportMeasure getInstance() { 
		return INSTANCE; 
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleCoverageInformation {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public double evaluate(RuleCoverageInformation ruleCoverageInformation) {
		return getNumberOfElementsFromListInSet(ruleCoverageInformation.getIndicesOfCoveredObjects(), ruleCoverageInformation.getIndicesOfPositiveObjects());
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
			IntSet positiveObjects = ruleConditions.getIndicesOfPositiveObjects();
			
			return getNumberOfElementsFromListInSet(coveredObjects, positiveObjects);
		}
		else {
			return Double.MIN_VALUE; 
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
		
		IntList coveredObjects = ruleConditions.getIndicesOfCoveredObjects();
		IntSet positiveObjects = ruleConditions.getIndicesOfPositiveObjects();
		
		return getNumberOfElementsFromListInSet(coveredObjects, positiveObjects);
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
		IntSet positiveObjects = ruleConditions.getIndicesOfPositiveObjects();
		
		return getNumberOfElementsFromListInSet(coveredObjects, positiveObjects);
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
		
		IntList coveredObjects = ruleConditions.getIndicesOfCoveredObjectsWhenReplacingCondition(conditionIndex, newCondition);
		IntSet positiveObjects = ruleConditions.getIndicesOfPositiveObjects();
		
		return getNumberOfElementsFromListInSet(coveredObjects, positiveObjects);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public MonotonicityType getMonotonictyType() {
		return MonotonicityType.IMPROVES_WITH_NUMBER_OF_COVERED_OBJECTS;
	}
	
}