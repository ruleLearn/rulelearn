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

import org.rulelearn.core.InvalidSizeException;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Contract of a pruner used to remove redundant rule conditions {@link RuleConditions} from a set (list) of rule conditions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleConditionsSetPruner {
	
	/**
	 * Prunes given list of rule conditions by removing redundant rule conditions, so as to produce a new list of rule conditions that together cover the same objects
	 * which are covered by the rule conditions from the original list.
	 * 
	 * @param ruleConditionsList (input) list of rule conditions that should be pruned; this object should not be modified as a result of performed pruning
	 * @param ruleConditionsEvaluators array of evaluators used to evaluate rule conditions that can potentially be removed from the currently considered list of rule conditions
	 * @param indicesOfObjectsToKeepCovered set of indices of objects that are covered by at least one rule conditions from the given (input) list of rule conditions,
	 *        and should remain covered by at least one rule conditions from the returned (output) list of rule conditions
	 * 
	 * @return (output) pruned list of rule conditions (new object)
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if any of the given lists is empty
	 */
	public List<RuleConditions> prune(List<RuleConditions> ruleConditionsList, RuleConditionsEvaluator[] ruleConditionsEvaluators, IntList indicesOfObjectsToKeepCovered);

}
