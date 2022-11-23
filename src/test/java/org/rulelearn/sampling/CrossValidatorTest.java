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

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.DecisionDistribution;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableTestConfiguration;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.SimpleDecision;
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
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(0).getTrainingTable(), folds.get(0).getValidationTable()));
		assertEquals(15, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(15, folds.get(1).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(1).getTrainingTable(), folds.get(1).getValidationTable()));
		folds = validator.splitIntoKFold(informationTable, 3);
		assertEquals(3, folds.size());
		assertEquals(20, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(10, folds.get(0).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(0).getTrainingTable(), folds.get(0).getValidationTable()));
		assertEquals(20, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(10, folds.get(1).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(1).getTrainingTable(), folds.get(1).getValidationTable()));
		assertEquals(20, folds.get(2).getTrainingTable().getNumberOfObjects());
		assertEquals(10, folds.get(2).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(2).getTrainingTable(), folds.get(2).getValidationTable()));
		folds = validator.splitIntoKFold(informationTable, 4);
		assertEquals(4, folds.size());
		assertEquals(23, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(7, folds.get(0).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(0).getTrainingTable(), folds.get(0).getValidationTable()));
		assertEquals(23, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(7, folds.get(1).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(1).getTrainingTable(), folds.get(1).getValidationTable()));
		assertEquals(23, folds.get(2).getTrainingTable().getNumberOfObjects());
		assertEquals(7, folds.get(2).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(2).getTrainingTable(), folds.get(2).getValidationTable()));
		// last fold is different (all remaining objects are placed into the validation set)
		assertEquals(21, folds.get(3).getTrainingTable().getNumberOfObjects());
		assertEquals(9, folds.get(3).getValidationTable().getNumberOfObjects());
		assertTrue(noEqualValuesOnFirstAttribute(folds.get(3).getTrainingTable(), folds.get(3).getValidationTable()));
	}
	
	/**
	 * Tests for {@link CrossValidator#splitStratifiedIntoKFold(InformationTableWithDecisionDistributions, int)}.
	 */
	@SuppressWarnings("deprecation")
	@Test
	void testSplit03() {
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(informationTableTestConfiguration.getInformationTable(true));
		CrossValidator validator = new CrossValidator(new Random(64));
		// check distribution of decisions in the information table
		for (Decision decision : informationTable.getDecisions()) {
			assertEquals(1.0/3, ((double)informationTable.getDecisionDistribution().getCount(decision))/informationTable.getNumberOfObjects());
		}
		// perform cross-validation
		List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitStratifiedIntoKFold(informationTable, 0);
		assertEquals(0, folds.size());
		folds = validator.splitStratifiedIntoKFold(informationTable, 1);
		assertEquals(1, folds.size());
		assertEquals(0, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(30, folds.get(0).getValidationTable().getNumberOfObjects());
		folds = validator.splitStratifiedIntoKFold(informationTable, 2);
		assertEquals(2, folds.size());
		assertEquals(15, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(15, folds.get(0).getValidationTable().getNumberOfObjects());
		assertEquals(15, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(15, folds.get(1).getValidationTable().getNumberOfObjects());
		// check distribution of decisions in information sub-tables and no 
		for (int i = 0; i < folds.size(); i++) {
			CrossValidator.CrossValidationFold<InformationTable> fold = folds.get(i);
			// last fold is different so it is not checked for validity of distribution of decisions
			if (i < (folds.size()-1)) {
				InformationTableWithDecisionDistributions informationSubTable = new InformationTableWithDecisionDistributions(fold.getTrainingTable());
	 			for (Decision decision : informationTable.getDecisions()) {
					assertEquals(1.0/3, ((double)informationSubTable.getDecisionDistribution().getCount(decision))/informationSubTable.getNumberOfObjects());
				}
	 			informationSubTable = new InformationTableWithDecisionDistributions(fold.getValidationTable());
	 			for (Decision decision : informationTable.getDecisions()) {
					assertEquals(1.0/3, ((double)informationSubTable.getDecisionDistribution().getCount(decision))/informationSubTable.getNumberOfObjects());
				}
			}
 			assertTrue(noEqualValuesOnFirstAttribute(fold.getTrainingTable(), fold.getValidationTable()));
		}
		folds = validator.splitStratifiedIntoKFold(informationTable, 3);
		assertEquals(3, folds.size());
		assertEquals(21, folds.get(0).getTrainingTable().getNumberOfObjects());
		assertEquals(9, folds.get(0).getValidationTable().getNumberOfObjects());
		assertEquals(21, folds.get(1).getTrainingTable().getNumberOfObjects());
		assertEquals(9, folds.get(1).getValidationTable().getNumberOfObjects());
		assertEquals(18, folds.get(2).getTrainingTable().getNumberOfObjects());
		assertEquals(12, folds.get(2).getValidationTable().getNumberOfObjects());
		// check distribution of decisions in information sub-tables and no 
		for (int i = 0; i < folds.size(); i++) {
			CrossValidator.CrossValidationFold<InformationTable> fold = folds.get(i);
			// last fold is different so it is not checked for validity of distribution of decisions
			if (i < (folds.size()-1)) {
				InformationTableWithDecisionDistributions informationSubTable = new InformationTableWithDecisionDistributions(fold.getTrainingTable());
	 			for (Decision decision : informationTable.getDecisions()) {
					assertEquals(1.0/3, ((double)informationSubTable.getDecisionDistribution().getCount(decision))/informationSubTable.getNumberOfObjects());
				}
	 			informationSubTable = new InformationTableWithDecisionDistributions(fold.getValidationTable());
	 			for (Decision decision : informationTable.getDecisions()) {
					assertEquals(1.0/3, ((double)informationSubTable.getDecisionDistribution().getCount(decision))/informationSubTable.getNumberOfObjects());
				}
			}
 			assertTrue(noEqualValuesOnFirstAttribute(fold.getTrainingTable(), fold.getValidationTable()));
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
	 * Test for {@link CrossValidator#splitStratifiedIntoKFolds(InformationTableWithDecisionDistributions, boolean, int)}.
	 * Tests 3 classes, with 13, 13, and 17 objects + 10-fold CV.
	 * Objects are ordered according to class.
	 */
	@Test
	void testSplitStratifiedIntoKFolds01() {
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		CrossValidator validator = new CrossValidator(new Random());
		int decisionAttributeIndex = 1;
		int countDecision1 = 13;
		int countDecision2 = 13;
		int countDecision3 = 17;
		int objectIndex = 0;
		int k = 10;
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), decisionAttributeIndex);
		DecisionDistribution decisionDistributionMock = Mockito.mock(DecisionDistribution.class);
		@SuppressWarnings("unchecked")
		Set<Decision> decisionsMock = (Set<Decision>)Mockito.mock(Set.class);
		
		Decision decisions[] = new Decision[countDecision1 + countDecision2 + countDecision3];
		for (int i = 0; i < countDecision1; i++) {
			decisions[objectIndex++] = decision1;
		}
		for (int i = 0; i < countDecision2; i++) {
			decisions[objectIndex++] = decision2;
		}
		for (int i = 0; i < countDecision3; i++) {
			decisions[objectIndex++] = decision3;
		}
				
		Mockito.when(informationTableMock.getDecisions()).thenReturn(decisions);
		Mockito.when(informationTableMock.getUniqueDecisions()).thenReturn(new Decision[] {decision1, decision2, decision3});
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(countDecision1 + countDecision2 + countDecision3);
		Mockito.when(informationTableMock.getDecisionDistribution()).thenReturn(decisionDistributionMock);
		Mockito.when(decisionDistributionMock.getCount(decision1)).thenReturn(countDecision1);
		Mockito.when(decisionDistributionMock.getCount(decision2)).thenReturn(countDecision2);
		Mockito.when(decisionDistributionMock.getCount(decision3)).thenReturn(countDecision3);
		Mockito.when(decisionDistributionMock.getDecisions()).thenReturn(decisionsMock);
		Mockito.when(decisionsMock.size()).thenReturn(decisions.length);
		
		//Mockito.when(informationTableMock.discard(Mockito.any(int[].class), Mockito.anyBoolean())).thenCallRealMethod();
		//Mockito.when(informationTableMock.select(Mockito.any(int[].class), Mockito.anyBoolean())).thenCallRealMethod();
		Mockito.when(informationTableMock.select(Mockito.any(int[].class), Mockito.anyBoolean())).thenReturn(null); //this is enough
		Mockito.when(informationTableMock.discard(Mockito.any(int[].class), Mockito.anyBoolean())).thenReturn(null); //this is enough
		
		//perform cross-validation
		@SuppressWarnings("unused")
		List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitStratifiedIntoKFolds(informationTableMock, k);
		
		ArgumentCaptor<int[]> inFoldSortedValidationSetObjectIndicesCaptor = ArgumentCaptor.forClass(int[].class); //captor used to capture all invocations of discard method
		
		Mockito.verify(informationTableMock, Mockito.times(k)).discard(inFoldSortedValidationSetObjectIndicesCaptor.capture(), Mockito.anyBoolean()); //capture all lists of object indices
		List<int[]> capturedInFoldSortedValidationSetObjectIndices = inFoldSortedValidationSetObjectIndicesCaptor.getAllValues(); //get all lists of object indices
		
		//assert on cardinality of validation objects in each fold
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(0).length, 5);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(1).length, 5);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(2).length, 5);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(3).length, 4);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(4).length, 4);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(5).length, 4);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(6).length, 4);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(7).length, 4);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(8).length, 4);
		assertEquals(capturedInFoldSortedValidationSetObjectIndices.get(9).length, 4);
		
		//visualize content of validation sets in particular folds
		for (int i = 0; i < k; i++) { //go over folds
			System.out.print(i+": "); //print fold index
			java.util.Arrays.stream(capturedInFoldSortedValidationSetObjectIndices.get(i)).forEach(
					index -> System.out.print(" "+index+"("+decisions[index].getEvaluation(decisionAttributeIndex)+")")); //print in-fold validation objects, along with their classes
			System.out.println();
		}
		
		System.out.println();
		
		//visualize presence of decisions in particular folds
		for (int i = 0; i < k; i++) { //go over folds
			System.out.print(i+": "); //print fold index
			java.util.Arrays.stream(capturedInFoldSortedValidationSetObjectIndices.get(i)).mapToObj(
					index -> decisions[index].getEvaluation(decisionAttributeIndex)).forEach(
							evaluation -> System.out.print(evaluation+" ")); //print decisions at fold
			System.out.println();
		}
		
		int foldIndex = 0;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1); //takes advantage of ordering of fold validation objects
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[4]], decision3);
		
		foldIndex = 1;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[4]], decision3);
		
		foldIndex = 2;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[4]], decision3);
		
		foldIndex = 3;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		
		foldIndex = 4;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		
		foldIndex = 5;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		
		foldIndex = 6;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision3);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		
		foldIndex = 7;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision3);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		
		foldIndex = 8;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision3);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
		
		foldIndex = 9;
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[0]], decision1);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[1]], decision2);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[2]], decision3);
		assertEquals(decisions[capturedInFoldSortedValidationSetObjectIndices.get(foldIndex)[3]], decision3);
	}
	
	/**
	 * Test for {@link CrossValidator#splitStratifiedIntoKFolds(InformationTableWithDecisionDistributions, boolean, int)}.
	 * Tests 3 classes, with 13, 13, and 17 objects + 10-fold CV.
	 * Objects are NOT ordered according to class.
	 */
	@Test
	void testSplitStratifiedIntoKFolds02() {
		InformationTableWithDecisionDistributions informationTableMock = Mockito.mock(InformationTableWithDecisionDistributions.class);
		CrossValidator validator = new CrossValidator(new Random());
		int decisionAttributeIndex = 1;
		int countDecision1 = 13;
		int countDecision2 = 13;
		int countDecision3 = 17;
		int k = 10;
		Decision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), decisionAttributeIndex);
		Decision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN), decisionAttributeIndex);
		Decision decision3 = new SimpleDecision(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN), decisionAttributeIndex);
		DecisionDistribution decisionDistributionMock = Mockito.mock(DecisionDistribution.class);
		@SuppressWarnings("unchecked")
		Set<Decision> decisionsMock = (Set<Decision>)Mockito.mock(Set.class);
		
		Decision decisions[] = {decision1, decision1, decision2, decision1, decision2, decision3, decision2, decision3, decision3, decision1,
				decision2, decision2, decision3, decision1, decision3, decision3, decision2, decision3, decision1, decision3,
				decision2, decision1, decision3, decision2, decision1, decision1, decision2, decision3, decision3, decision3,
				decision2, decision1, decision3, decision2, decision3, decision1, decision2, decision3, decision1, decision3,
				decision2, decision3, decision1}; //mix decisions, but preserve order of decisions (first seen decision1, then decision2, then decision3
		
		Mockito.when(informationTableMock.getDecisions()).thenReturn(decisions);
		Mockito.when(informationTableMock.getUniqueDecisions()).thenReturn(new Decision[] {decision1, decision2, decision3});
		Mockito.when(informationTableMock.getNumberOfObjects()).thenReturn(countDecision1 + countDecision2 + countDecision3);
		Mockito.when(informationTableMock.getDecisionDistribution()).thenReturn(decisionDistributionMock);
		Mockito.when(decisionDistributionMock.getCount(decision1)).thenReturn(countDecision1);
		Mockito.when(decisionDistributionMock.getCount(decision2)).thenReturn(countDecision2);
		Mockito.when(decisionDistributionMock.getCount(decision3)).thenReturn(countDecision3);
		Mockito.when(decisionDistributionMock.getDecisions()).thenReturn(decisionsMock);
		Mockito.when(decisionsMock.size()).thenReturn(decisions.length);
		
		//Mockito.when(informationTableMock.discard(Mockito.any(int[].class), Mockito.anyBoolean())).thenCallRealMethod();
		//Mockito.when(informationTableMock.select(Mockito.any(int[].class), Mockito.anyBoolean())).thenCallRealMethod();
		Mockito.when(informationTableMock.select(Mockito.any(int[].class), Mockito.anyBoolean())).thenReturn(null); //this is enough
		Mockito.when(informationTableMock.discard(Mockito.any(int[].class), Mockito.anyBoolean())).thenReturn(null); //this is enough
		
		//perform cross-validation
		@SuppressWarnings("unused")
		List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitStratifiedIntoKFolds(informationTableMock, k);
		
		ArgumentCaptor<int[]> inFoldSortedValidationSetObjectIndicesCaptor = ArgumentCaptor.forClass(int[].class); //captor used to capture all invocations of discard method
		
		Mockito.verify(informationTableMock, Mockito.times(k)).discard(inFoldSortedValidationSetObjectIndicesCaptor.capture(), Mockito.anyBoolean()); //capture all lists of object indices
		List<int[]> capturedSortedValidationSetObjectIndices = inFoldSortedValidationSetObjectIndicesCaptor.getAllValues(); //get all lists of object indices
		
		//assert on cardinality of validation objects in each fold
		assertEquals(capturedSortedValidationSetObjectIndices.get(0).length, 5);
		assertEquals(capturedSortedValidationSetObjectIndices.get(1).length, 5);
		assertEquals(capturedSortedValidationSetObjectIndices.get(2).length, 5);
		assertEquals(capturedSortedValidationSetObjectIndices.get(3).length, 4);
		assertEquals(capturedSortedValidationSetObjectIndices.get(4).length, 4);
		assertEquals(capturedSortedValidationSetObjectIndices.get(5).length, 4);
		assertEquals(capturedSortedValidationSetObjectIndices.get(6).length, 4);
		assertEquals(capturedSortedValidationSetObjectIndices.get(7).length, 4);
		assertEquals(capturedSortedValidationSetObjectIndices.get(8).length, 4);
		assertEquals(capturedSortedValidationSetObjectIndices.get(9).length, 4);
		
		//visualize content of validation sets in particular folds
		for (int i = 0; i < k; i++) { //go over folds
			System.out.print(i+": "); //print fold index
			java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(i)).forEach(
					index -> System.out.print(" "+index+"("+decisions[index].getEvaluation(decisionAttributeIndex)+")")); //print in-fold validation objects, along with their classes
			System.out.println();
		}
		
		System.out.println();
		
		//visualize presence of decisions in particular folds
		for (int i = 0; i < k; i++) { //go over folds
			System.out.print(i+": "); //print fold index
			java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(i)).mapToObj(
					index -> decisions[index].getEvaluation(decisionAttributeIndex)).forEach(
							evaluation -> System.out.print(evaluation+" ")); //print decisions at fold
			System.out.println();
		}
		
		int foldIndex = -1;
		int decisionIndex;
		Iterator<Decision> sortedDecisionsIteartor;
		Decision[] expectedSortedDecisions;
		Comparator<? super Decision> decisionComparator = (dec1, dec2) -> {
			return ((IntegerField)dec1.getEvaluation(decisionAttributeIndex)).compareTo((IntegerField)dec2.getEvaluation(decisionAttributeIndex));
		};
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision1, decision2, decision3, decision3}; //decisions of validation objects of fold 0
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision1, decision2, decision3, decision3}; //decisions of validation objects of fold 1
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision1, decision2, decision3, decision3}; //decisions of validation objects of fold 2
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision2, decision2, decision3}; //decisions of validation objects of fold 3
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision2, decision2, decision3}; //decisions of validation objects of fold 4
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision2, decision2, decision3}; //decisions of validation objects of fold 5
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision2, decision3, decision3}; //decisions of validation objects of fold 6
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision2, decision3, decision3}; //decisions of validation objects of fold 7
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision2, decision3, decision3}; //decisions of validation objects of fold 8
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
		
		sortedDecisionsIteartor = java.util.Arrays.stream(capturedSortedValidationSetObjectIndices.get(++foldIndex)).mapToObj(index -> decisions[index]).sorted(decisionComparator).iterator(); //sort stream of decisions
		expectedSortedDecisions = new Decision[] {decision1, decision2, decision3, decision3}; //decisions of validation objects of fold 9
		for (decisionIndex = 0; sortedDecisionsIteartor.hasNext(); decisionIndex++) {
			assertEquals(sortedDecisionsIteartor.next(), expectedSortedDecisions[decisionIndex]);
		}
		assertEquals(decisionIndex, expectedSortedDecisions.length); //all decisions from the iterator and array were compared
	}
	
}
