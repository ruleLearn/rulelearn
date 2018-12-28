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

package org.rulelearn.rules;

import org.rulelearn.approximations.ApproximatedSet;

/**
 * Contract for a supplier of approximated sets {@link ApproximatedSet}. Each set is identified by its number on a list.
 * Each implementing class should ensure that the set of objects belonging to the approximated set obtained by {@code getApproximatedSet(i)}
 * does not include the set of objects belonging to the approximated set obtained by {@code getApproximatedSet(i+j)}, with j &gt; 0. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ApproximatedSetSupplier {

	/**
	 * Gets number of approximated sets that this supplier has to offer.
	 * 
	 * @return number of approximated sets offered by this supplier
	 */
	public int getCount();
	/**
	 * Gets i-th approximated set.
	 * 
	 * @param i index of requested approximated set
	 * @return i-th approximated set
	 * 
	 * @throws IndexOutOfBoundsException if given index is less than zero or
	 *         greater or equal to the number of available approximated sets
	 */
	public ApproximatedSet getApproximatedSet(int i);
	
}
