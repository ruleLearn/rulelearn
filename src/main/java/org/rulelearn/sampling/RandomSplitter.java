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

package org.rulelearn.sampling;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

/**
 * RandomSplitter.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RandomSplitter implements Splitter {
	
	/**
	 * Source of randomness.
	 */
	Random random = null;

	/** 
	 * Constructor setting random generator (i.e., the source of randomness for this splitter).
	 * 
	 * @param random random generator
	 * 
	 * @throws NullPointerException when the {@link Random random generator} is {@code null}
	 */
	public RandomSplitter(Random random) {
		this.random = Precondition.notNull(random, "Provided random generator is null.");
	}
	
	/**
	 * Randomly splits {@link InformationTable an information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter.
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning read-only information tables, or should return safe information tables (which may be modified),
	 *        at the cost of returning the result slower
	 * @param splits an array with proportions, where the length is the number of {@link InformationTable information tables} to
     * construct; each value, in this array, is a proportion (fraction in interval (0, 1)) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table}, splits array or source of randomness 
	 * provided as parameters are {@code null}
	 * @throws IllegalArgumentException when size of array with split proportions is lower than 2 or sum of split proportions is greater than 1.0
	 * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	@Override
	public List<InformationTable> split(InformationTable informationTable, boolean accelerateByReadOnlyResult, double... splits) {
		Precondition.notNull(informationTable, "Information table provided to split is null.");
		Precondition.notNull(splits, "Provided array with splits is null.");
		checkProportionsArray(splits);
		List<InformationTable> subTables = new ArrayList<InformationTable> (splits.length);
		int informationTableSize = informationTable.getNumberOfObjects(), splitSize, i, j;
		BitSet picked = new BitSet(informationTableSize);
		int [] indices; 
		for (double split : splits) {
			splitSize =  (int)(split * informationTableSize);
			indices = new int [splitSize];
			i = 0;
			while (i < splitSize) {
				j = random.nextInt(informationTableSize);
				if (!picked.get(j)) {
					indices[i++] = j;
					picked.set(j);
				}
			}
			subTables.add(informationTable.select(indices, accelerateByReadOnlyResult));
		}
		return subTables;
	}
		
	/**
	 * Randomly splits {@link InformationTable an information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter, and according to the distribution of decisions in the information table (i.e., each constructed 
	 * sub-table has the same distribution of decisions as the original information table). 
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning read-only information tables, or should return safe information tables (which may be modified),
	 *        at the cost of returning the result slower
	 * @param splits an array with proportions, where the length is the number of {@link InformationTable information tables} to
     * construct; each value, in this array, is a proportion (fraction in interval (0, 1)) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter; each constructed information sub-table has the same distribution of decisions as the original information table
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table}, splits array or source of randomness
	 * provided as parameters are {@code null}
	 * @throws IllegalArgumentException when size of array with split proportions is lower than 2 or sum of split proportions is greater than 1.0
	 * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	@Override
	public List<InformationTable> splitStratified(InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, double... splits) {
		Precondition.notNull(informationTable, "Information table provided to split is null.");
		Precondition.notNull(splits, "Provided array with splits is null.");
		checkProportionsArray(splits);
		// initialization
		List<InformationTable> subTables = new ArrayList<InformationTable> (splits.length);
		Set<Decision> decisions = informationTable.getDecisionDistribution().getDecisions();
		int numberOfDecisions = decisions.size(), numberOfObjectsForDecision;
		Map<Decision, IntArrayList> objectsToSelect = new Object2ObjectLinkedOpenHashMap<Decision, IntArrayList>(numberOfDecisions);
		Map<Decision, BitSet> pickedObjects = new Object2ObjectLinkedOpenHashMap<Decision, BitSet>(numberOfDecisions);
		Decision [] decisionsSet = informationTable.getUniqueDecisions();
		if (decisionsSet != null) {
			for (Decision decision : decisionsSet) {
				numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
				objectsToSelect.put(decision, new IntArrayList(numberOfObjectsForDecision));
				pickedObjects.put(decision, new BitSet(numberOfObjectsForDecision));
			}
			Decision[] objectDecisions = informationTable.getDecisions();
			if (objectDecisions != null) {
				IntArrayList list;
				for (int i = 0; i < objectDecisions.length; i++) {
					list = objectsToSelect.get(objectDecisions[i]);
					list.add(i);
				}
				// selection
				IntArrayList indices;
				BitSet picked;
				int numberOfObjects = informationTable.getNumberOfObjects(), splitForDecisionSize, i, j;
				for (double split : splits) {
					indices = new IntArrayList((int)(split * numberOfObjects));
					for (Decision decision : decisionsSet) {
						numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
						splitForDecisionSize = (int)(split * numberOfObjectsForDecision);
						list = objectsToSelect.get(decision);
						picked = pickedObjects.get(decision);
						i = 0;
						while (i < splitForDecisionSize) {
							j = random.nextInt(numberOfObjectsForDecision);
							if (!picked.get(j)) {
								indices.add(list.getInt(j));
								picked.set(j);
								i++;
							}
						}
					}
					subTables.add(informationTable.select(indices.toIntArray(), accelerateByReadOnlyResult));
				}
			}
		}
		return subTables;
	}
	
}
