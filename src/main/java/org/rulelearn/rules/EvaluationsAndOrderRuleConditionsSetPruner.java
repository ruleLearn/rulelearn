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

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Prunes lists of rule conditions {@link RuleConditions} using rule conditions evaluators {@link RuleConditionsEvaluator}.
 * Aims at reducing the number of rule conditions to only those, which are necessary to keep covered given objects. 
 * Rule conditions are removed in an iterative procedure which consists of three steps.
 * First, each rule conditions that can be removed is put on a list. If the list is non-empty, then one of the rule conditions can be removed. Otherwise, the checking is stopped.
 * Second, the worst rule condition is selected from the list according to the specified rule conditions evaluators, which are considered lexicographically.
 * In case of a tie between two or more rule conditions, with respect to all rule conditions evaluators, rule conditions with the smallest index on the list of rule conditions are selected. 
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
		//TODO: implement
		return null;
	}

}
