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
import static org.mockito.Mockito.*;

/**
 * Tests for {@link LearningData}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class LearningDataTest {

	/**
	 * Tests {@link LearningData#select(int[], boolean) method.
	 */
	@Test
	public void testSelectIntArrayBoolean() {
		InformationTable informationTableMock = mock(InformationTable.class);
		PreferenceInformation preferenceInformation = mock(PreferenceInformation.class);
		
		int[] objectIndices = new int[]{1, 2, 4};
		
		//when(informationTableMock.select(objectIndices, true)).thenReturn();
		
		LearningData learningData = new LearningData(informationTableMock, preferenceInformation);
		
		//TODO: implement
	}

}
