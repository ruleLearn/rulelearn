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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.json.AttributeParser;

/**
 * Test for {@link ObjectBuilder}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectBuilderTest {

	/**
	 * Test method for {@link ObjectBuilder.Builder#attributes(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectBuilder01() {
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder().attributes(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectBuilder.Builder#attributes(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectBuilder02() {
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder().attributes(attributes).build();});
	}
	
	/**
	 * Test method for {@link ObjectBuilder.Builder#encoding(String)}.
	 */
	@Test
	void testConstructionOfObjectBuilder03() {
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder().encoding(null).build();});
	}
	
	
	/**
	 * Test method for {@link ObjectBuilder.Builder#missingValueString(String)}.
	 */
	@Test
	void testConstructionOfObjectBuilder04() {
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder().missingValueString(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#getObjects(String)}.
	 */
	@Test
	void testGetObjects01() {
		ObjectBuilder ob = new ObjectBuilder.Builder().header(true).build();
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
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(objects != null);
		assertEquals(579, objects.size());
	}
	
	/**
	 * Test method for {@link ObjectBuilder#getObjects(String)}.
	 */
	@Test
	void testGetObjects02() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/prioritisation.json")) {
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectBuilder ob = new ObjectBuilder.Builder().attributes(attributes).header(true).build();
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
				catch (IOException ex) {
					System.out.println(ex);
				}
				assertTrue(objects != null);
				assertEquals(579, objects.size());
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * Test method for {@link ObjectBuilder#getObjects(java.io.Reader))}.
	 */
	@Test
	void testGetObjects03() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/prioritisation.json")) {
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectBuilder ob = new ObjectBuilder.Builder().attributes(attributes).header(true).build();
				List<String []> objects = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/prioritisation1.csv")) {
					 objects = ob.getObjects(objectsReader);
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex);
				}
				catch (UnsupportedEncodingException ex) {
					System.out.println(ex);
				}
				catch (IOException ex) {
					System.out.println(ex);
				}
				assertTrue(objects != null);
				assertEquals(579, objects.size());
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * Test method for {@link ObjectBuilder#getObjects(String)}.
	 */
	@Test
	void testGetObjects04() {		 
		ObjectBuilder ob = new ObjectBuilder.Builder().header(false).separator('\t').build();
		List<String []> objects = null;
		try {
			 objects = ob.getObjects("src/test/resources/data/csv/windsor.csv");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(objects != null);
		assertEquals(546, objects.size());
	}
	
	/**
	 * Test method for {@link ObjectBuilder#getObjects(java.io.Reader))}.
	 */
	@Test
	void testGetObjects05() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectBuilder ob = new ObjectBuilder.Builder().attributes(attributes).header(false).separator('\t').build();
				List<String []> objects = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/windsor.csv")) {
					 objects = ob.getObjects(objectsReader);
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex);
				}
				catch (UnsupportedEncodingException ex) {
					System.out.println(ex);
				}
				catch (IOException ex) {
					System.out.println(ex);
				}
				assertTrue(objects != null);
				assertEquals(546, objects.size());
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
	}

}
