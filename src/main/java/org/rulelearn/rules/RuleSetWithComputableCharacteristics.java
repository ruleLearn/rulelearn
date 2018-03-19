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
import org.rulelearn.data.InformationTable;

/**
 * Set of decision rules and their complete characteristics (pre-stored or computed on demand - see {@link ComputableRuleCharacteristics}),
 * each identified by rule's index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleSetWithComputableCharacteristics extends RuleSetWithCharacteristics {
	
	/**
	 * Information table against which all rule characteristics are computed.
	 */
	protected InformationTable informationTable = null;
	
	/**
	 * Constructor storing rules from the given array and the information table against which characteristics of all rules should be computed.
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param informationTable information table against which all rule characteristics should be computed
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleSetWithComputableCharacteristics(Rule[] rules, InformationTable informationTable) {
		this(rules, informationTable, false);
	}
	
	/**
	 * Constructor storing rules from the given array and the information table against which characteristics of all rules should be computed.
	 * 
	 * @param rules decision rules to store in this rule set
	 * @param informationTable information table against which all rule characteristics should be computed
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given references
	 *        to an array of rules is not going to be used outside this class
	 *        to modify that array (and thus, this object does not need to clone the array for internal read-only use)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public RuleSetWithComputableCharacteristics(Rule[] rules, InformationTable informationTable, boolean accelerateByReadOnlyParams) {
		super(rules, accelerateByReadOnlyParams);
		
		this.ruleCharacteristics = new ComputableRuleCharacteristics[rules.length];
		this.informationTable = notNull(informationTable, "Information table is null.");
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
			this.ruleCharacteristics[ruleIndex] = new ComputableRuleCharacteristics(rules[ruleIndex], this.informationTable);
		}
		
		return (ComputableRuleCharacteristics)this.ruleCharacteristics[ruleIndex];
	}

	/**
	 * Gets the information table against which all rule characteristics are computed.
	 * 
	 * @return information table against which all rule characteristics are computed
	 */
	public InformationTable getInformationTable() {
		return this.informationTable;
	}

}
