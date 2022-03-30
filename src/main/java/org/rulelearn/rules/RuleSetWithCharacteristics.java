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
import org.rulelearn.data.InformationTable;

/**
 * Set of decision rules and their characteristics, each identified by rule's index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleSetWithCharacteristics extends RuleSet {
	
	/**
	 * Array with characteristics of subsequent rules stored in this rule set.
	 */
	RuleCharacteristics[] ruleCharacteristics;
	
	/**
	 * Constructor storing rules from the given array and their characteristics in this rule set. 
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param ruleCharacteristics characteristics of the rules to store in this rule set
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if the number of rules and their characteristics is different
	 */
	public RuleSetWithCharacteristics(Rule[] rules, RuleCharacteristics[] ruleCharacteristics) {
		this(rules, ruleCharacteristics, false);
	}
	
	/**
	 * Constructor storing rules from the given array and their characteristics in this rule set. 
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param ruleCharacteristics characteristics of the rules to store in this rule set
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given references
	 *        to an array of rules and to an array of rule characteristics are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal read-only use)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if the number of rules and their characteristics is different
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public RuleSetWithCharacteristics(Rule[] rules, RuleCharacteristics[] ruleCharacteristics, boolean accelerateByReadOnlyParams) {
		super(rules, accelerateByReadOnlyParams); //assert that rules != null
		notNull(ruleCharacteristics, "Rule characteristics to be stored in a rule set are null."); //assert that ruleCharacteristics != null
		if (rules.length != ruleCharacteristics.length) {
			throw new InvalidSizeException("The number of rules and their characteristics is different.");
		}
		this.ruleCharacteristics = accelerateByReadOnlyParams ? ruleCharacteristics : ruleCharacteristics.clone();
	}
	
	/**
	 * Constructor only storing rules from the given array, to be used by subclass constructors.
	 * 
	 * @param rules decision rules to store in this rule set
	 * @throws NullPointerException if given array of rules is {@code null}
	 */
	protected RuleSetWithCharacteristics(Rule[] rules) {
		this(rules, false);
	}
	
	/**
	 * Constructor only storing rules from the given array, to be used by subclass constructors.
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given references
	 *        to an array of rules and to an array of rule characteristics are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal read-only use)
	 * 
	 * @throws NullPointerException if given array of rules is {@code null}
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	protected RuleSetWithCharacteristics(Rule[] rules, boolean accelerateByReadOnlyParams) {
		super(rules, accelerateByReadOnlyParams);
	}
	
	/**
	 * Gets characteristics of a decision rule with given index.
	 * 
	 * @param ruleIndex index of a rule for which characteristics should be returned
	 * @return characteristics of the rule from this set having given index
	 * 
	 * @throws IndexOutOfBoundsException if given index does not match any rule characteristics in this rule set
	 */
	public RuleCharacteristics getRuleCharacteristics(int ruleIndex) {
		return this.ruleCharacteristics[ruleIndex];
	}
	
	/**
	 * Gets a new rule set with characteristics by joining given two rule sets (with characteristics).
	 * 
	 * @param ruleSet1 first rule set (with characteristics) to join
	 * @param ruleSet2 second rule set (with characteristics) to join
	 * @return a new rule set (with characteristics) composing of rules from the two given rule sets
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public static RuleSetWithCharacteristics join(RuleSetWithCharacteristics ruleSet1, RuleSetWithCharacteristics ruleSet2) {
		Precondition.notNull(ruleSet1, "First rule set to join is null.");
		Precondition.notNull(ruleSet2, "Second rule set to join is null.");
		
		// join rules
		Rule[] rules = new Rule[ruleSet1.size() + ruleSet2.size()];
		for (int i = 0; i < ruleSet1.size(); i++) {
			rules[i] = ruleSet1.getRule(i);
		}
		int shift = ruleSet1.size();
		for (int i = 0; i < ruleSet2.size(); i++) {
			rules[shift + i] = ruleSet2.getRule(i);
		}
		
		// join characteristics
		RuleCharacteristics[] characteristics = new RuleCharacteristics[ruleSet1.size() + ruleSet2.size()];
		for (int i = 0; i < ruleSet1.size(); i++) {
			characteristics[i] = ruleSet1.getRuleCharacteristics(i);
		}
		shift = ruleSet1.size();
		for (int i = 0; i < ruleSet2.size(); i++) {
			characteristics[shift + i] = ruleSet2.getRuleCharacteristics(i);
		}
		
		return new RuleSetWithCharacteristics(rules, characteristics, true);
	}
	
	/**
	 * Filters given set of rules with characteristics, and returns a subset of rules, composed of rules that are accepted by the given rule filter.
	 * Calls {@link #filter(RuleFilter)} on the given rule set, passing given rule filter as a parameter.
	 * 
	 * @param ruleSet rule set to filter
	 * @param ruleFilter rule filter used to test each rule from the given rule set
	 * 
	 * @return a new rule set (with characteristics) composed of rules accepted by the given filter
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public static RuleSetWithCharacteristics filter(RuleSetWithCharacteristics ruleSet, RuleFilter ruleFilter) {
		Precondition.notNull(ruleSet, "Rule set to be filtered is null.");
		
		return ruleSet.filter(ruleFilter);
	}
	
	/**
	 * Browses this rule set using given rule filter and transfers accepted rules to the returned new rule set.<br>
	 * <br>
	 * If rule filter is an instance of {@link AcceptingRuleFilter}, then just returns this rule set.
	 * 
	 * @param ruleFilter rule filter used to test each rule from this rule set
	 * @return filtered set of rules (new object, or the same object if rule filter is an instance of {@link AcceptingRuleFilter})
	 * 
	 * @throws NullPointerException if given rule filter is {@code null}
	 */
	public RuleSetWithCharacteristics filter(RuleFilter ruleFilter) {
		Precondition.notNull(ruleFilter, "Rule filter is null.");
		
		if (ruleFilter instanceof AcceptingRuleFilter) {
			return this; //optimization for the default case (all induced rules remain)
		}
		
		int ruleSetSize = size();
		byte[] accepted = new byte[ruleSetSize];
		int acceptedCount = 0;
		
		for (int i = 0; i < ruleSetSize; i++) {
			if (ruleFilter.accepts(getRule(i), getRuleCharacteristics(i))) {
				acceptedCount++;
				accepted[i] = 1;
			} else {
				accepted[i] = 0;
			}
		}
		
		Rule[] newRules = new Rule[acceptedCount];
		RuleCharacteristics[] newRuleCharacteristics = new RuleCharacteristics[acceptedCount];
		int newAcceptedRuleIndex = 0;
		
		for (int i = 0; i < ruleSetSize; i++) {
			if (accepted[i] == 1) {
				newRules[newAcceptedRuleIndex] = getRule(i);
				newRuleCharacteristics[newAcceptedRuleIndex] = getRuleCharacteristics(i);
				newAcceptedRuleIndex++;
			}
		}
		
		return new RuleSetWithCharacteristics(newRules, newRuleCharacteristics, true);
	}
	
	/**
	 * Serializes this rule set to multiline plain text (rules + chosen, most important characteristics).
	 * If for any rule some characteristics is unknown, prints "?" for its value.
	 * 
	 * @return multiline plain text representation of this rule set
	 */
	@Override
	public String serialize() {
		StringBuilder rulesTxtBuilder = new StringBuilder();
		int size = size();
		RuleCharacteristics ruleCharacteristics;
		
		for (int ruleIndex = 0; ruleIndex < size; ruleIndex++) {
			ruleCharacteristics = getRuleCharacteristics(ruleIndex);
			rulesTxtBuilder.append(getRule(ruleIndex))
			.append(" [support=").append(ruleCharacteristics.isSupportSet() ? ruleCharacteristics.getSupport() : "?")
			.append(", strength=").append(ruleCharacteristics.isStrengthSet() ? ruleCharacteristics.getStrength() : "?")
			.append(", coverage-factor=").append(ruleCharacteristics.isCoverageFactorSet() ? ruleCharacteristics.getCoverageFactor() : "?")
			.append(", confidence=").append(ruleCharacteristics.isConfidenceSet() ? ruleCharacteristics.getConfidence() : "?")
			.append(", epsilon=").append(ruleCharacteristics.isEpsilonSet() ? ruleCharacteristics.getEpsilon() : "?")
			.append("]")
			.append(System.lineSeparator());
		}
		
		return rulesTxtBuilder.toString();
	}
	
	/**
	 * Iterates over pairs composed of a rule and its characteristics, and for each characteristics sets basic rule coverage information,
	 * using respective rule and given learning information table. It is assumed that given information table is a learning set of the rules
	 * from this rule set.
	 * 
	 * @param learningInformationTable learning information table for which rules from this rule set has been induced
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public void calculateBasicRuleCoverageInformation(InformationTable learningInformationTable) {
		Precondition.notNull(learningInformationTable, "Learning information table is null.");
		for (int i = 0; i < this.rules.length; i++) {
			ruleCharacteristics[i].setRuleCoverageInformation(new BasicRuleCoverageInformation(rules[i], learningInformationTable));
		}
	}
}
