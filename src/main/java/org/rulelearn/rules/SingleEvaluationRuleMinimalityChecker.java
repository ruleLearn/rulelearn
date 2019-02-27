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

import java.util.List;

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.core.Precondition;

/**
 * Rule minimality checker that involves comparison of decision rules with respect to a single rule conditions evaluator {@link RuleConditionsEvaluator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SingleEvaluationRuleMinimalityChecker extends RuleMinimalityChecker {
	
	/**
	 * Constructs this checker storing given rule conditions evaluator.
	 * 
	 * @param ruleConditionsEvaluator rule conditions evaluator used to evaluate compared decision rules
	 * @throws NullPointerException if given rule conditions evaluator is {@code null}
	 */
	public SingleEvaluationRuleMinimalityChecker(RuleConditionsEvaluator ruleConditionsEvaluator) {
		super(new RuleConditionsEvaluator[] {ruleConditionsEvaluator});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleSet {@inheritDoc}
	 * @param rule {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public boolean check(List<RuleConditionsWithApproximatedSet> ruleSet, RuleConditionsWithApproximatedSet rule) {
		Precondition.notNull(ruleSet, "Rule set used to check minimality of a decision rule is null.");
		Precondition.notNull(rule, "Rule checked for minimaility null.");
		
		RuleConditions ruleConditions = rule.getRuleConditions();
		ApproximatedSet approximatedSet = rule.getApproximatedSet();
		
		RuleConditions priorRuleConditions;
		ApproximatedSet priorApproximatedSet;
		
		boolean ruleIsMinimal = true;
		
		for (RuleConditionsWithApproximatedSet priorRule : ruleSet) {
			priorRuleConditions = priorRule.getRuleConditions();
			priorApproximatedSet = priorRule.getApproximatedSet();
			
			if (approximatedSet.includes(priorApproximatedSet)) { //tested rule is less or equally specific w.r.t. decision part (and thus, its conclusion is not more precise)
				if (ruleConditions.isLessOrEquallyGeneralAs(priorRuleConditions)) { //tested rule is less or equally general w.r.t. condition part
					if (ruleConditionsEvaluators[0].confront(ruleConditions, priorRuleConditions) <= 0) { //tested rule is not better w.r.t. considered rule conditions evaluator
						ruleIsMinimal = false; //tested rule is not minimal
						break; //stop verification - tested rule already found to be not minimal 
					}
				}
			}
		}
		
		return ruleIsMinimal;
	}

}
