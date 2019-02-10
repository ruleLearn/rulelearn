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

import org.rulelearn.approximations.Union;
import org.rulelearn.measures.ConsistencyMeasure;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Stores and provides all parameters associated with induction of rules by VC-DomLEM algorithm.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
@Data
@Builder(toBuilder = true)
public class VCDomLEMparameters {	
	@NonNull
	@Builder.Default
	private ConsistencyMeasure<Union> consistencyMeasure = EpsilonConsistencyMeasure.getInstance();
	
	@Builder.Default
	private double consistencyThreshold = 0.0;
 	
	@NonNull
	@Builder.Default
	private RuleConditionsEvaluator ruleConditionsEvaluator = EpsilonConsistencyMeasure.getInstance();
	
	@NonNull
	@Builder.Default
	private MonotonicConditionAdditionEvaluator[] conditionAdditionEvaluators = new MonotonicConditionAdditionEvaluator[] {EpsilonConsistencyMeasure.getInstance()};
	
	@NonNull
	@Builder.Default
	private ConditionRemovalEvaluator[] conditionRemovalEvaluators = new ConditionRemovalEvaluator[] {EpsilonConsistencyMeasure.getInstance()};
	
	@NonNull
	@Builder.Default
	private RuleConditionsEvaluator[] ruleConditionsEvaluators = new RuleConditionsEvaluator[] {EpsilonConsistencyMeasure.getInstance()};
	
	@NonNull
	@Builder.Default
	private ConditionGenerator conditionGenerator = new M4OptimizedConditionGenerator(new MonotonicConditionAdditionEvaluator[] {EpsilonConsistencyMeasure.getInstance()});

	
	@NonNull
	@Builder.Default
	private RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), 0.0);
	
	@Builder.Default
	private ConditionSeparator conditionSeparator = null;
	
	@NonNull
	@Builder.Default
	private AbstractRuleConditionsPruner ruleConditionsPruner = new AttributeOrderRuleConditionsPruner(new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), 0.0));
	
	@NonNull
	@Builder.Default
	private AbstractRuleConditionsPruner ruleConditionsPrunerWithEvaluators = new AbstractRuleConditionsPrunerWithEvaluators(new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), 0.0),
			new ConditionRemovalEvaluator[] {EpsilonConsistencyMeasure.getInstance()}) {
		@Override
		public RuleConditions prune(RuleConditions ruleConditions) {
			return null;
		}
	};
	
	@NonNull
	@Builder.Default
	private RuleConditionsSetPruner ruleConditionsSetPruner = new EvaluationsAndOrderRuleConditionsSetPruner(new RuleConditionsEvaluator[] {EpsilonConsistencyMeasure.getInstance()});
	
	@NonNull
	@Builder.Default
	private RuleMinimalityChecker ruleMinimalityChecker = new SingleEvaluationRuleMinimalityChecker(EpsilonConsistencyMeasure.getInstance());
}
