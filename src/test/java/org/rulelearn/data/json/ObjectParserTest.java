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

/**
 * Test for {@link ObjectParser}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class ObjectParserTest {

	/**
	 * Test method for {@link ObjectBuilder#getObjects(String)}.
	 */
	@Test
	void testConstructionOfInformationTableBuilder() {
		Attribute [] attributes = null;
		
		AttributeParser aParser = new AttributeParser();
		try {
			attributes = aParser.parseAttributes(new FileReader("src/test/resources/data/csv/prioritisation.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		if (attributes != null) {
			ObjectParser oParser = new ObjectParser(attributes);
			InformationTable iTable = null;
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

	}

}
