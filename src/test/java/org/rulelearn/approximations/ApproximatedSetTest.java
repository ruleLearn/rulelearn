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
import org.mockito.Mockito;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;

import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

import static org.mockito.Mockito.*;

import java.util.Collection;

/**
 * Test for {@link ApproximatedSet} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ApproximatedSetTest {
	
	/**
	 * Mock of {@link InformationTable}, injected in the constructor of {@link ApproximatedSet}.
	 */
	private InformationTable informationTableMock;
	/**
	 * Mock of {@link Decision}, injected in the constructor of {@link ApproximatedSet}.
	 */
	private Decision limitingDecisionMock;
	/**
	 * Mock of {@link RoughSetCalculator}, injected in the constructor of {@link ApproximatedSet}.
	 */
	private RoughSetCalculator<? extends ApproximatedSet> roughSetCalculatorMock;
	/**
	 * Mock of {@link ApproximatedSet}, used to provide an ad hoc implementation of abstract methods.
	 * Each invocation of an abstract method on {@link #approximatedSet} results in invocation of that method on {@link #approximatedSetMock}.
	 * In other words, {@link #approximatedSet} implements each abstract method of {@link ApproximatedSet} class by calling respective method on
	 * {@link #approximatedSetMock}.
	 */
	private ApproximatedSet approximatedSetMock;
	
	/**
	 * Instance of tested class, initialized in each testing method.
	 */
	private ApproximatedSet approximatedSet;
	
	/**
	 * Initializes all mocks before each unit test.
	 */
	@BeforeEach
	@SuppressWarnings("unchecked")
	private void setup() {
		this.informationTableMock = mock(InformationTable.class);
		this.limitingDecisionMock = mock(Decision.class);
		this.roughSetCalculatorMock = (RoughSetCalculator<ApproximatedSet>)mock(RoughSetCalculator.class);
		
		this.approximatedSetMock = mock(ApproximatedSet.class);
	}
	
	/**
	 * Initializes {@link #approximatedSet} by calling {@link ApproximatedSet} constructor using mocks: {@link #informationTableMock},
	 * {@link #limitingDecisionMock}, and {@link #roughSetCalculatorMock}. Abstract methods of {@link ApproximatedSet} are implemented using calls
	 * to respective methods of {@link #approximatedSetMock} (which, obviously, will work if first recorded on mock).
	 */
	private void createApproximatedSet() {
		this.approximatedSet = new ApproximatedSet(informationTableMock, limitingDecisionMock, roughSetCalculatorMock) {
			@Override
			protected TernaryLogicValue isConcordantWithDecision(Decision decision) {
				return approximatedSetMock.isConcordantWithDecision(decision);
			}
			
			@Override
			public IntSortedSet getObjects() {
				return approximatedSetMock.getObjects();
			}
			
			@Override
			public int getComplementarySetSize() {
				return approximatedSetMock.getComplementarySetSize();
			}
			
			@Override
			protected IntSortedSet calculateUpperApproximation() {
				return approximatedSetMock.calculateUpperApproximation();
			}
			
			@Override
			protected IntSet calculatePositiveRegion(IntSortedSet lowerApproximation) {
				return approximatedSetMock.calculatePositiveRegion(lowerApproximation);
			}
			
			@Override
			protected IntSet calculateNegativeRegion() {
				return approximatedSetMock.calculateNegativeRegion();
			}
			
			@Override
			protected IntSortedSet calculateLowerApproximation() {
				return approximatedSetMock.calculateLowerApproximation();
			}
		};
	}
	
	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getInformationTable()}.
	 */
	@Test
	void testGetInformationTable() {
		createApproximatedSet();
		assertSame(this.approximatedSet.getInformationTable(), this.informationTableMock);
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getLimitingDecision()}.
	 */
	@Test
	void testGetLimitingDecision() {
		createApproximatedSet();
		assertSame(this.approximatedSet.getLimitingDecision(), this.limitingDecisionMock);
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getRoughSetCalculator()}.
	 */
	@Test
	void testGetRoughSetCalculator() {
		createApproximatedSet();
		assertSame(this.approximatedSet.getRoughSetCalculator(), this.roughSetCalculatorMock);
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getLowerApproximation()}.
	 */
	@Test
	void testGetLowerApproximation() {
		createApproximatedSet();
		
		//record behavior of approximated set mock
		IntSortedSet expectedLowerApproximation = new IntLinkedOpenHashSet();
		expectedLowerApproximation.add(1);
		expectedLowerApproximation.add(5);
		expectedLowerApproximation.add(7);
		when(this.approximatedSetMock.calculateLowerApproximation()).thenReturn(expectedLowerApproximation);
		
		IntSortedSet lowerApproximation = this.approximatedSet.getLowerApproximation();
		assertEquals(lowerApproximation.size(), expectedLowerApproximation.size());
		assertEquals(lowerApproximation, expectedLowerApproximation); //performs value comparison
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getUpperApproximation()}.
	 */
	@Test
	void testGetUpperApproximation() {
		createApproximatedSet();
		
		//record behavior of approximated set mock
		IntSortedSet expectedUpperApproximation = new IntLinkedOpenHashSet();
		expectedUpperApproximation.add(2);
		expectedUpperApproximation.add(4);
		expectedUpperApproximation.add(9);
		when(this.approximatedSetMock.calculateUpperApproximation()).thenReturn(expectedUpperApproximation);
		
		IntSortedSet upperApproximation = this.approximatedSet.getUpperApproximation();
		assertEquals(upperApproximation.size(), expectedUpperApproximation.size());
		assertEquals(upperApproximation, expectedUpperApproximation); //performs value comparison
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getBoundary()}.
	 */
	@Test
	void testGetBoundary() {
		createApproximatedSet();
		
		//record behavior of approximated set mock
		IntSortedSet expectedLowerApproximation = new IntLinkedOpenHashSet();
		expectedLowerApproximation.add(1);
		expectedLowerApproximation.add(5);
		expectedLowerApproximation.add(7);
		when(this.approximatedSetMock.calculateLowerApproximation()).thenReturn(expectedLowerApproximation);
		//
		IntSortedSet expectedUpperApproximation = new IntLinkedOpenHashSet();
		expectedUpperApproximation.add(1);
		expectedUpperApproximation.add(2);
		expectedUpperApproximation.add(5);
		expectedUpperApproximation.add(6);
		expectedUpperApproximation.add(7);
		expectedUpperApproximation.add(19);
		expectedUpperApproximation.add(23);
		when(this.approximatedSetMock.calculateUpperApproximation()).thenReturn(expectedUpperApproximation);
		
		//define expected result
		IntSortedSet expectedBoundary = new IntLinkedOpenHashSet();
		expectedBoundary.add(2);
		expectedBoundary.add(6);
		expectedBoundary.add(19);
		expectedBoundary.add(23);
		
		IntSortedSet boundary = this.approximatedSet.getBoundary();
		assertEquals(boundary.size(), expectedBoundary.size());
		assertEquals(boundary, expectedBoundary); //performs value comparison
	}
	
	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getBoundary()}.
	 */
	@Test
	void testGetBoundary_02() {
		//record behavior of approximated set mock
		IntSortedSet expectedLowerApproximation = new IntLinkedOpenHashSet();
		expectedLowerApproximation.add(1);
		expectedLowerApproximation.add(7);
		expectedLowerApproximation.add(8);
		when(this.approximatedSetMock.getLowerApproximation()).thenReturn(expectedLowerApproximation);
		//
		IntSortedSet expectedUpperApproximation = new IntLinkedOpenHashSet();
		expectedUpperApproximation.add(1);
		expectedUpperApproximation.add(3);
		expectedUpperApproximation.add(7);
		expectedUpperApproximation.add(8);
		expectedUpperApproximation.add(19);
		when(this.approximatedSetMock.getUpperApproximation()).thenReturn(expectedUpperApproximation);
		
		when(this.approximatedSetMock.getBoundary()).thenCallRealMethod();
		
		//define expected result
		IntSortedSet expectedBoundary = new IntLinkedOpenHashSet();
		expectedBoundary.add(3);
		expectedBoundary.add(19);
		
		IntSortedSet boundary = this.approximatedSetMock.getBoundary();
		assertEquals(boundary.size(), expectedBoundary.size());
		assertEquals(boundary, expectedBoundary); //performs value comparison
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getPositiveRegion()}.
	 */
	@Test
	void testGetPositiveRegion() {
		createApproximatedSet();
		
		//record behavior of approximated set mock
		IntSortedSet expectedLowerApproximation = new IntLinkedOpenHashSet();
		expectedLowerApproximation.add(1);
		expectedLowerApproximation.add(5);
		expectedLowerApproximation.add(7);
		when(this.approximatedSetMock.calculateLowerApproximation()).thenReturn(expectedLowerApproximation);
		//
		IntSet expectedPositiveRegion = new IntOpenHashSet();
		expectedPositiveRegion.add(1);
		expectedPositiveRegion.add(13);
		expectedPositiveRegion.add(5);
		expectedPositiveRegion.add(7);
		expectedPositiveRegion.add(12);
		expectedPositiveRegion.add(4);
		//expectedLowerApproximation is fine below as actual method parameter, since it is equal to unmodifiable set
		//made of expectedLowerApproximation returned by ApproximatedSet.getLowerApproximation
		when(this.approximatedSetMock.calculatePositiveRegion(expectedLowerApproximation)).thenReturn(expectedPositiveRegion);
		
		IntSet positiveRegion = this.approximatedSet.getPositiveRegion();
		assertEquals(positiveRegion.size(), expectedPositiveRegion.size());
		assertEquals(positiveRegion, expectedPositiveRegion); //performs value comparison
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getNegativeRegion()}.
	 */
	@Test
	void testGetNegativeRegion() {
		createApproximatedSet();
		
		//record behavior of approximated set mock
		IntSet expectedNegativeRegion = new IntOpenHashSet();
		expectedNegativeRegion.add(14);
		expectedNegativeRegion.add(2);
		expectedNegativeRegion.add(15);
		expectedNegativeRegion.add(9);
		when(this.approximatedSetMock.calculateNegativeRegion()).thenReturn(expectedNegativeRegion);
		
		IntSet negativeRegion = this.approximatedSet.getNegativeRegion();
		assertEquals(negativeRegion.size(), expectedNegativeRegion.size());
		assertEquals(negativeRegion, expectedNegativeRegion); //performs value comparison
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getBoundaryRegion()}.
	 */
	@Test
	void testGetBoundaryRegion() {
		createApproximatedSet();
		
		//record behavior of approximated set mock
		IntSortedSet expectedLowerApproximation = new IntLinkedOpenHashSet();
		expectedLowerApproximation.add(1);
		expectedLowerApproximation.add(5);
		expectedLowerApproximation.add(7);
		when(this.approximatedSetMock.calculateLowerApproximation()).thenReturn(expectedLowerApproximation);
		//
		IntSet expectedPositiveRegion = new IntOpenHashSet();
		expectedPositiveRegion.add(1);
		expectedPositiveRegion.add(13);
		expectedPositiveRegion.add(5);
		expectedPositiveRegion.add(7);
		expectedPositiveRegion.add(12);
		expectedPositiveRegion.add(4);
		//expectedLowerApproximation is fine below as actual method parameter, since it is equal to unmodifiable set
		//made of expectedLowerApproximation returned by ApproximatedSet.getLowerApproximation
		when(this.approximatedSetMock.calculatePositiveRegion(expectedLowerApproximation)).thenReturn(expectedPositiveRegion);
		//
		IntSet expectedNegativeRegion = new IntOpenHashSet();
		expectedNegativeRegion.add(14);
		expectedNegativeRegion.add(2);
		expectedNegativeRegion.add(15);
		expectedNegativeRegion.add(9);
		when(this.approximatedSetMock.calculateNegativeRegion()).thenReturn(expectedNegativeRegion);
		
		//record behavior of information table mock
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(16);
		
		//define expected result
		IntSet expectedBoundaryRegion = new IntOpenHashSet();
		expectedBoundaryRegion.add(0);
		expectedBoundaryRegion.add(3);
		expectedBoundaryRegion.add(6);
		expectedBoundaryRegion.add(8);
		expectedBoundaryRegion.add(10);
		expectedBoundaryRegion.add(11);
		
		IntSet boundaryRegion = this.approximatedSet.getBoundaryRegion();
		assertEquals(boundaryRegion.size(), expectedBoundaryRegion.size());
		assertEquals(boundaryRegion, expectedBoundaryRegion); //performs value comparison
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getAccuracyOfApproximation()}.
	 */
	@Test
	void testGetAccuracyOfApproximation() {
		createApproximatedSet();
		
		//record behavior of approximated set mock
		IntSortedSet expectedLowerApproximation = new IntLinkedOpenHashSet();
		expectedLowerApproximation.add(1);
		expectedLowerApproximation.add(5);
		expectedLowerApproximation.add(7);
		when(this.approximatedSetMock.calculateLowerApproximation()).thenReturn(expectedLowerApproximation);
		//
		IntSortedSet expectedUpperApproximation = new IntLinkedOpenHashSet();
		expectedUpperApproximation.add(1);
		expectedUpperApproximation.add(3);
		expectedUpperApproximation.add(5);
		expectedUpperApproximation.add(7);
		expectedUpperApproximation.add(12);
		expectedUpperApproximation.add(14);
		when(this.approximatedSetMock.calculateUpperApproximation()).thenReturn(expectedUpperApproximation);
		
		assertEquals(this.approximatedSet.getAccuracyOfApproximation(), (double)expectedLowerApproximation.size() / (double)expectedUpperApproximation.size());
	}

//	/**
//	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#getQualityOfApproximation()}.
//	 */
//	@Test
//	void testGetQualityOfApproximation() {
//		createApproximatedSet();
//		
//		//record behavior of approximated set mock
//		IntSortedSet expectedLowerApproximation = new IntLinkedOpenHashSet();
//		expectedLowerApproximation.add(1);
//		expectedLowerApproximation.add(5);
//		expectedLowerApproximation.add(7);
//		when(this.approximatedSetMock.calculateLowerApproximation()).thenReturn(expectedLowerApproximation);
//		
//		fail("Not yet implemented"); // TODO
//		//assertEquals(this.approximatedSet.getQualityOfApproximation(), (double)expectedLowerApproximation.size() / 10); //TODO: correct denominator!
//	}
//
//	/**
//	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#size()}.
//	 */
//	@Test
//	void testSize() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link org.rulelearn.approximations.ApproximatedSet#contains(int)}.
//	 */
//	@Test
//	void testContains() {
//		fail("Not yet implemented"); // TODO
//	}

}
