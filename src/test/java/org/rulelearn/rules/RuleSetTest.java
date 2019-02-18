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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
		SimpleConditionAtLeast condition = null;
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();
		conditions.add(condition);
		
		SimpleConditionAtLeast decision = Mockito.mock(SimpleConditionAtLeast.class);;
		
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

}
