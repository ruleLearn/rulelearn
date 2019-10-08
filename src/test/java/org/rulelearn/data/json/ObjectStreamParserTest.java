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

package org.rulelearn.data.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link ObjectStreamParser}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectStreamParserTest {
	
	/**
	 * Test method for {@link ObjectStreamParser.Builder#Builder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectParser01() {
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(null).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).build();});
	}
	
	/**
	 * Test method for {@link ObjectStreamParser.Builder#Builder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectParser02() {
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(null).encoding(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).build();});
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectStreamParser.Builder#Builder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectParser03() {
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(null).encoding(null).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(null).encoding(null).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(null).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(null).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(null).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(null).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectStreamParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectStreamParser#parseObjects(java.io.Reader)}.
	 */
	@Test
	void testParseObjects01() {
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/prioritisation.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ObjectStreamParser objectParser = new ObjectStreamParser.Builder(attributes).build();
				InformationTable informationTable = null;
				try (FileReader objectReader = new FileReader("src/test/resources/data/json/examples.json")) {
					informationTable = objectParser.parseObjects(objectReader);
					if (informationTable != null) {
						assertEquals(2, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
					}
					else {
						fail("Unable to load JSON test file with definition of objects");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test method for {@link ObjectStreamParser#parseObjects(java.io.Reader)}.
	 */
	@Test
	void testParseObjects02() {
		try (FileReader attributeReader = new FileReader("src/test/resources/data/json/metadata-example.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ObjectStreamParser objectParser = new ObjectStreamParser.Builder(attributes).build();
				InformationTable informationTable = null;
				try (FileReader objectReader = new FileReader("src/test/resources/data/json/data-example.json")) {
					informationTable = objectParser.parseObjects(objectReader);
					if (informationTable != null) {
						assertEquals(2, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
					}
					else {
						fail("Unable to load JSON test file with definition of objects");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test method for {@link ObjectStreamParser#parseObjects(java.io.Reader)}.
	 */
	@Test
	void testParseObjects03() {
		try (FileReader attributeReader = new FileReader("src/test/resources/data/json/metadata-prioritisation.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ObjectStreamParser objectParser = new ObjectStreamParser.Builder(attributes).build();
				InformationTable informationTable = null;
				try (FileReader objectReader = new FileReader("src/test/resources/data/json/learning-set-prioritisation-2019-02-27.json")) {
					informationTable = objectParser.parseObjects(objectReader);
					if (informationTable != null) {
						assertEquals(50, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
					}
					else {
						fail("Unable to load JSON test file with definition of objects");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test method for {@link ObjectStreamParser#parseObjects(java.io.Reader)}.
	 */
	@Test
	@Tag("integration")
	void testParseObjects04() {
		try (FileReader attributeReader = new FileReader("src/test/resources/data/json/metadata-prioritisation.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ObjectStreamParser objectParser = new ObjectStreamParser.Builder(attributes).build();
				InformationTable informationTable = null;
				try (FileReader objectReader = new FileReader("src/test/resources/data/json/learning-set-prioritisation-2604v1.json")) {
					informationTable = objectParser.parseObjects(objectReader);
					if (informationTable != null) {
						assertEquals(6853, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
					}
					else {
						fail("Unable to load JSON test file with definition of objects");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test method for {@link ObjectStreamParser#parseObjects(java.io.Reader)}.
	 */
	@Test
	@Tag("integration")
	void testParseObjects05() {
		try (FileReader attributeReader = new FileReader("src/test/resources/data/json/metadata-prioritisation.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ObjectStreamParser objectParser = new ObjectStreamParser.Builder(attributes).build();
				InformationTable informationTable = null;
				try (InputStreamReader objectReader = new InputStreamReader(new FileInputStream("src/test/resources/data/json/learning-set-prioritisation-0609v1.json"), "UTF-8")) {
					informationTable = objectParser.parseObjects(objectReader);
					if (informationTable != null) {
						assertEquals(27824, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
					}
					else {
						fail("Unable to load JSON test file with definition of objects");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}

}
