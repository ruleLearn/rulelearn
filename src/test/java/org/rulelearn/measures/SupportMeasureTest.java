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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.MonotonicConditionAdditionEvaluator;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleCoverageInformation;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;

/**
 * Tests for {@link SupportMeasure}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SupportMeasureTest {
	
	@Mock 
	private RuleConditions ruleConditionsMock;
	
	@Mock
	private Condition<EvaluationField> conditionMock;

	@Mock
	private RuleCoverageInformation ruleCoverageInformationMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(this.ruleCoverageInformationMock.getIndicesOfCoveredObjects()).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4, 5}));
		when(this.ruleCoverageInformationMock.getIndicesOfPositiveObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 1, 2, 5}));
		
		when(this.ruleConditionsMock.getIndicesOfCoveredObjects()).thenReturn(new IntArrayList(new int [] {0, 1, 3, 4, 5}));
		when(this.ruleConditionsMock.getIndicesOfCoveredObjectsWithCondition(this.conditionMock)).thenReturn(new IntArrayList(new int [] {0, 1, 5}));
		when(this.ruleConditionsMock.getIndicesOfCoveredObjectsWithoutCondition(0)).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4, 5, 7}));
		when(this.ruleConditionsMock.getIndicesOfPositiveObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 2, 3, 5, 6, 9}));
	}
	
	/**
	 * Test for method {@link SupportMeasure#evaluate(RuleCoverageInformation)}.
	 */
	@Test
	void testEvaluate01() {
		SupportMeasure support = SupportMeasure.getInstance();
		assertEquals(4, support.evaluate(this.ruleCoverageInformationMock));
	}
	
	/**
	 * Test for method {@link SupportMeasure#evaluate(RuleConditions)}.
	 */
	@Test
	void testEvaluate02() {
		SupportMeasure support = SupportMeasure.getInstance();
		
		assertEquals(3.0, support.evaluate(this.ruleConditionsMock));
	}
	
	/**
	 * Test for method {@link SupportMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testEvaluateWithCondition01() {
		SupportMeasure support = SupportMeasure.getInstance();
		
		assertEquals(2.0, support.evaluateWithCondition(this.ruleConditionsMock, this.conditionMock));
	}
	
	/**
	 * Test for method {@link SupportMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testEvaluateWithCondition02() {
		SupportMeasure support = SupportMeasure.getInstance();
		
		assertEquals(Double.MIN_VALUE, support.evaluateWithCondition(this.ruleConditionsMock, null));
	}
	
	/**
	 * Test for method {@link SupportMeasure#evaluateWithoutCondition(RuleConditions, int)}.
	 */
	@Test
	void testEvaluateWithoutCondition() {
		SupportMeasure support = SupportMeasure.getInstance();
		
		assertEquals(4.0, support.evaluateWithoutCondition(this.ruleConditionsMock, 0));
	}
	
	/**
	 * Test for method {@link SupportMeasure#getMonotonictyType()}.
	 */
	@Test
	void testGetMonotonicityType() {
		SupportMeasure support = SupportMeasure.getInstance(); 
		
		assertEquals(MonotonicConditionAdditionEvaluator.MonotonicityType.IMPROVES_WITH_NUMBER_OF_COVERED_OBJECTS, 
				support.getMonotonictyType());
	}

}
