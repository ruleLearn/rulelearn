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

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Abstract condition generator, employing condition addition evaluators {@link ConditionAdditionEvaluator} and implementing {@link ConditionGenerator} interface.
 * The evaluators are used lexicographically, i.e., when searching for the best elementary condition, first the evaluation returned by the first evaluator is taken into account,
 * and only if two or more conditions are evaluated equally, the second evaluator is used, and so on. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class AbstractConditionGeneratorWithEvaluators implements ConditionGenerator {

	/**
	 * Condition addition evaluators used lexicographically to evaluate each condition by assuming its addition to considered rule conditions.
	 */
	ConditionAdditionEvaluator[] conditionAdditionEvaluators = null;
	
	/**
	 * Constructor for this condition generator. Stores given evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionAdditionEvaluators array with condition addition evaluators used lexicographically
	 * 
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 * @throws NullPointerException if type of any condition addition evaluator is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 */
	public AbstractConditionGeneratorWithEvaluators(ConditionAdditionEvaluator[] conditionAdditionEvaluators) {
		super();
		Precondition.nonEmpty(Precondition.notNullWithContents(conditionAdditionEvaluators,
				"Condition addition evaluators are null.",
				"Condition addition evaluator is null at index %i."), "Array of condition addition evaluators is empty.");
		for (ConditionAdditionEvaluator conditionEvaluator : conditionAdditionEvaluators) {
			Precondition.notNull(conditionEvaluator.getType(), "Type of a condition addition evaluator is null.");
		}
		this.conditionAdditionEvaluators = conditionAdditionEvaluators;
	}
	
	//TODO: add getter for conditionAdditionEvaluators
	
}
