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

import java.io.FileNotFoundException;
import java.io.FileReader;
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
 * InformationTableBuilderTest
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
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#setAttributesFromJSON(java.lang.String)}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder01() {
		setUp01();
		InformationTableBuilder iTB = new InformationTableBuilder(attributes1);
		assertTrue(iTB != null);
	}

	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAddObject01() {
		setUp01();
		InformationTableBuilder iTB = new InformationTableBuilder(attributes1, ",");
		iTB.addObject("1, 1.0, a");
		iTB.addObject("2, 2.0, b");
		InformationTable iT = iTB.build();
		assertTrue(iT.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(0, 1).isEqualTo(RealFieldFactory.getInstance().create(1.0, AttributePreferenceType.COST)) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(1, 2).isEqualTo(EnumerationFieldFactory.getInstance().create(((EnumerationField) ((EvaluationAttribute)iT.getAttributes()[2]).getValueType()).getElementList(), 
												1, AttributePreferenceType.GAIN)) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAddObject02() {
		setUp01();
		InformationTableBuilder iTB = new InformationTableBuilder(attributes1, ",", new String[] {"?", "*"})  ;
		iTB.addObject("1, ?, a");
		iTB.addObject("2, 2.0, *");
		InformationTable iT = iTB.build();
		assertTrue(iT.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(0, 1).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(1, 2).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test method for {@link ObjectBuilder} and {@link InformationTableBuilder}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder02() {
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
		}
		else {
			fail("Unable to load JSON test file with definition of attributes");
		}
		
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
		}
		else {
			fail("Unable to load JSON test file with definition of objects");
		}
		
		ObjectBuilder ob = new ObjectBuilder(attributes);
		List<String []> objects = null;
		objects = ob.getObjects(json);
		assertTrue(objects != null);
		assertEquals(objects.size(), 2);
		
		InformationTableBuilder iTB = new InformationTableBuilder(attributes, ",", new String[] {"?"});
		for (int i = 0; i < objects.size(); i++) {
			iTB.addObject(objects.get(i));
		}		
		
		InformationTable iT = iTB.build();
		assertTrue(iT != null);
		assertEquals(iT.getNumberOfObjects(), 2);
		
	}

}
