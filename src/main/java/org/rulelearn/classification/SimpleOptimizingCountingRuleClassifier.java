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
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithComputableCharacteristics;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Class extending {@link SimpleOptimizingRuleClassifier} by counting how many times classification result was resolved in each possible way (using only up limit, using only down limit,
 * using both limits being equal, using both limits being different, using default classification result).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleOptimizingCountingRuleClassifier extends SimpleOptimizingRuleClassifier {
	
	/**
	 * Resolution strategy used for classification with upward/downward decision rules.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static enum ResolutionStrategy {
		UP_LIMIT,
		DOWN_LIMIT,
		EQUAL_LIMIT,
		MODE,
		DEFAULT;
	}
	
	/**
	 * Resolution strategy used for the most recently classified object; {@code null} if classification has not been performed yet.
	 */
	ResolutionStrategy latestResolutionStrategy = null;
	
	/**
	 * Down limit resulting from covering rules with decision "<=" for the most recently classified object for which {@link ResolutionStrategy#MODE} resolution strategy has been employed;
	 * {@code null} if classification with {@link ResolutionStrategy#MODE} resolution strategy has not been performed yet.
	 * Down limit is the most cautious class in the intersection of covering rules with decision "<=".
	 */
	EvaluationField latestModeDownLimit = null;
	/**
	 * Up limit resulting from covering rules with decision ">=" for the most recently classified object for which {@link ResolutionStrategy#MODE} resolution strategy has been employed;
	 * {@code null} if classification with {@link ResolutionStrategy#MODE} resolution strategy has not been performed yet.
	 * Up limit is the most cautious class in the intersection of covering rules with decision ">=".
	 */
	EvaluationField latestModeUpLimit = null;
	
	long resolvedToUpLimitCount = 0;
	long resolvedToDownLimitCount = 0;
	long resolvedToEqualLimitCount = 0;
	long resolvedToModeCount = 0;
	long resolvedToDefaultCount = 0;
	
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
	public SimpleOptimizingCountingRuleClassifier(RuleSetWithComputableCharacteristics ruleSet, SimpleClassificationResult defaultClassificationResult) {
		super(ruleSet, defaultClassificationResult);
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
	public SimpleOptimizingCountingRuleClassifier(RuleSet ruleSet, SimpleClassificationResult defaultClassificationResult, InformationTable learningInformationTable) {
		super(ruleSet, defaultClassificationResult, learningInformationTable);
	}
	
	/**
	 * Computes classification result. Updates count of particular situation (only up limit was used, only down limit was used, both limits being equal were used,
	 * both limits being different were used, default classification result was used).
	 * 
	 * @param upLimit most cautious common class in the intersection of rules with decision of type {@link ConditionAtLeast}
	 * @param downLimit most cautious common class in the intersection of rules with decision of type {@link ConditionAtMost}
	 * @param decisionAttributeIndex index of decision attribute
	 * @param indicesOfCoveringAtLeastRules indices of at least rules (with decision of type {@link ConditionAtLeast}) from the rule set that cover classified object
	 * @param indicesOfCoveringAtMostRules indices of at most rules (with decision of type {@link ConditionAtMost}) from the rule set that cover classified object
	 * 
	 * @return computed classification result
	 */
	@Override
	SimpleClassificationResult resolveClassificationResult(EvaluationField upLimit, EvaluationField downLimit, int decisionAttributeIndex,
			IntList indicesOfCoveringAtLeastRules, IntList indicesOfCoveringAtMostRules) {
		SimpleClassificationResult result;
		
		//latestModeDownLimit = null; //redundant
		//latestModeUpLimit = null; //redundant
		
		//set the result
		if (upLimit != null) {
			if (downLimit != null) {
				if (upLimit.isEqualTo(downLimit) == TernaryLogicValue.TRUE) { //both limits are set and equal to each other
					result = new SimpleClassificationResult(new SimpleDecision(upLimit, decisionAttributeIndex));
					resolvedToEqualLimitCount++;
					latestResolutionStrategy = ResolutionStrategy.EQUAL_LIMIT;
				}	
				else { //both limits are set but different
					//remark that not necessarily downLimit <= upLimit! We can have, e.g., >=2 && <=6 or <=2 && >=6 
					result = new SimpleClassificationResult(new SimpleDecision(calculateModalDecisionEvaluation(
							downLimit, upLimit, indicesOfCoveringAtMostRules, indicesOfCoveringAtLeastRules),
							decisionAttributeIndex));
					resolvedToModeCount++;
					latestResolutionStrategy = ResolutionStrategy.MODE;
					latestModeDownLimit = downLimit;
					latestModeUpLimit = upLimit;
				}
			}
			else { //only upLimit is set
				result = new SimpleClassificationResult(new SimpleDecision(upLimit, decisionAttributeIndex));
				resolvedToUpLimitCount++;
				latestResolutionStrategy = ResolutionStrategy.UP_LIMIT;
			}
		}
		else if (downLimit != null) { //only downLimit is set
			result = new SimpleClassificationResult(new SimpleDecision(downLimit, decisionAttributeIndex));
			resolvedToDownLimitCount++;
			latestResolutionStrategy = ResolutionStrategy.DOWN_LIMIT;
		}
		else { //no limit is set
			result = this.getDefaultClassificationResult(); //set default result, returned if no rule covers considered object;
			resolvedToDefaultCount++;
			latestResolutionStrategy = ResolutionStrategy.DEFAULT;
		}

		return result;
	}

	/**
	 * Gets count of situations when classification result was set to up limit (and there was no down limit).
	 * 
	 * @return count of situations when classification result was set to up limit (and there was no down limit)
	 */
	public long getResolvedToUpLimitCount() {
		return resolvedToUpLimitCount;
	}

	/**
	 * Gets count of situations when classification result was set to down limit (and there was no up limit).
	 * 
	 * @return count of situations when classification result was set to down limit (and there was no up limit)
	 */
	public long getResolvedToDownLimitCount() {
		return resolvedToDownLimitCount;
	}

	/**
	 * Gets count of situations when classification result was set to up limit = down limit.
	 * 
	 * @return count of situations when classification result was set to up limit = down limit
	 */
	public long getResolvedToEqualLimitCount() {
		return resolvedToEqualLimitCount;
	}

	/**
	 * Gets count of situations when classification result was set to the modal value of up limit and down limit.
	 * 
	 * @return count of situations when classification result was set to the modal value of up limit and down limit
	 */
	public long getResolvedToModeCount() {
		return resolvedToModeCount;
	}

	/**
	 * Gets count of situations when classification result was set to default classification result (as no rule covered classified object).
	 * 
	 * @return count of situations when classification result was set to default classification result (as no rule covered classified object)
	 */
	public long getResolvedToDefaultCount() {
		return resolvedToDefaultCount;
	}

	/**
	 * Gets resolution strategy used for the most recently classified object.
	 *  
	 * @return resolution strategy used for the most recently classified object
	 */
	public ResolutionStrategy getLatestResolutionStrategy() {
		return latestResolutionStrategy;
	}

	/**
	 * Gets down limit resulting from covering rules with decision "<=" for the most recently classified object for which {@link ResolutionStrategy#MODE} resolution strategy has been employed;
	 * {@code null} if classification with {@link ResolutionStrategy#MODE} resolution strategy has not been performed yet.
	 * Down limit is the most cautious class in the intersection of covering rules with decision "<=".
	 * 
	 * @return down limit resulting from covering rules with decision "<=" for the most recently classified object for which {@link ResolutionStrategy#MODE} resolution strategy has been employed
	 */
	public EvaluationField getLatestModeDownLimit() {
		return latestModeDownLimit;
	}

	/**
	 * Gets up limit resulting from covering rules with decision ">=" for the most recently classified object for which {@link ResolutionStrategy#MODE} resolution strategy has been employed;
	 * {@code null} if classification with {@link ResolutionStrategy#MODE} resolution strategy has not been performed yet.
	 * Up limit is the most cautious class in the intersection of covering rules with decision ">=".
	 * 
	 * @return up limit resulting from covering rules with decision ">=" for the most recently classified object for which {@link ResolutionStrategy#MODE} resolution strategy has been employed
	 */
	public EvaluationField getLatestModeUpLimit() {
		return latestModeUpLimit;
	}
	
}
