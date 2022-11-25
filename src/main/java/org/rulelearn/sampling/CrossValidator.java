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

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
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
	public List<CrossValidationFold<InformationTable>> splitIntoKFold(InformationTable informationTable, int k) {
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
	public List<CrossValidationFold<InformationTable>> splitIntoKFold(InformationTable informationTable, boolean accelerateByReadOnlyResult, int k) {
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
				splitSize =  (int)(informationTableSize/k); //TODO: this leads to too big last fold!
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
	 * decisions as the original information table).<br>
	 * <br>
	 * This method is deprecated since version 0.25.0 of the library, due to producing uneven splits into folds (with too large last fold).
	 * Use {@link #splitStratifiedIntoKFolds(InformationTableWithDecisionDistributions, int)} instead.
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into folds
	 * @param k number of folds
	 * 
	 * @return k {@link CrossValidationFold folds}
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} is {@code null}
	 * @throws InvalidValueException when the provided number of folds is negative
	 */
	@Deprecated
	public List<CrossValidationFold<InformationTable>> splitStratifiedIntoKFold(InformationTableWithDecisionDistributions informationTable, int k) {
		return splitStratifiedIntoKFold(informationTable, false, k);
	}
	
	/**
	 * Randomly splits {@link InformationTable an information table} provided as a parameter into a given number k (also provided as a parameter) 
	 * of {@link CrossValidationFold folds}. Each fold consists of two disjoint sub-tables of the information table. 
	 * This splitting preserves distribution of decisions in the information table (i.e., each constructed sub-table has the same distribution of
	 * decisions as the original information table).<br>
	 * <br>
	 * This method is deprecated since version 0.25.0 of the library, due to producing uneven splits into folds (with too large last fold).
	 * Use {@link #splitStratifiedIntoKFolds(InformationTableWithDecisionDistributions, boolean, int)} instead.
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
	@Deprecated
	public List<CrossValidationFold<InformationTable>> splitStratifiedIntoKFold(InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, int k) {
		Precondition.notNull(informationTable, "Information table provided to cross-validate is null.");
		Precondition.nonNegative(k, "Provided number of folds is negative.");
		List<CrossValidationFold<InformationTable>> folds = new ArrayList<CrossValidationFold<InformationTable>> (k);
		
		// initialization
		int numberOfDecisions = informationTable.getDecisionDistribution().getDecisions().size();
		int numberOfObjectsForDecision;
		
		//TODO: are linked maps necessary?
		Map<Decision, IntArrayList> objectsToSelect = new Object2ObjectLinkedOpenHashMap<Decision, IntArrayList>(numberOfDecisions); //maps decision to list of indices of objects having that decision
		Map<Decision, BitSet> pickedObjects = new Object2ObjectLinkedOpenHashMap<Decision, BitSet>(numberOfDecisions);
		
		Decision[] decisionsSet = informationTable.getUniqueDecisions(); //array with all unique decisions
		
		if (decisionsSet != null) {
			for (Decision decision : decisionsSet) { //iterate over all unique decisions and allocate memory
				numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
				objectsToSelect.put(decision, new IntArrayList(numberOfObjectsForDecision)); //use known capacity to skip future relocation
				pickedObjects.put(decision, new BitSet(numberOfObjectsForDecision));
			}
			
			Decision[] objectDecisions = informationTable.getDecisions(); //get decisions of subsequent objects from the information table
			
			if (objectDecisions != null) {
				IntArrayList list;
				
				for (int i = 0; i < objectDecisions.length; i++) { //fill objectsToSelect for each decision
					list = objectsToSelect.get(objectDecisions[i]);
					list.add(i);
				}
				
				IntArrayList validationSetObjectIndices = null; //indices of objects in the validation set of the current fold
				BitSet picked = null;
				
				int numberOfObjects = informationTable.getNumberOfObjects();
				int numberOfObjectsSelectedSoFar = 0; //number of objects selected so far in all the folds (increases with number of folds)
				int numberOfObjectsToSelectForDecisionWithinFold;
				int i, j;
				
				//selection loop (assigning training and test objects for each of the k folds)
				for (int l = 0; l < k; l++) { //for each fold
					if (l == (k-1)) { //last fold
						// in the last fold - take all objects that remained
						validationSetObjectIndices = new IntArrayList(numberOfObjects - numberOfObjectsSelectedSoFar);
						
						for (Decision decision : decisionsSet) { //within fold, select objects for each decision
							numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
							
							list = objectsToSelect.get(decision);
							picked = pickedObjects.get(decision);
							
							for (j = 0; j < numberOfObjectsForDecision; j++) {
								if (!picked.get(j)) {
									validationSetObjectIndices.add(list.getInt(j));
									picked.set(j);
									numberOfObjectsSelectedSoFar++;
								}
							} //for
						} //for decision
					} //if
					else { //not in the last fold
						//TODO: control total number of objects in fold!
						validationSetObjectIndices = new IntArrayList((int)(((double)numberOfObjects)/k));
						
						for (Decision decision : decisionsSet) { //within fold, select objects for each decision
							numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
							numberOfObjectsToSelectForDecisionWithinFold = (int)(((double)numberOfObjectsForDecision)/k); //TODO: this leads to too big last fold!
							
							list = objectsToSelect.get(decision);
							picked = pickedObjects.get(decision);
							
							i = 0;
							
							while (i < numberOfObjectsToSelectForDecisionWithinFold) { //draw as long as not successful 
								j = random.nextInt(numberOfObjectsForDecision);
								
								if (!picked.get(j)) {
									validationSetObjectIndices.add(list.getInt(j));
									picked.set(j);
									numberOfObjectsSelectedSoFar++;
									i++; //increase the number of objects already drawn
								}
							} //while
						} //for decision
					} //else
					
					int[] sortedValidationSetObjectIndices = validationSetObjectIndices.toIntArray();
					Arrays.sort(sortedValidationSetObjectIndices); //sort indices, so the objects in the validation table are also in the original order
					
					folds.add(new CrossValidationFold<InformationTable>(informationTable.discard(sortedValidationSetObjectIndices, accelerateByReadOnlyResult), 
							informationTable.select(sortedValidationSetObjectIndices, accelerateByReadOnlyResult)));
				} //for each fold
			} //if
		} //if
		
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
	 * @throws InvalidValueException when the provided number of folds is not positive
	 */
	public List<CrossValidationFold<InformationTable>> splitStratifiedIntoKFolds(InformationTableWithDecisionDistributions informationTable, int k) {
		return splitStratifiedIntoKFolds(informationTable, false, k);
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
	 * @param k number of folds (should be &gt;=1)
	 * 
	 * @return k {@link CrossValidationFold folds}
	 * 
	 * @throws NullPointerException when the informationTable {@link InformationTable information table} is {@code null}
	 * @throws InvalidValueException when the provided number of folds is not positive
	 */
	public List<CrossValidationFold<InformationTable>> splitStratifiedIntoKFolds(InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, int k) {
		Precondition.notNull(informationTable, "Information table provided to cross-validate is null.");		
		Precondition.satisfied(k > 0, "Provided number of folds is not positive.");
		
		final class FoldIndexPicker {
			private int numberOfFolds;
			private int currentFoldIndex;
			
			//assumes numberOfFolds >= 1
			private FoldIndexPicker(int numberOfFolds) {
				this.numberOfFolds = numberOfFolds;
				this.currentFoldIndex = 0; //initially fold 0 is picked!
			}
			
			//picks next fold index, gracefully wrapping to 0 after the last fold index
			private int next() {
				return currentFoldIndex = (currentFoldIndex + 1 == numberOfFolds ? 0 : currentFoldIndex + 1);
			}
		}
		
		List<CrossValidationFold<InformationTable>> folds = new ArrayList<CrossValidationFold<InformationTable>>(k);
		IntList[] validationSetObjectIndices = new IntList[k]; //for each fold, stores indices of validation set objects
		Decision[] decisionsSet = informationTable.getUniqueDecisions(); //array with all unique decisions
		
		int numberOfDecisions = informationTable.getDecisionDistribution().getDecisions().size();
		Map<Decision, IntArrayList> objectsToSelect = new Object2ObjectLinkedOpenHashMap<Decision, IntArrayList>(numberOfDecisions); //maps decision to list of indices of objects having that decision
		
		int numberOfObjectsForDecision;
		
		if (decisionsSet != null) {
			for (Decision decision : decisionsSet) { //iterate over all unique decisions and allocate memory
				numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
				objectsToSelect.put(decision, new IntArrayList(numberOfObjectsForDecision)); //use known capacity to skip future relocation
			}
			
			Decision[] objectDecisions = informationTable.getDecisions(); //get decisions of subsequent objects from the information table
			
			if (objectDecisions != null) {
				for (int objectIndex = 0; objectIndex < objectDecisions.length; objectIndex++) { //fill objectsToSelect for each decision
					objectsToSelect.get(objectDecisions[objectIndex]).add(objectIndex); //add current object index to list of indices of objects having given decision
				}
				
				//allocate memory for validation set indices in each fold
				for (int foldIndex = 0; foldIndex < k; foldIndex++) {
					validationSetObjectIndices[foldIndex] = new IntArrayList((int)Math.ceil((double)informationTable.getNumberOfObjects() / k) + numberOfDecisions);
				}
				
				//initialize fold index picker
				FoldIndexPicker foldIndexPicker = new FoldIndexPicker(k);
				
				//supplementary variables
				int numberOfSelectedObjectsForDecision; //number of objects selected so far among objects having current decision
				BitSet selected;
				int randomObjectIndex;
				int[] inFoldSortedValidationSetObjectIndices;
				
				//distribute validation objects among folds, as evenly as possible
				for (Decision decision : decisionsSet) { //go over all unique decisions
					numberOfObjectsForDecision = informationTable.getDecisionDistribution().getCount(decision);
					numberOfSelectedObjectsForDecision = 0;
					selected = new BitSet(numberOfObjectsForDecision);
					
					while (numberOfSelectedObjectsForDecision < numberOfObjectsForDecision) { //iterate until all objects having current decision are distributed among folds
						//select to current fold, at random, one object having current decision
						randomObjectIndex = random.nextInt(numberOfObjectsForDecision);
						
						if (!selected.get(randomObjectIndex)) { //new object index has been selected
							validationSetObjectIndices[foldIndexPicker.currentFoldIndex].add(objectsToSelect.get(decision).getInt(randomObjectIndex));
							selected.set(randomObjectIndex); //mark selected
							numberOfSelectedObjectsForDecision++;
							foldIndexPicker.next(); //choose next fold (possibly wrapping to fold 0)
						}
					}
				}
				
				//create folds
				for (int foldIndex = 0; foldIndex < k; foldIndex++) {
					inFoldSortedValidationSetObjectIndices = validationSetObjectIndices[foldIndex].toIntArray();
					Arrays.sort(inFoldSortedValidationSetObjectIndices); //sort indices, so the objects in the validation table are also in the original order
					
					folds.add(new CrossValidationFold<InformationTable>(
							informationTable.discard(inFoldSortedValidationSetObjectIndices, accelerateByReadOnlyResult), 
							informationTable.select(inFoldSortedValidationSetObjectIndices, accelerateByReadOnlyResult)));
				}
			} //if
		} //if
		
		return folds;
	}

}
