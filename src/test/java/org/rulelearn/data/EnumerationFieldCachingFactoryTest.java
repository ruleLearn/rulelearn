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

package org.rulelearn.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldCachingFactory;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;

/**
 * Tests for {@link EnumerationFieldCachingFactory} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class EnumerationFieldCachingFactoryTest {
	
	private ElementList domain1;
	
	private String[] values1 = {"1", "2", "3", "4"};

	@BeforeEach
	void setUp() {
		EnumerationFieldCachingFactory.getInstance().clearVolatileCache();
		
		try {
			domain1 = new ElementList(values1);
		}
		catch (NoSuchAlgorithmException ex) {
			fail("Should create element list.");
		}
	}

	/**
	 * Test for {@link EnumerationFieldCachingFactory#create(ElementList, int, AttributePreferenceType, boolean)}.
	 * Tests persistent cache.
	 */
	@Test
	void testCreate01() {
		EnumerationFieldCachingFactory factory = EnumerationFieldCachingFactory.getInstance();
		
		EnumerationField field1 = factory.create(domain1, 1, AttributePreferenceType.GAIN, true);
		EnumerationField field1a = factory.create(domain1, 1, AttributePreferenceType.GAIN, true);
		EnumerationField field2 = factory.create(domain1, 1, AttributePreferenceType.COST, true);
		EnumerationField field2a = factory.create(domain1, 1, AttributePreferenceType.COST, true);
		EnumerationField field3 = factory.create(domain1, 1, AttributePreferenceType.NONE, true);
		EnumerationField field3a = factory.create(domain1, 1, AttributePreferenceType.NONE, true);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#create(ElementList, int, AttributePreferenceType, boolean)}.
	 * Tests volatile cache.
	 */
	@Test
	void testCreate02() {
		EnumerationFieldCachingFactory factory = EnumerationFieldCachingFactory.getInstance();
		
		EnumerationField field1 = factory.create(domain1, 1, AttributePreferenceType.GAIN, false);
		EnumerationField field1a = factory.create(domain1, 1, AttributePreferenceType.GAIN, false);
		EnumerationField field2 = factory.create(domain1, 1, AttributePreferenceType.COST, false);
		EnumerationField field2a = factory.create(domain1, 1, AttributePreferenceType.COST, false);
		EnumerationField field3 = factory.create(domain1, 1, AttributePreferenceType.NONE, false);
		EnumerationField field3a = factory.create(domain1, 1, AttributePreferenceType.NONE, false);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}.
	 * Tests {@code null} attribute.
	 */
	@Test
	void testCreateWithPersistentCache01() {
		try {
			EnumerationFieldCachingFactory.getInstance().createWithPersistentCache("1", null);
			fail("Should not create field for null attribute.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}.
	 * Tests attribute with wrong {@link EvaluationAttribute#getValueType() value type}.
	 */
	@Test
	void testCreateWithPersistentCache02() {
		IntegerField valueType = Mockito.mock(IntegerField.class);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(valueType);
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			EnumerationFieldCachingFactory.getInstance().createWithPersistentCache("1", attribute);
			fail("Should not create field for an attribute with incompatible value type.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}.
	 * Tests value outside attribute domain.
	 */
	@Test
	void testCreateWithPersistentCache03() {
		EnumerationField valueType = Mockito.mock(EnumerationField.class);
		Mockito.when(valueType.getElementList()).thenReturn(this.domain1);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(valueType);
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			EnumerationFieldCachingFactory.getInstance().createWithPersistentCache("a", attribute); //test value not present in attribute's domain
			fail("Incorrect text should not be parsed as an enumeration value.");
		} catch (FieldParseException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)} and
	 * {@link EnumerationFieldCachingFactory#getPersistentCacheSize()}.
	 */
	@Test
	void testCreateWithPersistentCache04() {
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(attributeMock1.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.GAIN));
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(attributeMock2.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.COST));
		
		EvaluationAttribute attributeMock3 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock3.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(attributeMock3.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.NONE));
		
		EnumerationFieldCachingFactory factory = EnumerationFieldCachingFactory.getInstance();
		
		int initialPersistentCacheSize = factory.getPersistentCacheSize();
		int initialVolatileCacheSize = factory.getVolatileCacheSize();
		
		EvaluationField field1 = factory.createWithPersistentCache("2", attributeMock1);
		EvaluationField field1a = factory.createWithPersistentCache("2", attributeMock1);
		EvaluationField field2 = factory.createWithPersistentCache("2", attributeMock2);
		EvaluationField field2a = factory.createWithPersistentCache("2", attributeMock2);
		EvaluationField field3 = factory.createWithPersistentCache("2", attributeMock3);
		EvaluationField field3a = factory.createWithPersistentCache("2", attributeMock3);
		
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
	 * Test for {@link EnumerationFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}.
	 * Tests {@code null} attribute.
	 */
	@Test
	void testCreateWithVolatileCache01() {
		try {
			EnumerationFieldCachingFactory.getInstance().createWithVolatileCache("1", null);
			fail("Should not create field for null attribute.");
		} catch (NullPointerException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}.
	 * Tests attribute with wrong {@link EvaluationAttribute#getValueType() value type}.
	 */
	@Test
	void testCreateWithVolatileCache02() {
		IntegerField valueType = Mockito.mock(IntegerField.class);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(valueType);
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			EnumerationFieldCachingFactory.getInstance().createWithVolatileCache("1", attribute);
			fail("Should not create field for an attribute with incompatible value type.");
		} catch (InvalidTypeException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}.
	 * Tests value outside attribute domain.
	 */
	@Test
	void testCreateWithVolatileCache03() {
		EnumerationField valueType = Mockito.mock(EnumerationField.class);
		Mockito.when(valueType.getElementList()).thenReturn(this.domain1);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(valueType);
		Mockito.when(attribute.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		
		try {
			EnumerationFieldCachingFactory.getInstance().createWithVolatileCache("a", attribute); //test value not present in attribute's domain
			fail("Incorrect text should not be parsed as an enumeration value.");
		} catch (FieldParseException exception) {
			//OK
		}
	}
	
	/**
	 * Test for {@link EnumerationFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}
	 * and {@link EnumerationFieldCachingFactory#getVolatileCacheSize()}.
	 */
	@Test
	void testCreateWithVolatileCache04() {
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(attributeMock1.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.GAIN));
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(attributeMock2.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.COST));
		
		EvaluationAttribute attributeMock3 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock3.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(attributeMock3.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.NONE));
		
		EnumerationFieldCachingFactory factory = EnumerationFieldCachingFactory.getInstance();
		
		int initialPersistentCacheSize = factory.getPersistentCacheSize();
		factory.clearVolatileCache();
		assertEquals(factory.getVolatileCacheSize(), 0);
		
		EvaluationField field1 = factory.createWithVolatileCache("3", attributeMock1);
		EvaluationField field1a = factory.createWithVolatileCache("3", attributeMock1);
		EvaluationField field2 = factory.createWithVolatileCache("3", attributeMock2);
		EvaluationField field2a = factory.createWithVolatileCache("3", attributeMock2);
		EvaluationField field3 = factory.createWithVolatileCache("3", attributeMock3);
		EvaluationField field3a = factory.createWithVolatileCache("3", attributeMock3);
		
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
	 * Test for {@link EnumerationFieldCachingFactory#clearVolatileCache()}.
	 */
	@Test
	void testClearVolatileCache01() {
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(attributeMock1.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.GAIN));
		
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(attributeMock2.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.COST));
		
		EvaluationAttribute attributeMock3 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock3.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(attributeMock3.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain1, 0, AttributePreferenceType.NONE));
		
		EnumerationFieldCachingFactory factory = EnumerationFieldCachingFactory.getInstance();
		
		assertEquals(factory.getVolatileCacheSize(), 0); //due to clearing in setUp method
		
		factory.createWithVolatileCache("1", attributeMock1);
		factory.createWithVolatileCache("1", attributeMock1);
		factory.createWithVolatileCache("2", attributeMock1);
		factory.createWithVolatileCache("2", attributeMock2);
		factory.createWithVolatileCache("2", attributeMock2);
		factory.createWithVolatileCache("3", attributeMock2);
		factory.createWithVolatileCache("3", attributeMock3);
		factory.createWithVolatileCache("3", attributeMock3);
		factory.createWithVolatileCache("4", attributeMock3);
		
		assertEquals(factory.getVolatileCacheSize(), 6);
		assertEquals(factory.clearVolatileCache(), 6); //6 items cleared
		assertEquals(factory.getVolatileCacheSize(), 0);
		assertEquals(factory.clearVolatileCache(), 0); //0 items cleared
	}
	
}
