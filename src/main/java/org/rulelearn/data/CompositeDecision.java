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
import static org.rulelearn.core.Precondition.nonNegative;

import java.util.Objects;

import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Composite decision reflecting an ordered set of evaluations of a single object on active decision attributes of an information table.
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
	 * Constructs this composite decision using an ordered set of evaluations contributing to this decision.
	 * 
	 * @param evaluations array of evaluations of a single object on active decision attributes of an information table
	 * @param attributeIndices indices of attributes of an information table, which should be active and decision attributes
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null} or if any single evaluation is {@code null}
	 * @throws InvalidValueException if the number of evaluations is different than the number of attribute indices
	 * @throws InvalidValueException if the number of evaluations is less than 2
	 * @throws InvalidValueException if any attribute index is negative
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
			this.attributeIndex2EvaluationMap.put(
					nonNegative(attributeIndices[i], "Attribute index is negative."),
					notNull(evaluations[i], "Evaluation contributing to a composite decision is null."));
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param decision {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Decision decision) {
		// TODO: implement
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param decision {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Decision decision) {
		// TODO: implement
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param decision {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public TernaryLogicValue isEqualTo(Decision decision) {
		// TODO: implement
		throw new UnsupportedOperationException("Not implemented yet!");
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
