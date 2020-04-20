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

import org.rulelearn.core.Precondition;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.InformationTable;

/**
 * Set of decision rules, each identified by its index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleSet {
	
	/**
	 * Array with subsequent rules stored in this rule set.
	 */
	Rule[] rules;
	
	/**
	 * {@link InformationTable#getHash() Hash of the learning information table} for which rules from this rule set have been induced.
	 * If not set explicitly, equals {@code null}.
	 * Is set, gets written to RuleML file when the rules are saved.
	 */
	String learningInformationTableHash = null;
	
	/**
	 * Constructor storing rules from the given array in this rule set. 
	 * 
	 * @param rules decision rules to store in this rule set
	 * @throws NullPointerException if given array of rules is {@code null}
	 */
	public RuleSet(Rule[] rules) {
		this(rules, false);
	}
	
	/**
	 * Constructor storing rules from the given array in this rule set. 
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given reference
	 *        to an array of rules is not going to be used outside this class
	 *        to modify that array (and thus, this object does not need to clone the array for internal read-only use)
	 * @throws NullPointerException if given array of rules is {@code null}
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public RuleSet(Rule[] rules, boolean accelerateByReadOnlyParams) {
		notNull(rules, "Rules to be stored in a rule set are null."); //assert rules are not null
		this.rules = accelerateByReadOnlyParams ? rules : rules.clone(); //ruleList.toArray(new Rule[0]);
	}
	
	/**
	 * Gets decision rule with given index.
	 * 
	 * @param ruleIndex index of a rule to get from this set of rules
	 * @return rule from this set having given index
	 * 
	 * @throws IndexOutOfBoundsException if given index does not match any rule in this rule set
	 */
	public Rule getRule(int ruleIndex) {
		return this.rules[ruleIndex];
	}
	
	/**
	 * Gets size (number of rules) of this rules set.
	 * 
	 * @return size (number of rules) of this rules set
	 */
	public int size() {
		return this.rules.length;
	}
	
	/**
	 * Gets a new rule set by joining given two rule sets.
	 * 
	 * @param ruleSet1 first rule set to join
	 * @param ruleSet2 second rule set to join
	 * @return a new rule set composing of rules from the two given rule sets
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public static RuleSet join(RuleSet ruleSet1, RuleSet ruleSet2) {
		Precondition.notNull(ruleSet1, "First rule set to join is null.");
		Precondition.notNull(ruleSet2, "Second rule set to join is null.");
		
		Rule[] rules = new Rule[ruleSet1.size() + ruleSet2.size()];
		for (int i = 0; i < ruleSet1.size(); i++) {
			rules[i] = ruleSet1.getRule(i);
		}
		int shift = ruleSet1.size();
		for (int i = 0; i < ruleSet2.size(); i++) {
			rules[shift + i] = ruleSet2.getRule(i);
		}
		return new RuleSet(rules, true);
	}
	
	/**
	 * Gets the hash of the learning information table for which rules from this rule set have been induced.
	 * 
	 * @return hash of the learning information table for which rules from this rule set have been induced
	 */
	public String getLearningInformationTableHash() {
		return learningInformationTableHash;
	}

	/**
	 * Stores the hash of the learning information table for which rules from this rule set have been induced.
	 * Hash gets written to RuleML file when the rules are saved.
	 * 
	 * @param learningInformationTableHash the hash of the learning information table for which rules from this rule set have been induced
	 */
	public void setLearningInformationTableHash(String learningInformationTableHash) {
		this.learningInformationTableHash = learningInformationTableHash;
	}
	
	/**
	 * Serializes this rule set to multiline plain text.
	 * 
	 * @return multiline plain text representation of this rule set
	 */
	public String serialize() {
		StringBuilder rulesTxtBuilder = new StringBuilder();
		int size = size();
		
		for (int ruleIndex = 0; ruleIndex < size; ruleIndex++) {
			rulesTxtBuilder.append(getRule(ruleIndex)).append(System.lineSeparator());
		}
		
		return rulesTxtBuilder.toString();
	}
	
}
