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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Test for {@link ObjectParser}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectParserTest {
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectParser01() {
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectParser02() {
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).encoding(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).build();});
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectParser03() {
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).encoding(null).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).encoding(null).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(null).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(null).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(null).missingValueString(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(null).missingValueString(ObjectBuilder.DEFAULT_MISSING_VALUE_STRING).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).missingValueString(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser#parseObjects(java.io.Reader)}.
	 */
	@Test
	void testParseObjects01() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/prioritisation.json")) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).build();
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
	 * Test method for {@link ObjectParser#parseObjects(java.io.Reader)}.
	 */
	@Test
	void testParseObjects02() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributeReader = new FileReader("src/test/resources/data/json/metadata-example.json")) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).build();
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

}
