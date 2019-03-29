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

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Checker verifying if evaluation of rule conditions satisfy given threshold and rule conditions cover only allowed objects.
 * This checker, for evaluator being rule consistency measure, is described in:<br>
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches. Information Sciences, 181, 2011, pp. 987-1002
 * (Algorithm 2, line 6).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EvaluationAndCoverageStoppingConditionChecker implements RuleInductionStoppingConditionCheckerWithThreshold {
	
	/**
	 * Evaluator applied to rule conditions when verifying if they meet stopping conditions.
	 */
	RuleConditionsEvaluator ruleConditionsEvaluator;
	
	/**
	 * Evaluation threshold used when verifying if rule conditions meet stopping conditions.
	 */
	double evaluationThreshold;

	/**
	 * Constructs this stopping condition checker.
	 * 
	 * @param ruleConditionsEvaluator evaluator applied to rule conditions when verifying if they meet stopping conditions
	 * @param evaluationThreshold threshold to be compared with the evaluation of rule conditions calculated by the given evaluator
	 * 
	 * @throws NullPointerException if rule conditions evaluator is {@code null}
	 */
	public EvaluationAndCoverageStoppingConditionChecker(RuleConditionsEvaluator ruleConditionsEvaluator, double evaluationThreshold) {
		this.ruleConditionsEvaluator = Precondition.notNull(ruleConditionsEvaluator, "Rule conditions evaluator is null.");
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
		
		if (!ruleConditionsEvaluator.evaluationSatisfiesThresholdWithoutCondition(ruleConditions, evaluationThreshold, conditionIndex)) {
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
	
	/* (non-Javadoc)
	 * @see org.rulelearn.rules.RuleInductionStoppinConditionCheckerWithThreshold#getThreshold()
	 */
	@Override
	public double getThreshold() {
		return this.evaluationThreshold;
	}

	/* (non-Javadoc)
	 * @see org.rulelearn.rules.RuleInductionStoppinConditionCheckerWithThreshold#copyWithNewThreshold(double)
	 */
	@Override
	public RuleInductionStoppingConditionCheckerWithThreshold copyWithNewThreshold(double evaluationThreshold) {
		return new EvaluationAndCoverageStoppingConditionChecker(this.ruleConditionsEvaluator, evaluationThreshold);
	}
	
}
