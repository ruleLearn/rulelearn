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
 * Dummy rule minimality checker assuming that all checked rules are minimal.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DummyRuleMinimalityChecker extends RuleMinimalityChecker {
	
	/**
	 * Sole constructor.
	 */
	public DummyRuleMinimalityChecker() {
		super();
	}

	/**
	 * Checks if given rule is acceptable in the context of a given set (list) of rules. Each rule is an instance of {@link RuleConditionsWithApproximatedSet} that contains elementary conditions
	 * obtained using {@link RuleConditionsWithApproximatedSet#getRuleConditions()} and elementary decisions obtained in two steps, first using
	 * {@link RuleConditionsWithApproximatedSet#getApproximatedSet()} and then {@link ApproximatedSetRuleDecisionsProvider#getRuleDecisions(ApproximatedSet)}.<br>
	 * <br>
	 * Always returns {@code true}.
	 * 
	 * @param ruleSet {@inheritDoc}
	 * @param rule {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public boolean check(List<RuleConditionsWithApproximatedSet> ruleSet, RuleConditionsWithApproximatedSet rule) {
		Precondition.notNull(ruleSet, "Rule set used to check minimality of a decision rule by a dummy minimality checker is null.");
		Precondition.notNull(rule, "Rule checked for minimaility by a dummy minimality checker is null.");
		
		return true;
	}

}
