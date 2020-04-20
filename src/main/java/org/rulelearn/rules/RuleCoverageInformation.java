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

package org.rulelearn.rules;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Coverage information concerning a single decision rule {@link Rule}, like set of objects positive with respect to the rule,
 * set of objects neutral with respect to the rule, set of objects covered by the rule, and number of all objects
 * in the information table that was used to induce the rule. If considered rule happens to cover some neutral objects,
 * it should have no influence on any rule's characteristics.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleCoverageInformation {
	//rule coverage info
	//rule coverage report
	//rule coverage summary
	//rule description
	
	/**
	 * Indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part) of considered decision rule.
	 * In case of a certain/possible rule, these are the objects from considered approximated set.
	 */
	IntSet indicesOfPositiveObjects = null;
	
	/**
	 * Indices of all neutral objects from rule's learning information (decision) table. Can be an empty set.
	 * Neutral objects are such objects that their decision is neither positive nor negative with respect to the approximated set used to induce the rule.
	 */
	IntSet indicesOfNeutralObjects = null;
	
	/**
	 * Indices of objects from rule's learning information table that are covered by the rule.
	 */
	IntList indicesOfCoveredObjects;
	
	/**
	 * Decisions of objects from rule's learning information table that are covered by the rule.
	 * Maps object index to its decision.
	 */
	Int2ObjectMap<Decision> decisionsOfCoveredObjects;
	
	/**
	 * Number of all objects in rule's learning information table.
	 */
	int allObjectsCount;
	
	/**
	 * Constructs this rule coverage info.
	 * 
	 * @param indicesOfPositiveObjects indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part)
	 *        of considered decision rule; in case of a certain/possible rule, these are the objects from considered approximated set
	 * @param indicesOfNeutralObjects indices of all neutral objects from rule's learning information (decision) table, i.e., objects such that their decision
	 *        is neither positive nor negative with respect to the considered approximated set
	 *        for the approximated set used to induce the rule
	 * @param indicesOfCoveredObjects indices of all objects from rule's learning information table that are covered by the rule
	 * @param decisionsOfCoveredObjects decisions of objects from rule's learning information table that are covered by the rule; maps
	 *        object index to its decision
	 * @param allObjectsCount number of all objects in rule's learning information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if given number of all objects is less than zero
	 * @throws InvalidValueException if the number of indices of covered objects and the number of decisions of covered objects are different
	 */
	public RuleCoverageInformation(IntSet indicesOfPositiveObjects, IntSet indicesOfNeutralObjects, IntList indicesOfCoveredObjects,
			Int2ObjectMap<Decision> decisionsOfCoveredObjects, int allObjectsCount) {
		super();
		this.indicesOfPositiveObjects = Precondition.notNull(indicesOfPositiveObjects, "Positive objects are null.");
		this.indicesOfNeutralObjects = Precondition.notNull(indicesOfNeutralObjects, "Neutral objects are null.");
		this.indicesOfCoveredObjects = Precondition.notNull(indicesOfCoveredObjects, "Covered objects are null.");
		this.decisionsOfCoveredObjects = Precondition.notNull(decisionsOfCoveredObjects, "Decisions of covered objects are null.");
		this.allObjectsCount = Precondition.nonNegative(allObjectsCount, "Number of objects is less than zero.");
		
		if (indicesOfCoveredObjects.size() != decisionsOfCoveredObjects.size()) {
			throw new InvalidValueException("Different number of indices of covered objects and decisions of covered objects.");
		}
	}
	
	/**
	 * Constructs this rule coverage info. Sets indices of positive and neutral objects to {@code null} and calculates all remaining fields.
	 * Thus, subsequent calls to {@link #getIndicesOfPositiveObjects()} or {@link #getIndicesOfNeutralObjects()} will return {@code null}.
	 * 
	 * @param rule rule of interest, matched against objects from the given learning information table
	 * @param learningInformationTable rule's learning information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleCoverageInformation(Rule rule, InformationTable learningInformationTable) {
		Precondition.notNull(rule, "Rule for constructing rule coverage information is null.");
		Precondition.notNull(learningInformationTable, "Learning information table for constructing rule coverage information is null.");
		
		this.indicesOfPositiveObjects = null;
		this.indicesOfNeutralObjects = null;
		
		this.indicesOfCoveredObjects = new IntArrayList();
		this.decisionsOfCoveredObjects = new Int2ObjectOpenHashMap<Decision>();
		
		this.allObjectsCount = learningInformationTable.getNumberOfObjects();
		
		for (int i = 0; i < allObjectsCount; i++) {
			if (rule.covers(i, learningInformationTable)) {
				indicesOfCoveredObjects.add(i);
				decisionsOfCoveredObjects.put(i, learningInformationTable.getDecision(i));
			}
		}
	}

	/**
	 * Gets indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part)
	 * of considered decision rule. In case of a certain/possible rule, these are the objects from considered approximated set.
	 * 
	 * @return indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part)
	 *         of considered decision rule;
	 *         {@code null} if this object has been constructed using constructor {@link #RuleCoverageInformation(Rule, InformationTable)}
	 */
	public IntSet getIndicesOfPositiveObjects() {
		return indicesOfPositiveObjects;
	}

	/**
	 * Gets indices of all objects from rule's learning information (decision) table that are neutral with respect to the rule, i.e., objects such that their decision
	 * is neither positive nor negative with respect to the approximated set used to induce the rule.
	 * 
	 * @return indices of all objects from rule's learning information (decision) table that are neutral with respect to the rule;
	 *         {@code null} if this object has been constructed using constructor {@link #RuleCoverageInformation(Rule, InformationTable)}
	 */
	public IntSet getIndicesOfNeutralObjects() {
		return indicesOfNeutralObjects;
	}

	/**
	 * Gets indices of all objects from rule's learning information table that are covered by the rule.
	 * 
	 * @return indices of all objects from rule's learning information table that are covered by the rule
	 */
	public IntList getIndicesOfCoveredObjects() {
		return indicesOfCoveredObjects;
	}
	
	/**
	 * Gets decisions of objects from rule's learning information table that are covered by the rule.
	 * Decision of particular object can be obtained from the map using object's index as the key.
	 * 
	 * @return decisions of objects from rule's learning information table that are covered by the rule
	 */
	public Int2ObjectMap<Decision> getDecisionsOfCoveredObjects() {
		return decisionsOfCoveredObjects;
	}

	/**
	 * Gets number of all objects in rule's learning information table.
	 * 
	 * @return number of all objects in rule's learning information table
	 */
	public int getAllObjectsCount() {
		return allObjectsCount;
	}

}
