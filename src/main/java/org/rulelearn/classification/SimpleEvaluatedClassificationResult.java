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

import org.rulelearn.core.Precondition;
import org.rulelearn.data.SimpleDecision;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;

/**
 * Simple result of classification of an object from an information table, reflecting its classification to exactly one decision class, obtained by choosing the decision that optimizes
 * the quality measure used to evaluate all conceivable decisions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleEvaluatedClassificationResult extends SimpleClassificationResult {
	
	/**
	 * Evaluation of suggested decision, which may be interpreted as its certainty.
	 */
	double suggestedDecisionEvaluation;
	
	/**
	 * Maps different conceivable decisions to their evaluations with respect to employed quality measure.
	 */
	Object2DoubleMap<SimpleDecision> decision2ScoreMap = null;
	
	/**
	 * Constructs this classification result using suggested decision and its evaluation.
	 * 
	 * @param suggestedDecision evaluation of an object on the decision attribute (i.e., decision) suggested by a classifier
	 * @param suggestedDecisionEvaluation evaluation of suggested decision
	 * 
	 * @throws NullPointerException if given suggested decision is {@code null}
	 */
	public SimpleEvaluatedClassificationResult(SimpleDecision suggestedDecision, double suggestedDecisionEvaluation) {
		super(suggestedDecision);
		this.suggestedDecisionEvaluation = suggestedDecisionEvaluation;
		this.decision2ScoreMap = new Object2DoubleOpenHashMap<SimpleDecision>();
		this.decision2ScoreMap.put(suggestedDecision, suggestedDecisionEvaluation);
	}
	
	/**
	 * Constructs this classification result using suggested decision and its evaluation.
	 * 
	 * @param suggestedDecision evaluation of an object on the decision attribute (i.e., decision) suggested by a classifier
	 * @param suggestedDecisionEvaluation evaluation of suggested decision
	 * @param decision2ScoreMap mapping between all considered decisions and their scores (including also given suggested decision)
	 * 
	 * @throws NullPointerException if given suggested decision or given map is {@code null}
	 */
	public SimpleEvaluatedClassificationResult(SimpleDecision suggestedDecision, double suggestedDecisionEvaluation, Object2DoubleMap<SimpleDecision> decision2ScoreMap) {
		super(suggestedDecision);
		this.suggestedDecisionEvaluation = suggestedDecisionEvaluation;
		this.decision2ScoreMap = Precondition.notNull(decision2ScoreMap, "Decision to score map is null.");
	}

	/**
	 * Gets evaluation of suggested decision.
	 * 
	 * @return evaluation of suggested decision
	 */
	public double getSuggestedDecisionEvaluation() {
		return suggestedDecisionEvaluation;
	}

	/**
	 * Gets a mapping between different conceivable decisions and their evaluations with respect to employed quality measure.
	 * The returned map should not be modified.
	 * 
	 * @return a mapping between different conceivable decisions and their evaluations with respect to employed quality measure;
	 *         {@code null} if not set in class constructor
	 */
	public Object2DoubleMap<SimpleDecision> getDecision2ScoreMap() {
		return decision2ScoreMap;
	}
	
	
}
