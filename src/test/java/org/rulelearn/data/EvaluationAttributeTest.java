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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.data.json.EvaluationAttributeSerializer;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * Test for {@link EvaluationAttribute}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class EvaluationAttributeTest {
	
	private Attribute [] attributes = null;
	
	/**
	 * Set attributes for {@link IntegerField} tests
	 */
	private void setI01() {
		attributes = new Attribute[36];
		UnknownSimpleField [] unknownTypeFields = {new UnknownSimpleFieldMV15(), new UnknownSimpleFieldMV2()};
		boolean [] activities = {false, true};
		AttributeType [] types = {AttributeType.CONDITION, AttributeType.DESCRIPTION, AttributeType.DECISION};
		AttributePreferenceType [] preferenceTypes = {AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN};
		
		int i = 0;
		// construct all variants of attributes
		for (UnknownSimpleField unknownTypeField : unknownTypeFields)
			for (boolean activity : activities ) 
				for (AttributeType type: types)
					for (AttributePreferenceType preferenceType: preferenceTypes) {
						attributes[i++] = new EvaluationAttribute("a"+i, activity, type, IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType), 
							unknownTypeField, preferenceType);
					}
	}
	
	/**
	 * Tests equals and hashCode
	 */
	@Test
	public void testEqualsI01() {
		setI01();
		for (int i = 0; i < attributes.length; i++) 
			for (int j = (i + 1); j < attributes.length; j++) {
				assertNotEquals(attributes[i], attributes[j]);
				assertFalse(attributes[i].hashCode() == attributes[j].hashCode());
			}		
	}

	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionI01() {
		setI01();
			
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new EvaluationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Set attributes for {@link RealField} tests
	 */
	private void setR01() {
		attributes = new Attribute[36];
		UnknownSimpleField [] unknownTypeFields = {new UnknownSimpleFieldMV15(), new UnknownSimpleFieldMV2()};
		boolean [] activities = {false, true};
		AttributeType [] types = {AttributeType.CONDITION, AttributeType.DESCRIPTION, AttributeType.DECISION};
		AttributePreferenceType [] preferenceTypes = {AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN};
		
		int i = 0;
		// construct all variants of attributes
		for (UnknownSimpleField unknownTypeField : unknownTypeFields)
			for (boolean activity : activities ) 
				for (AttributeType type: types)
					for (AttributePreferenceType preferenceType: preferenceTypes) {
						attributes[i++] = new EvaluationAttribute("a"+i, activity, type, RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType), 
							unknownTypeField, preferenceType);
					}
	}
	
	/**
	 * Tests equals and hashCode
	 */
	@Test
	public void testEqualsR01() {
		setR01();
		for (int i = 0; i < attributes.length; i++) 
			for (int j = (i + 1); j < attributes.length; j++) {
				assertNotEquals(attributes[i], attributes[j]);
				assertFalse(attributes[i].hashCode() == attributes[j].hashCode());
			}		
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionR01() {
		setR01();
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new EvaluationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Set attributes for {@link EnumerationField} tests
	 */
	private void setE01() {
		attributes = new Attribute[36];
		String [] labels = {"a", "b", "c"};
		UnknownSimpleField [] unknownTypeFields = {new UnknownSimpleFieldMV15(), new UnknownSimpleFieldMV2()};
		boolean [] activities = {false, true};
		AttributeType [] types = {AttributeType.CONDITION, AttributeType.DESCRIPTION, AttributeType.DECISION};
		AttributePreferenceType [] preferenceTypes = {AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN};
		
		int i = 0;
		// construct all variants of attributes
		for (UnknownSimpleField unknownTypeField : unknownTypeFields)
			for (boolean activity : activities ) 
				for (AttributeType type: types)
					for (AttributePreferenceType preferenceType: preferenceTypes) {
						try {
							attributes[i++] = new EvaluationAttribute("a"+i, activity, type, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
																EnumerationField.DEFAULT_VALUE, preferenceType), unknownTypeField, preferenceType);
						}
						catch (NoSuchAlgorithmException ex) {
							System.out.println(ex);
						}
					}
	}
	
	/**
	 * Tests equals and hashCode
	 */
	@Test
	public void testEqualsE01() {
		setE01();
		for (int i = 0; i < attributes.length; i++) 
			for (int j = (i + 1); j < attributes.length; j++) {
				assertNotEquals(attributes[i], attributes[j]);
				assertFalse(attributes[i].hashCode() == attributes[j].hashCode());
			}		
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionE01() {
		setE01();
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Set attributes for {@link PairField} tests
	 */
	private void setP01() {
		attributes = new Attribute[36];
		UnknownSimpleField [] unknownTypeFields = {new UnknownSimpleFieldMV15(), new UnknownSimpleFieldMV2()};
		boolean [] activities = {false, true};
		AttributeType [] types = {AttributeType.CONDITION, AttributeType.DESCRIPTION, AttributeType.DECISION};
		AttributePreferenceType [] preferenceTypes = {AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN};
		
		int i = 0;
		// construct all variants of attributes
		for (UnknownSimpleField unknownTypeField : unknownTypeFields)
			for (boolean activity : activities ) 
				for (AttributeType type: types)
					for (AttributePreferenceType preferenceType: preferenceTypes) {
						attributes[i++] = new EvaluationAttribute("a"+i, activity, type, new PairField<IntegerField>(
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType), 
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType)),									
								unknownTypeField, preferenceType);
					}
	}
	
	/**
	 * Set attributes for {@link PairField} tests
	 */
	private void setP02() {
		attributes = new Attribute[36];
		UnknownSimpleField [] unknownTypeFields = {new UnknownSimpleFieldMV15(), new UnknownSimpleFieldMV2()};
		boolean [] activities = {false, true};
		AttributeType [] types = {AttributeType.CONDITION, AttributeType.DESCRIPTION, AttributeType.DECISION};
		AttributePreferenceType [] preferenceTypes = {AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN};
		
		int i = 0;
		// construct all variants of attributes
		for (UnknownSimpleField unknownTypeField : unknownTypeFields)
			for (boolean activity : activities ) 
				for (AttributeType type: types)
					for (AttributePreferenceType preferenceType: preferenceTypes) {
						attributes[i++] = new EvaluationAttribute("a"+i, activity, type, new PairField<RealField>(
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType), 
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType)),									
								unknownTypeField, preferenceType);
					}
	}
	
	/**
	 * Set attributes for {@link PairField} tests
	 */
	private void setP03() {
		attributes = new Attribute[36];
		String [] labels = {"a", "b", "c"};
		UnknownSimpleField [] unknownTypeFields = {new UnknownSimpleFieldMV15(), new UnknownSimpleFieldMV2()};
		boolean [] activities = {false, true};
		AttributeType [] types = {AttributeType.CONDITION, AttributeType.DESCRIPTION, AttributeType.DECISION};
		AttributePreferenceType [] preferenceTypes = {AttributePreferenceType.NONE, AttributePreferenceType.COST, AttributePreferenceType.GAIN};
		
		int i = 0;
		// construct all variants of attributes
		for (UnknownSimpleField unknownTypeField : unknownTypeFields)
			for (boolean activity : activities ) 
				for (AttributeType type: types)
					for (AttributePreferenceType preferenceType: preferenceTypes) {
						try {
							attributes[i++] = new EvaluationAttribute("a"+i, activity, type, new PairField<EnumerationField>(
									EnumerationFieldFactory.getInstance().create(new ElementList(labels), EnumerationField.DEFAULT_VALUE, preferenceType), 
									EnumerationFieldFactory.getInstance().create(new ElementList(labels), EnumerationField.DEFAULT_VALUE, preferenceType)),
									unknownTypeField, preferenceType);
						}
						catch (NoSuchAlgorithmException ex) {
							System.out.println(ex);
						}
					}
	}
	
	/**
	 * Tests equals and hashCode
	 */
	@Test
	public void testEqualsP01() {
		setP01();
		for (int i = 0; i < attributes.length; i++) 
			for (int j = (i + 1); j < attributes.length; j++) {
				assertNotEquals(attributes[i], attributes[j]);
				assertFalse(attributes[i].hashCode() == attributes[j].hashCode());
			}
		setP02();
		for (int i = 0; i < attributes.length; i++) 
			for (int j = (i + 1); j < attributes.length; j++) {
				assertNotEquals(attributes[i], attributes[j]);
				assertFalse(attributes[i].hashCode() == attributes[j].hashCode());
			}
		setP03();
		for (int i = 0; i < attributes.length; i++) 
			for (int j = (i + 1); j < attributes.length; j++) {
				assertNotEquals(attributes[i], attributes[j]);
				assertFalse(attributes[i].hashCode() == attributes[j].hashCode());
			}
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionP01() {
		setP01();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
		
		setP02();
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gson = gsonBuilder.setPrettyPrinting().create();
		json = gson.toJson(attributes);
		testAttributes= gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
		
		setP03();
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gson = gsonBuilder.setPrettyPrinting().create();
		json = gson.toJson(attributes);
		testAttributes= gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionI02() {
		Attribute [] attributes = new Attribute[3];
		
		Attribute attribute = new EvaluationAttribute("a1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.NONE), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
		attributes[0] = attribute;
		attribute = new EvaluationAttribute("c1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.COST), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new EvaluationAttribute("g1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[2] = attribute;
	
		String json = "[" + 
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
				    "\"valueType\": \"integer\"," +
				    "\"missingValueType\": \"mv2\"" +
				  "}," +
				  "{" +
				    "\"name\": \"g1\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"gain\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": \"integer\"," +
				    "\"missingValueType\": \"mv2\"" +
				  "}" +
			"]"; 
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		// compare result of deserialization to declaration 
		Attribute [] testAttributes = gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
		
		// compare serialized (and deserialized) declaration
		json = gson.toJson(attributes);
		testAttributes = gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionR02() {
		Attribute [] attributes = new Attribute[3];
		
		Attribute attribute = new EvaluationAttribute("a2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.NONE), 
				new UnknownSimpleFieldMV15(), AttributePreferenceType.NONE);
		attributes[0] = attribute;
		attribute = new EvaluationAttribute("c2", false, AttributeType.CONDITION, RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.COST), 
				new UnknownSimpleFieldMV15(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new EvaluationAttribute("g2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 
				new UnknownSimpleFieldMV15(), AttributePreferenceType.GAIN);
		attributes[2] = attribute;
	
		String json = "[" + 
				 "{" +
				    "\"name\": \"a2\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"none\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": \"real\"," +
				    "\"missingValueType\": \"mv1.5\"" +
				  "}," +
				  "{" +
				    "\"name\": \"c2\"," +
				    "\"active\": false," +
				    "\"preferenceType\": \"cost\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": \"real\"," +
				    "\"missingValueType\": \"mv1.5\"" +
				  "}," +
				  "{" +
				    "\"name\": \"g2\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"gain\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": \"real\"," +
				    "\"missingValueType\": \"mv1.5\"" +
				  "}" +
			"]"; 
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		// compare result of deserialization to declaration 
		Attribute [] testAttributes = gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
		
		// compare serialized (and deserialized) declaration
		json = gson.toJson(attributes);
		testAttributes = gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionE02() {
		Attribute [] attributes = new Attribute[3];
		String [] labels = {"a", "b", "c"};
		
		Attribute attribute;
		try {	
			attribute = new EvaluationAttribute("a3", true, AttributeType.DESCRIPTION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
					EnumerationField.DEFAULT_VALUE, AttributePreferenceType.NONE), new UnknownSimpleFieldMV15(), AttributePreferenceType.NONE);
			attributes[0] = attribute;
			attribute = new EvaluationAttribute("c3", true, AttributeType.CONDITION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
					EnumerationField.DEFAULT_VALUE, AttributePreferenceType.COST), new UnknownSimpleFieldMV15(), AttributePreferenceType.COST);
			attributes[1] = attribute;
			attribute = new EvaluationAttribute("g3", true, AttributeType.DECISION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
					EnumerationField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV15(), AttributePreferenceType.GAIN);
			attributes[2] = attribute;
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		String json = "[" + 
				 "{" +
				    "\"name\": \"a3\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"none\"," +
				    "\"type\": \"description\"," +
				    "\"valueType\": \"enumeration\"," +
				    "\"domain\": [\"a\",\"b\",\"c\"]," +
				    "\"missingValueType\": \"mv1.5\"" +
				  "}," +
				  "{" +
				    "\"name\": \"c3\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"cost\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": \"enumeration\"," +
				    "\"domain\": [\"a\",\"b\",\"c\"]," +
				    "\"missingValueType\": \"mv1.5\"" +
				  "}," +
				  "{" +
				    "\"name\": \"g3\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"gain\"," +
				    "\"type\": \"decision\"," +
				    "\"valueType\": \"enumeration\"," +
				    "\"domain\": [\"a\",\"b\",\"c\"]," +
				    "\"missingValueType\": \"mv1.5\"" +
				  "}" +
			"]"; 

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		// compare result of deserialization to declaration 
		Attribute [] testAttributes = gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
		
		// compare serialized (and deserialized) declaration
		json = gson.toJson(attributes);
		testAttributes = gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionP02() {
		Attribute [] attributes = new Attribute[3];
		try {
			String [] values = {"0", "1", "2"}; 
			ElementList domain = new ElementList(values);
			Attribute attribute = new EvaluationAttribute("a4", true, AttributeType.CONDITION, 
					new PairField<EnumerationField>(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.NONE), 
							EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.NONE)), 
					new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
			attributes[0] = attribute;
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		Attribute attribute = new EvaluationAttribute("c4", true, AttributeType.CONDITION, 
				new PairField<IntegerField>(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.COST), 
						IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.COST)), new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new EvaluationAttribute("g4", false, AttributeType.CONDITION, 
				new PairField<RealField>(RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 
						RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN)), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[2] = attribute;
		
		String json = "[" + 
				 "{" +
				    "\"name\": \"a4\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"none\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": [\"pair\",\"enumeration\"]," +
				    "\"domain\": [\"0\",\"1\",\"2\"]," +
				    "\"missingValueType\": \"mv2\"" +
				  "}," +
				  "{" +
				    "\"name\": \"c4\"," +
				    "\"active\": true," +
				    "\"preferenceType\": \"cost\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": [\"integer\",\"pair\"]," +
				    "\"missingValueType\": \"mv2\"" +
				  "}," +
				  "{" +
				    "\"name\": \"g4\"," +
				    "\"active\": false," +
				    "\"preferenceType\": \"gain\"," +
				    "\"type\": \"condition\"," +
				    "\"valueType\": [\"pair\",\"real\"]," +
				    "\"missingValueType\": \"mv2\"" +
				  "}" +
			"]";
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		// compare result of deserialization to declaration 
		Attribute [] testAttributes = gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
		
		// compare serialized (and deserialized) declaration
		json = gson.toJson(attributes);
		testAttributes = gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests parsing JSON with empty attribute name 
	 */
	@Test
	@SuppressWarnings("unused")
	public void testEmptyName() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		String json1 = "{" +
				    "\"valueType\": \"enumeration\"," +
				    "\"domain\": [\"0\",\"1\",\"2\"]" +
				  "}";
		String json2 = "{" +
				    "\"name\": \"\"," +
				    "\"valueType\": \"integer\"" +
				  "}";
		String json3 = "{" +
				    "\"name\": \" \"," +
				    "\"valueType\": \"real\"" +
				  "}";
		String json4 = "{" +
				    "\"name\": \"	\"," +
				    "\"valueType\": \"real\"" +
				  "}";		
		
		try {
			Attribute testAttribute = gson.fromJson(json1, Attribute.class);
			fail("Parsing of JSON with definition of attribute with an empty name should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
		try {
			Attribute testAttribute = gson.fromJson(json2, Attribute.class);
			fail("Parsing of JSON with definition of attribute with an empty name should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
		try {
			Attribute testAttribute = gson.fromJson(json3, Attribute.class);
			fail("Parsing of JSON with definition of attribute with an empty name should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
		try {
			Attribute testAttribute = gson.fromJson(json4, Attribute.class);
			fail("Parsing of JSON with definition of attributes with an empty name should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Tests parsing JSON with empty attribute valueType 
	 */
	@Test
	@SuppressWarnings("unused")
	public void testEmptyValueType() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		String json = "{" +
					"\"name\": \"a1\"" +
				  "}";
		try {
			Attribute testAttribute = gson.fromJson(json, Attribute.class);
			fail("Parsing of JSON with definition of attribute with no valueType specified should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Tests parsing JSON with empty attribute valueType 
	 */
	@Test
	@SuppressWarnings("unused")
	public void testIncorrectValueType() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		String json1 = "{" +
					"\"name\": \"a1\"," +
					"\"valueType\": \"int\"" +
				  "}";
		String json2 = "{" +
				"\"name\": \"a2\"," +
				"\"valueType\": \"enumeration\"" +
			  "}";
		String json3 = "{" +
				"\"name\": \"a3\"," +
				"\"valueType\": [\"pair\"]" +
			  "}";
		String json4 = "{" +
				"\"name\": \"a5\"," +
				"\"valueType\": [\"pair\", \"enumeration\"]" +
			  "}";
		
		try {
			Attribute testAttribute = gson.fromJson(json1, Attribute.class);
			fail("Parsing of JSON with definition of attribute with no valueType specified should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
		try {
			Attribute testAttribute = gson.fromJson(json2, Attribute.class);
			fail("Parsing of JSON with definition of attribute of valueType enumeration without domain specified should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
		try {
			Attribute testAttribute = gson.fromJson(json3, Attribute.class);
			fail("Parsing of JSON with definition of attribute of valueType pair of no type specified should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
		try {
			Attribute testAttribute = gson.fromJson(json4, Attribute.class);
			fail("Parsing of JSON with definition of attribute of valueType pair, enumeration without domain specified should result in fail.");
		}
		catch (JsonParseException ex) {
			System.out.println(ex.toString());
		}
	}
}
