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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.Table;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Tests for {@link DominanceConeCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class DominanceConeCalculatorTest {
	
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
	 * Creates a mock of an {@link InformationTable}.
	 * 
	 * @param evaluationsList list with arrays of evaluations such that each array stores subsequent evaluations of a single object of an information table
	 * @return mock of an {@link InformationTable} corresponding to given evaluations
	 */
	private InformationTable createInformationTableMock(List<EvaluationField[]> evaluationsList) {
		@SuppressWarnings("unchecked")
		Table<EvaluationField> evaluations = (Table<EvaluationField>)Mockito.mock(Table.class);
		
		for (int i = 0; i < evaluationsList.size(); i++) {
			Mockito.when(evaluations.getFields(i)).thenReturn(evaluationsList.get(i));
		}
		
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(evaluationsList.size());
		Mockito.when(informationTableMock.getActiveConditionAttributeFields()).thenReturn(evaluations);
		
		return informationTableMock;
	}
	
	/**
	 * Gets first mock of an information table, injected in tested methods.
	 * 
	 * @return first mock of an information table, injected in tested methods
	 */
	private InformationTable getInformationTableMock01() {
		List<EvaluationField[]> evaluationsList = new ArrayList<EvaluationField[]>();
		
		evaluationsList.add(new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), intField(5, AttributePreferenceType.COST)}); //x
		evaluationsList.add(new EvaluationField[] {intField(3, AttributePreferenceType.GAIN), intField(4, AttributePreferenceType.COST)}); //dominating object
		evaluationsList.add(new EvaluationField[] {intField(3, AttributePreferenceType.GAIN), intField(6, AttributePreferenceType.COST)}); //incomparable object
		evaluationsList.add(new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), intField(4, AttributePreferenceType.COST)}); //incomparable object
		evaluationsList.add(new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), intField(6, AttributePreferenceType.COST)}); //dominated object
		
		return createInformationTableMock(evaluationsList);
	}
	
	/**
	 * Gets second mock of an information table, injected in tested methods. Constructed information table contains missing values.
	 * 
	 * @return second mock of an information table, injected in tested methods
	 */
	private InformationTable getInformationTableMock02() {
		List<EvaluationField[]> evaluationsList = new ArrayList<EvaluationField[]>();
		
		evaluationsList.add(new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), intField(5, AttributePreferenceType.GAIN)}); //x
		evaluationsList.add(new EvaluationField[] {intField(3, AttributePreferenceType.GAIN), intField(5, AttributePreferenceType.GAIN)}); //dominating object
		evaluationsList.add(new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), intField(6, AttributePreferenceType.GAIN)}); //dominating object
		
		evaluationsList.add(new EvaluationField[] {intField(3, AttributePreferenceType.GAIN), intField(4, AttributePreferenceType.GAIN)}); //incomparable object
		evaluationsList.add(new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), intField(6, AttributePreferenceType.GAIN)}); //incomparable object
		
		evaluationsList.add(new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), intField(4, AttributePreferenceType.GAIN)}); //dominated object
		evaluationsList.add(new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), intField(5, AttributePreferenceType.GAIN)}); //dominated object
		
		evaluationsList.add(new EvaluationField[] {new UnknownSimpleFieldMV2(), intField(5, AttributePreferenceType.GAIN)}); //object that x can be compared with
		evaluationsList.add(new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV15()}); //object that x can not be compared with
		
		return createInformationTableMock(evaluationsList);
	}

	/**
	 * Test method for {@link DominanceConeCalculator#calculatePositiveDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculatePositiveDCone01() {
		InformationTable informationTableMock = getInformationTableMock01();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculatePositiveDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 2);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(1));
	}
	
	/**
	 * Test method for {@link DominanceConeCalculator#calculatePositiveDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculatePositiveDCone02() {
		InformationTable informationTableMock = getInformationTableMock02();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculatePositiveDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 5);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(1));
		assertTrue(dominanceCone.contains(2));
		assertTrue(dominanceCone.contains(7));
		assertTrue(dominanceCone.contains(8));
	}

	/**
	 * Test method for {@link DominanceConeCalculator#calculateNegativeDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculateNegativeDCone01() {
		InformationTable informationTableMock = getInformationTableMock01();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculateNegativeDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 2);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(4));
	}
	
	/**
	 * Test method for {@link DominanceConeCalculator#calculateNegativeDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculateNegativeDCone02() {
		InformationTable informationTableMock = getInformationTableMock02();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculateNegativeDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 4);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(5));
		assertTrue(dominanceCone.contains(6));
		assertTrue(dominanceCone.contains(7));
	}

	/**
	 * Test method for {@link DominanceConeCalculator#calculatePositiveInvDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculatePositiveInvDCone01() {
		InformationTable informationTableMock = getInformationTableMock01();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculatePositiveInvDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 2);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(1));
	}
	
	/**
	 * Test method for {@link DominanceConeCalculator#calculatePositiveInvDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculatePositiveInvDCone02() {
		InformationTable informationTableMock = getInformationTableMock02();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculatePositiveInvDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 4);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(1));
		assertTrue(dominanceCone.contains(2));
		assertTrue(dominanceCone.contains(7));
		//no object no. 8!
	}

	/**
	 * Test method for {@link DominanceConeCalculator#calculateNegativeInvDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculateNegativeInvDCone01() {
		InformationTable informationTableMock = getInformationTableMock01();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculateNegativeInvDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 2);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(4));
	}
	
	/**
	 * Test method for {@link DominanceConeCalculator#calculateNegativeInvDCone(int, InformationTable)}.
	 */
	@Test
	void testCalculateNegativeInvDCone02() {
		InformationTable informationTableMock = getInformationTableMock02();
		IntSortedSet dominanceCone = DominanceConeCalculator.INSTANCE.calculateNegativeInvDCone(0, informationTableMock);
		assertEquals(dominanceCone.size(), 5);
		assertTrue(dominanceCone.contains(0));
		assertTrue(dominanceCone.contains(5));
		assertTrue(dominanceCone.contains(6));
		assertTrue(dominanceCone.contains(7));
		assertTrue(dominanceCone.contains(8)); //object no. 8!
	}

}
