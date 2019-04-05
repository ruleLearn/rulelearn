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

package org.rulelearn.wrappers;

import org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Unions;
import org.rulelearn.approximations.UnionsWithSingleLimitingDecision;
import org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;
import org.rulelearn.rules.ApproximatedSetProvider;
import org.rulelearn.rules.ApproximatedSetRuleDecisionsProvider;
import org.rulelearn.rules.AttributeOrderRuleConditionsPruner;
import org.rulelearn.rules.CertainRuleInducerComponents;
import org.rulelearn.rules.EvaluationAndCoverageStoppingConditionChecker;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleInducerComponents;
import org.rulelearn.rules.RuleInductionStoppingConditionChecker;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.RuleSetWithComputableCharacteristics;
import org.rulelearn.rules.UnionProvider;
import org.rulelearn.rules.UnionWithSingleLimitingDecisionRuleDecisionsProvider;
import org.rulelearn.rules.VCDomLEM;

/**
 * Wraps {@link VCDomLEM VC-DomLEM rule induction algorithm}.<br>
 * <br>
 * For a given information table, induces only certain decision rules (both "at least" and "at most" ones), according to the Dominance-based Rough Set Approach (DRSA),
 * or according to the Variable Consistency Dominance-based Rough Set Approach (VC-DRSA). In the former case, induces rules using default {@link CertainRuleInducerComponents
 * certain rule inducer components}. In the latter case, employs:
 * <ul>
 * <li>{@link EpsilonConsistencyMeasure epsilon consistency measure}, both for calculating lower approximations
 * of {@link UnionsWithSingleLimitingDecision unions of ordered decision classes} and for evaluating {@link RuleConditions rule conditions}
 * while they are being constructed by VC-DomLEM algorithm,</li>
 * <li>single consistency threshold, both for calculating lower approximations of all upward/downward unions of decision classes
 * and for deciding whether {@link RuleConditions rule conditions} can be accepted as a left hand side of a decision rule.</li>
 * </ul>
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class VCDomLEMWrapper implements VariableConsistencyRuleInducerWrapper {
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param informationTable {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws InvalidValueException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public RuleSet induceRules(InformationTable informationTable) {
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper is null.");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		
		Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable), new ClassicalDominanceBasedRoughSetCalculator());
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet upwardRules = (new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider)).generateRules();
		RuleSet downwardRules = (new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider)).generateRules();
		
		return RuleSet.join(upwardRules, downwardRules);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws InvalidValueException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public RuleSetWithCharacteristics induceRulesWithCharacteristics(InformationTable informationTable) {
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper is null.");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		
		Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable), new ClassicalDominanceBasedRoughSetCalculator());
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSetWithComputableCharacteristics upwardRules = (new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider)).generateRules();
		upwardRules.calculateAllCharacteristics();
		RuleSetWithComputableCharacteristics downwardRules = (new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider)).generateRules();
		downwardRules.calculateAllCharacteristics();
		
		return RuleSetWithCharacteristics.join(upwardRules, downwardRules);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param informationTable {@inheritDoc}
	 * @param consistencyThreshold {@inheritDoc} 
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws InvalidValueException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public RuleSet induceRules(InformationTable informationTable, double consistencyThreshold) {
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper employing consistency threshold is null.");
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				build();
		
		Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable), 
								   new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold));
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet upwardRules = (new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider)).generateRules();
		RuleSet downwardRules = (new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider)).generateRules();
		
		return RuleSet.join(upwardRules, downwardRules);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param informationTable {@inheritDoc}
	 * @param consistencyThreshold {@inheritDoc} 
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws InvalidValueException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public RuleSetWithCharacteristics induceRulesWithCharacteristics(InformationTable informationTable, double consistencyThreshold) {
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper employing consistency threshold is null.");
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				build();
		
		Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable), 
								   new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold));
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSetWithComputableCharacteristics upwardRules = (new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider)).generateRules();
		upwardRules.calculateAllCharacteristics();
		RuleSetWithComputableCharacteristics downwardRules = (new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider)).generateRules();
		downwardRules.calculateAllCharacteristics();
		
		return RuleSetWithCharacteristics.join(upwardRules, downwardRules);
	}

}
