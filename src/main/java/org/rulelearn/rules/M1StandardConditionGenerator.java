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

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Condition generator taking advantage of the assumption that it is not necessary to consider addition of elementary conditions on attributes which have been already used in rule conditions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class M1StandardConditionGenerator extends StandardConditionGenerator {
	
	/**
	 * Constructor for this condition generator. Stores given evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionEvaluators array with condition evaluators used lexicographically
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public M1StandardConditionGenerator(ConditionAdditionEvaluator[] conditionEvaluators) {
		super(conditionEvaluators);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 */
	@Override
	boolean skipUsedAttributes() {
		return true;
	}
	
}
