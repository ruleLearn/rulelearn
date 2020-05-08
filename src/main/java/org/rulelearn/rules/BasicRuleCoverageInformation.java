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

import org.rulelearn.core.Precondition;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Basic coverage information concerning a single decision rule {@link Rule}, like:
 * set of objects covered by the rule,
 * decisions of particular objects covered by the rule,
 * set of objects covered by the rule but not matching rule's decision, 
 * and number of all objects in the information table that was used to induce the rule.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class BasicRuleCoverageInformation {
	
	//TODO: make returned fields unmodifiable
	
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
	 * Indices of objects from rule's learning information table that are covered by the rule
	 * but do not support that rule (do not match rule's decision part, or in other words, are not positive).
	 */
	IntSet indicesOfCoveredNotSupportingObjects;
	
	/**
	 * Number of all objects in rule's learning information table.
	 */
	int allObjectsCount;
	
	/**
	 * Constructs this basic rule coverage info.
	 * 
	 * @param ruleConditions rule conditions supplying useful information: {@link RuleConditions#getIndicesOfCoveredObjects() indices of covered objects},
	 *        {@link RuleConditions#getLearningInformationTable() learning information table}, and {@link RuleConditions#getIndicesOfPositiveObjects()
	 *        indices of positive objects}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	public BasicRuleCoverageInformation(RuleConditions ruleConditions) {
		super();
		Precondition.notNull(ruleConditions, "Rule conditions for basic rule coverage information are null.");
		
		IntList indicesOfCoveredObjects = ruleConditions.getIndicesOfCoveredObjects();
		Int2ObjectMap<Decision> decisionsOfCoveredObjects = new Int2ObjectOpenHashMap<Decision>();
		
		indicesOfCoveredNotSupportingObjects = new IntOpenHashSet();
		
		for (int objectIndex : indicesOfCoveredObjects) {
			decisionsOfCoveredObjects.put(objectIndex, ruleConditions.getLearningInformationTable().getDecision(objectIndex));
			if (!ruleConditions.getIndicesOfPositiveObjects().contains(objectIndex)) {
				indicesOfCoveredNotSupportingObjects.add(objectIndex);
			}
		}
		
		this.indicesOfCoveredObjects = indicesOfCoveredObjects;
		this.decisionsOfCoveredObjects = decisionsOfCoveredObjects;
		
		allObjectsCount = ruleConditions.getLearningInformationTable().getNumberOfObjects();
	}
	
	/**
	 * Constructs this basic rule coverage info.
	 * 
	 * @param rule rule of interest, matched against objects from the given learning information table
	 * @param learningInformationTable rule's learning information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public BasicRuleCoverageInformation(Rule rule, InformationTable learningInformationTable) {
		Precondition.notNull(rule, "Rule for constructing basic rule coverage information is null.");
		Precondition.notNull(learningInformationTable, "Learning information table for constructing basic rule coverage information is null.");
		
		indicesOfCoveredObjects = new IntArrayList();
		decisionsOfCoveredObjects = new Int2ObjectOpenHashMap<Decision>();
		indicesOfCoveredNotSupportingObjects = new IntOpenHashSet();
		
		allObjectsCount = learningInformationTable.getNumberOfObjects();
		
		for (int i = 0; i < allObjectsCount; i++) {
			if (rule.covers(i, learningInformationTable)) {
				indicesOfCoveredObjects.add(i);
				decisionsOfCoveredObjects.put(i, learningInformationTable.getDecision(i));
				if (!rule.decisionsMatchedBy(i, learningInformationTable)) {
					indicesOfCoveredNotSupportingObjects.add(i);
				}
			}
		}
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
	 * Gets indices of objects from rule's learning information table that are covered by the rule
	 * but do not support that rule (do not match rule's decision part, or in other words, are not positive).
	 * 
	 * @return indices of objects from rule's learning information table that are covered by the rule
	 *         but do not support that rule
	 */
	public IntSet getIndicesOfCoveredNotSupportingObjects() {
		return indicesOfCoveredNotSupportingObjects;
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
