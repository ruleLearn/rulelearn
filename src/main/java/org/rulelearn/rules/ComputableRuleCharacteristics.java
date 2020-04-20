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

import org.rulelearn.core.OperationsOnCollections;
import org.rulelearn.measures.SupportMeasure;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;
import org.rulelearn.measures.dominance.RoughMembershipMeasure;

/**
 * Characteristics of a decision rule, calculated using rule coverage information {@link RuleCoverageInformation}. This class extends {@link RuleCharacteristics}
 * by ensuring that if any characteristic is not stored explicitly, it will be calculated on demand.
 * This class also offers caching of rule characteristics, i.e., each referenced characteristic will be calculated only once and stored, and all subsequent
 * calls will return the stored value.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ComputableRuleCharacteristics extends RuleCharacteristics {
	
	/**
	 * Coverage information concerning considered decision rule {@link Rule}.
	 */
	RuleCoverageInformation ruleCoverageInformation;
	
	/**
	 * Number of all objects from the information table
	 * which are not covered by the rule but match rule's decision part.
	 */
	int positiveNotCoveredObjectsCount = UNKNOWN_INT_VALUE;
	/**
	 * Number of all objects from the information table
	 * which are not covered by the rule and do not match rule's decision part.
	 */
	int negativeNotCoveredObjectsCount = UNKNOWN_INT_VALUE;
	
	/**
	 * Parameter of confirmation measure $c<sub>1</sub>$.
	 */
	double alpha = 0.5;
	/**
	 * Parameter of confirmation measure $c<sub>1</sub>$.
	 */
	double beta = 0.5;
	
	/**
	 * Creates these rule characteristics using given rule coverage information.
	 * 
	 * @param ruleCoverageInformation coverage information concerning considered decision rule {@link RuleCoverageInformation}
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public ComputableRuleCharacteristics(RuleCoverageInformation ruleCoverageInformation) {
		super();
		this.ruleCoverageInformation = notNull(ruleCoverageInformation, "Rule coverage information is null.");
	}
	
	/**
	 * Gets value of given rule evaluator.
	 * TODO: use caching?
	 * 
	 * @param ruleEvaluator rule evaluator
	 * @return value of given rule evaluator
	 */
	public double getCharacteristic(RuleEvaluator ruleEvaluator) {
		return ruleEvaluator.evaluate(this.ruleCoverageInformation);
	}
	
	//TODO: use additional supplementary fields: positiveNotCoveredObjectsCount, negativeNotCoveredObjectsCount
	
	/**
	 * Gets coverage information concerning considered decision rule.
	 * 
	 * @return coverage information concerning considered decision rule
	 * @see RuleCoverageInformation
	 */
	public RuleCoverageInformation getRuleCoverageInformation() {
		return this.ruleCoverageInformation;
	}
	
	/**
	 * Gets support of a decision rule in the context of an information table.
	 * 
	 * @return support of the decision rule in the context of the information table
	 */
	@Override
	public int getSupport() {
		if (support == UNKNOWN_INT_VALUE) {
			support = (int)SupportMeasure.getInstance().evaluate(this.ruleCoverageInformation); //should be an integer, so casting should be safe
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
			strength = ((double)getSupport()) / ((double)this.ruleCoverageInformation.getAllObjectsCount()); 
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
			confidence = RoughMembershipMeasure.getInstance().evaluate(this.ruleCoverageInformation);
		}
		return confidence;
	}
	
	/**
	 * Gets coverage factor of the decision rule in the context of the information table.<br>
	 * <br>
	 * If {@link #getRuleCoverageInformation() rule coverage information} does not store indices of positive objects
	 * (method {@link RuleCoverageInformation#getIndicesOfPositiveObjects()} returns {@code null}), then result of this
	 * method cannot be calculated, and will default to {@link RuleCharacteristics#UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @return coverage factor of the decision rule in the context of the information table
	 */
	@Override
	public double getCoverageFactor() {
		if (coverageFactor == UNKNOWN_DOUBLE_VALUE) {
			if (this.ruleCoverageInformation.getIndicesOfPositiveObjects() != null) {
				coverageFactor = ((double)getSupport()) / ((double)this.ruleCoverageInformation.getIndicesOfPositiveObjects().size());
			}
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
			coverage = this.ruleCoverageInformation.getIndicesOfCoveredObjects().size();
		}
		return coverage;
	}
	
	/**
	 * Gets negative coverage of the decision rule (number of negative objects covered by the rule) in the context of the information table.
	 * An object is negative, if its decision does not match rule's decision part.<br>
	 * <br>
	 * If {@link #getRuleCoverageInformation() rule coverage information} does not store indices of positive or neutral objects
	 * (method {@link RuleCoverageInformation#getIndicesOfPositiveObjects()} returns {@code null}, or
	 * method {@link RuleCoverageInformation#getIndicesOfNeutralObjects()} returns {@code null}), then result of this
	 * method cannot be calculated, and will default to {@link RuleCharacteristics#UNKNOWN_INT_VALUE}.
	 * 
	 * @return negative coverage of the decision rule (number of negative objects covered by the rule) in the context of the information table
	 */
	@Override
	public int getNegativeCoverage() {
		if (negativeCoverage == UNKNOWN_INT_VALUE) {
			if (this.ruleCoverageInformation.getIndicesOfPositiveObjects() != null &&
					this.ruleCoverageInformation.getIndicesOfNeutralObjects() != null) {
			
				negativeCoverage = OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets(
						this.ruleCoverageInformation.getIndicesOfCoveredObjects(),
						this.ruleCoverageInformation.getIndicesOfPositiveObjects(), this.ruleCoverageInformation.getIndicesOfNeutralObjects());
			}
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
			epsilon = EpsilonConsistencyMeasure.getInstance().evaluate(this.ruleCoverageInformation);
		}
		return epsilon;
	}
	
	/**
	 * Gets value of rule consistency measure $\epsilon'$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule consistency measure $\epsilon'$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getEpsilonPrime() {
		if (epsilonPrime == UNKNOWN_DOUBLE_VALUE) {
			//TODO: calculate measure
		}
		return epsilonPrime;
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
	
	/**
	 * Enforces that values of all rule characteristics are calculated instantly and remembered, so each subsequent call to any getter
	 * will return requested characteristic at once, without additional calculations.
	 */
	public void calculateAllCharacteristics() {
		this.getAConfirmation();
		this.getC1Confirmation();
		this.getConfidence();
		this.getCoverage();
		this.getCoverageFactor();
		this.getEpsilon();
		this.getEpsilonPrime();
		this.getFConfirmation();
		this.getLConfirmation();
		this.getNegativeCoverage();
		this.getSConfirmation();
		this.getStrength();
		this.getSupport();
		this.getZConfirmation();
	}
}
