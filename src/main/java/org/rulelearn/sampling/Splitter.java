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
import java.util.Random;

import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

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
     * construct; each value, in this array, is a proportion (fraction) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter 
	 */
	List<InformationTable> split(InformationTable informationTable, double... splits) {
		List<InformationTable> subTables = null;
		
		// TODO implementation
		
		return subTables;
	}
	
	/**
	 * Splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter, and according to the distribution of decisions in the information table (i.e., each constructed 
	 * sub-table has the same distribution of decisions as the original information table). Objects are selected from the information table 
	 * into information sub-tables subsequently (i.e., according to the order of their occurrence in the information table).
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param splits an array with proportions, where the length is the number of {@link InformationTable information tables} to
     * construct; each value, in this array, is a proportion (fraction) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter; each constructed information sub-table has the same distribution of decisions as the original information table 
	 */
	List<InformationTable> stratifiedSplit(InformationTableWithDecisionDistributions informationTable, double... splits) {
		List<InformationTable> subTables = null;
		
		// TODO implementation
		
		return subTables;
	}
	
	/**
	 * Randomly splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter.
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param random the source of randomness in the splitting
	 * @param splits an array with proportions, where the length is the number of {@link InformationTable information tables} to
     * construct; each value, in this array, is a proportion (fraction) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter 
	 */
	List<InformationTable> randomSplit(InformationTable informationTable, Random random, double... splits) {
		List<InformationTable> subTables = null;
		
		// TODO implementation
		
		return subTables;
	}
	
	/**
	 * Randomly splits {@link InformationTable information table} provided as a parameter into multiple information sub-tables according to proportions
	 * provided as a parameter, and according to the distribution of decisions in the information table (i.e., each constructed 
	 * sub-table has the same distribution of decisions as the original information table). 
	 * 
	 * @param informationTable {@link InformationTable information table} which will be split into sub tables
	 * @param random the source of randomness in the splitting
	 * @param splits an array with proportions, where the length is the number of {@link InformationTable information tables} to
     * construct; each value, in this array, is a proportion (fraction) of objects from the {@link InformationTable information table} 
     * passed as a parameter, which will be selected to respective constructed {@link InformationTable information sub-table}; 
     * The sum of proportions must be less than or equal to 1.0
     * 
	 * @return list with {@link InformationTable information sub-tables} with subsets of objects from the original {@link InformationTable information table}
	 * passed as a parameter; each constructed information sub-table has the same distribution of decisions as the original information table 
	 */
	List<InformationTable> randomStratifiedSplit(InformationTableWithDecisionDistributions informationTable, Random random, double... splits) {
		List<InformationTable> subTables = null;
		
		// TODO implementation
		
		return subTables;
	}
	
}
