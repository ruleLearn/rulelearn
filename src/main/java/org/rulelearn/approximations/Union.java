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

import org.rulelearn.data.InformationTableWithDecisionClassDistributions;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Union of ordered decision classes, i.e., set of objects whose decision class is not worse or not better than given limiting decision class.
 * TODO: write javadoc
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Union extends ApproximatedSet {
	
	protected UnionType unionType;
	protected EvaluationField limitingDecision;
	protected DominanceBasedRoughSetCalculator roughSetCalculator;
	
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
	 * Reference to opposite union of decision classes that complements this union w.r.t. set of all objects U. This reference is useful when calculating the upper approximation of this union
	 * by complementing the lower approximation of the opposite union. Initialized with {@code null}. Can be updated by {@link #registerComplementaryUnion(Union)} method.
	 */
	protected Union complementaryUnion = null;
	
	/**
	 * Constructs this union.
	 * 
	 * @param unionType type of this union; see {@link UnionType}
	 * @param limitingDecision value of active decision criterion that serves as a limit for this union; e.g., value 3 is a limit for union "at least 3" and "at most 3" 
	 * @param informationTable information table with considered objects, some of which belong to this union
	 * @param roughSetCalculator object capable of calculating lower/upper approximation of this union
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public Union(UnionType unionType, EvaluationField limitingDecision, InformationTableWithDecisionClassDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(informationTable);
		
		//TODO: add validation
		
		this.unionType = unionType;
		this.limitingDecision = limitingDecision;
		this.roughSetCalculator = roughSetCalculator;
		
		//TODO: validate presence and preference type of active decision attribute
	}
	
	/**
	 * Remembers opposite union of decision classes that complements this union w.r.t. set of all objects U. This reference is useful when calculating the upper approximation of this union
	 * by complementing the lower approximation of the opposite union.
	 * 
	 * @param union opposite union of decision classes; e.g., if there are five decision classes: 1, 2, 3, 4, 5, and this union concerns classes 3-5 (^gt;=3), then the opposite union concerns classes 1-2 (&lt;=2)
	 */
	public void registerComplementaryUnion(Union union) {
		//accept change if upper appr. not already calculated
		if (this.upperApproximation == null) {
			this.complementaryUnion = union;
		}
	}

	/**
	 * Gets opposite union of decision classes that complements this union w.r.t. set of all objects U.
	 * 
	 * @return opposite union of decision classes; e.g., if there are five decision classes: 1, 2, 3, 4, 5, and this union concerns classes 3-5 (&gt;=3), then the opposite union concerns classes 1-2 (&lt;=2)
	 */
	public Union getComplementaryUnion() {
		return this.complementaryUnion;
	}

	/**
	 * @return the lowerApproximation
	 */
	public IntSortedSet getLowerApproximation() {
		return lowerApproximation;
	}

	/**
	 * @return the upperApproximation
	 */
	public IntSortedSet getUpperApproximation() {
		return upperApproximation;
	}

	/**
	 * @return the boundary
	 */
	public IntSortedSet getBoundary() {
		return boundary;
	}

	/**
	 * @return the unionType
	 */
	public UnionType getUnionType() {
		return unionType;
	}

	/**
	 * @return the limitingDecision
	 */
	public EvaluationField getLimitingDecision() {
		return limitingDecision;
	}

	/**
	 * @return the roughSetCalculator
	 */
	public DominanceBasedRoughSetCalculator getRoughSetCalculator() {
		return roughSetCalculator;
	}
	
	//TODO: implement
}
