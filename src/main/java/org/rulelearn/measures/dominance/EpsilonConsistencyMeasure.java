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

package org.rulelearn.measures.dominance;

import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;
import org.rulelearn.dominance.DominanceConesDecisionDistributions;
import org.rulelearn.measures.ConsistencyMeasure;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAdditionEvaluator;
import org.rulelearn.rules.ConditionRemovalEvaluator;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleConditions;
import org.rulelearn.rules.RuleConditionsEvaluator;
import org.rulelearn.rules.RuleEvaluator;
import org.rulelearn.types.EvaluationField;

/**
 * Epsilon consistency measure defined with respect to union of decision classes in Błaszczyński, J., Greco, S., Słowiński, R., Szeląg, M.: 
 * Monotonic variable consistency rough set approaches. International Journal of Approximate Reasoning 50(7), 979--999 (2009).
 * 
 * The exact definition implemented here is more general and allows proper handling of missing values. It involves standard and inverted
 * dominance relations as defined in M. Szeląg, J. Błaszczyński, R. Słowiński, Rough Set Analysis of Classification Data with Missing Values. [In]:
 * L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017, Proceedings, Part I.
 * Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.   
 *	
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EpsilonConsistencyMeasure implements ConsistencyMeasure<Union>, ConditionAdditionEvaluator, ConditionRemovalEvaluator, RuleConditionsEvaluator, RuleEvaluator {

	protected final static double BEST_VALUE = 0.0;
	protected final static double WORST_VALUE = 1.0;
	
	/**
	 * Calculates value of epsilon consistency of the given object with respect to the given union of decision classes.
	 * 
	 * @param objectIndex index of an object in the information table for which the given union is defined
	 * @param union approximated union
	 * @return value of epsilon consistency measure for the given object with respect to the given union
	 */
	@Override
	public double calculateConsistency(int objectIndex, Union union) {
		DominanceConesDecisionDistributions dominanceCDD = union.getInformationTable().getDominanceConesDecisionDistributions();
		int negativeCount = 0;
		
		if (union.getUnionType() == UnionType.AT_LEAST) {
			for (Decision decision : dominanceCDD.getPositiveInvDConeDecisionClassDistribution(objectIndex).getDecisions()) {
				// check how many objects in a positive inverted dominance cone based on the object are not concordant with the union (i.e., not in the union and not uncomparable) 
				if (union.isDecisionNegative(decision)) {
					negativeCount += dominanceCDD.getPositiveInvDConeDecisionClassDistribution(objectIndex).getCount(decision);
				}
			}
		
		}
		else if (union.getUnionType() == UnionType.AT_MOST) {
			for (Decision decision : dominanceCDD.getNegativeDConeDecisionClassDistribution(objectIndex).getDecisions()) {
				// check how many objects in a negative dominance cone based on the object are not concordant with the union (i.e., not in the union and not uncomparable) 
				if (union.isDecisionNegative(decision)) {
					negativeCount += dominanceCDD.getNegativeDConeDecisionClassDistribution(objectIndex).getCount(decision);
				}
			}
		} 
		
		return (((double)negativeCount) / union.getComplementarySetSize());
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc} 
	 */
	@Override 
	public MeasureType getType() {
		return MeasureType.COST;
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param condition {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public double evaluateWithCondition(RuleConditions ruleConditions, Condition<EvaluationField> condition) {
		// TODO: implement
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc} 
	 */
	@Override
	public double evaluate(RuleConditions ruleConditions) {
		// TODO: implement
		return 0;
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @param conditionIndex {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public double evaluateWithoutCondition(RuleConditions ruleConditions, int conditionIndex) {
		// TODO: implement
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param rule {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public double evaluate(Rule rule, InformationTable informationTable) {
		// TODO: implement
		return 0;
	}

}
