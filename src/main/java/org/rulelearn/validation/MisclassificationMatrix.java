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
import org.rulelearn.data.Decision;

/**
 * Contract for validation results of classification in a form of a misclassification matrix (sometimes also called confusion matrix or an error matrix). 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface MisclassificationMatrix extends ValidationResult {

	/**
	 * Gets value from misclassification matrix, which corresponds to a given original decision and assigned decision both passed as parameters.
	 * 
	 * @param originalDecision original {@link Decision decision}
	 * @param assignedDecision assigned {@link Decision decision}
	 * @return value from misclassification matrix, which corresponds to a given original decision and assigned decision
	 * 
	 * @throws NullPointerException when any of parameters is null 
	 * @throws InvalidValueException when size of the array with original decisions and size of the array assignments differ
	 */
	public double getValue(Decision originalDecision, Decision assignedDecision);
	
	/**
	 * Gets number of unknown assignments for a given original {@link Decision decision} passed as a parameter.
	 * 
	 * @param originalDecision original {@link Decision decision}
	 * @return number of unknown assignments for a given original {@link Decision decision}
	 */
	public double getNumberOfUnknownAssignments(Decision originalDecision);
	
	/**
	 * Gets number of unknown original decisions for a given assigned {@link Decision decision} passed as a parameter.
	 * 
	 * @param assignedDecision assigned {@link Decision decision}
	 * @return number of unknown original decisions for a given assigned {@link Decision decision}
	 */
	public double getNumberOfUnknownOriginalDecisions(Decision assignedDecision);
	
}
