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
 * Contract for objects that allow other objects to be compared with them.
 * This, contrary to {@link ComparableExt} contract, allows to implement calculation of comparison result
 * between object {@code a} and {@code b} in the class of object {@code b}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 * @param <T> class of objects that can be compared to this object
 */
public interface ReverseComparableExt<T> {
	
	/**
	 * Compares the other object to this object. Does the reverse comparison than {@link ComparableExt#compareToEx(Object)}.
	 * 
	 * @param otherObject other object to be compared to this object
	 * @return negative number if the other object is smaller than this object,<br>
	 *         zero if both objects are equal,<br>
	 *         positive number if the other object is greater than this object
	 * 
	 * @throws ClassCastException if type of the other object is such that the other object cannot be compared to this object  
	 * @throws NullPointerException if the other object is {@code null}
	 * @throws UncomparableException if the other object is semantically uncomparable with this object
	 */
	abstract public int reverseCompareToEx(T otherObject) throws UncomparableException;
	
	/**
	 * Compares the other object to this object. Conveniently wraps {@link ReverseComparableExt#reverseCompareToEx(Object)},
	 * handling possible {@link UncomparableException} exception by returning {@link ComparisonResult#UNCOMPARABLE}.
	 * 
	 * @param otherObject other object to be compared to this object
	 * @return {@link ComparisonResult#SMALLER_THAN} if the other object is smaller than this object,<br>
	 *         {@link ComparisonResult#EQUAL} if both objects are equal,<br>
	 *         {@link ComparisonResult#GREATER_THAN} if the other object is greater than this object,
	 *         {@link ComparisonResult#UNCOMPARABLE} if the other object is semantically uncomparable with this object
	 * 
	 * @throws ClassCastException if type of the other object is such that the other object cannot be compared to this object  
	 * @throws NullPointerException if the other object is {@code null}
	 */
	public default ComparisonResult reverseCompareToEnum(T otherObject) {
		int result;
		try {
			result = this.reverseCompareToEx(otherObject);
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
