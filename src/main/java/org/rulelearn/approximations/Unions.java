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

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

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
	 * Ordered (from the worst to the best) array of all distinct decisions, which can be found in information table.
	 * Each decision class is present only once.
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
	 */
	public Unions(InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(informationTable, roughSetCalculator);
	}
	
	/**
	 * Calculates ordered (from the worst to the best) array of all distinct decisions, which can be found in memory container.
	 */
	protected void calculateLimitingDecisions() {
		//TODO: implement
	}
	
	/**
	 * Creates array of all upward unions, sorted from the most to the least specific union.
	 */
	void constructUpwardUnions() {
		//TODO: implement
	}
	
	/**
	 * Creates array of all downward unions, sorted from the most to the least specific union.
	 */
	void constructDownwardUnions() {
		//TODO: implement
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
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Decision[] getLimitingDecisions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? limitingDecisions : limitingDecisions.clone();
	}
	
	/**
	 * Gets upward/downward union of decision classes (depending on union type), defined for given decision.
	 * 
	 * @param unionType type of the union sought-after
	 * @param limitingDecision limiting decision of the union sought-after
	 * 
	 * @return upward/downward union of decision classes (depending on union type),
	 *         with given limiting decision, or {@code null} if the union sought-after is not present in this container
	 */
	public Union getUnion(Union.UnionType unionType, Decision limitingDecision) {
		return null; //TODO: implement
	}

}
