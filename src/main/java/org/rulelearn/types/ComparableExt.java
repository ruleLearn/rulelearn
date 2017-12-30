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

import org.rulelearn.utils.UncomparableException;

/**
 * Generalization of standard {@link Comparable} interface, accounting for uncomparable objects (e.g., objects of different types). 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ComparableExt<T> {
	
	/**
	 * Compares two objects - this object, being the object of interest, and the other object.
	 * This is an extended version of {@link Comparable#compareTo(Object)} that can be used to
	 * compare objects that may turn out to be semantically uncomparable - see {@link UncomparableException}.
	 * 
	 * @param otherObject other object to be compared with this object
	 * 
	 * @return negative value when this object is smaller than the other object,<br>
	 *         zero if both objects are equal,<br>
	 *         positive number when this object is greater than the other object
	 * 
	 * @throws ClassCastException if the other object is of different type than this object
	 * @throws NullPointerException if the other object is {@code null}
	 * @throws UncomparableException if this object is semantically uncomparable with the other object
	 */
	public int compareToEx(T otherObject) throws UncomparableException;
	
	/**
	 * Compares two objects - this object, being the object of interest, and the other object.
	 * Conveniently wraps {{@link #compareToEx(Object)},
	 * handling possible {@link UncomparableException} exception.
	 * 
	 * @param otherObject other object to be compared with this object
	 * @return see {@link ComparisonResult}
	 * 
	 * @throws ClassCastException if the other object is of different type than this object
	 * @throws NullPointerException if the other object is {@code null}
	 */
	public default ComparisonResult compareToEnum(T otherObject) {
		int result;
		try {
			result = this.compareToEx(otherObject);
			if (result > 0) {
				return ComparisonResult.GREATER_THAN;
			} else if (result == 0) {
				return ComparisonResult.EQUAL;
			} else {
				return ComparisonResult.SMALLER_THAN;
			}
		} catch (UncomparableException exception) {
			return ComparisonResult.UNCOMPARABLE;
		}
	}
	
	/**
	 * Result of comparison of two objects - the object of interest (first object), and the other object (second object).
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

}

