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

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

//TODO: constructor allowing to set different thresholds for different upward/downward unions

/**
 * Container for all upward and downward unions (i.e., unions of type {@link Union.UnionType#AT_LEAST}, and {@link Union.UnionType#AT_MOST}, respectively)
 * that can be defined with respect to an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Unions extends ApproximatedSets {
	
	/**
	 * Array containing all meaningful (i.e., not containing all objects) upward unions which can be defined for the information table.
	 * Unions are sorted from the most specific to the least specific. In general, the array represents a partial order of upward unions.<br>
	 * <br>
	 * The assertion that need to be satisfied is that given indices i,j >= 0, upwardUnions[i] is either a subset of upwardUnions[i+j], or both unions are "uncomparable",
	 * i.e., neither upwardUnions[i] is a subset of upwardUnions[i+j], nor upwardUnions[i+j] is a subset of upwardUnions[i].
	 */
	Union[] upwardUnions = null;
	
	/**
	 * Array containing all meaningful (i.e., not containing all objects) downward unions which can be defined for the information table.
	 * Unions are sorted from the most specific to the least specific. In general, the array represents a partial order of downward unions.<br>
	 * <br>
	 * The assertion that need to be satisfied is that given indices {@code i,j >= 0}, {@code downwardUnions[i]} is either a subset of {@code downwardUnions[i+j]},
	 * or both unions are "uncomparable",
	 * i.e., neither {@code downwardUnions[i]} is a subset of {@code downwardUnions[i+j]}, nor {@code downwardUnions[i+j]} is a subset of {@code downwardUnions[i]}.
	 */
	Union[] downwardUnions = null;
	
	/**
	 * Constructs this container of upward and downward unions that can be defined with respect to an information table.
	 * 
	 * @param informationTable information table for which all unions stored in this container are defined
	 * @param roughSetCalculator rough set calculator used to calculate approximations of all unions stored in this container
	 * 
	 * @throws NullPointerException if given information table does not store decisions for subsequent objects
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if given information table does not contain at least one fully-determined decision - see {@link Decision#hasNoMissingEvaluation()}
	 */
	Unions(InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(informationTable, roughSetCalculator);
	}
	
	/**
	 * Creates array of all meaningful (i.e., not containing all objects) upward unions {@link #upwardUnions}, sorted from the most to the least specific union.<br>
	 * <br>
	 * The assertion that needs to be satisfied is that given indices {@code i,j >= 0}, {@code upwardUnions[i]} is either a subset of {@code upwardUnions[i+j]},
	 * or both unions are "uncomparable",
	 * i.e., neither {@code upwardUnions[i]} is a subset of {@code upwardUnions[i+j]}, nor {@code upwardUnions[i+j]} is a subset of {@code upwardUnions[i]}.
	 */
	abstract void calculateUpwardUnions();
	
	/**
	 * Creates array of all meaningful (i.e., not containing all objects) downward unions {@link #downwardUnions}, sorted from the most to the least specific union.<br>
	 * <br>
	 * The assertion that needs to be satisfied is that given indices {@code i,j >= 0}, {@code downwardUnions[i]} is either a subset of {@code downwardUnions[i+j]},
	 * or both unions are "uncomparable",
	 * i.e., neither {@code downwardUnions[i]} is a subset of {@code downwardUnions[i+j]}, nor {@code downwardUnions[i+j]} is a subset of {@code downwardUnions[i]}.
	 */
	abstract void calculateDownwardUnions();
	
	/**
	 * Gets the dominance-based rough set calculator used to calculate approximations of all unions stored in this container.
	 * 
	 * @return the dominance-based rough set calculator used to calculate approximations of all unions stored in this container
	 */
	@Override
	public DominanceBasedRoughSetCalculator getRoughSetCalculator() {
		return (DominanceBasedRoughSetCalculator)roughSetCalculator;
	}
	
	/**
	 * Gets information table for which all unions stored in this container are defined.
	 * 
	 * @return the information table for which all unions stored in this container are defined
	 */
	public InformationTableWithDecisionDistributions getInformationTable() {
		return (InformationTableWithDecisionDistributions)informationTable;
	}

	/**
	 * Gets quality of (approximation of) classification concerning all unions which can be defined for the information table.
	 * As in general (in presence of missing values) the theorem concerning identity of boundaries
	 * does not hold, counts an object as inconsistent if it belongs to the boundary of any upward
	 * or downward union from this container.
	 * 
	 * @return quality of (approximation of) classification concerning all unions which can be defined for the information table
	 */
	@Override
	public double getQualityOfApproximation() {
		int allObjectCount = getInformationTable().getNumberOfObjects();
		
		return (double)getNumberOfConsistentObjects() / (double)allObjectCount;
	}
	
	/**
	 * Gets the number of consistent objects in the information table for which all unions stored in this container are defined.
	 * 
	 * @return the number of consistent objects in the information table for which all unions stored in this container are defined
	 *         (this is the numerator of the quality of classification)
	 */
	@Override
	public int getNumberOfConsistentObjects() {
		Union[] upwardUnions = getUpwardUnions(true);
		Union[] downwardUnions = getDownwardUnions(true);
		
		int allObjectCount = getInformationTable().getNumberOfObjects();
		IntSet inconsistentObjectIndices = new IntOpenHashSet();
		
		for (Union union : upwardUnions) {
			inconsistentObjectIndices.addAll(union.getBoundary());
		}
		for (Union union : downwardUnions) {
			inconsistentObjectIndices.addAll(union.getBoundary());
		}
		
		return allObjectCount - inconsistentObjectIndices.size();
	}
	
	/**
	 * Gets ordered indices of all consistent objects.
	 * 
	 * @return ordered indices of all consistent objects
	 */
	public int[] getNumbersOfConsistentObjects() {
		Union[] upwardUnions = getUpwardUnions(true);
		Union[] downwardUnions = getDownwardUnions(true);
		
		int allObjectCount = getInformationTable().getNumberOfObjects();
		IntSet inconsistentObjectIndices = new IntOpenHashSet();
		
		//collect inconsistent objects
		for (Union union : upwardUnions) {
			inconsistentObjectIndices.addAll(union.getBoundary());
		}
		for (Union union : downwardUnions) {
			inconsistentObjectIndices.addAll(union.getBoundary());
		}
		
		int[] result = new int[allObjectCount - inconsistentObjectIndices.size()];
		int index = 0;
		for (int i = 0; i < allObjectCount; i++) {
			if (!inconsistentObjectIndices.contains(i)) {
				result[index++] = i;
			}
		}
		
		return result;
	}
	
	/**
	 * Gets array containing all meaningful (i.e., not containing all objects) upward unions which can be defined for the information table.
	 * Unions are sorted from the most specific (i.e., containing the smallest number of objects)
	 * to the least specific (i.e., containing the greatest number of objects). In general, returned array represents a partial order of upward unions.<br>
	 * <br>
	 * The assertion that is satisfied is that given indices {@code i,j >= 0}, {@code upwardUnions[i]} is either a subset of {@code upwardUnions[i+j]},
	 * or both unions are "uncomparable",
	 * i.e., neither {@code upwardUnions[i]} is a subset of {@code upwardUnions[i+j]}, nor {@code upwardUnions[i+j]} is a subset of {@code upwardUnions[i]}.
	 * 
	 * @return array containing all upward unions which can be defined for the information table
	 */
	public Union[] getUpwardUnions() {
		if (upwardUnions == null) {
			calculateUpwardUnions();
		}
		return upwardUnions.clone();
	}
	
	/**
	 * Gets array containing all meaningful (i.e., not containing all objects) upward unions which can be defined for the information table.
	 * Unions are sorted from the most specific (i.e., containing the smallest number of objects)
	 * to the least specific (i.e., containing the greatest number of objects). In general, returned array represents a partial order of upward unions.<br>
	 * <br>
	 * The assertion that is satisfied is that given indices {@code i,j >= 0}, {@code upwardUnions[i]} is either a subset of {@code upwardUnions[i+j]},
	 * or both unions are "uncomparable",
	 * i.e., neither {@code upwardUnions[i]} is a subset of {@code upwardUnions[i+j]}, nor {@code upwardUnions[i+j]} is a subset of {@code upwardUnions[i]}.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array containing all upward unions which can be defined for information table
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Union[] getUpwardUnions(boolean accelerateByReadOnlyResult) {
		if (upwardUnions == null) {
			calculateUpwardUnions();
		}
		return accelerateByReadOnlyResult ? upwardUnions : upwardUnions.clone();
	}
	
	/**
	 * Gets array containing all meaningful (i.e., not containing all objects) downward unions which can be defined for the information table.
	 * Unions are sorted from the most specific (i.e., containing the smallest number of objects)
	 * to the least specific (i.e., containing the greatest number of objects). In general, returned array represents a partial order of downward unions.<br>
	 * <br>
	 * The assertion that is satisfied is that given indices {@code i,j >= 0}, {@code downwardUnions[i]} is either a subset of {@code downwardUnions[i+j]},
	 * or both unions are "uncomparable",
	 * i.e., neither {@code downwardUnions[i]} is a subset of {@code downwardUnions[i+j]}, nor {@code downwardUnions[i+j]} is a subset of {@code downwardUnions[i]}.
	 * 
	 * @return array containing all downward unions which can be defined for information table
	 */
	public Union[] getDownwardUnions() {
		if (downwardUnions == null) {
			calculateDownwardUnions();
		}
		return downwardUnions.clone();
	}
	
	/**
	 * Gets array containing all meaningful (i.e., not containing all objects) downward unions which can be defined for the information table.
	 * Unions are sorted from the most specific (i.e., containing the smallest number of objects)
	 * to the least specific (i.e., containing the greatest number of objects). In general, returned array represents a partial order of downward unions.<br>
	 * <br>
	 * The assertion that is satisfied is that given indices {@code i,j >= 0}, {@code downwardUnions[i]} is either a subset of {@code downwardUnions[i+j]},
	 * or both unions are "uncomparable",
	 * i.e., neither {@code downwardUnions[i]} is a subset of {@code downwardUnions[i+j]}, nor {@code downwardUnions[i+j]} is a subset of {@code downwardUnions[i]}.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array containing all downward unions which can be defined for information table
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Union[] getDownwardUnions(boolean accelerateByReadOnlyResult) {
		if (downwardUnions == null) {
			calculateDownwardUnions();
		}
		return accelerateByReadOnlyResult ? downwardUnions : downwardUnions.clone();
	}
	
}
