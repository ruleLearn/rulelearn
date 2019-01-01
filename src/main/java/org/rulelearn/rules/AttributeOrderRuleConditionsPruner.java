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

import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;

/**
 * Pruner for rule conditions that analyzes conditions according to the order of attributes in the learning information table {@link InformationTable}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class AttributeOrderRuleConditionsPruner extends AbstractRuleConditionsPruner {
	
	/**
	 * Constructor storing given stopping condition checker.
	 * 
	 * @param stoppingConditionChecker stopping condition checker
	 * @throws NullPointerException if given stopping condition checker is {@code null}
	 */
	public AttributeOrderRuleConditionsPruner(RuleInductionStoppingConditionChecker stoppingConditionChecker) {
		super(stoppingConditionChecker);
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	@Override
	public RuleConditions prune(RuleConditions ruleConditions) {
		Precondition.notNull(ruleConditions, "Rule conditions for attribute order preserving rule conditions pruner are null.");
		
		if (ruleConditions.size() > 1) {
			int [] attribute2ConditionMap = getAttribute2RuleConditionMap(ruleConditions);
			int attributeIndex = 0; 
			while (attributeIndex < attribute2ConditionMap.length) {
				if (ruleConditions.containsConditionForAttribute(attributeIndex)) {
					if (stoppingConditionChecker.isStoppingConditionSatisifiedWithoutCondition(ruleConditions, attribute2ConditionMap[attributeIndex])) {
						ruleConditions.removeCondition(attribute2ConditionMap[attributeIndex]);
						// map must be recreated after a rule condition is removed 
						// TODO this part can be improved by constructing map starting from attributeIndex
						attribute2ConditionMap = getAttribute2RuleConditionMap(ruleConditions);
					}
				}
				// TODO this part may also be improved by checking not all attributes but rule conditions
				attributeIndex++;
			}			
		}		
		return ruleConditions;
	}
	
	/**
	 * For a given list of rule conditions represented as {@link RuleConditions} constructs a map between attribute indices in the learning information table
	 * and indices of rule conditions on the list. An entry in this map links between an attribute and a rule condition containing the attribute. 
	 * 
	 * @param ruleConditions a list of rule conditions 
	 * @return an array representing a map between attribute indices in the learning information table and indices of rule conditions on the list
	 */
	int [] getAttribute2RuleConditionMap(RuleConditions ruleConditions) {
		Precondition.notNull(ruleConditions, "Rule conditions provided to construct attribute to rule condition map are null.");
		
		int numberOfAttribues = ruleConditions.getLearningInformationTable().getNumberOfAttributes();
		List<Condition<?>> conditions = ruleConditions.getConditions();
		int [] attribute2ConditionMap = new int[numberOfAttribues];
		for (int conditionIndex = 0; conditionIndex < conditions.size(); conditionIndex++) {
			attribute2ConditionMap[conditions.get(conditionIndex).getAttributeWithContext().getAttributeIndex()] = conditionIndex;
		}
		return attribute2ConditionMap;
	}

}
