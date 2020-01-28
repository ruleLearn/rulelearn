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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;

/**
 * Tests for {@link RuleConditions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleConditionsTest {

	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsA01() {
		try {
			new RuleConditions(null, Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), RuleType.CERTAIN, RuleSemantics.AT_LEAST);
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsA02() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), null, Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), RuleType.CERTAIN, RuleSemantics.AT_LEAST);
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsA03() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), Mockito.mock(IntSet.class), null, Mockito.mock(IntSet.class), RuleType.CERTAIN, RuleSemantics.AT_LEAST);
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsA04() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), null, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsA05() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), null, RuleSemantics.AT_LEAST);
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsA06() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), RuleType.CERTAIN, null);
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}, {@link RuleConditions#getLearningInformationTable()},
	 * {@link RuleConditions#getIndicesOfPositiveObjects()}, {@link RuleConditions#getIndicesOfApproximationObjects()()}, {@link RuleConditions#getIndicesOfObjectsThatCanBeCovered()},
	 * {@link RuleConditions#getIndicesOfNeutralObjects()}, {@link RuleConditions#getRuleType()}, and {@link RuleConditions#getRuleSemantics()}.
	 */
	@Test
	void testRuleConditionsA07() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfApproximationObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfObjectsThatCanBeCovered = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered,
				RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		
		assertEquals(ruleConditions.getLearningInformationTable(), informationTable);
		assertEquals(ruleConditions.getIndicesOfPositiveObjects(), indicesOfPositiveObjects);
		assertEquals(ruleConditions.getIndicesOfApproximationObjects(), indicesOfApproximationObjects);
		assertEquals(ruleConditions.getIndicesOfObjectsThatCanBeCovered(), indicesOfObjectsThatCanBeCovered);
		assertEquals(ruleConditions.getIndicesOfNeutralObjects(), IntSets.EMPTY_SET);
		assertEquals(ruleConditions.getRuleType(), RuleType.CERTAIN);
		assertEquals(ruleConditions.getRuleSemantics(), RuleSemantics.AT_LEAST);
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsB01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfApproximationObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfObjectsThatCanBeCovered = Mockito.mock(IntSet.class);
		IntSet indicesOfNeutralObjects = null;
		
		assertThrows(NullPointerException.class, () -> {
			new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, indicesOfNeutralObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		});
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, IntSet, RuleType, RuleSemantics)}.
	 */
	@Test
	void testRuleConditionsB02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfApproximationObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfObjectsThatCanBeCovered = Mockito.mock(IntSet.class);
		IntSet indicesOfNeutralObjects = Mockito.mock(IntSet.class);
		RuleType ruleType = RuleType.CERTAIN;
		RuleSemantics ruleSemantics = RuleSemantics.AT_LEAST;
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, indicesOfNeutralObjects,
				ruleType, ruleSemantics);
		
		assertEquals(ruleConditions.getLearningInformationTable(), informationTable);
		assertEquals(ruleConditions.getIndicesOfPositiveObjects(), indicesOfPositiveObjects);
		assertEquals(ruleConditions.getIndicesOfApproximationObjects(), indicesOfApproximationObjects);
		assertEquals(ruleConditions.getIndicesOfObjectsThatCanBeCovered(), indicesOfObjectsThatCanBeCovered);
		assertEquals(ruleConditions.getIndicesOfNeutralObjects(), indicesOfNeutralObjects);
		assertEquals(ruleConditions.getRuleType(), ruleType);
		assertEquals(ruleConditions.getRuleSemantics(), ruleSemantics); 
	}
	
	/**
	 * Test method for {@link RuleConditions#addCondition(Condition)}.
	 */
	@Test
	void testAddCondition01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		
		try {
			ruleConditions.addCondition(null);
			fail("Should not add null condition to rule conditions.");
		} catch (NullPointerException exception) {
			//do nothing - exception is correctly thrown
		}
	}

	/**
	 * Test method for {@link RuleConditions#addCondition(Condition)}.
	 */
	@Test
	void testAddCondition02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.POSSIBLE, RuleSemantics.AT_LEAST);
		
		EvaluationAttributeWithContext attributeWithContextMock1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock1.getAttributeIndex()).thenReturn(1);
		Condition<?> conditionMock1 = Mockito.mock(Condition.class);
		Mockito.when(conditionMock1.getAttributeWithContext()).thenReturn(attributeWithContextMock1);
		assertEquals(ruleConditions.addCondition(conditionMock1), 0);
		assertEquals(ruleConditions.getConditions().size(), 1);
		
		EvaluationAttributeWithContext attributeWithContextMock2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock2.getAttributeIndex()).thenReturn(2);
		Condition<?> conditionMock2 = Mockito.mock(Condition.class);
		Mockito.when(conditionMock2.getAttributeWithContext()).thenReturn(attributeWithContextMock2);
		assertEquals(ruleConditions.addCondition(conditionMock2), 1);
		assertEquals(ruleConditions.getConditions().size(), 2);
	}
	
	/**
	 * Test method for {@link RuleConditions#addCondition(Condition)}.
	 * Tests inner casting to {@code Condition<EvaluationField>}.
	 */
	@Test
	void testAddCondition03() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		
		EvaluationAttributeWithContext attributeWithContextMock1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock1.getAttributeIndex()).thenReturn(1);
		Mockito.when(attributeWithContextMock1.getAttributeName()).thenReturn("attr1");
		ConditionAtLeastObjectVSThreshold<IntegerField> condition1 =
				new ConditionAtLeastObjectVSThreshold<IntegerField>(attributeWithContextMock1, IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN));
		assertEquals(ruleConditions.addCondition(condition1), 0);
		assertEquals(ruleConditions.getConditions().size(), 1);
		
		EvaluationAttributeWithContext attributeWithContextMock2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock2.getAttributeIndex()).thenReturn(2);
		Mockito.when(attributeWithContextMock2.getAttributeName()).thenReturn("attr2");
		ConditionAtMostObjectVSThreshold<RealField> condition2 =
				new ConditionAtMostObjectVSThreshold<RealField>(attributeWithContextMock2, RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.COST));
		assertEquals(ruleConditions.addCondition(condition2), 1);
		assertEquals(ruleConditions.getConditions().size(), 2);
		
		List<Condition<EvaluationField>> conditions = ruleConditions.getConditions();
		
		for (Condition<EvaluationField> condition : conditions) {
			System.out.println(condition);
		}
	}

	/**
	 * Test method for {@link RuleConditions#removeCondition(int)}.
	 */
	@Test
	void testRemoveCondition01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.POSSIBLE, RuleSemantics.AT_LEAST);
		
		try {
			ruleConditions.removeCondition(0);
			fail("Should not remove a non-existing condition.");
		} catch (IndexOutOfBoundsException exception) {
			//do nothing - exception is correctly thrown
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#removeCondition(int)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testRemoveCondition02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.POSSIBLE, RuleSemantics.AT_LEAST);
		
		EvaluationAttributeWithContext attributeWithContext1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext1.getAttributeIndex()).thenReturn(1);
		ConditionAtLeast<EvaluationField> condition1 = Mockito.mock(ConditionAtLeastObjectVSThreshold.class);
		Mockito.when(condition1.getAttributeWithContext()).thenReturn(attributeWithContext1);
		ruleConditions.addCondition(condition1);
		
		EvaluationAttributeWithContext attributeWithContext2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext2.getAttributeIndex()).thenReturn(2);
		ConditionAtMost<EvaluationField> condition2 = Mockito.mock(ConditionAtMostObjectVSThreshold.class);
		Mockito.when(condition2.getAttributeWithContext()).thenReturn(attributeWithContext2);
		ruleConditions.addCondition(condition2);
		
		EvaluationAttributeWithContext attributeWithContext3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext3.getAttributeIndex()).thenReturn(3);
		ConditionEqual<EvaluationField> condition3 = Mockito.mock(ConditionEqual.class);
		Mockito.when(condition3.getAttributeWithContext()).thenReturn(attributeWithContext3);
		ruleConditions.addCondition(condition3);
		
		assertEquals(ruleConditions.size(), 3);
		
		ruleConditions.removeCondition(1);
		
		assertEquals(ruleConditions.size(), 2);
		assertEquals(ruleConditions.getCondition(0), condition1);
		assertEquals(ruleConditions.getCondition(1), condition3);
	}
	
	/**
	 * Test method for {@link RuleConditions#addCondition(Condition)} and {@link RuleConditions#removeCondition(int)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testAddRemoveCondition01() {
		int conditionIndex = 0;
		int addedConditionIndex;
		
		int attributeIndex;
		EvaluationAttributeWithContext attributeWithContext;
		Condition<EvaluationField> condition;
		IntList conditionIndices;
		
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		Mockito.when(informationTable.getNumberOfObjects()).thenReturn(500);
		Mockito.when(informationTable.getNumberOfAttributes()).thenReturn(10);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		
		attributeIndex = 0;
		attributeWithContext = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext.getAttributeIndex()).thenReturn(attributeIndex);
		condition = Mockito.mock(ConditionAtLeastThresholdVSObject.class);
		Mockito.when(condition.getAttributeWithContext()).thenReturn(attributeWithContext);
		addedConditionIndex = ruleConditions.addCondition(condition);
		assertEquals(addedConditionIndex, conditionIndex);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).getInt(0), addedConditionIndex);
		conditionIndex++;
		
		attributeIndex = 8;
		attributeWithContext = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext.getAttributeIndex()).thenReturn(attributeIndex);
		condition = Mockito.mock(ConditionAtLeastThresholdVSObject.class);
		Mockito.when(condition.getAttributeWithContext()).thenReturn(attributeWithContext);
		addedConditionIndex = ruleConditions.addCondition(condition);
		assertEquals(addedConditionIndex, conditionIndex);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).getInt(0), addedConditionIndex);
		conditionIndex++;
		
		attributeIndex = 4;
		attributeWithContext = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext.getAttributeIndex()).thenReturn(attributeIndex);
		condition = Mockito.mock(ConditionAtLeastThresholdVSObject.class);
		Mockito.when(condition.getAttributeWithContext()).thenReturn(attributeWithContext);
		addedConditionIndex = ruleConditions.addCondition(condition);
		assertEquals(addedConditionIndex, conditionIndex);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).getInt(0), addedConditionIndex);
		conditionIndex++;
		
		attributeIndex = 6;
		attributeWithContext = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext.getAttributeIndex()).thenReturn(attributeIndex);
		condition = Mockito.mock(ConditionAtLeastThresholdVSObject.class);
		Mockito.when(condition.getAttributeWithContext()).thenReturn(attributeWithContext);
		addedConditionIndex = ruleConditions.addCondition(condition);
		assertEquals(addedConditionIndex, conditionIndex);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).getInt(0), addedConditionIndex);
		conditionIndex++;
		
		attributeIndex = 2;
		attributeWithContext = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext.getAttributeIndex()).thenReturn(attributeIndex);
		condition = Mockito.mock(ConditionAtLeastThresholdVSObject.class);
		Mockito.when(condition.getAttributeWithContext()).thenReturn(attributeWithContext);
		addedConditionIndex = ruleConditions.addCondition(condition);
		assertEquals(addedConditionIndex, conditionIndex);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).getInt(0), addedConditionIndex);
		conditionIndex++;
		
		//-----
		
		assertEquals(ruleConditions.getConditionIndicesForAttribute(0).getInt(0), 0);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(8).getInt(0), 1);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(4).getInt(0), 2);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(6).getInt(0), 3);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(2).getInt(0), 4);
		
		attributeIndex = 4;
		conditionIndices = ruleConditions.getConditionIndicesForAttribute(attributeIndex);
		assertEquals(conditionIndices.size(), 1);
		assertTrue(ruleConditions.containsConditionForAttribute(attributeIndex));
		ruleConditions.removeCondition(conditionIndices.getInt(0)); //remove condition for considered attribute
		assertFalse(ruleConditions.containsConditionForAttribute(attributeIndex));
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).size(), 0);
		//
		assertEquals(ruleConditions.getConditionIndicesForAttribute(0).getInt(0), 0);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(8).getInt(0), 1);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(6).getInt(0), 2);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(2).getInt(0), 3);
		
		attributeIndex = 8;
		conditionIndices = ruleConditions.getConditionIndicesForAttribute(attributeIndex);
		assertEquals(conditionIndices.size(), 1);
		assertTrue(ruleConditions.containsConditionForAttribute(attributeIndex));
		ruleConditions.removeCondition(conditionIndices.getInt(0)); //remove condition for considered attribute
		assertFalse(ruleConditions.containsConditionForAttribute(attributeIndex));
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).size(), 0);
		//
		assertEquals(ruleConditions.getConditionIndicesForAttribute(0).getInt(0), 0);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(6).getInt(0), 1);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(2).getInt(0), 2);
		
		attributeIndex = 2;
		conditionIndices = ruleConditions.getConditionIndicesForAttribute(attributeIndex);
		assertEquals(conditionIndices.size(), 1);
		assertTrue(ruleConditions.containsConditionForAttribute(attributeIndex));
		ruleConditions.removeCondition(conditionIndices.getInt(0)); //remove condition for considered attribute
		assertFalse(ruleConditions.containsConditionForAttribute(attributeIndex));
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).size(), 0);
		//
		assertEquals(ruleConditions.getConditionIndicesForAttribute(0).getInt(0), 0);
		assertEquals(ruleConditions.getConditionIndicesForAttribute(6).getInt(0), 1);
		
		attributeIndex = 0;
		conditionIndices = ruleConditions.getConditionIndicesForAttribute(attributeIndex);
		assertEquals(conditionIndices.size(), 1);
		assertTrue(ruleConditions.containsConditionForAttribute(attributeIndex));
		ruleConditions.removeCondition(conditionIndices.getInt(0)); //remove condition for considered attribute
		assertFalse(ruleConditions.containsConditionForAttribute(attributeIndex));
		assertEquals(ruleConditions.getConditionIndicesForAttribute(attributeIndex).size(), 0);
		//
		assertEquals(ruleConditions.getConditionIndicesForAttribute(6).getInt(0), 0);
	}

	/**
	 * Test method for {@link RuleConditions#getConditions()}.
	 */
	@Test
	void testGetConditions01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		assertTrue(ruleConditions.getConditions().isEmpty());
	}
	
	/**
	 * Test method for {@link RuleConditions#getConditions())}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testGetConditions02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		
		EvaluationAttributeWithContext attributeWithContext1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext1.getAttributeIndex()).thenReturn(1);
		ConditionAtLeast<EvaluationField> condition1 = Mockito.mock(ConditionAtLeast.class);
		Mockito.when(condition1.getAttributeWithContext()).thenReturn(attributeWithContext1);
		ruleConditions.addCondition(condition1);
		
		EvaluationAttributeWithContext attributeWithContext2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext2.getAttributeIndex()).thenReturn(2);
		ConditionAtMost<EvaluationField> condition2 = Mockito.mock(ConditionAtMost.class);
		Mockito.when(condition2.getAttributeWithContext()).thenReturn(attributeWithContext2);
		ruleConditions.addCondition(condition2);
		
		EvaluationAttributeWithContext attributeWithContext3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext3.getAttributeIndex()).thenReturn(3);
		ConditionEqual<EvaluationField> condition3 = Mockito.mock(ConditionEqual.class);
		Mockito.when(condition3.getAttributeWithContext()).thenReturn(attributeWithContext3);
		ruleConditions.addCondition(condition3);
		
		List<Condition<EvaluationField>> conditions = ruleConditions.getConditions();
		
		assertEquals(conditions.size(), 3);
		assertEquals(conditions.get(0), condition1);
		assertEquals(conditions.get(1), condition2);
		assertEquals(conditions.get(2), condition3);
	}
	
	/**
	 * Test method for {@link RuleConditions#getCondition(int)}.
	 */
	@Test
	void testGetCondition01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		try {
			ruleConditions.getCondition(0);
			fail("Should not get a non-existing condition.");
		} catch (IndexOutOfBoundsException exception) {
			//do nothing - exception is correctly thrown
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#getCondition(int)}.
	 */
	@Test
	void testGetCondition02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_MOST);
		
		EvaluationAttributeWithContext attributeWithContextMock1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock1.getAttributeIndex()).thenReturn(0);
		Condition<?> conditionMock = Mockito.mock(Condition.class);
		Mockito.when(conditionMock.getAttributeWithContext()).thenReturn(attributeWithContextMock1);
		ruleConditions.addCondition(conditionMock);
		
		EvaluationAttributeWithContext attributeWithContextMock2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock2.getAttributeIndex()).thenReturn(1);
		ConditionAtLeast<EvaluationField> simpleCondition = new ConditionAtLeastThresholdVSObject<>(attributeWithContextMock2, IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.COST));
		ruleConditions.addCondition(simpleCondition);
		
		assertEquals(ruleConditions.getCondition(1), simpleCondition);
	}
	
	/**
	 * Test method for {@link RuleConditions#getConditionIndex(Condition)}.
	 */
	@Test
	void testGetConditionIndex() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		
		EvaluationAttributeWithContext attributeWithContextMock1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock1.getAttributeIndex()).thenReturn(0);
		Condition<?> conditionMock1 = Mockito.mock(Condition.class);
		Mockito.when(conditionMock1.getAttributeWithContext()).thenReturn(attributeWithContextMock1);
		ruleConditions.addCondition(conditionMock1);
		
		EvaluationAttributeWithContext attributeWithContextMock2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock2.getAttributeIndex()).thenReturn(1);
		Condition<?> conditionMock2 = Mockito.mock(Condition.class);
		Mockito.when(conditionMock2.getAttributeWithContext()).thenReturn(attributeWithContextMock2);
		ruleConditions.addCondition(conditionMock2);
		
		EvaluationAttributeWithContext attributeWithContextMock3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock3.getAttributeIndex()).thenReturn(2);
		ConditionAtLeast<EvaluationField> simpleCondition = new ConditionAtLeastThresholdVSObject<>(attributeWithContextMock3, IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN));
		ruleConditions.addCondition(simpleCondition);
		
		assertEquals(ruleConditions.getConditionIndex(simpleCondition), 2);
	}
	
	/**
	 * Test method for {@link RuleConditions#size()}.
	 */
	@Test
	void testSize() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.POSSIBLE, RuleSemantics.AT_LEAST);
		
		EvaluationAttributeWithContext attributeWithContextMock1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock1.getAttributeIndex()).thenReturn(1);
		Condition<?> conditionMock1 = Mockito.mock(Condition.class);
		Mockito.when(conditionMock1.getAttributeWithContext()).thenReturn(attributeWithContextMock1);
		ruleConditions.addCondition(conditionMock1);
		
		EvaluationAttributeWithContext attributeWithContextMock2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock2.getAttributeIndex()).thenReturn(2);
		Condition<?> conditionMock2 = Mockito.mock(Condition.class);
		Mockito.when(conditionMock2.getAttributeWithContext()).thenReturn(attributeWithContextMock2);
		ruleConditions.addCondition(conditionMock2);
		
		EvaluationAttributeWithContext attributeWithContextMock3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock3.getAttributeIndex()).thenReturn(3);
		Condition<?> conditionMock3 = Mockito.mock(Condition.class);
		Mockito.when(conditionMock3.getAttributeWithContext()).thenReturn(attributeWithContextMock3);
		ruleConditions.addCondition(conditionMock3);
	}
	
	/**
	 * Test method for {@link RuleConditions#containsCondition()}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testContainsCondition() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.POSSIBLE, RuleSemantics.AT_LEAST);
		
		EvaluationAttributeWithContext attributeWithContext1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext1.getAttributeIndex()).thenReturn(1);
		ConditionAtLeast<EvaluationField> condition1 = Mockito.mock(ConditionAtLeast.class);
		Mockito.when(condition1.getAttributeWithContext()).thenReturn(attributeWithContext1);
		ruleConditions.addCondition(condition1);
		
		EvaluationAttributeWithContext attributeWithContext2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext2.getAttributeIndex()).thenReturn(2);
		ConditionAtMost<EvaluationField> condition2 = Mockito.mock(ConditionAtMost.class);
		Mockito.when(condition2.getAttributeWithContext()).thenReturn(attributeWithContext2);
		ruleConditions.addCondition(condition2);
		
		EvaluationAttributeWithContext attributeWithContext3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext3.getAttributeIndex()).thenReturn(3);
		ConditionEqual<EvaluationField> condition3 = Mockito.mock(ConditionEqual.class);
		Mockito.when(condition3.getAttributeWithContext()).thenReturn(attributeWithContext3);
		ruleConditions.addCondition(condition3);
		
		assertEquals(ruleConditions.size(), 3);
		
		assertTrue(ruleConditions.containsCondition(condition1));
		assertTrue(ruleConditions.containsCondition(condition2));
		assertTrue(ruleConditions.containsCondition(condition3));
		
		ruleConditions.removeCondition(1);
		
		assertTrue(ruleConditions.containsCondition(condition1));
		assertFalse(ruleConditions.containsCondition(condition2)); //!
		assertTrue(ruleConditions.containsCondition(condition3));
	}
	
	/**
	 * Test method for {@link RuleConditions#getRuleCoverageInformation()}.
	 */
	@Test
	void testGetRuleCoverageInformation() {
		int positiveObjectIndices[] = {0, 2, 5}; //union "class at least 4"
		
		int coveredObjectIndices[] = {0, 1, 2, 3, 4, 5}; //3 positive, 3 negative objects
		Decision[] coveredObjectsDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN), 4),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), 4),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(6, AttributePreferenceType.GAIN), 4),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), 4),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), 4),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.GAIN), 4)
		};
		if (coveredObjectIndices.length != coveredObjectsDecisions.length) {
			fail("Different number of indices of covered objects and their decisions.");
		}
		IntList expectedIndicesOfCoveredObjects = new IntArrayList();
		Int2ObjectMap<Decision> expectedDecisionsOfCoveredObjects = new Int2ObjectOpenHashMap<Decision>();
		
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int informationTableSize = coveredObjectIndices.length;
		Mockito.when(informationTable.getNumberOfObjects()).thenReturn(informationTableSize);
		
		int objectIndex;
		for (int i = 0; i < coveredObjectIndices.length; i++) {
			objectIndex = coveredObjectIndices[i];
			Mockito.when(informationTable.getDecision(objectIndex)).thenReturn(coveredObjectsDecisions[i]);
			expectedIndicesOfCoveredObjects.add(objectIndex);
			expectedDecisionsOfCoveredObjects.put(objectIndex, coveredObjectsDecisions[i]);
		}
		
		IntSet indicesOfPositiveObjects = new IntOpenHashSet();
		for (int positiveObjectIndex : positiveObjectIndices) {
			indicesOfPositiveObjects.add(positiveObjectIndex);
		}
		
		//empty rule conditions cover all the objects from learning information table
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects, RuleType.CERTAIN, RuleSemantics.AT_LEAST);
		RuleCoverageInformation ruleCoverageInformation = ruleConditions.getRuleCoverageInformation();
		
		assertEquals(ruleCoverageInformation.getIndicesOfPositiveObjects(), indicesOfPositiveObjects);
		assertEquals(ruleCoverageInformation.getIndicesOfNeutralObjects().size(), 0);
		assertEquals(ruleCoverageInformation.getIndicesOfCoveredObjects(), expectedIndicesOfCoveredObjects);
		assertEquals(ruleCoverageInformation.getAllObjectsCount(), informationTableSize);
		
		Int2ObjectMap<Decision> decisionsOfCoveredObjects = ruleCoverageInformation.getDecisionsOfCoveredObjects();
		
		assertEquals(decisionsOfCoveredObjects, expectedDecisionsOfCoveredObjects);
	}

}
