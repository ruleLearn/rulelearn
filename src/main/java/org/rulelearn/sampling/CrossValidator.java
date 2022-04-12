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
import java.util.Arrays;
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
 * Splits {@link InformationTable an information table (a data set)} into multiple disjoint information sub-tables (subsets of the data set).
 * The splitting is organized according to cross-validation technique, sometimes also called rotation estimation, or out-of-sample testing.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CrossValidator {
	
	/** 
	 * Cross-validation fold consisting of two sub-tables: {@link InformationTable training table} and {@link InformationTable validation table}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public class CrossValidationFold <T extends InformationTable> {
		T trainingTable;
		T validationTable;
		
		/**
		 * Constructs this cross validator.
		 * 
		 * @param trainingSet training set of objects
		 * @param validationSet test (validation) set of objects
		 */
		public CrossValidationFold(T trainingSet, T validationSet) {
			this.trainingTable = trainingSet;
			this.validationTable = validationSet;
		}
		/**
		 * Gets training information table.
		 * @return training information table
		 */
		public T getTrainingTable() {
			return trainingTable;
		}
		/**
		 * Gets test (validation) information table.
		 * @return test (validation) information table
		 */
		public T getValidationTable() {
			return validationTable;
		}
	}
	
	/**
	 * Source of randomness.
	 */
	Random random = null;
	
	/** 
	 * Constructor setting random generator (i.e., the source of randomness for this cross-validator).
	 * 
	 * @param random random generator
	 * 
	 * @throws NullPointerException when the {@link Random random generator} is {@code null}
	 */
	public CrossValidator(Random random) {
		this.random = Precondition.notNull(random, "Provided random generator is null.");
	}
	
	/**
	 * Sets the seed of the random number generator.
	 * 
	 * @param seed new seed of the random number generator
	 * @return {@code true} if given seed has been successfully set, {@code false otherwise}
	 */
	public boolean setSeed(long seed) {
		try {
			this.random.setSeed(seed);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}
	
	/**
	 * Randomly splits {@link InformationTable an information table} provided as a parameter into a given number k (also provided as a parameter) 
	 * of {@link CrossValidationFold folds}. Each fold consists of two disjoint sub-tables of the information table. 
	 *
	 * @param informationTable {@link InformationTable information table} which will be split into folds
	 * @param k number of folds
	 * 
	 * @return list with k {@link CrossValidationFold folds}
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} is {@code null}
	 * @throws InvalidValueException when the provided number of folds is negative
	 */
	public List<CrossValidationFold<InformationTable>> splitIntoKFold (InformationTable informationTable, int k) {
		return splitIntoKFold(informationTable, false, k);
	}
	
	/**
	 * Randomly splits {@link InformationTable an information table} provided as a parameter into a given number k (also provided as a parameter) 
	 * of {@link CrossValidationFold folds}. Each fold consists of two disjoint sub-tables of the information table.
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into folds
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning read-only information tables, or should return folds with safe information tables (which may be modified),
	 *        at the cost of returning the result slower
	 * @param k number of folds
	 * 
	 * @return k {@link CrossValidationFold folds}
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} is {@code null}
	 * @throws InvalidValueException when the provided number of folds is negative
	 */
	public List<CrossValidationFold<InformationTable>> splitIntoKFold (InformationTable informationTable, boolean accelerateByReadOnlyResult, int k) {
		Precondition.notNull(informationTable, "Information table provided to cross-validate is null.");
		Precondition.nonNegative(k, "Provided number of folds is negative.");
		List<CrossValidationFold<InformationTable>> folds = new ArrayList<CrossValidationFold<InformationTable>> (k);
		
		int informationTableSize = informationTable.getNumberOfObjects(), numberSelectedSoFar = 0, splitSize, i, j;
		BitSet picked = new BitSet(informationTableSize);
		int [] indices; 
		for (int l = 0; l < k; l++) {
			if (l == (k-1)) {
				// in the last fold - take all that remained
				indices = new int [informationTableSize - numberSelectedSoFar];
				i = 0;
				for (j = 0; j < informationTableSize; j++) {
					if (!picked.get(j)) {
						indices[i++] = j;
						picked.set(j);
						numberSelectedSoFar++;
					}
				}
			}
			else {
				splitSize =  (int)(informationTableSize/k);
				indices = new int [splitSize];
				i = 0;
				while (i < splitSize) {
					j = random.nextInt(informationTableSize);
					if (!picked.get(j)) {
						indices[i++] = j;
						picked.set(j);
						numberSelectedSoFar++;
					}
				}
			}
			
			Arrays.sort(indices); //sort indices, so the objects in the validation table are also in the original order
			
			folds.add(new CrossValidationFold<InformationTable>(informationTable.discard(indices, accelerateByReadOnlyResult), informationTable.select(indices, accelerateByReadOnlyResult)));
		}
		
		return folds;
	}
	
	/**
	 * Randomly splits {@link InformationTable an information table} provided as a parameter into a given number k (also provided as a parameter) 
	 * of {@link CrossValidationFold folds}. Each fold consists of two disjoint sub-tables of the information table. 
	 * This splitting preserves distribution of decisions in the information table (i.e., each constructed sub-table has the same distribution of 
	 * decisions as the original information table).
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into folds
	 * @param k number of folds
	 * 
	 * @return k {@link CrossValidationFold folds}
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} is {@code null}
	 * @throws InvalidValueException when the provided number of folds is negative
	 */
	public List<CrossValidationFold<InformationTable>> splitStratifiedIntoKFold (InformationTableWithDecisionDistributions informationTable, int k) {
		return splitStratifiedIntoKFold(informationTable, false, k);
	}
	
	/**
	 * Randomly splits {@link InformationTable an information table} provided as a parameter into a given number k (also provided as a parameter) 
	 * of {@link CrossValidationFold folds}. Each fold consists of two disjoint sub-tables of the information table. 
	 * This splitting preserves distribution of decisions in the information table (i.e., each constructed sub-table has the same distribution of
	 * decisions as the original information table).
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into folds
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning read-only information tables, or should return folds with safe information tables (which may be modified),
	 *        at the cost of returning the result slower
	 * @param k number of folds
	 * 
	 * @return k {@link CrossValidationFold folds}
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} is {@code null}
	 * @throws InvalidValueException when the provided number of folds is negative
	 */
	public List<CrossValidationFold<InformationTable>> splitStratifiedIntoKFold (InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, int k) {
		Precondition.notNull(informationTable, "Information table provided to cross-validate is null.");
		Precondition.nonNegative(k, "Provided number of folds is negative.");
		List<CrossValidationFold<InformationTable>> folds = new ArrayList<CrossValidationFold<InformationTable>> (k);
		
		// initialization
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
				IntArrayList indices = null;
				BitSet picked = null;
				int numberOfObjects = informationTable.getNumberOfObjects(), numberSelectedSoFar = 0, splitForDecisionSize, i, j;
				for (int l = 0; l < k; l++) {
					if (l == (k-1)) {
						// in the last fold - take all that remained
						indices = new IntArrayList(numberOfObjects - numberSelectedSoFar);
						for (Decision decision : decisionsSet) {
							numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
							list = objectsToSelect.get(decision);
							picked = pickedObjects.get(decision);
							for (j = 0; j < numberOfObjectsForDecision; j++) {
								if (!picked.get(j)) {
									indices.add(list.getInt(j));
									picked.set(j);
									numberSelectedSoFar++;
								}
							}
						}
					}
					else {
						indices = new IntArrayList((int)(((double)numberOfObjects)/k));
						for (Decision decision : decisionsSet) {
							numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
							splitForDecisionSize = (int)(((double)numberOfObjectsForDecision)/k);
							list = objectsToSelect.get(decision);
							picked = pickedObjects.get(decision);
							i = 0;
							while (i < splitForDecisionSize) {
								j = random.nextInt(numberOfObjectsForDecision);
								if (!picked.get(j)) {
									indices.add(list.getInt(j));
									picked.set(j);
									numberSelectedSoFar++;
									i++;
								}
							}
						}
					}
					
					int[] indicesArray = indices.toIntArray();
					Arrays.sort(indicesArray); //sort indices, so the objects in the validation table are also in the original order
					
					folds.add(new CrossValidationFold<InformationTable>(informationTable.discard(indicesArray, accelerateByReadOnlyResult), 
							informationTable.select(indicesArray, accelerateByReadOnlyResult)));
				}
			}
		}
		
		return folds;
	}

}
