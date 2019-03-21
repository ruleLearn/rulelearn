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
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;
import org.rulelearn.rules.ApproximatedSetProvider;
import org.rulelearn.rules.ApproximatedSetRuleDecisionsProvider;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.UnionProvider;
import org.rulelearn.rules.UnionWithSingleLimitingDecisionRuleDecisionsProvider;
import org.rulelearn.rules.VCDomLEM;
import org.rulelearn.rules.VCDomLEMParameters;

/**
 * Wraps VC-DomLEM rule induction algorithm.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class VCDomLEMWrapper implements VariableConsistencyRuleInducerWrapper {
	
	/**
	 * Default consistency threshold for DRSA rules when {@link EpsilonConsistencyMeasure} is used
	 */
	public static final double DEFAULT_CONSISTENCY_TRESHOLD = 0.0;
	
	/**
	 * Induced set of rules.
	 */
	RuleSet rules = null;

	/**
	 * Consistency threshold for which rules were induced (provided that they were already).
	 */
	double consistencyThreshold  = 0.0;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws InvalidValueException InvalidValueException when informationTable does not contain decision attribute/attributes
	 */
	@Override
	public RuleSet induceRules(InformationTable informationTable) {
		// rules were not already computed for the most restrictive value of consistency threshold
		if ((this.rules == null) || (this.consistencyThreshold != DEFAULT_CONSISTENCY_TRESHOLD)) {
			VCDomLEMParameters vcDomLEMParameters = VCDomLEMParameters.builder().build();
			Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable), new ClassicalDominanceBasedRoughSetCalculator());
			ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
			ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
			ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
			
			RuleSet upwardRules = (new VCDomLEM(vcDomLEMParameters)).generateRules(unionAtLeastProvider, unionRuleDecisionsProvider, VCDomLEMParameters.DEFAULT_CONSISTENCY_TRESHOLD);
			RuleSet downwardRules = (new VCDomLEM(vcDomLEMParameters)).generateRules(unionAtMostProvider, unionRuleDecisionsProvider, VCDomLEMParameters.DEFAULT_CONSISTENCY_TRESHOLD);
			
			this.rules = RuleSet.join(upwardRules, downwardRules);
			this.consistencyThreshold = 0.0;
		}
		
		return this.rules;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws InvalidValueException when informationTable does not contain decision attribute/attributes
	 */
	@Override
	public RuleSet induceRules(InformationTable informationTable, double consistencyThreshold) {
		// rules were not already computed for the provided value of consistency threshold
		if ((this.rules == null) || (this.consistencyThreshold != consistencyThreshold)) {
			VCDomLEMParameters vcDomLEMParameters = VCDomLEMParameters.builder().consistencyThreshold(consistencyThreshold).build();
			Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable), 
									   new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold));
			ApproximatedSetProvider unionAtLeastProvider = new UnionProvider(Union.UnionType.AT_LEAST, unions);
			ApproximatedSetProvider unionAtMostProvider = new UnionProvider(Union.UnionType.AT_MOST, unions);
			ApproximatedSetRuleDecisionsProvider unionRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
			
			RuleSet upwardRules = (new VCDomLEM(vcDomLEMParameters)).generateRules(unionAtLeastProvider, unionRuleDecisionsProvider, consistencyThreshold);
			RuleSet downwardRules = (new VCDomLEM(vcDomLEMParameters)).generateRules(unionAtMostProvider, unionRuleDecisionsProvider, consistencyThreshold);
			
			this.rules = RuleSet.join(upwardRules, downwardRules);
			this.consistencyThreshold = consistencyThreshold;
		}
		
		return this.rules;
	}

}
