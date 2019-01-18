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

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;

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
	 * Test method for {@link ObjectBuilder#ObjectBuilder(Attribute[])}.
	 */
	@Test
	void testConstructionOfObjectBuilder01() {
		Attribute[] attributes = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes);});
	}
	
	/**
	 * Test method for {@link ObjectBuilder#ObjectBuilder(Attribute[], String)}.
	 */
	@Test
	void testConstructionOfObjectBuilder04() {
		Attribute[] attributes = null;
		String encoding = null;
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, encoding);});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(attributes, "");});
		assertThrows(NullPointerException.class, () -> {new ObjectBuilder(new Attribute[0], encoding);});
	}

	/**
	 * Test method for {@link ObjectBuilder#getObjects(String)}.
	 */
	@Test
	void testGetObjects() {
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
		
		JsonElement json = null;
		jsonReader = null;
		try {
			jsonReader = new JsonReader(new FileReader("src/test/resources/data/json/prioritisation1.json"));
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
		
		ObjectBuilder ob = new ObjectBuilder(attributes);
		List<String []> objects = null;
		objects = ob.getObjects(json);
	
		assertTrue(objects != null);
		assertEquals(objects.size(), 2);
	}

}
