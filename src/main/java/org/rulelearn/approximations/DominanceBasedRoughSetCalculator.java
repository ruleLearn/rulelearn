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
import static org.rulelearn.core.Precondition.notNull;

/**
 * Contract for classes capable of calculating dominance-based rough approximations and boundaries of unions of decision classes.
 * 
 * TODO: move parameter union to class constructors?
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
	 */
	public DominanceBasedRoughSetCalculator() {}
	
	/**
	 * TODO: write javadoc
	 * 
	 * @param lowerApproximationConsistencyMeasures array with object consistency measures applied when calculating lower approximation
	 * @param lowerApproximationConsistencyThresholds array with thresholds for object consistency measures applied when calculating lower approximation
	 * 
	 * @throws InvalidValueException if numbers of object consistency measures and respective thresholds are different
	 */
	public DominanceBasedRoughSetCalculator(ObjectConsistencyMeasure[] lowerApproximationConsistencyMeasures, double[] lowerApproximationConsistencyThresholds) {
		notNull(lowerApproximationConsistencyMeasures, "Consistency measures are null.");
		notNull(lowerApproximationConsistencyThresholds, "Consistency thresholds are null.");
		
		if (lowerApproximationConsistencyMeasures.length != lowerApproximationConsistencyThresholds.length) {
			throw new InvalidValueException("Numbers of object consistency measures and respective thresholds are different.");
		}
	}
	
	/**
	 * TODO: write javadoc
	 * 
	 * @param lowerApproximationConsistencyMeasures array with object consistency measures applied when calculating lower approximation
	 * @param lowerApproximationConsistencyThresholds array with thresholds for object consistency measures applied when calculating lower approximation
	 * @param upperApproximationConsistencyMeasures array with object consistency measures applied when calculating upper approximation
	 * @param upperApproximationConsistencyThresholds array with thresholds for object consistency measures applied when calculating upper approximation
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if numbers of lower approximation object consistency measures and respective thresholds are different
	 * @throws InvalidValueException if numbers of upper approximation object consistency measures and respective thresholds are different
	 */
	public DominanceBasedRoughSetCalculator(ObjectConsistencyMeasure[] lowerApproximationConsistencyMeasures, double[] lowerApproximationConsistencyThresholds,
			ObjectConsistencyMeasure[] upperApproximationConsistencyMeasures, double[] upperApproximationConsistencyThresholds) {
		notNull(lowerApproximationConsistencyMeasures, "Lower approximation consistency measures are null.");
		notNull(lowerApproximationConsistencyThresholds, "Lower approximation consistency thresholds are null.");
		
		notNull(upperApproximationConsistencyMeasures, "Upper approximation consistency measures are null.");
		notNull(upperApproximationConsistencyThresholds, "Upper approximation consistency thresholds are null.");
		
		if (lowerApproximationConsistencyMeasures.length != lowerApproximationConsistencyThresholds.length) {
			throw new InvalidValueException("Numbers of lower approximation object consistency measures and respective thresholds are different.");
		}
		
		if (upperApproximationConsistencyMeasures.length != upperApproximationConsistencyThresholds.length) {
			throw new InvalidValueException("Numbers of upper approximation object consistency measures and respective thresholds are different.");
		}
	}
	
}
