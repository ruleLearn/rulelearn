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

package org.rulelearn.sampling;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.classification.SimpleClassificationResult;
import org.rulelearn.classification.SimpleRuleClassifier;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.validation.OrdinalMisclassificationMatrix;
import org.rulelearn.wrappers.VCDomLEMWrapper;

/**
 * CrossValidatorRealTest.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class CrossValidatorRealTest {
	
	/**
	 * Test cross validation on windsor data set.
	 */
	@Test
	@Tag("integration")
	void testCrossValidationWindsor() {
		InformationTableWithDecisionDistributions windsor = null;
		int n = 3, a = 1;
		double consistencyThreshold = 0.99;
		
		try {
			windsor = new InformationTableWithDecisionDistributions(InformationTableBuilder.safelyBuildFromCSVFile("src/test/resources/data/csv/windsor.json", 
					"src/test/resources/data/csv/windsor.csv", false, '\t'));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
		if (windsor != null) {
			SimpleClassificationResult defaultResponse = null;
			Attribute[] windsorAttributes = windsor.getAttributes();
			
			System.out.println("=== Integration test on windsor data set ===");
			for (int i = 0; i < windsorAttributes.length; i++) {
				if((windsorAttributes[i].isActive()) && (windsorAttributes[i] instanceof EvaluationAttribute)) {
					EvaluationAttribute attribute = (EvaluationAttribute)windsorAttributes[i];
					if ((attribute.getType() == AttributeType.DECISION) && (attribute.getValueType() instanceof EnumerationField)) {
						ElementList classLabels = ((EnumerationField)attribute.getValueType()).getElementList();
						defaultResponse = new SimpleClassificationResult(new SimpleDecision(EnumerationFieldFactory.getInstance().create(classLabels, 
								classLabels.getSize()/2, attribute.getPreferenceType()), i));
						System.out.println("Set default response (rank): " + defaultResponse.getSuggestedDecision());
						break;
					}
				}
			}
			
			CrossValidator validator = new CrossValidator(ThreadLocalRandom.current());
			OrdinalMisclassificationMatrix mMatrices[] = new OrdinalMisclassificationMatrix[n];
			OrdinalMisclassificationMatrix aMatrices[] = new OrdinalMisclassificationMatrix[a];
			OrdinalMisclassificationMatrix avgMMatrix = null;
			VCDomLEMWrapper vcDomLEMWrapper = new VCDomLEMWrapper();
			RuleSet rules = null;
			SimpleRuleClassifier classifier = null;
			int validationTableSize = 0;
			InformationTable validationTable = null;
			for (int j = 0; j < a; j++) {
				List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitStratifiedIntoKFold(windsor, n);
				for (int i = 0; i < n; i++) {
					rules = vcDomLEMWrapper.induceRulesWithCharacteristics(folds.get(i).getTrainingTable(), consistencyThreshold);
					validationTable = folds.get(i).getValidationTable();
					validationTableSize = validationTable.getNumberOfObjects();
					if (validationTableSize > 0) {
						System.out.println("Validation table " + ((j*10)+i) + " size: " + validationTableSize);
						SimpleDecision[] decisions = new SimpleDecision[validationTableSize];
						classifier = new SimpleRuleClassifier(rules, defaultResponse);
						for (int k = 0; k < validationTableSize; k++) {
							decisions[k] = classifier.classify(k, folds.get(i).getValidationTable()).getSuggestedDecision();
							// debug
							// System.out.println(decisions[k] + " : " + validationTable.getDecision(k));
						}
						mMatrices[i] = new OrdinalMisclassificationMatrix(validationTable.getOrderedUniqueFullyDeterminedDecisions(), validationTable.getDecisions(), decisions);
						System.out.println("Validation table " + ((j*10)+i) + " accuracy: " + mMatrices[i].getAccuracy() + ", MAE: " + mMatrices[i].getMAE());
					}
				}
				aMatrices[j] = new OrdinalMisclassificationMatrix(windsor.getOrderedUniqueFullyDeterminedDecisions(), mMatrices);
				System.out.println("Averaged validation table accuracy: " + aMatrices[j].getAccuracy() + ", deviation: " + aMatrices[j].getDeviationOfAccuracy());
				System.out.println("Averaged validation table MAE: " + aMatrices[j].getMAE());
				for (Decision decision : windsor.getOrderedUniqueFullyDeterminedDecisions()) {
					System.out.println("Averaged validation table, TP for decision: " + decision + ": " + aMatrices[j].getTruePositiveRate(decision) + ", deviation: " + aMatrices[j].getDeviationOfTruePositiveRate(decision));
				}
			}
			avgMMatrix = new OrdinalMisclassificationMatrix(windsor.getOrderedUniqueFullyDeterminedDecisions(), aMatrices);
			System.out.println("===");
			System.out.println("Averaged validation table accuracy: " + avgMMatrix.getAccuracy() + ", deviation: " + avgMMatrix.getDeviationOfAccuracy());
			System.out.println("Averaged validation table MAE: " + avgMMatrix.getMAE());
			for (Decision decision : windsor.getOrderedUniqueFullyDeterminedDecisions()) {
				System.out.println("Averaged validation table, TP for decision: " + decision + ": " + avgMMatrix.getTruePositiveRate(decision) + ", deviation: " + avgMMatrix.getDeviationOfTruePositiveRate(decision));
			}
			System.out.println("Number unknown assignments in averaged validation table: " + avgMMatrix.getNumberOfUnknownAssignments());
		}
		else {
			fail("Unable to load CSV windsor file");
		}
	}
	
	/**
	 * Test cross validation on learning-set-prioritisation-2604v1 data set.
	 */
	@Test
	@Tag("integration")
	void testCrossValidationLearningSetPrioritisation2604() {
		InformationTableWithDecisionDistributions learningSet = null;
		int n = 3, a = 1;
		double consistencyThreshold = 0.99;
		
		try {
			learningSet = new InformationTableWithDecisionDistributions(InformationTableBuilder.safelyBuildFromJSONFile("src/test/resources/data/json/metadata-prioritisation.json", 
					"src/test/resources/data/json/learning-set-prioritisation-2604v1.json"));
			//assertEquals(6853, learningSet.getNumberOfObjects());
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
		if (learningSet != null) {
			SimpleClassificationResult defaultResponse = null;
			Attribute[] attributes = learningSet.getAttributes();
			
			System.out.println("=== Integration test on learning data set for prioritisation 2604 ===");
			for (int i = 0; i < attributes.length; i++) {
				if((attributes[i].isActive()) && (attributes[i] instanceof EvaluationAttribute)) {
					EvaluationAttribute attribute = (EvaluationAttribute)attributes[i];
					if ((attribute.getType() == AttributeType.DECISION) && (attribute.getValueType() instanceof EnumerationField)) {
						ElementList classLabels = ((EnumerationField)attribute.getValueType()).getElementList();
						defaultResponse = new SimpleClassificationResult(new SimpleDecision(EnumerationFieldFactory.getInstance().create(classLabels, 
								classLabels.getSize()/2, attribute.getPreferenceType()), i));
						System.out.println("Set default response (rank): " + defaultResponse.getSuggestedDecision());
						break;
					}
				}
			}
			
			CrossValidator validator = new CrossValidator(new Random(64));
			OrdinalMisclassificationMatrix mMatrices[] = new OrdinalMisclassificationMatrix[n];
			OrdinalMisclassificationMatrix aMatrices[] = new OrdinalMisclassificationMatrix[a];
			OrdinalMisclassificationMatrix avgMMatrix = null;
			VCDomLEMWrapper vcDomLEMWrapper = new VCDomLEMWrapper();
			RuleSet rules = null;
			SimpleRuleClassifier classifier = null;
			int validationTableSize = 0;
			InformationTable validationTable = null;
			for (int j = 0; j < a; j++) {
				List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitStratifiedIntoKFold(learningSet, n);
				for (int i = 0; i < n; i++) {
					rules = vcDomLEMWrapper.induceRulesWithCharacteristics(folds.get(i).getTrainingTable(), consistencyThreshold);
					validationTable = folds.get(i).getValidationTable();
					validationTableSize = validationTable.getNumberOfObjects();
					if (validationTableSize > 0) {
						System.out.println("Validation table " + ((j*10)+i) + " size: " + validationTableSize);
						SimpleDecision[] decisions = new SimpleDecision[validationTableSize];
						classifier = new SimpleRuleClassifier(rules, defaultResponse);
						for (int k = 0; k < validationTableSize; k++) {
							decisions[k] = classifier.classify(k, folds.get(i).getValidationTable()).getSuggestedDecision();
							// debug
							// System.out.println(decisions[k] + " : " + validationTable.getDecision(k));
						}
						mMatrices[i] = new OrdinalMisclassificationMatrix(validationTable.getOrderedUniqueFullyDeterminedDecisions(), validationTable.getDecisions(), decisions);
						System.out.println("Validation table " + ((j*10)+i) + " accuracy: " + mMatrices[i].getAccuracy() + ", MAE: " + mMatrices[i].getMAE());
					}
				}
				aMatrices[j] = new OrdinalMisclassificationMatrix(learningSet.getOrderedUniqueFullyDeterminedDecisions(), mMatrices);
				System.out.println("Averaged validation table accuracy: " + aMatrices[j].getAccuracy() + ", deviation: " + aMatrices[j].getDeviationOfAccuracy());
				System.out.println("Averaged validation table MAE: " + aMatrices[j].getMAE());
				for (Decision decision : learningSet.getOrderedUniqueFullyDeterminedDecisions()) {
					System.out.println("Averaged validation table, TP for decision: " + decision + ": " + aMatrices[j].getTruePositiveRate(decision) + ", deviation: " + aMatrices[j].getDeviationOfTruePositiveRate(decision));
				}
			}
			avgMMatrix = new OrdinalMisclassificationMatrix(learningSet.getOrderedUniqueFullyDeterminedDecisions(), aMatrices);
			System.out.println("===");
			System.out.println("Averaged validation table accuracy: " + avgMMatrix.getAccuracy() + ", deviation: " + avgMMatrix.getDeviationOfAccuracy());
			System.out.println("Averaged validation table MAE: " + avgMMatrix.getMAE());
			for (Decision decision : learningSet.getOrderedUniqueFullyDeterminedDecisions()) {
				System.out.println("Averaged validation table, TP for decision: " + decision + ": " + avgMMatrix.getTruePositiveRate(decision) + ", deviation: " + avgMMatrix.getDeviationOfTruePositiveRate(decision));
			}
			System.out.println("Number unknown assignments in averaged validation table: " + avgMMatrix.getNumberOfUnknownAssignments());
		}
		else {
			fail("Unable to load JSON learning-set-prioritisation-2604v1 file");
		}
	}
	
	/**
	 * Test cross validation on learning-set-prioritisation-0609 data set.
	 */
	@Test
	@Tag("integration")
	void testCrossValidationLearningSetPrioritisation0609() {
		InformationTableWithDecisionDistributions learningSet = null;
		int n = 3, a = 1;
		double consistencyThreshold = 0.99;
		
		try {
			learningSet = new InformationTableWithDecisionDistributions(InformationTableBuilder.safelyBuildFromJSONFile("src/test/resources/data/json/metadata-prioritisation.json", 
					"src/test/resources/data/json/learning-set-prioritisation-0609.json"));
			//assertEquals(27824, learningSet.getNumberOfObjects());
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
		if (learningSet != null) {
			SimpleClassificationResult defaultResponse = null;
			Attribute[] attributes = learningSet.getAttributes();
			
			System.out.println("=== Integration test on learning data set for prioritisation 0609 ===");
			for (int i = 0; i < attributes.length; i++) {
				if((attributes[i].isActive()) && (attributes[i] instanceof EvaluationAttribute)) {
					EvaluationAttribute attribute = (EvaluationAttribute)attributes[i];
					if ((attribute.getType() == AttributeType.DECISION) && (attribute.getValueType() instanceof EnumerationField)) {
						ElementList classLabels = ((EnumerationField)attribute.getValueType()).getElementList();
						defaultResponse = new SimpleClassificationResult(new SimpleDecision(EnumerationFieldFactory.getInstance().create(classLabels, 
								classLabels.getSize()/2, attribute.getPreferenceType()), i));
						System.out.println("Set default response (rank): " + defaultResponse.getSuggestedDecision());
						break;
					}
				}
			}
			
			CrossValidator validator = new CrossValidator(new Random(64));
			OrdinalMisclassificationMatrix mMatrices[] = new OrdinalMisclassificationMatrix[n];
			OrdinalMisclassificationMatrix aMatrices[] = new OrdinalMisclassificationMatrix[a];
			OrdinalMisclassificationMatrix avgMMatrix = null;
			VCDomLEMWrapper vcDomLEMWrapper = new VCDomLEMWrapper();
			RuleSet rules = null;
			SimpleRuleClassifier classifier = null;
			int validationTableSize = 0;
			InformationTable validationTable = null;
			for (int j = 0; j < a; j++) {
				List<CrossValidator.CrossValidationFold<InformationTable>> folds = validator.splitStratifiedIntoKFold(learningSet, n);
				for (int i = 0; i < n; i++) {
					rules = vcDomLEMWrapper.induceRulesWithCharacteristics(folds.get(i).getTrainingTable(), consistencyThreshold);
					validationTable = folds.get(i).getValidationTable();
					validationTableSize = validationTable.getNumberOfObjects();
					if (validationTableSize > 0) {
						System.out.println("Validation table " + ((j*10)+i) + " size: " + validationTableSize);
						SimpleDecision[] decisions = new SimpleDecision[validationTableSize];
						classifier = new SimpleRuleClassifier(rules, defaultResponse);
						for (int k = 0; k < validationTableSize; k++) {
							decisions[k] = classifier.classify(k, folds.get(i).getValidationTable()).getSuggestedDecision();
							// debug
							// System.out.println(decisions[k] + " : " + validationTable.getDecision(k));
						}
						mMatrices[i] = new OrdinalMisclassificationMatrix(validationTable.getOrderedUniqueFullyDeterminedDecisions(), validationTable.getDecisions(), decisions);
						System.out.println("Validation table " + ((j*10)+i) + " accuracy: " + mMatrices[i].getAccuracy() + ", MAE: " + mMatrices[i].getMAE());
					}
				}
				aMatrices[j] = new OrdinalMisclassificationMatrix(learningSet.getOrderedUniqueFullyDeterminedDecisions(), mMatrices);
				System.out.println("Averaged validation table accuracy: " + aMatrices[j].getAccuracy() + ", deviation: " + aMatrices[j].getDeviationOfAccuracy());
				System.out.println("Averaged validation table MAE: " + aMatrices[j].getMAE());
				for (Decision decision : learningSet.getOrderedUniqueFullyDeterminedDecisions()) {
					System.out.println("Averaged validation table, TP for decision: " + decision + ": " + aMatrices[j].getTruePositiveRate(decision) + ", deviation: " + aMatrices[j].getDeviationOfTruePositiveRate(decision));
				}
			}
			avgMMatrix = new OrdinalMisclassificationMatrix(learningSet.getOrderedUniqueFullyDeterminedDecisions(), aMatrices);
			System.out.println("===");
			System.out.println("Averaged validation table accuracy: " + avgMMatrix.getAccuracy() + ", deviation: " + avgMMatrix.getDeviationOfAccuracy());
			System.out.println("Averaged validation table MAE: " + avgMMatrix.getMAE());
			for (Decision decision : learningSet.getOrderedUniqueFullyDeterminedDecisions()) {
				System.out.println("Averaged validation table, TP for decision: " + decision + ": " + avgMMatrix.getTruePositiveRate(decision) + ", deviation: " + avgMMatrix.getDeviationOfTruePositiveRate(decision));
			}
			System.out.println("Number unknown assignments in averaged validation table: " + avgMMatrix.getNumberOfUnknownAssignments());
		}
		else {
			fail("Unable to load JSON learning-set-prioritisation-0609 file");
		}
	}

}
