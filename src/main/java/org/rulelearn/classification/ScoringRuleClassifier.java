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

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Rule classifier based on measure Score(Cl,z) - where Cl denotes a decision class, and z denotes classified object - described in the paper:<br>
 * J. Błaszczyński, S. Greco, R. Słowiński, Multi-criteria classification - A new scheme for application of dominance-based decision rules.
 * European Journal of Operational Research, 181(3), 2007, pp. 1030-1044.<br>
 * This classifier can be used in two modes:<br>
 * - score, i.e., always using Score measure to compute optimal classification decision (as proposed in the above paper), or<br>
 * - hybrid, using Score measure only to resolve conflicts (e.g., if covering rules suggest class &gt;=1 and &lt;=2, or if covering rules suggest class &gt;=3 and &lt;=1);
 *   if there is no conflict, returns "standard" prudent classification decision (e.g., for covering rules suggesting classes &gt;=2 and &gt;=3 returns class 3,
 *   while for covering rules suggesting classes &lt;=1 and &lt;=2 returns class 1).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ScoringRuleClassifier extends RuleClassifier implements SimpleClassifier {
	
	public static enum Mode {
		/**
		 * Mode indicating that always Score measure is used to compute optimal classification decision.
		 */
		SCORE,
		/**
		 * Mode indicating that Score measure is used to compute optimal classification decision
		 * only to resolve conflicts (e.g., if rules suggest class &gt;=1 and &lt;=2, or if rules suggest class &gt;=3 and &lt;=1).
		 */
		HYBRID
	}
	
	/**
	 * Classification mode used by this classifier.
	 */
	Mode mode;

	/**
	 * Constructs this classifier.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from an information table
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules
	 * @param mode classification mode to be used by this classifier; {@link Mode#HYBRID} is expected to give better results on average
	 * @param learningInformationTable learning (training) information table for which rules from the given rule set have been induced
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ScoringRuleClassifier(RuleSet ruleSet, SimpleEvaluatedClassificationResult defaultClassificationResult, Mode mode, InformationTable learningInformationTable) {
		super(ruleSet, defaultClassificationResult);
		this.mode = Precondition.notNull(mode, "Classification mode of scoring rule classifier is null.");
		//TODO: check (and save?) learningInformationTable
	}
	
	/**
	 * Gets classification mode used by this classifier.
	 * 
	 * @return classification mode used by this classifier
	 */
	public Mode getMode() {
		return mode;
	}
	
	/**
	 * Gets default classification result returned by this classifier if it is unable to calculate such a result (no rule covers classified object).
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public SimpleEvaluatedClassificationResult getDefaultClassificationResult() {
		return (SimpleEvaluatedClassificationResult)defaultClassificationResult;
	}

	/**
	 * Classifies an object from an information table using rules stored in this classifier.
	 * 
	 * @param objectIndex {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * 
	 * @return simple optimal classification result for the considered object
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering considered object cannot be compared
	 */
	@Override
	public SimpleEvaluatedClassificationResult classify(int objectIndex, InformationTable informationTable) {
		return getDefaultClassificationResult(); //TODO: implement
	}

	/**
	 * Classifies all objects from the given information table.
	 * 
	 * @param informationTable {@inheritDoc}
	 * @return array with simple optimal classification results for subsequent objects from the given information table
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering any considered object cannot be compared
	 */
	@Override
	public SimpleEvaluatedClassificationResult[] classifyAll(InformationTable informationTable) {
		SimpleEvaluatedClassificationResult[] classificationResults = new SimpleEvaluatedClassificationResult[informationTable.getNumberOfObjects()];
		for (int i = 0; i < classificationResults.length; i++) {
			classificationResults[i] = this.classify(i, informationTable);
		}
		return classificationResults;
	}

	/**
	 * Classifies an object from an information table, recording indices of covering rules at the given list.
	 * 
	 * @param objectIndex {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * @param indicesOfCoveringRules {@inheritDoc}
	 * 
	 * @return simple optimal classification result for the considered object
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering considered object cannot be compared
	 */
	@Override
	public SimpleEvaluatedClassificationResult classify(int objectIndex, InformationTable informationTable, IntList indicesOfCoveringRules) {
		Condition<EvaluationField> decisionCondition = null; //decision condition of the current rule
		int rulesCount = this.ruleSet.size();
		IntList indicesOfCoveringAtLeastRules = new IntArrayList();
		IntList indicesOfCoveringAtMostRules = new IntArrayList();
		
		for (int i = 0; i < rulesCount; i++) {
			if (this.ruleSet.getRule(i).covers(objectIndex, informationTable)) { //current rule covers considered object
				decisionCondition = this.ruleSet.getRule(i).getDecision();
				
				if (decisionCondition instanceof ConditionAtLeast<?>) {
					indicesOfCoveringAtLeastRules.add(i); //remember index of covering "at least" rule
				}
				else if (decisionCondition instanceof ConditionAtMost<?>) {
					indicesOfCoveringAtMostRules.add(i); //remember index of covering "at most" rule
				}
			}
		}
		
		//append to given list indices of all covering rules
		indicesOfCoveringRules.addAll(indicesOfCoveringAtLeastRules);
		indicesOfCoveringRules.addAll(indicesOfCoveringAtMostRules);
		
		return getDefaultClassificationResult(); //TODO: implement
	}

}
