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
import org.rulelearn.core.Precondition;

/**
 * Abstract rule conditions pruner, storing rule induction stopping condition checker {@link RuleInductionStoppingConditionChecker} as well as array of condition removal evaluators {@link ConditionRemovalEvaluator},
 * and implementing {@link RuleConditionsPruner} interface.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class AbstractRuleConditionsPrunerWithEvaluators extends AbstractRuleConditionsPruner {
	
	/**
	 * Array with condition removal evaluators used lexicographically.
	 */
	ConditionRemovalEvaluator[] conditionRemovalEvaluators = null;

	/**
	 * Constructor storing given stopping condition checker and array of condition removal evaluators.
	 * 
	 * @param stoppingConditionChecker stopping condition checker
	 * @param conditionRemovalEvaluators array of condition removal evaluators used to evaluate conditions that can potentially be removed from the considered rule conditions;
	 *        these evaluators are to be considered lexicographically
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if given array of evaluators is empty
	 */
	public AbstractRuleConditionsPrunerWithEvaluators(RuleInductionStoppingConditionChecker stoppingConditionChecker, ConditionRemovalEvaluator[] conditionRemovalEvaluators) {
		super(stoppingConditionChecker);
		this.conditionRemovalEvaluators = Precondition.nonEmpty(Precondition.notNull(conditionRemovalEvaluators, "Condition removal evaluators are null."), "Array with condition removal evaluators is empty.");
		
		// TODO: check each evaluator if not null
	}

}
