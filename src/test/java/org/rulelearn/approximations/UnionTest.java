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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.CompositeDecision;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

//TODO: extract common code blocks into separate method
/**
 * Tests for {@link Union}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class UnionTest {
	
	/**
	 * Configures mock of information table referenced in given union.
	 * 
	 * @param union union referencing information table mock that should be configured
	 */
	private void configureInformationTableMock(Union union) {
		SimpleDecision limitingDecision = (SimpleDecision)union.getLimitingDecision();
		int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue();
		AttributePreferenceType attributePreferenceType = ((IntegerField)limitingDecision.getEvaluation()).getPreferenceType();
		int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[1])[0];
		
		InformationTable informationTableMock = union.getInformationTable();
		
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(7); //reconfigure information table mock
		
		Mockito.when(informationTableMock.getDecision(0)).thenReturn(
				new SimpleDecision(IntegerFieldFactory.getInstance().create(limitingDecisionValue, attributePreferenceType), attributeIndex));
		Mockito.when(informationTableMock.getDecision(1)).thenReturn(
				new SimpleDecision(IntegerFieldFactory.getInstance().create(limitingDecisionValue + 1, attributePreferenceType), attributeIndex));
		Mockito.when(informationTableMock.getDecision(2)).thenReturn(
				new SimpleDecision(IntegerFieldFactory.getInstance().create(limitingDecisionValue - 1, attributePreferenceType), attributeIndex));
		Mockito.when(informationTableMock.getDecision(3)).thenReturn(
				new SimpleDecision(IntegerFieldFactory.getInstance().create(limitingDecisionValue + 2, attributePreferenceType), attributeIndex));
		Mockito.when(informationTableMock.getDecision(4)).thenReturn(
				new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex)); //uncomparable decision
		Mockito.when(informationTableMock.getDecision(5)).thenReturn(
				new SimpleDecision(IntegerFieldFactory.getInstance().create(limitingDecisionValue - 2, attributePreferenceType), attributeIndex));
		Mockito.when(informationTableMock.getDecision(6)).thenReturn(
				new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex)); //in union
	}
	
	/**
	 * Test method for {@link Union#findObjects()}.
	 */
	@Test
	void testFindObjects01() {
		Union union = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		
		union.findObjects();
		
		assertEquals(union.objects.size(), 4);
		assertTrue(union.objects.contains(0));
		assertTrue(union.objects.contains(1));
		assertTrue(union.objects.contains(3));
		assertTrue(union.objects.contains(6));
		assertEquals(union.neutralObjects.size(), 1);
		assertTrue(union.neutralObjects.contains(4));
		
		try {
			union.objects.add(10);
			fail("Should not modify list of positive objects.");
		} catch (UnsupportedOperationException exception) {
			//OK
		}
		
		try {
			union.neutralObjects.add(10);
			fail("Should not modify list of neutral objects.");
		} catch (UnsupportedOperationException exception) {
			//OK
		}
		
	}
	
	/**
	 * Test method for {@link Union#findObjects()}.
	 */
	@Test
	void testFindObjects02() {
		Union union = getTestAtMostUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		
		union.findObjects();
		
		assertEquals(union.objects.size(), 4);
		assertTrue(union.objects.contains(0));
		assertTrue(union.objects.contains(2));
		assertTrue(union.objects.contains(5));
		assertTrue(union.objects.contains(6));
		assertEquals(union.neutralObjects.size(), 1);
		assertTrue(union.neutralObjects.contains(4));
		
		try {
			union.objects.add(5);
			fail("Should not modify list of positive objects.");
		} catch (UnsupportedOperationException exception) {
			//OK
		}
		
		try {
			union.neutralObjects.add(5);
			fail("Should not modify list of neutral objects.");
		} catch (UnsupportedOperationException exception) {
			//OK
		}
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
	 * Tests union "at least".
	 */
	@Test
	void testCalculateNegativeRegion() {
		//TODO: implement test
	}

	/**
	 * Test method for {@link Union#isConcordantWithDecision(Decision)}.
	 */
	@Test
	void testIsConcordantWithDecision01() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
	
			assertEquals(union.isConcordantWithDecision(equalDecision), TernaryLogicValue.TRUE);
			assertEquals(union.isConcordantWithDecision(betterDecision), TernaryLogicValue.TRUE);
			assertEquals(union.isConcordantWithDecision(worseDecision), TernaryLogicValue.FALSE);
			assertEquals(union.isConcordantWithDecision(comparableDecision), TernaryLogicValue.TRUE);
			assertEquals(union.isConcordantWithDecision(uncomparableDecision), TernaryLogicValue.UNCOMPARABLE);
			
			assertEquals(strictUnion.isConcordantWithDecision(equalDecision), TernaryLogicValue.FALSE);
			assertEquals(strictUnion.isConcordantWithDecision(betterDecision), TernaryLogicValue.TRUE);
			assertEquals(strictUnion.isConcordantWithDecision(worseDecision), TernaryLogicValue.FALSE);
			assertEquals(strictUnion.isConcordantWithDecision(comparableDecision), TernaryLogicValue.FALSE);
			assertEquals(strictUnion.isConcordantWithDecision(uncomparableDecision), TernaryLogicValue.UNCOMPARABLE);
		}
	}
	
	/**
	 * Test method for {@link Union#isConcordantWithDecision(Decision)}.
	 * Tests union "at most".
	 */
	@Test
	void testIsConcordantWithDecision02() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
	
			assertEquals(union.isConcordantWithDecision(equalDecision), TernaryLogicValue.TRUE);
			assertEquals(union.isConcordantWithDecision(betterDecision), TernaryLogicValue.FALSE);
			assertEquals(union.isConcordantWithDecision(worseDecision), TernaryLogicValue.TRUE);
			assertEquals(union.isConcordantWithDecision(comparableDecision), TernaryLogicValue.TRUE);
			assertEquals(union.isConcordantWithDecision(uncomparableDecision), TernaryLogicValue.UNCOMPARABLE);
			
			assertEquals(strictUnion.isConcordantWithDecision(equalDecision), TernaryLogicValue.FALSE);
			assertEquals(strictUnion.isConcordantWithDecision(betterDecision), TernaryLogicValue.FALSE);
			assertEquals(strictUnion.isConcordantWithDecision(worseDecision), TernaryLogicValue.TRUE);
			assertEquals(strictUnion.isConcordantWithDecision(comparableDecision), TernaryLogicValue.FALSE);
			assertEquals(strictUnion.isConcordantWithDecision(uncomparableDecision), TernaryLogicValue.UNCOMPARABLE);
		}
	}

	/**
	 * Test method for {@link Union#getComplementarySetSize()}.
	 */
	@Test
	void testGetComplementarySetSize01() {
		Union union = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertEquals(union.getComplementarySetSize(), 2);
	}
	
	/**
	 * Test method for {@link Union#getComplementarySetSize()}.
	 */
	@Test
	void testGetComplementarySetSize02() {
		Union union = getTestAtMostUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertEquals(union.getComplementarySetSize(), 2);
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
	 * Gets test "at least" union with {@link SimpleDecision} limiting decision, useful for testing protected methods.
	 * This decision has one {@link IntegerField} contributing evaluation.
	 * 
	 * @param preferenceType attribute preference type; should be {@link AttributePreferenceType#GAIN} or {@link AttributePreferenceType#COST}
	 * @return structure holding constructed test union and parameters used to construct that union
	 */
	private UnionWithConstructorParameters getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType preferenceType, boolean includeLimitingDecision) {
		UnionType unionType = UnionType.AT_LEAST;
		
		int value = 10;
		int attributeIndex = 1;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(value, preferenceType), attributeIndex);
		
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock.getPreferenceType()).thenReturn(preferenceType);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(0); //avoids looping in findObjects() method
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		return new UnionWithConstructorParameters(
				new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, includeLimitingDecision),
				unionType,
				limitingDecision,
				informationTableMock,
				roughSetCalculatorMock);
	}
	
	/**
	 * Gets test "at least" union with {@link SimpleDecision} limiting decision, useful for testing protected methods.
	 * This decision has one {@link IntegerField} contributing evaluation.
	 * 
	 * @param preferenceType attribute preference type; should be {@link AttributePreferenceType#GAIN} or {@link AttributePreferenceType#COST}
	 * @return structure holding constructed test union and parameters used to construct that union
	 */
	private UnionWithConstructorParameters getTestAtMostUnionWithSimpleLimitingDecision(AttributePreferenceType preferenceType, boolean includeLimitingDecision) {
		UnionType unionType = UnionType.AT_MOST;
		
		int value = 5;
		int attributeIndex = 2;
		Decision limitingDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(value, preferenceType), attributeIndex);
		
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		
		EvaluationAttribute attributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock.isActive()).thenReturn(true);
		Mockito.when(attributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(attributeMock.getPreferenceType()).thenReturn(preferenceType);
		Mockito.when(informationTableMock.getAttribute(attributeIndex)).thenReturn(attributeMock);
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(0); //avoids looping in findObjects() method
		
		DominanceBasedRoughSetCalculator roughSetCalculatorMock = Mockito.mock(DominanceBasedRoughSetCalculator.class);
		
		return new UnionWithConstructorParameters(
				new Union(unionType, limitingDecision, informationTableMock, roughSetCalculatorMock, includeLimitingDecision),
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
	 * Tests {@link CompositeDecision} decision.
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
	 * Tests {@link CompositeDecision} decision.
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
	 * Tests {@link CompositeDecision} decision.
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
	 * Tests {@link CompositeDecision} decision.
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
		Union union =  getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		Union complementaryUnion = Mockito.mock(Union.class);
		Union complementaryUnion2 = Mockito.mock(Union.class);
		
		assertTrue(union.setComplementaryUnion(complementaryUnion));
		assertEquals(union.complementaryUnion, complementaryUnion);
		
		assertFalse(union.setComplementaryUnion(complementaryUnion2));
		assertEquals(union.complementaryUnion, complementaryUnion); //still the same complementary union
	}

	/**
	 * Test method for {@link Union#getComplementaryUnion()}.
	 */
	@Test
	void testGetComplementaryUnion01() {
		Union union = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		Union complementaryUnion = Mockito.mock(Union.class);
		union.setComplementaryUnion(complementaryUnion);
		
		assertEquals(union.complementaryUnion, complementaryUnion);
		assertEquals(union.getComplementaryUnion(), complementaryUnion);
	}
	
	/**
	 * Test method for {@link Union#getComplementaryUnion()}.
	 */
	@Test
	void testGetComplementaryUnion02() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true);
		Union union = unionWithConstructorParameters.union;
		assertEquals(union.complementaryUnion, null);
		Union complementaryUnion = union.getComplementaryUnion();
		assertNotNull(complementaryUnion); //tests if complementary union is calculated on demand
		assertEquals(complementaryUnion.getLimitingDecision(), union.getLimitingDecision());
		assertEquals(complementaryUnion.getUnionType(), UnionType.AT_MOST);
		assertFalse(complementaryUnion.isIncludeLimitingDecision());
	}
	
	/**
	 * Test method for {@link Union#getComplementaryUnion()}.
	 */
	@Test
	void testGetComplementaryUnion03() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(AttributePreferenceType.COST, true);
		Union union = unionWithConstructorParameters.union;
		assertEquals(union.complementaryUnion, null);
		Union complementaryUnion = union.getComplementaryUnion();
		assertNotNull(complementaryUnion); //tests if complementary union is calculated on demand
		assertEquals(complementaryUnion.getLimitingDecision(), union.getLimitingDecision());
		assertEquals(complementaryUnion.getUnionType(), UnionType.AT_LEAST);
		assertFalse(complementaryUnion.isIncludeLimitingDecision());
	}

