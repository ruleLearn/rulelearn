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

import static org.rulelearn.core.Precondition.notNull;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Class used to perform operations on collections.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class OperationsOnCollections {
	
	/**
	 * Gets the number of elements from the list which are present in the set.
	 * @param list list with elements to check
	 * @param set set on which the elements are counted 
	 * @return the number of elements from list which are present in the set
	 */
	public static int getNumberOfElementsFromListInSet (IntList list, IntSet set) {
		notNull(list, "List provided to get number of elements which are present in the set is null.");
		notNull(set, "Set on which number of elements from the list are counted is null.");
		int count = 0;
		for (int element : list) {
			if (set.contains(element)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the number of elements from the list which are not present in the set.
	 * @param list list with elements to check
	 * @param set set on which the elements are checked 
	 * @return the number of elements from list which are not present in the set
	 */
	public static int getNumberOfElementsFromListNotPresentInSet (IntList list, IntSet set) {
		notNull(list, "List provided to get number of elements which are not present in the set is null.");
		notNull(set, "Set on which number of elements from the list are checked is null.");
		int count = 0;
		for (int element : list) {
			if (!set.contains(element)) {
				count++;
			}
		}
		return count;
	}
}
