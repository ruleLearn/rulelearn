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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link UnknownSimpleFieldMV2}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class UnknownSimpleFieldMV2Test {

	@Test
	public void testIsAtLeastAsGoodAs_01() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV2();
		Field otherField = new UnknownSimpleFieldMV2();
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	@Test
	public void testIsAtLeastAsGoodAs_02() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV2();
		Field otherField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	@Test
	public void testIsAtLeastAsGoodAs_03() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV2();
		Field otherField = RealFieldFactory.getInstance().create(-1.0, AttributePreferenceType.COST);
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}
	
	@Test
	public void testIsAtLeastAsGoodAs_04() {
		UnknownSimpleField mvField = new UnknownSimpleFieldMV2();
		Field otherField = null;
		try {
			otherField = EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b", "c"}), 1, AttributePreferenceType.NONE);
		} catch (Exception exception) {
			fail("Element list could not be constructed.");
		}
		assertEquals(mvField.isAtLeastAsGoodAs(otherField), TernaryLogicValue.TRUE);
	}

//	@Test
//	public void testIsAtMostAsGoodAs() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsEqualTo() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testReverseIsAtLeastAsGoodAs() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testReverseIsAtMostAsGoodAs() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testReverseIsEqualTo() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCompareToEx() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testReverseCompareToEx() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSelfClone() {
//		fail("Not yet implemented");
//	}

}
