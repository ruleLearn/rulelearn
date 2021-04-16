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

package org.rulelearn.rules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.measures.Measure;

/**
 * Tests for {@link ConditionRemovalEvaluator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ConditionRemovalEvaluatorTest {
	
	private int conditionIndex;
	private double threshold;
	
	@Mock
	ConditionRemovalEvaluator conditionRemovalEvaluatorMock;
	@Mock
	RuleConditions ruleConditionsMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.conditionIndex = 0;
		this.threshold = 0.0;
		when(conditionRemovalEvaluatorMock.evaluationSatisfiesThresholdWithoutCondition(this.ruleConditionsMock, this.threshold, this.conditionIndex)).thenCallRealMethod();
	}

	/**
	 * Test for method {@link ConditionRemovalEvaluator#evaluationSatisfiesThresholdWithoutCondition(RuleConditions, double, int)}.
	 */
	@Test
	void testEvaluationSatisfiesThresholdWithoutConditionForGainTypeEvaluator () {
		when(this.conditionRemovalEvaluatorMock.getType()).thenReturn(Measure.MeasureType.GAIN);
		// test the same value as threshold
		when(this.conditionRemovalEvaluatorMock.evaluateWithoutCondition(this.ruleConditionsMock, this.conditionIndex)).thenReturn(this.threshold);
		assertTrue(this.conditionRemovalEvaluatorMock.evaluationSatisfiesThresholdWithoutCondition(this.ruleConditionsMock, this.threshold, this.conditionIndex));
		// test lower value than threshold
		when(this.conditionRemovalEvaluatorMock.evaluateWithoutCondition(this.ruleConditionsMock, this.conditionIndex)).thenReturn(this.threshold - 0.5);
		assertFalse(this.conditionRemovalEvaluatorMock.evaluationSatisfiesThresholdWithoutCondition(this.ruleConditionsMock, this.threshold, this.conditionIndex));
		// test higher value than threshold
		when(this.conditionRemovalEvaluatorMock.evaluateWithoutCondition(this.ruleConditionsMock, this.conditionIndex)).thenReturn(this.threshold + 0.5);
		assertTrue(this.conditionRemovalEvaluatorMock.evaluationSatisfiesThresholdWithoutCondition(this.ruleConditionsMock, this.threshold, this.conditionIndex));
	}
	
	/**
	 * Test for method {@link ConditionRemovalEvaluator#evaluationSatisfiesThresholdWithoutCondition(RuleConditions, double, int)}.
	 */
	@Test
	void testEvaluationSatisfiesThresholdWithoutConditionForCostTypeEvaluator () {
		when(this.conditionRemovalEvaluatorMock.getType()).thenReturn(Measure.MeasureType.COST);
		// test the same value as threshold
		when(this.conditionRemovalEvaluatorMock.evaluateWithoutCondition(this.ruleConditionsMock, this.conditionIndex)).thenReturn(this.threshold);
		assertTrue(this.conditionRemovalEvaluatorMock.evaluationSatisfiesThresholdWithoutCondition(this.ruleConditionsMock, this.threshold, this.conditionIndex));
		// test lower value than threshold
		when(this.conditionRemovalEvaluatorMock.evaluateWithoutCondition(this.ruleConditionsMock, this.conditionIndex)).thenReturn(this.threshold - 0.5);
		assertTrue(this.conditionRemovalEvaluatorMock.evaluationSatisfiesThresholdWithoutCondition(this.ruleConditionsMock, this.threshold, this.conditionIndex));
		// test higher value than threshold
		when(this.conditionRemovalEvaluatorMock.evaluateWithoutCondition(this.ruleConditionsMock, this.conditionIndex)).thenReturn(this.threshold + 0.5);
		assertFalse(this.conditionRemovalEvaluatorMock.evaluationSatisfiesThresholdWithoutCondition(this.ruleConditionsMock, this.threshold, this.conditionIndex));
	}

}
