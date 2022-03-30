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

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;

/**
 * Set of decision rules and their complete characteristics (pre-stored or computed on demand - see {@link ComputableRuleCharacteristics}),
 * each identified by rule's index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleSetWithComputableCharacteristics extends RuleSetWithCharacteristics {
	
	/**
	 * Array with rule coverage information for each decision rule stored in this rule set.
	 */
	RuleCoverageInformation[] ruleCoverageInformationArray;
	
	/**
	 * Private constructor used to compose rule set with computable characteristics from components.
	 * Assumes all parameters are correct. Used by {@link #join(RuleSetWithComputableCharacteristics, RuleSetWithComputableCharacteristics)}
	 * and {@link #filter(RuleSetWithComputableCharacteristics, RuleFilter).
	 * 
	 * @param rules rules to be set in constructed rule set
	 * @param ruleCoverageInformationArray {@link RuleCoverageInformation rule coverage information} array to be set in constructed rule set
	 * @param computableRuleCharacteristicsArray array of {@link ComputableRuleCharacteristics computable rule characteristics} to be set in constructed rule set;
	 *        any element can be {@code null}
	 */
	private RuleSetWithComputableCharacteristics(Rule[] rules, RuleCoverageInformation[] ruleCoverageInformationArray, ComputableRuleCharacteristics[] computableRuleCharacteristicsArray) {
		super(rules, true);
		this.ruleCoverageInformationArray = ruleCoverageInformationArray;
		this.ruleCharacteristics = computableRuleCharacteristicsArray;
	}
	
	/**
	 * Constructor storing given rules and rule coverage information for each rule (given arrays get cloned).
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param ruleCoverageInformationArray array with rule coverage information for each rule
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleSetWithComputableCharacteristics(Rule[] rules, RuleCoverageInformation[] ruleCoverageInformationArray) {
		this(rules, ruleCoverageInformationArray, false);
	}
	
	/**
	 * Constructor storing given rules and rule coverage information for each rule.
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param ruleCoverageInformationArray array with rule coverage information for each rule
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that given references
	 *        to an array of rules and to an array with rule coverage information for each rule are not going to be used outside this class
	 *        to modify these arrays (and thus, this object does not need to clone the arrays for internal read-only use)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if length of given array with decision rules is different than length of given array with rule coverage information for each rule
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public RuleSetWithComputableCharacteristics(Rule[] rules, RuleCoverageInformation[] ruleCoverageInformationArray, boolean accelerateByReadOnlyParams) {
		super(rules, accelerateByReadOnlyParams);
		
		this.ruleCharacteristics = new ComputableRuleCharacteristics[rules.length];
		
		notNull(ruleCoverageInformationArray, "Array with rule coverage infos is null.");
		
		this.ruleCoverageInformationArray = accelerateByReadOnlyParams ? ruleCoverageInformationArray : ruleCoverageInformationArray.clone();
		
		if (rules.length != ruleCoverageInformationArray.length) {
			throw new InvalidSizeException("Number of rules and rule coverage infos are different.");
		}
	}
	
	/**
	 * Gets computable characteristics of a decision rule with given index.
	 * 
	 * @param ruleIndex {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws IndexOutOfBoundsException if given index does not match any rule characteristics in this rule set
	 */
	@Override
	public ComputableRuleCharacteristics getRuleCharacteristics(int ruleIndex) {
		if (this.ruleCharacteristics[ruleIndex] == null) {
			this.ruleCharacteristics[ruleIndex] = new ComputableRuleCharacteristics(this.ruleCoverageInformationArray[ruleIndex]);
		}
		
		return (ComputableRuleCharacteristics)this.ruleCharacteristics[ruleIndex];
	}
	
	/**
	 * Ensures that all values of all rule characteristics are calculated.
	 */
	public void calculateAllCharacteristics() {
		for (int i = 0; i < rules.length; i++) {
			getRuleCharacteristics(i).calculateAllCharacteristics();
		}
	}
	
	/**
	 * Gets a new rule set with computable characteristics by joining given two rule sets (with computable rule characteristics).
	 * The resulting rule set contains rules in the order of given rule sets, i.e., first the rules from the first rule set,
	 * then the rules from the second rule set. 
	 * 
	 * @param ruleSet1 first rule set (with computable rule characteristics) to join
	 * @param ruleSet2 second rule set (with computable rule characteristics) to join
	 * @return a new rule set (with computable rule characteristics) composing of rules from the two given rule sets (with computable rule characteristics)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public static RuleSetWithComputableCharacteristics join(RuleSetWithComputableCharacteristics ruleSet1, RuleSetWithComputableCharacteristics ruleSet2) {
		Precondition.notNull(ruleSet1, "First rule set to join is null.");
		Precondition.notNull(ruleSet2, "Second rule set to join is null.");
		
		int size1 = ruleSet1.size();
		int size2 = ruleSet2.size();
		
		//join rules
		Rule[] rules = new Rule[size1 + size2];
		for (int i = 0; i < size1; i++) {
			rules[i] = ruleSet1.getRule(i);
		}
		for (int i = 0; i < size2; i++) {
			rules[size1 + i] = ruleSet2.getRule(i);
		}
		
		//join rule coverage informations and computable rule characteristics
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[size1 + size2];
		ComputableRuleCharacteristics[] computableRuleCharacteristics = new ComputableRuleCharacteristics[size1 + size2];
		
		for (int i = 0; i < size1; i++) {
			ruleCoverageInformationArray[i] = ruleSet1.ruleCoverageInformationArray[i];
			computableRuleCharacteristics[i] = (ComputableRuleCharacteristics)ruleSet1.ruleCharacteristics[i]; //this cast is safe
		}
		for (int i = 0; i < size2; i++) {
			ruleCoverageInformationArray[size1 + i] = ruleSet2.ruleCoverageInformationArray[i];
			computableRuleCharacteristics[size1 + i] = (ComputableRuleCharacteristics)ruleSet2.ruleCharacteristics[i]; //this cast is safe
		}
		
		return new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, computableRuleCharacteristics);
	}
	
	/**
	 * Filters given set of rules with characteristics, and returns a subset of rules, composed of rules that are accepted by the given rule filter.
	 * 
	 * @param ruleSet rule set to filter
	 * @param ruleFilter rule filter used to test each rule from the given rule set
	 * 
	 * @return a new rule set (with characteristics) composing of rules accepted by the given filter
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public static RuleSetWithComputableCharacteristics filter(RuleSetWithComputableCharacteristics ruleSet, RuleFilter ruleFilter) {
		Precondition.notNull(ruleSet, "Rule set to be filtered is null.");
		Precondition.notNull(ruleFilter, "Rule filter is null.");
		
		int ruleSetSize = ruleSet.size();
		byte[] accepted = new byte[ruleSetSize];
		int acceptedCount = 0;
		
		for (int i = 0; i < ruleSetSize; i++) {
			if (ruleFilter.accepts(ruleSet.getRule(i), ruleSet.getRuleCharacteristics(i))) {
				acceptedCount++;
				accepted[i] = 1;
			} else {
				accepted[i] = 0;
			}
		}
		
		Rule[] rules = new Rule[acceptedCount];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[acceptedCount];
		ComputableRuleCharacteristics[] computableRuleCharacteristics = new ComputableRuleCharacteristics[acceptedCount];
		
		int newAcceptedRuleIndex = 0;
		
		for (int i = 0; i < ruleSetSize; i++) {
			if (accepted[i] == 1) {
				rules[newAcceptedRuleIndex] = ruleSet.getRule(i);
				ruleCoverageInformationArray[newAcceptedRuleIndex] = ruleSet.ruleCoverageInformationArray[i];
				computableRuleCharacteristics[newAcceptedRuleIndex] = (ComputableRuleCharacteristics)ruleSet.ruleCharacteristics[i]; //this cast is safe
				newAcceptedRuleIndex++;
			}
		}
		
		return new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, computableRuleCharacteristics);
	}
	
}
