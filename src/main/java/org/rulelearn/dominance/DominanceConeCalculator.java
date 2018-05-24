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

package org.rulelearn.dominance;

import org.rulelearn.data.InformationTable;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Calculator of dominance cones, capable of calculating different types of dominance cones of an object found in an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DominanceConeCalculator {
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its positive dominance cone w.r.t. (straight) dominance relation D.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the positive dominance cone of the object indexed by x, calculated w.r.t. (straight) dominance relation D
	 */
	public IntSortedSet calculatePositiveDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.dominates(y, x, informationTable)) {// y D x
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its negative dominance cone w.r.t. (straight) dominance relation D.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the negative dominance cone of the object indexed by x, calculated w.r.t. (straight) dominance relation D
	 */
	public IntSortedSet calculateNegativeDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.dominates(x, y, informationTable)) {// x D y
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its positive dominance cone w.r.t. (inverse) dominance relation InvD.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the positive dominance cone of the object indexed by x, calculated w.r.t. (inverse) dominance relation InvD
	 */
	public IntSortedSet calculatePositiveInvDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.isDominatedBy(x, y, informationTable)) {// x InvD y
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its negative dominance cone w.r.t. (inverse) dominance relation InvD.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the negative dominance cone of the object indexed by x, calculated w.r.t. (inverse) dominance relation InvD
	 */
	public IntSortedSet calculateNegativeInvDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.isDominatedBy(y, x, informationTable)) {// y InvD x
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}

}
