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

import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;

import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSortedSets;

/**
 * Top level class for all sets of objects that can be approximated using the rough set concept.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class ApproximatedSet {
	
	/**
	 * Set of indices of objects belonging to the lower approximation of this approximated set, calculated using the rough set calculator.
	 */
	protected IntSortedSet lowerApproximation = null;
	
	/**
	 * Set of indices of objects belonging to the upper approximation of this approximated set, calculated using the rough set calculator.
	 */
	protected IntSortedSet upperApproximation = null;
	
	/**
	 * Set of indices of objects belonging to the boundary of this approximated set.
	 */
	protected IntSortedSet boundary = null;
	
	/**
	 * Information table containing, among other objects, the objects belonging to this approximated set.
	 */
	protected InformationTable informationTable;
	
	/**
	 * Rough set calculator used to calculate approximations of this set.
	 */
	protected RoughSetCalculator<? extends ApproximatedSet> roughSetCalculator;
	
	/**
	 * Set of indices of objects belonging to the positive region of this approximated set.
	 */
	protected IntSet positiveRegion = null;
	/**
	 * Set of indices of objects belonging to the negative region of this approximated set.
	 */
	protected IntSet negativeRegion = null;
	/**
	 * Set of indices of objects belonging to the boundary region of this approximated set.
	 */
	protected IntSet boundaryRegion = null;
	
	/**
	 * Set with indices of objects belonging to this approximated set (so-called positive objects).
	 */
	protected IntSortedSet objects = null;
	
	/**
	 * Constructs this approximated set using given information table and rough set calculator.
	 * 
	 * @param informationTable information table containing, among other objects, the objects belonging to this approximated set
	 * @param roughSetCalculator rough set calculator used to calculate approximations and boundary of this set
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ApproximatedSet(InformationTable informationTable, RoughSetCalculator<? extends ApproximatedSet> roughSetCalculator) {
		this.informationTable = notNull(informationTable, "Information table for constructed approximated set is null.");
		this.roughSetCalculator = notNull(roughSetCalculator, "Rough set calculator for constructed approximated set is null.");
	}
	
	/**
	 * Sole constructor, facilitating construction of subclass objects. Does not provide any initialization of fields.
	 */
	ApproximatedSet() {}
	
	/**
	 * Gets the information table for which this approximated set was defined.
	 * 
	 * @return the information table for which this approximated set was defined
	 */
	public InformationTable getInformationTable() {
		return informationTable;
	}
	
	/**
	 * Gets rough set calculator used to calculate approximations of this set.
	 * 
	 * @return rough set calculator used to calculate approximations of this set
	 */
	public RoughSetCalculator<? extends ApproximatedSet> getRoughSetCalculator() {
		return roughSetCalculator;
	}

	/**
	 * Gets set with indices of objects belonging to this approximated set (so-called positive objects).
	 * 
	 * @return set with indices of objects belonging to this approximated set  (so-called positive objects)
	 * @throws NullPointerException if the set with indices of objects belonging to this approximated set cannot be determined
	 */
	public IntSortedSet getObjects() {
		if (this.objects == null) {
			this.findObjects();
			
			if (this.objects == null) {
				throw new NullPointerException("Cannot determine objects belonging to an approximated set.");
			}
		}
		
		return this.objects;
	}
	
	/**
	 * Calculates and stores in the field {@link #objects} the set with indices of objects belonging to this approximated set (so-called positive objects).
	 * Stored set should be unmodifiable, as obtained by {@link IntSortedSets#unmodifiable(IntSortedSet)}.
	 */
	protected abstract void findObjects();
	
	/**
	 * Gets set of objects from information table that are neither positive nor negative with respect to this approximated set.
	 * 
	 * @return set of objects from information table that are neither positive nor negative with respect to this approximated set;
	 *         if the notion of neutral objects does not make sense for particular approximated set, returns an empty set
	 */
	public abstract IntSortedSet getNeutralObjects();
	
