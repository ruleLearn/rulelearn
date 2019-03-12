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

import java.util.List;

import org.rulelearn.measures.SupportMeasure;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.FieldDefaults;

/**
 * Stores and provides all parameters associated with induction of rules by VC-DomLEM algorithm.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
@Data
@Builder(toBuilder = true)
@FieldDefaults(makeFinal = true)
public class VCDomLEMParameters {
	
	// TODO modify getters of array fields to provide copy of array by default (and reference in a specific getter)
	
	public static final double DEFAULT_CONSISTENCY_TRESHOLD = 0.0;
	public static final ConditionAdditionEvaluator[] DEFAULT_CONDITION_ADDITION_EVALUATORS = new MonotonicConditionAdditionEvaluator[] {
			EpsilonConsistencyMeasure.getInstance(), 
			SupportMeasure.getInstance()};
	public static final RuleConditionsEvaluator[] DEFAULT_RULE_CONDITIONS_EVALUATORS = new RuleConditionsEvaluator[] {
			SupportMeasure.getInstance(), 
			EpsilonConsistencyMeasure.getInstance()};
	public static final RuleInductionStoppingConditionChecker DEFAULT_STOPPING_CONDITION_CHECKER = 
			new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), DEFAULT_CONSISTENCY_TRESHOLD);
	
	@NonNull
	@Singular("consistencyThreshold")
	private List<Double> consistencyThresholds;
 	
	@NonNull
	@Builder.Default
	private ConditionAdditionEvaluator[] conditionAdditionEvaluators = DEFAULT_CONDITION_ADDITION_EVALUATORS;
	
	@NonNull
	@Builder.Default
	private ConditionRemovalEvaluator[] conditionRemovalEvaluators = new ConditionRemovalEvaluator[] {EpsilonConsistencyMeasure.getInstance()}; //not used in default rule conditions pruner
	
	@NonNull
	@Builder.Default
	private RuleConditionsEvaluator[] ruleConditionsEvaluators = DEFAULT_RULE_CONDITIONS_EVALUATORS;
	
	@NonNull
	@Builder.Default
	private ConditionGenerator conditionGenerator = new M4OptimizedConditionGenerator((MonotonicConditionAdditionEvaluator[])DEFAULT_CONDITION_ADDITION_EVALUATORS);

	@NonNull
	@Builder.Default
	private RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = DEFAULT_STOPPING_CONDITION_CHECKER;
		
	@Builder.Default
	private ConditionSeparator conditionSeparator = null;
	
	@NonNull
	@Builder.Default
	private RuleConditionsPruner ruleConditionsPruner = new AttributeOrderRuleConditionsPruner(DEFAULT_STOPPING_CONDITION_CHECKER);
	
	@NonNull
	@Builder.Default
	private RuleConditionsSetPruner ruleConditionsSetPruner = new EvaluationsAndOrderRuleConditionsSetPruner(DEFAULT_RULE_CONDITIONS_EVALUATORS);
	
	@NonNull
	@Builder.Default
	private RuleMinimalityChecker ruleMinimalityChecker = new SingleEvaluationRuleMinimalityChecker(EpsilonConsistencyMeasure.getInstance());
	
	@NonNull
	@Builder.Default
	RuleType ruleType = RuleType.CERTAIN; //certain/possible
	
	@NonNull
	@Builder.Default
	AllowedNegativeObjectsType allowedNegativeObjectsType = AllowedNegativeObjectsType.POSITIVE_REGION;
}
