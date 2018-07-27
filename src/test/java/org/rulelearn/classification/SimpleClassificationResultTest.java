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

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.SimpleField;

/**
 * Tests for {@link SimpleClassificationResult}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SimpleClassificationResultTest {
	
	/**
	 * Test construction of {@link SimpleClassificationResult}.
	 */
	@SuppressWarnings("unused")
	@Test
	void testConstruction() {
		try {
			SimpleClassificationResult result = new SimpleClassificationResult(null);
			fail("Construction of classification result with null suggested decision should fail.");
		}
		catch (NullPointerException ex) {
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * Test {@link SimpleClassificationResult#isConsistentWith(org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testIsConsistentWith() {
		SimpleField field1 = IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(new SimpleDecision(field1, 0));
		try {
			classificationResult1.isConsistentWith(null);
			fail("Checking consistency of classification result with null value should fail.");
		}
		catch(NullPointerException ex) {
			System.out.println(ex.toString());
		}
	}

	/**
	 * Test {@link SimpleClassificationResult#getSuggestedDecision()}. 
	 */
	@Test
	void testGetSuggestedDecisionIntegerField01() {
		SimpleField field1 = IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(new SimpleDecision(field1, 0));
		SimpleClassificationResult classificationResult2 = new SimpleClassificationResult(new SimpleDecision(field1, 0));
		assertTrue(classificationResult1.getSuggestedDecision().equals(classificationResult2.getSuggestedDecision()));
	}
	
	/**
	 * Test {@link SimpleClassificationResult#getSuggestedDecision()}. 
	 */
	@Test
	void testGetSuggestedDecisionIntegerField02() {
		SimpleField field1 = IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN);
		SimpleField field2 = IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.COST);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(new SimpleDecision(field1, 0));
		SimpleClassificationResult classificationResult2 = new SimpleClassificationResult(new SimpleDecision(field2, 0));
		assertFalse(classificationResult1.getSuggestedDecision().equals(classificationResult2.getSuggestedDecision()));
	}
	
	/**
	 * Test {@link SimpleClassificationResult#getSuggestedDecision()}. 
	 */
	@Test
	void testGetSuggestedDecisionIntegerField03() {
		SimpleField field1 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		SimpleField field2 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(new SimpleDecision(field1, 0));
		SimpleClassificationResult classificationResult2 = new SimpleClassificationResult(new SimpleDecision(field2, 0));
		assertFalse(classificationResult1.getSuggestedDecision().equals(classificationResult2.getSuggestedDecision()));
	}
	
	/**
	 * Test {@link SimpleClassificationResult#getSuggestedDecision()}. * 
	 */
	@Test
	void testGetSuggestedDecisionIntegerField04() {
		SimpleField field1 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		SimpleField field2 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(new SimpleDecision(field1, 0));
		SimpleClassificationResult classificationResult2 = new SimpleClassificationResult(new SimpleDecision(field2, 1));
		assertFalse(classificationResult1.getSuggestedDecision().equals(classificationResult2.getSuggestedDecision()));
	}
	
	/**
	 * Test {@link SimpleClassificationResult#isConsistentWith(org.rulelearn.types.EvaluationField)}. 
	 */
	@Test
	void testIsConsistentWithIntegerField01() {
		SimpleDecision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		assertTrue(classificationResult1.isConsistentWith(decision1) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test {@link SimpleClassificationResult#isConsistentWith(org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testIsConsistentWithIntegerField02() {
		SimpleDecision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 0);
		SimpleDecision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.COST), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		assertFalse(classificationResult1.isConsistentWith(decision2) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test {@link SimpleClassificationResult#isConsistentWith(org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testIsConsistentWithIntegerField03() {
		SimpleDecision decision1 = new SimpleDecision(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), 0);
		SimpleDecision decision2 = new SimpleDecision(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		assertFalse(classificationResult1.isConsistentWith(decision2) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test {@link SimpleClassificationResult#getSuggestedDecision()}. 
	 */
	@Test
	void testGetSuggestedDecisionEnumerationField01() {
		ElementList domain = null;
		String [] values = {"1", "2", "3"};
		try {
			domain = new ElementList(values);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		SimpleDecision decision1 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 0);		
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		SimpleClassificationResult classificationResult2 = new SimpleClassificationResult(decision1);
		assertTrue(classificationResult1.getSuggestedDecision().equals(classificationResult2.getSuggestedDecision()));
	}
	
	/**
	 * Test {@link SimpleClassificationResult#getSuggestedDecision()}. 
	 */
	@Test
	void testGetSuggestedDecisionEnumerationField02() {
		ElementList domain = null;
		String [] values = {"1", "2", "3"};
		try {
			domain = new ElementList(values);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		SimpleDecision decision1 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 0);
		SimpleDecision decision2 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.COST), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		SimpleClassificationResult classificationResult2 = new SimpleClassificationResult(decision2);
		assertFalse(classificationResult1.getSuggestedDecision().equals(classificationResult2.getSuggestedDecision()));
	}
	
	/**
	 * Test {@link SimpleClassificationResult#getSuggestedDecision()}. 
	 */
	@Test
	void testGetSuggestedDecisionEnumerationField03() {
		ElementList domain = null;
		String [] values = {"1", "2", "3"};
		try {
			domain = new ElementList(values);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		SimpleDecision decision1 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, 0, AttributePreferenceType.GAIN), 0);
		SimpleDecision decision2 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, 1, AttributePreferenceType.GAIN), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		SimpleClassificationResult classificationResult2 = new SimpleClassificationResult(decision2);
		assertFalse(classificationResult1.getSuggestedDecision().equals(classificationResult2.getSuggestedDecision()));
	}
	
	/**
	 * Test {@link SimpleClassificationResult#isConsistentWith(org.rulelearn.types.EvaluationField)}. 
	 */
	@Test
	void testIsConsistentWithEnumerationField01() {
		ElementList domain = null;
		String [] values = {"1", "2", "3"};
		try {
			domain = new ElementList(values);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		SimpleDecision decision1 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		assertTrue(classificationResult1.isConsistentWith(decision1) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test {@link SimpleClassificationResult#isConsistentWith(org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testIsConsistentWithEnumerationField02() {
		ElementList domain = null;
		String [] values = {"1", "2", "3"};
		try {
			domain = new ElementList(values);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		SimpleDecision decision1 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.GAIN), 0);
		SimpleDecision decision2 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, AttributePreferenceType.COST), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		assertFalse(classificationResult1.isConsistentWith(decision2) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test {@link SimpleClassificationResult#isConsistentWith(org.rulelearn.types.EvaluationField)}.
	 */
	@Test
	void testIsConsistentWithEnumerationField03() {
		ElementList domain = null;
		String [] values = {"1", "2", "3"};
		try {
			domain = new ElementList(values);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		SimpleDecision decision1 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, 0, AttributePreferenceType.GAIN), 0);
		SimpleDecision decision2 = new SimpleDecision(EnumerationFieldFactory.getInstance().create(domain, 1, AttributePreferenceType.GAIN), 0);
		SimpleClassificationResult classificationResult1 = new SimpleClassificationResult(decision1);
		assertFalse(classificationResult1.isConsistentWith(decision2) == TernaryLogicValue.TRUE);
	}
	
}
