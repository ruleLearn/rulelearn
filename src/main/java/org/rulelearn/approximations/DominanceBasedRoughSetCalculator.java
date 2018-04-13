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

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.measures.object.ObjectConsistencyMeasure;

import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Contract for classes capable of calculating dominance-based rough approximations and boundaries of unions of decision classes.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class DominanceBasedRoughSetCalculator {

	public abstract IntSortedSet getLowerApproximation(Union union);
	public abstract IntSortedSet getUpperApproximation(Union union);
	public abstract IntSortedSet getBoundary(Union union);
	
	/**
	 * TODO: write javadoc
	 * 
	 * @param consistencyMeasures
	 * @param thresholds
	 * 
	 * @throws InvalidValueException if numbers of object consistency measures and respective thresholds are different
	 */
	public DominanceBasedRoughSetCalculator(ObjectConsistencyMeasure[] consistencyMeasures, double[] thresholds) {
		if (consistencyMeasures != null && thresholds != null) {
			if (consistencyMeasures.length != thresholds.length) {
				throw new InvalidValueException("Numbers of object consistency measures and respective thresholds are different.");
			}
		}
	}
	
	//ObjectConsistencyMeasure consistencyMeasure, double threshold
}
