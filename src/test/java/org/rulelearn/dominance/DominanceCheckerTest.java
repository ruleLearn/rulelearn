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

package org.rulelearn.dominance;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.Table;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;

/**
 * Tests for {@link DominanceChecker}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class DominanceCheckerTest {
	
	/**
	 * Supplementary method for creating {@link IntegerField} instances.
	 * 
	 * @param value value passed to {@link IntegerFieldFactory#create(int, AttributePreferenceType)} method.
	 * @param preferenceType preference type passed to {@link IntegerFieldFactory#create(int, AttributePreferenceType)} method.
	 * @return created {@link IntegerField} instance.
	 */
	private IntegerField intField(int value, AttributePreferenceType preferenceType) {
		return IntegerFieldFactory.getInstance().create(value, preferenceType);
	}
	
	/**
	 * Supplementary method for creating {@link RealField} instances.
	 * 
	 * @param value value passed to {@link RealFieldFactory#create(double, AttributePreferenceType)} method.
	 * @param preferenceType preference type passed to {@link RealFieldFactory#create(double, AttributePreferenceType)} method.
	 * @return created {@link RealField} instance.
	 */
	private RealField realField(double value, AttributePreferenceType preferenceType) {
		return RealFieldFactory.getInstance().create(value, preferenceType);
	}
	
	/**
	 * Creates a mock of an {@link InformationTable}.
	 * 
	 * @param x index of a first object
	 * @param y index of a second object
	 * @param xEvaluations array with evaluations of the first object
	 * @param yEvaluations array with evaluations of the second object
	 * 
	 * @return mock of an {@link InformationTable}
	 */
	private InformationTable createInformationTableMock(int x, int y, EvaluationField[] xEvaluations, EvaluationField[] yEvaluations) {
		@SuppressWarnings("unchecked")
		Table<EvaluationAttribute, EvaluationField> evaluations = (Table<EvaluationAttribute, EvaluationField>)Mockito.mock(Table.class);
		
		Mockito.when(evaluations.getFields(x)).thenReturn(xEvaluations);
		Mockito.when(evaluations.getFields(y)).thenReturn(yEvaluations);
		
		InformationTable informationTableMock = Mockito.mock(InformationTable.class);
		
		Mockito.when(informationTableMock.getActiveConditionAttributeFields()).thenReturn(evaluations);
		
		return informationTableMock;
	}

	/**
	 * Test method for {@link DominanceChecker#dominates(int, int, InformationTable)}.
	 */
	@Test
	void testDominates01() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(2, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertTrue(DominanceChecker.dominates(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#dominates(int, int, InformationTable)}.
	 */
	@Test
	void testDominates02() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), realField(2, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertTrue(DominanceChecker.dominates(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#dominates(int, int, InformationTable)}.
	 */
	@Test
	void testDominates03() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(2, AttributePreferenceType.NONE)};
		
		assertFalse(DominanceChecker.dominates(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#dominates(int, int, InformationTable)}.
	 */
	@Test
	void testDominates04() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertFalse(DominanceChecker.dominates(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#dominates(int, int, InformationTable)}.
	 */
	@Test
	void testDominates05() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(2, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertFalse(DominanceChecker.dominates(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}

	/**
	 * Test method for {@link DominanceChecker#isDominatedBy(int, int, InformationTable)}.
	 */
	@Test
	void testIsDominatedBy01() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(2, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertTrue(DominanceChecker.isDominatedBy(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#isDominatedBy(int, int, InformationTable)}.
	 */
	@Test
	void testIsDominatedBy02() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(2, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertTrue(DominanceChecker.isDominatedBy(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#isDominatedBy(int, int, InformationTable)}.
	 */
	@Test
	void testIsDominatedBy03() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(2, AttributePreferenceType.NONE)};
		
		assertFalse(DominanceChecker.isDominatedBy(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#isDominatedBy(int, int, InformationTable)}.
	 */
	@Test
	void testIsDominatedBy04() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(2, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertFalse(DominanceChecker.isDominatedBy(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}
	
	/**
	 * Test method for {@link DominanceChecker#isDominatedBy(int, int, InformationTable)}.
	 */
	@Test
	void testIsDominatedBy05() {
		int x = 0;
		int y = 1;
		
		EvaluationField[] xEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(1, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		EvaluationField[] yEvaluations = new EvaluationField[] {intField(1, AttributePreferenceType.GAIN), realField(2, AttributePreferenceType.COST), realField(3, AttributePreferenceType.NONE)};
		
		assertFalse(DominanceChecker.isDominatedBy(x, y, createInformationTableMock(x, y, xEvaluations, yEvaluations)));
	}

}
