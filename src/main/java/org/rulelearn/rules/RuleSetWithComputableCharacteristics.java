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
	
}
