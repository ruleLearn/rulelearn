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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link InformationTable}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class InformationTableTest {
	
	private InformationTableTestConfiguration configuration01;
	private InformationTableTestConfiguration configuration02;
	
	/**
	 * Sole constructor initializing information table test configurations.
	 */
	public InformationTableTest() {
		//<CONFIGURATION 01>
		AttributePreferenceType[] attributePreferenceTypes01 = {
				AttributePreferenceType.GAIN, AttributePreferenceType.COST, AttributePreferenceType.GAIN, AttributePreferenceType.NONE,
				AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN, AttributePreferenceType.GAIN}; //supplementary variable
		this.configuration01 = new InformationTableTestConfiguration (
				new Attribute[] {
						new EvaluationAttribute("a0", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[0]), new UnknownSimpleFieldMV2(), attributePreferenceTypes01[0]),
						new EvaluationAttribute("a1", false, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[1]), new UnknownSimpleFieldMV15(), attributePreferenceTypes01[1]),
						new EvaluationAttribute("a2", true, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[2]), new UnknownSimpleFieldMV2(), attributePreferenceTypes01[2]),
						new EvaluationAttribute("a3", false, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[3]), new UnknownSimpleFieldMV2(), attributePreferenceTypes01[3]),
						new EvaluationAttribute("a4", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[4]), new UnknownSimpleFieldMV15(), attributePreferenceTypes01[4]),
						new EvaluationAttribute("a5", true, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[5]), new UnknownSimpleFieldMV2(), attributePreferenceTypes01[5]),
						new EvaluationAttribute("a6", false, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[6]), new UnknownSimpleFieldMV2(), attributePreferenceTypes01[6]),
						new EvaluationAttribute("a7", false, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes01[7]), new UnknownSimpleFieldMV2(), attributePreferenceTypes01[7])
					}, //attributeMap = [1, -1, -2, -3, 2, 0, -4, -5]
				new String[][] {
						{ "3",  "0",   "1", "15",  "32",  "35",   "28", "4"},
						{ "2", "-5",  "-3", "20", "-64", "-72", "-122", "3"},
						{ "4",  "7", "-15",  "5",  "12",  "19",  "256", "2"},
						{"-1",  "6",  "14", "-7", "-23",  "34",  "-15", "1"},
						{ "0", "-6", "-14", "77", "433", "-25",  "233", "0"}
				});
		//</CONFIGURATION 01>
		
		//<CONFIGURATION 02>
		AttributePreferenceType[] attributePreferenceTypes02 = {
				AttributePreferenceType.GAIN, AttributePreferenceType.NONE, AttributePreferenceType.GAIN, AttributePreferenceType.NONE,
				AttributePreferenceType.COST, AttributePreferenceType.NONE, AttributePreferenceType.NONE, AttributePreferenceType.GAIN}; //supplementary variable
		try {
		this.configuration02 = new InformationTableTestConfiguration (
				new Attribute[] {
						new EvaluationAttribute("a0", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes02[0]), new UnknownSimpleFieldMV2(), attributePreferenceTypes02[0]),
						new EvaluationAttribute("a1", true, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes02[1]), new UnknownSimpleFieldMV15(), attributePreferenceTypes02[1]),
						new EvaluationAttribute("a2", false, AttributeType.CONDITION,
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attributePreferenceTypes02[2]), new UnknownSimpleFieldMV2(), attributePreferenceTypes02[2]),
						new EvaluationAttribute("a3", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes02[3]), new UnknownSimpleFieldMV2(), attributePreferenceTypes02[3]),
						new EvaluationAttribute("a4", true, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes02[4]), new UnknownSimpleFieldMV15(), attributePreferenceTypes02[4]),
						new EvaluationAttribute("a5", false, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes02[5]), new UnknownSimpleFieldMV2(), attributePreferenceTypes02[5]),
						new EvaluationAttribute("a6", false, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes02[6]), new UnknownSimpleFieldMV2(), attributePreferenceTypes02[6]),
						new IdentificationAttribute("a7", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE)),
						new IdentificationAttribute("a8", false, new UUIDIdentificationField(UUIDIdentificationField.DEFAULT_VALUE)),
						new EvaluationAttribute("a9", true, AttributeType.CONDITION,
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"low", "medium", "high"}), EnumerationField.DEFAULT_VALUE, attributePreferenceTypes02[7]), new UnknownSimpleFieldMV2(), attributePreferenceTypes02[7]),
					}, //attributeMap = [1, -1, -2, 2, 0, -3, -4, 0, -5, 3]
				new String[][] {
						{ "3",  "0",   "1.2", "15",  "32",  "35",   "28", "o1", UUIDIdentificationField.getRandomValue().toString(), "low"},
						{ "2", "-5",  "-3.5", "20", "-64", "-72", "-122", "o2", UUIDIdentificationField.getRandomValue().toString(), "medium"},
						{ "4",  "7", "-15.7",  "5",  "12",  "19",  "256", "o3", UUIDIdentificationField.getRandomValue().toString(), "low"},
						{"-1",  "6",  "14.2", "-7", "-23",  "34",  "-15", "o4", UUIDIdentificationField.getRandomValue().toString(), "high"},
						{ "0", "-6", "-14.9", "77", "433", "-25",  "233", "o5", UUIDIdentificationField.getRandomValue().toString(), "low"},
						{"-1",  "2",   "7.6", "16", "-33", "-28",   "5",  "o5", UUIDIdentificationField.getRandomValue().toString(), "high"},
						{ "1",  "3",   "2.6", "13", "-32", "-28",   "5",  "o6", UUIDIdentificationField.getRandomValue().toString(), "medium"},
				});
		} catch (NoSuchAlgorithmException exception) {
			throw new InvalidValueException("NoSuchAlgorithmException thrown by when creating domain of an enumeration field.");
		}
		//</CONFIGURATION 02>
	}
	
	/**
	 * Test for {@link InformationTable#InformationTable(Attribute[], List, boolean)} class constructor.
	 * Tests if {@link NullPointerException} exception is thrown when attributes are {@code null}.
	 */
	@Test
	public void testInformationTable01() {
		try {
			new InformationTable(null, this.configuration01.getListOfFields(), true);
			fail("Should not create information table for null attributes.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link InformationTable#InformationTable(Attribute[], List, boolean)} class constructor.
	 * Tests if {@link NullPointerException} exception is thrown when list of objects' fields is {@code null}.
	 */
	@Test
	public void testInformationTable02() {
		try {
			new InformationTable(this.configuration01.getAttributes(), null, true);
			fail("Should not create information table for null list of objects' fields.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link InformationTable#InformationTable(Attribute[], List, boolean)} class constructor.
	 * Tests if information table is constructed when list of objects' fields is empty.
	 */
	@Test
	public void testInformationTable03() {
		try {
			new InformationTable(this.configuration01.getAttributes(), new ArrayList<Field[]>(), true);
			//OK
		} catch (Exception exception) {
			fail("Should create information table for an empty list of objects' fields.");
		}
	}
	
	/**
	 * Test for {@link InformationTable#InformationTable(Attribute[], List, boolean)} class constructor.
	 * Tests if information table is constructed when lists of attributes and objects' fields are empty.
	 */
	@Test
	public void testInformationTable04() {
		try {
			InformationTable informationTable = new InformationTable(new Attribute[0], new ArrayList<Field[]>(), true);
			assertEquals(informationTable.getNumberOfAttributes(), 0);
			assertEquals(informationTable.getNumberOfObjects(), 0);
			//OK
		} catch (Exception exception) {
			fail("Should create information table for an empty list of attributes and objects' fields.");
		}
	}
	
	/**
	 * Test for {@link InformationTable#getAttributes(boolean)} method.
	 */
	@Test
	public void testGetAttributesBoolean01() {
		InformationTable informationTable = configuration01.getInformationTable(false);
		
		Attribute[] expectedAttributes = configuration01.getAttributes();
		Attribute[] attributes = informationTable.getAttributes(false); //get a fresh table with attributes
		
		assertEquals(attributes.length, expectedAttributes.length);
		
		for (int i = 0; i < attributes.length; i++) {
			assertEquals(attributes[i], expectedAttributes[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getAttributes(boolean)} method.
	 */
	@Test
	public void testGetAttributesBoolean02() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		
		Attribute[] expectedAttributes = configuration02.getAttributes();
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
	public void testGetIndex2IdMapper01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		
		Index2IdMapper mapper = informationTable.getIndex2IdMapper();
		
		assertNotEquals(mapper.getId(0), mapper.getId(1));
		assertNotEquals(mapper.getId(1), mapper.getId(2));
		assertNotEquals(mapper.getId(2), mapper.getId(3));
		assertNotEquals(mapper.getId(3), mapper.getId(4));
	}
	
	/**
	 * Test for {@link InformationTable#getIndex2IdMapper()} method.
	 */
	@Test
	public void testGetIndex2IdMapper02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		
		Index2IdMapper mapper = informationTable.getIndex2IdMapper();
		
		assertNotEquals(mapper.getId(0), mapper.getId(1));
		assertNotEquals(mapper.getId(1), mapper.getId(2));
		assertNotEquals(mapper.getId(2), mapper.getId(3));
		assertNotEquals(mapper.getId(3), mapper.getId(4));
		assertNotEquals(mapper.getId(4), mapper.getId(5));
		assertNotEquals(mapper.getId(5), mapper.getId(6));
	}

	/**
	 * Test for {@link InformationTable#getField(int, int)} method.
	 */
	@Test
	public void testGetField01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		
		List<Field[]> listOfFields = configuration01.getListOfFields();
		
		for (int i = 0; i < listOfFields.size(); i++) {
			for (int j = 0; j < listOfFields.get(i).length; j++) {
				assertEquals(informationTable.getField(i, j), listOfFields.get(i)[j]);
			}
		}
	}
	
	/**
	 * Test for {@link InformationTable#getField(int, int)} method.
	 */
	@Test
	public void testGetField02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		
		List<Field[]> listOfFields = configuration02.getListOfFields();
		
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
	public void testSelectIntArrayBoolean01() {
		InformationTable informationTable = configuration01.getInformationTable(false);
		
		int[] objectIndices = new int[] {1, 2, 4};
		InformationTable newInformationTable = informationTable.select(objectIndices, true);
		
		assertEquals(newInformationTable.getNumberOfObjects(), objectIndices.length);
		assertEquals(newInformationTable.getNumberOfAttributes(), informationTable.getNumberOfAttributes());
		
		//check fields of the new table
		List<Field[]> expectedFields = configuration01.getListOfFields(objectIndices);
		for (int i = 0; i < expectedFields.size(); i++) {
			for (int j = 0; j < expectedFields.get(i).length; j++) {
				assertEquals(expectedFields.get(i)[j], newInformationTable.getField(i, j));
			}
		}
		
		//check attributes of the new table
		Attribute[] expectedAttributes = configuration01.getAttributes();
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
		
		int numberOfObjects = newInformationTable.getNumberOfObjects();
		
		//check decisions of the new table
		Decision[] expectedDecisions = configuration01.getDecisions(objectIndices);
		Decision[] decisions = newInformationTable.getDecisions(true);
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
		
		//check decision attribute index of the new table
//		assertEquals(newInformationTable.getActiveDecisionAttributeIndex(), configuration01.getActiveDecisionAttributeIndex());
		
		//check identifiers of the new table
		Field[] expectedIdentifiers = configuration01.getIdentifiers(objectIndices);
		Field[] identifiers = newInformationTable.getIdentifiers(false);
		
		assertEquals(expectedIdentifiers, null);
		assertEquals(identifiers, null);
		
		//check identification attribute index of the new table
		assertEquals(newInformationTable.getActiveIdentificationAttributeIndex(), -1);
		assertEquals(configuration01.getActiveIdentificationAttributeIndex(), -1);
	}
	
	/**
	 * Test for {@link InformationTable#select(int[], boolean)} method.
	 */
	@Test
	public void testSelectIntArrayBoolean02() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		
		int[] objectIndices = new int[] {1, 2, 5, 6};
		InformationTable newInformationTable = informationTable.select(objectIndices, true);
		
		assertEquals(newInformationTable.getNumberOfObjects(), objectIndices.length);
		assertEquals(newInformationTable.getNumberOfAttributes(), informationTable.getNumberOfAttributes());
		
		//check fields of the new table
		List<Field[]> expectedFields = configuration02.getListOfFields(objectIndices);
		for (int i = 0; i < expectedFields.size(); i++) {
			for (int j = 0; j < expectedFields.get(i).length; j++) {
				assertEquals(expectedFields.get(i)[j], newInformationTable.getField(i, j));
			}
		}
		
		//check attributes of the new table
		Attribute[] expectedAttributes = configuration02.getAttributes();
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
		
		int numberOfObjects = newInformationTable.getNumberOfObjects();
		
		//check decisions of the new table
		Decision[] expectedDecisions = configuration02.getDecisions(objectIndices);
		Decision[] decisions = newInformationTable.getDecisions(true);
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
		
		//check decision attribute index of the new table
//		assertEquals(newInformationTable.getActiveDecisionAttributeIndex(), configuration02.getActiveDecisionAttributeIndex());
		
		//check identifiers of the new table
		Field[] expectedIdentifiers = configuration02.getIdentifiers(objectIndices);
		Field[] identifiers = newInformationTable.getIdentifiers(false);
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(expectedIdentifiers[i], identifiers[i]);
		}
		
		//check identification attribute index of the new table
		assertEquals(newInformationTable.getActiveIdentificationAttributeIndex(), configuration02.getActiveIdentificationAttributeIndex());
	}
	
	/**
	 * Test for {@link InformationTable#getNumberOfObjects()} method}.
	 */
	@Test
	public void testGetNumberOfObjects01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		assertEquals(informationTable.getNumberOfObjects(), configuration01.getNumberOfObjects());
	}
	
	/**
	 * Test for {@link InformationTable#getNumberOfObjects()} method}.
	 */
	@Test
	public void testGetNumberOfObjects02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		assertEquals(informationTable.getNumberOfObjects(), configuration02.getNumberOfObjects());
	}

	/**
	 * Test for {@link InformationTable#getNumberOfAttributes()} method}.
	 */
	@Test
	public void testGetNumberOfAttributes01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		assertEquals(informationTable.getNumberOfAttributes(), configuration01.getNumberOfAttributes());
	}
	
	/**
	 * Test for {@link InformationTable#getNumberOfAttributes()} method}.
	 */
	@Test
	public void testGetNumberOfAttributes02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		assertEquals(informationTable.getNumberOfAttributes(), configuration02.getNumberOfAttributes());
	}
	
	/**
	 * Test for {@link InformationTable#getActiveConditionAttributeFields()} method}.
	 */
	@Test
	public void testGetActiveConditionFields01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		assertEquals(informationTable.getActiveConditionAttributeFields().getNumberOfAttributes(), 2);
	}
	
	/**
	 * Test for {@link InformationTable#getActiveConditionAttributeFields()} method}.
	 */
	@Test
	public void testGetActiveConditionFields02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		assertEquals(informationTable.getActiveConditionAttributeFields().getNumberOfAttributes(), 3);
	}
	
	/**
	 * Test for {@link InformationTable#getNotActiveOrDescriptionFields} method}.
	 */
	@Test
	public void testGetNotActiveOrDescriptionFields01() {
		InformationTable informationTable = configuration01.getInformationTable(false);
		assertEquals(informationTable.getNotActiveOrDescriptionAttributeFields().getNumberOfAttributes(), 5);
	}
	
	/**
	 * Test for {@link InformationTable#getNotActiveOrDescriptionFields} method}.
	 */
	@Test
	public void testGetNotActiveOrDescriptionFields02() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		assertEquals(informationTable.getNotActiveOrDescriptionAttributeFields().getNumberOfAttributes(), 5);
	}
	
