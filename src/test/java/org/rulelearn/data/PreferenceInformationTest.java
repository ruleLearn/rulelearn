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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealFieldFactory;

/**
 * Tests for {@link PreferenceInformation}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class PreferenceInformationTest {

	/**
	 * Test for {@link PreferenceInformation#getPreferenceInformation(int)}.
	 */
	@Test
	public void testGetPreferenceInformation() {
		List<Field> preferenceInformation = new ArrayList<Field>();
		
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		
		PreferenceInformation preferenceInfo = new PreferenceInformation(preferenceInformation);
		
		assertEquals(preferenceInfo.getPreferenceInformation(0), preferenceInformation.get(0));
		assertEquals(preferenceInfo.getPreferenceInformation(1), preferenceInformation.get(1));
		assertEquals(preferenceInfo.getPreferenceInformation(2), preferenceInformation.get(2));
		assertEquals(preferenceInfo.getPreferenceInformation(3), preferenceInformation.get(3));
		assertEquals(preferenceInfo.getPreferenceInformation(4), preferenceInformation.get(4));
	}

	/**
	 * Test for {@link PreferenceInformation#getNumberOfObjects()}.
	 */
	@Test
	public void testGetNumberOfObjects() {
		List<Field> preferenceInformation = new ArrayList<Field>();
		preferenceInformation.add(RealFieldFactory.getInstance().create(3, AttributePreferenceType.COST));
		preferenceInformation.add(RealFieldFactory.getInstance().create(1, AttributePreferenceType.COST));
		preferenceInformation.add(RealFieldFactory.getInstance().create(2, AttributePreferenceType.COST));
		preferenceInformation.add(RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST));
		
		PreferenceInformation preferenceInfo = new PreferenceInformation(preferenceInformation);
		
		assertEquals(preferenceInfo.getNumberOfObjects(), preferenceInformation.size());
	}
	
	/**
	 * Test for {@link PreferenceInformation#select(int[])}.
	 */
	@Test
	public void testSelect() {
		List<Field> preferenceInformation = new ArrayList<Field>();
		
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST));
		preferenceInformation.add(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST));
		
		PreferenceInformation preferenceInfo = new PreferenceInformation(preferenceInformation);
		
		int[] objectIndices = new int[]{1, 2, 4};
		PreferenceInformation newPreferenceInfo = preferenceInfo.select(objectIndices);
		
		assertEquals(newPreferenceInfo.getNumberOfObjects(), objectIndices.length);
		
		assertEquals(newPreferenceInfo.getPreferenceInformation(0), preferenceInformation.get(objectIndices[0]));
		assertEquals(newPreferenceInfo.getPreferenceInformation(1), preferenceInformation.get(objectIndices[1]));
		assertEquals(newPreferenceInfo.getPreferenceInformation(2), preferenceInformation.get(objectIndices[2]));
	}

}
