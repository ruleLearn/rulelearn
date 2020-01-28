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
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.Decision;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Tests for {@link ComputableRuleCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ComputableRuleCharacteristicsTest {
	
	private static ComputableRuleCharacteristics computableRuleCharacteristics01;
	private static ComputableRuleCharacteristics computableRuleCharacteristics02;
	
	@BeforeAll
	static void setUp() {
		IntSet indicesOfPositiveObjects = new IntLinkedOpenHashSet(new int[] {0, 2, 3, 7, 10, 12});
		IntSet indicesOfNeutralObjects = new IntLinkedOpenHashSet(new int[] {1, 8, 11});
		IntList indicesOfCoveredObjects = new IntArrayList(new int[] {0, 1, 3, 5, 7, 8, 9, 11, 12});
		@SuppressWarnings("unchecked")
		Int2ObjectMap<Decision> decisionsOfCoveredObjects = Mockito.mock(Int2ObjectOpenHashMap.class);
		Mockito.when(decisionsOfCoveredObjects.size()).thenReturn(indicesOfCoveredObjects.size());
		int allObjectsCount = 13;
		
		computableRuleCharacteristics01 = new ComputableRuleCharacteristics(
				new RuleCoverageInformation(indicesOfPositiveObjects, indicesOfNeutralObjects, indicesOfCoveredObjects,
						decisionsOfCoveredObjects, allObjectsCount));
		
		//-----
		
		indicesOfPositiveObjects = new IntLinkedOpenHashSet(new int[] {0, 3, 6, 7, 9, 12, 14, 15});
		indicesOfNeutralObjects = new IntLinkedOpenHashSet(new int[] {1, 4, 10, 16});
		indicesOfCoveredObjects = new IntArrayList(new int[] {0, 1, 2, 3, 4, 5, 7, 9, 10, 11, 12, 13, 15});
		@SuppressWarnings("unchecked")
		Int2ObjectMap<Decision> decisionsOfCoveredObjects2 = Mockito.mock(Int2ObjectOpenHashMap.class);
		Mockito.when(decisionsOfCoveredObjects2.size()).thenReturn(indicesOfCoveredObjects.size());
		allObjectsCount = 18;
		
		computableRuleCharacteristics02 = new ComputableRuleCharacteristics(
				new RuleCoverageInformation(indicesOfPositiveObjects, indicesOfNeutralObjects, indicesOfCoveredObjects,
						decisionsOfCoveredObjects2, allObjectsCount));
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getSupport()}.
	 */
	@Test
	void testGetSupport01() {
		assertEquals(computableRuleCharacteristics01.getSupport(), 4);
	}
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getSupport()}.
	 */
	@Test
	void testGetSupport02() {
		assertEquals(computableRuleCharacteristics02.getSupport(), 6);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getStrength()}.
	 */
	@Test
	void testGetStrength01() {
		assertEquals(computableRuleCharacteristics01.getStrength(), (double)4 / (double)13);
	}
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getStrength()}.
	 */
	@Test
	void testGetStrength02() {
		assertEquals(computableRuleCharacteristics02.getStrength(), (double)6 / (double)18);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testGetConfidence01() {
		assertEquals(computableRuleCharacteristics01.getConfidence(), (double)4 / (double)6);
	}
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getConfidence()}.
	 */
	@Test
	void testGetConfidence02() {
		assertEquals(computableRuleCharacteristics02.getConfidence(), (double)6 / (double)10);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getCoverageFactor()}.
	 */
	@Test
	void testGetCoverageFactor01() {
		assertEquals(computableRuleCharacteristics01.getCoverageFactor(), (double)4 / (double)6);
	}
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getCoverageFactor()}.
	 */
	@Test
	void testGetCoverageFactor02() {
		assertEquals(computableRuleCharacteristics02.getCoverageFactor(), (double)6 / (double)8);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testGetCoverage01() {
		assertEquals(computableRuleCharacteristics01.getCoverage(), 9);
	}
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getCoverage()}.
	 */
	@Test
	void testGetCoverage02() {
		assertEquals(computableRuleCharacteristics02.getCoverage(), 13);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testGetNegativeCoverage01() {
		assertEquals(computableRuleCharacteristics01.getNegativeCoverage(), 2);
	}
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getNegativeCoverage()}.
	 */
	@Test
	void testGetNegativeCoverage02() {
		assertEquals(computableRuleCharacteristics02.getNegativeCoverage(), 4);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testGetEpsilon01() {
		assertEquals(computableRuleCharacteristics01.getEpsilon(), (double)2 / (double)4);
	}
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getEpsilon()}.
	 */
	@Test
	void testGetEpsilon02() {
		assertEquals(computableRuleCharacteristics02.getEpsilon(), (double)4 / (double)6);
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
