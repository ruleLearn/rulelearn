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

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.data.json.AttributeSerializer;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Test for {@link Attribute}
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class AttributeTest {

	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionI01() {
		Attribute [] attributes = new Attribute[3];
		
		//TODO Should be default value taken from a config class
		Attribute attribute = new Attribute("a1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
		attributes[0] = attribute;
		attribute = new Attribute("c1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new Attribute("g1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[2] = attribute;
			
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionR01() {
		Attribute [] attributes = new Attribute[3];
		
		//TODO Should be default value taken from a config class
		Attribute attribute = new Attribute("a2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.NONE), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
		attributes[0] = attribute;
		attribute = new Attribute("c2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.COST), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new Attribute("g2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.GAIN), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[2] = attribute;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionE01() {
		Attribute [] attributes = new Attribute[3];
		String [] labels = {"a", "b", "c"};
		
		//TODO Should be default value taken from a config class
		Attribute attribute;
		try {	
			attribute = new Attribute("a3", true, AttributeType.CONDITION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 0, AttributePreferenceType.NONE),
					new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
			attributes[0] = attribute;
			attribute = new Attribute("c3", true, AttributeType.CONDITION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 0, AttributePreferenceType.COST),
					new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
			attributes[1] = attribute;
			attribute = new Attribute("g3", true, AttributeType.CONDITION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 0, AttributePreferenceType.GAIN),
					new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
			attributes[2] = attribute;
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionP01() {
		Attribute [] attributes = new Attribute[3];
		//TODO Default values should be taken from a default configuration class
		try {
			String [] values = {"0", "1", "2"}; 
			ElementList domain = new ElementList(values);
			Attribute attribute = new Attribute("a4", true, AttributeType.CONDITION, 
					new PairField<EnumerationField>(EnumerationFieldFactory.getInstance().create(domain, 0, AttributePreferenceType.NONE), 
							EnumerationFieldFactory.getInstance().create(domain, 0, AttributePreferenceType.NONE)), 
					new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
			attributes[0] = attribute;
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		Attribute attribute = new Attribute("c4", true, AttributeType.CONDITION, 
				new PairField<IntegerField>(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST)), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new Attribute("g4", true, AttributeType.CONDITION, 
				new PairField<RealField>(RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.GAIN), RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.GAIN)), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[2] = attribute;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson(attributes);
		Attribute [] testAttributes= gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
	/**
	 * Tests construction and serialization/deserialization to/from JSON
	 */
	@Test
	public void testConstructionI02() {
		Attribute [] attributes = new Attribute[3];
		
		//TODO Should be default value taken from a config class
		Attribute attribute = new Attribute("a1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
		attributes[0] = attribute;
		attribute = new Attribute("c1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new Attribute("g1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), 
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
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
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
		
		//TODO Should be default value taken from a config class
		Attribute attribute = new Attribute("a2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.NONE), 
				new UnknownSimpleFieldMV15(), AttributePreferenceType.NONE);
		attributes[0] = attribute;
		attribute = new Attribute("c2", false, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.COST), 
				new UnknownSimpleFieldMV15(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new Attribute("g2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.GAIN), 
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
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
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
		
		//TODO Should be default value taken from a config class
		Attribute attribute;
		try {	
			attribute = new Attribute("a3", true, AttributeType.DESCRIPTION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 0, AttributePreferenceType.NONE),
					new UnknownSimpleFieldMV15(), AttributePreferenceType.NONE);
			attributes[0] = attribute;
			attribute = new Attribute("c3", true, AttributeType.CONDITION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 0, AttributePreferenceType.COST),
					new UnknownSimpleFieldMV15(), AttributePreferenceType.COST);
			attributes[1] = attribute;
			attribute = new Attribute("g3", true, AttributeType.DECISION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 0, AttributePreferenceType.GAIN),
					new UnknownSimpleFieldMV15(), AttributePreferenceType.GAIN);
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
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
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
		//TODO Default values should be taken from a default configuration class
		try {
			String [] values = {"0", "1", "2"}; 
			ElementList domain = new ElementList(values);
			Attribute attribute = new Attribute("a4", true, AttributeType.CONDITION, 
					new PairField<EnumerationField>(EnumerationFieldFactory.getInstance().create(domain, 0, AttributePreferenceType.NONE), 
							EnumerationFieldFactory.getInstance().create(domain, 0, AttributePreferenceType.NONE)), 
					new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
			attributes[0] = attribute;
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		Attribute attribute = new Attribute("c4", true, AttributeType.CONDITION, 
				new PairField<IntegerField>(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST), IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST)), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[1] = attribute;
		attribute = new Attribute("g4", false, AttributeType.CONDITION, 
				new PairField<RealField>(RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.GAIN), RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.GAIN)), 
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
				    "\"valueType\": [\"pair\",\"integer\"]," +
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
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		// compare result of deserialization to declaration 
		Attribute [] testAttributes = gson.fromJson(json, Attribute[].class);
		assertArrayEquals(attributes, testAttributes);
		
		// compare serialized (and deserialized) declaration
		json = gson.toJson(attributes);
		testAttributes = gson.fromJson(json, Attribute[].class);
		
		assertArrayEquals(attributes, testAttributes);
	}
	
}