//	/**
//	 * Test method for {@link Union#calculateComplementaryUnion()}.
//	 */
//	@Test
//	void testCalculateComplementaryUnion() {
//	}

	/**
	 * Test method for {@link Union#getUnionType()}.
	 */
	@Test
	void testGetUnionType() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true);
		assertEquals(unionWithConstructorParameters.union.getUnionType(), unionWithConstructorParameters.unionType);
	}
	
	/**
	 * Test method for {@link Union#getLimitingDecision()}.
	 * This method is inherited from superclass.
	 */
	@Test
	void testGetLimitingDecision() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true);
		assertEquals(unionWithConstructorParameters.union.getLimitingDecision(), unionWithConstructorParameters.limitingDecision);
	}

	/**
	 * Test method for {@link Union#getRoughSetCalculator()}.
	 */
	@Test
	void testGetRoughSetCalculator() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, false);
		assertEquals(unionWithConstructorParameters.union.getRoughSetCalculator(), unionWithConstructorParameters.roughSetCalculator);
	}

	/**
	 * Test method for {@link Union#isDecisionPositive(Decision)}.
	 * Tests union "at least".
	 */
	@Test
	void testIsDecisionPositive01() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
	
			assertTrue(union.isDecisionPositive(equalDecision));
			assertTrue(union.isDecisionPositive(betterDecision));
			assertFalse(union.isDecisionPositive(worseDecision));
			assertTrue(union.isDecisionPositive(comparableDecision));
			assertFalse(union.isDecisionPositive(uncomparableDecision));
			
			assertFalse(strictUnion.isDecisionPositive(equalDecision));
			assertTrue(strictUnion.isDecisionPositive(betterDecision));
			assertFalse(strictUnion.isDecisionPositive(worseDecision));
			assertFalse(strictUnion.isDecisionPositive(comparableDecision));
			assertFalse(strictUnion.isDecisionPositive(uncomparableDecision));
		}
	}
	
	/**
	 * Test method for {@link Union#isDecisionPositive(Decision)}.
	 * Tests union "at most".
	 */
	@Test
	void testIsDecisionPositive02() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
			
			assertTrue(union.isDecisionPositive(equalDecision));
			assertFalse(union.isDecisionPositive(betterDecision));
			assertTrue(union.isDecisionPositive(worseDecision));
			assertTrue(union.isDecisionPositive(comparableDecision));
			assertFalse(union.isDecisionPositive(uncomparableDecision));
			
			assertFalse(strictUnion.isDecisionPositive(equalDecision));
			assertFalse(strictUnion.isDecisionPositive(betterDecision));
			assertTrue(strictUnion.isDecisionPositive(worseDecision));
			assertFalse(strictUnion.isDecisionPositive(comparableDecision));
			assertFalse(strictUnion.isDecisionPositive(uncomparableDecision));
		}
	}

	/**
	 * Test method for {@link Union#isDecisionNegative(Decision)}.
	 * Tests union "at least".
	 */
	@Test
	void testIsDecisionNegative01() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
	
			assertFalse(union.isDecisionNegative(equalDecision));
			assertFalse(union.isDecisionNegative(betterDecision));
			assertTrue(union.isDecisionNegative(worseDecision));
			assertFalse(union.isDecisionNegative(comparableDecision));
			assertFalse(union.isDecisionNegative(uncomparableDecision));
			
			assertTrue(strictUnion.isDecisionNegative(equalDecision));
			assertFalse(strictUnion.isDecisionNegative(betterDecision));
			assertTrue(strictUnion.isDecisionNegative(worseDecision));
			assertTrue(strictUnion.isDecisionNegative(comparableDecision));
			assertFalse(strictUnion.isDecisionNegative(uncomparableDecision));
		}
	}
		
	/**
	 * Test method for {@link Union#isDecisionNegative(Decision)}.
	 * Tests union "at most".
	 */
	@Test
	void testIsDecisionNegative02() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
	
			assertFalse(union.isDecisionNegative(equalDecision));
			assertTrue(union.isDecisionNegative(betterDecision));
			assertFalse(union.isDecisionNegative(worseDecision));
			assertFalse(union.isDecisionNegative(comparableDecision));
			assertFalse(union.isDecisionNegative(uncomparableDecision));
			
			assertTrue(strictUnion.isDecisionNegative(equalDecision));
			assertTrue(strictUnion.isDecisionNegative(betterDecision));
			assertFalse(strictUnion.isDecisionNegative(worseDecision));
			assertTrue(strictUnion.isDecisionNegative(comparableDecision));
			assertFalse(strictUnion.isDecisionNegative(uncomparableDecision));
		}
	}

	/**
	 * Test method for {@link Union#isDecisionNeutral(Decision)}.
	 * Tests union "at least".
	 */
	@Test
	void testIsDecisionNeutral01() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
	
			assertFalse(union.isDecisionNeutral(equalDecision));
			assertFalse(union.isDecisionNeutral(betterDecision));
			assertFalse(union.isDecisionNeutral(worseDecision));
			assertFalse(union.isDecisionNeutral(comparableDecision));
			assertTrue(union.isDecisionNeutral(uncomparableDecision));
			
			assertFalse(strictUnion.isDecisionNeutral(equalDecision));
			assertFalse(strictUnion.isDecisionNeutral(betterDecision));
			assertFalse(strictUnion.isDecisionNeutral(worseDecision));
			assertFalse(strictUnion.isDecisionNeutral(comparableDecision));
			assertTrue(strictUnion.isDecisionNeutral(uncomparableDecision));
		}
	}
	
	/**
	 * Test method for {@link Union#isDecisionNeutral(Decision)}.
	 * Tests union "at most".
	 */
	@Test
	void testIsDecisionNeutral02() {
		AttributePreferenceType[] attributePreferenceTypes = new AttributePreferenceType[] {AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		UnionWithConstructorParameters unionWithConstructorParameters;
		UnionWithConstructorParameters strictUnionWithConstructorParameters;
		Union union;
		Union strictUnion;
		SimpleDecision limitingDecision;
		
		for (int i = 0; i < attributePreferenceTypes.length; i++) {
			unionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], true);
			union = unionWithConstructorParameters.union;
			
			strictUnionWithConstructorParameters = getTestAtMostUnionWithSimpleLimitingDecision(attributePreferenceTypes[i], false);
			strictUnion = strictUnionWithConstructorParameters.union;
			
			limitingDecision = (SimpleDecision)unionWithConstructorParameters.limitingDecision; //ensure type of limiting decision is SimpleDecision
			
			int attributeIndex = limitingDecision.getAttributeIndices().toArray(new int[limitingDecision.getNumberOfEvaluations()])[0]; //get attribute index
			AttributePreferenceType preferenceType = ((KnownSimpleField)limitingDecision.getEvaluation(attributeIndex)).getPreferenceType(); //get preference type
			int limitingDecisionValue = ((IntegerField)limitingDecision.getEvaluation()).getValue(); //get value
	
			int equalValue = limitingDecisionValue;
			Decision equalDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(equalValue, preferenceType), attributeIndex);
			
			int betterValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue + 1 : limitingDecisionValue - 1); //take better value for tested decision
			Decision betterDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(betterValue, preferenceType), attributeIndex);
			
			int worseValue = (preferenceType == AttributePreferenceType.GAIN ? limitingDecisionValue - 1 : limitingDecisionValue + 1); //take worse value for tested decision
			Decision worseDecision = new SimpleDecision(IntegerFieldFactory.getInstance().create(worseValue, preferenceType), attributeIndex);
			
			Decision comparableDecision = new SimpleDecision(new UnknownSimpleFieldMV2(), attributeIndex); //take missing value of type mv_2 for tested decision
			
			Decision uncomparableDecision = new SimpleDecision(new UnknownSimpleFieldMV15(), attributeIndex); //take missing value of type mv_{1.5} for tested decision
	
			assertFalse(union.isDecisionNeutral(equalDecision));
			assertFalse(union.isDecisionNeutral(betterDecision));
			assertFalse(union.isDecisionNeutral(worseDecision));
			assertFalse(union.isDecisionNeutral(comparableDecision));
			assertTrue(union.isDecisionNeutral(uncomparableDecision));
			
			assertFalse(strictUnion.isDecisionNeutral(equalDecision));
			assertFalse(strictUnion.isDecisionNeutral(betterDecision));
			assertFalse(strictUnion.isDecisionNeutral(worseDecision));
			assertFalse(strictUnion.isDecisionNeutral(comparableDecision));
			assertTrue(strictUnion.isDecisionNeutral(uncomparableDecision));
		}
	}

	/**
	 * Test method for {@link Union#getInformationTable()}.
	 */
	@Test
	void testGetInformationTable() {
		UnionWithConstructorParameters unionWithConstructorParameters = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, false);
		assertEquals(unionWithConstructorParameters.union.getInformationTable(), unionWithConstructorParameters.informationTable);
	}

	/**
	 * Test method for {@link Union#isObjectPositive(int)}.
	 */
	@Test
	void testIsObjectPositive01() {
		Union union = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertTrue(union.isObjectPositive(0));
		assertTrue(union.isObjectPositive(1));
		assertFalse(union.isObjectPositive(2));
		assertTrue(union.isObjectPositive(3));
		assertFalse(union.isObjectPositive(4));
		assertFalse(union.isObjectPositive(5));
		assertTrue(union.isObjectPositive(6));
	}
	
	/**
	 * Test method for {@link Union#isObjectPositive(int)}.
	 */
	@Test
	void testIsObjectPositive02() {
		Union union = getTestAtMostUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertTrue(union.isObjectPositive(0));
		assertFalse(union.isObjectPositive(1));
		assertTrue(union.isObjectPositive(2));
		assertFalse(union.isObjectPositive(3));
		assertFalse(union.isObjectPositive(4));
		assertTrue(union.isObjectPositive(5));
		assertTrue(union.isObjectPositive(6));
	}

	/**
	 * Test method for {@link Union#isObjectNeutral(int)}.
	 */
	@Test
	void testIsObjectNeutral01() {
		Union union = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertFalse(union.isObjectNeutral(0));
		assertFalse(union.isObjectNeutral(1));
		assertFalse(union.isObjectNeutral(2));
		assertFalse(union.isObjectNeutral(3));
		assertTrue(union.isObjectNeutral(4));
		assertFalse(union.isObjectNeutral(5));
		assertFalse(union.isObjectNeutral(6));
	}
	
	/**
	 * Test method for {@link Union#isObjectNeutral(int)}.
	 */
	@Test
	void testIsObjectNeutral02() {
		Union union = getTestAtMostUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertFalse(union.isObjectNeutral(0));
		assertFalse(union.isObjectNeutral(1));
		assertFalse(union.isObjectNeutral(2));
		assertFalse(union.isObjectNeutral(3));
		assertTrue(union.isObjectNeutral(4));
		assertFalse(union.isObjectNeutral(5));
		assertFalse(union.isObjectNeutral(6));
	}

	/**
	 * Test method for {@link Union#isObjectNegative(int)}.
	 */
	@Test
	void testIsObjectNegative01() {
		Union union = getTestAtLeastUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertFalse(union.isObjectNegative(0));
		assertFalse(union.isObjectNegative(1));
		assertTrue(union.isObjectNegative(2));
		assertFalse(union.isObjectNegative(3));
		assertFalse(union.isObjectNegative(4));
		assertTrue(union.isObjectNegative(5));
		assertFalse(union.isObjectNegative(6));
	}
	
	/**
	 * Test method for {@link Union#isObjectNegative(int)}.
	 */
	@Test
	void testIsObjectNegative02() {
		Union union = getTestAtMostUnionWithSimpleLimitingDecision(AttributePreferenceType.GAIN, true).union;
		this.configureInformationTableMock(union);
		union.findObjects();
		
		assertFalse(union.isObjectNegative(0));
		assertTrue(union.isObjectNegative(1));
		assertFalse(union.isObjectNegative(2));
		assertTrue(union.isObjectNegative(3));
		assertFalse(union.isObjectNegative(4));
		assertFalse(union.isObjectNegative(5));
		assertFalse(union.isObjectNegative(6));
	}

}
