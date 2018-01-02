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

package org.rulelearn.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link RealField}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RealFieldTest {

	/**
	 * Tests creation and "at least" comparisons of gain-type fields
	 */
	@Test
	public void testIsAtLeastAsGoodAs01() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		
		assertEquals(field1a.isAtLeastAsGoodAs(field0), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtLeastAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field0.isAtLeastAsGoodAs(field1a), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests creation and "at most" comparisons of cost-type fields
	 */
	@Test
	public void testIsAtLeastAsGoodAs02() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		
		assertEquals(field1a.isAtLeastAsGoodAs(field0), TernaryLogicValue.FALSE);
		assertEquals(field1a.isAtLeastAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field0.isAtLeastAsGoodAs(field1a), TernaryLogicValue.TRUE);
	}
	
	
	/**
	 * Tests creation and "at most" comparisons of gain-type fields
	 */
	@Test
	public void testIsMostLeastAsGoodAs01() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		
		assertEquals(field0.isAtMostAsGoodAs(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtMostAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtMostAsGoodAs(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests creation and "at most" comparisons of cost-type fields
	 */
	@Test
	public void testIsMostLeastAsGoodAs02() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		
		assertEquals(field0.isAtMostAsGoodAs(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isAtMostAsGoodAs(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isAtMostAsGoodAs(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests creation and "is equal" comparisons of fields without preference type
	 */
	@Test
	public void testIsEqualTo01() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		
		assertEquals(field0.isEqualTo(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isEqualTo(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isEqualTo(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests creation and "is equal" comparisons of gain-type fields
	 */
	@Test
	public void testIsEqualTo02() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		
		assertEquals(field0.isEqualTo(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isEqualTo(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isEqualTo(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests creation and "is equal" comparisons of cost-type fields
	 */
	@Test
	public void testIsEqualTo03() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		
		assertEquals(field0.isEqualTo(field1a), TernaryLogicValue.FALSE);
		assertEquals(field1a.isEqualTo(field1b), TernaryLogicValue.TRUE);
		assertEquals(field1a.isEqualTo(field0), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests creation and "is different than" comparisons of fields without preference type
	 */
	@Test
	public void testIsDifferentThan01() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		
		assertEquals(field0.isDifferentThan(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isDifferentThan(field1b), TernaryLogicValue.FALSE);
		assertEquals(field1a.isDifferentThan(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests creation and "is different than" comparisons of fields with gain-type preference
	 */
	@Test
	public void testIsDifferentThan02() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		
		assertEquals(field0.isDifferentThan(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isDifferentThan(field1b), TernaryLogicValue.FALSE);
		assertEquals(field1a.isDifferentThan(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests creation and "is different than" comparisons of fields with cost-type preference
	 */
	@Test
	public void testIsDifferentThan03() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		
		assertEquals(field0.isDifferentThan(field1a), TernaryLogicValue.TRUE);
		assertEquals(field1a.isDifferentThan(field1b), TernaryLogicValue.FALSE);
		assertEquals(field1a.isDifferentThan(field0), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests {@link RealField#compareTo(SimpleField)} method.
	 */
	@Test
	public void testCompareTo01() {
		RealField field0 = RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		RealField field1a = RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		RealField field1b = RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		
		assertTrue(field0.compareTo(field1a) < 0);
		assertTrue(field1a.compareTo(field0) > 0);
		assertTrue(field1a.compareTo(field1b) == 0);
	}
	
}
