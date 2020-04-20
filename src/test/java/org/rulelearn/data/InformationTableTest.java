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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.MessageDigest;
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
	private InformationTableTestConfiguration configuration03; //for testing imposition of preference orders
	private InformationTableTestConfiguration configuration04; //for testing imposition of preference orders
	
	private String originalOnlyLearningAttributesSerialization = //concerns configuration04
			"1:+;a-dec-enum3-none;decision;none;noneEnum(l,m,h);mv1.5|" +
			"5:+;a-cond-enum3-gain;condition;gain;gainEnum(l,m,h);mv2|" +
			"6:+;a-cond-enum3-cost;condition;cost;costEnum(l,m,h);mv1.5|" +
			"7:+;a-cond-int-gain;condition;gain;gainInt;mv2|" +
			"8:+;a-cond-real-cost;condition;cost;costReal;mv1.5|" +
			"9:+;a-cond-int-none;condition;none;noneInt;mv2|" +
			"10:+;a-cond-real-none;condition;none;noneReal;mv1.5|" +
			"11:+;a-cond-enum2-none;condition;none;noneEnum(a,b);mv2|" +
			"12:+;a-cond-enum3-none;condition;none;noneEnum(l,m,h);mv1.5\n" +
			"m|l|m|1|2.0|3|1.0|a|l\n" + 
			"h|m|h|2|3.0|4|2.0|b|m\n" + 
			"m|h|l|3|4.0|5|3.0|a|h";
	private String originalOnlyLearningAttributesHash = "9FDD19E3CE03DF538C42F2A146D6A7B56860FBC3303B1DF067C3D416BF57E169"; //hash for the above original only learning attributes serialization
	
	private String originalAllAttributesSerialization = //concerns configuration04
			"0:-;na-cond-enum3-none;condition;none;noneEnum(l,m,h);mv2|" +
			"1:+;a-dec-enum3-none;decision;none;noneEnum(l,m,h);mv1.5|" +
			"2:+;a-desc-enum3-none;description;none;noneEnum(l,m,h);mv2|" +
			"3:+;a-neval-text;textId|" +
			"4:-;a-neval-uuid;uuid|" +
			"5:+;a-cond-enum3-gain;condition;gain;gainEnum(l,m,h);mv2|" +
			"6:+;a-cond-enum3-cost;condition;cost;costEnum(l,m,h);mv1.5|" +
			"7:+;a-cond-int-gain;condition;gain;gainInt;mv2|" +
			"8:+;a-cond-real-cost;condition;cost;costReal;mv1.5|" +
			"9:+;a-cond-int-none;condition;none;noneInt;mv2|" +
			"10:+;a-cond-real-none;condition;none;noneReal;mv1.5|" +
			"11:+;a-cond-enum2-none;condition;none;noneEnum(a,b);mv2|" +
			"12:+;a-cond-enum3-none;condition;none;noneEnum(l,m,h);mv1.5" + System.lineSeparator() +
			"l|m|h|o1|00000000-0000-0000-0000-000000000000|l|m|1|2.0|3|1.0|a|l" + System.lineSeparator() + 
			"m|h|l|o2|00000000-0000-0000-0000-000000000001|m|h|2|3.0|4|2.0|b|m" + System.lineSeparator() + 
			"h|m|l|o3|00000000-0000-0000-0000-000000000002|h|l|3|4.0|5|3.0|a|h";
	
	/**
	 * Sole constructor initializing information table test configurations.
	 */
	public InformationTableTest() {
		UnknownSimpleFieldMV2 mv2 = UnknownSimpleFieldMV2.getInstance();
		UnknownSimpleFieldMV15 mv15 = UnknownSimpleFieldMV15.getInstance();
		AttributePreferenceType attrPrefType; //temporary variable
		
		//<CONFIGURATION 01>
		this.configuration01 = new InformationTableTestConfiguration (
				new Attribute[] {
						new EvaluationAttribute("a0", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a1", false, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType),
						new EvaluationAttribute("a2", true, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a3", false, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						new EvaluationAttribute("a4", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType),
						new EvaluationAttribute("a5", true, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv2, attrPrefType),
						new EvaluationAttribute("a6", false, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a7", false, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType)
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
		try {
		this.configuration02 = new InformationTableTestConfiguration (
				new Attribute[] {
						new EvaluationAttribute("a0", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a1", true, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType),
						new EvaluationAttribute("a2", false, AttributeType.CONDITION,
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a3", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						new EvaluationAttribute("a4", true, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType),
						new EvaluationAttribute("a5", false, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						new EvaluationAttribute("a6", false, AttributeType.DESCRIPTION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						new IdentificationAttribute("a7", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE)),
						new IdentificationAttribute("a8", false, new UUIDIdentificationField(UUIDIdentificationField.DEFAULT_VALUE)),
						new EvaluationAttribute("a9", true, AttributeType.CONDITION,
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"low", "medium", "high"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
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
		
		//<CONFIGURATION 03>
		try {
		this.configuration03 = new InformationTableTestConfiguration (
				new Attribute[] {
						new EvaluationAttribute("na-cond-enum3-none", false, AttributeType.CONDITION, //!active
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						new EvaluationAttribute("a-dec-enum3-none", true, AttributeType.DECISION, //!CONDITION
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType),
						new EvaluationAttribute("a-desc-enum3-none", true, AttributeType.DESCRIPTION, //!CONDITION
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						
						new IdentificationAttribute("a-neval-text", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE)), //!EvaluationAttribute
						new IdentificationAttribute("a-neval-uuid", false, new UUIDIdentificationField(UUIDIdentificationField.DEFAULT_VALUE)), //!EvaluationAttribute, !active
						
						new EvaluationAttribute("a-cond-enum3-gain", true, AttributeType.CONDITION, //!NONE
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a-cond-enum3-cost", true, AttributeType.CONDITION, //!NONE
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType),
						
						new EvaluationAttribute("a-cond-int-gain", true, AttributeType.CONDITION, //!NONE
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a-cond-real-cost", true, AttributeType.CONDITION, //!NONE
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType),
						
						new EvaluationAttribute("a-cond-enum2-none", true, AttributeType.CONDITION, //NONE, enum, 2 domain values => leaved "as is"
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
					},
				new String[][] {
						{ "l", "m", "h",  "o1", UUIDIdentificationField.getRandomValue().toString(),  "l", "m",  "1", "2.0", "a"},
						{ "m", "h", "l",  "o2", UUIDIdentificationField.getRandomValue().toString(),  "m", "h",  "2", "3.0", "b"},
						{ "h", "m", "l",  "o3", UUIDIdentificationField.getRandomValue().toString(),  "h", "l",  "3", "4.0", "a"},
				});
		} catch (NoSuchAlgorithmException exception) {
			throw new InvalidValueException("NoSuchAlgorithmException thrown by when creating domain of an enumeration field.");
		}
		//</CONFIGURATION 03>
		
		//<CONFIGURATION 04>
		try {
		this.configuration04 = new InformationTableTestConfiguration (
				new Attribute[] {
						//attributes that do not change when imposing preference orders:
						
						new EvaluationAttribute("na-cond-enum3-none", false, AttributeType.CONDITION, //!active
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						new EvaluationAttribute("a-dec-enum3-none", true, AttributeType.DECISION, //!CONDITION
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType),
						new EvaluationAttribute("a-desc-enum3-none", true, AttributeType.DESCRIPTION, //!CONDITION
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						
						new IdentificationAttribute("a-neval-text", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE)), //!EvaluationAttribute
						new IdentificationAttribute("a-neval-uuid", false, new UUIDIdentificationField(UUIDIdentificationField.DEFAULT_VALUE)), //!EvaluationAttribute, !active
						
						new EvaluationAttribute("a-cond-enum3-gain", true, AttributeType.CONDITION, //!NONE
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a-cond-enum3-cost", true, AttributeType.CONDITION, //!NONE
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType),
						
						new EvaluationAttribute("a-cond-int-gain", true, AttributeType.CONDITION, //!NONE
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType),
						new EvaluationAttribute("a-cond-real-cost", true, AttributeType.CONDITION, //!NONE
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType),
						
						//two attributes that change when imposing preference orders:
						
						new EvaluationAttribute("a-cond-int-none", true, AttributeType.CONDITION, //NONE, int => 2 attributes with inverse preference orders
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						new EvaluationAttribute("a-cond-real-none", true, AttributeType.CONDITION, //NONE, real => 2 attributes with inverse preference orders
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType),
						
						//attribute that does not change when imposing preference orders:
						
						new EvaluationAttribute("a-cond-enum2-none", true, AttributeType.CONDITION, //NONE, enum, 2 domain values => leaved "as is"
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv2, attrPrefType),
						
						//attribute which changes or not, depending on parameter of InformationTable.imposePreferenceOrders method
						
						new EvaluationAttribute("a-cond-enum3-none", true, AttributeType.CONDITION, //NONE, enum, 3 domain values => 3 pairs of int attributes with inverse preference orders
								EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType),
					},
				new String[][] {
						{ "l", "m", "h",  "o1", "00000000-0000-0000-0000-000000000000",  "l", "m",  "1", "2.0",  "3", "1.0",  "a",  "l"},
						{ "m", "h", "l",  "o2", "00000000-0000-0000-0000-000000000001",  "m", "h",  "2", "3.0",  "4", "2.0",  "b",  "m"},
						{ "h", "m", "l",  "o3", "00000000-0000-0000-0000-000000000002",  "h", "l",  "3", "4.0",  "5", "3.0",  "a",  "h"},
				});
		} catch (NoSuchAlgorithmException exception) {
			throw new InvalidValueException("NoSuchAlgorithmException thrown when creating domain of an enumeration field.");
		}
		//</CONFIGURATION 04>
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
	 * Test for {@link InformationTable#InformationTable(InformationTable, boolean)} class constructor.
	 * Tests if given information table is properly copied.
	 */
	@Test
	public void testInformationTable05() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		
		InformationTable copiedInformationTable = new InformationTable(informationTable, true);
		
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
		assertEquals(copiedInformationTable.attributeMap, informationTable.attributeMap);
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
	 * Test for {@link InformationTable#getFields(int)} method.
	 */
	@Test
	public void testGetFields01() {
		InformationTable informationTable = configuration01.getInformationTable(true);
		
		List<Field[]> listOfFields = configuration01.getListOfFields();
		Field[] fields;
		
		for (int i = 0; i < listOfFields.size(); i++) { //for each object
			fields = informationTable.getFields(i); //get fields of the current object
			assertEquals(fields.length, listOfFields.get(i).length);
			for (int j = 0; j < fields.length; j++) {
				assertEquals(fields[j], listOfFields.get(i)[j]);
			}
		}
	}
	
	/**
	 * Test for {@link InformationTable#getFields(int)} method.
	 */
	@Test
	public void testGetFields02() {
		InformationTable informationTable = configuration02.getInformationTable(true);
		
		List<Field[]> listOfFields = configuration02.getListOfFields();
		Field[] fields;
		
		for (int i = 0; i < listOfFields.size(); i++) { //for each object
			fields = informationTable.getFields(i); //get fields of the current object
			assertEquals(fields.length, listOfFields.get(i).length);
			for (int j = 0; j < fields.length; j++) {
				assertEquals(fields[j], listOfFields.get(i)[j]);
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
	 * Test for {@link InformationTable#discard(int[], boolean)} method.
	 */
	@Test
	public void testDiscardIntArrayBoolean01() {
		InformationTable informationTable = configuration01.getInformationTable(false);
		
		int[] remainingObjectIndices = new int[] {1, 2, 4}; //indices of remaining objects
		int[] discardedObjectIndices = new int[] {0, 3}; //indices of discarded objects
		
		InformationTable newInformationTable = informationTable.discard(discardedObjectIndices, true);
		
		assertEquals(newInformationTable.getNumberOfObjects(), remainingObjectIndices.length);
		assertEquals(newInformationTable.getNumberOfAttributes(), informationTable.getNumberOfAttributes());
		
		//check fields of the new table
		List<Field[]> expectedFields = configuration01.getListOfFields(remainingObjectIndices);
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
		for (int i = 0; i < remainingObjectIndices.length; i++) {
			assertEquals(newMapper.getId(i), informationTable.getIndex2IdMapper().getId(remainingObjectIndices[i]));
		}
		
		int numberOfObjects = newInformationTable.getNumberOfObjects();
		
		//check decisions of the new table
		Decision[] expectedDecisions = configuration01.getDecisions(remainingObjectIndices);
		Decision[] decisions = newInformationTable.getDecisions(true);
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
		
		//check identifiers of the new table
		Field[] expectedIdentifiers = configuration01.getIdentifiers(remainingObjectIndices);
		Field[] identifiers = newInformationTable.getIdentifiers(false);
		
		assertEquals(expectedIdentifiers, null);
		assertEquals(identifiers, null);
		
		//check identification attribute index of the new table
		assertEquals(newInformationTable.getActiveIdentificationAttributeIndex(), -1);
		assertEquals(configuration01.getActiveIdentificationAttributeIndex(), -1);
	}
	
	/**
	 * Test for {@link InformationTable#discard(int[], boolean)} method.
	 */
	@Test
	public void testDiscardIntArrayBoolean02() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		
		int[] remainingObjectIndices = new int[] {1, 2, 5, 6};
		int[] discardedObjectIndices = new int[] {0, 3, 4, 3}; //not sorted and 3 appears twice
		
		InformationTable newInformationTable = informationTable.discard(discardedObjectIndices, true);
		
		assertEquals(newInformationTable.getNumberOfObjects(), remainingObjectIndices.length);
		assertEquals(newInformationTable.getNumberOfAttributes(), informationTable.getNumberOfAttributes());
		
		//check fields of the new table
		List<Field[]> expectedFields = configuration02.getListOfFields(remainingObjectIndices);
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
		for (int i = 0; i < remainingObjectIndices.length; i++) {
			assertEquals(newMapper.getId(i), informationTable.getIndex2IdMapper().getId(remainingObjectIndices[i]));
		}
		
		int numberOfObjects = newInformationTable.getNumberOfObjects();
		
		//check decisions of the new table
		Decision[] expectedDecisions = configuration02.getDecisions(remainingObjectIndices);
		Decision[] decisions = newInformationTable.getDecisions(true);
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(decisions[i], expectedDecisions[i]);
		}
		
		//check identifiers of the new table
		Field[] expectedIdentifiers = configuration02.getIdentifiers(remainingObjectIndices);
		Field[] identifiers = newInformationTable.getIdentifiers(false);
		
		for (int i = 0; i < numberOfObjects; i++) {
			assertEquals(expectedIdentifiers[i], identifiers[i]);
		}
		
		//check identification attribute index of the new table
		assertEquals(newInformationTable.getActiveIdentificationAttributeIndex(), configuration02.getActiveIdentificationAttributeIndex());
	}
	
	/**
	 * Test for {@link InformationTable#discard(int[], boolean)} method.
	 * Tests if {@link IndexOutOfBoundsException} is thrown if discarded object's index is out of range.
	 */
	@Test
	public void testDiscardIntArrayBoolean03() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		
		int[] discardedObjectIndices = new int[] {0, 3, 4, 7}; //7 - out of range
		
		try {
			informationTable.discard(discardedObjectIndices, true);
			fail("Should throw an exception if discarded object's index is out of range.");
		} catch (IndexOutOfBoundsException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link InformationTable#discard(int[], boolean)} method.
	 * Tests if {@link NullPointerException} is thrown if array of discarded objects' indices is {@code null}.
	 */
	@Test
	public void testDiscardIntArrayBoolean04() {
		InformationTable informationTable = configuration02.getInformationTable(false);
		
		try {
			informationTable.discard(null, true);
			fail("Should throw a NullPointerException if array with discarded objects' indices is null.");
		} catch (NullPointerException exception) {
			//OK
		}
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
	
	//TODO: write more tests for InformationTable#getNumberOfObjects, for the case when there are no active condition evaluation attributes

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
	 * Test for {@link InformationTable#getUniqueDecisions()} method}.
	 * Tests cost-type decision criterion.
	 */
	@Test
	public void testGetUniqueDecisions01() {
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
		Mockito.when(informationTable.getUniqueDecisions()).thenCallRealMethod();
		Mockito.when(informationTable.calculateUniqueDecisions()).thenCallRealMethod();
		
		Decision[] expectedDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
		};
		
		Decision[] uniqueDecisions = informationTable.getUniqueDecisions();
		
		assertEquals(uniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < uniqueDecisions.length; i++) {
			assertEquals(uniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} method}.
	 * Tests cost-type decision criterion.
	 */
	@Test
	public void testGetOrderedUniqueFullyDeterminedDecisions01() {
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
		
		Decision[] orderedUniqueDecisions = informationTable.getOrderedUniqueFullyDeterminedDecisions();
		
		assertEquals(orderedUniqueDecisions.length, 5);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} method}.
	 * Tests gain-type decision criterion and repeating evaluations on decision attribute.
	 */
	@Test
	public void testGetOrderedUniqueFullyDeterminedDecisions02() {
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
		Mockito.when(informationTable.getOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		Mockito.when(informationTable.calculateOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		
		Decision[] expectedDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
		};
		
		Decision[] orderedUniqueDecisions = informationTable.getOrderedUniqueFullyDeterminedDecisions();
		
		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} method}.
	 * Tests sorting in presence of an mv_2-type missing value.
	 */
	@Test
	public void testGetOrderedUniqueFullyDeterminedDecisions03() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int activeDecisionAttributeIndex = 0;
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		Decision[] allDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(UnknownSimpleFieldMV2.getInstance(), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
		};
		
		Mockito.when(informationTable.getDecisions(true)).thenReturn(allDecisions);
		Mockito.when(informationTable.getOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		Mockito.when(informationTable.calculateOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		
		Decision[] expectedDecisions = {
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-3, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(-2, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(1, preferenceType), activeDecisionAttributeIndex),
				new SimpleDecision(IntegerFieldFactory.getInstance().create(5, preferenceType), activeDecisionAttributeIndex),
		};
		
		Decision[] orderedUniqueDecisions = informationTable.getOrderedUniqueFullyDeterminedDecisions();
		
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			System.out.println(orderedUniqueDecisions[i]);
		}
		
		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} method}.
	 * Tests sorting in presence of composite decisions with missing values.
	 */
	@Test
	public void testGetOrderedUniqueFullyDeterminedDecisions04() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int activeDecisionAttributeIndex1 = 0;
		int activeDecisionAttributeIndex2 = 1;
		AttributePreferenceType preferenceType1 = AttributePreferenceType.GAIN;
		AttributePreferenceType preferenceType2 = AttributePreferenceType.GAIN;
		
		Decision[] allDecisions = {
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceType1), UnknownSimpleFieldMV2.getInstance()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(1, preferenceType1), IntegerFieldFactory.getInstance().create(4, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {UnknownSimpleFieldMV2.getInstance(), IntegerFieldFactory.getInstance().create(3, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(0, preferenceType1), IntegerFieldFactory.getInstance().create(5, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(0, preferenceType1), IntegerFieldFactory.getInstance().create(6, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(1, preferenceType1), IntegerFieldFactory.getInstance().create(4, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}), //again (1,4),
				new CompositeDecision(new EvaluationField[] {UnknownSimpleFieldMV2.getInstance(), UnknownSimpleFieldMV2.getInstance()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2})
		};
		
		Mockito.when(informationTable.getDecisions(true)).thenReturn(allDecisions);
		Mockito.when(informationTable.getOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		Mockito.when(informationTable.calculateOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		
		//1 0 0
		//4 5 6
		Decision[] expectedDecisions = {
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(1, preferenceType1), IntegerFieldFactory.getInstance().create(4, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(0, preferenceType1), IntegerFieldFactory.getInstance().create(5, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(0, preferenceType1), IntegerFieldFactory.getInstance().create(6, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2})
		};
		
		Decision[] orderedUniqueDecisions = informationTable.getOrderedUniqueFullyDeterminedDecisions();
		
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			System.out.println(orderedUniqueDecisions[i]);
		}

		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} method}.
	 * Tests sorting in presence of composite decisions, all not fully-determined.
	 */
	@Test
	public void testGetOrderedUniqueFullyDeterminedDecisions05() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int activeDecisionAttributeIndex1 = 0;
		int activeDecisionAttributeIndex2 = 1;
		AttributePreferenceType preferenceType1 = AttributePreferenceType.GAIN;
		AttributePreferenceType preferenceType2 = AttributePreferenceType.GAIN;
		
		Decision[] allDecisions = {
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceType1), UnknownSimpleFieldMV2.getInstance()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {UnknownSimpleFieldMV2.getInstance(), IntegerFieldFactory.getInstance().create(4, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {UnknownSimpleFieldMV2.getInstance(), IntegerFieldFactory.getInstance().create(3, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {UnknownSimpleFieldMV2.getInstance(), UnknownSimpleFieldMV2.getInstance()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2})
		};
		
		Mockito.when(informationTable.getDecisions(true)).thenReturn(allDecisions);
		Mockito.when(informationTable.getOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		Mockito.when(informationTable.calculateOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		
		Decision[] expectedDecisions = {}; //empty list
		Decision[] orderedUniqueDecisions = informationTable.getOrderedUniqueFullyDeterminedDecisions();
		
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			System.out.println(orderedUniqueDecisions[i]);
		}

		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
	}
	
	/**
	 * Test for {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} method}.
	 * Tests sorting in presence of only one fully-determined composite decision.
	 */
	@Test
	public void testGetOrderedUniqueFullyDeterminedDecisions06() {
		InformationTable informationTable = Mockito.mock(InformationTable.class);
		int activeDecisionAttributeIndex1 = 0;
		int activeDecisionAttributeIndex2 = 1;
		AttributePreferenceType preferenceType1 = AttributePreferenceType.GAIN;
		AttributePreferenceType preferenceType2 = AttributePreferenceType.GAIN;
		
		Decision[] allDecisions = {
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(2, preferenceType1), UnknownSimpleFieldMV2.getInstance()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {UnknownSimpleFieldMV2.getInstance(), IntegerFieldFactory.getInstance().create(4, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(3, preferenceType1), IntegerFieldFactory.getInstance().create(3, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2}),
				new CompositeDecision(new EvaluationField[] {UnknownSimpleFieldMV2.getInstance(), UnknownSimpleFieldMV2.getInstance()},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2})
		};
		
		Mockito.when(informationTable.getDecisions(true)).thenReturn(allDecisions);
		Mockito.when(informationTable.getOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		Mockito.when(informationTable.calculateOrderedUniqueFullyDeterminedDecisions()).thenCallRealMethod();
		
		//3
		//3
		Decision[] expectedDecisions = {
				new CompositeDecision(new EvaluationField[] {IntegerFieldFactory.getInstance().create(3, preferenceType1), IntegerFieldFactory.getInstance().create(3, preferenceType2)},
						new int[] {activeDecisionAttributeIndex1, activeDecisionAttributeIndex2})};
		Decision[] orderedUniqueDecisions = informationTable.getOrderedUniqueFullyDeterminedDecisions();
		
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			System.out.println(orderedUniqueDecisions[i]);
		}

		assertEquals(orderedUniqueDecisions.length, expectedDecisions.length);
		for (int i = 0; i < orderedUniqueDecisions.length; i++) {
			assertEquals(orderedUniqueDecisions[i], expectedDecisions[i]);
		}
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
	
	/**
	 * Test for {@link InformationTable#imposePreferenceOrders(boolean))} method}.
	 * Tests information table that does not change after imposition of preference orders.
	 */
	@Test
	public void testImposePreferenceOrders01True() {
		InformationTable informationTable = configuration03.getInformationTable(false);
		assertSame(informationTable, informationTable.imposePreferenceOrders(true));
	}
	
	/**
	 * Test for {@link InformationTable#imposePreferenceOrders(boolean))} method}.
	 * Tests information table that does not change after imposition of preference orders.
	 */
	@Test
	public void testImposePreferenceOrders01False() {
		InformationTable informationTable = configuration03.getInformationTable(false);
		assertSame(informationTable, informationTable.imposePreferenceOrders(false));
	}
	
	private void testImposePreferenceOrders02(boolean transformNominalAttributesWith3PlusValues) {
		EvaluationAttribute expectedAttribute;
		UnknownSimpleFieldMV2 mv2 = UnknownSimpleFieldMV2.getInstance();
		UnknownSimpleFieldMV15 mv15 = UnknownSimpleFieldMV15.getInstance();
		AttributePreferenceType attrPrefType; //temporary variable
		int i;
		String gainSuffix = InformationTable.attributeNameSuffixGain;
		String costSuffix = InformationTable.attributeNameSuffixCost;
		String noneSuffix = InformationTable.attributeNameSuffixNone;
		
		String[][] expectedFieldsAsText;
		
		InformationTable informationTable = configuration04.getInformationTable(false);
		InformationTable newInformationTable = informationTable.imposePreferenceOrders(transformNominalAttributesWith3PlusValues);
		
		assertEquals(newInformationTable.getNumberOfAttributes(), transformNominalAttributesWith3PlusValues ? 17 : 15);
		
		//------------------------------
		//Compare old and new attributes
		//------------------------------
		
		//the same attributes
		assertEquals(newInformationTable.getAttribute(i = 0), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 1), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 2), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 3), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 4), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 5), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 6), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 7), informationTable.getAttribute(i));
		assertEquals(newInformationTable.getAttribute(i = 8), informationTable.getAttribute(i));
		
		//new attributes
		expectedAttribute = new EvaluationAttribute("a-cond-int-none"+gainSuffix, true, AttributeType.CONDITION, //NONE, int => 2 attributes with inverse preference orders
				IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType);
		assertEquals(newInformationTable.getAttribute(i = 9), expectedAttribute); //the same attribute
		expectedAttribute = new EvaluationAttribute("a-cond-int-none"+costSuffix, true, AttributeType.CONDITION, //NONE, int => 2 attributes with inverse preference orders
				IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv2, attrPrefType);
		assertEquals(newInformationTable.getAttribute(i = 10), expectedAttribute); //the same attribute
		//
		expectedAttribute = new EvaluationAttribute("a-cond-real-none"+gainSuffix, true, AttributeType.CONDITION, //NONE, real => 2 attributes with inverse preference orders
				RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv15, attrPrefType);
		assertEquals(newInformationTable.getAttribute(i = 11), expectedAttribute); //the same attribute
		expectedAttribute = new EvaluationAttribute("a-cond-real-none"+costSuffix, true, AttributeType.CONDITION, //NONE, real => 2 attributes with inverse preference orders
				RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType);
		assertEquals(newInformationTable.getAttribute(i = 12), expectedAttribute); //the same attribute
		
//		try {
//			expectedAttribute = new EvaluationAttribute("a-cond-enum2-none"+gainSuffix, true, AttributeType.CONDITION, //NONE, enum, 2 domain values => 2 enum attributes with inverse preference orders
//					EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv2, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 13), expectedAttribute); //the same attribute
//		} catch (NoSuchAlgorithmException exception) {
//			throw new InvalidValueException("NoSuchAlgorithmException thrown by when creating domain of an enumeration field.");
//		}
//		try {
//			expectedAttribute = new EvaluationAttribute("a-cond-enum2-none"+costSuffix, true, AttributeType.CONDITION, //NONE, enum, 2 domain values => 2 enum attributes with inverse preference orders
//					EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"a", "b"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv2, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 14), expectedAttribute); //the same attribute
//		} catch (NoSuchAlgorithmException exception) {
//			throw new InvalidValueException("NoSuchAlgorithmException thrown by when creating domain of an enumeration field.");
//		}
		
		assertEquals(newInformationTable.getAttribute(i = 13), informationTable.getAttribute(11)); //the same nominal 2-valued attribute
		
		if (transformNominalAttributesWith3PlusValues) {
//			//NONE, enum, 3 domain values => 3 pairs of int attributes with inverse preference orders
//			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_l"+gainSuffix, true, AttributeType.CONDITION,
//					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv15, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 14), expectedAttribute); //the same attribute
//			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_l"+costSuffix, true, AttributeType.CONDITION,
//					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 15), expectedAttribute); //the same attribute
			
			//NONE, enum, 3 domain values => 3 int attributes without preference orders
			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_l"+noneSuffix, true, AttributeType.CONDITION,
					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType);
			assertEquals(newInformationTable.getAttribute(i = 14), expectedAttribute); //the same attribute
			
			//
			
//			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_m"+gainSuffix, true, AttributeType.CONDITION,
//					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv15, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 16), expectedAttribute); //the same attribute
//			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_m"+costSuffix, true, AttributeType.CONDITION,
//					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 17), expectedAttribute); //the same attribute
			
			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_m"+noneSuffix, true, AttributeType.CONDITION,
					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType);
			assertEquals(newInformationTable.getAttribute(i = 15), expectedAttribute); //the same attribute
			
			//
			
//			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_h"+gainSuffix, true, AttributeType.CONDITION,
//					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.GAIN), mv15, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 18), expectedAttribute); //the same attribute
//			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_h"+costSuffix, true, AttributeType.CONDITION,
//					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.COST), mv15, attrPrefType);
//			assertEquals(newInformationTable.getAttribute(i = 19), expectedAttribute); //the same attribute
			
			expectedAttribute = new EvaluationAttribute("a-cond-enum3-none"+"_h"+noneSuffix, true, AttributeType.CONDITION,
					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType);
			assertEquals(newInformationTable.getAttribute(i = 16), expectedAttribute); //the same attribute
			
			//--------------------------
			//Compare old and new fields (by comparing their text representations - simpler approach)
			//--------------------------
			expectedFieldsAsText = new String[][] {
				{ "l", "m", "h",  "o1", "00000000-0000-0000-0000-000000000000",  "l", "m",  "1", "2.0",  "3","3", "1.0","1.0",  "a",  "1","0","0"},
				{ "m", "h", "l",  "o2", "00000000-0000-0000-0000-000000000001",  "m", "h",  "2", "3.0",  "4","4", "2.0","2.0",  "b",  "0","1","0"},
				{ "h", "m", "l",  "o3", "00000000-0000-0000-0000-000000000002",  "h", "l",  "3", "4.0",  "5","5", "3.0","3.0",  "a",  "0","0","1"},
			};
		} else { //transformNominalAttributesWith3PlusValues == false
			try {
				expectedAttribute = new EvaluationAttribute("a-cond-enum3-none", true, AttributeType.CONDITION, //NONE, enum, 3 domain values => leaved "as is"
						EnumerationFieldFactory.getInstance().create(new ElementList(new String[] {"l", "m", "h"}), EnumerationField.DEFAULT_VALUE, attrPrefType = AttributePreferenceType.NONE), mv15, attrPrefType);
			} catch (NoSuchAlgorithmException e) {
				throw new InvalidValueException("NoSuchAlgorithmException thrown when creating domain of an enumeration field.");
			}
			assertEquals(newInformationTable.getAttribute(i = 14), expectedAttribute); //the same attribute
			
			//--------------------------
			//Compare old and new fields (by comparing their text representations - simpler approach)
			//--------------------------
			expectedFieldsAsText = new String[][] {
				{ "l", "m", "h",  "o1", "00000000-0000-0000-0000-000000000000",  "l", "m",  "1", "2.0",  "3","3", "1.0","1.0",  "a",  "l"},
				{ "m", "h", "l",  "o2", "00000000-0000-0000-0000-000000000001",  "m", "h",  "2", "3.0",  "4","4", "2.0","2.0",  "b",  "m"},
				{ "h", "m", "l",  "o3", "00000000-0000-0000-0000-000000000002",  "h", "l",  "3", "4.0",  "5","5", "3.0","3.0",  "a",  "h"},
			};
		}
		
		for (int objectIndex = 0; objectIndex < expectedFieldsAsText.length; objectIndex++) {
			for (int attributeIndex = 0; attributeIndex < expectedFieldsAsText[objectIndex].length; attributeIndex++) {
				assertEquals(expectedFieldsAsText[objectIndex][attributeIndex], newInformationTable.getField(objectIndex, attributeIndex).toString());
			}
		}
	}
	
	/**
	 * Test for {@link InformationTable#imposePreferenceOrders(boolean))} method}.
	 * Tests information table that changes after imposition of preference orders.
	 * Invokes tested method with parameter equal to {@code true}.
	 */
	@Test
	public void testImposePreferenceOrders02True() {
		testImposePreferenceOrders02(true);
	}
	
	/**
	 * Test for {@link InformationTable#imposePreferenceOrders(boolean))} method}.
	 * Tests information table that changes after imposition of preference orders.
	 * Invokes tested method with parameter equal to {@code false}.
	 */
	@Test
	public void testImposePreferenceOrders02False() {
		testImposePreferenceOrders02(false);
	}
	
	/**
	 * Test for {@link InformationTable#serialize()} method}.
	 * Tests serialization with only learning attributes.
	 */
	@Test
	public void testSerialize01() {
		InformationTable informationTable = configuration04.getInformationTable(false);
		String expected = originalOnlyLearningAttributesSerialization;
		String serialized = informationTable.serialize(true);
		System.out.println(serialized); //!
		assertEquals(serialized, expected);
	}
	
	/**
	 * Test for {@link InformationTable#serialize()} method}.
	 * Tests serialization with all attributes.
	 */
	@Test
	public void testSerialize02() {
		InformationTable informationTable = configuration04.getInformationTable(false);
		String expected = originalAllAttributesSerialization;
		String serialized = informationTable.serialize(false);
		System.out.println(serialized); //!
		assertEquals(serialized, expected);
	}
	
	/**
	 * Test for {@link InformationTable#getHash} method}.
	 * Tests if digest algorithm "SHA-256" is supported by {@link MessageDigest}. Moreover,
	 * test if encoding "UTF-8" is supported by {@link String#getBytes(String)}. 
	 */
	@Test
	public void testGetHash01() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.serialize(true)).thenReturn(originalOnlyLearningAttributesSerialization);
		Mockito.when(informationTableMock.getHash()).thenCallRealMethod();
		String expected = originalOnlyLearningAttributesHash;
		String hash = informationTableMock.getHash();
		System.out.println(hash + " (original hash)"); //!
		assertEquals(hash, expected);
	}
	
	/**
	 * Test for {@link InformationTable#getHash} method}.
	 * Tests if slight modification of serialized string influences the hash.
	 */
	@Test
	public void testGetHash02() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.serialize(true)).thenReturn(originalOnlyLearningAttributesSerialization.replaceFirst(
				"1:+;a-dec-enum3-none;decision;none;noneEnum(l,m,h);mv1.5|",
				"1:+;a-dec-enum3-none;decision;none;noneEnum(l,m,h,vh);mv1.5|"));
		Mockito.when(informationTableMock.getHash()).thenCallRealMethod();
		String hash = informationTableMock.getHash();
		System.out.println(hash); //!
		assertNotEquals(hash, originalOnlyLearningAttributesHash);
	}
	
	/**
	 * Test for {@link InformationTable#getHash} method}.
	 * Tests if slight modification of serialized string influences the hash.
	 */
	@Test
	public void testGetHash03() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.serialize(true)).thenReturn(originalOnlyLearningAttributesSerialization.replaceFirst(
				"5:+;a-cond-enum3-gain;condition;gain;gainEnum(l,m,h);mv2|",
				"5:-;a-cond-enum3-gain;condition;gain;gainEnum(l,m,h);mv2|"));
		Mockito.when(informationTableMock.getHash()).thenCallRealMethod();
		String hash = informationTableMock.getHash();
		System.out.println(hash); //!
		assertNotEquals(hash, originalOnlyLearningAttributesHash);
	}
	
	/**
	 * Test for {@link InformationTable#getHash} method}.
	 * Tests if slight modification of serialized string influences the hash.
	 */
	@Test
	public void testGetHash04() {
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		Mockito.when(informationTableMock.serialize(true)).thenReturn(originalOnlyLearningAttributesSerialization.replaceFirst(
				"m|l|m|1|2.0|3|1.0|a|l",
				"m|l|m|1|2.0|3|1.01|a|l"));
		Mockito.when(informationTableMock.getHash()).thenCallRealMethod();
		String hash = informationTableMock.getHash();
		System.out.println(hash); //!
		assertNotEquals(hash, originalOnlyLearningAttributesHash);
	}
	
}
