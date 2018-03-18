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

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;

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
	protected Rule[] rules;
	
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
}
