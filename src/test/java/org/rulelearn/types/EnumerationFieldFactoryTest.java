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
import static org.junit.jupiter.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;

/**
 * Tests for {@link EnumerationFieldFactory}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class EnumerationFieldFactoryTest {

	private ElementList domain1;
	
	private String[] values1 = {"1", "2", "3", "4"};
		
	private void setUp01() {
		try {
			domain1 = new ElementList(values1);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * Tests creation of enumeration field without preference type.
	 */
	@Test
	public void testCreate01() {
		setUp01();
		
		int index = 0;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.NONE);
		
		assertEquals(field.getValue(), index);
	}
	
	/**
	 * Tests creation of enumeration field with gain preference type.
	 */
	@Test
	public void testCreate02() {
		setUp01();
		
		int index = 1;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.GAIN);
		
		assertEquals(field.getValue(), index);
	}
	
	/**
	 * Tests creation of enumeration field with cost preference type.
	 */
	@Test
	public void testCreate03() {
		setUp01();
		
		int index = 2;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.COST);
		
		assertEquals(field.getValue(), index);
	}
	
	/**
	 * Tests creation of enumeration field without (with {@code null} domain).
	 */
	@Test
	@SuppressWarnings("unused")
	public void testCreate04() {
		
		int index = 2;
		try {
			EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.COST);
			fail("Construction of enumeration field with null domain should result in an exception.");
		}
		catch (NullPointerException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * Tests creation of enumeration field with incorrect index.
	 */
	@Test
	@SuppressWarnings("unused")
	public void testCreate05() {
		setUp01();
		
		int index = -1;
		try {
			EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.COST);
			fail("Construction of enumeration field with negative index value should result in an exception.");
		}
		catch (IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}
		
		index = domain1.getSize();
		try {
			EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.COST);
			fail("Construction of enumeration field with index value out of domain should result in an exception.");
		}
		catch (IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * Tests creation of enumeration field from text {@link EnumerationFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests if an exception is thrown if parsed textual value is not in attribute's domain.
	 */
	@Test
	public void testCreateFromString01() {
		setUp01(); //set attribute's domain
		
		EnumerationField valueType = Mockito.mock(EnumerationField.class);
		Mockito.when(valueType.getElementList()).thenReturn(this.domain1);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(valueType);
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			EnumerationFieldFactory.getInstance().create("a", attribute); //test value not present in attribute's domain
			fail("Incorrect text should not be parsed as an enumeration value.");
		} catch (FieldParseException exception) {
			//OK
		}
	}
	
	/**
	 * Tests creation of enumeration field from text {@link EnumerationFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests if an exception is thrown if attribute is {@code null}.
	 */
	@Test
	public void testCreateFromString02() {
		try {
			EnumerationFieldFactory.getInstance().create("1", null);
			fail("Should not parse text as an enumeration value for null attribute.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Tests creation of enumeration field from text {@link EnumerationFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests if an exception is thrown if attribute has wrong {@link EvaluationAttribute#getValueType() value type}
	 */
	@Test
	public void testCreateFromString03() {
		IntegerField valueType = Mockito.mock(IntegerField.class);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(valueType);
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			EnumerationFieldFactory.getInstance().create("1", attribute);
			fail("Should not create field for an attribute with incompatible value type.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Tests creation of enumeration field from text {@link EnumerationFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests if object is constructed for correct textual value and correct evaluation attribute.
	 */
	@Test
	public void testCreateFromString04() {
		setUp01(); //set attribute's domain
		
		EnumerationField valueType = Mockito.mock(EnumerationField.class);
		Mockito.when(valueType.getElementList()).thenReturn(this.domain1);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(valueType);
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		
		EnumerationField field = EnumerationFieldFactory.getInstance().create("4", attribute); //test value present in attribute's domain
		
		assertEquals(field.getValue(), 3); //"4" has index 3
	}
	
	/**
	 * Tests cloning of enumeration field without preference type.
	 */
	@Test
	public void testClone01() {
		setUp01();
		
		int index = 3;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.NONE);
		EnumerationField clonedField = EnumerationFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), index);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of enumeration field with gain preference type.
	 */
	@Test
	public void testClone02() {
		setUp01();
		
		int index = 2;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.GAIN);
		EnumerationField clonedField = EnumerationFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), index);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of enumeration field with cost preference type.
	 */
	@Test
	public void testClone03() {
		setUp01();
		
		int index = 1;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.COST);
		EnumerationField clonedField = EnumerationFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), index);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
}
