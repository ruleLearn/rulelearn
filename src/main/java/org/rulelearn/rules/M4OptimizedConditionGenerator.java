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
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Condition generator taking advantage of monotonicity property (m4) of optimized rule consistency measure.
 * 
 * TODO: is this well designed?
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class M4OptimizedConditionGenerator implements ConditionGenerator {
	
	/**
	 * Condition evaluators used lexicographically.
	 */
	ConditionAdditionEvaluator[] conditionEvaluators;

	/**
	 * Constructor for this condition generator.
	 * 
	 * @param conditionEvaluators array with condition evaluators used lexicographically.
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public M4OptimizedConditionGenerator(ConditionAdditionEvaluator[] conditionEvaluators) {
		super();
		this.conditionEvaluators = Precondition.notNull(conditionEvaluators, "Condition evaluators are null.");
		
		for (int i = 0; i < conditionEvaluators.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("Condition evaluator at index ").append(i).append(" is null.");
			Precondition.notNull(conditionEvaluators[i], sb.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param consideredObjects {@inheritDoc}
	 * @param ruleConditions rule conditions for which best condition should be obtained
	 * @return best condition that can be added to given rule conditions.
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	@Override
	public Condition<EvaluationField> getBestCondition(IntList consideredObjects, RuleConditions ruleConditions) {
		Precondition.notNull(consideredObjects, "List of objects considered in m4-optimized condition generator is null.");
		Precondition.notNull(ruleConditions, "Rule conditions considered in m4-optimized condition generator is null.");
		
		// TODO implement
		return null;
	}

}
