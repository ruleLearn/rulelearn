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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link ComputableRuleCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ComputableRuleCharacteristicsTest {

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getSupport()}.
	 */
	@Test
	void testGetSupport() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getStrength()}.
	 */
	@Test
	void testGetStrength() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testGetConfidence() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getCoverageFactor()}.
	 */
	@Test
	void testGetCoverageFactor() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testGetCoverage() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testGetNegativeCoverage() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testGetEpsilon() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getEpsilonPrime()}.
	 */
	@Test
	void testGetEpsilonPrime() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getFConfirmation()}.
	 */
	@Test
	void testGetFConfirmation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getAConfirmation()}.
	 */
	@Test
	void testGetAConfirmation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getZConfirmation()}.
	 */
	@Test
	void testGetZConfirmation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getLConfirmation()}.
	 */
	@Test
	void testGetLConfirmation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getC1Confirmation()}.
	 */
	@Test
	void testGetC1Confirmation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getSConfirmation()}.
	 */
	@Test
	void testGetSConfirmation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#ComputableRuleCharacteristics(RuleCoverageInfo))}
	 * and {@link ComputableRuleCharacteristics#getRuleCoverageInformation()}.
	 */
	@Test
	void testComputableRuleCharacteristics01() {
		RuleCoverageInformation ruleCoverageInformationMock = Mockito.mock(RuleCoverageInformation.class);
		
		ComputableRuleCharacteristics characteristics = new ComputableRuleCharacteristics(ruleCoverageInformationMock);
		
		assertEquals(ruleCoverageInformationMock, characteristics.getRuleCoverageInformation());
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#ComputableRuleCharacteristics(RuleCoverageInfo))}.
	 * Tests throwing of {@link NullPointerException} when constructor parameter is {@code null}.
	 */
	@Test
	void testComputableRuleCharacteristics02() {
		try {
			new ComputableRuleCharacteristics(null);
			fail("Should not create computable rule characteristics with a null rule coverage info.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}

}
