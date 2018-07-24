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
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.SimpleField;

/**
 * Tests for {@link SimpleCondition}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleConditionTest {
	
	private SimpleCondition simpleCondition;
	
	private String attributeName = "testAttribute";
	private String relationSymbol = "R";
	private int value = 5;
	
	private SimpleCondition constructSimpleCondition() {
		EvaluationAttributeWithContext attributeWithContext = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext.getAttributeName()).thenReturn(attributeName);
		IntegerField limitingEvaluation = IntegerFieldFactory.getInstance().create(value, AttributePreferenceType.COST);
		
		return new SimpleCondition(attributeWithContext, limitingEvaluation) {
			
			@Override
			public boolean satisfiedBy(SimpleField evaluation) { //not used in tests
				return false;
			}
			
			@Override
			public RuleSemantics getRuleSemantics() { //not used in tests
				return null;
			}
			
			@Override
			public boolean equals(Object otherObject) { //not used in tests
				return false;
			}
			
			@Override
			public Condition<SimpleField> duplicate() { //not used in tests
				return null;
			}
			
			@Override
			public String getRelationSymbol() {
				return relationSymbol;
			}
		};
	}
	
	/**
	 * Sole constructor initializing tested simple condition.
	 */
	public SimpleConditionTest() {
		this.simpleCondition = this.constructSimpleCondition();
	}

	/**
	 * Test method for {@link org.rulelearn.rules.SimpleCondition#hashCode()}.
	 */
	@Test
	void testHashCode() {
		assertNotNull(simpleCondition.hashCode());
		assertEquals(simpleCondition.hashCode(), simpleCondition.hashCode());
		assertEquals(simpleCondition.hashCode(), constructSimpleCondition().hashCode());
	}

	/**
	 * Test method for {@link org.rulelearn.rules.SimpleCondition#toString()}.
	 */
	@Test
	void testToString() {
		String separator = " ";
		assertEquals(simpleCondition.toString(), attributeName + separator + this.relationSymbol + separator + value);
	}

}
