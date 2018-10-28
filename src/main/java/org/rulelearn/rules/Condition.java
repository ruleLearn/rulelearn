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

import java.util.Objects;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.AttributeWithContext;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EvaluationField;

/**
 * Condition of a decision rule. May be present both in the condition part and in the decision part of the rule.
 * 
 * @param <T> type of limiting evaluation of this condition and type of evaluations against which this condition is going to be checked
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Condition<T extends EvaluationField> {
	
	/**
	 * Information about an attribute for which this condition has been created.
	 */
	EvaluationAttributeWithContext attributeWithContext;
	
	/**
	 * Limiting evaluation with respect to which this condition is defined. E.g., in case of condition 'price &gt;= 5', limiting evaluation is equal to 5.
	 */
	T limitingEvaluation;
	
	/**
	 * Gets the limiting evaluation of this condition.
	 * E.g., in case of condition 'price &gt;= 5', limiting evaluation is equal to 5.
	 * 
	 * @return the limiting evaluation of this condition
	 */
	public T getLimitingEvaluation() {
		return limitingEvaluation;
	}

	/**
	 * Constructs this condition. Used by subclass constructors.
	 * 
	 * @param attributeWithContext structure embracing an attribute for which this condition is constructed and its contextual information; see {@link AttributeWithContext}
	 * @param limitingEvaluation limiting evaluation of the constructed condition; see {@link #getLimitingEvaluation()}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	Condition(EvaluationAttributeWithContext attributeWithContext, T limitingEvaluation) {
		this.attributeWithContext = notNull(attributeWithContext, "Attribute with context of constructed condition is null.");
		this.limitingEvaluation = notNull(limitingEvaluation, "Limiting evaluation of constructed condition is null.");
	}

	/**
     * Checks if given evaluation of an object satisfies this condition.
     * 
     * @param evaluation evaluation (field) to check
     * @return {@code true} if given evaluation satisfies this condition, {@code false} otherwise
     * 
     * @throws NullPointerException if given evaluation does not conform to {@link org.rulelearn.core.Precondition#notNull(Object, String)}
     */
    public abstract boolean satisfiedBy(T evaluation);
    
    /**
     * Checks if an object from the information table, defined by its index, fulfills this condition.
     * 
     * @param objectIndex index of an object in the given information table
     * @param informationTable information table containing the object to check (and possibly also other objects)
     * 
     * @return {@code true} if considered object fulfills this condition, {@code false} otherwise
     * 
     * @throws IndexOutOfBoundsException if given object index does not correspond to any object in the given information table
     * @throws NullPointerException if given information table is {@code null}
     * @throws ClassCastException if the field against which this condition should be checked is not of type {@link T}
     * @throws IndexOutOfBoundsException if attribute index of this condition does not correspond to any attribute for which given information table stores fields
     */
    @SuppressWarnings("unchecked")
    public boolean satisfiedBy(int objectIndex, InformationTable informationTable) {
    	return this.satisfiedBy((T)informationTable.getField(objectIndex, this.attributeWithContext.getAttributeIndex()));
    }
    
	/**
	 * Gets text representation of this condition
	 * 
	 * @return text representation of this condition
	 */
	public abstract String toString();
	
	/**
	 * Gets symbol of relation embodied in this condition.
	 * 
	 * @return symbol of relation embodied in this condition
	 */
	public abstract String getRelationSymbol();
	
	/**
	 * Gets a "meta" object storing attribute for which this condition is defined + context of that attribute.
	 * See {@link AttributeWithContext}.
	 * 
	 * @return Gets a "meta" object storing attribute for which this condition is defined + context of that attribute.
	 */
	public EvaluationAttributeWithContext getAttributeWithContext() {
		return this.attributeWithContext;
	}

	/**
	 * Returns duplicate of this condition
	 * 
	 * @return duplicate of this condition
	 */
	public abstract Condition<T> duplicate();
	
	/**
	 * Tells if this condition is equal to the other object.
	 * 
	 * @param otherObject other object that this object should be compared with
	 * @return {@code true} if this object is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public abstract boolean equals(Object otherObject);
	
	/**
     * Gets hash code of this condition.
     *
     * @return hash code of this condition
     */
	@Override
    public int hashCode() {
		return Objects.hash(this.getClass(), this.limitingEvaluation);
	}
	
	/**
	 * Gets semantics of a decision rule having this condition on the RHS, as the only condition.
	 * 
	 * @return semantics of a decision rule having this condition on the RHS, as the only condition
	 * @throws InvalidValueException if the type of the attribute for which this condition is defined is not decision one
	 */
	public abstract RuleSemantics getRuleSemantics();
}
