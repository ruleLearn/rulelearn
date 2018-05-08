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

package org.rulelearn.approximations;

import static org.rulelearn.core.Precondition.notNull;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * TODO: verify javadoc
 * 
 * Compound object, grouping indices of two sets of objects:<br>
 * - a set of (sufficiently) consistent objects belonging to the lower approximation of some approximated set,<br>
 * - a set of objects that are inconsistent with the objects belonging to that lower approximation.<br>
 * The latter set of objects contains objects that do not belong to the approximated set, but are:<br>
 * - indiscernible with at least one object from the lower approximation of approximated decision class, or<br>
 * - fall into a proper-type dominance cone of some object from the lower approximation of approximated union of ordered decision classes (where a proper-type dominance cone is understood as a positive
 * dominance cone when approximating an upward union of classes, or as a negative dominance cone, when approximating a downward union of classes).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class PositiveRegion {
	
	/**
	 * Empty set of object indices.
	 */
	static private IntSet emptySetOfObjects = new IntOpenHashSet();
	
	/**
	 * Set of indices of objects belonging to the lower approximation of an approximated set.
	 */
	protected IntSortedSet lowerApproximation = null;

	/**
	 * Set of indices of objects from the information table that belong to this positive region but not to the lower approximation.
	 */
	protected IntSet lowerApproximationComplement;
	
	/**
	 * Constructor initializing all fields.
	 * 
	 * @param lowerApproximation set of indices of objects belonging to the lower approximation of an approximated set
	 * @param lowerApproximationComplement TODO
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public PositiveRegion(IntSortedSet lowerApproximation, IntSet lowerApproximationComplement) {
		this.lowerApproximation = notNull(lowerApproximation, "Lower approximation is null.");
		this.lowerApproximationComplement = notNull(lowerApproximationComplement, "Set of indices of objects belonging to positive region and complementing lower approximation is null.");
	}
	
	/**
	 * Constructor initializing lower approximation.
	 * 
	 * @param lowerApproximation set of indices of objects belonging to the lower approximation of an approximated set
	 * @throws NullPointerException TODO
	 */
	public PositiveRegion(IntSortedSet lowerApproximation) {
		this.lowerApproximation = notNull(lowerApproximation, "Lower approximation is null.");
		this.lowerApproximationComplement = emptySetOfObjects;
	}
	
	/**
	 * Gets the set of indices of objects belonging to the lower approximation of an approximated set.
	 * 
	 * @return the set of indices of objects belonging to the lower approximation of an approximated set
	 */
	public IntSortedSet getLowerApproximation() {
		return lowerApproximation;
	}

	/**
	 * TODO
	 * 
	 * @return TODO
	 */
	public IntSet getLowerApproximationComplement() {
		return lowerApproximationComplement;
	}
	
	/**
	 * Gets set of indices all objects belonging to this positive region.
	 * 
	 * @return set of indices all objects belonging to this positive region
	 */
	public IntSet getObjects() {
		IntSet positiveRegion = new IntOpenHashSet(lowerApproximation.size() + lowerApproximationComplement.size());
		positiveRegion.addAll(lowerApproximation);
		positiveRegion.addAll(lowerApproximationComplement);
		
		return positiveRegion;
	}
	
}
