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

package org.rulelearn.approximations;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.csv.ObjectParser;
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.measures.dominance.EpsilonConsistencyMeasure;

/**
 * Integration tests consisting in calculation of lower approximations of unions in ordinal benchmark data sets.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
@Tag("integration")
class LowerApproximationsTest {

	/**
	 * Test calculation of lower approximations of unions in {@code windsor} data set using {@link ClassicalDominanceBasedRoughSetCalculator}.
	 */
	@Test
	void testWindsorWithClassicalDominanceBasedRoughSetCalculator() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).header(false).separator('\t').build();
				InformationTable informationTable = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/windsor.csv")) {
					informationTable = objectParser.parseObjects(objectsReader);
					if (informationTable != null) {
						assertEquals(546, informationTable.getNumberOfObjects());
						// calculate unions
						Unions unions = new Unions(new InformationTableWithDecisionDistributions(informationTable), new ClassicalDominanceBasedRoughSetCalculator());
						Union[] downwardUnions = unions.getDownwardUnions(true);
						Union[] upwardUnions = unions.getUpwardUnions(true);
						// test downward unions size and lower approximations size 
						assertEquals(137, downwardUnions[0].size());
 						assertEquals(46, downwardUnions[0].getLowerApproximation().size());
 						assertEquals(275, downwardUnions[1].size());
 						assertEquals(179, downwardUnions[1].getLowerApproximation().size());
 						assertEquals(411, downwardUnions[2].size());
 						assertEquals(337, downwardUnions[2].getLowerApproximation().size());
 						// test upward unions size and lower approximations size 
						assertEquals(135, upwardUnions[0].size());
 						assertEquals(90, upwardUnions[0].getLowerApproximation().size());
 						assertEquals(271, upwardUnions[1].size());
 						assertEquals(200, upwardUnions[1].getLowerApproximation().size());
 						assertEquals(409, upwardUnions[2].size());
 						assertEquals(294, upwardUnions[2].getLowerApproximation().size());
					}
					else {
						fail("Unable to load CSV test file with definition of objects");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test calculation of extended (variable consistency) lower approximations of unions in {@code windsor} data set using {@link VCDominanceBasedRoughSetCalculator} and {@link EpsilonConsistencyMeasure}.
	 */
	@Test
	void testWindsorWithVCDominanceBasedRoughSetCalculator() {
		Attribute [] attributes = null;
		
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).header(false).separator('\t').build();
				InformationTable informationTable = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/windsor.csv")) {
					informationTable = objectParser.parseObjects(objectsReader);
					if (informationTable != null) {
						assertEquals(546, informationTable.getNumberOfObjects());
						// calculate unions
						Unions unions = new Unions(new InformationTableWithDecisionDistributions(informationTable), new VCDominanceBasedRoughSetCalculator(EpsilonConsistencyMeasure.getInstance(), 0.1));
						Union[] downwardUnions = unions.getDownwardUnions(true);
						Union[] upwardUnions = unions.getUpwardUnions(true);
						// test downward unions size and lower approximations size 
						assertEquals(137, downwardUnions[0].size());
 						assertEquals(134, downwardUnions[0].getLowerApproximation().size());
 						assertEquals(275, downwardUnions[1].size());
 						assertEquals(275, downwardUnions[1].getLowerApproximation().size());
 						assertEquals(411, downwardUnions[2].size());
 						assertEquals(409, downwardUnions[2].getLowerApproximation().size());
 						// test upward unions size and lower approximations size 
						assertEquals(135, upwardUnions[0].size());
 						assertEquals(135, upwardUnions[0].getLowerApproximation().size());
 						assertEquals(271, upwardUnions[1].size());
 						assertEquals(268, upwardUnions[1].getLowerApproximation().size());
 						assertEquals(409, upwardUnions[2].size());
 						assertEquals(391, upwardUnions[2].getLowerApproximation().size());
					}
					else {
						fail("Unable to load CSV test file with definition of objects");
					}
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}
			else {
				fail("Unable to load JSON test file with definition of attributes");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
}
