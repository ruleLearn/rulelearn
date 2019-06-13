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

import org.rulelearn.measures.CoverageInApproximationMeasure;
import org.rulelearn.measures.RelativeCoverageOutsideApproximationMeasure;

/**
 * Stores and provides components and parameters associated with induction of possible decision rules with sequential covering algorithms.<br>
 * 
 * Particularly suited to induction of sets of possible decision rules by VC-DomLEM sequential rule induction algorithm described in:
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches.
 * Information Sciences, 181, 2011, pp. 987-1002.<br>
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class PossibleRuleInducerComponents extends RuleInducerComponents {
	
//	/**
//	 * Default consistency threshold for epsilon consistency measure {@link EpsilonConsistencyMeasure}.
//	 */
//	public static final double DEFAULT_COVERAGE_TRESHOLD = 0.0;
	
	/**
	 * Default evaluator of rule conditions.
	 */
	public static final RuleConditionsEvaluator[] DEFAULT_RULE_CONDITIONS_EVALUATORS = new RuleConditionsEvaluator[] {CoverageInApproximationMeasure.getInstance(), 
			RelativeCoverageOutsideApproximationMeasure.getInstance()};
	
	/**
	 * Default evaluator of a condition while it may added to an induced rule.
	 */
	public static final ConditionAdditionEvaluator[] DEFAULT_CONDITION_ADDITION_EVALUATORS = new MonotonicConditionAdditionEvaluator[] {
			RelativeCoverageOutsideApproximationMeasure.getInstance(), 
			CoverageInApproximationMeasure.getInstance()};
	
	/**
	 * Default checker used to determine occurrence of stopping condition of rule induction process {@link RuleInductionStoppingConditionChecker}.
	 */
	public static final RuleInductionStoppingConditionChecker DEFAULT_STOPPING_CONDITION_CHECKER = 
			new EvaluationAndCoverageStoppingConditionChecker(RelativeCoverageOutsideApproximationMeasure.getInstance(), RelativeCoverageOutsideApproximationMeasure.getInstance(), 0.0);
	
	/**
	 * Default rule conditions pruner.
	 */
	public static final RuleConditionsPruner DEFAULT_RULE_CONDITIONS_PRUNER = new AttributeOrderRuleConditionsPruner(DEFAULT_STOPPING_CONDITION_CHECKER);
	
	/**
	 * Contract of a builder of possible rule inducer components {@link PossibleRuleInducerComponents}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class Builder implements RuleInducerComponents.Builder {
		
		private ConditionGenerator conditionGenerator = new M4OptimizedConditionGenerator((MonotonicConditionAdditionEvaluator[])DEFAULT_CONDITION_ADDITION_EVALUATORS);

		private RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = DEFAULT_STOPPING_CONDITION_CHECKER;
			
		private ConditionSeparator conditionSeparator = null;
		
		private RuleConditionsPruner ruleConditionsPruner = DEFAULT_RULE_CONDITIONS_PRUNER;
		
		private RuleConditionsSetPruner ruleConditionsSetPruner = new EvaluationsAndOrderRuleConditionsSetPruner(DEFAULT_RULE_CONDITIONS_EVALUATORS);
		
		private RuleMinimalityChecker ruleMinimalityChecker = new SingleEvaluationRuleMinimalityChecker(RelativeCoverageOutsideApproximationMeasure.getInstance());
		
		private RuleType ruleType = RuleType.POSSIBLE;
		
		private AllowedNegativeObjectsType allowedNegativeObjectsType = AllowedNegativeObjectsType.APPROXIMATION;
		
		/**
		 * {@inheritDoc}
		 * 
		 * @param conditionGenerator {@inheritDoc}
		 * @return {@inheritDoc}
		 * @throws NullPointerException if given parameter is {@code null}
		 */
		@Override
		public Builder conditionGenerator(ConditionGenerator conditionGenerator) {
			this.conditionGenerator = notNull(conditionGenerator, "Provided condition generator is null.");
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @param ruleInductionStoppingConditionChecker {@inheritDoc}
		 * @return {@inheritDoc}
		 * @throws NullPointerException if given parameter is {@code null}
		 */
		@Override
		public Builder ruleInductionStoppingConditionChecker(RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker) {
			this.ruleInductionStoppingConditionChecker = notNull(ruleInductionStoppingConditionChecker, "Provided rule induction stopping condition checker is null.");
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @param conditionSeparator {@inheritDoc}
		 * @return {@inheritDoc}
		 */
		@Override
		public Builder conditionSeparator(ConditionSeparator conditionSeparator) {
			this.conditionSeparator = conditionSeparator;
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @param ruleConditionsPruner {@inheritDoc}
		 * @return {@inheritDoc}
		 * @throws NullPointerException if given parameter is {@code null}
		 */
		@Override
		public Builder ruleConditionsPruner(RuleConditionsPruner ruleConditionsPruner) {
			this.ruleConditionsPruner = notNull(ruleConditionsPruner, "Provided rule conditions pruner is null.");
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @param ruleConditionsSetPruner {@inheritDoc}
		 * @return {@inheritDoc}
		 * @throws NullPointerException if given parameter is {@code null}
		 */
		@Override
		public Builder ruleConditionsSetPruner(RuleConditionsSetPruner ruleConditionsSetPruner) {
			this.ruleConditionsSetPruner = notNull(ruleConditionsSetPruner, "Provided rule condition set pruner is null.");
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @param ruleMinimalityChecker {@inheritDoc}
		 * @return {@inheritDoc}
		 * @throws NullPointerException if given parameter is {@code null}
		 */
		@Override
		public Builder ruleMinimalityChecker(RuleMinimalityChecker ruleMinimalityChecker) {
			this.ruleMinimalityChecker = notNull(ruleMinimalityChecker, "Provided rule minimality checker is null.");
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @param ruleType {@inheritDoc}
		 * @return {@inheritDoc}
		 */
		@Override
		public Builder ruleType(RuleType ruleType) {
			this.ruleType = notNull(ruleType, "Provided rule type is null.");
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public ConditionGenerator conditionGenerator() {
			return this.conditionGenerator;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker() {
			return this.ruleInductionStoppingConditionChecker;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public ConditionSeparator conditionSeparator() {
			return this.conditionSeparator;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public RuleConditionsPruner ruleConditionsPruner() {
			return this.ruleConditionsPruner;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public RuleConditionsSetPruner ruleConditionsSetPruner() {
			return this.ruleConditionsSetPruner;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public RuleMinimalityChecker ruleMinimalityChecker() {
			return this.ruleMinimalityChecker;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public RuleType ruleType() {
			return this.ruleType;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public AllowedNegativeObjectsType allowedNegativeObjectsType() {
			return this.allowedNegativeObjectsType;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public PossibleRuleInducerComponents build() {
			return new PossibleRuleInducerComponents(this);
		}
		
	}
	
	/**
	 * Constructor setting all rule inducer components according to provided builder {@link Builder}.
	 * 
	 * @param builder builder of rule induction components
	 */
	protected PossibleRuleInducerComponents(Builder builder) {
		super(builder);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public RuleInducerComponents.Builder builder() {
		PossibleRuleInducerComponents.Builder builder = new PossibleRuleInducerComponents.Builder();
		return builder.conditionGenerator(this.getConditionGenerator()).
				conditionSeparator(this.getConditionSeparator()).ruleConditionsPruner(this.getRuleConditionsPruner()).
				ruleConditionsSetPruner(this.getRuleConditionsSetPruner()).ruleInductionStoppingConditionChecker(this.getRuleInductionStoppingConditionChecker()).
				ruleMinimalityChecker(this.getRuleMinimalityChecker()).ruleType(this.getRuleType());
	}
	
}
