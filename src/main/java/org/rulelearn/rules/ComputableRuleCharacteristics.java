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
 * Characteristics of a decision rule, calculated using {@link RuleCoverageInformation rule coverage information}. This class extends {@link RuleCharacteristics}
 * by ensuring that if any characteristic is not stored explicitly, it will be calculated on demand.
 * This class also offers caching of rule characteristics, i.e., each referenced characteristic will be calculated only once and stored,
 * and all subsequent calls will return the stored value.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ComputableRuleCharacteristics extends RuleCharacteristics {
	
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
	 * Parameter alpha of confirmation measure c<sub>1</sub>. Initialized with default value 0.5.
	 */
	double c1ConfirmationAlpha = 0.5;
	/**
	 * Parameter beta of confirmation measure c<sub>1</sub>. Initialized with default value 0.5.
	 */
	double c1ConfirmationBeta = 0.5;
	
	/**
	 * Creates these rule characteristics using given rule coverage information.
	 * 
	 * @param ruleCoverageInformation coverage information concerning considered decision rule {@link RuleCoverageInformation}
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public ComputableRuleCharacteristics(RuleCoverageInformation ruleCoverageInformation) {
		super(notNull(ruleCoverageInformation, "Rule coverage information for computable rule characteristics is null."));
	}
	
	/**
	 * Gets value of given rule evaluator.
	 * TODO: use caching?
	 * 
	 * @param ruleEvaluator rule evaluator
	 * @return value of given rule evaluator
	 */
	public double getCharacteristic(RuleEvaluator ruleEvaluator) {
		return ruleEvaluator.evaluate(getRuleCoverageInformation());
	}
	
	/**
	 * Gets coverage information concerning considered decision rule.
	 * 
	 * @return coverage information concerning considered decision rule
	 * @see RuleCoverageInformation
	 */
	public RuleCoverageInformation getRuleCoverageInformation() {
		return (RuleCoverageInformation)this.ruleCoverageInformation;
	}
	
	/**
	 * Overrides super-type setter by throwing {@link UnsupportedOperationException}.
	 * 
	 * @param ruleCoverageInformation basic rule coverage information
	 * @throws UnsupportedOperationException if called, to prevent update of rule coverage information
	 */
	public void setRuleCoverageInformation(BasicRuleCoverageInformation ruleCoverageInformation) {
		throw new UnsupportedOperationException("Rule coverage information for computable rule characteristics cannot be changed.");
	}
	
	/**
	 * Gets support of a decision rule (i.e., number of positive objects covered by the rule) in the context of an information table.
	 * 
	 * @return support of the decision rule (i.e., number of positive objects covered by the rule) in the context of the information table
	 */
	@Override
	public int getSupport() {
		if (support == UNKNOWN_INT_VALUE) {
			support = (int)SupportMeasure.getInstance().evaluate(getRuleCoverageInformation()); //should be an integer, so casting should be safe
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
			confidence = RoughMembershipMeasure.getInstance().evaluate(getRuleCoverageInformation());
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
			if (getRuleCoverageInformation().getIndicesOfPositiveObjects() != null) {
				coverageFactor = ((double)getSupport()) / ((double)getRuleCoverageInformation().getIndicesOfPositiveObjects().size());
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
	 * An covered object is negative, if its decision does not match rule's decision part, and object is not neutral.
	 * 
	 * @return negative coverage of the decision rule (number of negative objects covered by the rule) in the context of the information table
	 */
	@Override
	public int getNegativeCoverage() {
		if (negativeCoverage == UNKNOWN_INT_VALUE) {
			if (getRuleCoverageInformation().getIndicesOfPositiveObjects() != null &&
					getRuleCoverageInformation().getIndicesOfNeutralObjects() != null) {
			
				negativeCoverage = OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets(
						this.ruleCoverageInformation.getIndicesOfCoveredObjects(),
						getRuleCoverageInformation().getIndicesOfPositiveObjects(), getRuleCoverageInformation().getIndicesOfNeutralObjects());
			}
		}
		return negativeCoverage;
	}
	
	/**
	 * Gets number of positive objects not covered by the rule.
	 * 
	 * @return number of positive objects not covered by the rule
	 */
	int getPositiveNotCoveredObjectsCount() {
		if (positiveNotCoveredObjectsCount == UNKNOWN_INT_VALUE) {
			if (getRuleCoverageInformation().getIndicesOfPositiveObjects() != null) {
				positiveNotCoveredObjectsCount = getRuleCoverageInformation().getIndicesOfPositiveObjects().size() - getSupport(); //number of all positive objects - number of covered positive objects
			}
		}
		return positiveNotCoveredObjectsCount;
	}
	
	/**
	 * Gets number of negative objects not covered by the rule.
	 * 
	 * @return number of negative objects not covered by the rule
	 */
	int getNegativeNotCoveredObjectsCount() {
		if (negativeNotCoveredObjectsCount == UNKNOWN_INT_VALUE) {
			if (getRuleCoverageInformation().getIndicesOfPositiveObjects() != null &&
					getRuleCoverageInformation().getIndicesOfNeutralObjects() != null) {
				
				negativeNotCoveredObjectsCount = (getRuleCoverageInformation().getAllObjectsCount()
						- getRuleCoverageInformation().getIndicesOfPositiveObjects().size()
						- getRuleCoverageInformation().getIndicesOfNeutralObjects().size()) //get number of all negative objects
						- getNegativeCoverage(); //and subtract number of covered negative objects
			}
		}
		return negativeNotCoveredObjectsCount;
	}
	
	/**
	 * Gets value of rule consistency measure $\epsilon$ calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule consistency measure $\epsilon$ calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getEpsilon() {
		if (epsilon == UNKNOWN_DOUBLE_VALUE) {
			epsilon = EpsilonConsistencyMeasure.getInstance().evaluate(getRuleCoverageInformation());
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
			epsilonPrime = (double)getNegativeCoverage() / (double)getRuleCoverageInformation().getIndicesOfPositiveObjects().size();
		}
		return epsilonPrime;
	}
	
	/**
	 * Gets value of rule confirmation measure F calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure F calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getFConfirmation() {
		if (fConfirmation == UNKNOWN_DOUBLE_VALUE) {
			double a, b, c, d;
			
			a = getSupport();
			b = getPositiveNotCoveredObjectsCount();
			c = getNegativeCoverage();
			d = getNegativeNotCoveredObjectsCount();
			
			fConfirmation = ( ((a*d)-(b*c)) / ((a*d)+(b*c)+(2*a*c)) );
		}
		
		return fConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure A calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure A calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getAConfirmation() {
		if (aConfirmation == UNKNOWN_DOUBLE_VALUE) {
			double a, b, c, d;
			
			a = getSupport();
			b = getPositiveNotCoveredObjectsCount();
			c = getNegativeCoverage();
			d = getNegativeNotCoveredObjectsCount();
			
			if ( (a / (a+c)) >= ((a+b) / (a+b+c+d)) ) {
				aConfirmation = ( ((a*d)-(b*c)) / ((a+b)*(b+d)) );
			}
			else {
				aConfirmation = ( ((a*d)-(b*c)) / ((b+d)*(c+d)) );
			}
		}
		
		return aConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure Z calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure Z calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getZConfirmation() {
		if (zConfirmation == UNKNOWN_DOUBLE_VALUE) {
			double a, b, c, d;
			
			a = getSupport();
			b = getPositiveNotCoveredObjectsCount();
			c = getNegativeCoverage();
			d = getNegativeNotCoveredObjectsCount();
			
			if ( (a / (a+c)) >= ((a+b)/(a+b+c+d)) ) {
				zConfirmation = ( ((a*d)-(b*c)) / ((a+c)*(c+d)) );
			}
			else {
				zConfirmation = ( ((a*d)-(b*c)) / ((a+c)*(a+b)) );
			}
		}
		
		return zConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure L calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure L calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getLConfirmation() {
		if (lConfirmation == UNKNOWN_DOUBLE_VALUE) {
			double a, b, c, d;
			
			a = getSupport();
			b = getPositiveNotCoveredObjectsCount();
			c = getNegativeCoverage();
			d = getNegativeNotCoveredObjectsCount();
			
			if (c != 0) {
				lConfirmation = Math.log( (a / (a+b)) / (c / (c+d)) );
			}
			else if (a == 0) {
				lConfirmation = Double.NaN;
			}
			else {
				lConfirmation = Double.POSITIVE_INFINITY;
			}
		}
		return lConfirmation;
	}
	
	/**
	 * Gets value of rule confirmation measure c<sub>1</sub> calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure c<sub>1</sub> calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getC1Confirmation() {
		if (c1Confirmation == UNKNOWN_DOUBLE_VALUE) {
			double a, b, c, d;

			a = getSupport();
			b = getPositiveNotCoveredObjectsCount();
			c = getNegativeCoverage();
			d = getNegativeNotCoveredObjectsCount();
			
			if ( (a / (a+c)) >= ((a+b) / (a+b+c+d)) ) {
				if (c == 0.0) {
					c1Confirmation = c1ConfirmationAlpha + (c1ConfirmationBeta * ( ((a*d)-(b*c)) / ((a+b)*(b+d)) ));
				}
				else {
					c1Confirmation = (c1ConfirmationAlpha * ( ((a*d)-(b*c)) / ((a+c)*(c+d)) ));
				}
			}
			else {
				if (a == 0.0) {
					c1Confirmation = (-1 * c1ConfirmationAlpha) + (c1ConfirmationBeta * ( ((a*d)-(b*c)) / ((b+d)*(c+d)) ));
				}
				else {
					c1Confirmation = (c1ConfirmationAlpha * ( ((a*d)-(b*c)) / ((a+b)*(a+c)) ));
				}
			}
		}
		
		return c1Confirmation;
	}
	
	/**
	 * Gets parameter alpha of rule confirmation measure c<sub>1</sub>.
	 * Default value is 0.5.
	 * 
	 * @return parameter alpha of rule confirmation measure c<sub>1</sub>.
	 */
	public double getC1ConfirmationAlpha() {
		return c1ConfirmationAlpha;
	}
	
	/**
	 * Gets parameter beta of rule confirmation measure c<sub>1</sub>.
	 * Default value is 0.5.
	 * 
	 * @return parameter beta of rule confirmation measure c<sub>1</sub>.
	 */
	public double getC1ConfirmationBeta() {
		return c1ConfirmationBeta;
	}
	
	/**
	 * Sets parameters of rule confirmation measure c<sub>1</sub>, only if value of confirmation measure c<sub>1</sub> has not been calculated yet
	 * ({@link #isC1ConfirmationSet()} returns {@code false}). This restriction corresponds to the case when parameters alpha and beta would be set
	 * in constructor, and is concordant with general design choice of making objects unmodifiable.
	 * 
	 * @param c1ConfirmationAlpha new parameter alpha of rule confirmation measure c<sub>1</sub>
	 * @param c1ConfirmationBeta new parameter beta of rule confirmation measure c<sub>1</sub>
	 * 
	 * @return {@code true} if parameters alpha and beta of rule confirmation measure c<sub>1</sub> have been successfully set,
	 *         {@code false} otherwise
	 */
	public boolean setC1ConfirmationParameters(double c1ConfirmationAlpha, double c1ConfirmationBeta) {
		if (c1Confirmation == UNKNOWN_DOUBLE_VALUE) {
			this.c1ConfirmationAlpha = c1ConfirmationAlpha;
			this.c1ConfirmationBeta = c1ConfirmationBeta;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets value of rule confirmation measure S calculated for the decision rule in the context of the information table.
	 * 
	 * @return value of rule confirmation measure S calculated for the decision rule in the context of the information table
	 */
	@Override
	public double getSConfirmation() {
		if (sConfirmation == UNKNOWN_DOUBLE_VALUE) {
			//s(H,E) = Pr(H|E) - Pr(H|not E)
			double a, b, c, d;

			a = getSupport();
			b = getPositiveNotCoveredObjectsCount();
			c = getNegativeCoverage();
			d = getNegativeNotCoveredObjectsCount();
			
			sConfirmation = a / (a + c) - b / (b + d); //does not take into account neutral objects!
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
