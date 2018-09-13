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

import org.rulelearn.measures.Measure;

/**
 * Contract of an evaluator of a condition {@link Condition} used to check whether it can be removed from rule conditions {@link RuleConditions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ConditionRemovalEvaluator extends Measure {
	
	/**
	 * Evaluates condition in the context of given rule conditions.
	 * This evaluation concerns modified rule conditions, that would be obtained by removal of concerned condition. 
	 * 
	 * @param ruleConditions rule conditions being the context of evaluation of condition with given index
	 * @param conditionIndex index of concerned condition in given rule conditions
	 * 
	 * @return evaluation of a hypothetical rule conditions obtained from the given ones by removing condition with given index
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws IndexOutOfBoundsException if given condition index does not index any condition in given rule conditions
	 */
	public double evaluateWithoutCondition(RuleConditions ruleConditions, int conditionIndex);

}
