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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.InformationTable;

/**
 * Tests for {@link ApproximatedSets}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ApproximatedSetsTest {
	
	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSets#ApproximatedSets(org.rulelearn.data.InformationTable, org.rulelearn.approximations.RoughSetCalculator)}.
	 * Tests if {@link NullPointerException} is thrown when one of the constructor parameters is {@code null}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	void testApproximatedSets01() {
		InformationTable informationTableMock = null;
		RoughSetCalculator<? extends ApproximatedSet> roughSetCalculatorMock = (RoughSetCalculator<ApproximatedSet>)Mockito.mock(RoughSetCalculator.class);
		
		assertThrows(NullPointerException.class, () -> {
			new ApproximatedSets(informationTableMock, roughSetCalculatorMock) {
				public double getQualityOfApproximation() {
					return -1;
				}

				@Override
				public int getNumberOfConsistentObjects() {
					return 0;
				}
			};
		});
	}
	
	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSets#ApproximatedSets(org.rulelearn.data.InformationTable, org.rulelearn.approximations.RoughSetCalculator)}.
	 * Tests if {@link NullPointerException} is thrown when one of the constructor parameters is {@code null}.
	 */
	@Test
	void testApproximatedSets02() {
		//InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		//RoughSetCalculator<? extends ApproximatedSet> roughSetCalculatorMock = Mockito.mock(RoughSetCalculator.class);
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		RoughSetCalculator<? extends ApproximatedSet> roughSetCalculatorMock = null;
		
		assertThrows(NullPointerException.class, () -> {
			new ApproximatedSets(informationTableMock, roughSetCalculatorMock) {
				public double getQualityOfApproximation() {
					return -1;
				}
				
				@Override
				public int getNumberOfConsistentObjects() {
					return 0;
				}
			};
		}); 
	}
	
	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSets#ApproximatedSets(org.rulelearn.data.InformationTable, org.rulelearn.approximations.RoughSetCalculator)}.
	 * Tests if object is constructed when parameters are not {@code null}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	void testApproximatedSets03() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		RoughSetCalculator<? extends ApproximatedSet> roughSetCalculatorMock = (RoughSetCalculator<ApproximatedSet>)Mockito.mock(RoughSetCalculator.class);
		
		try {
			new ApproximatedSets(informationTableMock, roughSetCalculatorMock) {
				public double getQualityOfApproximation() {
					return -1;
				}
				
				@Override
				public int getNumberOfConsistentObjects() {
					return 0;
				}
			};
		} catch (Throwable throwable) {
			fail("Cannot construct approximated sets.");
		}
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSets#getInformationTable()}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	void testGetInformationTable() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		RoughSetCalculator<? extends ApproximatedSet> roughSetCalculatorMock = (RoughSetCalculator<ApproximatedSet>)Mockito.mock(RoughSetCalculator.class);
		
		ApproximatedSets approximatedSets = new ApproximatedSets(informationTableMock, roughSetCalculatorMock) {
			public double getQualityOfApproximation() {
				return -1;
			}
			
			@Override
			public int getNumberOfConsistentObjects() {
				return 0;
			}
		};
		
		assertEquals(approximatedSets.getInformationTable(), informationTableMock);
	}

	/**
	 * Test method for {@link org.rulelearn.approximations.ApproximatedSets#getRoughSetCalculator()}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	void testGetRoughSetCalculator() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		RoughSetCalculator<? extends ApproximatedSet> roughSetCalculatorMock = (RoughSetCalculator<ApproximatedSet>)Mockito.mock(RoughSetCalculator.class);
		
		ApproximatedSets approximatedSets = new ApproximatedSets(informationTableMock, roughSetCalculatorMock) {
			public double getQualityOfApproximation() {
				return -1;
			}
			
			@Override
			public int getNumberOfConsistentObjects() {
				return 0;
			}
		};
		
		assertEquals(approximatedSets.getRoughSetCalculator(), roughSetCalculatorMock);
	}

}
