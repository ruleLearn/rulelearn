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
import java.util.Random;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

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
		
		public CrossValidationFold(T triningSet, T validationSet) {
			this.trainingTable = triningSet;
			this.validationTable = validationSet;
		}
		public T getTrainingTable() {
			return trainingTable;
		}
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
	public List<CrossValidationFold<InformationTableWithDecisionDistributions>> splitStratifiedIntoKFold (InformationTableWithDecisionDistributions informationTable, int k) {
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
	public List<CrossValidationFold<InformationTableWithDecisionDistributions>> splitStratifiedIntoKFold (InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, int k) {
		Precondition.notNull(informationTable, "Information table provided to cross-validate is null.");
		Precondition.nonNegative(k, "Provided number of folds is negative.");
		List<CrossValidationFold<InformationTableWithDecisionDistributions>> folds = new ArrayList<CrossValidationFold<InformationTableWithDecisionDistributions>> ();
		
		// TODO
		
		return folds;
	}

}
