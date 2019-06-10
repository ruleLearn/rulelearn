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
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Decision;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

/**
 * Representation of results of classification in a form of a misclassification matrix (sometimes also called confusion matrix or an error matrix). 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class MisclassificationMatrix implements ValidationResult {

	/**
	 * Maps counts of unknown assigned {@link Decision decisions} for given original {@link Decision decisions}.
	 */
	Object2IntMap<Decision> unknownAssignedDecisionsCount;
	
	/**
	 * Maps counts of unknown original {@link Decision decisions} for given assigned {@link Decision decisions}.
	 */
	Object2IntMap<Decision> unknownOriginalDecisionsCount;
	
	/**
	 * Stores number of both original {@link Decision decisions} and assigned {@link Decision decisions} beeing unknown.
	 */
	int numberOfBothUnknownDecisions;
	
	/**
	 * Maps counts of given assigned {@link Decision decisions} for given original {@link Decision decisions}.
	 */
	Object2ObjectMap<Decision, Object2IntMap <Decision>> assignedDecisions2OriginalDecisionsCount;
	
	/**
	 * Number of correct classification assignments.
	 */
	int numberOfCorrectAssignments;
	
	/**
	 * Number of incorrect classification assignments.
	 */
	int numberOfIncorrectAssignments;
	
	/**
	 * Number of unknown classification assigned decisions.
	 */
	int numberOfUnknownAssignmnets;
	
	/**
	 * Number of unknown classification original decisions.
	 */
	int numberOfUnknownOriginalDecisions;
	
	/**
	 * Number of assigned decisions.
	 */
	int numberOfObjectsWithAssignedDecision;
	
	/**
	 * Set of all {@link Decision decisions} which were assigned.
	 */
	ObjectSet<Decision> setOfAllAssignedDecisions;
	
	/**
	 * Set of all original {@link Decision decisions}.
	 */
	ObjectSet<Decision> setOfAllOriginalDecisions;
	
	/**
	 * Calculates values in misclassification matrix.
	 * 
	 * @param originalDecisions array with original {@link Decision decisions} of objects in the batch
	 * @param assignedDecisions array with assigned {@link Decision decisions} which are validated 
	 *  
	 * @throws NullPointerException when any of arrays (with original decisions or assigned decisions) passed as parameters or their elements is null 
	 * @throws InvalidValueException when size of the array with original decisions and size of the array with assigned decisions differ
	 */
	void calculateMisclassificationMatrix(Decision[] originalDecisions, Decision[] assignedDecisions) {
		Precondition.notNullWithContents(originalDecisions, "Array with original decisions is null.", "Element %i of array with original decisions is null.");
		Precondition.notNullWithContents(assignedDecisions, "Array with assigned decisions is null.", "Element %i of array with assigned decisions is null.");
		Precondition.equal(originalDecisions.length, assignedDecisions.length, "Number of elements in the array with original decision and in the array with assigned decisions differ.");

		assignedDecisions2OriginalDecisionsCount = new Object2ObjectOpenHashMap<Decision, Object2IntMap<Decision>> ();
		unknownAssignedDecisionsCount = new Object2IntOpenHashMap<Decision>();
		unknownOriginalDecisionsCount = new Object2IntOpenHashMap<Decision>();
		numberOfBothUnknownDecisions = 0;
		numberOfCorrectAssignments = 0;
		numberOfIncorrectAssignments = 0;
		numberOfUnknownAssignmnets = 0;
		numberOfUnknownOriginalDecisions = 0;
		numberOfObjectsWithAssignedDecision = 0;
		setOfAllOriginalDecisions = new ObjectOpenHashSet<Decision>();
		Object2IntMap<Decision> map = null;
		int previousValue; 
		for (int i = 0; i < assignedDecisions.length; i++) {
			if (!assignedDecisions[i].hasAllMissingEvaluations()) { 
				if (!originalDecisions[i].hasAllMissingEvaluations()) {
					numberOfObjectsWithAssignedDecision++;
					setOfAllOriginalDecisions.add(originalDecisions[i]);
					// calculate number correct and incorrect assignments
					if (assignedDecisions[i].equals(originalDecisions[i])) {
						numberOfCorrectAssignments++;
					}
					else {
						numberOfIncorrectAssignments++;
					}
					// set value in matrix
					if (assignedDecisions2OriginalDecisionsCount.containsKey(assignedDecisions[i])) {
						map = assignedDecisions2OriginalDecisionsCount.get(assignedDecisions[i]);
						if (map.containsKey(originalDecisions[i])) {
							previousValue = map.getInt(originalDecisions[i]);
							map.put(originalDecisions[i], ++previousValue);
						}
						else {
							map.put(originalDecisions[i], 1);
						}
					}
					else {
						map = new Object2IntOpenHashMap<Decision>();
						map.put(originalDecisions[i], 1);
						assignedDecisions2OriginalDecisionsCount.put(assignedDecisions[i], map);
					}
				}
				else { // assigned decision is known (at least partially) but original is not
					numberOfUnknownOriginalDecisions++;
					if (unknownOriginalDecisionsCount.containsKey(assignedDecisions[i])) {
						previousValue = unknownOriginalDecisionsCount.getInt(assignedDecisions[i]);
						unknownOriginalDecisionsCount.put(assignedDecisions[i], ++previousValue);
					}
					else {
						unknownOriginalDecisionsCount.put(assignedDecisions[i], 1);
					}
				}
			}
			else {
				numberOfUnknownAssignmnets++;
				// set value in matrix
				if (!originalDecisions[i].hasAllMissingEvaluations()) { // original decision is known (at least partially) but assigned is not
					if (unknownAssignedDecisionsCount.containsKey(originalDecisions[i])) {
						previousValue = unknownAssignedDecisionsCount.getInt(originalDecisions[i]);
						unknownAssignedDecisionsCount.put(originalDecisions[i], ++previousValue);
					}
					else {
						unknownAssignedDecisionsCount.put(originalDecisions[i], 1);
					}
				}
				else { // both assigned and original decisions are unknown
					numberOfUnknownOriginalDecisions++;
					numberOfBothUnknownDecisions++;
				}
			}
		}
		setOfAllAssignedDecisions = assignedDecisions2OriginalDecisionsCount.keySet();
	}
	
	/**
	 * Gets value from this misclassification matrix, which corresponds to a given original decision and assigned decision both passed as parameters.
	 * 
	 * @param originalDecision original {@link Decision decision}
	 * @param assignedDecision assigned {@link Decision decision}
	 * 
	 * @return value from misclassification matrix, which corresponds to a given original decision and assigned decision; 
	 * or {@code 0.0} when the given pair of decisions is not present in this misclassification matrix   
	 * 
	 * @throws NullPointerException when any of parameters is null 
	 */
	public double getValue(Decision originalDecision, Decision assignedDecision) {
		Precondition.notNull(originalDecision, "Original decision passed as parameter is null.");
		Precondition.notNull(assignedDecision, "Assigned decision passed as parameter is null.");
		if (assignedDecisions2OriginalDecisionsCount.containsKey(assignedDecision)) {
			if (assignedDecisions2OriginalDecisionsCount.get(assignedDecision).containsKey(originalDecision)) {
				return assignedDecisions2OriginalDecisionsCount.get(assignedDecision).getInt(originalDecision);
			}
		}
		return 0.0;
	}
	
	/**
	 * Gets number of unknown assigned {@link Decision decisions} for a given original {@link Decision decision} passed as a parameter.
	 * 
	 * @param originalDecision original {@link Decision decision}
	 * @return number of unknown assignments for a given original {@link Decision decision}; 
	 * or {@code 0.0} when the given original {@link Decision decision} has not unknown decisions assigned in this misclassification matrix
	 * 
	 * @throws NullPointerException when originalDecision is null 
	 */
	public double getNumberOfUnknownAssignedDecisions(Decision originalDecision) {
		Precondition.notNull(originalDecision, "Original passed as parameter decision is null.");
		if (unknownAssignedDecisionsCount.containsKey(originalDecision)) {
			return unknownAssignedDecisionsCount.getInt(originalDecision);
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Gets number of unknown original {@link Decision decisions} for a given assigned {@link Decision decision} passed as a parameter.
	 * 
	 * @param assignedDecision assigned {@link Decision decision}
	 * @return number of unknown original decisions for a given assigned {@link Decision decision};
	 * or {@code 0.0} when the given assigned {@link Decision decision} has not unknown original decisions in this misclassification matrix
	 * 
	 * @throws NullPointerException when assignedDecision is null
	 */
	public double getNumberOfUnknownOriginalDecisions(Decision assignedDecision) {
		Precondition.notNull(assignedDecision, "Assigned passed as parameter decision is null.");
		if (unknownOriginalDecisionsCount.containsKey(assignedDecision)) {
			return unknownOriginalDecisionsCount.getInt(assignedDecision);
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Gets number of unknown original {@link Decision decisions} for all assigned (known or unknown) {@link Decision decisions}.
	 * 
	 * @return number of unknown original {@link Decision decisions} for all assigned {@link Decision decisions}
	 * 
	 */
	public double getNumberOfUnknownOriginalDecisions() {
		return numberOfUnknownOriginalDecisions;
	}
	
	/**
	 * Gets number of unknown original {@link Decision decisions} for all unknown assigned {@link Decision decisions}.
	 * 
	 * @return number of unknown original decisions for all unknown assigned decisions
	 * 
	 */
	public double getNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions() {
		return numberOfBothUnknownDecisions;
	}
	
	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfCorrectAssignments() {
		return numberOfCorrectAssignments;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfIncorrectAssignments() {
		return numberOfIncorrectAssignments;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfUnknownAssignments() {
		return numberOfUnknownAssignmnets;
	}
	
	/**
	 * Gets number of objects with assigned decision based on information from this misclassification matrix.
	 * 
	 * @return number of objects with assigned decision
	 */
	public double getNumberObjectsWithAssignedDecision() {
		return numberOfObjectsWithAssignedDecision;
	}
	
	/**
	 * Gets accuracy in this misclassification matrix calculated as the number of correct assignments divided by sum of correct and incorrect assignments.
	 * Please note that this ratio does not take into account unknown assignments.
	 *  	
	 * @return accuracy in this misclassification matrix
	 */
	public double getAccuracy() {
		if (numberOfObjectsWithAssignedDecision > 0) {
			return (((double) numberOfCorrectAssignments) / (numberOfObjectsWithAssignedDecision));
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Gets true positive rate TPR for a given decision (sometimes also called sensitivity, recall or hit rate). 
	 * This rate, based on values in this misclassification matrix, is calculated as the number of object correctly assigned to the class divided
	 * by all objects from this class which were assigned. Please note that this ratio does not take into account unknown assignments.
	 * 
	 * @param decision {@link Decision decision} for which true positive rate is being calculated
	 *  	
	 * @return true positive rate (TPR) for a given decision
	 */
	public double getTruePositiveRate(Decision decision) {
		Precondition.notNull(decision, "Decision passed as parameter is null.");
		int truePositives = 0, allOtherAssignedPositives = 0;
		for (Decision assignedDecsion : assignedDecisions2OriginalDecisionsCount.keySet()) {
			if (decision.equals(assignedDecsion)) {
				if (assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).containsKey(decision)) {
					truePositives = assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).getInt(decision);
				}
			}
			else {
				if (assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).containsKey(decision)) {
					allOtherAssignedPositives += assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).getInt(decision);
				}
			}
		}
		if (truePositives > 0) {
			return (((double)truePositives) / (truePositives + allOtherAssignedPositives));
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Calculates geometric mean (G-mean) of true positive rates (i.e., sensitivity and specificity for two dimensional misclassification matrices)
	 * based on information from this misclassification matrix.
	 * 
	 * @return value of geometric mean (G-mean)  
	 */
	public double getGmean () {
		double gmean = 0.0;
		int i = 0;
		
		for (Decision decision : setOfAllOriginalDecisions) {
			if (i == 0) {
				gmean = getTruePositiveRate(decision);
			}
			else {
				gmean *= getTruePositiveRate(decision);
			}
			i++;
		}
		
		gmean = Math.pow(gmean, 1.0/i);
		return gmean;
	}
	
}
