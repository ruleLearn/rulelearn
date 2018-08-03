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

import static org.junit.jupiter.api.Assertions.*;

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
 * Tests for {@link CentralTendencyCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CentralTendencyTest {

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
	 * Test {@link CentralTendencyCalculator#mean(SimpleField, SimpleField)} for {@link IntegerField} type fields.
	 */
	@Test
	void testSimpleFieldAsIntegerCentralTendency() {
		this.setUnknownFields();
		this.setSimpleFieldsToIntegers();
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.mean(sField0n, sField1n)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.mean(sField0c, sField1c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.mean(sField0g, sField1g)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.mean(sField1n, sField0n)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.mean(sField1c, sField0c)).getValue());
		assertEquals(3, ((IntegerField)CentralTendencyCalculator.mean(sField1g, sField0g)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV15, sField1n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV2, sField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV15, sField0g)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV2, sField0n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(sField1n, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(sField1n, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(sField0g, uFieldMV15)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(sField0n, uFieldMV2)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(sField, sField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(sField0g, sField)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(uField0, kField1)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test {@link CentralTendencyCalculator#mean(SimpleField, SimpleField)} for {@link RealField} type fields.
	 */
	@Test
	void testSimpleFieldAsRealCentralTendency() {
		this.setUnknownFields();
		this.setSimpleFieldsToReal();
		assertEquals(3, ((RealField)CentralTendencyCalculator.mean(sField0n, sField1n)).getValue());
		assertEquals(3, ((RealField)CentralTendencyCalculator.mean(sField0c, sField1c)).getValue());
		assertEquals(3, ((RealField)CentralTendencyCalculator.mean(sField0g, sField1g)).getValue());
		assertEquals(3, ((RealField)CentralTendencyCalculator.mean(sField1n, sField0n)).getValue());
		assertEquals(3, ((RealField)CentralTendencyCalculator.mean(sField1c, sField0c)).getValue());
		assertEquals(3, ((RealField)CentralTendencyCalculator.mean(sField1g, sField0g)).getValue());
		assertEquals(4, ((RealField)CentralTendencyCalculator.mean(uFieldMV15, sField1n)).getValue());
		assertEquals(4, ((RealField)CentralTendencyCalculator.mean(uFieldMV2, sField1c)).getValue());
		assertEquals(2, ((RealField)CentralTendencyCalculator.mean(uFieldMV15, sField0g)).getValue());
		assertEquals(2, ((RealField)CentralTendencyCalculator.mean(uFieldMV2, sField0n)).getValue());
		assertEquals(4, ((RealField)CentralTendencyCalculator.mean(sField1n, uFieldMV15)).getValue());
		assertEquals(4, ((RealField)CentralTendencyCalculator.mean(sField1n, uFieldMV2)).getValue());
		assertEquals(2, ((RealField)CentralTendencyCalculator.mean(sField0g, uFieldMV15)).getValue());
		assertEquals(2, ((RealField)CentralTendencyCalculator.mean(sField0n, uFieldMV2)).getValue());
		assertEquals(4, ((RealField)CentralTendencyCalculator.mean(sField, sField1c)).getValue());
		assertEquals(2, ((RealField)CentralTendencyCalculator.mean(sField0g, sField)).getValue());
		assertEquals(2, ((RealField)CentralTendencyCalculator.mean(uField0, kField1)).getValue());
		assertEquals(2, ((RealField)CentralTendencyCalculator.mean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test {@link CentralTendencyCalculator#mean(SimpleField, SimpleField)} for {@link EnumerationField} type fields.
	 */
	@Test
	void testSimpleFieldAsEnumerationCentralTendency() {
		this.setUnknownFields();
		this.setSimpleFieldsToEnumeration();
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.mean(sField0n, sField1n)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.mean(sField0c, sField1c)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.mean(sField0g, sField1g)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.mean(sField1n, sField0n)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.mean(sField1c, sField0c)).getValue());
		assertEquals(3, ((EnumerationField)CentralTendencyCalculator.mean(sField1g, sField0g)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV15, sField1n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV2, sField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV15, sField0g)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV2, sField0n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(sField1n, uFieldMV15)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(sField1n, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(sField0g, uFieldMV15)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(sField0n, uFieldMV2)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(sField, sField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(sField0g, sField)).getValue());
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(sField0n, sField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(sField0n, sField0c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(sField1n, sField0g)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(sField2c, sField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(sField0n, sField2n)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(sField1g, sField2g)));
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(uField0, kField1)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(kField0, uField1)).getValue());
	}
	
	/**
	 * Test {@link CentralTendencyCalculator#mean(IntegerField, IntegerField)}.
	 */
	@Test
	void testIntegerFieldCentralTendency() {
		this.setUnknownFields();
		this.setIntegerFields();
		assertEquals(3, CentralTendencyCalculator.mean(iField0n, iField1n).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(iField0c, iField1c).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(iField0g, iField1g).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(iField1n, iField0n).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(iField1c, iField0c).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(iField1g, iField0g).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV15, iField1n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV2, iField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV15, iField0g)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(uFieldMV2, iField0n)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(iField1n, uFieldMV15)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(iField1n, uFieldMV2)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(iField0g, uFieldMV15)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(iField0n, uFieldMV2)).getValue());
		assertEquals(4, ((IntegerField)CentralTendencyCalculator.mean(iField, iField1c)).getValue());
		assertEquals(2, ((IntegerField)CentralTendencyCalculator.mean(iField0g, iField)).getValue());
		assertNull(((IntegerField)CentralTendencyCalculator.mean(iField0n, iField1c)));
		assertNull(((IntegerField)CentralTendencyCalculator.mean(iField0n, iField0c)));
		assertNull(((IntegerField)CentralTendencyCalculator.mean(iField1n, iField0g)));
	}
	
	/**
	 * Test {@link CentralTendencyCalculator#mean(RealField, RealField)}.
	 */
	@Test
	void testRealFieldCentralTendency() {
		this.setUnknownFields();
		this.setRealFields();
		assertEquals(3.0, CentralTendencyCalculator.mean(rField0n, rField1n).getValue());
		assertEquals(3.0, CentralTendencyCalculator.mean(rField0c, rField1c).getValue());
		assertEquals(3.0, CentralTendencyCalculator.mean(rField0g, rField1g).getValue());
		assertEquals(3.0, CentralTendencyCalculator.mean(rField1n, rField0n).getValue());
		assertEquals(3.0, CentralTendencyCalculator.mean(rField1c, rField0c).getValue());
		assertEquals(3.0, CentralTendencyCalculator.mean(rField1g, rField0g).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.mean(uFieldMV15, rField1n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.mean(uFieldMV2, rField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.mean(uFieldMV15, rField0g)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.mean(uFieldMV2, rField0n)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.mean(rField1n, uFieldMV15)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.mean(rField1n, uFieldMV2)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.mean(rField0g, uFieldMV15)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.mean(rField0n, uFieldMV2)).getValue());
		assertEquals(4.0, ((RealField)CentralTendencyCalculator.mean(rField, rField1c)).getValue());
		assertEquals(2.0, ((RealField)CentralTendencyCalculator.mean(rField0g, rField)).getValue());
		assertNull(((RealField)CentralTendencyCalculator.mean(rField0n, rField1c)));
		assertNull(((RealField)CentralTendencyCalculator.mean(rField0n, rField0c)));
		assertNull(((RealField)CentralTendencyCalculator.mean(rField1n, rField0g)));
	}
	
	/**
	 * Test {@link CentralTendencyCalculator#mean(EnumerationField, EnumerationField)}.
	 */
	@Test
	void testEnumerationFieldCentralTendency() {
		this.setUnknownFields();
		this.setEnumerationFields();
		assertEquals(3, CentralTendencyCalculator.mean(eField0n, eField1n).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(eField0c, eField1c).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(eField0g, eField1g).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(eField1n, eField0n).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(eField1c, eField0c).getValue());
		assertEquals(3, CentralTendencyCalculator.mean(eField1g, eField0g).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV15, eField1n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV2, eField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV15, eField0g)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(uFieldMV2, eField0n)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(eField1n, uFieldMV15)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(eField1n, uFieldMV2)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(eField0g, uFieldMV15)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(eField0n, uFieldMV2)).getValue());
		assertEquals(4, ((EnumerationField)CentralTendencyCalculator.mean(eField, eField1c)).getValue());
		assertEquals(2, ((EnumerationField)CentralTendencyCalculator.mean(eField0g, eField)).getValue());
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(eField0n, eField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(eField0n, eField0c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(eField1n, eField0g)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(eField2c, eField1c)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(eField0n, eField2n)));
		assertNull(((EnumerationField)CentralTendencyCalculator.mean(eField1g, eField2g)));
	}

}
