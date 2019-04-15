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

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;

/**
 * Abstract rule conditions set pruner, storing array of rule conditions evaluators and implementing {@link RuleConditionsSetPruner} interface.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class AbstractRuleConditionsSetPrunerWithEvaluators implements RuleConditionsSetPruner {
	
	/**
	 * Rule conditions evaluators used lexicographically to evaluate each rule conditions {@link RuleConditions} considered by this pruner.
	 */
	RuleConditionsEvaluator[] ruleConditionsEvaluators;
	
	/**
	 * Constructor for this rule conditions set pruner storing given rule conditions evaluators.
	 * 
	 * @param ruleConditionsEvaluators rule conditions evaluators used lexicographically to evaluate each {@link RuleConditions} from considered list
	 * 
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 */
	public AbstractRuleConditionsSetPrunerWithEvaluators(RuleConditionsEvaluator[] ruleConditionsEvaluators) {
		super();
		this.ruleConditionsEvaluators = Precondition.nonEmpty(Precondition.notNullWithContents(ruleConditionsEvaluators,
				"Rule conditions evaluators are null.",
				"Rule conditions evaluator is null at index %i."), "Array of rule conditions evaluators is empty.");
	}

	/**
	 * Gets array with {@link RuleConditionsEvaluator rules conditions evaluators} for which this pruner has been constructed.
	 * 
	 * @return array with {@link RuleConditionsEvaluator rules conditions evaluators} for which this pruner has been constructed
	 */
	public RuleConditionsEvaluator[] getRuleConditionsEvaluators() {
		return this.getRuleConditionsEvaluators(false);
	}
	
	/**
	 * Gets array with {@link RuleConditionsEvaluator rules conditions evaluators} for which this pruner has been constructed.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array with {@link RuleConditionsEvaluator rules conditions evaluators} for which this pruner has been constructed
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public RuleConditionsEvaluator[] getRuleConditionsEvaluators(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? this.ruleConditionsEvaluators : this.ruleConditionsEvaluators.clone(); //evaluators should not be null at this point (the array is verified in constructor)
	}

}
