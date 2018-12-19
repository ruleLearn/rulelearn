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
 * Tests for {@link MeanCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class MeanCalculatorTest {

	private MeanCalculator calculator;
	
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
		
	/**
	 * Set up calculator.
	 */
	private void setCalculator() {
		calculator = new MeanCalculator();
	}
	
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
	 * Test method for {@link MeanCalculator#calculate(IntegerField, SimpleField)} on {@link SimpleField} type fields.
	 */
	@Test
	void testSimpleFieldAsIntegerMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setSimpleFieldsToIntegers();
		assertEquals(3, ((IntegerField)sField0n.getMean(calculator, sField1n)).getValue());
		assertEquals(3, ((IntegerField)sField0c.getMean(calculator, sField1c)).getValue());
		assertEquals(3, ((IntegerField)sField0g.getMean(calculator, sField1g)).getValue());
		assertEquals(3, ((IntegerField)sField1n.getMean(calculator, sField0n)).getValue());
		assertEquals(3, ((IntegerField)sField1c.getMean(calculator, sField0c)).getValue());
		assertEquals(3, ((IntegerField)sField1g.getMean(calculator, sField0g)).getValue());
		assertEquals(4, ((IntegerField)uFieldMV15.getMean(calculator, sField1n)).getValue());
		assertEquals(4, ((IntegerField)uFieldMV2.getMean(calculator, sField1c)).getValue());
		assertEquals(2, ((IntegerField)uFieldMV15.getMean(calculator, sField0g)).getValue());
		assertEquals(2, ((IntegerField)uFieldMV2.getMean(calculator, sField0n)).getValue());
		assertEquals(4, ((IntegerField)sField1n.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)sField1n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)sField0g.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(2, ((IntegerField)sField0n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)sField0g.getMean(calculator, sField)).getValue());
		assertEquals(2, ((IntegerField)uField0.getMean(calculator, kField1)).getValue());
		assertEquals(2, ((IntegerField)kField0.getMean(calculator, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(RealField, SimpleField)} on {@link SimpleField} type fields.
	 */
	@Test
	void testSimpleFieldAsRealMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setSimpleFieldsToReal();
		assertEquals(3.0, ((RealField)sField0n.getMean(calculator, sField1n)).getValue());
		assertEquals(3.0, ((RealField)sField0c.getMean(calculator, sField1c)).getValue());
		assertEquals(3.0, ((RealField)sField0g.getMean(calculator, sField1g)).getValue());
		assertEquals(3.0, ((RealField)sField1n.getMean(calculator, sField0n)).getValue());
		assertEquals(3.0, ((RealField)sField1c.getMean(calculator, sField0c)).getValue());
		assertEquals(3.0, ((RealField)sField1g.getMean(calculator, sField0g)).getValue());
		assertEquals(4.0, ((RealField)uFieldMV15.getMean(calculator, sField1n)).getValue());
		assertEquals(4.0, ((RealField)uFieldMV2.getMean(calculator, sField1c)).getValue());
		assertEquals(2.0, ((RealField)uFieldMV15.getMean(calculator, sField0g)).getValue());
		assertEquals(2.0, ((RealField)uFieldMV2.getMean(calculator, sField0n)).getValue());
		assertEquals(4.0, ((RealField)sField1n.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(4.0, ((RealField)sField1n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)sField0g.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(2.0, ((RealField)sField0n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)sField0g.getMean(calculator, sField)).getValue());
		assertEquals(2.0, ((RealField)uField0.getMean(calculator, kField1)).getValue());
		assertEquals(2.0, ((RealField)kField0.getMean(calculator, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(EnumerationField, SimpleField)} on {@link SimpleField} type fields.
	 */
	@Test
	void testSimpleFieldAsEnumerationMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setSimpleFieldsToEnumeration();
		assertEquals(3, ((EnumerationField)sField0n.getMean(calculator, sField1n)).getValue());
		assertEquals(3, ((EnumerationField)sField0c.getMean(calculator, sField1c)).getValue());
		assertEquals(3, ((EnumerationField)sField0g.getMean(calculator, sField1g)).getValue());
		assertEquals(3, ((EnumerationField)sField1n.getMean(calculator, sField0n)).getValue());
		assertEquals(3, ((EnumerationField)sField1c.getMean(calculator, sField0c)).getValue());
		assertEquals(3, ((EnumerationField)sField1g.getMean(calculator, sField0g)).getValue());
		assertEquals(4, ((EnumerationField)uFieldMV15.getMean(calculator, sField1n)).getValue());
		assertEquals(4, ((EnumerationField)uFieldMV2.getMean(calculator, sField1c)).getValue());
		assertEquals(2, ((EnumerationField)uFieldMV15.getMean(calculator, sField0g)).getValue());
		assertEquals(2, ((EnumerationField)uFieldMV2.getMean(calculator, sField0n)).getValue());
		assertEquals(4, ((EnumerationField)sField1n.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(4, ((EnumerationField)sField1n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)sField0g.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(2, ((EnumerationField)sField0n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)sField0g.getMean(calculator, sField)).getValue());
		assertNull(((EnumerationField)sField0n.getMean(calculator, sField1c)));
		assertNull(((EnumerationField)sField0n.getMean(calculator, sField0c)));
		assertNull(((EnumerationField)sField1n.getMean(calculator, sField0g)));
		assertNull(((EnumerationField)sField2c.getMean(calculator, sField1c)));
		assertNull(((EnumerationField)sField0n.getMean(calculator, sField2n)));
		assertNull(((EnumerationField)sField1g.getMean(calculator, sField2g)));
		assertEquals(2, ((EnumerationField)uField0.getMean(calculator, kField1)).getValue());
		assertEquals(2, ((EnumerationField)kField0.getMean(calculator, uField1)).getValue());
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(IntegerField, SimpleField)}.
	 */
	@Test
	void testIntegerFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setIntegerFields();
		assertEquals(3, ((IntegerField)iField0n.getMean(calculator, iField1n)).getValue());
		assertEquals(3, ((IntegerField)iField0c.getMean(calculator, iField1c)).getValue());
		assertEquals(3, ((IntegerField)iField0g.getMean(calculator, iField1g)).getValue());
		assertEquals(3, ((IntegerField)iField1n.getMean(calculator, iField0n)).getValue());
		assertEquals(3, ((IntegerField)iField1c.getMean(calculator, iField0c)).getValue());
		assertEquals(3, ((IntegerField)iField1g.getMean(calculator, iField0g)).getValue());
		assertEquals(4, ((IntegerField)uFieldMV15.getMean(calculator, iField1n)).getValue());
		assertEquals(4, ((IntegerField)uFieldMV2.getMean(calculator, iField1c)).getValue());
		assertEquals(2, ((IntegerField)uFieldMV15.getMean(calculator, iField0g)).getValue());
		assertEquals(2, ((IntegerField)uFieldMV2.getMean(calculator, iField0n)).getValue());
		assertEquals(4, ((IntegerField)iField1n.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)iField1n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)iField0g.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(2, ((IntegerField)iField0n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)iField0g.getMean(calculator, iField)).getValue());
		assertNull(((IntegerField)iField0n.getMean(calculator, iField1c)));
		assertNull(((IntegerField)iField0n.getMean(calculator, iField0c)));
		assertNull(((IntegerField)iField1n.getMean(calculator, iField0g)));
	}
	
	/**
	 * Another test method for {@link MeanCalculator#calculate(IntegerField, SimpleField)}.
	 */
	@Test
	void testMoreIntegerFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setMoreIntegerFields();
		assertEquals(3, ((IntegerField)iField0n.getMean(calculator, iField1n)).getValue());
		assertEquals(3, ((IntegerField)iField0c.getMean(calculator, iField1c)).getValue());
		assertEquals(3, ((IntegerField)iField0g.getMean(calculator, iField1g)).getValue());
		assertEquals(3, ((IntegerField)iField1n.getMean(calculator, iField0n)).getValue());
		assertEquals(3, ((IntegerField)iField1c.getMean(calculator, iField0c)).getValue());
		assertEquals(3, ((IntegerField)iField1g.getMean(calculator, iField0g)).getValue());
		assertEquals(4, ((IntegerField)uFieldMV15.getMean(calculator, iField1n)).getValue());
		assertEquals(4, ((IntegerField)uFieldMV2.getMean(calculator, iField1c)).getValue());
		assertEquals(3, ((IntegerField)uFieldMV15.getMean(calculator, iField0g)).getValue());
		assertEquals(3, ((IntegerField)uFieldMV2.getMean(calculator, iField0n)).getValue());
		assertEquals(4, ((IntegerField)iField1n.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)iField1n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(3, ((IntegerField)iField0g.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(3, ((IntegerField)iField0n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(3, ((IntegerField)iField0g.getMean(calculator, iField)).getValue());
		assertNull(((IntegerField)iField0n.getMean(calculator, iField1c)));
		assertNull(((IntegerField)iField0n.getMean(calculator, iField0c)));
		assertNull(((IntegerField)iField1n.getMean(calculator, iField0g)));
	}

	/**
	 * Test method for {@link MeanCalculator#calculate(RealField, SimpleField)}.
	 */
	@Test
	void testRealFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setRealFields();
		assertEquals(3.0, ((RealField)rField0n.getMean(calculator, rField1n)).getValue());
		assertEquals(3.0, ((RealField)rField0c.getMean(calculator, rField1c)).getValue());
		assertEquals(3.0, ((RealField)rField0g.getMean(calculator, rField1g)).getValue());
		assertEquals(3.0, ((RealField)rField1n.getMean(calculator, rField0n)).getValue());
		assertEquals(3.0, ((RealField)rField1c.getMean(calculator, rField0c)).getValue());
		assertEquals(3.0, ((RealField)rField1g.getMean(calculator, rField0g)).getValue());
		assertEquals(4.0, ((RealField)uFieldMV15.getMean(calculator, rField1n)).getValue());
		assertEquals(4.0, ((RealField)uFieldMV2.getMean(calculator, rField1c)).getValue());
		assertEquals(2.0, ((RealField)uFieldMV15.getMean(calculator, rField0g)).getValue());
		assertEquals(2.0, ((RealField)uFieldMV2.getMean(calculator, rField0n)).getValue());
		assertEquals(4.0, ((RealField)rField1n.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(4.0, ((RealField)rField1n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)rField0g.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(2.0, ((RealField)rField0n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)rField0g.getMean(calculator, rField)).getValue());
		assertNull(((RealField)rField0n.getMean(calculator, rField1c)));
		assertNull(((RealField)rField0n.getMean(calculator, rField0c)));
		assertNull(((RealField)rField1n.getMean(calculator, rField0g)));
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(EnumerationField, SimpleField)}.
	 */
	@Test
	void testEnumerationFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setEnumerationFields();
		assertEquals(3, ((EnumerationField)eField0n.getMean(calculator, eField1n)).getValue());
		assertEquals(3, ((EnumerationField)eField0c.getMean(calculator, eField1c)).getValue());
		assertEquals(3, ((EnumerationField)eField0g.getMean(calculator, eField1g)).getValue());
		assertEquals(3, ((EnumerationField)eField1n.getMean(calculator, eField0n)).getValue());
		assertEquals(3, ((EnumerationField)eField1c.getMean(calculator, eField0c)).getValue());
		assertEquals(3, ((EnumerationField)eField1g.getMean(calculator, eField0g)).getValue());
		assertEquals(4, ((EnumerationField)uFieldMV15.getMean(calculator, eField1n)).getValue());
		assertEquals(4, ((EnumerationField)uFieldMV2.getMean(calculator, eField1c)).getValue());
		assertEquals(2, ((EnumerationField)uFieldMV15.getMean(calculator, eField0g)).getValue());
		assertEquals(2, ((EnumerationField)uFieldMV2.getMean(calculator, eField0n)).getValue());
		assertEquals(4, ((EnumerationField)eField1n.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(4, ((EnumerationField)eField1n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)eField0g.getMean(calculator, uFieldMV15)).getValue());
		assertEquals(2, ((EnumerationField)eField0n.getMean(calculator, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)eField0g.getMean(calculator, eField)).getValue());
		assertNull(((EnumerationField)eField0n.getMean(calculator, eField1c)));
		assertNull(((EnumerationField)eField0n.getMean(calculator, eField0c)));
		assertNull(((EnumerationField)eField1n.getMean(calculator, eField0g)));
		assertNull(((EnumerationField)eField2c.getMean(calculator, eField1c)));
		assertNull(((EnumerationField)eField0n.getMean(calculator, eField2n)));
		assertNull(((EnumerationField)eField1g.getMean(calculator, eField2g)));
	}
	
}
