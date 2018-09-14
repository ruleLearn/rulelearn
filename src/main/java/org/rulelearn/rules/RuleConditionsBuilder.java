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
import org.rulelearn.data.InformationTable;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Builder of {@link RuleConditions} objects.
 * 
 * TODO: write javadoc
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleConditionsBuilder {
	
	IntSet consideredObjects;
	InformationTable learningInformationTable;
	IntSet indicesOfPositiveObjects;
	IntSet indicesOfObjectsThatCanBeCovered;
	IntSet indicesOfNeutralObjects;
	ConditionGenerator conditionGenerator;
	RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker;
	
	/**
	 * TODO: write javadoc
	 * 
	 * @param consideredObjects set of indices of positive object used to generate elementary conditions
	 * @param learningInformationTable TODO
	 * @param indicesOfPositiveObjects TODO
	 * @param indicesOfObjectsThatCanBeCovered TODO
	 * @param indicesOfNeutralObject TODO
	 * @param conditionGenerator TODO
	 * @param ruleInductionStoppingConditionChecker TODO
	 */
	public RuleConditionsBuilder(IntSet consideredObjects, InformationTable learningInformationTable,
			IntSet indicesOfPositiveObjects, IntSet indicesOfObjectsThatCanBeCovered, IntSet indicesOfNeutralObject,
			ConditionGenerator conditionGenerator, RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker) {
		this.consideredObjects = Precondition.notNull(consideredObjects, "Considered objects are null.");
		this.learningInformationTable = Precondition.notNull(learningInformationTable, "Learning information table is null.");
		this.indicesOfPositiveObjects = Precondition.notNull(indicesOfPositiveObjects, "Indices of positive objects are null.");
		this.indicesOfObjectsThatCanBeCovered = Precondition.notNull(indicesOfObjectsThatCanBeCovered, "Indices of objects that can be covered are null.");
		this.indicesOfNeutralObjects = Precondition.notNull(indicesOfNeutralObject, "Indices of neutral objects are null.");
		this.conditionGenerator = Precondition.notNull(conditionGenerator, "Condition generator is null.");
		this.ruleInductionStoppingConditionChecker = Precondition.notNull(ruleInductionStoppingConditionChecker, "Rule induction stopping condition checker is null.");
	}
	
	/**
	 * Builds rule conditions by iteratively adding best condition suggested by employed condition generator, until stopping condition is satisfied.
	 * 
	 * @return built rule conditions
	 */
	public RuleConditions build() {
		RuleConditions ruleConditions = new RuleConditions(learningInformationTable, indicesOfPositiveObjects, indicesOfObjectsThatCanBeCovered, indicesOfNeutralObjects);
		
		//TODO: implement
		
		return ruleConditions;
	}
	
}
