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

package org.rulelearn.data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * Tests for {@link SimpleDecision}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleDecisionTest {

	/**
	 * Test method for {@link org.rulelearn.data.SimpleDecision#hashCode()}.
	 */
	@Test
	void testHashCode() {
		EvaluationField evaluation = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		EvaluationField otherEvaluation = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN); //the same eval
		int attributeIndex = 3;
		
		SimpleDecision decision = new SimpleDecision(evaluation, attributeIndex);
		SimpleDecision otherDecision = new SimpleDecision(otherEvaluation, attributeIndex);
		
		assertNotNull(decision.hashCode());
		assertEquals(decision.hashCode(), decision.hashCode());
		assertEquals(decision.hashCode(), otherDecision.hashCode());
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.SimpleDecision#isAtLeastAsGoodAs(org.rulelearn.data.Decision)}.
	 */
	@Test
	void testIsAtLeastAsGoodAs() {
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex1 = 3;
		
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex2 = 3;
		
		EvaluationField evaluation3 = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN); //diff
		int attributeIndex3 = 3;
		
		EvaluationField evaluation4 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN); //diff
		int attributeIndex4 = 3;
		
		EvaluationField evaluation5 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex5 = 2; //diff
		
		SimpleDecision simpleDecision1 = new SimpleDecision(evaluation1, attributeIndex1);
		SimpleDecision simpleDecision2 = new SimpleDecision(evaluation2, attributeIndex2);
		SimpleDecision simpleDecision3 = new SimpleDecision(evaluation3, attributeIndex3);
		SimpleDecision simpleDecision4 = new SimpleDecision(evaluation4, attributeIndex4);
		SimpleDecision simpleDecision5 = new SimpleDecision(evaluation5, attributeIndex5);
		
		Decision decision6 = getIncompatibleDecision();
		
		try {
			assertTrue(simpleDecision1.isAtLeastAsGoodAs(simpleDecision2) == TernaryLogicValue.TRUE); //equal
			assertTrue(simpleDecision1.isAtLeastAsGoodAs(simpleDecision3) == TernaryLogicValue.FALSE); //worse
			assertTrue(simpleDecision1.isAtLeastAsGoodAs(simpleDecision4) == TernaryLogicValue.TRUE); //better
			assertTrue(simpleDecision1.isAtLeastAsGoodAs(simpleDecision5) == TernaryLogicValue.UNCOMPARABLE); //different attribute
			assertTrue(simpleDecision1.isAtLeastAsGoodAs(decision6) == TernaryLogicValue.UNCOMPARABLE); //different attribute
		} catch (UnsupportedOperationException exception) {
			//TODO: remove this try-catch when implementation of tested method is finished
		}
	}

	/**
	 * Test method for {@link org.rulelearn.data.SimpleDecision#isAtMostAsGoodAs(org.rulelearn.data.Decision)}.
	 */
	@Test
	void testIsAtMostAsGoodAs() {
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex1 = 3;
		
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex2 = 3;
		
		EvaluationField evaluation3 = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN); //diff
		int attributeIndex3 = 3;
		
		EvaluationField evaluation4 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN); //diff
		int attributeIndex4 = 3;
		
		EvaluationField evaluation5 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex5 = 2; //diff
		
		SimpleDecision simpleDecision1 = new SimpleDecision(evaluation1, attributeIndex1);
		SimpleDecision simpleDecision2 = new SimpleDecision(evaluation2, attributeIndex2);
		SimpleDecision simpleDecision3 = new SimpleDecision(evaluation3, attributeIndex3);
		SimpleDecision simpleDecision4 = new SimpleDecision(evaluation4, attributeIndex4);
		SimpleDecision simpleDecision5 = new SimpleDecision(evaluation5, attributeIndex5);
		
		Decision decision6 = getIncompatibleDecision();
		
		try {
			assertTrue(simpleDecision1.isAtMostAsGoodAs(simpleDecision2) == TernaryLogicValue.TRUE); //equal
			assertTrue(simpleDecision1.isAtMostAsGoodAs(simpleDecision3) == TernaryLogicValue.TRUE); //worse
			assertTrue(simpleDecision1.isAtMostAsGoodAs(simpleDecision4) == TernaryLogicValue.FALSE); //better
			assertTrue(simpleDecision1.isAtMostAsGoodAs(simpleDecision5) == TernaryLogicValue.UNCOMPARABLE); //different attribute
			assertTrue(simpleDecision1.isAtMostAsGoodAs(decision6) == TernaryLogicValue.UNCOMPARABLE); //different attribute
		} catch (UnsupportedOperationException exception) {
			//TODO: remove this try-catch when implementation of tested method is finished
		}
	}

	/**
	 * Test method for {@link org.rulelearn.data.SimpleDecision#isEqualTo(org.rulelearn.data.Decision)}.
	 */
	@Test
	void testIsEqualTo() {
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex1 = 3;
		
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex2 = 3;
		
		EvaluationField evaluation3 = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN); //diff
		int attributeIndex3 = 3;
		
		EvaluationField evaluation4 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex4 = 2; //diff
		
		SimpleDecision simpleDecision1 = new SimpleDecision(evaluation1, attributeIndex1);
		SimpleDecision simpleDecision2 = new SimpleDecision(evaluation2, attributeIndex2);
		SimpleDecision simpleDecision3 = new SimpleDecision(evaluation3, attributeIndex3);
		SimpleDecision simpleDecision4 = new SimpleDecision(evaluation4, attributeIndex4);
		
		Decision decision5 = getIncompatibleDecision();
		
		try {
			assertTrue(simpleDecision1.isEqualTo(simpleDecision2) == TernaryLogicValue.TRUE);
			assertTrue(simpleDecision1.isEqualTo(simpleDecision3) == TernaryLogicValue.FALSE);
			assertTrue(simpleDecision1.isEqualTo(simpleDecision4) == TernaryLogicValue.FALSE);
			assertTrue(simpleDecision1.isEqualTo(decision5) == TernaryLogicValue.UNCOMPARABLE);
		} catch (UnsupportedOperationException exception) {
			//TODO: remove this try-catch when implementation of tested method is finished
		}
	}

	/**
	 * Test method for {@link org.rulelearn.data.SimpleDecision#getEvaluation(int)}.
	 */
	@Test
	void testGetEvaluation() {
		EvaluationField evaluation = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		EvaluationField expectedEvaluation = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		int attributeIndex = 0;
		
		SimpleDecision decision = new SimpleDecision(evaluation, attributeIndex);
		
		assertEquals(decision.getEvaluation(attributeIndex), expectedEvaluation);
	}

	/**
	 * Test method for {@link org.rulelearn.data.SimpleDecision#getNumberOfEvaluations()}.
	 */
	@Test
	void testGetNumberOfEvaluations() {
		EvaluationField evaluation = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		int attributeIndex = 2;
		
		SimpleDecision decision = new SimpleDecision(evaluation, attributeIndex);
		
		assertEquals(decision.getNumberOfEvaluations(), 1);
	}

	/**
	 * Test method for {@link org.rulelearn.data.SimpleDecision#equals(java.lang.Object)}.
	 */
	@Test
	void testEqualsObject() {
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		EvaluationField evaluation3 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		
		int attributeIndex = 0;
		
		SimpleDecision decision1 = new SimpleDecision(evaluation1, attributeIndex);
		SimpleDecision decision2 = new SimpleDecision(evaluation2, attributeIndex);
		SimpleDecision decision3 = new SimpleDecision(evaluation3, attributeIndex);
		
		assertTrue(decision1.equals(decision2));
		assertFalse(decision1.equals(decision3));
	}

	/**
	 * Test for class constructor. Tests exception throwing in case of incorrect constructor parameters.
	 */
	@Test
	void testSimpleDecision01() {
		//EvaluationField evaluation = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex = 3;
		
		try {
			new SimpleDecision(null, attributeIndex);
			fail("Simple decision should not be constructed for null evaluation.");
		} catch (NullPointerException exception) {
			//exception is thrown as expected - do nothing
		}
	}
	
	/**
	 * Test for class constructor. Tests exception throwing in case of incorrect constructor parameters.
	 */
	@Test
	void testSimpleDecision02() {
		EvaluationField evaluation = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		int attributeIndex = -1;
		
		try {
			new SimpleDecision(evaluation, attributeIndex);
			fail("Simple decision should not be constructed for negative attribute index.");
		} catch (InvalidValueException exception) {
			//exception is thrown as expected - do nothing
		}
	}
	
	private Decision getIncompatibleDecision() {
		return mock(Decision.class);
	}
	
}
