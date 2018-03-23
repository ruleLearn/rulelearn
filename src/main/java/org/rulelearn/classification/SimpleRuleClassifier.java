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
import org.rulelearn.rules.RuleSet;

/**
 * Simple classifier using decision rules to classify each object from an information table to exactly one decision class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleRuleClassifier extends RuleClassifier {

	/**
	 * Constructs this classifier.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from an information table
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules
	 * @throws NullPointerException if any of the parameters is {@code null} 
	 */
	public SimpleRuleClassifier(RuleSet ruleSet, SimpleClassificationResult defaultClassificationResult) {
		super(ruleSet, defaultClassificationResult);
	}
	
	/**
	 * Classifies an object from an information table using rules stored in this classifier.
	 * 
	 * @param objectIndex {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public SimpleClassificationResult classify(int objectIndex, InformationTable informationTable) {
		//TODO: implement
		return null;
	}

	/**
	 * Classifies all objects from the given information table.
	 * 
	 * @param informationTable {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public SimpleClassificationResult[] classifyAll(InformationTable informationTable) {
		SimpleClassificationResult[] classificationResults = new SimpleClassificationResult[informationTable.getNumberOfObjects()];
		
		for (int i = 0; i < classificationResults.length; i++) {
			classificationResults[i] = this.classify(i, informationTable);
		}
		
		return classificationResults;
	}

}
