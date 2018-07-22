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

package org.rulelearn.classification;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.data.json.EvaluationAttributeSerializer;
import org.rulelearn.data.json.IdentificationAttributeSerializer;
import org.rulelearn.data.json.ObjectBuilder;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.ruleml.RuleParser;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Tests for {@link SimpleRuleClassifier}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class SimpleRuleClassifierTest {

	/**
	 * Tests parsing RuleML file and using rules to classify objects loaded from JSON.
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
			try {
				jsonReader.close();
				jsonReader = null;
			}
			catch (IOException ex){
				System.out.println(ex.toString());
			}
			
			RuleParser ruleParser = new RuleParser(attributes);
			try {
				rules = ruleParser.parseRules(new FileInputStream("src/test/resources/data/ruleml/prioritisation2.rules.xml"));
			}
			catch (FileNotFoundException ex) {
				System.out.println(ex.toString());
			}
			if ((rules != null) & (rules.size() > 0)) {
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
				try {
					jsonReader.close();
				}
				catch (IOException ex){
					System.out.println(ex.toString());
				}
				
				ObjectBuilder ob = new ObjectBuilder(attributes);
				List<String []> objects = null;
				objects = ob.getObjects(json);
				
				InformationTableBuilder iTB = new InformationTableBuilder(attributes, ",", new String[] {"?"});
				for (int i = 0; i < objects.size(); i++) {
					iTB.addObject(objects.get(i));
				}
				InformationTable iT = iTB.build();
				
				SimpleRuleClassifier classifier = new SimpleRuleClassifier(rules.get(1), new SimpleClassificationResult(
						EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 2, 
																AttributePreferenceType.COST)));
				assertEquals(classifier.classify(0, iT).getSuggestedDecision(), EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 2, 
						AttributePreferenceType.COST));
				assertEquals(classifier.classify(1, iT).getSuggestedDecision(), EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 2, 
						AttributePreferenceType.COST));
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
