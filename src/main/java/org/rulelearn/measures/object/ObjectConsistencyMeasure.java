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

package org.rulelearn.measures.object;

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.measures.ConsistencyMeasureType;

/**
 * Contract of all object consistency measures.
 * 
 * @param <T> type of approximated set for which the object consistency measure can be calculated
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ObjectConsistencyMeasure<T extends ApproximatedSet> {
	
	/**
	 * Calculates consistency of the given object with respect to the given set of objects.
	 * 
	 * @param objectIndex index of an object in the information table for which approximated set is defined
	 * @param approximatedSet approximated set of objects
	 * @return consistency of the given object with respect to the given set of objects
	 */
	public double consistency(int objectIndex, T approximatedSet);
	
	/**
	 * Calculates consistency of the given object with respect to the given set of objects and checks whether a given threshold is reached.
	 * 
	 * @param objectIndex index of an object in the information table for which approximated set is defined
	 * @param approximatedSet approximated set of objects
	 * @param threshold threshold specified for the  consistency measure
	 * @return consistency of the given object with respect to the given set of objects
	 */
	public boolean isConsistencyThresholdReached(int objectIndex, T approximatedSet, double threshold);
	
	/**
	 * Gets type of this consistency measure.
	 * 
	 * @return see {@link ConsistencyMeasureType}
	 */
	public ConsistencyMeasureType type();
	
}
