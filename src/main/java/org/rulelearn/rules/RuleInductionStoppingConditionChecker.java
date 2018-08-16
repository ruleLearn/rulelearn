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
 * Interface to be implemented by checkers verifying {@link RuleConditions} against different stopping conditions, like satisfying consistency measure threshold or reaching a given number of conditions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleInductionStoppingConditionChecker {
	
	/**
	 * Checks if this stopping condition in satisfied by given rule conditions.
	 *  
	 * @param ruleConditions rule conditions to be checked
	 * @return {@code true} if this stopping condition in satisfied by given rule conditions, {@code false} otherwise
	 */
	public boolean isConditionSatisified(RuleConditions ruleConditions);

}
