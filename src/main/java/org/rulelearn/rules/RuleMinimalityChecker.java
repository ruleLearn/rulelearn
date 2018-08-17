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
import org.rulelearn.measures.ConsistencyMeasure;

/**
 * Checks whether given rule is minimal in the context of a given set of rules. Rule minimality is understood as in:
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches. Information Sciences, 181, 2011, pp. 987-1002.
 * Moreover, implemented minimality check involves comparison of rule consistency measure values, i.e., a rule is minimal in the context of a given set of rules if this set
 * does not contain a rule with not less general conditions, not less specific decision, and not worse consistency measure value.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleMinimalityChecker implements RuleChecker {
	
	List<ConsistencyMeasure<ApproximatedSet>> consistencyMeasures;
	
	/**
	 * Constructs this checker.
	 * 
	 * @param consistencyMeasures consistency measure used to calculate consistency of decision rules
	 */
	public RuleMinimalityChecker(List<ConsistencyMeasure<ApproximatedSet>> consistencyMeasures) {
		this.consistencyMeasures = consistencyMeasures;
	}

	/**
	 * {@inheritDoc}
	 * TODO
	 * 
	 * @param ruleSet {@inheritDoc}
	 * @param rule {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean check(List<Rule> ruleSet, Rule rule) {
		return false; //TODO
	}

}
