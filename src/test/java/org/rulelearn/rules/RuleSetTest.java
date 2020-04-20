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

package org.rulelearn.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.rules.ruleml.RuleMLBuilder;
import org.rulelearn.rules.ruleml.RuleParser;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Tests for {@link RuleSet}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleSetTest {

	private Rule firstRule = null;
	private Rule secondRule = null; 
	
	/**
	 * Set up test environment for each test.
	 */
	@BeforeEach
	public void setUp() {
		ConditionAtLeast<? extends EvaluationField> condition = null;
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();
		conditions.add(condition);
		
		@SuppressWarnings("unchecked")
		ConditionAtLeast<? extends EvaluationField> decision = Mockito.mock(ConditionAtLeast.class);
		
		List<List<Condition<? extends EvaluationField>>> decisions = new ObjectArrayList<>();
		decisions.add(new ObjectArrayList<>());
		decisions.get(0).add(decision);
		
		this.firstRule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_LEAST, conditions, decisions);
		this.secondRule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_MOST, conditions, decisions);
	}
	
	/**
	 * Test method of construction of rule set.
	 */
	@Test
	@SuppressWarnings("unused")
	void testRuleSetConstruction1() {
		try {
			RuleSet ruleSet = new RuleSet(null);
			fail("Construction of a rule set with null content (array of rules) should fail.");
		}
		catch (NullPointerException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Test method for {@link RuleSet#getRule(int)}. 
	 */
	@Test
	void testRuleSetGetRule1() {
		Rule [] rules = new Rule [] {this.firstRule, this.secondRule};
		RuleSet ruleSet = new RuleSet(rules);
		assertEquals(ruleSet.getRule(0), this.firstRule);
		assertEquals(ruleSet.getRule(1), this.secondRule);
	}
	
	/**
	 * Test method for {@link RuleSet#getRule(int)}. 
	 */
	@Test
	void testRuleSetGetRule2() {
		Rule [] rules = new Rule [] {this.firstRule, this.secondRule};
		RuleSet ruleSet = new RuleSet(rules, true);
		assertEquals(ruleSet.getRule(0), this.firstRule);
		assertEquals(ruleSet.getRule(1), this.secondRule);
	}
	
	/**
	 * Test method for {@link RuleSet#size()}. 
	 */
	@Test
	void testRuleSetSize() {
		Rule [] rules = new Rule [] {this.firstRule, this.secondRule};
		RuleSet ruleSet = new RuleSet(rules, false);
		assertEquals(ruleSet.size(), 2);
	}
	
	/**
	 * Integration test for {@link RuleSet#setLearningInformationTableHash()} and {@link RuleSet#getLearningInformationTableHash()}. 
	 */
	@Test
	@Tag("integration")
	void testSetAndGetLearningInformationTableHash() {
		Attribute[] attributes = null;
		AttributeParser attributeParser = new AttributeParser();
		InformationTable informationTable = null;
		RuleParser ruleParser;
		Map<Integer, RuleSetWithCharacteristics> rules;
		RuleSet ruleSet = null;
		
		String attributesFilePath = "src/test/resources/data/csv/ERA-imposed.json";
		String objectsFilePath = "src/test/resources/data/csv/ERA-imposed-train-0.csv";
		String originalRulesFilePath = "src/test/resources/data/ruleml/ERA-train-0.xml";
		String modifiedRulesFilePath = "src/test/resources/data/ruleml/ERA-train-0-hash.xml";
		
		//1) read information table from file and calculate its hash
		try {
			informationTable = InformationTableBuilder.safelyBuildFromCSVFile(attributesFilePath, objectsFilePath, false, ' ');
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
		
		assertTrue(informationTable != null);
		assertEquals(9, informationTable.getNumberOfAttributes());
		assertEquals(750, informationTable.getNumberOfObjects());
		
		String hash = informationTable.getHash();
		System.out.println("Calculated (1st time) hash of learning data: " + hash);
		
		//2) read rules from file (the rules were calculated for the above information table!)
		try (FileReader attributeReader = new FileReader(attributesFilePath)) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				ruleParser = new RuleParser(attributes);
				
				try (FileInputStream fileRulesStream = new FileInputStream(originalRulesFilePath)) {
					rules = ruleParser.parseRulesWithCharacteristics(fileRulesStream);
					if (rules != null) {
						ruleSet = rules.get(1);
						System.out.println(ruleSet.size() + " rules read.");
						System.out.println("Learning data hash stored in rules file = " + ruleSet.getLearningInformationTableHash());
					}
					else {
						fail("Unable to load RuleML file.");
					}
				}
				catch (IOException ex) {
					fail(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON file with meta-data.");
			}
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
		
		//3) store information table hash in rule set
		ruleSet.setLearningInformationTableHash(hash);
		
		//4) serialize modified rule set into XML string and write to RuleML file
		RuleMLBuilder ruleMLBuilder = new RuleMLBuilder();
		String serializedRuleSet = ruleMLBuilder.toRuleMLString(ruleSet, 1);
		try (FileWriter fileWriter = new FileWriter(modifiedRulesFilePath)) {
			fileWriter.write(serializedRuleSet);
			fileWriter.close();
		}
		catch (IOException exception) {
			fail(exception.toString());
		}
		
		//5) parse modified rule set
		ruleParser = new RuleParser(attributes);
		
		try (FileInputStream fileRulesStream = new FileInputStream(modifiedRulesFilePath)) {
			rules = ruleParser.parseRulesWithCharacteristics(fileRulesStream);
			if (rules != null) {
				ruleSet = rules.get(1);
				System.out.println(ruleSet.size() + " rules read.");
				System.out.println("Learning data hash stored in rules file = " + ruleSet.getLearningInformationTableHash());
			}
			else {
				fail("Unable to load RuleML file.");
			}
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
		
		//6) read information table again and again calculate its hash
		try {
			informationTable = InformationTableBuilder.safelyBuildFromCSVFile(attributesFilePath, objectsFilePath, false, ' ');
		}
		catch (IOException ex) {
			fail(ex.toString());
		}
		
		assertTrue(informationTable != null);
		assertEquals(9, informationTable.getNumberOfAttributes());
		assertEquals(750, informationTable.getNumberOfObjects());
		
		hash = informationTable.getHash();
		System.out.println("Calculated (2nd time) hash of learning data: " + hash);
		
		//7) test if the second calculated hash is the same as the one stored in rule set
		assertEquals(hash, ruleSet.getLearningInformationTableHash());
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleSet#serialize()}.
	 */
	@Test
	void testSerialize() {
		String rule1AsText = "(in3_g >= 13.0) & (in1_g >= 14.0) => (out1 >= 9)";
		String rule2AsText = "(in1_g >= 14.0) & (in2_g >= 4.0) => (out1 >= 7)";
		
		Rule ruleMock1 = Mockito.mock(Rule.class);
		Mockito.when(ruleMock1.toString()).thenReturn(rule1AsText);
		Rule ruleMock2 = Mockito.mock(Rule.class);
		Mockito.when(ruleMock2.toString()).thenReturn(rule2AsText);
		
		RuleSet ruleSet = new RuleSet(new Rule[] {ruleMock1, ruleMock2});
		
		String serializedRuleSet = ruleSet.serialize();
		System.out.println(serializedRuleSet); //!
		assertEquals(serializedRuleSet, rule1AsText+System.lineSeparator()
			+rule2AsText+System.lineSeparator());
	}

}
