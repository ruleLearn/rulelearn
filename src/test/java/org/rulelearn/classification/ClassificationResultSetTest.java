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

package org.rulelearn.classification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Tests for {@link ClassificationResultSet}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class ClassificationResultSetTest {
	
	private ClassificationResultSet classificationResultSet;
	private Attribute[] attributes;
	private IntSet decisionAttributeIndices;
	private ElementList listOfLabels;
	private Field o0a0, o0a1, o0a2, o1a0, o1a1, o1a2;
	private EvaluationField o0a3, o1a3;
	
	@Mock
	private InformationTable informationTableMock;
	@Mock
	private Classifier classifierMock;
	@Mock
	private ClassificationResult classificationResultMock1, classificationResultMock2;
	@Mock
	private SimpleDecision simpleDecisionMock1, simpleDecisionMock2;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		
		attributes = new Attribute[4];
		attributes[0] = new IdentificationAttribute("i1", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE));
		attributes[1] = new EvaluationAttribute("a1", true, AttributeType.CONDITION, 
				IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.COST),
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[2] = new EvaluationAttribute("a2", true, AttributeType.CONDITION, 
				RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN),
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		String [] labels = {"a", "b", "c"};
		try {
			this.listOfLabels = new ElementList(labels);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		attributes[3] = new EvaluationAttribute("d", true, AttributeType.DECISION, 
				EnumerationFieldFactory.getInstance().create(listOfLabels, 0, AttributePreferenceType.GAIN),
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		
		when(this.informationTableMock.getNumberOfAttributes()).thenReturn(4);
		when(this.informationTableMock.getAttributes()).thenReturn(this.attributes);
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(2);
		this.o0a0 = new TextIdentificationField("o1");
		when(this.informationTableMock.getField(0, 0)).thenReturn(this.o0a0);
		this.o0a1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		when(this.informationTableMock.getField(0, 1)).thenReturn(this.o0a1);
		this.o0a2 = RealFieldFactory.getInstance().create(0.0, AttributePreferenceType.GAIN);
		when(this.informationTableMock.getField(0, 2)).thenReturn(this.o0a2);
		this.o1a0 = new TextIdentificationField("o2");
		when(this.informationTableMock.getField(1, 0)).thenReturn(this.o1a0);
		this.o1a1 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		when(this.informationTableMock.getField(1, 1)).thenReturn(this.o1a1);
		this.o1a2 = RealFieldFactory.getInstance().create(1.0, AttributePreferenceType.GAIN);
		when(this.informationTableMock.getField(1, 2)).thenReturn(this.o1a2);
		
		
		when(this.classifierMock.classify(0, this.informationTableMock)).thenReturn(this.classificationResultMock1);
		when(this.classifierMock.classify(1, this.informationTableMock)).thenReturn(this.classificationResultMock2);
		when(this.classificationResultMock1.getSuggestedDecision()).thenReturn(this.simpleDecisionMock1);
		when(this.classificationResultMock2.getSuggestedDecision()).thenReturn(this.simpleDecisionMock2);
		this.decisionAttributeIndices = new IntArraySet(1);
		this.decisionAttributeIndices.add(3);
		when(this.simpleDecisionMock1.getAttributeIndices()).thenReturn(this.decisionAttributeIndices);
		this.o0a3 = EnumerationFieldFactory.getInstance().create(listOfLabels, 0, AttributePreferenceType.GAIN);
		when(this.simpleDecisionMock1.getEvaluation(3)).thenReturn(this.o0a3);
		when(this.simpleDecisionMock2.getAttributeIndices()).thenReturn(this.decisionAttributeIndices);
		this.o1a3 = EnumerationFieldFactory.getInstance().create(listOfLabels, 1, AttributePreferenceType.GAIN);
		when(this.simpleDecisionMock2.getEvaluation(3)).thenReturn(this.o1a3);
		
		this.classificationResultSet = new ClassificationResultSet(this.informationTableMock, this.classifierMock);
	}

	/**
	 * Test construction of {@link ClassificationResultSet}.
	 */
	@SuppressWarnings("unused")
	@Test
	void testConstruction() {
		try {
			ClassificationResultSet result = new ClassificationResultSet(null, null);
			fail("Construction of classification result set based on no (null) information table and no (null) classifier should fail.");
		}
		catch (NullPointerException ex) {
			System.out.println(ex.toString());
		}
		try {
			ClassificationResultSet result = new ClassificationResultSet(null, this.classifierMock);
			fail("Construction of classification result set based on no (null) information table should fail.");
		}
		catch (NullPointerException ex) {
			System.out.println(ex.toString());
		}
		try {
			ClassificationResultSet result = new ClassificationResultSet(this.informationTableMock, null);
			fail("Construction of classification result set based on no (null) classifier should fail.");
		}
		catch (NullPointerException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test for {@link ClassificationResultSet#getClassificationResult(int)}.
	 */
	@Test
	void testGetClassificationResult01() {
		try {
			this.classificationResultSet.getClassificationResult(2);
			fail("Getting classification result for an object with index higher than size of information table for which classification result set was created should fail.");
		}
		catch (IndexOutOfBoundsException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test for {@link ClassificationResultSet#getClassificationResult(int)}.
	 */
	@Test
	void testGetClassificationResult02() {
		assertEquals(this.classificationResultSet.getClassificationResult(0), this.classificationResultMock1);
		assertEquals(this.classificationResultSet.getClassificationResult(1), this.classificationResultMock2);
	}
	
	/**
	 * Test for {@link ClassificationResultSet#getInformationTable()}.
	 */
	@Test
	void testGetInformationTable() {
		assertEquals(this.classificationResultSet.getInformationTable(), this.informationTableMock);
	}
	
	/**
	 * Test for {@link ClassificationResultSet#getClassifier()}.
	 */
	@Test
	void testGetClassifier() {
		assertEquals(this.classificationResultSet.getClassifier(), this.classifierMock);
	}

	/**
	 * Test for {@link ClassificationResultSet#getClassificationResults}.
	 */
	@Test
	void testGetClassificationResuls01() {
		assertEquals(this.classificationResultSet.getClassificationResults()[0], this.classificationResultMock1);
		assertEquals(this.classificationResultSet.getClassificationResults()[1], this.classificationResultMock2);
	}
	
	/**
	 * Test for {@link ClassificationResultSet#getClassificationResults(boolean)}.
	 */
	@Test
	void testGetClassificationResuls02() {
		assertEquals(this.classificationResultSet.getClassificationResults(true)[0], this.classificationResultMock1);
		assertEquals(this.classificationResultSet.getClassificationResults(true)[1], this.classificationResultMock2);
	}
	
	/**
	 * Test for {@link ClassificationResultSet#getClassificationResults(boolean)}.
	 */
	@Test
	void testGetInformationTableWithClassificationResults() {
		InformationTable testInformationTable = this.classificationResultSet.getInformationTableWithClassificationResults();
		//for debugging
		/*for (int i = 0; i < testInformationTable.getNumberOfObjects(); i++) {
			for (int j = 0; j < testInformationTable.getNumberOfAttributes(); j++) {
				System.out.println(testInformationTable.getField(i, j));
			}
		}*/
		assertEquals(testInformationTable.getField(0, 0), this.o0a0);
		assertEquals(testInformationTable.getField(0, 1), this.o0a1);
		assertEquals(testInformationTable.getField(0, 2), this.o0a2);
		assertEquals(testInformationTable.getField(0, 3), this.o0a3);
		assertEquals(testInformationTable.getField(1, 0), this.o1a0);
		assertEquals(testInformationTable.getField(1, 1), this.o1a1);
		assertEquals(testInformationTable.getField(1, 2), this.o1a2);
		assertEquals(testInformationTable.getField(1, 3), this.o1a3);
	}
	
}
