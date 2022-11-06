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
 * Abstract rule conditions generalizer, storing {@link RuleInductionStoppingConditionChecker rule induction stopping condition checker}
 * and implementing {@link RuleConditionsGeneralizer} interface.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class AbstractRuleConditionsGeneralizer implements RuleConditionsGeneralizer {
	
	/**
	 * Stopping condition checker consulted for checking correctness of generalizing rule conditions.
	 */
	RuleInductionStoppingConditionChecker stoppingConditionChecker;
	
	/**
	 * Constructor storing given stopping condition checker.
	 * 
	 * @param stoppingConditionChecker stopping condition checker used to assure that generalized rule conditions satisfy stopping condition(s)
	 * @throws NullPointerException if given stopping condition checker is {@code null}
	 */
	public AbstractRuleConditionsGeneralizer(RuleInductionStoppingConditionChecker stoppingConditionChecker) {
		super();
		this.stoppingConditionChecker = Precondition.notNull(stoppingConditionChecker, "Rule induction stopping condition checker is null.");
	}
	
	/**
	 * Makes a copy of rule conditions generalizer with provided stopping condition checker.
	 * 
	 * @param stoppingConditionChecker stopping condition checker {@link RuleInductionStoppingConditionChecker} stored in generalizer
	 * @return a copy of rule conditions generalizer with provided stopping condition checker
	 */
	public abstract AbstractRuleConditionsGeneralizer copyWithNewStoppingConditionChecker(RuleInductionStoppingConditionChecker stoppingConditionChecker); //TODO: remove this method?

	/**
	 * Gets the stopping condition checker for which this generalizer has been constructed.
	 * 
	 * @return the stopping condition checker for which this generalizer has been constructed
	 */
	public RuleInductionStoppingConditionChecker getStoppingConditionChecker() {
		return this.stoppingConditionChecker;
	}

}
