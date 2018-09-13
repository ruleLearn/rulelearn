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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Unions;
import org.rulelearn.data.InformationTable;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;

/**
 * Integration tests for VCDomLEM algorithm.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
@Tag("integration")
class VCDomLemTest {

	/**
	 * Tests upward unions and certain rules.
	 */
	@Test
	public void testUpwardUnionCertain() {
		InformationTable informationTable;
		Unions unionContainer;
		Union[] unions; //upward/downward unions
		RuleType type; //certain/possible
		
		EpsilonConsistencyMeasure consistencyMeasure = new EpsilonConsistencyMeasure();
		double consistencyThreshold = 0.0;
		
		RuleEvaluator ruleEvaluator = consistencyMeasure;
		RuleConditionsEvaluator ruleConditionsEvaluator = consistencyMeasure;
		RuleConditionsEvaluator[] ruleConditionsEvaluators = {consistencyMeasure};
		ConditionAdditionEvaluator[] conditionAdditionEvaluators = {consistencyMeasure};
		ConditionRemovalEvaluator conditionRemovalEvaluator = consistencyMeasure;
		
		RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = new EvaluationAndCoverageStoppingConditionChecker(ruleConditionsEvaluator, consistencyThreshold);
		ConditionGenerator conditionGeneration = new StandardConditionGenerator(conditionAdditionEvaluators);
		RuleConditionsPruner ruleConditionsPruner = new FIFORuleConditionsPruner(ruleInductionStoppingConditionChecker);
		RuleConditionsSetPruner ruleConditionsSetPruner = new EvaluationsAndOrderRuleConditionsSetPruner(ruleConditionsEvaluators);
		RuleMinimalityChecker ruleMinimalityChecker = new SingleEvaluationRuleMinimalityChecker(ruleEvaluator);
		
		//RuleSemantics semantics;
		//conditionsSelectionMethod //mix
		//negativeExamplesTreatment
	}

}
