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
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link ObjectParser}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectParserTest {
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])} and {@link ObjectParser.Builder#build()}.
	 */
	@Test
	void testConstructionOfObjectParser01() {
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])} and {@link ObjectParser.Builder#build()}.
	 */
	@Test
	void testConstructionOfObjectParser02() {
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).missingValueString(null).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).missingValueString(null).build();});
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).missingValueString(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])} and {@link ObjectParser.Builder#build()}.
	 */
	@Test
	void testConstructionOfObjectParser03() {
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(null).missingValueString(null).encoding(null).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).missingValueString(null).encoding(null).build();});
		String mv = "?";
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).missingValueString(mv).encoding(null).build();});
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).missingValueString(mv).encoding(null).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])} and {@link ObjectParser.Builder#build()}.
	 */
	@Test
	void testObjectParser() {
		Attribute[] attributes = new Attribute[1];
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN); 
		String mv = "?";
		String encoding = ObjectBuilder.DEFAULT_ENCODING;
		ObjectParser objectParser = new ObjectParser.Builder(attributes).missingValueString(mv).encoding(encoding).header(false).separator(',').build();
		assertThrows(NullPointerException.class, () -> {objectParser.parseObjects(null);});
	}

	/**
	 * Test method for {@link ObjectParser#parseObjects(java.io.Reader, boolean)}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).header(false).separator('\t').build();
				InformationTable informationTable = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/windsor.csv")) {
					informationTable = objectParser.parseObjects(objectsReader);
					if (informationTable != null) {
						assertEquals(546, informationTable.getNumberOfObjects());
					}
					else {
						fail("Unable to load CSV test file with definition of objects");
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
	 * Test method for {@link ObjectParser#parseObjects(java.io.Reader, boolean)}.
	 */
	@Test
	void testConstructionOfInformationTableBuilderWithMissingValues() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).header(false).separator('\t').build();
				InformationTable informationTable = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/windsor-mv.csv")) {
					informationTable = objectParser.parseObjects(objectsReader);
					if (informationTable != null) {
						UnknownSimpleFieldMV2 missingValue = new UnknownSimpleFieldMV2();
						assertEquals(546, informationTable.getNumberOfObjects());
						// check whether the first values (of the first object) is a missing value
						assertEquals (missingValue, informationTable.getField(0, 0));
						// check whether two first values (of the first object) are the same (they are missing values)
						assertEquals(informationTable.getField(0, 0), informationTable.getField(0, 1));
						assertEquals (missingValue, informationTable.getField(11, 0));
					}
					else {
						fail("Unable to load CSV test file with definition of objects");
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
