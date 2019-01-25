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
		Attribute[] attributes = null;
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])} and {@link ObjectParser.Builder#build()}.
	 */
	@Test
	void testConstructionOfObjectParser02() {
		Attribute[] attributes = null;
		String mv = null;
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).missingValueString(mv).build();});
		Attribute[] newAttributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(newAttributes).missingValueString(mv).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])} and {@link ObjectParser.Builder#build()}.
	 */
	@Test
	void testConstructionOfObjectParser03() {
		Attribute[] attributes = null;
		String mv = null;
		String encoding = null;
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).missingValueString(mv).encoding(encoding).build();});
		Attribute[] newAttributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(newAttributes).missingValueString(mv).encoding(encoding).build();});
		String newMv = "?";
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(newAttributes).missingValueString(newMv).encoding(encoding).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser.Builder#Builder(Attribute[])} and {@link ObjectParser.Builder#build()}.
	 */
	@Test
	void testObjectParser() {
		Attribute[] attributes = new Attribute[1];
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN); 
		String mv = new String("?");
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
				fail("Unable to load CSV test file with definition of attributes");
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
