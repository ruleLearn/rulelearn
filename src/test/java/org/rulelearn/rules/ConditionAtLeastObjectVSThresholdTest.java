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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link ConditionAtLeastObjectVSThreshold}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ConditionAtLeastObjectVSThresholdTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}
	
	private EvaluationField iField(int value, AttributePreferenceType attributePreferenceType) {
		return IntegerFieldFactory.getInstance().create(value, attributePreferenceType);
	}
	
	private EvaluationField rField(double value, AttributePreferenceType attributePreferenceType) {
		return RealFieldFactory.getInstance().create(value, attributePreferenceType);
	}
	
	private ConditionAtLeastObjectVSThreshold<EvaluationField> getCondition(AttributeType attributeType, AttributePreferenceType attributePreferenceType, int attributeIndex, EvaluationField limitingEvaluation) {
		EvaluationAttributeWithContext evalAttrWithContextMock = mock(EvaluationAttributeWithContext.class);
		when(evalAttrWithContextMock.getAttributeType()).thenReturn(attributeType);
		when(evalAttrWithContextMock.getAttributePreferenceType()).thenReturn(attributePreferenceType);
		when(evalAttrWithContextMock.getAttributeIndex()).thenReturn(attributeIndex);
		when(evalAttrWithContextMock.getAttributeName()).thenReturn("attr");
		
		return new ConditionAtLeastObjectVSThreshold<EvaluationField>(evalAttrWithContextMock, limitingEvaluation);
	}

	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#satisfiedBy(EvaluationField)}.
	 * Tests satisfied condition for gain-type attribute.
	 */
	@Test
	void testSatisfiedBy01() {
		AttributePreferenceType prefType = AttributePreferenceType.GAIN;
		assertTrue(this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType))
				.satisfiedBy(iField(5, prefType)));
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#satisfiedBy(EvaluationField)}.
	 * Tests not satisfied condition for gain-type attribute.
	 */
	@Test
	void testSatisfiedBy02() {
		AttributePreferenceType prefType = AttributePreferenceType.GAIN;
		assertFalse(this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType))
				.satisfiedBy(iField(4, prefType)));
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#satisfiedBy(EvaluationField)}.
	 * Tests satisfied condition for cost-type attribute.
	 */
	@Test
	void testSatisfiedBy03() {
		AttributePreferenceType prefType = AttributePreferenceType.COST;
		assertTrue(this.getCondition(AttributeType.CONDITION, prefType, 1, rField(5, prefType))
				.satisfiedBy(rField(6, prefType)));
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#satisfiedBy(EvaluationField)}.
	 * Tests not satisfied condition for cost-type attribute.
	 */
	@Test
	void testSatisfiedBy04() {
		AttributePreferenceType prefType = AttributePreferenceType.COST;
		assertFalse(this.getCondition(AttributeType.CONDITION, prefType, 1, rField(5, prefType))
				.satisfiedBy(rField(4, prefType)));
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#satisfiedBy(EvaluationField)}.
	 * Tests satisfied condition for gain-type attribute, with object's evaluation being a missing value of type mv_2.
	 */
	@Test
	void testSatisfiedBy05() {
		AttributePreferenceType prefType = AttributePreferenceType.GAIN;
		assertTrue(this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType))
				.satisfiedBy(new UnknownSimpleFieldMV2()));
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#satisfiedBy(EvaluationField)}.
	 * Tests satisfied condition for cost-type attribute, with object's evaluation being a missing value of type mv_1.5.
	 */
	@Test
	void testSatisfiedBy06() {
		AttributePreferenceType prefType = AttributePreferenceType.COST;
		assertTrue(this.getCondition(AttributeType.CONDITION, prefType, 1, rField(5, prefType))
				.satisfiedBy(new UnknownSimpleFieldMV15()));
	}

	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#toString()}.
	 */
	@Test
	void testToString() {
		AttributePreferenceType prefType = AttributePreferenceType.COST;
		Condition<EvaluationField> condition = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		assertEquals(condition.toString(), "attr >= 5");
	}

	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#equals(Object)}.
	 */
	@Test
	void testEquals01() {
		AttributePreferenceType prefType = AttributePreferenceType.COST;
		Condition<EvaluationField> condition1 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		Condition<EvaluationField> condition2 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		assertEquals(condition1, condition2);
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#equals(Object)}.
	 */
	@Test
	void testEquals02() {
		AttributePreferenceType prefType = AttributePreferenceType.GAIN;
		Condition<EvaluationField> condition1 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		Condition<EvaluationField> condition2 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(4, prefType));
		assertNotEquals(condition1, condition2);
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#equals(Object)}.
	 */
	@Test
	void testEquals03() {
		AttributePreferenceType prefType = AttributePreferenceType.COST;
		Condition<EvaluationField> condition1 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		Condition<EvaluationField> condition2 = this.getCondition(AttributeType.CONDITION, prefType, 2, iField(5, prefType));
		assertNotEquals(condition1, condition2);
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#equals(Object)}.
	 */
	@Test
	void testEquals04() {
		AttributePreferenceType prefType1 = AttributePreferenceType.GAIN;
		AttributePreferenceType prefType2 = AttributePreferenceType.COST;
		Condition<EvaluationField> condition1 = this.getCondition(AttributeType.CONDITION, prefType1, 1, iField(5, prefType1));
		Condition<EvaluationField> condition2 = this.getCondition(AttributeType.CONDITION, prefType2, 1, iField(5, prefType2));
		assertNotEquals(condition1, condition2);
	}

	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#isAtMostAsGeneralAs(org.rulelearn.rules.Condition)}.
	 * Tests conditions for a gain-type attribute.
	 */
	@Test
	void testIsAtMostAsGeneralAs01() {
		AttributePreferenceType prefType = AttributePreferenceType.GAIN;
		Condition<EvaluationField> condition = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		
		Condition<EvaluationField> condition1 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		assertEquals(condition1.isAtMostAsGeneralAs(condition), TernaryLogicValue.TRUE);
		
		Condition<EvaluationField> condition2 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(10, prefType));
		assertEquals(condition2.isAtMostAsGeneralAs(condition), TernaryLogicValue.TRUE);
		
		Condition<EvaluationField> condition3 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(4, prefType));
		assertEquals(condition3.isAtMostAsGeneralAs(condition), TernaryLogicValue.FALSE);
		
		AttributePreferenceType prefType2 = AttributePreferenceType.COST;
		Condition<EvaluationField> condition4 = this.getCondition(AttributeType.CONDITION, prefType2, 1, iField(10, prefType));
		assertEquals(condition4.isAtMostAsGeneralAs(condition), TernaryLogicValue.TRUE); //different preference type does not matter
	}
	
	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#isAtMostAsGeneralAs(org.rulelearn.rules.Condition)}.
	 * Tests conditions for a cost-type attribute.
	 */
	@Test
	void testIsAtMostAsGeneralAs02() {
		AttributePreferenceType prefType = AttributePreferenceType.COST;
		Condition<EvaluationField> condition = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		
		Condition<EvaluationField> condition1 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(5, prefType));
		assertEquals(condition1.isAtMostAsGeneralAs(condition), TernaryLogicValue.TRUE);
		
		Condition<EvaluationField> condition2 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(10, prefType));
		assertEquals(condition2.isAtMostAsGeneralAs(condition), TernaryLogicValue.TRUE);
		
		Condition<EvaluationField> condition3 = this.getCondition(AttributeType.CONDITION, prefType, 1, iField(4, prefType));
		assertEquals(condition3.isAtMostAsGeneralAs(condition), TernaryLogicValue.FALSE);
	}

	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#ConditionAtLeastObjectVSThreshold(EvaluationAttributeWithContext, EvaluationField)}.
	 */
	@Test
	void testConditionAtLeastObjectVSThreshold() {
		try {
			new ConditionAtLeastObjectVSThreshold<IntegerField>(null, IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
			fail("Should not create condition at least with null evaluation attribute with context.");
		} catch (NullPointerException exception) {
			//do nothing => exception is correctly thrown
		}
		
		try {
			new ConditionAtLeastObjectVSThreshold<IntegerField>(Mockito.mock(EvaluationAttributeWithContext.class), null);
			fail("Should not create condition at least with null limiting evaluation.");
		} catch (NullPointerException exception) {
			//do nothing => exception is correctly thrown
		}
	}

	/**
	 * Test method for {@link ConditionAtLeastObjectVSThreshold#duplicate()}.
	 */
	@Test
	void testDuplicate() {
		Condition<IntegerField> condition = new ConditionAtLeastObjectVSThreshold<IntegerField>(
				Mockito.mock(EvaluationAttributeWithContext.class),
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		Condition<IntegerField> duplicate = condition.duplicate();
		
		assertEquals(condition.getClass(), duplicate.getClass());
		assertEquals(condition.getAttributeWithContext(), duplicate.getAttributeWithContext());
		assertEquals(condition.getLimitingEvaluation(), duplicate.getLimitingEvaluation());
		
		assertEquals(condition, duplicate);
		assertNotSame(condition, duplicate);
	}

}
