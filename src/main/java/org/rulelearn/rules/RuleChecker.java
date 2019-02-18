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

/**
 * Contract of a checker used to decide whether a decision rule, consisting of a set of elementary conditions and a set of elementary decisions, is acceptable in the context of a given set (list) of rules.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleChecker {
	
	/**
	 * Checks if given rule is acceptable in the context of a given set (list) of rules. Each rule is an instance of {@link RuleConditionsWithApproximatedSet} that contains elementary conditions
	 * obtained using {@link RuleConditionsWithApproximatedSet#getRuleConditions()} and elementary decisions obtained in two steps, first using
	 * {@link RuleConditionsWithApproximatedSet#getApproximatedSet()} and then {@link ApproximatedSetRuleDecisionsProvider#getRuleDecisions(ApproximatedSet)}.
	 *  
	 * @param ruleSet set of rules
	 * @param rule rule to be verified against given set of rules
	 * 
	 * @return {@code true} if given rule is acceptable in the context of a given set (list) of rules,
	 *         {@code false} otherwise
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public boolean check(List<RuleConditionsWithApproximatedSet> ruleSet, RuleConditionsWithApproximatedSet rule);

}
