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

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.core.UncomparableException;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link UnknownSimpleFieldMV15}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class UnknownSimpleFieldMV15Test {

	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtLeastAsGoodAs(Field)} method. Tests if mv_{1.5} missing value outranks the same missing value.
	 */
	@Test
	public void testIsAtLeastAsGoodAs_01() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = new UnknownSimpleFieldMV15();
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtLeastAsGoodAs(Field)} method. Tests if mv_{1.5} missing value outranks a known value.
	 */
	@Test
	public void testIsAtLeastAsGoodAs_02() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtLeastAsGoodAs(Field)} method. Tests if mv_{1.5} missing value outranks a known value.
	 */
	@Test
	public void testIsAtLeastAsGoodAs_03() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = RealFieldFactory.getInstance().create(-1.0, AttributePreferenceType.COST);
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtLeastAsGoodAs(Field)} method. Tests if mv_{1.5} missing value outranks a known value.
	 */
	@Test
	public void testIsAtLeastAsGoodAs_04() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = null;
		try {
			otherField = EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b", "c"}), 1, AttributePreferenceType.NONE);
		} catch (Exception exception) {
			fail("Element list could not be constructed.");
		}
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtMostAsGoodAs(Field)} method. Tests if mv_{1.5} missing value is outranked by the same missing value.
	 */
	@Test
	public void testIsAtMostAsGoodAs_01() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = new UnknownSimpleFieldMV15();
		assertEquals(mvField.isAtMostAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtMostAsGoodAs(Field)} method. Tests if mv_{1.5} missing value is outranked by a known value.
	 */
	@Test
	public void testIsAtMostAsGoodAs_02() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		assertEquals(mvField.isAtMostAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtMostAsGoodAs(Field)} method. Tests if mv_{1.5} missing value is outranked by a known value.
	 */
	@Test
	public void testIsAtMostAsGoodAs_03() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = RealFieldFactory.getInstance().create(-1.0, AttributePreferenceType.COST);
		assertEquals(mvField.isAtMostAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isAtMostAsGoodAs(Field)} method. Tests if mv_{1.5} missing value is outranked by a known value.
	 */
	@Test
	public void testIsAtMostAsGoodAs_04() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = null;
		try {
			otherField = EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b", "c"}), 1, AttributePreferenceType.NONE);
		} catch (Exception exception) {
			fail("Element list could not be constructed.");
		}
		assertEquals(mvField.isAtMostAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isEqualTo(Field)} method. Tests if mv_{1.5} missing value is equal to the same missing value.
	 */
	@Test
	public void testIsEqualTo_01() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = new UnknownSimpleFieldMV15();
		assertEquals(mvField.isEqualTo(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isEqualTo(Field)} method. Tests if mv_{1.5} missing value is equal to a known value.
	 */
	@Test
	public void testIsEqualTo_02() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		assertEquals(mvField.isEqualTo(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isEqualTo(Field)} method. Tests if mv_{1.5} missing value is equal to a known value.
	 */
	@Test
	public void testIsEqualTo_03() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = RealFieldFactory.getInstance().create(-1.0, AttributePreferenceType.COST);
		assertEquals(mvField.isEqualTo(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#isEqualTo(Field)} method. Tests if mv_{1.5} missing value is equal to a known value.
	 */
	@Test
	public void testIsEqualTo_04() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field otherField = null;
		try {
			otherField = EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b", "c"}), 1, AttributePreferenceType.NONE);
		} catch (Exception exception) {
			fail("Element list could not be constructed.");
		}
		assertEquals(mvField.isEqualTo(otherField), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#reverseIsAtLeastAsGoodAs(KnownSimpleField)} method. Tests if other known field DOES NOT outrank mv_{1.5} missing value.
	 */
	@Test
	public void testReverseIsAtLeastAsGoodAs_01() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		KnownSimpleField otherField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		assertEquals(mvField.reverseIsAtLeastAsGoodAs(otherField), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#reverseIsAtMostAsGoodAs(KnownSimpleField)} method. Tests if other known field is NOT outranked by mv_{1.5} missing value.
	 */
	@Test
	public void testReverseIsAtMostAsGoodAs_01() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		KnownSimpleField otherField = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		assertEquals(mvField.reverseIsAtMostAsGoodAs(otherField), TernaryLogicValue.FALSE);
	}

	/**
	 * Test for {@link UnknownSimpleFieldMV15#reverseIsEqualTo(KnownSimpleField)} method. Tests if other known field is NOT equal to mv_{1.5} missing value.
	 */
	@Test
	public void testReverseIsEqualTo_01() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		KnownSimpleField otherField = null;
		
		try {
			otherField = EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b", "c"}), 1, AttributePreferenceType.NONE);
		} catch (Exception exception) {
			fail("Element list could not be constructed.");
		}
		assertEquals(mvField.reverseIsEqualTo(otherField), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link UnknownSimpleFieldMV15#compareToEx(Field)} method. Tests if result of comparison of mv_{1.5} missing value and another mv_{1.5} missing value is equal to zero.
	 */
	@Test
	public void testCompareToEx_01() throws UncomparableException {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		EvaluationField otherField = new UnknownSimpleFieldMV15();
		assertEquals(mvField.compareToEx(otherField), 0);
	}

	/**
	 * Test for {@link UnknownSimpleFieldMV15#compareToEx(Field)} method. Tests if result of comparison of mv_{1.5} missing value and other known field is equal to zero.
	 */
	@Test
	public void testCompareToEx_02() throws UncomparableException {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		KnownSimpleField otherField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		assertEquals(mvField.compareToEx(otherField), 0);
	}

	/**
	 *  Test for {@link UnknownSimpleFieldMV15#reverseCompareToEx(Field)} method. Tests if comparison of a known simple field and mv_{1.5} missing value causes an exception.
	 */
	@Test
	public void testReverseCompareToEx() throws UncomparableException {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		KnownSimpleField otherField = RealFieldFactory.getInstance().create(-1.0, AttributePreferenceType.NONE);
		try {
			mvField.reverseCompareToEx(otherField);
			fail("Known simple field should be incomparable with mv_{1.5} missing value.");
		} catch (UncomparableException exception) {
			
		}
	}

	/**
	 * Test for {@link UnknownSimpleFieldMV15#selfClone()} method.
	 */
	@Test
	public void testSelfClone() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV15();
		Field clonedField = mvField.selfClone();
		
		assertTrue(clonedField instanceof UnknownSimpleFieldMV15);
	}
	
	/**
	 * Tests {@link UnknownSimpleFieldMV15#equals(Object)} method.
	 */
	@Test
	public void testEquals() {
		UnknownSimpleField mvField1 = new UnknownSimpleFieldMV15();
		UnknownSimpleField mvField2 = new UnknownSimpleFieldMV15();
		
		assertTrue(mvField1.equals(mvField2));
		
		IntegerField fieldI = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		RealField fieldR = RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.NONE);
		EnumerationField fieldE = null;
		try {
			fieldE = EnumerationFieldFactory.getInstance().create(new ElementList(new String [] {"a","b"}), 0, AttributePreferenceType.NONE);
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		PairField<IntegerField> fieldP = new PairField<IntegerField>(fieldI, fieldI);
		UnknownSimpleField FieldUmv2 = new UnknownSimpleFieldMV2();
		
		assertFalse(mvField1.equals(fieldI));
		assertFalse(mvField1.equals(fieldR));
		assertFalse(mvField1.equals(fieldE));
		assertFalse(mvField1.equals(fieldP));
		assertFalse(mvField1.equals(FieldUmv2));		
	}
	
	/**
	 * Tests {@link UnknownSimpleFieldMV15d#hashCode()} method.
	 */
	@Test
	public void testHashCode() {
		UnknownSimpleField mvField1 = new UnknownSimpleFieldMV15();
		UnknownSimpleField mvField2 = new UnknownSimpleFieldMV15();
		
		assertTrue(mvField1.hashCode() == mvField2.hashCode());	
	}

}
