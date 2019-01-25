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
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.measures.ConsistencyMeasure;

import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Tests for {@link VCDominanceBasedRoughSetCalculator}.
 *	
 * Test is based on illustrative example represented in figure: <img src="./doc-files/approximationsOfUnions.png"/>.
 *
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class VCDominanceBasedRoughSetCalculatorTest {

	private VCDominanceBasedRoughSetCalculator vcDRSAcalculator;
	private double lowerApproximationConsistencyThreshold = 0.0;
		
	@Mock
	private InformationTableWithDecisionDistributions informationTableMock;
	@Mock
	private IntSortedSet unionObjectsMock, complementaryUnionLowerApproximationMock;
	@Mock
	private IntBidirectionalIterator unionObjectIndicesIteratorMock;
	@Mock
	private Union unionMock, complementaryUnionMock;
	@Mock
	private ConsistencyMeasure<Union> lowerApproximationConsistencyMeasureMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.lowerApproximationConsistencyThreshold = 0.01;
		this.vcDRSAcalculator = new VCDominanceBasedRoughSetCalculator(this.lowerApproximationConsistencyMeasureMock, this.lowerApproximationConsistencyThreshold);
	}
	
	/**
	 * Sets up tests for calculation of lower approximation of at least union.
	 */
	void setUpAtLeastUnionForCalculationOfLowerApproximation() {
		// mock union objects iterator
		when(this.unionMock.getObjects()).thenReturn(this.unionObjectsMock);
		when(this.unionMock.getObjects().iterator()).thenReturn(this.unionObjectIndicesIteratorMock);
		when(this.unionObjectIndicesIteratorMock.hasNext()).thenReturn(true, false);
		when(this.unionObjectIndicesIteratorMock.nextInt()).thenReturn(1);
		// mock consistency measure
		when(this.lowerApproximationConsistencyMeasureMock.isConsistencyThresholdReached(1, this.unionMock, this.lowerApproximationConsistencyThreshold)).thenReturn(false);
	}
	
	/**
	 * Sets up tests for calculation of upper approximation of at least union.
	 */
	void setUpAtLeastUnionForCalculationOfUpperApproximation() {
		// mock information table
		when(this.unionMock.getInformationTable()).thenReturn(this.informationTableMock);
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(3);
		// mock complementary union
		when(this.unionMock.getComplementaryUnion()).thenReturn(this.complementaryUnionMock);
		// mock complementary union lower approximation
		when(this.complementaryUnionMock.getLowerApproximation()).thenReturn(this.complementaryUnionLowerApproximationMock);
		when(this.complementaryUnionLowerApproximationMock.contains(0)).thenReturn(true);
		when(this.complementaryUnionLowerApproximationMock.contains(1)).thenReturn(false);
		when(this.complementaryUnionLowerApproximationMock.contains(2)).thenReturn(false);
	}
	
	/**
	 * Sets up tests for for calculation of lower approximation of at most union.
	 */
	void setUpAtMostUnionForCalculationOfLowerApproximation() {
		// mock union objects iterator
		when(this.unionMock.getObjects()).thenReturn(this.unionObjectsMock);
		when(this.unionMock.getObjects().iterator()).thenReturn(this.unionObjectIndicesIteratorMock);
		when(this.unionObjectIndicesIteratorMock.hasNext()).thenReturn(true, true, false);
		when(this.unionObjectIndicesIteratorMock.nextInt()).thenReturn(0, 2);
		// mock consistency measure
		when(this.lowerApproximationConsistencyMeasureMock.isConsistencyThresholdReached(0, this.unionMock, this.lowerApproximationConsistencyThreshold)).thenReturn(true);
		when(this.lowerApproximationConsistencyMeasureMock.isConsistencyThresholdReached(2, this.unionMock, this.lowerApproximationConsistencyThreshold)).thenReturn(false);
	}
	
	/**
	 * Sets up tests for calculation of upper approximation of at least union.
	 */
	void setUpAtMostUnionForCalculationOfUpperApproximation() {
		// mock information table
		when(this.unionMock.getInformationTable()).thenReturn(this.informationTableMock);
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(3);
		// mock complementary union
		when(this.unionMock.getComplementaryUnion()).thenReturn(this.complementaryUnionMock);
		// mock complementary union lower approximation
		when(this.complementaryUnionMock.getLowerApproximation()).thenReturn(this.complementaryUnionLowerApproximationMock);
		when(this.complementaryUnionLowerApproximationMock.contains(0)).thenReturn(false);
		when(this.complementaryUnionLowerApproximationMock.contains(1)).thenReturn(false);
		when(this.complementaryUnionLowerApproximationMock.contains(2)).thenReturn(false);
	}
	
	/**
	 * Test for constructor {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#VCDominanceBasedRoughSetCalculator(ConsistencyMeasure, double)}, and
	 * getters: {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#getLowerApproximationConsistencyMeasure()}, and 
	 * {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#getLowerApproximationConsistencyThreshold()}.
	 */
	@Test
	void testConstruction() {
		assertEquals(this.lowerApproximationConsistencyMeasureMock, vcDRSAcalculator.getLowerApproximationConsistencyMeasure());
		assertEquals(this.lowerApproximationConsistencyThreshold, vcDRSAcalculator.getLowerApproximationConsistencyThreshold());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#calculateLowerApproximation(Union)}.
	 */
	@Test
	void testCalculateLowerApproximationForAtLeastUnion() {
		this.setUpAtLeastUnionForCalculationOfLowerApproximation();
		assertEquals(0, vcDRSAcalculator.calculateLowerApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 */
	@Test
	void testCalculateUpperApproximationForAtLeastUnion01() {
		this.setUpAtLeastUnionForCalculationOfUpperApproximation();
		assertEquals(2, vcDRSAcalculator.calculateUpperApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 */
	@Test
	void testCalculateUpperApproximationForAtLeastUnion02() {
		this.setUpAtLeastUnionForCalculationOfUpperApproximation();
		IntSortedSet upperApproximationIndices = vcDRSAcalculator.calculateUpperApproximation(this.unionMock);
		assertTrue(upperApproximationIndices.contains(1));
		assertTrue(upperApproximationIndices.contains(2));
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#calculateLowerApproximation(Union)}.
	 */
	@Test
	void testCalculateLowerApproximationForAtMostUnion01() {
		this.setUpAtMostUnionForCalculationOfLowerApproximation();
		assertEquals(1, vcDRSAcalculator.calculateLowerApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#calculateLowerApproximation(Union)}.
	 */
	@Test
	void testCalculateLowerApproximationForAtMostUnion02() {
		this.setUpAtMostUnionForCalculationOfLowerApproximation();
		assertTrue(vcDRSAcalculator.calculateLowerApproximation(this.unionMock).contains(0));
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 */
	@Test
	void testCalculateUpperApproximationForAtMostUnion01() {
		this.setUpAtMostUnionForCalculationOfUpperApproximation();
		assertEquals(3, vcDRSAcalculator.calculateUpperApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 */
	@Test
	void testCalculateUpperApproximationForAtMostUnion02() {
		this.setUpAtMostUnionForCalculationOfUpperApproximation();
		IntSortedSet upperApproximationIndices = vcDRSAcalculator.calculateUpperApproximation(this.unionMock);
		assertTrue(upperApproximationIndices.contains(0));
		assertTrue(upperApproximationIndices.contains(1));
		assertTrue(upperApproximationIndices.contains(2));
	}
}
