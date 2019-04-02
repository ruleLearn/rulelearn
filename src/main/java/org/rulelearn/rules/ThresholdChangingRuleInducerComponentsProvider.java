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

/**
 * Provider of rule inducer components {@link RuleInducerComponents} with stopping condition checkers {@link RuleInductionStoppingConditionCheckerWithThreshold}
 * able to handle different values of evaluation threshold.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ThresholdChangingRuleInducerComponentsProvider implements RuleInducerComponentsProvider {

	/**
	 * Builder used to provide rule inducer components {@link RuleInducerComponents}.
	 */
	RuleInducerComponents.Builder builder;

	/**
	 * Indication whether pruner {@link AbstractRuleConditionsPruner} used by the builder is associated with a stopping condition checker {@link RuleInductionStoppingConditionCheckerWithThreshold}
	 * and consequently needs to be updated when the stopping condition checker gets updated.
	 */
	boolean updatePruner = false;
	
	/**
	 * Constructs provider with stopping condition checker {@link RuleInductionStoppingConditionCheckerWithThreshold} defined for different values of evaluation threshold. 
	 * It is assumed that other parts of rule induction process are not dependent on modification of value of evaluation threshold in stopping condition checker. 
	 * In case when this is not true, different constructor should be used.  
	 * 
	 * @param builder builder of rule inducer components {@link RuleInducerComponents}
	 * @param stoppingCondtionChecker stopping condition checker {@link RuleInductionStoppingConditionCheckerWithThreshold} able to handle different values of evaluation threshold
	 */
	public ThresholdChangingRuleInducerComponentsProvider(RuleInducerComponents.Builder builder, RuleInductionStoppingConditionCheckerWithThreshold stoppingCondtionChecker) {
		this.builder = notNull(builder, "Provdided rule components builder is null.");
		// set stopping condition checker
		this.builder = this.builder.ruleInductionStoppingConditionChecker(notNull(stoppingCondtionChecker, "Provided rule induction stopping condition checker is null."));
		this.updatePruner = false; //be straightforward
	}
	
	/**
	 * Constructs provider with stopping condition checker {@link RuleInductionStoppingConditionCheckerWithThreshold} defined for different values of evaluation threshold, and rule conditions pruner associated with the checker.
	 * 
	 * @param builder builder of rule inducer components {@link RuleInducerComponents}
	 * @param stoppingCondtionChecker stopping condition checker {@link RuleInductionStoppingConditionCheckerWithThreshold} able to handle different values of evaluation threshold
	 * @param pruner rule conditions pruner associated with the stopping condition checker (it will be updated every time stopping condition checker is modified)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ThresholdChangingRuleInducerComponentsProvider(RuleInducerComponents.Builder builder, RuleInductionStoppingConditionCheckerWithThreshold stoppingCondtionChecker, AbstractRuleConditionsPruner pruner) {
		this.builder = notNull(builder, "Provdided rule components builder is null.");
		// set stopping condition checker
		this.builder.ruleInductionStoppingConditionChecker(notNull(stoppingCondtionChecker, "Provided rule induction stopping condition checker is null."));
		
		notNull(pruner, "Provided rule conditions pruner is null.");
		// set pruner
		this.builder.ruleConditionsPruner(pruner.copyWithNewStoppingConditionChecker(this.builder.ruleInductionStoppingConditionChecker()));
		this.updatePruner = true;
	}
	
	private RuleInductionStoppingConditionCheckerWithThreshold getBuilderRuleInductionStoppingConditionChecker() {
		return (RuleInductionStoppingConditionCheckerWithThreshold)this.builder.ruleInductionStoppingConditionChecker(); //cast internally
	}
	
	private AbstractRuleConditionsPruner getBuilderRuleConditionsPruner() {
		return (AbstractRuleConditionsPruner)this.builder.ruleConditionsPruner(); //cast internally
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public RuleInducerComponents provide() {
		return this.builder.build();
	}
	
	/**
	 * Provides rule inducer components {@link RuleInducerComponents} for provided value of evaluation threshold.
	 * 
	 * @param evaluationThreshold value of evaluation threshold
	 * @return rule inducer components {@link RuleInducerComponents}
	 */
	public RuleInducerComponents provide(double evaluationThreshold) {
		this.builder.ruleInductionStoppingConditionChecker(getBuilderRuleInductionStoppingConditionChecker().copyWithNewThreshold(evaluationThreshold)); //update builder's checker first
		if (this.updatePruner) { //pruner depends on checker
			this.builder.ruleConditionsPruner(getBuilderRuleConditionsPruner().copyWithNewStoppingConditionChecker(getBuilderRuleInductionStoppingConditionChecker())); //update builder's pruner too
		}
		return this.builder.build();
	}
	
}
