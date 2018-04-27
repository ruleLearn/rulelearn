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
import org.rulelearn.core.Precondition;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Compound object, grouping indices of two sets of objects:<br>
 * - a set of sufficiently consistent objects belonging to the lower approximation of some approximated set,<br>
 * - a set of objects that are inconsistent with the objects belonging to that lower approximation.<br>
 * The latter set of objects contains objects that do not belong to the approximated set, but are:<br>
 * - indiscernible with at least one object from the lower approximation of approximated decision class, or<br>
 * - fall into a proper-type dominance cone of some object from the lower approximation of approximated union of ordered decision classes (where a proper-type dominance cone is understood as a positive
 * dominance cone when approximating an upward union of classes, or as a negative dominance cone, when approximating a downward union of classes).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class LowerApproximationAndInconsistentObjects {
	/**
	 * Set of indices of objects belonging to the lower approximation of an approximated set.
	 */
	protected IntSortedSet lowerApproximation = null;

	/**
	 * Set of indices of objects from the information table that are inconsistent with the objects belonging to the lower approximation of an approximated set.
	 */
	protected IntSet inconsistentObjectsInPositiveRegion = null;
	
	/**
	 * Constructor initializing all fields.
	 * 
	 * @param lowerApproximation set of indices of objects belonging to the lower approximation of an approximated set
	 * @param inconsistentObjectsInPositiveRegion set of indices of objects from the information table that are inconsistent with the objects belonging to the lower approximation of an approximated set
	 * 
	 * @throws NullPointerException if the lower approximation or the set of inconsistent objects in positive region does not conform to {@link Precondition#notNull(Object, String)}
	 */
	public LowerApproximationAndInconsistentObjects(IntSortedSet lowerApproximation, IntSet inconsistentObjectsInPositiveRegion) {
		this.lowerApproximation = notNull(lowerApproximation, "Lower approximation is null.");
		this.inconsistentObjectsInPositiveRegion = notNull(inconsistentObjectsInPositiveRegion, "Set of inconsistent objects in positive region is null.");
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
	 * Gets the set of indices of objects from the information table that are inconsistent with the objects belonging to the lower approximation of an approximated set
	 * 
	 * @return the set of indices of objects from the information table that are inconsistent with the objects belonging to the lower approximation of an approximated set
	 */
	public IntSet getInconsistentObjectsInPositiveRegion() {
		return inconsistentObjectsInPositiveRegion;
	}
	
	
}
