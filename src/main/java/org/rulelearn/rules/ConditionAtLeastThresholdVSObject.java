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
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.types.EvaluationField;

/**
 * At least condition that is satisfied by a given object's evaluation if limiting evaluation of this condition is smaller than or equal to that evaluation.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
//"at least" condition for certain rules
public class ConditionAtLeastThresholdVSObject<T extends EvaluationField> extends ConditionAtLeast<T> {

	/**
	 * Constructs this condition.
	 * 
	 * @param attributeWithContext structure embracing an attribute for which this condition is constructed and its contextual information; see {@link EvaluationAttributeWithContext}
	 * @param limitingEvaluation limiting evaluation of the constructed condition; see {@link #getLimitingEvaluation()}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ConditionAtLeastThresholdVSObject(EvaluationAttributeWithContext attributeWithContext, T limitingEvaluation) {
		super(attributeWithContext, limitingEvaluation);
	}
	
	/**
	 * {@inheritDoc}
	 * Checks if the other object is a {@link ConditionAtLeastThresholdVSObject} condition, defined for the attribute with the same index, and having the same limiting evaluation.
	 * 
	 * @param otherObject {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object otherObject) {
		return (otherObject instanceof ConditionAtLeastThresholdVSObject) 
				&& (((ConditionAtLeastThresholdVSObject<?>)otherObject).attributeWithContext.getAttributeIndex() == this.attributeWithContext.getAttributeIndex())
				&& (((ConditionAtLeastThresholdVSObject<?>)otherObject).limitingEvaluation.equals(this.limitingEvaluation));
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
	public boolean satisfiedBy(T evaluation) {
		ComparisonResult comparisonResult = this.limitingEvaluation.compareToEnum(notNull(evaluation, "Evaluation to be verified against at least condition is null."));
		return (comparisonResult == ComparisonResult.SMALLER_THAN) || (comparisonResult == ComparisonResult.EQUAL);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (new StringBuilder()).append(this.limitingEvaluation).append(" ").append(this.getRelationSymbol()).append(" ").append(this.attributeWithContext.getAttributeName()).toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConditionAtLeastThresholdVSObject<T> duplicate() {
		return new ConditionAtLeastThresholdVSObject<T>(this.attributeWithContext, this.limitingEvaluation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <S extends EvaluationField> TernaryLogicValue isAtMostAsGeneralAs(Condition<S> otherCondition) {
		if (otherCondition instanceof ConditionAtLeastThresholdVSObject &&
				otherCondition.getLimitingEvaluation().getClass().isAssignableFrom(this.limitingEvaluation.getClass())) {
			return otherCondition.satisfiedBy((S)this.limitingEvaluation) ? TernaryLogicValue.TRUE : TernaryLogicValue.FALSE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

}
