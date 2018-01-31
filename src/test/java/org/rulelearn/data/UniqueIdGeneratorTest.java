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
 * Tests for {@link UniqueIdGenerator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class UniqueIdGeneratorTest {
	
	/**
	 * Test for {@link UniqueIdGenerator#getUniqueId() method.
	 */
	@Test
	void testGetUniqueId() {
		int firstId = UniqueIdGenerator.getInstance().getUniqueId();
		int secondtId = UniqueIdGenerator.getInstance().getUniqueId();
		assertNotEquals(firstId, secondtId);
	}

	/**
	 * Test for {@link UniqueIdGenerator#getUniqueIds() method.
	 */
	@Test
	void testGetUniqueIds() {
		int[] ids = UniqueIdGenerator.getInstance().getUniqueIds(4);
		assertNotEquals(ids[0], ids[1]);
		assertNotEquals(ids[0], ids[2]);
		assertNotEquals(ids[0], ids[3]);
		assertNotEquals(ids[1], ids[2]);
		assertNotEquals(ids[1], ids[3]);
		assertNotEquals(ids[2], ids[3]);
	}

}
