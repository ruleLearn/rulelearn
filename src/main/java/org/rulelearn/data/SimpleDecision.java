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
import org.rulelearn.core.TernaryLogicValue;

/**
 * Simple decision reflecting {@link EvaluationField} evaluation of a single object on the only active decision attribute of an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleDecision implements Decision {
	
	/**
	 * Evaluation of a single object on the only active decision attribute of an information table.
	 */
	protected EvaluationField decision;

	/**
	 * Index of the only active decision attribute of an information table.
	 */
	protected int attributeIndex;

	/**
	 * Constructs this decision.
	 * 
	 * @param decision evaluation of a single object on the only active decision attribute of an information table
	 * @param attributeIndex index of the only active decision attribute of an information table
	 */
	public SimpleDecision(EvaluationField decision, int attributeIndex) {
		this.decision = notNull(decision, "Simple decision is null.");
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
	
	
	
}
