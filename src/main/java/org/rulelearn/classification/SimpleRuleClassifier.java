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

import org.rulelearn.core.MeanCalculator;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.core.UncomparableException;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.types.EvaluationField;

/**
 * Simple classifier using decision rules to classify each object from an information table to exactly one decision class.
 * If no rule matches an object, returns for that object a {@link #getDefaultClassificationResult() default classification result}.
 * Calculates intersection of covering at least / at most rules.
 * For example, if there are covering rules with the following decisions: "&gt;= 2", "&gt;= 3",
 * then the classification result will be "3". Analogously, if there are covering rules with the following decisions: "&lt;= 1", "&lt;= 2",
 * then the classification result will be "1". If there are covering rules of both types, then first calculates value
 * resulting from the intersection of all &gt;= rules, and also value resulting from intersection of all &lt;= rules.
 * Then returns a mean value of the two values, as calculated by {@link MeanCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleRuleClassifier extends RuleClassifier implements SimpleClassifier {

	/**
	 * Mean calculator {@link MeanCalculator}.
	 */
	private MeanCalculator meanCalculator = null;
	
	/**
	 * Constructs this classifier.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from an information table
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules
	 * @throws NullPointerException if any of the parameters is {@code null} 
	 */
	public SimpleRuleClassifier(RuleSet ruleSet, SimpleClassificationResult defaultClassificationResult) {
		super(ruleSet, defaultClassificationResult);
		this.meanCalculator = new MeanCalculator();	
	}
	
	/**
	 * Gets default classification result returned by this classifier if it is unable to calculate such a result.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public SimpleClassificationResult getDefaultClassificationResult() {
		return (SimpleClassificationResult)defaultClassificationResult;
	}
	
	/**
	 * Classifies an object from an information table using rules stored in this classifier.
	 * 
	 * @param objectIndex {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public SimpleClassificationResult classify(int objectIndex, InformationTable informationTable) {
		SimpleClassificationResult result = this.getDefaultClassificationResult(); //set default result, returned if no rule covers considered object
		int decisionAttributeIndex = -1;
		
		Condition<EvaluationField> decision = null; //decision condition of the current rule
		EvaluationField upLimit = null, downLimit = null;
		
		int rulesCount = this.ruleSet.size();
		
		for (int i = 0; i < rulesCount; i++) {
			if (this.ruleSet.getRule(i).covers(objectIndex, informationTable)) {
				decision = this.ruleSet.getRule(i).getDecision();
				if (decisionAttributeIndex == -1) { //TODO: what if decision attribute index changes (for now index from the first covering rule is assigned)
					decisionAttributeIndex = decision.getAttributeWithContext().getAttributeIndex();
				}
				if (decision instanceof ConditionAtLeast<?>) {
					if (upLimit == null) {
						upLimit = decision.getLimitingEvaluation();
					}
					else {
						try {
							if (decision.getLimitingEvaluation().compareToEx(upLimit) > 0) {
								upLimit = decision.getLimitingEvaluation();
							}
						}
						catch (UncomparableException ex) {
							//TODO: should we throw an exception here?
							System.out.println("Uncomparable decision value detected during comparison: " + ex.toString());
						}
					}
				}
				else if (decision instanceof ConditionAtMost<?>) {
					if (downLimit == null) {
						downLimit = decision.getLimitingEvaluation();
					}
					else {
						try {
							if (decision.getLimitingEvaluation().compareToEx(downLimit) < 0) {
								downLimit = decision.getLimitingEvaluation();
							}
						}
						catch (UncomparableException ex) {
							System.out.println("Uncomparable decision value detected during comparison: " + ex.toString());
							//TODO: should we throw an exception here?
						}
					}
				}
			}
		}
		
		// set the result
		if (upLimit != null) {
			if (downLimit != null) {
				if (upLimit.isEqualTo(downLimit) == TernaryLogicValue.TRUE) {
					result = new SimpleClassificationResult(new SimpleDecision(upLimit, decisionAttributeIndex));
				}	
				else { //both limits set
					//remark that not necessarily downLimit <= upLimit! We can have, e.g., <=2 && >=6 or >=2 && <=6
					result = new SimpleClassificationResult(new SimpleDecision(downLimit.calculate(this.meanCalculator, upLimit), decisionAttributeIndex));
				}
			}
			else {
				result = new SimpleClassificationResult(new SimpleDecision(upLimit, decisionAttributeIndex));
			}
		}
		else if (downLimit != null) {
			result = new SimpleClassificationResult(new SimpleDecision(downLimit, decisionAttributeIndex));
		}
		
		return result;
	}
	
	/**
	 * Classifies all objects from the given information table.
	 * 
	 * @param informationTable {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public SimpleClassificationResult[] classifyAll(InformationTable informationTable) {
		SimpleClassificationResult[] classificationResults = new SimpleClassificationResult[informationTable.getNumberOfObjects()];
		for (int i = 0; i < classificationResults.length; i++) {
			classificationResults[i] = this.classify(i, informationTable);
		}
		return classificationResults;
	}

}
