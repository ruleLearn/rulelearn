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

package org.rulelearn.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * Tests for {@link FieldDistribution}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class FieldDistributionTest {

	/**
	 * Test method for {@link FieldDistribution#FieldDistribution()}.
	 */
	@Test
	void testFieldDistribution() {
		FieldDistribution fieldDistribution = new FieldDistribution();
		assertEquals(fieldDistribution.getCount(Mockito.mock(IntegerField.class)), 0);
	}

	/**
	 * Test method for {@link FieldDistribution#FieldDistribution(InformationTable, int)}.
	 */
	@Test
	void testFieldDistributionInformationTableInt01() {
		try {
			new FieldDistribution(null, 5);
			fail("Should not create field ditribution for null information table.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link #FieldDistribution(InformationTable, int)}.
	 */
	@Test
	void testFieldDistributionInformationTableInt02() {
		try {
			new FieldDistribution(Mockito.mock(InformationTable.class), 3);
		} catch (Throwable throwable) {
			fail("Should create field ditribution.");
		}
	}

	/**
	 * Test method for {@link FieldDistribution#getCount(org.rulelearn.types.Field)}.
	 */
	@Test
	void testGetCount() {
		int attributeIndex = 0;
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(4);
		IntegerField field0 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField field1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		IntegerField field2 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		IntegerField field3 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
				
		Mockito.when(informationTableMock.getField(0, attributeIndex)).thenReturn(field0);
		Mockito.when(informationTableMock.getField(1, attributeIndex)).thenReturn(field1);
		Mockito.when(informationTableMock.getField(2, attributeIndex)).thenReturn(field2);
		Mockito.when(informationTableMock.getField(3, attributeIndex)).thenReturn(field3);
		
		IntegerField otherField = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		
		FieldDistribution fieldDistribution = new FieldDistribution(informationTableMock, attributeIndex);
		
		assertEquals(fieldDistribution.getCount(otherField), 0);
		assertEquals(fieldDistribution.getCount(field0), 2);
		assertEquals(fieldDistribution.getCount(field1), 2);
		assertEquals(fieldDistribution.getCount(field2), 2);
		assertEquals(fieldDistribution.getCount(field3), 2);
	}

	/**
	 * Test method for {@link FieldDistribution#increaseCount(org.rulelearn.types.Field)}.
	 */
	@Test
	void testIncreaseCount() {
		int attributeIndex = 0;
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(2);
		IntegerField field0 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
				
		Mockito.when(informationTableMock.getField(0, attributeIndex)).thenReturn(field0);
		
		FieldDistribution fieldDistribution = new FieldDistribution(informationTableMock, attributeIndex);
		
		assertEquals(fieldDistribution.getCount(field0), 1);
		
		fieldDistribution.increaseCount(field0.selfClone());
		assertEquals(fieldDistribution.getCount(field0), 2);
	}

}
