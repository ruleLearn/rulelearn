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
import java.util.Set;

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.CompositeDecision;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

/**
 * Container for all upward and downward unions (i.e., unions of type {@link Union.UnionType#AT_LEAST}, and {@link Union.UnionType#AT_MOST}, respectively),
 * with single limiting decision, that can be defined with respect to an information table.
 * Stores and retrieves unions of type {@link UnionWithSingleLimitingDecision}.<br>
 * <br>
 * Upward/downward unions supplied by this container are sorted from the most specific to the least specific one.
 * For example - if possible values of a decision criterion are: 1, 2 and 3
 * and preference type for this criterion is gain then:
 * <ul>
 * <li>{@link #getUpwardUnions()}[0] yields union "class 3 and better",</li>
 * <li>{@link #getUpwardUnions()}[1] yields union "class 2 and better".</li>
 * </ul>
 * Moreover:
 * <ul>
 * <li>{@link #getDownwardUnions()}[0] yields union "class 1 and worse",</li>
 * <li>{@link #getDownwardUnions()}[1] yields union "class 2 and worse".</li>
 * </ul>
 * If information table contains more than one active decision attribute, then each object is assigned a composite decision
 * (i.e., {@link InformationTable#getDecision(int)} returns {@link CompositeDecision}. In such case, some decisions may be incomparable (mutually non-dominated).
 *  Consequently, unions having incomparable limiting decisions are also incomparable. The order of incomparable unions is arbitrary (i.e., any of the two unions may be
 *  first, followed by the other union).<br>
 *  <br>
 *  Upward/downward unions returned by this union container are defined only for such limiting decisions, that are fully determined (i.e., do not involve a missing attribute
 *  value).
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnionsWithSingleLimitingDecision extends Unions {
	
	/**
	 * Ordered (from the worst to the best) array of all unique (distinct) fully-determined decisions, which can be found in the information table.
	 * Each decision is present only once.
	 * The order is such that each decision in the array is worse than decisions to the right
	 * being comparable to this decision, and better than decisions to the left being comparable to this decision.
	 * If two decisions are incomparable, then their respective order is not constrained and may be any.<br>
	 * <br>
	 * See {@link InformationTable#calculateOrderedUniqueFullyDeterminedDecisions()}.
	 */
	Decision[] limitingDecisions = null;
	
	/**
	 * Constructs this container of upward and downward unions with single limiting decision that can be defined with respect to an information table.
	 * 
	 * @param informationTable information table for which all unions stored in this container are defined
	 * @param roughSetCalculator rough set calculator used to calculate approximations of all unions stored in this container
	 * 
	 * @throws NullPointerException if given information table does not store decisions for subsequent objects
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidSizeException if given information table does not contain at least one fully-determined decision - see {@link Decision#hasNoMissingEvaluation()}
	 */
	public UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(informationTable, roughSetCalculator);
		
		//calculate limiting decisions
		limitingDecisions = informationTable.calculateOrderedUniqueFullyDeterminedDecisions();
		
		if (limitingDecisions == null) {
			throw new NullPointerException("Information table does not store decisions for subsequent objects.");
		}
		
		if (limitingDecisions.length < 1) {
			throw new InvalidSizeException("Cannot create unions for less than one fully-determined decision.");
		}
		
		//create all upward and downward unions
		calculateUpwardUnions();
		calculateDownwardUnions();
	}
	
	/**
	 * {@inheritDoc}
	 */
	void calculateUpwardUnions() {
		ArrayList<Union> upwardUnionsList = new ArrayList<Union>(); //use a list, as the number of upward unions is unknown, in general
		
		int numberOfLimitingDecisions = limitingDecisions.length;
		boolean nonPositiveDecisionFound;
		
		Set<Decision> allUniqueDecisions = this.getInformationTable().getDecisionDistribution().getDecisions();
		
		//all decisions are fully-determined => make use of order of decisions
		if (limitingDecisions.length == allUniqueDecisions.size()) {
			//iterate from the best to the worst limiting decision (check all decisions!)
			for (int i = numberOfLimitingDecisions - 1; i >= 0; i--) { //!
				nonPositiveDecisionFound = false;
				
				for (int j = 0; j < numberOfLimitingDecisions; j++) { //iterate in inverse order to maximize the chance of quickly finding non-positive decision
					if (!UnionWithSingleLimitingDecision.isDecisionPositive(limitingDecisions[j], Union.UnionType.AT_LEAST, limitingDecisions[i], this.getInformationTable())) {
						//there is at least one non-positive decision for the union to be constructed => it makes sense to construct this union
						//as it will not contain all objects from the information table
						nonPositiveDecisionFound = true; 
						break;
					}
				} //for (j)
				
				if (nonPositiveDecisionFound) { //upward union with limiting decision equal to limitingDecisions[i] does not contain all objects (is meaningful)
					upwardUnionsList.add(new UnionWithSingleLimitingDecision(Union.UnionType.AT_LEAST, limitingDecisions[i], this.getInformationTable(), this.getRoughSetCalculator()));
				}
			} //for (i)
		}
		//not all decisions are fully-determined => check all decisions
		else {
			//iterate from the best to the worst limiting decision (check all decisions!)
			for (int i = numberOfLimitingDecisions - 1; i >= 0; i--) { //!
				nonPositiveDecisionFound = false;
				
				for (Decision decision : allUniqueDecisions) {
					if (!UnionWithSingleLimitingDecision.isDecisionPositive(decision, Union.UnionType.AT_LEAST, limitingDecisions[i], this.getInformationTable())) {
						nonPositiveDecisionFound = true;
						break;
					}
				}
				
				if (nonPositiveDecisionFound) { //upward union with limiting decision equal to limitingDecisions[i] does not contain all objects (is meaningful)
					upwardUnionsList.add(new UnionWithSingleLimitingDecision(Union.UnionType.AT_LEAST, limitingDecisions[i], this.getInformationTable(), this.getRoughSetCalculator()));
				}
			} //for (i)
		}
		
		//copy unions from the temporary list to the array stored in this object
		this.upwardUnions = new Union[upwardUnionsList.size()];
		for (int i = 0; i < this.upwardUnions.length; i++) {
			this.upwardUnions[i] = upwardUnionsList.get(i);
		} //for (i)
	}
	
	/**
	 * {@inheritDoc}
	 */
	void calculateDownwardUnions() {
		ArrayList<Union> downwardUnionsList = new ArrayList<Union>(); //use a list, as the number of downward unions is unknown, in general
		
		int numberOfLimitingDecisions = limitingDecisions.length;
		boolean nonPositiveDecisionFound;
		
		Set<Decision> allUniqueDecisions = this.getInformationTable().getDecisionDistribution().getDecisions();

		//all decisions are fully-determined => make use of order of decisions
		if (limitingDecisions.length == allUniqueDecisions.size()) {
			//iterate from the worst to the best decision (check all decisions!)
			for (int i = 0; i < numberOfLimitingDecisions; i++) { //!
				nonPositiveDecisionFound = false;
				
				for (int j = numberOfLimitingDecisions - 1; j >= 0; j--) { //iterate in inverse order to maximize the chance of quickly finding non-positive decision
					if (!UnionWithSingleLimitingDecision.isDecisionPositive(limitingDecisions[j], Union.UnionType.AT_MOST, limitingDecisions[i], this.getInformationTable())) {
						//there is at least one non-positive decision for the union to be constructed => it makes sense to construct this union
						//as it will not contain all objects from the information table
						nonPositiveDecisionFound = true;
						break;
					}
				} //for (j)
				
				if (nonPositiveDecisionFound) { //downward union with limiting decision equal to limitingDecisions[i] does not contain all objects (is meaningful)
					downwardUnionsList.add(new UnionWithSingleLimitingDecision(Union.UnionType.AT_MOST, limitingDecisions[i], this.getInformationTable(), this.getRoughSetCalculator()));
				}
			} //for (i)
		}
		//not all decisions are fully-determined => check all decisions
		else {
			//iterate from the worst to the best decision (check all decisions!)
			for (int i = 0; i < numberOfLimitingDecisions; i++) { //!
				nonPositiveDecisionFound = false;
				
				for (Decision decision : allUniqueDecisions) {
					if (!UnionWithSingleLimitingDecision.isDecisionPositive(decision, Union.UnionType.AT_MOST, limitingDecisions[i], this.getInformationTable())) {
						nonPositiveDecisionFound = true;
						break;
					}
				} //for
				
				if (nonPositiveDecisionFound) { //downward union with limiting decision equal to limitingDecisions[i] does not contain all objects (is meaningful)
					downwardUnionsList.add(new UnionWithSingleLimitingDecision(Union.UnionType.AT_MOST, limitingDecisions[i], this.getInformationTable(), this.getRoughSetCalculator()));
				}
			} //for (i)
		}
		
		//copy unions from the temporary list to the array stored in this object
		this.downwardUnions = new Union[downwardUnionsList.size()];
		for (int i = 0; i < this.downwardUnions.length; i++) {
			this.downwardUnions[i] = downwardUnionsList.get(i);
		} //for (i)
	}
	
	/**
	 * Gets ordered (from the worst to the best) array of all distinct fully-determined decisions, which can be found in information table.
	 * These decisions are limiting decisions of unions present in this union container.
	 * The order is such that each decision in the array is worse than decisions to the right
	 * being comparable to this decision, and better than decisions to the left being comparable to this decision.
	 * If any two decisions are incomparable, then their respective order may be arbitrary.<br>
	 * <br>
	 * See {@link InformationTable#calculateOrderedUniqueFullyDeterminedDecisions()}.
	 * 
	 * @return ordered (from the worst to the best) array of all distinct decisions, which can be found in information table
	 * @see InformationTable#calculateOrderedUniqueFullyDeterminedDecisions()
	 */
	public Decision[] getLimitingDecisions() {
		return limitingDecisions.clone();
	}
	
	/**
	 * Gets ordered (from the worst to the best) array of all distinct fully-determined decisions, which can be found in information table.
	 * These decisions are limiting decisions of unions present in this union container.
	 * The order is such that each decision in the array is worse than decisions to the right
	 * being comparable to this decision, and better than decisions to the left being comparable to this decision.
	 * If any two decisions are incomparable, then their respective order may be arbitrary.<br>
	 * <br>
	 * See {@link InformationTable#calculateOrderedUniqueFullyDeterminedDecisions()}.<br>
	 * <br>
	 * This method can be used in certain circumstances to accelerate calculations.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return ordered (from the worst to the best) array of all distinct decisions, which can be found in information table
	 * @see InformationTable#calculateOrderedUniqueFullyDeterminedDecisions()
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Decision[] getLimitingDecisions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? limitingDecisions : limitingDecisions.clone();
	}
	
	/**
	 * Gets stored upward/downward union of decision classes (depending on given union type), defined for given limiting decision.
	 * 
	 * @param unionType type of the union sought-after
	 * @param limitingDecision limiting decision of the union sought-after
	 * 
	 * @return upward/downward union of decision classes (depending on given union type),
	 *         with given limiting decision, or {@code null} if the union sought-after is not present in this container
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public UnionWithSingleLimitingDecision getUnion(Union.UnionType unionType, Decision limitingDecision) {
		notNull(unionType, "Cannot find union with null union type.");
		notNull(limitingDecision, "Cannot find union with null limiting decision.");
		
		Union unions[];
		UnionWithSingleLimitingDecision union;
		
		if (unionType == Union.UnionType.AT_LEAST) {
			unions = upwardUnions;
		} else if (unionType == Union.UnionType.AT_MOST) {
			unions = downwardUnions;
		} else {
			throw new org.rulelearn.core.InvalidValueException("Incorrect type of a union."); //this should not happen
		}
		
		for (int i = 0; i < unions.length; i++) {
			union = (UnionWithSingleLimitingDecision)unions[i];
			if (union.getLimitingDecision().equals(limitingDecision)) {
				return union;
			}
		}
		
		return null; //if nothing found
	}
	
}
