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
import org.rulelearn.data.AttributePreferenceType;

/**
 * Tests for {@link PairField}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class PairFieldTest {

	/**
	 * Test for {@link PairField} class constructor.
	 */
	@Test
	void testPairField01() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		PairField<IntegerField,IntegerField> field = new PairField<IntegerField,IntegerField>(firstField, secondField);
		
		assertEquals(field.getFirstValue().getValue(), firstField.getValue());
		assertEquals(field.getSecondValue().getValue(), secondField.getValue());
	}
	
//	/**
//	 * Test for {@link PairField} class constructor.
//	 */
//	@Test
//	void testPairField02() {
//		SimpleField firstField = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
//		SimpleField secondField = RealFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
//		PairField<SimpleField> field = new PairField<SimpleField>(firstField, secondField);
//		
//		//TODO - implement test
//	}

	@Test
	void testSelfClone() {
		IntegerField firstField = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
		IntegerField secondField = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		PairField<IntegerField,IntegerField> field = new PairField<IntegerField,IntegerField>(firstField, secondField);
		
		PairField<IntegerField,IntegerField> clonedField = field.selfClone();
		
		assertEquals(field.getFirstValue().getValue(), clonedField.getFirstValue().getValue());
		assertEquals(field.getSecondValue().getValue(), clonedField.getSecondValue().getValue());
	}

}
