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

import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.core.ComparisonResult;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.types.EvaluationField;

/**
 * At least condition that is satisfied by a given object's evaluation if that evaluation is greater than or equal to limiting evaluation of this condition.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
//"at least" condition for possible rules
public class ConditionAtLeastObjectGEThreshold<T extends EvaluationField> extends ConditionAtLeast<T> {

	/**
	 * Constructs this condition.
	 * 
	 * @param attributeWithContext structure embracing an attribute for which this condition is constructed and its contextual information; see {@link EvaluationAttributeWithContext}
	 * @param limitingEvaluation limiting evaluation of the constructed condition; see {@link #getLimitingEvaluation()}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ConditionAtLeastObjectGEThreshold(EvaluationAttributeWithContext attributeWithContext, T limitingEvaluation) {
		super(attributeWithContext, limitingEvaluation);
	}

	/**
     * {@inheritDoc}
     * 
     * @param evaluation {@inheritDoc}
     * @return {@inheritDoc}
     * 
     * @throws NullPointerException {@inheritDoc}
     */
	@Override
	public boolean satisfiedBy(EvaluationField evaluation) {
		ComparisonResult comparisonResult = notNull(evaluation, "Evaluation to be verified against condition is null.").compareToEnum(this.limitingEvaluation);
		return (comparisonResult == ComparisonResult.GREATER_THAN) || (comparisonResult == ComparisonResult.EQUAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConditionAtLeastObjectGEThreshold<T> duplicate() {
		return new ConditionAtLeastObjectGEThreshold<T>(this.attributeWithContext, this.limitingEvaluation);
	}

}
