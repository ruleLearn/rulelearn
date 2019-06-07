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

package org.rulelearn.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldCachingFactory;
import org.rulelearn.types.RealFieldFactory;

/**
 * Test for {@link RealFieldCachingFactory} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RealFieldCachingFactoryTest {

	@BeforeEach
	void setUp() {
		RealFieldCachingFactory.getInstance().clearVolatileCache();
	}

	/**
	 * Test for {@link RealFieldCachingFactory#create(double, AttributePreferenceType, boolean)}.
	 * Tests persistent cache.
	 */
	@Test
	void testCreate01() {
		RealFieldCachingFactory factory = RealFieldCachingFactory.getInstance();
		
		RealField field1 = factory.create(1.0, AttributePreferenceType.GAIN, true);
		RealField field1a = factory.create(1.0, AttributePreferenceType.GAIN, true);
		RealField field2 = factory.create(1.0, AttributePreferenceType.COST, true);
		RealField field2a = factory.create(1.0, AttributePreferenceType.COST, true);
		RealField field3 = factory.create(1, AttributePreferenceType.NONE, true);
		RealField field3a = factory.create(1, AttributePreferenceType.NONE, true);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#create(double, AttributePreferenceType, boolean)}.
	 * Tests volatile cache.
	 */
	@Test
	void testCreate02() {
		RealFieldCachingFactory factory = RealFieldCachingFactory.getInstance();
		
		RealField field1 = factory.create(1, AttributePreferenceType.GAIN, false);
		RealField field1a = factory.create(1, AttributePreferenceType.GAIN, false);
		RealField field2 = factory.create(1, AttributePreferenceType.COST, false);
		RealField field2a = factory.create(1, AttributePreferenceType.COST, false);
		RealField field3 = factory.create(1, AttributePreferenceType.NONE, false);
		RealField field3a = factory.create(1, AttributePreferenceType.NONE, false);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}.
	 * Tests {@code null} attribute.
	 */
	@Test
	void testCreateWithPersistentCache01() {
		try {
			RealFieldCachingFactory.getInstance().createWithPersistentCache("-12.8", null);
			fail("Should not create field for null attribute.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}.
	 * Tests attribute with wrong {@link EvaluationAttribute#getValueType() value type}.
	 */
	@Test
	void testCreateWithPersistentCache02() {
		String value = "1";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(IntegerField.class)); //deliberately wrong value type
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			RealFieldCachingFactory.getInstance().createWithPersistentCache(value, attribute);
			fail("Should not create field for an attribute with incompatible value type.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Tests creation of real field from text {@link RealFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}.
	 * Tests incorrect textual representation of a real number.
	 */
	@Test
	public void testCreateWithPersistentCache03() {
		String value = "a";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(RealField.class));
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			RealFieldCachingFactory.getInstance().createWithPersistentCache(value, attribute);
			fail("Incorrect text should not be parsed as a real value.");
		} catch (FieldParseException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}
	 * and {@link RealFieldCachingFactory#getPersistentCacheSize()}.
	 */
	@Test
	void testCreateWithPersistentCache04() {
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(attributeMock1.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(attributeMock2.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST));
		
		EvaluationAttribute attributeMock3 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock3.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(attributeMock3.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE));
		
		RealFieldCachingFactory factory = RealFieldCachingFactory.getInstance();
		
		int initialPersistentCacheSize = factory.getPersistentCacheSize();
		int initialVolatileCacheSize = factory.getVolatileCacheSize();
		
		EvaluationField field1 = factory.createWithPersistentCache("-128.01", attributeMock1);
		EvaluationField field1a = factory.createWithPersistentCache("-128.01", attributeMock1);
		EvaluationField field2 = factory.createWithPersistentCache("-128.01", attributeMock2);
		EvaluationField field2a = factory.createWithPersistentCache("-128.01", attributeMock2);
		EvaluationField field3 = factory.createWithPersistentCache("-128.01", attributeMock3);
		EvaluationField field3a = factory.createWithPersistentCache("-128.01", attributeMock3);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
		
		assertEquals(factory.getPersistentCacheSize(), initialPersistentCacheSize + 3);
		assertEquals(factory.getVolatileCacheSize(), initialVolatileCacheSize); //the same size
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}.
	 * Tests {@code null} attribute.
	 */
	@Test
	void testCreateWithVolatileCache01() {
		try {
			RealFieldCachingFactory.getInstance().createWithVolatileCache("-12.8", null);
			fail("Should not create field for null attribute.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}.
	 * Tests attribute with wrong {@link EvaluationAttribute#getValueType() value type}.
	 */
	@Test
	void testCreateWithVolatileCache02() {
		String value = "1";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(IntegerField.class)); //deliberately wrong value type
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			RealFieldCachingFactory.getInstance().createWithVolatileCache(value, attribute);
			fail("Should not create field for an attribute with incompatible value type.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Tests creation of real field from text {@link RealFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}.
	 * Tests incorrect textual representation of a real number.
	 */
	@Test
	public void testCreateWithVolatileCache03() {
		String value = "a";
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(Mockito.mock(RealField.class));
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			RealFieldCachingFactory.getInstance().createWithVolatileCache(value, attribute);
			fail("Incorrect text should not be parsed as a real value.");
		} catch (FieldParseException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}
	 * and {@link RealFieldCachingFactory#getVolatileCacheSize()}.
	 */
	@Test
	void testCreateWithVolatileCache04() {
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(attributeMock1.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(attributeMock2.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST));
		
		EvaluationAttribute attributeMock3 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock3.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(attributeMock3.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE));
		
		RealFieldCachingFactory factory = RealFieldCachingFactory.getInstance();
		
		int initialPersistentCacheSize = factory.getPersistentCacheSize();
		factory.clearVolatileCache();
		assertEquals(factory.getVolatileCacheSize(), 0);
		
		EvaluationField field1 = factory.createWithVolatileCache("-1.2", attributeMock1);
		EvaluationField field1a = factory.createWithVolatileCache("-1.2", attributeMock1);
		EvaluationField field2 = factory.createWithVolatileCache("-1.2", attributeMock2);
		EvaluationField field2a = factory.createWithVolatileCache("-1.2", attributeMock2);
		EvaluationField field3 = factory.createWithVolatileCache("-1.2", attributeMock3);
		EvaluationField field3a = factory.createWithVolatileCache("-1.2", attributeMock3);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
		
		assertEquals(factory.getPersistentCacheSize(), initialPersistentCacheSize); //the same size
		assertEquals(factory.getVolatileCacheSize(), 3);
	}
	
	/**
	 * Test for {@link RealFieldCachingFactory#clearVolatileCache()}.
	 */
	@Test
	void testClearVolatileCache01() {
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(attributeMock1.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(attributeMock2.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST));
		
		EvaluationAttribute attributeMock3 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock3.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(attributeMock3.getValueType()).thenReturn(RealFieldFactory.getInstance().create(0, AttributePreferenceType.NONE));
		
		RealFieldCachingFactory factory = RealFieldCachingFactory.getInstance();
		
		assertEquals(factory.getVolatileCacheSize(), 0); //due to clearing in setUp method
		
		factory.createWithVolatileCache("20", attributeMock1);
		factory.createWithVolatileCache("20", attributeMock1);
		factory.createWithVolatileCache("21", attributeMock1);
		factory.createWithVolatileCache("20", attributeMock2);
		factory.createWithVolatileCache("20", attributeMock2);
		factory.createWithVolatileCache("21", attributeMock2);
		factory.createWithVolatileCache("20", attributeMock3);
		factory.createWithVolatileCache("20", attributeMock3);
		factory.createWithVolatileCache("21", attributeMock3);
		
		assertEquals(factory.getVolatileCacheSize(), 6);
		assertEquals(factory.clearVolatileCache(), 6); //6 items cleared
		assertEquals(factory.getVolatileCacheSize(), 0);
		assertEquals(factory.clearVolatileCache(), 0); //0 items cleared
	}

}
