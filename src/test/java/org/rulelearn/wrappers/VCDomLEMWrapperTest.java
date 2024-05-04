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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.ruleml.RuleMLBuilder;
import org.rulelearn.rules.ruleml.RuleParser;

/**
 * Tests for {@link VCDomLEMWrapper}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class VCDomLEMWrapperTest {

	/**
	 * Test for {@link VCDomLEMWrapper#induceRules(InformationTable)}.
	 */
	@Test
	@Tag("integration")
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
//							for (int i = 0; i < rules.size(); i++) {
//								//System.out.println(ruleSet.getRule(i).toString(true));
//								System.out.println(rules.getRule(i).toString());
//							} //!
//							System.out.println("---"); //!
							
							assertEquals(17, rules.size()); //using rule conditions generalizer,
							//we get rule (TimeFromDetectTime >= 1531.7505) & (SeverityForAttackCategory <= med)
							//generalized to (TimeFromDetectTime >= 1531.7505) & (SeverityForAttackCategory <= high), which causes that rule
							//(TimeFromDetectTime >= 1983.7672333333333) => (Priority >= 3) becomes redundant and gets deleted
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
	
	/**
	 * Test for {@link VCDomLEMWrapper#induceRulesWithCharacteristics(InformationTable)}.
	 * Induced rules are serialized to RuleML and deserialized as well. 
	 */
	@Test
	@Tag("integration")
	void testVCDomLEMWrapperAndTransformToAndFromRuleML() {
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
						RuleSetWithCharacteristics ruleSetWithCharacteristics = vcDomLEMWrapper.induceRulesWithCharacteristics(informationTable);
						if (ruleSetWithCharacteristics != null) {
//							for (int i = 0; i < ruleSetWithCharacteristics.size(); i++) {
//								//System.out.println(ruleSet.getRule(i).toString(true));
//								System.out.println(ruleSetWithCharacteristics.getRule(i).toString());
//							} //!
//							System.out.println("---"); //!
							
							assertEquals(17, ruleSetWithCharacteristics.size()); //using rule conditions generalizer,
							//we get rule (TimeFromDetectTime >= 1531.7505) & (SeverityForAttackCategory <= med)
							//generalized to (TimeFromDetectTime >= 1531.7505) & (SeverityForAttackCategory <= high), which causes that rule
							//(TimeFromDetectTime >= 1983.7672333333333) => (Priority >= 3) becomes redundant and gets deleted
							
							assertEquals(0.04, ruleSetWithCharacteristics.getRuleCharacteristics(0).getStrength());
							RuleMLBuilder ruleMLBuilder = new RuleMLBuilder();
							// serialize rules
							String serializedRuleSet = ruleMLBuilder.toRuleMLString(ruleSetWithCharacteristics, 1);
							//System.out.println(serializedRuleSet);
							Map<Integer, RuleSetWithCharacteristics> allRules = null;
							RuleParser ruleParser = new RuleParser(attributes);
							RuleSetWithCharacteristics deserializedRuleSet = null;
							// deserialize rules
							try (InputStream rulesStream = new ByteArrayInputStream(serializedRuleSet.getBytes(StandardCharsets.UTF_8.name()))) {
								allRules = ruleParser.parseRulesWithCharacteristics(rulesStream);
							}
							catch (IOException ex) {
								fail(ex.toString());
							}
							if ((allRules != null) && (allRules.size() > 0)) {
								deserializedRuleSet = allRules.get(1);
							}
							assertEquals(17, deserializedRuleSet.size());
							assertEquals(0.04, deserializedRuleSet.getRuleCharacteristics(0).getStrength());
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
