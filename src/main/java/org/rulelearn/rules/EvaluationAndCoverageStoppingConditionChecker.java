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

import org.rulelearn.core.Precondition;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Checker verifying if evaluation of rule conditions satisfy given threshold and rule conditions cover only {@link RuleConditions#getIndicesOfObjectsThatCanBeCovered() allowed objects}.
 * This checker, for evaluator being rule consistency measure, is described in:<br>
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches. Information Sciences, 181, 2011, pp. 987-1002
 * (Algorithm 2, line 6).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EvaluationAndCoverageStoppingConditionChecker implements RuleInductionStoppingConditionChecker {
	
	/**
	 * Evaluator applied to {@link RuleConditions rule conditions} when verifying if they meet stopping conditions verified by this checker.
	 */
	RuleConditionsEvaluator ruleConditionsEvaluator;
	
	/**
	 * Evaluator applied to evaluate {@link RuleConditions rule conditions} after removing a single condition.
	 */
	ConditionRemovalEvaluator conditionRemovalEvaluator;
	
	/**
	 * Evaluator applied to evaluate {@link RuleConditions rule conditions} after replacing a single condition.
	 */
	ConditionReplacementEvaluator conditionReplacementEvaluator;
	
	/**
	 * Evaluation threshold used when verifying if {@link RuleConditions rule conditions} meet stopping conditions verified by this checker.
	 */
	double evaluationThreshold;

	/**
	 * Constructs this stopping condition checker.
	 * 
	 * @param ruleConditionsEvaluator evaluator applied to rule conditions when verifying if they meet stopping conditions
	 * @param conditionRemovalEvaluator evaluator applied to rule conditions when verifying if they would still meet stopping conditions after removal of some condition
	 * @param conditionReplacementEvaluator evaluator applied to rule conditions when verifying if they would still meet stopping conditions after replacement of some condition
	 * @param evaluationThreshold threshold to be compared with the evaluation of rule conditions calculated by the given evaluator
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public EvaluationAndCoverageStoppingConditionChecker(RuleConditionsEvaluator ruleConditionsEvaluator, ConditionRemovalEvaluator conditionRemovalEvaluator,
			ConditionReplacementEvaluator conditionReplacementEvaluator, double evaluationThreshold) {
		this.ruleConditionsEvaluator = Precondition.notNull(ruleConditionsEvaluator, "Rule conditions evaluator is null.");
		this.conditionRemovalEvaluator = Precondition.notNull(conditionRemovalEvaluator, "Condition removal evaluator is null.");
		this.conditionReplacementEvaluator = Precondition.notNull(conditionReplacementEvaluator, "Condition replacement evaluator is null.");
		this.evaluationThreshold = evaluationThreshold;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	@Override
	public boolean isStoppingConditionSatisified(RuleConditions ruleConditions) {
		Precondition.notNull(ruleConditions, "Rule conditions for stopping condition checker are null.");
		
		if (!ruleConditionsEvaluator.evaluationSatisfiesThreshold(ruleConditions, evaluationThreshold)) {
			return false;
		} else {
			IntSet indicesOfObjectsThatCanBeCovered = ruleConditions.getIndicesOfObjectsThatCanBeCovered();
			IntList indicesOfCoveredObjects = ruleConditions.getIndicesOfCoveredObjects();
			int coveredObjectIndex = 0;
			int coveredObjectsCount = indicesOfCoveredObjects.size();
			
			while (coveredObjectIndex < coveredObjectsCount) {
				if (!indicesOfObjectsThatCanBeCovered.contains(indicesOfCoveredObjects.getInt(coveredObjectIndex++))) {
					return false;
				}
			}
			
			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param conditionIndex {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning the number of conditions present in given rule conditions
	 */
	@Override
	public boolean isStoppingConditionSatisifiedWithoutCondition(RuleConditions ruleConditions, int conditionIndex) {
		Precondition.notNull(ruleConditions, "Rule conditions for stopping condition checker are null.");
		
		if (!conditionRemovalEvaluator.evaluationSatisfiesThresholdWithoutCondition(ruleConditions, evaluationThreshold, conditionIndex)) {
			return false;
		} else {
			IntSet indicesOfObjectsThatCanBeCovered = ruleConditions.getIndicesOfObjectsThatCanBeCovered();
			IntList indicesOfCoveredObjects = ruleConditions.getIndicesOfCoveredObjectsWithoutCondition(conditionIndex);
			int coveredObjectIndex = 0;
			int coveredObjectsCount = indicesOfCoveredObjects.size();
			
			while (coveredObjectIndex < coveredObjectsCount) {
				if (!indicesOfObjectsThatCanBeCovered.contains(indicesOfCoveredObjects.getInt(coveredObjectIndex++))) {
					return false;
				}
			}
			
			return true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param conditionIndex {@inheritDoc}
	 * @param newCondition {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}, or when given new condition is {@code null}
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning the number of conditions present in given rule conditions
	 */
	@Override
	public boolean isStoppingConditionSatisifiedWhenReplacingCondition(RuleConditions ruleConditions, int conditionIndex, Condition<? extends EvaluationField> newCondition) {
		Precondition.notNull(ruleConditions, "Rule conditions for stopping condition checker are null.");
		Precondition.notNull(newCondition, "New condition for stopping condition checker is null.");

		if (!conditionReplacementEvaluator.evaluationSatisfiesThresholdWhenReplacingCondition(ruleConditions, evaluationThreshold, conditionIndex, newCondition)) {
			return false;
		} else {
			IntSet indicesOfObjectsThatCanBeCovered = ruleConditions.getIndicesOfObjectsThatCanBeCovered();
			IntList indicesOfCoveredObjects = ruleConditions.getIndicesOfCoveredObjectsWhenReplacingCondition(conditionIndex, newCondition);
			int coveredObjectIndex = 0;
			int coveredObjectsCount = indicesOfCoveredObjects.size();
			
			while (coveredObjectIndex < coveredObjectsCount) {
				if (!indicesOfObjectsThatCanBeCovered.contains(indicesOfCoveredObjects.getInt(coveredObjectIndex++))) {
					return false;
				}
			}
			
			return true;
		}
	}

	/**
	 * Gets {@link RuleConditionsEvaluator rule conditions evaluator} used by this checker to evaluate {@link RuleConditions rule conditions}
	 * when verifying if they meet stopping conditions.
	 * 
	 * @return {@link RuleConditionsEvaluator rule conditions evaluator} used by this checker
	 */
	public RuleConditionsEvaluator getRuleConditionsEvaluator() {
		return ruleConditionsEvaluator;
	}
	
	/**
	 * Gets {@link ConditionRemovalEvaluator condition removal evaluator} used by this checker to evaluate {@link RuleConditions rule conditions}
	 * when verifying if they would still meet stopping conditions after removing a single condition.
	 * 
	 * @return {@link ConditionRemovalEvaluator condition removal evaluator} used by this checker
	 */
	public ConditionRemovalEvaluator getConditionRemovalEvaluator() {
		return conditionRemovalEvaluator;
	}
	
	/**
	 * Gets {@link ConditionReplacementEvaluator condition replacement evaluator} used by this checker to evaluate {@link RuleConditions rule conditions}
	 * when verifying if they would still meet stopping conditions after replacing a single condition.
	 * 
	 * @return {@link ConditionReplacementEvaluator condition replacement evaluator} used by this checker
	 */
	public ConditionReplacementEvaluator getConditionReplacementEvaluator() {
		return conditionReplacementEvaluator;
	}

	/**
	 * Evaluation threshold used when verifying if {@link RuleConditions rule conditions} meet stopping conditions.
	 * 
	 * @return Evaluation threshold used by this checker
	 */
	public double getEvaluationThreshold() {
		return evaluationThreshold;
	}

}
