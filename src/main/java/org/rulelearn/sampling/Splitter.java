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

import java.util.List;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

/**
 * Contract of a splitter able to split {@link InformationTable an information table (a data set)} into multiple disjoint information sub-tables (subsets of the data set).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface Splitter {	
	
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
	public default List<InformationTable> split(InformationTable informationTable, double... splits){
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
	public List<InformationTable> split(InformationTable informationTable, boolean accelerateByReadOnlyResult, double... splits);
	
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
	public default List<InformationTable> splitStratified(InformationTableWithDecisionDistributions informationTable, double... splits) {
		return splitStratified(informationTable, false, splits);
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
	public List<InformationTable> splitStratified(InformationTableWithDecisionDistributions informationTable, boolean accelerateByReadOnlyResult, double... splits);
	
	/**
	 * Checks array with proportions - i.e., it should have not less than 2 elements and sum of its elements should be less than or equal to 1.0.
	 * 
	 * @param proportions an array with proportions in range [0, 1]
     * 
     * @throws IllegalArgumentException when size of array with proportions is lower than 2 or sum of proportions is greater than 1.0
     * @throws InvalidValueException if any proportion is not within interval [0,1]
	 */
	default void checkProportionsArray(double... proportions){
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
