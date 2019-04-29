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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link Rule}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleTest {
	
	private Condition<? extends EvaluationField> getCondition1() {
		AttributePreferenceType preferenceType1 = AttributePreferenceType.GAIN;
		return new SimpleConditionAtLeast(
				new EvaluationAttributeWithContext(
						new EvaluationAttribute(
								"attr1",
								true,
								AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType1),
								new UnknownSimpleFieldMV2(),
								preferenceType1),
						1),
				IntegerFieldFactory.getInstance().create(3, preferenceType1));
	}
	
	private Condition<? extends EvaluationField> getCondition2() {
		AttributePreferenceType preferenceType2 = AttributePreferenceType.COST;
		return new SimpleConditionAtMost(
				new EvaluationAttributeWithContext(
						new EvaluationAttribute(
								"attr3",
								true,
								AttributeType.CONDITION,
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType2),
								new UnknownSimpleFieldMV15(),
								preferenceType2),
						3),
				RealFieldFactory.getInstance().create(-2.1, preferenceType2));
	}
	
	private Condition<? extends EvaluationField> getDecision() {
		AttributePreferenceType preferenceTypeDec = AttributePreferenceType.GAIN;
		return new SimpleConditionAtLeast(
				new EvaluationAttributeWithContext(
						new EvaluationAttribute(
								"dec",
								true,
								AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceTypeDec),
								new UnknownSimpleFieldMV2(),
								preferenceTypeDec),
						7),
				IntegerFieldFactory.getInstance().create(5, preferenceTypeDec));
	}
	
	private Condition<? extends EvaluationField> getDecisionInv() {
		AttributePreferenceType preferenceTypeDec = AttributePreferenceType.GAIN;
		return new SimpleConditionAtMost(
				new EvaluationAttributeWithContext(
						new EvaluationAttribute(
								"dec",
								true,
								AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceTypeDec),
								new UnknownSimpleFieldMV2(),
								preferenceTypeDec),
						7),
				IntegerFieldFactory.getInstance().create(3, preferenceTypeDec));
	}
	
	private Condition<? extends EvaluationField> getDecision2() {
		AttributePreferenceType preferenceTypeDec = AttributePreferenceType.COST;
		return new SimpleConditionAtMost(
				new EvaluationAttributeWithContext(
						new EvaluationAttribute(
								"dec2",
								true,
								AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceTypeDec),
								new UnknownSimpleFieldMV15(),
								preferenceTypeDec),
						9),
				IntegerFieldFactory.getInstance().create(3, preferenceTypeDec));
	}
	
	private Condition<? extends EvaluationField> getDecision2Inv() {
		AttributePreferenceType preferenceTypeDec = AttributePreferenceType.COST;
		return new SimpleConditionAtLeast(
				new EvaluationAttributeWithContext(
						new EvaluationAttribute(
								"dec2",
								true,
								AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceTypeDec),
								new UnknownSimpleFieldMV15(),
								preferenceTypeDec),
						9),
				IntegerFieldFactory.getInstance().create(5, preferenceTypeDec));
	}
	
	//constructs rule using longer constructor
	private Rule getTestRule1() {
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();
		List<Condition<? extends EvaluationField>> andDecisions = new ObjectArrayList<Condition<?>>();
		List<List<Condition<? extends EvaluationField>>> decisions = new ObjectArrayList<>();
		
		//create conditions
		conditions.add(getCondition1());
		conditions.add(getCondition2());
		
		//create decisions
		andDecisions.add(getDecision());
		decisions.add(andDecisions);
		
		//create and return rule
		Rule rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_LEAST, conditions, decisions);
		return rule;
	}
	
	//constructs rule using shorter constructor
	private Rule getTestRule2() {
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();
		
		//create conditions
		conditions.add(getCondition1());
		conditions.add(getCondition2());
		
		//create and return rule
		Rule rule = new Rule(RuleType.CERTAIN, conditions, (SimpleCondition)getDecision());
		return rule;
	}
	
	//constructs 2-decision rule using longer constructor
	private Rule getTestRule3() {
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();
		List<Condition<? extends EvaluationField>> andDecisions = new ObjectArrayList<Condition<?>>();
		List<List<Condition<? extends EvaluationField>>> decisions = new ObjectArrayList<>();

		//create conditions
		conditions.add(getCondition1());
		conditions.add(getCondition2());

		//create decisions
		andDecisions.add(getDecision());
		andDecisions.add(getDecision2());
		decisions.add(andDecisions);

		//create and return rule
		Rule rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_MOST, conditions, decisions);
		return rule;
	}
	
	//constructs 2 x 2-decision rule using longer constructor
	private Rule getTestRule4() {
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();

		//create conditions
		conditions.add(getCondition1());
		conditions.add(getCondition2());

		//create decisions
		List<List<Condition<? extends EvaluationField>>> decisions = new ObjectArrayList<>();
		decisions.add(new ObjectArrayList<>());
		decisions.add(new ObjectArrayList<>());
		decisions.get(0).add(getDecision());
		decisions.get(0).add(getDecision2());
		decisions.get(1).add(getDecisionInv());
		decisions.get(1).add(getDecision2Inv());
		
		//create and return rule
		Rule rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_LEAST, conditions, decisions);
		return rule;
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#getType()}.
	 */
	@Test
	void testGetType() {
		SimpleConditionAtLeast condition = null;
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();
		conditions.add(condition);
		
		SimpleConditionAtLeast decision = Mockito.mock(SimpleConditionAtLeast.class);;
		
		List<List<Condition<? extends EvaluationField>>> decisions = new ObjectArrayList<>();
		decisions.add(new ObjectArrayList<>());
		decisions.get(0).add(decision);
		
		Rule rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_LEAST, conditions, decisions);
		assertEquals(rule.getType(), RuleType.CERTAIN);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#getSemantics()}.
	 */
	@Test
	void testGetSemantics() {
		SimpleConditionAtLeast condition = null;
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<>();
		conditions.add(condition);
		
		SimpleConditionAtLeast decision = Mockito.mock(SimpleConditionAtLeast.class);;
		
		List<List<Condition<? extends EvaluationField>>> decisions = new ObjectArrayList<>();
		decisions.add(new ObjectArrayList<>());
		decisions.get(0).add(decision);
		
		Rule rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_MOST, conditions, decisions);
		assertEquals(rule.getSemantics(), RuleSemantics.AT_MOST);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#getConditions()}.
	 */
	@Test
	void testGetConditions() {
		Rule rule = getTestRule1();
		Condition<? extends EvaluationField>[] conditions = rule.getConditions();
		assertEquals(conditions.length, 2);
		assertEquals(conditions[0], getCondition1());
		assertEquals(conditions[1], getCondition2());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#getConditions(boolean)}.
	 */
	@Test
	void testGetConditionsBoolean() {
		Rule rule = getTestRule1();
		Condition<EvaluationField>[] conditions = rule.getConditions(true);
		assertEquals(conditions.length, 2);
		assertEquals(conditions[0], getCondition1());
		assertEquals(conditions[1], getCondition2());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#getDecisions()}.
	 */
	@Test
	void testGetDecisions() {
		Rule rule = getTestRule1();
		Condition<? extends EvaluationField>[][] decisions = rule.getDecisions();
		assertEquals(decisions.length, 1);
		assertEquals(decisions[0][0], getDecision());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#getDecisions(boolean)}.
	 */
	@Test
	void testGetDecisionsBoolean() {
		Rule rule = getTestRule1();
		Condition<? extends EvaluationField>[][] decisions = rule.getDecisions(true);
		assertEquals(decisions.length, 1);
		assertEquals(decisions.length, 1);
		assertEquals(decisions[0][0], getDecision());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#getDecision()}.
	 */
	@Test
	void testGetDecision() {
		Rule rule = getTestRule1();
		Condition<? extends EvaluationField> decision = rule.getDecision();
		assertEquals(decision, getDecision());
	}
	
	/**
	 * Test method for shorter constructor {@link org.rulelearn.rules.Rule#Rule(RuleType, List, SimpleCondition)}.
	 */
	@Test
	void testRule() {
		Rule rule = getTestRule2();
		assertEquals(rule.getSemantics(), RuleSemantics.AT_LEAST);
		assertEquals(rule.getDecision(), getDecision());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#toString()}.
	 */
	@Test
	void testToString01() {
		Rule rule = getTestRule1();
		System.out.println(rule);
		assertEquals(rule.toString(), "(attr1 >= 3) & (attr3 <= -2.1) => (dec >= 5)");
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#toString()}. Tests rule with 2 AND-connected decisions.
	 */
	@Test
	void testToString02() {
		Rule rule = getTestRule3();
		System.out.println(rule);
		assertEquals(rule.toString(), "(attr1 >= 3) & (attr3 <= -2.1) => (dec >= 5) & (dec2 <= 3)");
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#toString()}. Tests rule with 2 x 2 AND-connected decisions.
	 */
	@Test
	void testToString03() {
		Rule rule = getTestRule4();
		System.out.println(rule);
		assertEquals(rule.toString(), "(attr1 >= 3) & (attr3 <= -2.1) => ((dec >= 5) & (dec2 <= 3)) OR ((dec <= 3) & (dec2 >= 5))");
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#covers(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testCovers_01() {
		Rule rule = getTestRule1();
		int objectIndex = 0;
		InformationTable informationTableMock = mock(InformationTable.class);
		when(informationTableMock.getField(objectIndex, 1)).thenReturn(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		when(informationTableMock.getField(objectIndex, 3)).thenReturn(RealFieldFactory.getInstance().create(-2.1, AttributePreferenceType.COST));
		assertTrue(rule.covers(objectIndex, informationTableMock));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#covers(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testCovers_02() {
		Rule rule = getTestRule1();
		int objectIndex = 1;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 1)).thenReturn(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN));
		when(informationTable.getField(objectIndex, 3)).thenReturn(RealFieldFactory.getInstance().create(-3, AttributePreferenceType.COST));
		assertTrue(rule.covers(objectIndex, informationTable));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#covers(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testCovers_03() {
		Rule rule = getTestRule1();
		int objectIndex = 2;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 1)).thenReturn(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		when(informationTable.getField(objectIndex, 3)).thenReturn(RealFieldFactory.getInstance().create(-2.0, AttributePreferenceType.COST));
		assertFalse(rule.covers(objectIndex, informationTable));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#covers(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testCovers_04() {
		Rule rule = getTestRule1();
		int objectIndex = 3;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 1)).thenReturn(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		when(informationTable.getField(objectIndex, 3)).thenReturn(RealFieldFactory.getInstance().create(-2.1, AttributePreferenceType.COST));
		assertFalse(rule.covers(objectIndex, informationTable));
	}


	/**
	 * Test method for {@link org.rulelearn.rules.Rule#decisionsMatchedBy(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testDecisionsMatchedBy_01() {
		Rule rule = getTestRule1();
		int objectIndex = 0;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 7)).thenReturn(IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN));
		assertTrue(rule.decisionsMatchedBy(objectIndex, informationTable));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#decisionsMatchedBy(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testDecisionsMatchedBy_02() {
		Rule rule = getTestRule1();
		int objectIndex = 0;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 7)).thenReturn(IntegerFieldFactory.getInstance().create(6, AttributePreferenceType.GAIN));
		assertTrue(rule.decisionsMatchedBy(objectIndex, informationTable));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#decisionsMatchedBy(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testDecisionsMatchedBy_03() {
		Rule rule = getTestRule1();
		int objectIndex = 0;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 7)).thenReturn(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN));
		assertFalse(rule.decisionsMatchedBy(objectIndex, informationTable));
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Rule#supportedBy(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testSupportedBy_01() {
		Rule rule = getTestRule1();
		int objectIndex = 0;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 1)).thenReturn(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		when(informationTable.getField(objectIndex, 3)).thenReturn(RealFieldFactory.getInstance().create(-2.1, AttributePreferenceType.COST));
		when(informationTable.getField(objectIndex, 7)).thenReturn(IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN));
		assertTrue(rule.supportedBy(objectIndex, informationTable));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#supportedBy(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testSupportedBy_02() {
		Rule rule = getTestRule1();
		int objectIndex = 0;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 1)).thenReturn(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		when(informationTable.getField(objectIndex, 3)).thenReturn(RealFieldFactory.getInstance().create(-2.1, AttributePreferenceType.COST));
		when(informationTable.getField(objectIndex, 7)).thenReturn(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN));
		assertFalse(rule.supportedBy(objectIndex, informationTable));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Rule#supportedBy(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testSupportedBy_03() {
		Rule rule = getTestRule1();
		int objectIndex = 0;
		InformationTable informationTable = mock(InformationTable.class);
		when(informationTable.getField(objectIndex, 1)).thenReturn(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		when(informationTable.getField(objectIndex, 3)).thenReturn(RealFieldFactory.getInstance().create(-2.0, AttributePreferenceType.COST));
		when(informationTable.getField(objectIndex, 7)).thenReturn(IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN));
		assertFalse(rule.supportedBy(objectIndex, informationTable));
	}

}
