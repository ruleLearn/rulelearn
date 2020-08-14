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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Tests for {@link Unions} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnionsTest {
	
	/**
	 * Test method for {@link Unions#getInformationTable()}.
	 */
	@Test
	void testGetInformationTable() {
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		Unions unions = new Unions(informationTableMock, roughSetCalculatorMock) {
			@Override
			void calculateUpwardUnions() {
				throw new UnsupportedOperationException("Calculation of upward unions should not be requested during test.");
			}
			
			@Override
			void calculateDownwardUnions() {
				throw new UnsupportedOperationException("Calculation of downward unions should not be requested during test.");
			}
		};
		
		assertEquals(unions.getInformationTable(), informationTableMock);
	}
	
	/**
	 * Test method for {@link Unions#getRoughSetCalculator()}.
	 */
	@Test
	void testGetRoughSetCalculator() {
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		Unions unions = new Unions(informationTableMock, roughSetCalculatorMock) {
			@Override
			void calculateUpwardUnions() {
				throw new UnsupportedOperationException("Calculation of upward unions should not be requested during test.");
			}
			
			@Override
			void calculateDownwardUnions() {
				throw new UnsupportedOperationException("Calculation of downward unions should not be requested during test.");
			}
		};
		
		assertEquals(unions.getRoughSetCalculator(), roughSetCalculatorMock);
	}
	
	/**
	 * Test method for {@link Unions#getQualityOfApproximation()}.
	 * Tests Symptoms data set composed of 17 objects from classes 0-2, with class 2 being the best class and class 0 being the worst class.
	 * Three objects from class 2 and two objects from class 1 are inconsistent w.r.t. dominance principle.
	 */
	@Test
	void testGetQualityOfApproximation01() {
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(17);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		//union >=2
		Union unionGE2 = Mockito.mock(Union.class);
		IntSortedSet boundaryGE2 = new IntLinkedOpenHashSet();
		boundaryGE2.add(4);
		boundaryGE2.add(5);
		boundaryGE2.add(6);
		boundaryGE2.add(7);
		boundaryGE2.add(8);
		Mockito.when(unionGE2.getBoundary()).thenReturn(boundaryGE2);
		
		//union >=1
		Union unionGE1 = Mockito.mock(Union.class);
		IntSortedSet boundaryGE1 = new IntLinkedOpenHashSet();
		Mockito.when(unionGE1.getBoundary()).thenReturn(boundaryGE1);
		
		//union <=0
		Union unionLE0 = Mockito.mock(Union.class);
		IntSortedSet boundaryLE0 = new IntLinkedOpenHashSet();
		Mockito.when(unionLE0.getBoundary()).thenReturn(boundaryLE0);
		
		//union <=1
		Union unionLE1 = Mockito.mock(Union.class);
		IntSortedSet boundaryLE1 = new IntLinkedOpenHashSet();
		boundaryGE2.add(4);
		boundaryGE2.add(5);
		boundaryGE2.add(6);
		boundaryGE2.add(7);
		boundaryGE2.add(8);
		Mockito.when(unionLE1.getBoundary()).thenReturn(boundaryLE1);
		
		Union[] upwardUnions = {unionGE2, unionGE1};
		Union[] downwardUnions = {unionLE0, unionLE1};
		
		Unions unions = new Unions(informationTableMock, roughSetCalculatorMock) {
			@Override
			void calculateUpwardUnions() {
				throw new UnsupportedOperationException("Calculation of upward unions should not be requested during test.");
				//upwardUnions property is set in this test case before calling getUpwardUnions, so calculateUpwardUnions will not be called
			}
			
			@Override
			void calculateDownwardUnions() {
				throw new UnsupportedOperationException("Calculation of downward unions should not be requested during test.");
				//downwardUnions property is set in this test case before calling getDownwardUnions, so calculateDownwardUnions will not be called
			}
		};
		
		unions.upwardUnions = upwardUnions;
		unions.downwardUnions = downwardUnions;
		
		double expectedQualityOfApproximation = (double)12 / (double)17;
		assertEquals(unions.getQualityOfApproximation(), expectedQualityOfApproximation);
	}
	
	/**
	 * Test method for {@link Unions#getQualityOfApproximation()}.
	 * Tests modified Symptoms data set composed of 17 objects from classes 0-2, with class 2 being the best class and class 0 being the worst class.
	 * Three objects from class 2, four objects from class 1, and two object from class 0 are inconsistent w.r.t. dominance principle.
	 * Differences w.r.t. original Symptoms data:
	 * <ul>
	 * <li>object with index 8 changes class from 1 to 0,</li>
	 * <li>object with index 9 changes class from 1 to 0,</li>
	 * <li>object with index 10 changes class from 1 to 0,</li>
	 * <li>object with index 13 changes class from 0 to 1,</li>
	 * <li>object with index 15 changes class from 0 to 1.</li>
	 * </ul> 
	 */
	@Test
	void testGetQualityOfApproximation02() {
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(17);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		//union >=2
		Union unionGE2 = Mockito.mock(Union.class);
		IntSortedSet boundaryGE2 = new IntLinkedOpenHashSet();
		boundaryGE2.add(4);
		boundaryGE2.add(5);
		boundaryGE2.add(6);
		boundaryGE2.add(7);
		boundaryGE2.add(8);
		Mockito.when(unionGE2.getBoundary()).thenReturn(boundaryGE2);
		
		//union >=1
		Union unionGE1 = Mockito.mock(Union.class);
		IntSortedSet boundaryGE1 = new IntLinkedOpenHashSet();
		boundaryGE1.add(5);
		boundaryGE1.add(6);
		boundaryGE1.add(8); //again!
		boundaryGE1.add(9);
		boundaryGE1.add(10);
		boundaryGE1.add(12);
		boundaryGE1.add(13);
		boundaryGE1.add(15);
		Mockito.when(unionGE1.getBoundary()).thenReturn(boundaryGE1);
		
		//union <=0
		Union unionLE0 = Mockito.mock(Union.class);
		IntSortedSet boundaryLE0 = new IntLinkedOpenHashSet();
		boundaryLE0.add(5);
		boundaryLE0.add(6);
		boundaryLE0.add(8);
		boundaryLE0.add(9);
		boundaryLE0.add(10);
		boundaryLE0.add(12);
		boundaryLE0.add(13);
		boundaryLE0.add(15);
		Mockito.when(unionLE0.getBoundary()).thenReturn(boundaryLE0);
		
		//union <=1
		Union unionLE1 = Mockito.mock(Union.class);
		IntSortedSet boundaryLE1 = new IntLinkedOpenHashSet();
		boundaryGE2.add(4);
		boundaryGE2.add(5);
		boundaryGE2.add(6);
		boundaryGE2.add(7);
		boundaryGE2.add(8); //again!
		Mockito.when(unionLE1.getBoundary()).thenReturn(boundaryLE1);
		
		Union[] upwardUnions = {unionGE2, unionGE1};
		Union[] downwardUnions = {unionLE0, unionLE1};
		
		Unions unions = new Unions(informationTableMock, roughSetCalculatorMock) {
			@Override
			void calculateUpwardUnions() {
				throw new UnsupportedOperationException("Calculation of upward unions should not be requested during test.");
				//upwardUnions property is set in this test case before calling getUpwardUnions, so calculateUpwardUnions will not be called
			}
			
			@Override
			void calculateDownwardUnions() {
				throw new UnsupportedOperationException("Calculation of downward unions should not be requested during test.");
				//downwardUnions property is set in this test case before calling getDownwardUnions, so calculateDownwardUnions will not be called
			}
		};
		
		unions.upwardUnions = upwardUnions;
		unions.downwardUnions = downwardUnions;
		
		double expectedQualityOfApproximation = (double)7 / (double)17;
		assertEquals(unions.getQualityOfApproximation(), expectedQualityOfApproximation);
	}
	
}
