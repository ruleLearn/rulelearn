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

import org.rulelearn.core.UncomparableException;

/**
 * Generalization of standard {@link Comparable} interface, accounting for uncomparable objects. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 * 
 * @param <T> class of objects that this object can be compared to 
 */
public interface ComparableExt<T> {
	
	/**
	 * Compares two objects - this object, being the object of interest, and the other object.
	 * This is an extended version of {@link Comparable#compareTo(Object)} that can be used to
	 * compare objects that may turn out to be semantically uncomparable - see {@link UncomparableException}.
	 * 
	 * @param otherObject other object that this object is compared to
	 * 
	 * @return negative value if this object is smaller than the other object,<br>
	 *         zero if both objects are equal,<br>
	 *         positive number if this object is greater than the other object
	 * 
	 * @throws ClassCastException if type of the other object is such that this object cannot be compared to the other object
	 * @throws NullPointerException if the other object is {@code null}
	 * @throws UncomparableException if this object is semantically uncomparable with the other object
	 */
	public int compareToEx(T otherObject) throws UncomparableException;
	
	/**
	 * Compares two objects - this object, being the object of interest, and the other object.
	 * Conveniently wraps {@link ComparableExt#compareToEx(Object)},
	 * handling possible {@link UncomparableException} exception by returning {@link ComparisonResult#UNCOMPARABLE}.
	 * 
	 * @param otherObject other object that this object is compared to
	 * @return {@link ComparisonResult#SMALLER_THAN} if this object is smaller than the other object,<br>
	 *         {@link ComparisonResult#EQUAL} if both objects are equal,<br>
	 *         {@link ComparisonResult#GREATER_THAN} if this object is greater than the other object,
	 *         {@link ComparisonResult#UNCOMPARABLE} if this object is semantically uncomparable with the other object
	 * 
	 * @throws ClassCastException if type of the other object is such that this object cannot be compared to the other object
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

}

