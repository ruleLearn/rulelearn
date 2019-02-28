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
	private RuleConditions ruleConditionsMock;
	
	@Mock
	private Condition<EvaluationField> conditionMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		when(this.ruleConditionsMock.getIndicesOfCoveredObjects()).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4, 5, 10}));
		when(this.ruleConditionsMock.getIndicesOfCoveredObjectsWithCondition(this.conditionMock)).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4}));
		when(this.ruleConditionsMock.getIndicesOfCoveredObjectsWithoutCondition(0)).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4, 5, 10, 11}));
		when(this.ruleConditionsMock.getIndicesOfApproximationObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9}));
		when(this.ruleConditionsMock.getIndicesOfNeutralObjects()).thenReturn(new IntLinkedOpenHashSet());
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluate(RuleConditions)}.
	 */
	@Test
	void testEvaluate() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(1.0, coverageOutsideApproximationMeasure.evaluate(this.ruleConditionsMock));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testEvaluateWithCondition01() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(0.0, coverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock, this.conditionMock));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testEvaluateWithCondition02() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(Double.MAX_VALUE, coverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock, null));
	}

	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#evaluateWithoutCondition(RuleConditions, int)}.
	 */
	@Test
	void testEvaluateWithoutCondition() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(2.0, coverageOutsideApproximationMeasure.evaluateWithoutCondition(this.ruleConditionsMock, 0));
	}
	
	/**
	 * Test for method {@link CoverageOutsideApproximationMeasure#getMonotonictyType()}.
	 */
	@Test
	void testGetMonotonicityType() {
		CoverageOutsideApproximationMeasure coverageOutsideApproximationMeasure = CoverageOutsideApproximationMeasure.getInstance(); 
		
		assertEquals(MonotonicConditionAdditionEvaluator.MonotonicityType.DETERIORATES_WITH_NUMBER_OF_COVERED_OBJECTS, 
				coverageOutsideApproximationMeasure.getMonotonictyType());
	}

}
