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

import java.util.function.Function;

import org.rulelearn.core.InvalidValueException;

/**
 * Rule characteristic enumeration. Different singletons correspond to characteristics handled by {@link RuleCharacteristics} class.
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public enum RuleCharacteristic {
	SUPPORT,
	STRENGTH,
	CONFIDENCE,
	COVERAGE_FACTOR,
	COVERAGE,
	NEGATIVE_COVERAGE,
	EPSILON,
	EPSILON_PRIME,
	F,
	A,
	Z,
	L,
	C1,
	S,
	LENGTH;
	
	/**
	 * Textual representation of {@link #SUPPORT} value.
	 */
	public static final String support = "support";
	/**
	 * Textual representation of {@link #STRENGTH} value.
	 */
	public static final String strength = "strength";
	/**
	 * Textual representation of {@link #CONFIDENCE} value.
	 */
	public static final String confidence = "confidence";
	/**
	 * Textual representation of {@link #COVERAGE_FACTOR} value.
	 */
	public static final String coverageFactor = "coverage-factor";
	/**
	 * Textual representation of {@link #COVERAGE} value.
	 */
	public static final String coverage = "coverage";
	/**
	 * Textual representation of {@link #NEGATIVE_COVERAGE} value.
	 */
	public static final String negativeCoverage = "negative-coverage";
	/**
	 * Textual representation of {@link #EPSILON} value.
	 */
	public static final String epsilon = "epsilon";
	/**
	 * Textual representation of {@link #EPSILON_PRIME} value.
	 */
	public static final String epsilonPrime = "epsilon'";
	/**
	 * Textual representation of {@link #F} value.
	 */
	public static final String f = "F";
	/**
	 * Textual representation of {@link #A} value.
	 */
	public static final String a = "A";
	/**
	 * Textual representation of {@link #Z} value.
	 */
	public static final String z = "Z";
	/**
	 * Textual representation of {@link #L} value.
	 */
	public static final String l = "L";
	/**
	 * Textual representation of {@link #C1} value.
	 */
	public static final String c1 = "c1";
	/**
	 * Textual representation of {@link #S} value.
	 */
	public static final String s = "S";
	/**
	 * Textual representation of {@link #LENGTH} value.
	 */
	public static final String length = "length";
	
	/**
	 * Gets reference to method of {@link RuleCharacteristics} that calculates given characteristic.
	 * 
	 * @return reference to method of {@link RuleCharacteristics} that calculates given characteristic
	 */
	public Function<RuleCharacteristics, Number> getCalculationMethod() {
		switch(this) {
		case SUPPORT:
			return RuleCharacteristics::getSupport;
		case STRENGTH:
			return RuleCharacteristics::getStrength;
		case CONFIDENCE:
			return RuleCharacteristics::getConfidence;
		case COVERAGE_FACTOR:
			return RuleCharacteristics::getCoverageFactor;
		case COVERAGE:
			return RuleCharacteristics::getCoverage;
		case NEGATIVE_COVERAGE:
			return RuleCharacteristics::getNegativeCoverage;
		case EPSILON:
			return RuleCharacteristics::getEpsilon;
		case EPSILON_PRIME:
			return RuleCharacteristics::getEpsilonPrime;
		case F:
			return RuleCharacteristics::getFConfirmation;
		case A:
			return RuleCharacteristics::getAConfirmation;
		case Z:
			return RuleCharacteristics::getZConfirmation;
		case L:
			return RuleCharacteristics::getLConfirmation;
		case C1:
			return RuleCharacteristics::getC1Confirmation;
		case S:
			return RuleCharacteristics::getSConfirmation;
		case LENGTH:
			return RuleCharacteristics::getNumberOfConditions;
		default:
			throw new InvalidValueException("Not supported rule characteristic."); //this should not happen
		}
	}
	
	/**
	 * Gets textual representation of this rule characteristic.
	 * 
	 * @return textual representation of this rule characteristic
	 */
	@Override
	public String toString() {
		switch(this) {
		case SUPPORT:
			return support;
		case STRENGTH:
			return strength;
		case CONFIDENCE:
			return confidence;
		case COVERAGE_FACTOR:
			return coverageFactor;
		case COVERAGE:
			return coverage;
		case NEGATIVE_COVERAGE:
			return negativeCoverage;
		case EPSILON:
			return epsilon;
		case EPSILON_PRIME:
			return epsilonPrime;
		case F:
			return f;
		case A:
			return a;
		case Z:
			return z;
		case L:
			return l;
		case C1:
			return c1;
		case S:
			return s;
		case LENGTH:
			return length;
		default:
			throw new InvalidValueException("Not supported rule characteristic."); //this should not happen
		}
	}
	
	/**
	 * Gets name of this rule characteristic, which is the same as its {@link #toString() textual representation}.
	 * 
	 * @return name of this rule characteristic
	 */
	public String getName() {
		return toString();
	}
	
	/**
	 * Constructs this rule characteristic using given textual representation.
	 * 
	 * @param ruleCharacteristicName textual representation of rule characteristic; should be equal (ignoring case!) to one of the public static fields of this enum 
	 * @return constructed rule characteristic
	 * @throws InvalidValueException if given text does not correspond to any supported rule characteristic
	 */
	public static RuleCharacteristic of(String ruleCharacteristicName) {
		if (ruleCharacteristicName.equalsIgnoreCase(support)) {return SUPPORT;}
		if (ruleCharacteristicName.equalsIgnoreCase(strength)) {return STRENGTH;}
		if (ruleCharacteristicName.equalsIgnoreCase(confidence)) {return CONFIDENCE;}
		if (ruleCharacteristicName.equalsIgnoreCase(coverageFactor)) {return COVERAGE_FACTOR;}
		if (ruleCharacteristicName.equalsIgnoreCase(coverage)) {return COVERAGE;}
		if (ruleCharacteristicName.equalsIgnoreCase(negativeCoverage)) {return NEGATIVE_COVERAGE;}
		if (ruleCharacteristicName.equalsIgnoreCase(epsilon)) {return EPSILON;}
		if (ruleCharacteristicName.equalsIgnoreCase(epsilonPrime)) {return EPSILON_PRIME;}
		if (ruleCharacteristicName.equalsIgnoreCase(f)) {return F;}
		if (ruleCharacteristicName.equalsIgnoreCase(a)) {return A;}
		if (ruleCharacteristicName.equalsIgnoreCase(z)) {return Z;}
		if (ruleCharacteristicName.equalsIgnoreCase(l)) {return L;}
		if (ruleCharacteristicName.equalsIgnoreCase(c1)) {return C1;}
		if (ruleCharacteristicName.equalsIgnoreCase(s)) {return S;}
		if (ruleCharacteristicName.equalsIgnoreCase(length)) {return LENGTH;}
		throw new InvalidValueException("Not supported textual representation of rule characteristic.");
	}
	
}
