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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.data.json.ObjectBuilder;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;


/**
 * Tests for {@link InformationTableBuilder}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class InformationTableBuilderTest {
	
	private String jsonAttributes1 = "[" + 
			 "{" +
			    "\"name\": \"a1\"," +
			    "\"active\": true," +
			    "\"preferenceType\": \"none\"," +
			    "\"type\": \"condition\"," +
			    "\"valueType\": \"integer\"," +
			    "\"missingValueType\": \"mv2\"" +
			  "}," +
			  "{" +
			    "\"name\": \"c1\"," +
			    "\"active\": true," +
			    "\"preferenceType\": \"cost\"," +
			    "\"type\": \"condition\"," +
			    "\"valueType\": \"real\"," +
			    "\"missingValueType\": \"mv2\"" +
			  "}," +
			  "{" +
			    "\"name\": \"d1\"," +
			    "\"active\": true," +
			    "\"preferenceType\": \"gain\"," +
			    "\"type\": \"decision\"," +
			    "\"valueType\": \"enumeration\"," +
			    "\"domain\": [\"a\",\"b\",\"c\"]," +
			    "\"missingValueType\": \"mv2\"" +
			  "}" +
		"]";
		
	private Attribute [] attributes1 = null;
	
	private void setUp01() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		attributes1 = gson.fromJson(jsonAttributes1, Attribute[].class);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#InformationTableBuilder(Attribute[])}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder01() {
		setUp01();
		InformationTableBuilder iTB = new InformationTableBuilder(attributes1);
		assertTrue(iTB != null);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#InformationTableBuilder(Attribute[])}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder02() {
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(null);});
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#InformationTableBuilder(Attribute[], String)}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder03() {
		String separator = null;
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(null, separator);});
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(new Attribute[0], separator);});
	}

	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#InformationTableBuilder(Attribute[], String[])}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder04() {
		String[] missingValues = null;
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(null, missingValues);});
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(new Attribute[0], missingValues);});
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#InformationTableBuilder(Attribute[], String, String[])}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder05() {
		String separator = null;
		String[] missingValues = null;
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(null, separator, missingValues);});
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(new Attribute[0], separator, missingValues);});
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(null, "", missingValues);});
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(new Attribute[0], "", missingValues);});
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(null, separator, new String[] {""});});
		assertThrows(NullPointerException.class, () -> {new InformationTableBuilder(new Attribute[0], separator, new String[] {""});});
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String)}.
	 */
	@Test
	void testAddObject01() {
		setUp01();
		InformationTableBuilder informationTableBuilder = new InformationTableBuilder(attributes1, ",");
		informationTableBuilder.addObject("1, 1.0, a");
		informationTableBuilder.addObject("2, 2.0, b");
		InformationTable informationTable = informationTableBuilder.build();
		assertTrue(informationTable.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(0, 1).isEqualTo(RealFieldFactory.getInstance().create(1.0, AttributePreferenceType.COST)) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(1, 2).isEqualTo(EnumerationFieldFactory.getInstance().create(((EnumerationField) ((EvaluationAttribute)informationTable.getAttributes()[2]).getValueType()).getElementList(), 
												1, AttributePreferenceType.GAIN)) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String)}.
	 */
	@Test
	void testAddObject02() {
		setUp01();
		InformationTableBuilder informationTableBuilder = new InformationTableBuilder(attributes1, ",", new String[] {"?", "*"})  ;
		informationTableBuilder.addObject("1, ?, a");
		informationTableBuilder.addObject("2, 2.0, *");
		InformationTable informationTable = informationTableBuilder.build();
		assertTrue(informationTable.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(0, 1).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(1, 2).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String)}.
	 */
	@Test
	void testAddObject03() {
		setUp01();
		InformationTableBuilder informationTableBuilder = new InformationTableBuilder(attributes1, "", new String[] {""});
		assertThrows(IndexOutOfBoundsException.class, () -> {informationTableBuilder.addObject("1, 1.0, a");});
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String)}.
	 */
	@Test
	void testAddObject04() {
		setUp01();
		InformationTableBuilder informationTableBuilder = new InformationTableBuilder(attributes1, ";", new String[] {""});
		informationTableBuilder.addObject("1; 1.0; a");
		informationTableBuilder.addObject("2; 2.0; b");
		InformationTable informationTable = informationTableBuilder.build();
		assertTrue(informationTable.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(0, 1).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(1, 2).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAddObject05() {
		setUp01();
		InformationTableBuilder informationTableBuilder = new InformationTableBuilder(attributes1, ",", new String[] {""});
		informationTableBuilder.addObject("1, 1.0, a");
		informationTableBuilder.addObject("2; 2.0; b", ";");
		InformationTable informationTable = informationTableBuilder.build();
		assertTrue(informationTable.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(0, 1).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
		assertTrue(informationTable.getField(1, 2).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test method for {@link ObjectBuilder} and {@link InformationTableBuilder}.
	 */
	@Test
	void testConstructionOfInformationTable01() {
		// load attributes
		Attribute [] attributes = null;
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		JsonReader jsonReader = null;
		try {
			jsonReader = new JsonReader(new FileReader("src/test/resources/data/csv/prioritisation.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		if (jsonReader != null) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
			try {
				jsonReader.close();
			}
			catch (IOException ex) {
				System.out.println(ex.toString());
			}
		}
		else {
			fail("Unable to load JSON test file with definition of attributes");
		}
		
		// load objects
		jsonReader = null;
		JsonElement json = null;
		try {
			jsonReader = new JsonReader(new FileReader("src/test/resources/data/json/examples.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		if (jsonReader != null) {
			JsonParser jsonParser = new JsonParser();
			json = jsonParser.parse(jsonReader);
			try {
				jsonReader.close();
			}
			catch (IOException ex) {
				System.out.println(ex.toString());
			}
		}
		else {
			fail("Unable to load JSON test file with definition of objects");
		}
		ObjectBuilder ob = new ObjectBuilder.Builder(attributes).build();
		List<String []> objects = null;
		objects = ob.getObjects(json);
		assertTrue(objects != null);
		assertEquals(objects.size(), 2);
		
		// build information table
		InformationTableBuilder informationTableBuilder = new InformationTableBuilder(attributes, ",", new String[] {"?"});
		for (int i = 0; i < objects.size(); i++) {
			informationTableBuilder.addObject(objects.get(i));
		}		
		
		InformationTable informationTable = informationTableBuilder.build();
		assertTrue(informationTable != null);
		assertEquals(informationTable.getNumberOfObjects(), 2);
		
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#buildFromCSVFile(String, String, boolean)}.
	 */
	@Test
	void testConstructionOfInformationTable02() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.buildFromCSVFile("src/test/resources/data/csv/prioritisation.json", "src/test/resources/data/csv/prioritisation.csv", true);
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable != null);
		assertEquals(11, informationTable.getNumberOfAttributes());
		assertEquals(2, informationTable.getNumberOfObjects());		
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#buildFromCSVFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable03() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.buildFromCSVFile("src/test/resources/data/csv/prioritisation.json", "");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#buildFromCSVFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable04() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.buildFromCSVFile("", "");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#buildFromJSONFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable05() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.buildFromJSONFile("src/test/resources/data/csv/prioritisation.json", "src/test/resources/data/json/examples.json");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable != null);
		assertEquals(11, informationTable.getNumberOfAttributes());
		assertEquals(2, informationTable.getNumberOfObjects());		
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#buildFromJSONFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable06() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.buildFromJSONFile("src/test/resources/data/csv/prioritisation.json", "");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#buildFromJSONFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable07() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.buildFromJSONFile("", "");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean)}.
	 */
	@Test
	void testConstructionOfInformationTable08() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.safelyBuildFromCSVFile("src/test/resources/data/csv/prioritisation.json", "src/test/resources/data/csv/prioritisation.csv", true);
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable != null);
		assertEquals(11, informationTable.getNumberOfAttributes());
		assertEquals(2, informationTable.getNumberOfObjects());		
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean)}.
	 */
	@Test
	void testConstructionOfInformationTable09() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.safelyBuildFromCSVFile("src/test/resources/data/csv/prioritisation.json", "", true);
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean)}.
	 */
	@Test
	void testConstructionOfInformationTable11() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.safelyBuildFromCSVFile("", "", true);
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#safelyBuildFromJSONFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable12() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.safelyBuildFromJSONFile("src/test/resources/data/csv/prioritisation.json", "src/test/resources/data/json/examples.json");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable != null);
		assertEquals(11, informationTable.getNumberOfAttributes());
		assertEquals(2, informationTable.getNumberOfObjects());		
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#safelyBuildFromJSONFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable13() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.safelyBuildFromJSONFile("src/test/resources/data/csv/prioritisation.json", "");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
	/**
	 * Test method for {@link InformationTableBuilder#safelyBuildFromJSONFile(String, String)}.
	 */
	@Test
	void testConstructionOfInformationTable14() {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.safelyBuildFromJSONFile("", "");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		assertTrue(informationTable == null);
	}
	
}
