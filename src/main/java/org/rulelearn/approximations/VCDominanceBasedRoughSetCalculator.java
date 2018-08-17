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

import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.measures.ConsistencyMeasure;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Calculator of variable consistency rough approximations of a union of decision classes according to Variable Consistency Dominance-based Rough Set Approach (VC-DRSA). 
 * For more details about VC-DRSA please refer to Błaszczyński, J., Greco, S., Słowiński, R., Szeląg, M.: 
 * Monotonic variable consistency rough set approaches. International Journal of Approximate Reasoning 50(7), 979--999 (2009).
 * 
 * The precise definition implemented here is more general and allows proper handling of missing values. 
 * It involves standard and inverted dominance relations as defined in M. Szeląg, J. Błaszczyński, R. Słowiński, 
 * Rough Set Analysis of Classification Data with Missing Values. [In]:
 * L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017, Proceedings, Part I.
 * Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class VCDominanceBasedRoughSetCalculator implements ExtendedDominanceBasedRoughSetCalculator {
	
	/**
	 * Consistency measure {@link ConsistencyMeasure} applied when calculating lower approximation of union of decision classes. 
	 */
	protected ConsistencyMeasure<Union> lowerApproximationConsistencyMeasure;
	
	/**
	 * Threshold on consistency measure {@link ConsistencyMeasure} applied when calculating lower approximation of union of decision classes. 
	 */
	protected double lowerApproximationConsistencyThreshold;
	
	/**
	 * Constructs calculator for specified consistency measure and threshold value used to limit consitency of objects included in 
	 * extended lower approximation of a union of decision classes. 
	 * 
	 * @param lowerApproximationConsistencyMeasure consistency measure applied when calculating lower approximation
	 * @param lowerApproximationConsistencyThreshold threshold for object consistency measures applied when calculating lower approximation
	 * 
	 * @throws NullPointerException if lower approximation consistency measure is {@code null}
	 */
	public VCDominanceBasedRoughSetCalculator(ConsistencyMeasure<Union> lowerApproximationConsistencyMeasure, double lowerApproximationConsistencyThreshold) {
		super();
		notNull(lowerApproximationConsistencyMeasure, "Consistency measure is null.");
		
		this.lowerApproximationConsistencyMeasure = lowerApproximationConsistencyMeasure;
		this.lowerApproximationConsistencyThreshold = lowerApproximationConsistencyThreshold;
	}
		
	/**
	 * Gets consistency measure applied when calculating lower approximation.
	 * 
	 * @return consistency measure applied when calculating lower approximation
	 */
	public ConsistencyMeasure<Union> getLowerApproximationConsistencyMeasure() {
		return lowerApproximationConsistencyMeasure;
	}

	/**
	 * Gets threshold for object consistency measures applied when calculating lower approximation.
	 * 
	 * @return threshold for object consistency measures applied when calculating lower approximation
	 */
	public double getLowerApproximationConsistencyThreshold() {
		return lowerApproximationConsistencyThreshold;
	}

	/**
	 * Calculates extended (variable consistency) lower approximation of a union of decision classes.
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return set of indices of objects belonging to the lower approximation of the given union
	 */
	@Override
	public IntSortedSet calculateLowerApproximation(Union union) {
		IntSortedSet lowerApproximationObjects = new IntLinkedOpenHashSet();
		IntIterator unionObjectIndicesIterator  = union.getObjects().iterator();
		
		int objectIndex;
		while (unionObjectIndicesIterator.hasNext()) {
			objectIndex = unionObjectIndicesIterator.nextInt();
			if (this.lowerApproximationConsistencyMeasure.isConsistencyThresholdReached(objectIndex, union, lowerApproximationConsistencyThreshold)) {
				lowerApproximationObjects.add(objectIndex);		
			}
		}
		return lowerApproximationObjects;
	}
	
	/**
	 * Calculates variable consistency upper approximation of a union of decision classes.
	 *  
	 * @param union union of interest; should not be {@code null}
	 * @return set of indices of objects belonging to the upper approximation of the given union
	 */
	@Override
	public IntSortedSet calculateUpperApproximation(Union union) {
		IntSortedSet upperApproximationObjects = new IntLinkedOpenHashSet();
		int objectsCount = union.getInformationTable().getNumberOfObjects();
		
		IntSortedSet compementaryLowerApproximationObjects = union.getComplementaryUnion().getLowerApproximation();
		for (int i = 0; i < objectsCount; i++) {
			// check whether object i is in lower approximation of complement union
			if (!compementaryLowerApproximationObjects.contains(i)) {
				upperApproximationObjects.add(i);
			}
		}
		return upperApproximationObjects;
	}
}
