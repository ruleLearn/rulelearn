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

import org.rulelearn.core.UnknownValueException;
import static org.rulelearn.core.Precondition.nonNegative;
import static org.rulelearn.core.Precondition.known;
import static org.rulelearn.core.Precondition.within01Interval;
import static org.rulelearn.core.Precondition.withinMinus1Plus1Interval;
import org.rulelearn.core.InvalidValueException;

/**
 * Characteristics of a decision rule. One can get from this object only:<br>
 * - the characteristics that prior have been set with the respective setter,<br>
 * - the characteristics that depend (directly on indirectly) exclusively on the above characteristics.<br>
 * For instance, if support and number of covered objects have been set first, then one can get confidence being the ratio of the two characteristics.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleCharacteristics {
	
	/**
	 * Default value of all integer characteristics, indicating that given characteristic is unknown, i.e., it has not been set or calculated yet.
	 */
	public static final int UNKNOWN_INT_VALUE = Integer.MIN_VALUE;
	
	/**
	 * Default value of all floating-point characteristics, indicating that given characteristic is unknown, i.e., it has not been set or calculated yet.
	 */
	public static final double UNKNOWN_DOUBLE_VALUE = Double.MIN_VALUE;
	
	//initialization of all characteristics as unknown
	/**
	 * Support (positive coverage) of a decision rule in the context of an information table.
	 */
	protected int support = UNKNOWN_INT_VALUE;
	/**
	 * Strength of a decision rule in the context of an information table.
	 */
	protected double strength = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Confidence of a decision rule in the context of an information table.
	 */
	protected double confidence = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Coverage factor of a decision rule in the context of an information table.
	 */
	protected double coverageFactor = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Coverage of a decision rule (number of objects covered by the rule) in the context of an information table.
	 */
	protected int coverage = UNKNOWN_INT_VALUE;
	/**
	 * Negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table.
	 * An object is negative, if its decision part does not match rule's decision part.
	 */
	protected int negativeCoverage = UNKNOWN_INT_VALUE;
	/**
	 * Value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table.
	 */
	protected double epsilon = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table.
	 */
	protected double epsilonPrim = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table.
	 */
	protected double fConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table.
	 */
	protected double aConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table.
	 */
	protected double zConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table.
	 */
	protected double lConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table.
	 */
	protected double c1Confirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure $s$ calculated for a decision rule in the context of an information table.
	 */
	protected double sConfirmation = UNKNOWN_DOUBLE_VALUE;
	
	/**
	 * Sole constructor.
	 */
	public RuleCharacteristics() {}
	
	/**
	 * Gets support of a decision rule in the context of an information table.
	 * 
	 * @return support of a decision rule in the context of an information table
	 * @throws UnknownValueException if support is unknown (not stored in these characteristics)
	 */
	public int getSupport() {
		return known(support, UNKNOWN_INT_VALUE, "Rule's support is unknown.");
	}

	/**
	 * Sets support of a decision rule in the context of an information table. In order to forget stored support, 
	 * one can invoke this method with {@link #UNKNOWN_INT_VALUE}.
	 *  
	 * @param support support of a decision rule in the context of an information table
	 * @throws InvalidValueException if given support is negative
	 */
	public void setSupport(int support) {
		if (support != UNKNOWN_INT_VALUE) {
			nonNegative(support, "Rule's support has to be >= 0."); //assert that characteristic satisfies constraint(s)
		}
		this.support = support;
	}

	/**
	 * Gets strength of a decision rule in the context of an information table.
	 * 
	 * @return strength of a decision rule in the context of an information table
	 * @throws UnknownValueException if strength is unknown (not stored in these characteristics)
	 */
	public double getStrength() {
		return known(strength, UNKNOWN_DOUBLE_VALUE, "Rule's strength is unknown.");
	}

	/**
	 * Sets strength of a decision rule in the context of an information table. In order to forget stored strength, 
	 * one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param strength strength of a decision rule in the context of an information table
	 * @throws InvalidValueException if given strength is not within [0,1] interval
	 */
	public void setStrength(double strength) {
		if (strength != UNKNOWN_DOUBLE_VALUE) {
			within01Interval(strength, "Rule's strength has to be within [0,1] interval."); //assert that characteristic satisfies constraint(s)
		}
		this.strength = strength;
	}

	/**
	 * Gets confidence of a decision rule in the context of an information table. If confidence is not stored in these characteristics,
	 * an attempt is made to compute it as a ratio of support and coverage.
	 * 
	 * @return confidence confidence of a decision rule in the context of an information table
	 * @throws UnknownValueException if confidence is unknown (not stored in these characteristics) and cannot be computed
	 *         as either support or coverage is also unknown
	 */
	public double getConfidence() {
		if (confidence == UNKNOWN_DOUBLE_VALUE) {
			try {
				confidence = (double)getSupport() / (double)getCoverage();
			} catch (UnknownValueException exception) {
				throw new UnknownValueException("Rule's confidence is unknown and cannot be computed.");
			}
		}
		return confidence;
	}

	/**
	 * Sets confidence of a decision rule in the context of an information table. In order to forget stored confidence, 
	 * one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param confidence confidence of a decision rule in the context of an information table
	 * @throws InvalidValueException if given confidence is not within [0,1] interval
	 */
	public void setConfidence(double confidence) {
		if (confidence != UNKNOWN_DOUBLE_VALUE) {
			within01Interval(confidence, "Rule's confidence has to be within [0,1] interval."); //assert that characteristic satisfies constraint(s)
		}
		this.confidence = confidence;
	}

	/**
	 * Gets coverage factor of a decision rule in the context of an information table.
	 * 
	 * @return coverage factor of a decision rule in the context of an information table
	 * @throws UnknownValueException if coverage factor is unknown (not stored in these characteristics)
	 */
	public double getCoverageFactor() {
		return known(coverageFactor, UNKNOWN_DOUBLE_VALUE, "Rule's coverage factor is unknown.");
	}

	/**
	 * Sets coverage factor of a decision rule in the context of an information table. In order to forget stored coverage factor, 
	 * one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param coverageFactor coverage factor of a decision rule in the context of an information table
	 * @throws InvalidValueException if given coverage factor is not within [0,1] interval
	 */
	public void setCoverageFactor(double coverageFactor) {
		if (coverageFactor != UNKNOWN_DOUBLE_VALUE) {
			within01Interval(coverageFactor, "Rule's coverage factor has to be within [0,1] interval."); //assert that characteristic satisfies constraint(s)
		}
		this.coverageFactor = coverageFactor;
	}

	/**
	 * Gets coverage of a decision rule (number of objects covered by the rule) in the context of an information table.
	 * 
	 * @return coverage of a decision rule (number of objects covered by the rule) in the context of an information table
	 * @throws UnknownValueException if coverage is unknown (not stored in these characteristics)
	 */
	public int getCoverage() {
		return known(coverage, UNKNOWN_INT_VALUE, "Rule's coverage is unknown.");
	}

	/**
	 * Sets coverage of a decision rule (number of objects covered by the rule) in the context of an information table. In order to forget stored coverage, 
	 * one can invoke this method with {@link #UNKNOWN_INT_VALUE}.
	 * 
	 * @param coverage coverage of a decision rule (number of objects covered by the rule) in the context of an information table
	 * @throws InvalidValueException if given coverage is negative
	 */
	public void setCoverage(int coverage) {
		if (coverage != UNKNOWN_INT_VALUE) {
			nonNegative(coverage, "Rule's coverage has to be >= 0."); //assert that characteristic satisfies constraint(s)
		}
		this.coverage = coverage;
	}

	/**
	 * Gets negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table.
	 * An object is negative, if its decision does not match rule's decision part.
	 * If negative coverage is not stored in these characteristics, an attempt is made to compute it as a difference between coverage and support.
	 * 
	 * @return negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table
	 * @throws UnknownValueException if negative coverage is unknown (not stored in these characteristics) and cannot be computed
	 *         as either support or coverage is also unknown
	 */
	public int getNegativeCoverage() {
		if (negativeCoverage == UNKNOWN_INT_VALUE) {
			try {
				negativeCoverage = getCoverage() - getSupport(); 
			} catch (UnknownValueException exception) {
				throw new UnknownValueException("Rule's negative coverage is unknown and cannot be computed.");
			}
		}
		return negativeCoverage;
	}

	/**
	 * Sets negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table.
	 * An object is negative, if its decision part does not match rule's decision part.
	 * In order to forget stored negative coverage, one can invoke this method with {@link #UNKNOWN_INT_VALUE}.
	 * 
	 * @param negativeCoverage negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table
	 * @throws InvalidValueException if given negative coverage is smaller than zero
	 */
	public void setNegativeCoverage(int negativeCoverage) {
		if (negativeCoverage != UNKNOWN_INT_VALUE) {
			nonNegative(negativeCoverage, "Rule's negative coverage has to be >= 0."); //assert that characteristic satisfies constraint(s)
		}
		this.negativeCoverage = negativeCoverage;
	}

	/**
	 * Gets value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule consistency measure $\epsilon$ is unknown (not stored in these characteristics)
	 */
	public double getEpsilon() {
		return known(epsilon, UNKNOWN_DOUBLE_VALUE, "Value of rule consistency measure epsilon is unknown.");
	}

	/**
	 * Sets value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of rule consistency measure $\epsilon$, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param epsilon value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule consistency measure $\epsilon$ is not within [0,1] interval
	 */
	public void setEpsilon(double epsilon) {
		if (epsilon != UNKNOWN_DOUBLE_VALUE) {
			within01Interval(epsilon, "Value of rule consistency measure epsilon has to be within [0,1] interval."); //assert that characteristic satisfies constraint(s)
		}
		this.epsilon = epsilon;
	}

	/**
	 * Gets value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule consistency measure $\epsilon'$ is unknown (not stored in these characteristics)
	 */
	public double getEpsilonPrime() {
		return known(epsilonPrim, UNKNOWN_DOUBLE_VALUE, "Value of rule consistency measure epsilon' is unknown.");
	}
	
	/**
	 * Sets value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of rule consistency measure $\epsilon'$, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param epsilonPrim value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule consistency measure $\epsilon'$ is lower than zero 
	 */
	public void setEpsilonPrime(double epsilonPrim) {
		if (epsilonPrim != UNKNOWN_DOUBLE_VALUE) {
			nonNegative(epsilonPrim, "Value of rule consistency measure epsilon' has to be >= 0."); //assert that characteristic satisfies constraint(s)
		}
		this.epsilonPrim = epsilonPrim;
	}

	/**
	 * Gets value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure $f$ is unknown (not stored in these characteristics)
	 */
	public double getFConfirmation() {
		return known(fConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'f' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param fConfirmation value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table
	 */
	public void setFConfirmation(double fConfirmation) {
		this.fConfirmation = fConfirmation; //TODO: analyze if any checking is necessary here
	}

	/**
	 * Gets value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure $a$ is unknown (not stored in these characteristics)
	 */
	public double getAConfirmation() {
		return known(aConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'a' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param aConfirmation value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule confirmation measure 'a' is outside interval [-1,1]
	 */
	public void setAConfirmation(double aConfirmation) {
		if (aConfirmation != UNKNOWN_DOUBLE_VALUE) { //do validation
			withinMinus1Plus1Interval(aConfirmation, "Value of rule confirmation measure 'a' has to be within interval [-1,1].");
		}
		this.aConfirmation = aConfirmation;
	}

	/**
	 * Gets value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure $z$ is unknown (not stored in these characteristics)
	 */
	public double getZConfirmation() {
		return known(zConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'z' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param zConfirmation value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule confirmation measure 'z' is outside interval [-1,1]
	 */
	public void setZConfirmation(double zConfirmation) {
		if (zConfirmation != UNKNOWN_DOUBLE_VALUE) { //do validation
			withinMinus1Plus1Interval(zConfirmation, "Value of rule confirmation measure 'z' has to be within interval [-1,1].");
		}
		this.zConfirmation = zConfirmation;
	}

	/**
	 * Gets value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure $l$ is unknown (not stored in these characteristics)
	 */
	public double getLConfirmation() {
		return known(lConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'l' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param lConfirmation value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table
	 */
	public void setLConfirmation(double lConfirmation) {
		this.lConfirmation = lConfirmation; //TODO: analyze if any checking is necessary here
	}

	/**
	 * Gets value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure $c_1$ is unknown (not stored in these characteristics)
	 */
	public double getC1Confirmation() {
		return known(c1Confirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'c_1' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param c1Confirmation value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table
	 */
	public void setC1Confirmation(double c1Confirmation) {
		this.c1Confirmation = c1Confirmation; //TODO: analyze if any checking is necessary here
	}
	
	/**
	 * Gets value of rule confirmation measure $s$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure $s$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure $s$ is unknown (not stored in these characteristics)
	 */
	public double getSConfirmation() {
		return known(sConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 's' is unknown.");
	}
	
	/**
	 * Sets value of rule confirmation measure $s$ calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param sConfirmation value of rule confirmation measure $s$ calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule confirmation measure 's' is outside interval [-1,1]
	 */
	public void setSConfirmation(double sConfirmation) {
		if (sConfirmation != UNKNOWN_DOUBLE_VALUE) { //do validation
			withinMinus1Plus1Interval(sConfirmation, "Value of rule confirmation measure 's' has to be within interval [-1,1].");
		}
		this.sConfirmation = sConfirmation;
	}
	
}
