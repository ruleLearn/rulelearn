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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
 * Integration tests for reading information table from files (attributes + data) representing ordinal benchmark data sets, calculating unions of decision classes, and calculating their approximations.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
@Tag("integration")
class ApproximationsTest {

	/**
	 * Test calculation of lower approximations of unions in {@code windsor} data set using {@link ClassicalDominanceBasedRoughSetCalculator}.
	 */
	@Test
	void testWindsorWithClassicalDominanceBasedRoughSetCalculator() {
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).header(false).separator('\t').build();
				InformationTable informationTable = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/windsor.csv")) {
					informationTable = objectParser.parseObjects(objectsReader);
					if (informationTable != null) {
						assertEquals(546, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
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
		try (FileReader attributesReader = new FileReader("src/test/resources/data/csv/windsor.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributesReader);
			if (attributes != null) {
				ObjectParser objectParser = new ObjectParser.Builder(attributes).header(false).separator('\t').build();
				InformationTable informationTable = null;
				try (FileReader objectsReader = new FileReader("src/test/resources/data/csv/windsor.csv")) {
					informationTable = objectParser.parseObjects(objectsReader);
					if (informationTable != null) {
						assertEquals(546, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
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
	
	/**
	 * Test calculation of lower, and upper approximations of unions in @code a learning data set for the prioritisation problem using {@link ClassicalDominanceBasedRoughSetCalculator}.
	 */
	@Test
	void testLearningSetPriotitisationWithClassicalDominanceBasedRoughSetCalculator() {
		try (FileReader attributeReader = new FileReader("src/test/resources/data/json/metadata-prioritisation.json")) {
			Attribute [] attributes = null;
			AttributeParser attributeParser = new AttributeParser();
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				org.rulelearn.data.json.ObjectParser objectParser = new org.rulelearn.data.json.ObjectParser.Builder(attributes).build();
				InformationTable informationTable = null;
				try (FileReader objectReader = new FileReader("src/test/resources/data/json/learning-set-prioritisation-2019-02-27.json")) {
					informationTable = objectParser.parseObjects(objectReader);
					if (informationTable != null) {
						assertEquals(50, informationTable.getNumberOfObjects());
						assertTrue(informationTable.getDecisions() != null);
						// calculate unions
						Unions unions = new Unions(new InformationTableWithDecisionDistributions(informationTable), new ClassicalDominanceBasedRoughSetCalculator());
						Union[] downwardUnions = unions.getDownwardUnions(true);
						Union[] upwardUnions = unions.getUpwardUnions(true);
						// print them out
						for (Union union : downwardUnions) {
							//System.out.println("at least "+union.getLimitingDecision()+" -> "+union.getAccuracyOfApproximation());
							assertEquals(1.0, union.getAccuracyOfApproximation());
						}
						for (Union union : upwardUnions) {
							//System.out.println("at least "+union.getLimitingDecision()+" -> "+union.getAccuracyOfApproximation());
							assertEquals(1.0, union.getAccuracyOfApproximation());
						}
					}
					else {
						fail("Unable to load JSON test file with definition of objects");
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
