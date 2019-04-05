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

import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.core.InvalidValueException;

/**
 * Provider of {@link RuleInducerComponents rule inducer components} with different {@link RuleInductionStoppingConditionChecker stopping condition checkers}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class StoppingConditionsChangingRuleInducerComponentsProvider implements RuleInducerComponentsProvider {

	/**
	 * Builder used to provide {@link RuleInducerComponents rule inducer components}.
	 */
	RuleInducerComponents.Builder builder;
	
	/**
	 * Provider of {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers}.
	 */
	RuleInductionStoppingConditionCheckerProvider stoppingCondtionCheckerProvider;
	
	/**
	 * Provider of {@link RuleConditionsPruner rule conditions pruners}.
	 */
	RuleConditionsPrunerProvider ruleConditionsPrunerProvider;
	
	/**
	 * Constructs this provider of rule inducer components with a given {@link RuleInductionStoppingConditionCheckerProvider rule induction stopping condition checker provider}.<br>
	 * It is assumed that other rule inducer components, which are provided by this provider, are not dependent on {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers} 
	 * provided by the given rule induction stopping condition checker provider.<br>
	 *   
	 * In case when this is not true, different a constructor should be used.<br>  
	 * 
	 * @param builder builder of {@link RuleInducerComponents rule inducer components}
	 * @param stoppingCondtionCheckerProvider provider of {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public StoppingConditionsChangingRuleInducerComponentsProvider(RuleInducerComponents.Builder builder, RuleInductionStoppingConditionCheckerProvider stoppingCondtionCheckerProvider) {
		this.builder = notNull(builder, "Provdided rule components builder is null.");
		// set stopping condition checker provider
		this.stoppingCondtionCheckerProvider = (notNull(stoppingCondtionCheckerProvider, "Provided rule induction stopping condition checker provider is null."));
		this.ruleConditionsPrunerProvider = null; //be straightforward
	}
	
	/**
	 * Constructs this provider of rule inducer components with a given {@link RuleInductionStoppingConditionCheckerProvider rule induction stopping condition checker provider}, 
	 * and a given {@link RuleConditionsPrunerProvider rule conditions pruner provider}.<br> 
	 * It is assumed that {@link RuleConditionsPruner rule condition pruners} are dependent on {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers}. 
	 * Both needs to be defined explicitly while constructing this rule inducer components provider.<br>
	 * 
	 * @param builder builder of {@link RuleInducerComponents rule inducer components}
	 * @param stoppingCondtionCheckerProvider provider of {@link RuleInductionStoppingConditionChecker rule induction stopping condition checkers}
	 * @param ruleConditionsPrunerProvider provider of {@link RuleConditionsPruner rule condition pruners}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if the number of stopping condition checkers and the number rule conditions pruners specified by providers differ
	 */
	public StoppingConditionsChangingRuleInducerComponentsProvider(RuleInducerComponents.Builder builder, RuleInductionStoppingConditionCheckerProvider stoppingCondtionCheckerProvider,
			RuleConditionsPrunerProvider ruleConditionsPrunerProvider) {
		this.builder = notNull(builder, "Provdided rule components builder is null.");
		// set stopping condition checker provider
		this.stoppingCondtionCheckerProvider = notNull(stoppingCondtionCheckerProvider, "Provided rule induction stopping condition checker provider is null.");
		// set pruner provider
		this.ruleConditionsPrunerProvider = notNull(ruleConditionsPrunerProvider, "Provided rule conditions pruner provider is null.");
		if (this.stoppingCondtionCheckerProvider.getCount() != this.ruleConditionsPrunerProvider.getCount()) {
			throw new InvalidValueException("Different number of stopping condition checkers and rule conditions pruners in providers.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param i {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws IndexOutOfBoundsException if given index is less than zero or
	 *         greater or equal to the number of available stopping condition checkers
	 */
	@Override
	public RuleInducerComponents provide(int i) {
		//update builder's checker first
		builder.ruleInductionStoppingConditionChecker(stoppingCondtionCheckerProvider.getStoppingConditionChecker(i)); 
		if (ruleConditionsPrunerProvider != null) { //check if pruner depends on checker
			//then update builder's pruner too
			builder.ruleConditionsPruner(ruleConditionsPrunerProvider.getRuleConditionsPruner(i)); 
		}
		return builder.build();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return stoppingCondtionCheckerProvider.getCount();
	}
	
}
