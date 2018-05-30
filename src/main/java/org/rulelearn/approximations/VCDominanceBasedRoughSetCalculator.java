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

import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.measures.ConsistencyMeasure;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * VCDominanceBasedRoughSetCalculator (assumes x \in X)
 * TODO: write javadoc
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class VCDominanceBasedRoughSetCalculator implements ExtendedDominanceBasedRoughSetCalculator {
	
	protected ConsistencyMeasure<Union> lowerApproximationConsistencyMeasure;
	protected double lowerApproximationConsistencyThreshold;

	
	/**
	 * TODO: add javadoc
	 * 
	 * @param lowerApproximationConsistencyMeasure object consistency measures applied when calculating lower approximation
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
	 * Calculate lower approximation of an union according to the definition...
	 * TODO: write javadoc
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return positive region {@link PositiveRegion} containing set of indices of objects belonging to the lower approximation of the union and complement 
	 * of the lower approximation
	 */
	@Override
	public IntSortedSet calculateLowerApproximation(Union union) {
		IntSortedSet lowerApproximationObjects = new IntLinkedOpenHashSet();
		IntIterator objectIndicesIterator  = union.getObjects().iterator();
		
		int objectIndex;
		while (objectIndicesIterator.hasNext()) {
			objectIndex = objectIndicesIterator.nextInt();
			if (this.lowerApproximationConsistencyMeasure.isConsistencyThresholdReached(objectIndex, union, lowerApproximationConsistencyThreshold)) {
				lowerApproximationObjects.add(objectIndex);		
			}
		}
		return lowerApproximationObjects;
	}
	
	/**
	 * Calculate upper approximation of an union according to the definition...
	 * TODO: write javadoc
	 * TODO: only one decision
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return set of indices of objects belonging to the upper approximation of the given set
	 */
	@Override
	public IntSortedSet calculateUpperApproximation(Union union) {
		IntSortedSet upperApproximationObjects = null;
		//check whether the union is defined for only one decision criterion
		if (union.getLimitingDecision().getNumberOfEvaluations() == 1) {
			InformationTableWithDecisionDistributions infromationTable = union.getInformationTable();
			int objectsCount = infromationTable.getNumberOfObjects();
			upperApproximationObjects = new IntLinkedOpenHashSet();
			
			IntSortedSet compLowerApproximationObjects = union.getComplementaryUnion().getLowerApproximation();
			notNull(compLowerApproximationObjects, "Complementary union is not set.");
			for (int i = 0; i < objectsCount; i++) {
				// check whether object i is in lower approximation of complement union
				if (!compLowerApproximationObjects.contains(i)) {
					upperApproximationObjects.add(i);
				}
			}
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return upperApproximationObjects;
	}
}
