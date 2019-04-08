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

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.Precondition;

/**
 * Simple rule induction stopping condition checker provider, constructed using and array of {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers},
 * and capable of providing any of these checkers, by its index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleRuleInductionStoppingConditionCheckerProvider implements RuleInductionStoppingConditionCheckerProvider {

	/**
	 * Provided {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers}.
	 */
	RuleInductionStoppingConditionChecker[] stoppingConditionCheckers;
	
	/**
	 * Constructor storing given array of {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers}.
	 * 
	 * @param stoppingConditionCheckers array of rule induction stopping condition checkers to be stored in this provider
	 * 
	 * @throws NullPointerException if given array is {@code null}
	 * @throws NullPointerException if any of the elements of the given array is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 */
	public SimpleRuleInductionStoppingConditionCheckerProvider(RuleInductionStoppingConditionChecker[] stoppingConditionCheckers) {
		this.stoppingConditionCheckers = Precondition.nonEmpty(Precondition.notNullWithContents(stoppingConditionCheckers,
				"Provided stopping condition checkers are null.",
				"Provided stopping condition checker is null at index %i."), "Array with rule induction stopping condition checkers is empty.");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return stoppingConditionCheckers.length;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param i {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
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
