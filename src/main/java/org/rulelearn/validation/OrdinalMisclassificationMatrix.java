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

package org.rulelearn.validation;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Decision;

/**
 * Representation of results of classification in a form of a misclassification matrix (sometimes also called confusion matrix or an error matrix).
 * Order is assumed for assigned (predicted) and original (true) {@link Decision decisions} in this matrix.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class OrdinalMisclassificationMatrix extends MisclassificationMatrix {

	/**
	 * Ordering of decisions.
	 */
	Decision [] orderOfDecisions;
	
	/**
	 * Constructor calculating values in misclassification matrix.
	 * 
	 * @param orderOfDecisions array with ordered {@link Decision decisions} (i.e., array indicating order of decisions in original decisions and assigned decisions) 
	 * @param originalDecisions array with original {@link Decision decisions} of objects in the batch
	 * @param assignedDecisions array with assigned {@link Decision decisions} which are validated
	 * 
	 * @throws NullPointerException when any of arrays (with original decisions, assigned decisions or ordered decisions) passed as parameters or their elements is null 
	 * @throws InvalidValueException when size of the array with original decisions and size of the array with assigned decisions differ
	 */
	public OrdinalMisclassificationMatrix(Decision[] orderOfDecisions, Decision[] originalDecisions, Decision[] assignedDecisions) {
		super();
		this.orderOfDecisions = Precondition.notNullWithContents(orderOfDecisions, "Array with ordered decisions is null.", "Element %i of array with ordered decisions is null.");
		calculateMisclassificationMatrix(originalDecisions, assignedDecisions);
	}
	
	/**
	 * Constructor calculating mean and variance of all values in misclassification matrix.
	 * 
	 * @param orderOfDecisions array with ordered {@link Decision decisions} (i.e., array indicating order of decisions in all original decisions and assigned decisions
	 * 		which are present in matrices)
	 *  @param matrices an array with {@link OrdinalMisclassificationMatrix misclassification matrices} to be averaged
	 * 
	 * @throws NullPointerException when any of arrays (with misclassification matrices or ordered decisions) passed as parameters or their elements is null
	 */
	public OrdinalMisclassificationMatrix(Decision[] orderOfDecisions, OrdinalMisclassificationMatrix... matrices) {
		super();
		this.orderOfDecisions = Precondition.notNullWithContents(orderOfDecisions, "Array with ordered decisions is null.", "Element %i of array with ordered decisions is null.");
		calculateMeanAndVariance(matrices);
	}
	
	/**
	 * Calculates mean absolute error (MAE) based on information from this misclassification matrix.
	 * 
	 * @return value of mean absolute error (MAE) 
	 */
	public double getMAE() {
		double mae = 0.0, sum = 0.0;
		int numDecisions = orderOfDecisions.length;
		
		double value;
		for (int i = 0; i < numDecisions; i++) {
			for (int j = 0; j < numDecisions; j++) {
				value = getValue(orderOfDecisions[i], orderOfDecisions[j]);
				mae += Math.abs(value * (i-j));
				sum += value;
			}
		}
		
		if (sum > 0.0) {
			mae /= sum;
		}
		else {
			mae = 0.0;
		}
				
		return mae;
	}
	
	/**
	 * Calculates root mean squared error (RMSE) based on information from this misclassification matrix.
	 * 
	 * @return value of root mean squared error (RMSE)  
	 */
	public double getRMSE() {
		double rmse = 0.0, sum = 0.0;
		int numDecisions = orderOfDecisions.length;
		
		double value;
		for (int i = 0; i < numDecisions; i++) {
			for (int j = 0; j < numDecisions; j++) {
				value = getValue(orderOfDecisions[i], orderOfDecisions[j]);
				rmse += (value * ((i-j)*(i-j)));
				sum += value;
			}
		}
		
		if (sum > 0.0) {
			rmse /= sum;
			rmse = Math.sqrt(rmse);
		}
		else {
			rmse = 0.0;
		}
				
		return rmse;
	}
	
}
