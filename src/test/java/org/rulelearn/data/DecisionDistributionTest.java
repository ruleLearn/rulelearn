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

package org.rulelearn.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * Tests for {@link DecisionDistribution}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class DecisionDistributionTest {

	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#DecisionDistribution()}.
	 */
	@Test
	void testDecisionDistribution() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		assertEquals(decisionDistribution.getCount(null), 0);
	}

	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#DecisionDistribution(org.rulelearn.data.InformationTable)}.
	 * Tests exception throwing for {@code null} parameter.
	 */
	@Test
	void testDecisionDistributionInformationTable() {
		try {
			new DecisionDistribution(null);
			fail("Should not construct decision distribution with null information table.");
		} catch (NullPointerException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#DecisionDistribution(org.rulelearn.data.InformationTable)}
	 * and {@link org.rulelearn.data.DecisionDistribution#getCount(org.rulelearn.data.Decision)}.
	 */
	@Test
	void testGetCount() {
		Decision decision1 = Mockito.mock(Decision.class);
		Decision decision2 = Mockito.mock(Decision.class);
		
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(5);
		
		Mockito.when(informationTableMock.getDecision(0)).thenReturn(decision1);
		Mockito.when(informationTableMock.getDecision(1)).thenReturn(decision2);
		Mockito.when(informationTableMock.getDecision(2)).thenReturn(decision1);
		Mockito.when(informationTableMock.getDecision(3)).thenReturn(decision2);
		Mockito.when(informationTableMock.getDecision(4)).thenReturn(decision1);
		
		DecisionDistribution decisionDistribution = new DecisionDistribution(informationTableMock);
		
		assertEquals(decisionDistribution.getCount(decision1), 3);
		assertEquals(decisionDistribution.getCount(decision2), 2);
	}

	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#increaseCount(org.rulelearn.data.Decision)}.
	 */
	@Test
	void testIncreaseCount01() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		Decision decision = Mockito.mock(Decision.class); //decision mock
		
		decisionDistribution.increaseCount(decision);
		assertEquals(decisionDistribution.getCount(decision), 1);
		
		decisionDistribution.increaseCount(decision); //the same decision
		assertEquals(decisionDistribution.getCount(decision), 2);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#increaseCount(org.rulelearn.data.Decision)}.
	 * Tests distribution of simple decisions.
	 */
	@Test
	void testIncreaseCount02() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		
		Decision decision1  = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), 2);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), 2); //equal decision
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), 2);
		
		decisionDistribution.increaseCount(decision1);
		decisionDistribution.increaseCount(decision2);
		
		decisionDistribution.increaseCount(decision3);
		
		assertEquals(decisionDistribution.getCount(decision1), 2);
		assertEquals(decisionDistribution.getCount(decision3), 1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#increaseCount(org.rulelearn.data.Decision)}.
	 * Tests distribution of composite decisions.
	 */
	@Test
	void testIncreaseCount03() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		
		int[] attributeIndices = {4, 5};
		
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST)};
				
		Decision decision1  = new CompositeDecision(evaluations1, attributeIndices);
		Decision decision2 = new CompositeDecision(evaluations1.clone(), attributeIndices);
		
		EvaluationField[] evaluations2 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST)};
		
		Decision decision3  = new CompositeDecision(evaluations2, attributeIndices);
		
		decisionDistribution.increaseCount(decision1);
		decisionDistribution.increaseCount(decision2);
		
		decisionDistribution.increaseCount(decision3);
		
		assertEquals(decisionDistribution.getCount(decision1), 2);
		assertEquals(decisionDistribution.getCount(decision3), 1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#increaseCount(org.rulelearn.data.Decision)}.
	 * Tests if an exception is thrown when trying to increase count of a {@code null} decision.
	 */
	@Test
	void testIncreaseCount04() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		try {
			decisionDistribution.increaseCount(null);
			fail("Should not increase count of a null decision.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#equals(Object)}.
	 */
	@Test
	void testEquals() {
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5,AttributePreferenceType.COST), 1);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(7,AttributePreferenceType.GAIN), 1);
		
		Decision decision1Copy = new SimpleDecision(IntegerFieldFactory.getInstance().create(5,AttributePreferenceType.COST), 1);
		Decision decision2Copy = new SimpleDecision(IntegerFieldFactory.getInstance().create(7,AttributePreferenceType.GAIN), 1);
		
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(3);
		Mockito.when(informationTableMock.getDecision(0)).thenReturn(decision1);
		Mockito.when(informationTableMock.getDecision(1)).thenReturn(decision2);
		Mockito.when(informationTableMock.getDecision(2)).thenReturn(decision1);
		
		InformationTable informationTableMockCopy = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMockCopy.getNumberOfObjects()).thenReturn(3);
		Mockito.when(informationTableMockCopy.getDecision(0)).thenReturn(decision1Copy);
		Mockito.when(informationTableMockCopy.getDecision(1)).thenReturn(decision1Copy);
		Mockito.when(informationTableMockCopy.getDecision(2)).thenReturn(decision2Copy);
		
		DecisionDistribution decisionDistribution  = new DecisionDistribution(informationTableMock);
		DecisionDistribution decisionDistributionCopy = new DecisionDistribution(informationTableMockCopy);
		
		assertEquals(decisionDistribution, decisionDistributionCopy);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#hashCode()}.
	 */
	@Test
	void testHashCode() {
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5,AttributePreferenceType.NONE), 1);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(7,AttributePreferenceType.GAIN), 1);
		
		Decision decision1Copy = new SimpleDecision(IntegerFieldFactory.getInstance().create(5,AttributePreferenceType.NONE), 1);
		Decision decision2Copy = new SimpleDecision(IntegerFieldFactory.getInstance().create(7,AttributePreferenceType.GAIN), 1);
		
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(3);
		Mockito.when(informationTableMock.getDecision(0)).thenReturn(decision1);
		Mockito.when(informationTableMock.getDecision(1)).thenReturn(decision2);
		Mockito.when(informationTableMock.getDecision(2)).thenReturn(decision1);
		
		InformationTable informationTableMockCopy = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMockCopy.getNumberOfObjects()).thenReturn(3);
		Mockito.when(informationTableMockCopy.getDecision(0)).thenReturn(decision1Copy);
		Mockito.when(informationTableMockCopy.getDecision(1)).thenReturn(decision1Copy);
		Mockito.when(informationTableMockCopy.getDecision(2)).thenReturn(decision2Copy);
		
		DecisionDistribution decisionDistribution  = new DecisionDistribution(informationTableMock);
		DecisionDistribution decisionDistributionCopy = new DecisionDistribution(informationTableMockCopy);
		
		assertEquals(decisionDistribution.hashCode(), decisionDistribution.hashCode());
		assertEquals(decisionDistribution.hashCode(), decisionDistributionCopy.hashCode());
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getDifferentDecisionsCount()}.
	 */
	@Test
	void testGetDifferentDecisionsCount01() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), 2);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), 2); //equal decision
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), 2);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), 2); //equal decision
		
		decisionDistribution.increaseCount(decision1);
		decisionDistribution.increaseCount(decision2);
		
		decisionDistribution.increaseCount(decision3);
		decisionDistribution.increaseCount(decision4);
		
		assertEquals(decisionDistribution.getDifferentDecisionsCount(), 2);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMode()}.
	 */
	@Test
	void testGetMode01() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 2;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), decisionAttributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), decisionAttributeIndex); //equal decision
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), decisionAttributeIndex);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), decisionAttributeIndex); //equal decision
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), decisionAttributeIndex); //equal decision
		
		decisionDistribution.increaseCount(decision1);
		decisionDistribution.increaseCount(decision2);
		
		decisionDistribution.increaseCount(decision3);
		decisionDistribution.increaseCount(decision4);
		decisionDistribution.increaseCount(decision5);
		
		List<Decision> mode = decisionDistribution.getMode();
		assertEquals(mode.size(), 1);
		assertEquals(mode.get(0), decision3);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMode()}.
	 */
	@Test
	void testGetMode02() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 2;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 2; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 4; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 3; i++) {
			decisionDistribution.increaseCount(decision3);
		}
		
		List<Decision> mode = decisionDistribution.getMode();
		assertEquals(mode.size(), 1);
		assertEquals(mode.get(0), decision2);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMode()}.
	 */
	@Test
	void testGetMode03() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 2;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 4; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision3);
		}
		
		List<Decision> mode = decisionDistribution.getMode();
		assertEquals(mode.size(), 2);
		assertTrue(mode.contains(decision1));
		assertTrue(mode.contains(decision3));
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMode()}.
	 */
	@Test
	void testGetMode04() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 2;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 3; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision3);
		}
		
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 6; i++) {
			decisionDistribution.increaseCount(decision4);
		}
		
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 6; i++) {
			decisionDistribution.increaseCount(decision5);
		}
		
		List<Decision> mode = decisionDistribution.getMode();
		assertEquals(mode.size(), 2);
		assertTrue(mode.contains(decision4));
		assertTrue(mode.contains(decision5));
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMode()}.
	 */
	@Test
	void testGetMode05() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		assertNull(decisionDistribution.getMode()); //no mode for an empty distribution
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMedian(Decision[])}.
	 */
	@Test
	void testGetMedian01() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 4;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 6; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision median = decisionDistribution.getMedian(new Decision[] {decision1, decision2});
		assertEquals(median, decision1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMedian(Decision[])}.
	 */
	@Test
	void testGetMedian02() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 4;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 6; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision median = decisionDistribution.getMedian(new Decision[] {decision1, decision2});
		assertEquals(median, decision2);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMedian(Decision[])}.
	 */
	@Test
	void testGetMedian03() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 4;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 5; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision median = decisionDistribution.getMedian(new Decision[] {decision1, decision2});
		assertEquals(median, decision1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMedian(Decision[])}.
	 */
	@Test
	void testGetMedian04() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 6;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(100, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision median = decisionDistribution.getMedian(new Decision[] {decision1, decision2});
		assertEquals(median, decision1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMedian(Decision[])}.
	 */
	@Test
	void testGetMedian05() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 6;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision3);
		}
		
		Decision median = decisionDistribution.getMedian(new Decision[] {decision1, decision2, decision3});
		assertEquals(median, decision2);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMedian(Decision[])}.
	 */
	@Test
	void testGetMedian06() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 6;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 99; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 200; i++) {
			decisionDistribution.increaseCount(decision3);
		}
		
		Decision median = decisionDistribution.getMedian(new Decision[] {decision1, decision2, decision3});
		assertEquals(median, decision3);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionDistribution#getMedian(Decision[])}.
	 */
	@Test
	void testGetMedian07() {
		DecisionDistribution decisionDistribution = new DecisionDistribution();
		int decisionAttributeIndex = 6;
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision1);
		}
		
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 100; i++) {
			decisionDistribution.increaseCount(decision2);
		}
		
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		for (int i = 0; i < 200; i++) {
			decisionDistribution.increaseCount(decision3);
		}
		
		Decision median = decisionDistribution.getMedian(new Decision[] {decision1, decision2, decision3});
		assertEquals(median, decision2);
	}

}
