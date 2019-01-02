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
 * Contract of an evaluator of rule conditions {@link RuleConditions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleConditionsEvaluator extends Measure {
	
	/**
	 * Evaluates given rule conditions.
	 * 
	 * @param ruleConditions rule conditions to be evaluated
	 * @return evaluation of given rule conditions
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	public double evaluate(RuleConditions ruleConditions);
	
	/**
	 * Evaluates given rule conditions without a selected condition.
	 * 
	 * @param ruleConditions rule conditions to be evaluated
	 * @param conditionIndex index of the condition to be excluded
	 * @return evaluation of given rule conditions without the selected condition
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning the number of conditions present in given rule conditions
	 */
	public double evaluateWithoutCondition(RuleConditions ruleConditions, int conditionIndex);
	
	/**
	 * Checks if evaluation of given rule conditions, as returned by {@link #evaluate(RuleConditions)}, satisfies given threshold.
	 * Takes into account type of this measure, as returned by {@link #getType()}.
	 * 
	 * @param ruleConditions rule conditions to evaluate
	 * @param threshold threshold compared with evaluation of given rule conditions
	 * @return {@code true} if evaluation of given rule conditions, as returned by {@link #evaluate(RuleConditions)}, satisfies given threshold,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	public default boolean evaluationSatisfiesThreshold(RuleConditions ruleConditions, double threshold) {
		return (this.getType() == MeasureType.GAIN ?
					this.evaluate(ruleConditions) >= threshold:
					this.evaluate(ruleConditions) <= threshold);
	}
	
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
					this.evaluateWithoutCondition(ruleConditions, conditionIndex) >= threshold:
					this.evaluateWithoutCondition(ruleConditions, conditionIndex) <= threshold);
	}
	
	/**
	 * Confronts two rule conditions with respect to this evaluator.
	 * 
	 * @param ruleConditions1 first rule conditions
	 * @param ruleConditions2 second rule conditions
	 * @return 0 if both rule conditions evaluate the same,<br>
	 *         1 if the first rule conditions have better evaluation than the second rule conditions,<br>
	 *         -1 if the second rule conditions have better evaluation than the first rule conditions
	 */
	public default int confront(RuleConditions ruleConditions1, RuleConditions ruleConditions2) {
		double evaluation1 = this.evaluate(ruleConditions1);
		double evaluation2 = this.evaluate(ruleConditions2);
		MeasureType measureType = this.getType(); //must not be null
		
		if (evaluation1 == evaluation2) {
			return 0;
		} else {
			if (evaluation1 > evaluation2) {
				return measureType == MeasureType.GAIN ? 1 : -1;
			} else { //evaluation1 < evaluation2
				return measureType == MeasureType.GAIN ? -1 : 1;
			}
		}
		
	}

}
