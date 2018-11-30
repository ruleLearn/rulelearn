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

package org.rulelearn.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.UnknownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link CentralTendencyCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CentralTendencyCalculatorTest {

	private SimpleField sField, sField0n, sField0c, sField0g;
	private SimpleField sField1n, sField1c, sField1g;
	private SimpleField sField2n, sField2c, sField2g;
	
	private UnknownSimpleField uField0, uField1;
	private KnownSimpleField kField0, kField1;
	
	private UnknownSimpleFieldMV15 uFieldMV15;
	private UnknownSimpleFieldMV2 uFieldMV2;
	
	private IntegerField iField, iField0n, iField0c, iField0g;
	private IntegerField iField1n, iField1c, iField1g;
	
	private RealField rField, rField0n, rField0c, rField0g;
	private RealField rField1n, rField1c, rField1g;
	
	private EnumerationField eField, eField0n, eField0c, eField0g;
	private EnumerationField eField1n, eField1c, eField1g;
	private EnumerationField eField2n, eField2c, eField2g;
	
	private EvaluationField evField, evField0n, evField0c, evField0g;
	private EvaluationField evField1n, evField1c, evField1g;
	private EnumerationField evField2n, evField2c, evField2g;
	
	/**
	 * Set up.
	 */
	private void setUnknownFields() {
		this.uFieldMV15 = new UnknownSimpleFieldMV15();
		this.uFieldMV2 = new UnknownSimpleFieldMV2();
	}
	
	/**
	 * Set up.
	 */
	private void setSimpleFieldsToIntegers() {
		this.sField = null;
		this.sField0n = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		this.sField0c = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.sField0g = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.sField1n = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.NONE);
		this.sField1c = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		this.sField1g = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		this.uField0 = new UnknownSimpleFieldMV15();
		this.uField1 = new UnknownSimpleFieldMV2();
		this.kField0 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.kField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setSimpleFieldsToReal() {
		this.sField = null;
		this.sField0n = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.NONE);
		this.sField0c = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.GAIN);
		this.sField0g = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.COST);
		this.sField1n = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.NONE);
		this.sField1c = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.GAIN);
		this.sField1g = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.COST);
		this.uField0 = new UnknownSimpleFieldMV15();
		this.uField1 = new UnknownSimpleFieldMV2();
		this.kField0 = RealFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.kField1 = RealFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
	}
	
	/**
	 * Set up.
	 */
	private void setSimpleFieldsToEnumeration() {
		ElementList domain1 = null;
		ElementList domain2 = null;
		
		String [] values1 = {"1", "2", "3", "4", "5"};
		String [] values2 = {"1", "2", "3"};
		
		try {
			domain1 = new ElementList(values1);
			domain2 = new ElementList(values2);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		this.sField = null;
		this.sField0n = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.NONE);
		this.sField0c = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.GAIN);
		this.sField0g = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.COST);
		this.sField1n = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.NONE);
		this.sField1c = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.GAIN);
		this.sField1g = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.COST);
		this.sField2n = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.NONE);
		this.sField2c = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.GAIN);
		this.sField2g = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.COST);
		this.uField0 = new UnknownSimpleFieldMV15();
		this.uField1 = new UnknownSimpleFieldMV2();
		this.kField0 = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.NONE);
		this.kField1 = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.NONE);
	}
	
	/**
	 * Set up.
	 */
	private void setIntegerFields() {
		this.iField = null;
		this.iField0n = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		this.iField0c = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.iField0g = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.iField1n = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.NONE);
		this.iField1c = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		this.iField1g = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setMoreIntegerFields() {
		this.iField = null;
		this.iField0n = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.NONE);
		this.iField0c = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		this.iField0g = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST);
		this.iField1n = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.NONE);
		this.iField1c = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		this.iField1g = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setRealFields() {
		this.rField = null;
		this.rField0n = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.NONE);
		this.rField0c = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.GAIN);
		this.rField0g = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.COST);
		this.rField1n = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.NONE);
		this.rField1c = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.GAIN);
		this.rField1g = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setEnumerationFields() {
		ElementList domain1 = null;
		ElementList domain2 = null;
		
		String [] values1 = {"1", "2", "3", "4", "5"};
		String [] values2 = {"1", "2", "3"};
		
		try {
			domain1 = new ElementList(values1);
			domain2 = new ElementList(values2);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		this.eField = null;
		this.eField0n = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.NONE);
		this.eField0c = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.GAIN);
		this.eField0g = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.COST);
		this.eField1n = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.NONE);
		this.eField1c = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.GAIN);
		this.eField1g = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.COST);
		this.eField2n = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.NONE);
		this.eField2c = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.GAIN);
		this.eField2g = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setEvaluationFieldsToIntegers() {
		this.evField = null;
		this.evField0n = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		this.evField0c = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.evField0g = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.evField1n = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.NONE);
		this.evField1c = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		this.evField1g = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		this.uField0 = new UnknownSimpleFieldMV15();
		this.uField1 = new UnknownSimpleFieldMV2();
		this.kField0 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.kField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setEvaluationFieldsToReal() {
		this.evField = null;
		this.evField0n = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.NONE);
		this.evField0c = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.GAIN);
		this.evField0g = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.COST);
		this.evField1n = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.NONE);
		this.evField1c = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.GAIN);
		this.evField1g = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.COST);
		this.uField0 = new UnknownSimpleFieldMV15();
		this.uField1 = new UnknownSimpleFieldMV2();
		this.kField0 = RealFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.kField1 = RealFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
	}
	
	/**
	 * Set up.
	 */
	private void setEvaluationFieldsToEnumeration() {
		ElementList domain1 = null;
		ElementList domain2 = null;
		
		String [] values1 = {"1", "2", "3", "4", "5"};
		String [] values2 = {"1", "2", "3"};
		
		try {
			domain1 = new ElementList(values1);
			domain2 = new ElementList(values2);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		this.evField = null;
		this.evField0n = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.NONE);
		this.evField0c = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.GAIN);
		this.evField0g = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.COST);
		this.evField1n = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.NONE);
		this.evField1c = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.GAIN);
		this.evField1g = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.COST);
		this.evField2n = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.NONE);
		this.evField2c = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.GAIN);
		this.evField2g = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.COST);
		this.uField0 = new UnknownSimpleFieldMV15();
		this.uField1 = new UnknownSimpleFieldMV2();
		this.kField0 = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.NONE);
		this.kField1 = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.NONE);
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(SimpleField, SimpleField)} on {@link IntegerField} type fields.
	 */
	@Test
	void testSimpleFieldAsIntegerCentralTendency() {
		this.setUnknownFields();
		this.setSimpleFieldsToIntegers();
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(sField0n, sField1n)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(sField0c, sField1c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(sField0g, sField1g)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(sField1n, sField0n)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(sField1c, sField0c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(sField1g, sField0g)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, sField1n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, sField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, sField0g)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, sField0n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(sField1n, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(sField1n, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(sField0g, uFieldMV15)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(sField0n, uFieldMV2)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(sField, sField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(sField0g, sField)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uField0, kField1)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(SimpleField, SimpleField)} on {@link RealField} type fields.
	 */
	@Test
	void testSimpleFieldAsRealCentralTendency() {
		this.setUnknownFields();
		this.setSimpleFieldsToReal();
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(sField0n, sField1n)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(sField0c, sField1c)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(sField0g, sField1g)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(sField1n, sField0n)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(sField1c, sField0c)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(sField1g, sField0g)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV15, sField1n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV2, sField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV15, sField0g)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV2, sField0n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(sField1n, uFieldMV15)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(sField1n, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(sField0g, uFieldMV15)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(sField0n, uFieldMV2)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(sField, sField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(sField0g, sField)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uField0, kField1)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(SimpleField, SimpleField)} on {@link EnumerationField} type fields.
	 */
	@Test
	void testSimpleFieldAsEnumerationCentralTendency() {
		this.setUnknownFields();
		this.setSimpleFieldsToEnumeration();
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField0n, sField1n)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField0c, sField1c)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField0g, sField1g)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField1n, sField0n)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField1c, sField0c)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField1g, sField0g)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV15, sField1n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV2, sField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV15, sField0g)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV2, sField0n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField1n, uFieldMV15)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField1n, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField0g, uFieldMV15)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField0n, uFieldMV2)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField, sField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(sField0g, sField)).getValue());
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(sField0n, sField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(sField0n, sField0c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(sField1n, sField0g)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(sField2c, sField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(sField0n, sField2n)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(sField1g, sField2g)));
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uField0, kField1)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(IntegerField, IntegerField)}.
	 */
	@Test
	void testIntegerFieldCentralTendency() {
		this.setUnknownFields();
		this.setIntegerFields();
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField0n, iField1n).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField0c, iField1c).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField0g, iField1g).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField1n, iField0n).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField1c, iField0c).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField1g, iField0g).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, iField1n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, iField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, iField0g)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, iField0n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(iField1n, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(iField1n, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(iField0g, uFieldMV15)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(iField0n, uFieldMV2)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(iField, iField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(iField0g, iField)).getValue());
		assertNull(((IntegerField)CentralTendencyCalculator.calculateMean(iField0n, iField1c)));
		assertNull(((IntegerField)CentralTendencyCalculator.calculateMean(iField0n, iField0c)));
		assertNull(((IntegerField)CentralTendencyCalculator.calculateMean(iField1n, iField0g)));
	}
	
	/**
	 * Another test method for {@link CentralTendencyCalculator#calculateMean(IntegerField, IntegerField)}.
	 */
	@Test
	void testMoreIntegerFieldCentralTendency() {
		this.setUnknownFields();
		this.setMoreIntegerFields();
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField0n, iField1n).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField0c, iField1c).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField0g, iField1g).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField1n, iField0n).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField1c, iField0c).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(iField1g, iField0g).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, iField1n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, iField1c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, iField0g)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, iField0n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(iField1n, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(iField1n, uFieldMV2)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(iField0g, uFieldMV15)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(iField0n, uFieldMV2)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(iField, iField1c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(iField0g, iField)).getValue());
		assertNull(((IntegerField)CentralTendencyCalculator.calculateMean(iField0n, iField1c)));
		assertNull(((IntegerField)CentralTendencyCalculator.calculateMean(iField0n, iField0c)));
		assertNull(((IntegerField)CentralTendencyCalculator.calculateMean(iField1n, iField0g)));
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(RealField, RealField)}.
	 */
	@Test
	void testRealFieldCentralTendency() {
		this.setUnknownFields();
		this.setRealFields();
		assertEquals(3.0, CentralTendencyCalculator.calculateMean(rField0n, rField1n).getValue());
		assertEquals(3.0, CentralTendencyCalculator.calculateMean(rField0c, rField1c).getValue());
		assertEquals(3.0, CentralTendencyCalculator.calculateMean(rField0g, rField1g).getValue());
		assertEquals(3.0, CentralTendencyCalculator.calculateMean(rField1n, rField0n).getValue());
		assertEquals(3.0, CentralTendencyCalculator.calculateMean(rField1c, rField0c).getValue());
		assertEquals(3.0, CentralTendencyCalculator.calculateMean(rField1g, rField0g).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV15, rField1n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV2, rField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV15, rField0g)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV2, rField0n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(rField1n, uFieldMV15)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(rField1n, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(rField0g, uFieldMV15)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(rField0n, uFieldMV2)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(rField, rField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(rField0g, rField)).getValue());
		assertNull(((RealField)CentralTendencyCalculator.calculateMean(rField0n, rField1c)));
		assertNull(((RealField)CentralTendencyCalculator.calculateMean(rField0n, rField0c)));
		assertNull(((RealField)CentralTendencyCalculator.calculateMean(rField1n, rField0g)));
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(EnumerationField, EnumerationField)}.
	 */
	@Test
	void testEnumerationFieldCentralTendency() {
		this.setUnknownFields();
		this.setEnumerationFields();
		assertEquals(3, CentralTendencyCalculator.calculateMean(eField0n, eField1n).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(eField0c, eField1c).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(eField0g, eField1g).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(eField1n, eField0n).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(eField1c, eField0c).getValue());
		assertEquals(3, CentralTendencyCalculator.calculateMean(eField1g, eField0g).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV15, eField1n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV2, eField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV15, eField0g)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV2, eField0n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(eField1n, uFieldMV15)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(eField1n, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(eField0g, uFieldMV15)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(eField0n, uFieldMV2)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(eField, eField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(eField0g, eField)).getValue());
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(eField0n, eField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(eField0n, eField0c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(eField1n, eField0g)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(eField2c, eField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(eField0n, eField2n)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(eField1g, eField2g)));
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(EvaluationField, EvaluationField)} on {@link IntegerField} type fields.
	 */
	@Test
	void testEvaluationFieldAsIntegerCentralTendency() {
		this.setUnknownFields();
		this.setEvaluationFieldsToIntegers();
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(evField0n, evField1n)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(evField0c, evField1c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(evField0g, evField1g)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(evField1n, evField0n)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(evField1c, evField0c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.calculateMean(evField1g, evField0g)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, evField1n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, evField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV15, evField0g)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uFieldMV2, evField0n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(evField1n, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(evField1n, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(evField0g, uFieldMV15)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(evField0n, uFieldMV2)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.calculateMean(evField, evField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(evField0g, evField)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(uField0, kField1)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.calculateMean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(EvaluationField, EvaluationField)} on {@link RealField} type fields.
	 */
	@Test
	void testEvaluationFieldAsRealCentralTendency() {
		this.setUnknownFields();
		this.setEvaluationFieldsToReal();
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(evField0n, evField1n)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(evField0c, evField1c)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(evField0g, evField1g)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(evField1n, evField0n)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(evField1c, evField0c)).getValue());
		assertEquals(3.0, ((RealField)CentralTendencyCalculator.calculateMean(evField1g, evField0g)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV15, evField1n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV2, evField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV15, evField0g)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uFieldMV2, evField0n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(evField1n, uFieldMV15)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(evField1n, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(evField0g, uFieldMV15)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(evField0n, uFieldMV2)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.calculateMean(evField, evField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(evField0g, evField)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(uField0, kField1)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.calculateMean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link CentralTendencyCalculator#calculateMean(EvaluationField, EvaluationField)} on {@link EnumerationField} type fields.
	 */
	@Test
	void testEvaluationFieldAsEnumerationCentralTendency() {
		this.setUnknownFields();
		this.setEvaluationFieldsToEnumeration();
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField0n, evField1n)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField0c, evField1c)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField0g, evField1g)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField1n, evField0n)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField1c, evField0c)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField1g, evField0g)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV15, evField1n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV2, evField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV15, evField0g)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uFieldMV2, evField0n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField1n, uFieldMV15)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField1n, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField0g, uFieldMV15)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField0n, uFieldMV2)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField, evField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(evField0g, evField)).getValue());
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(evField0n, evField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(evField0n, evField0c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(evField1n, evField0g)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(evField2c, evField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(evField0n, evField2n)));
		assertNull(((EnumerationField)CentralTendencyCalculator.calculateMean(evField1g, evField2g)));
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(uField0, kField1)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.calculateMean(kField0, uField1)).getValue());
	}

}
