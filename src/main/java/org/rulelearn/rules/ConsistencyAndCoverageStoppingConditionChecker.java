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

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Checker verifying if consistency measure threshold is reached and rule conditions cover only allowed objects.
 * This checker is described in:<br>
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches. Information Sciences, 181, 2011, pp. 987-1002
 * (Algorithm 2, line 6).<br>
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ConsistencyAndCoverageStoppingConditionChecker implements RuleInductionStoppingConditionChecker {
	
	RuleConditionsEvaluator ruleConditionsEvaluator;
	double consistencyThreshold;
	IntSet objectsThatCanBeCovered;

	/**
	 * Constructs this stopping condition checker.
	 * 
	 * @param ruleConditionsEvaluator evaluator used to evaluate given rule conditions
	 * @param consistencyThreshold consistency threshold to be compared with evaluation of given rule conditions by given evaluator
	 * @param objectsThatCanBeCovered set of indices of allowed objects, i.e., objects that given rule conditions can cover
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ConsistencyAndCoverageStoppingConditionChecker(RuleConditionsEvaluator ruleConditionsEvaluator, double consistencyThreshold, IntSet objectsThatCanBeCovered) {
		this.ruleConditionsEvaluator = Precondition.notNull(ruleConditionsEvaluator, "Rule conditions evaluator is null.");
		this.consistencyThreshold = consistencyThreshold;
		this.objectsThatCanBeCovered = Precondition.notNull(objectsThatCanBeCovered, "Set of objects that can be covered is null.");
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
		
		if (!ruleConditionsEvaluator.evaluationSatisfiesThreshold(ruleConditions, consistencyThreshold)) {
			return false;
		} else {
			RuleConditions.CoveredObjectsIterator coveredObjectsIterator = ruleConditions.getCoveredObjectsIterator();
			int coveredObjectIndex;
			while ((coveredObjectIndex = coveredObjectsIterator.next()) >= 0) {
				if (!objectsThatCanBeCovered.contains(coveredObjectIndex)) { //not allowed object is covered by rule conditions
					return false;
				}
			}
			return true;
		}
	}

}
