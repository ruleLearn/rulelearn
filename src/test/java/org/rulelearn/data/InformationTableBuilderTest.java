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
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;


/**
 * InformationTableBuilderTest
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class InformationTableBuilderTest {
	
	private String jsonAttributes1 = "[" + 
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
			    "\"valueType\": \"real\"," +
			    "\"missingValueType\": \"mv2\"" +
			  "}," +
			  "{" +
			    "\"name\": \"d1\"," +
			    "\"active\": true," +
			    "\"preferenceType\": \"gain\"," +
			    "\"type\": \"decision\"," +
			    "\"valueType\": \"enumeration\"," +
			    "\"domain\": [\"a\",\"b\",\"c\"]," +
			    "\"missingValueType\": \"mv2\"" +
			  "}" +
		"]";
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#setAttributesFromJSON(java.lang.String)}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder() {		 
		InformationTableBuilder iTB = new InformationTableBuilder(jsonAttributes1);
		assertTrue(iTB != null);
	}

	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAddObject01() {
		InformationTableBuilder iTB = new InformationTableBuilder(jsonAttributes1, ",");
		iTB.addObject("1, 1.0, a");
		iTB.addObject("2, 2.0, b");
		InformationTable iT = iTB.build();
		assertTrue(iT.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(0, 1).isEqualTo(RealFieldFactory.getInstance().create(1.0, AttributePreferenceType.COST)) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(1, 2).isEqualTo(EnumerationFieldFactory.getInstance().create(((EnumerationField)iT.getAttributes()[2].getValueType()).getElementList(), 
												1, AttributePreferenceType.GAIN)) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.InformationTableBuilder#addObject(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testAddObject02() {
		InformationTableBuilder iTB = new InformationTableBuilder(jsonAttributes1, ",", new String[] {"?", "*"})  ;
		iTB.addObject("1, ?, a");
		iTB.addObject("2, 2.0, *");
		InformationTable iT = iTB.build();
		assertTrue(iT.getField(0, 0).isEqualTo(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE)) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(0, 1).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
		assertTrue(iT.getField(1, 2).isEqualTo(new UnknownSimpleFieldMV2()) == TernaryLogicValue.TRUE);
	}

}
