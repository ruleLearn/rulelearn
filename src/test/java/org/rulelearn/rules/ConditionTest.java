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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.PairField;
import org.rulelearn.types.SimpleField;

/**
 * Tests for {@link Condition}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ConditionTest {
	
	/**
	 * Mock of {@link Condition}, used to provide an ad hoc implementation of abstract methods of that class.
	 * Each invocation of an abstract method on {@link #condition} results in invocation of that method on {@link #conditionMock}.
	 * In other words, {@link #condition} implements each abstract method of {@link Condition} by calling respective method on
	 * {@link #conditionMock}.
	 */
	private Condition<? extends EvaluationField> conditionMock;
	
	private int attributeIndex = 3;
	
	private EvaluationAttributeWithContext attributeWithContextMock;
	private EvaluationField limitingEvaluation = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.NONE);
	
	/**
	 * Instance of tested class, newly initialized in each testing method.
	 */
	private Condition<? extends EvaluationField> condition;
	
	@SuppressWarnings("unchecked")
	private static <T extends EvaluationField> boolean satisfiedByHelper(Condition<T> conditionMock, EvaluationField evaluation) { //applies wildcard capture
		return conditionMock.satisfiedBy((T)evaluation); //TODO: test this cast
	}
	
	/**
	 * Initializes all mocks before each unit test.
	 */
	@BeforeEach
	@SuppressWarnings("unchecked")
	private void setup() {
		this.attributeWithContextMock = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(this.attributeWithContextMock.getAttributeIndex()).thenReturn(this.attributeIndex);
		this.conditionMock = Mockito.mock(Condition.class);
	}
	
	private <T extends EvaluationField> Condition<T> createCondition(EvaluationAttributeWithContext attributeWithContext, T limitingEvaluation) {
		return new Condition<T>(attributeWithContext, limitingEvaluation) {
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
			public Condition<T> duplicate() {
				return (Condition<T>)conditionMock.duplicate(); //TODO: test this cast
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
			public RuleSemantics getRuleSemantics() {
				return conditionMock.getRuleSemantics();
			}

			@Override
			public String getRelationSymbol() {
				return conditionMock.getRelationSymbol();
			}

			@Override
			public <S extends EvaluationField> TernaryLogicValue isAtMostAsGeneralAs(Condition<S> otherCondition) {
				return conditionMock.isAtMostAsGeneralAs(otherCondition);
			}
		};
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Condition#Condition(org.rulelearn.data.EvaluationAttributeWithContext, org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testCondition() {
		try {
			createCondition(null, limitingEvaluation);
			fail("Should not create condition with null evaluation attribute with context.");
		} catch (NullPointerException exception) {
			//do nothing => exception is correctly thrown
		}
		
		try {
			createCondition(attributeWithContextMock, null);
			fail("Should not create condition with null limiting evaluation.");
		} catch (NullPointerException exception) {
			//do nothing => exception is correctly thrown
		}
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Condition#satisfiedBy(int, org.rulelearn.data.InformationTable)}.
	 */
	@Test
	void testSatisfiedByIntInformationTable() {
		condition = createCondition(attributeWithContextMock, limitingEvaluation);
		
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		EvaluationField evaluation = IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.NONE);
		int objectIndex = 2;
		boolean result = true;
		
		Mockito.when(informationTableMock.getField(objectIndex, attributeWithContextMock.getAttributeIndex())).thenReturn(evaluation);
		//Mockito.when(conditionMock.satisfiedBy(evaluation)).thenReturn(result);
		Mockito.when(satisfiedByHelper(conditionMock, evaluation)).thenReturn(result);
		
		assertEquals(condition.satisfiedBy(objectIndex, informationTableMock), result);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.Condition#getAttributeWithContext()}.
	 */
	@Test
	void testGetAttributeWithContext() {
		condition = createCondition(attributeWithContextMock, limitingEvaluation);
		
		assertEquals(condition.getAttributeWithContext(), attributeWithContextMock);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Condition#getLimitingEvaluation()}.
	 */
	@Test
	void testGetLimitingEvaluation() {
		condition = createCondition(attributeWithContextMock, limitingEvaluation);
		
		assertEquals(condition.getLimitingEvaluation(), limitingEvaluation);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Condition#isDecomposable()}.
	 */
	@Test
	void testIsDecomposable01() {
		condition = createCondition(attributeWithContextMock, limitingEvaluation);
		
		assertFalse(condition.isDecomposable());
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Condition#isDecomposable()}.
	 */
	@Test
	void testIsDecomposable02() {
		condition = createCondition(attributeWithContextMock, new PairField<SimpleField>(
				IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN)));
		
		assertTrue(condition.isDecomposable());
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.Condition#hashCode()}.
	 */
	@Test
	void testHashCode() {
		condition = createCondition(attributeWithContextMock, limitingEvaluation);
		Condition<EvaluationField> condition2 = createCondition(attributeWithContextMock, limitingEvaluation);
		
		assertEquals(condition.hashCode(), condition2.hashCode());
	}

}
