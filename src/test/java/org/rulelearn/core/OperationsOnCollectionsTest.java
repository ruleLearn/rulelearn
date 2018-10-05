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

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Tests for {@link OperationsOnCollections}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class OperationsOnCollectionsTest {

	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet01() {
		IntList list = null;
		IntSet set = null;
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListInSet(list, set);}); 
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet02() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = null;
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListInSet(list, set);}); 
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet03() {
		IntList list = null;
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListInSet(list, set);}); 
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet04() {
		IntList list = new IntArrayList();
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertEquals(0, OperationsOnCollections.getNumberOfElementsFromListInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet05() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = new IntArraySet();
		
		assertEquals(0, OperationsOnCollections.getNumberOfElementsFromListInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet06() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertEquals(6, OperationsOnCollections.getNumberOfElementsFromListInSet(list, set));
	}

	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet07() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = new IntArraySet(new int [] {0});
		
		assertEquals(1, OperationsOnCollections.getNumberOfElementsFromListInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListInSet08() {
		IntList list = new IntArrayList(new int [] {10});
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertEquals(0, OperationsOnCollections.getNumberOfElementsFromListInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet01() {
		IntList list = null;
		IntSet set = null;
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set);}); 
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet02() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = null;
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set);}); 
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet03() {
		IntList list = null;
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set);}); 
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet04() {
		IntList list = new IntArrayList();
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertEquals(0, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet05() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = new IntArraySet();
		
		assertEquals(6, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet06() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertEquals(0, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set));
	}

	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet07() {
		IntList list = new IntArrayList(new int [] {0, 1, 2, 3, 4, 5});
		IntSet set = new IntArraySet(new int [] {0});
		
		assertEquals(5, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSet(it.unimi.dsi.fastutil.ints.IntList, it.unimi.dsi.fastutil.ints.IntSet)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSet08() {
		IntList list = new IntArrayList(new int [] {10});
		IntSet set = new IntArraySet(new int [] {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8 , 9});
		
		assertEquals(1, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSet(list, set));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSets(IntList, IntSet...)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSets01() {
		IntList list = null;
		IntSet set1 = new IntArraySet(new int [] {1, 2, 3, 4, 5, 6, 7, 8, 9});
		IntSet set2 = new IntArraySet(new int [] {11, 12, 13, 14, 15, 16, 17, 18, 19});
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets(list, set1, set2);});
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSets(IntList, IntSet...)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSets02() {
		IntList list = new IntArrayList(new int [] {0, 10, 20});
		IntSet set1 = new IntArraySet(new int [] {1, 2, 3, 4, 5, 6, 7, 8, 9});
		IntSet set2 = null;
		
		assertThrows(NullPointerException.class, () -> {OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets(list, set1, set2);});
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSets(IntList, IntSet...)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSets03() {
		IntList list = new IntArrayList(new int [] {0, 10, 20});
		IntSet set1 = new IntArraySet(new int [] {1, 2, 3, 4, 5, 6, 7, 8, 9});
		IntSet set2 = new IntArraySet(new int [] {11, 12, 13, 14, 15, 16, 17, 18, 19});
		
		assertEquals(3, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets(list, set1, set2));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSets(IntList, IntSet...)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSets04() {
		IntList list = new IntArrayList(new int [] {0, 10, 20});
		IntSet set1 = new IntArraySet(new int [] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20});
		IntSet set2 = new IntArraySet(new int [] {11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
		
		assertEquals(1, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets(list, set1, set2));
	}
	
	/**
	 * Tests method {@link OperationsOnCollections#getNumberOfElementsFromListNotPresentInSets(IntList, IntSet...)}.
	 */
	@Test
	void testGetNumberOfElementsFromListNotPresentInSets05() {
		IntList list = new IntArrayList(new int [] {0, 10, 20});
		IntSet set1 = new IntArraySet(new int [] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20});
		IntSet set2 = new IntArraySet(new int [] {0, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
		
		assertEquals(0, OperationsOnCollections.getNumberOfElementsFromListNotPresentInSets(list, set1, set2));
	}
	
}
