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

import it.unimi.dsi.fastutil.ints.IntSet;

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
	 * Constructor calculating sum or mean and variance of all values in misclassification matrix.
	 * 
	 * @param sum indication whether sum or mean and variance should be applied
	 * @param orderOfDecisions array with ordered {@link Decision decisions} (i.e., array indicating order of decisions in all original decisions and assigned decisions
	 * 		which are present in matrices)
	 *  @param matrices an array with {@link OrdinalMisclassificationMatrix misclassification matrices} to be averaged
	 * 
	 * @throws NullPointerException when any of arrays (with misclassification matrices or ordered decisions) passed as parameters or their elements is null
	 */
	public OrdinalMisclassificationMatrix(boolean sum, Decision[] orderOfDecisions, OrdinalMisclassificationMatrix... matrices) {
		super();
		this.orderOfDecisions = Precondition.notNullWithContents(orderOfDecisions, "Array with ordered decisions is null.", "Element %i of array with ordered decisions is null.");
		if (sum) {
			calculateSum(matrices);
		}
		else {
			calculateMeanAndVariance(matrices);
		}
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
	
	/**
	 * Serializes this ordinal misclassification matrix to a (multiline) text.
	 *  
	 * @return serialized ordinal misclassification matrix
	 */
	public String serialize() {
		StringBuilder resultBuilder = new StringBuilder();
		String separator = "\t";
		String newLine = "\n";

		resultBuilder.append("original decision\\suggested decision");
		resultBuilder.append(newLine);
		
		//print top left cell - row\column header
		resultBuilder.append("X");
		
		//print column headers
		for (Decision assignedDecision : orderOfDecisions) {
			resultBuilder.append(separator);
			resultBuilder.append(serializeDecision(assignedDecision));
		}
		
		resultBuilder.append(newLine);
		
		//-----
		
		for (Decision originalDecision : orderOfDecisions) {
			resultBuilder.append(serializeDecision(originalDecision));
			
			for (Decision assignedDecision : orderOfDecisions) {
				resultBuilder.append(separator);
				resultBuilder.append(getValue(originalDecision, assignedDecision));
			}
			
			resultBuilder.append(newLine);
		}
		
		//-----
		
		resultBuilder.append("Accuracy: ").append(getAccuracy()).append(newLine);
		resultBuilder.append("MAE: ").append(getMAE()).append(newLine);
		resultBuilder.append("RMSE: ").append(getRMSE()).append(newLine);
		resultBuilder.append("GMean: ").append(getGmean()).append(newLine);
		resultBuilder.append("Number of correct assignments: ").append(getNumberOfCorrectAssignments()).append(newLine);
		resultBuilder.append("Number of incorrect assignments: ").append(getNumberOfIncorrectAssignments()).append(newLine);
		resultBuilder.append("Number of objects with assigned decision: ").append(getNumberObjectsWithAssignedDecision()).append(newLine);
		
		return resultBuilder.toString();
	}
	
	/**
	 * Serializes given decision to single-line text.
	 * 
	 * @param decision decision to convert to text
	 * @return serialized decision
	 */
	private String serializeDecision(Decision decision) {
		StringBuilder serializedDecisionBuilder = new StringBuilder();
		
		int evaluationsCount = decision.getNumberOfEvaluations();
		IntSet attributeIndices = decision.getAttributeIndices();
		
		int count = 0;
		for (int attributeIndex : attributeIndices) { //TODO: assure ascending order of attribute indices if there is more than 1 attribute index in the set
			serializedDecisionBuilder.append(decision.getEvaluation(attributeIndex));
			count++;
			if (count < evaluationsCount) { //there will be next evaluation
				serializedDecisionBuilder.append(",");
			}
		}
		
		return serializedDecisionBuilder.toString();
	}
	
	/**
	 * Gets a clone of the array containing ordered decisions for which this ordinal misclassification matrix has been constructed.
	 * 
	 * @return a clone of the array containing ordered decisions for which this ordinal misclassification matrix has been constructed
	 */
	public Decision[] getOrderOfDecisions() {
		return orderOfDecisions.clone();
	}
	
}
