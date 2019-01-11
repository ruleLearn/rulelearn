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

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Contract of a pruner used to remove redundant rule conditions {@link RuleConditions} from a list of rule conditions.
 * For a given a set of objects, rule conditions are considered to be redundant if it is possible to remove them from the list and the remaining rule conditions still cover all the objects from the considered set.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleConditionsSetPruner {
	
	/**
	 * Prunes given list of rule conditions by removing redundant rule conditions, so as to get a sub-list of rule conditions that together cover each object from the given set.
	 * 
	 * @param ruleConditionsList (input) list of rule conditions that should be pruned; this object can be modified as a result of performed pruning
	 * @param indicesOfObjectsToKeepCovered set of indices of positive objects that are covered by at least one rule conditions from the given (input) list of rule conditions,
	 *        and should remain covered by at least one rule conditions from the returned (output) list of rule conditions
	 * 
	 * @return (output) pruned list of rule conditions (the same object as the first parameter, but possibly with fewer rule conditions)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public List<RuleConditions> prune(List<RuleConditions> ruleConditionsList, IntSet indicesOfObjectsToKeepCovered);

}
