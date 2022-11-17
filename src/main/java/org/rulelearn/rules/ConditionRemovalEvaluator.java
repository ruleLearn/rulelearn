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
	 * @throws NullPointerException if given rule conditions are {@code null}
	 * @throws IndexOutOfBoundsException if given condition index does not index any condition in given rule conditions
	 */
	public double evaluateWithoutCondition(RuleConditions ruleConditions, int conditionIndex);
	
	/**
	 * Checks if evaluation of given rule conditions without a selected condition, as returned by {@link #evaluateWithoutCondition(RuleConditions, int)}, satisfies given threshold.
	 * Takes into account type of this measure, as returned by {@link #getType()}.
	 * 
	 * @param ruleConditions rule conditions to evaluate
	 * @param threshold threshold compared with evaluation of given rule conditions without the selected condition
	 * @param conditionIndex index of the condition to be excluded
	 * @return {@code true} if evaluation of given rule conditions without the selected condition, as returned by {@link #evaluateWithoutCondition(RuleConditions, int)}, 
	 * 			satisfies given threshold, {@code false} otherwise
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning the number of conditions present in given rule conditions
	 */
	public default boolean evaluationSatisfiesThresholdWithoutCondition(RuleConditions ruleConditions, double threshold, int conditionIndex) {
		return (this.getType() == MeasureType.GAIN ?
					this.evaluateWithoutCondition(ruleConditions, conditionIndex) >= threshold :
					this.evaluateWithoutCondition(ruleConditions, conditionIndex) <= threshold);
	}

}
