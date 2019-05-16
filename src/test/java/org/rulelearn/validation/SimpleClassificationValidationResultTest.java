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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rulelearn.classification.SimpleClassificationResult;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link ClassificationValidationResult}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleClassificationValidationResultTest {

	private SimpleClassificationResult classificationResult1, classificationResult2, classificationResult3, classificationResult4,
			classificationResult5;

	private SimpleDecision decision1, decision2, decision3, decision4, decision5;

	private SimpleClassificationResult[] assignments; 
	private SimpleDecision[] originalDecisions;
	
	@BeforeEach
	void setUp() {		
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
				
		classificationResult1 = new SimpleClassificationResult(decision1);
		classificationResult2 = new SimpleClassificationResult(decision2);
		classificationResult3 = new SimpleClassificationResult(decision3);
		classificationResult4 = new SimpleClassificationResult(decision4);
		classificationResult5 = new SimpleClassificationResult(decision5);
	}
	
	/**
	 * Test for {@link ClassificationValidationResult#ClassificationValidationResult(org.rulelearn.data.Decision[], org.rulelearn.classification.ClassificationResult[])}, 
	 * and its resulting values.
	 */
	@Test
	void testAll00() {
		assignments = new SimpleClassificationResult[] {classificationResult1, classificationResult2, classificationResult3,
				classificationResult4, classificationResult5};
		originalDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		ClassificationValidationResult validationResult = new ClassificationValidationResult(originalDecisions, assignments);
		assertEquals(3, validationResult.getNumberOfCorrectAssignments());
		assertEquals(0, validationResult.getNumberOfIncorrectAssignments());
		assertEquals(2, validationResult.getNumberOfUnknownAssignments());
		assertEquals(5, validationResult.getNumberOfConsistentAssignments());
		assertEquals(0, validationResult.getNumberOfInconsistentAssignments());
		assertEquals(0, validationResult.getNumberOfUncomparableAssignments());
	}
	
	/**
	 * Test for {@link ClassificationValidationResult#ClassificationValidationResult(org.rulelearn.data.Decision[], org.rulelearn.classification.ClassificationResult[])}, 
	 * and its resulting values.
	 */
	@Test
	void testAll01() {
		assignments = new SimpleClassificationResult[] {classificationResult1, classificationResult2, classificationResult3,
				classificationResult4, classificationResult5};
		originalDecisions = new SimpleDecision[] {decision1, decision1, decision1, decision1, decision1};
		ClassificationValidationResult validationResult = new ClassificationValidationResult(originalDecisions, assignments);
		assertEquals(1, validationResult.getNumberOfCorrectAssignments());
		assertEquals(2, validationResult.getNumberOfIncorrectAssignments());
		assertEquals(2, validationResult.getNumberOfUnknownAssignments());
		assertEquals(2, validationResult.getNumberOfConsistentAssignments());
		assertEquals(1, validationResult.getNumberOfInconsistentAssignments());
		assertEquals(2, validationResult.getNumberOfUncomparableAssignments());
	}
	
	/**
	 * Test for {@link ClassificationValidationResult#ClassificationValidationResult(org.rulelearn.data.Decision[], org.rulelearn.classification.ClassificationResult[])}, 
	 * and its resulting values.
	 */
	@Test
	void testAll02() {
		assignments = new SimpleClassificationResult[] {classificationResult1, classificationResult1, classificationResult1,
				classificationResult1, classificationResult1};
		originalDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		ClassificationValidationResult validationResult = new ClassificationValidationResult(originalDecisions, assignments);
		assertEquals(1, validationResult.getNumberOfCorrectAssignments());
		assertEquals(4, validationResult.getNumberOfIncorrectAssignments());
		assertEquals(0, validationResult.getNumberOfUnknownAssignments());
		assertEquals(1, validationResult.getNumberOfConsistentAssignments());
		assertEquals(2, validationResult.getNumberOfInconsistentAssignments());
		assertEquals(2, validationResult.getNumberOfUncomparableAssignments());
	}

	/**
	 * Test for {@link ClassificationValidationResult#ClassificationValidationResult(org.rulelearn.data.Decision[], org.rulelearn.classification.ClassificationResult[])}, 
	 * and its resulting values.
	 */
	@Test
	void testAll03() {
		assignments = new SimpleClassificationResult[] {classificationResult1, classificationResult2, classificationResult3,
				classificationResult4, classificationResult5};
		originalDecisions = new SimpleDecision[] {decision4, decision4, decision4, decision4, decision4};
		ClassificationValidationResult validationResult = new ClassificationValidationResult(originalDecisions, assignments);
		assertEquals(1, validationResult.getNumberOfCorrectAssignments());
		assertEquals(2, validationResult.getNumberOfIncorrectAssignments());
		assertEquals(2, validationResult.getNumberOfUnknownAssignments());
		assertEquals(2, validationResult.getNumberOfConsistentAssignments());
		assertEquals(0, validationResult.getNumberOfInconsistentAssignments());
		assertEquals(3, validationResult.getNumberOfUncomparableAssignments());
	}
	
	/**
	 * Test for {@link ClassificationValidationResult#ClassificationValidationResult(org.rulelearn.data.Decision[], org.rulelearn.classification.ClassificationResult[])}, 
	 * and its resulting values.
	 */
	@Test
	void testAll04() {
		assignments = new SimpleClassificationResult[] {classificationResult4, classificationResult4, classificationResult4,
				classificationResult4, classificationResult4};
		originalDecisions = new SimpleDecision[] {decision1, decision2, decision3, decision4, decision5};
		ClassificationValidationResult validationResult = new ClassificationValidationResult(originalDecisions, assignments);
		assertEquals(1, validationResult.getNumberOfCorrectAssignments());
		assertEquals(4, validationResult.getNumberOfIncorrectAssignments());
		assertEquals(0, validationResult.getNumberOfUnknownAssignments());
		assertEquals(2, validationResult.getNumberOfConsistentAssignments());
		assertEquals(0, validationResult.getNumberOfInconsistentAssignments());
		assertEquals(3, validationResult.getNumberOfUncomparableAssignments());
	}
	
}
