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

package org.rulelearn.measures;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;

/**
 * Tests for {@link CoverageOutsideApproximationMeasure}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CoverageOutsideApproximationMeasureTest {

	@Mock 
	private RuleConditions ruleConditionsMock1;
	
	@Mock 
	private RuleConditions ruleConditionsMock2;
	
	@Mock
	private Condition<EvaluationField> conditionMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(this.ruleConditionsMock1.getIndicesOfCoveredObjects()).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4, 5, 10}));
		when(this.ruleConditionsMock1.getIndicesOfCoveredObjectsWithCondition(this.conditionMock)).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4}));
		when(this.ruleConditionsMock1.getIndicesOfCoveredObjectsWithoutCondition(0)).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4, 5, 10, 11}));
		when(this.ruleConditionsMock1.getIndicesOfApproximationObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 1, 2, 3, 4, 5,6, 7, 8, 9}));
		when(this.ruleConditionsMock1.getIndicesOfNeutralObjects()).thenReturn(new IntLinkedOpenHashSet());
		
		when(this.ruleConditionsMock2.getIndicesOfCoveredObjects()).thenReturn(new IntArrayList(new int [] {0, 1, 3, 4, 6, 7, 9, 11, 13, 14}));
		when(this.ruleConditionsMock2.getIndicesOfCoveredObjectsWithCondition(this.conditionMock)).thenReturn(new IntArrayList(new int [] {0, 1, 3, 7, 9, 11, 14}));
		when(this.ruleConditionsMock2.getIndicesOfCoveredObjectsWithoutCondition(0)).thenReturn(new IntArrayList(new int [] {0, 1, 3, 4, 6, 7, 9, 10, 11, 12, 13, 14, 15}));
		when(this.ruleConditionsMock2.getIndicesOfApproximationObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 2, 4, 5, 7, 8, 12}));
		when(this.ruleConditionsMock2.getIndicesOfNeutralObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {1, 6, 10, 14}));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluate(RuleConditions)}.
	 */
	@Test
	void testMock1Evaluate() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(1.0, coverageOutsideApproximationMeasure.evaluate(this.ruleConditionsMock1));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluate(RuleConditions)}.
	 */
	@Test
	void testMock2Evaluate() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(4.0, coverageOutsideApproximationMeasure.evaluate(this.ruleConditionsMock2));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testMock1EvaluateWithCondition01() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(0.0, coverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock1, this.conditionMock));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testMock1EvaluateWithCondition02() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(Double.MAX_VALUE, coverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock1, null));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testMock2EvaluateWithCondition01() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(3.0, coverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock2, this.conditionMock));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testMock2EvaluateWithCondition02() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(Double.MAX_VALUE, coverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock2, null));
	}

	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithoutCondition(RuleConditions, int)}.
	 */
	@Test
	void testMock1EvaluateWithoutCondition() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(2.0, coverageOutsideApproximationMeasure.evaluateWithoutCondition(this.ruleConditionsMock1, 0));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithoutCondition(RuleConditions, int)}.
	 */
	@Test
	void testMock2EvaluateWithoutCondition() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(5.0, coverageOutsideApproximationMeasure.evaluateWithoutCondition(this.ruleConditionsMock2, 0));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#getMonotonictyType()}.
	 */
	@Test
	void testGetMonotonicityType() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance(); 
		
		assertEquals(coverageOutsideApproximationMeasure.getMonotonictyType(), MonotonicConditionAdditionEvaluator.MonotonicityType.DETERIORATES_WITH_NUMBER_OF_COVERED_OBJECTS);
	}

}
