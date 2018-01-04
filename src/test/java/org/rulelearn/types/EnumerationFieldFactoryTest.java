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
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link EnumerationFieldFactory}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class EnumerationFieldFactoryTest {

	private ElementList domain1;
	
	private String [] values1 = {"1", "2", "3", "4"};
		
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
	void testCreate01() {
		setUp01();
		
		int index = -5; // negative index is OK (it is not checked)
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.NONE);
		
		assertEquals(field.getValue(), index);
		try {
			field.getElement();
			fail("Getting element with negative index should result in an exception.");
		}
		catch (IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * Tests creation of enumeration field with gain preference type.
	 */
	@Test
	void testCreate02() {
		setUp01();
		
		int index = 0;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.GAIN);
		
		assertEquals(field.getValue(), index);
	}
	
	/**
	 * Tests creation of enumeration field with cost preference type.
	 */
	@Test
	void testCreate03() {
		setUp01();
		
		int index = 5;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.COST);
		
		assertEquals(field.getValue(), index);
	}
	
	/**
	 * Tests cloning of enumeration field without preference type.
	 */
	@Test
	void testClone01() {
		int index = 1;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.NONE);
		EnumerationField clonedField = EnumerationFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), index);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of enumeration field with gain preference type.
	 */
	@Test
	void testClone02() {
		int index = 1;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.GAIN);
		EnumerationField clonedField = EnumerationFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), index);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
	
	/**
	 * Tests cloning of enumeration field with cost preference type.
	 */
	@Test
	void testClone03() {
		int index = 1;
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain1, index, AttributePreferenceType.COST);
		EnumerationField clonedField = EnumerationFieldFactory.getInstance().clone(field);
		assertEquals(clonedField.getValue(), index);
		assertEquals(field.getClass(), clonedField.getClass()); //check also equality of fields' types
	}
}
