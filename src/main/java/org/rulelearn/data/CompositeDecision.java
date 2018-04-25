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
import org.rulelearn.types.EvaluationField;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Composite decision reflecting an ordered set of evaluations of a single object on active decision attributes of an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CompositeDecision implements Decision {
	
	/**
	 * Array of evaluations of a single object on active decision attributes of an information table.
	 */
	protected EvaluationField[] decisions;

	/**
	 * Indices of considered active decision attributes of an information table.
	 */
	protected int[] attributeIndices;

	/**
	 * Constructs this composite decision.
	 * 
	 * @param decisions array of evaluations of a single object on active decision attributes of an information table;
	 *        each entry has to be different than {@code null}
	 * @param attributeIndices indices of attributes of an information table, which should be active and decision attributes
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if the number of decisions is different than the number of attribute indices
	 */
	public CompositeDecision(EvaluationField[] decisions, int[] attributeIndices) {
		this.decisions = notNull(decisions, "Evaluations of a composite decision are null.");
		this.attributeIndices = notNull(attributeIndices, "Attribute indices of a composite decision are null.");
		
		if (this.decisions.length != this.attributeIndices.length) {
			throw new InvalidValueException("Different number of evaluations and attribute indices for a composite decision.");
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

}
