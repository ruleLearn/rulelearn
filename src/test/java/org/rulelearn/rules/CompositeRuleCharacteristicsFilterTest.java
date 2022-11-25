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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link CompositeRuleCharacteristicsFilter} class.
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CompositeRuleCharacteristicsFilterTest {

	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#CompositeRuleCharacteristicsFilter(List)}.
	 * Tests if {@link NullPointerException} is thrown if constructor parameter is {@code null}.
	 */
	@Test
	void testCompositeRuleFilter01() {
		assertThrows(NullPointerException.class, () -> {
			new CompositeRuleCharacteristicsFilter(null);
		});
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 * Tests the case with two accepting base filters.
	 */
	@Test
	void testAccepts01() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(2);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(1.0);
		
		ArrayList<RuleCharacteristicsFilter> ruleCharacteristicsFilters = new ArrayList<RuleCharacteristicsFilter>(2);
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 2));
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getConfidence, RuleCharacteristic.CONFIDENCE.getName(), Relation.EQ, 1.0));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(ruleCharacteristicsFilters);
		
		assertTrue(compositeRuleFilter.accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 * Tests the case with two base filters, one of which does not accept rule characteristics.
	 */
	@Test
	void testAccepts02() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(1);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(1.0);
		
		ArrayList<RuleCharacteristicsFilter> ruleCharacteristicsFilters = new ArrayList<RuleCharacteristicsFilter>(2);
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 2));
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getConfidence, RuleCharacteristic.CONFIDENCE.getName(), Relation.EQ, 1.0));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(ruleCharacteristicsFilters);
		
		assertFalse(compositeRuleFilter.accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 * Tests the case with three base filters, each accepting rule characteristics.
	 */
	@Test
	void testAccepts03() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(2);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(0.95);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.02);
		
		ArrayList<RuleCharacteristicsFilter> ruleCharacteristicsFilters = new ArrayList<RuleCharacteristicsFilter>(3);
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 2));
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getConfidence, RuleCharacteristic.CONFIDENCE.getName(), Relation.EQ, 0.95));
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LE, 0.02));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(ruleCharacteristicsFilters);
		
		assertTrue(compositeRuleFilter.accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 * Tests the case with three base filters, one not accepting rule characteristics.
	 */
	@Test
	void testAccepts04() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(2);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(0.95);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.02);
		
		ArrayList<RuleCharacteristicsFilter> ruleCharacteristicsFilters = new ArrayList<RuleCharacteristicsFilter>(3);
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 2));
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getConfidence, RuleCharacteristic.CONFIDENCE.getName(), Relation.EQ, 0.95));
		ruleCharacteristicsFilters.add(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LE, 0.01));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(ruleCharacteristicsFilters);
		
		assertFalse(compositeRuleFilter.accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 * Tests the case with three base filters constructed from text, all accepting rule characteristics.
	 */
	@Test
	void testAccepts05() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(2);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(0.95);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.02);
		
		ArrayList<RuleCharacteristicsFilter> ruleCharacteristicsFilters = new ArrayList<RuleCharacteristicsFilter>(3);
		ruleCharacteristicsFilters.add(RuleCharacteristicsFilter.of("support", ">=", "2"));
		ruleCharacteristicsFilters.add(RuleCharacteristicsFilter.of("confidence", "=", "0.95"));
		ruleCharacteristicsFilters.add(RuleCharacteristicsFilter.of("epsilon", "<=", "0.02"));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(ruleCharacteristicsFilters);
		
		assertTrue(compositeRuleFilter.accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#accepts(Rule, RuleCharacteristics)}.
	 * Tests the case with three base filters constructed from text, one not accepting rule characteristics.
	 */
	@Test
	void testAccepts06() {
		RuleCharacteristics ruleCharacteristicsMock = Mockito.mock(RuleCharacteristics.class);
		Mockito.when(ruleCharacteristicsMock.getSupport()).thenReturn(2);
		Mockito.when(ruleCharacteristicsMock.getConfidence()).thenReturn(0.95);
		Mockito.when(ruleCharacteristicsMock.getEpsilon()).thenReturn(0.02);
		
		ArrayList<RuleCharacteristicsFilter> ruleCharacteristicsFilters = new ArrayList<RuleCharacteristicsFilter>(3);
		ruleCharacteristicsFilters.add(RuleCharacteristicsFilter.of("support", ">=", "2"));
		ruleCharacteristicsFilters.add(RuleCharacteristicsFilter.of("confidence", "=", "0.95"));
		ruleCharacteristicsFilters.add(RuleCharacteristicsFilter.of("epsilon", "<=", "0.01"));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(ruleCharacteristicsFilters);
		
		assertFalse(compositeRuleFilter.accepts(null, ruleCharacteristicsMock));
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#getRuleCharacteristicsFilters()}.
	 * Tests the order of filters in the returned copy of internal list of filters.
	 */
	@Test
	void testGetRuleCharacteristicsFilters() {
		ArrayList<RuleCharacteristicsFilter> originalFilterList = new ArrayList<RuleCharacteristicsFilter>(3);
		originalFilterList.add(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 2));
		originalFilterList.add(new RuleCharacteristicsFilter(RuleCharacteristics::getConfidence, RuleCharacteristic.CONFIDENCE.getName(), Relation.EQ, 0.95));
		originalFilterList.add(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LE, 0.01));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(originalFilterList);
		
		List<RuleCharacteristicsFilter> returnedFilterList = compositeRuleFilter.getRuleCharacteristicsFilters();
		
		for (int i = 0; i < 3; i++) {
			assertEquals(returnedFilterList.get(i), originalFilterList.get(i));
		}
		assertNotSame(returnedFilterList, originalFilterList);
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#toString()}.
	 */
	@Test
	void testToString() {
		ArrayList<RuleCharacteristicsFilter> originalFilterList = new ArrayList<RuleCharacteristicsFilter>(3);
		originalFilterList.add(new RuleCharacteristicsFilter(RuleCharacteristics::getSupport, RuleCharacteristic.SUPPORT.getName(), Relation.GE, 2));
		originalFilterList.add(new RuleCharacteristicsFilter(RuleCharacteristics::getConfidence, RuleCharacteristic.CONFIDENCE.getName(), Relation.EQ, 0.95));
		originalFilterList.add(new RuleCharacteristicsFilter(RuleCharacteristics::getEpsilon, RuleCharacteristic.EPSILON.getName(), Relation.LE, 0.01));
		
		CompositeRuleCharacteristicsFilter compositeRuleFilter = new CompositeRuleCharacteristicsFilter(originalFilterList);
		
		String expected = originalFilterList.get(0).toString()+CompositeRuleCharacteristicsFilter.separator +
				originalFilterList.get(1).toString()+CompositeRuleCharacteristicsFilter.separator +
				originalFilterList.get(2).toString();
		
		//System.out.println(compositeRuleFilter);
		//System.out.println(expected);
		
		assertEquals(compositeRuleFilter.toString(), expected);
	}
	
	/**
	 * Test for {@link CompositeRuleCharacteristicsFilter#of(String)}.
	 */
	@Test
	void testOf() {
		String textualCompositeFilter01 = "support>10";
		String textualCompositeFilter02 = "support>10&confidence>=0.95&epsilon=0.1&coverage<0.3&negative-coverage<=12.0";
		CompositeRuleCharacteristicsFilter compositeRuleFilter01 = CompositeRuleCharacteristicsFilter.of(textualCompositeFilter01);
		CompositeRuleCharacteristicsFilter compositeRuleFilter02 = CompositeRuleCharacteristicsFilter.of(textualCompositeFilter02);
		
		assertEquals(compositeRuleFilter01.toString(), textualCompositeFilter01);
		assertEquals(compositeRuleFilter02.toString(), "support>10&confidence>=0.95&epsilon=0.1&coverage<0.3&negative-coverage<=12");
	}
}
