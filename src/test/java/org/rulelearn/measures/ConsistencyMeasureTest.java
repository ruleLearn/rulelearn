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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.approximations.ApproximatedSet;

/**
 * Tests for {@link ConsistencyMeasure}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ConsistencyMeasureTest {
	
	private int objectIndex;
	private double threshold;
	
	@Mock
	private ConsistencyMeasure<ApproximatedSet> measureMock;
	@Mock
	private ApproximatedSet setMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.objectIndex = 0;
		this.threshold = 0.0;
		when(measureMock.isConsistencyThresholdReached(this.objectIndex, this.setMock, this.threshold)).thenCallRealMethod();
	}
	
	/**
	 * Test for method {@link org.rulelearn.measures.ConsistencyMeasure#isConsistencyThresholdReached(int, ApproximatedSet, double)}.
	 */
	@Test
	void testIsConsistencyThresholdReachedForGainTypeMeasure() {
		when(measureMock.getType()).thenReturn(Measure.MeasureType.GAIN);
		// test the same value as threshold
		when(measureMock.calculateConsistency(this.objectIndex, this.setMock)).thenReturn(this.threshold);
		assertTrue(this.measureMock.isConsistencyThresholdReached(this.objectIndex, this.setMock, this.threshold));
		// test lower value than threshold
		when(measureMock.calculateConsistency(this.objectIndex, this.setMock)).thenReturn(this.threshold - 0.5);
		assertFalse(this.measureMock.isConsistencyThresholdReached(this.objectIndex, this.setMock, this.threshold));
		// test higher value than threshold
		when(measureMock.calculateConsistency(this.objectIndex, this.setMock)).thenReturn(this.threshold + 0.5);
		assertTrue(this.measureMock.isConsistencyThresholdReached(this.objectIndex, this.setMock, this.threshold));
	}

	/**
	 * Test for method {@link org.rulelearn.measures.ConsistencyMeasure#isConsistencyThresholdReached(int, ApproximatedSet, double)}.
	 */
	@Test
	void testIsConsistencyThresholdReachedForCostTypeMeasure() {
		when(measureMock.getType()).thenReturn(Measure.MeasureType.COST);
		// test the same value as threshold
		when(measureMock.calculateConsistency(this.objectIndex, this.setMock)).thenReturn(this.threshold);
		assertTrue(this.measureMock.isConsistencyThresholdReached(this.objectIndex, this.setMock, this.threshold));
		// test lower value than threshold
		when(measureMock.calculateConsistency(this.objectIndex, this.setMock)).thenReturn(this.threshold - 0.5);
		assertTrue(this.measureMock.isConsistencyThresholdReached(this.objectIndex, this.setMock, this.threshold));
		// test higher value than threshold
		when(measureMock.calculateConsistency(this.objectIndex, this.setMock)).thenReturn(this.threshold + 0.5);
		assertFalse(this.measureMock.isConsistencyThresholdReached(this.objectIndex, this.setMock, this.threshold));
	}
}
