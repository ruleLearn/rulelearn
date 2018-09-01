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

import java.util.ArrayList;

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

//TODO: set complementary unions, if possible

/**
 * Container for all upward and downward unions that can be defined with respect to an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Unions extends ApproximatedSets {
	
	/**
	 * Array containing all upward unions which can be defined for information table.
	 * Unions are sorted from the most specific to the least specific.
	 * For example - if possible values of decision criterion are: 1, 2 and 3
	 * and preference type for this criterion is gain then:<br>
	 * upwardUnions[0] => union "class 3 and better"<br>
	 * upwardUnions[1] => union "class 2 and better"
	 */
	Union[] upwardUnions = null;
	
	/**
	 * Array containing all downward unions which can be defined for information table.
	 * Unions are sorted from the most specific to the least specific.
	 * For example, if possible values of decision criterion are: 1, 2 and 3
	 * and preference type for this criterion is gain, then:<br>
	 * downwardUnions[0] => union "class 1 and worse"<br>
	 * downwardUnions[1] => union "class 2 and worse"
	 */
	Union[] downwardUnions = null;
	
	/**
	 * Ordered (from the worst to the best) array of all unique (distinct) decisions, which can be found in information table.
	 * Each decision is present only once.
	 * The order is such that each decision in the array is worse than decisions to the right
	 * being comparable to this decision, and better than decisions to the left being comparable to this decision.
	 * If two decisions are incomparable, then their respective order is not constrained and may be any.
	 */
	Decision[] limitingDecisions = null;
	
	/**
	 * Constructs this container of upward and downward unions that can be defined with respect to an information table.
	 * 
	 * @param informationTable information table for which all approximated sets stored in this container are defined
	 * @param roughSetCalculator rough set calculator used to calculate approximations of all approximated sets stored in this container
	 * 
	 * @throws NullPointerException if given information table does not store decisions for subsequent objects
	 */
	public Unions(InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(informationTable, roughSetCalculator);
		
		//calculate limiting decisions
		limitingDecisions = informationTable.calculateOrderedUniqueDecisions();
		
		if (limitingDecisions == null) {
			throw new NullPointerException("Information table does not store decisions for subsequent objects.");
		}
		
		//create all upward and downward unions
		constructUpwardUnions();
		constructDownwardUnions();
	}
	
	/**
	 * Creates array of all meaningful (i.e., not containing all objects) upward unions, sorted from the most to the least specific union.
	 */
	void constructUpwardUnions() {
		ArrayList<Union> upwardUnionsList = new ArrayList<Union>(); //use a list, as the number of upward unions is unknown, in general
		
		int numberOfLimitingDecisions = limitingDecisions.length;
		boolean nonPositiveDecisionFound;

		//iterate from the best to the worst limiting decision (check all decisions!)
		for (int i = numberOfLimitingDecisions - 1; i >= 0; i--) { //!
			nonPositiveDecisionFound = false;
			
			for (int j = 0; j < numberOfLimitingDecisions; j++) { //iterate in inverse order to maximize the chance of quickly finding non-positive decision
				if (!Union.isDecisionPositive(limitingDecisions[j], Union.UnionType.AT_LEAST, limitingDecisions[i], this.getInformationTable())) {
					nonPositiveDecisionFound = true;
					break;
				}
			} //for (j)
			
			if (nonPositiveDecisionFound) { //upward union with limiting decision equal to limitingDecisions[i] does not contain all objects (is meaningful)
				upwardUnionsList.add(new Union(Union.UnionType.AT_LEAST, limitingDecisions[i], this.getInformationTable(), this.getRoughSetCalculator()));
			}
		} //for (i)
		
		//copy unions from the temporary list to the array stored in this object
		this.upwardUnions = new Union[upwardUnionsList.size()];
		for (int i = 0; i < this.upwardUnions.length; i++) {
			this.upwardUnions[i] = upwardUnionsList.get(i);
		} //for (i)
	}
	
	/**
	 * Creates array of all meaningful (i.e., not containing all objects) downward unions, sorted from the most to the least specific union.
	 */
	void constructDownwardUnions() {
		ArrayList<Union> downwardUnionsList = new ArrayList<Union>(); //use a list, as the number of downward unions is unknown, in general
		
		int numberOfLimitingDecisions = limitingDecisions.length;
		boolean nonPositiveDecisionFound;

		//iterate from the worst to the best decision (check all decisions!)
		for (int i = 0; i < numberOfLimitingDecisions; i++) { //!
			nonPositiveDecisionFound = false;
			
			for (int j = numberOfLimitingDecisions - 1; j >= 0; j--) { //iterate in inverse order to maximize the chance of quickly finding non-positive decision
				if (!Union.isDecisionPositive(limitingDecisions[j], Union.UnionType.AT_MOST, limitingDecisions[i], this.getInformationTable())) {
					nonPositiveDecisionFound = true;
					break;
				}
			} //for (j)
			
			if (nonPositiveDecisionFound) { //downward union with limiting decision equal to limitingDecisions[i] does not contain all objects (is meaningful)
				downwardUnionsList.add(new Union(Union.UnionType.AT_MOST, limitingDecisions[i], this.getInformationTable(), this.getRoughSetCalculator()));
			}
		} //for (i)
		
		//copy unions from the temporary list to the array stored in this object
		this.downwardUnions = new Union[downwardUnionsList.size()];
		for (int i = 0; i < this.downwardUnions.length; i++) {
			this.downwardUnions[i] = downwardUnionsList.get(i);
		} //for (i)
	}
	
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
	 * Gets quality of approximation of all unions which can be defined for information table.
	 * 
	 * @return quality of approximation of all unions which can be defined for information table
	 */
	@Override
	public double getQualityOfApproximation() {
		// TODO: implement
		return 0;
	}
	
	/**
	 * Gets array containing all upward unions which can be defined for information table.
	 * Unions are sorted from the most specific to the least specific.
	 * 
	 * @return array containing all upward unions which can be defined for information table
	 */
	public Union[] getUpwardUnions() {
		return upwardUnions.clone();
	}
	
	/**
	 * Gets array containing all upward unions which can be defined for information table.
	 * Unions are sorted from the most specific to the least specific.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array containing all upward unions which can be defined for information table
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Union[] getUpwardUnions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? upwardUnions : upwardUnions.clone();
	}
	
	/**
	 * Gets array containing all downward unions which can be defined for information table.
	 * Unions are sorted from the most specific to the least specific.
	 * 
	 * @return array containing all downward unions which can be defined for information table
	 */
	public Union[] getDownwardUnions() {
		return downwardUnions.clone();
	}
	
	/**
	 * Gets array containing all downward unions which can be defined for information table.
	 * Unions are sorted from the most specific to the least specific.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array containing all downward unions which can be defined for information table
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Union[] getDownwardUnions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? downwardUnions : downwardUnions.clone();
	}
	
	/**
	 * Gets ordered (from the worst to the best) array of all distinct decisions, which can be found in information table.
	 * These decisions are limiting decisions of unions present in this union container.
	 * The order is such that each decision in the array is worse than decisions to the right
	 * being comparable to this decision, and better than decisions to the left being comparable to this decision.
	 * If any two decisions are incomparable, then their respective order may be arbitrary.
	 * 
	 * @return ordered (from the worst to the best) array of all distinct decisions, which can be found in information table
	 * @see InformationTable#calculateOrderedUniqueDecisions()
	 */
	public Decision[] getLimitingDecisions() {
		return limitingDecisions.clone();
	}
	
	/**
	 * Gets ordered (from the worst to the best) array of all distinct decisions, which can be found in information table.
	 * These decisions are limiting decisions of unions present in this union container.
	 * The order is such that each decision in the array is worse than decisions to the right
	 * being comparable to this decision, and better than decisions to the left being comparable to this decision.
	 * If any two decisions are incomparable, then their respective order may be arbitrary.<br>
	 * <br>
	 * This method can be used in certain circumstances to accelerate calculations.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return ordered (from the worst to the best) array of all distinct decisions, which can be found in information table
	 * @see InformationTable#calculateOrderedUniqueDecisions()
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Decision[] getLimitingDecisions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? limitingDecisions : limitingDecisions.clone();
	}
	
	/**
	 * Gets upward/downward union of decision classes (depending on union type), defined for given limiting decision.
	 * 
	 * @param unionType type of the union sought-after
	 * @param limitingDecision limiting decision of the union sought-after
	 * 
	 * @return upward/downward union of decision classes (depending on union type),
	 *         with given limiting decision, or {@code null} if the union sought-after is not present in this container
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public Union getUnion(Union.UnionType unionType, Decision limitingDecision) {
		notNull(unionType, "Cannot find union with null union type.");
		notNull(limitingDecision, "Cannot find union with null limiting decision.");
		
		Union unions[];
		
		if (unionType == Union.UnionType.AT_LEAST) {
			unions = upwardUnions;
		} else if (unionType == Union.UnionType.AT_MOST) {
			unions = downwardUnions;
		} else {
			throw new org.rulelearn.core.InvalidValueException("Incorrect type of a union."); //this should not happen
		}
		
		for (int i = 0; i < unions.length; i++) {
			if (unions[i].getLimitingDecision().equals(limitingDecision)) {
				return unions[i];
			}
		}
		
		return null; //if nothing found
	}

}
