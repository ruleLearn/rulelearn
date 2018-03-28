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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.SimpleField;

/**
 * Tests for {@link SimpleConditionAtMost}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleConditionAtMostTest {
	
	//wrapper for IntegerField limiting evaluations
	private SimpleConditionAtMost getCondition(AttributeType attributeType, AttributePreferenceType attributePreferenceType, int attributeIndex, int limitingEvaluation) {
		return this.getCondition(attributeType, attributePreferenceType, attributeIndex,
				IntegerFieldFactory.getInstance().create(limitingEvaluation, attributePreferenceType));
	}

	// wrapper for RealField limiting evaluations
	private SimpleConditionAtMost getCondition(AttributeType attributeType, AttributePreferenceType attributePreferenceType, int attributeIndex, double limitingEvaluation) {
		return this.getCondition(attributeType, attributePreferenceType, attributeIndex,
				RealFieldFactory.getInstance().create(limitingEvaluation, attributePreferenceType));
	}

	private SimpleConditionAtMost getCondition(AttributeType attributeType, AttributePreferenceType attributePreferenceType, int attributeIndex, SimpleField limitingEvaluation) {
		EvaluationAttributeWithContext evalAttrWithContextMock = mock(EvaluationAttributeWithContext.class);
		when(evalAttrWithContextMock.getAttributeType()).thenReturn(attributeType);
		when(evalAttrWithContextMock.getAttributePreferenceType()).thenReturn(attributePreferenceType);
		when(evalAttrWithContextMock.getAttributeIndex()).thenReturn(attributeIndex);

		return new SimpleConditionAtMost(evalAttrWithContextMock, limitingEvaluation);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject_01() {
		assertEquals(
				getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 3),
				getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 3));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject_02() {
		assertNotEquals(
				getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 3),
				getCondition(AttributeType.CONDITION, AttributePreferenceType.COST, 3, -2.1));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject_03() {
		assertNotEquals(
				getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 3),
				getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 4));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject_04() {
		assertNotEquals(
				getCondition(AttributeType.CONDITION, AttributePreferenceType.COST, 1, 4),
				getCondition(AttributeType.CONDITION, AttributePreferenceType.COST, 2, 4));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#getRuleSemantics()}.
	 */
	@Test
	public void testGetRuleSemantics_01() {
		try {
			this.getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 3).getRuleSemantics(); //not a decision attribute
			fail("Should not determine semantics of a non-decision attribute.");
		} catch (InvalidValueException exception) {
			//OK
		}
		
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#getRuleSemantics()}.
	 */
	@Test
	public void testGetRuleSemantics_02() {
		try {
			assertEquals(this.getCondition(AttributeType.DECISION, AttributePreferenceType.GAIN, 5, 5).getRuleSemantics(), RuleSemantics.AT_MOST);
		} catch (InvalidValueException exception) {
			fail("Should determine semantics of a decision attribute.");
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#getRuleSemantics()}.
	 */
	@Test
	public void testGetRuleSemantics_03() {
		try {
			assertEquals(this.getCondition(AttributeType.DECISION, AttributePreferenceType.COST, 6, 3).getRuleSemantics(), RuleSemantics.AT_LEAST);
		} catch (InvalidValueException exception) {
			fail("Should determine semantics of a decision attribute.");
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#getRelationSymbol()}.
	 */
	@Test
	public void testGetRelationSymbol() {
		assertEquals(this.getCondition(AttributeType.CONDITION, AttributePreferenceType.COST, 1, -12.0).getRelationSymbol(), "<=");
	}

	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#satisfiedBy(org.rulelearn.types.SimpleField)}.
	 */
	@Test
	public void testSatisfiedBySimpleField_01() {
		assertTrue(this.getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 5).satisfiedBy(IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN)));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#satisfiedBy(org.rulelearn.types.SimpleField)}.
	 */
	@Test
	public void testSatisfiedBySimpleField_02() {
		assertFalse(this.getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 5).satisfiedBy(IntegerFieldFactory.getInstance().create(6, AttributePreferenceType.GAIN)));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#satisfiedBy(org.rulelearn.types.SimpleField)}.
	 */
	@Test
	public void testSatisfiedBySimpleField_03() {
		assertTrue(this.getCondition(AttributeType.CONDITION, AttributePreferenceType.COST, 1, 5).satisfiedBy(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#satisfiedBy(org.rulelearn.types.SimpleField)}.
	 */
	@Test
	public void testSatisfiedBySimpleField_04() {
		assertFalse(this.getCondition(AttributeType.CONDITION, AttributePreferenceType.COST, 1, 5).satisfiedBy(IntegerFieldFactory.getInstance().create(6, AttributePreferenceType.COST)));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#satisfiedBy(int, org.rulelearn.data.InformationTable))}.
	 */
	@Test
	public void testSatisfiedBy_01() { //5 <= 5
		int objectIndex = 0;
		int attributeIndex = 1;
		AttributePreferenceType attributePreferenceType = AttributePreferenceType.GAIN;
		InformationTable informationTableMock = mock(InformationTable.class);
		when(informationTableMock.getField(objectIndex, attributeIndex)).thenReturn(IntegerFieldFactory.getInstance().create(5, attributePreferenceType));
		assertTrue(this.getCondition(AttributeType.CONDITION, attributePreferenceType, attributeIndex, IntegerFieldFactory.getInstance().create(5, attributePreferenceType)).satisfiedBy(objectIndex, informationTableMock));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#satisfiedBy(int, org.rulelearn.data.InformationTable))}.
	 */
	@Test
	public void testSatisfiedBy_02() { //not 6 <= 5
		int objectIndex = 0;
		int attributeIndex = 1;
		AttributePreferenceType attributePreferenceType = AttributePreferenceType.GAIN;
		InformationTable informationTableMock = mock(InformationTable.class);
		when(informationTableMock.getField(objectIndex, attributeIndex)).thenReturn(IntegerFieldFactory.getInstance().create(6, attributePreferenceType));
		assertFalse(this.getCondition(AttributeType.CONDITION, attributePreferenceType, attributeIndex, IntegerFieldFactory.getInstance().create(5, attributePreferenceType)).satisfiedBy(objectIndex, informationTableMock));
	}


	/**
	 * Test method for {@link org.rulelearn.rules.SimpleConditionAtMost#duplicate()}.
	 */
	@Test
	public void testDuplicate() {
		SimpleConditionAtMost condition = this.getCondition(AttributeType.CONDITION, AttributePreferenceType.GAIN, 1, 5);
		SimpleConditionAtMost duplicatedCondition = condition.duplicate();
		assertNotSame(condition, duplicatedCondition);
		assertEquals(condition, duplicatedCondition);
	}

}
