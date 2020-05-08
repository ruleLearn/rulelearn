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

package org.rulelearn.rules.ruleml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.RuleType;

/**
 * Test for {@link RuleParser}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class RuleParserTest {

	/**
	 * Tests parsing RuleML file.
	 */
	@Test
	public void testLoading01() {
		Attribute [] attributes = null;
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/prioritisation.json")) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				Map<Integer, RuleSet> rules = null;
				RuleParser ruleParser = new RuleParser(attributes);
				try (FileInputStream fileRulesStream = new FileInputStream("src/test/resources/data/ruleml/prioritisation1.rules.xml")) {
					rules = ruleParser.parseRules(fileRulesStream);
					if (rules != null) {
						assertEquals(rules.size(), 1);
						RuleSet firstRuleSet = rules.get(1);
						System.out.println(firstRuleSet.size() + " rules read.");
						
						assertEquals(firstRuleSet.size(), 2);
					}
					else {
						fail("Unable to load RuleML file.");
					}
				}
				catch (FileNotFoundException ex) {
					fail(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON file with meta-data.");
			}
		}
		catch (FileNotFoundException ex) {
			fail(ex.toString());
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
	}
	
	/**
	 * Tests parsing RuleML file with certain rules.
	 */
	@Test
	public void testLoading02() {
		Attribute [] attributes = null;
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				Map<Integer, RuleSet> rules = null;
				RuleParser ruleParser = new RuleParser(attributes);
				try (FileInputStream fileRulesStream = new FileInputStream("src/test/resources/data/ruleml/windsor-mv-certain.rules.xml")) {
					rules = ruleParser.parseRules(fileRulesStream);
					if (rules != null) {
						assertEquals(rules.size(), 1);
						RuleSet firstRuleSet = rules.get(1);
						System.out.println(firstRuleSet.size() + " rules read.");
						
						assertEquals(firstRuleSet.getRule(0).getType(), RuleType.CERTAIN);
					}
					else {
						fail("Unable to load RuleML file.");
					}
				}
				catch (FileNotFoundException ex) {
					fail(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON file with meta-data.");
			}
		}
		catch (FileNotFoundException ex) {
			fail(ex.toString());
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
	}
	
	/**
	 * Tests parsing RuleML file with possible rules.
	 */
	@Test
	public void testLoading03() {
		Attribute [] attributes = null;
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				Map<Integer, RuleSet> rules = null;
				RuleParser ruleParser = new RuleParser(attributes);
				try (FileInputStream fileRulesStream = new FileInputStream("src/test/resources/data/ruleml/windsor-mv1.5-possible.rules.xml")) {
					rules = ruleParser.parseRules(fileRulesStream);
					if (rules != null) {
						assertEquals(rules.size(), 1);
						RuleSet firstRuleSet = rules.get(1);
						System.out.println(firstRuleSet.size() + " rules read.");
						
						assertEquals(firstRuleSet.getRule(0).getType(), RuleType.POSSIBLE);
					}
					else {
						fail("Unable to load RuleML file.");
					}
				}
				catch (FileNotFoundException ex) {
					fail(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON file with meta-data.");
			}
		}
		catch (FileNotFoundException ex) {
			fail(ex.toString());
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
	}
	
	/**
	 * Tests parsing a rule set (with and without characteristics) from a RuleML file with certain and possible rules,
	 * which is marked either by &lt;implies type="certain"&gt; or &lt;implies type="possible"&gt;.
	 * or by &lt;ruleType&gt;certain&lt;/ruleType&gt; or &lt;ruleType&gt;possible&lt;/ruleType&gt;,
	 * or both ways (consistently or not).
	 */
	@Test
	public void testLoading04() {
		Attribute [] attributes = null;
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/ERA-imposed.json")) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				RuleParser ruleParser = new RuleParser(attributes);
				
				try (FileInputStream fileRulesStream = new FileInputStream("src/test/resources/data/ruleml/ERA-test-different-rule-types.xml")) {
					
					//1) parse bare rules
					Map<Integer, RuleSet> rules = ruleParser.parseRules(fileRulesStream);
					if (rules != null) {
						assertEquals(rules.size(), 1);
						RuleSet firstRuleSet = rules.get(1);
						
						System.out.println(firstRuleSet.size() + " rules read.");
						
						assertEquals(firstRuleSet.size(), 7);
						assertEquals(firstRuleSet.getRule(0).getType(), RuleType.CERTAIN); //<implies>, no <ruleType> tag (should set default rule type, i.e., certain)
						assertEquals(firstRuleSet.getRule(1).getType(), RuleType.CERTAIN); //<implies type="certain">, no <ruleType> tag
						assertEquals(firstRuleSet.getRule(2).getType(), RuleType.POSSIBLE); //<implies type="possible">, no <ruleType> tag
						assertEquals(firstRuleSet.getRule(3).getType(), RuleType.CERTAIN); //<implies>, <ruleType>certain</ruleType>
						assertEquals(firstRuleSet.getRule(4).getType(), RuleType.POSSIBLE); //<implies>, <ruleType>possible</ruleType>
						assertEquals(firstRuleSet.getRule(5).getType(), RuleType.CERTAIN); //<implies type="certain">, <ruleType>certain</ruleType>
						assertEquals(firstRuleSet.getRule(6).getType(), RuleType.POSSIBLE); //<implies type="possible">, <ruleType>possible</ruleType>
					}
					else {
						fail("Unable to load RuleML file.");
					}
				}
				catch (FileNotFoundException ex) {
					fail(ex.toString());
				}
					
				//------------------------------------
				
				try (FileInputStream fileRulesStream = new FileInputStream("src/test/resources/data/ruleml/ERA-test-different-rule-types.xml")) {
					//2) parse rules witch characteristics
					Map<Integer, RuleSetWithCharacteristics> rules = ruleParser.parseRulesWithCharacteristics(fileRulesStream);
					if (rules != null) {
						assertEquals(rules.size(), 1);
						RuleSet firstRuleSet = rules.get(1);
						
						System.out.println(firstRuleSet.size() + " rules read.");
						
						assertEquals(firstRuleSet.size(), 7);
						assertEquals(firstRuleSet.getRule(0).getType(), RuleType.CERTAIN); //<implies>, no <ruleType> tag (should set default rule type, i.e., certain)
						assertEquals(firstRuleSet.getRule(1).getType(), RuleType.CERTAIN); //<implies type="certain">, no <ruleType> tag
						assertEquals(firstRuleSet.getRule(2).getType(), RuleType.POSSIBLE); //<implies type="possible">, no <ruleType> tag
						assertEquals(firstRuleSet.getRule(3).getType(), RuleType.CERTAIN); //<implies>, <ruleType>certain</ruleType>
						assertEquals(firstRuleSet.getRule(4).getType(), RuleType.POSSIBLE); //<implies>, <ruleType>possible</ruleType>
						assertEquals(firstRuleSet.getRule(5).getType(), RuleType.CERTAIN); //<implies type="certain">, <ruleType>certain</ruleType>
						assertEquals(firstRuleSet.getRule(6).getType(), RuleType.POSSIBLE); //<implies type="possible">, <ruleType>possible</ruleType>
					}
					else {
						fail("Unable to load RuleML file.");
					}
				}
				catch (FileNotFoundException ex) {
					fail(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON file with meta-data.");
			}
		}
		catch (FileNotFoundException ex) {
			fail(ex.toString());
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
	}
	
}
