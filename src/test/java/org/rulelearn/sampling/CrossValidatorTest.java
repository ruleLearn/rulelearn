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

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableTestConfiguration;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link CrossValidator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CrossValidatorTest {

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
	 * Tests for {@link CrossValidator#splitIntoKFold(InformationTable, int)}.
	 */
	@Test
	void testCrossValidator01() {
		InformationTable informationTable = informationTableTestConfiguration.getInformationTable(true);
		CrossValidator validator = new CrossValidator(new Random(64));
		assertThrows(NullPointerException.class, () -> {validator.splitIntoKFold(null, -1);});
		assertThrows(NullPointerException.class, () -> {validator.splitIntoKFold(null, 0);});
		assertThrows(InvalidValueException.class, () -> {validator.splitIntoKFold(informationTable, -1);});
	}

	/**
	 * Tests for {@link CrossValidator#splitIntoKFold(InformationTable, int)}.
	 */
	@Test
	void testCrossValidator02() {
		InformationTable informationTable = informationTableTestConfiguration.getInformationTable(true);
		CrossValidator validator = new CrossValidator(new Random(64));
		List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitIntoKFold(informationTable, 0);
		assertEquals(0, folds.size());
		folds = validator.splitIntoKFold(informationTable, 1);
		assertEquals(1, folds.size());
		assertEquals(0, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(30, folds.get(0).getValidationTable().getNumberOfObjects());
		folds = validator.splitIntoKFold(informationTable, 2);
		assertEquals(2, folds.size());
		assertEquals(15, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(15, folds.get(0).getValidationTable().getNumberOfObjects());
		assertEquals(15, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(15, folds.get(1).getValidationTable().getNumberOfObjects());
		folds = validator.splitIntoKFold(informationTable, 3);
		assertEquals(3, folds.size());
		assertEquals(20, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(10, folds.get(0).getValidationTable().getNumberOfObjects());
		assertEquals(20, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(10, folds.get(1).getValidationTable().getNumberOfObjects());
		assertEquals(20, folds.get(2).getTrainingTable().getNumberOfObjects());
		assertEquals(10, folds.get(2).getValidationTable().getNumberOfObjects());
		folds = validator.splitIntoKFold(informationTable, 4);
		assertEquals(4, folds.size());
		assertEquals(23, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(7, folds.get(0).getValidationTable().getNumberOfObjects());
		assertEquals(23, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(7, folds.get(1).getValidationTable().getNumberOfObjects());
		assertEquals(23, folds.get(2).getTrainingTable().getNumberOfObjects());
		assertEquals(7, folds.get(2).getValidationTable().getNumberOfObjects());
		// last fold is different (all remaining objects are taken to the validation set)
		assertEquals(21, folds.get(3).getTrainingTable().getNumberOfObjects());
		assertEquals(9, folds.get(3).getValidationTable().getNumberOfObjects());
	}
}
