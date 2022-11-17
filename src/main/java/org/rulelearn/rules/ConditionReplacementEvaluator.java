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
 * Contract of a {@link Condition condition} replacement evaluator used to check how does the evaluation change
 * when one condition of {@link RuleConditions rule conditions} is replaced with another condition.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ConditionReplacementEvaluator extends Measure {
	
	/**
	 * Evaluates replacement of condition in the context of given rule conditions.
	 * This evaluation concerns modified rule conditions, that would be obtained by replacing condition with given index with given new condition. 
	 * 
	 * @param ruleConditions rule conditions being the context of evaluation
	 * @param conditionIndex index of replaced condition in given rule conditions
	 * @param newCondition the replacing condition
	 * 
	 * @return evaluation of a hypothetical rule conditions obtained from the given ones by replacing condition with given index with given new condition
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws IndexOutOfBoundsException if given condition index does not index any condition in given rule conditions
	 */
	public double evaluateWhenReplacingCondition(RuleConditions ruleConditions, int conditionIndex, Condition<? extends EvaluationField> newCondition);
	
	/**
	 * Checks if evaluation of rule conditions obtained by replacing condition with given index with given new condition,
	 * as returned by {@link #evaluateWhenReplacingCondition(RuleConditions, int, Condition)}, satisfies given threshold.
	 * Takes into account type of this measure, as returned by {@link #getType()}.
	 * 
	 * @param ruleConditions rule conditions to evaluate
	 * @param threshold threshold compared with evaluation of rule conditions obtained by replacing condition with given index with given new condition
	 * @param conditionIndex index of the condition to be excluded
	 * @param newCondition new condition to be included
	 * @return {@code true} if evaluation of rule conditions obtained by replacing condition with given index with given new condition,
	 *         as returned by {@link #evaluateWhenReplacingCondition(RuleConditions, int, Condition)}, 
	 *         satisfies given threshold, {@code false} otherwise
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning the number of conditions present in given rule conditions
	 */
	public default boolean evaluationSatisfiesThresholdWhenReplacingCondition(RuleConditions ruleConditions, double threshold, int conditionIndex, Condition<? extends EvaluationField> newCondition) {
		return (this.getType() == MeasureType.GAIN ?
					this.evaluateWhenReplacingCondition(ruleConditions, conditionIndex, newCondition) >= threshold :
					this.evaluateWhenReplacingCondition(ruleConditions, conditionIndex, newCondition) <= threshold);
	}

}
