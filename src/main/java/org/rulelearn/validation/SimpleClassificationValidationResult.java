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

import org.rulelearn.classification.SimpleClassificationResult;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.InformationTable;

/**
 * Simple representation of results of validation of a batch of classified objects from an information table {@link InformationTable}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleClassificationValidationResult implements ValidationResult {
	
	int numberOfCorrectAssignments = 0;
	int numberOfIncorrectAssignments = 0;
	int numberOfUnknownAssignemnets = 0;

	/**
	 * Constructor validating number of correct, incorrect, and unknown assignments.
	 * 
	 * @param informationTable an information table {@link InformationTable} with objects
	 * @param assignments assignments of objects from the information, which are validated 
	 */
	public SimpleClassificationValidationResult(InformationTable informationTable, SimpleClassificationResult[] assignments) {
		Precondition.notNull(informationTable, "Information table is null.");
		Precondition.notNull(assignments, "Assignments are null.");
		Precondition.equal(informationTable.getNumberOfObjects(), assignments.length, "Number of objects in the information table and number of assignments differ.");
		validate(informationTable, assignments);
	}
	
	/**
	 * Validates number of correct, incorrect, and unknown assignments of objects from information table.
	 * Equal number of objects in information table and number of provided assignments is assumed.  
	 * 
	 * @param informationTable an information table {@link InformationTable} with objects
	 * @param assignments assignments of objects from the information, which are validated
	 */
	void validate(InformationTable informationTable, SimpleClassificationResult[] assignments) {
		TernaryLogicValue comparison = null;
		for (int i = 0; i < informationTable.getNumberOfObjects(); i++) {
			comparison = assignments[i].isConsistentWith(informationTable.getDecision(i)); 
			if (comparison == TernaryLogicValue.TRUE) {
				this.numberOfCorrectAssignments++;
			}
			else if (comparison == TernaryLogicValue.FALSE) {
				this.numberOfIncorrectAssignments++;
			}
			else {
				this.numberOfUnknownAssignemnets++;
			}
		}
	}
	
	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getNumberOfCorrectAssignments() {
		return this.numberOfCorrectAssignments;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getNumberOfIncorrectAssignments() {
		return this.numberOfIncorrectAssignments;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getNumberOfUnknownAssignments() {
		return this.numberOfUnknownAssignemnets;
	}

}
