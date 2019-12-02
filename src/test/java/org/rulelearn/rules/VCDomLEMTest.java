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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.approximations.UnionsWithSingleLimitingDecision;
import org.rulelearn.approximations.VCDominanceBasedRoughSetCalculator;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableTestConfiguration;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.csv.ObjectParser;
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.measures.SupportMeasure;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;
import org.rulelearn.rules.ruleml.RuleMLBuilder;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
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
	
	private Attribute[] getAttributesSymptoms() {
		AttributePreferenceType attrPrefType;
		
		return new Attribute[] {
				new IdentificationAttribute("bus", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE)),
				new EvaluationAttribute("symptom1", true, AttributeType.CONDITION,
						RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), attrPrefType),
				new EvaluationAttribute("symptom2", true, AttributeType.CONDITION,
						RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), attrPrefType),
				new EvaluationAttribute("state", true, AttributeType.DECISION,
						IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), attrPrefType)
			};
	}
	
	/**
	 * Gets information table with decision distributions for "symptoms" data set.
	 * 
	 * @return information table with decision distributions for "symptoms" data set
	 */
	private InformationTableWithDecisionDistributions getInformationTableSymptoms() {
		InformationTableTestConfiguration informationTableTestConfiguration = new InformationTableTestConfiguration (
				getAttributesSymptoms(),
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
		
		return new InformationTableWithDecisionDistributions(informationTableTestConfiguration.getInformationTable(true));
	}
	
	/**
	 * Gets information table with decision distributions for "symptoms2" data set.
	 * 
	 * @return information table with decision distributions for "symptoms2" data set
	 */
	private InformationTableWithDecisionDistributions getInformationTableSymptoms2() {
		InformationTableTestConfiguration informationTableTestConfiguration = new InformationTableTestConfiguration (
				getAttributesSymptoms(),
				new String[][] {
						{ "a", "40",   "17.8", "2"},
						{ "b", "35",   "30",   "2"},
						{ "c", "32.5", "39",   "2"},
						{ "d", "31",   "35",   "2"},
						{ "e", "27.5", "28",   "2"}, //changes w.r.t. "symptoms" data: symptom2 changed from 17.5 to 28
						{ "f", "24",   "27",   "2"}, //changes w.r.t. "symptoms" data: symptom2 changed from 17.5 to 27
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
		
		return new InformationTableWithDecisionDistributions(informationTableTestConfiguration.getInformationTable(true));
	}
	
	/**
	 * Gets information table with decision distributions for "windsor" data set.
	 * 
	 * @return information table with decision distributions for "windsor" data set
	 */
	private InformationTableWithDecisionDistributions getInformationTableWindsor(String metadataPath, String dataPath) {
		InformationTableWithDecisionDistributions informationTableWithDecisionDistributions = null;
		//try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
		try (FileReader attributesReader = new FileReader(metadataPath)) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).header(false).separator('\t').build();
				InformationTable informationTable = null;
				try (FileReader objectsReader = new FileReader(dataPath)) {
					informationTable = objectParser.parseObjects(objectsReader);
					if (informationTable != null) {
						informationTableWithDecisionDistributions = new InformationTableWithDecisionDistributions(informationTable);
					}
					else {
						fail("Unable to load CSV file with definition of windsor objects.");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of windsor attributes.");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
		
		return informationTableWithDecisionDistributions;
	}

	/**
	 * Manually tests upward unions and certain rules.
	 */
	@Test
	@Tag("integration")
	void testUpwardUnionCertainManually() {
		EpsilonConsistencyMeasure consistencyMeasure = EpsilonConsistencyMeasure.getInstance();
		double consistencyThreshold = 0.0;
		
		RuleConditionsEvaluator ruleConditionsEvaluator = consistencyMeasure;
		ConditionRemovalEvaluator conditionRemovalEvaluator = consistencyMeasure;
		MonotonicConditionAdditionEvaluator[] conditionAdditionEvaluators = {consistencyMeasure, SupportMeasure.getInstance()};  //see page 11 of the VC-DomLEM article
		ConditionRemovalEvaluator[] conditionRemovalEvaluators = {consistencyMeasure};
		RuleConditionsEvaluator[] ruleConditionsEvaluators = {SupportMeasure.getInstance(), consistencyMeasure}; //see page 12 of the VC-DomLEM article
		//RuleEvaluator ruleEvaluator = consistencyMeasure; //just single evaluator, for rule minimality checker taking into account just single evaluation
		
		ConditionGenerator conditionGenerator = new M4OptimizedConditionGenerator(conditionAdditionEvaluators);
		RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker = new EvaluationAndCoverageStoppingConditionChecker(ruleConditionsEvaluator, conditionRemovalEvaluator, consistencyThreshold);
		
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

			@Override
			public AbstractRuleConditionsPruner copyWithNewStoppingConditionChecker(
					RuleInductionStoppingConditionChecker stoppingConditionChecker) {
				return null;
			}
		};
		//---
		RuleConditionsSetPruner ruleConditionsSetPruner = new EvaluationsAndOrderRuleConditionsSetPruner(ruleConditionsEvaluators);
		RuleMinimalityChecker ruleMinimalityChecker = new SingleEvaluationRuleMinimalityChecker(ruleConditionsEvaluator);
		
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleType ruleType = RuleType.CERTAIN; //certain/possible
		RuleSemantics ruleSemantics = RuleSemantics.AT_LEAST;
		AllowedNegativeObjectsType allowedNegativeObjectsType = AllowedNegativeObjectsType.POSITIVE_REGION;
		
		List<RuleConditionsWithApproximatedSet> minimalRuleConditionsWithApproximatedSets = new ObjectArrayList<RuleConditionsWithApproximatedSet>(); //rule conditions for approximated sets considered so far
		List<RuleConditions> approximatedSetRuleConditions; //rule conditions for current approximated set
		List<RuleConditionsWithApproximatedSet> verifiedRuleConditionsWithApproximatedSet; //minimal rule conditions for current approximated set
		RuleConditionsWithApproximatedSet ruleConditionsWithApproximatedSet;
		
		int approximatedSetsCount = approximatedSetProvider.getCount(); //supplementary variable
		ApproximatedSet approximatedSet; //supplementary variable
		
		for (int i = 0; i < approximatedSetsCount; i++) {
			approximatedSet = approximatedSetProvider.getApproximatedSet(i);
			approximatedSetRuleConditions = calculateApproximatedSetRuleConditionsListManually(approximatedSet, ruleType, ruleSemantics, allowedNegativeObjectsType,
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
		
//		System.out.println("Rules induced by VC-DomLEM:"); //DEL
		
		for (RuleConditionsWithApproximatedSet minimalRuleConditionsWithApproximatedSet : minimalRuleConditionsWithApproximatedSets ) {
			rules[ruleIndex] = new Rule(ruleType, ruleSemantics, minimalRuleConditionsWithApproximatedSet.getRuleConditions(),
					approximatedSetRuleDecisionsProvider.getRuleDecisions(minimalRuleConditionsWithApproximatedSet.getApproximatedSet()));
			
			ruleCoverageInformationArray[ruleIndex] = minimalRuleConditionsWithApproximatedSet.getRuleConditions().getRuleCoverageInformation();
			
//			System.out.println("  "+rules[ruleIndex]); //DEL
			ruleIndex++;
		}
		
		RuleSet ruleSet = new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true); //TODO: second version of VCDomLEM returning just decision rules
		
		assertEquals(ruleSet.size(), 3);
		
		//expected output:
		//(symptom1 >= 31.0) => (state >= 2)
		//(symptom1 >= 18.0) => (state >= 1)
		//(symptom2 >= 17.0) => (state >= 1)
	}
	
	private List<RuleConditions> calculateApproximatedSetRuleConditionsListManually(ApproximatedSet approximatedSet, RuleType ruleType, RuleSemantics ruleSemantics, AllowedNegativeObjectsType allowedNegativeObjectsType,
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
			switch (allowedNegativeObjectsType) {
			case POSITIVE_REGION:
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(); //TODO: give expected
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getObjects()); //positive objects
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getPositiveRegion());
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
				break;
			case POSITIVE_AND_BOUNDARY_REGIONS:
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(); //TODO: give expected
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getObjects()); //positive objects
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
			default:
				throw new InvalidValueException("Type of negative objects allowed to be covered by certain rules is not set.");
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
	
	/**
	 * Tests upward unions and certain rules for "symptoms" data set.
	 */
	@Test
	@Tag("integration")
	public void testSymptomsUpwardUnionsCertain() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = (new VCDomLEM((new CertainRuleInducerComponents.Builder()).build(), approximatedSetProvider, approximatedSetRuleDecisionsProvider)).generateRules();
		
		assertEquals(ruleSet.size(), 3);
		
//		System.out.println("Certain at least rules induced with VC-DomLEM for symptoms data set:"); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			System.out.println("  "+ruleSet.getRule(i));
//		}
		
		//expected output:
		int ruleIndex;
		int condIndex;
		//(symptom1 >= 31.0) => (state >= 2)
		assertTrue(ruleSet.getRule(ruleIndex = 0).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(31.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		//(symptom1 >= 18.0) => (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 1).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(18.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		//(symptom2 >= 17.0) => (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(17.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
	}
	
	/**
	 * Tests upward unions and certain rules for "symptoms" data set using VC-DRSA.
	 */
	@Test
	@Tag("integration")
	public void testSymptomsUpwardUnionsCertainVCDRSA() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms();
		
		double consistencyThreshold = (double)1 / (double)10;
		final RuleInductionStoppingConditionChecker STOPPING_CONDITION_CHECKER =
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
				
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(STOPPING_CONDITION_CHECKER).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(STOPPING_CONDITION_CHECKER)).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()). //skip removal of redundant rules to check all the rules that have been built along the way
				build();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
				
		assertEquals(ruleSet.size(), 5);
		
//		System.out.println("Certain at least rules induced with VC-DomLEM for symptoms data set using VC-DRSA:"); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			System.out.println("  "+ruleSet.getRule(i));
//		}
		
		//expected output:
		int ruleIndex;
		int condIndex;
		//(symptom1 >= 31.0) => (state >= 2)
		assertTrue(ruleSet.getRule(ruleIndex = 0).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(31.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		//(symptom1 >= 27.5) => (state >= 2)
		assertTrue(ruleSet.getRule(ruleIndex = 1).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(27.5, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		//(symptom1 >= 22.5) & (symptom2 >= 20.0) => (state >= 2)
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(22.5, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 1] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(20.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		//(symptom1 >= 18.0) => (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 3).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(18.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		//(symptom2 >= 17.0) => (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 4).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(17.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
	}
	
	/**
	 * Tests upward unions and certain rules for "symptoms2" data set.
	 */
	@Test
	@Tag("integration")
	public void testSymptoms2UpwardUnionsCertain() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms2();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = (new VCDomLEM((new CertainRuleInducerComponents.Builder()).build(), approximatedSetProvider, approximatedSetRuleDecisionsProvider)).generateRules();
		
		assertEquals(ruleSet.size(), 4);
		
//		System.out.println("Certain at least rules induced with VC-DomLEM for symptoms2 data set:"); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			System.out.println("  "+ruleSet.getRule(i));
//		}
		
		//expected output:
		int ruleIndex;
		int condIndex;
		//(symptom2 >= 27.0) => (state >= 2)
		assertTrue(ruleSet.getRule(ruleIndex = 0).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(27.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		//(symptom1 >= 40.0) => (state >= 2)
		assertTrue(ruleSet.getRule(ruleIndex = 1).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(40.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		//(symptom1 >= 18.0) => (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(18.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		//(symptom2 >= 17.0) => (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 3).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(17.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
	}
	
	/**
	 * Tests upward unions and possible rules for "symptoms" data set.
	 */
	@Test
	@Tag("integration")
	public void testSymptomsUpwardUnionsPossible() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms();
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().build();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		assertEquals(ruleSet.size(), 3);
		
//		System.out.println("Possible at least rules induced with VC-DomLEM for symptoms data set:"); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			System.out.println("  "+ruleSet.getRule(i));
//		}
		
		//expected output:
		int ruleIndex;
		int condIndex;
		//(symptom1 >= 22.5) =>[p] (state >= 2)
		assertTrue(ruleSet.getRule(ruleIndex = 0).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastObjectVSThreshold); //ConditionAtLeastObjectVSThreshold (!)
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(22.5, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN));
		//(symptom1 >= 18.0) =>[p] (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 1).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastObjectVSThreshold);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(18.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		//(symptom2 >= 17.0) =>[p] (state >= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 0] instanceof ConditionAtLeastObjectVSThreshold);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(17.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtLeastThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
	}
	
	/**
	 * Tests downward unions and certain rules for "symptoms" data set.
	 */
	@Test
	@Tag("integration")
	public void testSymptomsDownwardUnionsCertain() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = (new VCDomLEM((new CertainRuleInducerComponents.Builder()).build(), approximatedSetProvider, approximatedSetRuleDecisionsProvider)).generateRules();
		
		assertEquals(ruleSet.size(), 3);
		
//		System.out.println("Certain at most rules induced with VC-DomLEM for symptoms data set:"); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			System.out.println("  "+ruleSet.getRule(i));
//		}
//		
		//expected output:
		int ruleIndex;
		int condIndex;
		//(symptom2 <= 9.0) => (state <= 0)
		assertTrue(ruleSet.getRule(ruleIndex = 0).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(9.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		//(symptom1 <= 5.0) => (state <= 0)
		assertTrue(ruleSet.getRule(ruleIndex = 1).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(5.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		//(symptom1 <= 21.0) => (state <= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(21.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
	}
	
	/**
	 * Tests downward unions and certain rules for "symptoms" data set using VC-DRSA.
	 */
	@Test
	@Tag("integration")
	public void testSymptomsDownwardUnionsCertainVCDRSA() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms();
		
		double consistencyThreshold = (double)2 / (double)7;
		final RuleInductionStoppingConditionChecker STOPPING_CONDITION_CHECKER =
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
				
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(STOPPING_CONDITION_CHECKER).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(STOPPING_CONDITION_CHECKER)).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()). //skip removal of redundant rules to check all the rules that have been built along the way
				build();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		assertEquals(ruleSet.size(), 5);
		
//		System.out.println("Certain at most rules induced with VC-DomLEM for symptoms data set using VC-DRSA:"); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			System.out.println("  "+ruleSet.getRule(i));
//		}
		
		//expected output:
		int ruleIndex;
		int condIndex;
		//(symptom2 <= 9.0) => (state <= 0)
		assertTrue(ruleSet.getRule(ruleIndex = 0).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(9.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		//(symptom1 <= 5.0) => (state <= 0)
		assertTrue(ruleSet.getRule(ruleIndex = 1).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(5.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		//(symptom1 <= 21.0) => (state <= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(21.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		//(symptom1 <= 27.0) => (state <= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 3).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(27.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		//(symptom1 <= 30.8) & (symptom2 <= 19.0) => (state <= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 4).getConditions(true)[condIndex = 0] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(30.8, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex = 4).getConditions(true)[condIndex = 1] instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(19.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
	}
	
	/**
	 * Tests downward unions and possible rules for "symptoms" data set.
	 */
	@Test
	@Tag("integration")
	public void testSymptomsDownwardUnionsPossible() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableSymptoms();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
				
		RuleSet ruleSet = (new VCDomLEM((new PossibleRuleInducerComponents.Builder()).build(), approximatedSetProvider, approximatedSetRuleDecisionsProvider)).generateRules();
		
		assertEquals(ruleSet.size(), 3);
		
//		System.out.println("Possible at most rules induced with VC-DomLEM for symptoms data set:"); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			System.out.println("  "+ruleSet.getRule(i));
//		}
		
		//expected output:
		int ruleIndex;
		int condIndex;
		//(symptom2 <= 9.0) =>[p] (state <= 0)
		assertTrue(ruleSet.getRule(ruleIndex = 0).getConditions(true)[condIndex = 0] instanceof ConditionAtMostObjectVSThreshold);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 2);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(9.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		//(symptom1 <= 5.0) =>[p] (state <= 0)
		assertTrue(ruleSet.getRule(ruleIndex = 1).getConditions(true)[condIndex = 0] instanceof ConditionAtMostObjectVSThreshold);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(5.0, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		//(symptom1 <= 30.8) =>[p] (state <= 1)
		assertTrue(ruleSet.getRule(ruleIndex = 2).getConditions(true)[condIndex = 0] instanceof ConditionAtMostObjectVSThreshold);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getAttributeWithContext().getAttributeIndex(), 1);
		assertEquals(ruleSet.getRule(ruleIndex).getConditions(true)[condIndex].getLimitingEvaluation(), RealFieldFactory.getInstance().create(30.8, AttributePreferenceType.GAIN));
		assertTrue(ruleSet.getRule(ruleIndex).getDecision() instanceof ConditionAtMostThresholdVSObject);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getAttributeWithContext().getAttributeIndex(), 3);
		assertEquals(ruleSet.getRule(ruleIndex).getDecision().getLimitingEvaluation(), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorUpwardUnionsCertainRulesDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				build();
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		
		String[] expectedRules = {
				"(lot_size >= 13200.0) => (sale_price >= 3)",
				"(nbath >= 4) => (sale_price >= 3)",
				"(nbed >= 6) & (lot_size >= 4300.0) => (sale_price >= 3)",
				"(lot_size >= 11440.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(lot_size >= 11175.0) & (basement >= 1) => (sale_price >= 3)",
				"(nbath >= 3) & (lot_size >= 5960.0) => (sale_price >= 3)",
				"(lot_size >= 10500.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 8500.0) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) & (ngarage >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbath >= 2) & (nbed >= 4) => (sale_price >= 3)",
				"(nbed >= 5) & (lot_size >= 6840.0) => (sale_price >= 3)",
				"(ngarage >= 3) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 9960.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 9620.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 8800.0) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 9000.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 8372.0) & (nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 8250.0) & (nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6360.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (desire_loc >= 1) & (lot_size >= 5500.0) => (sale_price >= 3)",
				"(lot_size >= 8100.0) & (air_cond >= 1) & (nbed >= 4) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 7800.0) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 7440.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 7420.0) & (nbed >= 4) & (air_cond >= 1) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7155.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 6900.0) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbed >= 4) & (basement >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (air_cond >= 1) & (lot_size >= 6600.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) & (lot_size >= 4560.0) => (sale_price >= 3)",
				"(lot_size >= 6550.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 6420.0) & (nbath >= 2) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (air_cond >= 1) & (lot_size >= 5450.0) => (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 6240.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) => (sale_price >= 2)",
				"(lot_size >= 13200.0) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (ngarage >= 1) => (sale_price >= 2)",
				"(lot_size >= 10700.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbed >= 6) & (lot_size >= 4300.0) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 10240.0) & (ngarage >= 2) => (sale_price >= 2)",
				"(lot_size >= 10269.0) & (nbed >= 3) & (ngarage >= 1) => (sale_price >= 2)",
				"(nbath >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 9166.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 8520.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 8520.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 9000.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 8372.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (lot_size >= 5400.0) => (sale_price >= 2)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 2)",
				"(ngarage >= 3) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 8150.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 8150.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 8100.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 5000.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 4800.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 3240.0) & (nbed >= 4) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7440.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 7600.0) & (nbed >= 4) => (sale_price >= 2)",
				"(lot_size >= 7686.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7410.0) & (nbed >= 4) => (sale_price >= 2)",
				"(lot_size >= 7410.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7320.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 7155.0) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 7155.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7160.0) & (rec_room >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 7160.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 7020.0) & (rec_room >= 1) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 7020.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6660.0) & (desire_loc >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (air_cond >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 5136.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6000.0) & (ngarage >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbath >= 2) & (drive >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (ngarage >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 7000.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 7000.0) & (basement >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 6862.0) & (air_cond >= 1) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 6540.0) & (nbath >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 6550.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 6550.0) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 6550.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 6420.0) & (nbath >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 6420.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6100.0) & (nbath >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 6100.0) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 6100.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6321.0) & (basement >= 1) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 6050.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (lot_size >= 4400.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5720.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) & (lot_size >= 4815.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 5400.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 5500.0) & (drive >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4995.0) & (drive >= 1) & (nbed >= 4) & (basement >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (ngarage >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (lot_size >= 4000.0) & (drive >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (nstoreys >= 2) & (ngarage >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4510.0) & (nbed >= 4) & (drive >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (nbed >= 4) & (ngarage >= 2) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (drive >= 1) & (ngarage >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 4510.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4950.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4260.0) & (basement >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 4640.0) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(nstoreys >= 4) => (sale_price >= 1)",
				"(lot_size >= 9166.0) => (sale_price >= 1)",
				"(nbed >= 6) => (sale_price >= 1)",
				"(lot_size >= 8150.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(lot_size >= 8150.0) & (nbath >= 2) => (sale_price >= 1)",
				"(lot_size >= 8250.0) & (desire_loc >= 1) => (sale_price >= 1)",
				"(lot_size >= 8250.0) & (basement >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (drive >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (lot_size >= 4800.0) => (sale_price >= 1)",
				"(nbed >= 5) & (nbath >= 2) => (sale_price >= 1)",
				"(nbed >= 5) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 3) & (lot_size >= 5400.0) => (sale_price >= 1)",
				"(nbath >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(nbath >= 3) & (nbed >= 4) => (sale_price >= 1)",
				"(lot_size >= 8080.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) => (sale_price >= 1)",
				"(nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (nbath >= 2) => (sale_price >= 1)",
				"(nstoreys >= 3) & (ngarage >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (desire_loc >= 1) & (lot_size >= 2856.0) => (sale_price >= 1)",
				"(lot_size >= 7800.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(lot_size >= 7800.0) & (desire_loc >= 1) => (sale_price >= 1)",
				"(lot_size >= 6825.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(lot_size >= 6900.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 6930.0) & (nbath >= 2) => (sale_price >= 1)",
				"(lot_size >= 6930.0) & (nbed >= 4) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 4000.0) => (sale_price >= 1)",
				"(nbath >= 2) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (ngarage >= 2) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 3640.0) & (nbed >= 3) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 2817.0) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) & (basement >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (desire_loc >= 1) & (lot_size >= 2850.0) => (sale_price >= 1)",
				"(nbath >= 2) & (rec_room >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 6800.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 6650.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 6650.0) & (rec_room >= 1) => (sale_price >= 1)",
				"(lot_size >= 6100.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(lot_size >= 6450.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 6450.0) & (nbed >= 4) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 5320.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (ngarage >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (rec_room >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) & (drive >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(rec_room >= 1) & (ngarage >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 5680.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 5800.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 5800.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(lot_size >= 6060.0) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 5885.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(lot_size >= 6000.0) & (nbed >= 4) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 4280.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 4320.0) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (air_cond >= 1) & (lot_size >= 3816.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 3960.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 5500.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4950.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (nbed >= 4) & (lot_size >= 4260.0) => (sale_price >= 1)",
				"(nbed >= 4) & (basement >= 1) & (lot_size >= 4100.0) => (sale_price >= 1)",
				"(nbed >= 4) & (lot_size >= 4640.0) & (ngarage >= 1) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "dummy", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //191
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorUpwardUnionsCertainRulesDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = (new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider)).generateRules();
		
		String[] expectedRules = {
				"(nbed >= 6) & (lot_size >= 4300.0) => (sale_price >= 3)",
				"(lot_size >= 11175.0) & (basement >= 1) => (sale_price >= 3)",
				"(nbath >= 3) & (lot_size >= 5960.0) => (sale_price >= 3)",
				"(nstoreys >= 4) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbath >= 2) & (nbed >= 4) => (sale_price >= 3)",
				"(nbed >= 5) & (lot_size >= 6840.0) => (sale_price >= 3)",
				"(lot_size >= 8800.0) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 9000.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 8250.0) & (nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (desire_loc >= 1) & (lot_size >= 5500.0) => (sale_price >= 3)",
				"(lot_size >= 6900.0) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (air_cond >= 1) & (lot_size >= 6600.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) & (lot_size >= 4560.0) => (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 6240.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (ngarage >= 1) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 10269.0) & (nbed >= 3) & (ngarage >= 1) => (sale_price >= 2)",
				"(nbath >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 2)",
				"(ngarage >= 3) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 8100.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 4800.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 3240.0) & (nbed >= 4) => (sale_price >= 2)",
				"(lot_size >= 7410.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7320.0) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (air_cond >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 5136.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbath >= 2) & (drive >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (ngarage >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 7000.0) & (basement >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 6050.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (lot_size >= 4400.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) & (lot_size >= 4815.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 5500.0) & (drive >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (ngarage >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (nstoreys >= 2) & (ngarage >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4510.0) & (nbed >= 4) & (drive >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (drive >= 1) & (ngarage >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 4510.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4950.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4260.0) & (basement >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 4640.0) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 9166.0) => (sale_price >= 1)",
				"(nbed >= 5) & (drive >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 3) & (lot_size >= 5400.0) => (sale_price >= 1)",
				"(nbath >= 3) & (nbed >= 4) => (sale_price >= 1)",
				"(nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (ngarage >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (ngarage >= 2) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 3640.0) & (nbed >= 3) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 2817.0) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) & (basement >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (desire_loc >= 1) & (lot_size >= 2850.0) => (sale_price >= 1)",
				"(nbath >= 2) & (rec_room >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 6450.0) & (basement >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (rec_room >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) & (drive >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 5800.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(lot_size >= 6060.0) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 6000.0) & (nbed >= 4) => (sale_price >= 1)",
				"(ngarage >= 2) & (air_cond >= 1) & (lot_size >= 3816.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 3960.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 5500.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (nbed >= 4) & (lot_size >= 4260.0) => (sale_price >= 1)",
				"(nbed >= 4) & (basement >= 1) & (lot_size >= 4100.0) => (sale_price >= 1)",
				"(nbed >= 4) & (lot_size >= 4640.0) & (ngarage >= 1) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "pruning", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //85
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set.
	 * Employs VC-DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorUpwardUnionsCertainRulesVCDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size >= 13200.0) => (sale_price >= 3)",
				"(nbath >= 4) => (sale_price >= 3)",
				"(nbed >= 6) & (lot_size >= 4300.0) => (sale_price >= 3)",
				"(lot_size >= 11440.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(lot_size >= 11175.0) & (basement >= 1) => (sale_price >= 3)",
				"(nbath >= 3) & (lot_size >= 5960.0) => (sale_price >= 3)",
				"(lot_size >= 10500.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 8500.0) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (ngarage >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (air_cond >= 1) => (sale_price >= 3)",
				"(nbed >= 5) & (lot_size >= 6840.0) => (sale_price >= 3)",
				"(ngarage >= 3) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 9960.0) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 9620.0) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 8800.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 8875.0) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 8875.0) & (nbed >= 3) & (ngarage >= 1) => (sale_price >= 3)",
				"(lot_size >= 8372.0) & (nstoreys >= 3) => (sale_price >= 3)",
				"(lot_size >= 8250.0) & (nstoreys >= 3) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6360.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (nbath >= 2) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (desire_loc >= 1) & (lot_size >= 5500.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 3240.0) & (nbed >= 4) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 5200.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (air_cond >= 1) & (lot_size >= 4800.0) => (sale_price >= 3)",
				"(lot_size >= 8100.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 8000.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7800.0) & (nbath >= 2) => (sale_price >= 3)",
				"(lot_size >= 7700.0) & (nbath >= 2) => (sale_price >= 3)",
				"(lot_size >= 7440.0) & (nbath >= 2) => (sale_price >= 3)",
				"(lot_size >= 7410.0) & (nbed >= 4) => (sale_price >= 3)",
				"(lot_size >= 7410.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7320.0) & (nbath >= 2) => (sale_price >= 3)",
				"(lot_size >= 7231.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7155.0) & (nbath >= 2) => (sale_price >= 3)",
				"(lot_size >= 7160.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 7020.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 7000.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 6900.0) & (nbath >= 2) => (sale_price >= 3)",
				"(lot_size >= 6800.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 6750.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 6615.0) & (nbath >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbed >= 4) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbath >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (air_cond >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) & (lot_size >= 4560.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 6400.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbath >= 2) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 4800.0) & (basement >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 6550.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 6550.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 6420.0) & (nbath >= 2) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(lot_size >= 6420.0) & (air_cond >= 1) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (air_cond >= 1) & (lot_size >= 5450.0) => (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 6240.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (ngarage >= 2) & (nbed >= 4) & (lot_size >= 3500.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 4260.0) & (drive >= 1) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (ngarage >= 1) & (lot_size >= 4300.0) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 3)",
				"(nbed >= 4) & (lot_size >= 6000.0) & (ngarage >= 2) => (sale_price >= 3)",
				"(desire_loc >= 1) & (lot_size >= 6000.0) & (nstoreys >= 2) & (ngarage >= 1) => (sale_price >= 3)",
				"(ngarage >= 2) & (basement >= 1) & (nstoreys >= 2) & (lot_size >= 4320.0) => (sale_price >= 3)",
				"(ngarage >= 2) & (basement >= 1) & (lot_size >= 3960.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 3)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3760.0) => (sale_price >= 3)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (nstoreys >= 2) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 3)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4040.0) & (drive >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) => (sale_price >= 2)",
				"(lot_size >= 13200.0) => (sale_price >= 2)",
				"(lot_size >= 10500.0) => (sale_price >= 2)",
				"(nbed >= 6) & (lot_size >= 4300.0) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 9860.0) & (ngarage >= 2) => (sale_price >= 2)",
				"(lot_size >= 9860.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nbath >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 9166.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 8520.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 8520.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 8875.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 8875.0) & (ngarage >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 8372.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (lot_size >= 5400.0) => (sale_price >= 2)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 2)",
				"(ngarage >= 3) => (sale_price >= 2)",
				"(lot_size >= 8150.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 8150.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 8250.0) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 8100.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 5000.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 4800.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 4160.0) & (drive >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 3240.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 3510.0) & (drive >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 2970.0) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 7980.0) & (desire_loc >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 7980.0) & (ngarage >= 2) => (sale_price >= 2)",
				"(lot_size >= 7440.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 7600.0) & (nbed >= 4) => (sale_price >= 2)",
				"(lot_size >= 7686.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7410.0) & (nbed >= 4) => (sale_price >= 2)",
				"(lot_size >= 7410.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7320.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 7155.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 7160.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7160.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7020.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 7020.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6660.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 5136.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 4095.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 2870.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6450.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6000.0) & (ngarage >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbath >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (ngarage >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 4800.0) & (nbed >= 3) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 4800.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 4770.0) & (nbed >= 3) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 3750.0) & (drive >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 3540.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7000.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6862.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 6720.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 6540.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 6550.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6420.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 6420.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6100.0) & (nbath >= 2) => (sale_price >= 2)",
				"(lot_size >= 6100.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6300.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6300.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 6040.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6040.0) & (desire_loc >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (lot_size >= 4400.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5360.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5360.0) & (nbed >= 3) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5400.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) & (lot_size >= 4815.0) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 4880.0) & (ngarage >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 4320.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3840.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 2880.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 5400.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 5500.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4995.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (ngarage >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (lot_size >= 4000.0) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4510.0) & (nbed >= 4) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (ngarage >= 2) & (nbed >= 4) => (sale_price >= 2)",
				"(nbath >= 2) & (ngarage >= 2) & (basement >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (ngarage >= 2) & (lot_size >= 3650.0) => (sale_price >= 2)",
				"(nbath >= 2) & (ngarage >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4260.0) & (nbed >= 4) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4300.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 3420.0) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (nbed >= 4) & (ngarage >= 2) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 5948.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 5885.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 4510.0) => (sale_price >= 2)",
				"(ngarage >= 2) & (basement >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (basement >= 1) & (lot_size >= 3960.0) => (sale_price >= 2)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3760.0) => (sale_price >= 2)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3630.0) => (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 3760.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 5400.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4950.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4260.0) => (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 4640.0) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 4520.0) & (basement >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (ngarage >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(air_cond >= 1) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 3640.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3410.0) => (sale_price >= 2)",
				"(lot_size >= 5010.0) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 4920.0) & (nstoreys >= 2) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 4900.0) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4120.0) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4350.0) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4040.0) & (ngarage >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 3450.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 3745.0) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 3000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (drive >= 1) & (lot_size >= 2550.0) & (nbed >= 3) => (sale_price >= 2)",
				"(ngarage >= 1) & (lot_size >= 4200.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4040.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(nstoreys >= 4) => (sale_price >= 1)",
				"(lot_size >= 9166.0) => (sale_price >= 1)",
				"(nbed >= 6) => (sale_price >= 1)",
				"(lot_size >= 8150.0) => (sale_price >= 1)",
				"(nbed >= 5) & (drive >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (lot_size >= 4800.0) => (sale_price >= 1)",
				"(nbed >= 5) & (nbath >= 2) => (sale_price >= 1)",
				"(nbed >= 5) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 3) => (sale_price >= 1)",
				"(nbath >= 3) => (sale_price >= 1)",
				"(lot_size >= 8080.0) => (sale_price >= 1)",
				"(nstoreys >= 3) => (sale_price >= 1)",
				"(lot_size >= 7770.0) => (sale_price >= 1)",
				"(lot_size >= 6825.0) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 4000.0) => (sale_price >= 1)",
				"(nbath >= 2) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (ngarage >= 2) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 3640.0) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(nbath >= 2) & (desire_loc >= 1) & (lot_size >= 2850.0) => (sale_price >= 1)",
				"(nbath >= 2) & (rec_room >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 3) & (lot_size >= 2135.0) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 3150.0) => (sale_price >= 1)",
				"(lot_size >= 6800.0) => (sale_price >= 1)",
				"(lot_size >= 6650.0) => (sale_price >= 1)",
				"(lot_size >= 6100.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 5320.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (ngarage >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (rec_room >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (basement >= 1) & (lot_size >= 2015.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(rec_room >= 1) & (ngarage >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 4800.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (air_cond >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 4770.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 3750.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 3540.0) => (sale_price >= 1)",
				"(lot_size >= 6020.0) => (sale_price >= 1)",
				"(lot_size >= 5885.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 4280.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (air_cond >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 3760.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 3630.0) => (sale_price >= 1)",
				"(lot_size >= 5880.0) => (sale_price >= 1)",
				"(lot_size >= 5500.0) => (sale_price >= 1)",
				"(lot_size >= 5450.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5000.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4950.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4520.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (ngarage >= 1) & (lot_size >= 3162.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (basement >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4240.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3630.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3480.0) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 5400.0) => (sale_price >= 1)",
				"(nbed >= 4) & (basement >= 1) => (sale_price >= 1)",
				"(nbed >= 4) & (lot_size >= 4360.0) => (sale_price >= 1)",
				"(lot_size >= 5010.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 4900.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 4900.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 4900.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(lot_size >= 4820.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 4820.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 4032.0) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3745.0) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3630.0) => (sale_price >= 1)",
				"(basement >= 1) & (ngarage >= 1) & (nbed >= 3) & (lot_size >= 3450.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3570.0) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3120.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 3000.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 2550.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 4410.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 4400.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 4340.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 4200.0) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 4040.0) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 4000.0) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 3640.0) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 3600.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 3990.0) & (nstoreys >= 2) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 3960.0) & (nstoreys >= 2) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 3900.0) & (nstoreys >= 2) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "dummy", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //301
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set.
	 * Employs VC-DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorUpwardUnionsCertainRulesVCDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(nstoreys >= 4) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 8875.0) & (nbed >= 3) & (ngarage >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 5500.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 3240.0) & (nbed >= 4) => (sale_price >= 3)",
				"(nstoreys >= 3) & (air_cond >= 1) & (lot_size >= 4800.0) => (sale_price >= 3)",
				"(lot_size >= 7160.0) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 6750.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 6615.0) & (nbath >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (air_cond >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 4800.0) & (basement >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 6550.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 6420.0) & (air_cond >= 1) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) => (sale_price >= 3)",
				"(nbath >= 2) & (ngarage >= 2) & (nbed >= 4) & (lot_size >= 3500.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 4260.0) & (drive >= 1) & (nstoreys >= 2) => (sale_price >= 3)",
				"(ngarage >= 2) & (basement >= 1) & (lot_size >= 3960.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 3)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3760.0) => (sale_price >= 3)",
				"(lot_size >= 6000.0) & (nstoreys >= 2) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 3)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 3)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4040.0) & (drive >= 1) => (sale_price >= 3)",
				"(lot_size >= 10500.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 2970.0) & (drive >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 2870.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 3540.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6720.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) & (lot_size >= 4815.0) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 2880.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (ngarage >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4260.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 3420.0) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 5885.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3630.0) => (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 3760.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 5400.0) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (ngarage >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3410.0) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4040.0) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (drive >= 1) & (lot_size >= 2550.0) & (nbed >= 3) => (sale_price >= 2)",
				"(ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(nstoreys >= 3) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 2135.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (basement >= 1) & (lot_size >= 2015.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 3540.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3630.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3480.0) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 5400.0) => (sale_price >= 1)",
				"(nbed >= 4) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 4900.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3570.0) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3120.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 2550.0) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 4400.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 4340.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 3600.0) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 3900.0) & (nstoreys >= 2) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "pruning", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //67
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and possible rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorUpwardUnionsPossibleRulesDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();

		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size >= 13200.0) =>[p] (sale_price >= 3)",
				"(nbath >= 4) =>[p] (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 5020.0) =>[p] (sale_price >= 3)",
				"(nstoreys >= 4) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 11440.0) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(nbed >= 6) & (lot_size >= 4300.0) =>[p] (sale_price >= 3)",
				"(lot_size >= 10700.0) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 10700.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 10500.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 10500.0) & (nbed >= 4) =>[p] (sale_price >= 3)",
				"(nbath >= 3) & (lot_size >= 4410.0) =>[p] (sale_price >= 3)",
				"(lot_size >= 9960.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 10240.0) & (ngarage >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 10269.0) & (nbed >= 3) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(ngarage >= 3) & (lot_size >= 7200.0) =>[p] (sale_price >= 3)",
				"(lot_size >= 8520.0) & (ngarage >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 8875.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 8875.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 8875.0) & (ngarage >= 1) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(nbed >= 5) & (lot_size >= 5300.0) =>[p] (sale_price >= 3)",
				"(lot_size >= 8372.0) & (ngarage >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 8150.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 8150.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 8080.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 8000.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 7800.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 7800.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 7440.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 7482.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 7686.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 7410.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 7020.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 7085.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (lot_size >= 4100.0) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 5200.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (air_cond >= 1) & (lot_size >= 4800.0) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 3240.0) & (nbed >= 4) =>[p] (sale_price >= 3)",
				"(lot_size >= 7320.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 7000.0) & (basement >= 1) & (ngarage >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 6825.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6900.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 6900.0) & (rec_room >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6800.0) & (rec_room >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6750.0) & (rec_room >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6660.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 6615.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 5960.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 6000.0) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 5136.0) & (desire_loc >= 1) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 5680.0) & (nbed >= 3) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 4785.0) & (nbed >= 3) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 4560.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(lot_size >= 6550.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6420.0) & (basement >= 1) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6420.0) & (nbath >= 2) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6240.0) & (nbath >= 2) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6300.0) & (basement >= 1) & (ngarage >= 1) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(lot_size >= 6300.0) & (air_cond >= 1) & (ngarage >= 2) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(lot_size >= 6100.0) & (nbath >= 2) & (ngarage >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (desire_loc >= 1) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (desire_loc >= 1) & (lot_size >= 5360.0) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (desire_loc >= 1) & (nstoreys >= 2) & (lot_size >= 4320.0) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (lot_size >= 6000.0) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 4510.0) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 4500.0) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nbed >= 4) & (nbath >= 2) & (lot_size >= 3500.0) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nbath >= 2) & (lot_size >= 4992.0) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nbath >= 2) & (lot_size >= 3880.0) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (basement >= 1) & (lot_size >= 3960.0) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (basement >= 1) & (lot_size >= 3960.0) & (drive >= 1) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3760.0) =>[p] (sale_price >= 3)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (nstoreys >= 2) & (drive >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (nbed >= 3) & (drive >= 1) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6000.0) & (desire_loc >= 1) & (nstoreys >= 2) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 5948.0) & (air_cond >= 1) & (nstoreys >= 2) & (drive >= 1) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 5450.0) & (air_cond >= 1) & (drive >= 1) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (desire_loc >= 1) & (air_cond >= 1) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 4510.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 4) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (nstoreys >= 2) & (drive >= 1) & (lot_size >= 4000.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 4260.0) & (ngarage >= 1) & (basement >= 1) & (drive >= 1) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 4260.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 4) =>[p] (sale_price >= 3)",
				"(lot_size >= 5850.0) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 5500.0) & (air_cond >= 1) & (nbed >= 3) & (drive >= 1) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(basement >= 1) & (ngarage >= 1) & (lot_size >= 4040.0) & (nstoreys >= 2) & (drive >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 4) =>[p] (sale_price >= 2)",
				"(lot_size >= 10500.0) =>[p] (sale_price >= 2)",
				"(ngarage >= 3) =>[p] (sale_price >= 2)",
				"(nbed >= 6) =>[p] (sale_price >= 2)",
				"(lot_size >= 8520.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 8880.0) & (ngarage >= 2) =>[p] (sale_price >= 2)",
				"(lot_size >= 8880.0) & (air_cond >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 8150.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(nbath >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 8080.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(nbed >= 5) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(nbed >= 5) & (lot_size >= 4800.0) =>[p] (sale_price >= 2)",
				"(lot_size >= 7800.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 2970.0) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 3700.0) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 3540.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 3150.0) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (nbath >= 2) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 2870.0) =>[p] (sale_price >= 2)",
				"(lot_size >= 7600.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 6862.0) & (ngarage >= 2) =>[p] (sale_price >= 2)",
				"(lot_size >= 6930.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(lot_size >= 6420.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(lot_size >= 6420.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 6720.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 6040.0) & (air_cond >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 6040.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 6040.0) & (ngarage >= 2) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 6000.0) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (desire_loc >= 1) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 4510.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 4600.0) & (air_cond >= 1) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (nbath >= 2) & (lot_size >= 3420.0) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (nbath >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 4) & (air_cond >= 1) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 4320.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3630.0) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 3) & (lot_size >= 3760.0) =>[p] (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 4260.0) =>[p] (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 3792.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 3180.0) =>[p] (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 3640.0) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 3420.0) & (nbath >= 2) =>[p] (sale_price >= 2)",
				"(lot_size >= 5948.0) & (air_cond >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 5985.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 6000.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 5885.0) & (air_cond >= 1) =>[p] (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 3640.0) =>[p] (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(lot_size >= 5850.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5720.0) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5500.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 4520.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 4815.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 4815.0) & (drive >= 1) & (desire_loc >= 1) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (desire_loc >= 1) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 4500.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3162.0) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 2953.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 5495.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5400.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 4320.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 2880.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (basement >= 1) & (lot_size >= 2610.0) & (drive >= 1) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(lot_size >= 5010.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 4900.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 4900.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 4820.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4120.0) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4040.0) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 3970.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 3745.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 3570.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 3210.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 3150.0) & (nstoreys >= 2) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 3000.0) & (drive >= 1) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 2550.0) & (drive >= 1) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 4350.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 4200.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 4040.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4000.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 3990.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 3968.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(lot_size >= 3960.0) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(nstoreys >= 2) & (lot_size >= 3750.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(nstoreys >= 2) & (lot_size >= 3650.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(nstoreys >= 2) & (lot_size >= 3510.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 4340.0) =>[p] (sale_price >= 1)",
				"(nbed >= 4) =>[p] (sale_price >= 1)",
				"(nstoreys >= 3) =>[p] (sale_price >= 1)",
				"(nbath >= 3) =>[p] (sale_price >= 1)",
				"(lot_size >= 3986.0) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(lot_size >= 3986.0) & (nstoreys >= 2) =>[p] (sale_price >= 1)",
				"(lot_size >= 3986.0) & (basement >= 1) =>[p] (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 2135.0) =>[p] (sale_price >= 1)",
				"(lot_size >= 3934.0) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(lot_size >= 3968.0) & (nstoreys >= 2) =>[p] (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2610.0) =>[p] (sale_price >= 1)",
				"(desire_loc >= 1) & (rec_room >= 1) =>[p] (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2015.0) & (nstoreys >= 2) =>[p] (sale_price >= 1)",
				"(lot_size >= 3745.0) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 3240.0) =>[p] (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 3540.0) =>[p] (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 3) =>[p] (sale_price >= 1)",
				"(lot_size >= 3640.0) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(lot_size >= 3510.0) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(lot_size >= 3600.0) & (nstoreys >= 2) =>[p] (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) =>[p] (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3480.0) & (nbed >= 3) =>[p] (sale_price >= 1)",
				"(nstoreys >= 2) & (lot_size >= 3036.0) =>[p] (sale_price >= 1)",
				"(nstoreys >= 2) & (ngarage >= 1) =>[p] (sale_price >= 1)",
				"(nstoreys >= 2) & (lot_size >= 2325.0) & (nbed >= 3) =>[p] (sale_price >= 1)",
				"(lot_size >= 3480.0) & (ngarage >= 1) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(lot_size >= 3290.0) & (ngarage >= 1) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(nbed >= 3) & (lot_size >= 3060.0) & (drive >= 1) =>[p] (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "dummy", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //212
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and possible rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorUpwardUnionsPossibleRulesDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();

		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(nstoreys >= 4) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 8875.0) & (ngarage >= 1) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(lot_size >= 7085.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (air_cond >= 1) & (lot_size >= 4800.0) =>[p] (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 1) & (lot_size >= 3240.0) & (nbed >= 4) =>[p] (sale_price >= 3)",
				"(lot_size >= 6615.0) & (nbath >= 2) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 5960.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) =>[p] (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 4785.0) & (nbed >= 3) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6550.0) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6420.0) & (basement >= 1) & (air_cond >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 6240.0) & (nbath >= 2) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nbed >= 4) & (nbath >= 2) & (lot_size >= 3500.0) & (nstoreys >= 2) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (basement >= 1) & (lot_size >= 3960.0) & (drive >= 1) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3760.0) =>[p] (sale_price >= 3)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (nbed >= 3) & (drive >= 1) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (drive >= 1) & (lot_size >= 4000.0) & (basement >= 1) =>[p] (sale_price >= 3)",
				"(nbath >= 2) & (lot_size >= 4260.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 4) =>[p] (sale_price >= 3)",
				"(lot_size >= 5850.0) & (nstoreys >= 2) & (drive >= 1) & (ngarage >= 1) =>[p] (sale_price >= 3)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) =>[p] (sale_price >= 3)",
				"(basement >= 1) & (ngarage >= 1) & (lot_size >= 4040.0) & (nstoreys >= 2) & (drive >= 1) =>[p] (sale_price >= 3)",
				"(lot_size >= 10500.0) =>[p] (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 2970.0) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 3540.0) & (basement >= 1) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 3150.0) =>[p] (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 2870.0) =>[p] (sale_price >= 2)",
				"(lot_size >= 6720.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (nbath >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 3) & (lot_size >= 3760.0) =>[p] (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 3420.0) & (nbath >= 2) =>[p] (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 3640.0) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5720.0) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 4815.0) & (drive >= 1) & (desire_loc >= 1) =>[p] (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 2953.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 2880.0) & (nstoreys >= 2) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4040.0) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 2550.0) & (drive >= 1) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(nstoreys >= 2) & (lot_size >= 3750.0) & (nbed >= 3) =>[p] (sale_price >= 2)",
				"(nstoreys >= 2) & (lot_size >= 3510.0) & (nbed >= 3) & (drive >= 1) =>[p] (sale_price >= 2)",
				"(lot_size >= 4340.0) =>[p] (sale_price >= 1)",
				"(nbed >= 4) =>[p] (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 2135.0) =>[p] (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2610.0) =>[p] (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2015.0) & (nstoreys >= 2) =>[p] (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 3540.0) =>[p] (sale_price >= 1)",
				"(lot_size >= 3510.0) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) =>[p] (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3480.0) & (nbed >= 3) =>[p] (sale_price >= 1)",
				"(nstoreys >= 2) & (lot_size >= 2325.0) & (nbed >= 3) =>[p] (sale_price >= 1)",
				"(lot_size >= 3290.0) & (ngarage >= 1) & (drive >= 1) =>[p] (sale_price >= 1)",
				"(nbed >= 3) & (lot_size >= 3060.0) & (drive >= 1) =>[p] (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "pruning", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //55
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests downward unions and certain rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorDownwardUnionsCertainRulesDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size <= 1836.0) => (sale_price <= 0)",
				"(nbed <= 1) => (sale_price <= 0)",
				"(lot_size <= 2000.0) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2160.0) & (desire_loc <= 0) & (nbed <= 3) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 2500.0) & (nstoreys <= 1) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2700.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2700.0) & (nstoreys <= 1) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2800.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 2835.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2910.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2910.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 2990.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (nstoreys <= 1) & (nbed <= 2) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (nbed <= 2) & (rec_room <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(lot_size <= 3040.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 3090.0) & (nbed <= 2) & (nstoreys <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 2) & (lot_size <= 3500.0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 2) & (lot_size <= 3930.0) & (basement <= 0) => (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (lot_size <= 3180.0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (lot_size <= 4320.0) & (basement <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(lot_size <= 3185.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3264.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3500.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) => (sale_price <= 0)",
				"(lot_size <= 2520.0) => (sale_price <= 1)",
				"(nbed <= 1) => (sale_price <= 1)",
				"(lot_size <= 2787.0) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 2787.0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(lot_size <= 2800.0) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 2856.0) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 2850.0) & (ngarage <= 0) & (drive <= 0) => (sale_price <= 1)",
				"(lot_size <= 2910.0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(lot_size <= 2990.0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 2) & (basement <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (lot_size <= 3300.0) & (ngarage <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (lot_size <= 3300.0) & (nbath <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (nstoreys <= 1) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (basement <= 0) & (air_cond <= 0) & (lot_size <= 3660.0) => (sale_price <= 1)",
				"(lot_size <= 3120.0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(lot_size <= 3120.0) & (nbed <= 2) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3120.0) & (basement <= 0) & (nstoreys <= 2) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3185.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3180.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3290.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3240.0) & (basement <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3360.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3350.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3420.0) & (nbed <= 2) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3480.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3450.0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (air_cond <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (air_cond <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3512.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3520.0) & (nstoreys <= 1) & (nbed <= 2) => (sale_price <= 1)",
				"(lot_size <= 3620.0) & (nstoreys <= 1) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3600.0) & (nbed <= 2) & (desire_loc <= 0) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3630.0) & (nstoreys <= 1) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3649.0) & (nstoreys <= 1) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4095.0) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4000.0) & (rec_room <= 0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4280.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4500.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4750.0) & (basement <= 0) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4840.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 5320.0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 5320.0) & (air_cond <= 0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (lot_size <= 8400.0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) & (nstoreys <= 1) & (lot_size <= 10360.0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 5600.0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3750.0) & (nstoreys <= 1) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3850.0) & (nstoreys <= 1) & (rec_room <= 0) & (desire_loc <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 3960.0) & (nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 4000.0) & (nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 4340.0) & (nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5880.0) & (basement <= 0) & (desire_loc <= 0) & (ngarage <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6020.0) => (sale_price <= 1)",
				"(lot_size <= 3210.0) => (sale_price <= 2)",
				"(drive <= 0) => (sale_price <= 2)",
				"(nbed <= 1) => (sale_price <= 2)",
				"(lot_size <= 3480.0) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 3680.0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 3640.0) & (ngarage <= 0) => (sale_price <= 2)",
				"(lot_size <= 3630.0) & (nstoreys <= 2) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 3630.0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 3750.0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 3934.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 3900.0) & (nbed <= 2) => (sale_price <= 2)",
				"(lot_size <= 3900.0) & (nbed <= 3) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 3792.0) & (ngarage <= 0) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 3990.0) & (ngarage <= 0) & (rec_room <= 0) => (sale_price <= 2)",
				"(nbed <= 2) & (lot_size <= 6440.0) => (sale_price <= 2)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 8400.0) => (sale_price <= 2)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (ngarage <= 0) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (ngarage <= 1) & (air_cond <= 0) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 4079.0) & (ngarage <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4040.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 4240.0) & (nstoreys <= 1) & (ngarage <= 0) => (sale_price <= 2)",
				"(lot_size <= 4240.0) & (ngarage <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4240.0) & (ngarage <= 0) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4200.0) & (ngarage <= 1) & (air_cond <= 0) & (nstoreys <= 2) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 4260.0) & (ngarage <= 0) & (nbath <= 1) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 4400.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 4400.0) & (ngarage <= 0) & (nbed <= 3) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4360.0) & (ngarage <= 0) & (nbath <= 1) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 4520.0) & (nstoreys <= 1) & (ngarage <= 0) => (sale_price <= 2)",
				"(lot_size <= 4520.0) & (ngarage <= 0) & (nbed <= 3) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4500.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 3) => (sale_price <= 2)",
				"(lot_size <= 4775.0) & (nstoreys <= 1) & (ngarage <= 0) => (sale_price <= 2)",
				"(lot_size <= 4775.0) & (ngarage <= 0) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4640.0) & (ngarage <= 0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 4640.0) & (nbath <= 1) & (ngarage <= 1) & (nstoreys <= 2) & (rec_room <= 0) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 4600.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 4840.0) & (ngarage <= 0) & (air_cond <= 0) & (nbed <= 3) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 4840.0) & (nbath <= 1) & (basement <= 0) & (nstoreys <= 2) & (rec_room <= 0) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 4995.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 4995.0) & (nstoreys <= 1) & (rec_room <= 0) & (ngarage <= 0) => (sale_price <= 2)",
				"(lot_size <= 4950.0) & (ngarage <= 0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 4920.0) & (nbath <= 1) & (basement <= 0) & (nstoreys <= 2) & (rec_room <= 0) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 5010.0) & (ngarage <= 0) & (air_cond <= 0) & (nbed <= 3) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (ngarage <= 0) & (nbath <= 1) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 5495.0) & (nstoreys <= 1) & (nbed <= 3) & (rec_room <= 0) & (ngarage <= 0) => (sale_price <= 2)",
				"(lot_size <= 5500.0) & (nstoreys <= 1) & (rec_room <= 0) & (nbed <= 3) & (ngarage <= 0) => (sale_price <= 2)",
				"(lot_size <= 5500.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) & (nstoreys <= 2) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 5800.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 5880.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 5985.0) & (nstoreys <= 1) & (rec_room <= 0) & (nbed <= 3) & (ngarage <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 7000.0) & (air_cond <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8250.0) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6100.0) & (ngarage <= 0) & (nbed <= 3) & (rec_room <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (ngarage <= 0) & (air_cond <= 0) & (rec_room <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (lot_size <= 6360.0) & (nbed <= 3) & (rec_room <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (nbath <= 1) & (nstoreys <= 2) & (basement <= 0) => (sale_price <= 2)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "dummy", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //140
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests downward unions and certain rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorDownwardUnionsCertainRulesDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size <= 2000.0) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2160.0) & (desire_loc <= 0) & (nbed <= 3) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (nbed <= 2) & (rec_room <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(lot_size <= 3040.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 2) & (lot_size <= 3930.0) & (basement <= 0) => (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (lot_size <= 4320.0) & (basement <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(lot_size <= 3264.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3500.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) => (sale_price <= 0)",
				"(lot_size <= 2520.0) => (sale_price <= 1)",
				"(lot_size <= 2856.0) & (basement <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (lot_size <= 3300.0) & (ngarage <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (nstoreys <= 1) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (basement <= 0) & (air_cond <= 0) & (lot_size <= 3660.0) => (sale_price <= 1)",
				"(lot_size <= 3350.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (air_cond <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4095.0) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (lot_size <= 8400.0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) & (nstoreys <= 1) & (lot_size <= 10360.0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 5600.0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3750.0) & (nstoreys <= 1) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5880.0) & (basement <= 0) & (desire_loc <= 0) & (ngarage <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6020.0) => (sale_price <= 1)",
				"(lot_size <= 3210.0) => (sale_price <= 2)",
				"(drive <= 0) => (sale_price <= 2)",
				"(lot_size <= 3480.0) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 3630.0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 3750.0) & (nbed <= 3) => (sale_price <= 2)",
				"(nbed <= 2) & (lot_size <= 6440.0) => (sale_price <= 2)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (ngarage <= 1) & (air_cond <= 0) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 4400.0) & (ngarage <= 0) & (nbed <= 3) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4500.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 3) => (sale_price <= 2)",
				"(lot_size <= 4775.0) & (ngarage <= 0) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4600.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 4950.0) & (ngarage <= 0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 4920.0) & (nbath <= 1) & (basement <= 0) & (nstoreys <= 2) & (rec_room <= 0) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (ngarage <= 0) & (nbath <= 1) & (rec_room <= 0) => (sale_price <= 2)",
				"(lot_size <= 5500.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) & (nstoreys <= 2) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 5880.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8250.0) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6100.0) & (ngarage <= 0) & (nbed <= 3) & (rec_room <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (ngarage <= 0) & (air_cond <= 0) & (rec_room <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (lot_size <= 6360.0) & (nbed <= 3) & (rec_room <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (nbath <= 1) & (nstoreys <= 2) & (basement <= 0) => (sale_price <= 2)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "pruning", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //46
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests downward unions and certain rules for "windsor" data set.
	 * Employs VC-DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorDownwardUnionsCertainRulesVCDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size <= 1836.0) => (sale_price <= 0)",
				"(nbed <= 1) => (sale_price <= 0)",
				"(lot_size <= 2000.0) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2160.0) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2145.0) & (basement <= 0) & (nbath <= 1) & (ngarage <= 0) => (sale_price <= 0)",
				"(lot_size <= 2145.0) & (nbed <= 3) & (nstoreys <= 2) => (sale_price <= 0)",
				"(lot_size <= 2500.0) & (nstoreys <= 1) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2475.0) & (desire_loc <= 0) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 2700.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2700.0) & (nstoreys <= 1) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 2650.0) & (desire_loc <= 0) & (nbed <= 3) & (nbath <= 1) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2610.0) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2787.0) & (desire_loc <= 0) & (basement <= 0) & (nbath <= 2) => (sale_price <= 0)",
				"(lot_size <= 2800.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 2835.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2910.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2910.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 2990.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (nstoreys <= 1) & (nbed <= 2) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (nbed <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (drive <= 0) & (nbath <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 3000.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3040.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 3100.0) & (nbed <= 2) & (nstoreys <= 1) => (sale_price <= 0)",
				"(lot_size <= 3100.0) & (drive <= 0) & (nbath <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 3100.0) & (drive <= 0) & (nbath <= 1) & (nbed <= 3) & (rec_room <= 0) => (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 2) => (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (lot_size <= 3180.0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (lot_size <= 4320.0) & (basement <= 0) => (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (basement <= 0) & (lot_size <= 5300.0) => (sale_price <= 0)",
				"(drive <= 0) & (lot_size <= 3120.0) & (nbath <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(drive <= 0) & (lot_size <= 3150.0) & (nbath <= 1) & (basement <= 0) => (sale_price <= 0)",
				"(drive <= 0) & (lot_size <= 3300.0) & (basement <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (lot_size <= 3420.0) & (basement <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (lot_size <= 3480.0) & (basement <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (basement <= 0) & (lot_size <= 3660.0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (basement <= 0) & (lot_size <= 3630.0) & (nbed <= 3) => (sale_price <= 0)",
				"(drive <= 0) & (basement <= 0) & (lot_size <= 4352.0) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (basement <= 0) & (nbath <= 1) & (lot_size <= 4960.0) => (sale_price <= 0)",
				"(lot_size <= 3210.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3210.0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 3180.0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) & (nbed <= 4) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 3264.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3240.0) & (basement <= 0) & (nstoreys <= 2) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3360.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3450.0) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3450.0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) & (air_cond <= 0) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 3480.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3500.0) & (nbed <= 2) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3500.0) & (basement <= 0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 0)",
				"(lot_size <= 3620.0) & (nbed <= 2) & (desire_loc <= 0) & (basement <= 0) => (sale_price <= 0)",
				"(lot_size <= 3600.0) & (nbed <= 2) & (desire_loc <= 0) & (rec_room <= 0) & (air_cond <= 0) & (ngarage <= 1) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 3630.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 3649.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 3800.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 3934.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4000.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4040.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4095.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4120.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4400.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4500.0) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4750.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (ngarage <= 0) & (basement <= 0) & (lot_size <= 4960.0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (ngarage <= 0) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (ngarage <= 0) & (nstoreys <= 1) & (lot_size <= 4960.0) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (nstoreys <= 1) & (lot_size <= 8100.0) & (ngarage <= 1) => (sale_price <= 0)",
				"(lot_size <= 3630.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) & (nstoreys <= 2) & (nbed <= 4) => (sale_price <= 0)",
				"(lot_size <= 3640.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) & (nstoreys <= 2) & (nbed <= 4) => (sale_price <= 0)",
				"(lot_size <= 3750.0) & (nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3750.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3792.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3850.0) & (nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3850.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 3960.0) & (nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3970.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) & (rec_room <= 0) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 4000.0) & (nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 4000.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 4000.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) & (ngarage <= 1) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 4370.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbed <= 3) & (air_cond <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 4400.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbed <= 3) & (air_cond <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 0)",
				"(nstoreys <= 1) & (lot_size <= 5495.0) & (ngarage <= 0) & (desire_loc <= 0) & (rec_room <= 0) & (nbath <= 1) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 0)",
				"(nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6060.0) & (nbath <= 1) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 0)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 5880.0) & (ngarage <= 1) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(lot_size <= 4500.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbed <= 3) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 4775.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 5200.0) & (basement <= 0) & (ngarage <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 3) => (sale_price <= 0)",
				"(ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (lot_size <= 5400.0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 0)",
				"(ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (nbed <= 3) & (desire_loc <= 0) & (lot_size <= 6615.0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 2520.0) => (sale_price <= 1)",
				"(nbed <= 1) => (sale_price <= 1)",
				"(lot_size <= 2787.0) => (sale_price <= 1)",
				"(lot_size <= 2800.0) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 2856.0) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 2850.0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 2953.0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(lot_size <= 2953.0) & (rec_room <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 2990.0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 2) => (sale_price <= 1)",
				"(drive <= 0) & (lot_size <= 3300.0) & (ngarage <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (lot_size <= 3300.0) & (nbath <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (nstoreys <= 1) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (basement <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(drive <= 0) & (basement <= 0) & (lot_size <= 6480.0) => (sale_price <= 1)",
				"(drive <= 0) & (air_cond <= 0) & (rec_room <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(drive <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 3) & (rec_room <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 3120.0) & (nstoreys <= 1) => (sale_price <= 1)",
				"(lot_size <= 3120.0) & (nbed <= 2) => (sale_price <= 1)",
				"(lot_size <= 3120.0) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 3036.0) & (rec_room <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3000.0) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3150.0) & (rec_room <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3210.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3210.0) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 3210.0) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3290.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3240.0) & (basement <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3400.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3400.0) & (basement <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 3400.0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3420.0) & (nbed <= 2) => (sale_price <= 1)",
				"(lot_size <= 3480.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3460.0) & (basement <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 3450.0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3450.0) & (nbath <= 1) & (air_cond <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3500.0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3512.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3520.0) & (nstoreys <= 1) & (nbed <= 2) => (sale_price <= 1)",
				"(lot_size <= 3520.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3520.0) & (basement <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 3620.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3600.0) & (nbed <= 2) => (sale_price <= 1)",
				"(lot_size <= 3600.0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3570.0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3630.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3630.0) & (basement <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3649.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3640.0) & (nbath <= 1) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3640.0) & (ngarage <= 0) & (rec_room <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbath <= 2) => (sale_price <= 1)",
				"(lot_size <= 3680.0) & (basement <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4095.0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4280.0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4500.0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4750.0) & (basement <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4840.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 5320.0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 5320.0) & (air_cond <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (lot_size <= 8400.0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) & (nstoreys <= 1) & (lot_size <= 10360.0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (lot_size <= 5600.0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4960.0) & (rec_room <= 0) & (desire_loc <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 5850.0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 1) & (air_cond <= 0) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 6060.0) & (rec_room <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3750.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3750.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3792.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3850.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 3850.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3960.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 3960.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3990.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 3970.0) & (ngarage <= 0) & (rec_room <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 4000.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 4000.0) & (ngarage <= 0) & (basement <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4000.0) & (basement <= 0) & (ngarage <= 1) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 4080.0) & (nstoreys <= 1) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 4080.0) & (ngarage <= 0) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 4080.0) & (basement <= 0) & (nbath <= 1) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 4046.0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 1) & (air_cond <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 4130.0) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 4240.0) & (ngarage <= 0) & (basement <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4340.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 1)",
				"(lot_size <= 4320.0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 4370.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4360.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 4400.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4500.0) & (nstoreys <= 1) & (rec_room <= 0) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 4500.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4500.0) & (basement <= 0) & (ngarage <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4500.0) & (air_cond <= 0) & (rec_room <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) => (sale_price <= 1)",
				"(lot_size <= 4632.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 4632.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbath <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 4600.0) & (basement <= 0) & (ngarage <= 1) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4785.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) => (sale_price <= 1)",
				"(lot_size <= 4785.0) & (nbath <= 1) & (ngarage <= 1) & (desire_loc <= 0) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4840.0) & (basement <= 0) & (ngarage <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 4840.0) & (basement <= 0) & (air_cond <= 0) & (ngarage <= 1) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4992.0) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5400.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5495.0) & (rec_room <= 0) & (desire_loc <= 0) & (ngarage <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5880.0) & (basement <= 0) & (desire_loc <= 0) & (ngarage <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5800.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5985.0) & (rec_room <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 7000.0) & (air_cond <= 0) => (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (lot_size <= 8250.0) & (nbath <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8080.0) & (desire_loc <= 0) & (nbath <= 1) & (ngarage <= 2) => (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 6060.0) & (air_cond <= 0) & (desire_loc <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (rec_room <= 0) & (lot_size <= 8250.0) & (nbath <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 7260.0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 5040.0) & (ngarage <= 0) & (rec_room <= 0) & (nstoreys <= 2) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 5200.0) & (basement <= 0) & (ngarage <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 3) => (sale_price <= 1)",
				"(lot_size <= 5300.0) & (basement <= 0) & (air_cond <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 5) => (sale_price <= 1)",
				"(lot_size <= 5400.0) & (basement <= 0) & (ngarage <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 5500.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 5500.0) & (ngarage <= 0) & (air_cond <= 0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbed <= 3) & (nstoreys <= 2) => (sale_price <= 1)",
				"(ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (lot_size <= 6450.0) & (nbath <= 1) => (sale_price <= 1)",
				"(ngarage <= 0) & (basement <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(ngarage <= 0) & (air_cond <= 0) & (lot_size <= 5960.0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 5850.0) & (air_cond <= 0) & (nbath <= 1) & (desire_loc <= 0) & (rec_room <= 0) & (ngarage <= 1) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 1)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) & (lot_size <= 9000.0) & (nbed <= 3) => (sale_price <= 1)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (lot_size <= 9500.0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 3210.0) => (sale_price <= 2)",
				"(drive <= 0) => (sale_price <= 2)",
				"(nbed <= 1) => (sale_price <= 2)",
				"(lot_size <= 3480.0) => (sale_price <= 2)",
				"(lot_size <= 3680.0) => (sale_price <= 2)",
				"(lot_size <= 3750.0) => (sale_price <= 2)",
				"(lot_size <= 3934.0) => (sale_price <= 2)",
				"(lot_size <= 3990.0) => (sale_price <= 2)",
				"(nbed <= 2) & (lot_size <= 6440.0) => (sale_price <= 2)",
				"(nbed <= 2) & (basement <= 0) => (sale_price <= 2)",
				"(nbed <= 2) & (rec_room <= 0) & (lot_size <= 9166.0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (ngarage <= 0) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (ngarage <= 1) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4000.0) & (nbed <= 3) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 4080.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 4080.0) & (ngarage <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4080.0) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 4046.0) & (ngarage <= 1) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4240.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 4240.0) & (ngarage <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4240.0) & (ngarage <= 0) & (rec_room <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4200.0) & (ngarage <= 1) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4130.0) & (air_cond <= 0) => (sale_price <= 2)",
				"(lot_size <= 4260.0) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4410.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 4410.0) & (ngarage <= 0) & (nbed <= 3) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4410.0) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4410.0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4410.0) & (air_cond <= 0) & (nbed <= 4) => (sale_price <= 2)",
				"(lot_size <= 4520.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 4520.0) & (ngarage <= 0) & (nbed <= 3) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4510.0) & (ngarage <= 0) & (air_cond <= 0) & (nbed <= 4) => (sale_price <= 2)",
				"(lot_size <= 4510.0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4500.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 4500.0) & (air_cond <= 0) & (nbed <= 4) => (sale_price <= 2)",
				"(lot_size <= 4785.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 4785.0) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4785.0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4600.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) => (sale_price <= 2)",
				"(lot_size <= 4840.0) & (ngarage <= 0) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 4840.0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4995.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 4992.0) & (ngarage <= 0) & (air_cond <= 0) & (nbed <= 4) => (sale_price <= 2)",
				"(lot_size <= 4992.0) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4992.0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 4992.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 5010.0) & (ngarage <= 0) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 5000.0) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 5136.0) & (ngarage <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 5170.0) & (ngarage <= 0) & (nbath <= 1) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nstoreys <= 1) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (ngarage <= 0) & (nbed <= 5) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (ngarage <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nstoreys <= 2) & (basement <= 0) & (rec_room <= 0) & (nbed <= 4) & (desire_loc <= 0) => (sale_price <= 2)",
				"(lot_size <= 5495.0) & (nstoreys <= 1) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 5680.0) & (nstoreys <= 1) & (rec_room <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 5680.0) & (air_cond <= 0) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 5680.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 5680.0) & (ngarage <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 5680.0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 5800.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 5800.0) & (ngarage <= 0) & (nbed <= 3) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 2)",
				"(lot_size <= 5948.0) & (nstoreys <= 1) & (basement <= 0) => (sale_price <= 2)",
				"(lot_size <= 5948.0) & (air_cond <= 0) & (ngarage <= 1) & (nbed <= 3) & (nstoreys <= 2) => (sale_price <= 2)",
				"(lot_size <= 5948.0) & (ngarage <= 0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 2)",
				"(lot_size <= 5985.0) & (nstoreys <= 1) & (rec_room <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(lot_size <= 5960.0) & (ngarage <= 0) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 7000.0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8520.0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6100.0) & (ngarage <= 0) & (nbed <= 3) => (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6100.0) & (rec_room <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(nstoreys <= 1) & (ngarage <= 0) & (air_cond <= 0) & (rec_room <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6360.0) & (nbath <= 1) => (sale_price <= 2)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (lot_size <= 7260.0) & (air_cond <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 8250.0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (air_cond <= 0) & (lot_size <= 7085.0) & (nbath <= 1) => (sale_price <= 2)",
				"(nstoreys <= 1) & (rec_room <= 0) & (lot_size <= 7800.0) & (nbath <= 1) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (lot_size <= 6360.0) & (nbed <= 3) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 2)",
				"(ngarage <= 0) & (air_cond <= 0) & (nstoreys <= 2) & (lot_size <= 6600.0) & (nbed <= 4) => (sale_price <= 2)",
				"(ngarage <= 0) & (nbath <= 1) & (nstoreys <= 2) & (basement <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 2)",
				"(ngarage <= 0) & (nbath <= 1) & (basement <= 0) & (nstoreys <= 3) => (sale_price <= 2)",
				"(ngarage <= 0) & (lot_size <= 6000.0) & (rec_room <= 0) & (nbed <= 3) & (desire_loc <= 0) => (sale_price <= 2)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 2)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 2)",
				"(air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) & (ngarage <= 1) & (nbed <= 4) & (rec_room <= 0) & (desire_loc <= 0) => (sale_price <= 2)",
				"(air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) & (nbed <= 4) & (desire_loc <= 0) => (sale_price <= 2)",
				"(air_cond <= 0) & (nbath <= 1) & (ngarage <= 1) & (nstoreys <= 2) & (lot_size <= 7200.0) => (sale_price <= 2)",
				"(air_cond <= 0) & (desire_loc <= 0) & (ngarage <= 1) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) & (lot_size <= 9667.0) => (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6660.0) & (ngarage <= 1) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) => (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6710.0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) & (nbath <= 2) => (sale_price <= 2)",
				"(nbath <= 1) & (lot_size <= 6321.0) & (nstoreys <= 2) & (ngarage <= 1) => (sale_price <= 2)",
				"(nbath <= 1) & (basement <= 0) & (nstoreys <= 2) & (lot_size <= 7600.0) => (sale_price <= 2)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "dummy", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //319
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests downward unions and certain rules for "windsor" data set.
	 * Employs VC-DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorDownwardUnionsCertainRulesVCDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size <= 2145.0) & (basement <= 0) & (ngarage <= 0) => (sale_price <= 0)",
				"(lot_size <= 2610.0) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 2787.0) & (desire_loc <= 0) & (basement <= 0) & (nbath <= 2) => (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 2) => (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (basement <= 0) & (lot_size <= 5300.0) => (sale_price <= 0)",
				"(drive <= 0) & (basement <= 0) & (lot_size <= 3630.0) & (nbed <= 3) => (sale_price <= 0)",
				"(drive <= 0) & (basement <= 0) & (nbath <= 1) & (lot_size <= 4960.0) => (sale_price <= 0)",
				"(lot_size <= 3210.0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 3180.0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) & (nbed <= 4) & (rec_room <= 0) => (sale_price <= 0)",
				"(lot_size <= 3500.0) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 0)",
				"(lot_size <= 3600.0) & (nbed <= 2) & (desire_loc <= 0) & (ngarage <= 1) => (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4120.0) & (basement <= 0) & (desire_loc <= 0) => (sale_price <= 0)",
				"(nbed <= 2) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 8100.0) & (ngarage <= 1) => (sale_price <= 0)",
				"(lot_size <= 3630.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 2) & (nbed <= 4) => (sale_price <= 0)",
				"(lot_size <= 3640.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) & (nbed <= 4) => (sale_price <= 0)",
				"(lot_size <= 3970.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (rec_room <= 0) & (nbed <= 3) => (sale_price <= 0)",
				"(lot_size <= 4000.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (ngarage <= 1) & (nbed <= 3) => (sale_price <= 0)",
				"(nstoreys <= 1) & (lot_size <= 5495.0) & (ngarage <= 0) & (desire_loc <= 0) & (rec_room <= 0) & (nbed <= 3) => (sale_price <= 0)",
				"(nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6060.0) & (nbath <= 1) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 0)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 5880.0) & (ngarage <= 1) & (desire_loc <= 0) & (air_cond <= 0) => (sale_price <= 0)",
				"(lot_size <= 4500.0) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) & (nbed <= 3) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 0)",
				"(lot_size <= 5200.0) & (basement <= 0) & (ngarage <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 3) => (sale_price <= 0)",
				"(ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (lot_size <= 5400.0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 0)",
				"(ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (nbed <= 3) & (desire_loc <= 0) & (lot_size <= 6615.0) & (nstoreys <= 2) & (nbath <= 1) => (sale_price <= 0)",
				"(drive <= 0) & (nbath <= 1) => (sale_price <= 1)",
				"(drive <= 0) & (air_cond <= 0) & (rec_room <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 3) & (ngarage <= 0) => (sale_price <= 1)",
				"(lot_size <= 3210.0) & (nbath <= 1) => (sale_price <= 1)",
				"(lot_size <= 3640.0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 3640.0) & (ngarage <= 0) & (rec_room <= 0) & (nbath <= 2) => (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4500.0) => (sale_price <= 1)",
				"(nbed <= 2) & (basement <= 0) & (air_cond <= 0) & (lot_size <= 10360.0) => (sale_price <= 1)",
				"(lot_size <= 3850.0) & (nstoreys <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(ngarage <= 0) & (basement <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4500.0) & (air_cond <= 0) & (rec_room <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) => (sale_price <= 1)",
				"(lot_size <= 4632.0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (nbath <= 1) & (rec_room <= 0) => (sale_price <= 1)",
				"(lot_size <= 4600.0) & (basement <= 0) & (ngarage <= 1) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4785.0) & (nbath <= 1) & (ngarage <= 1) & (desire_loc <= 0) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 4992.0) & (basement <= 0) & (air_cond <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8080.0) & (desire_loc <= 0) & (nbath <= 1) & (ngarage <= 2) => (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (rec_room <= 0) & (lot_size <= 8250.0) & (nbath <= 1) => (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 7260.0) & (nbed <= 3) => (sale_price <= 1)",
				"(lot_size <= 5040.0) & (ngarage <= 0) & (nstoreys <= 2) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) => (sale_price <= 1)",
				"(basement <= 0) & (ngarage <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 3) => (sale_price <= 1)",
				"(lot_size <= 5300.0) & (basement <= 0) & (air_cond <= 0) & (ngarage <= 0) & (nstoreys <= 2) & (nbed <= 5) => (sale_price <= 1)",
				"(ngarage <= 0) & (air_cond <= 0) & (lot_size <= 5960.0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) => (sale_price <= 1)",
				"(lot_size <= 5850.0) & (air_cond <= 0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 1) & (nstoreys <= 2) & (nbed <= 3) => (sale_price <= 1)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (lot_size <= 9500.0) & (nbed <= 3) => (sale_price <= 1)",
				"(drive <= 0) => (sale_price <= 2)",
				"(lot_size <= 3990.0) => (sale_price <= 2)",
				"(nbed <= 2) & (basement <= 0) => (sale_price <= 2)",
				"(nbed <= 2) & (lot_size <= 9166.0) & (nbath <= 1) => (sale_price <= 2)",
				"(ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (nbed <= 5) => (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nstoreys <= 2) & (basement <= 0) & (rec_room <= 0) & (nbed <= 4) & (desire_loc <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8520.0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6100.0) & (rec_room <= 0) & (air_cond <= 0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (ngarage <= 0) & (nbath <= 1) => (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6360.0) & (nbath <= 1) => (sale_price <= 2)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 8250.0) => (sale_price <= 2)",
				"(nstoreys <= 1) & (air_cond <= 0) & (lot_size <= 7085.0) & (nbath <= 1) => (sale_price <= 2)",
				"(nstoreys <= 1) & (rec_room <= 0) & (lot_size <= 7800.0) & (nbath <= 1) => (sale_price <= 2)",
				"(ngarage <= 0) & (nbath <= 1) & (basement <= 0) & (nstoreys <= 3) => (sale_price <= 2)",
				"(ngarage <= 0) & (lot_size <= 6000.0) & (rec_room <= 0) & (nbed <= 3) & (desire_loc <= 0) => (sale_price <= 2)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) => (sale_price <= 2)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (desire_loc <= 0) => (sale_price <= 2)",
				"(air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) & (nbed <= 4) & (desire_loc <= 0) => (sale_price <= 2)",
				"(air_cond <= 0) & (nbath <= 1) & (ngarage <= 1) & (nstoreys <= 2) & (lot_size <= 7200.0) => (sale_price <= 2)",
				"(air_cond <= 0) & (desire_loc <= 0) & (ngarage <= 1) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) & (lot_size <= 9667.0) => (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6660.0) & (ngarage <= 1) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) => (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6710.0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) & (nbath <= 2) => (sale_price <= 2)",
				"(nbath <= 1) & (lot_size <= 6321.0) & (nstoreys <= 2) & (ngarage <= 1) => (sale_price <= 2)",
				"(nbath <= 1) & (basement <= 0) & (nstoreys <= 2) & (lot_size <= 7600.0) => (sale_price <= 2)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "pruning", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //73
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests downward unions and possible rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorDownwardUnionsPossibleRulesDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();

		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size <= 1836.0) =>[p] (sale_price <= 0)",
				"(nbed <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 2135.0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 2175.0) & (nbed <= 3) & (ngarage <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2500.0) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 2398.0) & (nstoreys <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 2550.0) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 2747.0) & (nbed <= 2) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2747.0) & (nstoreys <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 2747.0) & (nbed <= 3) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2747.0) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2747.0) & (desire_loc <= 0) & (nbed <= 4) & (nbath <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 2787.0) & (desire_loc <= 0) & (nbed <= 4) & (nbath <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 2800.0) & (nstoreys <= 1) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2835.0) & (nbed <= 2) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2990.0) & (nbed <= 2) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2970.0) & (nstoreys <= 1) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2970.0) & (desire_loc <= 0) & (nbed <= 3) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 2) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbath <= 1) & (nbed <= 3) & (lot_size <= 4900.0) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbath <= 1) & (lot_size <= 3660.0) & (nbed <= 4) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbath <= 1) & (lot_size <= 3420.0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbath <= 1) & (lot_size <= 4960.0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 3) & (air_cond <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3120.0) & (nbed <= 2) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3120.0) & (nstoreys <= 1) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3120.0) & (basement <= 0) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3036.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3036.0) & (desire_loc <= 0) & (nbed <= 3) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3162.0) & (basement <= 0) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3150.0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3210.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3210.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3180.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 4) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 3290.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3240.0) & (basement <= 0) & (desire_loc <= 0) & (nbed <= 3) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3360.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3350.0) & (basement <= 0) & (desire_loc <= 0) & (nbed <= 3) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3450.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3450.0) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3450.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 3450.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3460.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 3480.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3510.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3510.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3500.0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) & (nbed <= 4) =>[p] (sale_price <= 0)",
				"(lot_size <= 3584.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3570.0) & (nbed <= 2) & (desire_loc <= 0) & (ngarage <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3570.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3620.0) & (nbed <= 2) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3600.0) & (nbed <= 2) & (desire_loc <= 0) & (ngarage <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3600.0) & (nstoreys <= 1) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 3800.0) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 3934.0) & (desire_loc <= 0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4040.0) & (desire_loc <= 0) & (ngarage <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4095.0) & (desire_loc <= 0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4120.0) & (desire_loc <= 0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4120.0) & (desire_loc <= 0) & (nbath <= 1) & (ngarage <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4400.0) & (desire_loc <= 0) & (ngarage <= 1) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4500.0) & (desire_loc <= 0) & (ngarage <= 1) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4800.0) & (desire_loc <= 0) & (ngarage <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4960.0) & (desire_loc <= 0) & (ngarage <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (ngarage <= 0) & (desire_loc <= 0) & (rec_room <= 0) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (desire_loc <= 0) & (air_cond <= 0) & (basement <= 0) & (lot_size <= 8400.0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3630.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3630.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 4) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 3640.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3640.0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) & (nbed <= 4) =>[p] (sale_price <= 0)",
				"(lot_size <= 3650.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3792.0) & (nstoreys <= 1) & (desire_loc <= 0) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3792.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 3792.0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) & (nbed <= 4) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3850.0) & (nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3850.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3900.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3990.0) & (nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 3990.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 4046.0) & (nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4046.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (ngarage <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 4000.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4080.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 4240.0) & (nstoreys <= 1) & (desire_loc <= 0) & (ngarage <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 4240.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 4370.0) & (nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4370.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4360.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (ngarage <= 0) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4400.0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4500.0) & (nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 4500.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 4520.0) & (ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 4600.0) & (ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4785.0) & (nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 4785.0) & (ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4785.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 4800.0) & (nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 4840.0) & (ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 4840.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 4920.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 5010.0) & (ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (nbed <= 3) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (lot_size <= 5495.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (ngarage <= 0) & (basement <= 0) & (desire_loc <= 0) & (lot_size <= 8250.0) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (lot_size <= 6060.0) & (air_cond <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (lot_size <= 5800.0) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (ngarage <= 2) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 5880.0) & (desire_loc <= 0) & (ngarage <= 1) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (basement <= 0) & (ngarage <= 1) & (desire_loc <= 0) & (lot_size <= 8875.0) & (air_cond <= 0) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (basement <= 0) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 8250.0) & (ngarage <= 2) & (nbath <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 5200.0) & (ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 5400.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(ngarage <= 0) & (air_cond <= 0) & (basement <= 0) & (desire_loc <= 0) & (nbed <= 3) & (lot_size <= 6615.0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 5850.0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(air_cond <= 0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) & (lot_size <= 9000.0) & (ngarage <= 2) & (rec_room <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 2787.0) =>[p] (sale_price <= 1)",
				"(nbed <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 2800.0) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 2990.0) & (ngarage <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 2990.0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 3120.0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 3210.0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 3180.0) & (basement <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 3180.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3290.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3240.0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 3) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 3) & (ngarage <= 0) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (basement <= 0) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (air_cond <= 0) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 3400.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3400.0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 3680.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3680.0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3680.0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 3680.0) & (basement <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3640.0) & (nstoreys <= 1) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 3640.0) & (ngarage <= 0) & (rec_room <= 0) & (nbath <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3792.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3792.0) & (nstoreys <= 1) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 3792.0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 3792.0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 3990.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3990.0) & (nstoreys <= 1) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 3990.0) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 3970.0) & (nbath <= 1) & (rec_room <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 3880.0) & (rec_room <= 0) & (nbed <= 3) & (nbath <= 2) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4095.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4080.0) & (nstoreys <= 1) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 4080.0) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 4046.0) & (nbath <= 1) & (rec_room <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4240.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4240.0) & (nstoreys <= 1) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 4240.0) & (nbath <= 1) & (rec_room <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4160.0) & (nbath <= 1) & (rec_room <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 4130.0) & (basement <= 0) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4280.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4260.0) & (basement <= 0) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4370.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4370.0) & (nstoreys <= 1) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 4370.0) & (nbath <= 1) & (nbed <= 3) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4360.0) & (nbath <= 1) & (rec_room <= 0) & (nstoreys <= 2) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4300.0) & (air_cond <= 0) & (rec_room <= 0) & (nbed <= 3) & (nbath <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4400.0) & (nbed <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4400.0) & (nbath <= 1) & (nbed <= 3) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4500.0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 4800.0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 5600.0) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 5600.0) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 5900.0) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 5885.0) & (desire_loc <= 0) & (nbath <= 1) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (lot_size <= 6060.0) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (air_cond <= 0) & (lot_size <= 6360.0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (air_cond <= 0) & (lot_size <= 10360.0) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (air_cond <= 0) & (lot_size <= 10360.0) & (basement <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4500.0) & (nstoreys <= 1) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 4500.0) & (nbath <= 1) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4500.0) & (basement <= 0) & (air_cond <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 4500.0) & (air_cond <= 0) & (rec_room <= 0) & (nbath <= 2) & (nbed <= 4) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4520.0) & (nbath <= 1) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4632.0) & (nbath <= 1) & (air_cond <= 0) & (ngarage <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4632.0) & (nbath <= 1) & (ngarage <= 0) & (nstoreys <= 2) & (rec_room <= 0) & (basement <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4600.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4785.0) & (nstoreys <= 1) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 4785.0) & (ngarage <= 0) & (nbath <= 1) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4785.0) & (nbath <= 1) & (nbed <= 3) & (nstoreys <= 2) & (ngarage <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4800.0) & (nstoreys <= 1) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 4840.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 4920.0) & (nstoreys <= 1) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4920.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 4992.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 5010.0) & (air_cond <= 0) & (nbed <= 3) & (rec_room <= 0) & (nstoreys <= 2) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(lot_size <= 5000.0) & (nbath <= 1) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5040.0) & (nbath <= 1) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5200.0) & (air_cond <= 0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 3) & (nbed <= 4) =>[p] (sale_price <= 1)",
				"(lot_size <= 5300.0) & (air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) & (nbed <= 5) & (ngarage <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5360.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 5400.0) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) & (nbed <= 4) & (ngarage <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5495.0) & (nstoreys <= 1) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5500.0) & (nstoreys <= 1) & (air_cond <= 0) & (nbed <= 3) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5500.0) & (air_cond <= 0) & (nbath <= 1) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5500.0) & (nbath <= 1) & (nstoreys <= 2) & (nbed <= 3) & (rec_room <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 5985.0) & (air_cond <= 0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 6040.0) & (air_cond <= 0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 6000.0) & (basement <= 0) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (lot_size <= 6060.0) & (air_cond <= 0) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (ngarage <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 6300.0) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (air_cond <= 0) & (lot_size <= 7000.0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (air_cond <= 0) & (lot_size <= 8875.0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8080.0) & (nbath <= 1) & (ngarage <= 2) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6450.0) & (air_cond <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 7260.0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 8250.0) & (nbath <= 1) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5948.0) & (air_cond <= 0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 5948.0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) & (nbed <= 3) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5960.0) & (air_cond <= 0) & (nbath <= 1) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(ngarage <= 0) & (air_cond <= 0) & (basement <= 0) & (lot_size <= 6650.0) & (nstoreys <= 2) & (nbed <= 4) =>[p] (sale_price <= 1)",
				"(ngarage <= 0) & (nbath <= 1) & (desire_loc <= 0) & (nstoreys <= 2) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(air_cond <= 0) & (lot_size <= 6000.0) & (nbath <= 1) & (nstoreys <= 2) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (lot_size <= 9500.0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 3990.0) =>[p] (sale_price <= 2)",
				"(drive <= 0) =>[p] (sale_price <= 2)",
				"(lot_size <= 4095.0) & (air_cond <= 0) =>[p] (sale_price <= 2)",
				"(lot_size <= 4000.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(nbed <= 2) & (lot_size <= 6440.0) =>[p] (sale_price <= 2)",
				"(nbed <= 2) & (basement <= 0) =>[p] (sale_price <= 2)",
				"(nbed <= 2) & (lot_size <= 9166.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 4260.0) & (air_cond <= 0) =>[p] (sale_price <= 2)",
				"(lot_size <= 4260.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 4520.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 4510.0) & (air_cond <= 0) & (nbed <= 4) =>[p] (sale_price <= 2)",
				"(lot_size <= 4785.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 4600.0) & (basement <= 0) & (ngarage <= 1) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(lot_size <= 4840.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 5010.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 4995.0) & (nstoreys <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 4992.0) & (air_cond <= 0) & (nbed <= 4) =>[p] (sale_price <= 2)",
				"(lot_size <= 5136.0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nstoreys <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nbath <= 1) & (nstoreys <= 3) =>[p] (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (nbed <= 5) =>[p] (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nstoreys <= 2) & (basement <= 0) & (nbed <= 4) & (rec_room <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(lot_size <= 5495.0) & (nbath <= 1) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 12944.0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6540.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6450.0) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6254.0) & (nbed <= 3) & (rec_room <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6254.0) & (rec_room <= 0) & (air_cond <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6825.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6600.0) & (air_cond <= 0) & (rec_room <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 7085.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (ngarage <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 8250.0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (ngarage <= 1) & (lot_size <= 8050.0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (nbath <= 1) & (lot_size <= 8000.0) =>[p] (sale_price <= 2)",
				"(lot_size <= 5680.0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 2)",
				"(lot_size <= 5500.0) & (air_cond <= 0) & (desire_loc <= 0) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(lot_size <= 5948.0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 2)",
				"(lot_size <= 5828.0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) =>[p] (sale_price <= 2)",
				"(lot_size <= 5800.0) & (ngarage <= 0) & (desire_loc <= 0) & (rec_room <= 0) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(lot_size <= 5960.0) & (nbath <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (ngarage <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (ngarage <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (ngarage <= 0) & (basement <= 0) & (nstoreys <= 3) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (lot_size <= 6325.0) & (nstoreys <= 2) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (lot_size <= 6325.0) & (rec_room <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 9000.0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (desire_loc <= 0) & (lot_size <= 7000.0) & (rec_room <= 0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (desire_loc <= 0) & (rec_room <= 0) & (lot_size <= 8100.0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (basement <= 0) & (nstoreys <= 2) & (lot_size <= 9500.0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (lot_size <= 7200.0) & (nstoreys <= 2) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (nstoreys <= 2) & (lot_size <= 8400.0) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 4) =>[p] (sale_price <= 2)",
				"(ngarage <= 0) & (nstoreys <= 2) & (lot_size <= 6600.0) & (air_cond <= 0) & (nbed <= 4) =>[p] (sale_price <= 2)",
				"(ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6000.0) & (nstoreys <= 3) & (rec_room <= 0) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6000.0) & (rec_room <= 0) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6000.0) & (nbed <= 3) & (desire_loc <= 0) & (nbath <= 2) =>[p] (sale_price <= 2)",
				"(air_cond <= 0) & (desire_loc <= 0) & (nbath <= 2) & (nstoreys <= 2) & (nbed <= 4) & (lot_size <= 10500.0) =>[p] (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6660.0) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) & (ngarage <= 1) =>[p] (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6710.0) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 3) & (ngarage <= 1) =>[p] (sale_price <= 2)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "dummy", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //283
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests downward unions and possible rules for "windsor" data set.
	 * Employs DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorDownwardUnionsPossibleRulesDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_MOST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();

		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size <= 2175.0) & (nbed <= 3) & (ngarage <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2747.0) & (nbed <= 3) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 2787.0) & (desire_loc <= 0) & (nbed <= 4) & (nbath <= 2) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nstoreys <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbath <= 1) & (lot_size <= 4960.0) & (basement <= 0) =>[p] (sale_price <= 0)",
				"(drive <= 0) & (nbed <= 3) & (air_cond <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 0)",
				"(nbed <= 2) & (lot_size <= 4040.0) & (desire_loc <= 0) & (ngarage <= 1) =>[p] (sale_price <= 0)",
				"(lot_size <= 3630.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 4) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 3792.0) & (nbath <= 1) & (desire_loc <= 0) & (ngarage <= 0) & (nbed <= 4) & (rec_room <= 0) =>[p] (sale_price <= 0)",
				"(lot_size <= 4920.0) & (nbath <= 1) & (desire_loc <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (lot_size <= 5495.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(nstoreys <= 1) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (lot_size <= 6060.0) & (air_cond <= 0) & (nbed <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 5200.0) & (ngarage <= 0) & (basement <= 0) & (air_cond <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nstoreys <= 3) =>[p] (sale_price <= 0)",
				"(lot_size <= 5400.0) & (ngarage <= 0) & (desire_loc <= 0) & (nbath <= 1) & (air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 5850.0) & (nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(air_cond <= 0) & (basement <= 0) & (desire_loc <= 0) & (nbath <= 1) & (nbed <= 3) & (lot_size <= 9000.0) & (ngarage <= 2) & (rec_room <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 0)",
				"(lot_size <= 3210.0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (nbath <= 1) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (nbed <= 3) & (ngarage <= 0) =>[p] (sale_price <= 1)",
				"(drive <= 0) & (air_cond <= 0) & (nstoreys <= 2) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 3990.0) & (nstoreys <= 1) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4240.0) & (nbath <= 1) & (rec_room <= 0) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(nbed <= 2) & (air_cond <= 0) & (lot_size <= 10360.0) & (basement <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4500.0) & (air_cond <= 0) & (rec_room <= 0) & (nbath <= 2) & (nbed <= 4) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4632.0) & (nbath <= 1) & (ngarage <= 0) & (nstoreys <= 2) & (rec_room <= 0) & (basement <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4600.0) & (basement <= 0) & (nbed <= 3) & (ngarage <= 1) & (nstoreys <= 2) =>[p] (sale_price <= 1)",
				"(lot_size <= 4785.0) & (ngarage <= 0) & (nbath <= 1) & (air_cond <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 4992.0) & (air_cond <= 0) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 5200.0) & (air_cond <= 0) & (basement <= 0) & (ngarage <= 0) & (nstoreys <= 3) & (nbed <= 4) =>[p] (sale_price <= 1)",
				"(lot_size <= 5300.0) & (air_cond <= 0) & (basement <= 0) & (nstoreys <= 2) & (nbed <= 5) & (ngarage <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 8080.0) & (nbath <= 1) & (ngarage <= 2) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 7260.0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(nstoreys <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 8250.0) & (nbath <= 1) & (rec_room <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5948.0) & (nbath <= 1) & (nstoreys <= 2) & (rec_room <= 0) & (nbed <= 3) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(lot_size <= 5960.0) & (air_cond <= 0) & (nbath <= 1) & (ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) =>[p] (sale_price <= 1)",
				"(ngarage <= 0) & (air_cond <= 0) & (basement <= 0) & (lot_size <= 6650.0) & (nstoreys <= 2) & (nbed <= 4) =>[p] (sale_price <= 1)",
				"(ngarage <= 0) & (desire_loc <= 0) & (nstoreys <= 2) & (basement <= 0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(air_cond <= 0) & (basement <= 0) & (nbath <= 1) & (nstoreys <= 2) & (lot_size <= 9500.0) & (nbed <= 3) =>[p] (sale_price <= 1)",
				"(lot_size <= 3990.0) =>[p] (sale_price <= 2)",
				"(nbed <= 2) & (lot_size <= 9166.0) & (nbath <= 1) =>[p] (sale_price <= 2)",
				"(lot_size <= 5400.0) & (air_cond <= 0) & (nbed <= 5) =>[p] (sale_price <= 2)",
				"(lot_size <= 5400.0) & (nstoreys <= 2) & (basement <= 0) & (nbed <= 4) & (rec_room <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (basement <= 0) & (lot_size <= 12944.0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6450.0) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(nstoreys <= 1) & (lot_size <= 6254.0) & (nbed <= 3) & (rec_room <= 0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (ngarage <= 0) & (desire_loc <= 0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (ngarage <= 0) & (basement <= 0) & (nstoreys <= 3) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (lot_size <= 6325.0) & (nstoreys <= 2) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (desire_loc <= 0) & (air_cond <= 0) & (lot_size <= 9000.0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (desire_loc <= 0) & (rec_room <= 0) & (lot_size <= 8100.0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (basement <= 0) & (nstoreys <= 2) & (lot_size <= 9500.0) =>[p] (sale_price <= 2)",
				"(nbath <= 1) & (nstoreys <= 2) & (lot_size <= 8400.0) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(ngarage <= 0) & (nstoreys <= 2) & (desire_loc <= 0) & (nbed <= 4) =>[p] (sale_price <= 2)",
				"(ngarage <= 0) & (desire_loc <= 0) & (lot_size <= 6000.0) & (rec_room <= 0) & (nbed <= 3) =>[p] (sale_price <= 2)",
				"(air_cond <= 0) & (desire_loc <= 0) & (nbath <= 2) & (nstoreys <= 2) & (nbed <= 4) & (lot_size <= 10500.0) =>[p] (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6660.0) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 4) & (ngarage <= 1) =>[p] (sale_price <= 2)",
				"(air_cond <= 0) & (lot_size <= 6710.0) & (nstoreys <= 2) & (nbath <= 2) & (nbed <= 3) & (ngarage <= 1) =>[p] (sale_price <= 2)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "pruning", "!mv", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //57
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values.
	 * Employs DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV2UpwardUnionsCertainRulesDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(nbed >= 6) & (nstoreys >= 4) => (sale_price >= 3)",
				"(nbed >= 6) & (nbath >= 2) & (lot_size >= 4000.0) => (sale_price >= 3)",
				"(ngarage >= 3) & (lot_size >= 8960.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(ngarage >= 3) & (air_cond >= 1) & (lot_size >= 7200.0) => (sale_price >= 3)",
				"(nbed >= 5) & (ngarage >= 2) => (sale_price >= 3)",
				"(nbed >= 5) & (nbath >= 3) => (sale_price >= 3)",
				"(nbed >= 5) & (lot_size >= 6840.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 13200.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 13200.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 15600.0) & (ngarage >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 11440.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 11460.0) & (desire_loc >= 1) & (ngarage >= 2) => (sale_price >= 3)",
				"(lot_size >= 11175.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nbath >= 3) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(nbath >= 3) & (nbed >= 4) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(nbath >= 3) & (rec_room >= 1) & (lot_size >= 5960.0) & (ngarage >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 10500.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 8500.0) & (nbed >= 3) => (sale_price >= 3)",
				"(nstoreys >= 4) & (desire_loc >= 1) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (desire_loc >= 1) & (lot_size >= 7440.0) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) & (lot_size >= 7680.0) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) & (air_cond >= 1) & (ngarage >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 7475.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 7000.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (ngarage >= 2) & (lot_size >= 6000.0) & (nbath >= 2) & (nbed >= 4) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbed >= 4) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 6525.0) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 3)",
				"(lot_size >= 9960.0) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 9620.0) & (ngarage >= 2) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 8880.0) & (air_cond >= 1) & (nbath >= 2) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 8372.0) & (nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 8250.0) & (nstoreys >= 3) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 7420.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6670.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (lot_size >= 6360.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6500.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 7440.0) & (rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 7155.0) & (nbath >= 2) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbed >= 4) & (basement >= 1) & (air_cond >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 6600.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) & (lot_size >= 4560.0) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 6550.0) & (nbath >= 2) & (air_cond >= 1) & (desire_loc >= 1) => (sale_price >= 3)",
				"(desire_loc >= 1) & (nbed >= 4) & (air_cond >= 1) & (lot_size >= 5450.0) => (sale_price >= 3)",
				"(desire_loc >= 1) & (nbed >= 4) & (air_cond >= 1) & (nbath >= 2) & (nstoreys >= 2) => (sale_price >= 3)",
				"(desire_loc >= 1) & (nbath >= 2) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(nbed >= 6) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbed >= 6) & (nbath >= 2) & (lot_size >= 4300.0) => (sale_price >= 2)",
				"(nbath >= 4) & (air_cond >= 1) => (sale_price >= 2)",
				"(ngarage >= 3) & (air_cond >= 1) => (sale_price >= 2)",
				"(ngarage >= 3) & (nstoreys >= 4) => (sale_price >= 2)",
				"(nbed >= 5) & (rec_room >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (ngarage >= 2) => (sale_price >= 2)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 2)",
				"(nbed >= 5) & (nbath >= 3) => (sale_price >= 2)",
				"(nstoreys >= 4) & (rec_room >= 1) => (sale_price >= 2)",
				"(nstoreys >= 4) & (desire_loc >= 1) => (sale_price >= 2)",
				"(nstoreys >= 4) & (lot_size >= 8500.0) => (sale_price >= 2)",
				"(nstoreys >= 4) & (nbath >= 2) & (lot_size >= 5150.0) => (sale_price >= 2)",
				"(nstoreys >= 4) & (ngarage >= 2) & (lot_size >= 7000.0) => (sale_price >= 2)",
				"(nstoreys >= 4) & (air_cond >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(nbath >= 3) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbath >= 3) & (ngarage >= 2) & (nbed >= 4) => (sale_price >= 2)",
				"(nbath >= 3) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(lot_size >= 13200.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 13200.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 15600.0) & (ngarage >= 2) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (ngarage >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (ngarage >= 2) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 10240.0) & (ngarage >= 2) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 9166.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 8520.0) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 8372.0) & (rec_room >= 1) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 8372.0) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(nstoreys >= 3) & (rec_room >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) & (lot_size >= 6000.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 8150.0) & (rec_room >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 7410.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) & (ngarage >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6825.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6900.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 5136.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6710.0) & (basement >= 1) & (desire_loc >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 5500.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (ngarage >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (ngarage >= 2) & (nstoreys >= 2) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7600.0) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 7320.0) & (nbath >= 2) & (nbed >= 4) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (air_cond >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (nbath >= 2) & (lot_size >= 6600.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 6050.0) & (basement >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 6420.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(lot_size >= 6540.0) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (air_cond >= 1) & (lot_size >= 4510.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 6000.0) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (lot_size >= 4000.0) & (drive >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4995.0) & (basement >= 1) & (nbed >= 4) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 4510.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 6321.0) & (air_cond >= 1) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4950.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4260.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (desire_loc >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (ngarage >= 2) => (sale_price >= 1)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 1)",
				"(nbed >= 5) & (nbath >= 3) => (sale_price >= 1)",
				"(nbed >= 5) & (lot_size >= 4300.0) & (nbath >= 2) => (sale_price >= 1)",
				"(ngarage >= 3) & (lot_size >= 5400.0) => (sale_price >= 1)",
				"(nstoreys >= 4) & (lot_size >= 4500.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 5320.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (rec_room >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(desire_loc >= 1) & (ngarage >= 1) & (lot_size >= 3400.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (nstoreys >= 3) & (lot_size >= 2856.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 4320.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) & (basement >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 7155.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nstoreys >= 3) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 6450.0) & (nbed >= 3) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 6800.0) & (basement >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (ngarage >= 2) & (lot_size >= 4075.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 4) & (nbath >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbath >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 5680.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 5800.0) & (air_cond >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbath >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 9166.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 10500.0) & (nbath >= 2) & (nstoreys >= 2) => (sale_price >= 1)",
				"(nstoreys >= 3) & (basement >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (lot_size >= 6000.0) & (drive >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 8520.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(nbath >= 3) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbath >= 3) & (ngarage >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(nbath >= 3) & (basement >= 1) & (lot_size >= 3300.0) => (sale_price >= 1)",
				"(lot_size >= 8080.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (air_cond >= 1) & (lot_size >= 3816.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 1)",
				"(ngarage >= 2) & (nbath >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(ngarage >= 2) & (nbath >= 2) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 6000.0) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 4320.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 7152.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 7320.0) & (nbath >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(nbath >= 2) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) & (basement >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (ngarage >= 1) & (nbed >= 3) & (basement >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (basement >= 1) & (lot_size >= 3640.0) & (nbed >= 3) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5885.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 6480.0) & (nbed >= 3) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4950.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (basement >= 1) & (nbed >= 4) & (lot_size >= 4260.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nbed >= 4) & (lot_size >= 4632.0) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "dummy", "mv2", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //179
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values.
	 * Employs DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV2UpwardUnionsCertainRulesDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(nbed >= 6) & (nbath >= 2) & (lot_size >= 4000.0) => (sale_price >= 3)",
				"(ngarage >= 3) & (air_cond >= 1) & (lot_size >= 7200.0) => (sale_price >= 3)",
				"(nbed >= 5) & (lot_size >= 6840.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 15600.0) & (ngarage >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 11460.0) & (desire_loc >= 1) & (ngarage >= 2) => (sale_price >= 3)",
				"(nbath >= 3) & (nbed >= 4) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(nbath >= 3) & (rec_room >= 1) & (ngarage >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 10500.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) & (air_cond >= 1) & (ngarage >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (ngarage >= 2) & (lot_size >= 6000.0) & (nbath >= 2) & (nbed >= 4) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbed >= 4) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 9620.0) & (ngarage >= 2) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 8372.0) & (nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 7420.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6670.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6500.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(desire_loc >= 1) & (air_cond >= 1) & (nbath >= 2) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(nbed >= 6) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 2)",
				"(nstoreys >= 4) & (air_cond >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(nbath >= 3) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(lot_size >= 8372.0) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(nstoreys >= 3) & (rec_room >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) & (lot_size >= 6000.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 7410.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 5136.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5500.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (ngarage >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (ngarage >= 2) & (nstoreys >= 2) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7320.0) & (nbath >= 2) & (nbed >= 4) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 6420.0) & (air_cond >= 1) => (sale_price >= 2)",
				"(ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (air_cond >= 1) & (lot_size >= 4510.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 6000.0) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (lot_size >= 4000.0) & (drive >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 4510.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4950.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4260.0) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (lot_size >= 4300.0) & (nbath >= 2) => (sale_price >= 1)",
				"(ngarage >= 3) & (lot_size >= 5400.0) => (sale_price >= 1)",
				"(nstoreys >= 4) & (lot_size >= 4500.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (rec_room >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(desire_loc >= 1) & (ngarage >= 1) & (lot_size >= 3400.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 4320.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) & (basement >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 6450.0) & (nbed >= 3) => (sale_price >= 1)",
				"(rec_room >= 1) & (ngarage >= 2) & (lot_size >= 4075.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nbath >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbath >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (basement >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 1)",
				"(nbath >= 3) & (basement >= 1) & (lot_size >= 3300.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (air_cond >= 1) & (lot_size >= 3816.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (nbath >= 2) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 4320.0) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 7320.0) & (nbath >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (basement >= 1) & (lot_size >= 3640.0) & (nbed >= 3) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4950.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (nbed >= 4) & (lot_size >= 4260.0) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "pruning", "mv2", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //75
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values.
	 * Employs VC-DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV2UpwardUnionsCertainRulesVCDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		RuleMLBuilder ruleMLBuilder = new RuleMLBuilder();
		
		System.out.println("-----*****-----");
		System.out.println(ruleMLBuilder.toRuleMLString(ruleSet, 1)); //prints content of windsor-mv-certain.rules.xml
		System.out.println("-----*****-----");
		
		String[] expectedRules = {
				"(nbed >= 6) & (ngarage >= 2) => (sale_price >= 3)",
				"(nbed >= 6) & (nstoreys >= 4) => (sale_price >= 3)",
				"(nbed >= 6) & (nbath >= 2) & (lot_size >= 4000.0) => (sale_price >= 3)",
				"(ngarage >= 3) & (lot_size >= 8960.0) => (sale_price >= 3)",
				"(ngarage >= 3) & (air_cond >= 1) & (lot_size >= 7200.0) => (sale_price >= 3)",
				"(nbed >= 5) & (ngarage >= 2) => (sale_price >= 3)",
				"(nbed >= 5) & (nbath >= 3) => (sale_price >= 3)",
				"(nbed >= 5) & (lot_size >= 6840.0) => (sale_price >= 3)",
				"(lot_size >= 13200.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 13200.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 13200.0) & (ngarage >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 13200.0) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(lot_size >= 11440.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 11175.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nbath >= 3) & (air_cond >= 1) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(nbath >= 3) & (nbed >= 4) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(nbath >= 3) & (rec_room >= 1) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(nbath >= 3) & (rec_room >= 1) & (lot_size >= 5960.0) => (sale_price >= 3)",
				"(lot_size >= 10500.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 8500.0) & (nbed >= 3) => (sale_price >= 3)",
				"(nstoreys >= 4) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (rec_room >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 7475.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 7000.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 7000.0) & (nbed >= 3) => (sale_price >= 3)",
				"(nstoreys >= 4) & (ngarage >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (ngarage >= 2) & (basement >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (nbed >= 4) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 6325.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (lot_size >= 6325.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 4) & (air_cond >= 1) & (lot_size >= 4500.0) => (sale_price >= 3)",
				"(lot_size >= 9960.0) & (nbath >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(lot_size >= 9620.0) & (ngarage >= 2) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 8800.0) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(lot_size >= 8875.0) & (air_cond >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(lot_size >= 8875.0) & (ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 3)",
				"(lot_size >= 8372.0) & (nstoreys >= 3) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 8250.0) & (nstoreys >= 3) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 7420.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (rec_room >= 1) & (nbed >= 4) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6670.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (lot_size >= 6360.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (nbath >= 2) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (nbed >= 4) & (ngarage >= 1) & (lot_size >= 3240.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (desire_loc >= 1) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (desire_loc >= 1) & (ngarage >= 1) & (lot_size >= 5500.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (air_cond >= 1) & (lot_size >= 5200.0) => (sale_price >= 3)",
				"(lot_size >= 8000.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7440.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7410.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7320.0) & (nbath >= 2) & (nbed >= 4) & (drive >= 1) => (sale_price >= 3)",
				"(lot_size >= 7231.0) & (rec_room >= 1) => (sale_price >= 3)",
				"(lot_size >= 7155.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(lot_size >= 7160.0) & (desire_loc >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 6900.0) & (nbath >= 2) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 6800.0) & (ngarage >= 2) & (basement >= 1) => (sale_price >= 3)",
				"(lot_size >= 6750.0) & (rec_room >= 1) & (ngarage >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (nbed >= 4) & (basement >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (lot_size >= 6540.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 2) & (desire_loc >= 1) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 6600.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (air_cond >= 1) & (lot_size >= 3700.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 6400.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) & (lot_size >= 4560.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 1) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 4800.0) & (basement >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 6550.0) & (nbath >= 2) & (air_cond >= 1) => (sale_price >= 3)",
				"(desire_loc >= 1) & (nbed >= 4) & (air_cond >= 1) & (lot_size >= 5450.0) => (sale_price >= 3)",
				"(desire_loc >= 1) & (nbed >= 4) & (air_cond >= 1) & (nbath >= 2) & (nstoreys >= 2) => (sale_price >= 3)",
				"(desire_loc >= 1) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 3)",
				"(desire_loc >= 1) & (nbath >= 2) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 3)",
				"(desire_loc >= 1) & (lot_size >= 6420.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(ngarage >= 2) & (nbed >= 4) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(ngarage >= 2) & (nbed >= 4) & (nbath >= 2) & (lot_size >= 3500.0) & (drive >= 1) => (sale_price >= 3)",
				"(ngarage >= 2) & (basement >= 1) & (nstoreys >= 2) & (lot_size >= 4320.0) => (sale_price >= 3)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3760.0) & (drive >= 1) => (sale_price >= 3)",
				"(nbed >= 4) & (nbath >= 2) & (drive >= 1) & (lot_size >= 4260.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(nbath >= 2) & (ngarage >= 1) & (basement >= 1) & (lot_size >= 4300.0) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 3)",
				"(lot_size >= 6000.0) & (ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 3)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4040.0) & (drive >= 1) => (sale_price >= 3)",
				"(nbed >= 6) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbed >= 6) & (rec_room >= 1) => (sale_price >= 2)",
				"(nbed >= 6) & (ngarage >= 2) => (sale_price >= 2)",
				"(nbed >= 6) & (nbath >= 2) & (lot_size >= 4300.0) => (sale_price >= 2)",
				"(nbath >= 4) & (air_cond >= 1) => (sale_price >= 2)",
				"(ngarage >= 3) & (air_cond >= 1) => (sale_price >= 2)",
				"(ngarage >= 3) & (nstoreys >= 4) => (sale_price >= 2)",
				"(nbed >= 5) & (rec_room >= 1) => (sale_price >= 2)",
				"(nbed >= 5) & (ngarage >= 2) => (sale_price >= 2)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 2)",
				"(nbed >= 5) & (nbath >= 3) => (sale_price >= 2)",
				"(nstoreys >= 4) & (rec_room >= 1) => (sale_price >= 2)",
				"(nstoreys >= 4) & (desire_loc >= 1) => (sale_price >= 2)",
				"(nstoreys >= 4) & (lot_size >= 8500.0) => (sale_price >= 2)",
				"(nstoreys >= 4) & (nbath >= 2) => (sale_price >= 2)",
				"(nstoreys >= 4) & (ngarage >= 2) => (sale_price >= 2)",
				"(nstoreys >= 4) & (air_cond >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(nstoreys >= 4) & (air_cond >= 1) & (lot_size >= 4500.0) => (sale_price >= 2)",
				"(nstoreys >= 4) & (lot_size >= 5000.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nbath >= 3) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbath >= 3) & (ngarage >= 2) => (sale_price >= 2)",
				"(nbath >= 3) & (lot_size >= 6720.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nbath >= 3) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 13200.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 13200.0) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 13200.0) & (ngarage >= 2) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 13200.0) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 10500.0) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 9620.0) & (ngarage >= 2) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 9860.0) & (ngarage >= 2) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 9860.0) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 9860.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 9166.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 8520.0) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(lot_size >= 8875.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 8875.0) & (ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 8372.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 8372.0) & (air_cond >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(nstoreys >= 3) & (rec_room >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) => (sale_price >= 2)",
				"(nstoreys >= 3) & (air_cond >= 1) & (lot_size >= 4800.0) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (basement >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbed >= 4) & (ngarage >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 5500.0) & (desire_loc >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (lot_size >= 3510.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbed >= 3) & (lot_size >= 2970.0) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 8150.0) & (rec_room >= 1) => (sale_price >= 2)",
				"(lot_size >= 8250.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 7410.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6825.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 5136.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (air_cond >= 1) & (lot_size >= 4095.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6710.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6450.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 5500.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (ngarage >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 2870.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 6000.0) & (ngarage >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbath >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (ngarage >= 2) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 4800.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (lot_size >= 4770.0) & (basement >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (basement >= 1) & (drive >= 1) & (lot_size >= 3750.0) => (sale_price >= 2)",
				"(lot_size >= 7800.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 7980.0) & (desire_loc >= 1) => (sale_price >= 2)",
				"(lot_size >= 7600.0) & (air_cond >= 1) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 7320.0) & (nbath >= 2) & (nbed >= 4) & (drive >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (air_cond >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (nbath >= 2) & (ngarage >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (nbed >= 4) & (nbath >= 2) & (lot_size >= 6600.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5720.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 5360.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (ngarage >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 4320.0) & (nbed >= 3) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 4040.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (ngarage >= 1) & (lot_size >= 3840.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nbed >= 3) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 2880.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(lot_size >= 7000.0) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 6540.0) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (air_cond >= 1) & (nbath >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (air_cond >= 1) & (lot_size >= 4510.0) => (sale_price >= 2)",
				"(ngarage >= 2) & (nbed >= 4) => (sale_price >= 2)",
				"(ngarage >= 2) & (nbath >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (basement >= 1) => (sale_price >= 2)",
				"(ngarage >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 3760.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nbath >= 2) & (air_cond >= 1) & (lot_size >= 4000.0) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 5500.0) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4995.0) & (basement >= 1) & (nbed >= 4) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4920.0) & (ngarage >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 4510.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 3420.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (lot_size >= 4300.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 6321.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4950.0) => (sale_price >= 2)",
				"(nbed >= 4) & (air_cond >= 1) & (lot_size >= 4260.0) => (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 5400.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (ngarage >= 1) & (lot_size >= 4640.0) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (air_cond >= 1) & (basement >= 1) => (sale_price >= 2)",
				"(lot_size >= 6000.0) & (ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5885.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5885.0) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 4520.0) & (basement >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3640.0) => (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3410.0) => (sale_price >= 2)",
				"(lot_size >= 4900.0) & (basement >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4350.0) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4040.0) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 3745.0) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 3000.0) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (drive >= 1) & (lot_size >= 2550.0) & (nbed >= 3) => (sale_price >= 2)",
				"(ngarage >= 1) & (lot_size >= 4200.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(nbed >= 5) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (rec_room >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (desire_loc >= 1) => (sale_price >= 1)",
				"(nbed >= 5) & (ngarage >= 2) => (sale_price >= 1)",
				"(nbed >= 5) & (nstoreys >= 3) => (sale_price >= 1)",
				"(nbed >= 5) & (nbath >= 3) => (sale_price >= 1)",
				"(nbed >= 5) & (lot_size >= 4300.0) => (sale_price >= 1)",
				"(nbed >= 5) & (basement >= 1) => (sale_price >= 1)",
				"(ngarage >= 3) & (lot_size >= 4815.0) => (sale_price >= 1)",
				"(nbed >= 6) & (ngarage >= 1) => (sale_price >= 1)",
				"(nstoreys >= 4) & (lot_size >= 4500.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 5320.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (rec_room >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(desire_loc >= 1) & (ngarage >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (air_cond >= 1) => (sale_price >= 1)",
				"(desire_loc >= 1) & (nstoreys >= 3) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 4000.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 7155.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nstoreys >= 3) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 6450.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (ngarage >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbath >= 3) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 4800.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (air_cond >= 1) => (sale_price >= 1)",
				"(rec_room >= 1) & (nbath >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 4770.0) => (sale_price >= 1)",
				"(rec_room >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 3750.0) => (sale_price >= 1)",
				"(lot_size >= 9166.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 9860.0) & (nbath >= 2) => (sale_price >= 1)",
				"(lot_size >= 9860.0) & (ngarage >= 2) => (sale_price >= 1)",
				"(lot_size >= 9860.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 9860.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 9860.0) & (nbed >= 4) => (sale_price >= 1)",
				"(lot_size >= 9860.0) & (drive >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (basement >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (lot_size >= 6000.0) => (sale_price >= 1)",
				"(nstoreys >= 3) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (nbath >= 2) => (sale_price >= 1)",
				"(nstoreys >= 3) & (nbed >= 4) => (sale_price >= 1)",
				"(nstoreys >= 3) & (lot_size >= 4079.0) & (drive >= 1) => (sale_price >= 1)",
				"(nstoreys >= 3) & (lot_size >= 3510.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 8250.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 8250.0) & (ngarage >= 2) => (sale_price >= 1)",
				"(lot_size >= 8250.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 8250.0) & (drive >= 1) => (sale_price >= 1)",
				"(nbath >= 3) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbath >= 3) & (ngarage >= 2) => (sale_price >= 1)",
				"(nbath >= 3) & (basement >= 1) => (sale_price >= 1)",
				"(nbath >= 3) & (nbed >= 4) => (sale_price >= 1)",
				"(lot_size >= 8080.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 7770.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (air_cond >= 1) => (sale_price >= 1)",
				"(ngarage >= 2) & (nbath >= 2) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 5800.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 4320.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 3760.0) => (sale_price >= 1)",
				"(ngarage >= 2) & (lot_size >= 3630.0) => (sale_price >= 1)",
				"(lot_size >= 6930.0) & (air_cond >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 6930.0) & (nbath >= 2) => (sale_price >= 1)",
				"(lot_size >= 6930.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 6930.0) & (nbed >= 4) => (sale_price >= 1)",
				"(lot_size >= 7424.0) & (nbed >= 3) => (sale_price >= 1)",
				"(nbath >= 2) & (air_cond >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 4) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 6720.0) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 5500.0) & (nbed >= 3) => (sale_price >= 1)",
				"(nbath >= 2) & (lot_size >= 4920.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (ngarage >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (basement >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 3) & (lot_size >= 3680.0) & (drive >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(nbath >= 2) & (nbed >= 3) & (lot_size >= 2135.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5885.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 6480.0) & (nbed >= 3) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4950.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (ngarage >= 1) & (lot_size >= 3120.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (basement >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (nbed >= 4) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 4240.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3640.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3410.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3480.0) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 6360.0) & (nbed >= 4) => (sale_price >= 1)",
				"(lot_size >= 6360.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(lot_size >= 6360.0) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 6020.0) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 6020.0) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 5985.0) & (basement >= 1) & (drive >= 1) => (sale_price >= 1)",
				"(nbed >= 4) & (basement >= 1) => (sale_price >= 1)",
				"(nbed >= 4) & (lot_size >= 5400.0) => (sale_price >= 1)",
				"(nbed >= 4) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 4900.0) & (basement >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(lot_size >= 4900.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 4350.0) & (nbed >= 3) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 4350.0) & (drive >= 1) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 4032.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 4032.0) & (drive >= 1) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3960.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3745.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 3570.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 3000.0) => (sale_price >= 1)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 2550.0) => (sale_price >= 1)",
				"(lot_size >= 4410.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(lot_size >= 4400.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 4200.0) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 4040.0) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 4000.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 3640.0) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 3600.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (nstoreys >= 2) & (drive >= 1) & (lot_size >= 3420.0) => (sale_price >= 1)",
				"(lot_size >= 4340.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 3990.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 3900.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "dummy", "mv2", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //334
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values.
	 * Employs VC-DRSA.
	 * Uses all pruners and rule minimality checker.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV2UpwardUnionsCertainRulesVCDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		String[] expectedRules = {
				"(lot_size >= 13200.0) & (basement >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(lot_size >= 11440.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 4) & (air_cond >= 1) & (lot_size >= 4500.0) => (sale_price >= 3)",
				"(lot_size >= 8875.0) & (air_cond >= 1) & (ngarage >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (lot_size >= 6420.0) & (desire_loc >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (nbed >= 4) & (ngarage >= 1) & (lot_size >= 3240.0) => (sale_price >= 3)",
				"(nstoreys >= 3) & (desire_loc >= 1) & (air_cond >= 1) => (sale_price >= 3)",
				"(nstoreys >= 3) & (desire_loc >= 1) & (ngarage >= 1) & (lot_size >= 5500.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (nbed >= 4) & (lot_size >= 3700.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(rec_room >= 1) & (ngarage >= 1) & (lot_size >= 6000.0) => (sale_price >= 3)",
				"(rec_room >= 1) & (lot_size >= 4800.0) & (basement >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(desire_loc >= 1) & (lot_size >= 6420.0) & (air_cond >= 1) => (sale_price >= 3)",
				"(ngarage >= 2) & (nbed >= 4) & (nbath >= 2) & (lot_size >= 3500.0) & (drive >= 1) => (sale_price >= 3)",
				"(ngarage >= 2) & (nstoreys >= 2) & (lot_size >= 3760.0) & (drive >= 1) => (sale_price >= 3)",
				"(nbed >= 4) & (nbath >= 2) & (drive >= 1) & (lot_size >= 4260.0) & (nstoreys >= 2) => (sale_price >= 3)",
				"(nbath >= 2) & (air_cond >= 1) & (basement >= 1) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(lot_size >= 6000.0) & (ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 3)",
				"(air_cond >= 1) & (lot_size >= 5000.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 3)",
				"(basement >= 1) & (ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4040.0) & (drive >= 1) => (sale_price >= 3)",
				"(nbed >= 6) & (air_cond >= 1) => (sale_price >= 2)",
				"(nbath >= 3) & (lot_size >= 6720.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbath >= 2) => (sale_price >= 2)",
				"(nstoreys >= 3) & (nbed >= 3) & (lot_size >= 2970.0) & (drive >= 1) => (sale_price >= 2)",
				"(lot_size >= 8250.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 2)",
				"(rec_room >= 1) & (nbed >= 4) => (sale_price >= 2)",
				"(rec_room >= 1) & (desire_loc >= 1) & (lot_size >= 2870.0) => (sale_price >= 2)",
				"(rec_room >= 1) & (basement >= 1) & (lot_size >= 3750.0) => (sale_price >= 2)",
				"(desire_loc >= 1) & (air_cond >= 1) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 3520.0) & (nbed >= 3) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 2880.0) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (nstoreys >= 2) => (sale_price >= 2)",
				"(ngarage >= 2) & (lot_size >= 3760.0) & (nbed >= 3) => (sale_price >= 2)",
				"(nbath >= 2) & (ngarage >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(nbath >= 2) & (nbed >= 4) & (lot_size >= 3420.0) & (drive >= 1) => (sale_price >= 2)",
				"(nbed >= 4) & (lot_size >= 5400.0) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (lot_size >= 5885.0) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (basement >= 1) & (drive >= 1) => (sale_price >= 2)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3410.0) => (sale_price >= 2)",
				"(basement >= 1) & (lot_size >= 4350.0) & (drive >= 1) => (sale_price >= 2)",
				"(basement >= 1) & (nstoreys >= 2) & (drive >= 1) & (lot_size >= 2550.0) & (nbed >= 3) => (sale_price >= 2)",
				"(ngarage >= 1) & (nstoreys >= 2) & (lot_size >= 4000.0) & (drive >= 1) & (nbed >= 3) => (sale_price >= 2)",
				"(desire_loc >= 1) & (lot_size >= 2787.0) => (sale_price >= 1)",
				"(desire_loc >= 1) & (nstoreys >= 2) => (sale_price >= 1)",
				"(rec_room >= 1) & (lot_size >= 3750.0) => (sale_price >= 1)",
				"(nstoreys >= 3) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 5500.0) & (nbed >= 3) => (sale_price >= 1)",
				"(nbath >= 2) & (basement >= 1) => (sale_price >= 1)",
				"(nbath >= 2) & (nstoreys >= 2) => (sale_price >= 1)",
				"(air_cond >= 1) & (basement >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) & (lot_size >= 3410.0) => (sale_price >= 1)",
				"(air_cond >= 1) & (nstoreys >= 2) & (drive >= 1) => (sale_price >= 1)",
				"(air_cond >= 1) & (lot_size >= 3480.0) & (nbed >= 3) => (sale_price >= 1)",
				"(lot_size >= 6360.0) & (drive >= 1) => (sale_price >= 1)",
				"(nbed >= 4) & (basement >= 1) => (sale_price >= 1)",
				"(lot_size >= 4900.0) & (nstoreys >= 2) => (sale_price >= 1)",
				"(basement >= 1) & (lot_size >= 4032.0) & (drive >= 1) => (sale_price >= 1)",
				"(basement >= 1) & (nstoreys >= 2) & (lot_size >= 2550.0) => (sale_price >= 1)",
				"(lot_size >= 4400.0) & (ngarage >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (lot_size >= 3640.0) & (drive >= 1) => (sale_price >= 1)",
				"(ngarage >= 1) & (nstoreys >= 2) & (drive >= 1) & (lot_size >= 3420.0) => (sale_price >= 1)",
				"(lot_size >= 4340.0) & (nbed >= 3) & (drive >= 1) => (sale_price >= 1)",
				"(lot_size >= 3900.0) & (nstoreys >= 2) & (drive >= 1) & (nbed >= 3) => (sale_price >= 1)"
		}; //rules generated for the same settings using jRS
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "pruning", "mv2", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		assertEquals(ruleSet.size(), expectedRules.length); //63
		
		for (int i = 0; i < expectedRules.length; i++) {
			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values of type mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * Employs DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 * This test just verifies if no exception is thrown during induction of rules.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV15UpwardUnionsCertainRulesDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor-mv1.5.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
//		String[] expectedRules = {
//				
//		};
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "dummy", "mv1.5", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}

		//TODO: add assertions
		
//		assertEquals(ruleSet.size(), expectedRules.length);
//		
//		for (int i = 0; i < expectedRules.length; i++) {
//			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
//		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values of type mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * Employs DRSA.
	 * Uses all pruners and rule minimality checker.
	 * This test just verifies if no exception is thrown during induction of rules.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV15UpwardUnionsCertainRulesDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor-mv1.5.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
//		String[] expectedRules = {
//				
//		};
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "pruning", "mv1.5", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		//TODO: add assertions
		
//		assertEquals(ruleSet.size(), expectedRules.length);
//		
//		for (int i = 0; i < expectedRules.length; i++) {
//			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
//		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values of type mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * Employs VC-DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 * This test just verifies if no exception is thrown during induction of rules.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV15UpwardUnionsCertainRulesVCDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor-mv1.5.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
//		String[] expectedRules = {
//				
//		};
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "dummy", "mv1.5", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		//TODO: add assertions
		
//		assertEquals(ruleSet.size(), expectedRules.length);
//		
//		for (int i = 0; i < expectedRules.length; i++) {
//			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
//		}
	}
	
	/**
	 * Tests upward unions and certain rules for "windsor" data set with missing values of type mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * Employs VC-DRSA.
	 * Uses all pruners and rule minimality checker.
	 * This test just verifies if no exception is thrown during induction of rules.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV15UpwardUnionsCertainRulesVCDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor-mv1.5.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		final double consistencyThreshold = 0.1;
		
		final RuleInductionStoppingConditionChecker stoppingConditionChecker = 
				new EvaluationAndCoverageStoppingConditionChecker(EpsilonConsistencyMeasure.getInstance(), EpsilonConsistencyMeasure.getInstance(), consistencyThreshold);
		
		RuleInducerComponents ruleInducerComponents = new CertainRuleInducerComponents.Builder().
				ruleInductionStoppingConditionChecker(stoppingConditionChecker).
				ruleConditionsPruner(new AttributeOrderRuleConditionsPruner(stoppingConditionChecker)).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), consistencyThreshold)));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();
		
		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
//		String[] expectedRules = {
//				
//		};
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "VC-DRSA", "pruning", "mv1.5", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		//TODO: add assertions
		
//		assertEquals(ruleSet.size(), expectedRules.length);
//		
//		for (int i = 0; i < expectedRules.length; i++) {
//			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
//		}
	}
	
	/**
	 * Tests upward unions and possible rules for "windsor" data set with missing values of type mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * Employs DRSA.
	 * Uses dummy pruners and dummy rule minimality checker.
	 * This test just verifies if no exception is thrown during induction of rules.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV15UpwardUnionsPossibleRulesDRSADummy() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor-mv1.5.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().
				ruleConditionsPruner(new DummyRuleConditionsPruner()).
				ruleConditionsSetPruner(new DummyRuleConditionsSetPruner()).
				ruleMinimalityChecker(new DummyRuleMinimalityChecker()).
				build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();

		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
		RuleMLBuilder ruleMLBuilder = new RuleMLBuilder();
		System.out.println("-----+++++-----");
		System.out.println(ruleMLBuilder.toRuleMLString(ruleSet, 1)); //prints content of windsor-mv1.5-possible.rules.xml
		System.out.println("-----+++++-----");
		
//		String[] expectedRules = {
//				
//		};
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "dummy", "mv1.5", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		//TODO: add assertions
		
//		assertEquals(ruleSet.size(), expectedRules.length);
//		
//		for (int i = 0; i < expectedRules.length; i++) {
//			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
//		}
	}
	
	/**
	 * Tests upward unions and possible rules for "windsor" data set with missing values of type mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * Employs DRSA.
	 * Uses all pruners and rule minimality checker.
	 * This test just verifies if no exception is thrown during induction of rules.
	 */
	@Test
	@Tag("integration")
	public void testWindsorMV15UpwardUnionsPossibleRulesDRSAPruning() {
		InformationTableWithDecisionDistributions informationTable = getInformationTableWindsor("src/test/resources/data/csv/windsor-mv1.5.json", "src/test/resources/data/csv/windsor-mv.csv");
		
		RuleInducerComponents ruleInducerComponents = new PossibleRuleInducerComponents.Builder().build();
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new UnionsWithSingleLimitingDecision(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionWithSingleLimitingDecisionRuleDecisionsProvider();

		RuleSet ruleSet = new VCDomLEM(ruleInducerComponents, approximatedSetProvider, approximatedSetRuleDecisionsProvider).generateRules();
		
//		String[] expectedRules = {
//				
//		};
		
		System.out.println(formatVCDomLEMRunDescription("windsor-mv", ((UnionProvider)approximatedSetProvider).getProvidedUnionType(), ruleInducerComponents.getRuleType(), "DRSA", "pruning", "mv1.5", ruleSet.size())); //DEL
//		for (int i = 0; i < ruleSet.size(); i++) {
//			//System.out.println(ruleSet.getRule(i).toString(true));
//			System.out.println(ruleSet.getRule(i).toString());
//		}
		
		//TODO: add assertions
		
//		assertEquals(ruleSet.size(), expectedRules.length);
//		
//		for (int i = 0; i < expectedRules.length; i++) {
//			assertEquals(ruleSet.getRule(i).toString(), expectedRules[i]);
//		}
	}
	
	private String formatVCDomLEMRunDescription(String datSet, UnionType unionType, RuleType ruleType, String approach, String pruning, String mvType, int rulesCount) {
		//[VC-DomLEM run:] <data> <at-least|at-most> <certain|possible> <DRSA|VC-DRSA> <dummy|pruning> <!mv|mv2|mv1.5> (<#rules> rules)
		return new StringBuilder()
				.append("[VC-DomLEM run] ")
				.append(datSet).append(" ")
				.append(unionType).append(" ")
				.append(ruleType).append(" ")
				.append(approach).append(" ")
				.append(pruning).append(" ")
				.append(mvType).append(" ")
				.append("(").append(rulesCount).append(" rules)")
				.toString();
	}

}
