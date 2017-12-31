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
import org.rulelearn.data.AttributePreferenceType;

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
	void testCreate01() {
		int value = -5;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.NONE);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of integer field with gain preference type.
	 */
	@Test
	void testCreate02() {
		int value = 0;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.GAIN);
		
		assertEquals(field.getValue(), value);
	}
	
	/**
	 * Tests creation of integer field with cost preference type..
	 */
	@Test
	void testCreate03() {
		int value = 5;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.COST);
		
		assertEquals(field.getValue(), value);
	}

	/**
	 * Tests cloning of integer field without preference type.
	 */
	@Test
	void testClone01() {
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
	void testClone02() {
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
	void testClone03() {
		int value = 0;
		IntegerField field = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.COST);
		IntegerField clonedField = IntegerFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), value);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}

}
