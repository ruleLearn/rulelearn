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

package org.rulelearn.data;

import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.UnknownSimpleField;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Decision reflecting a single object from an information table. It may relate to an {@link EvaluationField} evaluation of this object on the only active decision attribute,
 * or to a set of {@link EvaluationField} evaluations of this object on subsequent active decision attributes. Each such evaluation contributes to this decision.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Decision {
	
	/**
	 * Checks if this decision is at most as good as the other decision.
	 * 
	 * @param otherDecision other decision that this decision is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this decision is at most as good as the other decision,
	 *         {@link TernaryLogicValue#FALSE} if this decision is not at most as good as the other decision,
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	public abstract TernaryLogicValue isAtMostAsGoodAs(Decision otherDecision);
	
	/**
	 * Checks if this decision is at least as good as the other decision.
	 * 
	 * @param otherDecision other decision that this decision is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this decision is at least as good as the other decision,
	 *         {@link TernaryLogicValue#FALSE} if this decision is not at least as good as the other decision,
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	public abstract TernaryLogicValue isAtLeastAsGoodAs(Decision otherDecision);
	
	/**
	 * Checks if this decision is equal to the other decision.
	 * 
	 * @param otherDecision other decision that this decision is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this decision is equal to the other decision,
	 *         {@link TernaryLogicValue#FALSE} if this decision is not equal to the other decision,
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	public abstract TernaryLogicValue isEqualTo(Decision otherDecision);
	
	/**
	 * Gets particular evaluation contributing to this decision.
	 * 
	 * @param attributeIndex index of an attribute from an information table, for which contributing evaluation should be returned
	 * @return evaluation with respect to the attribute with given index, contributing to this decision, if there is such an evaluation,
	 *         {@code null} otherwise
	 */
	public abstract EvaluationField getEvaluation(int attributeIndex);
	
	/**
	 * Gets number of evaluations on active decision attributes contributing to this decision.
	 * 
	 * @return number of evaluations on active decision attributes contributing to this decision
	 */
	public abstract int getNumberOfEvaluations();
	
	/**
	 * Gets set of indices of active decision attributes contributing to this decision.
	 * 
	 * @return set of indices of active decision attributes contributing to this decision
	 */
	public abstract IntSet getAttributeIndices();
	
	/**
	 * Tells if this decision is equal to the other object.
	 * 
	 * @param otherObject other object that this decision should be compared with
	 * @return {@code true} if this decision is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public abstract boolean equals(Object otherObject);
	
	/**
     * Gets hash code of this decision.
     *
     * @return hash code of this decision
     */
	@Override
    public abstract int hashCode();
	
	/**
     * Gets text representation of this decision.
     *
     * @return text representation of this decision
     */
	@Override
    public abstract String toString();
	
	/**
     * Gets plain text representation of this decision.
     *
     * @return plain text representation of this decision
     */
    public abstract String serialize();
	
	/**
	 * Tells if this decision is fully-determined, i.e., all its contributing evaluations are non-missing (are instances of {@link KnownSimpleField}).
	 * 
	 * @return {@code true} if this decision is fully-determined, i.e., all its contributing evaluations are non-missing (are instances of {@link KnownSimpleField})
	 */
	public abstract boolean hasNoMissingEvaluation();
	
	/**
	 * Tells if this decision is fully-undetermined, i.e., all its contributing evaluations are missing (are instances of {@link UnknownSimpleField}).
	 * 
	 * @return {@code true} if this decision is fully-undetermined, i.e., all its contributing evaluations are missing (are instances of {@link UnknownSimpleField})
	 */
	public abstract boolean hasAllMissingEvaluations();
	
}