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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link RealField}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RealFieldTest {
	
	private RealField field0;
	private RealField field1a;
	private RealField field1b;
	
	private void setUp01(AttributePreferenceType type) {
		field0 = RealFieldFactory.getInstance().create(0, type);
		field1a = RealFieldFactory.getInstance().create(1, type);
		field1b = RealFieldFactory.getInstance().create(1, type);
	}
	
	private void setUp02() {
		this.field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		this.field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		this.field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
	}

	/**
	 * Tests construction and "at least" comparisons of gain-type fields
	 */
	@Test
	public void testIsAtLeastAsGoodAs01() {
		this.setUp01(AttributePreferenceType.GAIN);
		
		assertEquals(field1a.isAtLeastAsGoodAs(field0), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtLeastAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field0.isAtLeastAsGoodAs(field1a), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests construction and "at most" comparisons of cost-type fields
	 */
	@Test
	public void testIsAtLeastAsGoodAs02() {
		this.setUp01(AttributePreferenceType.COST);
		
		assertEquals(field1a.isAtLeastAsGoodAs(field0), TernaryLogicValue.FALSE);
		assertEquals(field1a.isAtLeastAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field0.isAtLeastAsGoodAs(field1a), TernaryLogicValue.TRUE);
	}
	
	
	/**
	 * Tests construction and "at most" comparisons of gain-type fields
	 */
	@Test
	public void testIsAtMostAsGoodAs01() {
		this.setUp01(AttributePreferenceType.GAIN);
		
		assertEquals(field0.isAtMostAsGoodAs(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtMostAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtMostAsGoodAs(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests construction and "at most" comparisons of cost-type fields
	 */
	@Test
	public void testIsAtMostAsGoodAs02() {
		this.setUp01(AttributePreferenceType.COST);
		
		assertEquals(field0.isAtMostAsGoodAs(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isAtMostAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtMostAsGoodAs(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests construction and "is equal" comparisons of fields without preference type
	 */
	@Test
	public void testIsEqualTo01() {
		this.setUp01(AttributePreferenceType.NONE);
		
		assertEquals(field0.isEqualTo(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isEqualTo(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isEqualTo(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests construction and "is equal" comparisons of gain-type fields
	 */
	@Test
	public void testIsEqualTo02() {
		this.setUp01(AttributePreferenceType.GAIN);
		
		assertEquals(field0.isEqualTo(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isEqualTo(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isEqualTo(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests construction and "is equal" comparisons of cost-type fields
	 */
	@Test
	public void testIsEqualTo03() {
		this.setUp01(AttributePreferenceType.COST);
		
		assertEquals(field0.isEqualTo(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isEqualTo(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isEqualTo(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests construction and "is different than" comparisons of fields without preference type
	 */
	@Test
	public void testIsDifferentThan01() {
		this.setUp01(AttributePreferenceType.NONE);
		
		assertEquals(field0.isDifferentThan(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isDifferentThan(field1b), TernaryLogicValue.FALSE);
		assertEquals(field1a.isDifferentThan(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests construction and "is different than" comparisons of fields with gain-type preference
	 */
	@Test
	public void testIsDifferentThan02() {
		this.setUp01(AttributePreferenceType.GAIN);
		
		assertEquals(field0.isDifferentThan(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isDifferentThan(field1b), TernaryLogicValue.FALSE);
		assertEquals(field1a.isDifferentThan(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests construction and "is different than" comparisons of fields with cost-type preference
	 */
	@Test
	public void testIsDifferentThan03() {
		this.setUp01(AttributePreferenceType.COST);
		
		assertEquals(field0.isDifferentThan(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isDifferentThan(field1b), TernaryLogicValue.FALSE);
		assertEquals(field1a.isDifferentThan(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests {@link RealField#compareTo(KnownSimpleField)} method.
	 */
	@Test
	public void testCompareTo01() {
		this.setUp02();
		
		assertTrue(field0.compareTo(field1a) < 0);
		assertTrue(field1a.compareTo(field0) > 0);
		assertTrue(field1a.compareTo(field1b) == 0);
	}
	
	/**
	 * Tests {@link RealField#selfClone()} method.
	 */
	@Test
	public void testSelfClone01() {
		this.setUp02();
		
		RealField clonedField = field0.selfClone();
		assertEquals(clonedField.getClass(), field0.getClass());
		assertEquals(((RealField)clonedField).getValue(), field0.getValue());
		
		clonedField = field1a.selfClone();
		assertEquals(clonedField.getClass(), field1a.getClass());
		assertEquals(((RealField)clonedField).getValue(), field1a.getValue());
		
		clonedField = field1b.selfClone();
		assertEquals(clonedField.getClass(), field1b.getClass());
		assertEquals(((RealField)clonedField).getValue(), field1b.getValue());
	}
	
	@SuppressWarnings("unused")
	private void testSelfCloneHelper(RealField field) {
		RealField f1 = field.<RealField>selfClone();
		assertEquals(f1.getClass(), field.getClass());
		assertEquals(f1.getValue(), ((RealField)field).getValue());
		
		Field f2 = field.<RealField>selfClone();
		assertEquals(f2.getClass(), field.getClass());
		assertEquals(((RealField)f2).getValue(), ((RealField)field).getValue());
		
		RealField f3 = field.selfClone();
		assertEquals(f3.getClass(), field.getClass());
		assertEquals(f3.getValue(), ((RealField)field).getValue());
		
		Field f4 = field.selfClone();
		assertEquals(f4.getClass(), field.getClass());
		assertEquals(((RealField)f4).getValue(), ((RealField)field).getValue());
		
		try {
			IntegerField f5 = field.selfClone();
			fail("Cloning of real field should not give an integer field.");
		}
		catch (ClassCastException exception) {
			System.out.println(exception.getMessage());
		}
		
		try {
			IntegerField f6 = field.<IntegerField>selfClone();
			fail("Cloning of real field with explicit return type should not give an integer field.");
		}
		catch (ClassCastException exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	/**
	 * Tests {@link RealField#selfClone()} method for a field without preference type.
	 */
	@Test
	public void testSelfClone02None() {
		RealField field = RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		this.testSelfCloneHelper(field);
	}
	
	/**
	 * Tests {@link RealField#selfClone()} method for a field with gain-type preference.
	 */
	@Test
	public void testSelfClone02Gain() {
		RealField field = RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		this.testSelfCloneHelper(field);
	}
	
	/**
	 * Tests {@link RealField#selfClone()} method for a field with cost-type preference.
	 */
	@Test
	public void testSelfClone02Cost() {
		RealField field = RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		this.testSelfCloneHelper(field);
	}
	
}
