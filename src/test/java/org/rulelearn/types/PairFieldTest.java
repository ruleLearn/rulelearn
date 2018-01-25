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
import org.rulelearn.core.InvalidTypeException;
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
	
//	/**
//	 * Test for {@link PairField} class constructor.
//	 */
//	@Test
//	void testPairField02() {
//		SimpleField firstField = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
//		SimpleField secondField = RealFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
//		PairField<SimpleField> field = new PairField<SimpleField>(firstField, secondField);
//		
//		//TODO - implement test
//	}

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
	 * Tests {@link UnknownSimpleFieldMV2#equals(Object)} method.
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
	 * Tests {@link UnknownSimpleFieldMV2d#hashCode()} method.
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

}
