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
 * Result of comparison of two objects - the object of interest (the first object), and the other object (the second object).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public enum ComparisonResult {
	/**
	 * Value indicating that the object of interest is greater than the other object. 
	 */
	GREATER_THAN,
	/**
	 * Value indicating that the object of interest is smaller than the other object.
	 */
	SMALLER_THAN,
	/**
	 * Value indicating that the object of interest is equal to the other object.
	 */
	EQUAL,
	/**
	 * Value indicating that the object of interest is uncomparable with the other object.
	 */
	UNCOMPARABLE
}