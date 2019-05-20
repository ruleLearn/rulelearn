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
	
	/**
	 * Tests for pair: original decisions = {} and assigned decisions = {}.
	 */
	@Test
	void testAll00() {
		setUp0();
		originalDecisions = new SimpleDecision[] {};
		assignedDecisions = new SimpleDecision[] {};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(0.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions());
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision1));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5));
		assertEquals(0.0, misclassificationMatrix.getAccuracy());
		assertEquals(0.0, misclassificationMatrix.getGmean());
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
		
		assertEquals(3.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(3.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5));
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions());
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions());
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1));
		assertEquals(1.0, misclassificationMatrix.getValue(decision2, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3));
		assertEquals(1.0, misclassificationMatrix.getValue(decision4, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision1));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision4));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5));
		assertEquals(1.0, misclassificationMatrix.getAccuracy());
		assertEquals(1.0, misclassificationMatrix.getGmean());
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
		
		assertEquals(1.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(2.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(3.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5));
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions());
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision1));
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3));
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5));
		assertEquals((double)1/3, misclassificationMatrix.getTruePositiveRate(decision1));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5));
		assertEquals((double)1/3, misclassificationMatrix.getAccuracy());
		assertEquals(Math.sqrt((double)1/3), misclassificationMatrix.getGmean());
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
		
		assertEquals(1.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(2.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(3.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5));
		assertEquals(2.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions());
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision5));
		assertEquals(1.0, misclassificationMatrix.getValue(decision2, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5));
		assertEquals(1.0, misclassificationMatrix.getValue(decision4, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision1));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision3));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5));
		assertEquals((double)1/3, misclassificationMatrix.getAccuracy());
		assertEquals(0.0, misclassificationMatrix.getGmean());
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
		
		assertEquals(1.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(4.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(5.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(decision5));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision3));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision4));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(decision5));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions());
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision1, decision4));
		assertEquals(1.0, misclassificationMatrix.getValue(decision1, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision3));
		assertEquals(1.0, misclassificationMatrix.getValue(decision2, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision2, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision2));
		assertEquals(1.0, misclassificationMatrix.getValue(decision3, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision3, decision5));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision1));
		assertEquals(1.0, misclassificationMatrix.getValue(decision4, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision4, decision5));
		assertEquals(1.0, misclassificationMatrix.getValue(decision5, decision1));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision2));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision3));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision4));
		assertEquals(0.0, misclassificationMatrix.getValue(decision5, decision5));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision1));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision2));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(decision3));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision4));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(decision5));
		assertEquals((double)1/5, misclassificationMatrix.getAccuracy());
		assertEquals(0.0, misclassificationMatrix.getGmean());
	}
}
