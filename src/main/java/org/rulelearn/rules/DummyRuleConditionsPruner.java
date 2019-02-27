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

/**
 * Dummy rule conditions pruner, that does not perform any pruning, and just returns original rule conditions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DummyRuleConditionsPruner implements RuleConditionsPruner {

	/**
	 * Returns given rule conditions (not performing any pruning).
	 * 
	 * @param ruleConditions (input) rule conditions that should be pruned; this object is not modified as a result of performed pruning
	 * 
	 * @return rule conditions being the first parameter, unmodified
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	@Override
	public RuleConditions prune(RuleConditions ruleConditions) {
		Precondition.notNull(ruleConditions, "Rule conditions for dummy rule conditions pruner are null.");
		
		return ruleConditions;
	}

}
