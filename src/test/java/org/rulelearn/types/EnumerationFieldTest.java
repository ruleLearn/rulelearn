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

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link EnumerationField}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class EnumerationFieldTest {
	private EnumerationField field0;
	private EnumerationField field1a;
	private EnumerationField field1b;
	
	private ElementList domain1;
	private ElementList domain2;
	
	private String [] values1 = {"1", "2", "3", "4"};
	private String [] values2 = {"1", "2", "3"};
		
	private void setUp01(AttributePreferenceType type) {
		try {
			domain1 = new ElementList(values1);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		this.field0 = EnumerationFieldFactory.getInstance().create(domain1, 0, type);
		this.field1a = EnumerationFieldFactory.getInstance().create(domain1, 1, type);
		this.field1b = EnumerationFieldFactory.getInstance().create(domain1, 1, type);
	}
	
	private void setUp02() {
		try {
			domain1 = new ElementList(values1);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		this.field0 = EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.NONE);
		this.field1a = EnumerationFieldFactory.getInstance().create(domain1, 1, AttributePreferenceType.GAIN);
		this.field1b = EnumerationFieldFactory.getInstance().create(domain1, 1, AttributePreferenceType.COST);
	}
	
	private void setUp03() {
		try {
			domain1 = new ElementList(values1);
			domain2 = new ElementList(values2);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		this.field0 = EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.GAIN);
		this.field1a = EnumerationFieldFactory.getInstance().create(domain2, 0, AttributePreferenceType.GAIN);
		this.field1b = EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.GAIN);
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
	 * Tests {@link EnumerationField#compareTo(SimpleField)} method.
	 */
	@Test
	public void testCompareTo01() {
		this.setUp02();
		
		assertTrue(field0.compareTo(field1a) < 0);
		assertTrue(field1a.compareTo(field0) > 0);
		assertTrue(field1a.compareTo(field1b) == 0);
	}
	
	/**
	 * Tests {@link EnumerationField#getElement()} method.
	 */
	@Test
	public void testGetElement() {
		this.setUp03();
		
		assertEquals(field0.getElement(), field1a.getElement());
		assertEquals(field0.getElement(), field1b.getElement());
	}
	
	/**
	 * Tests {@link EnumerationField#hasEqualHashOfElementList} method.
	 */
	@Test
	public void testHasEqualHashOfElementList() {
		this.setUp03();
		
		assertEquals(field0.hasEqualHashOfElementList(field1a), TernaryLogicValue.FALSE);
		assertEquals(field0.hasEqualHashOfElementList(field1b), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests {@link EnumerationField#hasEqualElementList} method.
	 */
	@Test
	public void testHasEqualElementList() {
		this.setUp03();
		
		assertEquals(field0.hasEqualElementList(field1a), TernaryLogicValue.FALSE);
		assertEquals(field0.hasEqualElementList(field1b), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests {@link EnumerationField#selfClone()} method.
	 */
	@Test
	public void testSelfClone01() {
		this.setUp02();
		
		EnumerationField clonedField = field0.selfClone();
		assertEquals(clonedField.getClass(), field0.getClass());
		assertEquals(((EnumerationField)clonedField).getValue(), field0.getValue());
		
		clonedField = field1a.selfClone();
		assertEquals(clonedField.getClass(), field1a.getClass());
		assertEquals(((EnumerationField)clonedField).getValue(), field1a.getValue());
		
		clonedField = field1b.selfClone();
		assertEquals(clonedField.getClass(), field1b.getClass());
		assertEquals(((EnumerationField)clonedField).getValue(), field1b.getValue());
	}
	
	@SuppressWarnings("unused")
	private void testSelfCloneHelper(EnumerationField field) {
		EnumerationField f1 = field.<EnumerationField>selfClone();
		assertEquals(f1.getClass(), field.getClass());
		assertEquals(f1.getValue(), ((EnumerationField)field).getValue());
		
		Field f2 = field.<EnumerationField>selfClone();
		assertEquals(f2.getClass(), field.getClass());
		assertEquals(((EnumerationField)f2).getValue(), ((EnumerationField)field).getValue());
		
		EnumerationField f3 = field.selfClone();
		assertEquals(f3.getClass(), field.getClass());
		assertEquals(f3.getValue(), ((EnumerationField)field).getValue());
		
		Field f4 = field.selfClone();
		assertEquals(f4.getClass(), field.getClass());
		assertEquals(((EnumerationField)f4).getValue(), ((EnumerationField)field).getValue());
		
		try {
			RealField f5 = field.selfClone();
			fail("Cloning of enumeration field should not give a real field.");
		}
		catch (ClassCastException exception) {
			System.out.println(exception.getMessage());
		}
		
		try {
			RealField f6 = field.<RealField>selfClone();
			fail("Cloning of enumeration field with explicit return type should not give a real field.");
		}
		catch (ClassCastException exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	/**
	 * Tests {@link EnumerationField#selfClone()} method for a field without preference type.
	 */
	@Test
	public void testSelfClone02None() {
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.NONE);
		this.testSelfCloneHelper(field);
	}
	
	/**
	 * Tests {@link EnumerationField#selfClone()} method for a field with gain-type preference.
	 */
	@Test
	public void testSelfClone02Gain() {
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.GAIN);
		this.testSelfCloneHelper(field);
	}
	
	/**
	 * Tests {@link EnumerationField#selfClone()} method for a field with cost-type preference.
	 */
	@Test
	public void testSelfClone02Cost() {
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.COST);
		this.testSelfCloneHelper(field);
	}
}