//	/**
//	 * Test for {@link InformationTable#getActiveDecisionAttributeIndex} method}.
//	 */
//	@Test
//	public void testGetActiveDecisionAttributeIndex01() {
//		InformationTable informationTable = configuration01.getInformationTable(false);
//		assertEquals(informationTable.getActiveDecisionAttributeIndex(), configuration01.getActiveDecisionAttributeIndex());
//	}
//	
//	/**
//	 * Test for {@link InformationTable#getActiveDecisionAttributeIndex} method}.
//	 */
//	@Test
//	public void testGetActiveDecisionAttributeIndex02() {
//		InformationTable informationTable = configuration02.getInformationTable(false);
//		assertEquals(informationTable.getActiveDecisionAttributeIndex(), configuration02.getActiveDecisionAttributeIndex());
//	}
	
	/**
	 * Test for {@link InformationTable#getDecision(int)} method}.
	 */
	@Test
	public void testGetDecision01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		Decision[] expectedDecisions = configuration01.getDecisions();
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(informationTable.getDecision(i), expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getDecision(int)} method}.
	 */
	@Test
	public void testGetDecision02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		Decision[] expectedDecisions = configuration02.getDecisions();
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(informationTable.getDecision(i), expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getIdentifier(int)} method}.
	 */
	@Test
	public void testGetIdentifier01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		assertTrue(informationTable.getIdentifier(0) == null);
	}
	
	/**
	 * Test for {@link InformationTable#getIdentifier(int)} method}.
	 */
	@Test
	public void testGetIdentifier02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		Field[] expectedIdentifiers = configuration02.getIdentifiers();
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(informationTable.getIdentifier(i), expectedIdentifiers[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getDecisions(boolean)} method}.
	 */
	@Test
	public void testGetDecisions01() {
		InformationTable informationTable = configuration01.getInformationTable(false);
		Decision[] expectedDecisions = configuration01.getDecisions();
		Decision[] decisions = informationTable.getDecisions(false);
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getDecisions(boolean)} method}.
	 */
	@Test
	public void testGetDecisions02() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		Decision[] expectedDecisions = configuration02.getDecisions();
		Decision[] decisions = informationTable.getDecisions(false);
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#calculateOrderedUniqueDecisions()} method}.
	 * Tests cost-type decision criterion.
	 */
	@Test
	public void testCalculateOrderedUniqueDecisions01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		int activeDecisionAttributeIndex = 5;
		AttributePreferenceType preferenceType = ((EvaluationAttribute)this.configuration01.getAttributes()[activeDecisionAttributeIndex]).getPreferenceType();
		
		
		Decision[] expectedDecisions = { //take into account that decision criterion has cost preference type
				new SimpleDecision(IntegerFieldFactory.getInstance().create(35, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(34, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(19, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-25, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-72, preferenceType), activeDecisionAttributeIndex)
		};
		
		Decision[] orderedUniqueDecisions = informationTable.calculateOrderedUniqueDecisions();
		
		assertEquals(orderedUniqueDecisions.length, 5);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#calculateOrderedUniqueDecisions()} method}.
	 * Tests gain-type decision criterion.
	 */
	@Test
	public void testCalculateOrderedUniqueDecisions02() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int activeDecisionAttributeIndex = 0;
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		Decision[] allDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
		};
		
		Mockito.when(informationTable.getDecisions(true)).thenReturn(allDecisions);
		Mockito.when(informationTable.calculateOrderedUniqueDecisions()).thenCallRealMethod();
		
		Decision[] expectedDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
		};
		
		Decision[] orderedUniqueDecisions = informationTable.calculateOrderedUniqueDecisions();
		
		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#calculateOrderedUniqueDecisions()} method}.
	 * Tests sorting in presence of an mv_2-type missing value.
	 */
	@Test
	public void testCalculateOrderedUniqueDecisions03() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int activeDecisionAttributeIndex = 0;
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		Decision[] allDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(new UnknownSimpleFieldMV2(), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
		};
		
		Mockito.when(informationTable.getDecisions(true)).thenReturn(allDecisions);
		Mockito.when(informationTable.calculateOrderedUniqueDecisions()).thenCallRealMethod();
		
		Decision[] expectedDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(new UnknownSimpleFieldMV2(), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
		};
		
		Decision[] orderedUniqueDecisions = informationTable.calculateOrderedUniqueDecisions();
		
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			System.out.println(orderedUniqueDecisions[i]);
		}
		
		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#calculateOrderedUniqueDecisions()} method}.
	 * Tests sorting in presence of composite decisions with missing values.
	 */
	@Test
	public void testCalculateOrderedUniqueDecisions04() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int activeDecisionAttributeIndex1 = 0;
		int activeDecisionAttributeIndex2 = 1;
		AttributePreferenceType preferenceType1 = AttributePreferenceType.GAIN;
		AttributePreferenceType preferenceType2 = AttributePreferenceType.GAIN;
		
		Decision[] allDecisions = {
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceType1), new UnknownSimpleFieldMV2()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {new UnknownSimpleFieldMV2(), IntegerFieldFactory.getInstance().create(3, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(1, preferenceType1), IntegerFieldFactory.getInstance().create(4, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2})
		};
		
		Mockito.when(informationTable.getDecisions(true)).thenReturn(allDecisions);
		Mockito.when(informationTable.calculateOrderedUniqueDecisions()).thenCallRealMethod();
		
		//? 1 2
		//3 4 ?
		Decision[] expectedDecisions = {
				new CompositeDecision(new EvaluationField[] {new UnknownSimpleFieldMV2(), IntegerFieldFactory.getInstance().create(3, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(1, preferenceType1), IntegerFieldFactory.getInstance().create(4, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceType1), new UnknownSimpleFieldMV2()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2})
		};
		
		Decision[] orderedUniqueDecisions = informationTable.calculateOrderedUniqueDecisions();
		
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			System.out.println(orderedUniqueDecisions[i]);
		}

//TODO: uncomment		
//		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
//		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
//			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
//		}
	}
	
	/**
	 * Test for {@link InformationTable#getIdentifiers(boolean)} method}.
	 */
	@Test
	public void testGetIdentifiers01() {
		InformationTable informationTable = configuration01.getInformationTable(false);
		assertTrue(informationTable.getIdentifiers(false) == null);
	}
	
	/**
	 * Test for {@link InformationTable#getIdentifiers(boolean)} method}.
	 */
	@Test
	public void testGetIdentifiers02() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		Field[] expectedIdentifiers = configuration02.getIdentifiers();
		Field[] identifiers = informationTable.getIdentifiers(false);
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(identifiers[i], expectedIdentifiers[i]);
		}
	}

}
