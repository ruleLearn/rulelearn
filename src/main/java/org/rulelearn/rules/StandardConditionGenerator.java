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
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Condition generator used when no assumptions can be made with respect to evaluations of elementary conditions (checking each unique elementary conditions for every considered attribute).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class StandardConditionGenerator extends AbstractConditionGeneratorWithEvaluators {
	
	/**
	 * Constructor for this condition generator. Stores given evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionEvaluators array with condition evaluators used lexicographically
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public StandardConditionGenerator(ConditionAdditionEvaluator[] conditionEvaluators) {
		super(conditionEvaluators);
	}
	
	/**
	 * Tells if attributes already present in rule conditions can be skipped when generating next best condition.
	 * 
	 * @return {@code false}
	 */
	boolean skipUsedAttributes() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param consideredObjects {@inheritDoc}
	 * @param ruleConditions {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws ElementaryConditionNotFoundException when it is impossible to find any new condition that could be added to given rule conditions
	 */
	@Override
	public Condition<EvaluationField> getBestCondition(IntList consideredObjects, RuleConditions ruleConditions) {
		Precondition.notNull(consideredObjects, "List of objects considered in standard condition generator is null.");
		Precondition.notNull(ruleConditions, "Rule conditions considered in standard condition generator is null.");
		
		// TODO: implement
		return null;
	}

}
