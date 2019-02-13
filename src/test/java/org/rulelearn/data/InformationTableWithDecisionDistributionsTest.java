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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UnknownSimpleFieldMV2;

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
	 * Test method for {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(InformationTable)}.
	 */
	@Test
	void testInformationTableWithDecisionDistributionsInformationTableBoolean01() {
		Attribute[] attributes = new Attribute[] {
				new EvaluationAttribute("a0", true, AttributeType.CONDITION,
						IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN),
				new EvaluationAttribute("a1", true, AttributeType.DECISION,
						IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.COST), new UnknownSimpleFieldMV2(), AttributePreferenceType.COST),
				new IdentificationAttribute("a2", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE))
		};
		List<Field[]> listOfFields = new ArrayList<Field[]>();
		listOfFields.add(new Field[] {
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST),
				new TextIdentificationField("test1")
		});
		listOfFields.add(new Field[] {
				IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST),
				new TextIdentificationField("test2")
		});
		
		InformationTable informationTable = new InformationTable(attributes, listOfFields);
		
		InformationTableWithDecisionDistributions copiedInformationTable = new InformationTableWithDecisionDistributions(informationTable);
		
		//test if fields are copied
		assertEquals(copiedInformationTable.getAttributes().length, informationTable.getAttributes().length);
		int attributesCount = copiedInformationTable.getAttributes().length;
		for (int i = 0; i < attributesCount; i++) {
			assertEquals(copiedInformationTable.getAttributes()[i], informationTable.getAttributes()[i]);
		}
		assertEquals(copiedInformationTable.getIndex2IdMapper(), informationTable.getIndex2IdMapper());
		assertEquals(copiedInformationTable.getActiveConditionAttributeFields(), informationTable.getActiveConditionAttributeFields());
		assertEquals(copiedInformationTable.getNotActiveOrDescriptionAttributeFields(), informationTable.getNotActiveOrDescriptionAttributeFields());
		assertEquals(copiedInformationTable.getDecisions().length, informationTable.getDecisions().length);
		int decisionsCount = copiedInformationTable.getDecisions().length;
		for (int i = 0; i < decisionsCount; i++) {
			assertEquals(copiedInformationTable.getDecisions()[i], informationTable.getDecisions()[i]);
		}
		assertEquals(copiedInformationTable.getIdentifiers().length, informationTable.getIdentifiers().length);
		int identifiersCount = copiedInformationTable.getIdentifiers().length;
		for (int i = 0; i < identifiersCount; i++) {
			assertEquals(copiedInformationTable.getIdentifiers()[i], informationTable.getIdentifiers()[i]);
		}
		assertEquals(copiedInformationTable.getActiveIdentificationAttributeIndex(), informationTable.getActiveIdentificationAttributeIndex());
		assertEquals(copiedInformationTable.attributeMap.length, informationTable.attributeMap.length);
		for (int i = 0; i < copiedInformationTable.attributeMap.length; i++) {
			assertEquals(copiedInformationTable.attributeMap[i], informationTable.attributeMap[i]);
		}
		assertEquals(copiedInformationTable.localActiveConditionAttributeIndex2GlobalAttributeIndexMap,
				informationTable.localActiveConditionAttributeIndex2GlobalAttributeIndexMap);
		
		//test if additionally distributions are created
		assertNotNull(copiedInformationTable.getDecisionDistribution());
		assertNotNull(copiedInformationTable.getDominanceConesDecisionDistributions());
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
