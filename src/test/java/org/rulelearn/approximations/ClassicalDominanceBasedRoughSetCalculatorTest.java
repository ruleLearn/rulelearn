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

package org.rulelearn.approximations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassicalDominanceBasedRoughSetCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ClassicalDominanceBasedRoughSetCalculatorTest {
	
	protected ClassicalDominanceBasedRoughSetCalculator cDRSACalculator;

	@BeforeEach
	public void setUp() {
		this.cDRSACalculator = new ClassicalDominanceBasedRoughSetCalculator();
	}
	
	/**
	 * Test for constructor {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#ClassicalDominanceBasedRoughSetCalculator()}, and
	 * for constructor {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#ClassicalDominanceBasedRoughSetCalculator(boolean)}.
	 */
	@Test
	void testConstruction() {
		assertTrue(cDRSACalculator.areDominanceRelationsReflexive());
		this.cDRSACalculator = new ClassicalDominanceBasedRoughSetCalculator(false);
		assertFalse(cDRSACalculator.areDominanceRelationsReflexive());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateLowerApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateLowerApproximation() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateUpperApproximation() {
		fail("Not yet implemented");
	}

}
