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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.SimpleDecision;

/**
 * Tests for {@link NonOrdinalMisclassificationMatrix}. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class NonOrdinalMisclassificationMatrixTest {

	@Mock
	private SimpleDecision simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock3;
	
	private SimpleDecision[] originalDecisions;
	
	private SimpleDecision[] assignedDecisions; 
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		
		// simpleDecisionMock1 = a, simpleDecisionMock2 = b, simpleDecisionMock3 = ?;
		when(this.simpleDecisionMock1.hasNoMissingEvaluation()).thenReturn(true);
		when(this.simpleDecisionMock1.isEqualTo(simpleDecisionMock1)).thenReturn(TernaryLogicValue.TRUE);
		when(this.simpleDecisionMock1.isEqualTo(simpleDecisionMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(this.simpleDecisionMock1.isEqualTo(simpleDecisionMock3)).thenReturn(TernaryLogicValue.UNCOMPARABLE); // TODO
		when(this.simpleDecisionMock2.hasNoMissingEvaluation()).thenReturn(true);
		when(this.simpleDecisionMock2.isEqualTo(simpleDecisionMock1)).thenReturn(TernaryLogicValue.FALSE);
		when(this.simpleDecisionMock2.isEqualTo(simpleDecisionMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(this.simpleDecisionMock2.isEqualTo(simpleDecisionMock3)).thenReturn(TernaryLogicValue.UNCOMPARABLE); // TODO
		when(this.simpleDecisionMock3.hasNoMissingEvaluation()).thenReturn(false);
	}
	
	/**
	 * Tests for pair: original decisions = {} and assigned decisions = {}.
	 */
	@Test
	void testAll00() {
		originalDecisions = new SimpleDecision[] {};
		assignedDecisions = new SimpleDecision[] {};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(0.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getAccuracy());
	}
	
	/**
	 * Tests for pair: original decisions = {a, b, ?} and assigned decisions = {?, ?, ?}.
	 */
	@Test
	void testAll01() {
		originalDecisions = new SimpleDecision[] {simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock3};
		assignedDecisions = new SimpleDecision[] {simpleDecisionMock3, simpleDecisionMock3, simpleDecisionMock3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(0.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(3.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(1.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock1));
		assertEquals(1.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getAccuracy());
	}
	
	
	/**
	 * Tests for pair: original decisions = {?, ?, ?} and assigned decisions = {a, b, ?}.
	 */
	@Test
	void testAll02() {
		originalDecisions = new SimpleDecision[] {simpleDecisionMock3, simpleDecisionMock3, simpleDecisionMock3};
		assignedDecisions = new SimpleDecision[] {simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(0.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(1.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(1.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock1));
		assertEquals(1.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getAccuracy());
	}
	
	/**
	 * Tests for pair: original decisions = {a, b, ?} and assigned decisions = {a, b, ?}.
	 */
	@Test
	void testAll03() {
		originalDecisions = new SimpleDecision[] {simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock3};
		assignedDecisions = new SimpleDecision[] {simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(2.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(1.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock3));
		assertEquals(2.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock3));
		assertEquals(1.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock1));
		assertEquals(1.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock3));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock1));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock3));
		assertEquals(1.0, misclassificationMatrix.getAccuracy());
	}
	
	/**
	 * Tests for pair: original decisions = {a, b, a, ?} and assigned decisions = {a, b, b, ?}.
	 */
	@Test
	void testAll04() {
		originalDecisions = new SimpleDecision[] {simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock1, simpleDecisionMock3};
		assignedDecisions = new SimpleDecision[] {simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock2, simpleDecisionMock3};
		NonOrdinalMisclassificationMatrix misclassificationMatrix = new NonOrdinalMisclassificationMatrix(originalDecisions, assignedDecisions);
		
		assertEquals(2.0, misclassificationMatrix.getNumberOfCorrectAssignments());
		assertEquals(1.0, misclassificationMatrix.getNumberOfIncorrectAssignments());
		assertEquals(1.0, misclassificationMatrix.getNumberOfUnknownAssignments());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownAssignedDecisions(simpleDecisionMock3));
		assertEquals(3.0, misclassificationMatrix.getNumberObjectsWithAssignedDecision());
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getNumberOfUnknownOriginalDecisions(simpleDecisionMock3));
		assertEquals(1.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock1));
		assertEquals(1.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock1, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock1));
		assertEquals(1.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock2, simpleDecisionMock3));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock1));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getValue(simpleDecisionMock3, simpleDecisionMock3));
		assertEquals(0.5, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock1));
		assertEquals(1.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock2));
		assertEquals(0.0, misclassificationMatrix.getTruePositiveRate(simpleDecisionMock3));
		assertEquals(2.0/3, misclassificationMatrix.getAccuracy());
	}

}
