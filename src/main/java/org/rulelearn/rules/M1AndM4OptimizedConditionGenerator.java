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
import org.rulelearn.types.SimpleField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Condition generator taking advantage of two assumptions. The first assumption is that it is not necessary to consider addition of elementary conditions on attributes which have been already used in rule conditions.  
 * The second assumption is that for any condition attribute having {@link SimpleField} evaluations (which can be completely ordered),
 * the order of elementary conditions involving that attribute, implied by each considered condition addition evaluator, is consistent with the preference order in the value set of that attribute.<br>
 * <br>
 * For example, given attribute q with integer values, the following monotonic relationships are assumed:
 * <ul>
 *   <li>the better the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the better the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each cost-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each cost-type condition addition evaluator,</li>
 * </ul>
 * where q(x) denotes value (evaluation) of object x with respect to attribute q.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class M1AndM4OptimizedConditionGenerator extends M4OptimizedConditionGenerator {

	/**
	 * Constructor for this condition generator. Stores given monotonic condition addition evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionEvaluators array with monotonic condition addition evaluators used lexicographically
	 * 
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 * @throws NullPointerException if type of any condition addition evaluator is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 */
	public M1AndM4OptimizedConditionGenerator(MonotonicConditionAdditionEvaluator[] conditionEvaluators) {
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
