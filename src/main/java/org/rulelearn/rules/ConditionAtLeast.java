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

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.types.EvaluationField;

/**
 * Condition reflecting relation "&gt;=".
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class ConditionAtLeast<T extends EvaluationField> extends Condition<T> {
	
	/**
	 * Symbolic representation of relation embodied in this condition.
	 */
	String relationSymbol = ">=";
	
	/**
	 * Constructs this condition.
	 * 
	 * @param attributeWithContext structure embracing an attribute for which this condition is constructed and its contextual information; see {@link EvaluationAttributeWithContext}
	 * @param limitingEvaluation limiting evaluation of the constructed condition; see {@link #getLimitingEvaluation()}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	ConditionAtLeast(EvaluationAttributeWithContext attributeWithContext, T limitingEvaluation) {
		super(attributeWithContext, limitingEvaluation);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (new StringBuilder()).append(this.attributeWithContext.getAttributeName()).append(" ").append(this.getRelationSymbol()).append(" ").append(this.limitingEvaluation).toString();
	}

	/**
	 * {@inheritDoc}
	 * Checks if the other object is a {@link ConditionAtLeast} condition, defined for the attribute with the same index, and having the same limiting evaluation.
	 * 
	 * @param otherObject {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object otherObject) {
		return (otherObject instanceof ConditionAtLeast) 
				&& (((ConditionAtLeast<?>)otherObject).attributeWithContext.getAttributeIndex() == this.attributeWithContext.getAttributeIndex())
				&& (((ConditionAtLeast<?>)otherObject).limitingEvaluation.equals(this.limitingEvaluation));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRelationSymbol() {
		return this.relationSymbol;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws InvalidValueException {@inheritDoc}
	 * @throws InvalidValueException if the preference type of the decision attribute is neither {@link AttributePreferenceType#GAIN} nor {@link AttributePreferenceType#COST}
	 */
	@Override
	public RuleSemantics getRuleSemantics() {
		if (this.attributeWithContext.getAttributeType() != AttributeType.DECISION) {
			throw new InvalidValueException("Cannot establish rule semantics given an 'at least' condition not defined on a decision attribute.");
		}
		
		if (this.attributeWithContext.getAttributePreferenceType() == AttributePreferenceType.GAIN) {
			return RuleSemantics.AT_LEAST;
		} else if (this.attributeWithContext.getAttributePreferenceType() == AttributePreferenceType.COST) {
			return RuleSemantics.AT_MOST;
		} else {
			throw new InvalidValueException("Cannot establish rule semantics given an 'at least' condition w.r.t. a decision attribute without preference type.");
			//TODO: do something else?
		}
	}

}
