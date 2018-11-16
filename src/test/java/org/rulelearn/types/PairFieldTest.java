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

package org.rulelearn.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.core.UncomparableException;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link PairField}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class PairFieldTest {

	/**
	 * Test for {@link PairField} class constructor.
	 */
	@Test
	public void testPairField01() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		PairField<IntegerField> field = new PairField<>(firstField, secondField);
		
		assertEquals(field.getFirstValue().getValue(), firstField.getValue());
		assertEquals(field.getSecondValue().getValue(), secondField.getValue());
	}
	
	/**
	 * Test for {@link PairField} class constructor.
	 */
	@Test
	public void testPairField02() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		RealField secondField = RealFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		
		try {
			@SuppressWarnings("unused")
			PairField<SimpleField> field = new PairField<>(firstField, secondField);
			fail("Different types of fields in a pair should throw an exception.");
		} catch (InvalidTypeException exception) {			
		}
		
	}

	/**
	 * Test for {@link PairField#selfClone()} method.
	 */
	@Test
	public void testSelfClone() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		PairField<IntegerField> field = new PairField<IntegerField>(firstField, secondField);
		
		PairField<IntegerField> clonedField = field.selfClone();
		
		assertEquals(field.getFirstValue().getValue(), clonedField.getFirstValue().getValue());
		assertEquals(field.getSecondValue().getValue(), clonedField.getSecondValue().getValue());
	}
	
	/**
	 * Test for {@link PairField#isAtLeastAsGoodAs(Field)} method; tests gain-type fields.
	 */
	@Test
	public void testIsAtLeastAsGoodAs_01() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		PairField<IntegerField> firstPair = new PairField<>(firstField, secondField);
		
		//---
		
		//equal pair
		firstField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		PairField<IntegerField> secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//better pair
		firstField = IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//inverted, worse pair
		firstField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.FALSE);
		
		//uncomparable pair
		firstField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link PairField#isAtLeastAsGoodAs(Field)} method; tests cost-type fields.
	 */
	@Test
	public void testIsAtLeastAsGoodAs_02() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		PairField<IntegerField> firstPair = new PairField<>(firstField, secondField);
		
		//---
		
		//equal pair
		firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		PairField<IntegerField> secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//better pair
		firstField = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//inverted, worse pair
		firstField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.FALSE);
		
		//uncomparable pair
		firstField = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtLeastAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtLeastAsGoodAs(firstPair), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link PairField#isAtLeastAsGoodAs(Field)} method. Uses mocks.
	 */
	@Test
	public void testIsAtLeastAsGoodAs_03() {
		IntegerField firstFieldMock = mock(IntegerField.class);
		IntegerField secondFieldMock = mock(IntegerField.class);
		PairField<IntegerField> firstPair = new PairField<>(firstFieldMock, secondFieldMock);
		
		TernaryLogicValue comparisonResult;
		
		//
		
		IntegerField firstFieldMock2 = mock(IntegerField.class);
		IntegerField secondFieldMock2 = mock(IntegerField.class);
		PairField<IntegerField> secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtLeastAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(secondFieldMock.isAtMostAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		comparisonResult = firstPair.isAtLeastAsGoodAs(secondPair);
		verify(firstFieldMock).isAtLeastAsGoodAs(firstFieldMock2);
		verify(secondFieldMock).isAtMostAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.TRUE);
		
		//
		
		firstFieldMock2 = mock(IntegerField.class);
		secondFieldMock2 = mock(IntegerField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtLeastAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(secondFieldMock.isAtMostAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		comparisonResult = firstPair.isAtLeastAsGoodAs(secondPair);
		verify(firstFieldMock, atMost(1)).isAtLeastAsGoodAs(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isAtMostAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
		
		//
		
		firstFieldMock2 = mock(IntegerField.class);
		secondFieldMock2 = mock(IntegerField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtLeastAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(secondFieldMock.isAtMostAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		comparisonResult = firstPair.isAtLeastAsGoodAs(secondPair);
		verify(firstFieldMock, atMost(1)).isAtLeastAsGoodAs(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isAtMostAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
		
		//
		
		firstFieldMock2 = mock(IntegerField.class);
		secondFieldMock2 = mock(IntegerField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtLeastAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(secondFieldMock.isAtMostAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		comparisonResult = firstPair.isAtLeastAsGoodAs(secondPair);
		verify(firstFieldMock, atMost(1)).isAtLeastAsGoodAs(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isAtMostAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link PairField#isAtMostAsGoodAs(Field)} method; tests gain-type fields.
	 */
	@Test
	public void testIsAtMostAsGoodAs_01() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		PairField<IntegerField> firstPair = new PairField<>(firstField, secondField);
		
		//---
		
		//equal pair
		firstField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		PairField<IntegerField> secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//better pair
		firstField = IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.FALSE);
		
		//inverted, worse pair
		firstField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//uncomparable pair
		firstField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		secondField = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link PairField#isAtMostAsGoodAs(Field)} method; tests cost-type fields.
	 */
	@Test
	public void testIsAtMostAsGoodAs_02() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		PairField<IntegerField> firstPair = new PairField<>(firstField, secondField);
		
		//---
		
		//equal pair
		firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		PairField<IntegerField> secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//better pair
		firstField = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.COST);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.TRUE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.FALSE);
		
		//inverted, worse pair
		firstField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.TRUE);
		
		//uncomparable pair
		firstField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		secondField = IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.COST);
		secondPair = new PairField<>(firstField, secondField);
		//
		assertEquals(firstPair.isAtMostAsGoodAs(secondPair), TernaryLogicValue.FALSE);
		assertEquals(secondPair.isAtMostAsGoodAs(firstPair), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link PairField#isAtMostAsGoodAs(Field)} method. Uses mocks.
	 */
	@Test
	public void testIsAtMostAsGoodAs_03() {
		RealField firstFieldMock = mock(RealField.class);
		RealField secondFieldMock = mock(RealField.class);
		PairField<RealField> firstPair = new PairField<>(firstFieldMock, secondFieldMock);
		
		TernaryLogicValue comparisonResult;
		
		//
		
		RealField firstFieldMock2 = mock(RealField.class);
		RealField secondFieldMock2 = mock(RealField.class);
		PairField<RealField> secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtMostAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(secondFieldMock.isAtLeastAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		comparisonResult = firstPair.isAtMostAsGoodAs(secondPair);
		verify(firstFieldMock).isAtMostAsGoodAs(firstFieldMock2);
		verify(secondFieldMock).isAtLeastAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.TRUE);
		
		//
		
		firstFieldMock2 = mock(RealField.class);
		secondFieldMock2 = mock(RealField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtMostAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(secondFieldMock.isAtLeastAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		comparisonResult = firstPair.isAtMostAsGoodAs(secondPair);
		verify(firstFieldMock, atMost(1)).isAtMostAsGoodAs(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isAtLeastAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
		
		//
		
		firstFieldMock2 = mock(RealField.class);
		secondFieldMock2 = mock(RealField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtMostAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(secondFieldMock.isAtLeastAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		comparisonResult = firstPair.isAtMostAsGoodAs(secondPair);
		verify(firstFieldMock, atMost(1)).isAtMostAsGoodAs(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isAtLeastAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
		
		//
		
		firstFieldMock2 = mock(RealField.class);
		secondFieldMock2 = mock(RealField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isAtMostAsGoodAs(firstFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(secondFieldMock.isAtLeastAsGoodAs(secondFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		comparisonResult = firstPair.isAtMostAsGoodAs(secondPair);
		verify(firstFieldMock, atMost(1)).isAtMostAsGoodAs(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isAtLeastAsGoodAs(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
	}
	
	private int compareToEx(int firstCompareToResult, int secondCompareToResult) throws UncomparableException {
		IntegerField firstFieldMock = mock(IntegerField.class);
		IntegerField secondFieldMock = mock(IntegerField.class);
		PairField<IntegerField> firstPair = new PairField<>(firstFieldMock, secondFieldMock);
		
		int comparisonResult = 0;
		
		IntegerField firstFieldMock2 = mock(IntegerField.class);
		IntegerField secondFieldMock2 = mock(IntegerField.class);
		PairField<IntegerField> secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		try {
			when(firstFieldMock.compareToEx(firstFieldMock2)).thenReturn(firstCompareToResult);
			when(secondFieldMock.compareToEx(secondFieldMock2)).thenReturn(secondCompareToResult);
			
			comparisonResult = firstPair.compareToEx(secondPair);
			verify(firstFieldMock).compareToEx(firstFieldMock2);
			verify(secondFieldMock).compareToEx(secondFieldMock2);
		} catch (UncomparableException exception) {
			throw exception;
		}
		
		return comparisonResult;
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_01() {
		try {
			compareToEx(-1, -1);
			fail("Uncomparable pairs found to be comparable.");
		} catch (UncomparableException exception) {
			//OK - do nothing
		}
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_02() {
		try {
			assertTrue(compareToEx(-1, 0) < 0);
		} catch (UncomparableException exception) {
			fail("Comparable pairs found to be uncomparable.");
		}
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_03() {
		try {
			assertTrue(compareToEx(-1, 1) < 0);
		} catch (UncomparableException exception) {
			fail("Comparable pairs found to be uncomparable.");
		}
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_04() {
		try {
			assertTrue(compareToEx(0, -1) > 0);
		} catch (UncomparableException exception) {
			fail("Comparable pairs found to be uncomparable.");
		}
	}
	

	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_05() {
		try {
			assertTrue(compareToEx(0, 0) == 0);
		} catch (UncomparableException exception) {
			fail("Comparable pairs found to be uncomparable.");
		}
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_06() {
		try {
			assertTrue(compareToEx(0, 1) < 0);
		} catch (UncomparableException exception) {
			fail("Comparable pairs found to be uncomparable.");
		}
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_07() {
		try {
			assertTrue(compareToEx(1, -1) > 0);
		} catch (UncomparableException exception) {
			fail("Comparable pairs found to be uncomparable.");
		}
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_08() {
		try {
			assertTrue(compareToEx(1, 0) > 0);
		} catch (UncomparableException exception) {
			fail("Comparable pairs found to be uncomparable.");
		}
	}
	
	/**
	 * Test for {@link PairField#compareToEx(Field)} method. Uses mocks.
	 */
	@Test
	public void testCompareToEx_09() {
		try {
			compareToEx(1, 1);
			fail("Uncomparable pairs found to be comparable.");
		} catch (UncomparableException exception) {
			//OK - do nothing
		}
	}
	
	/**
	 * Test for {@link PairField#isEqualTo(Field)} method. Uses mocks.
	 */
	@Test
	public void testIsEqualTo() {
		IntegerField firstFieldMock = mock(IntegerField.class);
		IntegerField secondFieldMock = mock(IntegerField.class);
		PairField<IntegerField> firstPair = new PairField<>(firstFieldMock, secondFieldMock);
		
		TernaryLogicValue comparisonResult;
		
		//
		
		IntegerField firstFieldMock2 = mock(IntegerField.class);
		IntegerField secondFieldMock2 = mock(IntegerField.class);
		PairField<IntegerField> secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isEqualTo(firstFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(secondFieldMock.isEqualTo(secondFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		comparisonResult = firstPair.isEqualTo(secondPair);
		verify(firstFieldMock).isEqualTo(firstFieldMock2);
		verify(secondFieldMock).isEqualTo(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.TRUE);
		
		//
		
		firstFieldMock2 = mock(IntegerField.class);
		secondFieldMock2 = mock(IntegerField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isEqualTo(firstFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(secondFieldMock.isEqualTo(secondFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		comparisonResult = firstPair.isEqualTo(secondPair);
		verify(firstFieldMock, atMost(1)).isEqualTo(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isEqualTo(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
		
		//
		
		firstFieldMock2 = mock(IntegerField.class);
		secondFieldMock2 = mock(IntegerField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isEqualTo(firstFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(secondFieldMock.isEqualTo(secondFieldMock2)).thenReturn(TernaryLogicValue.TRUE);
		comparisonResult = firstPair.isEqualTo(secondPair);
		verify(firstFieldMock, atMost(1)).isEqualTo(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isEqualTo(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
		
		//
		
		firstFieldMock2 = mock(IntegerField.class);
		secondFieldMock2 = mock(IntegerField.class);
		secondPair = new PairField<>(firstFieldMock2, secondFieldMock2);
		
		when(firstFieldMock.isEqualTo(firstFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(secondFieldMock.isEqualTo(secondFieldMock2)).thenReturn(TernaryLogicValue.FALSE);
		comparisonResult = firstPair.isEqualTo(secondPair);
		verify(firstFieldMock, atMost(1)).isEqualTo(firstFieldMock2);
		verify(secondFieldMock, atMost(1)).isEqualTo(secondFieldMock2);
		assertEquals(comparisonResult, TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests {@link PairField#equals(Object)} method.
	 */
	@Test
	public void testEquals() {
		IntegerField firstField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField secondField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		PairField<IntegerField> field1 = new PairField<IntegerField>(firstField1, secondField1);
		
		IntegerField firstField2 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField secondField2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		PairField<IntegerField> field2 = new PairField<IntegerField>(firstField2, secondField2);
		
		IntegerField firstField3 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		IntegerField secondField3 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		PairField<IntegerField> field3 = new PairField<IntegerField>(firstField3, secondField3);
		
		assertTrue(field1.equals(field2));
		assertFalse(field1.equals(field3));
	}
	
	/**
	 * Tests {@link PairField#hashCode()} method.
	 */
	@Test
	public void testHashCode() {
		IntegerField firstField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField secondField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		PairField<IntegerField> field1 = new PairField<IntegerField>(firstField1, secondField1);
		
		IntegerField firstField2 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField secondField2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		PairField<IntegerField> field2 = new PairField<IntegerField>(firstField2, secondField2);
		
		IntegerField firstField3 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		IntegerField secondField3 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		PairField<IntegerField> field3 = new PairField<IntegerField>(firstField3, secondField3);
		
		assertTrue(field1.hashCode() == field2.hashCode());
		assertFalse(field1.hashCode() == field3.hashCode());
	}
	
	/**
	 * Tests {@link PairField#toString()} method.
	 */
	@Test
	public void testToString() {
		IntegerField firstField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField secondField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		PairField<IntegerField> pairField = new PairField<IntegerField>(firstField1, secondField1);
		assertEquals(pairField.toString(), "(1,2)");		
	}
	
	/**
	 * Tests {@link PairField#isUnknown()} method.
	 */
	@Test
	public void testIsUnknown01() {
		IntegerField firstField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField secondField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		PairField<IntegerField> pairField = new PairField<IntegerField>(firstField1, secondField1);
		assertFalse(pairField.isUnknown());
	}
	
	/**
	 * Tests {@link PairField#isUnknown()} method.
	 */
	@Test
	public void testIsUnknown02() {
		IntegerField secondField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		PairField<SimpleField> pairField = new PairField<>(new UnknownSimpleFieldMV2(), secondField1);
		assertFalse(pairField.isUnknown());
	}
	
	/**
	 * Tests {@link PairField#isUnknown()} method.
	 */
	@Test
	public void testIsUnknown03() {
		IntegerField firstField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		PairField<SimpleField> pairField = new PairField<>(firstField1, new UnknownSimpleFieldMV2());
		assertFalse(pairField.isUnknown());
	}
	
	/**
	 * Tests {@link PairField#isUnknown()} method.
	 */
	@Test
	public void testIsUnknown04() {
		PairField<SimpleField> pairField = new PairField<>(new UnknownSimpleFieldMV2(), new UnknownSimpleFieldMV2());
		assertTrue(pairField.isUnknown());
	}
	
	/**
	 * Tests {@link PairField#isUnknown()} method.
	 */
	@Test
	public void testIsUnknown05() {
		PairField<SimpleField> pairField = new PairField<>(new UnknownSimpleFieldMV15(), new UnknownSimpleFieldMV15());
		assertTrue(pairField.isUnknown());
	}

}
