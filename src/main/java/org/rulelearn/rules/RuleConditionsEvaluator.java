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
import org.rulelearn.core.ValueAlreadySetException;
import org.rulelearn.types.EvaluationField;

/**
 * Evaluates rule conditions using a real number.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class RuleConditionsEvaluator {
	
	/**
	 * Evaluation context.
	 */
	ConditionEvaluationContext context;
	
	/**
	 * TODO
	 * @param ruleConditions TODO
	 * @param condition TODO
	 * @return TODO
	 */
	public abstract double evaluate(RuleConditions ruleConditions, Condition<EvaluationField> condition);
	
	/**
	 * TODO
	 * @param ruleConditions TODO
	 * @return TODO
	 */
	public abstract double evaluate(RuleConditions ruleConditions);
	
	/**
	 * Sets condition evaluation context. This is possible only once.
	 * 
	 * @param context context to set
	 * @throws ValueAlreadySetException if context is already set
	 * @throws NullPointerException if given context is {@code null}
	 */
	public void setEvaluationContext(ConditionEvaluationContext context) {
		if (this.context != null) {
			throw new ValueAlreadySetException("Condition evaluation context already set.");
		}
		
		this.context = Precondition.notNull(context, "Condition evaluation context is null.");
	}

}
