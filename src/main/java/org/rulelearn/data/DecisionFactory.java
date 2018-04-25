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

/**
 * Factory of {@link Decision} instances, written according to singleton pattern.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public enum DecisionFactory {
	
	/**
	 * The only instance of this decision factory.
	 */
	INSTANCE;
	
	/**
	 * Constructs a decision. If there is exactly one evaluation and one attribute index, this factory tries to construct an instance of {@link SimpleDecision}.
	 * Otherwise, this factory tries to construct an instance of {@link CompositeDecision}. This method forwards any exception throw by the respective constructor.
	 * 
	 * @param evaluations evaluations of a single object from an information table on subsequent active decision attributes
	 * @param attributeIndices indices of subsequent active decision attributes
	 * 
	 * @return constructed decision
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public Decision create(EvaluationField[] evaluations, int[] attributeIndices) {
		if (evaluations.length == 1 && attributeIndices.length == 1) {
			return new SimpleDecision(evaluations[0], attributeIndices[0]); //further validation done inside constructor
		} else {
			return new CompositeDecision(evaluations, attributeIndices); //further validation done inside constructor
		}
	}
}
