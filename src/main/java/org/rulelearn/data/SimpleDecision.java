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

import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

import static org.rulelearn.core.Precondition.notNull;
import java.util.Objects;
import java.util.function.BiPredicate;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Simple decision reflecting {@link EvaluationField} evaluation of a single object on an active decision attribute of an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleDecision extends Decision {
	
	/**
	 * Evaluation of a single object on an active decision attribute of an information table.
	 */
	protected EvaluationField evaluation;

	/**
	 * Index of an active decision attribute of an information table.
	 */
	protected int attributeIndex;

	/**
	 * Constructs this simple decision.
	 * 
	 * @param evaluation evaluation of a single object on an active decision attribute of an information table
	 * @param attributeIndex index of an active decision attribute of an information table
	 */
	public SimpleDecision(EvaluationField evaluation, int attributeIndex) {
		super();
		this.evaluation = notNull(evaluation, "Evaluation of constructed simple decision is null.");
		this.attributeIndex = attributeIndex;
	}
	
	/**
	 * Checks if this simple decision is in relation with the other decision (expected to also be a simple decision). Such relation holds if both decisions concern the same attribute,
	 * and {@code integralRelationTester} verifies that the first evaluation (contributing to the first decision) is in a particular relation with the second evaluation (contributing to the second decision).
	 * Two evaluations are in relation verified by {@code integralRelationTester} if:<br>
	 * <br>
	 * {@code integralRelationTester.test(firstEvaluation, secondEvaluation) == true}.
	 * 
	 * @param otherDecision other decision that this decision is being compared to
	 * @param nullPointerMessage message of the {@link NullPointerException} thrown if given other decision is {@code null}
	 * @param integralRelationTester object implementing {@link BiPredicate#test(Object, Object)} method that compares two evaluations;
	 *        this method is used to compare two evaluations, the first from this decision, and the second from the other decision
	 * @return {@link TernaryLogicValue#TRUE} if this decision is in relation with the other decision
	 *         {@link TernaryLogicValue#FALSE} if this decision is not in relation with the other decision
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison,
	 *         or the other decision concerns a different attribute
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	protected TernaryLogicValue isInRelationWith(Decision otherDecision, String nullPointerMessage, BiPredicate<EvaluationField, EvaluationField> integralRelationTester) {
		notNull(otherDecision, nullPointerMessage);
		
		if (otherDecision instanceof SimpleDecision) {
			SimpleDecision otherSimpleDecision = (SimpleDecision)otherDecision;
			
			if (this.attributeIndex == otherSimpleDecision.attributeIndex) {
				return integralRelationTester.test(this.evaluation, otherSimpleDecision.evaluation) ? TernaryLogicValue.TRUE : TernaryLogicValue.FALSE;
			} else {
				return TernaryLogicValue.UNCOMPARABLE;
			}
			
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}
	
	/**
	 * {@inheritDoc} The other decision is expected to also be a simple decision.
	 * 
	 * @param otherDecision {@inheritDoc}
	 * @return {@link TernaryLogicValue#TRUE} if this decision is at most as good as the other decision
	 *         {@link TernaryLogicValue#FALSE} if this decision is not at most as good as the other decision
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison, or the other decision concerns a different attribute
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Decision otherDecision) {
		return this.isInRelationWith(otherDecision, "Cannot verify if a simple decision is at most as good as null.",
				(evaluation1, evaluation2) -> evaluation1.isAtMostAsGoodAs(evaluation2) == TernaryLogicValue.TRUE);
	}

	/**
	 * {@inheritDoc} The other decision is expected to also be a simple decision.
	 * 
	 * @param otherDecision {@inheritDoc}
	 * @return {@link TernaryLogicValue#TRUE} if this decision is at least as good as the other decision
	 *         {@link TernaryLogicValue#FALSE} if this decision is not at least as good as the other decision
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison, or the other decision concerns a different attribute
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Decision otherDecision) {
		return this.isInRelationWith(otherDecision, "Cannot verify if a simple decision is at least as good as null.",
				(evaluation1, evaluation2) -> evaluation1.isAtLeastAsGoodAs(evaluation2) == TernaryLogicValue.TRUE);
	}

	/**
	 * {@inheritDoc} The other decision is expected to also be a simple decision.
	 * 
	 * @param otherDecision other decision that this decision is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this decision is equal to the other decision,
	 *         {@link TernaryLogicValue#FALSE} if this decision is not equal to the other decision,
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison, or the other decision concerns a different attribute
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	@Override
	public TernaryLogicValue isEqualTo(Decision otherDecision) {
		return this.isInRelationWith(otherDecision, "Cannot verify if a simple decision is equal to null.",
				(evaluation1, evaluation2) -> evaluation1.isEqualTo(evaluation2) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param attributeIndex {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationField getEvaluation(int attributeIndex) {
		return attributeIndex == this.attributeIndex ? this.evaluation : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumberOfEvaluations() {
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntSet getAttributeIndices() {
		IntSet indexSet = new IntArraySet(1);
		indexSet.add(this.attributeIndex);
		
		return indexSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param otherObject {@inheritDoc}
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof SimpleDecision &&
				((SimpleDecision)otherObject).attributeIndex == this.attributeIndex &&
				((SimpleDecision)otherObject).evaluation.equals(this.evaluation)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getClass(), evaluation, attributeIndex);
	}
	
}
