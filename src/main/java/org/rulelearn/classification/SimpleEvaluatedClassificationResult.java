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

import org.rulelearn.data.SimpleDecision;

/**
 * Simple result of classification of an object from an information table, reflecting its classification to exactly one decision class, obtained by choosing the decision that optimizes
 * the quality measure used to evaluate all conceivable decisions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleEvaluatedClassificationResult extends SimpleClassificationResult {
	
	//TODO: store also distribution of evaluations among all decisions?
	
	/**
	 * Evaluation of suggested decision, which may be interpreted as its certainty.
	 */
	double suggestedDecisionEvaluation;
	
	/**
	 * Constructs this classification result using suggested decision and its evaluation.
	 * 
	 * @param suggestedDecision evaluation of an object on the decision attribute (i.e., decision) suggested by a classifier
	 * @param suggestedDecisionEvaluation evaluation of suggested decision
	 * 
	 * @throws NullPointerException if given suggested decision in {@code null}
	 */
	public SimpleEvaluatedClassificationResult(SimpleDecision suggestedDecision, double suggestedDecisionEvaluation) {
		super(suggestedDecision);
		this.suggestedDecisionEvaluation = suggestedDecisionEvaluation;
	}

	/**
	 * Gets evaluation of suggested decision.
	 * 
	 * @return evaluation of suggested decision
	 */
	public double getSuggestedDecisionEvaluation() {
		return suggestedDecisionEvaluation;
	}
	
	
}
