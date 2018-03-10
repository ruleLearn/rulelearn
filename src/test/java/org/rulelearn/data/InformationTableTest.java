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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link InformationTable}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class InformationTableTest {
	
	//<CONFIGURATION 1>
	private final AttributePreferenceType[] attributePreferenceTypes1 = {
			AttributePreferenceType.GAIN, AttributePreferenceType.COST, AttributePreferenceType.GAIN, AttributePreferenceType.NONE,
			AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN, AttributePreferenceType.GAIN}; //supplementary variable
	private InformationTableTestConfiguration configuration1 = new InformationTableTestConfiguration (
			new Attribute[] {
					new EvaluationAttribute("a0", true, AttributeType.CONDITION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[0]), new UnknownSimpleFieldMV2(), attributePreferenceTypes1[0]),
					new EvaluationAttribute("a1", false, AttributeType.DESCRIPTION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[1]), new UnknownSimpleFieldMV15(), attributePreferenceTypes1[1]),
					new EvaluationAttribute("a2", true, AttributeType.DESCRIPTION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[2]), new UnknownSimpleFieldMV2(), attributePreferenceTypes1[2]),
					new EvaluationAttribute("a3", false, AttributeType.CONDITION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[3]), new UnknownSimpleFieldMV2(), attributePreferenceTypes1[3]),
					new EvaluationAttribute("a4", true, AttributeType.CONDITION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[4]), new UnknownSimpleFieldMV15(), attributePreferenceTypes1[4]),
					new EvaluationAttribute("a5", true, AttributeType.DECISION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[5]), new UnknownSimpleFieldMV2(), attributePreferenceTypes1[5]),
					new EvaluationAttribute("a6", false, AttributeType.CONDITION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[6]), new UnknownSimpleFieldMV2(), attributePreferenceTypes1[6]),
					new EvaluationAttribute("a7", false, AttributeType.DECISION,
							IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes1[7]), new UnknownSimpleFieldMV2(), attributePreferenceTypes1[7])
				}, //attributeMap: [1, -1, -2, -3, 2, 0, -4, -5]
			new String[][] {
					{ "3",  "0",   "1", "15",  "32",  "35",   "28", "4"},
					{ "2", "-5",  "-3", "20", "-64", "-72", "-122", "3"},
					{ "4",  "7", "-15",  "5",  "12",  "19",  "256", "2"},
					{"-1",  "6",  "14", "-7", "-23",  "34",  "-15", "1"},
					{ "0", "-6", "-14", "77", "433", "-25",  "233", "0"}
			});
	//</CONFIGURATION 1>
	
	/**
	 * Test for {@link InformationTable#getAttributes(boolean)} method.
	 */
	@Test
	public void testGetAttributesBoolean() {
		InformationTable informationTable = configuration1.getInformationTable(false);
		
		Attribute[] expectedAttributes = configuration1.getAttributes();
		Attribute[] attributes = informationTable.getAttributes(false); //get a fresh table with attributes
		
		assertEquals(attributes.length, expectedAttributes.length);
		
		for (int i = 0; i < attributes.length; i++) {
			assertEquals(attributes[i], expectedAttributes[i]);
		}
	}

	/**
	 * Test for {@link InformationTable#getIndex2IdMapper()} method.
	 */
	@Test
	public void testGetIndex2IdMapper() {
		InformationTable informationTable = configuration1.getInformationTable(true);
		
		Index2IdMapper mapper = informationTable.getIndex2IdMapper();
		
		assertNotEquals(mapper.getId(0), mapper.getId(1));
		assertNotEquals(mapper.getId(1), mapper.getId(2));
		assertNotEquals(mapper.getId(2), mapper.getId(3));
		assertNotEquals(mapper.getId(3), mapper.getId(4));
	}

	/**
	 * Test for {@link InformationTable#getField(int, int)} method.
	 */
	@Test
	public void testGetField() {
		InformationTable informationTable = configuration1.getInformationTable(true);
		
		List<Field[]> listOfFields = configuration1.getListOfFields();
		
		for (int i = 0; i < listOfFields.size(); i++) {
			for (int j = 0; j < listOfFields.get(i).length; j++) {
				assertEquals(informationTable.getField(i, j), listOfFields.get(i)[j]);
			}
		}
	}

	/**
	 * Test for {@link InformationTable#select(int[], boolean)} method.
	 */
	@Test
	public void testSelectIntArrayBoolean() {
		InformationTable informationTable = configuration1.getInformationTable(false);
		
		int[] objectIndices = new int[] {1, 2, 4};
		InformationTable newInformationTable = informationTable.select(objectIndices, true);
		
		assertEquals(newInformationTable.getNumberOfObjects(), objectIndices.length);
		assertEquals(newInformationTable.getNumberOfAttributes(), informationTable.getNumberOfAttributes());
		
		//check fields of the new table
		List<Field[]> expectedFields = configuration1.getListOfFields(objectIndices);
		for (int i = 0; i < expectedFields.size(); i++) {
			for (int j = 0; j < expectedFields.get(i).length; j++) {
				assertEquals(expectedFields.get(i)[j], newInformationTable.getField(i, j));
			}
		}
		
		//check attributes of the new table
		Attribute[] expectedAttributes = configuration1.getAttributes();
		Attribute[] attributes = newInformationTable.getAttributes();
		assertEquals(attributes.length, expectedAttributes.length);
		for (int i = 0; i < attributes.length; i++) {
			assertEquals(attributes[i], expectedAttributes[i]);
		}
		
		//check mapper of the new table
		Index2IdMapper newMapper = newInformationTable.getIndex2IdMapper();
		for (int i = 0; i < objectIndices.length; i++) {
			assertEquals(newMapper.getId(i), informationTable.getIndex2IdMapper().getId(objectIndices[i]));
		}
		
		//check decisions of the new table
		Field[] expectedDecisions = configuration1.getDecisions(objectIndices);
		Field[] decisions = newInformationTable.getDecisions(true);
		int numberOfObjects = newInformationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
		
		//check decision attribute index of the new table
		assertEquals(newInformationTable.getActiveDecisionAttributeIndex(), configuration1.getActiveDecisionAttributeIndex());
	}
	
	/**
	 * Test for {@link InformationTable#getNumberOfObjects()} method}.
	 */
	@Test
	public void testGetNumberOfObjects() {
		InformationTable informationTable = configuration1.getInformationTable(true);
		assertEquals(informationTable.getNumberOfObjects(), configuration1.getNumberOfObjects());
	}

	/**
	 * Test for {@link InformationTable#getNumberOfAttributes()} method}.
	 */
	@Test
	public void testGetNumberOfAttributes() {
		InformationTable informationTable = configuration1.getInformationTable(true);
		assertEquals(informationTable.getNumberOfAttributes(), configuration1.getNumberOfAttributes());
	}
	
	/**
	 * Test for {@link InformationTable#getActiveConditionFields()} method}.
	 */
	@Test
	public void testGetActiveConditionFields() {
		InformationTable informationTable = configuration1.getInformationTable(true);
		assertEquals(informationTable.getActiveConditionFields().getNumberOfAttributes(), 2);
	}
	
	/**
	 * Test for {@link InformationTable#getNotActiveOrDescriptionFields} method}.
	 */
	@Test
	public void testGetNotActiveOrDescriptionFields() {
		InformationTable informationTable = configuration1.getInformationTable(false);
		assertEquals(informationTable.getNotActiveOrDescriptionFields().getNumberOfAttributes(), 5);
	}
	
	/**
	 * Test for {@link InformationTable#getActiveDecisionAttributeIndex} method}.
	 */
	@Test
	public void testGetActiveDecisionAttributeIndex() {
		InformationTable informationTable = configuration1.getInformationTable(false);
		assertEquals(informationTable.getActiveDecisionAttributeIndex(), configuration1.getActiveDecisionAttributeIndex());
	}
	
	/**
	 * Test for {@link InformationTable#getDecision(int)} method}.
	 */
	@Test
	public void testGetDecision() {
		InformationTable informationTable = configuration1.getInformationTable(true);
		Field[] expectedDecisions = configuration1.getDecisions();
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(informationTable.getDecision(i), expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getDecisions(boolean)} method}.
	 */
	@Test
	public void testGetDecisions() {
		InformationTable informationTable = configuration1.getInformationTable(false);
		Field[] expectedDecisions = configuration1.getDecisions();
		Field[] decisions = informationTable.getDecisions(false);
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
	}

}
