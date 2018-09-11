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

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Precondition}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class PreconditionTest {

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#notNull(java.lang.Object, java.lang.String)}.
	 */
	@Test
	void testNotNull01() {
		String message = "Test message.";
		try {
			Precondition.notNull(null, message);
			fail("Not null precondition verified incorrectly.");
		} catch (NullPointerException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#notNull(java.lang.Object, java.lang.String)}.
	 */
	@Test
	void testNotNull02() {
		Object object = Integer.valueOf(0);

		assertEquals(Precondition.notNull(object, "Test message."), object);
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#notNull(Object, String...)}.
	 */
	@Test
	void testNotNull03() {
		String[] messageParts = {"Prefix", " suffix."};
		String expectedMessage = "Prefix suffix.";
		try {
			Precondition.notNull(null, messageParts);
			fail("Not null precondition verified incorrectly.");
		} catch (NullPointerException exception) {
			assertEquals(exception.getMessage(), expectedMessage);
		}
	}

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonNegative(int, java.lang.String)}.
	 */
	@Test
	void testNonNegativeIntString01() {
		String message = "Test message.";
		try {
			Precondition.nonNegative(-1, message);
			fail("Non negative precondition verified incorrectly.");
		} catch (InvalidValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonNegative(int, java.lang.String)}.
	 */
	@Test
	void testNonNegativeIntString02() {
		assertEquals(Precondition.nonNegative(0, "Test message."), 0);
	}

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonNegative(double, java.lang.String)}.
	 */
	@Test
	void testNonNegativeDoubleString01() {
		String message = "Test message.";
		try {
			Precondition.nonNegative(-1.0, message);
			fail("Non negative precondition verified incorrectly.");
		} catch (InvalidValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonNegative(double, java.lang.String)}.
	 */
	@Test
	void testNonNegativeDoubleString02() {
		assertEquals(Precondition.nonNegative(0.0, "Test message."), 0.0);
	}

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonEmpty(T[], java.lang.String)}.
	 */
	@Test
	void testNonEmptyTArrayString01() {
		String message = "Test message.";
		try {
			Precondition.nonEmpty(new Object[0], message);
			fail("Empty array precondition verified incorrectly.");
		} catch (InvalidSizeException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonEmpty(T[], java.lang.String)}.
	 */
	@Test
	void testNonEmptyTArrayString02() {
		Object[] array = {Integer.valueOf(0)};
		assertEquals(Precondition.nonEmpty(array, "Test message."), array);
	}

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonEmpty(java.util.Collection, java.lang.String)}.
	 */
	@Test
	void testNonEmptyTString01() {
		String message = "Test message.";
		try {
			Precondition.nonEmpty(new ArrayList<Integer>(), message);
			fail("Empty collection precondition verified incorrectly.");
		} catch (InvalidSizeException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#nonEmpty(java.util.Collection, java.lang.String)}.
	 */
	@Test
	void testNonEmptyTString02() {
		ArrayList<Integer> collection = new ArrayList<Integer>();
		collection.add(Integer.valueOf(0));
		assertEquals(Precondition.nonEmpty(collection, "Test message."), collection);
	}


	/**
	 * Test method for {@link org.rulelearn.core.Precondition#known(int, int, java.lang.String)}.
	 */
	@Test
	void testKnownIntIntString01() {
		String message = "Test message.";
		try {
			Precondition.known(5, 5, message);
			fail("Known value precondition verified incorrectly.");
		} catch (UnknownValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#known(int, int, java.lang.String)}.
	 */
	@Test
	void testKnownIntIntString02() {
		assertEquals(Precondition.known(4, 5, "Test message."), 4);
	}

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#known(double, double, java.lang.String)}.
	 */
	@Test
	void testKnownDoubleDoubleString01() {
		String message = "Test message.";
		try {
			Precondition.known(3.5, 3.5, message);
			fail("Known value precondition verified incorrectly.");
		} catch (UnknownValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#known(double, double, java.lang.String)}.
	 */
	@Test
	void testKnownDoubleDoubleString02() {
		assertEquals(Precondition.known(2.5, 3.5, "Test message."), 2.5);
	}

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#within01Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithin01Interval01() {
		String message = "Test message.";
		try {
			Precondition.within01Interval(-0.001, message);
			fail("Within 0-1 interval precondition verified incorrectly.");
		} catch (InvalidValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#within01Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithin01Interval02() {
		String message = "Test message.";
		try {
			Precondition.within01Interval(1.001, message);
			fail("Within [0, 1] interval precondition verified incorrectly.");
		} catch (InvalidValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#within01Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithin01Interval03() {
		assertEquals(Precondition.within01Interval(0, "Test message."), 0);
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#within01Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithin01Interval04() {
		assertEquals(Precondition.within01Interval(1, "Test message."), 1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#within01Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithin01Interval05() {
		assertEquals(Precondition.within01Interval(0.5, "Test message."), 0.5);
	}

	/**
	 * Test method for {@link org.rulelearn.core.Precondition#withinMinus1Plus1Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithinMinus1Plus1Interval01() {
		String message = "Test message.";
		try {
			Precondition.withinMinus1Plus1Interval(-1.001, message);
			fail("Within [-1, 1] interval precondition verified incorrectly.");
		} catch (InvalidValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#withinMinus1Plus1Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithinMinus1Plus1Interval02() {
		String message = "Test message.";
		try {
			Precondition.withinMinus1Plus1Interval(1.001, message);
			fail("Within [-1, 1] interval precondition verified incorrectly.");
		} catch (InvalidValueException exception) {
			assertEquals(exception.getMessage(), message);
		}
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#withinMinus1Plus1Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithinMinus1Plus1Interval03() {
		assertEquals(Precondition.withinMinus1Plus1Interval(-1, "Test message."), -1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#withinMinus1Plus1Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithinMinus1Plus1Interval04() {
		assertEquals(Precondition.withinMinus1Plus1Interval(1, "Test message."), 1);
	}
	
	/**
	 * Test method for {@link org.rulelearn.core.Precondition#withinMinus1Plus1Interval(double, java.lang.String)}.
	 */
	@Test
	void testWithinMinus1Plus1Interval05() {
		assertEquals(Precondition.withinMinus1Plus1Interval(0, "Test message."), 0);
	}

}
