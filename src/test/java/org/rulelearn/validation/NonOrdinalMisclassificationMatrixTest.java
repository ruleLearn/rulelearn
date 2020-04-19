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

package org.rulelearn.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link NonOrdinalMisclassificationMatrix}. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class NonOrdinalMisclassificationMatrixTest {
	
	private SimpleDecision decision1, decision2, decision3, decision4, decision5;
	
	private SimpleDecision[] originalDecisions;
	private SimpleDecision[] assignedDecisions; 
	
	private final double delta = 0.00000000001;
	
	void setUp0() {
		// evaluation1 = 1, evaluation2 = 2, evaluation3 = ? (mv1.5), evaluation4 = 2, evaluation5 = ? (mv2) 
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		EvaluationField evaluation3 = new UnknownSimpleFieldMV15();
		EvaluationField evaluation4 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		EvaluationField evaluation5 = new UnknownSimpleFieldMV2();
		
		int attributeIndex1 = 1, attributeIndex2 = 2;
		
		decision1 = new SimpleDecision(evaluation1, attributeIndex1);
		decision2 = new SimpleDecision(evaluation2, attributeIndex1);
		decision3 = new SimpleDecision(evaluation3, attributeIndex1);
		decision4 = new SimpleDecision(evaluation4, attributeIndex2);
		decision5 = new SimpleDecision(evaluation5, attributeIndex2);
	}
	
	void setUp1() {
		// evaluation1 = 1, evaluation2 = 2, evaluation3 = 3, evaluation4 = 4, evaluation5 = 5
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		EvaluationField evaluation3 = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		EvaluationField evaluation4 = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		EvaluationField evaluation5 = IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN);
				
		decision1 = new SimpleDecision(evaluation1, 1);
		decision2 = new SimpleDecision(evaluation2, 1);
		decision3 = new SimpleDecision(evaluation3, 1);
		decision4 = new SimpleDecision(evaluation4, 1);
		decision5 = new SimpleDecision(evaluation5, 1);
	}
	
	void setUp2() {
		// evaluation1 = 1, evaluation2 = 2, evaluation3 = 3
		EvaluationField evaluation1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		EvaluationField evaluation2 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		EvaluationField evaluation3 = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		
		decision1 = new SimpleDecision(evaluation1, 1);
		decision2 = new SimpleDecision(evaluation2, 1);
		decision3 = new SimpleDecision(evaluation3, 1);
	}
	
	/**
	 * Tests for pair: original decisions = {} and assigned decisions = {}.
	 */
	@Test
	void testAll00() {
		setUp0();
		originalDecisions = new SimpleDecision[] {};
		assignedDecisions = new SimpleDecision[] {};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		// check values / averages
		assertEquals(0.0, misclassificationMatrix.getNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix.getGmean(), delta);
		
		// check deviations
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfAccuracy(), delta);	
	}
	
	/**
	 * Tests for pair: original decisions == assigned decisions.
	 */
	@Test
	void testAll01() {
		setUp0();
		originalDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		assignedDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		// check values / averages
		assertEquals(3.0, misclassificationMatrix.getNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments(), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignments(), delta);
		assertEquals(3.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision4, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5), delta);
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision1), delta);
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3), delta);
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5), delta);
		assertEquals(1.0, misclassificationMatrix.getAccuracy(), delta);
		assertEquals(1.0, misclassificationMatrix.getGmean(), delta);
		
		// check deviations
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision1, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision2, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision3, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision4, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfValue(decision5, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfTruePositiveRate(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getDeviationOfAccuracy(), delta);
	}
	
	/**
	 * Tests for pair: original decisions != assigned decisions.
	 */
	@Test
	void testAll02() {
		setUp0();
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1, decision1, decision1};
		assignedDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(1.0, misclassificationMatrix.getNumberOfCorrectAssignments(), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfIncorrectAssignments(), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignments(), delta);
		assertEquals(3.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision1), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5), delta);
		assertEquals(((double)1)/3, misclassificationMatrix.getTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5), delta);
		assertEquals(((double)1)/3, misclassificationMatrix.getAccuracy(), delta);
		assertEquals(Math.pow(((double)1)/3, 1.0), misclassificationMatrix.getGmean(), delta);
	}
	
	/**
	 * Tests for pair: original decisions != assigned decisions.
	 */
	@Test
	void testAll03() {
		setUp0();
		originalDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		assignedDecisions = new SimpleDecision[] {decision1, decision1, decision1, decision1, decision1};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(1.0, misclassificationMatrix.getNumberOfCorrectAssignments(), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignments(), delta);
		assertEquals(3.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5), delta);
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision4, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5), delta);
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5), delta);
		assertEquals(((double)1)/3, misclassificationMatrix.getAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix.getGmean(), delta);
	}
	
	/**
	 * Tests for pair: original decisions != assigned decisions.
	 */
	@Test
	void testAll04() {
		setUp1();
		originalDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		assignedDecisions = new SimpleDecision[] {decision5, decision4, decision3, decision2, decision1};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(1.0, misclassificationMatrix.getNumberOfCorrectAssignments(), delta);
		assertEquals(4.0, misclassificationMatrix.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignments(), delta);
		assertEquals(5.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision2, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision4, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5), delta);
		assertEquals(1.0, misclassificationMatrix.getValue(decision5, decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2), delta);
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4), delta);
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5), delta);
		assertEquals(((double)1)/5, misclassificationMatrix.getAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix.getGmean(), delta);
	}
	
	/**
	 * Tests of averaging multiple misclassification matrices.
	 */
	@Test
	void testAll05() {
		setUp2();
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		assignedDecisions = new SimpleDecision[] {decision1, decision2, decision3,
				decision1, decision2, decision3,
				decision1, decision2, decision3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix1 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		assertEquals(3.0, misclassificationMatrix1.getNumberOfCorrectAssignments(), delta);
		assertEquals(6.0, misclassificationMatrix1.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignments(), delta);
		assertEquals(9.0, misclassificationMatrix1.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision1, decision1), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision1, decision2), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision1, decision3), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision2, decision1), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision2, decision2), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision2, decision3), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision3, decision1), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision3, decision2), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision3, decision3), delta);
		assertEquals(1.0/3, misclassificationMatrix1.getTruePositiveRate(decision1), delta);
		assertEquals(1.0/3, misclassificationMatrix1.getTruePositiveRate(decision2), delta);
		assertEquals(1.0/3, misclassificationMatrix1.getTruePositiveRate(decision3), delta);
		assertEquals((3.0)/9, misclassificationMatrix1.getAccuracy(), delta);
		assertEquals(Math.pow((1.0/3)*(1.0/3)*(1.0/3), 1.0/3), misclassificationMatrix1.getGmean(), delta);
		// check deviations
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision3, decision3), delta);
		
		NonOrdinalMisclassificationMatrix misclassificationMatrix2 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		NonOrdinalMisclassificationMatrix misclassificationMatrix3 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		NonOrdinalMisclassificationMatrix misclassificationMatrixA = new NonOrdinalMisclassificationMatrix(misclassificationMatrix1, misclassificationMatrix2, misclassificationMatrix3);
		// check averages
		assertEquals(3.0, misclassificationMatrixA.getNumberOfCorrectAssignments(), delta);
		assertEquals(6.0, misclassificationMatrixA.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignments(), delta);
		assertEquals(9.0, misclassificationMatrixA.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision1, decision1), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision1, decision2), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision1, decision3), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision2, decision1), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision2, decision2), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision2, decision3), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision3, decision1), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision3, decision2), delta);
		assertEquals(1.0, misclassificationMatrixA.getValue(decision3, decision3), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getTruePositiveRate(decision1), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getTruePositiveRate(decision2), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getTruePositiveRate(decision3), delta);
		assertEquals((3.0)/9, misclassificationMatrixA.getAccuracy(), delta);
		assertEquals(Math.pow((1.0/3)*(1.0/3)*(1.0/3), 1.0/3), misclassificationMatrixA.getGmean(), delta);
		// check deviations
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision3, decision3), delta);
	}
	
	/**
	 * Tests of adding multiple misclassification matrices.
	 */
	@Test
	void testAll05s() {
		setUp2();
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		assignedDecisions = new SimpleDecision[] {decision1, decision2, decision3,
				decision1, decision2, decision3,
				decision1, decision2, decision3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix1 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		assertEquals(3.0, misclassificationMatrix1.getNumberOfCorrectAssignments(), delta);
		assertEquals(6.0, misclassificationMatrix1.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignments(), delta);
		assertEquals(9.0, misclassificationMatrix1.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix1.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision1, decision1), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision1, decision2), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision1, decision3), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision2, decision1), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision2, decision2), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision2, decision3), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision3, decision1), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision3, decision2), delta);
		assertEquals(1.0, misclassificationMatrix1.getValue(decision3, decision3), delta);
		assertEquals(1.0/3, misclassificationMatrix1.getTruePositiveRate(decision1), delta);
		assertEquals(1.0/3, misclassificationMatrix1.getTruePositiveRate(decision2), delta);
		assertEquals(1.0/3, misclassificationMatrix1.getTruePositiveRate(decision3), delta);
		assertEquals((3.0)/9, misclassificationMatrix1.getAccuracy(), delta);
		assertEquals(Math.pow((1.0/3)*(1.0/3)*(1.0/3), 1.0/3), misclassificationMatrix1.getGmean(), delta);
		// check deviations
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix1.getDeviationOfValue(decision3, decision3), delta);
		
		NonOrdinalMisclassificationMatrix misclassificationMatrix2 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		NonOrdinalMisclassificationMatrix misclassificationMatrix3 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		NonOrdinalMisclassificationMatrix misclassificationMatrixA = new NonOrdinalMisclassificationMatrix(true, misclassificationMatrix1, misclassificationMatrix2, misclassificationMatrix3);
		// check averages
		assertEquals(9.0, misclassificationMatrixA.getNumberOfCorrectAssignments(), delta);
		assertEquals(18.0, misclassificationMatrixA.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignments(), delta);
		assertEquals(27.0, misclassificationMatrixA.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision1, decision1), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision1, decision2), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision1, decision3), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision2, decision1), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision2, decision2), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision2, decision3), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision3, decision1), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision3, decision2), delta);
		assertEquals(3.0, misclassificationMatrixA.getValue(decision3, decision3), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getTruePositiveRate(decision1), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getTruePositiveRate(decision2), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getTruePositiveRate(decision3), delta);
		assertEquals((3.0)/9, misclassificationMatrixA.getAccuracy(), delta);
		assertEquals(Math.pow((1.0/3)*(1.0/3)*(1.0/3), 1.0/3), misclassificationMatrixA.getGmean(), delta);
		// check deviations
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfValue(decision3, decision3), delta);
	}
	
	/**
	 * Tests of averaging multiple misclassification matrices.
	 */
	@Test
	void testAll06() {
		setUp2();
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		assignedDecisions = new SimpleDecision[] {decision1, decision2, decision3,
				decision1, decision2, decision3,
				decision1, decision2, decision3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix1 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		assignedDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix2 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		assertEquals(9.0, misclassificationMatrix2.getNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownAssignments(), delta);
		assertEquals(9.0, misclassificationMatrix2.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix2.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(3.0, misclassificationMatrix2.getValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getValue(decision2, decision1), delta);
		assertEquals(3.0, misclassificationMatrix2.getValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getValue(decision3, decision2), delta);
		assertEquals(3.0, misclassificationMatrix2.getValue(decision3, decision3), delta);
		assertEquals(1.0, misclassificationMatrix2.getTruePositiveRate(decision1), delta);
		assertEquals(1.0, misclassificationMatrix2.getTruePositiveRate(decision2), delta);
		assertEquals(1.0, misclassificationMatrix2.getTruePositiveRate(decision3), delta);
		assertEquals(1.0, misclassificationMatrix2.getAccuracy(), delta);
		assertEquals(Math.pow(1.0*1.0*1.0, 1.0/3), misclassificationMatrix2.getGmean(), delta);
		// check deviations
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix2.getDeviationOfValue(decision3, decision3), delta);
	
		NonOrdinalMisclassificationMatrix misclassificationMatrixA = new NonOrdinalMisclassificationMatrix(misclassificationMatrix1, misclassificationMatrix2);
		// check averages
		assertEquals(6.0, misclassificationMatrixA.getNumberOfCorrectAssignments(), delta);
		assertEquals(3.0, misclassificationMatrixA.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignments(), delta);
		assertEquals(9.0, misclassificationMatrixA.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(2.0, misclassificationMatrixA.getValue(decision1, decision1), delta);
		assertEquals(0.5, misclassificationMatrixA.getValue(decision1, decision2), delta);
		assertEquals(0.5, misclassificationMatrixA.getValue(decision1, decision3), delta);
		assertEquals(0.5, misclassificationMatrixA.getValue(decision2, decision1), delta);
		assertEquals(2.0, misclassificationMatrixA.getValue(decision2, decision2), delta);
		assertEquals(0.5, misclassificationMatrixA.getValue(decision2, decision3), delta);
		assertEquals(0.5, misclassificationMatrixA.getValue(decision3, decision1), delta);
		assertEquals(0.5, misclassificationMatrixA.getValue(decision3, decision2), delta);
		assertEquals(2.0, misclassificationMatrixA.getValue(decision3, decision3), delta);
		assertEquals(2.0/3, misclassificationMatrixA.getTruePositiveRate(decision1), delta);
		assertEquals(2.0/3, misclassificationMatrixA.getTruePositiveRate(decision2), delta);
		assertEquals(2.0/3, misclassificationMatrixA.getTruePositiveRate(decision3), delta);
		assertEquals((6.0)/9, misclassificationMatrixA.getAccuracy(), delta);
		assertEquals(Math.pow((2.0/3)*(2.0/3)*(2.0/3), 1.0/3), misclassificationMatrixA.getGmean(), delta);
		// check deviations
		assertEquals(Math.sqrt(18)/9, misclassificationMatrixA.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(Math.sqrt(18), misclassificationMatrixA.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(Math.sqrt(18), misclassificationMatrixA.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(Math.sqrt(2.0)/3, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(Math.sqrt(2.0)/3, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(Math.sqrt(2.0)/3, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(Math.sqrt(2.0), misclassificationMatrixA.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(Math.sqrt(0.5), misclassificationMatrixA.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(Math.sqrt(0.5), misclassificationMatrixA.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(Math.sqrt(0.5), misclassificationMatrixA.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(Math.sqrt(2.0), misclassificationMatrixA.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(Math.sqrt(0.5), misclassificationMatrixA.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(Math.sqrt(0.5), misclassificationMatrixA.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(Math.sqrt(0.5), misclassificationMatrixA.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(Math.sqrt(2.0), misclassificationMatrixA.getDeviationOfValue(decision3, decision3), delta);
	}
	
	/**
	 * Tests of averaging multiple misclassification matrices.
	 */
	@Test
	void testAll07() {
		setUp2();
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		assignedDecisions = new SimpleDecision[] {decision1, decision2, decision3,
				decision1, decision2, decision3,
				decision1, decision2, decision3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix1 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		assignedDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix2 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1,
				decision2, decision2, decision2,
				decision3, decision3, decision3};
		assignedDecisions = new SimpleDecision[] {decision3, decision3, decision3,
				decision2, decision2, decision2,
				decision1, decision1, decision1};
		NonOrdinalMisclassificationMatrix misclassificationMatrix3 = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		assertEquals(3.0, misclassificationMatrix3.getNumberOfCorrectAssignments(), delta);
		assertEquals(6.0, misclassificationMatrix3.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownAssignments(), delta);
		assertEquals(9.0, misclassificationMatrix3.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix3.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix3.getValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getValue(decision1, decision2), delta);
		assertEquals(3.0, misclassificationMatrix3.getValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getValue(decision2, decision1), delta);
		assertEquals(3.0, misclassificationMatrix3.getValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getValue(decision2, decision3), delta);
		assertEquals(3.0, misclassificationMatrix3.getValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getValue(decision3, decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getTruePositiveRate(decision1), delta);
		assertEquals(1.0, misclassificationMatrix3.getTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getTruePositiveRate(decision3), delta);
		assertEquals(3.0/9, misclassificationMatrix3.getAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix3.getGmean(), delta);
		// check deviations
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(0.0, misclassificationMatrix3.getDeviationOfValue(decision3, decision3), delta);
	
		NonOrdinalMisclassificationMatrix misclassificationMatrixA = new NonOrdinalMisclassificationMatrix(misclassificationMatrix1, misclassificationMatrix2, misclassificationMatrix3);
		// check averages
		assertEquals(5.0, misclassificationMatrixA.getNumberOfCorrectAssignments(), delta);
		assertEquals(4.0, misclassificationMatrixA.getNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignments(), delta);
		assertEquals(9.0, misclassificationMatrixA.getNumberObjectsWithAssignedDecision(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(4.0/3, misclassificationMatrixA.getValue(decision1, decision1), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getValue(decision1, decision2), delta);
		assertEquals(4.0/3, misclassificationMatrixA.getValue(decision1, decision3), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getValue(decision2, decision1), delta);
		assertEquals(7.0/3, misclassificationMatrixA.getValue(decision2, decision2), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getValue(decision2, decision3), delta);
		assertEquals(4.0/3, misclassificationMatrixA.getValue(decision3, decision1), delta);
		assertEquals(1.0/3, misclassificationMatrixA.getValue(decision3, decision2), delta);
		assertEquals(4.0/3, misclassificationMatrixA.getValue(decision3, decision3), delta);
		assertEquals((4.0/3)/(4.0/3+1.0/3+4.0/3) , misclassificationMatrixA.getTruePositiveRate(decision1), delta);
		assertEquals((7.0/3)/(1.0/3+7.0/3+1.0/3), misclassificationMatrixA.getTruePositiveRate(decision2), delta);
		assertEquals((4.0/3)/(4.0/3+1.0/3+4.0/3), misclassificationMatrixA.getTruePositiveRate(decision3), delta);
		assertEquals((5.0)/9, misclassificationMatrixA.getAccuracy(), delta);
		assertEquals(Math.pow(((4.0/3)/(4.0/3+1.0/3+4.0/3))*((7.0/3)/(1.0/3+7.0/3+1.0/3))*((4.0/3)/(4.0/3+1.0/3+4.0/3)), 1.0/3), misclassificationMatrixA.getGmean(), delta);
		// check deviations
		assertEquals(0.384900179459, misclassificationMatrixA.getDeviationOfAccuracy(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberObjectsWithAssignedDecision(), delta);
		assertEquals(Math.sqrt(12), misclassificationMatrixA.getDeviationOfNumberOfCorrectAssignments(), delta);
		assertEquals(Math.sqrt(12), misclassificationMatrixA.getDeviationOfNumberOfIncorrectAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisions(decision3), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownAssignments(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision1), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision2), delta);
		assertEquals(0.0, misclassificationMatrixA.getDeviationOfNumberOfUnknownOriginalDecisions(decision3), delta);
		assertEquals(0.509175077217, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision1), delta);
		assertEquals(0.38490017946, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision2), delta);
		assertEquals(0.509175077217, misclassificationMatrixA.getDeviationOfTruePositiveRate(decision3), delta);
		assertEquals(1.52752523165, misclassificationMatrixA.getDeviationOfValue(decision1, decision1), delta);
		assertEquals(0.57735026919, misclassificationMatrixA.getDeviationOfValue(decision1, decision2), delta);
		assertEquals(1.52752523165, misclassificationMatrixA.getDeviationOfValue(decision1, decision3), delta);
		assertEquals(0.57735026919, misclassificationMatrixA.getDeviationOfValue(decision2, decision1), delta);
		assertEquals(1.15470053838, misclassificationMatrixA.getDeviationOfValue(decision2, decision2), delta);
		assertEquals(0.57735026919, misclassificationMatrixA.getDeviationOfValue(decision2, decision3), delta);
		assertEquals(1.52752523165, misclassificationMatrixA.getDeviationOfValue(decision3, decision1), delta);
		assertEquals(0.57735026919, misclassificationMatrixA.getDeviationOfValue(decision3, decision2), delta);
		assertEquals(1.52752523165, misclassificationMatrixA.getDeviationOfValue(decision3, decision3), delta);
	}
}
