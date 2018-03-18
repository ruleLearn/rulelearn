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

import org.junit.jupiter.api.Test;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.data.json.EvaluationAttributeSerializer;
import org.rulelearn.data.json.IdentificationAttributeSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * Test for {@link IdentificationAttribute} and {@link EvaluationAttribute}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class AttributeTest {

	/**
	 * Tests loading JSON file and serialization/deserialization to/from JSON
	 */
	@Test
	public void testLoading() {
		Attribute [] attributes = null;
		Attribute [] testAttributes = null;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(IdentificationAttribute.class, new IdentificationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		JsonReader jsonReader = null;
		try {
			jsonReader = new JsonReader(new FileReader("src/test/resources/data/csv/prioritisation.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		
		// compare result of loading to serialization/deserialization
		if (jsonReader != null) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
			String json = gson.toJson(attributes);
			// System.out.println(json);
			testAttributes = gson.fromJson(json, Attribute[].class);
			
			assertArrayEquals(attributes, testAttributes);
		}
		else
			fail("Unable to load JSON test file");
	}

}
