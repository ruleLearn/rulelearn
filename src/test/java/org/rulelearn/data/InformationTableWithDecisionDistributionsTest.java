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

package org.rulelearn.data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * Tests for {@link InformationTableWithDecisionDistributions}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class InformationTableWithDecisionDistributionsTest {

	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfField01() {
		try {
			List<Field[]> listOfFields = new ArrayList<>();
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			new InformationTableWithDecisionDistributions(null, listOfFields);
			fail("Should not create information table with null attributes.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfField02() {
		try {
			new InformationTableWithDecisionDistributions(new Attribute[] {Mockito.mock(EvaluationAttribute.class)}, null);
			fail("Should not create information table with null list of fields.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfField03() {
		try {
			List<Field[]> listOfFields = new ArrayList<>();
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			new InformationTableWithDecisionDistributions(new Attribute[] {Mockito.mock(EvaluationAttribute.class)}, listOfFields);
			fail("Should not create information table with not matching number of attributes and number of fields corresponding to one object.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfField04() {
		try {
			List<Field[]> listOfFields = new ArrayList<>();
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			new InformationTableWithDecisionDistributions(new Attribute[] {Mockito.mock(EvaluationAttribute.class)}, listOfFields);
			fail("Should not create information table with decision distributions when there is no active decision attribute.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfFieldBoolean01() {
		try {
			List<Field[]> listOfFields = new ArrayList<>();
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			new InformationTableWithDecisionDistributions(null, listOfFields, true);
			fail("Should not create information table with null attributes.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfFieldBoolean02() {
		try {
			new InformationTableWithDecisionDistributions(new Attribute[] {Mockito.mock(EvaluationAttribute.class)}, null, true);
			fail("Should not create information table with null list of fields.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfFieldBoolean03() {
		try {
			List<Field[]> listOfFields = new ArrayList<>();
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			new InformationTableWithDecisionDistributions(new Attribute[] {Mockito.mock(EvaluationAttribute.class)}, listOfFields, true);
			fail("Should not create information table with not matching number of attributes and number of fields corresponding to one object.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}
	
	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsAttributeArrayListOfFieldBoolean04() {
		try {
			List<Field[]> listOfFields = new ArrayList<>();
			listOfFields.add(new Field[] {IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN)});
			new InformationTableWithDecisionDistributions(new Attribute[] {Mockito.mock(EvaluationAttribute.class)}, listOfFields, true);
			fail("Should not create information table with decision distributions when there is no active decision attribute.");
		} catch (InvalidValueException exception) {
			//OK
		}
	}

	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#getDominanceConesDecisionDistributions()}.
	 */
	@Test
	void testGetDominanceConesDecisionDistributions() {
		List<Field[]> listOfFields = new ArrayList<>();
		EvaluationField decisionEvaluation = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		listOfFields.add(new Field[] {
				IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN),
				decisionEvaluation});
		
		EvaluationAttribute conditionAttributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(conditionAttributeMock.getType()).thenReturn(AttributeType.CONDITION);
		Mockito.when(conditionAttributeMock.isActive()).thenReturn(true);
		
		EvaluationAttribute decisionAttributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(decisionAttributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(decisionAttributeMock.isActive()).thenReturn(true);
		
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(new Attribute[] {conditionAttributeMock, decisionAttributeMock}, listOfFields, false);
		
		Decision decision = new SimpleDecision(decisionEvaluation, 1);
		
		assertNotNull(informationTable.getDominanceConesDecisionDistributions());
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getPositiveDConeDecisionClassDistribution(0).getDecisions().size(), 1);
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getPositiveDConeDecisionClassDistribution(0).getCount(decision), 1);
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getNegativeDConeDecisionClassDistribution(0).getDecisions().size(), 1);
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getNegativeDConeDecisionClassDistribution(0).getCount(decision), 1);
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getPositiveInvDConeDecisionClassDistribution(0).getDecisions().size(), 1);
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getPositiveInvDConeDecisionClassDistribution(0).getCount(decision), 1);
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getNegativeInvDConeDecisionClassDistribution(0).getDecisions().size(), 1);
		assertEquals(informationTable.getDominanceConesDecisionDistributions().getNegativeInvDConeDecisionClassDistribution(0).getCount(decision), 1);
	}

	/**
	 * Test method for {@link InformationTableWithDecisionDistributions#getDecisionDistribution()}.
	 */
	@Test
	void testGetDecisionDistribution() {
		List<Field[]> listOfFields = new ArrayList<>();
		EvaluationField decisionEvaluation = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		listOfFields.add(new Field[] {
				IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN),
				decisionEvaluation});
		
		EvaluationAttribute conditionAttributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(conditionAttributeMock.getType()).thenReturn(AttributeType.CONDITION);
		Mockito.when(conditionAttributeMock.isActive()).thenReturn(true);
		
		EvaluationAttribute decisionAttributeMock = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(decisionAttributeMock.getType()).thenReturn(AttributeType.DECISION);
		Mockito.when(decisionAttributeMock.isActive()).thenReturn(true);
		
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(new Attribute[] {conditionAttributeMock, decisionAttributeMock}, listOfFields, false);
		
		Decision decision = new SimpleDecision(decisionEvaluation, 1);
		
		assertNotNull(informationTable.getDecisionDistribution());
		assertEquals(informationTable.getDecisionDistribution().getDecisions().size(), 1);
		assertEquals(informationTable.getDecisionDistribution().getCount(decision), 1);
	}

}
