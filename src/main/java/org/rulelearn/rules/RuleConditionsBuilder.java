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
 * until stopping condition is satisfied. 
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleConditionsBuilder {
	
	IntList indicesOfConsideredObjects;
	InformationTable learningInformationTable;
	IntSet indicesOfPositiveObjects;
	IntSet indicesOfApproximationObjects;
	IntSet indicesOfObjectsThatCanBeCovered;
	IntSet indicesOfNeutralObjects;
	ConditionGenerator conditionGenerator;
	RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker;
	
	/**
	 * Constructs this rule conditions builder.
	 * 
	 * @param indicesOfConsideredObjects set of indices of objects from learning information table that are used to generate elementary conditions
	 * @param learningInformationTable TODO
	 * @param indicesOfPositiveObjects TODO
	 * @param indicesOfApproximationObjects TODO
	 * @param indicesOfObjectsThatCanBeCovered TODO
	 * @param indicesOfNeutralObjects TODO
	 * @param conditionGenerator TODO
	 * @param ruleInductionStoppingConditionChecker TODO
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleConditionsBuilder(IntList indicesOfConsideredObjects, InformationTable learningInformationTable,
			IntSet indicesOfPositiveObjects, IntSet indicesOfApproximationObjects, IntSet indicesOfObjectsThatCanBeCovered, IntSet indicesOfNeutralObjects,
			ConditionGenerator conditionGenerator, RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker) {
		this.indicesOfConsideredObjects = Precondition.notNull(indicesOfConsideredObjects, "Indices of considered objects are null.");
		this.learningInformationTable = Precondition.notNull(learningInformationTable, "Learning information table is null.");
		this.indicesOfPositiveObjects = Precondition.notNull(indicesOfPositiveObjects, "Indices of positive objects are null.");
		this.indicesOfApproximationObjects = notNull(indicesOfApproximationObjects, "Set of indices of approximation objects is null.");
		this.indicesOfObjectsThatCanBeCovered = Precondition.notNull(indicesOfObjectsThatCanBeCovered, "Indices of objects that can be covered are null.");
		this.indicesOfNeutralObjects = Precondition.notNull(indicesOfNeutralObjects, "Indices of neutral objects are null.");
		this.conditionGenerator = Precondition.notNull(conditionGenerator, "Condition generator is null.");
		this.ruleInductionStoppingConditionChecker = Precondition.notNull(ruleInductionStoppingConditionChecker, "Rule induction stopping condition checker is null.");
	}
	
	/**
	 * Builds rule conditions by iteratively adding best condition suggested by employed condition generator, until stopping condition is satisfied.
	 * 
	 * @return built rule conditions
	 */
	public RuleConditions build() {
		RuleConditions ruleConditions = new RuleConditions(learningInformationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, indicesOfNeutralObjects);
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
				//TODO: handle exception
			}
		} //while
		
		return ruleConditions;
	}
	
}
