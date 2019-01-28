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

package org.rulelearn.data.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Test for {@link ObjectBuilder}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectBuilderTest {
	
	/**
	 * Test method for {@link ObjectBuilder.Builder#Builder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectBuilder01() {
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(null).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(attributes).build();});
	}
	
	/**
	 * Test method for Test method for {@link ObjectBuilder.Builder#Builder(Attribute[])} and Test method for {@link ObjectBuilder.Builder#encoding(String)}..
	 */
	@Test
	void testConstructionOfObjectBuilder04() {
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(null).encoding(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).build();});
		Attribute[] attributes = new Attribute[1];
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(attributes).encoding(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(attributes).encoding(ObjectBuilder.DEFAULT_ENCODING).build();});
		attributes[0] = new EvaluationAttribute("a", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(attributes).encoding(null).build();});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder.Builder(null).encoding(ObjectBuilder.DEFAULT_ENCODING).build();});
	}

	/**
	 * Test method for {@link ObjectBuilder#getObjects(JsonElement)}.
	 */
	@Test
	void testGetObjects() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/prioritisation.json"); JsonReader jsonAttributeReader = new JsonReader(attributeReader)) {
			Attribute [] attributes = gson.fromJson(jsonAttributeReader, Attribute[].class);
			if (attributes != null) {
				JsonElement json = null;
				try (FileReader objectReader = new FileReader("src/test/resources/data/json/prioritisation1.json"); JsonReader jsonObjectReader = new JsonReader(objectReader)){
					JsonParser jsonParser = new JsonParser();
					json = jsonParser.parse(jsonObjectReader);
						
					ObjectBuilder objectBuilder = new ObjectBuilder.Builder(attributes).build();
					List<String []> objects = null;
					objects = objectBuilder.getObjects(json);
					
					assertTrue(objects != null);
					assertEquals(objects.size(), 2);
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}

}
