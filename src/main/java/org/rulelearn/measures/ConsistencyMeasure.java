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

package org.rulelearn.measures;

import org.rulelearn.approximations.ApproximatedSet;

/**
 * Contract for all classes representing consistency measures.
 * 
 * @param <T> type of approximated set for which the consistency measure can be calculated
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ConsistencyMeasure<T extends ApproximatedSet> extends Measure {
	
	/**
	 * Calculates consistency of the given object with respect to the given set of objects.
	 * 
	 * @param objectIndex index of an object in the information table for which approximated set is defined
	 * @param set approximated set of objects
	 * @return consistency of the given object with respect to the given set of objects
	 */
	public double calculateConsistency(int objectIndex, T set);
	
	/**
	 * Calculates consistency of the given object with respect to the given set of objects and checks whether a given threshold is reached.
	 * 
	 * @param objectIndex index of an object in the information table for which approximated set is defined
	 * @param set approximated set of objects
	 * @param threshold threshold specified for the  consistency measure
	 * @return consistency of the given object with respect to the given set of objects
	 */
	public default boolean isConsistencyThresholdReached(int objectIndex, T set, double threshold) {
		boolean reached = false;
		if (this.getType() == MeasureType.GAIN) {
			if (this.calculateConsistency(objectIndex, set) >= threshold)
				reached = true;
		}
		else if (this.getType() == MeasureType.COST) {
			if (this.calculateConsistency(objectIndex, set) <= threshold)
				reached = true;
		}
		return reached;
	}
	
}
