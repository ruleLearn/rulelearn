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
	public static final int UNKNOWN_INT_VALUE = -1;
	
	/**
	 * Default value of all floating-point characteristics, indicating that given characteristic is unknown, i.e., it has not been set or calculated yet.
	 */
	public static final double UNKNOWN_DOUBLE_VALUE = Double.MIN_VALUE;
	
	//initialization of all characteristics as unknown
	/**
	 * Support of a decision rule in the context of an information table.
	 */
	protected int support = UNKNOWN_INT_VALUE; //can be also called positive coverage
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
	 * Sole constructor.
	 */
	public RuleCharacteristics() {}
	
	//TOD: validate input values in setters

	/**
	 * Gets support of a decision rule in the context of an information table.
	 * @return support of a decision rule in the context of an information table.
	 */
	public int getSupport() {
		if (support == UNKNOWN_INT_VALUE) {
			throw new UnknownValueException("Rule's support is unknown.");
		}
		return support;
	}

	/**
	 * Sets support of a decision rule in the context of an information table.
	 * @param support support of a decision rule in the context of an information table.
	 */
	public void setSupport(int support) {
		this.support = support;
	}

	/**
	 * Gets strength of a decision rule in the context of an information table.
	 * @return strength of a decision rule in the context of an information table.
	 */
	public double getStrength() {
		if (strength == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Rule's strength is unknown.");
		}
		return strength;
	}

	/**
	 * Sets strength of a decision rule in the context of an information table.
	 * @param strength strength of a decision rule in the context of an information table.
	 */
	public void setStrength(double strength) {
		this.strength = strength;
	}

	/**
	 * Gets confidence of a decision rule in the context of an information table.
	 * @return confidence confidence of a decision rule in the context of an information table.
	 */
	public double getConfidence() {
		if (confidence == UNKNOWN_DOUBLE_VALUE) {
			//TODO: try to calculate using support/coverage, and throw an exception only if that try fails
			throw new UnknownValueException("Rule's confidence is unknown.");
		}
		return confidence;
	}

	/**
	 * Sets confidence of a decision rule in the context of an information table.
	 * @param confidence confidence of a decision rule in the context of an information table
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	/**
	 * Gets coverage factor of a decision rule in the context of an information table.
	 * @return coverage factor of a decision rule in the context of an information table
	 */
	public double getCoverageFactor() {
		if (coverageFactor == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Rule's coverage factor is unknown.");
		}
		return coverageFactor;
	}

	/**
	 * Sets coverage factor of a decision rule in the context of an information table.
	 * @param coverageFactor coverage factor of a decision rule in the context of an information table
	 */
	public void setCoverageFactor(double coverageFactor) {
		this.coverageFactor = coverageFactor;
	}

	/**
	 * Gets coverage of a decision rule (number of objects covered by the rule) in the context of an information table.
	 * @return coverage of a decision rule (number of objects covered by the rule) in the context of an information table
	 */
	public int getCoverage() {
		if (coverage == UNKNOWN_INT_VALUE) {
			//TODO: try to calculate using negativeCoverage + support, and throw an exception only if that try fails
			throw new UnknownValueException("Rule's coverage is unknown.");
		}
		return coverage;
	}

	/**
	 * Sets coverage of a decision rule (number of objects covered by the rule) in the context of an information table.
	 * @param coverage coverage of a decision rule (number of objects covered by the rule) in the context of an information table
	 */
	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}

	/**
	 * Gets negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table.
	 * An object is negative, if its decision part does not match rule's decision part.
	 * 
	 * @return negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table
	 */
	public int getNegativeCoverage() {
		if (negativeCoverage == UNKNOWN_INT_VALUE) {
			//TODO: try to calculate using coverage - support, and throw an exception only if that try fails
			throw new UnknownValueException("Rule's negative coverage is unknown.");
		}
		return negativeCoverage;
	}

	/**
	 * Sets negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table.
	 * An object is negative, if its decision part does not match rule's decision part.
	 * 
	 * @param negativeCoverage negative coverage of a decision rule (number of negative objects covered by the rule) in the context of an information table
	 */
	public void setNegativeCoverage(int negativeCoverage) {
		this.negativeCoverage = negativeCoverage;
	}

	/**
	 * Gets value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table.
	 * @return value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table
	 */
	public double getEpsilon() {
		if (epsilon == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Value of epsilon rule consistency measure is unknown.");
		}
		return epsilon;
	}

	/**
	 * Sets value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table.
	 * @param epsilon value of rule consistency measure $\epsilon$ calculated for a decision rule in the context of an information table
	 */
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * Gets value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table.
	 * @return value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table
	 */
	public double getEpsilonPrim() {
		if (epsilonPrim == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Rule's epsilon' consistency measure value is unknown.");
		}
		return epsilonPrim;
	}

	/**
	 * Sets value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table.
	 * @param epsilonPrim value of rule consistency measure $\epsilon'$ calculated for a decision rule in the context of an information table
	 */
	public void setEpsilonPrim(double epsilonPrim) {
		this.epsilonPrim = epsilonPrim;
	}

	/**
	 * Gets value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table.
	 * @return value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table.
	 */
	public double getfConfirmation() {
		if (fConfirmation == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Value of rule's confirmation measure f is unknown.");
		}
		return fConfirmation;
	}

	/**
	 * Sets value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table.
	 * @param fConfirmation value of rule confirmation measure $f$ calculated for a decision rule in the context of an information table
	 */
	public void setfConfirmation(double fConfirmation) {
		this.fConfirmation = fConfirmation;
	}

	/**
	 * Gets value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table.
	 * @return value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table.
	 */
	public double getAConfirmation() {
		if (aConfirmation == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Value of rule's confirmation measure a is unknown.");
		}
		return aConfirmation;
	}

	/**
	 * Sets value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table.
	 * @param aConfirmation value of rule confirmation measure $a$ calculated for a decision rule in the context of an information table
	 */
	public void setAConfirmation(double aConfirmation) {
		this.aConfirmation = aConfirmation;
	}

	/**
	 * Gets value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table.
	 * @return value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table.
	 */
	public double getzConfirmation() {
		if (zConfirmation == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Value of rule's confirmation measure z is unknown.");
		}
		return zConfirmation;
	}

	/**
	 * Sets value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table.
	 * @param zConfirmation value of rule confirmation measure $z$ calculated for a decision rule in the context of an information table
	 */
	public void setzConfirmation(double zConfirmation) {
		this.zConfirmation = zConfirmation;
	}

	/**
	 * Gets value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table.
	 * @return value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table
	 */
	public double getlConfirmation() {
		if (lConfirmation == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Value of rule's confirmation measure l is unknown.");
		}
		return lConfirmation;
	}

	/**
	 * Sets value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table.
	 * @param lConfirmation value of rule confirmation measure $l$ calculated for a decision rule in the context of an information table
	 */
	public void setlConfirmation(double lConfirmation) {
		this.lConfirmation = lConfirmation;
	}

	/**
	 * Gets value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table.
	 * @return value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table
	 */
	public double getC1Confirmation() {
		if (c1Confirmation == UNKNOWN_DOUBLE_VALUE) {
			throw new UnknownValueException("Value of rule's confirmation measure c1 is unknown.");
		}
		return c1Confirmation;
	}

	/**
	 * Sets value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table.
	 * @param c1Confirmation value of rule confirmation measure $c_1$ calculated for a decision rule in the context of an information table.
	 */
	public void setC1Confirmation(double c1Confirmation) {
		this.c1Confirmation = c1Confirmation;
	}
}