//	/**
//	 * Gets indices of uncomparable objects from the information table such that this set's limiting decision is uncomparable with their decision.
//	 * The concept of uncomparable objects is important, e.g., in case of multicriteria decision problems,
//	 * when considering unions of ordered decision classes, and multiple decision criteria.
//	 * Then, it may be the case that limiting decision of a union of ordered decision classes is uncomparable with decisions assigned to some objects
//	 * from an {@link InformationTable}.<br>
//	 * <br>
//	 * If the concept of uncomparable objects is meaningless for a particular subtype of this class, then implementing method should return {@code null}.
//	 * 
//	 * @return indices of uncomparable objects from the information table, such that this set's limiting decision is uncomparable with their decision
//	 */
//	public abstract IntSortedSet getUncomparableObjects();

	/**
	 * Gets unmodifiable set of indices of objects belonging to the lower approximation of this approximated set.
	 * 
	 * @return unmodifiable set of indices of objects belonging to the lower approximation of this approximated set
	 */
	public IntSortedSet getLowerApproximation() {
		if (this.lowerApproximation == null) {
			this.lowerApproximation = IntSortedSets.unmodifiable(this.calculateLowerApproximation());
		}
		return this.lowerApproximation;
	}
	
	/**
	 * Calculates lower approximation of this approximated set, using the rough set calculator.
	 * 
	 * @return set of indices of objects belonging to the lower approximation of this approximated set, calculated using the rough set calculator
	 */
	protected abstract IntSortedSet calculateLowerApproximation();
	
	/**
	 * Gets unmodifiable set of indices of objects belonging to the upper approximation of this approximated set.
	 * 
	 * @return unmodifiable set of indices of objects belonging to the upper approximation of this approximated set
	 */
	public IntSortedSet getUpperApproximation() {
		if (this.upperApproximation == null) {
			this.upperApproximation = IntSortedSets.unmodifiable(this.calculateUpperApproximation());
		}
		return this.upperApproximation;
	}
	
	/**
	 * Calculates upper approximation of this approximated set, using the rough set calculator.
	 * 
	 * @return set of indices of objects belonging to the upper approximation of this approximated set, calculated using the rough set calculator
	 */
	protected abstract IntSortedSet calculateUpperApproximation();
	
	/**
	 * Gets unmodifiable set of indices of objects belonging to the boundary of this approximated set.
	 * 
	 * @return unmodifiable set of indices of objects belonging to the boundary of this approximated set
	 */
	public IntSortedSet getBoundary() {
		if (this.boundary == null) {
			IntSortedSet upperApproximation = this.getUpperApproximation();
			IntSortedSet lowerApproximation = this.getLowerApproximation();
			IntSortedSet boundary;
			int boundarySize = upperApproximation.size() - lowerApproximation.size();
			
			boundary = new IntLinkedOpenHashSet(boundarySize);
			
			if (boundarySize > 0) {
				IntBidirectionalIterator iterator = upperApproximation.iterator();
				int objectIndex;
				
				while (iterator.hasNext()) {
					objectIndex = iterator.nextInt();
					if (!lowerApproximation.contains(objectIndex)) {
						boundary.add(objectIndex);
					}
				}
			}
			
			this.boundary = IntSortedSets.unmodifiable(boundary);
		}
		
		return this.boundary;
	}
	
	/**
	 * Gets unmodifiable set of indices of objects belonging to the positive region of this approximated set.
	 * This region is composed of objects belonging to the lower approximation of this set plus
	 * objects belonging to so-called granules (equivalence classes or dominance cones)
	 * defined with respect to the objects from the lower approximation.
	 * 
	 * @return unmodifiable set of indices of objects belonging to the positive region of this approximated set
	 */
	public IntSet getPositiveRegion() {
		if (this.positiveRegion == null) { //positive region not calculated yet
			this.positiveRegion = IntSets.unmodifiable(this.calculatePositiveRegion(this.getLowerApproximation()));
		}
		
		return this.positiveRegion;
	}
	
	/**
	 * Calculates positive region of this approximated set, using the given lower approximation.
	 * This region is composed of objects belonging to the lower approximation of this set plus
	 * objects belonging to so-called granules (equivalence classes or dominance cones)
	 * defined with respect to the objects from the lower approximation.
	 * 
	 * @return set of indices of objects belonging to the positive region of this approximated set, calculated using given lower approximation
	 * @param lowerApproximation lower approximation of this approximated set
	 * @throws NullPointerException if given lower approximation is {@code null}
	 */
	protected abstract IntSet calculatePositiveRegion(IntSortedSet lowerApproximation);
	
	/**
	 * Gets unmodifiable set of indices of objects belonging to the negative region of this approximated set.
	 * The negative region is a (set) difference between positive region of the complement of this approximated set and positive region of this approximated set.
	 * 
	 * @return unmodifiable set of indices of objects belonging to the negative region of this approximated set
	 */
	public IntSet getNegativeRegion() {
		if (this.negativeRegion == null) { //negative region not calculated yet
			this.negativeRegion = IntSets.unmodifiable(this.calculateNegativeRegion());
		}
		
		return this.negativeRegion;
	}
	
	/**
	 * Calculates negative region of this approximated set.
	 * This region is composed of objects belonging to the positive region of the complement of this approximated set,
	 * but not to the positive region of this approximated set.
	 * 
	 * @return set of indices of objects belonging to the negative region of this approximated set
	 */
	protected abstract IntSet calculateNegativeRegion();
	
	/**
	 * Gets unmodifiable set of indices of objects belonging to the boundary region of this approximated set, i.e., the set of objects that are neither
	 * in the positive region nor in the negative region of this approximated set.
	 * 
	 * @return unmodifiable set of indices of objects belonging to the boundary region of this approximated set
	 */
	public IntSet getBoundaryRegion() {
		if (this.boundaryRegion == null) { //boundary region not calculated yet
			IntSet positiveRegion = this.getPositiveRegion();
			IntSet negativeRegion = this.getNegativeRegion();
			
			int objectsCount = this.informationTable.getNumberOfObjects();
			this.boundaryRegion = new IntOpenHashSet(objectsCount - positiveRegion.size() - negativeRegion.size());
			
			for (int i = 0; i < objectsCount; i++) {
				if (!positiveRegion.contains(i) && !negativeRegion.contains(i)) {
					this.boundaryRegion.add(i);
				}
			}
			
			this.boundaryRegion = IntSets.unmodifiable(this.boundaryRegion);
		}
		
		return this.boundaryRegion;
	}
	
	/**
	 * Gets accuracy of approximation of this set.
	 * This is the cardinality of the lower approximation divided by the cardinality of the upper approximation.
	 * 
	 * @return accuracy of approximation of this set by the set of all active condition attributes of the information table
	 */
	public double getAccuracyOfApproximation() {
		return (double)getLowerApproximation().size() / (double)getUpperApproximation().size();
	}
	
	/**
	 * Gets quality of approximation of this set.
	 * This is the cardinality of the lower approximation divided by the number of all objects belonging to this approximated set.
	 * 
	 * @return quality of approximation of this set by the set of all active condition attributes of the information table
	 */
	public double getQualityOfApproximation() {
		return (double)getLowerApproximation().size() / (double)size();
	}
	
	/**
	 * Gets number of (positive) objects belonging to this approximated set.
	 * 
	 * @return number of (positive) objects belonging to this approximated set
	 */
	public int size() {
		return this.getObjects().size();
	}
	
	/**
	 * Checks if an object with given index, from the information table for which this approximated set has been created,
	 * belongs to this approximated set.
	 * 
	 * @param objectIndex index of an object, concerning information table for which this approximated set has been created
	 * @return {@code true} if this approximated set contains the object having given index,
	 *         {@code false} otherwise
	 */
	public boolean contains(int objectIndex) {
		return this.getObjects().contains(objectIndex);
	}
	
	/**
	 * Tests if this approximated set is concordant with given decision.
	 * 
	 * @param decision tested decision
	 * @return {@link TernaryLogicValue#TRUE} if this set is concordant with given decision,
	 *         {@link TernaryLogicValue#FALSE} if this set is not concordant with given decision,
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if this set is neither concordant nor discordant with given decision
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	protected abstract TernaryLogicValue isConcordantWithDecision(Decision decision);
	
	/**
	 * Tells if given decision is positive with respect to this approximated set.
	 * 
	 * @param decision decision to verify for being positive with respect to this approximated set
	 * @return {@code true} if given decision is positive with respect to this approximated set,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	public abstract boolean isDecisionPositive(Decision decision);
	
	/**
	 * Tells if given decision is negative with respect to this approximated set.
	 * 
	 * @param decision decision to verify for being negative with respect to this approximated set
	 * @return {@code true} if given decision is negative with respect to this approximated set,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	public abstract boolean isDecisionNegative(Decision decision);
	
	/**
	 * Tells if given object is positive with respect to this approximated set.
	 * 
	 * @param objectNumber index of an object from the information table
	 * @return {@code true} if object with given number is positive with respect to this approximated set,
	 *         {@code false} otherwise
	 */
	public abstract boolean isObjectPositive(int objectNumber);
	
	/**
	 * Tells if given object is negative with respect to this approximated set.
	 * 
	 * @param objectNumber index of an object from the information table
	 * @return {@code true} if object with given number is negative with respect to this approximated set,
	 *         {@code false} otherwise
	 */
	public abstract boolean isObjectNegative(int objectNumber);
	
	/**
	 * Gets the size of the set of objects that is complementary to the set of objects belonging to this approximated set.
	 * The understanding of the notion of set complementarity is on behalf of the implementing subclass.
	 * 
	 * @return the size of the set of objects that is complementary to the set of (positive) objects belonging to this approximated set
	 */
	public abstract int getComplementarySetSize();

	/**
	 * Tells if this approximated set is meaningful, i.e., it does not contain all the objects from the information table
	 * 
	 * @return {@code true} if this approximated set is meaningful, i.e., it does not contain all the objects from the information table,
	 *         {@code false} otherwise
	 */
	public boolean isMeaningful() {
		return this.size() != this.informationTable.getNumberOfObjects();
	}
	
	/**
	 * Tells if this approximated set contains given set, i.e., conditions that determine what objects belong to this set are equally or less restrictive than those
	 * conditions, that determine the objects belonging to the other set.
	 * 
	 * @param approximatedSet other approximated set of the same type
	 * @return {@code true} if this set includes (contains) the other set,
	 *         {@code false} otherwise
	 * 
	 * @throws ClassCastException if type of the other set prevents it from comparison with this set 
	 */
	public abstract boolean includes(ApproximatedSet approximatedSet);
	
}
