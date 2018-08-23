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

/**
 * Contract of a pruner used to remove redundant conditions from rule conditions {@link RuleConditions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleConditionsPruner {
	
	/**
	 * Prunes given rule conditions by removing redundant conditions, so as to produce new rule conditions that still obey given stopping conditions.
	 * 
	 * @param ruleConditions rule conditions that should be pruned; this object should not be modified as a result of performed pruning
	 * @param stoppingConditionsToObey list of stopping conditions that are satisfied by given rule conditions, and have to be satisfied by the returned rule conditions 
	 * @param conditionRemovalEvaluators list of condition evaluators used to evaluate conditions that can potentially be removed from the currently considered rule conditions
	 * 
	 * @return pruned rule conditions (new object)
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if any of the given lists is empty
	 */
	public RuleConditions prune(RuleConditions ruleConditions, List<RuleInductionStoppingConditionChecker> stoppingConditionsToObey, List<ConditionRemovalEvaluator> conditionRemovalEvaluators);

}
