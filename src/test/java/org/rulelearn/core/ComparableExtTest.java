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

package org.rulelearn.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.rulelearn.types.Field;
import org.rulelearn.core.UncomparableException;

import static org.mockito.Mockito.*;

/**
 * Test for {@link ComparableExt}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ComparableExtTest {
	
	/**
	 * Test implementation of {@link ComparableExt<Field>}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private abstract class ComparableExtTestOnlyImpl implements ComparableExt<Field> {
		/**
		 * Method changing interface's method signature - no checked exception thrown.
		 * 
		 * @param otherObject {@inheritDoc}
		 * @return {@inheritDoc}
		 */
		@Override
		public abstract int compareToEx(Field otherObject);
	}
	
	/**
	 * Mock of other field.
	 */
	private Field otherField = mock(Field.class);
	
	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are compared, and the first field is greater than the other field.
	 */
	@Test
	void testCompareToEnum01() {
		ComparableExtTestOnlyImpl comparableExt = mock(ComparableExtTestOnlyImpl.class);
		when(comparableExt.compareToEx(otherField)).thenReturn(1);
		when(comparableExt.compareToEnum(otherField)).thenCallRealMethod();
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.GREATER_THAN);
	}
	
	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are compared, and the first field is smaller than the other field.
	 */
	@Test
	void testCompareToEnum02() {
		ComparableExtTestOnlyImpl comparableExt = mock(ComparableExtTestOnlyImpl.class);
		when(comparableExt.compareToEx(otherField)).thenReturn(-1);
		when(comparableExt.compareToEnum(otherField)).thenCallRealMethod();
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.SMALLER_THAN);
	}
	
	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are compared, and the first field is equal to the other field.
	 */
	@Test
	void testCompareToEnum03() {
		ComparableExtTestOnlyImpl comparableExt = mock(ComparableExtTestOnlyImpl.class);
		when(comparableExt.compareToEx(otherField)).thenReturn(0);
		when(comparableExt.compareToEnum(otherField)).thenCallRealMethod();
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.EQUAL);
	}
	
	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are uncomparable.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testCompareToEnum04() {
		ComparableExt<Field> comparableExt = (ComparableExt<Field>)mock(ComparableExt.class);
		try {
			when(comparableExt.compareToEx(otherField)).thenThrow(new UncomparableException("Fields are uncomparable."));
		} catch (UncomparableException e) {
		}
		when(comparableExt.compareToEnum(otherField)).thenCallRealMethod();
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.UNCOMPARABLE);
	}

}
