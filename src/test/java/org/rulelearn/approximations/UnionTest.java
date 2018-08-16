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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.CompositeDecision;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * Tests for {@link Union}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class UnionTest {

	/**
	 * Test method for {@link Union#findObjects()}.
	 */
	@Test
	void testFindObjects() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#calculateLowerApproximation()}.
	 */
	@Test
	void testCalculateLowerApproximation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#calculateUpperApproximation()}.
	 */
	@Test
	void testCalculateUpperApproximation() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#calculatePositiveRegion(it.unimi.dsi.fastutil.ints.IntSortedSet)}.
	 */
	@Test
	void testCalculatePositiveRegion() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#calculateNegativeRegion()}.
	 */
	@Test
	void testCalculateNegativeRegion() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#isConcordantWithDecision(Decision)}.
	 */
	@Test
	void testIsConcordantWithDecision() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#getComplementarySetSize()}.
	 */
	@Test
	void testGetComplementarySetSize() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 */
	@Test
	void testUnionUnion01() {
		UnionType unionType = null;
		Decision limitingDecision = Mockito.mock(Decision.class);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock);
			fail("Should not construct union with null union type.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 */
	@Test
	void testUnionUnion02() {
		UnionType unionType = UnionType.AT_LEAST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		Attribute attributeMock = Mockito.mock(IdentificationAttribute.class);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock);
			fail("Should not create union for limiting decision having contribution from an attribute which is not an evaluation attribute.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 */
	@Test
	void testUnionUnion03() {
		UnionType unionType = UnionType.AT_LEAST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(false);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock);
			fail("Should not create union for limiting decision having contribution from an attribute which is not active.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 */
	@Test
	void testUnionUnion04() {
		UnionType unionType = UnionType.AT_LEAST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.CONDITION);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock);
			fail("Should not create union for limiting decision having contribution from an attribute which is not decision one.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 */
	@Test
	void testUnionUnion05() {
		UnionType unionType = UnionType.AT_LEAST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock);
			fail("Should not create union if none of the attributes contributing to union's limiting decision is ordinal.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}.
	 */
	@Test
	void testUnionUnion06() {
		UnionType unionType = UnionType.AT_LEAST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			Union union = new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock);
			assertEquals(union.getUnionType(), unionType);
			assertEquals(union.getLimitingDecision(), limitingDecision);
			assertEquals(union.getInformationTable(), informationTableMock);
			assertEquals(union.getRoughSetCalculator(), roughSetCalculatorMock);
		} catch (Exception exception) {
			fail("Should create union for correct parameters.");
		}
	}

	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator, boolean)}.
	 */
	@Test
	void testUnionUnionBoolean01() {
		UnionType unionType = null;
		Decision limitingDecision = Mockito.mock(Decision.class);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, false);
			fail("Should not construct union with null union type.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator, boolean)}.
	 */
	@Test
	void testUnionUnionBoolean02() {
		UnionType unionType = UnionType.AT_MOST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		Attribute attributeMock = Mockito.mock(IdentificationAttribute.class);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, false);
			fail("Should not create union for limiting decision having contribution from an attribute which is not an evaluation attribute.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator, boolean)}.
	 */
	@Test
	void testUnionUnionBoolean03() {
		UnionType unionType = UnionType.AT_MOST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(false);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, false);
			fail("Should not create union for limiting decision having contribution from an attribute which is not active.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator, boolean)}.
	 */
	@Test
	void testUnionUnionBoolean04() {
		UnionType unionType = UnionType.AT_MOST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.CONDITION);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, false);
			fail("Should not create union for limiting decision having contribution from an attribute which is not decision one.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator, boolean)}.
	 */
	@Test
	void testUnionUnionBoolean05() {
		UnionType unionType = UnionType.AT_MOST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, false);
			fail("Should not create union if none of the attributes contributing to union's limiting decision is ordinal.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#Union(Union.UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator, boolean)}.
	 */
	@Test
	void testUnionUnionBoolean06() {
		UnionType unionType = UnionType.AT_MOST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		try {
			Union union = new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, false);
			assertEquals(union.getUnionType(), unionType);
			assertEquals(union.getLimitingDecision(), limitingDecision);
			assertEquals(union.getInformationTable(), informationTableMock);
			assertEquals(union.getRoughSetCalculator(), roughSetCalculatorMock);
			assertEquals(union.includeLimitingDecision, false);
		} catch (Exception exception) {
			fail("Should create union for correct parameters.");
		}
	}
	
	/**
	 * Internal structure for storing constructed union along with its construction parameters.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class UnionWithConstructorParameters {
		private Union union;
		private UnionType unionType;
		private Decision limitingDecision;
		private InformationTableWithDecisionDistributions informationTable;
		private DominanceBasedRoughSetCalculator roughSetCalculator;
		
		/**
		 * Constructs this structure.
		 * 
		 * @param union constructed union
		 * @param unionType type of constructed union
		 * @param limitingDecision limiting decision of constructed union
		 * @param informationTable information table for which union has been constructed
		 * @param roughSetCalculator rough set calculator used to construct union
		 */
		private UnionWithConstructorParameters(Union union, UnionType unionType, Decision limitingDecision,
				InformationTableWithDecisionDistributions informationTable,
				DominanceBasedRoughSetCalculator roughSetCalculator) {
			super();
			this.union = union;
			this.unionType = unionType;
			this.limitingDecision = limitingDecision;
			this.informationTable = informationTable;
			this.roughSetCalculator = roughSetCalculator;
		}
	}
	
	/**
	 * Gets test union, useful for testing protected methods.
	 * 
	 * @return structure holding constructed test union and parameters used to construct that union
	 */
	private UnionWithConstructorParameters getTestUnion() {
		UnionType unionType = UnionType.AT_LEAST;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		return new UnionWithConstructorParameters(
				new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock),
				unionType,
				limitingDecision,
				informationTableMock,
				roughSetCalculatorMock);
	}
	
	/**
	 * Test method for {@link Union#validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}.
	 * Tests {@link CompositeDecision} decision.
	 */
	@Test
	void testValidateLimitingDecision01() {
		int attributeIndex1 = 1;
		int attributeIndex2 = 2;
		
		Decision limitingDecision = new CompositeDecision(
				new EvaluationField[] {	
						IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN),
						IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST)
				},
				new int[] {
						attributeIndex1,
						attributeIndex2
				});
		//Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.isActive()).thenReturn(true);
		Mockito.when(attributeMock1.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(informationTableMock.getAttribute(attributeIndex1)).thenReturn(attributeMock1);
		
		IdentificationAttribute attributeMock2 = Mockito.mock(IdentificationAttribute.class); //!
		Mockito.when(informationTableMock.getAttribute(attributeIndex2)).thenReturn(attributeMock2);
		
		Union union = Mockito.mock(Union.class);
		Mockito.when(union.validateLimitingDecision(limitingDecision, informationTableMock)).thenCallRealMethod();
		
		try {
			union.validateLimitingDecision(limitingDecision, informationTableMock);
			fail("Should not validate limiting decision having contribution from an attribute which is not an evaluation attribute.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}.
	 */
	@Test
	void testValidateLimitingDecision02() {
		int attributeIndex1 = 1;
		int attributeIndex2 = 2;
		
		Decision limitingDecision = new CompositeDecision(
				new EvaluationField[] {	
						IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN),
						IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST)
				},
				new int[] {
						attributeIndex1,
						attributeIndex2
				});
		//Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.isActive()).thenReturn(true);
		Mockito.when(attributeMock1.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(informationTableMock.getAttribute(attributeIndex1)).thenReturn(attributeMock1);
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.isActive()).thenReturn(false);
		Mockito.when(attributeMock2.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(informationTableMock.getAttribute(attributeIndex2)).thenReturn(attributeMock2);
		
		Union union = Mockito.mock(Union.class);
		Mockito.when(union.validateLimitingDecision(limitingDecision, informationTableMock)).thenCallRealMethod();
		
		try {
			union.validateLimitingDecision(limitingDecision, informationTableMock);
			fail("Should not validate limiting decision having contribution from an attribute which is not active.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}.
	 */
	@Test
	void testValidateLimitingDecision03() {
		int attributeIndex1 = 1;
		int attributeIndex2 = 2;
		
		Decision limitingDecision = new CompositeDecision(
				new EvaluationField[] {	
						IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN),
						IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST)
				},
				new int[] {
						attributeIndex1,
						attributeIndex2
				});
		//Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.isActive()).thenReturn(true);
		Mockito.when(attributeMock1.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(informationTableMock.getAttribute(attributeIndex1)).thenReturn(attributeMock1);
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.isActive()).thenReturn(true);
		Mockito.when(attributeMock2.getType()).thenReturn(AttributeType.CONDITION);
		Mockito.when(informationTableMock.getAttribute(attributeIndex2)).thenReturn(attributeMock2);
		
		Union union = Mockito.mock(Union.class);
		Mockito.when(union.validateLimitingDecision(limitingDecision, informationTableMock)).thenCallRealMethod();
		
		try {
			union.validateLimitingDecision(limitingDecision, informationTableMock);
			fail("Should not validate limiting decision having contribution from an attribute which is not decision one.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}.
	 */
	@Test
	void testValidateLimitingDecision04() {
		int attributeIndex1 = 1;
		int attributeIndex2 = 2;
		
		Decision limitingDecision = new CompositeDecision(
				new EvaluationField[] {	
						IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE),
						IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE)
				},
				new int[] {
						attributeIndex1,
						attributeIndex2
				});
		//Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.isActive()).thenReturn(true);
		Mockito.when(attributeMock1.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(informationTableMock.getAttribute(attributeIndex1)).thenReturn(attributeMock1);
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.isActive()).thenReturn(true);
		Mockito.when(attributeMock2.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(informationTableMock.getAttribute(attributeIndex2)).thenReturn(attributeMock2);
		
		Union union = Mockito.mock(Union.class);
		Mockito.when(union.validateLimitingDecision(limitingDecision, informationTableMock)).thenCallRealMethod();
		
		try {
			union.validateLimitingDecision(limitingDecision, informationTableMock);
			fail("Should not validate limiting decision if none of the attributes contributing to that decision is ordinal.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link Union#validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}.
	 */
	@Test
	void testValidateLimitingDecision05() {
		int attributeIndex1 = 1;
		int attributeIndex2 = 2;
		
		Decision limitingDecision = new CompositeDecision(
				new EvaluationField[] {	
						IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE),
						IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST)
				},
				new int[] {
						attributeIndex1,
						attributeIndex2
				});
		//Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), attributeIndex);
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.isActive()).thenReturn(true);
		Mockito.when(attributeMock1.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(informationTableMock.getAttribute(attributeIndex1)).thenReturn(attributeMock1);
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.isActive()).thenReturn(true);
		Mockito.when(attributeMock2.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(informationTableMock.getAttribute(attributeIndex2)).thenReturn(attributeMock2);
		
		Union union = Mockito.mock(Union.class);
		Mockito.when(union.validateLimitingDecision(limitingDecision, informationTableMock)).thenCallRealMethod();
		
		try {
			assertTrue(union.validateLimitingDecision(limitingDecision, informationTableMock));
		} catch (Exception exception) {
			fail("Should validate correct decision.");
		}
	}

	/**
	 * Test method for {@link Union#setComplementaryUnion(Union)}.
	 */
	@Test
	void testSetComplementaryUnion() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#getComplementaryUnion()}.
	 */
	@Test
	void testGetComplementaryUnion() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#calculateComplementaryUnion()}.
	 */
	@Test
	void testCalculateComplementaryUnion() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#getUnionType()}.
	 */
	@Test
	void testGetUnionType() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestUnion();
		assertEquals(unionWithConstructorParameters.union.getUnionType(), unionWithConstructorParameters.unionType);
	}
	
	/**
	 * Test method for {@link Union#getLimitingDecision()}.
	 * This method is inherited from superclass.
	 */
	@Test
	void testGetLimitingDecision() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestUnion();
		assertEquals(unionWithConstructorParameters.union.getLimitingDecision(), unionWithConstructorParameters.limitingDecision);
	}

	/**
	 * Test method for {@link Union#getRoughSetCalculator()}.
	 */
	@Test
	void testGetRoughSetCalculator() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestUnion();
		assertEquals(unionWithConstructorParameters.union.getRoughSetCalculator(), unionWithConstructorParameters.roughSetCalculator);
	}

	/**
	 * Test method for {@link Union#isDecisionPositive(Decision)}.
	 */
	@Test
	void testIsDecisionPositive() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#isDecisionNegative(Decision)}.
	 */
	@Test
	void testIsDecisionNegative() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#isDecisionNeutral(Decision)}.
	 */
	@Test
	void testIsDecisionNeutral() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#getInformationTable()}.
	 */
	@Test
	void testGetInformationTable() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestUnion();
		assertEquals(unionWithConstructorParameters.union.getInformationTable(), unionWithConstructorParameters.informationTable);
	}

	/**
	 * Test method for {@link Union#isObjectPositive(int)}.
	 */
	@Test
	void testIsObjectPositive() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#isObjectNeutral(int)}.
	 */
	@Test
	void testIsObjectNeutral() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#isObjectNegative(int)}.
	 */
	@Test
	void testIsObjectNegative() {
		//TODO: implement test
	}

}
