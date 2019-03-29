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
 * Contract of a checker verifying rule conditions {@link RuleConditions} against different stopping conditions, which involve evaluation threshold, like e.g., satisfying consistency measure threshold. 
 * Other stopping conditions like reaching a given number of conditions may also be taken into account.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleInductionStoppingConditionCheckerWithThreshold extends RuleInductionStoppingConditionChecker {

	/**
	 * Makes a copy of stopping condition checker with defined value of evaluation threshold.
	 * 
	 * @param evaluationThreshold value of evaluation threshold
	 * @return copy of stopping condition checker with defined value of evaluation threshold.
	 */
	public RuleInductionStoppingConditionCheckerWithThreshold copyWithNewThreshold(double evaluationThreshold);
	
	/**
	 * Gets value of evaluation threshold.
	 * 
	 * @return value of evaluation threshold
	 */
	public double getThreshold();
	
}
