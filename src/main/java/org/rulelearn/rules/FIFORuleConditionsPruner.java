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

import org.rulelearn.core.InvalidSizeException;

/**
 * Pruner for rule conditions that analyzes conditions from the oldest (first added) to the newest one (last added). 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class FIFORuleConditionsPruner implements RuleConditionsPruner {

	/**
	 * {@inheritDoc}.
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param stoppingConditionsToObey {@inheritDoc} 
	 * @param conditionRemovalEvaluators {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if any of the given lists is empty
	 */
	@Override
	public RuleConditions prune(RuleConditions ruleConditions,
			RuleInductionStoppingConditionChecker stoppingConditionsToObey,
			ConditionRemovalEvaluator[] conditionRemovalEvaluators) { //TODO: what with conditionRemovalEvaluators?
		// TODO: implement
		return null;
	}

}
