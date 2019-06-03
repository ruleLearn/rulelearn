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
 * Tests for {@link RealFieldFactory}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RealFieldFactoryTest {

	/**
	 * Tests creation of real field without preference type.
	 */
	@Test
	public void testCreate01() {
		double value = -5;
		RealField field = RealFieldFactory.getInstance().create(value, AttributePreferenceType.NONE);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of real field with gain preference type.
	 */
	@Test
	public void testCreate02() {
		double value = 0;
		RealField field = RealFieldFactory.getInstance().create(value, AttributePreferenceType.GAIN);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of real field with cost preference type..
	 */
	@Test
	public void testCreate03() {
		double value = 5;
		RealField field = RealFieldFactory.getInstance().create(value, AttributePreferenceType.COST);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of real field from text {@link RealFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests incorrect textual representation of a real number.
	 */
	@Test
	public void testCreateFromString01() {
		String value = "a";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(RealField.class));
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			RealFieldFactory.getInstance().create(value, attribute);
			fail("Incorrect text should not be parsed as a real value.");
		} catch (FieldParseException exception) {
			//OK
		}

	}
	
	/**
	 * Tests creation of real field from text {@link RealFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests correct textual representation of a real number and {@code null} attribute.
	 */
	@Test
	public void testCreateFromString02() {
		try {
			RealFieldFactory.getInstance().create("1.0", null);
			fail("Should not parse text as a real number for null attribute.");
		} catch (NullPointerException exception) {
			//OK
		}

	}
	
	/**
	 * Tests creation of real field from text {@link RealFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests correct textual representation of a real number and attribute with wrong {@link EvaluationAttribute#getValueType() value type}.
	 */
	@Test
	public void testCreateFromString03() {
		String value = "1";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(IntegerField.class)); //deliberately wrong value type
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			RealFieldFactory.getInstance().create(value, attribute);
			fail("Should not create field for an attribute with incompatible value type.");
		} catch (InvalidTypeException exception) {
			//OK
		}

	}
	
	/**
	 * Tests creation of real field from text {@link RealFieldFactory#create(String, EvaluationAttribute)}.
	 * Tests correct textual representation of a real number and non-null attribute.
	 */
	@Test
	public void testCreateFromString04() {
		String value = "-1.0";
		double parsedValue = Double.parseDouble(value);
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(RealField.class));
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		RealField field = RealFieldFactory.getInstance().create(value, attribute);
		
		assertEquals(field.getValue(), parsedValue);
	}

	/**
	 * Tests cloning of real field without preference type.
	 */
	@Test
	public void testClone01() {
		double value = 1;
		RealField field = RealFieldFactory.getInstance().create(value, AttributePreferenceType.NONE);
		RealField clonedField = RealFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), value);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of real field with gain preference type.
	 */
	@Test
	public void testClone02() {
		double value = -7;
		RealField field = RealFieldFactory.getInstance().create(value, AttributePreferenceType.GAIN);
		RealField clonedField = RealFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), value);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of real field with cost preference type.
	 */
	@Test
	public void testClone03() {
		double value = 0;
		RealField field = RealFieldFactory.getInstance().create(value, AttributePreferenceType.COST);
		RealField clonedField = RealFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), value);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}

}
