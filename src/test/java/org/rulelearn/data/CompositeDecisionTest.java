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

import org.junit.jupiter.api.Test;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;

import it.unimi.dsi.fastutil.ints.IntSet;

import static org.mockito.Mockito.*;

/**
 * 
 * Tests for {@link CompositeDecision}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CompositeDecisionTest {
	
	/**
	 * Test for class constructor. Tests exception throwing in case of incorrect constructor parameters.
	 */
	@Test
	void testCompositeDecision_01() {
		EvaluationField[] evaluations = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices = {3, 6, 7};
		
		try {
			new CompositeDecision(evaluations, attributeIndices);
			fail("Composite decision should not be constructed for different number of evaluations and attribute indices.");
		} catch (InvalidValueException exception) {
			//exception is thrown as expected - do nothing
		}
	}
	
	/**
	 * Test for class constructor. Tests exception throwing in case of incorrect constructor parameters.
	 */
	@Test
	void testCompositeDecision_02() {
		EvaluationField[] evaluations = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN)
		};
		int[] attributeIndices = {3};
		
		try {
			new CompositeDecision(evaluations, attributeIndices);
			fail("Composite decision should not be constructed for just one evaluation.");
		} catch (InvalidValueException exception) {
			//exception is thrown as expected - do nothing
		}
	}
	
	/**
	 * Test for class constructor. Tests exception throwing in case of incorrect constructor parameters.
	 */
	@Test
	void testCompositeDecision_03() {
		EvaluationField[] evaluations = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				null
		};
		int[] attributeIndices = {3, 5};
		
		try {
			new CompositeDecision(evaluations, attributeIndices);
			fail("Composite decision should not be constructed if an evaluation contributing to that decision is null.");
		} catch (NullPointerException exception) {
			//exception is thrown as expected - do nothing
		}
	}

	/**
	 * Test for {@link CompositeDecision#hashCode()} method.
	 */
	@Test
	void testHashCode() {
		EvaluationField[] evaluations = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices = {3, 6};
		
		CompositeDecision compositeDecision = new CompositeDecision(evaluations, attributeIndices);
		CompositeDecision otherCompositeDecision = new CompositeDecision(evaluations, attributeIndices);
		
		assertEquals(compositeDecision.hashCode(), compositeDecision.hashCode());
		assertEquals(otherCompositeDecision.hashCode(), compositeDecision.hashCode());
	}

	/**
	 * Test for {@link CompositeDecision#isEqualTo(Decision)} method.
	 */
	@Test
	void testIsEqualTo() {
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices1 = {3, 6};
		
		EvaluationField[] evaluations2 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices2 = {3, 6};
		
		EvaluationField[] evaluations3 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices3 = {3, 5}; //diff
		
		EvaluationField[] evaluations4 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.COST)}; //diff
		int[] attributeIndices4 = {3, 6};
		
		EvaluationField[] evaluations5 = {
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), //diff
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices5 = {3, 6}; //diff
		
		CompositeDecision compositeDecision1 = new CompositeDecision(evaluations1, attributeIndices1);
		CompositeDecision compositeDecision2 = new CompositeDecision(evaluations2, attributeIndices2);
		CompositeDecision compositeDecision3 = new CompositeDecision(evaluations3, attributeIndices3);
		CompositeDecision compositeDecision4 = new CompositeDecision(evaluations4, attributeIndices4);
		CompositeDecision compositeDecision5 = new CompositeDecision(evaluations5, attributeIndices5);
		
		Decision decision6 = getIncompatibleDecision();
		
		assertTrue(compositeDecision1.isEqualTo(compositeDecision2) == TernaryLogicValue.TRUE);
		assertTrue(compositeDecision1.isEqualTo(compositeDecision3) == TernaryLogicValue.UNCOMPARABLE);
		assertTrue(compositeDecision1.isEqualTo(compositeDecision4) == TernaryLogicValue.FALSE); //
		assertTrue(compositeDecision1.isEqualTo(compositeDecision5) == TernaryLogicValue.FALSE);
		assertTrue(compositeDecision1.isEqualTo(decision6) == TernaryLogicValue.UNCOMPARABLE);
	}
	
	/**
	 * Test for {@link CompositeDecision#isAtLeastAsGoodAs(Decision)} method.
	 */
	@Test
	void testIsAtLeastAsGoodAs() {
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices1 = {3, 6};
		
		EvaluationField[] evaluations2 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), //equal evaluation
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)}; //equal evaluation
		int[] attributeIndices2 = {3, 6};
		
		EvaluationField[] evaluations3 = {
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), //worse evaluation
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices3 = {3, 6};
		
		EvaluationField[] evaluations4 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST)}; //better evaluation
		int[] attributeIndices4 = {3, 6};
		
		EvaluationField[] evaluations5 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices5 = {3, 5}; //diff
		
		
		CompositeDecision compositeDecision1 = new CompositeDecision(evaluations1, attributeIndices1);
		CompositeDecision compositeDecision2 = new CompositeDecision(evaluations2, attributeIndices2);
		CompositeDecision compositeDecision3 = new CompositeDecision(evaluations3, attributeIndices3);
		CompositeDecision compositeDecision4 = new CompositeDecision(evaluations4, attributeIndices4);
		CompositeDecision compositeDecision5 = new CompositeDecision(evaluations5, attributeIndices5);
		Decision decision6 = getIncompatibleDecision();
		
		assertTrue(compositeDecision1.isAtLeastAsGoodAs(compositeDecision2) == TernaryLogicValue.TRUE);
		assertTrue(compositeDecision1.isAtLeastAsGoodAs(compositeDecision3) == TernaryLogicValue.TRUE);
		assertTrue(compositeDecision1.isAtLeastAsGoodAs(compositeDecision4) == TernaryLogicValue.FALSE);
		assertTrue(compositeDecision1.isAtLeastAsGoodAs(compositeDecision5) == TernaryLogicValue.UNCOMPARABLE);
		assertTrue(compositeDecision1.isAtLeastAsGoodAs(decision6) == TernaryLogicValue.UNCOMPARABLE);
	}
	
	/**
	 * Test for {@link CompositeDecision#isAtMostAsGoodAs(Decision)} method.
	 */
	@Test
	void testIsAtMostAsGoodAs() {
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices1 = {3, 6};
		
		EvaluationField[] evaluations2 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), //equal evaluation
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)}; //equal evaluation
		int[] attributeIndices2 = {3, 6};
		
		EvaluationField[] evaluations3 = {
				IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), //better evaluation
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices3 = {3, 6};
		
		EvaluationField[] evaluations4 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.COST)}; //worse evaluation
		int[] attributeIndices4 = {3, 6};
		
		EvaluationField[] evaluations5 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST)};
		int[] attributeIndices5 = {3, 5}; //diff		
		
		CompositeDecision compositeDecision1 = new CompositeDecision(evaluations1, attributeIndices1);
		CompositeDecision compositeDecision2 = new CompositeDecision(evaluations2, attributeIndices2);
		CompositeDecision compositeDecision3 = new CompositeDecision(evaluations3, attributeIndices3);
		CompositeDecision compositeDecision4 = new CompositeDecision(evaluations4, attributeIndices4);
		CompositeDecision compositeDecision5 = new CompositeDecision(evaluations5, attributeIndices5);
		Decision decision6 = getIncompatibleDecision();
		
		assertTrue(compositeDecision1.isAtMostAsGoodAs(compositeDecision2) == TernaryLogicValue.TRUE);
		assertTrue(compositeDecision1.isAtMostAsGoodAs(compositeDecision3) == TernaryLogicValue.TRUE);
		assertTrue(compositeDecision1.isAtMostAsGoodAs(compositeDecision4) == TernaryLogicValue.FALSE);
		assertTrue(compositeDecision1.isAtMostAsGoodAs(compositeDecision5) == TernaryLogicValue.UNCOMPARABLE);
		assertTrue(compositeDecision1.isAtMostAsGoodAs(decision6) == TernaryLogicValue.UNCOMPARABLE);
	}

	/**
	 * Test for {@link CompositeDecision#getEvaluation(int)} method.
	 */
	@Test
	void testGetEvaluation() {
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		
		EvaluationField[] evaluations = {
				evaluation1,
				evaluation2};
		
		int attributeIndex1 = 3;
		int attributeIndex2 = 6;
		int attributeIndex0 = 0;
		
		int[] attributeIndices = {attributeIndex1, attributeIndex2};
		
		CompositeDecision compositeDecision = new CompositeDecision(evaluations, attributeIndices);
		
		assertEquals(compositeDecision.getEvaluation(attributeIndex1), evaluation1);
		assertEquals(compositeDecision.getEvaluation(attributeIndex2), evaluation2);
		
		assertNull(compositeDecision.getEvaluation(attributeIndex0));
	}

	/**
	 * Test for {@link CompositeDecision#getNumberOfEvaluations()} method.
	 */
	@Test
	void testGetNumberOfEvaluations() {
		EvaluationField[] evaluations = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST),
				RealFieldFactory.getInstance().create(3.5, AttributePreferenceType.GAIN)};
		int[] attributeIndices = {3, 6, 7};
		
		CompositeDecision compositeDecision = new CompositeDecision(evaluations, attributeIndices);
		
		assertEquals(compositeDecision.getNumberOfEvaluations(), 3);
	}
	
	/**
	 * Test for {@link CompositeDecision#getAttributeIndices()} method.
	 */
	@Test
	void testGetAttributeIndices() {
		EvaluationField[] evaluations = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST),
				RealFieldFactory.getInstance().create(3.5, AttributePreferenceType.GAIN)};
		int attrIndex1 = 3;
		int attrIndex2 = 6;
		int attrIndex3 = 7;
		int[] attributeIndices = {attrIndex1, attrIndex2, attrIndex3};
		
		CompositeDecision compositeDecision = new CompositeDecision(evaluations, attributeIndices);
		
		IntSet attrIndices = compositeDecision.getAttributeIndices();
		
		assertEquals(attrIndices.size(), attributeIndices.length);
		assertTrue(attrIndices.contains(attrIndex1));
		assertTrue(attrIndices.contains(attrIndex2));
		assertTrue(attrIndices.contains(attrIndex3));
	}

	/**
	 * Test for {@link CompositeDecision#equals(Object)} method.
	 */
	@Test
	void testEqualsObject() {
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST),
				RealFieldFactory.getInstance().create(3.5, AttributePreferenceType.GAIN)};
		int[] attributeIndices1 = {3, 6, 7};
		
		EvaluationField[] evaluations2 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST),
				RealFieldFactory.getInstance().create(3.5, AttributePreferenceType.GAIN)};
		int[] attributeIndices2 = {3, 6, 7};
		
		EvaluationField[] evaluations3 = evaluations2.clone();
		evaluations3[2] = RealFieldFactory.getInstance().create(3.51, AttributePreferenceType.GAIN); //different evaluation
		int[] attributeIndices3 = attributeIndices2.clone();
		
		CompositeDecision compositeDecision1 = new CompositeDecision(evaluations1, attributeIndices1);
		CompositeDecision compositeDecision2 = new CompositeDecision(evaluations2, attributeIndices2);
		CompositeDecision compositeDecision3 = new CompositeDecision(evaluations3, attributeIndices3);
		
		assertTrue(compositeDecision1.equals(compositeDecision2));
		assertFalse(compositeDecision1.equals(compositeDecision3));
	}
	
	private Decision getIncompatibleDecision() {
		return mock(Decision.class);
	}
	
	/**
	 * Test for toString() method.
	 */
	@Test
	void testToString() {
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST),
				RealFieldFactory.getInstance().create(3.5, AttributePreferenceType.GAIN)};
		int[] attributeIndices1 = {3, 6, 7};
		
		CompositeDecision decision = new CompositeDecision(evaluations1, attributeIndices1);
		
		String decisionText = decision.toString();
		System.out.println(decisionText);
		assertTrue(decisionText.indexOf("7=>3.5") > -1);
		assertTrue(decisionText.indexOf("6=>4") > -1);
		assertTrue(decisionText.indexOf("3=>2") > -1);
	}
	
	/**
	 * Test for {@link CompositeDecision#hasNoMissingEvaluation()} method.
	 */
	@Test
	void testHasNoMissingEvaluation01() {
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST),
				RealFieldFactory.getInstance().create(3.5, AttributePreferenceType.GAIN)};
		int[] attributeIndices1 = {3, 6, 7};
		
		Decision decision = new CompositeDecision(evaluations1, attributeIndices1);
		assertTrue(decision.hasNoMissingEvaluation());
	}
	
	/**
	 * Test for {@link CompositeDecision#hasNoMissingEvaluation()} method.
	 */
	@Test
	void testHasNoMissingEvaluation02() {
		EvaluationField[] evaluations1 = {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST),
				new UnknownSimpleFieldMV15()};
		int[] attributeIndices1 = {3, 6, 7};
		
		Decision decision = new CompositeDecision(evaluations1, attributeIndices1);
		assertFalse(decision.hasNoMissingEvaluation());
	}

}
