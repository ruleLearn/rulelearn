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
 * Tests for {@link RuleConditionsEvaluator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleConditionsEvaluatorTest {
	
	private double threshold;
	
	@Mock
	RuleConditionsEvaluator ruleConditionsEvaluatorMock;
	@Mock
	RuleConditions ruleConditionsMock;
	@Mock
	RuleConditions ruleConditionsMock2;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.threshold = 0.0;
		when(ruleConditionsEvaluatorMock.evaluationSatisfiesThreshold(this.ruleConditionsMock, this.threshold)).thenCallRealMethod();
		when(ruleConditionsEvaluatorMock.confront(ruleConditionsMock, ruleConditionsMock2)).thenCallRealMethod();
		when(ruleConditionsEvaluatorMock.confront(ruleConditionsMock2, ruleConditionsMock)).thenCallRealMethod();
	}
	
	/**
	 * Test for method {@link RuleConditionsEvaluator#evaluationSatisfiesThreshold(RuleConditions, double)}.
	 */
	@Test
	void testEvaluationSatisfiesThresholdForGainTypeEvaluator() {
		when(this.ruleConditionsEvaluatorMock.getType()).thenReturn(Measure.MeasureType.GAIN);
		// test the same value as threshold
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(this.threshold);
		assertTrue(this.ruleConditionsEvaluatorMock.evaluationSatisfiesThreshold(this.ruleConditionsMock, this.threshold));
		// test lower value than threshold
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(this.threshold - 0.5);
		assertFalse(this.ruleConditionsEvaluatorMock.evaluationSatisfiesThreshold(this.ruleConditionsMock, this.threshold));
		// test higher value than threshold
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(this.threshold + 0.5);
		assertTrue(this.ruleConditionsEvaluatorMock.evaluationSatisfiesThreshold(this.ruleConditionsMock, this.threshold));
	}
	
	/**
	 * Test for method {@link RuleConditionsEvaluator#evaluationSatisfiesThreshold(RuleConditions, double)}.
	 */
	@Test
	void testEvaluationSatisfiesThresholdForCostTypeEvaluator() {
		when(this.ruleConditionsEvaluatorMock.getType()).thenReturn(Measure.MeasureType.COST);
		// test the same value as threshold
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(this.threshold);
		assertTrue(this.ruleConditionsEvaluatorMock.evaluationSatisfiesThreshold(this.ruleConditionsMock, this.threshold));
		// test lower value than threshold
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(this.threshold - 0.5);
		assertTrue(this.ruleConditionsEvaluatorMock.evaluationSatisfiesThreshold(this.ruleConditionsMock, this.threshold));
		// test higher value than threshold
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(this.threshold + 0.5);
		assertFalse(this.ruleConditionsEvaluatorMock.evaluationSatisfiesThreshold(this.ruleConditionsMock, this.threshold));
	}
	
	/**
	 * Test for method {@link RuleConditionsEvaluator#confront(RuleConditions, RuleConditions)}.
	 */
	@Test
	void testConfrontForGainTypeEvaluator() {
		when(this.ruleConditionsEvaluatorMock.getType()).thenReturn(Measure.MeasureType.GAIN);
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(0.2);
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock2)).thenReturn(0.0);
		
		assertTrue(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock, this.ruleConditionsMock2) > 0);
		assertFalse(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock, this.ruleConditionsMock2) < 0);
		assertTrue(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock2, this.ruleConditionsMock) < 0);
		assertFalse(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock2, this.ruleConditionsMock) > 0);
	}
	
	/**
	 * Test for method {@link RuleConditionsEvaluator#confront(RuleConditions, RuleConditions)}.
	 */
	@Test
	void testConfrontForCostTypeEvaluator() {
		when(this.ruleConditionsEvaluatorMock.getType()).thenReturn(Measure.MeasureType.COST);
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock)).thenReturn(0.0);
		when(this.ruleConditionsEvaluatorMock.evaluate(this.ruleConditionsMock2)).thenReturn(0.2);
		
		assertTrue(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock, this.ruleConditionsMock2) > 0);
		assertFalse(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock, this.ruleConditionsMock2) < 0);
		assertTrue(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock2, this.ruleConditionsMock) < 0);
		assertFalse(this.ruleConditionsEvaluatorMock.confront(this.ruleConditionsMock2, this.ruleConditionsMock) > 0);
	}
}