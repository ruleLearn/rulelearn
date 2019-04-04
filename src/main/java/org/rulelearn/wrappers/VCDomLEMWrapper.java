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
import org.rulelearn.rules.RuleInducerComponents;
import org.rulelearn.rules.RuleInductionStoppingConditionChecker;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.UnionProvider;
import org.rulelearn.rules.UnionWithSingleLimitingDecisionRuleDecisionsProvider;
import org.rulelearn.rules.VCDomLEM;

/**
 * Wraps VC-DomLEM rule induction algorithm. TODO: more information
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class VCDomLEMWrapper implements VariableConsistencyRuleInducerWrapper {
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws InvalidValueException InvalidValueException when informationTable does not contain decision attribute/attributes TODO: verify if this exception can be thrown
	 * @throws NullPointerException if given information table is {@code null}
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
	 * TODO
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws InvalidValueException when informationTable does not contain decision attribute/attributes TODO: verify if this exception can be thrown
	 * @throws NullPointerException if given information table is {@code null}
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

}
