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
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.rules.ApproximatedSetProvider;
import org.rulelearn.rules.ApproximatedSetRuleDecisionsProvider;
import org.rulelearn.rules.PossibleRuleInducerComponents;
import org.rulelearn.rules.RuleInducerComponents;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithComputableCharacteristics;
import org.rulelearn.rules.UnionProvider;
import org.rulelearn.rules.UnionWithSingleLimitingDecisionRuleDecisionsProvider;
import org.rulelearn.rules.VCDomLEM;

/**
 * Wraps {@link VCDomLEM VC-DomLEM rule induction algorithm} and allows to induce possible decision rules.<br>
 * <br>
 * For a given information table, induces possible decision rules (more precisely, both types of possible decision rules: "at least" and "at most").
 * These decision rules are induced according to the Dominance-based Rough Set Approach (DRSA). 
 * 
 * This wrapper induces rules using default {@link PossibleRuleInducerComponents possible rule inducer components}. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class PossibleVCDomLEMWrapper implements RuleInducerWrapper {

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
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper inducing possible decision rules is null.");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().build();
		
		Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable, true), 
				new ClassicalDominanceBasedRoughSetCalculator());
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
		Precondition.notNull(informationTable, "Information table for VC-DomLEM wrapper inducing possible decision rules is null.");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().build();
		
		Unions unions = new UnionsWithSingleLimitingDecision(new InformationTableWithDecisionDistributions(informationTable, true), 
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

}
