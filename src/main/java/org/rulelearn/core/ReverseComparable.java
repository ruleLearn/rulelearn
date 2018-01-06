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
 * This, contrary to {@link Comparable} contract, allows to implement calculation of comparison result
 * between object {@code a} and {@code b} in the class of object {@code b}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 * @param <T> class of objects that can be compared to this object
 */
public interface ReverseComparable<T> {
	
	/**
	 * Compares the other object to this object. Does the reverse comparison than {@link Comparable#compareTo(Object)}.
	 * 
	 * @param otherObject other object to be compared to this object
	 * @return negative number when the other object is smaller than this object,<br>
	 *         zero if both objects are equal,<br>
	 *         positive number when the other object is greater than this object
	 * 
	 * @throws ClassCastException if the other object's type prevents it from being compared to this object
	 * @throws NullPointerException if the other object is {@code null}
	 */
	abstract public int reverseCompareTo(T otherObject);

}
