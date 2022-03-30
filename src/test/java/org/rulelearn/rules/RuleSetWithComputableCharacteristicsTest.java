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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.InvalidSizeException;

/**
 * Tests for {@link RuleSetWithComputableCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleSetWithComputableCharacteristicsTest {

	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[])}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristics01() {
		try {
			new RuleSetWithComputableCharacteristics(null, new RuleCoverageInformation[] {Mockito.mock(RuleCoverageInformation.class)});
			fail("Should not create rule set with computable characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[])}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristics02() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, null);
			fail("Should not create rule set with computable characteristics for null array with rule coverage information.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[])}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristics03() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, new RuleCoverageInformation[] {Mockito.mock(RuleCoverageInformation.class)});
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and array of rule coverage information.");
		}
	}	
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean01() {
		try {
			new RuleSetWithComputableCharacteristics(null, new RuleCoverageInformation[] {Mockito.mock(RuleCoverageInformation.class)}, true);
			fail("Should not create rule set with computable characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean02() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, null, true);
			fail("Should not create rule set with computable characteristics for null array of rule coverage information.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean03() {
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics;
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[] {Mockito.mock(RuleCoverageInformation.class)};
		try {
			ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)},
					ruleCoverageInformationArray, true); //allow cloning
			assertTrue(ruleSetWithComputableCharacteristics.ruleCoverageInformationArray == ruleCoverageInformationArray);
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and array of rule coverage information.");
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean04() {
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics;
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[] {Mockito.mock(RuleCoverageInformation.class)};
		try {
			ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)},
					ruleCoverageInformationArray, false); //avoid cloning
			assertFalse(ruleSetWithComputableCharacteristics.ruleCoverageInformationArray == ruleCoverageInformationArray);
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and array of rule coverage information.");
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInformation[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean05() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class), Mockito.mock(Rule.class)},
					new RuleCoverageInformation[] {Mockito.mock(RuleCoverageInformation.class)}, false); //different number of rules and rule coverage information objects
			fail("Should not create rule set with computable characteristics for different number of rules and rule coverage information objects.");
		} catch (InvalidSizeException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithComputableCharacteristics#getRuleCharacteristics(int)}.
	 */
	@Test
	void testGetRuleCharacteristicsInt01() {
		Rule rule = Mockito.mock(Rule.class);
		RuleCoverageInformation ruleCoverageInformation = Mockito.mock(RuleCoverageInformation.class);
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {rule},
				new RuleCoverageInformation[] {ruleCoverageInformation});
		
		ComputableRuleCharacteristics computableRuleCharacteristics = ruleSetWithComputableCharacteristics.getRuleCharacteristics(0);
		
		assertTrue(computableRuleCharacteristics instanceof ComputableRuleCharacteristics);
		assertTrue(computableRuleCharacteristics.getRuleCoverageInformation() == ruleCoverageInformation);
		
		assertTrue(computableRuleCharacteristics == ruleSetWithComputableCharacteristics.getRuleCharacteristics(0)); //test if existing object is returned
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithComputableCharacteristics#getRuleCharacteristics(int)}.
	 */
	@Test
	void testGetRuleCharacteristicsInt02() {
		Rule rule0 = Mockito.mock(Rule.class);
		Rule rule1 = Mockito.mock(Rule.class);
		RuleCoverageInformation ruleCoverageInformation0 = Mockito.mock(RuleCoverageInformation.class);
		RuleCoverageInformation ruleCoverageInformation1 = Mockito.mock(RuleCoverageInformation.class);
		
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {rule0, rule1},
				new RuleCoverageInformation[] {ruleCoverageInformation0, ruleCoverageInformation1}); //two rules, two rule coverage information objects
		
		ComputableRuleCharacteristics computableRuleCharacteristics = ruleSetWithComputableCharacteristics.getRuleCharacteristics(0);
		
		assertTrue(computableRuleCharacteristics instanceof ComputableRuleCharacteristics);
		assertTrue(computableRuleCharacteristics.getRuleCoverageInformation() == ruleCoverageInformation0);
		
		assertTrue(computableRuleCharacteristics == ruleSetWithComputableCharacteristics.getRuleCharacteristics(0)); //test if existing object is returned
		
		//---
		
		computableRuleCharacteristics = ruleSetWithComputableCharacteristics.getRuleCharacteristics(1);
		
		assertTrue(computableRuleCharacteristics instanceof ComputableRuleCharacteristics);
		assertTrue(computableRuleCharacteristics.getRuleCoverageInformation() == ruleCoverageInformation1);
		
		assertTrue(computableRuleCharacteristics == ruleSetWithComputableCharacteristics.getRuleCharacteristics(1)); //test if existing object is returned
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#join(RuleSetWithComputableCharacteristics, RuleSetWithComputableCharacteristics)}.
	 */
	@Test
	void testJoin() {
		int size1 = 5; //>=1
		Rule[] rules1 = new Rule[size1];
		RuleCoverageInformation[] ruleCoverageInformationArray1 = new RuleCoverageInformation[size1];
		ComputableRuleCharacteristics[] computableRuleCharacteristics1 = new ComputableRuleCharacteristics[size1];
		for (int i = 0; i < size1; i++) {
			rules1[i] = Mockito.mock(Rule.class);
			ruleCoverageInformationArray1[i] = Mockito.mock(RuleCoverageInformation.class);
			computableRuleCharacteristics1[i] = Mockito.mock(ComputableRuleCharacteristics.class);
		}
		computableRuleCharacteristics1[0] = null; //assume first computable rule characteristics have not been calculated yet
		computableRuleCharacteristics1[size1 - 1] = null; //assume last computable rule characteristics have not been calculated yet
		
		int size2 = 4; //>=1
		Rule[] rules2 = new Rule[size2];
		RuleCoverageInformation[] ruleCoverageInformationArray2 = new RuleCoverageInformation[size2];
		ComputableRuleCharacteristics[] computableRuleCharacteristics2 = new ComputableRuleCharacteristics[size2];
		for (int i = 0; i < size2; i++) {
			rules2[i] = Mockito.mock(Rule.class);
			ruleCoverageInformationArray2[i] = Mockito.mock(RuleCoverageInformation.class);
			computableRuleCharacteristics2[i] = Mockito.mock(ComputableRuleCharacteristics.class);
		}
		computableRuleCharacteristics2[0] = null; //assume first computable rule characteristics have not been calculated yet
		computableRuleCharacteristics2[size2 - 1] = null; //assume last computable rule characteristics have not been calculated yet
		
		RuleSetWithComputableCharacteristics ruleSet1 = new RuleSetWithComputableCharacteristics(rules1, ruleCoverageInformationArray1, true);
		ruleSet1.ruleCharacteristics = computableRuleCharacteristics1; //override computable rule characteristics
		
		RuleSetWithComputableCharacteristics ruleSet2 = new RuleSetWithComputableCharacteristics(rules2, ruleCoverageInformationArray2, true);
		ruleSet2.ruleCharacteristics = computableRuleCharacteristics2; //override computable rule characteristics
		
		RuleSetWithComputableCharacteristics jointRuleSet = RuleSetWithComputableCharacteristics.join(ruleSet1, ruleSet2);
		
		assertEquals(jointRuleSet.size(), size1 + size2);
		assertEquals(jointRuleSet.getRule(0), ruleSet1.getRule(0));
		assertEquals(jointRuleSet.getRule(1), ruleSet1.getRule(1));
		assertEquals(jointRuleSet.getRule(2), ruleSet1.getRule(2));
		assertEquals(jointRuleSet.getRule(3), ruleSet1.getRule(3));
		assertEquals(jointRuleSet.getRule(4), ruleSet1.getRule(4));
		assertEquals(jointRuleSet.getRule(5), ruleSet2.getRule(0));
		assertEquals(jointRuleSet.getRule(6), ruleSet2.getRule(1));
		assertEquals(jointRuleSet.getRule(7), ruleSet2.getRule(2));
		assertEquals(jointRuleSet.getRule(8), ruleSet2.getRule(3));
		assertEquals(jointRuleSet.ruleCoverageInformationArray[0], ruleSet1.ruleCoverageInformationArray[0]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[1], ruleSet1.ruleCoverageInformationArray[1]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[2], ruleSet1.ruleCoverageInformationArray[2]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[3], ruleSet1.ruleCoverageInformationArray[3]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[4], ruleSet1.ruleCoverageInformationArray[4]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[5], ruleSet2.ruleCoverageInformationArray[0]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[6], ruleSet2.ruleCoverageInformationArray[1]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[7], ruleSet2.ruleCoverageInformationArray[2]);
		assertEquals(jointRuleSet.ruleCoverageInformationArray[8], ruleSet2.ruleCoverageInformationArray[3]);
		assertEquals(jointRuleSet.ruleCharacteristics[0], null);
		assertEquals(jointRuleSet.ruleCharacteristics[1], ruleSet1.ruleCharacteristics[1]);
		assertEquals(jointRuleSet.ruleCharacteristics[2], ruleSet1.ruleCharacteristics[2]);
		assertEquals(jointRuleSet.ruleCharacteristics[3], ruleSet1.ruleCharacteristics[3]);
		assertEquals(jointRuleSet.ruleCharacteristics[4], null);
		assertEquals(jointRuleSet.ruleCharacteristics[5], null);
		assertEquals(jointRuleSet.ruleCharacteristics[6], ruleSet2.ruleCharacteristics[1]);
		assertEquals(jointRuleSet.ruleCharacteristics[7], ruleSet2.ruleCharacteristics[2]);
		assertEquals(jointRuleSet.ruleCharacteristics[8], null);
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#filter(RuleSetWithComputableCharacteristics, RuleFilter)}.
	 * Tests filtration using strict confidence filter.
	 */
	@Test
	void testFilter01() {
		int size = 5;
		Rule[] rules = new Rule[size];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[size];
		ComputableRuleCharacteristics[] computableRuleCharacteristics = new ComputableRuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCoverageInformationArray[i] = Mockito.mock(RuleCoverageInformation.class);
			computableRuleCharacteristics[i] = Mockito.mock(ComputableRuleCharacteristics.class);
		}
		
		Mockito.when(computableRuleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(computableRuleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(computableRuleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(computableRuleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(computableRuleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithComputableCharacteristics ruleSet = new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true);
		ruleSet.ruleCharacteristics = computableRuleCharacteristics; //override computable rule characteristics
		
		RuleSetWithComputableCharacteristics filteredRuleSet = RuleSetWithComputableCharacteristics.filter(ruleSet, new ConfidenceRuleFilter(0.5, true));
		
		assertEquals(filteredRuleSet.size(), 2);
		assertEquals(filteredRuleSet.getRule(0), ruleSet.getRule(2));
		assertEquals(filteredRuleSet.getRule(1), ruleSet.getRule(3));
		assertEquals(filteredRuleSet.ruleCoverageInformationArray[0], ruleSet.ruleCoverageInformationArray[2]);
		assertEquals(filteredRuleSet.ruleCoverageInformationArray[1], ruleSet.ruleCoverageInformationArray[3]);
		assertEquals(filteredRuleSet.ruleCharacteristics[0], ruleSet.ruleCharacteristics[2]);
		assertEquals(filteredRuleSet.ruleCharacteristics[1], ruleSet.ruleCharacteristics[3]);
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#filter(RuleSetWithComputableCharacteristics, RuleFilter)}.
	 * Tests filtration using weak confidence filter.
	 */
	@Test
	void testFilter02() {
		int size = 5;
		Rule[] rules = new Rule[size];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[size];
		ComputableRuleCharacteristics[] computableRuleCharacteristics = new ComputableRuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCoverageInformationArray[i] = Mockito.mock(RuleCoverageInformation.class);
			computableRuleCharacteristics[i] = Mockito.mock(ComputableRuleCharacteristics.class);
		}
		
		Mockito.when(computableRuleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(computableRuleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(computableRuleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(computableRuleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(computableRuleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithComputableCharacteristics ruleSet = new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true);
		ruleSet.ruleCharacteristics = computableRuleCharacteristics; //override computable rule characteristics
		
		RuleSetWithComputableCharacteristics filteredRuleSet = RuleSetWithComputableCharacteristics.filter(ruleSet, new ConfidenceRuleFilter(0.5, false));
		
		assertEquals(filteredRuleSet.size(), 4);
		assertEquals(filteredRuleSet.getRule(0), ruleSet.getRule(1));
		assertEquals(filteredRuleSet.getRule(1), ruleSet.getRule(2));
		assertEquals(filteredRuleSet.getRule(2), ruleSet.getRule(3));
		assertEquals(filteredRuleSet.getRule(3), ruleSet.getRule(4));
		assertEquals(filteredRuleSet.ruleCoverageInformationArray[0], ruleSet.ruleCoverageInformationArray[1]);
		assertEquals(filteredRuleSet.ruleCoverageInformationArray[1], ruleSet.ruleCoverageInformationArray[2]);
		assertEquals(filteredRuleSet.ruleCoverageInformationArray[2], ruleSet.ruleCoverageInformationArray[3]);
		assertEquals(filteredRuleSet.ruleCoverageInformationArray[3], ruleSet.ruleCoverageInformationArray[4]);
		assertEquals(filteredRuleSet.ruleCharacteristics[0], ruleSet.ruleCharacteristics[1]);
		assertEquals(filteredRuleSet.ruleCharacteristics[1], ruleSet.ruleCharacteristics[2]);
		assertEquals(filteredRuleSet.ruleCharacteristics[2], ruleSet.ruleCharacteristics[3]);
		assertEquals(filteredRuleSet.ruleCharacteristics[3], ruleSet.ruleCharacteristics[4]);
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#filter(RuleFilter)}.
	 * Tests filtration using strict confidence filter that should filter all rules.
	 */
	@Test
	void testFilter03() {
		int size = 5;
		Rule[] rules = new Rule[size];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[size];
		ComputableRuleCharacteristics[] computableRuleCharacteristics = new ComputableRuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCoverageInformationArray[i] = Mockito.mock(RuleCoverageInformation.class);
			computableRuleCharacteristics[i] = Mockito.mock(ComputableRuleCharacteristics.class);
		}
		
		Mockito.when(computableRuleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(computableRuleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(computableRuleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(computableRuleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(computableRuleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithComputableCharacteristics ruleSet = new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true);
		ruleSet.ruleCharacteristics = computableRuleCharacteristics; //override computable rule characteristics
		
		RuleSetWithComputableCharacteristics filteredRuleSet = ruleSet.filter(new ConfidenceRuleFilter(0.7, true));
		
		assertEquals(filteredRuleSet.size(), 0);
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#filter(RuleFilter)}.
	 * Tests if {@link AcceptingRuleFilter} causes that the original rule set is returned.
	 */
	@Test
	void testFilter04() {
		int size = 5;
		Rule[] rules = new Rule[size];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[size];
		ComputableRuleCharacteristics[] computableRuleCharacteristics = new ComputableRuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCoverageInformationArray[i] = Mockito.mock(RuleCoverageInformation.class);
			computableRuleCharacteristics[i] = Mockito.mock(ComputableRuleCharacteristics.class);
		}
		
		Mockito.when(computableRuleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(computableRuleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(computableRuleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(computableRuleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(computableRuleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithComputableCharacteristics ruleSet = new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true);
		ruleSet.ruleCharacteristics = computableRuleCharacteristics; //override computable rule characteristics
		
		RuleSetWithComputableCharacteristics filteredRuleSet = ruleSet.filter(new AcceptingRuleFilter());
		
		assertSame(ruleSet, filteredRuleSet);
	}

}
