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

package org.rulelearn.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.FieldDistribution;
import org.rulelearn.types.EnumerationField;

/**
 * Tests for {@link ModeCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ModeCalculatorTest {

	/**
	 * Test method for {@link org.rulelearn.core.ModeCalculator#calculate(org.rulelearn.types.EnumerationField, org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testCalculateEnumerationFieldEvaluationField01() {
		FieldDistribution fieldDistributionMock = Mockito.mock(FieldDistribution.class);
		EnumerationField field1 = Mockito.mock(EnumerationField.class);
		EnumerationField field2 = Mockito.mock(EnumerationField.class);
		Mockito.when(fieldDistributionMock.getCount(field1)).thenReturn(10);
		Mockito.when(fieldDistributionMock.getCount(field2)).thenReturn(11);
		Mockito.when(field1.isEqualTo(field2)).thenReturn(TernaryLogicValue.FALSE);
		Mockito.when(field1.hasEqualHashOfElementList(field2)).thenReturn(TernaryLogicValue.TRUE);
		
		ModeCalculator modeCalculator = new ModeCalculator(fieldDistributionMock);
		
		assertEquals(modeCalculator.calculate(field1, field2), field2);
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.ModeCalculator#calculate(org.rulelearn.types.EnumerationField, org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testCalculateEnumerationFieldEvaluationField02() {
		FieldDistribution fieldDistributionMock = Mockito.mock(FieldDistribution.class);
		EnumerationField field1 = Mockito.mock(EnumerationField.class);
		EnumerationField field2 = Mockito.mock(EnumerationField.class);
		Mockito.when(fieldDistributionMock.getCount(field1)).thenReturn(10);
		Mockito.when(fieldDistributionMock.getCount(field2)).thenReturn(5);
		Mockito.when(field1.isEqualTo(field2)).thenReturn(TernaryLogicValue.FALSE);
		Mockito.when(field1.hasEqualHashOfElementList(field2)).thenReturn(TernaryLogicValue.TRUE);
		
		ModeCalculator modeCalculator = new ModeCalculator(fieldDistributionMock);
		
		assertEquals(modeCalculator.calculate(field1, field2), field1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.ModeCalculator#calculate(org.rulelearn.types.EnumerationField, org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testCalculateEnumerationFieldEvaluationField03() {
		FieldDistribution fieldDistributionMock = Mockito.mock(FieldDistribution.class);
		EnumerationField field1 = Mockito.mock(EnumerationField.class);
		EnumerationField field2 = Mockito.mock(EnumerationField.class);
		Mockito.when(fieldDistributionMock.getCount(field1)).thenReturn(5);
		Mockito.when(fieldDistributionMock.getCount(field2)).thenReturn(5);
		Mockito.when(field1.isEqualTo(field2)).thenReturn(TernaryLogicValue.FALSE);
		Mockito.when(field1.hasEqualHashOfElementList(field2)).thenReturn(TernaryLogicValue.TRUE);
		
		ModeCalculator modeCalculator = new ModeCalculator(fieldDistributionMock);
		
		assertEquals(modeCalculator.calculate(field1, field2), field1); //TODO: update test if one of the two fields is drawn, using a seed
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.ModeCalculator#calculate(org.rulelearn.types.EnumerationField, org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testCalculateEnumerationFieldEvaluationField04() {
		FieldDistribution fieldDistributionMock = Mockito.mock(FieldDistribution.class);
		EnumerationField field1 = Mockito.mock(EnumerationField.class);
		EnumerationField field2 = Mockito.mock(EnumerationField.class);
		Mockito.when(fieldDistributionMock.getCount(field1)).thenReturn(5);
		Mockito.when(fieldDistributionMock.getCount(field2)).thenReturn(5);
		Mockito.when(field1.isEqualTo(field1)).thenReturn(TernaryLogicValue.TRUE); //comparing two equal enumeration fields
		Mockito.when(field1.hasEqualHashOfElementList(field1)).thenReturn(TernaryLogicValue.TRUE);
		
		ModeCalculator modeCalculator = new ModeCalculator(fieldDistributionMock);
		
		assertEquals(modeCalculator.calculate(field1, field1), field1);
	}

	/**
	 * Test method for constructor {@link org.rulelearn.core.ModeCalculator#ModeCalculator(org.rulelearn.data.FieldDistribution)}
	 * and method {@link org.rulelearn.core.ModeCalculator#getFieldDistribution()}.
	 */
	@Test
	void testModeCalculatorAndGetFieldDistribution() {
		FieldDistribution fieldDistributionMock = Mockito.mock(FieldDistribution.class);
		ModeCalculator modeCalculator = new ModeCalculator(fieldDistributionMock);
		assertEquals(modeCalculator.getFieldDistribution(), fieldDistributionMock);
	}

}
