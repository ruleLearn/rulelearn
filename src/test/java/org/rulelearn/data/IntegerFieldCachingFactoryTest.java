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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldCachingFactory;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * Test for {@link IntegerFieldCachingFactory} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class IntegerFieldCachingFactoryTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Test for {@link IntegerFieldCachingFactory#create(int, AttributePreferenceType, boolean)}.
	 */
	@Test
	void testCreate() {
		IntegerFieldCachingFactory factory = IntegerFieldCachingFactory.getInstance();
		
		IntegerField field1 = factory.create(1, AttributePreferenceType.GAIN, true);
		IntegerField field1a = factory.create(1, AttributePreferenceType.GAIN, true);
		IntegerField field2 = factory.create(1, AttributePreferenceType.COST, true);
		IntegerField field2a = factory.create(1, AttributePreferenceType.COST, true);
		IntegerField field3 = factory.create(1, AttributePreferenceType.NONE, true);
		IntegerField field3a = factory.create(1, AttributePreferenceType.NONE, true);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
	}
	
	/**
	 * Test for {@link IntegerFieldCachingFactory#createWithPersistentCache(String, EvaluationAttribute)}.
	 */
	@Test
	void testCreate02() {
		EvaluationAttribute attributeMock1 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock1.getPreferenceType()).thenReturn(AttributePreferenceType.GAIN);
		Mockito.when(attributeMock1.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN));
		EvaluationAttribute attributeMock2 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock2.getPreferenceType()).thenReturn(AttributePreferenceType.COST);
		Mockito.when(attributeMock2.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST));
		EvaluationAttribute attributeMock3 = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attributeMock3.getPreferenceType()).thenReturn(AttributePreferenceType.NONE);
		Mockito.when(attributeMock3.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE));
		
		IntegerFieldCachingFactory factory = IntegerFieldCachingFactory.getInstance();
		
		EvaluationField field1 = factory.create("1", attributeMock1);
		EvaluationField field1a = factory.create("1", attributeMock1);
		EvaluationField field2 = factory.create("1", attributeMock2);
		EvaluationField field2a = factory.create("1", attributeMock2);
		EvaluationField field3 = factory.create("1", attributeMock3);
		EvaluationField field3a = factory.create("1", attributeMock3);
		
		assertSame(field1, field1a);
		assertSame(field2, field2a);
		assertSame(field3, field3a);
		
		assertNotSame(field1, field2);
		assertNotSame(field1, field3);
		assertNotSame(field2, field3);
	}

}
