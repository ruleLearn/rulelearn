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

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import static org.rulelearn.core.Precondition.notNull;

/**
 * Union of ordered decision classes, i.e., set of objects whose decision class is not worse or not better than given limiting decision class.
 * TODO: write javadoc
 * TODO: neutral objects?
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Union extends ApproximatedSet {
	
	/**
	 * Type of union of decision classes.
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
	 * Type of this union.
	 */
	protected UnionType unionType;
	/**
	 * Limiting (boundary) decision of this union.
	 */
	protected Decision limitingDecision;
	/**
	 * Dominance-based rough set calculator used to calculate approximations and boundary of this union.
	 */
	protected DominanceBasedRoughSetCalculator roughSetCalculator;
	
	/**
	 * Reference to opposite union of decision classes that complements this union w.r.t. set of all objects U. This reference is useful when calculating the upper approximation of this union
	 * by complementing the lower approximation of the opposite union. Initialized with {@code null}. Can be updated by {@link #setComplementaryUnion(Union)} method.
	 */
	protected Union complementaryUnion = null;
	
	/**
	 * Constructs this union.
	 * 
	 * @param unionType type of this union; see {@link UnionType}
	 * @param limitingDecision decision that serves as a limit for this union; e.g., decision "3" is a limit for union "at least 3" and "at most 3" 
	 * @param informationTable information table with considered objects, some of which belong to this union
	 * @param roughSetCalculator object capable of calculating lower/upper approximation of this union
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public Union(UnionType unionType, Decision limitingDecision, InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(informationTable);
		
		//TODO: add validation
		
		this.unionType = unionType;
		this.limitingDecision = limitingDecision;
		this.roughSetCalculator = roughSetCalculator;
		
		//TODO: validate presence and preference type of active decision attribute
	}
	
	/**
	 * Registers opposite union of decision classes that complements this union w.r.t. set of all objects U.
	 * This reference is useful when calculating the upper approximation of this union
	 * by complementing the lower approximation of the opposite union.
	 * Complementary union may be set only if the upper approximation of this union has not been calculated yet 
	 * 
	 * @param union opposite union of decision classes; e.g., if there are five decision classes: 1, 2, 3, 4, 5,
	 *        and this union concerns classes 3-5 (^gt;=3), then the opposite union concerns classes 1-2 (&lt;=2)
	 * @return {@code true} if the upper approximation of this union has not been calculated yet,
	 *         {@code false} otherwise 
	 */
	public boolean setComplementaryUnion(Union union) {
		//accept change if upper appr. not already calculated
		if (this.upperApproximation == null) {
			this.complementaryUnion = union;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets opposite union of decision classes that complements this union w.r.t. set of all objects U.
	 * Can be {@code null} if complementary union has not been set using {@link #setComplementaryUnion(Union)} method.
	 * 
	 * @return opposite union of decision classes; e.g., if there are five decision classes: 1, 2, 3, 4, 5, and this union concerns classes 3-5 (&gt;=3),
	 *         then the opposite union concerns classes 1-2 (&lt;=2)
	 */
	public Union getComplementaryUnion() {
		return this.complementaryUnion;
	}

	/**
	 * Gets sorted set of indices of objects belonging to the lower approximation of this union.
	 * 
	 * @return sorted set of indices of objects belonging to the lower approximation of this union
	 */
	public IntSortedSet getLowerApproximation() {
		return lowerApproximation;
	}

	/**
	 * Gets sorted set of indices of objects belonging to the upper approximation of this union.
	 * 
	 * @return sorted set of indices of objects belonging to the upper approximation of this union
	 */
	public IntSortedSet getUpperApproximation() {
		return upperApproximation;
	}

	/**
	 * Gets sorted set of indices of objects belonging to the boundary of this union.
	 * 
	 * @return sorted set of indices of objects belonging to the boundary of this union
	 */
	public IntSortedSet getBoundary() {
		return boundary;
	}

	/**
	 * Gets type of this union.
	 * 
	 * @return type of this union
	 */
	public UnionType getUnionType() {
		return unionType;
	}

	/**
	 * Gets limiting (boundary) decision of this union.
	 * @return limiting (boundary) decision of this union
	 */
	public Decision getLimitingDecision() {
		return limitingDecision;
	}

	/**
	 * Gets the dominance-based rough set calculator used to calculate approximations and boundary of this union.
	 * 
	 * @return the dominance-based rough set calculator used to calculate approximations and boundary of this union
	 */
	public DominanceBasedRoughSetCalculator getRoughSetCalculator() {
		return roughSetCalculator;
	}

	/**
	 * Gets the negative region of this union, i.e., the positive region of the complementary union.
	 * 
	 * @return the negative region of this union, i.e., the positive region of the complementary union
	 */
	@Override
	public IntSortedSet getNegativeRegion() {
		// TODO: implement using complementaryUnion
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Tests if this union is compatible with given decision. In case of an upward union, tests if limiting decision of this union is at most as good as the given decision.
	 * In case of a downward union, tests if limiting decision of this union is at least as good as the given decision.
	 * 
	 * @param decision decision that limiting decision of this union is being compared with
	 * @return {@code true} if given decision is compatible with this union, {@code false} otherwise
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	public boolean isCompatibleWithDecision(Decision decision) {
		notNull(decision, "Decision tested for compatibility with union is null.");
		
		switch (this.unionType) {
		case AT_LEAST:
			return this.limitingDecision.isAtMostAsGoodAs(decision) == TernaryLogicValue.TRUE;
		case AT_MOST:
			return this.limitingDecision.isAtLeastAsGoodAs(decision) == TernaryLogicValue.TRUE;
		default:
			throw new InvalidValueException("Unexpected union type."); //this should not happen
		}
	}
	
	//TODO: implement further methods
}
