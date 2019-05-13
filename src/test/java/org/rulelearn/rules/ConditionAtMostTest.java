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
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;

/**
 * ConditionAtMostTest
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ConditionAtMostTest {

	/**
	 * Mock of {@link ConditionAtMost}, used to provide an ad hoc implementation of abstract methods of that class.
	 * Each invocation of an abstract method on {@link #condition} results in invocation of that method on {@link #conditionMock}.
	 * In other words, {@link #condition} implements each abstract method of {@link ConditionAtMost} by calling respective method on
	 * {@link #conditionMock}.
	 */
	private ConditionAtMost<? extends EvaluationField> conditionMock;
	
	private final int attributeIndex1 = 0;
	private final int attributeIndex2 = 1;
	
	private EvaluationAttributeWithContext attributeWithContextMock1;
	private EvaluationAttributeWithContext attributeWithContextMock2;
	
	private final IntegerField limitingEvaluation1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
	private final RealField limitingEvaluation2 = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.COST);
	
	@SuppressWarnings("unchecked")
	private static <T extends EvaluationField> boolean satisfiedByHelper(ConditionAtMost<T> conditionMock, EvaluationField evaluation) { //applies wildcard capture
		return conditionMock.satisfiedBy((T)evaluation); //TODO: test this cast
	}
	
	/**
	 * Initializes all mocks before each unit test.
	 */
	@BeforeEach
	@SuppressWarnings("unchecked")
	private void setup() {
		this.attributeWithContextMock1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(this.attributeWithContextMock1.getAttributeIndex()).thenReturn(this.attributeIndex1);
		Mockito.when(this.attributeWithContextMock1.getAttributeType()).thenReturn(AttributeType.DECISION);
		Mockito.when(this.attributeWithContextMock1.getAttributePreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		this.attributeWithContextMock2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(this.attributeWithContextMock2.getAttributeIndex()).thenReturn(this.attributeIndex2);
		Mockito.when(this.attributeWithContextMock2.getAttributeType()).thenReturn(AttributeType.DECISION);
		Mockito.when(this.attributeWithContextMock2.getAttributePreferenceType()).thenReturn(AttributePreferenceType.COST);
		
		this.conditionMock = Mockito.mock(ConditionAtMost.class);
	}
	
	private <T extends EvaluationField> ConditionAtMost<T> createCondition(EvaluationAttributeWithContext attributeWithContext, T limitingEvaluation) {
		return new ConditionAtMost<T>(attributeWithContext, limitingEvaluation) {
			@Override
			public boolean satisfiedBy(T evaluation) {
				return satisfiedByHelper(conditionMock, evaluation);
			}

			@Override
			public String toString() {
				return conditionMock.toString();
			}

			@Override
			@SuppressWarnings("unchecked")
			public ConditionAtMost<T> duplicate() {
				return (ConditionAtMost<T>)conditionMock.duplicate(); //TODO: test this cast
			}

			@Override
			public boolean equals(Object otherObject) {
				return conditionMock.equals(otherObject);
			}

			@Override
			public int hashCode() {
				return conditionMock.hashCode();
			}

			@Override
			public <S extends EvaluationField> TernaryLogicValue isAtMostAsGeneralAs(Condition<S> otherCondition) {
				return conditionMock.isAtMostAsGeneralAs(otherCondition);
			}
		};
	}
	
	/**
	 * Test method for {@link ConditionAtMost#ConditionAtMost(EvaluationAttributeWithContext, EvaluationField)}.
	 */
	@Test
	void testConditionAtMost() {
		try {
			createCondition(null, limitingEvaluation1);
			fail("Should not create condition at most with null evaluation attribute with context.");
		} catch (NullPointerException exception) {
			//do nothing => exception is correctly thrown
		}
		
		try {
			createCondition(attributeWithContextMock1, null);
			fail("Should not create condition at most with null limiting evaluation.");
		} catch (NullPointerException exception) {
			//do nothing => exception is correctly thrown
		}
	}	

	/**
	 * Test method for {@link ConditionAtMost#getRelationSymbol()}.
	 */
	@Test
	void testGetRelationSymbol01() {
		assertEquals(createCondition(attributeWithContextMock1, limitingEvaluation1).getRelationSymbol(), "<=");
	}
	
	/**
	 * Test method for {@link ConditionAtMost#getRelationSymbol()}.
	 */
	@Test
	void testGetRelationSymbol02() {
		assertEquals(createCondition(attributeWithContextMock2, limitingEvaluation2).getRelationSymbol(), "<=");
	}

	/**
	 * Test method for {@link ConditionAtMost#getRuleSemantics()}.
	 */
	@Test
	void testGetRuleSemantics01() {
		assertEquals(createCondition(attributeWithContextMock1, limitingEvaluation1).getRuleSemantics(), RuleSemantics.AT_MOST);
	}
	
	/**
	 * Test method for {@link ConditionAtMost#getRuleSemantics()}.
	 */
	@Test
	void testGetRuleSemantics02() {
		assertEquals(createCondition(attributeWithContextMock2, limitingEvaluation2).getRuleSemantics(), RuleSemantics.AT_LEAST);
	}

}
