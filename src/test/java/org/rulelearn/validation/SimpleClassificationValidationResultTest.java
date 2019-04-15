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
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.classification.SimpleClassificationResult;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.SimpleDecision;

/**
 * Tests for {@link ClassificationValidationResult}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleClassificationValidationResultTest {

	@Mock
	private SimpleClassificationResult classificationResultMock1, classificationResultMock2, classificationResultMock3;
	
	@Mock
	private SimpleDecision simpleDecisionMock1, simpleDecisionMock2, simpleDecisionMock3;
	
	private SimpleClassificationResult[] assignments; 
	
	private SimpleDecision[] originalDecisions;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
						
		when(this.classificationResultMock1.isConsistentWith(simpleDecisionMock1)).thenReturn(TernaryLogicValue.TRUE);
		when(this.classificationResultMock1.isConsistentWith(simpleDecisionMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(this.classificationResultMock2.isConsistentWith(simpleDecisionMock1)).thenReturn(TernaryLogicValue.TRUE);
		when(this.classificationResultMock2.isConsistentWith(simpleDecisionMock2)).thenReturn(TernaryLogicValue.TRUE);
		when(this.classificationResultMock3.isConsistentWith(simpleDecisionMock1)).thenReturn(TernaryLogicValue.UNCOMPARABLE);
		when(this.classificationResultMock3.isConsistentWith(simpleDecisionMock2)).thenReturn(TernaryLogicValue.UNCOMPARABLE);
		
		when(this.classificationResultMock1.getSuggestedDecision()).thenReturn(simpleDecisionMock1);
		when(this.classificationResultMock2.getSuggestedDecision()).thenReturn(simpleDecisionMock2);
		when(this.classificationResultMock3.getSuggestedDecision()).thenReturn(simpleDecisionMock3);
		
		when(this.simpleDecisionMock1.isEqualTo(simpleDecisionMock1)).thenReturn(TernaryLogicValue.TRUE);
		when(this.simpleDecisionMock2.isEqualTo(simpleDecisionMock1)).thenReturn(TernaryLogicValue.FALSE);
		when(this.simpleDecisionMock3.isEqualTo(simpleDecisionMock3)).thenReturn(TernaryLogicValue.UNCOMPARABLE);
		
		
		this.assignments = new SimpleClassificationResult[] {classificationResultMock1, classificationResultMock2, classificationResultMock3};
		this.originalDecisions = new SimpleDecision[] {simpleDecisionMock1, simpleDecisionMock1, simpleDecisionMock3};
	}
	
	/**
	 * Test for {@link ClassificationValidationResult#ClassificationValidationResult(org.rulelearn.data.Decision[], org.rulelearn.classification.ClassificationResult[])}. 
	 */
	@Test
	void testConstruction() {
		ClassificationValidationResult validationResult = new ClassificationValidationResult(originalDecisions, assignments);
		assertEquals(1, validationResult.getNumberOfCorrectAssignments());
		assertEquals(1, validationResult.getNumberOfIncorrectAssignments());
		assertEquals(1, validationResult.getNumberOfUnknownAssignments());
		assertEquals(2, validationResult.getNumberOfConsistentAssignments());
		assertEquals(0, validationResult.getNumberOfInconsistentAssignments());
		assertEquals(1, validationResult.getNumberOfUncomparableAssignments());
	}

}
