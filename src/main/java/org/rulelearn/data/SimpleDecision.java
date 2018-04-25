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
import static org.rulelearn.core.Precondition.notNull;

import java.util.Objects;

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
	 * {@inheritDoc}
	 * 
	 * @param decision {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Decision decision) {
		// TODO: implement
		return null;
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
		return null;
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
		return null;
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
