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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.InvalidSizeException;

/**
 * Tests for {@link RuleSetWithCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleSetWithCharacteristicsTest {
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[])}.
	 */
	@Test
	void testRuleSetWithCharacteristics01() {
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[0];
		try {
			new RuleSetWithCharacteristics(null, ruleCharacteristics);
			fail("Should not create rule set with characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[])}.
	 */
	@Test
	void testRuleSetWithCharacteristics02() {
		Rule[] rules = new Rule[0];
		try {
			new RuleSetWithCharacteristics(rules, null);
			fail("Should not create rule set with characteristics for null rule characteristics.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[])}.
	 */
	@Test
	void testRuleSetWithCharacteristics03() {
		Rule[] rules = new Rule[0];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[1];
		try {
			new RuleSetWithCharacteristics(rules, ruleCharacteristics);
		} catch (InvalidSizeException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[])}.
	 */
	@Test
	void testRuleSetWithCharacteristics04() {
		try {
			Rule[] rules = new Rule[2];
			Rule rule0 = Mockito.mock(Rule.class);
			rules[0] = rule0;
			Rule rule1 = Mockito.mock(Rule.class);
			rules[1] = rule1;
			
			RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[2];
			RuleCharacteristics ruleCharacteristics0 = Mockito.mock(RuleCharacteristics.class);
			ruleCharacteristics[0] = ruleCharacteristics0;
			RuleCharacteristics ruleCharacteristics1 = Mockito.mock(RuleCharacteristics.class);
			ruleCharacteristics[1] = ruleCharacteristics1;
			
			RuleSetWithCharacteristics ruleSetWithCharacteristics = new RuleSetWithCharacteristics(rules, ruleCharacteristics);
			
			assertTrue(ruleSetWithCharacteristics.getRule(0) == rule0);
			assertTrue(ruleSetWithCharacteristics.getRule(1) == rule1);
			assertTrue(ruleSetWithCharacteristics.getRuleCharacteristics(0) == ruleCharacteristics0);
			assertTrue(ruleSetWithCharacteristics.getRuleCharacteristics(1) == ruleCharacteristics1);
		} catch (Exception exception) {
			fail("Could not construct rule set with characteristics.");
		}
	}

	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[], boolean)}.
	 */
	@Test
	void testRuleSetWithCharacteristicsBoolean01() {
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[0];
		try {
			new RuleSetWithCharacteristics(null, ruleCharacteristics, true);
			fail("Should not create rule set with characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[], boolean)}.
	 */
	@Test
	void testRuleSetWithCharacteristicsBoolean02() {
		Rule[] rules = new Rule[0];
		try {
			new RuleSetWithCharacteristics(rules, null, true);
			fail("Should not create rule set with characteristics for null rule characteristics.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[], boolean)}.
	 */
	@Test
	void testRuleSetWithCharacteristicsBoolean03() {
		Rule[] rules = new Rule[0];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[1];
		try {
			new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
		} catch (InvalidSizeException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#RuleSetWithCharacteristics(Rule[], RuleCharacteristics[], boolean)}.
	 */
	@Test
	void testRuleSetWithCharacteristicsBoolean04() {
		try {
			Rule rule0 = Mockito.mock(Rule.class);
			Rule rule1 = Mockito.mock(Rule.class);
			Rule[] rules = new Rule[] {rule0, rule1};
			
			RuleCharacteristics ruleCharacteristics0 = Mockito.mock(RuleCharacteristics.class);
			RuleCharacteristics ruleCharacteristics1 = Mockito.mock(RuleCharacteristics.class);
			RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[] {ruleCharacteristics0, ruleCharacteristics1};
			
			RuleSetWithCharacteristics ruleSetWithCharacteristics = new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
			
			assertTrue(ruleSetWithCharacteristics.getRule(0) == rule0);
			assertTrue(ruleSetWithCharacteristics.getRule(1) == rule1);
			assertTrue(ruleSetWithCharacteristics.getRuleCharacteristics(0) == ruleCharacteristics0);
			assertTrue(ruleSetWithCharacteristics.getRuleCharacteristics(1) == ruleCharacteristics1);
		} catch (Exception exception) {
			fail("Could not construct rule set with characteristics.");
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithCharacteristics#RuleSetWithCharacteristics(org.rulelearn.rules.Rule[])}.
	 */
	@Test
	void testRuleSetWithCharacteristicsRuleArray01() {
		try {
			new RuleSetWithCharacteristics(null);
			fail("Should not create rule set with characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithCharacteristics#RuleSetWithCharacteristics(org.rulelearn.rules.Rule[])}.
	 */
	@Test
	void testRuleSetWithCharacteristicsRuleArray02() {
		RuleSetWithCharacteristics ruleSetWithCharacteristics = null;
		Rule[] rules = new Rule[2];
		Rule rule0 = Mockito.mock(Rule.class);
		Rule rule1 = Mockito.mock(Rule.class);
		rules[0] = rule0;
		rules[1] = rule1;
		
		try {
			ruleSetWithCharacteristics = new RuleSetWithCharacteristics(rules);
			assertTrue(ruleSetWithCharacteristics.getRule(1) == rule1);
		} catch (NullPointerException exception) {
			fail("Should not create rule set with characteristics for null rules.");
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithCharacteristics#RuleSetWithCharacteristics(org.rulelearn.rules.Rule[], boolean)}.
	 */
	@Test
	void testRuleSetWithCharacteristicsRuleArrayBoolean01() {
		try {
			new RuleSetWithCharacteristics(null, true);
			fail("Should not create rule set with characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithCharacteristics#RuleSetWithCharacteristics(org.rulelearn.rules.Rule[], boolean)}.
	 */
	@Test
	void testRuleSetWithCharacteristicsRuleArrayBoolean02() {
		RuleSetWithCharacteristics ruleSetWithCharacteristics = null;
		Rule[] rules = new Rule[2];
		Rule rule0 = Mockito.mock(Rule.class);
		Rule rule1 = Mockito.mock(Rule.class);
		rules[0] = rule0;
		rules[1] = rule1;
		
		try {
			ruleSetWithCharacteristics = new RuleSetWithCharacteristics(rules, true);
			assertTrue(ruleSetWithCharacteristics.getRule(1) == rule1);
		} catch (NullPointerException exception) {
			fail("Should not create rule set with characteristics for null rules.");
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithCharacteristics#getRuleCharacteristics(int)}.
	 */
	@Test
	void testGetRuleCharacteristics() {
		Rule[] rules = new Rule[] {Mockito.mock(Rule.class), Mockito.mock(Rule.class)};
		
		RuleCharacteristics ruleCharacteristics0 = Mockito.mock(RuleCharacteristics.class);
		RuleCharacteristics ruleCharacteristics1 = Mockito.mock(RuleCharacteristics.class);
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[] {ruleCharacteristics0, ruleCharacteristics1};
		
		RuleSetWithCharacteristics ruleSetWithCharacteristics = new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
		
		assertTrue(ruleSetWithCharacteristics.getRuleCharacteristics(0) == ruleCharacteristics0);
		assertTrue(ruleSetWithCharacteristics.getRuleCharacteristics(1) == ruleCharacteristics1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithCharacteristics#serialize()}.
	 */
	@Test
	void testSerialize() {
		String ruleAsText = "(in3_g >= 13.0) & (in1_g >= 14.0) => (out1 >= 9)";
		
		Rule ruleMock = Mockito.mock(Rule.class);
		Mockito.when(ruleMock.toString()).thenReturn(ruleAsText);
		
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.isSupportSet()).thenReturn(true);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(10);
		Mockito.when(ruleCharacteristicsMock.isStrengthSet()).thenReturn(true);
		Mockito.when(ruleCharacteristicsMock.getStrength()).thenReturn(0.2);
		Mockito.when(ruleCharacteristicsMock.isCoverageFactorSet()).thenReturn(true);
		Mockito.when(ruleCharacteristicsMock.getCoverageFactor()).thenReturn(0.3);
		Mockito.when(ruleCharacteristicsMock.isConfidenceSet()).thenReturn(false);
		Mockito.when(ruleCharacteristicsMock.isEpsilonSet()).thenReturn(true);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.1);
		
		RuleSetWithCharacteristics ruleSetWithCharacteristics = new RuleSetWithCharacteristics(new Rule[] {ruleMock}, new RuleCharacteristics[] {ruleCharacteristicsMock});
		
		String serializedRuleSet = ruleSetWithCharacteristics.serialize();
		System.out.println(serializedRuleSet); //!
		assertEquals(serializedRuleSet, ruleAsText+" [support=10, strength=0.2, coverage-factor=0.3, confidence=?, epsilon=0.1]"+System.lineSeparator());
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#join(RuleSetWithCharacteristics, RuleSetWithCharacteristics)}.
	 */
	@Test
	void testJoin() {
		int size1 = 5; //>=1
		Rule[] rules1 = new Rule[size1];
		RuleCharacteristics[] ruleCharacteristics1 = new RuleCharacteristics[size1];
		for (int i = 0; i < size1; i++) {
			rules1[i] = Mockito.mock(Rule.class);
			ruleCharacteristics1[i] = Mockito.mock(RuleCharacteristics.class);
		}
		
		int size2 = 4; //>=1
		Rule[] rules2 = new Rule[size2];
		RuleCharacteristics[] ruleCharacteristics2 = new RuleCharacteristics[size2];
		for (int i = 0; i < size2; i++) {
			rules2[i] = Mockito.mock(Rule.class);
			ruleCharacteristics2[i] = Mockito.mock(RuleCharacteristics.class);
		}
		
		RuleSetWithCharacteristics ruleSet1 = new RuleSetWithCharacteristics(rules1, ruleCharacteristics1, true);
		RuleSetWithCharacteristics ruleSet2 = new RuleSetWithCharacteristics(rules2, ruleCharacteristics2, true);
		
		RuleSetWithCharacteristics jointRuleSet = RuleSetWithCharacteristics.join(ruleSet1, ruleSet2);
		
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
		assertEquals(jointRuleSet.ruleCharacteristics[0], ruleSet1.ruleCharacteristics[0]);
		assertEquals(jointRuleSet.ruleCharacteristics[1], ruleSet1.ruleCharacteristics[1]);
		assertEquals(jointRuleSet.ruleCharacteristics[2], ruleSet1.ruleCharacteristics[2]);
		assertEquals(jointRuleSet.ruleCharacteristics[3], ruleSet1.ruleCharacteristics[3]);
		assertEquals(jointRuleSet.ruleCharacteristics[4], ruleSet1.ruleCharacteristics[4]);
		assertEquals(jointRuleSet.ruleCharacteristics[5], ruleSet2.ruleCharacteristics[0]);
		assertEquals(jointRuleSet.ruleCharacteristics[6], ruleSet2.ruleCharacteristics[1]);
		assertEquals(jointRuleSet.ruleCharacteristics[7], ruleSet2.ruleCharacteristics[2]);
		assertEquals(jointRuleSet.ruleCharacteristics[8], ruleSet2.ruleCharacteristics[3]);
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#filter(RuleSetWithCharacteristics, RuleFilter)}.
	 * Tests filtration using strict confidence filter.
	 */
	@Test
	void testFilter01() {
		int size = 5; //>=1
		Rule[] rules = new Rule[size];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCharacteristics[i] = Mockito.mock(RuleCharacteristics.class);
		}
		
		Mockito.when(ruleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(ruleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(ruleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(ruleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(ruleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithCharacteristics ruleSet = new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
		
		RuleSetWithCharacteristics filteredRuleSet = RuleSetWithCharacteristics.filter(ruleSet, new ConfidenceRuleFilter(0.5, true));
		
		assertEquals(filteredRuleSet.size(), 2);
		assertEquals(filteredRuleSet.getRule(0), ruleSet.getRule(2));
		assertEquals(filteredRuleSet.getRule(1), ruleSet.getRule(3));
		assertEquals(filteredRuleSet.ruleCharacteristics[0], ruleSet.ruleCharacteristics[2]);
		assertEquals(filteredRuleSet.ruleCharacteristics[1], ruleSet.ruleCharacteristics[3]);
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#filter(RuleSetWithCharacteristics, RuleFilter)}.
	 * Tests filtration using weak confidence filter.
	 */
	@Test
	void testFilter02() {
		int size = 5; //>=1
		Rule[] rules = new Rule[size];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCharacteristics[i] = Mockito.mock(RuleCharacteristics.class);
		}
		
		Mockito.when(ruleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(ruleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(ruleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(ruleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(ruleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithCharacteristics ruleSet = new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
		
		RuleSetWithCharacteristics filteredRuleSet = RuleSetWithCharacteristics.filter(ruleSet, new ConfidenceRuleFilter(0.5, false));
		
		assertEquals(filteredRuleSet.size(), 4);
		assertEquals(filteredRuleSet.getRule(0), ruleSet.getRule(1));
		assertEquals(filteredRuleSet.getRule(1), ruleSet.getRule(2));
		assertEquals(filteredRuleSet.getRule(2), ruleSet.getRule(3));
		assertEquals(filteredRuleSet.getRule(3), ruleSet.getRule(4));
		assertEquals(filteredRuleSet.ruleCharacteristics[0], ruleSet.ruleCharacteristics[1]);
		assertEquals(filteredRuleSet.ruleCharacteristics[1], ruleSet.ruleCharacteristics[2]);
		assertEquals(filteredRuleSet.ruleCharacteristics[2], ruleSet.ruleCharacteristics[3]);
		assertEquals(filteredRuleSet.ruleCharacteristics[3], ruleSet.ruleCharacteristics[4]);
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#filter(RuleFilter)}.
	 * Tests filtration using strict confidence filter that should filter all rules.
	 */
	@Test
	void testFilter03() {
		int size = 5; //>=1
		Rule[] rules = new Rule[size];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCharacteristics[i] = Mockito.mock(RuleCharacteristics.class);
		}
		
		Mockito.when(ruleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(ruleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(ruleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(ruleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(ruleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithCharacteristics ruleSet = new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
		
		RuleSetWithCharacteristics filteredRuleSet = ruleSet.filter(new ConfidenceRuleFilter(0.7, true));
		
		assertEquals(filteredRuleSet.size(), 0);
	}
	
	/**
	 * Test method for {@link RuleSetWithCharacteristics#filter(RuleFilter)}.
	 * Tests if {@link AcceptingRuleFilter} causes that the original rule set is returned.
	 */
	@Test
	void testFilter04() {
		int size = 5; //>=1
		Rule[] rules = new Rule[size];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[size];
		for (int i = 0; i < size; i++) {
			rules[i] = Mockito.mock(Rule.class);
			ruleCharacteristics[i] = Mockito.mock(RuleCharacteristics.class);
		}
		
		Mockito.when(ruleCharacteristics[0].getConfidence()).thenReturn(0.4);
		Mockito.when(ruleCharacteristics[1].getConfidence()).thenReturn(0.5);
		Mockito.when(ruleCharacteristics[2].getConfidence()).thenReturn(0.6);
		Mockito.when(ruleCharacteristics[3].getConfidence()).thenReturn(0.7);
		Mockito.when(ruleCharacteristics[4].getConfidence()).thenReturn(0.5);
		
		RuleSetWithCharacteristics ruleSet = new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
		RuleSetWithCharacteristics filteredRuleSet = ruleSet.filter(new AcceptingRuleFilter());
		
		assertSame(ruleSet, filteredRuleSet);
	}

}
