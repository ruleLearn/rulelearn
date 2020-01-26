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

import static org.rulelearn.core.Precondition.notNull;

import java.util.List;

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.Field;

import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Structure grouping a classifier, an information table, and classification results obtained by this classifier on that information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ClassificationResultSet {
	
	/**
	 * Information table containing objects to classify.
	 */
	protected InformationTable informationTable;
	
	/**
	 * Classifier used to classify each object from the information table.
	 */
	protected Classifier classifier;
	
	/**
	 * Results of classification of subsequent objects from the stored information table.
	 */
	protected ClassificationResult[] classificationResults;
	
	/**
	 * Number of objects for which individual classification results have already been calculated.
	 */
	protected int calculatedClassificationResultsCount;
	
	/**
	 * Constructs this classification result set.
	 * 
	 * @param informationTable information table containing objects to be classified using given classifier
	 * @param classifier classifier to be used to classify objects from the given information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ClassificationResultSet(InformationTable informationTable, Classifier classifier) {
		this.informationTable = notNull(informationTable, "Information table for classification result set is null.");
		this.classifier = notNull(classifier, "Classifier for classification result set is null.");
		this.initializeClassificationResults();
		this.calculatedClassificationResultsCount = 0;
	}
	
	/** 
	 * Initializes classification result array.
	 */
	protected void initializeClassificationResults() {
		this.classificationResults = new ClassificationResult[informationTable.getNumberOfObjects()];
	}
	
	/**
	 * Gets classification result for the object having given index.
	 * 
	 * @param objectIndex index of the object from the stored information table
	 * @return classification result for the object having given index
	 * 
	 * @throws IndexOutOfBoundsException if given index does not match any object from the stored information table
	 */
	public ClassificationResult getClassificationResult(int objectIndex) {
		if (this.classificationResults[objectIndex] == null) {
			this.classificationResults[objectIndex] = this.classifier.classify(objectIndex, informationTable);
			this.calculatedClassificationResultsCount++;
		}
		return this.classificationResults[objectIndex];
	}
	
	/**
	 * Gets information table for which all classification results are calculated.
	 * 
	 * @return the informationTable information table for which all classification results are calculated
	 */
	public InformationTable getInformationTable() {
		return this.informationTable;
	}

	/**
	 * Gets an information table with all classification results included. The information table 
	 * is constructed from scratch (i.e., it is a new copy of the information table,
	 * for which classification results are stored).
	 * 
	 * @return the informationTable information with all classification results included
	 */
	public InformationTable getInformationTableWithClassificationResults() {
		InformationTable newInformationTable = null;
		Attribute[] attributes = this.informationTable.getAttributes();
		int numObjects = this.informationTable.getNumberOfObjects(), numAttributes = attributes.length;
		List<Field[]> objects = new ObjectArrayList<Field []>();
		IntSet decisionAttributesIndices = null;
		
		this.calculateAllClassificationResults();
		for(int i = 0; i < numObjects; i++) {
			decisionAttributesIndices = this.classificationResults[i].getSuggestedDecision().getAttributeIndices();
			Field[] object = new Field[numAttributes];
			for(int j = 0; j < numAttributes; j++) {
				if (decisionAttributesIndices.contains(j)) {
					object[j] = this.classificationResults[i].getSuggestedDecision().getEvaluation(j);
				}
				else {
					object[j] = this.informationTable.getField(i, j);
				}
			}
			objects.add(object);
		}
		newInformationTable = new InformationTable(attributes, objects);
		
		return newInformationTable;
	}
	
	/**
	 * Gets classifier used to classify all objects from the stored information table.
	 * 
	 * @return the classifier classifier used to classify all objects from the stored information table
	 */
	public Classifier getClassifier() {
		return this.classifier;
	}
	
	/**
	 * Calculates all not already calculated classification results.
	 */
	protected void calculateAllClassificationResults() {
		int numberOfObjects = this.informationTable.getNumberOfObjects();
		for (int i = 0; i < numberOfObjects; i++) {
			if (this.classificationResults[i] == null) {
				this.classificationResults[i] = this.classifier.classify(i, informationTable);
			}
		}
		this.calculatedClassificationResultsCount = numberOfObjects;
	}
	
	/**
	 * Gets all classification results obtained for objects from the stored information table using stored classifier.
	 * 
	 * @return all classification results obtained for objects from the stored information table using stored classifier
	 */
	public ClassificationResult[] getClassificationResults() {
		return this.getClassificationResults(false);
	}

	/**
	 * Gets all classification results obtained for objects from the stored information table using stored classifier.
	 * 
	 * @return all classification results obtained for objects from the stored information table using stored classifier
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public ClassificationResult[] getClassificationResults(boolean accelerateByReadOnlyResult) {
		if (this.calculatedClassificationResultsCount < this.informationTable.getNumberOfObjects()) { //not all individual classification results have been calculated
			this.calculateAllClassificationResults();
		}
		
		return accelerateByReadOnlyResult ? this.classificationResults : this.classificationResults.clone();
	}
	
}
