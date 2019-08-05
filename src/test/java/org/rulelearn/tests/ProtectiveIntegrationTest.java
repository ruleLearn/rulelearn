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

package org.rulelearn.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;

/**
 * ProtectiveIntegrationTest.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ProtectiveIntegrationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InformationTable informationTable = null;
		try {
			informationTable = InformationTableBuilder.safelyBuildFromJSONFile("src/test/resources/data/csv/prioritisation-no-rank.json", "src/test/resources/data/json/LearningSet_2604v1.json");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		
		if (informationTable != null) {
			System.out.println("Information table read from file.");
			System.out.println("# objects: "+informationTable.getNumberOfObjects());
		} else {
			System.out.println("Error reading information table from json file.");
		}

	}

}
