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

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.dominance.DominanceConeCalculator;

import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSortedSets;

/**
 * Union of decision classes, concerning set of objects belonging to either of the considered decision classes.
 * TODO
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Union extends ApproximatedSet {
	
	/**
	 * Type of a union of decision classes.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static enum UnionType {
		/**
		 * Type of an upward union of decision classes.
		 */
		AT_LEAST,
		/**
		 * Type of an downward union of decision classes.
		 */
		AT_MOST
	}
	
	/**
	 * Type of this union. See {@link UnionType}.
	 */
	protected UnionType unionType;
	
	/**
	 * Reference to complementary union of decision classes that complements this union w.r.t. set of all objects U. This reference is useful, e.g., when calculating the upper approximation of this union using VC-DRSA
	 * (by complementing the lower approximation of the complementary union). Initialized with {@code null}. Can be updated by {@link #setComplementaryUnion(Union)} method.
	 */
	protected Union complementaryUnion = null;
	
//	/**
//	 * Set with indices of objects such that this union's limiting decision is uncomparable with their decision.
//	 * Limiting decision is considered to be uncomparable with a particular decision, if it is neither at least as good as nor at most as good as that decision.
//	 */
//	protected IntSortedSet uncomparableObjects;
	
	/**
	 * Set of objects from information table that are neither positive nor negative with respect to this union.  
	 */
	protected IntSortedSet neutralObjects;
	
	/**
	 * Constructs union of given type (at least or at most). Stores given information table.<br>
	 * <br>
	 * This is a minimal constructor that can be used to quickly construct a lightweight union,
	 * providing limited functionality. In particular, this constructor does not calculate objects belonging to this union (nor neutral objects),
	 * which is a time consuming process. Moreover, it does not set rough set calculator.
	 * 
	 * @param unionType see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param informationTable see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	Union(UnionType unionType, InformationTableWithDecisionDistributions informationTable) {
		super();
		
		this.unionType = notNull(unionType, "Union type is null.");
		this.informationTable = notNull(informationTable, "Information table for constructed union is null.");
	}
	
	/**
	 * TODO
	 * @param unionType
	 * @param informationTable
	 * @param roughSetCalculator
	 */
	Union(UnionType unionType, InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(informationTable, roughSetCalculator);
		this.unionType = notNull(unionType, "Union type is null.");
	}
	
	/**
	 * Finds (positive) objects belonging to this union and neutral objects.
	 */
	@Override
	protected void findObjects() {
		IntSortedSet objects = new IntLinkedOpenHashSet(); //TODO: estimate hash set capacity using distribution of decisions?
		IntSortedSet uncomparableObjects = new IntLinkedOpenHashSet(); //TODO: estimate hash set capacity using distribution of decisions?
		
		int objectsCount = this.informationTable.getNumberOfObjects();
		
		for (int i = 0; i < objectsCount; i++) {
			if (this.isDecisionPositive(this.informationTable.getDecision(i))) {
				objects.add(i);
			} else {
				if (this.isDecisionNeutral(this.informationTable.getDecision(i))) {
					uncomparableObjects.add(i);
				}
			}
		}
		
		this.objects = IntSortedSets.unmodifiable(objects);
		this.neutralObjects = IntSortedSets.unmodifiable(uncomparableObjects);
	}
	
	/**
	 * Gets set of objects from information table that are neither positive nor negative with respect to this union.
	 * 
	 * @return set of objects from information table that are neither positive nor negative with respect to this union
	 */
	@Override
	public IntSortedSet getNeutralObjects() {
		return this.neutralObjects;
	}

	/**
	 * Registers complementary union of decision classes that complements this union w.r.t. set of all objects U.
	 * This reference is useful, e.g., when calculating the upper approximation of this union using VC-DRSA
	 * (by complementing the lower approximation of the complementary union).
	 * Complementary union may be set only if it is not already stored in this union.
	 * 
	 * @param union complementary union of decision classes
	 * @return {@code true} if given union has been set as a complementary union,
	 *         {@code false} otherwise 
	 */
	public boolean setComplementaryUnion(Union union) {
		//accept change only if the complementary union has not been set nor calculated yet
		if (this.complementaryUnion == null) {
			this.complementaryUnion = union;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Calculates complementary union of decision classes that complements this union w.r.t. set of all objects U.
	 * 
	 * @return complementary union of decision classes
	 */
	protected abstract Union calculateComplementaryUnion();

	/**
	 * Gets complementary union of decision classes that complements this union w.r.t. set of all objects U.
	 * If complementary union has not been previously set
	 * using {@link #setComplementaryUnion(Union)} method, it is first calculated.
	 * 
	 * @return complementary union of decision classes 
	 */
	public Union getComplementaryUnion() {
		if (this.complementaryUnion == null) {
			this.complementaryUnion = calculateComplementaryUnion();
			this.complementaryUnion.setComplementaryUnion(this); //set this union as complementary to the returned one
		}
		
		return this.complementaryUnion;
	}
	
	/**
	 * Gets type of this union. See {@link UnionType}.
	 * 
	 * @return type of this union
	 */
	public UnionType getUnionType() {
		return unionType;
	}

	/**
	 * Gets the dominance-based rough set calculator used to calculate approximations of this union.
	 * 
	 * @return the dominance-based rough set calculator used to calculate approximations of this union
	 */
	@Override
	public DominanceBasedRoughSetCalculator getRoughSetCalculator() {
		return (DominanceBasedRoughSetCalculator)roughSetCalculator;
	}
	
	/**
	 * Calculates the negative region of this union.
	 * This region is composed of objects belonging to the positive region of the complementary union, but not to the positive region of this union.
	 * 
	 * @return the negative region of this union
	 */
	@Override
	protected IntSet calculateNegativeRegion() {
		IntSet complementaryUnionPositiveRegion = this.getComplementaryUnion().getPositiveRegion();
		IntSet positiveRegion = this.getPositiveRegion();
		
		IntSet negativeRegion = new IntOpenHashSet();
		
		IntIterator iterator = complementaryUnionPositiveRegion.iterator();
		int objectIndex;
		
		while (iterator.hasNext()) {
			objectIndex = iterator.nextInt();
			if (!positiveRegion.contains(objectIndex)) {
				negativeRegion.add(objectIndex);
			}
		}
		
		return negativeRegion;
	}
	

	
	/**
	 * TODO
	 * Tells if given decision is positive with respect to this union, i.e., in case of an upward union - limiting decision of this union is at most as good as the given decision,
	 * and in case of a downward union - limiting decision of this union is at least as good as the given decision.
	 * 
	 * @param decision decision to verify for being positive with respect to this union
	 * @return {@code true} if given decision is positive with respect to this union,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	@Override
	public boolean isDecisionPositive(Decision decision) {
		notNull(decision, "Decision tested for being positive with respect to union is null.");
		
		return this.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE;
	}
	
	/**
	 * TODO
	 * Tells if given decision is negative with respect to this union, i.e., in case of an upward union - limiting decision of this union is strictly better than the given decision,
	 * and in case of a downward union - limiting decision of this union is strictly worse than the given decision.
	 * 
	 * @param decision decision to verify for being negative with respect to this union
	 * @return {@code true} if given decision is negative with respect to this union,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	@Override
	public boolean isDecisionNegative(Decision decision) {
		notNull(decision, "Decision tested for being negative with respect to union is null.");
		
		return this.isConcordantWithDecision(decision) == TernaryLogicValue.FALSE;
	}
	
	/**
	 * TODO
	 * Tells if given decision is neutral with respect to this union, i.e., limiting decision of this union is neither at most as good as the given decision nor at least as good as the given decision.
	 * 
	 * @param decision decision to verify for being neutral with respect to this union
	 * @return {@code true} if given decision is neutral with respect to this union,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	protected boolean isDecisionNeutral(Decision decision) {
		notNull(decision, "Decision tested for being neutral with union is null.");
		
		return this.isConcordantWithDecision(decision) == TernaryLogicValue.UNCOMPARABLE;
	}
	
	/**
	 * Gets the information table for which this union was defined.
	 * 
	 * @return the information table for which this union was defined
	 */
	public InformationTableWithDecisionDistributions getInformationTable() {
		return (InformationTableWithDecisionDistributions)informationTable;
	}

//	/**
//	 * Gets indices of objects from the information table such that this union's limiting decision is uncomparable with their decision.
//	 * Limiting decision is considered to be uncomparable with a particular decision, if it is neither at least as good as nor at most as good as that decision.
//	 * 
//	 * @return indices of objects from the information table such that this union's limiting decision is uncomparable with their decision
//	 */
//	@Override
//	public IntSortedSet getUncomparableObjects() {
//		return this.uncomparableObjects; //uncomparable objects have been calculated in class constructor, so just return them
//	}
	
//	/**
//	 * Tells if this union's limiting decision is uncomparable with decision of a particular object from the information table.
//	 * 
//	 * @param objectNumber index of an object from the information table
//	 * @return {@code true} if this union's limiting decision is uncomparable with decision assigned to the object with given index,
//	 *         {@code false} otherwise
//	 */
//	public boolean objectIsUncomparable(int objectNumber) {
//		return this.uncomparableObjects.contains(objectNumber);
//	}
	
	/**
	 * Tells if given object is positive with respect to this union.
	 * 
	 * @param objectNumber index of an object from the information table
	 * @return {@code true} if object with given number is positive with respect to this union,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean isObjectPositive(int objectNumber) {
		return this.objects.contains(objectNumber);
	}
	
	/**
	 * Tells if given object is neutral with respect to this union.
	 * 
	 * @param objectNumber index of an object from the information table
	 * @return {@code true} if object with given number is neutral with respect to this union,
	 *         {@code false} otherwise
	 */
	protected boolean isObjectNeutral(int objectNumber) {
		return this.neutralObjects.contains(objectNumber);
	}
	
	/**
	 * Tells if given object is negative with respect to this union.
	 * 
	 * @param objectNumber index of an object from the information table
	 * @return {@code true} if object with given number is negative with respect to this union,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean isObjectNegative(int objectNumber) {
		return !this.objects.contains(objectNumber) &&
				!this.neutralObjects.contains(objectNumber);
	}

	/**
	 * Calculates lower approximation of this union, using the dominance-based rough set calculator.
	 * 
	 * @return set of indices of objects belonging to the lower approximation of this union, calculated using the dominance-based rough set calculator
	 */
	@Override
	protected IntSortedSet calculateLowerApproximation() {
		return this.getRoughSetCalculator().calculateLowerApproximation(this);
	}

	/**
	 * Calculates upper approximation of this union, using the dominance-based rough set calculator.
	 * 
	 * @return set of indices of objects belonging to the upper approximation of this union, calculated using the dominance-based rough set calculator
	 */
	@Override
	protected IntSortedSet calculateUpperApproximation() {
		return this.getRoughSetCalculator().calculateUpperApproximation(this);
	}
	
	/**
	 * Calculates positive region of this union, using the given lower approximation.
	 * This region is composed of objects belonging to the lower approximation of this union plus
	 * objects belonging to dominance cones defined with respect to the objects from the lower approximation.
	 * 
	 * @return set of indices of objects belonging to the positive region of this union, calculated using given lower approximation
	 * @throws NullPointerException if given lower approximation is {@code null}
	 */
	@Override
	protected IntSet calculatePositiveRegion(IntSortedSet lowerApproximation) {
		notNull(lowerApproximation, "Lower approximation for calculation of positive region is null.");
		IntSet positiveRegion = new IntOpenHashSet(lowerApproximation.size()); //use estimation of the size of calculated positive region
		
		IntBidirectionalIterator iterator = lowerApproximation.iterator();
		int objectIndex;
		IntSortedSet dominanceCone;
		
		while (iterator.hasNext()) {
			objectIndex = iterator.nextInt();
		
			switch (this.getUnionType()) {
			case AT_LEAST:
				dominanceCone = DominanceConeCalculator.INSTANCE.calculatePositiveInvDCone(objectIndex, this.informationTable); //SIC! hardcoded type of dominance cone
				break;
			case AT_MOST:
				dominanceCone = DominanceConeCalculator.INSTANCE.calculateNegativeDCone(objectIndex, this.informationTable); //SIC! hardcoded type of dominance cone
				break;
			default:
				throw new InvalidValueException("Unexpected union type."); //this should not happen
			}
			
			positiveRegion.addAll(dominanceCone);
		}
		
		return positiveRegion;
	}
	
	/**
	 * Gets the size of the set of objects that is complementary to the set of objects belonging to this union.
	 * The result is calculated as the number of all objects in the information tables minus number of objects belonging to this union,
	 * and minus number of objects that are neutral with respect to this union.
	 * 
	 * @return the size of the set of objects that is complementary to the set of (positive) objects belonging to this union
	 */
	public int getComplementarySetSize() {
		return this.informationTable.getNumberOfObjects() - this.size() - this.neutralObjects.size();
	}
	
}
