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
 * Abstract rule conditions set pruner, storing array of rule conditions evaluators and implementing {@link RuleConditionsSetPruner} interface.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class AbstractRuleConditionsSetPruner implements RuleConditionsSetPruner {
	
	/**
	 * Rule conditions evaluators used lexicographically to evaluate each rule conditions {@link RuleConditions} considered by this pruner.
	 */
	RuleConditionsEvaluator[] ruleConditionsEvaluators;

	/**
	 * Constructor for this rule conditions set pruner storing given rule conditions evaluators.
	 * 
	 * @param ruleConditionsEvaluators rule conditions evaluators used lexicographically to evaluate each {@link RuleConditions} from considered list
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public AbstractRuleConditionsSetPruner(RuleConditionsEvaluator[] ruleConditionsEvaluators) {
		super();
		this.ruleConditionsEvaluators = Precondition.notNullWithContents(ruleConditionsEvaluators, "Rule conditions evaluators are null.", "Rule conditions evaluator is null at index %i.");
	}

}
