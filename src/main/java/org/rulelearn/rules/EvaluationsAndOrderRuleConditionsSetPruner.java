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

import java.util.List;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Prunes lists of rule conditions {@link RuleConditions} using rule conditions evaluators {@link RuleConditionsEvaluator}.
 * Aims at reducing the number of rule conditions to only those, which are necessary to keep covered given objects. 
 * Rule conditions are removed in an iterative procedure which consists of three steps.
 * 
 * First, each rule conditions that can be removed are put on a list. If the list is non-empty, then one of the rule conditions
 * can be removed. Otherwise, the checking is stopped.
 * Second, the worst rule conditions are selected from the list according to the specified rule conditions evaluators, which are
 * considered lexicographically.
 * In case of a tie between two or more rule conditions, with respect to all rule conditions evaluators, rule conditions with
 * the smallest index on the list of rule conditions are selected.
 * Then, selected rule conditions are pruned (removed), the list of rule conditions that can be removed is updated, and the whole procedure repeats.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EvaluationsAndOrderRuleConditionsSetPruner extends AbstractRuleConditionsSetPrunerWithEvaluators {

	/**
	 * Constructor for this rule conditions set pruner storing given rule conditions evaluators.
	 * 
	 * @param ruleConditionsEvaluators rule conditions evaluators used lexicographically to evaluate each {@link RuleConditions} from considered list
	 * @throws NullPointerException if given array of rule conditions evaluators is {@code null}
	 */
	public EvaluationsAndOrderRuleConditionsSetPruner(RuleConditionsEvaluator[] ruleConditionsEvaluators) {
		super(ruleConditionsEvaluators);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleConditionsList {@inheritDoc}
	 * @param indicesOfObjectsToKeepCovered {@inheritDoc}; these are "observed" objects (for which coverage by at least one rule has to be maintained)
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public List<RuleConditions> prune(List<RuleConditions> ruleConditionsList, IntSet indicesOfObjectsToKeepCovered) {
		Precondition.notNull(ruleConditionsList, "List of rule conditions for evaluations and order rule conditions pruner is null.");
		Precondition.notNull(indicesOfObjectsToKeepCovered, "Set of objects to keep covered is null.");
		
		//General remarks for the following code:
		//- for brevity, "rule" is used instead of "ruleConditions"
		//- for brevity, "index" is dropped, e.g., "object" is used instead of "objectIndex"
		//----------
		
		//maps index of observed object to number of rule conditions covering that object
		Int2IntMap observedObjectToRuleCount = new Int2IntOpenHashMap(indicesOfObjectsToKeepCovered.size());
		
		//informs which observed objects (i.e., objects that need to be covered) are covered by particular rule conditions;
		//e.g., if indicesOfObjectsToKeepCovered == {1, 3, 5, 7} and ruleConditionsList.get(i).getIndicesOfCoveredObjects() == (1, 5, 9, 12), then
		//objectsObservedByRule.get(i) == {1, 5}
		int ruleCount = ruleConditionsList.size();
		List<IntSet> objectsObservedByRules = new ObjectArrayList<>(ruleCount); //when being filled, effectively indices of observed objects covered by rule conditions are put from list to hash set
		for (int i = 0; i < ruleCount; i++) {
			objectsObservedByRules.add(new IntOpenHashSet()); // initialize with an empty (hash) set
		}
		
		//----------
		
		int ruleIndex; //auxiliary variable
		int count; //auxiliary variable
		IntList observedObjectsCoveredOnce = new IntArrayList(); //list of indices of observed objects that are covered by just one rule conditions
		
		for (int observedObject : indicesOfObjectsToKeepCovered) { //initialize observedObjectToRuleCount, objectsObservedByRule, and observedObjectsCoveredOnce
			count = 0;
			ruleIndex = 0;
			for (RuleConditions rule : ruleConditionsList) {
				if (rule.covers(observedObject)) {
					observedObjectToRuleCount.put(observedObject, ++count);
					objectsObservedByRules.get(ruleIndex).add(observedObject); //remember that currently considered rule conditions cover currently considered observed object
				}
				ruleIndex++;
			}
			if (count == 1) { //current observed object is covered by just one rule
				observedObjectsCoveredOnce.add(observedObject);
			}
		}
		
		//----------
		
		if (observedObjectToRuleCount.size() != indicesOfObjectsToKeepCovered.size()) {
			throw new InvalidValueException("Rule conditions do not jointly cover all objects that need to be kept covered.");
		}
		
		//----------
		
		//determine rule conditions that may be removed (i.e., do not cover observed objects that are covered only once)
		IntList removableRules = new IntArrayList(); //contains indices of rule conditions 
		boolean ruleIsRemovable; //auxiliary variable
		ruleIndex = 0;
		for (IntSet objectsObservedByRule : objectsObservedByRules) { //iterate through rule conditions
			ruleIsRemovable = true;
			for (int observedObjectCoveredOnce: observedObjectsCoveredOnce) {
				if (objectsObservedByRule.contains(observedObjectCoveredOnce)) { //only current rule conditions cover some observed object
					ruleIsRemovable = false;
					break;
				}
			}
			if (ruleIsRemovable) {
				removableRules.add(ruleIndex);
			}
			ruleIndex++;
		}
		
		//----------
		
		if (removableRules.size() > 0) { //there are at least one rule conditions that can be removed
			IntList rulesToRemove = new IntArrayList(); //indices of rule conditions that should be removed at the end of this method
			int worstRemovableRule; //auxiliary variable
			int positionOfWorstRemovableRule; //auxiliary variable
			while (removableRules.size() > 0) {
				positionOfWorstRemovableRule = getPositionOfWorstRemovableRule(removableRules, ruleConditionsList);
				worstRemovableRule = removableRules.getInt(positionOfWorstRemovableRule);
				//update indexOfObjectToKeepCovered2CountMap - decrease count for each object covered by removed rule conditions
				for (int observedObject : objectsObservedByRules.get(worstRemovableRule)) {
					count = observedObjectToRuleCount.get(observedObject);
					observedObjectToRuleCount.put(observedObject, --count);
					if (count == 1) {
						observedObjectsCoveredOnce.add(observedObject);
					}
				}
				objectsObservedByRules.set(worstRemovableRule, null); //drop set of object indices (frees memory)
				removableRules.removeInt(positionOfWorstRemovableRule); //remove index of removed rule conditions
				rulesToRemove.add(worstRemovableRule); //remember index of removed rule conditions to remove them at the end of this method
				// TODO implement
			}
			return null; //TODO: modify
			// TODO implement
		} else {
			return ruleConditionsList;
		}
	}
	
	private int getPositionOfWorstRemovableRule(IntList removableRules, List<RuleConditions> ruleConditionsList) {
		int positionOfWorstRemovableRule = 0;
		int position = 0;
		for (int removableRule : removableRules) {
			//TODO: implement search
			position++;
		}
		return positionOfWorstRemovableRule;
	}

}
