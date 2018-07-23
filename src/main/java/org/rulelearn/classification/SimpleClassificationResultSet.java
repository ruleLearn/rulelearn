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

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.InformationTable;

/**
 * Structure grouping a classifier producing simple classification results,
 * an information table, and simple classification results produced by this classifier on that information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleClassificationResultSet extends ClassificationResultSet {

	/**
	 * Constructs this simple classification result set.
	 * 
	 * @param informationTable information table containing objects to be classified using given classifier
	 * @param classifier classifier to be used to classify objects from the given information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public SimpleClassificationResultSet(InformationTable informationTable, SimpleClassifier classifier) {
		super(informationTable, classifier);
	}
	
	/** 
	 * Initializes classification result array.
	 */
	@Override
	protected void initializeClassificationResults() {
		this.classificationResults = new SimpleClassificationResult[informationTable.getNumberOfObjects()];
	}
	
	/**
	 * Gets classification result for the object having given index.
	 * 
	 * @param objectIndex index of the object from the stored information table
	 * @return classification result for the object having given index
	 * 
	 * @throws IndexOutOfBoundsException if given index does not match any object from the stored information table
	 */
	@Override
	public SimpleClassificationResult getClassificationResult(int objectIndex) {
		if (this.classificationResults[objectIndex] == null) {
			this.classificationResults[objectIndex] = ((SimpleClassifier)this.classifier).classify(objectIndex, informationTable);
			calculatedClassificationResultsCount++;
		}
		return (SimpleClassificationResult)this.classificationResults[objectIndex];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public SimpleClassifier getClassifier() {
		return (SimpleClassifier)this.classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public SimpleClassificationResult[] getClassificationResults() {
		return this.getClassificationResults(false);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @param accelerateByReadOnlyResult {@inheritDoc}
	 */
	@Override
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public SimpleClassificationResult[] getClassificationResults(boolean accelerateByReadOnlyResult) {
		if (this.calculatedClassificationResultsCount < this.informationTable.getNumberOfObjects()) { //not all individual classification results have been calculated
			this.calculateAllClassificationResults();
		}
		
		return accelerateByReadOnlyResult ? (SimpleClassificationResult[])this.classificationResults : (SimpleClassificationResult[])this.classificationResults.clone();
	}

}
