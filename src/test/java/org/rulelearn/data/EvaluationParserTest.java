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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rulelearn.core.FieldParseException;
import org.rulelearn.data.EvaluationParser.CachingType;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldCachingFactory;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldCachingFactory;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldCachingFactory;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link EvaluationParser} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class EvaluationParserTest {
	
	/**
	 * Test for class constructor. Tests if an exception is thrown for incorrect parameters.
	 */
	@Test
	void testEvaluationParser01() {
		try {
			String[] missingValueStrings = {"_", "!"};
			new EvaluationParser(missingValueStrings, null);
			fail("Should not create evaluation parser with null caching type.");
		} catch (NullPointerException exception) {
			//OK - do nothing
		}
	}
	
	/**
	 * Test for class constructor. Tests if an exception is thrown for incorrect parameters.
	 */
	@Test
	void testEvaluationParser02() {
		try {
			new EvaluationParser(null, CachingType.NONE);
			fail("Should not create evaluation parser with null missing value strings.");
		} catch (NullPointerException exception) {
			//OK - do nothing
		}
	}
	
	/**
	 * Test for class constructor. Tests if an exception is thrown for incorrect parameters.
	 */
	@Test
	void testEvaluationParser03() {
		try {
			String[] missingValueStrings = {"*", null};
			new EvaluationParser(missingValueStrings, CachingType.NONE);
			fail("Should not create evaluation parser with a null missing value string.");
		} catch (NullPointerException exception) {
			//OK - do nothing
		}
	}
	
	/**
	 * Test for class constructor. Tests if an exception is thrown for incorrect parameters.
	 */
	@Test
	void testEvaluationParser04() {
		try {
			String[] missingValueStrings = {}; //shuould work - the user does not expect any missing values
			new EvaluationParser(missingValueStrings, CachingType.NONE);
		} catch (NullPointerException exception) {
			//OK - do nothing
		}
	}

	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link IntegerField integer field evaluation}.
	 */
	@Test
	void testParseEvaluation01() {
		String evaluationStr = "1";
		int evaluation = 1;
		AttributePreferenceType preferenceType = AttributePreferenceType.NONE;
		
		IntegerField field = IntegerFieldFactory.getInstance().create(evaluation, preferenceType);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		assertEquals(new EvaluationParser().parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link IntegerField integer field evaluation}.
	 */
	@Test
	void testParseEvaluation02() {
		String evaluationStr = " -100 ";
		int evaluation = -100;
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		IntegerField field = IntegerFieldFactory.getInstance().create(evaluation, preferenceType);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		assertEquals(new EvaluationParser().parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link RealField real field evaluation}.
	 */
	@Test
	void testParseEvaluation03() {
		String evaluationStr = "123.45";
		double evaluation = 123.45;
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		RealField field = RealFieldFactory.getInstance().create(evaluation, preferenceType);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		assertEquals(new EvaluationParser().parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link RealField real field evaluation}.
	 */
	@Test
	void testParseEvaluation04() {
		String evaluationStr = " -2e5 ";
		double evaluation = -2e5;
		AttributePreferenceType preferenceType = AttributePreferenceType.COST;
		
		RealField field = RealFieldFactory.getInstance().create(evaluation, preferenceType);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		assertEquals(new EvaluationParser().parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link EnumerationField enumeration field evaluation}.
	 */
	@Test
	void testParseEvaluation05() {
		String[] elements = {"a", "b", "c", "d"};
		String evaluationStr = "b";
		ElementList domain = null;
		try {
			domain = new ElementList(elements);
		}
		catch (NoSuchAlgorithmException ex) {
			fail("Should create element list.");
		}
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain, 1, preferenceType);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		assertEquals(new EvaluationParser().parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link EnumerationField enumeration field evaluation}.
	 */
	@Test
	void testParseEvaluation06() {
		String[] elements = {"a", "b", "c", "d"};
		String evaluationStr = "   d ";
		ElementList domain = null;
		try {
			domain = new ElementList(elements);
		}
		catch (NoSuchAlgorithmException ex) {
			fail("Should create element list.");
		}
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		EnumerationField field = EnumerationFieldFactory.getInstance().create(domain, 3, preferenceType);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		assertEquals(new EvaluationParser().parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@code null} and empty string.
	 */
	@Test
	void testParseEvaluation07() {
		AttributePreferenceType preferenceType = AttributePreferenceType.NONE;
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		try {
			new EvaluationParser().parseEvaluation(null, attribute);
			fail("Evaluation parser should fail for null evaluation.");
		} catch (FieldParseException exception) {
			//OK - do nothing
		}
		
		try {
			new EvaluationParser().parseEvaluation("", attribute);
			fail("Evaluation parser should fail for empty string evaluation.");
		} catch (FieldParseException exception) {
			//OK - do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link UnknownSimpleField missing values} using evaluation parser constructed with default parameters.
	 */
	@Test
	void testParseEvaluation08() {
		AttributePreferenceType preferenceType = AttributePreferenceType.NONE;
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		Mockito.when(attribute.getMissingValueType()).thenReturn(UnknownSimpleFieldMV15.getInstance());
		
		EvaluationParser evaluationParser = new EvaluationParser();
		assertEquals(evaluationParser.parseEvaluation("?", attribute), UnknownSimpleFieldMV15.getInstance());
		assertEquals(evaluationParser.parseEvaluation("*", attribute), UnknownSimpleFieldMV15.getInstance());
		assertEquals(evaluationParser.parseEvaluation("NA", attribute), UnknownSimpleFieldMV15.getInstance());
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link UnknownSimpleField missing values} using evaluation parser constructed with particular missing value strings.
	 */
	@Test
	void testParseEvaluation09() {
		AttributePreferenceType preferenceType = AttributePreferenceType.NONE;
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		Mockito.when(attribute.getMissingValueType()).thenReturn(UnknownSimpleFieldMV2.getInstance());
		
		String[] missingValueStrings = {"_", "!"};
		
		EvaluationParser evaluationParser = new EvaluationParser(missingValueStrings, CachingType.VOLATILE) ;
		assertEquals(evaluationParser.parseEvaluation(" _ ", attribute), UnknownSimpleFieldMV2.getInstance());
		assertEquals(evaluationParser.parseEvaluation(" ! ", attribute), UnknownSimpleFieldMV2.getInstance());
		
		try {
			evaluationParser.parseEvaluation("?", attribute);
			fail("Should not parse regular string as an integer.");
		} catch (FieldParseException exception) {
			//OK - do nothing
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link IntegerField integer field evaluation} using evaluation parser constructed in a way that involves using caching factory.
	 */
	@Test
	void testParseEvaluation10() {
		String evaluationStr = "0";
		int evaluation = 0;
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		IntegerFieldCachingFactory.getInstance().clearVolatileCache();
		
		IntegerField field = IntegerFieldCachingFactory.getInstance().create(evaluation, preferenceType, false); //use volatile cache
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		String[] missingValueStrings = {"?", "*"};
		
		assertSame(new EvaluationParser(missingValueStrings, CachingType.VOLATILE).parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link RealField real field evaluation} using evaluation parser constructed in a way that involves using caching factory.
	 */
	@Test
	void testParseEvaluation11() {
		String evaluationStr = "-12.5";
		double evaluation = -12.5;
		AttributePreferenceType preferenceType = AttributePreferenceType.COST;
		
		RealFieldCachingFactory.getInstance().clearVolatileCache();
		
		RealField field = RealFieldCachingFactory.getInstance().create(evaluation, preferenceType, false); //use volatile cache
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		String[] missingValueStrings = {"?", "*"};
		
		assertSame(new EvaluationParser(missingValueStrings, CachingType.VOLATILE).parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#parseEvaluation(java.lang.String, org.rulelearn.data.EvaluationAttribute)}.
	 * Tests parsing {@link EnumerationField enumeration field evaluation} using evaluation parser constructed in a way that involves using caching factory.
	 */
	@Test
	void testParseEvaluation12() {
		String[] elements = {"a", "b", "c", "d"};
		String evaluationStr = " c ";
		ElementList domain = null;
		try {
			domain = new ElementList(elements);
		}
		catch (NoSuchAlgorithmException ex) {
			fail("Should create element list.");
		}
		AttributePreferenceType preferenceType = AttributePreferenceType.GAIN;
		
		EnumerationFieldCachingFactory.getInstance().clearVolatileCache();
		
		EnumerationField field = EnumerationFieldCachingFactory.getInstance().create(domain, 2, preferenceType, false);
		
		EvaluationAttribute attribute = Mockito.mock(EvaluationAttribute.class);
		Mockito.when(attribute.getValueType()).thenReturn(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, preferenceType));
		Mockito.when(attribute.getPreferenceType()).thenReturn(preferenceType);
		
		String[] missingValueStrings = {"?", "*"};
		
		assertSame(new EvaluationParser(missingValueStrings, CachingType.VOLATILE).parseEvaluation(evaluationStr, attribute), field);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#getMissingValueStrings()}.
	 * Tests if evaluation parser constructed with default constructor indeed uses {@link EvaluationParser.DEFAULT_MISSING_VALUE_STRINGS}.
	 */
	@Test
	void testGetMissingValueStrings() {
		EvaluationParser evaluationParser = new EvaluationParser();
		String[] missingValueStrings = evaluationParser.getMissingValueStrings();
		
		assertEquals(missingValueStrings.length, EvaluationParser.DEFAULT_MISSING_VALUE_STRINGS.length);
		for (int i = 0; i < missingValueStrings.length; i++) {
			assertEquals(missingValueStrings[i], EvaluationParser.DEFAULT_MISSING_VALUE_STRINGS[i]);
		}
	}

	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#setMissingValueStrings(java.lang.String[])}.
	 * Tests if missing value strings are correctly set.
	 */
	@Test
	void testSetMissingValueStrings() {
		EvaluationParser evaluationParser = new EvaluationParser();
		String[] missingValueStrings = {"!", "_"};
		evaluationParser.setMissingValueStrings(missingValueStrings);
		String[] readMissingValueStrings = evaluationParser.getMissingValueStrings();
		
		assertEquals(missingValueStrings.length, readMissingValueStrings.length);
		for (int i = 0; i < missingValueStrings.length; i++) {
			assertEquals(missingValueStrings[i], readMissingValueStrings[i]);
		}
	}

	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#getCachingType()}.
	 * Tests if evaluation parser constructed with default constructor indeed uses {@link EvaluationParser.CachingType#NONE}.
	 */
	@Test
	void testGetCachingType() {
		EvaluationParser evaluationParser = new EvaluationParser();
		EvaluationParser.CachingType cachingType = evaluationParser.getCachingType();
		
		assertEquals(cachingType, EvaluationParser.CachingType.NONE);
	}

	/**
	 * Test method for {@link org.rulelearn.data.EvaluationParser#setCachingType(org.rulelearn.data.EvaluationParser.CachingType)}.
	 * Tests if caching type is correctly set.
	 */
	@Test
	void testSetCachingType() {
		EvaluationParser evaluationParser = new EvaluationParser();
		evaluationParser.setCachingType(EvaluationParser.CachingType.VOLATILE);
		
		assertEquals(evaluationParser.getCachingType(), EvaluationParser.CachingType.VOLATILE);
	}

}
