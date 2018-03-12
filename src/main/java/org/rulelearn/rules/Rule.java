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

import org.rulelearn.types.Field;

/**
 * Decision rule composed of elementary conditions on the LHS, connected by "and", and decisions on the RHS, connected by "or".
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Rule {
	
	/**
	 * Type of this rule. See {@link RuleType}.
	 */
	protected RuleType type;
	
	/**
	 * Semantics of this rule. See {@link RuleSemantics}.
	 */
	protected RuleSemantics semantics;
	
	/**
	 * Value of decision attribute inherent for this rule.
	 * Value v of a decision attribute d is inherent for this rule if:<br>
     * - positive examples for which this rule was induced come from lower/upper approximation or boundary of a set of objects x such that d(x) is equal to v,<br>
     * - positive examples for which this rule was induced come from lower/upper approximation or boundary of a set of objects x such that d(x) is at least as good as v,<br>
     * - positive examples for which this rule was induced come from lower/upper approximation or boundary of a set of objects x such that d(x) is at most as good as v.
	 */
	protected Field inherentDecision;
	
	/**
     * Array with conditions building decision part of this rule.
     */
    protected Condition[] decisions = null;
}
