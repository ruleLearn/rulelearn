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
	private static ComputableRuleCharacteristics computableRuleCharacteristics03;
	
	@BeforeAll
	static void setUp() {
		IntSet indicesOfPositiveObjects = new IntLinkedOpenHashSet(new int[] {0, 2, 3, 7, 10, 12});
		IntSet indicesOfNeutralObjects = new IntLinkedOpenHashSet(new int[] {1, 8, 11});
		IntList indicesOfCoveredObjects = new IntArrayList(new int[] {0, 1, 3, 5, 7, 8, 9, 11, 12});
		@SuppressWarnings("unchecked")
		Int2ObjectMap<Decision> decisionsOfCoveredObjectsMock = Mockito.mock(Int2ObjectOpenHashMap.class);
		//Mockito.when(decisionsOfCoveredObjectsMock.size()).thenReturn(indicesOfCoveredObjects.size());
		int allObjectsCount = 13;
		
		RuleCoverageInformation ruleCoverageInformationMock = Mockito.mock(RuleCoverageInformation.class);
		Mockito.when(ruleCoverageInformationMock.getIndicesOfPositiveObjects()).thenReturn(indicesOfPositiveObjects);
		Mockito.when(ruleCoverageInformationMock.getIndicesOfNeutralObjects()).thenReturn(indicesOfNeutralObjects);
		Mockito.when(ruleCoverageInformationMock.getIndicesOfCoveredObjects()).thenReturn(indicesOfCoveredObjects);
		Mockito.when(ruleCoverageInformationMock.getDecisionsOfCoveredObjects()).thenReturn(decisionsOfCoveredObjectsMock);
		Mockito.when(ruleCoverageInformationMock.getAllObjectsCount()).thenReturn(allObjectsCount);
		
		computableRuleCharacteristics01 = new ComputableRuleCharacteristics(ruleCoverageInformationMock);
		
		//-----
		
		indicesOfPositiveObjects = new IntLinkedOpenHashSet(new int[] {0, 3, 6, 7, 9, 12, 14, 15});
		indicesOfNeutralObjects = new IntLinkedOpenHashSet(new int[] {1, 4, 10, 16});
		indicesOfCoveredObjects = new IntArrayList(new int[] {0, 1, 2, 3, 4, 5, 7, 9, 10, 11, 12, 13, 15});
		@SuppressWarnings("unchecked")
		Int2ObjectMap<Decision> decisionsOfCoveredObjectsMock2 = Mockito.mock(Int2ObjectOpenHashMap.class);
		//Mockito.when(decisionsOfCoveredObjectsMock2.size()).thenReturn(indicesOfCoveredObjects.size());
		allObjectsCount = 18;
		
		RuleCoverageInformation ruleCoverageInformationMock2 = Mockito.mock(RuleCoverageInformation.class);
		Mockito.when(ruleCoverageInformationMock2.getIndicesOfPositiveObjects()).thenReturn(indicesOfPositiveObjects);
		Mockito.when(ruleCoverageInformationMock2.getIndicesOfNeutralObjects()).thenReturn(indicesOfNeutralObjects);
		Mockito.when(ruleCoverageInformationMock2.getIndicesOfCoveredObjects()).thenReturn(indicesOfCoveredObjects);
		Mockito.when(ruleCoverageInformationMock2.getDecisionsOfCoveredObjects()).thenReturn(decisionsOfCoveredObjectsMock2);
		Mockito.when(ruleCoverageInformationMock2.getAllObjectsCount()).thenReturn(allObjectsCount);
		
		computableRuleCharacteristics02 = new ComputableRuleCharacteristics(ruleCoverageInformationMock2);
		
		//-----
		
		indicesOfPositiveObjects = new IntLinkedOpenHashSet(new int[] {0, 1, 2});
		indicesOfNeutralObjects = new IntLinkedOpenHashSet(new int[] {6, 7}); //two neutral objects
		indicesOfCoveredObjects = new IntArrayList(new int[] {0, 1, 3, 6}); //covered 1 neutral object
		@SuppressWarnings("unchecked")
		Int2ObjectMap<Decision> decisionsOfCoveredObjectsMock3 = Mockito.mock(Int2ObjectOpenHashMap.class);
		//Mockito.when(decisionsOfCoveredObjectsMock3.size()).thenReturn(indicesOfCoveredObjects.size());
		allObjectsCount = 8;
		
		RuleCoverageInformation ruleCoverageInformationMock3 = Mockito.mock(RuleCoverageInformation.class);
		Mockito.when(ruleCoverageInformationMock3.getIndicesOfPositiveObjects()).thenReturn(indicesOfPositiveObjects);
		Mockito.when(ruleCoverageInformationMock3.getIndicesOfNeutralObjects()).thenReturn(indicesOfNeutralObjects);
		Mockito.when(ruleCoverageInformationMock3.getIndicesOfCoveredObjects()).thenReturn(indicesOfCoveredObjects);
		Mockito.when(ruleCoverageInformationMock3.getDecisionsOfCoveredObjects()).thenReturn(decisionsOfCoveredObjectsMock3);
		Mockito.when(ruleCoverageInformationMock3.getAllObjectsCount()).thenReturn(allObjectsCount);
		
		computableRuleCharacteristics03 = new ComputableRuleCharacteristics(ruleCoverageInformationMock3);
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
	void testGetEpsilonPrime_01() {
		assertEquals(computableRuleCharacteristics01.getEpsilonPrime(), (double)2 / (double)6);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getEpsilonPrime()}.
	 */
	@Test
	void testGetEpsilonPrime_02() {
		assertEquals(computableRuleCharacteristics02.getEpsilonPrime(), (double)4 / (double)8);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getFConfirmation()}.
	 */
	@Test
	void testGetFConfirmation() {
		assertEquals(computableRuleCharacteristics03.getFConfirmation(), (double)1 / (double)3);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getAConfirmation()}.
	 */
	@Test
	void testGetAConfirmation() {
		assertEquals(computableRuleCharacteristics03.getAConfirmation(), (double)1 / (double)3);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getZConfirmation()}.
	 */
	@Test
	void testGetZConfirmation() {
		assertEquals(computableRuleCharacteristics03.getZConfirmation(), (double)1 / (double)3);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getLConfirmation()}.
	 */
	@Test
	void testGetLConfirmation() {
		assertEquals(computableRuleCharacteristics03.getLConfirmation(), Math.log(2));
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getC1Confirmation()}.
	 */
	@Test
	void testGetC1Confirmation() {
		//TODO: implement test
	}
	
	//TODO: more tests of calculation of rule confirmation measures

	/**
	 * Test method for {@link org.rulelearn.rules.ComputableRuleCharacteristics#getSConfirmation()}.
	 */
	@Test
	void testGetSConfirmation() {
		assertEquals(computableRuleCharacteristics01.getSConfirmation(), (double)4 / (double)6 - (double)2 / (double)4);
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
