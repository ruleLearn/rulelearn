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
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;

/**
 * Tests for {@link CoverageInApproximationMeasure}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CoverageInApproximationMeasureTest {

	@Mock 
	private RuleConditions ruleConditionsMock;
	
	@Mock
	private Condition<EvaluationField> conditionMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		when(this.ruleConditionsMock.getIndicesOfCoveredObjects()).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4, 5}));
		when(this.ruleConditionsMock.getIndicesOfCoveredObjectsWithCondition(this.conditionMock)).thenReturn(new IntArrayList(new int [] {0, 1, 2, 3, 4}));
		when(this.ruleConditionsMock.getIndicesOfCoveredObjectsWithoutCondition(0)).thenReturn(new IntArrayList(new int [] {5}));
		when(this.ruleConditionsMock.getIndicesOfElementaryConditionsBaseObjects()).thenReturn(new IntLinkedOpenHashSet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9}));
	}
	
	/**
	 * Test for method {@link CoverageInApproximationMeasure#evaluate(RuleConditions)}.
	 */
	@Test
	void testEvalueate() {
		CoverageInApproximationMeasure coverageInApproximationMeasure = new CoverageInApproximationMeasure();
		
		assertEquals(6, coverageInApproximationMeasure.evaluate(this.ruleConditionsMock));
	}
	
	/**
	 * Test for method {@link CoverageInApproximationMeasure#evaluateWithCondition(RuleConditions, Condition)}.
	 */
	@Test
	void testEvalueateWithCondition() {
		CoverageInApproximationMeasure coverageInApproximationMeasure = new CoverageInApproximationMeasure();
		
		assertEquals(5, coverageInApproximationMeasure.evaluateWithCondition(this.ruleConditionsMock, this.conditionMock));
	}

	/**
	 * Test for method {@link CoverageInApproximationMeasure#evaluateWithoutCondition(RuleConditions, int)}.
	 */
	@Test
	void testEvalueateWithoutCondition() {
		CoverageInApproximationMeasure coverageInApproximationMeasure = new CoverageInApproximationMeasure();
		
		assertEquals(1, coverageInApproximationMeasure.evaluateWithoutCondition(this.ruleConditionsMock, 0));
	}
}
