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

import org.junit.jupiter.api.Test;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link IntegerField}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class IntegerFieldTest {

	/**
	 * Tests creation and "at least" comparisons of gain-type integer fields
	 */
	@Test
	public void testIsAtLeastAsGoodAs01() {
		IntegerField iField0 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		IntegerField iField1a = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField iField1b = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		
		assertEquals(iField1a.isAtLeastAsGoodAs(iField0), FieldComparisonResult.TRUE);
		assertEquals(iField1a.isAtLeastAsGoodAs(iField1b), FieldComparisonResult.TRUE);
		assertEquals(iField0.isAtLeastAsGoodAs(iField1a), FieldComparisonResult.FALSE);
	}
	
	/**
	 * Tests creation and "at most" comparisons of cost-type integer fields
	 */
	@Test
	public void testIsAtLeastAsGoodAs02() {
		IntegerField iField0 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		IntegerField iField1a = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		IntegerField iField1b = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		
		assertEquals(iField1a.isAtLeastAsGoodAs(iField0), FieldComparisonResult.FALSE);
		assertEquals(iField1a.isAtLeastAsGoodAs(iField1b), FieldComparisonResult.TRUE);
		assertEquals(iField0.isAtLeastAsGoodAs(iField1a), FieldComparisonResult.TRUE);
	}
	
	
	/**
	 * Tests creation and "at most" comparisons of gain-type integer fields
	 */
	@Test
	public void testIsMostLeastAsGoodAs01() {
		IntegerField iField0 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		IntegerField iField1a = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField iField1b = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		
		assertEquals(iField0.isAtMostAsGoodAs(iField1a), FieldComparisonResult.TRUE);
		assertEquals(iField1a.isAtMostAsGoodAs(iField1b), FieldComparisonResult.TRUE);
		assertEquals(iField1a.isAtMostAsGoodAs(iField0), FieldComparisonResult.FALSE);
	}
	
	/**
	 * Tests creation and "at most" comparisons of cost-type integer fields
	 */
	@Test
	public void testIsMostLeastAsGoodAs02() {
		IntegerField iField0 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		IntegerField iField1a = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		IntegerField iField1b = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		
		assertEquals(iField0.isAtMostAsGoodAs(iField1a), FieldComparisonResult.FALSE);
		assertEquals(iField1a.isAtMostAsGoodAs(iField1b), FieldComparisonResult.TRUE);
		assertEquals(iField1a.isAtMostAsGoodAs(iField0), FieldComparisonResult.TRUE);
	}
	
	/**
	 * Tests creation and "is equal" comparisons of integer fields without preference type
	 */
	@Test
	public void testIsEqualTo01() {
		IntegerField iField0 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		IntegerField iField1a = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField iField1b = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		
		assertEquals(iField0.isEqualTo(iField1a), FieldComparisonResult.FALSE);
		assertEquals(iField1a.isEqualTo(iField1b), FieldComparisonResult.TRUE);
		assertEquals(iField1a.isEqualTo(iField0), FieldComparisonResult.FALSE);
	}
	
	/**
	 * Tests creation and "is equal" comparisons of integer fields without preference type
	 */
	@Test
	public void testIsDifferentThan01() {
		IntegerField iField0 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
		IntegerField iField1a = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField iField1b = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		
		assertEquals(iField0.isDifferentThan(iField1a), FieldComparisonResult.TRUE);
		assertEquals(iField1a.isDifferentThan(iField1b), FieldComparisonResult.FALSE);
		assertEquals(iField1a.isDifferentThan(iField0), FieldComparisonResult.TRUE);
	}	
	
}
