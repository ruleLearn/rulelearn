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
	
	final class RuleConditionsWithEvaluations {
		double[] evaluations;
		int validEvaluationsCount;
		RuleConditions ruleConditions;
		
		//constructor; initializes fields taking into account the number of evaluations
		RuleConditionsWithEvaluations(RuleConditions ruleConditions) {
			evaluations = new double[ruleConditionsEvaluators.length];
			validEvaluationsCount = 0;
			this.ruleConditions = ruleConditions;
		}
		
		//retrieves stored evaluation, possibly first calculating it
		double getEvaluation(int evaluationIndex) {
			if (evaluationIndex < validEvaluationsCount) {
				return evaluations[evaluationIndex];
			}
			if (evaluationIndex == validEvaluationsCount) {
				evaluations[evaluationIndex] = ruleConditionsEvaluators[evaluationIndex].evaluate(ruleConditions);
				validEvaluationsCount++;
				return evaluations[evaluationIndex];
			} else { //not subsequent evaluation is retrieved
				throw new InvalidValueException("Not subsequent evaluation of rule conditions is being retrieved.");
			}
		}
	}

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
	 * @throws InvalidValueException if rule conditions do not jointly cover all objects that need to be kept covered
	 */
	@Override
	public List<RuleConditions> prune(List<RuleConditions> ruleConditionsList, IntSet indicesOfObjectsToKeepCovered) {
		Precondition.notNull(ruleConditionsList, "List of rule conditions for evaluations and order rule conditions pruner is null.");
		Precondition.notNull(indicesOfObjectsToKeepCovered, "Set of objects to keep covered is null.");
		
		//Inner naming convention: "rule" is used instead of "ruleConditions" and "index" is dropped, e.g., "object" is used instead of "objectIndex"
		
		//----- <auxiliary-variables>
		IntSet observedObjects = indicesOfObjectsToKeepCovered; //assume new name for objects that need to be covered
		List<RuleConditions> rules = ruleConditionsList; //assume new name for the list of rule conditions
		int ruleCount;
		//===== </auxiliary-variables>
		
		//----- <main-variables>
		//informs which observed objects are covered by particular rules;
		//e.g., if rules.get(i).getIndicesOfCoveredObjects() == (1, 5, 9, 12) and observedObjects == {1, 3, 5, 7}, then objectsObservedByRules.get(i) == {1, 5}
		List<IntSet> ruleToObservedObjects = new ObjectArrayList<IntSet>(rules.size()); //when being filled, effectively indices of observed objects covered by rule are put from list to hash set
		Int2IntMap observedObjectToRuleCount = new Int2IntOpenHashMap(observedObjects.size()); //maps index of observed object to count of rules covering that object
		IntList observedObjectsCoveredOnce = new IntArrayList(); //list of indices of observed objects that are covered by just one rule
		//===== </main-variables>
		
		//----- <initialization>
		for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
			ruleToObservedObjects.add(new IntOpenHashSet()); // initialize with an empty (hash) set
		}
		for (int observedObject : observedObjects) {
			ruleCount = 0;
			for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
				if (rules.get(ruleIndex).covers(observedObject)) {
					observedObjectToRuleCount.put(observedObject, ++ruleCount);
					ruleToObservedObjects.get(ruleIndex).add(observedObject); //remember that currently considered rule covers currently considered observed object
				}
			}
			if (ruleCount == 1) { //current observed object is covered by just one rule
				observedObjectsCoveredOnce.add(observedObject);
			}
		}
		//===== </initialization>
		
		//----- <validation>
		if (observedObjectToRuleCount.size() != observedObjects.size()) {
			throw new InvalidValueException("Rule conditions do not jointly cover all objects that need to be kept covered.");
		}
		//===== </validation>
		
		//determine rules that may be removed (i.e., do not cover observed objects that are covered only once)
		IntList removableRules = calculateRemovableRules(ruleToObservedObjects, observedObjectsCoveredOnce);
		
		//prune redundant rules?
		if (removableRules.size() > 0) { //there is at least one rule that can be removed
			IntList rulesToRemove = new IntArrayList(); //list of indices of rules that should be removed at the end of this method (sorted in ascending order)
			int worstRemovableRule; //auxiliary variable
			int positionOfWorstRemovableRule; //auxiliary variable
			
			while (removableRules.size() > 0) {
				positionOfWorstRemovableRule = getPositionOfWorstRemovableRule(removableRules, rules);
				worstRemovableRule = removableRules.getInt(positionOfWorstRemovableRule);
				
				//update observedObjectToRuleCount - decrease count for each object covered by removed rule
				for (int observedObject : ruleToObservedObjects.get(worstRemovableRule)) {
					ruleCount = observedObjectToRuleCount.get(observedObject);
					observedObjectToRuleCount.put(observedObject, --ruleCount); //#2#
					if (ruleCount == 1) {
						observedObjectsCoveredOnce.add(observedObject); //#3#
					}
				}
				ruleToObservedObjects.set(worstRemovableRule, null); //drop set of object indices (to free memory) //#1#
				
				removableRules.removeInt(positionOfWorstRemovableRule); //remove index of removed rule (once removed, it is no longer removable)
				updateRemovableRules(removableRules, ruleToObservedObjects, observedObjectsCoveredOnce); //check if remaining rules are still removable, and reduce list if necessary 
				updateRulesToRemove(rulesToRemove, worstRemovableRule); //remember index of removed rule to remove that rule at the end of this method
			}
			
			for (int i = rulesToRemove.size() - 1; i >= 0; i--) { //removed (prune) redundant rules, starting from the greatest index on the list
				rules.remove(rulesToRemove.getInt(i));
			}
			
			return rules; //return pruned rules
		} else {
			return rules; //return original rules (no rule could be removed)
		}
	}
	
	int getPositionOfWorstRemovableRule(IntList removableRules, List<RuleConditions> rules) {
		int positionOfWorstRemovableRule = 0;
		int position = 0;
		
		for (int removableRule : removableRules) {
			//TODO: implement search
			position++;
		}
		
		return positionOfWorstRemovableRule;
	}
	
	IntList calculateRemovableRules(List<IntSet> ruleToObservedObjects, IntList observedObjectsCoveredOnce) {
		//determine rules that may be removed (i.e., do not cover observed objects that are covered only once)
		IntList removableRules = new IntArrayList(); //contains indices of rules
		boolean ruleIsRemovable; //auxiliary variable
		IntSet observedObjects;
		int ruleCount = ruleToObservedObjects.size();

		for (int ruleIndex = 0; ruleIndex < ruleCount; ruleIndex++) {
			ruleIsRemovable = true;
			observedObjects = ruleToObservedObjects.get(ruleIndex);
			for (int observedObjectCoveredOnce : observedObjectsCoveredOnce) {
				if (observedObjects.contains(observedObjectCoveredOnce)) { //only current rule covers some observed object
					ruleIsRemovable = false;
					break;
				}
			}
			if (ruleIsRemovable) {
				removableRules.add(ruleIndex);
			}
		}
		
		return removableRules;
	}
	
	//updates removableRules in place
	void updateRemovableRules(IntList removableRules, List<IntSet> ruleToObservedObjects, IntList observedObjectsCoveredOnce) {
		IntSet observedObjects;
		
		for (int i = removableRules.size() - 1; i >= 0; i--) { //iterate from the end of the list
			observedObjects = ruleToObservedObjects.get(removableRules.getInt(i));
			
			for (int observedObjectCoveredOnce : observedObjectsCoveredOnce) {
				if (observedObjects.contains(observedObjectCoveredOnce)) { //only current rule covers some observed object
					removableRules.removeInt(i);
					break;
				}
			}
		}
	}
	
	//updates rules to remove in place, keeping ascending order of indices
	void updateRulesToRemove(IntList rulesToRemove, int rule) {
		boolean inserted = false;
		for (int i = 0; i < rulesToRemove.size(); i++) {
			if (rule < rulesToRemove.getInt(i)) {
				rulesToRemove.add(i, rule); //insert into proper list position
				inserted = true;
				break;
			}
		}
		if (!inserted) {
			rulesToRemove.add(rule); //append at the end of the list
		}
	}

}
