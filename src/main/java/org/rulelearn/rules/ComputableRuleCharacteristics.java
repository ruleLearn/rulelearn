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
	 * @return decision rule for which these characteristics are computed
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
	
	/**
	 * Gets support of a decision rule in the context of an information table.
	 * 
	 * @return support of the decision rule in the context of the information table
	 */
	@Override
	public int getSupport() {
		if (support == UNKNOWN_INT_VALUE) {
			//TODO: calculate measure
		}
		return support;
	}
	
	/**
	 * Gets strength of the decision rule in the context of the information table.
	 * 
	 * @return strength of the decision rule in the context of the information table
	 */
	@Override
	public double getStrength() {
		if (strength == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return strength;
	}
	
	/**
	 * Gets confidence (ratio of support and coverage) of the decision rule in the context of the information table.
	 * 
	 * @return confidence confidence of the decision rule in the context of the information table
	 */
	@Override
	public double getConfidence() {
		if (confidence == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return confidence;
	}
	
	/**
	 * Gets coverage factor of the decision rule in the context of the information table.
	 * 
	 * @return coverage factor of the decision rule in the context of the information table
	 */
	@Override
	public double getCoverageFactor() {
		if (coverageFactor == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return coverageFactor;
	}
	
	/**
	 * Gets coverage of the decision rule (number of objects covered by the rule) in the context of the information table.
	 * 
	 * @return coverage of the decision rule (number of objects covered by the rule) in the context of the information table
	 */
	@Override
	public int getCoverage() {
		if (coverage == UNKNOWN_INT_VALUE) {
			//TODO: calculate measure
		}
		return coverage;
	}
	
	/**
	 * Gets negative coverage of the decision rule (number of negative objects covered by the rule) in the context of the information table.
	 * An object is negative, if its decision does not match rule's decision part.
	 * 
	 * @return negative coverage of the decision rule (number of negative objects covered by the rule) in the context of the information table
	 */
	@Override
	public int getNegativeCoverage() {
		if (negativeCoverage == UNKNOWN_INT_VALUE) {
			//TODO: calculate measure
		}
		return negativeCoverage;
	}
	
	/**
	 * Gets value of rule consistency measure $\epsilon$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule consistency measure $\epsilon$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getEpsilon() {
		if (epsilon == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return epsilon;
	}
	
	/**
	 * Gets value of rule consistency measure $\epsilon'$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule consistency measure $\epsilon'$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getEpsilonPrim() {
		if (epsilonPrim == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return epsilonPrim;
	}
	
	/**
	 * Gets value of rule confirmation measure $f$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure $f$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getFConfirmation() {
		if (fConfirmation == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return fConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure $a$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure $a$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getAConfirmation() {
		if (aConfirmation == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return aConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure $z$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure $z$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getZConfirmation() {
		if (zConfirmation == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return zConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure $l$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure $l$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getLConfirmation() {
		if (lConfirmation == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return lConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure $c_1$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure $c_1$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getC1Confirmation() {
		if (c1Confirmation == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return c1Confirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure $s$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure $s$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getSConfirmation() {
		if (sConfirmation == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return sConfirmation;
	}
	
}
