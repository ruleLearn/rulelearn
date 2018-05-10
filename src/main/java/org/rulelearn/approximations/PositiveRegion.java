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
import org.rulelearn.data.InformationTable;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Positive region of an {@link ApproximatedSet}, grouping indices of two sets of objects from an {@link InformationTable}:<br>
 * (1) a set of (sufficiently) consistent objects belonging to the lower approximation of the approximated set,<br>
 * (2) a set of objects that do not belong to set (1), but are present in so-called granules (indiscernibility classes or dominance cones) of the objects from set (1).<br>
 * The objects from set (2) are found during calculation of the objects from set (1).<br>
 * <br>
 * E.g., when using an indiscernibility-based rough set approach (IRSA), if an object x assigned to decision class X_i belongs to the lower approximation of that class,
 * and there is an object y, assigned to a class different than X_i, such that x is indiscernible with y (i.e., y belongs to indiscernibility class of object x), then y would be in set (2).
 * Moreover, when using a dominance-based rough set approach (DRSA), if an object x assigned to decision class X_i belongs to the lower approximation of a union of ordered decision classes
 * "class X_i or better", and there is an object y, assigned to a class worse than X_i (i.e., not belonging to the considered union), such that x is dominated by y,
 * (i.e., y belongs to inverse dominance cone of object x), then y would be in set (2).<br>
 * <br>
 * The concept of a positive region was considered in:<br>
 * Błaszczyński, J., Słowiński, R., Szeląg, M., Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches. Information Sciences, 181, 2011, pp. 987-1002 - see Eq. (10).
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
	 * Set of indices of objects from an information table that belong to this positive region but not to the lower approximation.
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
