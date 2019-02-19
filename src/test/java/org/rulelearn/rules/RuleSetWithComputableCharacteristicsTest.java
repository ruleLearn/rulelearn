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

import static org.junit.jupiter.api.Assertions.assertFalse;
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

}
