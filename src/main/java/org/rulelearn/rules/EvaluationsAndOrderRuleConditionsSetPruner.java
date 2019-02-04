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
 * Selected rule conditions are pruned (removed). 
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
	 * @param indicesOfObjectsToKeepCovered {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public List<RuleConditions> prune(List<RuleConditions> ruleConditionsList, IntSet indicesOfObjectsToKeepCovered) {
		Precondition.notNull(ruleConditionsList, "List of rule conditions for evaluations and order rule conditions pruner is null.");
		Precondition.notNull(indicesOfObjectsToKeepCovered, "Set of objects to keep covered is null.");
		
		//----------
		
		//informs how many rule conditions cover the object whose index equals map's key
		Int2IntMap indexOfObjectToKeepCovered2CountMap = new Int2IntOpenHashMap(indicesOfObjectsToKeepCovered.size());
		
		//informs which relevant objects (i.e., objects that need to be covered) are covered by particular rule conditions;
		//e.g., if indicesOfObjectsToKeepCovered == {1, 3, 5, 7} and ruleConditionsList.get(i).getIndicesOfCoveredObjects() == [1, 5, 9, 12], then
		//relevantIndicesOfCoveredObjects.get(i) == {1, 5}
		int ruleConditionsCount = ruleConditionsList.size();
		List<IntSet> listOfRelevantIndicesOfCoveredObjects = new ObjectArrayList<>(ruleConditionsCount); //effectively, indices of covered objects are put from list to hash set
		for (int i = 0; i < ruleConditionsCount; i++) {
			listOfRelevantIndicesOfCoveredObjects.add(new IntOpenHashSet()); // initialize with an empty (hash) set
		}
		
		//----------
		
		int ruleConditionsIndex; //auxiliary variable
		int count; //auxiliary variable
		IntList indicesOfSingleCoveredObjects = new IntArrayList(); //list of indices of objects that should remain covered and are covered by just one rule conditions
		
		for (int indexOfObjectToKeepCovered : indicesOfObjectsToKeepCovered) { //initialize indexOfObjectToKeepCovered2CountMap & listOfRelevantIndicesOfCoveredObjects
			count = 0;
			ruleConditionsIndex = 0;
			for (RuleConditions ruleConditions : ruleConditionsList) {
				if (ruleConditions.covers(indexOfObjectToKeepCovered)) {
					indexOfObjectToKeepCovered2CountMap.put(indexOfObjectToKeepCovered, ++count);
					listOfRelevantIndicesOfCoveredObjects.get(ruleConditionsIndex).add(indexOfObjectToKeepCovered); //remember that currently considered rule conditions cover an object that should remain covered (once this method returns)
				}
				ruleConditionsIndex++;
			}
			if (count == 1) {
				indicesOfSingleCoveredObjects.add(indexOfObjectToKeepCovered);
			}
		}
		
		//----------
		
		if (indexOfObjectToKeepCovered2CountMap.size() != indicesOfObjectsToKeepCovered.size()) {
			throw new InvalidValueException("Rule conditions do not jointly cover all objects that need to be kept covered.");
		}
		
		//----------
		
		//determine rule conditions that may be removed (i.e., do not cover objects that are covered only once)
		IntList indicesOfRemovableRuleConditions = new IntArrayList();
		boolean ruleConditionsAreRemovable; //auxiliary variable
		ruleConditionsIndex = 0;
		for (IntSet relevantIndicesOfCoveredObjects : listOfRelevantIndicesOfCoveredObjects) {
			ruleConditionsAreRemovable = true;
			for (int indexOfSingleCoveredObject: indicesOfSingleCoveredObjects) {
				if (relevantIndicesOfCoveredObjects.contains(indexOfSingleCoveredObject)) { //rule conditions cover a single-covered object, and thus, it cannot be removed
					ruleConditionsAreRemovable = false;
					break;
				}
			}
			if (ruleConditionsAreRemovable) {
				indicesOfRemovableRuleConditions.add(ruleConditionsIndex);
			}
			ruleConditionsIndex++;
		}
		
		//----------
		
		if (indicesOfRemovableRuleConditions.size() > 0) { //there are at least one rule conditions that can be removed
			IntList indicesOfRuleConditionsToRemove = new IntArrayList(); //indices of rule conditions that should be removed at the end of this method
			int worstRemovableRuleConditionsIndex; //auxiliary variable
			int positionOfWorstRemovableRuleConditionsIndex; //auxiliary variable
			while (indicesOfRemovableRuleConditions.size() > 0) {
				positionOfWorstRemovableRuleConditionsIndex = getPositionOfTheWorstRemovableRuleConditionsIndex(indicesOfRemovableRuleConditions, ruleConditionsList);
				worstRemovableRuleConditionsIndex = indicesOfRemovableRuleConditions.getInt(positionOfWorstRemovableRuleConditionsIndex);
				//update indexOfObjectToKeepCovered2CountMap - decrease count for each object covered by removed rule conditions
				for (int relevantIndexOfCoveredObject : listOfRelevantIndicesOfCoveredObjects.get(worstRemovableRuleConditionsIndex)) {
					count = indexOfObjectToKeepCovered2CountMap.get(relevantIndexOfCoveredObject);
					indexOfObjectToKeepCovered2CountMap.put(relevantIndexOfCoveredObject, --count);
					if (count == 1) {
						indicesOfSingleCoveredObjects.add(relevantIndexOfCoveredObject);
					}
				}
				listOfRelevantIndicesOfCoveredObjects.set((worstRemovableRuleConditionsIndex), null); //drop set of object indices (frees memory)
				indicesOfRemovableRuleConditions.removeInt(positionOfWorstRemovableRuleConditionsIndex); //remove index of removed rule conditions
				indicesOfRuleConditionsToRemove.add(worstRemovableRuleConditionsIndex); //remember index of removed rule conditions to remove them at the end of this method
				// TODO implement
			}
			return null; //TODO: modify
			// TODO implement
		} else {
			return ruleConditionsList;
		}
	}
	
	private int getPositionOfTheWorstRemovableRuleConditionsIndex(IntList indicesOfRemovableRuleConditions, List<RuleConditions> ruleConditionsList) {
		int positionOfWorstRemovableRuleConditionsIndex = 0;
		int position = 0;
		for (int indexOfRemovableRuleConditions : indicesOfRemovableRuleConditions) {
			//TODO: implement search
			position++;
		}
		return positionOfWorstRemovableRuleConditionsIndex;
	}

}
