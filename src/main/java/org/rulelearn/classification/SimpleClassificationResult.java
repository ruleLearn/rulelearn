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

package org.rulelearn.classification;

import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;
import org.rulelearn.data.SimpleDecision;
import static org.rulelearn.core.Precondition.notNull;

/**
 * Simple result of classification of an object from an information table, reflecting its classification to exactly one decision class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleClassificationResult extends ClassificationResult {
	
	/**
	 * Evaluation of an object on the decision attribute (i.e., decision) suggested by a classifier.
	 */
	protected SimpleDecision suggestedDecision;
	
	/**
	 * Constructs this classification result.
	 * 
	 * @param suggestedDecision evaluation of an object on the decision attribute (i.e., decision) suggested by a classifier
	 */
	public SimpleClassificationResult(SimpleDecision suggestedDecision) {
		super();
		this.suggestedDecision = notNull(suggestedDecision, "Decision suggested by a classifier is null.");
	}


	/**
	 * Gets evaluation of an object on the decision attribute (i.e., decision) suggested by a classifier.
	 * 
	 * @return evaluation of an object on the decision attribute (i.e., decision) suggested by a classifier
	 */
	@Override
	public SimpleDecision getSuggestedDecision() {
		return suggestedDecision;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param decision {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public TernaryLogicValue isConsistentWith(Decision decision) {
		notNull(decision, "Original decision, compared with the decision suggested by a classifier, is null.");
		
		return this.suggestedDecision.isEqualTo(decision);
	}

}
