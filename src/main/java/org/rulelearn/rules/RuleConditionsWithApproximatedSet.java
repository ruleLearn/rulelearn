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

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.core.Precondition;

/**
 * Structure embracing rule conditions and approximated set for which these rule conditions have been built.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public final class RuleConditionsWithApproximatedSet {
	
	/**
	 * Set of elementary conditions on the left-hand side (LHS) of a decision rule induced to cover objects from considered approximated set.
	 */
	RuleConditions ruleConditions;
	/**
	 * Approximated set for which considered rule conditions have been built. 
	 */
	ApproximatedSet approximatedSet;
	
	/**
	 * Constructor initializing all fields.
	 * 
	 * @param ruleConditions set of elementary conditions on the left-hand side (LHS) of a decision rule induced to cover objects from given approximated set
	 * @param approximatedSet approximated set for which given rule conditions have been built
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleConditionsWithApproximatedSet(RuleConditions ruleConditions, ApproximatedSet approximatedSet) {
		super();
		this.ruleConditions = Precondition.notNull(ruleConditions, "Rule conditions are null.");
		this.approximatedSet = Precondition.notNull(approximatedSet, "Approximated set is null.");
	}

	/**
	 * Gets set of elementary conditions on the left-hand side (LHS) of a decision rule induced to cover objects from considered approximated set. 
	 * 
	 * @return set of elementary conditions on the left-hand side (LHS) of a decision rule induced to cover objects from considered approximated set.
	 */
	public RuleConditions getRuleConditions() {
		return ruleConditions;
	}

	/**
	 * Gets approximated set for which considered rule conditions have been built.
	 * 
	 * @return approximated set for which considered rule conditions have been built
	 */
	public ApproximatedSet getApproximatedSet() {
		return approximatedSet;
	}
	
}
