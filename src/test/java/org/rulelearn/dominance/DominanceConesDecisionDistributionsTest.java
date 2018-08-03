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

package org.rulelearn.dominance;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.DecisionDistribution;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.data.Table;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;

/**
 * Tests for {@link DominanceConesDecisionDistributions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class DominanceConesDecisionDistributionsTest {
	
	private int decisionAttributeIndex = 3;
	private AttributePreferenceType decisionAttributePreferenceType = AttributePreferenceType.GAIN;
	
	/**
	 * Supplementary method for creating {@link IntegerField} instances.
	 * 
	 * @param value value passed to {@link IntegerFieldFactory#create(int, AttributePreferenceType)} method.
	 * @param preferenceType preference type passed to {@link IntegerFieldFactory#create(int, AttributePreferenceType)} method.
	 * @return created {@link IntegerField} instance.
	 */
	private IntegerField intField(int value, AttributePreferenceType preferenceType) {
		return IntegerFieldFactory.getInstance().create(value, preferenceType);
	}
	
	/**
	 * Supplementary method for creating {@link RealField} instances.
	 * 
	 * @param value value passed to {@link RealFieldFactory#create(double, AttributePreferenceType)} method.
	 * @param preferenceType preference type passed to {@link RealFieldFactory#create(double, AttributePreferenceType)} method.
	 * @return created {@link RealField} instance.
	 */
	private RealField realField(double value, AttributePreferenceType preferenceType) {
		return RealFieldFactory.getInstance().create(value, preferenceType);
	}
	
	/**
	 * Creates a mock of an {@link InformationTable}.
	 * 
	 * @param evaluationsList list of arrays of evaluations, each corresponding to one object
	 * @return mock of an {@link InformationTable}
	 */
	private InformationTable createInformationTableMock(List<EvaluationField[]> evaluationsList) {
		@SuppressWarnings("unchecked")
		Table<EvaluationField> evaluations = (Table<EvaluationField>)Mockito.mock(Table.class);
		
		for (int i = 0; i < evaluationsList.size(); i++) {
			Mockito.when(evaluations.getFields(i)).thenReturn(evaluationsList.get(i));
		}
		
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		Mockito.when(informationTableMock.getActiveConditionAttributeFields()).thenReturn(evaluations);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(evaluationsList.size());
		
		return informationTableMock;
	}
	
	/**
	 * Gets a mock of an information.
	 * 
	 * @return mock of an information table
	 */
	private InformationTable getTestInformationTableMock() {
		List<EvaluationField[]> evaluationsList = new ArrayList<EvaluationField[]>();
		
		evaluationsList.add(new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), intField(3, AttributePreferenceType.NONE)});
		evaluationsList.add(new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(2, AttributePreferenceType.COST), intField(3, AttributePreferenceType.NONE)});
		evaluationsList.add(new EvaluationField[] {intField(3, AttributePreferenceType.GAIN), realField(0, AttributePreferenceType.COST), intField(3, AttributePreferenceType.NONE)});
		evaluationsList.add(new EvaluationField[] {intField(3, AttributePreferenceType.GAIN), realField(0, AttributePreferenceType.COST), intField(0, AttributePreferenceType.NONE)});
		evaluationsList.add(new EvaluationField[] {new UnknownSimpleFieldMV15(),              realField(2, AttributePreferenceType.COST), intField(3, AttributePreferenceType.NONE)});
		
		InformationTable informationTableMock = createInformationTableMock(evaluationsList);
		
		Mockito.when(informationTableMock.getDecision(0)).thenReturn(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		Mockito.when(informationTableMock.getDecision(1)).thenReturn(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		Mockito.when(informationTableMock.getDecision(2)).thenReturn(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		Mockito.when(informationTableMock.getDecision(3)).thenReturn(new SimpleDecision(intField(3, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		Mockito.when(informationTableMock.getDecision(4)).thenReturn(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		return informationTableMock;
	}

	/**
	 * Test method for {@link DominanceConesDecisionDistributions#DominanceConesDecisionDistributions(InformationTable)}.
	 */
	@Test
	void testDominanceConesDecisionDistributions01() {
		try {
			new DominanceConesDecisionDistributions(null);
			fail("Should not create dominance cones decision distributions for null information table.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#DominanceConesDecisionDistributions(InformationTable)}.
	 */
	@Test
	void testDominanceConesDecisionDistributions02() {
		try {
			InformationTable informationTableMock = Mockito.mock(InformationTable.class);
			Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(0);
			DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
			assertNotNull(dominanceConesDecisionDistributions);
		} catch (NullPointerException exception) {
			fail("Should create dominance cones decision distributions.");
		}
	}

	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNumberOfObjects()}.
	 */
	@Test
	void testGetNumberOfObjects01() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		int numberOfObjects = 0;
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(numberOfObjects);
		
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		assertEquals(dominanceConesDecisionDistributions.getNumberOfObjects(), numberOfObjects);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNumberOfObjects()}.
	 */
	@Test
	void testGetNumberOfObjects02() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		assertEquals(dominanceConesDecisionDistributions.getNumberOfObjects(), 5);
	}

	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveDConeDecisionClassDistribution01() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 0; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveDConeDecisionClassDistribution02() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 1; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveDConeDecisionClassDistribution03() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 2; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveDConeDecisionClassDistribution04() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 3; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(3, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveDConeDecisionClassDistribution05() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 4; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}

	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeDConeDecisionClassDistribution01() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 0; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeDConeDecisionClassDistribution02() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 1; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeDConeDecisionClassDistribution03() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 2; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeDConeDecisionClassDistribution04() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 3; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(3, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeDConeDecisionClassDistribution05() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 4; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}

	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveInvDConeDecisionClassDistribution01() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 0; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveInvDConeDecisionClassDistribution02() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 1; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveInvDConeDecisionClassDistribution03() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 2; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveInvDConeDecisionClassDistribution04() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 3; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(3, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getPositiveInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetPositiveInvDConeDecisionClassDistribution05() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 4; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getPositiveInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}

	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeInvDConeDecisionClassDistribution01() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 0; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeInvDConeDecisionClassDistribution02() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 1; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeInvDConeDecisionClassDistribution03() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 2; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(0, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(1, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(2, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeInvDConeDecisionClassDistribution04() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 3; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(3, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
	/**
	 * Test method for {@link DominanceConesDecisionDistributions#getNegativeInvDConeDecisionClassDistribution(int)}.
	 */
	@Test
	void testGetNegativeInvDConeDecisionClassDistribution05() {
		InformationTable informationTableMock = getTestInformationTableMock();
		DominanceConesDecisionDistributions dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(informationTableMock);
		
		int x = 4; //index of the object in the origin of dominance cone
		DecisionDistribution decisionDistribution = dominanceConesDecisionDistributions.getNegativeInvDConeDecisionClassDistribution(x);
		
		DecisionDistribution expectedDecisionDistribution = new DecisionDistribution();
		expectedDecisionDistribution.increaseCount(new SimpleDecision(intField(4, this.decisionAttributePreferenceType), this.decisionAttributeIndex));
		
		assertEquals(decisionDistribution, expectedDecisionDistribution);
	}
	
}
