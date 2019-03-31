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
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;

/**
 * Tests for {@link SimpleClassificationValidationResult}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleClassificationValidationResultTest {

	@Mock
	private InformationTable informationTableMock;
	
	@Mock
	private SimpleClassificationResult classificationResultMock1, classificationResultMock2, classificationResultMock3;
	
	@Mock
	private SimpleDecision simpleDecisionMock1, simpleDecisionMock2;
	
	private SimpleClassificationResult[] assignments; 
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(3);
		when(this.informationTableMock.getDecision(0)).thenReturn(simpleDecisionMock1);
		when(this.informationTableMock.getDecision(1)).thenReturn(simpleDecisionMock2);
		when(this.informationTableMock.getDecision(2)).thenReturn(simpleDecisionMock1);
				
		when(this.classificationResultMock1.isConsistentWith(simpleDecisionMock1)).thenReturn(TernaryLogicValue.TRUE);
		when(this.classificationResultMock1.isConsistentWith(simpleDecisionMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(this.classificationResultMock2.isConsistentWith(simpleDecisionMock1)).thenReturn(TernaryLogicValue.TRUE);
		when(this.classificationResultMock2.isConsistentWith(simpleDecisionMock2)).thenReturn(TernaryLogicValue.FALSE);
		when(this.classificationResultMock3.isConsistentWith(simpleDecisionMock1)).thenReturn(TernaryLogicValue.UNCOMPARABLE);
		when(this.classificationResultMock3.isConsistentWith(simpleDecisionMock2)).thenReturn(TernaryLogicValue.UNCOMPARABLE);
		
		this.assignments = new SimpleClassificationResult[] {classificationResultMock1, classificationResultMock2, classificationResultMock3};
	}
	
	/**
	 * Test for {@link SimpleClassificationValidationResult#SimpleClassificationValidationResult(InformationTable, org.rulelearn.classification.SimpleClassificationResult[])}. 
	 */
	@Test
	void testConstruction() {
		SimpleClassificationValidationResult validationResult = new SimpleClassificationValidationResult(this.informationTableMock, assignments);
		assertEquals(1, validationResult.getNumberOfCorrectAssignments());
		assertEquals(1, validationResult.getNumberOfIncorrectAssignments());
		assertEquals(1, validationResult.getNumberOfUnknownAssignments());
	}

}
