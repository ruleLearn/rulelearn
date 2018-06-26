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

import org.rulelearn.approximations.Union.UnionType;
//import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.dominance.DominanceConesDecisionDistributions;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Calculator of rough approximations of a union of decision classes according to the classical definition of Dominance-based Rough Set Approach (DRSA or CDRSA).
 * 
 * The precise definition implemented here is more general and allows proper handling of missing values. 
 * It involves standard and inverted dominance relations as defined in M. Szeląg, J. Błaszczyński, R. Słowiński, 
 * Rough Set Analysis of Classification Data with Missing Values. [In]:
 * L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017, Proceedings, Part I.
 * Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.   
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ClassicalDominanceBasedRoughSetCalculator implements DominanceBasedRoughSetCalculator {
	
	/**
	 * Reflexive property of dominance relations used to compare objects.
	 */
	protected boolean reflexiveDominanceRelations = true;
 	
	/**
	 * Constructs calculator of lower approximation of a union of decision classes for reflexive dominance relations (i.e., standard case). 
	 */
	public ClassicalDominanceBasedRoughSetCalculator () {
		super();
		this.reflexiveDominanceRelations = true;
	}
	
	/**
	 * Constructs calculator of lower approximation of a union of decision classes and sets reflexive property of dominance relations used to compare objects.
	 * 
	 * @param reflexiveDominanceRelations reflexive property of dominance relations used to compare objects
	 */
	public ClassicalDominanceBasedRoughSetCalculator (boolean reflexiveDominanceRelations) {
		super();
		this.reflexiveDominanceRelations = reflexiveDominanceRelations;
	}
	
	/**
	 * Calculates lower approximation of a union of decision classes.
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return set of indices of objects belonging to the lower approximation of this approximated set
	 */
	@Override
	public IntSortedSet calculateLowerApproximation(Union union) {
		if (this.reflexiveDominanceRelations) { 
			return calculateLowerApproximationForReflexiveDominanceRelations(union);
		}
		else {
			return calculateLowerApproximationForNotReflexiveDominanceRelations(union);
		}
	}
	
	/**
	 * Calculates lower approximation of a union of decision classes for dominance relations, which are reflexive.
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return set of indices of objects belonging to the lower approximation of this approximated set
	 */
	public IntSortedSet calculateLowerApproximationForReflexiveDominanceRelations(Union union) {
		InformationTableWithDecisionDistributions infromationTable = union.getInformationTable(); 
		IntIterator unionObjectIndicesIterator  = union.getObjects().iterator();
		DominanceConesDecisionDistributions dominanceCDD = infromationTable.getDominanceConesDecisionDistributions();
		IntSortedSet lowerApproximationObjects = null;  
		boolean canBeAdded = false;
		
		if (union.getUnionType() == UnionType.AT_LEAST) {
			lowerApproximationObjects = new IntLinkedOpenHashSet();
			int i = 0;
			while (unionObjectIndicesIterator.hasNext()) {
				i = unionObjectIndicesIterator.nextInt();
				canBeAdded = false;
				for (Decision decision : dominanceCDD.getPositiveInvDConeDecisionClassDistribution(i).getDecisions()) {
					// check whether some objects not concordant with union (i.e. not in the set and not uncomparable) are present in a positive inverted dominance cone based on the object
					//if (union.isConcordantWithDecision(decision) == TernaryLogicValue.FALSE) {
					if (union.isDecisionNegative(decision)) {
						canBeAdded = false;
						break;
					}
					// check whether some objects concordant with union are present in a positive inverted dominance cone based on the object - not necessary
					/*else if (set.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {	
						canBeAdded = true;
					}*/
				}
				if (canBeAdded) {
					lowerApproximationObjects.add(i);
				}
			}
		}
		else if (union.getUnionType() == UnionType.AT_MOST) {
			lowerApproximationObjects = new IntLinkedOpenHashSet();
			int i = 0;
			while (unionObjectIndicesIterator.hasNext()) {
				i = unionObjectIndicesIterator.nextInt();
				canBeAdded = false;
				for (Decision decision : dominanceCDD.getNegativeDConeDecisionClassDistribution(i).getDecisions()) {
					// check whether some objects not concordant with union (i.e. not in the set and not uncomparable) are present in a negative dominance cone based on the object
					//if (union.isConcordantWithDecision(decision) == TernaryLogicValue.FALSE) {
					if (union.isDecisionNegative(decision)) {
						canBeAdded = false;
						break;
					}
					// check whether some objects concordant with union are present in a negative dominance cone based on the object - not necessary
					/*else if (set.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {
						canBeAdded = true;
					}*/
				}
				if (canBeAdded) {
					lowerApproximationObjects.add(i);
				}
			}
		}
		return lowerApproximationObjects;
	}
	
	/**
	 * Calculates lower approximation of a union of decision classes for dominance relations, which are not reflexive.
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return set of indices of objects belonging to the lower approximation of this approximated set
	 */
	public IntSortedSet calculateLowerApproximationForNotReflexiveDominanceRelations(Union union) {
		InformationTableWithDecisionDistributions infromationTable = union.getInformationTable(); 
		int objectsCount = infromationTable.getNumberOfObjects();
		DominanceConesDecisionDistributions dominanceCDD = infromationTable.getDominanceConesDecisionDistributions();
		IntSortedSet lowerApproximationObjects = null;  
		boolean canBeAdded = false;
		
		if (union.getUnionType() == UnionType.AT_LEAST) {
			lowerApproximationObjects = new IntLinkedOpenHashSet();
			for (int i = 0; i < objectsCount; i++) {
				canBeAdded = false;
				for (Decision decision : dominanceCDD.getPositiveInvDConeDecisionClassDistribution(i).getDecisions()) {
					// check whether some objects not concordant with union (i.e. not in the set and not uncomparable) are present in a positive inverted dominance cone based on the object
					//if (union.isConcordantWithDecision(decision) == TernaryLogicValue.FALSE) {
					if (union.isDecisionNegative(decision)) {
						canBeAdded = false;
						break;
					}
					// check whether some objects concordant with union are present in a positive inverted dominance cone based on the object - not necessary
					/*else if (set.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {	
						canBeAdded = true;
					}*/
				}
				if (canBeAdded) {
					lowerApproximationObjects.add(i);
				}
			}
		}
		else if (union.getUnionType() == UnionType.AT_MOST) {
			lowerApproximationObjects = new IntLinkedOpenHashSet();
			for (int i = 0; i < objectsCount; i++) {
				canBeAdded = false;
				for (Decision decision : dominanceCDD.getNegativeDConeDecisionClassDistribution(i).getDecisions()) {
					// check whether some objects not concordant with union (i.e. not in the set and not uncomparable) are present in a negative dominance cone based on the object
					//if (union.isConcordantWithDecision(decision) == TernaryLogicValue.FALSE) {
					if (union.isDecisionNegative(decision)) {
						canBeAdded = false;
						break;
					}
					// check whether some objects concordant with union are present in a negative dominance cone based on the object - not necessary
					/*else if (set.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {
						canBeAdded = true;
					}*/
				}
				if (canBeAdded) {
					lowerApproximationObjects.add(i);
				}
			}
		}
		return lowerApproximationObjects;
	}
	
	/**
	 * Calculates upper approximation of a union of decision classes.
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return sorted set of indices of objects belonging to the upper approximation of the union
	 */
	@Override
	public IntSortedSet calculateUpperApproximation(Union union) {
		InformationTableWithDecisionDistributions infromationTable = union.getInformationTable(); 
		int objectsCount = infromationTable.getNumberOfObjects();
		DominanceConesDecisionDistributions dominanceCDD = infromationTable.getDominanceConesDecisionDistributions();
		IntSortedSet upperApproximationObjects = null;
		
		if (union.getUnionType() == UnionType.AT_LEAST) {
			upperApproximationObjects = new IntLinkedOpenHashSet();
			for (int i = 0; i < objectsCount; i++) {
				// check whether some objects from the set (i.e., union) are present in a negative dominance cone based on the object
				for (Decision decision : dominanceCDD.getNegativeDConeDecisionClassDistribution(i).getDecisions()) {
					//if (union.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {
					if (union.isDecisionPositive(decision)) {
						upperApproximationObjects.add(i);
						break;
					}
				}
			}
		}
		else if (union.getUnionType() == UnionType.AT_MOST) {
			upperApproximationObjects = new IntLinkedOpenHashSet();
			for (int i = 0; i < objectsCount; i++) {
				// check whether some objects from the set (i.e., union) are present in a positive inverted dominance cone based on the object 
				for (Decision decision : dominanceCDD.getPositiveInvDConeDecisionClassDistribution(i).getDecisions()) {
					//if (union.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {
					if (union.isDecisionPositive(decision)) {
						upperApproximationObjects.add(i);
						break;
					}
				}
			}
		}
		return upperApproximationObjects;
	}
}
