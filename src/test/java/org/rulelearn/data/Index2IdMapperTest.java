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

/**
 * Tests for {@link Index2IdMapper}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class Index2IdMapperTest {

	/**
	 * Test for {@link Index2IdMapper#getId(int)} method.
	 */
	@Test
	void testGetId_01() {
		int[] objectIndex2Id = {23, 6, 13};
		Index2IdMapper mapper = new Index2IdMapper(objectIndex2Id);
		assertEquals(mapper.getId(0), 23);
		assertEquals(mapper.getId(1), 6);
		assertEquals(mapper.getId(2), 13);
	}
	
	/**
	 * Test for {@link Index2IdMapper#getId(int, boolean)} method.
	 */
	@Test
	void testGetId_02() {
		int[] objectIndex2Id = {7, 14, 5};
		Index2IdMapper mapper = new Index2IdMapper(objectIndex2Id, true);
		assertEquals(mapper.getId(0), 7);
		assertEquals(mapper.getId(1), 14);
		assertEquals(mapper.getId(2), 5);
	}

	/**
	 * Test for {@link Index2IdMapper#getNumberOfObjects()} method.
	 */
	@Test
	void testGetNumberOfObjects() {
		int[] objectIndex2Id = {7, 14, 5};
		Index2IdMapper mapper = new Index2IdMapper(objectIndex2Id, false);
		assertEquals(mapper.getNumberOfObjects(), 3);
	}

}
