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

package org.rulelearn.classification;

import org.rulelearn.data.InformationTable;
import static org.rulelearn.core.Precondition.notNull;

/**
 * Abstract classifier.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Classifier {
	
	/**
	 * Default classification result, returned by this classifier if it is unable to calculate such result.
	 */
	protected ClassificationResult defaultClassificationResult;
	
	/**
	 * Classifies an object from an information table.
	 * 
	 * @param objectIndex index of an object in the given information table
	 * @param informationTable information table containing object of interest
	 * @return classification result for the considered object
	 * 
	 * @throws NullPointerException if given information table is {@code null}
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object (row) stored in the given information table
	 */
	public abstract ClassificationResult classify(int objectIndex, InformationTable informationTable);
	
	/**
	 * Classifies all objects from the given information table.
	 * 
	 * @param informationTable information table with objects to classify
	 * @return array with classification results for subsequent objects from the given information table
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public abstract ClassificationResult[] classifyAll(InformationTable informationTable);
	
	/**
	 * Gets default classification result returned by this classifier if it is unable to calculate such result
	 * 
	 * @return default classification result returned by this classifier
	 */
	public ClassificationResult getDefaultClassificationResult() {
		return defaultClassificationResult;
	}

	/**
	 * Constructs this classifier.
	 * 
	 * @param defaultClassificationResult default classification result, to be returned by this classifier if it is unable to calculate such result
	 * @throws NullPointerException if given default classification result is {@code null}
	 */
	public Classifier(ClassificationResult defaultClassificationResult) {
		this.defaultClassificationResult = notNull(defaultClassificationResult, "Default classification result is null.");
	}
}
