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

/**
 * Contract for validation of results of classification/regression (ranking).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ValidationResult {

	/**
	 * Gets number of assignments which were correct.
	 * 
	 * @return number of correct assignments
	 */
	public int getNumberOfCorrectAssignments();
	
	/**
	 * Gets number of assignments which were incorrect.
	 * 
	 * @return number of incorrect assignments
	 */
	public int getNumberOfIncorrectAssignments();
	
	/**
	 * Gets number of times no assignment was made.
	 * 
	 * @return number of no (unknown) assignments
	 */
	public int getNumberOfUnknownAssignments();
	
}
