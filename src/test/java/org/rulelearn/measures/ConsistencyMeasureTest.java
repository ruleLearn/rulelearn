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
import static org.mockito.Mockito.*;

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
	private ConsistencyMeasure<ApproximatedSet> measure;
	@Mock
	private ApproximatedSet set;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.objectIndex = 0;
		this.threshold = 0.0;
		when(measure.isConsistencyThresholdReached(this.objectIndex, this.set, this.threshold)).thenCallRealMethod();
	}
	
	/**
	 * Test for method {@link org.rulelearn.measures.ConsistencyMeasure#isConsistencyThresholdReached(int, ApproximatedSet, double)}.
	 */
	@Test
	void testIsConsistencyThresholdReachedForGainTypeMeasure() {
		when(measure.getType()).thenReturn(MeasureType.GAIN);
		// test the same value as threshold
		when(measure.calculateConsistency(this.objectIndex, this.set)).thenReturn(this.threshold);
		assertTrue(this.measure.isConsistencyThresholdReached(this.objectIndex, this.set, this.threshold));
		// test lower value than threshold
		when(measure.calculateConsistency(this.objectIndex, this.set)).thenReturn(this.threshold - 0.5);
		assertFalse(this.measure.isConsistencyThresholdReached(this.objectIndex, this.set, this.threshold));
		// test higher value than threshold
		when(measure.calculateConsistency(this.objectIndex, this.set)).thenReturn(this.threshold + 0.5);
		assertTrue(this.measure.isConsistencyThresholdReached(this.objectIndex, this.set, this.threshold));
	}

	/**
	 * Test for method {@link org.rulelearn.measures.ConsistencyMeasure#isConsistencyThresholdReached(int, ApproximatedSet, double)}.
	 */
	@Test
	void testIsConsistencyThresholdReachedForCostTypeMeasure() {
		when(measure.getType()).thenReturn(MeasureType.COST);
		// test the same value as threshold
		when(measure.calculateConsistency(this.objectIndex, this.set)).thenReturn(this.threshold);
		assertTrue(this.measure.isConsistencyThresholdReached(this.objectIndex, this.set, this.threshold));
		// test lower value than threshold
		when(measure.calculateConsistency(this.objectIndex, this.set)).thenReturn(this.threshold - 0.5);
		assertTrue(this.measure.isConsistencyThresholdReached(this.objectIndex, this.set, this.threshold));
		// test higher value than threshold
		when(measure.calculateConsistency(this.objectIndex, this.set)).thenReturn(this.threshold + 0.5);
		assertFalse(this.measure.isConsistencyThresholdReached(this.objectIndex, this.set, this.threshold));
	}
}
