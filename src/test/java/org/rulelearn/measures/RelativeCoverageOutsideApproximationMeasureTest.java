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
import org.rulelearn.data.InformationTable;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;

/**
 * Tests for {@link RelativeCoverageOutsideApproximationMeasure}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RelativeCoverageOutsideApproximationMeasureTest {

	@Mock 
	private RuleConditions ruleConditionsMock2;
	
	@Mock
	private Condition<EvaluationField> conditionMock;
	
	@Mock
	private InformationTable informationTableMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(this.ruleConditionsMock2.getIndicesOfCoveredObjects()).thenReturn(new IntArrayList(new int [] {0, 1, 3, 4, 6, 7, 9, 11, 13, 14}));
		when(this.ruleConditionsMock2.getIndicesOfCoveredObjectsWithCondition(this.conditionMock)).thenReturn(new IntArrayList(new int [] {0, 1, 3, 7, 9, 11, 14}));
		when(this.ruleConditionsMock2.getIndicesOfCoveredObjectsWithoutCondition(0)).thenReturn(new IntArrayList(new int [] {0, 1, 3, 4, 6, 7, 9, 10, 11, 12, 13, 14, 15}));
		when(this.ruleConditionsMock2.getIndicesOfApproximationObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 2, 4, 5, 7, 8, 12}));
		when(this.ruleConditionsMock2.getIndicesOfPositiveObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 2, 4, 5, 7, 8, 12, 16, 17, 18, 19})); //!
		when(this.ruleConditionsMock2.getIndicesOfNeutralObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {1, 6, 10, 14}));
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(20);
		when(this.ruleConditionsMock2.getLearningInformationTable()).thenReturn(informationTableMock);		
	}
	
	/**
	 * Test for method {@link RelativeCoverageOutsideApproximationMeasure#evaluate(RuleConditions)}.
	 */
	@Test
	void testMock2Evaluate() {
		RelativeCoverageOutsideApproximationMeasure relativeCoverageOutsideApproximationMeasure = RelativeCoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals((double)4 / 5, relativeCoverageOutsideApproximationMeasure.evaluate(this.ruleConditionsMock2));
	}
	
	/**
	 * Test for method {@link RelativeCoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testMock2EvaluateWithCondition01() {
		RelativeCoverageOutsideApproximationMeasure relativeCoverageOutsideApproximationMeasure = RelativeCoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals((double)3 / 5, relativeCoverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock2, this.conditionMock));
	}
	
	/**
	 * Test for method {@link RelativeCoverageOutsideApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testMock2EvaluateWithCondition02() {
		RelativeCoverageOutsideApproximationMeasure relativeCoverageOutsideApproximationMeasure = RelativeCoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals(Double.MAX_VALUE, relativeCoverageOutsideApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock2, null));
	}
	
	/**
	 * Test for method {@link RelativeCoverageOutsideApproximationMeasure#evaluateWithoutCondition(RuleConditions, int)}.
	 */
	@Test
	void testMock2EvaluateWithoutCondition() {
		RelativeCoverageOutsideApproximationMeasure relativeCoverageOutsideApproximationMeasure = RelativeCoverageOutsideApproximationMeasure.getInstance();
		
		assertEquals((double)5 / 5, relativeCoverageOutsideApproximationMeasure.evaluateWithoutCondition(this.ruleConditionsMock2, 0));
	}
	
	/**
	 * Test for method {@link RelativeCoverageOutsideApproximationMeasure#getMonotonictyType()}.
	 */
	@Test
	void testGetMonotonicityType() {
		RelativeCoverageOutsideApproximationMeasure relativeCoverageOutsideApproximationMeasure = RelativeCoverageOutsideApproximationMeasure.getInstance(); 
		
		assertEquals(relativeCoverageOutsideApproximationMeasure.getMonotonictyType(), MonotonicConditionAdditionEvaluator.MonotonicityType.DETERIORATES_WITH_NUMBER_OF_COVERED_OBJECTS);
	}

}
