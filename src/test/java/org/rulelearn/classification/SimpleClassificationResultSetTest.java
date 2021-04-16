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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.data.InformationTable;

/**
 * SimpleClassificationResultSetTest
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleClassificationResultSetTest {

	private SimpleClassificationResultSet classificationResultSet;
	
	@Mock
	private InformationTable informationTableMock;
	@Mock
	private SimpleClassifier classifierMock;
	@Mock
	private SimpleClassificationResult classificationResultMock1, classificationResultMock2;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(2);
		this.classificationResultSet = new SimpleClassificationResultSet(this.informationTableMock, this.classifierMock);
		
		when(this.classifierMock.classify(0, this.informationTableMock)).thenReturn(this.classificationResultMock1);
		when(this.classifierMock.classify(1, this.informationTableMock)).thenReturn(this.classificationResultMock2);
	}

	/**
	 * Test construction of {@link SimpleClassificationResultSet}.
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
	 * Test for {@link SimpleClassificationResultSet#getClassificationResult(int)}.
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
	 * Test for {@link SimpleClassificationResultSet#getClassificationResult(int)}.
	 */
	@Test
	void testGetClassificationResult02() {
		assertEquals(this.classificationResultSet.getClassificationResult(0), this.classificationResultMock1);
		assertEquals(this.classificationResultSet.getClassificationResult(1), this.classificationResultMock2);
	}
	
	/**
	 * Test for {@link SimpleClassificationResultSet#getClassifier()}.
	 */
	@Test
	void testGetClassifier() {
		assertEquals(this.classificationResultSet.getClassifier(), this.classifierMock);
	}
	
	/**
	 * Test for {@link SimpleClassificationResultSet#getClassificationResults}.
	 */
	@Test
	void testGetClassificationResuls01() {
		assertEquals(this.classificationResultSet.getClassificationResults()[0], this.classificationResultMock1);
		assertEquals(this.classificationResultSet.getClassificationResults()[1], this.classificationResultMock2);
	}
	
	/**
	 * Test for {@link SimpleClassificationResultSet#getClassificationResults(boolean)}.
	 */
	@Test
	void testGetClassificationResuls02() {
		assertEquals(this.classificationResultSet.getClassificationResults(true)[0], this.classificationResultMock1);
		assertEquals(this.classificationResultSet.getClassificationResults(true)[1], this.classificationResultMock2);
	}
}
