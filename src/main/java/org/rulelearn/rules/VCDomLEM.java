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

package org.rulelearn.rules;

import java.util.Arrays;
import java.util.List;

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * VC-DomLEM sequential rule induction algorithm described in:
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches.
 * Information Sciences, 181, 2011, pp. 987-1002.<br>
 * <br>
 * [DISCLAIMER]<br>
 * This algorithm is not suitable for induction of possible rules if learning data contain missing attribute values
 * that cause non-transitivity of dominance relation, for example of type {@link UnknownSimpleFieldMV2}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class VCDomLEM {
	
	/**
	 * VC-DomLEM parameters used for rule induction (private copy).
	 */
	VCDomLEMParameters vcDomLEMParameters; 
	
	/**
	 * Constructs this rule induction algorithm, employing given parameters.
	 * 
	 * @param vcDomLEMParameters parameters used for rule induction
	 * @throws NullPointerException if given object is {@code null} 
	 */
	public VCDomLEM(VCDomLEMParameters vcDomLEMParameters) {
		Precondition.notNull(vcDomLEMParameters, "VC-DomLEM parameters are null.");
		this.vcDomLEMParameters = vcDomLEMParameters;
	}
	
	/**
	 * Wrapper for {{@link #generateRules(ApproximatedSetProvider, ApproximatedSetRuleDecisionsProvider, double[])}, using given rule consistency threshold to build
	 * an array of identical thresholds, one for each provided approximated set.
	 * 
	 * @param approximatedSetProvider see {@link #generateRules(ApproximatedSetProvider, ApproximatedSetRuleDecisionsProvider, double[])}
	 * @param approximatedSetRuleDecisionsProvider see {@link #generateRules(ApproximatedSetProvider, ApproximatedSetRuleDecisionsProvider, double[])}
	 * @param ruleConsistencyThreshold thresholds reflecting consistency of generated decision rules - this threshold is used for all provided approximated sets
	 * @return see {@link #generateRules(ApproximatedSetProvider, ApproximatedSetRuleDecisionsProvider, double[])}
	 * 
	 * @throws NullPointerException NullPointerException if any of the parameters is {@code null}
	 */
	public RuleSetWithCharacteristics generateRules(ApproximatedSetProvider approximatedSetProvider, ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider, double ruleConsistencyThreshold) {
		Precondition.notNull(approximatedSetProvider, "VC-DomLEM approximated set provider is null.");
		
		double[] evaluationThresholds = new double[approximatedSetProvider.getCount()];
		Arrays.fill(evaluationThresholds, ruleConsistencyThreshold);
		return this.generateRules(approximatedSetProvider, approximatedSetRuleDecisionsProvider, evaluationThresholds);
	}
	
	/**
	 * Generates a minimal set of decision rules by VC-DomLEM algorithm. If certain rules are considered, rule conditions are generated using evaluations of objects from lower approximations.
	 * If possible rules are considered, rule conditions are generated using evaluations of objects from upper approximations.
	 * 
	 * @param approximatedSetProvider provides subsequent approximated sets to be processed; these sets are fetched using {@link ApproximatedSetProvider#getApproximatedSet(int)},
	 *        starting from index 0
	 * @param approximatedSetRuleDecisionsProvider provides rule decisions {@link Rule.RuleDecisions} that can be put on the right-hand side of a certain/possible decision rule
	 *        generated with respect to a considered approximated set
	 * @param ruleConsistencyThresholds array with thresholds reflecting consistency of generated decision rules - one threshold for each provided approximated set 
	 * @return set of induced decision rules
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if the number of given rule consistency thresholds is different than the number of provided approximated sets (see {@link ApproximatedSetProvider#getCount()})
	 */
	public RuleSetWithCharacteristics generateRules(ApproximatedSetProvider approximatedSetProvider, ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider, double[] ruleConsistencyThresholds) {
		Precondition.notNull(approximatedSetProvider, "VC-DomLEM approximated set provider is null.");
		Precondition.notNull(approximatedSetRuleDecisionsProvider, "VC-DomLEM approximated set provider is null.");
		Precondition.notNull(ruleConsistencyThresholds, "VC-DomLEM rule consistency thresholds are null.");
		
		if (approximatedSetProvider.getCount() != ruleConsistencyThresholds.length) {
			throw new InvalidValueException("Different number of provided approximated sets and rule consistency thresholds in VC-DomLEM algorithm.");
		}

		List<RuleConditionsWithApproximatedSet> minimalRuleConditionsWithApproximatedSets = new ObjectArrayList<RuleConditionsWithApproximatedSet>(); //rule conditions for approximated sets considered so far
		List<RuleConditions> approximatedSetRuleConditions; //rule conditions for current approximated set
		List<RuleConditionsWithApproximatedSet> verifiedRuleConditionsWithApproximatedSet; //minimal rule conditions for current approximated set
		RuleConditionsWithApproximatedSet ruleConditionsWithApproximatedSet;
		
		int approximatedSetsCount = approximatedSetProvider.getCount(); //supplementary variable
		ApproximatedSet approximatedSet; //supplementary variable
		
		for (int i = 0; i < approximatedSetsCount; i++) {
			approximatedSet = approximatedSetProvider.getApproximatedSet(i);
			approximatedSetRuleConditions = calculateApproximatedSetRuleConditions(approximatedSet, vcDomLEMParameters.getRuleType(), vcDomLEMParameters.getAllowedNegativeObjectsType(),
					approximatedSetRuleDecisionsProvider);
			
			verifiedRuleConditionsWithApproximatedSet = new ObjectArrayList<RuleConditionsWithApproximatedSet>();
			for (RuleConditions ruleConditions : approximatedSetRuleConditions) { //verify minimality of each rule conditions
				ruleConditionsWithApproximatedSet = new RuleConditionsWithApproximatedSet(ruleConditions, approximatedSet); 
				if (vcDomLEMParameters.getRuleMinimalityChecker().check(minimalRuleConditionsWithApproximatedSets, ruleConditionsWithApproximatedSet)) {
					verifiedRuleConditionsWithApproximatedSet.add(ruleConditionsWithApproximatedSet);
				}
			}
			
			minimalRuleConditionsWithApproximatedSets.addAll(verifiedRuleConditionsWithApproximatedSet);
		}
		
		Rule[] rules = new Rule[minimalRuleConditionsWithApproximatedSets.size()];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[minimalRuleConditionsWithApproximatedSets.size()];
		int ruleIndex = 0;
		
		for (RuleConditionsWithApproximatedSet minimalRuleConditionsWithApproximatedSet : minimalRuleConditionsWithApproximatedSets ) {
			rules[ruleIndex] = new Rule(vcDomLEMParameters.getRuleType(), approximatedSetRuleDecisionsProvider.getRuleSemantics(minimalRuleConditionsWithApproximatedSet.getApproximatedSet()),
					minimalRuleConditionsWithApproximatedSet.getRuleConditions(),
					approximatedSetRuleDecisionsProvider.getRuleDecisions(minimalRuleConditionsWithApproximatedSet.getApproximatedSet()));
			ruleCoverageInformationArray[ruleIndex] = minimalRuleConditionsWithApproximatedSet.getRuleConditions().getRuleCoverageInformation();
			ruleIndex++;
		}
		
		return new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true);
	}
	
	/**
	 * Generates a set of rule conditions for a single approximated set.
	 *  
	 * @param approximatedSet considered approximated set
	 * @param ruleType type of decision rules whose condition parts are going to be calculated
	 * @param allowedCoveredNegativeObjectsType type of negative objects {@link AllowedNegativeObjectsType} allowed to be covered by generated rule conditions
	 *        (obviously apart from positive objects, and apart from neutral objects);
	 *        in case of calculating possible rules, this parameter should be equal to {@link AllowedNegativeObjectsType#APPROXIMATION}
	 * @param approximatedSetRuleDecisionsProvider provider of rule decisions used to calculate semantics of a decision rule concerning given approximated set;
	 *        see {@link ApproximatedSetRuleDecisionsProvider#getRuleSemantics(ApproximatedSet)}
	 *        
	 * @return collection of rule conditions {@link RuleConditions} generated for given parameters
	 * @throws InvalidValueException if {@code allowedCoveredNegativeObjectsType} is not set properly; for certain decision rules it should be set to 
	 *         {@link AllowedNegativeObjectsType#POSITIVE_REGION}, {@link AllowedNegativeObjectsType#POSITIVE_AND_BOUNDARY_REGIONS}, or
	 *         {@link AllowedNegativeObjectsType#ANY_REGION};
	 *         for possible decision rules it should be set to {@link AllowedNegativeObjectsType#APPROXIMATION}
	 */
	private List<RuleConditions> calculateApproximatedSetRuleConditions(ApproximatedSet approximatedSet, RuleType ruleType, AllowedNegativeObjectsType allowedCoveredNegativeObjectsType,
			ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider) {
		List<RuleConditions> approximatedSetRuleConditions = new ObjectArrayList<RuleConditions>(); //the result
		
		IntSortedSet indicesOfApproximationObjects = null; //set of objects that need to be covered (each object by at least one rule conditions)
		switch (ruleType) {
		case CERTAIN:
			indicesOfApproximationObjects = approximatedSet.getLowerApproximation();
			break;
		case POSSIBLE:
			indicesOfApproximationObjects = approximatedSet.getUpperApproximation();
			break;
		case APPROXIMATE:
			indicesOfApproximationObjects = approximatedSet.getBoundary();
			break;
		}
		
		IntSet indicesOfObjectsThatCanBeCovered = null; //indices of objects that are allowed to be covered
		if (ruleType == RuleType.CERTAIN) {
			switch (allowedCoveredNegativeObjectsType) {
			case POSITIVE_REGION:
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(approximatedSet.size());
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getObjects()); //positive objects
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getPositiveRegion()); //positive objects from lower approximation (again) + negative objects in their dominance cones
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
				break;
			case POSITIVE_AND_BOUNDARY_REGIONS:
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(approximatedSet.size());
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getObjects()); //positive objects
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getPositiveRegion()); //positive objects from lower approximation (again) + negative objects in their dominance cones
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getBoundaryRegion());
				indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
				break;
			case ANY_REGION:
				int numberOfObjects = approximatedSet.getInformationTable().getNumberOfObjects();
				indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(numberOfObjects);
				for (int i = 0; i < numberOfObjects; i++) {
					indicesOfObjectsThatCanBeCovered.add(i);
				}
				break;
			default:
				throw new InvalidValueException("Type of negative objects allowed to be covered by certain rules is not properly set.");
			} //switch
		} else { //possible/approximate rule
			if (ruleType == RuleType.POSSIBLE && allowedCoveredNegativeObjectsType != AllowedNegativeObjectsType.APPROXIMATION) {
				throw new InvalidValueException("Type of negative objects allowed to be covered by possible rules is not properly set.");
			}
			
			indicesOfObjectsThatCanBeCovered = new IntOpenHashSet(indicesOfApproximationObjects.size());
			indicesOfObjectsThatCanBeCovered.addAll(indicesOfApproximationObjects);
			indicesOfObjectsThatCanBeCovered.addAll(approximatedSet.getNeutralObjects());
		}
		
		IntList setB = new IntArrayList(indicesOfApproximationObjects); //lower/upper approximation objects not already covered by rule conditions induced so far (set B from algorithm description)
		RuleConditions ruleConditions;
		RuleConditionsBuilder ruleConditionsBuilder;
		IntList indicesOfConsideredObjects; //intersection of current set B and set of objects covered by rule conditions
		
		while (!setB.isEmpty()) {
			indicesOfConsideredObjects = new IntArrayList(setB);
			
			ruleConditionsBuilder = new RuleConditionsBuilder(
					indicesOfConsideredObjects, approximatedSet.getInformationTable(),
					approximatedSet.getObjects(), indicesOfApproximationObjects, IntSets.unmodifiable(indicesOfObjectsThatCanBeCovered), approximatedSet.getNeutralObjects(),
					ruleType, approximatedSetRuleDecisionsProvider.getRuleSemantics(approximatedSet),
					vcDomLEMParameters.getConditionGenerator(), vcDomLEMParameters.getRuleInductionStoppingConditionChecker(), vcDomLEMParameters.getConditionSeparator());
			ruleConditions = ruleConditionsBuilder.build(); //build rule conditions
			
			ruleConditions = vcDomLEMParameters.getRuleConditionsPruner().prune(ruleConditions); //prune built rule conditions by removing redundant elementary conditions
			approximatedSetRuleConditions.add(ruleConditions);
			
			//remove objects covered by the new rule conditions
			//setB = setB \ ruleConditions.getIndicesOfCoveredObjects()
			IntSet setOfIndicesOfCoveredObjects = new IntOpenHashSet(ruleConditions.getIndicesOfCoveredObjects()); //translate list to hash set to accelerate subsequent removeAll method execution
			setB.removeAll(setOfIndicesOfCoveredObjects);
		}
	
		return vcDomLEMParameters.getRuleConditionsSetPruner().prune(approximatedSetRuleConditions, indicesOfApproximationObjects); //remove redundant rules, but keep covered all objects from lower/upper approximation
	}
	
	/**
	 * Gets parameters used during induction of decision rules by VC-DomLEMalgorithm.
	 *  
	 * @return parameters used during induction of decision rules
	 */
	public VCDomLEMParameters getParameters() {
		return this.vcDomLEMParameters;
	}

}
