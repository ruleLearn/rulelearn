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

import org.rulelearn.core.Precondition;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Dummy rule conditions set pruner, that does not perform any pruning, and just returns original list of rule conditions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DummyRuleConditionsSetPruner implements RuleConditionsSetPruner {

	/**
	 * Returns given list of rule conditions (not performing any pruning).
	 * 
	 * @param ruleConditionsList (input) list of rule conditions that should be pruned; this object is not modified as a result of performed pruning
	 * @param indicesOfObjectsToKeepCovered {@inheritDoc}
	 * 
	 * @return the list being the first parameter, unmodified
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	@Override
	public List<RuleConditions> prune(List<RuleConditions> ruleConditionsList, IntSet indicesOfObjectsToKeepCovered) {
		Precondition.notNull(ruleConditionsList, "List of rule conditions for dummy rule conditions set pruner is null.");
		Precondition.notNull(indicesOfObjectsToKeepCovered, "Set of objects to keep covered is null.");
		
		return ruleConditionsList;
	}

}
