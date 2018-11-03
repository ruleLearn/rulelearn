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

import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.Table;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.SimpleField;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Condition generator taking advantage of the assumption that for any condition attribute having {@link SimpleField} evaluations (which can be completely ordered),
 * the order of elementary conditions involving that attribute, implied by each considered condition addition evaluator, is consistent with the preference order in the value set of that attribute.<br>
 * <br>
 * For example, given attribute q with integer values, the following monotonic relationships are assumed:
 * <ul>
 *   <li>the better the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the better the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at least as good as t" calculated by each cost-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (less preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each gain-type condition addition evaluator,</li>
 *   <li>the worse the attribute value t, the lower (more preferred) the evaluation of elementary condition "q(x) is at most as good as t" calculated by each cost-type condition addition evaluator,</li>
 * </ul>
 * where q(x) denotes value (evaluation) of object x with respect to attribute q.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class M4OptimizedConditionGenerator extends AbstractConditionGeneratorWithEvaluators {
	
	/**
	 * Constructor for this condition generator. Stores given evaluators for use in {@link #getBestCondition(IntList, RuleConditions)}.
	 * 
	 * @param conditionEvaluators array with condition evaluators used lexicographically
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public M4OptimizedConditionGenerator(ConditionAdditionEvaluator[] conditionEvaluators) {
		super(conditionEvaluators);
	}
	
	/**
	 * Tells if attributes already present in rule conditions can be skipped when generating next best condition.
	 * 
	 * @return {@code false}
	 */
	boolean skipUsedAttributes() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * During search for the best condition, scans all active condition attributes. For each such an attribute (for one column of considered learning information able),
	 * optimizes scanning of values of considered objects by skipping not relevant values.
	 * During scanning of values in one column, elementary conditions are lexicographically evaluated by the condition addition evaluators set in constructor.
	 * Moreover, it is assumed that evaluations of elementary conditions are monotonically dependent on the preference order of an attribute.
	 * This relation enables to skip checking of some conditions and speed up search.
	 * 
	 * @param consideredObjects {@inheritDoc}
	 * @param ruleConditions {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws ElementaryConditionNotFoundException when it is impossible to find any new condition that could be added to given rule conditions
	 */
	@Override
	public Condition<EvaluationField> getBestCondition(IntList consideredObjects, RuleConditions ruleConditions) {
		Precondition.notNull(consideredObjects, "List of objects considered in m4-optimized condition generator is null.");
		Precondition.notNull(ruleConditions, "Rule conditions considered in m4-optimized condition generator are null.");
		
		Condition<EvaluationField> bestCondition = null;
		Condition candidateCondition;
		Table<EvaluationAttribute, EvaluationField> data = ruleConditions.getLearningInformationTable().getActiveConditionAttributeFields();
		EvaluationAttribute[] activeConditionAttributes = data.getAttributes(true);
		EvaluationField objectEvaluation;
		int globalAttributeIndex;
		int consideredObjectsSize;
		
		//initialize best condition
		for (int localActiveConditionAttributeIndex = 0; localActiveConditionAttributeIndex < activeConditionAttributes.length; localActiveConditionAttributeIndex++) { //go through attributes
			globalAttributeIndex = ruleConditions.getLearningInformationTable().translateLocalActiveConditionAttributeIndex2GlobalAttributeIndex(localActiveConditionAttributeIndex);
			//current attribute should be considered
			if (!(ruleConditions.hasConditionForAttribute(globalAttributeIndex) && this.skipUsedAttributes())) {
				//TODO: implement
			}
		}

		//iteratively update best condition
		for (int localActiveConditionAttributeIndex = 0; localActiveConditionAttributeIndex < activeConditionAttributes.length; localActiveConditionAttributeIndex++) { //go through attributes
			globalAttributeIndex = ruleConditions.getLearningInformationTable().translateLocalActiveConditionAttributeIndex2GlobalAttributeIndex(localActiveConditionAttributeIndex);
			//current attribute should be considered
			if (!(ruleConditions.hasConditionForAttribute(globalAttributeIndex) && this.skipUsedAttributes())) {
				consideredObjectsSize = consideredObjects.size();
				for (int consideredObjectIndex = 0; consideredObjectIndex < consideredObjectsSize; consideredObjectIndex++) {
					try {
						objectEvaluation = data.getField(consideredObjects.getInt(consideredObjectIndex), localActiveConditionAttributeIndex);
						//TODO: skip evaluation if missing
					} catch (ClassCastException exception) {
						throw new InvalidTypeException("Evaluation of an active condition attribute is not a simple field.");
					}
					//TODO: handle PairField evaluations (decomposition!)
					//candidateCondition = constructSimpleCondition(ruleConditions.getRuleSemantics(), activeConditionAttributes[localActiveConditionAttributeIndex],
					//		objectEvaluation, globalAttributeIndex);
					//TODO: implement
				}
			}
		}
		
		return bestCondition;
	}
	
	private Condition<EvaluationField> constructCondition(RuleType ruleType, RuleSemantics ruleSemantics, EvaluationAttribute evaluationAttribute,  EvaluationField limitingEvaluation, int globalAttributeIndex) {
		
		switch (ruleType) {
		case CERTAIN:
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
					throw new NullPointerException("Attribute preference type is null.");
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
					throw new NullPointerException("Attribute preference type is null.");
				}
			case EQUAL:
				return new ConditionEqualThresholdVSObject<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new NullPointerException("Rule semantics is null.");
			}
		case POSSIBLE:
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
					throw new NullPointerException("Attribute preference type is null.");
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
					throw new NullPointerException("Attribute preference type is null.");
				}
			case EQUAL:
				return new ConditionEqualObjectVSThreshold<EvaluationField>(new EvaluationAttributeWithContext(evaluationAttribute, globalAttributeIndex), limitingEvaluation);
			default:
				throw new NullPointerException("Rule semantics is null.");
			}
		default:
			throw new InvalidValueException("Cannot construct condition if rule type is neither certain nor possible.");
		}
		
	}

}
