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

import org.rulelearn.data.InformationTable;
import org.rulelearn.rules.RuleSet;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Classifier using decision rules to classify objects from an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class RuleClassifier extends AbstractClassifier {
	
	/**
	 * Set of decision rules used to classify objects from any information table to which this classifier is applied.
	 */
	RuleSet ruleSet;

	/**
	 * Constructs this classifier.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from any information table to which this classifier is applied
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleClassifier(RuleSet ruleSet, ClassificationResult defaultClassificationResult) {
		super(defaultClassificationResult);
		this.ruleSet = notNull(ruleSet, "Rule set for a rule classifier is null.");
	}

	/**
	 * Gets set of decision rules used to classify objects from any information table to which this classifier is applied.
	 * 
	 * @return set of decision rules used to classify objects from any information table to which this classifier is applied
	 */
	public RuleSet getRuleSet() {
		return ruleSet;
	}
	
	/**
	 * Classifies an object from an information table, recording indices of covering rules at the given list.
	 * 
	 * @param objectIndex index of an object from the given information table
	 * @param informationTable information table containing the object of interest
	 * @param indicesOfCoveringRules list to which there are going to be appended indices of rules from the rule set that cover the object indicated by given index (ideally a new empty list);
	 *        this list has to be not {@code null} and is going to be extended by this method with indices of covering rules (if there are any covering rules);
	 *        if the list is not empty, existing integer numbers will be left unchanged, and indices of covering rules are going to be appended at the end of the list
	 * 
	 * @return classification result for the considered object
	 * 
	 * @throws NullPointerException if given information table or list is {@code null}
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object (row) stored in the given information table
	 */
	public abstract ClassificationResult classify(int objectIndex, InformationTable informationTable, IntList indicesOfCoveringRules);
	
//	public abstract ClassificationResult[] classifyAll(InformationTable informationTable, IntList[] arrayOfIndicesOfCoveringRules); //TODO

}
