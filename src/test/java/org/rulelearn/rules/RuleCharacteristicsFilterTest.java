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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test for {@link RuleCharacteristicsFilter} class.
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleCharacteristicsFilterTest {

	/**
	 * Test for {@link RuleCharacteristicsFilter#RuleCharacteristicsFilter(java.util.function.Function, org.rulelearn.rules.Relation, java.lang.Number)}.
	 * Tests if {@link NullPointerException} is thrown when a parameter is {@code null}.
	 */
	@Test
	void testRuleCharacteristicsFilter01() {
		assertThrows(NullPointerException.class, () -> new RuleCharacteristicsFilter((Function<RuleCharacteristics, Number>)null, "", Relation.GE, Mockito.mock(Number.class)));
	}
	
	/**
	 * Test for {@link RuleCharacteristicsFilter#RuleCharacteristicsFilter(java.util.function.Function, org.rulelearn.rules.Relation, java.lang.Number)}.
	 * Tests if {@link NullPointerException} is thrown when a parameter is {@code null}.
	 */
	@Test
	void testRuleCharacteristicsFilter02() {
		assertThrows(NullPointerException.class, () -> new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, "support", null, Mockito.mock(Number.class)));
	}
	
	/**
	 * Test for {@link RuleCharacteristicsFilter#RuleCharacteristicsFilter(java.util.function.Function, org.rulelearn.rules.Relation, java.lang.Number)}.
	 * Tests if {@link NullPointerException} is thrown when a parameter is {@code null}.
	 */
	@Test
	void testRuleCharacteristicsFilter03() {
		assertThrows(NullPointerException.class, () -> new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, "support", Relation.GE, null));
	}

	/**
	 * Test for {@link RuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 */
	@Test
	void testAccepts01() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(10);
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 10).accepts(null, ruleCharacteristicsMock));
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 9).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 11).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GT, 9).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GT, 10).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.EQ, 10).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.EQ, 9).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.EQ, 11).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.LE, 10).accepts(null, ruleCharacteristicsMock));
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.LE, 11).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.LE, 9).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.LT, 11).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.LT, 10).accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link RuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 */
	@Test
	void testAccepts02() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.01);
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.GE, 0.01).accepts(null, ruleCharacteristicsMock));
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.GE, 0).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.GE, 0.02).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.GT, 0).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.GT, 0.01).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.EQ, 0.01).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.EQ, 0.02).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.EQ, 0).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LE, 0.01).accepts(null, ruleCharacteristicsMock));
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LE, 0.02).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LE, 0).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LT, 0.02).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LT, 0.01).accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link RuleCharacteristicsFilter#RuleCharacteristicsFilter(RuleCharacteristic, Relation, Number)} and
	 * {@link RuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 */
	@Test
	void testAccepts03() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.01);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(0.95);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(10);
		Mockito.when(ruleCharacteristicsMock.getCoverage()).thenReturn(12);
		Mockito.when(ruleCharacteristicsMock.getNegativeCoverage()).thenReturn(2);
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristic.EPSILON, Relation.GE, 0.01).accepts(null, ruleCharacteristicsMock));
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristic.EPSILON, Relation.GE, 0).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristic.EPSILON, Relation.GE, 0.02).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristic.CONFIDENCE, Relation.GT, 0.94).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristic.CONFIDENCE, Relation.GT, 0.95).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristic.SUPPORT, Relation.EQ, 10.0).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristic.SUPPORT, Relation.EQ, 9.0).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristic.SUPPORT, Relation.EQ, 11.0).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristic.COVERAGE, Relation.LE, 12).accepts(null, ruleCharacteristicsMock));
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristic.COVERAGE, Relation.LE, 13).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristic.COVERAGE, Relation.LE, 11).accepts(null, ruleCharacteristicsMock));
		
		assertTrue(new RuleCharacteristicsFilter(RuleCharacteristic.NEGATIVE_COVERAGE, Relation.LT, 3).accepts(null, ruleCharacteristicsMock));
		assertFalse(new RuleCharacteristicsFilter(RuleCharacteristic.NEGATIVE_COVERAGE, Relation.LT, 2).accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link RuleCharacteristicsFilter#of(String, String, String)} and
	 * {@link RuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 */
	@Test
	void testOf01() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.01);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(0.95);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(10);
		Mockito.when(ruleCharacteristicsMock.getCoverage()).thenReturn(12);
		Mockito.when(ruleCharacteristicsMock.getNegativeCoverage()).thenReturn(2);
		
		assertTrue(RuleCharacteristicsFilter.of(RuleCharacteristic.epsilon, Relation.ge, "0.01").accepts(null, ruleCharacteristicsMock));
		assertTrue(RuleCharacteristicsFilter.of(RuleCharacteristic.epsilon, Relation.ge, "0").accepts(null, ruleCharacteristicsMock));
		assertFalse(RuleCharacteristicsFilter.of(RuleCharacteristic.epsilon, Relation.ge, "0.02").accepts(null, ruleCharacteristicsMock));
		
		assertTrue(RuleCharacteristicsFilter.of(RuleCharacteristic.confidence, Relation.gt, "0.94").accepts(null, ruleCharacteristicsMock));
		assertFalse(RuleCharacteristicsFilter.of(RuleCharacteristic.confidence, Relation.gt, "0.95").accepts(null, ruleCharacteristicsMock));
		
		assertTrue(RuleCharacteristicsFilter.of(RuleCharacteristic.support, Relation.eq, "10.0").accepts(null, ruleCharacteristicsMock));
		assertFalse(RuleCharacteristicsFilter.of(RuleCharacteristic.support, Relation.eq, "9.0").accepts(null, ruleCharacteristicsMock));
		assertFalse(RuleCharacteristicsFilter.of(RuleCharacteristic.support, Relation.eq, "11.0").accepts(null, ruleCharacteristicsMock));
		
		assertTrue(RuleCharacteristicsFilter.of(RuleCharacteristic.coverage, Relation.le, "12").accepts(null, ruleCharacteristicsMock));
		assertTrue(RuleCharacteristicsFilter.of(RuleCharacteristic.coverage, Relation.le, "13").accepts(null, ruleCharacteristicsMock));
		assertFalse(RuleCharacteristicsFilter.of(RuleCharacteristic.coverage, Relation.le, "11").accepts(null, ruleCharacteristicsMock));
		
		assertTrue(RuleCharacteristicsFilter.of(RuleCharacteristic.negativeCoverage, Relation.lt, "3.0").accepts(null, ruleCharacteristicsMock));
		assertFalse(RuleCharacteristicsFilter.of(RuleCharacteristic.negativeCoverage, Relation.lt, "2.0").accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link RuleCharacteristicsFilter#of(String)} and
	 * {@link RuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 */
	@Test
	void testOf02() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.01);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(0.95);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(10);
		Mockito.when(ruleCharacteristicsMock.getCoverage()).thenReturn(12);
		Mockito.when(ruleCharacteristicsMock.getNegativeCoverage()).thenReturn(2);
		
		assertTrue(RuleCharacteristicsFilter.of("epsilon>=0.01").accepts(null, ruleCharacteristicsMock));
		assertTrue(RuleCharacteristicsFilter.of("confidence>0.94").accepts(null, ruleCharacteristicsMock));
		assertTrue(RuleCharacteristicsFilter.of("support=10.0").accepts(null, ruleCharacteristicsMock));
		assertTrue(RuleCharacteristicsFilter.of("coverage<=12").accepts(null, ruleCharacteristicsMock));
		assertTrue(RuleCharacteristicsFilter.of("negative-coverage<3").accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link RuleCharacteristicsFilter#toString()}.
	 */
	@Test
	void testToString() {
		assertEquals(RuleCharacteristicsFilter.of(RuleCharacteristic.support, Relation.gt, "10.0").toString(), "support>10"); //tests if support is stored as int, despite written as double
		assertEquals(RuleCharacteristicsFilter.of(RuleCharacteristic.strength, Relation.ge, "0.04").toString(), "strength>=0.04");
		
		assertEquals(RuleCharacteristicsFilter.of(RuleCharacteristic.coverageFactor, Relation.eq, "0.03").toString(), "coverage-factor=0.03");
		assertEquals(RuleCharacteristicsFilter.of(RuleCharacteristic.coverage, Relation.le, "12").toString(), "coverage<=12");
		assertEquals(RuleCharacteristicsFilter.of(RuleCharacteristic.epsilon, Relation.lt, "0.01").toString(), "epsilon<0.01");
	}

}
