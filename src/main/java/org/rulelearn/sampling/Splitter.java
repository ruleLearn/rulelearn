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
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

/**
 * Splits {@link InformationTable information table (a data set)} into multiple information sub-tables (subsets of the data set).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Splitter {

	/**
	 * Splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter. Objects are selected from the information table into information sub-tables subsequently (i.e., according
	 * to the order of their occurrence in the information table).
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param splits an array with proportions, where the length is the number of {@link InformationTable information tables} to
     * construct; each value, in this array, is a proportion (fraction in interval (0, 1)) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter 
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} or splits array provided as parameters are {@code null}
	 * @throws IllegalArgumentException when size of array with split proportions is lower than 2 or sum of split proportions is greater than 1.0
	 * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	public static List<InformationTable> split(InformationTable informationTable, double... splits) {
		return split(informationTable, false, splits);
	}
	
	/**
	 * Splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter. Objects are selected from the information table into information sub-tables subsequently (i.e., according
	 * to the order of their occurrence in the information table).
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
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} or splits array provided as parameters are {@code null}
	 * @throws IllegalArgumentException when size of array with split proportions is lower than 2 or sum of split proportions is greater than 1.0
	 * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	public static List<InformationTable> split(InformationTable informationTable, boolean accelerateByReadOnlyResult, double... splits) {
		Precondition.notNull(informationTable, "Information table provided to split is null.");
		Precondition.notNull(splits, "Provided array with splits is null.");
		checkProportionsArray(splits);
		List<InformationTable> subTables = new ArrayList<InformationTable> (splits.length);
		int start = 0, stop, informationTableSize = informationTable.getNumberOfObjects(), j;
		int [] indices;
		for (double split : splits) {
			stop = start + (int)(split * informationTableSize);
			if (stop > informationTableSize) { // just in case of numeric approximation errors
				stop = informationTableSize;
			}
			indices = new int [stop-start];
			j = 0;
			for (int i = start; i < stop; i++) {
				indices[j++] = i;
			}
			subTables.add(informationTable.select(indices, accelerateByReadOnlyResult));
			start = stop;
		}
		return subTables;
	}
	
	/**
	 * Splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter, and according to the distribution of decisions in the information table (i.e., each constructed 
	 * sub-table has the same distribution of decisions as the original information table). More precisely, for each sub-table a proportion of objects
	 * having each of decisions present in the information table is selected. In result, not all objects from the information table must be selected 
	 * into sub-tables even if sum of proportions is equal 1.0. Objects are selected from the information table 
	 * into information sub-tables subsequently (i.e., according to the order of their occurrence in the information table).
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param splits an array with proportions, where the length is the number of {@link InformationTable information tables} to
     * construct; each value, in this array, is a proportion (fraction in interval (0, 1)) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter; each constructed information sub-table has the same distribution of decisions as the original information table 
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} or splits array provided as parameters are {@code null}
	 * @throws IllegalArgumentException when size of array with split proportions is lower than 2 or sum of split proportions is greater than 1.0
	 * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	public static List<InformationTable> stratifiedSplit(InformationTableWithDecisionDistributions informationTable, double... splits) {
		return stratifiedSplit(informationTable, false, splits);
	}
	
	/**
	 * Splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter, and according to the distribution of decisions in the information table (i.e., each constructed 
	 * sub-table has the same distribution of decisions as the original information table). More precisely, for each sub-table a proportion of objects
	 * having each of decisions present in the information table is selected. In result, not all objects from the information table must be selected 
	 * into sub-tables even if sum of proportions is equal 1.0. Objects are selected from the information table 
	 * into information sub-tables subsequently (i.e., according to the order of their occurrence in the information table).
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
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} or splits array provided as parameters are {@code null}
	 * @throws IllegalArgumentException when size of array with split proportions is lower than 2 or sum of split proportions is greater than 1.0
	 * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	public static List<InformationTable> stratifiedSplit(InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, double... splits) {
		Precondition.notNull(informationTable, "Information table provided to split is null.");
		Precondition.notNull(splits, "Provided array with splits is null.");
		checkProportionsArray(splits);
		// initialization
		List<InformationTable> subTables = new ArrayList<InformationTable> (splits.length);;
		Set<Decision> decisions = informationTable.getDecisionDistribution().getDecisions();
		int numberOfDecisions = decisions.size();
		Map<Decision, Integer> starts = new Object2IntLinkedOpenHashMap<Decision>(numberOfDecisions);
		Map<Decision, IntArrayList> objectsToSelect = new Object2ObjectLinkedOpenHashMap<Decision, IntArrayList>(numberOfDecisions);
		Decision [] decisionsSet = informationTable.calculateUniqueDecisions();
		for (Decision decision : decisionsSet) {
			starts.put(decision, 0);
			objectsToSelect.put(decision, new IntArrayList(informationTable.getDecisionDistribution().getCount(decision)));
		}
		Decision[] objectDecisions = informationTable.getDecisions();
		if (objectDecisions != null) {
			IntArrayList list;
			for (int i = 0; i < objectDecisions.length; i++) {
				list = objectsToSelect.get(objectDecisions[i]);
				list.add(i);
			}
			// selection
			int numberOfObjects = informationTable.getNumberOfObjects(), start, stop, maxToSelectForDecison;
			IntArrayList indices;
			for (double split : splits) {
				indices = new IntArrayList((int)(split * numberOfObjects));
				for (Decision decision : decisionsSet) {
					start = starts.get(decision); 
					maxToSelectForDecison = informationTable.getDecisionDistribution().getCount(decision);
					stop = start + (int)(split * maxToSelectForDecison);
					if (stop > maxToSelectForDecison) { // just in case of numeric approximation errors
						stop = maxToSelectForDecison;
					}
					list = objectsToSelect.get(decision);
						for (int i = start; i < stop; i++) {
						indices.add(list.getInt(i));
					}
					starts.put(decision, stop);
				}
				subTables.add(informationTable.select(indices.toIntArray(), accelerateByReadOnlyResult));
			}
		}
		return subTables;
	}
	
	/**
	 * Randomly splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter.
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param random the source of randomness
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
	public static List<InformationTable> randomSplit(InformationTable informationTable, Random random, double... splits) {
		return randomSplit(informationTable, false, random, splits);
	}
	
	/**
	 * Randomly splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter.
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param random the source of randomness
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
	public static List<InformationTable> randomSplit(InformationTable informationTable, boolean accelerateByReadOnlyResult, Random random, double... splits) {
		Precondition.notNull(informationTable, "Information table provided to split is null.");
		Precondition.notNull(random, "Provided random generator is null.");
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
	 * Randomly splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter, and according to the distribution of decisions in the information table (i.e., each constructed 
	 * sub-table has the same distribution of decisions as the original information table). 
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param random the source of randomness
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
	public static List<InformationTable> randomStratifiedSplit(InformationTableWithDecisionDistributions informationTable, Random random, double... splits) {
		return randomStratifiedSplit(informationTable, false, random, splits);
	}
	
	/**
	 * Randomly splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter, and according to the distribution of decisions in the information table (i.e., each constructed 
	 * sub-table has the same distribution of decisions as the original information table). 
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning read-only information tables, or should return safe information tables (which may be modified),
	 *        at the cost of returning the result slower
	 * @param random the source of randomness
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
	public static List<InformationTable> randomStratifiedSplit(InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, Random random, double... splits) {
		Precondition.notNull(informationTable, "Information table provided to split is null.");
		Precondition.notNull(random, "Provided random generator is null.");
		Precondition.notNull(splits, "Provided array with splits is null.");
		checkProportionsArray(splits);
		// initialization
		List<InformationTable> subTables = new ArrayList<InformationTable> (splits.length);;
		Set<Decision> decisions = informationTable.getDecisionDistribution().getDecisions();
		int numberOfDecisions = decisions.size(), numberOfObjectsForDecision;
		Map<Decision, IntArrayList> objectsToSelect = new Object2ObjectLinkedOpenHashMap<Decision, IntArrayList>(numberOfDecisions);
		Map<Decision, BitSet> pickedObjects = new Object2ObjectLinkedOpenHashMap<Decision, BitSet>(numberOfDecisions);
		Decision [] decisionsSet = informationTable.calculateUniqueDecisions(); 
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
		return subTables;
	}
	
	/**
	 * Checks array with proportions - i.e., it should have not less than 2 elements and sum of its elements should be less than or equal to 1.0.
	 * 
	 * @param proportions an array with proportions in range [0, 1]
     * 
     * @throws IllegalArgumentException when size of array with proportions is lower than 2 or sum of proportions is greater than 1.0
     * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	static void checkProportionsArray (double... proportions) {
		if(proportions.length < 2)
            throw new IllegalArgumentException("Size of array with proportions must not be lower than 2.");
		
        double sum = 0;
        for(int i = 0; i < proportions.length; i++) {
        	sum += Precondition.within01Interval(proportions[i], "At least one proportion is not in interval [0, 1].");
            // check whether fractions sum up to 1 (with some flexibility) in case of numeric approximation errors
            if(sum >= 1.0001) {
                throw new IllegalArgumentException("Sum of proportions is greater than 1.0 by index " + (i+1) + ", and it is reaching value " + sum);
            }
        }
	}
	
}
