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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;

/**
 * Tests for {@link IntegerFieldFactory}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class IntegerFieldFactoryTest {

	/**
	 * Tests creation of integer field without preference type.
	 */
	@Test
	public void testCreate01() {
		int value = -5;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.NONE);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of integer field with gain preference type.
	 */
	@Test
	public void testCreate02() {
		int value = 0;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.GAIN);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of integer field with cost preference type..
	 */
	@Test
	public void testCreate03() {
		int value = 5;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.COST);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of integer field from text {@link IntegerFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests incorrect textual representation of an integer number.
	 */
	@Test
	public void testCreateFromString01() {
		String value = "a";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(IntegerField.class));
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			IntegerFieldFactory.getInstance().create(value, attribute);
			fail("Incorrect text should not be parsed as an integer value.");
		} catch (FieldParseException exception) {
			//OK
		}

	}
	
	/**
	 * Tests creation of integer field from text {@link IntegerFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests correct textual representation of an integer number and {@code null} attribute.
	 */
	@Test
	public void testCreateFromString02() {
		try {
			IntegerFieldFactory.getInstance().create("1", null);
			fail("Should not parse text as an integer number for null attribute.");
		} catch (NullPointerException exception) {
			//OK
		}

	}
	
	/**
	 * Tests creation of integer field from text {@link IntegerFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests correct textual representation of an integer number and attribute with wrong {@link EvaluationAttribute#getValueType() value type}.
	 */
	@Test
	public void testCreateFromString03() {
		String value = "1";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(RealField.class)); //deliberately wrong value type
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			IntegerFieldFactory.getInstance().create(value, attribute);
			fail("Should not create field for an attribute with incompatible value type.");
		} catch (InvalidTypeException exception) {
			//OK
		}

	}
	
	/**
	 * Tests creation of integer field from text {@link IntegerFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests correct textual representation of an integer number and non-null attribute.
	 */
	@Test
	public void testCreateFromString04() {
		String value = "-1";
		int parsedValue = Integer.valueOf(value);
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(IntegerField.class));
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		IntegerField field = IntegerFieldFactory.getInstance().create(value, attribute);
		
		assertEquals(field.getValue(), parsedValue);
	}

	/**
	 * Tests cloning of integer field without preference type.
	 */
	@Test
	public void testClone01() {
		int value = 1;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.NONE);
		IntegerField clonedField = IntegerFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), value);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of integer field with gain preference type.
	 */
	@Test
	public void testClone02() {
		int value = -7;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.GAIN);
		IntegerField clonedField = IntegerFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), value);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of integer field with cost preference type.
	 */
	@Test
	public void testClone03() {
		int value = 0;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.COST);
		IntegerField clonedField = IntegerFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), value);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}

}
