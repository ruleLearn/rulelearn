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
 * TODO
 * Provider of {@link RuleInducerComponents rule inducer components} with {@link RuleInductionStoppingConditionChecker stopping condition checkers}
 * able to handle different values of evaluation threshold.
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
	 * TODO
	 */
	RuleInductionStoppingConditionCheckerProvider stoppingCondtionCheckerProvider;
	
	/**
	 * TODO
	 */
	RuleConditionsPrunerProvider ruleConditionsPrunerProvider;
	
	/**
	 * TODO
	 * Constructs provider with stopping condition checker defined for different values of evaluation threshold. 
	 * It is assumed that other parts of rule induction process are not dependent on modification of value of evaluation threshold in stopping condition checker. 
	 * In case when this is not true, different constructor should be used.  
	 * 
	 * @param builder builder of {@link RuleInducerComponents rule inducer components}
	 * @param stoppingCondtionCheckerProvider stopping condition checker provider TODO
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
	 * TODO
	 * Constructs provider with stopping condition checker defined for different values of evaluation threshold,
	 * and rule conditions pruner associated with the checker.
	 * 
	 * @param builder builder of {@link RuleInducerComponents rule inducer components}
	 * @param stoppingCondtionCheckerProvider stopping condition checker provider TODO
	 * @param ruleConditionsPrunerProvider rule conditions pruner provider TODO
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException TODO
	 */
	public StoppingConditionsChangingRuleInducerComponentsProvider(RuleInducerComponents.Builder builder, RuleInductionStoppingConditionCheckerProvider stoppingCondtionCheckerProvider,
			RuleConditionsPrunerProvider ruleConditionsPrunerProvider) {
		this.builder = notNull(builder, "Provdided rule components builder is null.");
		// set stopping condition checker provider
		this.stoppingCondtionCheckerProvider = notNull(stoppingCondtionCheckerProvider, "Provided rule induction stopping condition checker provider is null.");
		// set pruner provider
		this.ruleConditionsPrunerProvider = notNull(ruleConditionsPrunerProvider, "Provided rule conditions pruner provider is null.");
		if (this.stoppingCondtionCheckerProvider.getCount() != this.ruleConditionsPrunerProvider.getCount()) {
			throw new InvalidValueException("Different number of stopping condition checkers and rule conditions pruners.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param i {@inheritDoc}
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
