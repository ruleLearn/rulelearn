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

import static org.rulelearn.core.Precondition.known;
import static org.rulelearn.core.Precondition.nonNegative;
import static org.rulelearn.core.Precondition.within01Interval;
import static org.rulelearn.core.Precondition.withinMinus1Plus1Interval;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.UnknownValueException;

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
	 * Set to {@link Double#MAX_VALUE}.
	 */
	public static final double UNKNOWN_DOUBLE_VALUE = Double.MAX_VALUE;
	
	//initialization of all characteristics as unknown
	/**
	 * Support (positive coverage) of a decision rule (i.e., number of positive objects covered by the rule) in the context of an information table.
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
	 * An object is negative, if its decision part does not match rule's decision part (and also its decision is not neutral w.r.t. rule's decision part).
	 */
	protected int negativeCoverage = UNKNOWN_INT_VALUE;
	/**
	 * Value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table.
	 */
	protected double epsilon = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table.
	 */
	protected double epsilonPrime = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure F calculated for a decision rule in the context of an information table.
	 */
	protected double fConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure A calculated for a decision rule in the context of an information table.
	 */
	protected double aConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure Z calculated for a decision rule in the context of an information table.
	 */
	protected double zConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure L calculated for a decision rule in the context of an information table.
	 */
	protected double lConfirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure c<sub>1</sub> calculated for a decision rule in the context of an information table.
	 */
	protected double c1Confirmation = UNKNOWN_DOUBLE_VALUE;
	/**
	 * Value of rule confirmation measure S calculated for a decision rule in the context of an information table.
	 */
	protected double sConfirmation = UNKNOWN_DOUBLE_VALUE;
	
	/**
	 * Number of elementary conditions in the rule.
	 */
	protected int numberOfConditions = UNKNOWN_INT_VALUE;
	
	/**
	 * Basic rule coverage information concerning considered decision rule. Equals {@code null} by default.
	 * Can be set in constructor or later, using respective setter.
	 */
	BasicRuleCoverageInformation ruleCoverageInformation = null;
	
	/**
	 * Sole constructor.
	 */
	public RuleCharacteristics() {}
	
	/**
	 * Constructor storing basic rule coverage information concerning considered decision rule.
	 * 
	 * @param ruleCoverageInformation basic rule coverage information to be stored, for future use
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public RuleCharacteristics(BasicRuleCoverageInformation ruleCoverageInformation) {
		this.ruleCoverageInformation = Precondition.notNull(ruleCoverageInformation, "Basic rule coverage information for rule characteristics is null.");
	}
	
	/**
	 * Gets basic rule coverage information concerning considered decision rule.
	 * Can be {@code null} if not set in constructor or using respective setter.
	 * 
	 * @return basic rule coverage information
	 */
	public BasicRuleCoverageInformation getRuleCoverageInformation() {
		return this.ruleCoverageInformation;
	}
	
	/**
	 * Sets basic rule coverage information concerning considered decision rule.
	 * 
	 * @param ruleCoverageInformation basic rule coverage information, for future use
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public void setRuleCoverageInformation(BasicRuleCoverageInformation ruleCoverageInformation) {
		this.ruleCoverageInformation = Precondition.notNull(ruleCoverageInformation, "Basic rule coverage information for rule characteristics is null.");
	}
	
	/**
	 * Gets support of a decision rule (i.e., number of positive objects covered by the rule) in the context of an information table.
	 * 
	 * @return support of a decision rule (i.e., number of positive objects covered by the rule) in the context of an information table
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
	 * Checks whether support is set.
	 * @return {@code true} if support is set or {@code false} otherwise
	 */
	public boolean isSupportSet() {
		return (support != UNKNOWN_INT_VALUE);
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
	 * Checks whether strength is set.
	 * @return {@code true} if strength is set or {@code false} otherwise
	 */
	public boolean isStrengthSet() {
		return (strength != UNKNOWN_DOUBLE_VALUE);
	}

	/**
	 * Gets confidence of a decision rule in the context of an information table.
	 * 
	 * @return confidence confidence of a decision rule in the context of an information table
	 * @throws UnknownValueException if confidence is unknown (not stored in these characteristics)
	 */
	public double getConfidence() {
		return known(confidence, UNKNOWN_DOUBLE_VALUE, "Rule's confidence is unknown.");
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
	 * Checks whether confidence is set.
	 * @return {@code true} if confidence is set or {@code false} otherwise
	 */
	public boolean isConfidenceSet() {
		return (confidence != UNKNOWN_DOUBLE_VALUE);
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
	 * Checks whether coverage factor is set.
	 * @return {@code true} if coverage factor is set or {@code false} otherwise
	 */
	public boolean isCoverageFactorSet() {
		return (coverageFactor != UNKNOWN_DOUBLE_VALUE);
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
	 * Checks whether coverage is set.
	 * @return {@code true} if coverage is set or {@code false} otherwise
	 */
	public boolean isCoverageSet() {
		return (coverage != UNKNOWN_INT_VALUE);
	}
	
	/**
	 * Gets negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table.
	 * An object is negative, if its decision does not match rule's decision part (and also its decision is not neutral w.r.t. rule's decision part).
	 * 
	 * @return negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table
	 * @throws UnknownValueException if negative coverage is unknown (not stored in these characteristics)
	 */
	public int getNegativeCoverage() {
		return known(negativeCoverage, UNKNOWN_INT_VALUE, "Rule's negative coverage is unknown.");
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
	 * Checks whether negative coverage is set.
	 * @return {@code true} if negative coverage is set or {@code false} otherwise
	 */
	public boolean isNegativeCoverageSet() {
		return (negativeCoverage != UNKNOWN_INT_VALUE);
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
	 * Checks whether epsilon is set.
	 * @return {@code true} if epsilon is set or {@code false} otherwise
	 */
	public boolean isEpsilonSet() {
		return (epsilon != UNKNOWN_DOUBLE_VALUE);
	}

	/**
	 * Gets value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule consistency measure $\epsilon'$ is unknown (not stored in these characteristics)
	 */
	public double getEpsilonPrime() {
		return known(epsilonPrime, UNKNOWN_DOUBLE_VALUE, "Value of rule consistency measure epsilon' is unknown.");
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
		this.epsilonPrime = epsilonPrim;
	}

	/**
	 * Checks whether epsilon prime is set.
	 * @return {@code true} if epsilon prime is set or {@code false} otherwise
	 */
	public boolean isEpsilonPrimeSet() {
		return (epsilonPrime != UNKNOWN_DOUBLE_VALUE);
	}
	
	/**
	 * Gets value of rule confirmation measure F calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure F calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure F is unknown (not stored in these characteristics)
	 */
	public double getFConfirmation() {
		return known(fConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'F' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure F calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param fConfirmation value of rule confirmation measure F calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule confirmation measure F is outside interval [-1,1]
	 */
	public void setFConfirmation(double fConfirmation) {
		if (fConfirmation != UNKNOWN_DOUBLE_VALUE) { //do validation
			withinMinus1Plus1Interval(fConfirmation, "Value of rule confirmation measure 'F' has to be within interval [-1,1].");
		}
		this.fConfirmation = fConfirmation;
	}

	/**
	 * Checks whether rule confirmation measure F is set.
	 * @return {@code true} if rule confirmation measure F is set or {@code false} otherwise
	 */
	public boolean isFConfirmationSet() {
		return (fConfirmation != UNKNOWN_DOUBLE_VALUE);
	}
	
	/**
	 * Gets value of rule confirmation measure A calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure A calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure A is unknown (not stored in these characteristics)
	 */
	public double getAConfirmation() {
		return known(aConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'A' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure A calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param aConfirmation value of rule confirmation measure A calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule confirmation measure 'A' is outside interval [-1,1]
	 */
	public void setAConfirmation(double aConfirmation) {
		if (aConfirmation != UNKNOWN_DOUBLE_VALUE) { //do validation
			withinMinus1Plus1Interval(aConfirmation, "Value of rule confirmation measure 'A' has to be within interval [-1,1].");
		}
		this.aConfirmation = aConfirmation;
	}

	/**
	 * Checks whether rule confirmation measure A is set.
	 * @return {@code true} if rule confirmation measure A is set or {@code false} otherwise
	 */
	public boolean isAConfirmationSet() {
		return (aConfirmation != UNKNOWN_DOUBLE_VALUE);
	}
	
	/**
	 * Gets value of rule confirmation measure Z calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure Z calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure Z is unknown (not stored in these characteristics)
	 */
	public double getZConfirmation() {
		return known(zConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'Z' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure Z calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param zConfirmation value of rule confirmation measure Z calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule confirmation measure 'Z' is outside interval [-1,1]
	 */
	public void setZConfirmation(double zConfirmation) {
		if (zConfirmation != UNKNOWN_DOUBLE_VALUE) { //do validation
			withinMinus1Plus1Interval(zConfirmation, "Value of rule confirmation measure 'Z' has to be within interval [-1,1].");
		}
		this.zConfirmation = zConfirmation;
	}
	
	/**
	 * Checks whether rule confirmation measure Z is set.
	 * @return {@code true} if rule confirmation measure Z is set or {@code false} otherwise
	 */
	public boolean isZConfirmationSet() {
		return (zConfirmation != UNKNOWN_DOUBLE_VALUE);
	}

	/**
	 * Gets value of rule confirmation measure L calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure L calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure L is unknown (not stored in these characteristics)
	 */
	public double getLConfirmation() {
		return known(lConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'L' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure L calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param lConfirmation value of rule confirmation measure L calculated for a decision rule in the context of an information table
	 */
	public void setLConfirmation(double lConfirmation) {
		this.lConfirmation = lConfirmation;
	}

	/**
	 * Checks whether rule confirmation measure L is set.
	 * @return {@code true} if rule confirmation measure L is set or {@code false} otherwise
	 */
	public boolean isLConfirmationSet() {
		return (lConfirmation != UNKNOWN_DOUBLE_VALUE);
	}
	
	/**
	 * Gets value of rule confirmation measure c<sub>1</sub> calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure c<sub>1</sub> calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure c<sub>1</sub> is unknown (not stored in these characteristics)
	 */
	public double getC1Confirmation() {
		return known(c1Confirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'c_1' is unknown.");
	}

	/**
	 * Sets value of rule confirmation measure c<sub>1</sub> calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param c1Confirmation value of rule confirmation measure c<sub>1</sub> calculated for a decision rule in the context of an information table
	 */
	public void setC1Confirmation(double c1Confirmation) {
		this.c1Confirmation = c1Confirmation; //no additional validation possible in the general case, with any values of measure's parameters alpha and beta
	}
	
	/**
	 * Checks whether rule confirmation measure c<sub>1</sub> is set.
	 * @return {@code true} if rule confirmation measure c<sub>1</sub> is set or {@code false} otherwise
	 */
	public boolean isC1ConfirmationSet() {
		return (c1Confirmation != UNKNOWN_DOUBLE_VALUE);
	}
	
	/**
	 * Gets value of rule confirmation measure S calculated for a decision rule in the context of an information table.
	 * 
	 * @return value of rule confirmation measure S calculated for a decision rule in the context of an information table
	 * @throws UnknownValueException if value of rule confirmation measure S is unknown (not stored in these characteristics)
	 */
	public double getSConfirmation() {
		return known(sConfirmation, UNKNOWN_DOUBLE_VALUE, "Value of rule confirmation measure 'S' is unknown.");
	}
	
	/**
	 * Sets value of rule confirmation measure S calculated for a decision rule in the context of an information table.
	 * In order to forget stored value of this rule confirmation measure, one can invoke this method with {@link #UNKNOWN_DOUBLE_VALUE}.
	 * 
	 * @param sConfirmation value of rule confirmation measure S calculated for a decision rule in the context of an information table
	 * @throws InvalidValueException if given value of rule confirmation measure 'S' is outside interval [-1,1]
	 */
	public void setSConfirmation(double sConfirmation) {
		if (sConfirmation != UNKNOWN_DOUBLE_VALUE) { //do validation
			withinMinus1Plus1Interval(sConfirmation, "Value of rule confirmation measure 'S' has to be within interval [-1,1].");
		}
		this.sConfirmation = sConfirmation;
	}
	
	/**
	 * Checks whether rule confirmation measure S is set.
	 * @return {@code true} if rule confirmation measure S is set or {@code false} otherwise
	 */
	public boolean isSConfirmationSet() {
		return (sConfirmation != UNKNOWN_DOUBLE_VALUE);
	}
	
	/**
	 * Gets the number of elementary conditions in the rule.
	 * 
	 * @return the number of elementary conditions in the rule
	 * @throws UnknownValueException if the number of elementary conditions in the rule is unknown (not stored in these characteristics)
	 */
	public int getNumberOfConditions() {
		return known(numberOfConditions, UNKNOWN_INT_VALUE, "Value of rule length (number of conditions) is unknown.");
	}
	
	/**
	 * Sets the number of elementary conditions in the rule.
	 * 
	 * @param numberOfConditions the number of elementary conditions in the rule
	 * @throws InvalidValueException if given number of elementary conditions in negative
	 */
	public void setNumberOfConditions(int numberOfConditions) {
		if (numberOfConditions != UNKNOWN_INT_VALUE) {
			nonNegative(numberOfConditions, "Rule's number of conditions has to be >= 0."); //assert that characteristic satisfies constraint(s)
		}
		this.numberOfConditions = numberOfConditions;
	}
	
	/**
	 * Checks whether the number of elementary conditions in the rule is set.
	 * @return {@code true} if the number of elementary conditions in the rule is set or {@code false} otherwise
	 */
	public boolean isNumberOfConditionsSet() {
		return (numberOfConditions != UNKNOWN_INT_VALUE);
	}
	
	/**
	 * Tells if the characteristic returned by the given getter is set. Exemplary usage:<br>
	 * {@code RuleCharacteristics ruleCharacteristics = new RuleCharacteristics();}<br>
	 * {@code boolean isSet = ruleCharacteristics.isCharacteristicSet(ruleCharacteristics::getSupport);}
	 * 
	 * @param intCharacteristicGetter reference to a getter returning an integer characteristic stored in this object
	 * @return {@code true} if the characteristic returned by the given getter is set, {@code false} otherwise
	 * 
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public boolean isCharacteristicSet(IntSupplier intCharacteristicGetter) {
		try {
			intCharacteristicGetter.getAsInt();
			return true;
		} catch (UnknownValueException exception) {
			return false;
		} catch (NullPointerException exception) {
			throw new NullPointerException("Integer-type characteristic getter is null.");
		}
	}
	
	/**
	 * Tells if the characteristic returned by the given getter is set. Exemplary usage:<br>
	 * {@code RuleCharacteristics ruleCharacteristics = new RuleCharacteristics();}<br>
	 * {@code boolean isSet = ruleCharacteristics.isCharacteristicSet(ruleCharacteristics::getEpsilon);}
	 * 
	 * @param doubleCharacteristicGetter reference to a getter returning a floating-point characteristic stored in this object
	 * @return {@code true} if the characteristic returned by the given getter is set, {@code false} otherwise
	 * 
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public boolean isCharacteristicSet(DoubleSupplier doubleCharacteristicGetter) {
		try {
			doubleCharacteristicGetter.getAsDouble();
			return true;
		} catch (UnknownValueException exception) {
			return false;
		} catch (NullPointerException exception) {
			throw new NullPointerException("Floating-point-type characteristic getter is null.");
		}
	}
	
}
