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
import org.rulelearn.types.EvaluationField;

/**
 * Contract of an evaluator of a condition {@link Condition} used to evaluate it before it may be added to rule conditions {@link RuleConditions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ConditionAdditionEvaluator extends Measure {
	
	/**
	 * Evaluates given condition in the context of given rule conditions.
	 * This evaluation concerns modified rule conditions, that would be obtained by adding given condition. 
	 * 
	 * @param ruleConditions rule conditions being the context of evaluation of given condition
	 * @param condition condition to be evaluated
	 * 
	 * @return evaluation of hypothetical rule conditions obtained from the given ones by adding given condition; if given condition is {@code null}, then returns {@link Double#MIN_VALUE} or
	 *         {@link Double#MAX_VALUE}, when this evaluator is a gain-type or cost-type measure, respectively.
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	public double evaluateWithCondition(RuleConditions ruleConditions, Condition<EvaluationField> condition);

}
