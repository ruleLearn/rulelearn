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
import org.rulelearn.data.InformationTable;

/**
 * Characteristics of a decision rule, calculated in the context of a particular rule and information table. This class extends {@link RuleCharacteristics}
 * by ensuring that if any characteristic is not stored explicitly, it will be calculated on demand.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ComputableRuleCharacteristics extends RuleCharacteristics {
	/**
	 * Decision rule against which these characteristics are calculated.
	 */
	protected Rule rule = null;
	
	/**
	 * Information table against which these characteristics are calculated.
	 */
	protected InformationTable informationTable = null;
	
	/**
	 * Number of all objects from the information table
	 * which are not covered by the rule but match rule's decision part.
	 */
	protected int positiveNotCoveredObjectsCount = UNKNOWN_INT_VALUE;
	/**
	 * Number of all objects from the information table
	 * which are not covered by the rule and do not match rule's decision part.
	 */
	protected int negativeNotCoveredObjectsCount = UNKNOWN_INT_VALUE;
	
	/**
	 * Creates these rule characteristics, for the given rule and information table.
	 * 
	 * @param rule decision rule against which these characteristics are calculated
	 * @param informationTable information table against which these characteristics are calculated
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ComputableRuleCharacteristics(Rule rule, InformationTable informationTable) {
		super();
		this.rule = notNull(rule, "Decision rule for which characteristics should be calculated is null.");
		this.informationTable = notNull(informationTable, "Information table for which rule characteristics should be calculated is null.");
	}
	
	//TODO: override getters throwing an exception, to compute required characteristic on demand
	//TODO: use additional supplementary fields: positiveNotCoveredObjectsCount, negativeNotCoveredObjectsCount
	
	/**
	 * Gets decision rule for which these characteristics are computed.
	 * 
	 * @return decision rule for which these characteristics are computed.
	 */
	public Rule getRule() {
		return this.rule;
	}
	
	/**
	 * Gets information table for which these characteristics are computed.
	 * 
	 * @return information table for which these characteristics are computed
	 */
	public InformationTable getInformationTable() {
		return this.informationTable;
	}
	
}
