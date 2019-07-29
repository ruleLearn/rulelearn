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

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
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
	Object2DoubleMap<Decision> unknownAssignedDecisionsCount;
	
	/**
	 * Maps deviations of counts of unknown assigned {@link Decision decisions} for given original {@link Decision decisions}.
	 */
	Object2DoubleMap<Decision> devUnknownAssignedDecisionsCount;
	
	/**
	 * Maps counts of unknown original {@link Decision decisions} for given assigned {@link Decision decisions}.
	 */
	Object2DoubleMap<Decision> unknownOriginalDecisionsCount;
	
	/**
	 * Maps deviations of counts of unknown original {@link Decision decisions} for given assigned {@link Decision decisions}.
	 */
	Object2DoubleMap<Decision> devUnknownOriginalDecisionsCount;
	
	/**
	 * Stores number of both original {@link Decision decisions} and assigned {@link Decision decisions} beeing unknown.
	 */
	double numberOfBothUnknownDecisions;
	
	/**
	 * Stores deviation of number of both original {@link Decision decisions} and assigned {@link Decision decisions} beeing unknown.
	 */
	double devNumberOfBothUnknownDecisions;
	
	/**
	 * Maps counts of given assigned {@link Decision decisions} for given original {@link Decision decisions}.
	 */
	Object2ObjectMap<Decision, Object2DoubleMap <Decision>> assignedDecisions2OriginalDecisionsCount;
	
	/**
	 * Maps deviations of counts of given assigned {@link Decision decisions} for given original {@link Decision decisions}.
	 */
	Object2ObjectMap<Decision, Object2DoubleMap <Decision>> devAssignedDecisions2OriginalDecisionsCount;
	
	/**
	 * Stores number of correct classification assignments.
	 */
	double numberOfCorrectAssignments;
	
	/**
	 * Stores deviation of number of correct classification assignments.
	 */
	double devNumberOfCorrectAssignments;
	
	/**
	 * Stores number of incorrect classification assignments.
	 */
	double numberOfIncorrectAssignments;
	
	/**
	 * Stores deviation of number of incorrect classification assignments.
	 */
	double devNumberOfIncorrectAssignments;
	
	/**
	 * Stores number of unknown classification assigned decisions.
	 */
	double numberOfUnknownAssignmnets;
	
	/**
	 * Stores deviation of number of unknown classification assigned decisions.
	 */
	double devNumberOfUnknownAssignmnets;
	
	/**
	 * Stores number of unknown classification original decisions.
	 */
	double numberOfUnknownOriginalDecisions;
	
	/**
	 * Stores deviation of number of unknown classification original decisions.
	 */
	double devNumberOfUnknownOriginalDecisions;
	
	/**
	 * Stores number of assigned decisions.
	 */
	double numberOfObjectsWithAssignedDecision;
	
	/**
	 * Stores deviation of number of assigned decisions.
	 */
	double devNumberOfObjectsWithAssignedDecision;
	
	/**
	 * Set of all {@link Decision decisions} which were assigned.
	 */
	ObjectSet<Decision> setOfAllAssignedDecisions;
		
	/**
	 * Set of all original {@link Decision decisions}.
	 */
	ObjectSet<Decision> setOfAllOriginalDecisions;
		
	/**
	 * Calculates all values in this misclassification matrix.
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

		assignedDecisions2OriginalDecisionsCount = new Object2ObjectOpenHashMap<Decision, Object2DoubleMap<Decision>> ();
		devAssignedDecisions2OriginalDecisionsCount = new Object2ObjectOpenHashMap<Decision, Object2DoubleMap<Decision>> ();
		unknownAssignedDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		devUnknownAssignedDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		unknownOriginalDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		devUnknownOriginalDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		numberOfBothUnknownDecisions = 0.0;
		devNumberOfBothUnknownDecisions = 0.0;
		numberOfCorrectAssignments = 0.0;
		devNumberOfCorrectAssignments = 0.0;
		numberOfIncorrectAssignments = 0.0;
		devNumberOfIncorrectAssignments = 0.0;
		numberOfUnknownAssignmnets = 0.0;
		devNumberOfUnknownAssignmnets = 0.0;
		numberOfUnknownOriginalDecisions = 0.0;
		devNumberOfUnknownOriginalDecisions = 0.0;
		numberOfObjectsWithAssignedDecision = 0.0;
		devNumberOfObjectsWithAssignedDecision = 0.0;
		setOfAllOriginalDecisions = new ObjectOpenHashSet<Decision>();
		
		Object2DoubleMap<Decision> countMap = null;
		Object2DoubleMap<Decision> devCountMap = null;
		double previousValue; 
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
						countMap = assignedDecisions2OriginalDecisionsCount.get(assignedDecisions[i]);
						devCountMap = devAssignedDecisions2OriginalDecisionsCount.get(assignedDecisions[i]);
						if (countMap.containsKey(originalDecisions[i])) {
							previousValue = countMap.getDouble(originalDecisions[i]);
							countMap.put(originalDecisions[i], ++previousValue);
						}
						else {
							countMap.put(originalDecisions[i], 1.0);
							devCountMap.put(originalDecisions[i], 0.0);
						}
					}
					else {
						countMap = new Object2DoubleOpenHashMap<Decision>();
						devCountMap = new Object2DoubleOpenHashMap<Decision>();
						countMap.put(originalDecisions[i], 1.0);
						assignedDecisions2OriginalDecisionsCount.put(assignedDecisions[i], countMap);
						// deviation is always 0
						devCountMap.put(originalDecisions[i], 0.0);
						devAssignedDecisions2OriginalDecisionsCount.put(assignedDecisions[i], devCountMap);
					}
				}
				else { // assigned decision is known (at least partially) but original is not
					numberOfUnknownOriginalDecisions++;
					if (unknownOriginalDecisionsCount.containsKey(assignedDecisions[i])) {
						previousValue = unknownOriginalDecisionsCount.getDouble(assignedDecisions[i]);
						unknownOriginalDecisionsCount.put(assignedDecisions[i], ++previousValue);
					}
					else {
						unknownOriginalDecisionsCount.put(assignedDecisions[i], 1.0);
						// deviation is always 0
						unknownOriginalDecisionsCount.put(originalDecisions[i], 0.0);
					}
				}
			}
			else {
				numberOfUnknownAssignmnets++;
				// set value in matrix
				if (!originalDecisions[i].hasAllMissingEvaluations()) { // original decision is known (at least partially) but assigned is not
					if (unknownAssignedDecisionsCount.containsKey(originalDecisions[i])) {
						previousValue = unknownAssignedDecisionsCount.getDouble(originalDecisions[i]);
						unknownAssignedDecisionsCount.put(originalDecisions[i], ++previousValue);
					}
					else {
						unknownAssignedDecisionsCount.put(originalDecisions[i], 1.0);
						// deviation is always 0
						devUnknownAssignedDecisionsCount.put(originalDecisions[i], 0.0);
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
	 * Calculates mean and deviation of all values in this misclassification matrix on the basis of values of misclassification matrices passed as parameter.
	 * 
	 * @param matrices an array with misclassification matrices to be averaged
	 * 
	 * @throws NullPointerException when array with misclassification matrices passed as parameters or any of its elements is null
	 */
	void calculateMeanAndDeviation(MisclassificationMatrix... matrices) {
		Precondition.notNullWithContents(matrices, "Array with misclassification matrices is null.", "Element %i of array with misclassification matrices is null.");
		assignedDecisions2OriginalDecisionsCount = new Object2ObjectOpenHashMap<Decision, Object2DoubleMap<Decision>> ();
		devAssignedDecisions2OriginalDecisionsCount = new Object2ObjectOpenHashMap<Decision, Object2DoubleMap<Decision>> ();
		unknownAssignedDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		devUnknownAssignedDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		unknownOriginalDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		devUnknownOriginalDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
		numberOfBothUnknownDecisions = 0.0;
		devNumberOfBothUnknownDecisions = 0.0;
		numberOfCorrectAssignments = 0.0;
		devNumberOfCorrectAssignments = 0.0;
		numberOfIncorrectAssignments = 0.0;
		devNumberOfIncorrectAssignments = 0.0;
		numberOfUnknownAssignmnets = 0.0;
		devNumberOfUnknownAssignmnets = 0.0;
		numberOfUnknownOriginalDecisions = 0.0;
		devNumberOfUnknownOriginalDecisions = 0.0;
		numberOfObjectsWithAssignedDecision = 0.0;
		devNumberOfObjectsWithAssignedDecision = 0.0;
		setOfAllOriginalDecisions = new ObjectOpenHashSet<Decision>();
		
		// calculation of sums of averages
		Object2DoubleOpenHashMap<Decision> originalDecisionsCount;
		int n = matrices.length;
		for (MisclassificationMatrix matrix : matrices) {
			// sum all assigned decision - to - original decision counts
			for (Decision assignedDecision : matrix.setOfAllAssignedDecisions) {
				if (!assignedDecisions2OriginalDecisionsCount.containsKey(assignedDecision)) {
					setOfAllAssignedDecisions.add(assignedDecision);
					originalDecisionsCount = new Object2DoubleOpenHashMap<Decision>();
					for (Decision originalDecision : matrix.assignedDecisions2OriginalDecisionsCount.get(assignedDecision).keySet()) {
						setOfAllOriginalDecisions.add(originalDecision);
						originalDecisionsCount.put(originalDecision, matrix.assignedDecisions2OriginalDecisionsCount.get(assignedDecision).getDouble(originalDecision)/n);
					}
					assignedDecisions2OriginalDecisionsCount.put(assignedDecision, originalDecisionsCount);
				}
				else {
					originalDecisionsCount = ((Object2DoubleOpenHashMap<Decision>)assignedDecisions2OriginalDecisionsCount.get(assignedDecision));
					for (Decision originalDecision : matrix.assignedDecisions2OriginalDecisionsCount.get(assignedDecision).keySet()) {
						if (!originalDecisionsCount.containsKey(originalDecision)) {
							setOfAllOriginalDecisions.add(originalDecision);
							originalDecisionsCount.put(originalDecision, matrix.assignedDecisions2OriginalDecisionsCount.get(assignedDecision).getDouble(originalDecision)/n);
						}
						else {
							originalDecisionsCount.addTo(originalDecision, matrix.assignedDecisions2OriginalDecisionsCount.get(assignedDecision).getDouble(originalDecision)/n);
						}
					}
					assignedDecisions2OriginalDecisionsCount.put(assignedDecision, originalDecisionsCount);
				}
			}
			// sum unknown assigned decision counts
			for (Decision assignedDecision : matrix.unknownAssignedDecisionsCount.keySet()) {
				if (unknownAssignedDecisionsCount.containsKey(assignedDecision)) {
					unknownAssignedDecisionsCount.put(assignedDecision, unknownAssignedDecisionsCount.getDouble(assignedDecision) + 
							(matrix.unknownAssignedDecisionsCount.getDouble(assignedDecision)/n));
				}
				else {
					unknownAssignedDecisionsCount.put(assignedDecision, matrix.unknownAssignedDecisionsCount.getDouble(assignedDecision)/n);
				}
			}
			// sum unknown original decision counts
			for (Decision originalDecision : matrix.unknownOriginalDecisionsCount.keySet()) {
				if (unknownOriginalDecisionsCount.containsKey(originalDecision)) {
					unknownOriginalDecisionsCount.put(originalDecision, unknownOriginalDecisionsCount.getDouble(originalDecision) + 
							(matrix.unknownOriginalDecisionsCount.getDouble(originalDecision)/n));
				}
				else {
					unknownOriginalDecisionsCount.put(originalDecision, matrix.unknownOriginalDecisionsCount.getDouble(originalDecision)/n);
				}
			}
			// sum other counters
			numberOfBothUnknownDecisions += (matrix.numberOfBothUnknownDecisions/n);
			numberOfCorrectAssignments += (matrix.numberOfCorrectAssignments/n);
			numberOfIncorrectAssignments += (matrix.numberOfIncorrectAssignments/n);
			numberOfUnknownAssignmnets += (matrix.numberOfUnknownAssignmnets/n);
			numberOfUnknownOriginalDecisions += (matrix.numberOfUnknownOriginalDecisions/n);
			numberOfObjectsWithAssignedDecision += (matrix.numberOfObjectsWithAssignedDecision/n);
		}
		
		// TODO calculation of averages and deviations
		
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
				return assignedDecisions2OriginalDecisionsCount.get(assignedDecision).getDouble(originalDecision);
			}
		}
		return 0.0;
	}
	
	/**
	 * Gets standard deviation of value from this misclassification matrix, which corresponds to a given original decision and assigned decision both passed as parameters.
	 * 
	 * @param originalDecision original {@link Decision decision}
	 * @param assignedDecision assigned {@link Decision decision}
	 * 
	 * @return standard deviation of value from misclassification matrix, which corresponds to a given original decision and assigned decision; 
	 * or {@code 0.0} when the given pair of decisions is not present in this misclassification matrix   
	 * 
	 * @throws NullPointerException when any of parameters is null 
	 */
	public double getDeviationOfValue(Decision originalDecision, Decision assignedDecision) {
		Precondition.notNull(originalDecision, "Original decision passed as parameter is null.");
		Precondition.notNull(assignedDecision, "Assigned decision passed as parameter is null.");
		if (devAssignedDecisions2OriginalDecisionsCount.containsKey(assignedDecision)) {
			if (devAssignedDecisions2OriginalDecisionsCount.get(assignedDecision).containsKey(originalDecision)) {
				return devAssignedDecisions2OriginalDecisionsCount.get(assignedDecision).getDouble(originalDecision);
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
			return unknownAssignedDecisionsCount.getDouble(originalDecision);
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Gets standard deviation of number of unknown assigned {@link Decision decisions} for a given original {@link Decision decision} passed as a parameter.
	 * 
	 * @param originalDecision original {@link Decision decision}
	 * @return standard deviation of number of unknown assignments for a given original {@link Decision decision}; 
	 * or {@code 0.0} when the given original {@link Decision decision} has not unknown decisions assigned in this misclassification matrix
	 * 
	 * @throws NullPointerException when originalDecision is null 
	 */
	public double getDeviationOfNumberOfUnknownAssignedDecisions(Decision originalDecision) {
		Precondition.notNull(originalDecision, "Original passed as parameter decision is null.");
		if (devUnknownAssignedDecisionsCount.containsKey(originalDecision)) {
			return devUnknownAssignedDecisionsCount.getDouble(originalDecision);
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
			return unknownOriginalDecisionsCount.getDouble(assignedDecision);
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Gets standard deviation of number of unknown original {@link Decision decisions} for a given assigned {@link Decision decision} passed as a parameter.
	 * 
	 * @param assignedDecision assigned {@link Decision decision}
	 * @return standard deviation of number of unknown original decisions for a given assigned {@link Decision decision};
	 * or {@code 0.0} when the given assigned {@link Decision decision} has not unknown original decisions in this misclassification matrix
	 * 
	 * @throws NullPointerException when assignedDecision is null
	 */
	public double getDeviationOfNumberOfUnknownOriginalDecisions(Decision assignedDecision) {
		Precondition.notNull(assignedDecision, "Assigned passed as parameter decision is null.");
		if (devUnknownOriginalDecisionsCount.containsKey(assignedDecision)) {
			return devUnknownOriginalDecisionsCount.getDouble(assignedDecision);
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
	 * Gets standard deviation of number of unknown original {@link Decision decisions} for all assigned (known or unknown) {@link Decision decisions}.
	 * 
	 * @return standard deviation of number of unknown original {@link Decision decisions} for all assigned {@link Decision decisions}
	 * 
	 */
	public double getDeviationOfNumberOfUnknownOriginalDecisions() {
		return devNumberOfUnknownOriginalDecisions;
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
	
	/**
	 * Gets standard deviation of number of unknown original {@link Decision decisions} for all unknown assigned {@link Decision decisions}.
	 * 
	 * @return standard deviation of number of unknown original decisions for all unknown assigned decisions
	 * 
	 */
	public double getDeviationOfNumberOfUnknownAssignedDecisionsForUnknownOriginalDecisions() {
		return devNumberOfBothUnknownDecisions;
	}
	
	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfCorrectAssignments() {
		return numberOfCorrectAssignments;
	}
	
	/**
	 * Gets standard deviation of number of assignments which are correct.
	 * 
	 * @return standard deviation of number of correct assignments
	 */
	public double getDeviationOfNumberOfCorrectAssignments() {
		return devNumberOfCorrectAssignments;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getNumberOfIncorrectAssignments() {
		return numberOfIncorrectAssignments;
	}
	
	/**
	 * Gets standard deviation of number of assignments which are incorrect.
	 * 
	 * @return standard deviation of number of incorrect assignments
	 */
	public double getDeviationOfNumberOfIncorrectAssignments() {
		return devNumberOfIncorrectAssignments;
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
	 * Gets standard deviation of number of times no assignment are made.
	 * 
	 * @return standard deviation of number of no (unknown) assignments
	 */
	public double getDeviationOfNumberOfUnknownAssignments() {
		return devNumberOfUnknownAssignmnets;
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
	 * Gets standard deviation of number of objects with assigned decision based on information from this misclassification matrix.
	 * 
	 * @return standard deviation of number of objects with assigned decision
	 */
	public double getDeviationOfNumberObjectsWithAssignedDecision() {
		return devNumberOfObjectsWithAssignedDecision;
	}
	
	/**
	 * Gets accuracy in this misclassification matrix calculated as the number of correct assignments divided by the number of objects with any assignments.
	 * Please note that this ratio does not take into account unknown assignments.
	 *  	
	 * @return accuracy in this misclassification matrix
	 */
	public double getAccuracy() {
		if (numberOfObjectsWithAssignedDecision > 0.0) {
			return (numberOfCorrectAssignments / numberOfObjectsWithAssignedDecision);
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Gets standard deviation of accuracy in this misclassification matrix calculated as deviation of the number of correct assignments divided by 
	 * deviation of the number of objects with any assignments. Please note that this ratio does not take into account unknown assignments.
	 *  	
	 * @return accuracy in this misclassification matrix
	 */
	public double getDeviationOfAccuracy() {
		if (devNumberOfObjectsWithAssignedDecision > 0.0) {
			return (devNumberOfCorrectAssignments / devNumberOfObjectsWithAssignedDecision);
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
		double truePositives = 0.0, allOtherAssignedPositives = 0.0;
		for (Decision assignedDecsion : assignedDecisions2OriginalDecisionsCount.keySet()) {
			if (decision.equals(assignedDecsion)) {
				if (assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).containsKey(decision)) {
					truePositives = assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).getDouble(decision);
				}
			}
			else {
				if (assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).containsKey(decision)) {
					allOtherAssignedPositives += assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).getDouble(decision);
				}
			}
		}
		if (truePositives > 0.0) {
			return (truePositives / (truePositives + allOtherAssignedPositives));
		}
		else {
			return 0.0;
		}
	}
	
	/**
	 * Gets standard deviation of true positive rate TPR for a given decision (sometimes also called sensitivity, recall or hit rate). 
	 * This rate, based on values in this misclassification matrix, is calculated as the number of object correctly assigned to the class divided
	 * by all objects from this class which were assigned. Please note that this ratio does not take into account unknown assignments.
	 * 
	 * @param decision {@link Decision decision} for which standard deviation of true positive rate is being calculated
	 *  	
	 * @return true positive rate (TPR) for a given decision
	 */
	public double getDeviationOfTruePositiveRate(Decision decision) {
		Precondition.notNull(decision, "Decision passed as parameter is null.");
		double devTruePositives = 0.0, devAllOtherAssignedPositives = 0.0;
		for (Decision assignedDecsion : assignedDecisions2OriginalDecisionsCount.keySet()) {
			if (decision.equals(assignedDecsion)) {
				if (assignedDecisions2OriginalDecisionsCount.get(assignedDecsion).containsKey(decision)) {
					devTruePositives = devAssignedDecisions2OriginalDecisionsCount.get(assignedDecsion).getDouble(decision);
				}
			}
			else {
				if (devAssignedDecisions2OriginalDecisionsCount.get(assignedDecsion).containsKey(decision)) {
					devAllOtherAssignedPositives += devAssignedDecisions2OriginalDecisionsCount.get(assignedDecsion).getDouble(decision);
				}
			}
		}
		if (devTruePositives > 0.0) {
			return (devTruePositives / (devTruePositives + devAllOtherAssignedPositives));
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
