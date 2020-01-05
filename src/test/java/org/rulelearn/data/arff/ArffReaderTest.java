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

package org.rulelearn.data.arff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link ArffReader} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ArffReaderTest {

	/**
	 * Test method for {@link org.rulelearn.data.arff.ArffReader#read(String, AttributePreferenceType))}.
	 */
	@Test
	void testReadAttributes() {
		EvaluationAttribute attribute;
		EnumerationField enumerationField;
		String[] elements;
		String[] expectedElements;
		ArffReader arffReader = new ArffReader();
		
		try {
			//test for exemplary arff file, with lot of nasty formatting
			InformationTable informationTable = arffReader.read("src/test/resources/data/arff/eucalyptus-10-objects.arff", AttributePreferenceType.GAIN);
			
			Attribute[] attributes = informationTable.getAttributes();
			assertEquals(attributes.length, 20);
			
			assertTrue(attributes[0] instanceof EvaluationAttribute);
			attribute = (EvaluationAttribute)attributes[0];
			assertTrue(attribute.isActive());
			assertEquals(attribute.getName(), "Abbrev");
			assertEquals(attribute.getMissingValueType(), UnknownSimpleFieldMV2.getInstance());
			assertEquals(attribute.getPreferenceType(), AttributePreferenceType.NONE);
			assertEquals(attribute.getType(), AttributeType.CONDITION);
			assertTrue(attribute.getValueType() instanceof EnumerationField);
			enumerationField = (EnumerationField)attribute.getValueType();
			elements = enumerationField.getElementList().getElements();
			expectedElements = new String[]{"Cra","Cly 2","Nga","Wai","K81","Wak","K82","WSp","K83","Lon","Puk","Paw","K81a","Mor","Wen","WSh"};
			assertEquals(elements.length, expectedElements.length);
			for (int i = 0; i < elements.length; i++) {
				assertEquals(elements[i], expectedElements[i]);
			}
			
			assertTrue(attributes[1] instanceof EvaluationAttribute);
			attribute = (EvaluationAttribute)attributes[1];
			assertTrue(attribute.isActive());
			assertEquals(attribute.getName(), "Rep");
			assertEquals(attribute.getMissingValueType(), UnknownSimpleFieldMV2.getInstance());
			assertEquals(attribute.getPreferenceType(), AttributePreferenceType.NONE);
			assertEquals(attribute.getType(), AttributeType.CONDITION);
			assertTrue(attribute.getValueType() instanceof RealField);
			
			assertTrue(attributes[2] instanceof EvaluationAttribute);
			attribute = (EvaluationAttribute)attributes[2];
			assertTrue(attribute.isActive());
			assertEquals(attribute.getName(), "Locality");
			assertEquals(attribute.getMissingValueType(), UnknownSimpleFieldMV2.getInstance());
			assertEquals(attribute.getPreferenceType(), AttributePreferenceType.NONE);
			assertEquals(attribute.getType(), AttributeType.CONDITION);
			assertTrue(attribute.getValueType() instanceof EnumerationField);
			enumerationField = (EnumerationField)attribute.getValueType();
			elements = enumerationField.getElementList().getElements();
			expectedElements = new String[]{"Central_Hawkes_Bay","Northern_Hawkes_Bay","Southern_Hawkes_Bay",
					"Central_Hawkes_Bay_(coastal)","Central Wairarapa","South_Wairarapa","Southern Hawkes Bay (coastal)","Central_Poverty_Bay"};
			assertEquals(elements.length, expectedElements.length);
			for (int i = 0; i < elements.length; i++) {
				assertEquals(elements[i], expectedElements[i]);
			}
			
			//...
			
			assertTrue(attributes[18] instanceof EvaluationAttribute);
			attribute = (EvaluationAttribute)attributes[18];
			assertTrue(attribute.isActive());
			assertEquals(attribute.getName(), "Brnch_Fm");
			assertEquals(attribute.getMissingValueType(), UnknownSimpleFieldMV2.getInstance());
			assertEquals(attribute.getPreferenceType(), AttributePreferenceType.NONE);
			assertEquals(attribute.getType(), AttributeType.CONDITION);
			assertTrue(attribute.getValueType() instanceof RealField);
			
			assertTrue(attributes[19] instanceof EvaluationAttribute);
			attribute = (EvaluationAttribute)attributes[19];
			assertTrue(attribute.isActive());
			assertEquals(attribute.getName(), "Utility");
			assertEquals(attribute.getMissingValueType(), UnknownSimpleFieldMV2.getInstance());
			assertEquals(attribute.getPreferenceType(), AttributePreferenceType.GAIN); //as requested upon invocation
			assertEquals(attribute.getType(), AttributeType.DECISION);
			assertTrue(attribute.getValueType() instanceof EnumerationField);
			enumerationField = (EnumerationField)attribute.getValueType();
			elements = enumerationField.getElementList().getElements();
			expectedElements = new String[]{"none","low","average","good","best"};
			assertEquals(elements.length, expectedElements.length);
			for (int i = 0; i < elements.length; i++) {
				assertEquals(elements[i], expectedElements[i]);
			}
			
			//assert data correctness
			assertEquals(informationTable.getNumberOfObjects(), 10);
			
			EvaluationField[] evaluationFields;
			String[] expectedStrEvaluations;
			
			evaluationFields = informationTable.getActiveConditionAttributeFields().getFields(0, true); //first row
			expectedStrEvaluations = new String[] {"K82","3.0","Central Wairarapa","N158_343/625","40__57","180.0","1080.0",
					"-3.0","1982.0","re","2574.0","15.26","14.08","25.0","3.0","4.0","4.0","4.0","3.0"};
			
			assertEquals(evaluationFields.length, expectedStrEvaluations.length);
			for (int i = 0; i < evaluationFields.length; i++) {
				assertEquals(evaluationFields[i].toString(), expectedStrEvaluations[i]);
			}
			
			//...
			
			evaluationFields = informationTable.getActiveConditionAttributeFields().getFields(9, true); //last row
			expectedStrEvaluations = new String[] {"Paw","2.0","Southern Hawkes Bay (coastal)","N146_273/737","40__00","150.0","1300.0",
					"-2.0","1983.0","ni","2547.0","46.36","19.09","40.0","4.3","2.6","3.0","2.9","2.0"};
			
			assertEquals(evaluationFields.length, expectedStrEvaluations.length);
			for (int i = 0; i < evaluationFields.length; i++) {
				assertEquals(evaluationFields[i].toString(), expectedStrEvaluations[i]);
			}
		} catch (FileNotFoundException exception) {
			fail("Should read arff file.");
		}
	}

}
