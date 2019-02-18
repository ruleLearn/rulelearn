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

/**
 * Contract of a (m4)-monotonic evaluator of a condition {@link Condition} used to evaluate it before it may be added to rule conditions {@link RuleConditions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface MonotonicConditionAdditionEvaluator extends ConditionAdditionEvaluator {
	
	/**
	 * Type of monotonicity of a condition addition evaluator.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static enum MonotonicityType {
		/**
		 * Indicates that value of evaluator improves (does not deteriorate) if the condition is made less restrictive (covers more objects).
		 */
		IMPROVES_WITH_NUMBER_OF_COVERED_OBJECTS,
		/**
		 * Indicates that value of evaluator deteriorates (does not improve) if the condition is made less restrictive (covers more objects).
		 */
		DETERIORATES_WITH_NUMBER_OF_COVERED_OBJECTS
	}
	
	/**
	 * Gets monotonicity type of this condition addition evaluator.
	 * 
	 * @return monotonicity type of this condition addition evaluator
	 */
	public MonotonicityType getMonotonictyType();

}
