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

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Decision;
import org.rulelearn.data.DecisionDistribution;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleSemantics;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.RuleSetWithComputableCharacteristics;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * Rule classifier based on measure Score(Cl,z) - where Cl denotes a decision class, and z denotes classified object - first described in the paper:<br>
 * J. Błaszczyński, S. Greco, R. Słowiński, Multi-criteria classification - A new scheme for application of dominance-based decision rules.
 * European Journal of Operational Research (EJOR), 181(3), 2007, pp. 1030-1044.<br>
 * This classifier (often called VC-DRSA classifier) can be used in two modes:<br>
 * - score, i.e., always using Score measure to compute optimal classification decision (as proposed in the above paper), or<br>
 * - hybrid, using Score measure only to resolve conflicts (e.g., if covering rules suggest class &gt;=1 and &lt;=2, or if covering rules suggest class &gt;=3 and &lt;=1);
 *   if there is no conflict, returns "standard" prudent classification decision (e.g., for covering rules suggesting classes &gt;=2 and &gt;=3 returns class 3,
 *   while for covering rules suggesting classes &lt;=1 and &lt;=2 returns class 1).<br>
 * <br>
 * In case of a tie (two or more decisions get the same score), the worst of the decisions involved in a tie is returned, i.e., the decision having the smallest index in the array returned by
 * {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} for the learning (training) information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ScoringRuleClassifier extends RuleClassifier implements SimpleClassifier {
	
	/**
	 * Operation mode of this classifier.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static enum Mode {
		/**
		 * Mode indicating that always Score measure is used to compute optimal classification decision.
		 */
		SCORE,
		/**
		 * Mode indicating that Score measure is used to compute optimal classification decision
		 * only to resolve conflicts (e.g., if rules suggest class &gt;=1 and &lt;=2, or if rules suggest class &gt;=3 and &lt;=1).
		 * Otherwise, standard DRSA classifier mechanism is used (as described, e.g., in {@link SimpleRuleClassifier}).
		 */
		HYBRID
	}
	
	/**
	 * Version of the definition of this classifier.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static enum Version {
		/**
		 * Version of the VC-DRSA classifier described in the seminal paper from EJOR 2007.
		 */
		EJOR_2007,
		/**
		 * Corrected version of the VC-DRSA classifier described in J. Błaszczyński,
		 * Rule Models for Ordinal Classification in Variable Consistency Rough Set Approaches (2010),
		 * PhD thesis. Poznan University of Technology, Poznań, Poland, 2010.
		 * The correction is twofold:<br>
		 * - in eq. (5.10), describing positive score, sum of sets is used in the denominator (instead of intersection, which is an error in notation),<br>
		 * - in eq. (5.11), describing negative score, in the numerator, each upward/downward union of classes is replaced by the complement of class Cl_t.
		 * This version of VC-DRSA classifier is implemented in the java Rough Sets (jRS) library, a predecessor of ruleLearn.
		 */
		COMPLEMENT
	}
	
	/**
	 * Stores indices of objects from learning information table that are covered by the considered decision rule,
	 * indices of covered objects from particular decision classes,
	 * indices of supporting objects (i.e., covered learning objects matching rule's decision condition) (if {@link ScoringRuleClassifier#version} equals {@link Version#EJOR_2007}),
	 * indices of covered objects from complements of particular decision classes (if {@link ScoringRuleClassifier#version} equals {@link Version#COMPLEMENT}),
	 * and indices of rule's positive objects (if {@link ScoringRuleClassifier#version} equals {@link Version#EJOR_2007}).
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	class DetailedRuleCoverageInformation {
		/**
		 * Indices of objects from rule's learning information table that are covered by the rule.
		 */
		IntSet indicesOfCoveredObjects = null;
		/**
		 * Maps a single decision class (represented by a simple decision) to the set of indices of covered learning objects from that class.
		 * The keys in the map depend on the original decisions of the covered objects, not on the decisions covered by the rule's decision condition.
		 */
		Object2ObjectMap<SimpleDecision, IntSet> decisionClass2IndicesOfCoveredObjects = null;
		/**
		 * Indices of objects from rule's learning information table that support the rule (i.e., indices of covered learning objects that match rule's decision condition).
		 * This set is calculated upon first request and then stored for future use (it is not always used for a rule that covers a test object).
		 * This set is used only if {@link ScoringRuleClassifier#version} equals {@link Version#EJOR_2007}.
		 */
		IntSet indicesOfSupportingObjects = null;
		/**
		 * Maps a single decision class (represented by a simple decision) to the set of indices of covered learning objects from the complement of that class.
		 * The keys in the map depend on the original decisions of the covered objects, not on the decisions covered by the rule's decision condition.
		 * This map is initialized in class constructor, only if {@link ScoringRuleClassifier#version} equals {@link Version#COMPLEMENT}. 
		 * This map is not built all at once. It contains mappings only for those decision classes, for which the set of indices of covered learning objects
		 * from the complement of that classes was requested at least once. If requested set of indices is not stored for a given decision,
		 * it is calculated, stored in this map for future reference, and only then returned.
		 * This map is used only if {@link ScoringRuleClassifier#version} equals {@link Version#COMPLEMENT}.
		 */
		Object2ObjectMap<SimpleDecision, IntSet> decisionClass2IndicesOfComplementCoveredObjects = null;
		/**
		 * Indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part) of the decision rule.
		 * In case of a certain/possible rule, these are the objects from considered approximated set.
		 * This set is calculated upon first request and then stored for future use (it is not always used for a rule that covers a test object).
		 * This set is used only if {@link ScoringRuleClassifier#version} equals {@link Version#EJOR_2007}.
		 */
		IntSet indicesOfPositiveObjects = null;
		
		/**
		 * Index of a rule in the rule set for which this detailed coverage information is defined.
		 */
		int ruleIndex;
		
		/**
		 * Constructs this detailed rule coverage information.
		 * Calculates {@link #indicesOfCoveredObjects} and {@link #decisionClass2IndicesOfCoveredObjects}.
		 * 
		 * @param ruleIndex index of a rule in the rule set for which this detailed coverage information is defined
		 * @throws InvalidValueException if given rule index is negative
		 */
		DetailedRuleCoverageInformation(int ruleIndex) {
			this.ruleIndex = Precondition.nonNegative(ruleIndex, "Rule index must be non-negative.");
			
			decisionClass2IndicesOfCoveredObjects = new Object2ObjectOpenHashMap<SimpleDecision, IntSet>();
			
			if (ruleSet instanceof RuleSetWithCharacteristics) {
				indicesOfCoveredObjects = new IntOpenHashSet(((RuleSetWithCharacteristics)ruleSet).getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getIndicesOfCoveredObjects());
				Int2ObjectMap<Decision> decisionsOfCoveredObjects = ((RuleSetWithCharacteristics)ruleSet).getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getDecisionsOfCoveredObjects();
				
				//calculate decisionClass2IndicesOfCoveredObjects
				for (Int2ObjectMap.Entry<Decision> entry : decisionsOfCoveredObjects.int2ObjectEntrySet()) {
					registerCoveredObjectIndexForDecision(entry.getIntKey(), (SimpleDecision)entry.getValue()); //update decisionClass2IndicesOfCoveredObjects
				}
			} else {
				indicesOfCoveredObjects = new IntOpenHashSet();
				int allObjectsCount = learningInformationTable.getNumberOfObjects();
				
				for (int objectIndex = 0; objectIndex < allObjectsCount; objectIndex++) {
					if (ruleSet.getRule(ruleIndex).covers(objectIndex, learningInformationTable)) {
						indicesOfCoveredObjects.add(objectIndex);
						//update decisionClass2IndicesOfCoveredObjects
						registerCoveredObjectIndexForDecision(objectIndex, (SimpleDecision)learningInformationTable.getDecision(objectIndex)); //safe cast as learning information table is checked to have only simple decisions
					}
				}
			}
			
			if (version == Version.COMPLEMENT) {
				decisionClass2IndicesOfComplementCoveredObjects = new Object2ObjectOpenHashMap<SimpleDecision, IntSet>(); //just initialize this field
			}
		}
		
		/**
		 * Adds given object index to the set of indices of covered learning (training) objects having given decision.   
		 * 
		 * @param objectIndex index of an learning (training) object covered by the rule
		 * @param decision decision of covered learning (training) object
		 */
		void registerCoveredObjectIndexForDecision(int objectIndex, SimpleDecision decision) {
			if (decisionClass2IndicesOfCoveredObjects.containsKey(decision)) {
				decisionClass2IndicesOfCoveredObjects.get(decision).add(objectIndex);
			} else {
				IntSet setOfObjectIndices = new IntOpenHashSet();
				setOfObjectIndices.add(objectIndex);
				decisionClass2IndicesOfCoveredObjects.put(decision, setOfObjectIndices);
			}
		}
		
		/**
		 * Gets indices of objects from rule's learning information table that are covered by the rule.
		 * 
		 * @return indices of objects from rule's learning information table that are covered by the rule
		 */
		IntSet getIndicesOfCoveredObjects() {
			return indicesOfCoveredObjects; //already calculated in class constructor
		}
		
		/**
		 * Gets mapping between decision classes of learning objects covered by the rule and lists of indices of covered learning objects from these classes.
		 * 
		 * @return mapping between decision classes of learning objects covered by the rule and lists of indices of covered learning objects from these classes
		 */
		Object2ObjectMap<SimpleDecision, IntSet> getDecisionClass2IndicesOfCoveredObjects() {
			return decisionClass2IndicesOfCoveredObjects; //already calculated in class constructor
		}
		
		/**
		 * Gets indices of objects supporting the rule (i.e., indices of covered learning objects that match rule's decision condition).
		 * The result is calculated upon first method call, and stored for future uses.
		 * 
		 * @return indices of objects supporting the rule (i.e., indices of covered learning objects that match rule's decision condition)
		 */
		IntSet getIndicesOfSupportingObjects() {
			if (indicesOfSupportingObjects == null) {
				indicesOfSupportingObjects = new IntOpenHashSet();
				IntSet calculatedIndicesOfCoveredObjects = getIndicesOfCoveredObjects();
				
				for (int objectIndex : calculatedIndicesOfCoveredObjects) {
					if (ruleSet.getRule(ruleIndex).decisionsMatchedBy(objectIndex, learningInformationTable)) {
						indicesOfSupportingObjects.add(objectIndex);
					}
				}
			}
			
			return indicesOfSupportingObjects;
		}
		
		/**
		 * Gets the set of indices of covered learning objects from the complement of decision class characterized by given decision.
		 * This method can be used only if {@link ScoringRuleClassifier#version} equals {@link Version#COMPLEMENT}.
		 * 
		 * @param decision decision indicating decision class whose complement is of interest
		 * @return the set of indices of covered learning objects from the complement of decision class characterized by given decision
		 * 
		 * @throws NullPointerException if {@link ScoringRuleClassifier#version} is different than {@link Version#COMPLEMENT}
		 */
		IntSet getIndicesOfComplementCoveredObjects(SimpleDecision decision) {
			if (!decisionClass2IndicesOfComplementCoveredObjects.containsKey(decision)) {
				IntSet indicesOfComplementCoveredObjects = new IntOpenHashSet(getIndicesOfCoveredObjects());
				indicesOfComplementCoveredObjects.removeAll(getDecisionClass2IndicesOfCoveredObjects().get(decision));
				decisionClass2IndicesOfComplementCoveredObjects.put(decision, indicesOfComplementCoveredObjects);
			}
			
			return decisionClass2IndicesOfComplementCoveredObjects.get(decision);
		}
		
		/**
		 * Gets indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part) of the decision rule.
		 * 
		 * @return indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part) of the decision rule
		 */
		IntSet getIndicesOfPositiveObjects() {
			if (indicesOfPositiveObjects == null) {
				if (ruleSet instanceof RuleSetWithComputableCharacteristics) {
					indicesOfPositiveObjects = ((RuleSetWithComputableCharacteristics)ruleSet).getRuleCharacteristics(ruleIndex).getRuleCoverageInformation().getIndicesOfPositiveObjects();
				} else {
					indicesOfPositiveObjects = new IntOpenHashSet();
					int allObjectsCount = learningInformationTable.getNumberOfObjects();
					
					for (int objectIndex = 0; objectIndex < allObjectsCount; objectIndex++) {
						if (ruleSet.getRule(ruleIndex).decisionsMatchedBy(objectIndex, learningInformationTable)) {
							indicesOfPositiveObjects.add(objectIndex);
						}
					}
				}
			}
			
			return indicesOfPositiveObjects;
		}
	}
	
	/**
	 * Version of {@link SimpleRuleClassifier} that does not resolve conflicts - when facing conflicting limiting evaluations, just returns {@code null} as classification result.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	class SkippingConflictResolutionSimpleRuleClassifer extends SimpleRuleClassifier {

		/**
		 * Default constructor, passing all arguments to super class constructor.
		 * 
		 * @param ruleSet set of decision rules to be used to classify objects from an information table
		 * @param defaultClassificationResult default classification result, to be returned by this classifier
		 *        if it is unable to calculate such result using stored decision rules
		 * 
		 * @throws NullPointerException if any of the parameters is {@code null}
		 */
		public SkippingConflictResolutionSimpleRuleClassifer(RuleSet ruleSet, SimpleClassificationResult defaultClassificationResult) {
			super(ruleSet, defaultClassificationResult);
		}
		
		/**
		 * Computes classification result in case when both limits are set but different. Just returns {@code null}.
		 * 
		 * @param upLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtLeast}
		 * @param downLimit evaluation corresponding to the most cautious class in the intersection of unions of classes suggested by rules with decision of type {@link ConditionAtMost}
		 * @param decisionAttributeIndex index of decision attribute
		 * 
		 * @return {@code null}
		 */
		SimpleClassificationResult resolveConflictingClassificationResult(EvaluationField upLimit, EvaluationField downLimit, int decisionAttributeIndex) {
			return null; //avoid conflict resolution!
		}
		
	}
	
	/**
	 * Container for three parameters defining a "for" loop iterating over all decisions covered by a decision rule, starting from the worst decision. 
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class DecisionLoopParameters {
		/**
		 * First index for which "for" loop iteration should be performed. Should be &gt;= 0.
		 */
		int startingDecisionIndex;
		/**
		 * Index change. Should be equal to +1 (for "at least" rule) or -1 (for "at most" rule).
		 */
		int change;
		/**
		 * Index for which "for" loop iteration should NOT be performed. Should be positive or equal to -1.
		 */
		int outOfRangeDecisionIndex;
	}
	
	private class ScoreHistory {
		private double maxScore = Double.NEGATIVE_INFINITY; //maximum score found so far; initialized with the worst possible value
		private int maxScoreDecisionIndex = -1; //index of decision with maximum score
		
		private Object2DoubleMap<SimpleDecision> decision2ScoreMap; //maps decision to its Score
		
		private ScoreHistory() {
			decision2ScoreMap = new Object2DoubleOpenHashMap<SimpleDecision>();
		}
		
		private void registerScoreForDecision(double score, int decisionIndex) {
			decision2ScoreMap.put(learningOrderedUniqueDecisions[decisionIndex], score);
			
			//update max score only if better score found - corresponds to "prudent" approach
			if (score > maxScore) {
				maxScore = score; //update maximum score
				maxScoreDecisionIndex = decisionIndex; //remember index of decision giving maximum score
				//if there is >1 decision with the best score, the first considered decision with the best score will be chosen (i.e., the worst decision)
			}
		}
	}
	
	/**
	 * Learning (training) information table for which all rules from the rule set have been calculated. 
	 */
	InformationTable learningInformationTable;
	
	/**
	 * Classification mode used by this classifier.
	 */
	Mode mode;
	
	/**
	 * Default version of the definition of this classifier.
	 * Equal to the first version, i.e., {@link Version#EJOR_2007}.
	 */
	public final Version DEFAULT_VERSION = Version.EJOR_2007;
	
	/**
	 * Version of the definition of this classifier.
	 * Initialized with the {@link #DEFAULT_VERSION default version}.
	 */
	Version version = DEFAULT_VERSION;
	
	/**
	 * Ordered (from the worst to the best) array of all unique (and fully-determined) simple decisions assigned to objects of the learning (training) information table.
	 * When constructing this object, this field is assigned the return value of {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()} invoked on the
	 * learning (training) information table (for caching purposes).
	 */
	SimpleDecision[] learningOrderedUniqueDecisions;
	
	/**
	 * Maps evaluation on the active decision attribute of the learning (training) information table (the attribute for which all simple decisions are defined)
	 * to index of the corresponding decision (to which that evaluation contributes) in the array returned by {@link InformationTable#getOrderedUniqueFullyDeterminedDecisions()}
	 * for the learning (training) information table and stored in {@link #learningOrderedUniqueDecisions}.
	 */
	Object2IntMap<EvaluationField> decisionEvaluation2DecisionIndex;
	
	/**
	 * Distribution of decisions in the learning (training) information table.
	 */
	DecisionDistribution learningDecisionDistribution;
	
	/**
	 * Maps rule index to {@link DetailedRuleCoverageInformation detailed rule coverage information} concerning that rule.
	 */
	Int2ObjectMap<DetailedRuleCoverageInformation> ruleIndex2DetailedRuleCoverageInfo;
	
	/**
	 * Constructs this classifier, assuming {@link #DEFAULT_VERSION default version} of its definition.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from an information table
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules
	 * @param mode classification mode to be used by this classifier; {@link Mode#HYBRID} is expected to give better results on average
	 * @param learningInformationTable learning (training) information table for which rules from the given rule set have been induced
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 *         of if given learning information table does not store {@link Decision decisions} for subsequent objects
	 * @throws InvalidSizeException if given learning information table does not store any object
	 * @throws InvalidTypeException if decisions stored for subsequent objects are not {@link SimpleDecision simple decisions}
	 * @throws InvalidValueException if preference type of learning information table active decision attribute is neither gain nor cost
	 */
	public ScoringRuleClassifier(RuleSet ruleSet, SimpleEvaluatedClassificationResult defaultClassificationResult, Mode mode, InformationTable learningInformationTable) {
		super(ruleSet, defaultClassificationResult);
		
		this.mode = Precondition.notNull(mode, "Classification mode of scoring rule classifier is null.");
		this.learningInformationTable = Precondition.notNull(learningInformationTable, "Learning information table is null.");
		
		//verify learning information table
		if (learningInformationTable.getNumberOfObjects() < 1) {
			throw new InvalidSizeException("Learning information table contains no objects.");
		}
		if (learningInformationTable.getDecisions(true) == null) {
			throw new NullPointerException("Learning information table contains no decisions.");
		}
		if (!(learningInformationTable.getDecision(0) instanceof SimpleDecision)) {
			throw new InvalidTypeException("Learning information table does not contain simple decisions.");
		}
		
		this.learningOrderedUniqueDecisions = (SimpleDecision[])learningInformationTable.getOrderedUniqueFullyDeterminedDecisions().clone(); //cache result
		
		//Construct and fill decisionEvaluation2DecisionIndex
		this.decisionEvaluation2DecisionIndex = new Object2IntOpenHashMap<EvaluationField>();
		for (int decisionIndex = 0; decisionIndex < learningOrderedUniqueDecisions.length; decisionIndex++) {
			this.decisionEvaluation2DecisionIndex.put(learningOrderedUniqueDecisions[decisionIndex].getEvaluation(), decisionIndex);
		}
		
		//ensure "global" distribution of decisions (in learning information table)
		this.learningDecisionDistribution = (learningInformationTable instanceof InformationTableWithDecisionDistributions) ?
				((InformationTableWithDecisionDistributions)learningInformationTable).getDecisionDistribution() :
					(new InformationTableWithDecisionDistributions(learningInformationTable, true)).getDecisionDistribution();
		
		//just initialize the mapping
		this.ruleIndex2DetailedRuleCoverageInfo = new Int2ObjectOpenHashMap<ScoringRuleClassifier.DetailedRuleCoverageInformation>();
		
		this.version = DEFAULT_VERSION; //use default version of the definition of this VC-DRSA classifier
	}
	
	/**
	 * Constructs this classifier, setting also the given version of its definition.
	 * 
	 * @param ruleSet set of decision rules to be used to classify objects from an information table
	 * @param defaultClassificationResult default classification result, to be returned by this classifier
	 *        if it is unable to calculate such result using stored decision rules
	 * @param mode classification mode to be used by this classifier; {@link Mode#HYBRID} is expected to give better results on average
	 * @param version requested version of the definition of this classifier
	 * @param learningInformationTable learning (training) information table for which rules from the given rule set have been induced
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 *         of if given learning information table does not store {@link Decision decisions} for subsequent objects
	 * @throws InvalidSizeException if given learning information table does not store any object
	 * @throws InvalidTypeException if decisions stored for subsequent objects are not {@link SimpleDecision simple decisions}
	 * @throws InvalidValueException if preference type of learning information table active decision attribute is neither gain nor cost
	 */
	public ScoringRuleClassifier(RuleSet ruleSet, SimpleEvaluatedClassificationResult defaultClassificationResult, Mode mode, Version version, InformationTable learningInformationTable) {
		this(ruleSet, defaultClassificationResult, mode, learningInformationTable);
		this.version = Precondition.notNull(version, "VC-DRSA classifier version is null.");
		if (version != Version.EJOR_2007 && version != Version.COMPLEMENT) {
			throw new InvalidValueException("Not supported version of VC-DRSA classifier."); //This allows to simplify further code - no switch necessary (version can have only two values)
		}
	}
	
	/**
	 * Gets learning information table for which this classifier is defined.
	 * 
	 * @return learning information table for which this classifier is defined
	 */
	public InformationTable getLearningInformationTable() {
		return learningInformationTable;
	}
	
	/**
	 * Gets classification mode used by this classifier.
	 * 
	 * @return classification mode used by this classifier
	 */
	public Mode getMode() {
		return mode;
	}
	
	/**
	 * Gets employed version of the definition of this classifier.
	 * 
	 * @return employed version of the definition of this classifier
	 */
	public Version getVersion() {
		return version;
	}
	
	/**
	 * Gets default classification result returned by this classifier if it is unable to calculate such a result (no rule covers classified object).
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public SimpleEvaluatedClassificationResult getDefaultClassificationResult() {
		return (SimpleEvaluatedClassificationResult)defaultClassificationResult;
	}

	/**
	 * Classifies an object from an information table using rules stored in this classifier.
	 * 
	 * @param objectIndex {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * 
	 * @return simple optimal classification result for the considered object
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering considered object cannot be compared
	 */
	@Override
	public SimpleEvaluatedClassificationResult classify(int objectIndex, InformationTable informationTable) {
		switch (mode) {
		case HYBRID:
			SkippingConflictResolutionSimpleRuleClassifer probingSimpleRuleClassifer = new SkippingConflictResolutionSimpleRuleClassifer(ruleSet, getDefaultClassificationResult());
			IntList indicesOfCoveringRules = new IntArrayList();
			SimpleClassificationResult result = probingSimpleRuleClassifer.classify(objectIndex, informationTable, indicesOfCoveringRules); //populates indicesOfCoveringRules
			if (result == null) { //conflicting suggestions
				return classifyWithScore(indicesOfCoveringRules); //fall back to Score calculation
			} else { //no conflict
				return new SimpleEvaluatedClassificationResult(result.getSuggestedDecision(), 1.0);
			}
		case SCORE:
			return classifyWithScore(getIndicesOfCoveringRules(objectIndex, informationTable));
		default:
			throw new InvalidValueException("Wrong mode of scoring classifier."); //this should not happen
		}
	}

	/**
	 * Classifies all objects from the given information table.
	 * 
	 * @param informationTable {@inheritDoc}
	 * @return array with simple optimal classification results for subsequent objects from the given information table
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering any considered object cannot be compared
	 */
	@Override
	public SimpleEvaluatedClassificationResult[] classifyAll(InformationTable informationTable) {
		SimpleEvaluatedClassificationResult[] classificationResults = new SimpleEvaluatedClassificationResult[informationTable.getNumberOfObjects()];
		for (int i = 0; i < classificationResults.length; i++) {
			classificationResults[i] = this.classify(i, informationTable);
		}
		return classificationResults;
	}

	/**
	 * Classifies an object from an information table, recording indices of covering rules at the given list.
	 * 
	 * @param objectIndex {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * @param indicesOfCoveringRules {@inheritDoc};
	 *        covering rules are added to the list in the order in which they appear in the rule set
	 * 
	 * @return simple optimal classification result for the considered object
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws InvalidValueException if limiting evaluations of decision conditions of two rules covering considered object cannot be compared
	 */
	@Override
	public SimpleEvaluatedClassificationResult classify(int objectIndex, InformationTable informationTable, IntList indicesOfCoveringRules) {
		switch (mode) {
		case HYBRID:
			SkippingConflictResolutionSimpleRuleClassifer probingSimpleRuleClassifer = new SkippingConflictResolutionSimpleRuleClassifer(ruleSet, getDefaultClassificationResult());
			SimpleClassificationResult result = probingSimpleRuleClassifer.classify(objectIndex, informationTable, indicesOfCoveringRules); //populates indicesOfCoveringRules
			if (result == null) { //conflicting suggestions
				return classifyWithScore(indicesOfCoveringRules); //fall back to Score calculation
			} else { //no conflict
				return new SimpleEvaluatedClassificationResult(result.getSuggestedDecision(), 1.0);
			}
		case SCORE:
			IntList calculatedIndicesOfCoveringRules = getIndicesOfCoveringRules(objectIndex, informationTable);
			indicesOfCoveringRules.addAll(calculatedIndicesOfCoveringRules);
			return classifyWithScore(calculatedIndicesOfCoveringRules);
		default:
			throw new InvalidValueException("Wrong mode of scoring classifier."); //this should not happen
		}
	}
	
	/**
	 * Classifies an object from an information table, using given indices of ("at least" and "at most") rules covering that object.
	 * 
	 * @param indicesOfCoveringRules list of indices of rules from the rule set that cover the object of interest
	 * @return simple optimal classification result for a test object covered by rules with given indices
	 */
	SimpleEvaluatedClassificationResult classifyWithScore(IntList indicesOfCoveringRules) {
		if (indicesOfCoveringRules.size() == 0) { //no covering rule
			return getDefaultClassificationResult();
		} else {
			//every rule covering considered test object will be used to calculate Score of that object w.r.t. different decisions, so it makes sense to create detailed rule coverage information for each such rule
			for (int ruleIndex : indicesOfCoveringRules) {
				if (!ruleIndex2DetailedRuleCoverageInfo.containsKey(ruleIndex)) {
					ruleIndex2DetailedRuleCoverageInfo.put(ruleIndex, new DetailedRuleCoverageInformation(ruleIndex)); 
				}
			}
			
			DecisionLoopParameters decisionLoopParameters = new DecisionLoopParameters(); //constructed once and then updated in calculateDecisionLoopParameters method
			ScoreHistory scoreHistory = new ScoreHistory();
			double score = 0;
			
			switch (indicesOfCoveringRules.size()) { //check number of rules covering classified test object
			case 1: //exactly one covering rule; this case is a special case of >1 covering rule, that is handled separately only due to efficiency concerns (less processing required if only one rule - no negative score)
				int onlyRuleIndex = indicesOfCoveringRules.getInt(0);
				calculateDecisionLoopParameters(ruleSet.getRule(onlyRuleIndex), decisionLoopParameters); //updates fields of decisionLoopParameters object
				
				for (int decisionIndex = decisionLoopParameters.startingDecisionIndex; decisionIndex != decisionLoopParameters.outOfRangeDecisionIndex; decisionIndex += decisionLoopParameters.change) {
					score = Math.pow((double)ruleIndex2DetailedRuleCoverageInfo.get(onlyRuleIndex).getDecisionClass2IndicesOfCoveredObjects().get(
						learningOrderedUniqueDecisions[decisionIndex]).size(), 2) /
							((double)ruleIndex2DetailedRuleCoverageInfo.get(onlyRuleIndex).getIndicesOfCoveredObjects().size() *
							(double)learningDecisionDistribution.getCount(learningOrderedUniqueDecisions[decisionIndex])); //denominator should not be zero
					scoreHistory.registerScoreForDecision(score, decisionIndex);
				} //for
				
				return new SimpleEvaluatedClassificationResult(learningOrderedUniqueDecisions[scoreHistory.maxScoreDecisionIndex], scoreHistory.maxScore, scoreHistory.decision2ScoreMap);
			default: //>1 covering rule
				double scorePlus;
				double scoreMinus = 0;
				int negativeSetSize;
				
				IntSet[] indicesOfRulesSupportingDecision = new IntOpenHashSet[learningOrderedUniqueDecisions.length]; //for each decision stores either null or a set of indices of rules supporting that decision
				
				//prepare indicesOfRulesSupportingDecision
				for (int ruleIndex : indicesOfCoveringRules) {
					calculateDecisionLoopParameters(ruleSet.getRule(ruleIndex), decisionLoopParameters); //updates fields of decisionLoopParameters object
					for (int decisionIndex = decisionLoopParameters.startingDecisionIndex; decisionIndex != decisionLoopParameters.outOfRangeDecisionIndex; decisionIndex += decisionLoopParameters.change) {
						updateIndicesOfRulesSupportingDecision(indicesOfRulesSupportingDecision, decisionIndex, ruleIndex);
					}
				}
				
				//following sets are calculated separately for each potential decision (i.e., decision covered by the decision part of at least one rule covering classified test object);
				//for each potential decision we have 2 sets of rules:
				//  a) rules whose decision part covers that decision (called positive rules)
				//  b) rules whose decision part does not cover that decision (called negative rules)
				
				IntSet sumCondCapDecisionClass; //in numerator of Score+ (sum of intersections of set of training objects that are covered by a positive rule and set of training objects that belong to considered decision class)
				IntSet sumCondPlus; //in denominator of Score+ (sum of sets of training objects that are covered by a positive rule)
				
				IntSet sumCondCapNegativeSet = null; //in numerator of Score-:
				//- for version EJOR_2007: sum of intersections of set of training objects that are covered by a negative rule and set of training objects that belong to union of decision classes suggested by the negative rule;
				//- for version COMPLEMENT: sum of sets of training objects that are covered by a negative rule and belong to the complement of currently considered decision class
				IntSet sumCondMinus = null; //in denominator of Score- (sum of sets of training objects that are covered by subsequent negative rules)
				IntSet sumUnions = null; //in denominator of Score- for version EJOR_2007 only (sum of sets of training objects that belong to union of decision classes suggested by a negative rule)
				
				boolean scoreMinusPresent; //tells if negative score needs to be calculated for current decision
				
				for (int decisionIndex = 0; decisionIndex < indicesOfRulesSupportingDecision.length; decisionIndex++) { //go over all decisions
					if (indicesOfRulesSupportingDecision[decisionIndex] != null) { //given decision is supported by at least one rule covering classified object, and thus it becomes a potential decision for which Score needs to be calculated
						//initialize sets for calculating positive Score
						sumCondCapDecisionClass = new IntOpenHashSet();
						sumCondPlus = new IntOpenHashSet();
						
						if (indicesOfRulesSupportingDecision[decisionIndex].size() < indicesOfCoveringRules.size()) { //there are also rules that do not support currently considered decision
							scoreMinusPresent = true;
							//initialize sets for calculating negative Score
							sumCondCapNegativeSet = new IntOpenHashSet();
							sumCondMinus = new IntOpenHashSet();
							sumUnions = (version == Version.EJOR_2007 ? new IntOpenHashSet() : null); //sumUnions not used for COMPLEMENT version
						} else {
							scoreMinusPresent = false;
						}
						
						for (int ruleIndex : indicesOfCoveringRules) { //go over all covering rules and calculate sets of objects (precisely: objects' indices) necessary to calculate Score
							if (indicesOfRulesSupportingDecision[decisionIndex].contains(ruleIndex)) { //current rule supports current decision
								sumCondCapDecisionClass.addAll(ruleIndex2DetailedRuleCoverageInfo.get(ruleIndex).getDecisionClass2IndicesOfCoveredObjects().get(learningOrderedUniqueDecisions[decisionIndex]));
								sumCondPlus.addAll(ruleIndex2DetailedRuleCoverageInfo.get(ruleIndex).getIndicesOfCoveredObjects());
							} else { //current rule does not support current decision
								switch (version) {
								case EJOR_2007:
									sumCondCapNegativeSet.addAll(ruleIndex2DetailedRuleCoverageInfo.get(ruleIndex).getIndicesOfSupportingObjects());
									sumUnions.addAll(ruleIndex2DetailedRuleCoverageInfo.get(ruleIndex).getIndicesOfPositiveObjects());
									break;
								case COMPLEMENT:
									sumCondCapNegativeSet.addAll(ruleIndex2DetailedRuleCoverageInfo.get(ruleIndex).getIndicesOfComplementCoveredObjects(learningOrderedUniqueDecisions[decisionIndex]));
									break; //sumUnions not used in the denominator!
								default:
									throw new InvalidValueException("Not supported version of scoring rule classifier."); //this should not happen
								} //switch
								sumCondMinus.addAll(ruleIndex2DetailedRuleCoverageInfo.get(ruleIndex).getIndicesOfCoveredObjects());
							}
						} //for (ruleIndex)
						
						scorePlus = Math.pow((double)sumCondCapDecisionClass.size(), 2) /
								((double)sumCondPlus.size() * (double)learningDecisionDistribution.getCount(learningOrderedUniqueDecisions[decisionIndex])); //calculate positive score
						
						if (scoreMinusPresent) {
							negativeSetSize = (version == Version.EJOR_2007 ? sumUnions.size() :
								learningInformationTable.getNumberOfObjects() - learningDecisionDistribution.getCount(learningOrderedUniqueDecisions[decisionIndex])); //version == Version.COMPLEMENT
							scoreMinus = Math.pow((double)sumCondCapNegativeSet.size(), 2) / ((double)sumCondMinus.size() * (double)negativeSetSize); //calculate negative score
						} else {
							scoreMinus = 0;
						}
						
						score = scorePlus - scoreMinus;
						scoreHistory.registerScoreForDecision(score, decisionIndex);
					} //if
				} //for (decisionIndex)
				
				return new SimpleEvaluatedClassificationResult(learningOrderedUniqueDecisions[scoreHistory.maxScoreDecisionIndex], scoreHistory.maxScore, scoreHistory.decision2ScoreMap);
			} //switch
		} //else
		
	}
	
	/**
	 * Stores information that decision with given index is supported by the RHS of the rule with given index. Updates <code>indicesOfRulesSupportingDecision</code>.
	 * 
	 * @param indicesOfRulesSupportingDecision already created array containing, for each decision from {@link #learningOrderedUniqueDecisions},
	 *        a set of indices of rules that support this decision;
	 *        an entry can potentially be {@code null}, if so far no rule supported respective decision
	 * @param decisionIndex	index of decision from {@link #learningOrderedUniqueDecisions} supported by the rule with given index
	 * @param ruleIndex index of the rule from {@link #ruleSet} that supports with its RHS decision having given index at {@link #learningOrderedUniqueDecisions}
	 */
	void updateIndicesOfRulesSupportingDecision(IntSet[] indicesOfRulesSupportingDecision, int decisionIndex, int ruleIndex) {
		if (indicesOfRulesSupportingDecision[decisionIndex] == null) {
			indicesOfRulesSupportingDecision[decisionIndex] = new IntOpenHashSet();
		}
		
		indicesOfRulesSupportingDecision[decisionIndex].add(ruleIndex);
	}
	
	/**
	 * Calculates decision loop parameters for the given rule (i.e., parameters of the loop that iterates over all decisions covered by the RHS of the given rule,
	 * from the worst decision to the best decision), and sets these parameters inside given parameter container.
	 * Updates all fields of the given {@link DecisionLoopParameters parameters} object.
	 * 
	 * @param rule decision rule for which parameters should be calculated
	 * @param parameters container for parameters (has to be not {@code null}), whose fields will get updated
	 */
	void calculateDecisionLoopParameters(Rule rule, DecisionLoopParameters parameters) {
		if (rule.getSemantics() == RuleSemantics.AT_LEAST) {
			parameters.startingDecisionIndex = decisionEvaluation2DecisionIndex.getInt(rule.getDecision().getLimitingEvaluation());
			parameters.change = 1;
			parameters.outOfRangeDecisionIndex = learningOrderedUniqueDecisions.length;
		} else if (rule.getSemantics() == RuleSemantics.AT_MOST) {
			parameters.startingDecisionIndex = 0;
			parameters.change = 1;
			parameters.outOfRangeDecisionIndex = decisionEvaluation2DecisionIndex.getInt(rule.getDecision().getLimitingEvaluation()) + 1;
		} else {
			throw new InvalidTypeException("Rule semantics is neither at least nor at most."); //this should not happen
		}
	}
	
	/**
	 * Iterates among rules from the rule set that cover selected object from given information table
	 * and gathers indices of these covering rules. The order of rules in the returned list is the same as in the rule set.
	 * 
	 * @param objectIndex index of an object from the given information table
	 * @param informationTable information table containing the object of interest
	 * 
	 * @return list of indices of rules from the rule set that cover the object of interest
	 */
	IntList getIndicesOfCoveringRules(int objectIndex, InformationTable informationTable) {
		IntList indicesOfCoveringRules = new IntArrayList();
		
		int rulesCount = this.ruleSet.size();
		
		for (int i = 0; i < rulesCount; i++) {
			if (this.ruleSet.getRule(i).covers(objectIndex, informationTable)) { //current rule covers considered object
				indicesOfCoveringRules.add(i); //remember index of covering rule
			} //if
		} //for
		
		return indicesOfCoveringRules;
	}

}
