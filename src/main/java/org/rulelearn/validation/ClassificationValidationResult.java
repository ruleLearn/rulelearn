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

import org.rulelearn.classification.ClassificationResult;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;

/**
 * Representation of results of validation of a batch of classified objects.<br>
 * 
 * Validation takes into account:<br>
 *
 * 1. {@link ClassificationResult#isConsistentWith(Decision) consistency} of {@link ClassificationResult suggested assignment} 
 * with respect to {@link Decision original decision},<br>
 * 2. correctness of {@link ClassificationResult#getSuggestedDecision() suggested decision}.<br>
 * 
 * Object is counted as uncomparable when checking of {@link ClassificationResult#isConsistentWith(Decision) consistency} of 
 * {@link ClassificationResult suggested assignment} leads to {@link TernaryLogicValue#UNCOMPARABLE} result.<br> 
 * 
 * Object is counted as unknown when {@link ClassificationResult#getSuggestedDecision() suggested decision} is {@link TernaryLogicValue#UNCOMPARABLE} 
 * with original decision.<br>
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ClassificationValidationResult implements ValidationResult {
	
	/**
	 * Number of correct classification assignments.
	 */
	int numberOfCorrectAssignments;
	
	/**
	 * Number of incorrect classification assignments.
	 */
	int numberOfIncorrectAssignments;
	
	/**
	 * Number of consistent classification assignments.
	 */
	int numberOfConsistentAssignments;
	
	/**
	 * Number of inconsistent classification assignments.
	 */
	int numberOfInconsistentAssignments;
	
	/**
	 * Number of unknown classification assignments.
	 */
	int numberOfUnknownAssignemnets;
	
	/**
	 * Number of uncomparable assignments.
	 */
	int numberOfUncomparableAssignments;

	/**
	 * Constructor validating number of correct, consistent, incorrect, inconsistent, unknown, and uncomparable assignments in the batch of classified objects.
	 * 
	 * @param originalDecisions array with original {@link Decision decisions} of objects in the batch 
	 * @param assignments array with {@link ClassificationResult assignments (i.e., results of classification)} which are validated
	 * 
	 * @throws NullPointerException when any of arrays (with original decisions or assignments) passed as parameters or their elements is null 
	 * @throws InvalidValueException when size of the array with original decisions and size of the array with assignments differ
	 */
	public ClassificationValidationResult(Decision[] originalDecisions, ClassificationResult[] assignments) {
		Precondition.notNullWithContents(originalDecisions, "Array with original decisions is null.", "Element %i of array with original decisions is null.");
		Precondition.notNullWithContents(assignments, "Array with assignemnts is null.", "Element %i of array with assignments is null.");
		Precondition.equal(originalDecisions.length, assignments.length, "Number of elements in the array with original decision and in the array with assignments differ.");
		this.numberOfConsistentAssignments = 0;
		this.numberOfCorrectAssignments = 0;
		this.numberOfInconsistentAssignments = 0;
		this.numberOfIncorrectAssignments = 0;
		this.numberOfUncomparableAssignments= 0;
		this.numberOfUnknownAssignemnets = 0;
		validate(originalDecisions, assignments);
	}
	
	/**
	 * Validates number of correct, consistent, incorrect, inconsistent, unknown, and uncomparable assignments in the batch of classified (assigned) objects.<br>
	 * 
	 * Equal number of elements in the provided array with original decisions and number of elements in the provided array with assignments is assumed.<br>
	 * 
	 * Validation takes into account:<br>
	 * 
	 * 1. {@link ClassificationResult#isConsistentWith(Decision) consistency} of {@link ClassificationResult suggested assignment} 
	 * with respect to {@link Decision original decision},<br>
	 * 2. correctness (equality) of {@link ClassificationResult#getSuggestedDecision() suggested decision}.<br>
	 * 
	 * Object is counted as uncomparable when checking of {@link ClassificationResult#isConsistentWith(Decision) consistency} of 
	 * {@link ClassificationResult suggested assignment} leads to {@link TernaryLogicValue#UNCOMPARABLE} result.<br> 
	 * 
	 * Object is counted as unknown when {@link ClassificationResult#getSuggestedDecision() suggested decision} has 
	 * {@link Decision#hasAllMissingEvaluations() all evaluations missing (unknown)}.<br>
	 * 
	 * @param originalDecisions array with original {@link Decision decisions} of objects in the batch 
	 * @param assignments array with {@link ClassificationResult assignments (i.e., results of classification)} which are validated
	 */
	void validate(Decision[] originalDecisions, ClassificationResult[] assignments) {
		TernaryLogicValue comparison = null;
		for (int i = 0; i < originalDecisions.length; i++) {
			// check if suggested assignment's decision is correct			
			if (!assignments[i].getSuggestedDecision().hasAllMissingEvaluations()) { // at least partially known
				if (assignments[i].getSuggestedDecision().equals(originalDecisions[i])) {
					this.numberOfCorrectAssignments++;
				}
				else {
					this.numberOfIncorrectAssignments++;
				}
			}
			else { // unknown with regard to all evaluations
				this.numberOfUnknownAssignemnets++;
			}
			// check if suggested assignment is consistent
			comparison = assignments[i].isConsistentWith(originalDecisions[i]); 
			if (comparison == TernaryLogicValue.TRUE) {
				this.numberOfConsistentAssignments++;
			}
			else if (comparison == TernaryLogicValue.FALSE) {
				this.numberOfInconsistentAssignments++;
			}
			else { // uncomparable
				this.numberOfUncomparableAssignments++;
			}
		}
	}
	
	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfCorrectAssignments() {
		return this.numberOfCorrectAssignments;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfIncorrectAssignments() {
		return this.numberOfIncorrectAssignments;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfUnknownAssignments() {
		return this.numberOfUnknownAssignemnets;
	}

	/**
	 * Gets number of assignments which are consistent.
	 * 
	 * @return number of consistent assignments
	 */
	public double getNumberOfConsistentAssignments() {
		return numberOfConsistentAssignments;
	}

	/**
	 * Gets number of assignments which are inconsistent.
	 * 
	 * @return number of inconsistent assignments
	 */
	public double getNumberOfInconsistentAssignments() {
		return numberOfInconsistentAssignments;
	}

	/**
	 * Gets number of assignments which are uncomparable.
	 * 
	 * @return number of uncomparable assignments
	 */
	public double getNumberOfUncomparableAssignments() {
		return numberOfUncomparableAssignments;
	}
	
}
