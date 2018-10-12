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
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.IntegerFieldFactory;

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
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet))}.
	 */
	@Test
	void testRuleConditionsA01() {
		try {
			new RuleConditions(null, Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class));
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet))}.
	 */
	@Test
	void testRuleConditionsA02() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), null, Mockito.mock(IntSet.class), Mockito.mock(IntSet.class));
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet))}.
	 */
	@Test
	void testRuleConditionsA03() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), Mockito.mock(IntSet.class), null, Mockito.mock(IntSet.class));
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet))}.
	 */
	@Test
	void testRuleConditionsA04() {
		try {
			new RuleConditions(Mockito.mock(InformationTable.class), Mockito.mock(IntSet.class), Mockito.mock(IntSet.class), null);
			fail("Should not construct rule conditions for null parameter.");
		} catch (NullPointerException exception) {
			//exception is correctly thrown => do nothing
		}
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet))}, {@link RuleConditions#getLearningInformationTable()},
	 * {@link RuleConditions#getIndicesOfPositiveObjects()}, {@link RuleConditions#getIndicesOfApproximationObjects()()}, {@link RuleConditions#getIndicesOfObjectsThatCanBeCovered()},
	 * and {@link RuleConditions#getIndicesOfNeutralObjects()}.
	 */
	@Test
	void testRuleConditionsA05() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfApproximationObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfObjectsThatCanBeCovered = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered);
		
		assertEquals(ruleConditions.getLearningInformationTable(), informationTable);
		assertEquals(ruleConditions.getIndicesOfPositiveObjects(), indicesOfPositiveObjects);
		assertEquals(ruleConditions.getIndicesOfApproximationObjects(), indicesOfApproximationObjects);
		assertEquals(ruleConditions.getIndicesOfObjectsThatCanBeCovered(), indicesOfObjectsThatCanBeCovered);
		assertEquals(ruleConditions.getIndicesOfNeutralObjects(), IntSets.EMPTY_SET);
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, IntSet)}.
	 */
	@Test
	void testRuleConditionsB01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfApproximationObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfObjectsThatCanBeCovered = Mockito.mock(IntSet.class);
		IntSet indicesOfNeutralObjects = null;
		
		assertThrows(NullPointerException.class, () -> {
			new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, indicesOfNeutralObjects);
		});
	}
	
	/**
	 * Test method for {@link RuleConditions#RuleConditions(InformationTable, IntSet, IntSet, IntSet, IntSet)}.
	 */
	@Test
	void testRuleConditionsB02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfApproximationObjects = Mockito.mock(IntSet.class);
		IntSet indicesOfObjectsThatCanBeCovered = Mockito.mock(IntSet.class);
		IntSet indicesOfNeutralObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, indicesOfNeutralObjects);
		
		assertEquals(ruleConditions.getLearningInformationTable(), informationTable);
		assertEquals(ruleConditions.getIndicesOfPositiveObjects(), indicesOfPositiveObjects);
		assertEquals(ruleConditions.getIndicesOfApproximationObjects(), indicesOfApproximationObjects);
		assertEquals(ruleConditions.getIndicesOfObjectsThatCanBeCovered(), indicesOfObjectsThatCanBeCovered);
		assertEquals(ruleConditions.getIndicesOfNeutralObjects(), indicesOfNeutralObjects);
	}
	
	/**
	 * Test method for {@link RuleConditions#addCondition(Condition)}.
	 */
	@Test
	void testAddCondition01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
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
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
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
	 * Test method for {@link RuleConditions#removeCondition(int)}.
	 */
	@Test
	void testRemoveCondition01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
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
	@Test
	void testRemoveCondition02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
		EvaluationAttributeWithContext attributeWithContext1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext1.getAttributeIndex()).thenReturn(1);
		SimpleConditionAtLeast condition1 = Mockito.mock(SimpleConditionAtLeast.class);
		Mockito.when(condition1.getAttributeWithContext()).thenReturn(attributeWithContext1);
		ruleConditions.addCondition(condition1);
		
		EvaluationAttributeWithContext attributeWithContext2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext2.getAttributeIndex()).thenReturn(2);
		SimpleConditionAtMost condition2 = Mockito.mock(SimpleConditionAtMost.class);
		Mockito.when(condition2.getAttributeWithContext()).thenReturn(attributeWithContext2);
		ruleConditions.addCondition(condition2);
		
		EvaluationAttributeWithContext attributeWithContext3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext3.getAttributeIndex()).thenReturn(3);
		SimpleConditionEqual condition3 = Mockito.mock(SimpleConditionEqual.class);
		Mockito.when(condition3.getAttributeWithContext()).thenReturn(attributeWithContext3);
		ruleConditions.addCondition(condition3);
		
		assertEquals(ruleConditions.size(), 3);
		
		ruleConditions.removeCondition(1);
		
		assertEquals(ruleConditions.size(), 2);
		assertEquals(ruleConditions.getCondition(0), condition1);
		assertEquals(ruleConditions.getCondition(1), condition3);
	}

	/**
	 * Test method for {@link RuleConditions#getConditions()}.
	 */
	@Test
	void testGetConditions01() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		assertTrue(ruleConditions.getConditions().isEmpty());
	}
	
	/**
	 * Test method for {@link RuleConditions#getConditions())}.
	 */
	@Test
	void testGetConditions02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
		EvaluationAttributeWithContext attributeWithContext1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext1.getAttributeIndex()).thenReturn(1);
		SimpleConditionAtLeast condition1 = Mockito.mock(SimpleConditionAtLeast.class);
		Mockito.when(condition1.getAttributeWithContext()).thenReturn(attributeWithContext1);
		ruleConditions.addCondition(condition1);
		
		EvaluationAttributeWithContext attributeWithContext2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext2.getAttributeIndex()).thenReturn(2);
		SimpleConditionAtMost condition2 = Mockito.mock(SimpleConditionAtMost.class);
		Mockito.when(condition2.getAttributeWithContext()).thenReturn(attributeWithContext2);
		ruleConditions.addCondition(condition2);
		
		EvaluationAttributeWithContext attributeWithContext3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext3.getAttributeIndex()).thenReturn(3);
		SimpleConditionEqual condition3 = Mockito.mock(SimpleConditionEqual.class);
		Mockito.when(condition3.getAttributeWithContext()).thenReturn(attributeWithContext3);
		ruleConditions.addCondition(condition3);
		
		List<Condition<?>> conditions = ruleConditions.getConditions();
		
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
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
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
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
		EvaluationAttributeWithContext attributeWithContextMock1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock1.getAttributeIndex()).thenReturn(0);
		Condition<?> conditionMock = Mockito.mock(Condition.class);
		Mockito.when(conditionMock.getAttributeWithContext()).thenReturn(attributeWithContextMock1);
		ruleConditions.addCondition(conditionMock);
		
		EvaluationAttributeWithContext attributeWithContextMock2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContextMock2.getAttributeIndex()).thenReturn(1);
		SimpleConditionAtLeast simpleCondition = new SimpleConditionAtLeast(attributeWithContextMock2, IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.COST));
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
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
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
		SimpleConditionAtLeast simpleCondition = new SimpleConditionAtLeast(attributeWithContextMock3, IntegerFieldFactory.getInstance().create(5, AttributePreferenceType.COST));
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
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
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
	@Test
	void testContainsCondition() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		IntSet indicesOfPositiveObjects = Mockito.mock(IntSet.class);
		
		RuleConditions ruleConditions = new RuleConditions(informationTable, indicesOfPositiveObjects, indicesOfPositiveObjects, indicesOfPositiveObjects);
		
		EvaluationAttributeWithContext attributeWithContext1 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext1.getAttributeIndex()).thenReturn(1);
		SimpleConditionAtLeast condition1 = Mockito.mock(SimpleConditionAtLeast.class);
		Mockito.when(condition1.getAttributeWithContext()).thenReturn(attributeWithContext1);
		ruleConditions.addCondition(condition1);
		
		EvaluationAttributeWithContext attributeWithContext2 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext2.getAttributeIndex()).thenReturn(2);
		SimpleConditionAtMost condition2 = Mockito.mock(SimpleConditionAtMost.class);
		Mockito.when(condition2.getAttributeWithContext()).thenReturn(attributeWithContext2);
		ruleConditions.addCondition(condition2);
		
		EvaluationAttributeWithContext attributeWithContext3 = Mockito.mock(EvaluationAttributeWithContext.class);
		Mockito.when(attributeWithContext3.getAttributeIndex()).thenReturn(3);
		SimpleConditionEqual condition3 = Mockito.mock(SimpleConditionEqual.class);
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

}
