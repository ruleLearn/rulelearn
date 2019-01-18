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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Tests for [@link TextIdentificationField}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class TextIdentificationFieldTest {

	/**
	 * Test method for {@link org.rulelearn.types.TextIdentificationField#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		String value = "rLId-1234";
		TextIdentificationField field1 = new TextIdentificationField(value);
		TextIdentificationField field2 = new TextIdentificationField(value);
		assertEquals(field1.hashCode(), field2.hashCode());
		
		TextIdentificationField field3 = new TextIdentificationField("rLId-5678");
		assertNotEquals(field1.hashCode(), field3.hashCode());
	}

	/**
	 * Test method for {@link org.rulelearn.types.TextIdentificationField#isEqualTo(org.rulelearn.types.Field)}.
	 */
	@Test
	public void testIsEqualTo() {
		String value = "rLId-1234";
		TextIdentificationField field1 = new TextIdentificationField(value);
		TextIdentificationField field2 = new TextIdentificationField(value);
		assertEquals(field1.isEqualTo(field2), TernaryLogicValue.TRUE);
		
		TextIdentificationField field3 = new TextIdentificationField("rLId-5678");
		assertEquals(field1.isEqualTo(field3), TernaryLogicValue.FALSE); //should be different
	}

	/**
	 * Test method for {@link org.rulelearn.types.TextIdentificationField#equals(Object)}.
	 */
	@Test
	public void testEqualsObject() {
		String value = "rLId-1234";
		TextIdentificationField field1 = new TextIdentificationField(value);
		TextIdentificationField field2 = new TextIdentificationField(value);
		assertEquals(field1, field2);
		
		TextIdentificationField field3 = new TextIdentificationField("rLId-5678");
		assertNotEquals(field1, field3); //should be different
	}

	/**
	 * Test method for {@link org.rulelearn.types.TextIdentificationField#getRandomValue(int)}.
	 */
	@Test
	public void testGetRandomValue() {
		String randomValue = TextIdentificationField.getRandomValue(16);
		assertNotEquals(TextIdentificationField.getRandomValue(16), randomValue);
	}
	
	/**
	 * Test method for {@link org.rulelearn.types.TextIdentificationField#getValue()}.
	 */
	@Test
	public void testGetValue() {
		String value = "rLId-1234";
		TextIdentificationField field = new TextIdentificationField(value);
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Test method for {@link org.rulelearn.types.TextIdentificationField#selfClone()}.
	 */
	@Test
	public void testSelfClone() {
		String value =  "rLId-1234";
		TextIdentificationField field = new TextIdentificationField(value);
		TextIdentificationField clonedField = field.selfClone();
		assertEquals(field, clonedField);
	}
	
	/**
	 * Test method for {@link org.rulelearn.types.TextIdentificationField#toString()}.
	 */
	@Test
	public void testToString() {
		String value =  "rLId-1234";
		TextIdentificationField field = new TextIdentificationField(value);
		assertEquals(field.toString(), value);
	}

}
