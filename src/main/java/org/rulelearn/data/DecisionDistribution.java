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

package org.rulelearn.data;

import static org.rulelearn.core.Precondition.notNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.rulelearn.core.InvalidSizeException;

//import org.rulelearn.approximations.Union;
//import org.rulelearn.core.TernaryLogicValue;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Distribution (histogram) of decisions in the set of considered objects (information table). For any decision observed in an information table,
 * this distribution offers information regarding how many objects share this decision. For example, if there are 7 objects in an information table,
 * three of them have decision 1, and four of them have decision 2, then this distribution will map decision 1 to value 3, and decision 2 to value 4.
 * So, this distribution is a kind of a map, where each key corresponds to a decision, and each value corresponds to the number of occurrences of the considered decision.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DecisionDistribution {
	/**
	 * Maps decision to number of objects having this decision.
	 */
	protected Object2IntMap<Decision> decision2CountMap;
	
	/**
	 * Sole constructor.
	 */
	public DecisionDistribution() {
		this.decision2CountMap = new Object2IntOpenHashMap<Decision>();
	}
	
	/**
	 * Constructs this distribution based on the given information table.
	 * 
	 * @param informationTable information table for which decision distribution should be constructed; this table should contain decisions for subsequent objects
	 * @throws NullPointerException if given information table is {@code null}
	 * @throws NullPointerException if given information table does contain decision for some object
	 */
	public DecisionDistribution(InformationTable informationTable) {
		notNull(informationTable, "Information table for calculation of distribution of decisions is null.");
		this.decision2CountMap = new Object2IntOpenHashMap<Decision>();
		
		int numberOfObjects = informationTable.getNumberOfObjects();
		for (int i = 0; i < numberOfObjects; i++) {
			this.increaseCount(informationTable.getDecision(i));
		}
	}
	
	/**
	 * Checks whether a given decision is present in this distribution (i.e., object/objects having given decision are present in the information table for which this distribution
	 * has been constructed).
	 * 
	 * @param decision decision of interest; should not be {@code null}
	 * @return true if a given decision is present in the distribution
	 */
	public boolean isPresent(Decision decision) {
		return this.decision2CountMap.containsKey(decision);
	}
	
	/**
	 * Gets all decisions, which are present in the distribution.
	 * 
	 * @return {@link Set set} of decisions
	 */
	public Set<Decision> getDecisions() {
		return this.decision2CountMap.keySet();
	}
	
	/**
	 * Gets number of objects having given decision.
	 * If there are no such objects, returns zero.
	 * 
	 * @param decision decision of interest; should not be {@code null}
	 * @return number of objects having given decision
	 */
	public int getCount(Decision decision) {
		return this.decision2CountMap.containsKey(decision) ? this.decision2CountMap.getInt(decision) : 0;
	}
	
	/**
	 * Increases by one the number of objects having given decision.
	 * 
	 * @param decision decision of interest; should not be {@code null}
	 * @throws NullPointerException if given decision is {@code null}
	 */
	public void increaseCount(Decision decision) {
		notNull(decision, "Could not increase count of a null decision.");
		int count = this.decision2CountMap.containsKey(decision) ? this.decision2CountMap.getInt(decision) : 0;
		this.decision2CountMap.put(decision, ++count);
	}
	
	/**
	 * Gets hash code of this decision distribution.
	 * 
	 * @return calculated hash code of this decision distribution
	 */
	public int hashCode() {
		return Objects.hash(this.getClass(), this.decision2CountMap);
	}
	
	/**
	 * Tests if this decision distribution is equal to the given object. Returns {@code true} if the given object is also a decision distribution,
	 * and the two distributions concern the same decisions with the same cardinalities.
	 * 
	 * @param otherObject other object that this decision distribution should be compared with
	 * @return {@code true} if this decision distribution is equal to the given objects,
	 *         {@code false} otherwise
	 */
	public boolean equals(Object otherObject) {
		if (otherObject instanceof DecisionDistribution &&
			this.decision2CountMap.equals(((DecisionDistribution)otherObject).decision2CountMap)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets number of different decisions in this distribution.
	 * 
	 * @return number of different decisions in this distribution
	 */
	public int getDifferentDecisionsCount() {
		return this.decision2CountMap.size();
	}
	
	/**
	 * Gets list of most frequent decisions in this distribution.
	 * The decisions are not returned in any particular order.
	 * 
	 * @return list of most frequent decisions in this distribution
	 *         or {@code null} if this distribution contains no decision
	 */
	public List<Decision> getMode() {
		Set<Decision> decisions = getDecisions();
		int count;
		int maxCount = 0;
		List<Decision> mostFrequentDecisions = null;
		
		for (Decision decision : decisions) {
			count = getCount(decision);
			if (count > maxCount) {
				maxCount = count;
			}
		}
		
		if (maxCount > 0) {
			mostFrequentDecisions = new ObjectArrayList<Decision>();
			for (Decision decision : decisions) {
				count = getCount(decision);
				if (count == maxCount) { //one of the most frequent classes
					mostFrequentDecisions.add(decision);
				}
			}
		}
		
		return mostFrequentDecisions;
	}

	/**
	 * Gets median value in this distribution, concerning given order of the decisions contained in this distribution.
	 * For example, if the distribution is: class 1 - 100 objects, class 2 - 99 object, then median will be class 1.
	 * If the distribution is: class 1 - 100 objects, class 2 - 100 object, then median will also be class 1.
	 * So, if the number of objects is even, and median decision falls between two decisions, than left decision is chosen.
	 * 
	 * @param orderedUniqueFullyDeterminedDecisions array with ordered unique fully-determined decisions;
	 *        such array can be obtained, e.g., from information table using method {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()}
	 * @return median value in this distribution, with respect to given order of the decisions contained in this distribution
	 * 
	 * @throws NullPointerException if supplied ordered unique fully determined decisions are {@code null}
	 * @throws InvalidSizeException if the number supplied of ordered unique fully determined decisions is different than the number of different decisions present in this distribution
	 */
	public Decision getMedian(Decision[] orderedUniqueFullyDeterminedDecisions) {
		notNull(orderedUniqueFullyDeterminedDecisions, "Supplied ordered unique fully determined decisions are null.");
		if (this.getDifferentDecisionsCount() != orderedUniqueFullyDeterminedDecisions.length) {
			throw new InvalidSizeException("Supplied number of ordered unique fully determined decisions is different than number of different decisions present in decision distribution.");
		}
		Decision median;
		
		int[] cumulativeSums = new int[orderedUniqueFullyDeterminedDecisions.length];
		int cumulativeSum = 0;

		for (int i = 0; i < cumulativeSums.length; i++) {
			cumulativeSum += getCount(orderedUniqueFullyDeterminedDecisions[i]);
			cumulativeSums[i] = cumulativeSum;
		}

		int roundedHalfOfCumulativeSum = (int)Math.round((double)cumulativeSum / 2); //rounds 0.5 upwards, to point to a proper decision if cumulativeSum is odd
		median = null;

		for (int i = 0; i < cumulativeSums.length; i++) {
			//TODO: if cumulativeSum is even, and there is a switch of decision between element cumulativeSum / 2 and cumulativeSum / 2 + 1, then take median randomly, using seed
			if (cumulativeSums[i] >= roundedHalfOfCumulativeSum) { //median decision found
				median = orderedUniqueFullyDeterminedDecisions[i];
				break;
			}
		}
		
		return median;
	}

}
