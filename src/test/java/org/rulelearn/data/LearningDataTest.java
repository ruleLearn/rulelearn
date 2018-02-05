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

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link LearningData}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class LearningDataTest {

	/**
	 * Tests {@link LearningData#select(int[], boolean) method. Only tests if proper select methods are invoked for component information table and preference information.
	 */
	@Test
	public void testSelectIntArrayBoolean_01() {
		InformationTable informationTableMock = mock(InformationTable.class);
		PreferenceInformation preferenceInformationMock = mock(PreferenceInformation.class);
		
		int[] objectIndices = new int[]{1, 2, 4};
		
		LearningData learningData = new LearningData(informationTableMock, preferenceInformationMock);
		
		@SuppressWarnings("unused")
		LearningData newLearningData = learningData.select(objectIndices, true);
		
		verify(informationTableMock).select(objectIndices, true);
		verify(preferenceInformationMock).select(objectIndices);
	}
	
	/**
	 * Tests {@link LearningData#select(int[], boolean) method. Only tests if proper select methods are invoked for component information table and preference information.
	 */
	@Test
	public void testSelectIntArrayBoolean_02() {
		InformationTable informationTableMock = mock(InformationTable.class);
		PreferenceInformation preferenceInformationMock = mock(PreferenceInformation.class);
		
		int[] objectIndices = new int[]{1, 2, 4};
		
		LearningData learningData = new LearningData(informationTableMock, preferenceInformationMock);
		
		@SuppressWarnings("unused")
		LearningData newLearningData = learningData.select(objectIndices, false);
		
		verify(informationTableMock).select(objectIndices, false);
		verify(preferenceInformationMock).select(objectIndices);
	}

}
