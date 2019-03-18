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
import org.rulelearn.measures.SupportMeasure;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;

/**
 * Stores and provides all parameters associated with induction of certain rules.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CertainTypeRuleInductionParameters extends RuleInductionParameters {
	
	public static final double DEFAULT_CONSISTENCY_TRESHOLD = 0.0;
	
	public static final ConditionAdditionEvaluator[] DEFAULT_CONDITION_ADDITION_EVALUATORS = new MonotonicConditionAdditionEvaluator[] {
			EpsilonConsistencyMeasure.getInstance(), 
			SupportMeasure.getInstance()};
	
	public static final RuleInductionStoppingConditionChecker DEFAULT_STOPPING_CONDITION_CHECKER = 
			new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), DEFAULT_CONSISTENCY_TRESHOLD);
	
	public static final RuleConditionsEvaluator[] DEFAULT_RULE_CONDITIONS_EVALUATORS = new RuleConditionsEvaluator[] {
			SupportMeasure.getInstance(), 
			EpsilonConsistencyMeasure.getInstance()};
	
	public static class Builder implements RuleInductionParameters.Builder {
		
		private ConditionGenerator conditionGenerator = new M4OptimizedConditionGenerator((MonotonicConditionAdditionEvaluator[])DEFAULT_CONDITION_ADDITION_EVALUATORS);

		private RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = DEFAULT_STOPPING_CONDITION_CHECKER;
			
		private ConditionSeparator conditionSeparator = null;
		
		private RuleConditionsPruner ruleConditionsPruner = new AttributeOrderRuleConditionsPruner(DEFAULT_STOPPING_CONDITION_CHECKER);
		
		private RuleConditionsSetPruner ruleConditionsSetPruner = new EvaluationsAndOrderRuleConditionsSetPruner(DEFAULT_RULE_CONDITIONS_EVALUATORS);
		
		private RuleMinimalityChecker ruleMinimalityChecker = new SingleEvaluationRuleMinimalityChecker(EpsilonConsistencyMeasure.getInstance());
		
		private RuleType ruleType = RuleType.CERTAIN;
		
		private AllowedObjectsType allowedObjectsType = AllowedObjectsType.POSITIVE_REGION;
		
		@Override
		public Builder conditionGenerator(ConditionGenerator conditionGenerator) {
			notNull(conditionGenerator, "Provdided condition generator is null.");
			this.conditionGenerator = conditionGenerator;
			return this;
		}
		
		@Override
		public Builder ruleInductionStoppingConditionChecker(RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker) {
			notNull(conditionGenerator, "Provdided rule induction stopping condition checker is null.");
			this.ruleInductionStoppingConditionChecker = ruleInductionStoppingConditionChecker;
			return this;
		}
		
		@Override
		public Builder conditionSeparator(ConditionSeparator conditionSeparator) {
			this.conditionSeparator = conditionSeparator;
			return this;
		}
		@Override
		public Builder ruleConditionsPruner(RuleConditionsPruner ruleConditionsPruner) {
			notNull(conditionGenerator, "Provdided rule conditions pruner is null.");
			this.ruleConditionsPruner = ruleConditionsPruner;
			return this;
		}
		
		@Override
		public Builder ruleConditionsSetPruner(RuleConditionsSetPruner ruleConditionsSetPruner) {
			notNull(conditionGenerator, "Provdided rule condition set pruner is null.");
			this.ruleConditionsSetPruner = ruleConditionsSetPruner;
			return this;
		}
		
		@Override
		public Builder ruleMinimalityChecker(RuleMinimalityChecker ruleMinimalityChecker) {
			notNull(conditionGenerator, "Provdided rule minimality checker is null.");
			this.ruleMinimalityChecker = ruleMinimalityChecker;
			return this;
		}
		
		@Override
		public Builder ruleType(RuleType ruleType) {
			this.ruleType = ruleType;
			return this;
		}
		
		public Builder allowedObjectsType(AllowedObjectsType allowedObjectsType) {
			this.allowedObjectsType = allowedObjectsType;
			return this;
		}
		
		@Override
		public ConditionGenerator conditionGenerator() {
			return this.conditionGenerator;
		}
		
		@Override
		public RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker() {
			return this.ruleInductionStoppingConditionChecker;
		}
		
		@Override
		public ConditionSeparator conditionSeparator() {
			return this.conditionSeparator;
		}
		
		@Override
		public RuleConditionsPruner ruleConditionsPruner() {
			return this.ruleConditionsPruner;
		}
		
		@Override
		public RuleConditionsSetPruner ruleConditionsSetPruner() {
			return this.ruleConditionsSetPruner;
		}
		
		@Override
		public RuleMinimalityChecker ruleMinimalityChecker() {
			return this.ruleMinimalityChecker;
		}
		
		@Override
		public RuleType ruleType() {
			return this.ruleType;
		}
		
		@Override
		public AllowedObjectsType allowedObjectsType() {
			return this.allowedObjectsType;
		}
		
		@Override
		public CertainTypeRuleInductionParameters build() {
			return new CertainTypeRuleInductionParameters(this);
		}
		
	}
	
	protected CertainTypeRuleInductionParameters(Builder builder) {
		super(builder);
	}
	
}
