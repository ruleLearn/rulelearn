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

package org.rulelearn.rules;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Contract of a condition generator used to find next best condition {@link Condition} to be added to the LHS of a constructed decision rule.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ConditionGenerator {

	/**
	 * Gets best condition that can be added to given rule conditions, constructed on the basis of evaluations of objects whose indices are on the given list.
	 * Takes into account active condition attributes from the learning information table for which given rule conditions are defined,
	 * as returned by {@link RuleConditions#getLearningInformationTable()}.<br>
	 * <br>
	 * Remark that, in general, the best condition is not guaranteed to improve evaluation of rule conditions (it can even deteriorate it).
	 * 
	 * @param consideredObjects list of indices of (positive) objects which are considered when generating candidate elementary conditions;
	 *        each of these objects should be covered by given rule conditions
	 * @param ruleConditions rule conditions for which best next condition is searched for
	 * @return best condition that can be added to given rule conditions
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws ElementaryConditionNotFoundException when it is impossible to find any new condition that could be added to given rule conditions
	 */
	public Condition<EvaluationField> getBestCondition(IntList consideredObjects, RuleConditions ruleConditions);
	
	/**
	 * Utility method for constructing condition given rule type, rule semantics, evaluation attribute, limiting evaluation, and global index of the attribute.
	 * Handles only certain and possible rule types.
	 * 
	 * @param ruleType type of rule for which new condition should be created
	 * @param ruleSemantics semantics of rule for which new condition should be created
	 * @param evaluationAttribute evaluation attribute for which new condition should be created
	 * @param limitingEvaluation limiting evaluation of the new condition
	 * @param globalAttributeIndex global index of the attribute
	 * 
	 * @return condition constructed for given parameters
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if {@code ruleType} is neither {@link RuleType#CERTAIN} nor {@link RuleType#POSSIBLE}
	 * @throws InvalidValueException if {@code ruleSemantics} is neither {@link RuleSemantics#AT_LEAST} nor {@link RuleSemantics#AT_MOST} nor {@link RuleSemantics#EQUAL}
	 * @throws InvalidValueException if {@link EvaluationAttribute#getPreferenceType() preference type} of the given attribute is neither
	 *         {@link AttributePreferenceType#GAIN} nor {@link AttributePreferenceType#COST} nor {@link AttributePreferenceType#NONE}
	 */
	static public Condition<EvaluationField> constructCondition(RuleType ruleType, RuleSemantics ruleSemantics, EvaluationAttribute evaluationAttribute, 
			EvaluationField limitingEvaluation, int globalAttributeIndex) {
		switch (ruleType) {
		case CERTAIN:
			return constructCertainRuleCondition(ruleSemantics, evaluationAttribute, limitingEvaluation, globalAttributeIndex);
		case POSSIBLE:
			return constructPossibleRuleCondition(ruleSemantics, evaluationAttribute, limitingEvaluation, globalAttributeIndex);
		default:
			throw new InvalidValueException("Cannot construct condition if rule type is neither certain nor possible.");
		}
	}
	
	
	/**
	 * Utility method for constructing certain rule's condition given rule semantics, evaluation attribute, limiting evaluation, and global index of the attribute.
	 * 
	 * @param ruleSemantics semantics of rule for which new condition should be created
	 * @param evaluationAttribute evaluation attribute for which new condition should be created
	 * @param limitingEvaluation limiting evaluation of the new condition
	 * @param globalAttributeIndex global index of the attribute
	 * 
	 * @return condition constructed for given parameters
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if given {@code ruleSemantics} is neither {@link RuleSemantics#AT_LEAST} nor {@link RuleSemantics#AT_MOST} nor {@link RuleSemantics#EQUAL}
	 * @throws InvalidValueException if {@link EvaluationAttribute#getPreferenceType() preference type} of the given attribute is neither
	 *         {@link AttributePreferenceType#GAIN} nor {@link AttributePreferenceType#COST} nor {@link AttributePreferenceType#NONE}
	 */
	static public Condition<EvaluationField> constructCertainRuleCondition(RuleSemantics ruleSemantics, EvaluationAttribute evaluationAttribute, EvaluationField limitingEvaluation, int globalAttributeIndex) {
		switch (ruleSemantics) {
		case AT_LEAST:
			switch (evaluationAttribute.getPreferenceType()) {
			case GAIN:
				return new ConditionAtLeastThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case COST:
				return new ConditionAtMostThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case NONE:
				return new ConditionEqualThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new InvalidValueException("Unhandled attribute's preference type.");
			}
		case AT_MOST:
			switch (evaluationAttribute.getPreferenceType()) {
			case GAIN:
				return new ConditionAtMostThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case COST:
				return new ConditionAtLeastThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case NONE:
				return new ConditionEqualThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new InvalidValueException("Unhandled attribute's preference type.");
			}
		case EQUAL:
			return new ConditionEqualThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
		default:
			throw new InvalidValueException("Unhandled rule semantics.");
		}
	}
	
	/**
	 * Utility method for constructing possible rule's condition given rule semantics, evaluation attribute, limiting evaluation, and global index of the attribute.
	 * 
	 * @param ruleSemantics semantics of rule for which new condition should be created
	 * @param evaluationAttribute evaluation attribute for which new condition should be created
	 * @param limitingEvaluation limiting evaluation of the new condition
	 * @param globalAttributeIndex global index of the attribute
	 * 
	 * @return condition constructed for given parameters
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if given {@code ruleSemantics} is neither {@link RuleSemantics#AT_LEAST} nor {@link RuleSemantics#AT_MOST} nor {@link RuleSemantics#EQUAL}
	 * @throws InvalidValueException if {@link EvaluationAttribute#getPreferenceType() preference type} of the given attribute is neither
	 *         {@link AttributePreferenceType#GAIN} nor {@link AttributePreferenceType#COST} nor {@link AttributePreferenceType#NONE}
	 */
	static public Condition<EvaluationField> constructPossibleRuleCondition(RuleSemantics ruleSemantics, EvaluationAttribute evaluationAttribute, EvaluationField limitingEvaluation, int globalAttributeIndex) {
		switch (ruleSemantics) {
		case AT_LEAST:
			switch (evaluationAttribute.getPreferenceType()) {
			case GAIN:
				return new ConditionAtLeastObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case COST:
				return new ConditionAtMostObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case NONE:
				return new ConditionEqualObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new InvalidValueException("Unhandled attribute's preference type.");
			}
		case AT_MOST:
			switch (evaluationAttribute.getPreferenceType()) {
			case GAIN:
				return new ConditionAtMostObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case COST:
				return new ConditionAtLeastObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			case NONE:
				return new ConditionEqualObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new InvalidValueException("Unhandled attribute's preference type.");
			}
		case EQUAL:
			return new ConditionEqualObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
		default:
			throw new InvalidValueException("Unhandled rule semantics.");
		}
	}
}
