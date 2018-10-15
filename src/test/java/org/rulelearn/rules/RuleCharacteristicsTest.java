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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.UnknownValueException;

/**
 * Tests for {@link RuleCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleCharacteristicsTest {
	
	private RuleCharacteristics ruleCharacteristics;
	
	/**
	 * Initializes rule characteristics before each unit test.
	 */
	@BeforeEach
	private void setup() {
		this.ruleCharacteristics = new RuleCharacteristics();
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getSupport()}.
	 */
	@Test
	void testGetSupport() {
		try {
			ruleCharacteristics.getSupport();
			fail("Should not get unknown support.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSupport(int)}.
	 */
	@Test
	void testSetSupport01() {
		try {
			ruleCharacteristics.setSupport(-1);
			fail("Should not set invalid support.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSupport(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getSupport()}.
	 */
	@Test
	void testSetSupport02() {
		ruleCharacteristics.setSupport(RuleCharacteristics.UNKNOWN_INT_VALUE);
		
		try {
			ruleCharacteristics.getSupport();
			fail("Should not get unknown support.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSupport(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getSupport()}.
	 */
	@Test
	void testSetSupport03() {
		int support = 0;
		ruleCharacteristics.setSupport(support);
		assertEquals(ruleCharacteristics.getSupport(), support);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSupport(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getSupport()}.
	 */
	@Test
	void testSetSupport04() {
		int support = 2;
		ruleCharacteristics.setSupport(support);
		assertEquals(ruleCharacteristics.getSupport(), support);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isSupportSet()}.
	 */
	@Test
	void testIsSupportSet() {
		ruleCharacteristics.setSupport(RuleCharacteristics.UNKNOWN_INT_VALUE);
		assertEquals(false, ruleCharacteristics.isSupportSet());
		
		ruleCharacteristics.setSupport(1);
		assertEquals(true, ruleCharacteristics.isSupportSet());
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getStrength()}.
	 */
	@Test
	void testGetStrength() {
		try {
			ruleCharacteristics.getStrength();
			fail("Should not get unknown strength.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setStrength(double)}.
	 */
	@Test
	void testSetStrength01a() {
		try {
			ruleCharacteristics.setStrength(-0.001);
			fail("Should not set invalid strength.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setStrength(double)}.
	 */
	@Test
	void testSetStrength01b() {
		try {
			ruleCharacteristics.setStrength(1.001);
			fail("Should not set invalid strength.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setStrength(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getStrength()}.
	 */
	@Test
	void testSetStrength02() {
		ruleCharacteristics.setStrength(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getStrength();
			fail("Should not get unknown strength.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setStrength(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getStrength()}.
	 */
	@Test
	void testSetStrength03() {
		double strength = 0;
		ruleCharacteristics.setStrength(strength);
		assertEquals(ruleCharacteristics.getStrength(), strength);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setStrength(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getStrength()}.
	 */
	@Test
	void testSetStrength04() {
		double strength = 1;
		ruleCharacteristics.setStrength(strength);
		assertEquals(ruleCharacteristics.getStrength(), strength);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isStrengthSet()}.
	 */
	@Test
	void testIsStrengthSet() {
		ruleCharacteristics.setStrength(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isStrengthSet());
		
		ruleCharacteristics.setStrength(1.0);
		assertEquals(true, ruleCharacteristics.isStrengthSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testGetConfidence() {
		try {
			ruleCharacteristics.getConfidence();
			fail("Should not get unknown cinfidence.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}.
	 */
	@Test
	void testSetConfidence01a() {
		try {
			ruleCharacteristics.setConfidence(-0.001);
			fail("Should not set invalid confidence.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}.
	 */
	@Test
	void testSetConfidence01b() {
		try {
			ruleCharacteristics.setConfidence(1.001);
			fail("Should not set invalid confidence.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testSetConfidence02() {
		ruleCharacteristics.setConfidence(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getConfidence();
			fail("Should not get unknown confidence.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testSetConfidence03() {
		double confidence = 0;
		ruleCharacteristics.setConfidence(confidence);
		assertEquals(ruleCharacteristics.getConfidence(), confidence);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testSetConfidence04() {
		double confidence = 1;
		ruleCharacteristics.setConfidence(confidence);
		assertEquals(ruleCharacteristics.getConfidence(), confidence);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testSetConfidence05() {
		int support = 10;
		int coverage = 20;
		ruleCharacteristics.setSupport(support);
		ruleCharacteristics.setCoverage(coverage);
		assertEquals(ruleCharacteristics.getConfidence(), (double)support / (double)coverage);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testSetConfidence06() {
		int support = 10; //coverage remains unknown
		ruleCharacteristics.setSupport(support);
		try {
			ruleCharacteristics.getConfidence();
			fail("Should not get unknown confidence.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testSetConfidence07() {
		int coverage = 20; //support remains unknown
		ruleCharacteristics.setCoverage(coverage);
		try {
			ruleCharacteristics.getConfidence();
			fail("Should not get unknown confidence.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isConfidenceSet()}.
	 */
	@Test
	void testIsConfidenceSet() {
		ruleCharacteristics.setConfidence(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isConfidenceSet());
		
		ruleCharacteristics.setConfidence(1.0);
		assertEquals(true, ruleCharacteristics.isConfidenceSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getCoverageFactor()}.
	 */
	@Test
	void testGetCoverageFactor() {
		try {
			ruleCharacteristics.getCoverageFactor();
			fail("Should not get unknown coverage factor.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverageFactor(double)}.
	 */
	@Test
	void testSetCoverageFactor01a() {
		try {
			ruleCharacteristics.setCoverageFactor(-0.001);
			fail("Should not set invalid coverage factor.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverageFactor(double)}.
	 */
	@Test
	void testSetCoverageFactor01b() {
		try {
			ruleCharacteristics.setCoverageFactor(1.001);
			fail("Should not set invalid coverage factor.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverageFactor(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getCoverageFactor()}.
	 */
	@Test
	void testSetCoverageFactor02() {
		ruleCharacteristics.setCoverageFactor(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getCoverageFactor();
			fail("Should not get unknown coverage factor.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverageFactor(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getCoverageFactor(double)}.
	 */
	@Test
	void testSetCoverageFactor03() {
		double coverageFactor = 0;
		ruleCharacteristics.setCoverageFactor(coverageFactor);
		assertEquals(ruleCharacteristics.getCoverageFactor(), coverageFactor);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverageFactor(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getCoverageFactor(double)}.
	 */
	@Test
	void testSetCoverageFactor04() {
		double coverageFactor = 1;
		ruleCharacteristics.setCoverageFactor(coverageFactor);
		assertEquals(ruleCharacteristics.getCoverageFactor(), coverageFactor);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isCoverageFactorSet()}.
	 */
	@Test
	void testIsCoverageFactorSet() {
		ruleCharacteristics.setCoverageFactor(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isCoverageFactorSet());
		
		ruleCharacteristics.setCoverageFactor(1.0);
		assertEquals(true, ruleCharacteristics.isCoverageFactorSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testGetCoverage() {
		try {
			ruleCharacteristics.getCoverage();
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverage(int)}.
	 */
	@Test
	void testSetCoverage01() {
		try {
			ruleCharacteristics.setCoverage(-1);
			fail("Should not set invalid coverage.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverage(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testSetCoverage02() {
		ruleCharacteristics.setCoverage(RuleCharacteristics.UNKNOWN_INT_VALUE);
		
		try {
			ruleCharacteristics.getCoverage();
			fail("Should not get unknown coverage.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverage(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testSetCoverage03() {
		int coverage = 0;
		ruleCharacteristics.setCoverage(coverage);
		assertEquals(ruleCharacteristics.getCoverage(), coverage);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverage(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testSetCoverage04() {
		int coverage = 2;
		ruleCharacteristics.setCoverage(coverage);
		assertEquals(ruleCharacteristics.getCoverage(), coverage);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isCoverageSet()}.
	 */
	@Test
	void testIsCoverageSet() {
		ruleCharacteristics.setCoverage(RuleCharacteristics.UNKNOWN_INT_VALUE);
		assertEquals(false, ruleCharacteristics.isCoverageSet());
		
		ruleCharacteristics.setCoverage(1);
		assertEquals(true, ruleCharacteristics.isCoverageSet());
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testGetNegativeCoverage() {
		try {
			ruleCharacteristics.getNegativeCoverage();
			fail("Should not get unknown negative coverage.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setNegativeCoverage(int)}.
	 */
	@Test
	void testSetNegativeCoverage01() {
		try {
			ruleCharacteristics.setNegativeCoverage(-1);
			fail("Should not set invalid negative coverage.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setNegativeCoverage(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testSetNegativeCoverage02() {
		ruleCharacteristics.setNegativeCoverage(RuleCharacteristics.UNKNOWN_INT_VALUE);
		
		try {
			ruleCharacteristics.getNegativeCoverage();
			fail("Should not get unknown negative coverage.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setNegativeCoverage(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testSetNegativeCoverage03() {
		int negativeCoverage = 0;
		ruleCharacteristics.setNegativeCoverage(negativeCoverage);
		assertEquals(ruleCharacteristics.getNegativeCoverage(), negativeCoverage);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setNegativeCoverage(int)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testSetNegativeCoverage04() {
		int negativeCoverage = 2;
		ruleCharacteristics.setNegativeCoverage(negativeCoverage);
		assertEquals(ruleCharacteristics.getNegativeCoverage(), negativeCoverage);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isNegativeCoverageSet()}.
	 */
	@Test
	void testIsNegativeCoverageSet() {
		ruleCharacteristics.setNegativeCoverage(RuleCharacteristics.UNKNOWN_INT_VALUE);
		assertEquals(false, ruleCharacteristics.isNegativeCoverageSet());
		
		ruleCharacteristics.setNegativeCoverage(1);
		assertEquals(true, ruleCharacteristics.isNegativeCoverageSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testGetEpsilon() {
		try {
			ruleCharacteristics.getEpsilon();
			fail("Should not get unknown epsilon.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilon(double)}.
	 */
	@Test
	void testSetEpsilon01a() {
		try {
			ruleCharacteristics.setEpsilon(-0.001);
			fail("Should not set invalid epsilon.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilon(double)}.
	 */
	@Test
	void testSetEpsilon01b() {
		try {
			ruleCharacteristics.setEpsilon(1.001);
			fail("Should not set invalid epsilon.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilon(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testSetEpsilon02() {
		ruleCharacteristics.setEpsilon(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getEpsilon();
			fail("Should not get unknown epsilon.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilon(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testSetEpsilon03() {
		double epsilon = 0;
		ruleCharacteristics.setEpsilon(epsilon);
		assertEquals(ruleCharacteristics.getEpsilon(), epsilon);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilon(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testSetEpsilon04() {
		double epsilon = 1;
		ruleCharacteristics.setEpsilon(epsilon);
		assertEquals(ruleCharacteristics.getEpsilon(), epsilon);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isEpsilonSet()}.
	 */
	@Test
	void testIsEpsilonSet() {
		ruleCharacteristics.setEpsilon(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isEpsilonSet());
		
		ruleCharacteristics.setEpsilon(1.0);
		assertEquals(true, ruleCharacteristics.isEpsilonSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getEpsilonPrime()}.
	 */
	@Test
	void testGetEpsilonPrime() {
		try {
			ruleCharacteristics.getEpsilonPrime();
			fail("Should not get unknown epsilon'.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilonPrime(double)}.
	 */
	@Test
	void testSetEpsilonPrime01() {
		try {
			ruleCharacteristics.setEpsilonPrime(-0.001);
			fail("Should not set invalid epsilon'.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilonPrime(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getEpsilonPrime()}.
	 */
	@Test
	void testSetEpsilonPrime02() {
		ruleCharacteristics.setEpsilonPrime(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getEpsilonPrime();
			fail("Should not get unknown epsilon'.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilonPrime(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getEpsilonPrime()}.
	 */
	@Test
	void testSetEpsilonPrime03() {
		double epsilonPrime = 0;
		ruleCharacteristics.setEpsilonPrime(epsilonPrime);
		assertEquals(ruleCharacteristics.getEpsilonPrime(), epsilonPrime);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilonPrime(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getEpsilonPrime()}.
	 */
	@Test
	void testSetEpsilonPrime04() {
		double epsilonPrime = 1;
		ruleCharacteristics.setEpsilonPrime(epsilonPrime);
		assertEquals(ruleCharacteristics.getEpsilonPrime(), epsilonPrime);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilonPrime(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getEpsilonPrime()}.
	 */
	@Test
	void testSetEpsilonPrime05() {
		double epsilonPrime = 2;
		ruleCharacteristics.setEpsilonPrime(epsilonPrime);
		assertEquals(ruleCharacteristics.getEpsilonPrime(), epsilonPrime);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isEpsilonPrimeSet()}.
	 */
	@Test
	void testIsEpsilonPrimeSet() {
		ruleCharacteristics.setEpsilonPrime(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isEpsilonPrimeSet());
		
		ruleCharacteristics.setEpsilonPrime(1.0);
		assertEquals(true, ruleCharacteristics.isEpsilonPrimeSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getFConfirmation()}.
	 */
	@Test
	void testGetFConfirmation() {
		try {
			ruleCharacteristics.getFConfirmation();
			fail("Should not get unknown f confirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setFConfirmation(double)}.
	 */
	@Test
	void testSetFConfirmation01a() {
		try {
			ruleCharacteristics.setFConfirmation(-1.001);
			fail("Should not set invalid fConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setFConfirmation(double)}.
	 */
	@Test
	void testSetFConfirmation01b() {
		try {
			ruleCharacteristics.setFConfirmation(1.001);
			fail("Should not set invalid fConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setFConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getFConfirmation()}.
	 */
	@Test
	void testSetFConfirmation02() {
		ruleCharacteristics.setFConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getFConfirmation();
			fail("Should not get unknown fConfirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setFConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getFConfirmation()}.
	 */
	@Test
	void testSetFConfirmation03() {
		double fConfirmation = -1;
		ruleCharacteristics.setFConfirmation(fConfirmation);
		assertEquals(ruleCharacteristics.getFConfirmation(), fConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setFConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getFConfirmation()}.
	 */
	@Test
	void testSetFConfirmation04() {
		double fConfirmation = 1;
		ruleCharacteristics.setFConfirmation(fConfirmation);
		assertEquals(ruleCharacteristics.getFConfirmation(), fConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isFConfirmationSet()}.
	 */
	@Test
	void testIsFConfirmationSet() {
		ruleCharacteristics.setFConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isFConfirmationSet());
		
		ruleCharacteristics.setFConfirmation(1.0);
		assertEquals(true, ruleCharacteristics.isFConfirmationSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getAConfirmation()}.
	 */
	@Test
	void testGetAConfirmation() {
		try {
			ruleCharacteristics.getAConfirmation();
			fail("Should not get unknown a confirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setAConfirmation(double)}.
	 */
	@Test
	void testSetAConfirmation01a() {
		try {
			ruleCharacteristics.setAConfirmation(-1.001);
			fail("Should not set invalid aConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setAConfirmation(double)}.
	 */
	@Test
	void testSetAConfirmation01b() {
		try {
			ruleCharacteristics.setAConfirmation(1.001);
			fail("Should not set invalid aConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setAConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getAConfirmation()}.
	 */
	@Test
	void testSetAConfirmation02() {
		ruleCharacteristics.setAConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getAConfirmation();
			fail("Should not get unknown aConfirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setAConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getAConfirmation()}.
	 */
	@Test
	void testSetAConfirmation03() {
		double aConfirmation = -1;
		ruleCharacteristics.setAConfirmation(aConfirmation);
		assertEquals(ruleCharacteristics.getAConfirmation(), aConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setAConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getAConfirmation()}.
	 */
	@Test
	void testSetAConfirmation04() {
		double aConfirmation = 1;
		ruleCharacteristics.setAConfirmation(aConfirmation);
		assertEquals(ruleCharacteristics.getAConfirmation(), aConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isAConfirmationSet()}.
	 */
	@Test
	void testIsAConfirmationSet() {
		ruleCharacteristics.setAConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isAConfirmationSet());
		
		ruleCharacteristics.setAConfirmation(1.0);
		assertEquals(true, ruleCharacteristics.isAConfirmationSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getZConfirmation()}.
	 */
	@Test
	void testGetZConfirmation() {
		try {
			ruleCharacteristics.getZConfirmation();
			fail("Should not get unknown z confirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setZConfirmation(double)}.
	 */
	@Test
	void testSetZConfirmation01a() {
		try {
			ruleCharacteristics.setZConfirmation(-1.001);
			fail("Should not set invalid zConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setZConfirmation(double)}.
	 */
	@Test
	void testSetZConfirmation01b() {
		try {
			ruleCharacteristics.setZConfirmation(1.001);
			fail("Should not set invalid zConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setZConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getZConfirmation()}.
	 */
	@Test
	void testSetZConfirmation02() {
		ruleCharacteristics.setZConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getZConfirmation();
			fail("Should not get unknown zConfirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setZConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getZConfirmation()}.
	 */
	@Test
	void testSetZConfirmation03() {
		double zConfirmation = -1;
		ruleCharacteristics.setZConfirmation(zConfirmation);
		assertEquals(ruleCharacteristics.getZConfirmation(), zConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setZConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getZConfirmation()}.
	 */
	@Test
	void testSetZConfirmation04() {
		double zConfirmation = 1;
		ruleCharacteristics.setZConfirmation(zConfirmation);
		assertEquals(ruleCharacteristics.getZConfirmation(), zConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isZConfirmationSet()}.
	 */
	@Test
	void testIsZConfirmationSet() {
		ruleCharacteristics.setZConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isZConfirmationSet());
		
		ruleCharacteristics.setZConfirmation(1.0);
		assertEquals(true, ruleCharacteristics.isZConfirmationSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getLConfirmation()}.
	 */
	@Test
	void testGetLConfirmation() {
		try {
			ruleCharacteristics.getLConfirmation();
			fail("Should not get unknown l confirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setLConfirmation(double)}.
	 */
	@Test
	void testSetLConfirmation01() {
		try {
			ruleCharacteristics.setLConfirmation(1.001);
			fail("Should not set invalid lConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setLConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getLConfirmation()}.
	 */
	@Test
	void testSetLConfirmation02() {
		ruleCharacteristics.setLConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getLConfirmation();
			fail("Should not get unknown lConfirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setLConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getLConfirmation()}.
	 */
	@Test
	void testSetLConfirmation03() {
		double lConfirmation = -2;
		ruleCharacteristics.setLConfirmation(lConfirmation);
		assertEquals(ruleCharacteristics.getLConfirmation(), lConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setLConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getLConfirmation()}.
	 */
	@Test
	void testSetLConfirmation04() {
		double lConfirmation = 1;
		ruleCharacteristics.setLConfirmation(lConfirmation);
		assertEquals(ruleCharacteristics.getLConfirmation(), lConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isLConfirmationSet()}.
	 */
	@Test
	void testIsLConfirmationSet() {
		ruleCharacteristics.setLConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isLConfirmationSet());
		
		ruleCharacteristics.setLConfirmation(1.0);
		assertEquals(true, ruleCharacteristics.isLConfirmationSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getC1Confirmation()}.
	 */
	@Test
	void testGetC1Confirmation() {
		try {
			ruleCharacteristics.getC1Confirmation();
			fail("Should not get unknown c1 confirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setC1Confirmation(double)}.
	 */
	@Test
	void testSetC1Confirmation01() {
		double c1Confirmation = -2;
		ruleCharacteristics.setC1Confirmation(c1Confirmation);
		assertEquals(ruleCharacteristics.getC1Confirmation(), c1Confirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setC1Confirmation(double)}.
	 */
	@Test
	void testSetC1Confirmation02() {
		double c1Confirmation = 2;
		ruleCharacteristics.setC1Confirmation(c1Confirmation);
		assertEquals(ruleCharacteristics.getC1Confirmation(), c1Confirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isC1ConfirmationSet()}.
	 */
	@Test
	void testIsC1ConfirmationSet() {
		ruleCharacteristics.setC1Confirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isC1ConfirmationSet());
		
		ruleCharacteristics.setC1Confirmation(1.0);
		assertEquals(true, ruleCharacteristics.isC1ConfirmationSet());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getSConfirmation()}.
	 */
	@Test
	void testGetSConfirmation() {
		try {
			ruleCharacteristics.getSConfirmation();
			fail("Should not get unknown s confirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSConfirmation(double)}.
	 */
	@Test
	void testSetSConfirmation01a() {
		try {
			ruleCharacteristics.setSConfirmation(-1.001);
			fail("Should not set invalid sConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSConfirmation(double)}.
	 */
	@Test
	void testSetSConfirmation01b() {
		try {
			ruleCharacteristics.setSConfirmation(1.001);
			fail("Should not set invalid sConfirmation.");
		} catch (InvalidValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getSConfirmation()}.
	 */
	@Test
	void testSetSConfirmation02() {
		ruleCharacteristics.setSConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		
		try {
			ruleCharacteristics.getSConfirmation();
			fail("Should not get unknown sConfirmation.");
		} catch (UnknownValueException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getSConfirmation()}.
	 */
	@Test
	void testSetSConfirmation03() {
		double sConfirmation = -1;
		ruleCharacteristics.setSConfirmation(sConfirmation);
		assertEquals(ruleCharacteristics.getSConfirmation(), sConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSConfirmation(double)}
	 * and {@link org.rulelearn.rules.RuleCharacteristics#getSConfirmation()}.
	 */
	@Test
	void testSetSConfirmation04() {
		double sConfirmation = 1;
		ruleCharacteristics.setSConfirmation(sConfirmation);
		assertEquals(ruleCharacteristics.getSConfirmation(), sConfirmation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isSConfirmationSet()}.
	 */
	@Test
	void testIsSConfirmationSet() {
		ruleCharacteristics.setSConfirmation(RuleCharacteristics.UNKNOWN_DOUBLE_VALUE);
		assertEquals(false, ruleCharacteristics.isSConfirmationSet());
		
		ruleCharacteristics.setSConfirmation(1.0);
		assertEquals(true, ruleCharacteristics.isSConfirmationSet());
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isCharacteristicSet(java.util.function.IntSupplier)}.
	 * Tests the case when characteristic is not set.
	 */
	@Test
	void testIsCharacteristicSet01() {
		assertFalse(ruleCharacteristics.isCharacteristicSet(ruleCharacteristics::getSupport));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isCharacteristicSet(java.util.function.IntSupplier)}.
	 * Tests the case when characteristic is set.
	 */
	@Test
	void testIsCharacteristicSet02() {
		ruleCharacteristics.setSupport(2);
		assertTrue(ruleCharacteristics.isCharacteristicSet(ruleCharacteristics::getSupport));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isCharacteristicSet(java.util.function.DoubleSupplier)}.
	 * Tests the case when characteristic is not set.
	 */
	@Test
	void testIsCharacteristicSet03() {
		assertFalse(ruleCharacteristics.isCharacteristicSet(ruleCharacteristics::getEpsilon));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#isCharacteristicSet(java.util.function.DoubleSupplier)}.
	 * Tests the case when characteristic is set.
	 */
	@Test
	void testIsCharacteristicSet04() {
		ruleCharacteristics.setEpsilon(0.2);
		assertTrue(ruleCharacteristics.isCharacteristicSet(ruleCharacteristics::getEpsilon));
	}

}
