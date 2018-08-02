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
import org.rulelearn.data.InformationTable;

/**
 * Tests for {@link RuleSetWithComputableCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleSetWithComputableCharacteristicsTest {

	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], InformationTable)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsRuleArrayInformationTable01() {
		try {
			new RuleSetWithComputableCharacteristics(null, Mockito.mock(InformationTable.class));
			fail("Should not create rule set with computable characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], InformationTable)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsRuleArrayInformationTable02() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, null);
			fail("Should not create rule set with computable characteristics for null information table.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], InformationTable)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsRuleArrayInformationTable03() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, Mockito.mock(InformationTable.class));
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and information table.");
		}
	}	
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], InformationTable, boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsRuleArrayInformationTableBoolean01() {
		try {
			new RuleSetWithComputableCharacteristics(null, Mockito.mock(InformationTable.class), true);
			fail("Should not create rule set with computable characteristics for null rules.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], InformationTable, boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsRuleArrayInformationTableBoolean02() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, null, true);
			fail("Should not create rule set with computable characteristics for null information table.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link RuleSetWithComputableCharacteristics#RuleSetWithComputableCharacteristics(Rule[], InformationTable, boolean)}.
	 */
	@Test
	void testRuleSetWithComputableCharacteristicsRuleArrayInformationTableBoolean03() {
		try {
			new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, Mockito.mock(InformationTable.class), true);
		} catch (NullPointerException exception) {
			fail("Should create rule set with computable characteristics for given rules and information table.");
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithComputableCharacteristics#getRuleCharacteristics(int)}.
	 */
	@Test
	void testGetRuleCharacteristicsInt() {
		Rule rule = Mockito.mock(Rule.class);
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {rule}, informationTable);
		
		ComputableRuleCharacteristics computableRuleCharacteristics = ruleSetWithComputableCharacteristics.getRuleCharacteristics(0);
		
		assertTrue(computableRuleCharacteristics instanceof ComputableRuleCharacteristics);
		assertTrue(computableRuleCharacteristics.getRule() == rule);
		assertTrue(computableRuleCharacteristics.getInformationTable() == informationTable);
		
		assertTrue(computableRuleCharacteristics == ruleSetWithComputableCharacteristics.getRuleCharacteristics(0)); //test if existing object is returned
		
	}

	/**
	 * Test method for {@link org.rulelearn.rules.RuleSetWithComputableCharacteristics#getInformationTable()}.
	 */
	@Test
	void testGetInformationTable() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		RuleSetWithComputableCharacteristics ruleSetWithComputableCharacteristics = new RuleSetWithComputableCharacteristics(new Rule[] {Mockito.mock(Rule.class)}, informationTable, true);
		
		assertTrue(ruleSetWithComputableCharacteristics.getInformationTable() == informationTable);
	}

}
