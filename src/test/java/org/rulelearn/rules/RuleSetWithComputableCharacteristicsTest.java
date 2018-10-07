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

/**
 * Tests for {@link RuleSetWithComputableCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleSetWithComputableCharacteristicsTest {

	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInfo[])}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristics01() {
		try {
			new RuleSetWithComputableCharacteristics(null, new RuleCoverageInfo[] {Mockito.mock(RuleCoverageInfo.class)});
			fail("Should not create rule set with computable characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInfo[])}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristics02() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, null);
			fail("Should not create rule set with computable characteristics for null rule coverage infos.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInfo[])}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristics03() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, new RuleCoverageInfo[] {Mockito.mock(RuleCoverageInfo.class)});
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and rule coverage infos.");
		}
	}	
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInfo[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean01() {
		try {
			new RuleSetWithComputableCharacteristics(null, new RuleCoverageInfo[] {Mockito.mock(RuleCoverageInfo.class)}, true);
			fail("Should not create rule set with computable characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInfo[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean02() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, null, true);
			fail("Should not create rule set with computable characteristics for null rule coverage infos.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInfo[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean03() {
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics;
		RuleCoverageInfo[] ruleCoverageInfos = new RuleCoverageInfo[] {Mockito.mock(RuleCoverageInfo.class)};
		try {
			ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)},
					ruleCoverageInfos, true); //allow cloning
			assertTrue(ruleSetWithComputableCharacteristics.ruleCoverageInfos == ruleCoverageInfos);
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and rule coverage infos.");
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], RuleCoverageInfo[], boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsBoolean04() {
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics;
		RuleCoverageInfo[] ruleCoverageInfos = new RuleCoverageInfo[] {Mockito.mock(RuleCoverageInfo.class)};
		try {
			ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)},
					ruleCoverageInfos, false); //avoid cloning
			assertFalse(ruleSetWithComputableCharacteristics.ruleCoverageInfos == ruleCoverageInfos);
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and rule coverage infos.");
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithComputableCharacteristics#getRuleCharacteristics(int)}.
	 */
	@Test
	void testGetRuleCharacteristicsInt() {
		Rule rule = Mockito.mock(Rule.class);
		RuleCoverageInfo ruleCoverageInfo = Mockito.mock(RuleCoverageInfo.class);
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {rule},
				new RuleCoverageInfo[] {ruleCoverageInfo});
		
		ComputableRuleCharacteristics computableRuleCharacteristics = ruleSetWithComputableCharacteristics.getRuleCharacteristics(0);
		
		assertTrue(computableRuleCharacteristics instanceof ComputableRuleCharacteristics);
		assertTrue(computableRuleCharacteristics.getRuleCoverageInfo() == ruleCoverageInfo);
		
		assertTrue(computableRuleCharacteristics == ruleSetWithComputableCharacteristics.getRuleCharacteristics(0)); //test if existing object is returned
		
	}

}
