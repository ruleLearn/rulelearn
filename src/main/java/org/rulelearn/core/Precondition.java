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

import java.util.Collection;

/**
 * Class used to verify if methods' preconditions (like non-null parameters) are verified.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public final class Precondition {

	/**
	 * Verifies if given object reference is not {@code null}, and if so, returns this reference.
	 * 
	 * @param object object reference to verify
	 * @param errorMsg error message of the thrown {@link NullPointerException}, used when given object does not verify non-null precondition
	 * @param <T> type of the object to verify
	 * @return {@code object}, if {@code object != null}
	 * 
	 * @throws NullPointerException with given error message if given object reference is {@code null}
	 */
	public static <T extends Object> T notNull(T object, String errorMsg) {
		if (object != null) {
			return object;
		} else {
			throw new NullPointerException(errorMsg);
		}
	}
	
	/**
	 * Verifies if given number is non-negative.
	 * 
	 * @param number number to verify
	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when given number does not verify non-negative precondition
	 * @return {@code number}, if it is non-negative
	 * 
	 * @throws InvalidValueException with given error message if given number is negative
	 */
	public static int nonNegative(int number, String errorMsg) {
		if (number >= 0) {
			return number;
		} else {
			throw new InvalidValueException(errorMsg);
		}
	}
	
	/**
	 * Verifies if given number is non-negative.
	 * 
	 * @param number number to verify
	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when given number does not verify non-negative precondition
	 * @return {@code number}, if it is non-negative
	 * 
	 * @throws InvalidValueException with given error message if given number is negative
	 */
	public static double nonNegative(double number, String errorMsg) {
		if (number >= 0) {
			return number;
		} else {
			throw new InvalidValueException(errorMsg);
		}
	}
	
//	/**
//	 * Verifies if length of the given array is grater than zero.
//	 * 
//	 * @param array array to verify
//	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when given array is empty
//	 * @return {@code array}, if it is not empty
//	 * 
//	 * @throws InvalidValueException if given array is empty (has length 0)
//	 */
//	public static <T extends Object> T[] nonEmpty(T[] array, String errorMsg) {
//		if (array.length > 0) {
//			return array;
//		} else {
//			throw new InvalidValueException(errorMsg);
//		}
//	}
	
	/**
	 * Verifies if given collection is not empty (i.e., it contains at least one element).
	 * 
	 * @param collection collection to verify
	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when given collection is empty
	 * @param <T> type of the collection to verify
	 * @return {@code collection}, if it is not empty
	 * 
	 * @throws InvalidSizeException with given error message if given collection is empty (contains no elements)
	 */
	public static <T extends Collection<?>> T nonEmpty(T collection, String errorMsg) {
		if (!collection.isEmpty()) {
			return collection;
		} else {
			throw new InvalidSizeException(errorMsg);
		}
	}
	
	/**
	 * Verifies if given value is known, i.e., different than given special number (code) whose semantics is "unknown value".
	 * 
	 * @param value value to verify 
	 * @param unknownValueCode number (code) whose semantics is "unknown value"
	 * @param errorMsg error message of the thrown {@link UnknownValueException}, used when given value is equal to given unknown value code 
	 * @return {@code value}, if it is different than the unknown value code
	 * 
	 * @throws UnknownValueException with given error message if given value is equal to the given unknown value code
	 */
	public static int known(int value, int unknownValueCode, String errorMsg) {
		if (value == unknownValueCode) {
			throw new UnknownValueException(errorMsg);
		}
		return value;
	}
	
	/**
	 * Verifies if given value is known, i.e., different than given special number (code) whose semantics is "unknown value".
	 * 
	 * @param value value to verify 
	 * @param unknownValueCode number (code) whose semantics is "unknown value"
	 * @param errorMsg error message of the thrown {@link UnknownValueException}, used when given value is equal to given unknown value code 
	 * @return {@code value}, if it is different than the unknown value code
	 * 
	 * @throws UnknownValueException with given error message if given value is equal to the given unknown value code
	 */
	public static double known(double value, double unknownValueCode, String errorMsg) {
		if (value == unknownValueCode) {
			throw new UnknownValueException(errorMsg);
		}
		return value;
	}
	
//	/**
//	 * Verifies if given number is different than given wrong number.
//	 * 
//	 * @param number number to verify
//	 * @param wrongNumber wrong number that should be avoided
//	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when both numbers are equal
//	 * @return the number, if it is different the wrong number
//	 * 
//	 * @throws InvalidValueException with given error message if given number is equal to the given wrong number
//	 */
//	public static int different(int number, int wrongNumber, String errorMsg) {
//		if (number == wrongNumber) {
//			throw new InvalidValueException(errorMsg);
//		}
//		return number;
//	}
//	
//	/**
//	 * Verifies if given number is different than given wrong number.
//	 * 
//	 * @param number number to verify
//	 * @param wrongNumber wrong number that should be avoided
//	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when both numbers are equal
//	 * @return the number, if it is different the wrong number
//	 * 
//	 * @throws InvalidValueException with given error message if given number is equal to the given wrong number
//	 */
//	public static double different(double number, double wrongNumber, String errorMsg) {
//		if (number == wrongNumber) {
//			throw new InvalidValueException(errorMsg);
//		}
//		return number;
//	}
	
	/**
	 * Verifies if given number is within interval [0,1].
	 * 
	 * @param number number to verify
	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when given number is not within interval [0,1]
	 * @return the number, if it is within interval [0,1]
	 * 
	 * @throws InvalidValueException if given number is not within interval [0,1]
	 */
	public static double within01Interval(double number, String errorMsg) {
		if (number < 0 || number > 1) {
			throw new InvalidValueException(errorMsg);
		}
		return number;
	}
	
	/**
	 * Verifies if given number is within interval [-1,1].
	 * 
	 * @param number number to verify
	 * @param errorMsg error message of the thrown {@link InvalidValueException}, used when given number is not within interval [-1,1]
	 * @return the number, if it is within interval [-1,1]
	 * 
	 * @throws InvalidValueException if given number is not within interval [-1,1]
	 */
	public static double withinMinus1Plus1Interval(double number, String errorMsg) {
		if (number < -1 || number > 1) {
			throw new InvalidValueException(errorMsg);
		}
		return number;
	}
	
	/**
	 * Constructor preventing object creation.
	 */
	private Precondition() {
		
	}
}
