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

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Unions;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
	public RuleSetWithCharacteristics testUpwardUnionCertain() {
		EpsilonConsistencyMeasure consistencyMeasure = new EpsilonConsistencyMeasure();
		double consistencyThreshold = 0.0;
		
		RuleConditionsEvaluator ruleConditionsEvaluator = consistencyMeasure;
		ConditionAdditionEvaluator[] conditionAdditionEvaluators = {consistencyMeasure};
		ConditionRemovalEvaluator[] conditionRemovalEvaluators = {consistencyMeasure};
		RuleConditionsEvaluator[] ruleConditionsEvaluators = {consistencyMeasure};
		//RuleEvaluator ruleEvaluator = consistencyMeasure; //just single evaluator, for rule minimality checker taking into account just single evaluation
		
		ConditionGenerator conditionGenerator = new StandardConditionGenerator(conditionAdditionEvaluators);
		RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = new EvaluationAndCoverageStoppingConditionChecker(ruleConditionsEvaluator, consistencyThreshold);
		
		ConditionSeparator conditionSeparator = null; //no separation required - all conditions concern limiting evaluations of type SimpleField
		
		AbstractRuleConditionsPruner ruleConditionsPruner = new FIFORuleConditionsPruner(ruleInductionStoppingConditionChecker); //enforce RuleInductionStoppingConditionChecker
		//---
		@SuppressWarnings("unused")
		AbstractRuleConditionsPruner ruleConditionsPrunerWithEvaluators = new AbstractRuleConditionsPrunerWithEvaluators(ruleInductionStoppingConditionChecker, conditionRemovalEvaluators) {
			@Override
			public RuleConditions prune(RuleConditions ruleConditions) {
				return null;
			}
		};
		//---
		RuleConditionsSetPruner ruleConditionsSetPruner = new EvaluationsAndOrderRuleConditionsSetPruner(ruleConditionsEvaluators);
		RuleMinimalityChecker ruleMinimalityChecker = new SingleEvaluationRuleMinimalityChecker(ruleConditionsEvaluator);
		
		InformationTableWithDecisionDistributions informationTable = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new Unions(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		
		RuleType ruleType = RuleType.CERTAIN; //certain/possible
		RuleSemantics ruleSemantics = RuleSemantics.AT_LEAST;
		AllowedObjectsType allowedObjectsType = AllowedObjectsType.POSITIVE_REGION;
		
		List<RuleConditionsWithApproximatedSet> minimalRuleConditionsWithApproximatedSets = new ObjectArrayList<RuleConditionsWithApproximatedSet>(); //rule conditions for approximated sets considered so far
		List<RuleConditions> approximatedSetRuleConditions; //rule conditions for current approximated set
		List<RuleConditionsWithApproximatedSet> verifiedRuleConditionsWithApproximatedSet; //minimal rule conditions for current approximated set
		RuleConditionsWithApproximatedSet ruleConditionsWithApproximatedSet;
		
		int approximatedSetsCount = approximatedSetProvider.getCount(); //supplementary variable
		ApproximatedSet approximatedSet; //supplementary variable
		
		for (int i = 0; i < approximatedSetsCount; i++) {
			approximatedSet = approximatedSetProvider.getApproximatedSet(i);
			approximatedSetRuleConditions = calculateApproximatedSetRuleConditionsList(approximatedSet, ruleType, ruleSemantics, allowedObjectsType,
					conditionGenerator, ruleInductionStoppingConditionChecker, conditionSeparator, ruleConditionsPruner, ruleConditionsSetPruner);
			
			verifiedRuleConditionsWithApproximatedSet = new ObjectArrayList<RuleConditionsWithApproximatedSet>();
			for (RuleConditions ruleConditions : approximatedSetRuleConditions) { //verify minimality of each rule conditions
				ruleConditionsWithApproximatedSet = new RuleConditionsWithApproximatedSet(ruleConditions, approximatedSet); 
				if (ruleMinimalityChecker.check(minimalRuleConditionsWithApproximatedSets, ruleConditionsWithApproximatedSet)) {
					verifiedRuleConditionsWithApproximatedSet.add(ruleConditionsWithApproximatedSet);
				}
			}
			
			minimalRuleConditionsWithApproximatedSets.addAll(verifiedRuleConditionsWithApproximatedSet);
		}
		
		Rule[] rules = new Rule[minimalRuleConditionsWithApproximatedSets.size()];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[minimalRuleConditionsWithApproximatedSets.size()];
		List<List<Condition<? extends EvaluationField>>> decisions;
		int ruleIndex = 0;
		
		for (RuleConditionsWithApproximatedSet minimalRuleConditionsWithApproximatedSet : minimalRuleConditionsWithApproximatedSets ) {
			decisions = new ObjectArrayList<>(); //TODO: optimize to create less objects
			decisions.add(minimalRuleConditionsWithApproximatedSet.getApproximatedSet().getElementaryDecisions()); //this way of constructing decisions is restricted to certain and possible rules
			rules[ruleIndex] = new Rule(ruleType, ruleSemantics, minimalRuleConditionsWithApproximatedSet.getRuleConditions(), decisions);
			ruleCoverageInformationArray[ruleIndex] = minimalRuleConditionsWithApproximatedSet.getRuleConditions().getRuleCoverageInformation();
			ruleIndex++;
		}
		
		return new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true); //TODO: second version of VCDomLEM returning just decision rules
	}
	
	private List<RuleConditions> calculateApproximatedSetRuleConditionsList(ApproximatedSet approximatedSet, RuleType ruleType, RuleSemantics ruleSemantics, AllowedObjectsType allowedObjectsType,
			ConditionGenerator conditionGenerator, RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker, ConditionSeparator conditionSeparator,
			AbstractRuleConditionsPruner ruleConditionsPruner, RuleConditionsSetPruner ruleConditionsSetPruner) {
		
		List<RuleConditions> approximatedSetRuleConditions = new ObjectArrayList<RuleConditions>(); //the result
		
		IntSortedSet indicesOfApproximationObjects = null; //set of objects that need to be covered (each object by at least one rule conditions)
		switch (ruleType) {
		case CERTAIN:
			indicesOfApproximationObjects = approximatedSet.getLowerApproximation();
			break;
		case POSSIBLE:
			indicesOfApproximationObjects = approximatedSet.getUpperApproximation();
			break;
		case APPROXIMATE:
			indicesOfApproximationObjects = approximatedSet.getBoundary();
			break;
		}
		
		IntSet indicesOfObjectsThatCanBeCovered = null; //indices of objects that are allowed to be covered
		if (ruleType == RuleType.CERTAIN) {
			switch (allowedObjectsType) {
			case POSITIVE_REGION:
				indicesOfObjectsThatCanBeCovered = approximatedSet.getPositiveRegion();
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
				break;
			case POSITIVE_AND_BOUNDARY_REGIONS:
				indicesOfObjectsThatCanBeCovered = approximatedSet.getPositiveRegion();
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getBoundaryRegion());
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
				break;
			case ANY_REGION:
				int numberOfObjects = approximatedSet.getInformationTable().getNumberOfObjects();
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(numberOfObjects);
				for (int i = 0; i < numberOfObjects; i++) {
					indicesOfObjectsThatCanBeCovered.add(i);
				}
				break;
			}
		} else { //possible/approximate rule
			indicesOfObjectsThatCanBeCovered = indicesOfApproximationObjects;
			indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
		}
		
		//TODO: test order of elements!
		IntList setB = new IntArrayList(indicesOfApproximationObjects); //lower/upper approximation objects not already covered by rule conditions induced so far (set B from algorithm description)
		RuleConditions ruleConditions;
		RuleConditionsBuilder ruleConditionsBuilder;
		IntList indicesOfConsideredObjects; //intersection of current set B and set of objects covered by rule conditions
		
		while (!setB.isEmpty()) {
			indicesOfConsideredObjects = new IntArrayList(setB);
			
			ruleConditionsBuilder = new RuleConditionsBuilder(
					indicesOfConsideredObjects, approximatedSet.getInformationTable(),
					approximatedSet.getObjects(), indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, approximatedSet.getNeutralObjects(),
					ruleType, ruleSemantics,
					conditionGenerator, ruleInductionStoppingConditionChecker, conditionSeparator);
			ruleConditions = ruleConditionsBuilder.build(); //build rule conditions
			
			ruleConditions = ruleConditionsPruner.prune(ruleConditions); //prune built rule conditions by removing redundant elementary conditions
			approximatedSetRuleConditions.add(ruleConditions);
			
			//remove objects covered by the new rule conditions
			//setB = setB \ ruleConditions.getIndicesOfCoveredObjects()
			IntSet setOfIndicesOfCoveredObjects = new IntOpenHashSet(ruleConditions.getIndicesOfCoveredObjects()); //translate list to hash set to accelerate subsequent removeAll method execution
			setB.removeAll(setOfIndicesOfCoveredObjects);
		}
	
		return ruleConditionsSetPruner.prune(approximatedSetRuleConditions, indicesOfApproximationObjects); //remove redundant rules, but keep covered all objects from lower/upper approximation
	}

}
