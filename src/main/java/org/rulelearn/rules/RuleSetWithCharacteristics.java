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
 * Set of decision rules and their characteristics, each identified by rule's index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleSetWithCharacteristics extends RuleSet {
	
	/**
	 * Array with characteristics of subsequent rules stored in this rule set.
	 */
	protected RuleCharacteristics[] ruleCharacteristics;
	
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
}
