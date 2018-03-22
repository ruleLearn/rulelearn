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
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Tests for [@link UUIDIdentificationField}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class UUIDIdentificationFieldTest {

	/**
	 * Test method for {@link org.rulelearn.types.UUIDIdentificationField#hashCode()}.
	 */
	@Test
	void testHashCode() {
		UUID value = UUID.randomUUID();
		UUIDIdentificationField field1 = new UUIDIdentificationField(value);
		UUIDIdentificationField field2 = new UUIDIdentificationField(value);
		assertEquals(field1.hashCode(), field2.hashCode());
		
		UUIDIdentificationField field3 = new UUIDIdentificationField(UUID.randomUUID());
		assertNotEquals(field1.hashCode(), field3.hashCode());
	}

	/**
	 * Test method for {@link org.rulelearn.types.UUIDIdentificationField#isEqualTo(org.rulelearn.types.Field)}.
	 */
	@Test
	void testIsEqualTo() {
		UUID value = UUID.randomUUID();
		UUIDIdentificationField field1 = new UUIDIdentificationField(value);
		UUIDIdentificationField field2 = new UUIDIdentificationField(value);
		assertEquals(field1.isEqualTo(field2), TernaryLogicValue.TRUE);
		
		UUIDIdentificationField field3 = new UUIDIdentificationField(UUID.randomUUID());
		assertEquals(field1.isEqualTo(field3), TernaryLogicValue.FALSE); //should be different
	}

	/**
	 * Test method for {@link org.rulelearn.types.UUIDIdentificationField#equals(Object)}.
	 */
	@Test
	void testEqualsObject() {
		UUID value = UUID.randomUUID();
		UUIDIdentificationField field1 = new UUIDIdentificationField(value);
		UUIDIdentificationField field2 = new UUIDIdentificationField(value);
		assertEquals(field1, field2);
		
		UUIDIdentificationField field3 = new UUIDIdentificationField(UUID.randomUUID());
		assertNotEquals(field1, field3); //should be different
	}

	/**
	 * Test method for {@link org.rulelearn.types.UUIDIdentificationField#getRandomValue()}.
	 */
	@Test
	void testGetRandomValue() {
		UUID randomValue = UUIDIdentificationField.getRandomValue();
		assertNotEquals(UUIDIdentificationField.getRandomValue(), randomValue);
	}
	
	/**
	 * Test method for {@link org.rulelearn.types.UUIDIdentificationField#getValue()}.
	 */
	@Test
	void testGetValue() {
		UUID value = UUID.randomUUID();
		UUIDIdentificationField field = new UUIDIdentificationField(value);
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Test method for {@link org.rulelearn.types.UUIDIdentificationField#selfClone()}.
	 */
	@Test
	void testSelfClone() {
		UUID value = UUID.randomUUID();
		UUIDIdentificationField field = new UUIDIdentificationField(value);
		UUIDIdentificationField clonedField = field.selfClone();
		assertEquals(field, clonedField);
	}

}
