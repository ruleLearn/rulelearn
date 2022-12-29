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
import org.rulelearn.core.MeanCalculator;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.core.UncomparableException;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Simple classifier using decision rules to classify each object from an information table to exactly one decision class.
 * If no rule matches an object, returns for that object a {@link #getDefaultClassificationResult() default classification result}.
 * Calculates most prudent class in the intersection of covering at least / at most rules, just like "standard" DRSA classifier.<br>
 * <br>
 * For example, if there are covering "at least" rules with the following decisions: "&gt;= 2", "&gt;= 3",
 * then the classification result will be "3".
 * Analogously, if there are covering rules with the following decisions: "&lt;= 1", "&lt;= 2",
 * then the classification result will be "1".
 * If there are covering rules of both types, then first calculates prudent value
 * resulting from the intersection of all &gt;= rules, and also prudent value resulting from intersection of all &lt;= rules.
 * If these values coincide, then returns the common value. Otherwise, resolves the conflicting assignments by
 * calculating a mean value of the two values, as calculated by {@link MeanCalculator}.
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
	 * Classifies an object from an information table using rules stored in this classifier. Can remember on the given list indices of covering rules from the rule set.
	 * 
	 * @param objectIndex index of an object from the given information table
	 * @param informationTable information table containing the object of interest 
	 * @param rememberIndicesOfCoveringRules flag indicating if indices of covering rules should be added to the given list
	 * @param indicesOfCoveringRules reference to a list where indices of covering rules should be added;
	 *        if no rule from the rule set covers given object, then no index will be added to the list;
	 *        used only if {@code rememberIndicesOfCoveringRules == true} - in such case has to be not {@code null} (otherwise a {@link NullPointerException} will be thrown);
	 *        not used if {@code rememberIndicesOfCoveringRules == false} - in such case may be {@code null};
	 *        covering rules are added to the list in the order in which they appear in the rule set
	 * 
	 * @return simple classification result for the considered object
	 * 
	 * @throws NullPointerException if given information table is {@code null}
	 * @throws NullPointerException if {@code rememberIndicesOfCoveringRules == true} and given list is {@code null}
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object (row) stored in the given information table
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering considered object cannot be compared
	 */
	SimpleClassificationResult classify(int objectIndex, InformationTable informationTable, boolean rememberIndicesOfCoveringRules, IntList indicesOfCoveringRules) {
		if (rememberIndicesOfCoveringRules) {
			Precondition.notNull(indicesOfCoveringRules, "List for remembering indices of covering rules is null.");
		}
		
		Condition<EvaluationField> decisionCondition = null; //decision condition of the current rule
		EvaluationField upLimit = null; //most cautious evaluation among limiting evaluations of decision conditions of type ConditionAtLeast
		EvaluationField downLimit = null; //most cautious evaluation among limiting evaluations of decision conditions of type ConditionAtMost
		
		int rulesCount = this.ruleSet.size();
		//take decision attribute index from the first rule -- all rules are expected to be defined for the same decision attribute
		int decisionAttributeIndex = (rulesCount > 0 ? this.ruleSet.getRule(0).getDecision().getAttributeWithContext().getAttributeIndex() : -1);
		
		for (int i = 0; i < rulesCount; i++) {
			if (this.ruleSet.getRule(i).covers(objectIndex, informationTable)) { //current rule covers considered object
				if (rememberIndicesOfCoveringRules) {
					indicesOfCoveringRules.add(i); //remember index of covering rule
				}
				decisionCondition = this.ruleSet.getRule(i).getDecision();

				if (decisionCondition instanceof ConditionAtLeast<?>) {
					if (upLimit == null) {
						upLimit = decisionCondition.getLimitingEvaluation();
					}
					else {
						if (decisionCondition.getLimitingEvaluation().compareToEx(upLimit) > 0) {
							upLimit = decisionCondition.getLimitingEvaluation();
						}
					}
				}
				else if (decisionCondition instanceof ConditionAtMost<?>) {
					if (downLimit == null) {
						downLimit = decisionCondition.getLimitingEvaluation();
					}
					else {
						if (decisionCondition.getLimitingEvaluation().compareToEx(downLimit) < 0) {
							downLimit = decisionCondition.getLimitingEvaluation();
						}
					}
				}
			}
		}
		
		//set the result
		return resolveClassificationResult(upLimit, downLimit, decisionAttributeIndex);
	}
	
	/**
	 * Computes classification result.
	 * 
	 * @param upLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtLeast}
	 * @param downLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtMost}
	 * @param decisionAttributeIndex index of decision attribute
	 * 
	 * @return computed classification result
	 */
	SimpleClassificationResult resolveClassificationResult(EvaluationField upLimit, EvaluationField downLimit, int decisionAttributeIndex) {
		SimpleClassificationResult result;

		//set the result
		if (upLimit != null) {
			if (downLimit != null) {
				if (upLimit.isEqualTo(downLimit) == TernaryLogicValue.TRUE) { //both limits are set and equal to each other
					result = new SimpleClassificationResult(new SimpleDecision(upLimit, decisionAttributeIndex));
				}	
				else { //both limits are set but different
					//remark that not necessarily downLimit <= upLimit! We can have, e.g., >=2 && <=6 or <=2 && >=6
					result = resolveConflictingClassificationResult(upLimit, downLimit, decisionAttributeIndex);
				}
			}
			else { //only upLimit is set
				result = new SimpleClassificationResult(new SimpleDecision(upLimit, decisionAttributeIndex));
			}
		}
		else if (downLimit != null) { //only downLimit is set
			result = new SimpleClassificationResult(new SimpleDecision(downLimit, decisionAttributeIndex));
		}
		else { //no limit is set
			result = this.getDefaultClassificationResult(); //set default result, returned if no rule covers considered object
		}
		
		return result;
	}
	
	/**
	 * Computes classification result in case when both limits are set but different. Returns mean value of the two limits.
	 * 
	 * @param upLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtLeast}
	 * @param downLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtMost}
	 * @param decisionAttributeIndex index of decision attribute
	 * 
	 * @return computed classification result (mean value of the two limits)
	 */
	SimpleClassificationResult resolveConflictingClassificationResult(EvaluationField upLimit, EvaluationField downLimit, int decisionAttributeIndex) {
		return new SimpleClassificationResult(new SimpleDecision(downLimit.calculate(this.meanCalculator, upLimit), decisionAttributeIndex));
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
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering considered object cannot be compared
	 */
	@Override
	public SimpleClassificationResult classify(int objectIndex, InformationTable informationTable) {
		return classify(objectIndex, informationTable, false, null);
	}
	
	/**
	 * Classifies all objects from the given information table.
	 * 
	 * @param informationTable {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering any considered object cannot be compared
	 */
	@Override
	public SimpleClassificationResult[] classifyAll(InformationTable informationTable) {
		SimpleClassificationResult[] classificationResults = new SimpleClassificationResult[informationTable.getNumberOfObjects()];
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
	 * @return simple classification result for the considered object
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering considered object cannot be compared
	 */
	public SimpleClassificationResult classify(int objectIndex, InformationTable informationTable, IntList indicesOfCoveringRules) {
		return classify(objectIndex, informationTable, true, indicesOfCoveringRules);
	}
	
//	public SimpleClassificationResult[] classifyAll(InformationTable informationTable, IntList[] arrayOfIndicesOfCoveringRules) { //size of the array should be equal to the number of objects
//		return null; //TODO
//	}

}
