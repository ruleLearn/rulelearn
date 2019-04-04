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

import org.rulelearn.core.Precondition;

/**
 * TODO SimpleRuleInductionStoppingConditionCheckerProvider
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleRuleInductionStoppingConditionCheckerProvider implements RuleInductionStoppingConditionCheckerProvider {

	/**
	 * TODO
	 */
	RuleInductionStoppingConditionChecker[] stoppingConditionCheckers;
	
	/**
	 * TODO
	 */
	public SimpleRuleInductionStoppingConditionCheckerProvider(RuleInductionStoppingConditionChecker[] stoppingConditionCheckers) {
		this.stoppingConditionCheckers = Precondition.notNullWithContents(stoppingConditionCheckers, "Provided stopping condition checkers are null.", "Provided stopping condition checker %i is null.");
	}
	
	/* TODO (non-Javadoc)
	 * @see org.rulelearn.rules.RuleInductionStoppingConditionCheckerProvider#getCount()
	 */
	@Override
	public int getCount() {
		return stoppingConditionCheckers.length;
	}

	/* TODO (non-Javadoc)
	 * @see org.rulelearn.rules.RuleInductionStoppingConditionCheckerProvider#getStoppingConditionChecker(int)
	 */
	@Override
	public RuleInductionStoppingConditionChecker getStoppingConditionChecker(int i) {
		if ((i >= 0) && (i < stoppingConditionCheckers.length)) {
			return stoppingConditionCheckers[i];
		}
		else {
			throw new IndexOutOfBoundsException("Requested stopping condition checker does not exist at index: " + i + ".");
		}
	}

}
