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

package org.rulelearn.approximations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.CompositeDecision;
import org.rulelearn.data.Decision;
import org.rulelearn.data.DecisionDistribution;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link UnionsWithSingleLimitingDecision}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class UnionsWithSingleLimitingDecisionTest {

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 * Tests if {@link NullPointerException} is thrown when one of the constructor parameters is {@code null}.
	 */
	@Test
	void testUnions01() {
		InformationTableWithDecisionDistributions informationTableMock = null;
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		assertThrows(NullPointerException.class, () -> {
			new UnionsWithSingleLimitingDecision(informationTableMock, roughSetCalculatorMock);
		}); 
	}
	
	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 * Tests if {@link NullPointerException} is thrown when one of the constructor parameters is {@code null}.
	 */
	@Test
	void testUnions02() {
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = null;
		
		assertThrows(NullPointerException.class, () -> {
			new UnionsWithSingleLimitingDecision(informationTableMock, roughSetCalculatorMock);
		}); 
	}
	
	/**
	 * Gets a mock of an {@link InformationTableWithDecisionDistributions} with one active decision attribute and {@link SimpleDecision} decisions.
	 * 
	 * @return a mock of an {@link InformationTableWithDecisionDistributions} with one active decision attribute and {@link SimpleDecision} decisions.
	 */
	private InformationTableWithDecisionDistributions getInformationTableMock01() {
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		int attributeIndex = 1;
		
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute decisionAttributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(decisionAttributeMock.isActive()).thenReturn(true);
		Mockito.when(decisionAttributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(decisionAttributeMock.getPreferenceType()).thenReturn(preferenceType);
		//
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(decisionAttributeMock);
		
		Decision decision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), attributeIndex);
		//
		Mockito.when(informationTableMock.getOrderedUniqueFullyDeterminedDecisions()).thenReturn(new Decision[] {decision});
		
		DecisionDistribution decisionDistribution = Mockito.mock(DecisionDistribution.class);
		Set<Decision> allDecisions = new HashSet<Decision>();
		allDecisions.add(decision);
		Mockito.when(decisionDistribution.getDecisions()).thenReturn(allDecisions);
		//
		Mockito.when(informationTableMock.getDecisionDistribution()).thenReturn(decisionDistribution);
		
		return informationTableMock;
	}
	
	/**
	 * Gets a mock of an {@link InformationTableWithDecisionDistributions} with two active decision attributes and {@link CompositeDecision} decisions.
	 * 
	 * @return a mock of an {@link InformationTableWithDecisionDistributions} with two active decision attributes and {@link CompositeDecision} decisions.
	 */
	private InformationTableWithDecisionDistributions getInformationTableMock02() {
		AttributePreferenceType[] preferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.GAIN};
		int[] attributeIndices = new int[] {1, 2};
		
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute[] decisionAttributeMocks = new EvaluationAttribute[] {Mockito.mock(EvaluationAttribute.class), Mockito.mock(EvaluationAttribute.class)};
		for (int i = 0; i < decisionAttributeMocks.length; i++) {
			Mockito.when(decisionAttributeMocks[i].isActive()).thenReturn(true);
			Mockito.when(decisionAttributeMocks[i].getType()).thenReturn(AttributeType.DECISION);
			Mockito.when(decisionAttributeMocks[i].getPreferenceType()).thenReturn(preferenceTypes[i]);
			Mockito.when(informationTableMock.getAttribute(attributeIndices[i])).thenReturn(decisionAttributeMocks[i]);
		}
		
		Decision[] orderedUniqueFullyDeterminedDecisions = {
				new CompositeDecision(
					new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceTypes[0]), IntegerFieldFactory.getInstance().create(2, preferenceTypes[1])},
					new int[] {attributeIndices[0], attributeIndices[1]}), //2,2
				new CompositeDecision(
						new EvaluationField[] {IntegerFieldFactory.getInstance().create(3, preferenceTypes[0]), IntegerFieldFactory.getInstance().create(2, preferenceTypes[1])},
						new int[] {attributeIndices[0], attributeIndices[1]}), //3,2
				new CompositeDecision(
						new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceTypes[0]), IntegerFieldFactory.getInstance().create(3, preferenceTypes[1])},
						new int[] {attributeIndices[0], attributeIndices[1]}), //2,3
				new CompositeDecision(
						new EvaluationField[] {IntegerFieldFactory.getInstance().create(3, preferenceTypes[0]), IntegerFieldFactory.getInstance().create(3, preferenceTypes[1])},
						new int[] {attributeIndices[0], attributeIndices[1]}), //3,3
		};
		//
		Mockito.when(informationTableMock.getOrderedUniqueFullyDeterminedDecisions()).thenReturn(orderedUniqueFullyDeterminedDecisions);
		
		Set<Decision> allDecisions = new HashSet<Decision>(Arrays.asList(orderedUniqueFullyDeterminedDecisions));
		allDecisions.add(
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceTypes[0]), new UnknownSimpleFieldMV2()},
				new int[] {attributeIndices[0], attributeIndices[1]})); //2,*
		allDecisions.add(
				new CompositeDecision(new EvaluationField[] {new UnknownSimpleFieldMV2(), IntegerFieldFactory.getInstance().create(3, preferenceTypes[1])},
				new int[] {attributeIndices[0], attributeIndices[1]})); //*,3
		DecisionDistribution decisionDistribution = Mockito.mock(DecisionDistribution.class);
		Mockito.when(decisionDistribution.getDecisions()).thenReturn(allDecisions);
		//
		Mockito.when(informationTableMock.getDecisionDistribution()).thenReturn(decisionDistribution);
		
		return informationTableMock;
	}
	
	/**
	 * Gets a mock of an {@link InformationTableWithDecisionDistributions} with one active decision attribute and {@link SimpleDecision} decisions.
	 * Employs five different decisions.
	 * 
	 * @param decisionAttributePreferenceType requested type of preference of decision attribute of returned information table mock;
	 *        should be {@link AttributePreferenceType#GAIN} or {@link AttributePreferenceType#COST} 
	 * @return a mock of an {@link InformationTableWithDecisionDistributions} with one active decision attribute and {@link SimpleDecision} decisions.
	 */
	private InformationTableWithDecisionDistributions getInformationTableMock03(AttributePreferenceType decisionAttributePreferenceType) {
		int attributeIndex = 1;
		
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute decisionAttributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(decisionAttributeMock.isActive()).thenReturn(true);
		Mockito.when(decisionAttributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(decisionAttributeMock.getPreferenceType()).thenReturn(decisionAttributePreferenceType);
		//
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(decisionAttributeMock);
		
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decisionAttributePreferenceType), attributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decisionAttributePreferenceType), attributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decisionAttributePreferenceType), attributeIndex);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decisionAttributePreferenceType), attributeIndex);
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5, decisionAttributePreferenceType), attributeIndex);
		//
		switch (decisionAttributePreferenceType) {
		case GAIN:
			Mockito.when(informationTableMock.getOrderedUniqueFullyDeterminedDecisions()).thenReturn(new Decision[] {decision1, decision2, decision3, decision4, decision5});
			break;
		case COST:
			Mockito.when(informationTableMock.getOrderedUniqueFullyDeterminedDecisions()).thenReturn(new Decision[] {decision5, decision4, decision3, decision2, decision1});
			break;
		default:
			throw new InvalidValueException("Incorrect decision attribute preference type.");
		}
		
		DecisionDistribution decisionDistribution = Mockito.mock(DecisionDistribution.class);
		Set<Decision> allDecisions = new HashSet<Decision>();
		allDecisions.add(decision1);
		allDecisions.add(decision2);
		allDecisions.add(decision3);
		allDecisions.add(decision4);
		allDecisions.add(decision5);
		Mockito.when(decisionDistribution.getDecisions()).thenReturn(allDecisions);
		//
		Mockito.when(informationTableMock.getDecisionDistribution()).thenReturn(decisionDistribution);
		
		return informationTableMock;
	}
	
	private DominanceBasedRoughSetCalculator getRoughSetCalculatorMock() {
		return Mockito.mock(DominanceBasedRoughSetCalculator.class);
	}
	
	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions, approximations.DominanceBasedRoughSetCalculator)}.
	 * Tests if object is constructed when parameters are not {@code null}.
	 */
	@Test
	void testUnions03() {
		try {
			new UnionsWithSingleLimitingDecision(this.getInformationTableMock01(), this.getRoughSetCalculatorMock());
		} catch (Throwable throwable) {
			fail("Could not construct unions for correct parameters.");
		}
	}
	
	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions, approximations.DominanceBasedRoughSetCalculator)}.
	 * Tests if object is constructed when parameters are not {@code null}.
	 */
	@Test
	void testUnions04() {
		try {
			new UnionsWithSingleLimitingDecision(this.getInformationTableMock02(), this.getRoughSetCalculatorMock());
		} catch (Throwable throwable) {
			fail("Could not construct unions for correct parameters.");
		}
	}
	
	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)},
	 * {@link UnionsWithSingleLimitingDecision#calculateUpwardUnions()}, {@link UnionsWithSingleLimitingDecision#calculateDownwardUnions()},
	 * {@link Unions#getUpwardUnions()}, {@link Unions#getDownwardUnions()}, and {@link UnionsWithSingleLimitingDecision#setComplementaryUnions()}.
	 * Tests if upward and downward unions are properly calculated, and if complementary unions are properly set.
	 * Employs gain-type decision attribute.
	 */
	@Test
	void testUnions05() {
		AttributePreferenceType decisionAttributePreferenceType = AttributePreferenceType.GAIN;
		
		InformationTableWithDecisionDistributions informationTableMock = this.getInformationTableMock03(decisionAttributePreferenceType);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = this.getRoughSetCalculatorMock();
		
		UnionsWithSingleLimitingDecision unions = new UnionsWithSingleLimitingDecision(informationTableMock, roughSetCalculatorMock);
		
		Union[] upwardUnions = unions.getUpwardUnions();
		Union[] downwardUnions = unions.getDownwardUnions();
		
		assertEquals(upwardUnions.length, 4);
		assertEquals(downwardUnions.length, 4);
		
		//as in getInformationTableMock03
		int attributeIndex = 1;

		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decisionAttributePreferenceType), attributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decisionAttributePreferenceType), attributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decisionAttributePreferenceType), attributeIndex);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decisionAttributePreferenceType), attributeIndex);
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5, decisionAttributePreferenceType), attributeIndex);
		
		//check upward unions (correctness of limiting decisions and order from the most specific union to the least specific union
		assertEquals(upwardUnions[0].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[0]).getLimitingDecision(), decision5);
		assertEquals(upwardUnions[1].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[1]).getLimitingDecision(), decision4);
		assertEquals(upwardUnions[2].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[2]).getLimitingDecision(), decision3);
		assertEquals(upwardUnions[3].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[3]).getLimitingDecision(), decision2);
		
		//check downward unions (correctness of limiting decisions and order from the most specific union to the least specific union
		assertEquals(downwardUnions[0].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[0]).getLimitingDecision(), decision1);
		assertEquals(downwardUnions[1].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[1]).getLimitingDecision(), decision2);
		assertEquals(downwardUnions[2].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[2]).getLimitingDecision(), decision3);
		assertEquals(downwardUnions[3].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[3]).getLimitingDecision(), decision4);
		
		assertEquals(upwardUnions[0].getComplementaryUnion(), downwardUnions[3]); //at least 5 vs at most 4
		assertEquals(upwardUnions[0], downwardUnions[3].getComplementaryUnion());
		assertEquals(upwardUnions[1].getComplementaryUnion(), downwardUnions[2]); //at least 4 vs at most 3
		assertEquals(upwardUnions[1], downwardUnions[2].getComplementaryUnion());
		assertEquals(upwardUnions[2].getComplementaryUnion(), downwardUnions[1]); //at least 3 vs at most 2
		assertEquals(upwardUnions[2], downwardUnions[1].getComplementaryUnion());
		assertEquals(upwardUnions[3].getComplementaryUnion(), downwardUnions[0]); //at least 2 vs at most 1
		assertEquals(upwardUnions[3], downwardUnions[0].getComplementaryUnion());
	}
	
	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)},
	 * {@link UnionsWithSingleLimitingDecision#calculateUpwardUnions()}, {@link UnionsWithSingleLimitingDecision#calculateDownwardUnions()},
	 * {@link Unions#getUpwardUnions(boolean)}, {@link Unions#getDownwardUnions(boolean)}, and {@link UnionsWithSingleLimitingDecision#setComplementaryUnions()}.
	 * Tests if upward and downward unions are properly calculated, and if complementary unions are properly set.
	 * Employs cost-type decision attribute.
	 */
	@Test
	void testUnions06() {
		AttributePreferenceType decisionAttributePreferenceType = AttributePreferenceType.COST;
		
		InformationTableWithDecisionDistributions informationTableMock = this.getInformationTableMock03(decisionAttributePreferenceType);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = this.getRoughSetCalculatorMock();
		
		UnionsWithSingleLimitingDecision unions = new UnionsWithSingleLimitingDecision(informationTableMock, roughSetCalculatorMock);
		
		Union[] upwardUnions = unions.getUpwardUnions(true);
		Union[] downwardUnions = unions.getDownwardUnions(true);
		
		assertEquals(upwardUnions.length, 4);
		assertEquals(downwardUnions.length, 4);
		
		//as in getInformationTableMock03
		int attributeIndex = 1;

		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decisionAttributePreferenceType), attributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decisionAttributePreferenceType), attributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decisionAttributePreferenceType), attributeIndex);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decisionAttributePreferenceType), attributeIndex);
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5, decisionAttributePreferenceType), attributeIndex);
		
		//check upward unions (correctness of limiting decisions and order from the most specific union to the least specific union
		assertEquals(upwardUnions[0].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[0]).getLimitingDecision(), decision1);
		assertEquals(upwardUnions[1].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[1]).getLimitingDecision(), decision2);
		assertEquals(upwardUnions[2].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[2]).getLimitingDecision(), decision3);
		assertEquals(upwardUnions[3].getUnionType(), UnionType.AT_LEAST);
		assertEquals(((UnionWithSingleLimitingDecision)upwardUnions[3]).getLimitingDecision(), decision4);
		
		//check downward unions (correctness of limiting decisions and order from the most specific union to the least specific union
		assertEquals(downwardUnions[0].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[0]).getLimitingDecision(), decision5);
		assertEquals(downwardUnions[1].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[1]).getLimitingDecision(), decision4);
		assertEquals(downwardUnions[2].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[2]).getLimitingDecision(), decision3);
		assertEquals(downwardUnions[3].getUnionType(), UnionType.AT_MOST);
		assertEquals(((UnionWithSingleLimitingDecision)downwardUnions[3]).getLimitingDecision(), decision2);
		
		assertEquals(upwardUnions[0].getComplementaryUnion(), downwardUnions[3]); //at least 1 vs at most 2
		assertEquals(upwardUnions[0], downwardUnions[3].getComplementaryUnion());
		assertEquals(upwardUnions[1].getComplementaryUnion(), downwardUnions[2]); //at least 2 vs at most 3
		assertEquals(upwardUnions[1], downwardUnions[2].getComplementaryUnion());
		assertEquals(upwardUnions[2].getComplementaryUnion(), downwardUnions[1]); //at least 3 vs at most 4
		assertEquals(upwardUnions[2], downwardUnions[1].getComplementaryUnion());
		assertEquals(upwardUnions[3].getComplementaryUnion(), downwardUnions[0]); //at least 4 vs at most 5
		assertEquals(upwardUnions[3], downwardUnions[0].getComplementaryUnion());
	}

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#getLimitingDecisions()}.
	 */
	@Test
	void testGetLimitingDecisions() {
		AttributePreferenceType decisionAttributePreferenceType = AttributePreferenceType.GAIN;

		InformationTableWithDecisionDistributions informationTableMock = this.getInformationTableMock03(decisionAttributePreferenceType);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = this.getRoughSetCalculatorMock();
		
		UnionsWithSingleLimitingDecision unions = new UnionsWithSingleLimitingDecision(informationTableMock, roughSetCalculatorMock);
		
		//as in getInformationTableMock03
		int attributeIndex = 1;

		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decisionAttributePreferenceType), attributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decisionAttributePreferenceType), attributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decisionAttributePreferenceType), attributeIndex);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decisionAttributePreferenceType), attributeIndex);
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5, decisionAttributePreferenceType), attributeIndex);
		
		Decision[] limitingDecisions = unions.getLimitingDecisions();
		
		assertEquals(limitingDecisions[0], decision1);
		assertEquals(limitingDecisions[1], decision2);
		assertEquals(limitingDecisions[2], decision3);
		assertEquals(limitingDecisions[3], decision4);
		assertEquals(limitingDecisions[4], decision5);
	}

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#getLimitingDecisions(boolean)}.
	 */
	@Test
	void testGetLimitingDecisionsBoolean() {
		AttributePreferenceType decisionAttributePreferenceType = AttributePreferenceType.COST;

		InformationTableWithDecisionDistributions informationTableMock = this.getInformationTableMock03(decisionAttributePreferenceType);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = this.getRoughSetCalculatorMock();
		
		UnionsWithSingleLimitingDecision unions = new UnionsWithSingleLimitingDecision(informationTableMock, roughSetCalculatorMock);
		
		//as in getInformationTableMock03
		int attributeIndex = 1;

		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decisionAttributePreferenceType), attributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decisionAttributePreferenceType), attributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decisionAttributePreferenceType), attributeIndex);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decisionAttributePreferenceType), attributeIndex);
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5, decisionAttributePreferenceType), attributeIndex);
		
		Decision[] limitingDecisions = unions.getLimitingDecisions(true);
		
		assertEquals(limitingDecisions[0], decision5);
		assertEquals(limitingDecisions[1], decision4);
		assertEquals(limitingDecisions[2], decision3);
		assertEquals(limitingDecisions[3], decision2);
		assertEquals(limitingDecisions[4], decision1);
	}

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#getUnion(Union.UnionType, Decision)}.
	 */
	@Test
	void testGetUnion() {
		AttributePreferenceType decisionAttributePreferenceType = AttributePreferenceType.GAIN;
		
		InformationTableWithDecisionDistributions informationTableMock = this.getInformationTableMock03(decisionAttributePreferenceType);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = this.getRoughSetCalculatorMock();
		
		UnionsWithSingleLimitingDecision unions = new UnionsWithSingleLimitingDecision(informationTableMock, roughSetCalculatorMock);
		
		//as in getInformationTableMock03
		int attributeIndex = 1;

		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, decisionAttributePreferenceType), attributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, decisionAttributePreferenceType), attributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, decisionAttributePreferenceType), attributeIndex);
		Decision decision4 = new SimpleDecision(IntegerFieldFactory.getInstance().create(4, decisionAttributePreferenceType), attributeIndex);
		Decision decision5 = new SimpleDecision(IntegerFieldFactory.getInstance().create(5, decisionAttributePreferenceType), attributeIndex);
		
		assertEquals(unions.getUnion(UnionType.AT_LEAST, decision5), unions.getUpwardUnions()[0]);
		assertEquals(unions.getUnion(UnionType.AT_LEAST, decision4), unions.getUpwardUnions()[1]);
		assertEquals(unions.getUnion(UnionType.AT_LEAST, decision3), unions.getUpwardUnions()[2]);
		assertEquals(unions.getUnion(UnionType.AT_LEAST, decision2), unions.getUpwardUnions()[3]);
		
		assertEquals(unions.getUnion(UnionType.AT_MOST, decision1), unions.getDownwardUnions()[0]);
		assertEquals(unions.getUnion(UnionType.AT_MOST, decision2), unions.getDownwardUnions()[1]);
		assertEquals(unions.getUnion(UnionType.AT_MOST, decision3), unions.getDownwardUnions()[2]);
		assertEquals(unions.getUnion(UnionType.AT_MOST, decision4), unions.getDownwardUnions()[3]);
	}

}
