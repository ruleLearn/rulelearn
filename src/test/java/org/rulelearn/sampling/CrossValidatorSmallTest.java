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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableTestConfiguration;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link CrossValidator} on small data sets.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CrossValidatorSmallTest {

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
						{ "2", "1"},
						{ "3", "1"},
						{ "4", "1"},
						{ "5", "1"},
						{ "6", "2"},
						{ "7", "2"}
				});
	}

	/**
	 * Tests for {@link CrossValidator#splitIntoKFold(InformationTable, int)}.
	 */
	@Test
	void testCrossValidator01() {
		InformationTable informationTable = informationTableTestConfiguration.getInformationTable(true);
		CrossValidator validator = new CrossValidator(new Random(64));
		List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitIntoKFold(informationTable, 3);
		assertEquals(3, folds.size());
		assertEquals(5, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(2, folds.get(0).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(0).getTrainingTable(), folds.get(0).getValidationTable()));
		assertEquals(5, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(2, folds.get(1).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(1).getTrainingTable(), folds.get(1).getValidationTable()));
		assertEquals(4, folds.get(2).getTrainingTable().getNumberOfObjects());
		assertEquals(3, folds.get(2).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(2).getTrainingTable(), folds.get(2).getValidationTable()));
	}
	
	/**
	 * Tests for {@link CrossValidator#splitStratifiedIntoKFold(InformationTable, int)}.
	 */
	@Test
	void testCrossValidator02() {
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(informationTableTestConfiguration.getInformationTable(true));
		CrossValidator validator = new CrossValidator(new Random(64));
		List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitStratifiedIntoKFold(informationTable, 3);
		assertEquals(3, folds.size());
		assertEquals(6, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(1, folds.get(0).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(0).getTrainingTable(), folds.get(0).getValidationTable()));
		assertEquals(6, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(1, folds.get(1).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(1).getTrainingTable(), folds.get(1).getValidationTable()));
		assertEquals(2, folds.get(2).getTrainingTable().getNumberOfObjects());
		assertEquals(5, folds.get(2).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(2).getTrainingTable(), folds.get(2).getValidationTable()));
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
}
