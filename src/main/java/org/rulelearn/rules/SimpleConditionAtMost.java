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

import org.rulelearn.core.ComparisonResult;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.types.SimpleField;
import static org.rulelearn.core.Precondition.notNull;

/**
 * Condition reflecting evaluations of type {@link SimpleField} and relation "&lt;=".
 * This condition is satisfied by {@link SimpleField} evaluations that are smaller than or equal to the stored {@link SimpleField} limiting evaluation.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleConditionAtMost extends SimpleCondition {

	/**
	 * Symbolic representation of relation embodied in this condition.
	 */
	protected String relationSymbol = "<=";
	
	/**
	 * Constructs this condition.
	 * 
	 * @param attributeWithContext structure embracing an attribute for which this condition is constructed and its contextual information; see {@link EvaluationAttributeWithContext}
	 * @param limitingEvaluation limiting evaluation of the constructed condition; see {@link #getLimitingEvaluation()}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public SimpleConditionAtMost(EvaluationAttributeWithContext attributeWithContext, SimpleField limitingEvaluation) {
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
	public boolean satisfiedBy(SimpleField evaluation) {
		ComparisonResult comparisonResult =  notNull(evaluation, "Evaluation to be verified against condition is null.").compareToEnum(this.limitingEvaluation);
		return (comparisonResult == ComparisonResult.SMALLER_THAN) || (comparisonResult == ComparisonResult.EQUAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimpleConditionAtMost duplicate() {
		return new SimpleConditionAtMost(this.attributeWithContext, this.limitingEvaluation);
	}

	/**
	 * {@inheritDoc}
	 * Checks if the other object is a {@link SimpleConditionAtMost} condition, defined for the attribute with the same index, and having the same limiting evaluation.
	 * 
	 * @param otherObject {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object otherObject) {
		return (otherObject instanceof SimpleConditionAtMost) 
				&& (((SimpleConditionAtMost)otherObject).attributeWithContext.getAttributeIndex() == this.attributeWithContext.getAttributeIndex())
				&& (((SimpleConditionAtMost)otherObject).limitingEvaluation.equals(this.limitingEvaluation));
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
			throw new InvalidValueException("Cannot establish rule semantics given a simple 'at most' condition not defined on a decision attribute.");
		}
		
		if (this.attributeWithContext.getAttributePreferenceType() == AttributePreferenceType.GAIN) {
			return RuleSemantics.AT_MOST;
		} else if (this.attributeWithContext.getAttributePreferenceType() == AttributePreferenceType.COST) {
			return RuleSemantics.AT_LEAST;
		} else {
			throw new InvalidValueException("Cannot establish rule semantics given a simple 'at most' condition w.r.t. a decision attribute without preference type.");
			//TODO: do something else?
		}
	}

}
