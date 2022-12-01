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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.rulelearn.rules.OptimizingRuleConditionsGeneralizer;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleInducerComponents;
import org.rulelearn.rules.RuleInductionStoppingConditionChecker;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithComputableCharacteristics;
import org.rulelearn.rules.UnionProvider;
import org.rulelearn.rules.UnionWithSingleLimitingDecisionRuleDecisionsProvider;
import org.rulelearn.rules.VCDomLEM;

/**
 * Wraps {@link VCDomLEM VC-DomLEM rule induction algorithm} and allows to induce certain decision rules.<br>
 * <br>
 * For a given information table, induces certain decision rules (more precisely, both types of decision rules: "at least" and "at most").
 * These certain decision rules are induced according to the Dominance-based Rough Set Approach (DRSA), 
 * or according to the Variable Consistency Dominance-based Rough Set Approach (VC-DRSA). 
 * In the former case, this wrapper induces rules using default {@link CertainRuleInducerComponents certain rule inducer components}. 
 * In the latter case, it employs:
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
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper inducing certain decision rules is null.");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		
		InformationTableWithDecisionDistributions informationTableWithDecisionDistributions = (informationTable instanceof InformationTableWithDecisionDistributions ?
				(InformationTableWithDecisionDistributions)informationTable : new InformationTableWithDecisionDistributions(informationTable, true));
		
		Unions unions = new UnionsWithSingleLimitingDecision(informationTableWithDecisionDistributions, new ClassicalDominanceBasedRoughSetCalculator());
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		List<VCDomLEM> vcDomLEMs = new ArrayList<VCDomLEM>(2);
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider));
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider));
		
		//calculate rules and their characteristics in parallel
		List<RuleSet> ruleSets = vcDomLEMs.parallelStream().map(vcDomLem -> vcDomLem.generateRules()).collect(Collectors.toList());
		
		return RuleSet.join(ruleSets.get(0), ruleSets.get(1));
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
	public RuleSetWithComputableCharacteristics induceRulesWithCharacteristics(InformationTable informationTable) {
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper inducing certain decision rules is null.");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		
		InformationTableWithDecisionDistributions informationTableWithDecisionDistributions = (informationTable instanceof InformationTableWithDecisionDistributions ?
				(InformationTableWithDecisionDistributions)informationTable : new InformationTableWithDecisionDistributions(informationTable, true));
		
		Unions unions = new UnionsWithSingleLimitingDecision(informationTableWithDecisionDistributions,
				new ClassicalDominanceBasedRoughSetCalculator());
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		List<VCDomLEM> vcDomLEMs = new ArrayList<VCDomLEM>(2);
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider));
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider));
		
		//calculate rules and their characteristics in parallel
		List<RuleSetWithComputableCharacteristics> ruleSets = vcDomLEMs.parallelStream().map(vcDomLem -> vcDomLem.generateRules()).collect(Collectors.toList());
		ruleSets.parallelStream().forEach(ruleSet -> ruleSet.calculateAllCharacteristics());
		
		return RuleSetWithComputableCharacteristics.join(ruleSets.get(0), ruleSets.get(1));
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
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(),
						EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				ruleConditionsGeneralizer(new OptimizingRuleConditionsGeneralizer(stoppingConditionChecker)).
				build();
		
		InformationTableWithDecisionDistributions informationTableWithDecisionDistributions = (informationTable instanceof InformationTableWithDecisionDistributions ?
				(InformationTableWithDecisionDistributions)informationTable : new InformationTableWithDecisionDistributions(informationTable, true));
		
		Unions unions = new UnionsWithSingleLimitingDecision(informationTableWithDecisionDistributions, 
								   new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold));
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		List<VCDomLEM> vcDomLEMs = new ArrayList<VCDomLEM>(2);
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider));
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider));
		
		//calculate rules and their characteristics in parallel
		List<RuleSet> ruleSets = vcDomLEMs.parallelStream().map(vcDomLem -> vcDomLem.generateRules()).collect(Collectors.toList());
		
		return RuleSet.join(ruleSets.get(0), ruleSets.get(1));
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
	public RuleSetWithComputableCharacteristics induceRulesWithCharacteristics(InformationTable informationTable, double consistencyThreshold) {
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper employing consistency threshold is null.");
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(),
						EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				ruleConditionsGeneralizer(new OptimizingRuleConditionsGeneralizer(stoppingConditionChecker)).
				build();
		
		InformationTableWithDecisionDistributions informationTableWithDecisionDistributions = (informationTable instanceof InformationTableWithDecisionDistributions ?
				(InformationTableWithDecisionDistributions)informationTable : new InformationTableWithDecisionDistributions(informationTable, true));
		
		Unions unions = new UnionsWithSingleLimitingDecision(informationTableWithDecisionDistributions, 
								   new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold));
		ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
		ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
		ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		List<VCDomLEM> vcDomLEMs = new ArrayList<VCDomLEM>(2);
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtLeastProvider, unionRuleDecisionsProvider));
		vcDomLEMs.add(new VCDomLEM(ruleInducerComponents, unionAtMostProvider, unionRuleDecisionsProvider));
		
		//calculate rules and their characteristics in parallel
		List<RuleSetWithComputableCharacteristics> ruleSets = vcDomLEMs.parallelStream().map(vcDomLem -> vcDomLem.generateRules()).collect(Collectors.toList());
		ruleSets.parallelStream().forEach(ruleSet -> ruleSet.calculateAllCharacteristics());
		
		return RuleSetWithComputableCharacteristics.join(ruleSets.get(0), ruleSets.get(1));
	}

}
