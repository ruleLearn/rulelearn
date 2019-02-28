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

package org.rulelearn.wrappers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.rules.RuleSet;

/**
 * Tests for {@link VCDomLEMWrapper}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class VCDomLEMWrapperTest {

	@Test
	void testVCDomLEMWrapper() {
		try (FileReader attributeReader = new FileReader("src/test/resources/data/json/metadata-prioritisation.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				org.rulelearn.data.json.ObjectParser objectParser = new org.rulelearn.data.json.ObjectParser.Builder(attributes).build();
				InformationTable informationTable = null;
				try (FileReader objectReader = new FileReader("src/test/resources/data/json/learning-set-prioritisation-2019-02-27.json")) {
					informationTable = objectParser.parseObjects(objectReader);
					if (informationTable != null) {
						assertEquals(50, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
						// induce rules
						VCDomLEMWrapper vcDomLEMWrapper = new VCDomLEMWrapper();
						RuleSet rules = vcDomLEMWrapper.induceRules(informationTable);
						if (rules != null) {
							assertEquals(18, rules.size());
						}
						else { 
							fail("Unable to induce rules with VC-DomLEM");
						}
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
