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

import it.unimi.dsi.fastutil.ints.IntList;

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
			int numberOfAttributes = ruleConditions.getLearningInformationTable().getNumberOfAttributes();
			int attributeIndex = 0;
			IntList conditionIndices = null;
			int i = 0;
			while (attributeIndex < numberOfAttributes) {
				if (ruleConditions.containsConditionForAttribute(attributeIndex)) {
					conditionIndices = ruleConditions.getConditionIndicesForAttribute(attributeIndex);
					while (i < conditionIndices.size()) {
						if (stoppingConditionChecker.isStoppingConditionSatisifiedWithoutCondition(ruleConditions, conditionIndices.getInt(i))) {
							ruleConditions.removeCondition(conditionIndices.getInt(i));
						}
						else {
							i++;
						}
					}
					i = 0;
				}
				attributeIndex++;
			}			
		}		
		return ruleConditions;
	}
	
}
