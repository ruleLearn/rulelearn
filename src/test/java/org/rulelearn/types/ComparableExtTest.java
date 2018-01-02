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
import org.rulelearn.types.ComparableExt.ComparisonResult;
import org.rulelearn.core.UncomparableException;

class ComparableExtTest {
	
	//mock of a field
	Field otherField = new Field() {
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			return null;
		}
		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			return null;
		}
		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			return null;
		}
		@Override
		public TernaryLogicValue isDifferentThan(Field otherField) {
			return null;
		}
		@Override
		public int compareToEx(Field otherObject) throws UncomparableException {
			return 0;
		}
		@Override
		public Field selfClone() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are compared, and the first field is greater than the other field.
	 */
	@Test
	void testCompareToEnum01() {
		ComparableExt<Field> comparableExt = new ComparableExt<Field>() {
			@Override
			public int compareToEx(Field otherObject) throws UncomparableException {
				return 1;
			}
		};		
		
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.GREATER_THAN);
	}
	
	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are compared, and the first field is smaller than the other field.
	 */
	@Test
	void testCompareToEnum02() {
		ComparableExt<Field> comparableExt = new ComparableExt<Field>() {
			@Override
			public int compareToEx(Field otherObject) throws UncomparableException {
				return -1;
			}
		};		
		
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.SMALLER_THAN);
	}
	
	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are compared, and the first field is equal to the other field.
	 */
	@Test
	void testCompareToEnum03() {
		ComparableExt<Field> comparableExt = new ComparableExt<Field>() {
			@Override
			public int compareToEx(Field otherObject) throws UncomparableException {
				return 0;
			}
		};		
		
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.EQUAL);
	}
	
	/**
	 * Tests default interface method {@link ComparableExt#compareToEnum(Object)}.
	 * Assumes that two fields are uncomparable.
	 */
	@Test
	void testCompareToEnum04() {
		ComparableExt<Field> comparableExt = new ComparableExt<Field>() {
			@Override
			public int compareToEx(Field otherObject) throws UncomparableException {
				throw new UncomparableException("Fields cannot be compared!");
			}
		};		
		
		assertEquals(comparableExt.compareToEnum(otherField), ComparisonResult.UNCOMPARABLE);
	}

}
