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

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.json.AttributeParser;

/**
 * Tests for {@link InformationTableWriter}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class InformationTableWriterTest {

	/**
	 * Test method for {@link org.rulelearn.data.csv.InformationTableWriter#writeAttributes(org.rulelearn.data.InformationTable, java.io.Writer)}
	 * and {@link org.rulelearn.data.csv.InformationTableWriter#writeObjects(org.rulelearn.data.InformationTable, java.io.Writer, java.lang.String)}.
	 * Tests if an information table can be saved as a pair of JSON + CSV files.
	 */
	@Test
	void testWriteAttributesAndObjects() {
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
						InformationTableWriter informationTableWriter = new InformationTableWriter();
						
						try (FileWriter fileWriter = new FileWriter("src/test/resources/data/json/windsor-metadata-2-generated.json")) {
							informationTableWriter.writeAttributes(informationTable, fileWriter);
						}
						catch (IOException exception) {
							fail(exception.toString());
						}
						
						try (FileWriter fileWriter = new FileWriter("src/test/resources/data/csv/windsor-mv-generated.csv")) {
							informationTableWriter.writeObjects(informationTable, fileWriter, ","); //use comma as separator
						}
						catch (IOException exception) {
							fail(exception.toString());
						}
					}
					else {
						fail("Unable to load CSV test file with definition of objects.");
					}
				}
				catch (FileNotFoundException exception) {
					fail(exception.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes.");
			}
		}
		catch (FileNotFoundException exception) {
			fail(exception.toString());
		}
		catch (IOException exception) {
			fail(exception.toString());
		}
	}

}
