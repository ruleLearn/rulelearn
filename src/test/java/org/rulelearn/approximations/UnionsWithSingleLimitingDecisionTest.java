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

//TODO: extract tests related to methods already implemented in type Unions to class UnionsTest
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
	 * Test method for {@link UnionsWithSingleLimitingDecision#UnionsWithSingleLimitingDecision(InformationTableWithDecisionDistributions, approximations.DominanceBasedRoughSetCalculator)}.
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
	 * Test method for {@link UnionsWithSingleLimitingDecision#calculateUpwardUnions()}.
	 */
	@Test
	void testCalculateUpwardUnions() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#calculateDownwardUnions()}.
	 */
	@Test
	void testCalculateDownwardUnions() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Unions#getInformationTable()}.
	 */
	@Test
	void testGetInformationTable() {
		InformationTableWithDecisionDistributions informationTableMock = this.getInformationTableMock01();
		Unions unions = new UnionsWithSingleLimitingDecision(informationTableMock, this.getRoughSetCalculatorMock());
		
		assertEquals(unions.getInformationTable(), informationTableMock);
	}
	
	/**
	 * Test method for {@link Unions#getRoughSetCalculator()}.
	 */
	@Test
	void testGetRoughSetCalculator() {
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = this.getRoughSetCalculatorMock();
		Unions unions = new UnionsWithSingleLimitingDecision(this.getInformationTableMock01(), roughSetCalculatorMock);
		
		assertEquals(unions.getRoughSetCalculator(), roughSetCalculatorMock);
	}

	/**
	 * Test method for {@link Unions#getUpwardUnions()}.
	 */
	@Test
	void testGetUpwardUnions() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Unions#getUpwardUnions(boolean)}.
	 */
	@Test
	void testGetUpwardUnionsBoolean() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Unions#getDownwardUnions()}.
	 */
	@Test
	void testGetDownwardUnions() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Unions#getDownwardUnions(boolean)}.
	 */
	@Test
	void testGetDownwardUnionsBoolean() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#getLimitingDecisions()}.
	 */
	@Test
	void testGetLimitingDecisions() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#getLimitingDecisions(boolean)}.
	 */
	@Test
	void testGetLimitingDecisionsBoolean() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link UnionsWithSingleLimitingDecision#getUnion(Union.UnionType, Decision)}.
	 */
	@Test
	void testGetUnion() {
		//TODO: implement test
	}

}
