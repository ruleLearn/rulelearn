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

package org.rulelearn.data.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;

/**
 * Test for {@link ObjectBuilder}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectBuilderTest {

	/**
	 * Test method for {@link ObjectBuilder#ObjectBuilder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectBuilder01() {
		Attribute[] attributes = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes);});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#ObjectBuilder(String)}.
	 */
	@Test
	void testConstructionOfObjectBuilder02() {
		String encoding = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(encoding);});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#ObjectBuilder(Attribute[], boolean)}.
	 */
	@Test
	void testConstructionOfObjectBuilder03() {
		Attribute[] attributes = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, true);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, false);});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#ObjectBuilder(Attribute[], String)}.
	 */
	@Test
	void testConstructionOfObjectBuilder04() {
		Attribute[] attributes = null;
		String encoding = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, encoding);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, "");});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(new Attribute[0], encoding);});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#ObjectBuilder(String, boolean)}.
	 */
	@Test
	void testConstructionOfObjectBuilder05() {
		String encoding = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(encoding, true);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(encoding, false);});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#ObjectBuilder(Attribute[], String, boolean))}.
	 */
	@Test
	void testConstructionOfObjectBuilder06() {
		Attribute[] attributes = null;
		String encoding = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, encoding, true);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, encoding, false);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, "", true);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, "", false);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(new Attribute[0], encoding, true);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(new Attribute[0], encoding, false);});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#getObjects(String)}.
	 */
	@Test
	void testGetObjects() {		 
		ObjectBuilder ob = new ObjectBuilder(true);
		List<String []> objects = null;
		try {
			 objects = ob.getObjects("src/test/resources/data/csv/prioritisation1.csv");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		assertTrue(objects != null);
		assertEquals(objects.size(), 579);
	}

}
