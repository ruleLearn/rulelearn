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


import java.util.HashSet;

import org.rulelearn.core.Precondition;

import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * TODO: write javadoc
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class LowerApproximationAndInconsistentObjects {
	/**
	 * Set of objects belonging to lower approximation of the approximated set.
	 */
	protected IntSortedSet lowerApproximation = null;

	/**
	 * Set of objects from the information table that are inconsistent with the objects belonging to the lower approximation of the approximated set.
	 */
	protected HashSet<Integer> inconsistentObjectsInPositiveRegion = null;
	
	/**
	 * Constructor initializing all fields.
	 * 
	 * @param lowerApproximation set of objects belonging to lower approximation of the approximated set
	 * @param inconsistentObjectsInPositiveRegion set of objects from the information table that are inconsistent with the objects belonging to the lower approximation of the approximated set
	 * 
	 * @throws NullPointerException if lower approximation or set of inconsistent objects in positive region does not conform to {@link Precondition#notNull(Object, String)}
	 */
	public LowerApproximationAndInconsistentObjects(IntSortedSet lowerApproximation, HashSet<Integer> inconsistentObjectsInPositiveRegion) {
		this.lowerApproximation = Precondition.notNull(lowerApproximation, "Lower approximation is null.");
		this.inconsistentObjectsInPositiveRegion = Precondition.notNull(inconsistentObjectsInPositiveRegion, "Set of inconsistent objects in positive region is null.");
	}

	/**
	 * @return the lowerApproximation
	 */
	public IntSortedSet getLowerApproximation() {
		return lowerApproximation;
	}

	/**
	 * @return the inconsistentObjectsInPositiveRegion
	 */
	public HashSet<Integer> getInconsistentObjectsInPositiveRegion() {
		return inconsistentObjectsInPositiveRegion;
	}
	
	
}
