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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Unions;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTableTestConfiguration;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UnknownSimpleFieldMV2;

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
class VCDomLEMTest {

	/**
	 * Tests upward unions and certain rules.
	 */
	@Test
	@Tag("integration")
	public void testUpwardUnionCertainManually() {
		EpsilonConsistencyMeasure consistencyMeasure = EpsilonConsistencyMeasure.getInstance();
		double consistencyThreshold = 0.0;
		
		RuleConditionsEvaluator ruleConditionsEvaluator = consistencyMeasure;
		MonotonicConditionAdditionEvaluator[] conditionAdditionEvaluators = {consistencyMeasure}; //TODO: use more evaluators, as in jRS! (see page 11 of the VC-DomLEM article)
		ConditionRemovalEvaluator[] conditionRemovalEvaluators = {consistencyMeasure};
		RuleConditionsEvaluator[] ruleConditionsEvaluators = {consistencyMeasure}; //TODO: use more evaluators, as in jRS! (see page 12 of the VC-DomLEM article)
		//RuleEvaluator ruleEvaluator = consistencyMeasure; //just single evaluator, for rule minimality checker taking into account just single evaluation
		
		ConditionGenerator conditionGenerator = new M4OptimizedConditionGenerator(conditionAdditionEvaluators);
		RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = new EvaluationAndCoverageStoppingConditionChecker(ruleConditionsEvaluator, consistencyThreshold);
		
		ConditionSeparator conditionSeparator = null; //no separation required - all conditions concern limiting evaluations of type SimpleField
		
		//AbstractRuleConditionsPruner ruleConditionsPruner = new FIFORuleConditionsPruner(ruleInductionStoppingConditionChecker); //enforce RuleInductionStoppingConditionChecker
		AbstractRuleConditionsPruner ruleConditionsPruner = new AttributeOrderRuleConditionsPruner(ruleInductionStoppingConditionChecker);
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
		
		InformationTableTestConfiguration informationTableTestConfiguration = new InformationTableTestConfiguration (
				new Attribute[] {
						new IdentificationAttribute("bus", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE)),
						new EvaluationAttribute("symptom1", true, AttributeType.CONDITION,
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN),
						new EvaluationAttribute("symptom2", true, AttributeType.CONDITION,
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN),
						new EvaluationAttribute("state", true, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN)
					},
				new String[][] {
						{ "a", "40",   "17.8", "2"},
						{ "b", "35",   "30",   "2"},
						{ "c", "32.5", "39",   "2"},
						{ "d", "31",   "35",   "2"},
						{ "e", "27.5", "17.5", "2"},
						{ "f", "24",   "17.5", "2"},
						{ "g", "22.5", "20",   "2"},
						{ "h", "30.8", "19",   "1"},
						{ "i", "27",   "25",   "1"},
						{ "j", "21",   "9.5",  "1"},
						{ "k", "18",   "12.5", "1"},
						{ "l", "10.5", "25.5", "1"},
						{ "m", "9.75", "17",   "1"},
						{ "n", "17.5", "5",    "0"},
						{ "o", "11",   "2",    "0"},
						{ "p", "10",   "9",    "0"},
						{ "q", "5",    "13",   "0"}
				});
		
		//InformationTableWithDecisionDistributions informationTable = Mockito.mock(InformationTableWithDecisionDistributions.class);
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(
				informationTableTestConfiguration.getAttributes(),
				informationTableTestConfiguration.getListOfFields(),
				true); //TODO: use copy constructor from develop branch
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new Unions(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionRuleDecisionsProvider();
		
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
			approximatedSetRuleConditions = calculateApproximatedSetRuleConditionsListManually(approximatedSet, ruleType, ruleSemantics, allowedObjectsType,
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
		int ruleIndex = 0;
		
		System.out.println("Rule induced with VC-DomLEM:"); //DEL
		
		for (RuleConditionsWithApproximatedSet minimalRuleConditionsWithApproximatedSet : minimalRuleConditionsWithApproximatedSets ) {
			rules[ruleIndex] = new Rule(ruleType, ruleSemantics, minimalRuleConditionsWithApproximatedSet.getRuleConditions(),
					approximatedSetRuleDecisionsProvider.getRuleDecisions(minimalRuleConditionsWithApproximatedSet.getApproximatedSet()));
			
			ruleCoverageInformationArray[ruleIndex] = minimalRuleConditionsWithApproximatedSet.getRuleConditions().getRuleCoverageInformation();
			
			System.out.println(rules[ruleIndex]); //DEL
			ruleIndex++;
		}
		
		RuleSet ruleSet = new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true); //TODO: second version of VCDomLEM returning just decision rules
		
		assertEquals(ruleSet.size(), 3);
		
		//expected output:
		//(symptom1 >= 31.0) => (state >= 2)
		//(symptom1 >= 18.0) => (state >= 1)
		//(symptom2 >= 17.0) => (state >= 1)
	}
	
	private List<RuleConditions> calculateApproximatedSetRuleConditionsListManually(ApproximatedSet approximatedSet, RuleType ruleType, RuleSemantics ruleSemantics, AllowedObjectsType allowedObjectsType,
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
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(); //TODO: give expected
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getPositiveRegion());
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
				break;
			case POSITIVE_AND_BOUNDARY_REGIONS:
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(); //TODO: give expected
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getPositiveRegion());
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
			indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(); //TODO: give expected
			indicesOfObjectsThatCanBeCovered.addAll(indicesOfApproximationObjects);
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
