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

package org.rulelearn.rules.ruleml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.data.json.EvaluationAttributeSerializer;
import org.rulelearn.data.json.IdentificationAttributeSerializer;
import org.rulelearn.rules.RuleSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * Test for {@link RuleParser}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class RuleParserTest {

	/**
	 * Tests parsing RuleML file.
	 */
	@Test
	public void testLoading() {
		Attribute [] attributes = null;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(IdentificationAttribute.class, new IdentificationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		Gson gson = gsonBuilder.create();
		
		JsonReader jsonReader = null;
		try {
			jsonReader = new JsonReader(new FileReader("src/test/resources/data/csv/prioritisation.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		
		Map<Integer, RuleSet> rules = null;
		if (jsonReader != null) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
			RuleParser ruleParser = new RuleParser(attributes);
			try {
				rules = ruleParser.parseRules(new FileInputStream("src/test/resources/data/ruleml/prioritisation1.rules.xml"));
				assertEquals(rules.size(), 1);
				RuleSet firstRuleSet = rules.get(1);
				assertEquals(firstRuleSet.size(), 2);
			}
			catch (FileNotFoundException ex) {
				System.out.println(ex.toString());
			}
			if (rules != null) {
				
			}
			else {
				fail("Unable to load RuleML file.");
			}
		}
		else {
			fail("Unable to load JSON file with attributes.");
		}		
	}
	
}
