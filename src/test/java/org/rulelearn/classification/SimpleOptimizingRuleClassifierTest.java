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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.ComputableRuleCharacteristics;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleCoverageInformation;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithComputableCharacteristics;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Tests for {@link SimpleOptimizingRuleClassifier}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleOptimizingRuleClassifierTest {
	
	int atLeastRuleIndex0 = 1;
	int atLeastRuleIndex1 = 3;
	int atMostRuleIndex0 = 5;
	int atMostRuleIndex1 = 9;
	AttributePreferenceType decAttrPrefType = AttributePreferenceType.GAIN;
	int decAttrIndex = 4;
	int ruleSetSize = 10;
	
	@SuppressWarnings("unchecked")
	private RuleSetWithComputableCharacteristics getRuleSetWithComputableCharacteristicsMock() {
		int ruleIndex;
		int[] coveredLearningObjectsIndices;
		Decision[] coveredLearningObjectsDecisions;
		IntList indicesOfCoveredLearningObjects;
		Int2ObjectMap<Decision> decisionsOfCoveredLearningObjects;
		
		RuleSetWithComputableCharacteristics ruleSetMock = Mockito.mock(RuleSetWithComputableCharacteristics.class);
		
		//->->->->->->->->->->
		//rule ">=2" given in javadoc description of tested class
		ruleIndex = atLeastRuleIndex0;
		coveredLearningObjectsIndices = new int[] {1, 2, 5, 17, 19, 20};
		coveredLearningObjectsDecisions = new Decision[] {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decAttrPrefType), decAttrIndex)
		};
		indicesOfCoveredLearningObjects = new IntArrayList();
		decisionsOfCoveredLearningObjects = new Int2ObjectOpenHashMap<Decision>();
		for (int i = 0; i < coveredLearningObjectsIndices.length; i++) {
			indicesOfCoveredLearningObjects.add(coveredLearningObjectsIndices[i]);
			decisionsOfCoveredLearningObjects.put(coveredLearningObjectsIndices[i], coveredLearningObjectsDecisions[i]);
		}
		
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex)).thenReturn(Mockito.mock(ComputableRuleCharacteristics.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation()).thenReturn(Mockito.mock(RuleCoverageInformation.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getIndicesOfCoveredObjects()).thenReturn(indicesOfCoveredLearningObjects);
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getDecisionsOfCoveredObjects()).thenReturn(decisionsOfCoveredLearningObjects);
		//-<-<-<-<-<-<-<-<-<-<
		
		//->->->->->->->->->->
		//rule ">=3" given in javadoc description of tested class
		ruleIndex = atLeastRuleIndex1;
		coveredLearningObjectsIndices =  new int[] {3, 6, 17, 18, 19, 21};
		coveredLearningObjectsDecisions = new Decision[] {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decAttrPrefType), decAttrIndex)
		};
		indicesOfCoveredLearningObjects = new IntArrayList();
		decisionsOfCoveredLearningObjects = new Int2ObjectOpenHashMap<Decision>();
		for (int i = 0; i < coveredLearningObjectsIndices.length; i++) {
			indicesOfCoveredLearningObjects.add(coveredLearningObjectsIndices[i]);
			decisionsOfCoveredLearningObjects.put(coveredLearningObjectsIndices[i], coveredLearningObjectsDecisions[i]);
		}
		
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex)).thenReturn(Mockito.mock(ComputableRuleCharacteristics.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation()).thenReturn(Mockito.mock(RuleCoverageInformation.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getIndicesOfCoveredObjects()).thenReturn(indicesOfCoveredLearningObjects);
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getDecisionsOfCoveredObjects()).thenReturn(decisionsOfCoveredLearningObjects);
		//-<-<-<-<-<-<-<-<-<-<
		
		//->->->->->->->->->->
		ruleIndex = atMostRuleIndex0; //rule "<=1" given in javadoc description of tested class
		coveredLearningObjectsIndices = new int[] {2, 7, 21};
		coveredLearningObjectsDecisions = new Decision[] {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(0, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decAttrPrefType), decAttrIndex)
		};
		indicesOfCoveredLearningObjects = new IntArrayList();
		decisionsOfCoveredLearningObjects = new Int2ObjectOpenHashMap<Decision>();
		for (int i = 0; i < coveredLearningObjectsIndices.length; i++) {
			indicesOfCoveredLearningObjects.add(coveredLearningObjectsIndices[i]);
			decisionsOfCoveredLearningObjects.put(coveredLearningObjectsIndices[i], coveredLearningObjectsDecisions[i]);
		}
		
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex)).thenReturn(Mockito.mock(ComputableRuleCharacteristics.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation()).thenReturn(Mockito.mock(RuleCoverageInformation.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getIndicesOfCoveredObjects()).thenReturn(indicesOfCoveredLearningObjects);
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getDecisionsOfCoveredObjects()).thenReturn(decisionsOfCoveredLearningObjects);
		//-<-<-<-<-<-<-<-<-<-<
		
		//->->->->->->->->->->
		ruleIndex = atMostRuleIndex1; //rule "<=2" given in javadoc description of tested class
		coveredLearningObjectsIndices = new int[] {4, 7, 33};
		coveredLearningObjectsDecisions = new Decision[] {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decAttrPrefType), decAttrIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decAttrPrefType), decAttrIndex)
		};
		indicesOfCoveredLearningObjects = new IntArrayList();
		decisionsOfCoveredLearningObjects = new Int2ObjectOpenHashMap<Decision>();
		for (int i = 0; i < coveredLearningObjectsIndices.length; i++) {
			indicesOfCoveredLearningObjects.add(coveredLearningObjectsIndices[i]);
			decisionsOfCoveredLearningObjects.put(coveredLearningObjectsIndices[i], coveredLearningObjectsDecisions[i]);
		}
		
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex)).thenReturn(Mockito.mock(ComputableRuleCharacteristics.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation()).thenReturn(Mockito.mock(RuleCoverageInformation.class));
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getIndicesOfCoveredObjects()).thenReturn(indicesOfCoveredLearningObjects);
		Mockito.when(ruleSetMock.getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getDecisionsOfCoveredObjects()).thenReturn(decisionsOfCoveredLearningObjects);
		//-<-<-<-<-<-<-<-<-<-<
		
		for (int i = 0; i < ruleSetSize; i++) {
			Mockito.when(ruleSetMock.getRule(i)).thenReturn(Mockito.mock(Rule.class));
		}
		
		Mockito.when(ruleSetMock.size()).thenReturn(ruleSetSize); //10 rules, with their computable characteristics
		
		Mockito.when(ruleSetMock.getRule(0).getDecision()).thenReturn(Mockito.mock(Condition.class));
		Mockito.when(ruleSetMock.getRule(0).getDecision().getAttributeWithContext()).thenReturn(Mockito.mock(EvaluationAttributeWithContext.class));
		Mockito.when(ruleSetMock.getRule(0).getDecision().getAttributeWithContext().getAttributeIndex()).thenReturn(decAttrIndex);
		
		return ruleSetMock;
	}

	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#SimpleOptimizingRuleClassifier(RuleSetWithComputableCharacteristics, SimpleClassificationResult)}.
	 */
	@Test
	void testSimpleOptimizingRuleClassifier01() {
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(Mockito.mock(RuleSetWithComputableCharacteristics.class), Mockito.mock(SimpleClassificationResult.class));
		assertEquals(simpleOptimizingRuleClassifier.hasComputableRuleCharacteristics, true);
		assertNull(simpleOptimizingRuleClassifier.ruleCoverageInformations);
	}

	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#SimpleOptimizingRuleClassifier(RuleSet, SimpleClassificationResult, InformationTable)}.
	 */
	@Test
	void testSimpleOptimizingRuleClassifier02() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(Mockito.mock(RuleSet.class), Mockito.mock(SimpleClassificationResult.class), informationTableMock);
		assertEquals(simpleOptimizingRuleClassifier.hasComputableRuleCharacteristics, false);
		assertNotNull(simpleOptimizingRuleClassifier.ruleCoverageInformations);
	}
	
	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#classify(int, InformationTable)}.
	 * Tests the case when classified object is covered only by at least rules.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testClassify01() {
		int objectIndex = 7;
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		RuleSetWithComputableCharacteristics ruleSetMock = getRuleSetWithComputableCharacteristicsMock();
		SimpleClassificationResult defaultClassificationResultMock = Mockito.mock(SimpleClassificationResult.class);
		
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(ruleSetMock, defaultClassificationResultMock);

		//set that only at least rules cover classified object
		for (int i = 0; i < ruleSetSize; i++) {
			if (i == atLeastRuleIndex0 || i == atLeastRuleIndex1) {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(true);
			} else {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(false);
			}
		}
		
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex0).getDecision()).thenReturn(Mockito.mock(ConditionAtLeast.class));
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex0).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(2, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex1).getDecision()).thenReturn(Mockito.mock(ConditionAtLeast.class));
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex1).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(3, decAttrPrefType));
				
		SimpleClassificationResult simpleClassificationResult = simpleOptimizingRuleClassifier.classify(objectIndex, informationTableMock);
		assertEquals(simpleClassificationResult.getSuggestedDecision(), new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex));
	}
	
	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#classify(int, InformationTable)}.
	 * Tests the case when classified object is covered only by at most rules.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testClassify02() {
		int objectIndex = 7;
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		RuleSetWithComputableCharacteristics ruleSetMock = getRuleSetWithComputableCharacteristicsMock();
		SimpleClassificationResult defaultClassificationResultMock = Mockito.mock(SimpleClassificationResult.class);
		
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(ruleSetMock, defaultClassificationResultMock);

		//set that only at least rules cover classified object
		for (int i = 0; i < ruleSetSize; i++) {
			if (i == atMostRuleIndex0 || i == atMostRuleIndex1) {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(true);
			} else {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(false);
			}
		}
		
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex0).getDecision()).thenReturn(Mockito.mock(ConditionAtMost.class));
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex0).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(1, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex1).getDecision()).thenReturn(Mockito.mock(ConditionAtMost.class));
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex1).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(2, decAttrPrefType));
				
		SimpleClassificationResult simpleClassificationResult = simpleOptimizingRuleClassifier.classify(objectIndex, informationTableMock);
		assertEquals(simpleClassificationResult.getSuggestedDecision(), new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decAttrPrefType), decAttrIndex));
	}
	
	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#classify(int, InformationTable)}.
	 * Tests the case when classified object is covered by both at least and at most rules.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testClassify03() {
		int objectIndex = 7;
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		RuleSetWithComputableCharacteristics ruleSetMock = getRuleSetWithComputableCharacteristicsMock();
		SimpleClassificationResult defaultClassificationResultMock = Mockito.mock(SimpleClassificationResult.class);
		
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(ruleSetMock, defaultClassificationResultMock);

		//set that only at least rules cover classified object
		for (int i = 0; i < ruleSetSize; i++) {
			if (i == atLeastRuleIndex0 || i == atLeastRuleIndex1 || i == atMostRuleIndex0 || i == atMostRuleIndex1) {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(true);
			} else {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(false);
			}
		}
		
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex0).getDecision()).thenReturn(Mockito.mock(ConditionAtLeast.class));
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex0).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(2, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex1).getDecision()).thenReturn(Mockito.mock(ConditionAtLeast.class));
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex1).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(3, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex0).getDecision()).thenReturn(Mockito.mock(ConditionAtMost.class));
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex0).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(1, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex1).getDecision()).thenReturn(Mockito.mock(ConditionAtMost.class));
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex1).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(2, decAttrPrefType));
				
		SimpleClassificationResult simpleClassificationResult = simpleOptimizingRuleClassifier.classify(objectIndex, informationTableMock);
		assertEquals(simpleClassificationResult.getSuggestedDecision(), new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decAttrPrefType), decAttrIndex));
	}
	
	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#classify(int, InformationTable)}.
	 * Tests the case when classified object is not covered by any rule and is assigned a default classification result.
	 */
	@Test
	void testClassify04() {
		int objectIndex = 7;
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		RuleSetWithComputableCharacteristics ruleSetMock = getRuleSetWithComputableCharacteristicsMock();
		SimpleClassificationResult defaultClassificationResultMock = Mockito.mock(SimpleClassificationResult.class);
		
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(ruleSetMock, defaultClassificationResultMock);

		//set that only at least rules cover classified object
		for (int i = 0; i < ruleSetSize; i++) {
			Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(false);
		}
		
		SimpleClassificationResult simpleClassificationResult = simpleOptimizingRuleClassifier.classify(objectIndex, informationTableMock);
		assertEquals(simpleClassificationResult, defaultClassificationResultMock);
	}
	
	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#classify(int, InformationTable)}.
	 * Tests the case when classified object is covered by both at least and at most rules,
	 * and their intersection boils down to one class.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testClassify05() {
		int objectIndex = 7;
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		RuleSetWithComputableCharacteristics ruleSetMock = getRuleSetWithComputableCharacteristicsMock();
		SimpleClassificationResult defaultClassificationResultMock = Mockito.mock(SimpleClassificationResult.class);
		
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(ruleSetMock, defaultClassificationResultMock);

		//set that only at least rules cover classified object
		for (int i = 0; i < ruleSetSize; i++) {
			if (i == atLeastRuleIndex0 || i == atLeastRuleIndex1 || i == atMostRuleIndex0 || i == atMostRuleIndex1) {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(true);
			} else {
				Mockito.when(ruleSetMock.getRule(i).covers(objectIndex, informationTableMock)).thenReturn(false);
			}
		}
		
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex0).getDecision()).thenReturn(Mockito.mock(ConditionAtLeast.class));
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex0).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(4, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex1).getDecision()).thenReturn(Mockito.mock(ConditionAtLeast.class));
		Mockito.when(ruleSetMock.getRule(atLeastRuleIndex1).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(3, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex0).getDecision()).thenReturn(Mockito.mock(ConditionAtMost.class));
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex0).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(5, decAttrPrefType));
		
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex1).getDecision()).thenReturn(Mockito.mock(ConditionAtMost.class));
		Mockito.when(ruleSetMock.getRule(atMostRuleIndex1).getDecision().getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(4, decAttrPrefType));
				
		SimpleClassificationResult simpleClassificationResult = simpleOptimizingRuleClassifier.classify(objectIndex, informationTableMock);
		assertEquals(simpleClassificationResult.getSuggestedDecision(), new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decAttrPrefType), decAttrIndex));
	}

	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#calculateModalDecisionEvaluation(EvaluationField, EvaluationField, List, List)}.
	 */
	@Test
	void testCalculateModalDecisionEvaluation() {
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier =
				new SimpleOptimizingRuleClassifier(getRuleSetWithComputableCharacteristicsMock(), Mockito.mock(SimpleClassificationResult.class));
		
		EvaluationField downLimit = IntegerFieldFactory.getInstance().create(1, decAttrPrefType);
		EvaluationField upLimit = IntegerFieldFactory.getInstance().create(3, decAttrPrefType);
		
		List<Integer> indicesOfCoveringAtMostRules = new IntArrayList();
		indicesOfCoveringAtMostRules.add(atMostRuleIndex0);
		indicesOfCoveringAtMostRules.add(atMostRuleIndex1);
		
		List<Integer> indicesOfCoveringAtLeastRules = new IntArrayList();
		indicesOfCoveringAtLeastRules.add(atLeastRuleIndex0);
		indicesOfCoveringAtLeastRules.add(atLeastRuleIndex1);
		
		EvaluationField mode = simpleOptimizingRuleClassifier.calculateModalDecisionEvaluation(downLimit, upLimit, indicesOfCoveringAtMostRules, indicesOfCoveringAtLeastRules);
		assertEquals(mode, upLimit);
	}
	
	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#getIndicesOfCoveredLearningObjectsWithDecisionEvaluation(EvaluationField, List)}.
	 * Tests calculations for at most rules.
	 */
	@Test
	void testGetIndicesOfCoveredLearningObjectsWithDecisionEvaluation01() {
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier = new SimpleOptimizingRuleClassifier(getRuleSetWithComputableCharacteristicsMock(), Mockito.mock(SimpleClassificationResult.class));
		
		EvaluationField decisionEvaluation = IntegerFieldFactory.getInstance().create(1, decAttrPrefType);
		
		List<Integer> indicesOfCoveringAtMostRules = new IntArrayList();
		indicesOfCoveringAtMostRules.add(atMostRuleIndex0);
		indicesOfCoveringAtMostRules.add(atMostRuleIndex1);
		
		IntSet indicesOfCoveredLearningObjectsWithDecisionEvaluation = 
				simpleOptimizingRuleClassifier.getIndicesOfCoveredLearningObjectsWithDecisionEvaluation(decisionEvaluation, indicesOfCoveringAtMostRules);
		
		assertEquals(indicesOfCoveredLearningObjectsWithDecisionEvaluation.size(), 2);
		assertTrue(indicesOfCoveredLearningObjectsWithDecisionEvaluation.contains(7));
		assertTrue(indicesOfCoveredLearningObjectsWithDecisionEvaluation.contains(21));
	}
	
	/**
	 * Test method for {@link SimpleOptimizingRuleClassifier#getIndicesOfCoveredLearningObjectsWithDecisionEvaluation(EvaluationField, List)}.
	 * Tests calculations for at least rules.
	 */
	@Test
	void testGetIndicesOfCoveredLearningObjectsWithDecisionEvaluation02() {
		SimpleOptimizingRuleClassifier simpleOptimizingRuleClassifier = new SimpleOptimizingRuleClassifier(getRuleSetWithComputableCharacteristicsMock(), Mockito.mock(SimpleClassificationResult.class));
		
		EvaluationField decisionEvaluation = IntegerFieldFactory.getInstance().create(3, decAttrPrefType);
		
		List<Integer> indicesOfCoveringAtLeastRules = new IntArrayList();
		indicesOfCoveringAtLeastRules.add(atLeastRuleIndex0);
		indicesOfCoveringAtLeastRules.add(atLeastRuleIndex1);
		
		IntSet indicesOfCoveredLearningObjectsWithDecisionEvaluation = 
				simpleOptimizingRuleClassifier.getIndicesOfCoveredLearningObjectsWithDecisionEvaluation(decisionEvaluation, indicesOfCoveringAtLeastRules);
		
		assertEquals(indicesOfCoveredLearningObjectsWithDecisionEvaluation.size(), 5);
		assertTrue(indicesOfCoveredLearningObjectsWithDecisionEvaluation.contains(2));
		assertTrue(indicesOfCoveredLearningObjectsWithDecisionEvaluation.contains(3));
		assertTrue(indicesOfCoveredLearningObjectsWithDecisionEvaluation.contains(6));
		assertTrue(indicesOfCoveredLearningObjectsWithDecisionEvaluation.contains(17));
		assertTrue(indicesOfCoveredLearningObjectsWithDecisionEvaluation.contains(19));
	}

}
