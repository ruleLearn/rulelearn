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

import org.rulelearn.data.InformationTable;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Top level class for all sets of objects that can be approximated using the rough set concept.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class ApproximatedSet {
	
	/**
	 * Set of objects belonging to lower approximation of this approximated set.
	 */
	protected IntSortedSet lowerApproximation = null;
	
	/**
	 * Set of objects belonging to upper approximation of this approximated set.
	 */
	protected IntSortedSet upperApproximation = null;
	
	/**
	 * Set of objects belonging to boundary of this approximated set.
	 */
	protected IntSortedSet boundary = null;
	
	/**
	 * Information table containing, among other objects, the objects belonging to this approximated set.
	 */
	protected InformationTable informationTable;
	
	/**
	 * Set of objects from the information table that are inconsistent with the objects belonging to the lower approximation of this approximated set.
	 */
	protected IntSet inconsistentObjectsInPositiveRegion = null;
	
	/**
	 * Sorted set with numbers of objects belonging to this set.
	 */
	protected IntSortedSet objects = null;
	
	//TODO: add hash set storing objects of this approximated set?
	//TODO: store regions?
	
	/**
	 * Constructs this approximated set.
	 * 
	 * @param informationTable information table containing, among other objects, the objects belonging to this approximated set
	 */
	public ApproximatedSet(InformationTable informationTable) {
		this.informationTable = informationTable;
	}
	
	/**
	 * Gets the information table for which this approximated set was defined.
	 * 
	 * @return the information table for which this approximated set was defined
	 */
	public InformationTable getInformationTable() {
		return informationTable;
	}

	/**
	 * Gets objects belonging to this approximated set.
	 * 
	 * @return objects belonging to this approximated set
	 */
	public IntSortedSet getObjects() {
		return objects;
	}

	/**
	 * Gets the lower approximation of this approximated set.
	 * 
	 * @return the lower approximation of this approximated set
	 */
	public abstract IntSortedSet getLowerApproximation();
	
	/**
	 * Gets the upper approximation of this approximated set.
	 * 
	 * @return the upper approximation of this approximated set
	 */
	public abstract IntSortedSet getUpperApproximation();
	
	/**
	 * Gets the boundary of this approximated set.
	 * 
	 * @return the boundary of this approximated set
	 */
	public abstract IntSortedSet getBoundary();
	
	/**
	 * Gets the positive region of this approximated set.
	 * 
	 * @return the positive region of this approximated set
	 */
	public IntSortedSet getPositiveRegion() {
		//TODO: implement
		return null;
	}
	
	/**
	 * Gets the negative region of this approximated set, i.e., the positive region of the complement of this approximated set.
	 * 
	 * @return the negative region of this approximated set
	 */
	public abstract IntSortedSet getNegativeRegion();
	
	/**
	 * Gets the boundary region of this approximated set, i.e., the set of objects that are neither
	 * in the positive region nor in the negative region of this approximated set.
	 * 
	 * @return the boundary region of this approximated set
	 */
	public IntSortedSet getBoundaryRegion() {
		//TODO: implement
		return null;
	}
	
	/**
	 * Gets set of objects from the information table that are inconsistent with the objects belonging to the lower approximation of this approximated set.
	 *  
	 * @return set of objects from the information table that are inconsistent with the objects belonging to the lower approximation of this approximated set
	 */
	public IntSet getInconsistentObjectsInPositiveRegion() {
		return this.inconsistentObjectsInPositiveRegion;
	}
	
	/**
	 * Gets accuracy of approximation of this set.
	 * This is the cardinality of lower approximation divided by the cardinality of upper approximation.
	 * 
	 * @return accuracy of approximation of this set by the set of all active condition attributes of the information table
	 */
	public double getAccuracyOfApproximation() {
		return (double)getLowerApproximation().size() / (double)getUpperApproximation().size();
	}
	
	/**
	 * Gets quality of approximation of this set.
	 * This is the cardinality of lower approximation divided by the number of all examples belonging to this set.
	 * 
	 * @return quality of approximation of this set by the set of all active condition attributes of the information table
	 */
	public double getQualityOfApproximation() {
		return (double)getLowerApproximation().size() / (double)size();
	}
	
	/**
	 * Gets number of examples belonging to this approximated set.
	 * 
	 * @return number of examples belonging to this approximated set
	 */
	public int size() {
		return this.objects.size();
	}
	
	/**
	 * Checks if an object with given index, from the information table for which this set has been created,
	 * belongs to this set.
	 * 
	 * @param objectIndex index of an object, concerning information table for which this set has been created
	 * @return {@code true} if this set contains the objects having given index,
	 *         {@code false} otherwise
	 */
	public boolean contains(int objectIndex) {
		//TODO: implement
		//return examplesSet.contains(Integer.valueOf(objectIndex));
		return false;
	}
	
}
