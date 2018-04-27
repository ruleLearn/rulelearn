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

package org.rulelearn.approximations;

/**
 * Extended rough set calculator, capable of calculating both the lower approximation of a set of objects and the set of objects from the information table
 * that are inconsistent with the objects belonging to that lower approximation.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ExtendedRoughSetCalculator<T extends ApproximatedSet> extends RoughSetCalculator<T> {

	/**
	 * Gets a compound object containing:<br>
	 * - the set of indices of objects belonging to the lower approximation of an approximated set, and<br>
	 * - the set of indices of objects that are inconsistent with the objects belonging to that lower approximation.
	 * 
	 * @param set set of objects that is going to be approximated
	 * @return see {@link LowerApproximationAndInconsistentObjects}
	 */
	public abstract LowerApproximationAndInconsistentObjects getLowerApproximationAndInconsistentObjects(T set);	

}
