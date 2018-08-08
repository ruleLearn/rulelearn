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
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;

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
	private Condition<EvaluationField> conditionMock;
	
	private int attributeIndex = 3;
	
	private EvaluationAttributeWithContext attributeWithContextMock;
	private EvaluationField limitingEvaluation = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.NONE);
	
	/**
	 * Instance of tested class, newly initialized in each testing method.
	 */
	private Condition<EvaluationField> condition;
	
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
	
	private Condition<EvaluationField> createCondition(EvaluationAttributeWithContext attributeWithContext, EvaluationField limitingEvaluation) {
		return new Condition<EvaluationField>(attributeWithContext, limitingEvaluation) {
			@Override
			public boolean satisfiedBy(EvaluationField evaluation) {
				return conditionMock.satisfiedBy(evaluation);
			}

			@Override
			public String toString() {
				return conditionMock.toString();
			}

			@Override
			public Condition<EvaluationField> duplicate() {
				return conditionMock.duplicate();
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
		Mockito.when(conditionMock.satisfiedBy(evaluation)).thenReturn(result);
		
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

}
