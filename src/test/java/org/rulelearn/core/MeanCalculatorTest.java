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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		this.sField0g = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.sField0c = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.sField1n = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.NONE);
		this.sField1g = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		this.sField1c = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
		this.kField0 = RealFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.kField1 = RealFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setSimpleFieldsToReal() {
		this.sField = null;
		this.sField0n = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.NONE);
		this.sField0g = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.GAIN);
		this.sField0c = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.COST);
		this.sField1n = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.NONE);
		this.sField1g = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.GAIN);
		this.sField1c = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.COST);
		this.kField0 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.kField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setSimpleFieldsToEnumeration() {
		ElementList domain1 = null;
		
		String [] values1 = {"1", "2", "3", "4", "5"};
		
		try {
			domain1 = new ElementList(values1);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		this.sField = null;
		this.sField0n = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.NONE);
		this.sField0g = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.GAIN);
		this.sField0c = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.COST);
		this.sField1n = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.NONE);
		this.sField1g = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.GAIN);
		this.sField1c = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.COST);
		this.kField0 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.kField1 = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setIntegerFields() {
		this.iField = null;
		this.iField0n = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.NONE);
		this.iField0g = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.GAIN);
		this.iField0c = IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST);
		this.iField1n = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.NONE);
		this.iField1g = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		this.iField1c = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setMoreIntegerFields() {
		this.iField = null;
		this.iField0n = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.NONE);
		this.iField0g = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN);
		this.iField0c = IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.COST);
		this.iField1n = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.NONE);
		this.iField1g = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.GAIN);
		this.iField1c = IntegerFieldFactory.getInstance().create(4, AttributePreferenceType.COST);
	}
	
	/**
	 * Set up.
	 */
	private void setRealFields() {
		this.rField = null;
		this.rField0n = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.NONE);
		this.rField0g = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.GAIN);
		this.rField0c = RealFieldFactory.getInstance().create(2.0, AttributePreferenceType.COST);
		this.rField1n = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.NONE);
		this.rField1g = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.GAIN);
		this.rField1c = RealFieldFactory.getInstance().create(4.0, AttributePreferenceType.COST);
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
		this.eField0g = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.GAIN);
		this.eField0c = EnumerationFieldFactory.getInstance().create(domain1, 2, AttributePreferenceType.COST);
		this.eField1n = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.NONE);
		this.eField1g = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.GAIN);
		this.eField1c = EnumerationFieldFactory.getInstance().create(domain1, 4, AttributePreferenceType.COST);
		this.eField2n = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.NONE);
		this.eField2g = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.GAIN);
		this.eField2c = EnumerationFieldFactory.getInstance().create(domain2, 2, AttributePreferenceType.COST);
	}
	
	
	/**
	 * Test method for {@link MeanCalculator#calculate(IntegerField, SimpleField)} on {@link SimpleField} type fields.
	 */
	@Test
	void testSimpleFieldAsIntegerMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setSimpleFieldsToIntegers();
		assertThrows(NullPointerException.class, () -> {
			sField0n.calculate(calculator, sField);
		});
		assertThrows(ClassCastException.class, () -> {
			sField0n.calculate(calculator, kField0);
		});
		assertEquals(2, ((IntegerField)sField0n.calculate(calculator, sField0n)).getValue());
		assertEquals(3, ((IntegerField)sField0n.calculate(calculator, sField1n)).getValue());
		assertEquals(3, ((IntegerField)sField0c.calculate(calculator, sField1c)).getValue());
		assertEquals(3, ((IntegerField)sField0g.calculate(calculator, sField1g)).getValue());
		assertEquals(3, ((IntegerField)sField1n.calculate(calculator, sField0n)).getValue());
		assertEquals(3, ((IntegerField)sField1c.calculate(calculator, sField0c)).getValue());
		assertEquals(3, ((IntegerField)sField1g.calculate(calculator, sField0g)).getValue());
		assertEquals(3, ((IntegerField)sField0n.calculate(calculator, sField1c)).getValue());
		assertEquals(3, ((IntegerField)sField0c.calculate(calculator, sField1c)).getValue());
		assertEquals(3, ((IntegerField)sField0g.calculate(calculator, sField1n)).getValue());
		assertEquals(3, ((IntegerField)sField1n.calculate(calculator, sField0c)).getValue());
		assertEquals(3, ((IntegerField)sField1c.calculate(calculator, sField0g)).getValue());
		assertEquals(3, ((IntegerField)sField1g.calculate(calculator, sField0n)).getValue());
		assertEquals(4, ((IntegerField)sField1g.calculate(calculator, sField1c)).getValue());
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, sField1n));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, sField1c));
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, sField0g));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, sField0n));
		assertEquals(uFieldMV15, sField1n.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, sField1c.calculate(calculator, uFieldMV2));
		assertEquals(uFieldMV15, sField0g.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, sField0n.calculate(calculator, uFieldMV2));
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(RealField, SimpleField)} on {@link SimpleField} type fields.
	 */
	@Test
	void testSimpleFieldAsRealMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setSimpleFieldsToReal();
		assertThrows(NullPointerException.class, () -> {
			sField0c.calculate(calculator, sField);
		});
		assertThrows(ClassCastException.class, () -> {
			sField0c.calculate(calculator, kField1);
		});
		assertEquals(2.0, ((RealField)sField0n.calculate(calculator, sField0n)).getValue());
		assertEquals(3.0, ((RealField)sField0n.calculate(calculator, sField1n)).getValue());
		assertEquals(3.0, ((RealField)sField0c.calculate(calculator, sField1c)).getValue());
		assertEquals(3.0, ((RealField)sField0g.calculate(calculator, sField1g)).getValue());
		assertEquals(3.0, ((RealField)sField1n.calculate(calculator, sField0n)).getValue());
		assertEquals(3.0, ((RealField)sField1c.calculate(calculator, sField0c)).getValue());
		assertEquals(3.0, ((RealField)sField1g.calculate(calculator, sField0g)).getValue());
		assertEquals(3.0, ((RealField)sField0n.calculate(calculator, sField1c)).getValue());
		assertEquals(3.0, ((RealField)sField0c.calculate(calculator, sField1n)).getValue());
		assertEquals(3.0, ((RealField)sField0g.calculate(calculator, sField1n)).getValue());
		assertEquals(3.0, ((RealField)sField1n.calculate(calculator, sField0c)).getValue());
		assertEquals(3.0, ((RealField)sField1c.calculate(calculator, sField0g)).getValue());
		assertEquals(3.0, ((RealField)sField1g.calculate(calculator, sField0n)).getValue());
		assertEquals(4.0, ((RealField)sField1g.calculate(calculator, sField1n)).getValue());
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, sField1n));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, sField1c));
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, sField0g));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, sField0n));
		assertEquals(uFieldMV15, sField1n.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, sField1c.calculate(calculator, uFieldMV2));
		assertEquals(uFieldMV15, sField0g.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, sField0n.calculate(calculator, uFieldMV2));
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(EnumerationField, SimpleField)} on {@link SimpleField} type fields.
	 */
	@Test
	void testSimpleFieldAsEnumerationMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setSimpleFieldsToEnumeration();
		assertThrows(NullPointerException.class, () -> {
			sField0g.calculate(calculator, sField);
		});
		assertThrows(ClassCastException.class, () -> {
			sField0g.calculate(calculator, kField0);
		});
		assertEquals(2, ((EnumerationField)sField0n.calculate(calculator, sField0n)).getValue());
		assertEquals(3, ((EnumerationField)sField0n.calculate(calculator, sField1n)).getValue());
		assertEquals(3, ((EnumerationField)sField0c.calculate(calculator, sField1c)).getValue());
		assertEquals(3, ((EnumerationField)sField0g.calculate(calculator, sField1g)).getValue());
		assertEquals(3, ((EnumerationField)sField1n.calculate(calculator, sField0n)).getValue());
		assertEquals(3, ((EnumerationField)sField1c.calculate(calculator, sField0c)).getValue());
		assertEquals(3, ((EnumerationField)sField1g.calculate(calculator, sField0g)).getValue());
		assertEquals(3, ((EnumerationField)sField0n.calculate(calculator, sField1g)).getValue());
		assertEquals(3, ((EnumerationField)sField0c.calculate(calculator, sField1n)).getValue());
		assertEquals(3, ((EnumerationField)sField0g.calculate(calculator, sField1n)).getValue());
		assertEquals(3, ((EnumerationField)sField1n.calculate(calculator, sField0g)).getValue());
		assertEquals(3, ((EnumerationField)sField1c.calculate(calculator, sField0g)).getValue());
		assertEquals(3, ((EnumerationField)sField1g.calculate(calculator, sField0c)).getValue());
		assertEquals(4, ((EnumerationField)sField1g.calculate(calculator, sField1n)).getValue());
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, sField1n));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, sField1c));
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, sField0g));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, sField0n));
		assertEquals(uFieldMV15, sField1n.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, sField1c.calculate(calculator, uFieldMV2));
		assertEquals(uFieldMV15, sField0g.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, sField0n.calculate(calculator, uFieldMV2));
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(IntegerField, SimpleField)}.
	 */
	@Test
	void testIntegerFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setIntegerFields();
		assertThrows(NullPointerException.class, () -> {
			iField0c.calculate(calculator, iField);
		});
		assertEquals(3, ((IntegerField)iField0n.calculate(calculator, iField1n)).getValue());
		assertEquals(3, ((IntegerField)iField0c.calculate(calculator, iField1c)).getValue());
		assertEquals(3, ((IntegerField)iField0g.calculate(calculator, iField1g)).getValue());
		assertEquals(3, ((IntegerField)iField1n.calculate(calculator, iField0n)).getValue());
		assertEquals(3, ((IntegerField)iField1c.calculate(calculator, iField0c)).getValue());
		assertEquals(3, ((IntegerField)iField1g.calculate(calculator, iField0g)).getValue());
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, iField1n));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, iField1c));
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, iField0g));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, iField0n));
		assertEquals(uFieldMV15, iField1n.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, iField1n.calculate(calculator, uFieldMV2));
		assertEquals(uFieldMV15, iField0g.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, iField0n.calculate(calculator, uFieldMV2));
	}
	
	/**
	 * Another test method for {@link MeanCalculator#calculate(IntegerField, SimpleField)}.
	 */
	@Test
	void testMoreIntegerFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setMoreIntegerFields();
		assertEquals(3, ((IntegerField)iField0n.calculate(calculator, iField1n)).getValue());
		assertEquals(3, ((IntegerField)iField0c.calculate(calculator, iField1c)).getValue());
		assertEquals(3, ((IntegerField)iField0g.calculate(calculator, iField1g)).getValue());
		assertEquals(3, ((IntegerField)iField1n.calculate(calculator, iField0n)).getValue());
		assertEquals(3, ((IntegerField)iField1c.calculate(calculator, iField0c)).getValue());
		assertEquals(3, ((IntegerField)iField1g.calculate(calculator, iField0g)).getValue());
	}

	/**
	 * Test method for {@link MeanCalculator#calculate(RealField, SimpleField)}.
	 */
	@Test
	void testRealFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setRealFields();
		assertThrows(NullPointerException.class, () -> {
			rField0g.calculate(calculator, rField);
		});
		assertEquals(3.0, ((RealField)rField0n.calculate(calculator, rField1n)).getValue());
		assertEquals(3.0, ((RealField)rField0c.calculate(calculator, rField1c)).getValue());
		assertEquals(3.0, ((RealField)rField0g.calculate(calculator, rField1g)).getValue());
		assertEquals(3.0, ((RealField)rField1n.calculate(calculator, rField0n)).getValue());
		assertEquals(3.0, ((RealField)rField1c.calculate(calculator, rField0c)).getValue());
		assertEquals(3.0, ((RealField)rField1g.calculate(calculator, rField0g)).getValue());
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, rField1n));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, rField1c));
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, rField0g));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, rField0n));
		assertEquals(uFieldMV15, rField1n.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, rField1n.calculate(calculator, uFieldMV2));
		assertEquals(uFieldMV15, rField0g.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, rField0n.calculate(calculator, uFieldMV2));
	}
	
	/**
	 * Test method for {@link MeanCalculator#calculate(EnumerationField, SimpleField)}.
	 */
	@Test
	void testEnumerationFieldMeanCalculator() {
		this.setCalculator();
		this.setUnknownFields();
		this.setEnumerationFields();
		assertThrows(NullPointerException.class, () -> {
			eField0n.calculate(calculator, eField);
		});
		assertThrows(InvalidValueException.class, () -> {
			eField0n.calculate(calculator, eField2n);
		});
		assertThrows(InvalidValueException.class, () -> {
			eField0c.calculate(calculator, eField2g);
		});
		assertThrows(InvalidValueException.class, () -> {
			eField0g.calculate(calculator, eField2c);
		});
		assertEquals(3, ((EnumerationField)eField0n.calculate(calculator, eField1n)).getValue());
		assertEquals(3, ((EnumerationField)eField0c.calculate(calculator, eField1c)).getValue());
		assertEquals(3, ((EnumerationField)eField0g.calculate(calculator, eField1g)).getValue());
		assertEquals(3, ((EnumerationField)eField1n.calculate(calculator, eField0n)).getValue());
		assertEquals(3, ((EnumerationField)eField1c.calculate(calculator, eField0c)).getValue());
		assertEquals(3, ((EnumerationField)eField1g.calculate(calculator, eField0g)).getValue());
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, eField1n));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, eField1c));
		assertEquals(uFieldMV15, uFieldMV15.calculate(calculator, eField0g));
		assertEquals(uFieldMV2, uFieldMV2.calculate(calculator, eField0n));
		assertEquals(uFieldMV15, eField1n.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, eField1n.calculate(calculator, uFieldMV2));
		assertEquals(uFieldMV15, eField0g.calculate(calculator, uFieldMV15));
		assertEquals(uFieldMV2, eField0n.calculate(calculator, uFieldMV2));
	}
	
}
