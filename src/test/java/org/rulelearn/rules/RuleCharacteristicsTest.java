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

import static org.junit.jupiter.api.Assertions.*;

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
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getStrength()}.
	 */
	@Test
	void testGetStrength() {
		try {
			ruleCharacteristics.getStrength();
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
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testGetConfidence() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setConfidence(double)}.
	 */
	@Test
	void testSetConfidence() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getCoverageFactor()}.
	 */
	@Test
	void testGetCoverageFactor() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverageFactor(double)}.
	 */
	@Test
	void testSetCoverageFactor() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testGetCoverage() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setCoverage(int)}.
	 */
	@Test
	void testSetCoverage() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testGetNegativeCoverage() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setNegativeCoverage(int)}.
	 */
	@Test
	void testSetNegativeCoverage() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testGetEpsilon() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilon(double)}.
	 */
	@Test
	void testSetEpsilon() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getEpsilonPrim()}.
	 */
	@Test
	void testGetEpsilonPrim() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setEpsilonPrim(double)}.
	 */
	@Test
	void testSetEpsilonPrim() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getFConfirmation()}.
	 */
	@Test
	void testGetFConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setFConfirmation(double)}.
	 */
	@Test
	void testSetFConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getAConfirmation()}.
	 */
	@Test
	void testGetAConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setAConfirmation(double)}.
	 */
	@Test
	void testSetAConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getZConfirmation()}.
	 */
	@Test
	void testGetZConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setZConfirmation(double)}.
	 */
	@Test
	void testSetZConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getLConfirmation()}.
	 */
	@Test
	void testGetLConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setLConfirmation(double)}.
	 */
	@Test
	void testSetLConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getC1Confirmation()}.
	 */
	@Test
	void testGetC1Confirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setC1Confirmation(double)}.
	 */
	@Test
	void testSetC1Confirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#getSConfirmation()}.
	 */
	@Test
	void testGetSConfirmation() {
		//TODO: implement tests
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleCharacteristics#setSConfirmation(double)}.
	 */
	@Test
	void testSetSConfirmation() {
		//TODO: implement tests
	}

}
