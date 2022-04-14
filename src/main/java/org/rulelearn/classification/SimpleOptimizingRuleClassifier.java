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
import org.rulelearn.core.ModeCalculator;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.core.UncomparableException;
import org.rulelearn.data.Decision;
import org.rulelearn.data.FieldDistribution;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.BasicRuleCoverageInformation;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithComputableCharacteristics;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Simple classifier using decision rules to classify each object from an information table to exactly one decision class.
 * If no rule matches an object, returns for that object a {@link #getDefaultClassificationResult() default classification result}.
 * Calculates most prudent class in the intersection of covering at least / at most rules, just like "standard" DRSA classifier.<br>
 * <br>
 * For example, if there are covering "at least" rules with the following decisions: "&gt;= 2", "&gt;= 3",
 * then the classification result will be "3".
 * Analogously, if there are covering "at most" rules with the following decisions: "&lt;= 1", "&lt;= 2",
 * then the classification result will be "1".
 * If there are covering rules of both types, then first calculates prudent value
 * resulting from the intersection of all &gt;= rules, and also prudent value resulting from intersection of all &lt;= rules.
 * If these values coincide, then returns the common value.
 * Otherwise, resolves the conflicting assignments by
 * returning one of these values, depending on the number of different objects supporting the rules and contributing to assignment to each of these classes.<br>
 * <br>
 * For example, if there are two covering "at least" rules with the following decisions: "&gt;= 2", "&gt;= 3", supported, respectively, by objects
 * {1, 2, 5, 17, 19, 20} and {3, 6, 17, 18, 19, 21}, where objects 1 and 5 belong to class "2", objects 2, 3, 6, 17 and 19 belong to class "3", and objects 18, 20, and 21 belong to class "4",
 * then decision "3" is supported by objects {2, 3, 6, 17, 19} (remark that objects 17 and 19 are counted only once!), so the a posteriori frequency of class 3
 * (i.e., frequency observed among "at least" rules covering class 3 with their decision part) is 5.
 * Analogously, if there are two covering "at most" rules with the following decisions: "&lt;= 2", "&lt;= 1", supported, respectively, by objects
 * {4, 7, 33} and {12, 7, 24}, where object 12 belongs to class "0", objects 7 and 24 belong to class "1", and objects 4 and 33 belong to class "2",
 * then decision "1" is supported by objects {7, 24} (remark that object 7 is counted only once!), so the a posteriori frequency of class 1
 * (i.e., frequency observed among "at most" rules covering class 1 with their decision part) is 2.
 * Then, the classification result returned by this classifier will be the mode, i.e., class "3", as this class has higher support (5 objects) than class 1 (2 objects).
 * Remark, that the modal value is equal to the median value of the class distribution composed of just these two classes.<br>
 * <br>
 * The approach adopted by this classifier aims at two goals:
 * <ul>
 * <li>proper reclassification of objects from consistent data set (remark, e.g., that presence of rules with the following decisions: "&gt;= 2", "&gt;= 3",
 * combined with absence of "at least" rules for higher classes, suggests that the classified object should be assigned to class "3"),</li>
 * <li>maximization of both accuracy (ACC) and mean absolute error (MAE) when this classifier is applied on test data drawn from the same probability distribution as train data.</li>
 * </ul>
 * <br>
 * One should construct this classifier with proper default classification result, returned when no rule matches classified object. If the goal is to classify
 * each object to the most probable class, then modal value in a priori distribution of decision classes should be supplied as a default classification result.
 * Contrary, if the goal is to classify each object so as to minimize mean absolute error criterion, then median value in a priori distribution of decision classes
 * should be supplied as a default classification result.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleOptimizingRuleClassifier extends SimpleRuleClassifier {
	
	/**
	 * Flag indicating if rule set has computable rule characteristics. Set in class constructor.
	 */
	boolean hasComputableRuleCharacteristics;
	
	/**
	 * Array with basic rule coverage information objects for each rule from the rule set.
	 * Used only if the rule set does not contain computable rule characteristics. Otherwise is {@code null}. Set in class constructor.
	 */
	BasicRuleCoverageInformation[] basicRuleCoverageInformations;
	
	/**
	 * Constructs this classifier using rule set with computable rule characteristics.
	 * This is a preferred constructor as it enables fast calculation of classification results.
	 * One should construct this classifier with proper default classification result, returned when no rule matches classified object.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from an information table
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules;
	 *        if the goal is to classify each object to the most probable class, then modal value in a priori distribution of decision classes
	 *        should be supplied as a default classification result;
	 *        if the goal is to classify each object so as to minimize mean absolute error criterion,
	 *        then median value in a priori distribution of decision classes should be supplied as a default classification result
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public SimpleOptimizingRuleClassifier(RuleSetWithComputableCharacteristics ruleSet, SimpleClassificationResult defaultClassificationResult) {
		super(ruleSet, defaultClassificationResult);
		this.hasComputableRuleCharacteristics = true;
		this.basicRuleCoverageInformations = null;
	}
	
	/**
	 * Constructs this classifier using a rule set and learning information table, for which rules from the given rule set have been calculated.
	 * One should construct this classifier with proper default classification result, returned when no rule matches classified object.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from an information table
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules;
	 *        if the goal is to classify each object to the most probable class, then modal value in a priori distribution of decision classes
	 *        should be supplied as a default classification result;
	 *        if the goal is to classify each object so as to minimize mean absolute error criterion,
	 *        then median value in a priori distribution of decision classes should be supplied as a default classification result
	 * @param learningInformationTable learning information table, for which rules from the given rule set have been calculated;
	 *        this table is necessary for checking which objects support rules that cover classified object
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public SimpleOptimizingRuleClassifier(RuleSet ruleSet, SimpleClassificationResult defaultClassificationResult, InformationTable learningInformationTable) {
		super(ruleSet, defaultClassificationResult);
		this.hasComputableRuleCharacteristics = false;
		
		int size = ruleSet.size();
		this.basicRuleCoverageInformations = new BasicRuleCoverageInformation[size];
		
		for (int i = 0; i < size; i++) {
			this.basicRuleCoverageInformations[i] = new BasicRuleCoverageInformation(ruleSet.getRule(i), learningInformationTable);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param objectIndex {@inheritDoc}
	 * @param informationTable {@inheritDoc} 
	 * @param rememberIndicesOfCoveringRules {@inheritDoc}
	 * @param indicesOfCoveringRules {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if given information table is {@code null}
	 * @throws NullPointerException if {@code rememberIndicesOfCoveringRules == true} and given list is {@code null}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws InvalidValueException {@inheritDoc} 
	 */
	@Override
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
		
		IntList indicesOfCoveringAtLeastRules = new IntArrayList();
		IntList indicesOfCoveringAtMostRules = new IntArrayList();
		
		for (int i = 0; i < rulesCount; i++) {
			if (this.ruleSet.getRule(i).covers(objectIndex, informationTable)) { //current rule covers considered object
				if (rememberIndicesOfCoveringRules) {
					indicesOfCoveringRules.add(i); //remember index of covering rule
				}				
				decisionCondition = this.ruleSet.getRule(i).getDecision();

				if (decisionCondition instanceof ConditionAtLeast<?>) {
					indicesOfCoveringAtLeastRules.add(i); //remember index of covering "at least" rule
					if (upLimit == null) {
						upLimit = decisionCondition.getLimitingEvaluation();
					}
					else {
						try {
							if (decisionCondition.getLimitingEvaluation().compareToEx(upLimit) > 0) {
								upLimit = decisionCondition.getLimitingEvaluation();
							}
						}
						catch (UncomparableException ex) {
							throw new InvalidValueException("Cannot compare limiting evaluations of two decisions of type at least.");
						}
					}
				}
				else if (decisionCondition instanceof ConditionAtMost<?>) {
					indicesOfCoveringAtMostRules.add(i); //remember index of covering "at most" rule
					if (downLimit == null) {
						downLimit = decisionCondition.getLimitingEvaluation();
					}
					else {
						try {
							if (decisionCondition.getLimitingEvaluation().compareToEx(downLimit) < 0) {
								downLimit = decisionCondition.getLimitingEvaluation();
							}
						}
						catch (UncomparableException ex) {
							throw new InvalidValueException("Cannot compare limiting evaluations of two decisions of type at most.");
						}
					}
				}
			}
		}

		//set the result
		return resolveClassificationResult(upLimit, downLimit, decisionAttributeIndex, indicesOfCoveringAtLeastRules, indicesOfCoveringAtMostRules);
	}
	
	/**
	 * Computes classification result.
	 * 
	 * @param upLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtLeast}
	 * @param downLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtMost}
	 * @param decisionAttributeIndex index of decision attribute
	 * @param indicesOfCoveringAtLeastRules indices of at least rules (with decision of type {@link ConditionAtLeast}) from the rule set that cover classified object
	 * @param indicesOfCoveringAtMostRules indices of at most rules (with decision of type {@link ConditionAtMost}) from the rule set that cover classified object
	 * 
	 * @return computed classification result
	 */
	SimpleClassificationResult resolveClassificationResult(EvaluationField upLimit, EvaluationField downLimit, int decisionAttributeIndex,
			IntList indicesOfCoveringAtLeastRules, IntList indicesOfCoveringAtMostRules) {
		
		SimpleClassificationResult result;
		
		//set the result
		if (upLimit != null) {
			if (downLimit != null) {
				if (upLimit.isEqualTo(downLimit) == TernaryLogicValue.TRUE) { //both limits are set and equal to each other
					result = new SimpleClassificationResult(new SimpleDecision(upLimit, decisionAttributeIndex));
				}	
				else { //both limits are set but different
					//remark that not necessarily downLimit <= upLimit! We can have, e.g., >=2 && <=6 or <=2 && >=6 
					result = new SimpleClassificationResult(new SimpleDecision(calculateModalDecisionEvaluation(
							downLimit, upLimit, indicesOfCoveringAtMostRules, indicesOfCoveringAtLeastRules),
							decisionAttributeIndex));
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
	 * Calculates {@link EvaluationField evaluation} modal value of the two given evaluations of decision attribute.
	 * Uses {{@link #modeCalculator}. For the description of counting frequencies of the given evaluations, see description of this class.
	 * 
	 * @param downLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtMost}
	 * @param upLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtLeast}
	 * @param indicesOfCoveringAtMostRules indices of at most rules (with decision of type {@link ConditionAtMost}) from the rule set that cover classified object
	 * @param indicesOfCoveringAtLeastRules indices of at least rules (with decision of type {@link ConditionAtLeast}) from the rule set that cover classified object
	 * 
	 * @return modal value of the two given evaluations of decision attribute
	 */
	EvaluationField calculateModalDecisionEvaluation(EvaluationField downLimit, EvaluationField upLimit, IntList indicesOfCoveringAtMostRules, IntList indicesOfCoveringAtLeastRules) {
		FieldDistribution fieldDistribution = new FieldDistribution();
		
		//indices of objects that: i) are covered by at least one rule covering classified object, ii) have decision with evaluation equal to limit
		fieldDistribution.increaseCount(downLimit, getIndicesOfCoveredLearningObjectsWithDecisionEvaluation(downLimit, indicesOfCoveringAtMostRules).size());
		fieldDistribution.increaseCount(upLimit, getIndicesOfCoveredLearningObjectsWithDecisionEvaluation(upLimit, indicesOfCoveringAtLeastRules).size());
		
		ModeCalculator modeCalculator = new ModeCalculator(fieldDistribution);
		return downLimit.calculate(modeCalculator, upLimit);
	}
	
	/**
	 * Browses rules from the rule set having indices on the given list and gathers indices of those learning objects covered by these rules
	 * whose simple decision has evaluation equal to given {@code decisionEvaluation}.
	 * In other words, gathers indices of covered learning objects supporting simple decision with given evaluation.
	 * 
	 * @param decisionEvaluation decision evaluation of interest, searched among simple decisions of covered learning objects
	 * @param indicesOfCoveringRules indices of "at least" or "at most" rules (from the rule set) covering classified test object
	 * @return set of indices of learning objects covered by the rules with given indices
	 *         and having simple decision with {@link SimpleDecision#getEvaluation() evaluation} equal to given {@code evaluation}
	 * @throws ClassCastException if decision of some considered learning object is not of type {@link SimpleDecision}
	 */
	IntSet getIndicesOfCoveredLearningObjectsWithDecisionEvaluation(EvaluationField decisionEvaluation, IntList indicesOfCoveringRules) {
		IntList indicesOfCoveredLearningObjects;
		Int2ObjectMap<Decision> decisionsOfCoveredLearningObjects;
		IntSet indicesOfCoveredLearningObjectsWithDecisionEvaluation = new IntOpenHashSet(); //result of this method
		
		for (int ruleIndex : indicesOfCoveringRules) {
			indicesOfCoveredLearningObjects = getBasicRuleCoverageInformation(ruleIndex).getIndicesOfCoveredObjects();
			decisionsOfCoveredLearningObjects = getBasicRuleCoverageInformation(ruleIndex).getDecisionsOfCoveredObjects();
			
			for (int coveredObjectIndex : indicesOfCoveredLearningObjects) {
				//decision of object covered by current rule is equal to downLimit 
				if (((SimpleDecision)decisionsOfCoveredLearningObjects.get(coveredObjectIndex)).getEvaluation().isEqualTo(decisionEvaluation) == TernaryLogicValue.TRUE) {
					indicesOfCoveredLearningObjectsWithDecisionEvaluation.add(coveredObjectIndex); //ensure learning object is in the set (will do nothing if already present there)
				}
			}
		}
		
		return indicesOfCoveredLearningObjectsWithDecisionEvaluation;
	}
	
	/**
	 * Gets {@link BasicRuleCoverageInformation basic rule coverage information} for the rule with given index
	 * 
	 * @param ruleIndex index of the rule from the rule set
	 * @return {@link BasicRuleCoverageInformation basic rule coverage information} for the rule with given index
	 */
	BasicRuleCoverageInformation getBasicRuleCoverageInformation(int ruleIndex) {
		if (hasComputableRuleCharacteristics) {
			return ((RuleSetWithComputableCharacteristics)ruleSet).getRuleCharacteristics(ruleIndex).getRuleCoverageInformation();
		} else {
			return basicRuleCoverageInformations[ruleIndex];
		}
	}
	
}
