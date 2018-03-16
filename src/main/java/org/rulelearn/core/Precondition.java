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

/**
 * Class used to verify if methods' preconditions (like non-null parameters) are verified.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Precondition {

	/**
	 * Verifies if given object reference is not {@code null}.
	 * 
	 * @param object object reference to verify
	 * @param errorMsg error message of the thrown {@link NullPointerException}, used when given object does not verify non-null precondition
	 * @return {@code object}, if {@code object != null}
	 * 
	 * @throws NullPointerException if given object reference is {@code null}
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
	 * @throws InvalidValueException if given number is negative
	 */
	public static int isNonNegative(int number, String errorMsg) {
		if (number < 0) {
			return number;
		} else {
			throw new InvalidValueException(errorMsg);
		}
	}
	
	/**
	 * Constructor preventing object creation.
	 */
	private Precondition() {
		
	}
}
