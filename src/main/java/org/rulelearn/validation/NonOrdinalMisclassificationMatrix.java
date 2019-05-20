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
 * Representation of results of classification in a form of a misclassification matrix (sometimes also called confusion matrix or an error matrix).
 * No order is assumed for assigned (predicted) nor original (true) {@link Decision decisions} in this matrix.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class NonOrdinalMisclassificationMatrix extends MisclassificationMatrix {
	
	/**
	 * Constructor calculating values in misclassification matrix.
	 * 
	 * @param originalDecisions array with original {@link Decision decisions} of objects in the batch
	 * @param assignedDecisions array with assigned {@link Decision decisions} which are validated 
	 * 
	 * @throws NullPointerException when any of arrays (with original decisions or assigned decisions) passed as parameters or their elements is null 
	 * @throws InvalidValueException when size of the array with original decisions and size of the array with assigned decisions differ
	 */
	public NonOrdinalMisclassificationMatrix(Decision[] originalDecisions, Decision[] assignedDecisions) {
		super();
		calculateMisclassificationMatrix(originalDecisions, assignedDecisions);
	}

}
