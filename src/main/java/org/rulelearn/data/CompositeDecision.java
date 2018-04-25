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

/**
 * Composite decision, i.e., decision corresponding to a collection of simultaneously fixed evaluations of active decision attributes.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CompositeDecision implements Decision {
	
	protected EvaluationField[] decisions;

	protected int[] attributeIndices;

	/**
	 * Constructs this composite decisions.
	 * 
	 * @param decisions evaluations on considered attributes, which should be active and decision attributes
	 * @param attributeIndices indices of attributes, which should be active and decision attributes
	 * 
	 * @throws InvalidValueException if the number of decisions is different than the number of attribute indices
	 */
	public CompositeDecision(EvaluationField[] decisions, int[] attributeIndices) {
		this.decisions = notNull(decisions, "Decisions of a composite decision are null.");
		this.attributeIndices = notNull(attributeIndices, "Attribute indices of a composite decision are null.");
		
		if (this.decisions.length != this.attributeIndices.length) {
			throw new InvalidValueException("Different number of decisions and attribute indices for a composite decision.");
		}
	}

	@Override
	public boolean isDominatedBy(Decision decision) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean dominates(Decision decision) {
		// TODO Auto-generated method stub
		return false;
	}

}
