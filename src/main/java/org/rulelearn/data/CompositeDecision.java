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

import static org.rulelearn.core.Precondition.notNull;
import java.util.Objects;
import java.util.function.BiPredicate;
import org.rulelearn.types.EvaluationField;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Composite decision reflecting a set of at least two evaluations of a single object on active decision attributes of an information table.
 * Each such evaluation contributes to this composite decision.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CompositeDecision extends Decision {
	
	/**
	 * Maps attribute index to an evaluation on that attribute that contributes to this composite decision.
	 */
	protected Int2ObjectOpenHashMap<EvaluationField> attributeIndex2EvaluationMap;

	/**
	 * Constructs this composite decision using an array of evaluations contributing to this decision.
	 * 
	 * @param evaluations array of evaluations of a single object on active decision attributes of an information table
	 * @param attributeIndices corresponding indices of active and decision attributes of an information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null} or if any single evaluation is {@code null}
	 * @throws InvalidValueException if the number of evaluations is different than the number of attribute indices
	 * @throws InvalidValueException if the number of evaluations is less than 2
	 */
	public CompositeDecision(EvaluationField[] evaluations, int[] attributeIndices) {
		notNull(evaluations, "Evaluations of a composite decision are null.");
		notNull(attributeIndices, "Attribute indices of a composite decision are null.");
		
		if (evaluations.length != attributeIndices.length) {
			throw new InvalidValueException("Different number of evaluations and attribute indices for a composite decision.");
		}
		
		if (evaluations.length < 2) {
			throw new InvalidValueException("Not enough contributing evaluations to construct a composite decision.");
		}
		
		this.attributeIndex2EvaluationMap = new Int2ObjectOpenHashMap<>(attributeIndices.length); //use known number of elements in the map to avoid its resizing
		
		for (int i = 0; i < attributeIndices.length; i++) {
			this.attributeIndex2EvaluationMap.put(attributeIndices[i], notNull(evaluations[i], "Evaluation contributing to a composite decision is null."));
		}

	}
	
	/**
	 * Checks if this composite decision is in relation with the other decision (expected to also be a composite decision).
	 * Such relation holds if for each pair of corresponding evaluations (i.e., evaluations on the same attribute),
	 * {@code integralRelationTester} verifies that the first evaluation (contributing to the first decision) is in a particular relation with the second evaluation (contributing to the second decision).
	 * Two evaluations are in relation verified by {@code integralRelationTester} if:<br>
	 * <br>
	 * {@code integralRelationTester.test(firstEvaluation, secondEvaluation) == true}.
	 * 
	 * @param otherDecision other decision that this decision is being compared to
	 * @param nullPointerMessage message of the {@link NullPointerException} thrown if given other decision is {@code null}
	 * @param integralRelationTester object implementing {@link BiPredicate#test(Object, Object)} method that compares two evaluations;
	 *        this method is used to compare every two corresponding evaluations (i.e., evaluations on the same attribute), the first from this decision, and the second from the other decision
	 * @return {@link TernaryLogicValue#TRUE} if this decision is in relation with the other decision
	 *         {@link TernaryLogicValue#FALSE} if this decision is not in relation with the other decision
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison,
	 *         or the other decision concerns a different set of attributes
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	protected TernaryLogicValue isInRelationWith(Decision otherDecision, String nullPointerMessage, BiPredicate<EvaluationField, EvaluationField> integralRelationTester) {
		notNull(otherDecision, nullPointerMessage);
		
		if (otherDecision instanceof CompositeDecision) {
			CompositeDecision otherCompositeDecision = (CompositeDecision)otherDecision;
			
			if (this.attributeIndex2EvaluationMap.size() == otherCompositeDecision.attributeIndex2EvaluationMap.size()) {
				
				for (Int2ObjectMap.Entry<EvaluationField> entry : this.attributeIndex2EvaluationMap.int2ObjectEntrySet()) {
					EvaluationField ownEvaluation = entry.getValue();
					EvaluationField otherEvaluation = otherCompositeDecision.getEvaluation(entry.getIntKey());
					
					if (otherEvaluation != null) {
						if (!integralRelationTester.test(ownEvaluation, otherEvaluation)) {
							return TernaryLogicValue.FALSE;
						}
					} else {
						return TernaryLogicValue.UNCOMPARABLE;
					}
				}
				
				return TernaryLogicValue.TRUE;
				
			} else {
				return TernaryLogicValue.UNCOMPARABLE;
			}
			
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}
	
	/**
	 * {@inheritDoc} The other decision is expected to also be a composite decision.
	 * 
	 * @param otherDecision {@inheritDoc}
	 * @return {@link TernaryLogicValue#TRUE} if this decision is at most as good as the other decision
	 *         {@link TernaryLogicValue#FALSE} if this decision is not at most as good as the other decision
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison,
	 *         or the other decision concerns a different set of attributes
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Decision otherDecision) {
		return this.isInRelationWith(otherDecision, "Cannot verify if a composite decision is at most as good as null.",
				(evaluation1, evaluation2) -> evaluation1.isAtMostAsGoodAs(evaluation2) == TernaryLogicValue.TRUE);
	}

	/**
	 * {@inheritDoc} The other decision is expected to also be a composite decision.
	 * 
	 * @param otherDecision {@inheritDoc}
	 * @return {@link TernaryLogicValue#TRUE} if this decision is at least as good as the other decision
	 *         {@link TernaryLogicValue#FALSE} if this decision is not at least as good as the other decision
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison,
	 *         or the other decision concerns a different set of attributes
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Decision otherDecision) {
		return this.isInRelationWith(otherDecision, "Cannot verify if a composite decision is at least as good as null.",
				(evaluation1, evaluation2) -> evaluation1.isAtLeastAsGoodAs(evaluation2) == TernaryLogicValue.TRUE);
	}

	/**
	 * {@inheritDoc} The other decision is expected to also be a composite decision.
	 * 
	 * @param otherDecision {@inheritDoc}
	 * @return {@link TernaryLogicValue#TRUE} if this decision is equal to the other decision
	 *         {@link TernaryLogicValue#FALSE} if this decision is not equal to the other decision
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other decision prevents comparison,
	 *         or the other decision concerns a different set of attributes
	 * @throws NullPointerException if the other decision is {@code null}
	 */
	@Override
	public TernaryLogicValue isEqualTo(Decision otherDecision) {
		return this.isInRelationWith(otherDecision, "Cannot verify if a composite decision is equal to null.",
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
		return this.attributeIndex2EvaluationMap.containsKey(attributeIndex) ? this.attributeIndex2EvaluationMap.get(attributeIndex) : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumberOfEvaluations() {
		return attributeIndex2EvaluationMap.size();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param otherObject {@inheritDoc}
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof CompositeDecision &&
				((CompositeDecision)otherObject).attributeIndex2EvaluationMap.equals(this.attributeIndex2EvaluationMap)) {
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
		return Objects.hash(this.getClass(), attributeIndex2EvaluationMap);
	}

}
