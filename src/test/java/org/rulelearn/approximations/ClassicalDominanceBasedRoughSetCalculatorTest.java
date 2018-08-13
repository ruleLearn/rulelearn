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

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.DecisionDistribution;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.dominance.DominanceConesDecisionDistributions;

import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Tests for {@link ClassicalDominanceBasedRoughSetCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ClassicalDominanceBasedRoughSetCalculatorTest {
	
	private ClassicalDominanceBasedRoughSetCalculator cDRSAcalculator;
	
	@Mock
	private InformationTableWithDecisionDistributions infromationTableMock;
	@Mock
	private IntSortedSet unionObjectsMock;
	@Mock
	private IntBidirectionalIterator unionObjectIndicesIteratorMock;
	@Mock
	DominanceConesDecisionDistributions dominanceCDDMock;
	@Mock
	private DecisionDistribution coneDecisionClassDistribution1, coneDecisionClassDistribution2, coneDecisionClassDistribution3;
	@Mock
	private Decision class1, class2;
	@Mock
	private Union unionMock;
	@Mock
	private Set<Decision> decisionClassDistributionSet1, decisionClassDistributionSet2, decisionClassDistributionSet3;
	@Mock
	private Iterator<Decision> decisionClassDistributionIterator1, decisionClassDistributionIterator2, decisionClassDistributionIterator3;
	
	@BeforeEach
	void setUp() {
		this.cDRSAcalculator = new ClassicalDominanceBasedRoughSetCalculator();
	}
	
	/**
	 * Sets up tests for calculation of lower approximation of at least union.
	 */
	void setUpAtLeastUnionForCalculationOfLowerApproximation() {
		MockitoAnnotations.initMocks(this);
		// mock union
		when(this.unionMock.getUnionType()).thenReturn(UnionType.AT_LEAST);
		when(this.unionMock.isDecisionNegative(class1)).thenReturn(true);
		when(this.unionMock.isDecisionNegative(class2)).thenReturn(false);
		// mock union objects iterator
		when(this.unionMock.getObjects()).thenReturn(this.unionObjectsMock);
		when(this.unionMock.getObjects().iterator()).thenReturn(this.unionObjectIndicesIteratorMock);
		when(this.unionObjectIndicesIteratorMock.hasNext()).thenReturn(true, false);
		when(this.unionObjectIndicesIteratorMock.nextInt()).thenReturn(1);
		// mock information table
		when(this.unionMock.getInformationTable()).thenReturn(this.infromationTableMock);
		when(this.infromationTableMock.getDominanceConesDecisionDistributions()).thenReturn(this.dominanceCDDMock);
		// mock class distribution in positive inverted cone based on inconsistent object 2
		when(this.dominanceCDDMock.getPositiveInvDConeDecisionClassDistribution(1)).thenReturn(coneDecisionClassDistribution2);
		when(this.coneDecisionClassDistribution2.getDecisions()).thenReturn(this.decisionClassDistributionSet2);
		when(this.decisionClassDistributionSet2.iterator()).thenReturn(decisionClassDistributionIterator2);
		when(this.decisionClassDistributionIterator2.hasNext()).thenReturn(true, true, false);
		when(this.decisionClassDistributionIterator2.next()).thenReturn(class1, class2);
	}
	
	/**
	 * Sets up tests for calculation of upper approximation of at least union.
	 */
	void setUpAtLeastUnionForCalculationOfUpperApproximation() {
		MockitoAnnotations.initMocks(this);
		// mock union
		when(this.unionMock.getUnionType()).thenReturn(UnionType.AT_LEAST);
		when(this.unionMock.isDecisionPositive(class1)).thenReturn(false);
		when(this.unionMock.isDecisionPositive(class2)).thenReturn(true);
		// mock information table
		when(this.unionMock.getInformationTable()).thenReturn(this.infromationTableMock);
		when(this.infromationTableMock.getNumberOfObjects()).thenReturn(3);
		when(this.infromationTableMock.getDominanceConesDecisionDistributions()).thenReturn(this.dominanceCDDMock);
		// mock class distribution in negative cone based on inconsistent object 1
		when(this.dominanceCDDMock.getNegativeDConeDecisionClassDistribution(0)).thenReturn(coneDecisionClassDistribution1);
		when(this.coneDecisionClassDistribution1.getDecisions()).thenReturn(this.decisionClassDistributionSet1);
		when(this.decisionClassDistributionSet1.iterator()).thenReturn(decisionClassDistributionIterator1);
		when(this.decisionClassDistributionIterator1.hasNext()).thenReturn(true, false);
		when(this.decisionClassDistributionIterator1.next()).thenReturn(class1);
		// mock class distribution in negative cone based on inconsistent object 2
		when(this.dominanceCDDMock.getNegativeDConeDecisionClassDistribution(1)).thenReturn(coneDecisionClassDistribution2);
		when(this.coneDecisionClassDistribution2.getDecisions()).thenReturn(this.decisionClassDistributionSet2);
		when(this.decisionClassDistributionSet2.iterator()).thenReturn(decisionClassDistributionIterator2);
		when(this.decisionClassDistributionIterator2.hasNext()).thenReturn(true, true, false);
		when(this.decisionClassDistributionIterator2.next()).thenReturn(class1, class2);
		// mock class distribution in negative cone based on inconsistent object 3
		when(this.dominanceCDDMock.getNegativeDConeDecisionClassDistribution(2)).thenReturn(coneDecisionClassDistribution3);
		when(this.coneDecisionClassDistribution3.getDecisions()).thenReturn(this.decisionClassDistributionSet3);
		when(this.decisionClassDistributionSet3.iterator()).thenReturn(decisionClassDistributionIterator3);
		when(this.decisionClassDistributionIterator3.hasNext()).thenReturn(true, true, false);
		when(this.decisionClassDistributionIterator3.next()).thenReturn(class1, class2);
	}
	
	/**
	 * Sets up tests for for calculation of lower approximation of at most union.
	 */
	void setUpAtMostUnionForCalculationOfLowerApproximation() {
		MockitoAnnotations.initMocks(this);
		// mock union
		when(this.unionMock.getUnionType()).thenReturn(UnionType.AT_MOST);
		when(this.unionMock.isDecisionNegative(class1)).thenReturn(false);
		when(this.unionMock.isDecisionNegative(class2)).thenReturn(true);
		// mock union objects iterator
		when(this.unionMock.getObjects()).thenReturn(this.unionObjectsMock);
		when(this.unionMock.getObjects().iterator()).thenReturn(this.unionObjectIndicesIteratorMock);
		when(this.unionObjectIndicesIteratorMock.hasNext()).thenReturn(true, true, false);
		when(this.unionObjectIndicesIteratorMock.nextInt()).thenReturn(0, 2);
		// mock information table
		when(this.unionMock.getInformationTable()).thenReturn(this.infromationTableMock);
		when(this.infromationTableMock.getDominanceConesDecisionDistributions()).thenReturn(this.dominanceCDDMock);
		// mock class distribution in negative cones based on consistent object 1 and inconsistent object 3	
		when(this.dominanceCDDMock.getNegativeDConeDecisionClassDistribution(0)).thenReturn(coneDecisionClassDistribution1);
		when(this.coneDecisionClassDistribution1.getDecisions()).thenReturn(this.decisionClassDistributionSet1);
		when(this.decisionClassDistributionSet1.iterator()).thenReturn(decisionClassDistributionIterator1);
		when(this.decisionClassDistributionIterator1.hasNext()).thenReturn(true, false);
		when(this.decisionClassDistributionIterator1.next()).thenReturn(class1);
		when(this.dominanceCDDMock.getNegativeDConeDecisionClassDistribution(2)).thenReturn(coneDecisionClassDistribution3);
		when(this.coneDecisionClassDistribution3.getDecisions()).thenReturn(this.decisionClassDistributionSet3);
		when(this.decisionClassDistributionSet3.iterator()).thenReturn(decisionClassDistributionIterator3);
		when(this.decisionClassDistributionIterator3.hasNext()).thenReturn(true, true, false);
		when(this.decisionClassDistributionIterator3.next()).thenReturn(class1, class2);
	}
	
	/**
	 * Sets up tests for calculation of upper approximation of at least union.
	 */
	void setUpAtMostUnionForCalculationOfUpperApproximation() {
		MockitoAnnotations.initMocks(this);
		// mock union
		when(this.unionMock.getUnionType()).thenReturn(UnionType.AT_MOST);
		when(this.unionMock.isDecisionPositive(class1)).thenReturn(true);
		when(this.unionMock.isDecisionPositive(class2)).thenReturn(false);
		// mock information table
		when(this.unionMock.getInformationTable()).thenReturn(this.infromationTableMock);
		when(this.infromationTableMock.getNumberOfObjects()).thenReturn(3);
		when(this.infromationTableMock.getDominanceConesDecisionDistributions()).thenReturn(this.dominanceCDDMock);
		// mock class distribution in positive inverted cone based on inconsistent object 1
		when(this.dominanceCDDMock.getPositiveInvDConeDecisionClassDistribution(0)).thenReturn(coneDecisionClassDistribution1);
		when(this.coneDecisionClassDistribution1.getDecisions()).thenReturn(this.decisionClassDistributionSet1);
		when(this.decisionClassDistributionSet1.iterator()).thenReturn(decisionClassDistributionIterator1);
		when(this.decisionClassDistributionIterator1.hasNext()).thenReturn(true, true, false);
		when(this.decisionClassDistributionIterator1.next()).thenReturn(class1, class2);
		// mock class distribution in positive inverted cone based on inconsistent object 2
		when(this.dominanceCDDMock.getPositiveInvDConeDecisionClassDistribution(1)).thenReturn(coneDecisionClassDistribution2);
		when(this.coneDecisionClassDistribution2.getDecisions()).thenReturn(this.decisionClassDistributionSet2);
		when(this.decisionClassDistributionSet2.iterator()).thenReturn(decisionClassDistributionIterator2);
		when(this.decisionClassDistributionIterator2.hasNext()).thenReturn(true, true, false);
		when(this.decisionClassDistributionIterator2.next()).thenReturn(class1, class2);
		// mock class distribution in positive inverted cone based on inconsistent object 3
		when(this.dominanceCDDMock.getPositiveInvDConeDecisionClassDistribution(2)).thenReturn(coneDecisionClassDistribution3);
		when(this.coneDecisionClassDistribution3.getDecisions()).thenReturn(this.decisionClassDistributionSet3);
		when(this.decisionClassDistributionSet3.iterator()).thenReturn(decisionClassDistributionIterator3);
		when(this.decisionClassDistributionIterator3.hasNext()).thenReturn(true, false);
		when(this.decisionClassDistributionIterator3.next()).thenReturn(class1);
	}
	
	/**
	 * Test for constructor {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#ClassicalDominanceBasedRoughSetCalculator()}, and
	 * for constructor {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#ClassicalDominanceBasedRoughSetCalculator(boolean)}.
	 */
	@Test
	void testConstruction() {
		assertTrue(cDRSAcalculator.areDominanceRelationsReflexive());
		this.cDRSAcalculator = new ClassicalDominanceBasedRoughSetCalculator(false);
		assertFalse(cDRSAcalculator.areDominanceRelationsReflexive());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateLowerApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateLowerApproximationForAtLeastUnion() {
		this.setUpAtLeastUnionForCalculationOfLowerApproximation();
		assertEquals(0, cDRSAcalculator.calculateLowerApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateUpperApproximationForAtLeastUnion01() {
		this.setUpAtLeastUnionForCalculationOfUpperApproximation();
		assertEquals(2, cDRSAcalculator.calculateUpperApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateUpperApproximationForAtLeastUnion02() {
		this.setUpAtLeastUnionForCalculationOfUpperApproximation();
		IntSortedSet upperApproximationIndices = cDRSAcalculator.calculateUpperApproximation(this.unionMock);
		assertTrue(upperApproximationIndices.contains(1));
		assertTrue(upperApproximationIndices.contains(2));
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateLowerApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateLowerApproximationForAtMostUnion01() {
		this.setUpAtMostUnionForCalculationOfLowerApproximation();
		assertEquals(1, cDRSAcalculator.calculateLowerApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateLowerApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateLowerApproximationForAtMostUnion02() {
		this.setUpAtMostUnionForCalculationOfLowerApproximation();
		assertTrue(cDRSAcalculator.calculateLowerApproximation(this.unionMock).contains(0));
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateUpperApproximationForAtMostUnion01() {
		this.setUpAtMostUnionForCalculationOfUpperApproximation();
		assertEquals(3, cDRSAcalculator.calculateUpperApproximation(this.unionMock).size());
	}
	
	/**
	 * Test for method {@link org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator#calculateUpperApproximation(Union)}.
	 * 
	 * Test at this point is only performed for one decision class.
	 */
	@Test
	void testCalculateUpperApproximationForAtMostUnion02() {
		this.setUpAtMostUnionForCalculationOfUpperApproximation();
		IntSortedSet upperApproximationIndices = cDRSAcalculator.calculateUpperApproximation(this.unionMock);
		assertTrue(upperApproximationIndices.contains(0));
		assertTrue(upperApproximationIndices.contains(1));
		assertTrue(upperApproximationIndices.contains(2));
	}
}
