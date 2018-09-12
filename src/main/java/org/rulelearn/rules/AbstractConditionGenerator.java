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

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Abstract condition generator, storing condition addition evaluators and implementing {@link ConditionGenerator} interface.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class AbstractConditionGenerator implements ConditionGenerator {

	/**
	 * Condition evaluators used lexicographically.
	 */
	ConditionAdditionEvaluator[] conditionEvaluators;
	
	/**
	 * Constructor for this condition generator. Stores given evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionEvaluators array with condition evaluators used lexicographically
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public AbstractConditionGenerator(ConditionAdditionEvaluator[] conditionEvaluators) {
		super();
		this.conditionEvaluators = Precondition.notNull(conditionEvaluators, "Condition evaluators are null.");
		
		for (int i = 0; i < conditionEvaluators.length; i++) {
			Precondition.notNull(conditionEvaluators[i], "Condition evaluator at index ", String.valueOf(i), " is null.");
		}
	}

}
