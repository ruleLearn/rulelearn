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

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Test for {@link InformationTableSerializer}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class InformationTableSerializerTest {

	/**
	 * Test method for {@link InformationTableSerializer}.
	 */
	@Test
	void testInformationTableSerializer() {
		Attribute [] attributes = null;
		InformationTable iTable = null;
		
		AttributeParser aParser = new AttributeParser();
		try {
			attributes = aParser.parseAttributes(new FileReader("src/test/resources/data/csv/prioritisation.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		if (attributes != null) {
			ObjectParser oParser = new ObjectParser(attributes);
			try {
				iTable = oParser.parseObjects(new FileReader("src/test/resources/data/json/examples.json"));
			}
			catch (FileNotFoundException ex) {
				System.out.println(ex.toString());
			}
			if (iTable != null) {
				assertEquals(iTable.getNumberOfObjects(), 2);
			}
			else {
				fail("Unable to load JSON test file with definition of objects");
			}
		}
		else {
			fail("Unable to load JSON test file with definition of attributes");
		}
		
		String serializediTable = "[\n" + 
				"  {\n" + 
				"    \"ID\": \"b70eac70-b4ac-11e7-9460-002564d9514f\",\n" + 
				"    \"SourceAssetCriticality\": \"low\",\n" + 
				"    \"TargetAssetCriticality\": \"critical\",\n" + 
				"    \"TimeToDueDate\": \"-100896.193\",\n" + 
				"    \"TimeFromDetectTime\": \"187296.193\",\n" + 
				"    \"SeverityForAttackCategory\": \"critical\",\n" + 
				"    \"MAQuality\": \"0.7\",\n" + 
				"    \"AttackSourceReputationScore\": \"0.5\",\n" + 
				"    \"MaxCVE\": \"0.0\"\n" + 
				"  },\n" + 
				"  {\n" + 
				"    \"ID\": \"b70eac70-b4ac-11e7-9460-002564d9514e\",\n" + 
				"    \"SourceAssetCriticality\": \"NA\",\n" + 
				"    \"TargetAssetCriticality\": \"critical\",\n" + 
				"    \"TimeToDueDate\": \"-1.0641696193E7\",\n" + 
				"    \"TimeFromDetectTime\": \"1.0728096193E7\",\n" + 
				"    \"SeverityForAttackCategory\": \"low\",\n" + 
				"    \"MAQuality\": \"0.7\",\n" + 
				"    \"AttackSourceReputationScore\": \"0.5\",\n" + 
				"    \"MaxCVE\": \"1.0\"\n" + 
				"  }\n" + 
				"]";
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(InformationTable.class, new InformationTableSerializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		assertEquals(serializediTable, gson.toJson(iTable).toString());
	}

}
