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

package org.rulelearn.data.csv;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTableBuilder;
import org.rulelearn.data.ObjectParseException;
import org.rulelearn.data.json.AttributeDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Testing speed of parsing different types of data files.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ParsingSpeedTest {
	
	void testObjectParsingCSVTime(String pathToJSONAttributeFile, String pathToCSVObjectFile, boolean header, char separator, String missingValueString) throws FileNotFoundException {
		Attribute[] attributes = null;
		List<String[]> objects = null;
		InformationTableBuilder informationTableBuilder = null;
		
		//load attributes
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try (JsonReader jsonReader = new JsonReader(new FileReader(pathToJSONAttributeFile))) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
			
			//construct information table builder
			if (attributes != null) {
				//load objects 
				org.rulelearn.data.csv.ObjectBuilder ob = new org.rulelearn.data.csv.ObjectBuilder.Builder().attributes(attributes)
						.header(header).separator(separator).missingValueString(missingValueString).build();
				objects = ob.getObjects(pathToCSVObjectFile); //can throw ObjectParseException
				
				informationTableBuilder = new InformationTableBuilder(attributes, new String[] {org.rulelearn.data.csv.ObjectBuilder.DEFAULT_MISSING_VALUE_STRING});
				
				long time = System.currentTimeMillis();
				if (objects != null) {
					for (int i = 0; i < objects.size(); i++) {
						try {
							informationTableBuilder.addObject(objects.get(i)); //uses volatile caches
						} catch (ObjectParseException exception) {
							throw new ObjectParseException(new StringBuilder("Error while parsing object no. ").append(i+1).append(" from CSV. ")
									.append(exception.toString()).toString()); //if exception was thrown, re-throw it
						}
					}
				}
				time = System.currentTimeMillis() - time;
				System.out.println(new StringBuffer("Time (ms) of parsing: ").append(pathToCSVObjectFile).append(": (").append(attributes.length)
						.append(", ").append(objects.size()).append(")").append(": ").append(time).toString());
			}
		}
		catch (IOException ex) {
			System.out.println(ex);
		}		
	}
	
	void testObjectParsingJSONTime(String pathToJSONAttributeFile, String pathToJSONObjectFile) throws FileNotFoundException {
		Attribute [] attributes = null;
		List<String[]> objects = null;
		InformationTableBuilder informationTableBuilder = null;
		
		// load attributes
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try (JsonReader jsonAttributesReader = new JsonReader(new FileReader(pathToJSONAttributeFile))) {
			attributes = gson.fromJson(jsonAttributesReader, Attribute[].class);
			// load objects
			JsonElement json = null;
			try (JsonReader jsonObjectsReader = new JsonReader(new FileReader(pathToJSONObjectFile))) {
				json = JsonParser.parseReader(jsonObjectsReader);
			}
			org.rulelearn.data.json.ObjectBuilder ob = new org.rulelearn.data.json.ObjectBuilder.Builder(attributes).build();
			objects = ob.getObjects(json);
			
			// construct information table builder
			if (attributes != null) {
				informationTableBuilder = new InformationTableBuilder(attributes, new String[] {org.rulelearn.data.json.ObjectBuilder.DEFAULT_MISSING_VALUE_STRING});
				long time = System.currentTimeMillis();
				if (objects != null) {
					for (int i = 0; i < objects.size(); i++) {
						try {
							informationTableBuilder.addObject(objects.get(i)); //uses volatile cache
						} catch (ObjectParseException exception) {
							throw new ObjectParseException(new StringBuilder("Error while parsing object no. ").append(i+1).append(" from JSON. ")
									.append(exception.toString()).toString()); //if exception was thrown, re-throw it
						}
					}
				}
				time = System.currentTimeMillis() - time;
				System.out.println(new StringBuffer("Time (ms) of parsing: ").append(pathToJSONObjectFile).append(": (").append(attributes.length)
						.append(", ").append(objects.size()).append(")").append(": ").append(time).toString());
			}
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
	@Test
	@Tag("integration")
	void testWindsorParsingTime() throws FileNotFoundException {
		testObjectParsingCSVTime("src/test/resources/data/csv/windsor.json", "src/test/resources/data/csv/windsor.csv", false, '\t', "?");
	}
		
	@Test
	@Tag("integration")
	void testBankParsingTime() throws FileNotFoundException {
		testObjectParsingCSVTime("src/test/resources/data/csv/bank2-10.json", "src/test/resources/data/csv/bank2-10-test-0.csv", false, ' ', "?");
	}
	
	@Test
	@Tag("integration")
	void testERATime() throws FileNotFoundException {
		testObjectParsingCSVTime("src/test/resources/data/csv/ERA-imposed.json", "src/test/resources/data/csv/ERA-imposed-train-0.csv", false, ' ', "?");
	}
	
	@Test
	@Tag("integration")
	void testPrioritisation0609Time() throws FileNotFoundException {
		testObjectParsingJSONTime("src/test/resources/data/json/metadata-prioritisation.json", "src/test/resources/data/json/learning-set-prioritisation-0609.json");
	}
	
	@Test
	@Tag("integration")
	void testPrioritisation2604Time() throws FileNotFoundException {
		testObjectParsingJSONTime("src/test/resources/data/json/metadata-prioritisation.json", "src/test/resources/data/json/learning-set-prioritisation-2604v1.json");
	}

	/**
	 * To perform this test please download "large.zip" file from the following location: <a href="https://drive.google.com/drive/folders/1aMpL164rxnY3Drwk7vfbUAsWgYPCjfgZ?usp=sharing">link</a>
	 * Please place unpacked files in test data folder: src/test/resources/data/large/csv/
	 *
	 * @throws FileNotFoundException when test data files are missing
	 */
	@Test
	@Tag("integration")
	void testPhones_accelerometerTime() throws FileNotFoundException {
		testObjectParsingCSVTime("src/test/resources/data/large/csv/Activity_real.json", "src/test/resources/data/large/csv/Phones_accelerometer_real.csv", true, ',', "?");
	}

	/**
	 * To perform this test please download "large.zip" file from the following location: <a href="https://drive.google.com/drive/folders/1aMpL164rxnY3Drwk7vfbUAsWgYPCjfgZ?usp=sharing">link</a>
	 * Please place unpacked files in test data folder: src/test/resources/data/large/csv/
	 *
	 * @throws FileNotFoundException when test data files are missing
	 */
	@Test
	@Tag("integration")
	void testPhones_gyroscopeTime() throws FileNotFoundException {
		testObjectParsingCSVTime("src/test/resources/data/large/csv/Activity_real.json", "src/test/resources/data/large/csv/Phones_gyroscope_real.csv", true, ',', "?");
	}
	
}