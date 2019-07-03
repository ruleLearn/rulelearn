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

package org.rulelearn.sampling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;
import org.rulelearn.data.InformationTableTestConfiguration;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link DeterministicSplitter}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class DeterministicSplitterTest {
	
	private InformationTableTestConfiguration informationTableTestConfiguration;

	@BeforeEach
	void setUp() {
		AttributePreferenceType[] attributePreferenceTypes = {
				AttributePreferenceType.GAIN,  AttributePreferenceType.GAIN}; //supplementary variable
		informationTableTestConfiguration = new InformationTableTestConfiguration (
				new Attribute[] {
						new EvaluationAttribute("a1", true, AttributeType.CONDITION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes[0]), new UnknownSimpleFieldMV2(), attributePreferenceTypes[0]),
						new EvaluationAttribute("d", true, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, attributePreferenceTypes[1]), new UnknownSimpleFieldMV2(), attributePreferenceTypes[1])
					},
				new String[][] {
						{ "1", "1"},
						{ "2", "0"},
						{ "3", "1"},
						{ "4", "1"},
						{ "5", "1"},
						{ "6", "1"},
						{ "7", "0"},
						{ "8", "0"},
						{ "9", "0"},
						{"10", "0"},
						{"11", "1"},
						{"12", "2"},
						{"13", "1"},
						{"14", "1"},
						{"15", "1"},
						{"16", "1"},
						{"17", "2"},
						{"18", "2"},
						{"19", "2"},
						{"20", "2"},
						{"21", "2"},
						{"22", "0"},
						{"23", "2"},
						{"24", "2"},
						{"25", "2"},
						{"26", "2"},
						{"27", "0"},
						{"28", "0"},
						{"29", "0"},
						{"30", "0"}
				});
	}
	
	/**
	 * Tests for {@link DeterministicSplitter#split(InformationTable, double...)}.
	 */
	@Test
	void testSplit00() {
		InformationTable informationTable = informationTableTestConfiguration.getInformationTable(true);
		DeterministicSplitter splitter = new DeterministicSplitter();
		assertThrows(NullPointerException.class, () -> {splitter.split(null);});
		assertThrows(NullPointerException.class, () -> {splitter.split(null, null);});
		assertThrows(NullPointerException.class, () -> {splitter.split(null, new double [] {});});
		assertThrows(NullPointerException.class, () -> {splitter.split(informationTable, null);});
		assertThrows(IllegalArgumentException.class, () -> {splitter.split(informationTable, new double [] {});});
		assertThrows(IllegalArgumentException.class, () -> {splitter.split(informationTable, new double [] {0.1});});
		assertThrows(IllegalArgumentException.class, () -> {splitter.split(informationTable, 0.1);});
		assertThrows(IllegalArgumentException.class, () -> {splitter.split(informationTable, new double [] {0.1, 1.0});});
		assertThrows(IllegalArgumentException.class, () -> {splitter.split(informationTable, 0.1, 1.0);});
		assertThrows(InvalidValueException.class, () -> {splitter.split(informationTable, new double [] {-0.1, 0.01});});
		assertThrows(InvalidValueException.class, () -> {splitter.split(informationTable, -0.1, 0.01);});
	}

	/**
	 * Tests for {@link DeterministicSplitter#split(InformationTable, double...)}.
	 */
	@Test
	void testSplit01() {
		InformationTable informationTable = informationTableTestConfiguration.getInformationTable(true);
		DeterministicSplitter splitter = new DeterministicSplitter();
		List<InformationTable> informationTables = splitter.split(informationTable, 0.5, 0.5);
		assertEquals(15, informationTables.get(0).getNumberOfObjects());
		assertEquals(15, informationTables.get(1).getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(1)));
		informationTables = splitter.split(informationTable, 1.0/3, 0.5);
		assertEquals(10, informationTables.get(0).getNumberOfObjects());
		assertEquals(15, informationTables.get(1).getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(1)));
		informationTables = splitter.split(informationTable, 1.0/3, 1.0/3, 1.0/3);
		assertEquals(10, informationTables.get(0).getNumberOfObjects());
		assertEquals(10, informationTables.get(1).getNumberOfObjects());
		assertEquals(10, informationTables.get(2).getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(1)));
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(2)));
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(1), informationTables.get(2)));
	}
	
	/**
	 * Tests for {@link DeterministicSplitter#split(InformationTable, double...)}.
	 */
	@Test
	void testSplit02() {
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(informationTableTestConfiguration.getInformationTable(true));
		DeterministicSplitter splitter = new DeterministicSplitter();
		assertThrows(NullPointerException.class, () -> {splitter.splitStratified(null);});
		assertThrows(NullPointerException.class, () -> {splitter.splitStratified(null, null);});
		assertThrows(NullPointerException.class, () -> {splitter.splitStratified(null, new double [] {});});
		assertThrows(NullPointerException.class, () -> {splitter.splitStratified(informationTable, null);});
		assertThrows(IllegalArgumentException.class, () -> {splitter.splitStratified(informationTable, new double [] {});});
		assertThrows(IllegalArgumentException.class, () -> {splitter.splitStratified(informationTable, new double [] {0.1});});
		assertThrows(IllegalArgumentException.class, () -> {splitter.splitStratified(informationTable, 0.1);});
		assertThrows(IllegalArgumentException.class, () -> {splitter.splitStratified(informationTable, new double [] {0.1, 1.0});});
		assertThrows(IllegalArgumentException.class, () -> {splitter.splitStratified(informationTable, 0.1, 1.0);});
		assertThrows(InvalidValueException.class, () -> {splitter.splitStratified(informationTable, new double [] {-0.1, 0.01});});
		assertThrows(InvalidValueException.class, () -> {splitter.splitStratified(informationTable, -0.1, 0.01);});
	}
	
	/**
	 * Tests for {@link DeterministicSplitter#splitStratified(InformationTableWithDecisionDistributions, double...)}.
	 */
	@Test
	void testSplit03() {
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(informationTableTestConfiguration.getInformationTable(true));
		DeterministicSplitter splitter = new DeterministicSplitter();
		// check distribution of decisions in the information table
		for (Decision decision : informationTable.getDecisions()) {
			assertEquals(1.0/3, ((double)informationTable.getDecisionDistribution().getCount(decision))/informationTable.getNumberOfObjects());
		}
		// perform splitting
		List<InformationTable> informationTables = splitter.splitStratified(informationTable, new double [] {0.5, 0.5});
		assertEquals(15, informationTables.get(0).getNumberOfObjects());
		assertEquals(15, informationTables.get(1).getNumberOfObjects());
		// check distribution of decisions in information sub-tables
		for (InformationTable table : informationTables) {
			InformationTableWithDecisionDistributions informationSubTable = new InformationTableWithDecisionDistributions(table);
			for (Decision decision : informationTable.getDecisions()) {
				assertEquals(1.0/3, ((double)informationSubTable.getDecisionDistribution().getCount(decision))/informationSubTable.getNumberOfObjects());
			}
		}
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(1)));
		
		informationTables = splitter.splitStratified(informationTable, new double [] {1.0/3, 0.5});
		assertEquals(9, informationTables.get(0).getNumberOfObjects());
		assertEquals(15, informationTables.get(1).getNumberOfObjects());
		// check distribution of decisions in information sub-tables
		for (InformationTable table : informationTables) {
			InformationTableWithDecisionDistributions informationSubTable = new InformationTableWithDecisionDistributions(table);
			for (Decision decision : informationTable.getDecisions()) {
				assertEquals(1.0/3, ((double)informationSubTable.getDecisionDistribution().getCount(decision))/informationSubTable.getNumberOfObjects());
			}
		}
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(1)));
		
		informationTables = splitter.splitStratified(informationTable, new double [] {1.0/3, 1.0/3, 1.0/3});
		assertEquals(9, informationTables.get(0).getNumberOfObjects());
		assertEquals(9, informationTables.get(1).getNumberOfObjects());
		assertEquals(9, informationTables.get(2).getNumberOfObjects());
		// check distribution of decisions in information sub-tables
		for (InformationTable table : informationTables) {
			InformationTableWithDecisionDistributions informationSubTable = new InformationTableWithDecisionDistributions(table);
			for (Decision decision : informationTable.getDecisions()) {
				assertEquals(1.0/3, ((double)informationSubTable.getDecisionDistribution().getCount(decision))/informationSubTable.getNumberOfObjects());
			}
		}
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(1)));
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(0), informationTables.get(2)));
		assertTrue(noEqualValuesOnFirstAttribute(informationTables.get(1), informationTables.get(2)));
	}
	
	/**
	 * Tests for repetitions of {@link DeterministicSplitter#splitStratified(InformationTableWithDecisionDistributions, double...)}.
	 */
	@Test
	void testSplit12() {
		for (int i = 0; i < 10; i++) {
			InformationTableWithDecisionDistributions informationTable = null;
			DeterministicSplitter splitter = new DeterministicSplitter();
			try {
				informationTable = new InformationTableWithDecisionDistributions(InformationTableBuilder.buildFromJSONFile("src/test/resources/data/json/metadata-prioritisation.json", "src/test/resources/data/json/data-example4.json"));
			}
			catch (FileNotFoundException ex) {
				System.out.println(ex);
			}
			catch (IOException ex) {
				System.out.println(ex);
			}
			List<InformationTable> informationTables1, informationTables2;
			informationTables1 = splitter.splitStratified(informationTable, 1.0/3, 0.5);
			informationTables2 = splitter.splitStratified(informationTable, true, 1.0/3, 0.5);
			/* debug
			System.out.println("-first repeated-");
			for (int j = 0; j < informationTables1.get(0).getNumberOfObjects(); j++) {
				System.out.println(informationTables1.get(0).getIdentifier(j).toString());
			}
			System.out.println("-second repeated-");
			for (int j = 0; j < informationTables2.get(0).getNumberOfObjects(); j++) {
				System.out.println(informationTables2.get(0).getIdentifier(j).toString());
			}*/
			assertTrue(allEqualValuesOnFirstAttribute(informationTables1.get(0), informationTables2.get(0)));
			assertTrue(allEqualValuesOnFirstAttribute(informationTables1.get(1), informationTables2.get(1)));
		}
	}
	
	/**
	 * Tests for repetitions of {@link DeterministicSplitter#splitStratified(InformationTableWithDecisionDistributions, double...)}.
	 */
	@Test
	void testSplit13() {
		InformationTableWithDecisionDistributions informationTable = null;
		DeterministicSplitter splitter = new DeterministicSplitter();
		try {
			informationTable = new InformationTableWithDecisionDistributions(InformationTableBuilder.buildFromJSONFile("src/test/resources/data/json/metadata-prioritisation.json", "src/test/resources/data/json/data-example4.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
		List<InformationTable> informationTables1;
		informationTables1 = splitter.splitStratified(informationTable, new double [] {1.0/3, 0.5});
		/* debug
		System.out.println("-first-");
		for (int j = 0; j < informationTables1.get(0).getNumberOfObjects(); j++) {
			System.out.println(informationTables1.get(0).getIdentifier(j).toString());
		}*/
		for (int i = 0; i < 10; i++) {
			try {
				informationTable = new InformationTableWithDecisionDistributions(InformationTableBuilder.buildFromJSONFile("src/test/resources/data/json/metadata-prioritisation.json", "src/test/resources/data/json/data-example4.json"));
			}
			catch (FileNotFoundException ex) {
				System.out.println(ex);
			}
			catch (IOException ex) {
				System.out.println(ex);
			}
			List<InformationTable> informationTables2;
			informationTables2 = splitter.splitStratified(informationTable, true, new double [] {1.0/3, 0.5});
			/* debug
			System.out.println("-repeated-");
			for (int j = 0; j < informationTables2.get(0).getNumberOfObjects(); j++) {
				System.out.println(informationTables2.get(0).getIdentifier(j).toString());
			}*/
			assertTrue(allEqualValuesOnFirstAttribute(informationTables1.get(0), informationTables2.get(0)));
			assertTrue(allEqualValuesOnFirstAttribute(informationTables1.get(1), informationTables2.get(1)));
		}
	}
	
	/**
	 * Checks whether two {@link InformationTable information tables} provided as parameters do not have the (even one) equal value on the first attribute.
	 * 
	 * @param it1 first {@link InformationTable information table}
	 * @param it2 second {@link InformationTable information table}
	 * 
	 * @return {@code true} when information tables do not have the (even one) equal value on the first attribute; {@code false} otherwise
	 */
	boolean noEqualValuesOnFirstAttribute(InformationTable it1, InformationTable it2) {
		boolean result = true;
		for (int i = 0; i < it1.getNumberOfObjects(); i++) {
			for (int j = 0; j < it2.getNumberOfObjects(); j++) {
				if (it1.getField(i, 0).isEqualTo(it2.getField(j, 0)) == TernaryLogicValue.TRUE) {
					result = false;
					break;
				}
			}
			if (!result) {
				break;
			}
		}
		return result;
	}
	
	/**
	 * Checks whether two {@link InformationTable information tables} provided as parameters have all the equal subsequent values on the first attribute.
	 * 
	 * @param it1 first {@link InformationTable information table}
	 * @param it2 second {@link InformationTable information table}
	 * 
	 * @return {@code true} when information tables do not have the (even one) equal value on the first attribute; {@code false} otherwise
	 */
	boolean allEqualValuesOnFirstAttribute(InformationTable it1, InformationTable it2) {
		if (it1.getNumberOfObjects() != it2.getNumberOfObjects()) {
			return false;
		}
		boolean result = true;
		for (int i = 0; i < it1.getNumberOfObjects(); i++) {
			if (it1.getField(i, 0).isEqualTo(it2.getField(i, 0)) != TernaryLogicValue.TRUE) {
				result = false;
					break;
			}
		}
		return result;
	}
	
}
