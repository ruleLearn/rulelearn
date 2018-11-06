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

import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Builder of {@link RuleConditions} objects. After creating an empty rule conditions object, iteratively adds best condition suggested by employed condition generator,
 * until stopping condition is satisfied. Does not perform any pruning once stopping condition is satisfied.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleConditionsBuilder {
	
	/**
	 * Indices of objects considered when adding next condition to constructed rule conditions. Updated at each iteration, after appending next condition.
	 */
	IntList indicesOfConsideredObjects;
	InformationTable learningInformationTable;
	IntSet indicesOfPositiveObjects;
	IntSet indicesOfApproximationObjects;
	IntSet indicesOfObjectsThatCanBeCovered;
	IntSet indicesOfNeutralObjects;
	RuleType ruleType;
	RuleSemantics ruleSemantics;
	ConditionGenerator conditionGenerator;
	RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker;
	
	/**
	 * Constructs this rule conditions builder by storing all the parameters for future use when building rule conditions.
	 * 
	 * @param indicesOfConsideredObjects list of indices of (positive) objects from learning information which are considered when generating candidate elementary conditions;
	 *        this list is directly stored in this builder and iteratively modified when building rule conditions
	 * @param learningInformationTable learning information table for which rule conditions are constructed
	 * @param indicesOfPositiveObjects set of indices of positive objects from the given learning information table, i.e., all objects that satisfy right-hand side (RHS, decision part) of induced decision rule;
	 *        in case of a certain/possible rule, these are the objects from considered approximated set
	 * @param indicesOfApproximationObjects indices of objects from learning information (decision) table that belong to lower/upper approximation, or boundary of considered approximated set;
	 *        it is expected that this set is a subset of {@code indicesOfObjectsThatCanBeCovered}
	 * @param indicesOfObjectsThatCanBeCovered indices of objects from the given learning information (decision) table that can be covered by soon-to-be-constructed rule conditions
	 * @param indicesOfNeutralObjects indices of neutral objects from the given learning information (decision) table; it is expected that this set is a subset of {@code indicesOfObjectsThatCanBeCovered};
	 * @param ruleType type of the decision rule for which conditions are built by this builder; see {@link RuleType}
	 * @param ruleSemantics semantics of the decision rule for which conditions are built by this builder; see {@link RuleSemantics}
	 * @param conditionGenerator generator of elementary conditions used to build rule conditions object
	 * @param ruleInductionStoppingConditionChecker stopping condition checker iteratively used to verify if rule conditions
	 *        (after extending with a new best elementary condition coming from condition generator)
	 *        satisfy stopping condition(s)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleConditionsBuilder(IntList indicesOfConsideredObjects, InformationTable learningInformationTable,
			IntSet indicesOfPositiveObjects, IntSet indicesOfApproximationObjects, IntSet indicesOfObjectsThatCanBeCovered, IntSet indicesOfNeutralObjects,
			RuleType ruleType, RuleSemantics ruleSemantics,
			ConditionGenerator conditionGenerator, RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker) {
		this.indicesOfConsideredObjects = Precondition.notNull(indicesOfConsideredObjects, "Indices of considered objects are null.");
		this.learningInformationTable = Precondition.notNull(learningInformationTable, "Learning information table is null.");
		this.indicesOfPositiveObjects = Precondition.notNull(indicesOfPositiveObjects, "Indices of positive objects are null.");
		this.indicesOfApproximationObjects = notNull(indicesOfApproximationObjects, "Set of indices of approximation objects is null.");
		this.indicesOfObjectsThatCanBeCovered = Precondition.notNull(indicesOfObjectsThatCanBeCovered, "Indices of objects that can be covered are null.");
		this.indicesOfNeutralObjects = Precondition.notNull(indicesOfNeutralObjects, "Indices of neutral objects are null.");
		this.ruleType = notNull(ruleType, "Rule type is null.");
		this.ruleSemantics = notNull(ruleSemantics, "Rule semantics is null.");
		this.conditionGenerator = Precondition.notNull(conditionGenerator, "Condition generator is null.");
		this.ruleInductionStoppingConditionChecker = Precondition.notNull(ruleInductionStoppingConditionChecker, "Rule induction stopping condition checker is null.");
	}
	
	/**
	 * Builds rule conditions by iteratively adding best condition suggested by employed condition generator, until stopping condition is satisfied.
	 * 
	 * @return built rule conditions
	 * @throws ElementaryConditionNotFoundException when at some point is was impossible to find any new elementary condition
	 *         that could be added to rule conditions that still do not satisfy stopping condition(s)
	 */
	public RuleConditions build() {
		RuleConditions ruleConditions = new RuleConditions(learningInformationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, indicesOfNeutralObjects,
				ruleType, ruleSemantics);
		Condition<EvaluationField> bestCondition;
		IntList indicesOfCoveredObjects;
		IntSet indicesOfNoLongerCoveredObjects;
		
		while (!ruleInductionStoppingConditionChecker.isStoppingConditionSatisified(ruleConditions)) {
			try {
				bestCondition = conditionGenerator.getBestCondition(indicesOfConsideredObjects, ruleConditions);
				ruleConditions.addCondition(bestCondition);
				
				//update indices of considered objects
				indicesOfCoveredObjects = ruleConditions.getIndicesOfCoveredObjects();
				indicesOfNoLongerCoveredObjects = new IntOpenHashSet();
				for (int previouslyCoveredObjectIndex : indicesOfConsideredObjects) {
					if (!indicesOfCoveredObjects.contains(previouslyCoveredObjectIndex)) { //previously covered object is no longer covered
						indicesOfNoLongerCoveredObjects.add(previouslyCoveredObjectIndex);
					}
				}
				indicesOfConsideredObjects.removeAll(indicesOfNoLongerCoveredObjects);
			} catch (ElementaryConditionNotFoundException exception) {
				throw exception;
			}
		} //while
		
		return ruleConditions;
	}
	
}
