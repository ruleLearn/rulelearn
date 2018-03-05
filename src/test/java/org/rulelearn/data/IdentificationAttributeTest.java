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

import org.junit.jupiter.api.Test;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.data.json.IdentificationAttributeSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Test for {@link IdentificationAttribute}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class IdentificationAttributeTest {
	
	private Attribute [] attributes = null;

	/**
	 * Set attributes for tests
	 */
	private void set01() {
		attributes = new Attribute[2];
		attributes[0] = new IdentificationAttribute("i1", true, IdentificationValueType.TEXT);
		attributes[1] = new IdentificationAttribute("i2", true, IdentificationValueType.UUID);
	}
	
	/**
	 * Tests equals and hashCode
	 */
	@Test
	public void testEquals01() {
		set01();
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
	public void testConstruction01() {
		set01();
			
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(IdentificationAttribute.class, new IdentificationAttributeSerializer());
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
	public void testConstruction02() {
		set01();
	
		String json = "[" + 
				 "{" +
				    "\"name\": \"i1\"," +
				    "\"active\": true," +
				    "\"identifierType\": \"text\"" +
				  "}," +
				  "{" +
				    "\"name\": \"i2\"," +
				    "\"active\": true," +
				    "\"identifierType\": \"uuid\"" +
				  "}" +
			"]"; 
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(IdentificationAttribute.class, new IdentificationAttributeSerializer());
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
