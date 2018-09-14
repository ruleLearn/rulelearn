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

import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Contract of a condition generator used to find next best condition {@link Condition} to be added to the LHS of a constructed decision rule.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ConditionGenerator {

	/**
	 * Gets best condition that can be added to given rule conditions, constructed on the basis of evaluations of objects whose indices are on the given list.
	 * Takes into account active condition attributes from the learning information table for which given rule conditions are defined, as returned by {@link RuleConditions#getLearningInformationTable()}.<br>
	 * <br>
	 * Remark that in general, the best condition is not guaranteed to improve evaluation of rule conditions (it can even deteriorate it).
	 * 
	 * @param consideredObjects list of indices of (positive) objects which are considered when generating candidate elementary conditions
	 * @param ruleConditions rule conditions for which best next condition is searched for
	 * @return best condition that can be added to given rule conditions
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws ElementaryConditionNotFoundException when it is impossible to find any new condition that could be added to given rule conditions
	 */
	public Condition<EvaluationField> getBestCondition(IntList consideredObjects, RuleConditions ruleConditions);
}
