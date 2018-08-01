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

import static org.junit.jupiter.api.Assertions.*;

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
	void testRuleSetWithCharacteristics3() {
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
	void testRuleSetWithCharacteristics4() {
		Rule[] rules = new Rule[2];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[2];
		try {
			new RuleSetWithCharacteristics(rules, ruleCharacteristics);
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
		Rule[] rules = new Rule[2];
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[2];
		try {
			new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
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
		Rule[] rules = new Rule[2];
		rules[0] = Mockito.mock(Rule.class);
		rules[1] = Mockito.mock(Rule.class);
		
		RuleCharacteristics[] ruleCharacteristics = new RuleCharacteristics[2];
		ruleCharacteristics[0] = Mockito.mock(RuleCharacteristics.class);
		RuleCharacteristics ruleCharacteristics1 = Mockito.mock(RuleCharacteristics.class);
		ruleCharacteristics[1] = ruleCharacteristics1;
		
		RuleSetWithCharacteristics ruleSetWithCharacteristics = new RuleSetWithCharacteristics(rules, ruleCharacteristics, true);
		
		assertTrue(ruleSetWithCharacteristics.getRuleCharacteristics(1) == ruleCharacteristics1);
	}

}
