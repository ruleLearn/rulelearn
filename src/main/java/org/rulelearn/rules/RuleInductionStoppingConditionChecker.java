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
 * Contract of a checker verifying rule conditions {@link RuleConditions} against different stopping conditions, like reaching a given number of conditions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleInductionStoppingConditionChecker {
	
	/**
	 * Checks if tested stopping condition in satisfied by given rule conditions.
	 *  
	 * @param ruleConditions rule conditions to be checked
	 * @return {@code true} if tested stopping condition in satisfied by given rule conditions,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	public boolean isStoppingConditionSatisified(RuleConditions ruleConditions);
	
	/**
	 * Checks if tested stopping condition in satisfied by given rule conditions without condition having given index.
	 *  
	 * @param ruleConditions rule conditions to be checked
	 * @param conditionIndex index of the condition to be excluded
	 * @return {@code true} if tested stopping condition in satisfied by given rule conditions without condition having given index,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning the number of conditions present in given rule conditions
	 */
	public boolean isStoppingConditionSatisifiedWithoutCondition(RuleConditions ruleConditions, int conditionIndex);

}
