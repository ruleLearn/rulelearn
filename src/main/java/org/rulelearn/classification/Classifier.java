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

/**
 * Contract for a classifier capable of producing for each object from an information table a single {@link ClassificationResult}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface Classifier {
	
	/**
	 * Classifies an object from an information table.
	 * 
	 * @param objectIndex index of an object from the given information table
	 * @param informationTable information table containing the object of interest
	 * @return classification result for the considered object
	 * 
	 * @throws NullPointerException if given information table is {@code null}
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object (row) stored in the given information table
	 */
	public ClassificationResult classify(int objectIndex, InformationTable informationTable);
	
	/**
	 * Classifies all objects from the given information table.
	 * 
	 * @param informationTable information table with objects to classify
	 * @return array with classification results for subsequent objects from the given information table
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public ClassificationResult[] classifyAll(InformationTable informationTable);
}
