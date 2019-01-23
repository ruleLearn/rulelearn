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

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.json.AttributeParser;

/**
 * Tests for {@link ObjectParser}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectParserTest {
	
	/**
	 * Test method for {@link ObjectParser#ObjectParser(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectParser01() {
		Attribute[] attributes = null;
		assertThrows(NullPointerException.class, () -> {new ObjectParser.Builder(attributes).build();});
	}
	
	/**
	 * Test method for {@link ObjectParser#ObjectParser(Attribute[], String)}.
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
	 * Test method for {@link ObjectParser#ObjectParser(Attribute[], String, String)}.
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
	 * Test method for {@link ObjectParser#parseObjects(java.io.Reader, boolean, char)}.
	 */
	@Test
	void testObjectParser() {
		Attribute[] attributes = new Attribute[1];
		String mv = new String("?");
		String encoding = ObjectBuilder.DEFAULT_ENCODING;
		ObjectParser objectParser = new ObjectParser.Builder(attributes).missingValueString(mv).encoding(encoding).build();
		assertThrows(NullPointerException.class, () -> {objectParser.parseObjects(null, false, ',');});
	}

	/**
	 * Test method for {@link ObjectParser#parseObjects(java.io.Reader, boolean)}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder() {
		Attribute [] attributes = null;
		
		AttributeParser aParser = new AttributeParser();
		try {
			attributes = aParser.parseAttributes(new FileReader("src/test/resources/data/csv/windsor.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		if (attributes != null) {
			ObjectParser oParser = new ObjectParser.Builder(attributes).build();
			InformationTable iTable = null;
			try {
				iTable = oParser.parseObjects(new FileReader("src/test/resources/data/csv/windsor.csv"), false, '\t');
			}
			catch (FileNotFoundException ex) {
				System.out.println(ex.toString());
			}
			if (iTable != null) {
				assertEquals(546, iTable.getNumberOfObjects());
			}
			else {
				fail("Unable to load CSV test file with definition of objects");
			}
		}
		else {
			fail("Unable to load CSV test file with definition of attributes");
		}

	}

}
