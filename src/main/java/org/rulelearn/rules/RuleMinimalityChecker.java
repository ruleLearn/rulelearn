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

/**
 * Checks if given rule is minimal in the context of a given set of rules. Rule minimality is understood as in:<br>
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches. Information Sciences, 181, 2011, pp. 987-1002.<br>
 * Moreover, implemented minimality check involves comparison of rule evaluations, i.e., a rule is minimal in the context of a given set of rules if this set
 * does not contain a rule with not less general conditions, not less specific decision, and at least as good evaluations (where, e.g., evaluations may be compared lexicographically,
 * or jointly using Pareto dominance relation).
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class RuleMinimalityChecker implements RuleChecker {
	
	/**
	 * List of rule evaluators used to evaluate compared decision rules.
	 */
	List<RuleEvaluator> ruleEvaluators;
	
	/**
	 * Constructs this checker.
	 * 
	 * @param ruleEvaluators list of rule evaluators used to evaluate compared decision rules
	 * @throws NullPointerException if the list of rule evaluators is {@code null}
	 */
	public RuleMinimalityChecker(List<RuleEvaluator> ruleEvaluators) {
		this.ruleEvaluators = ruleEvaluators;
	}

	/**
	 * {@inheritDoc}
	 * Checks minimality of the given rule using rule evaluators for which this checker has been constructed. 
	 * 
	 * @param ruleSet {@inheritDoc}
	 * @param rule {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public abstract boolean check(List<Rule> ruleSet, Rule rule);

}
