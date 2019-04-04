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
	 * TODO
	 */
	RuleInducerComponentsProvider ruleInducerComponentsProvider;
	
	/**
	 * TODO
	 */
	ApproximatedSetProvider approximatedSetProvider;
	
	/**
	 * TODO
	 */
	ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider;
	
	/**
	 * TODO
	 * DummyRuleInducerComponentsProvider
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class DummyRuleInducerComponentsProvider implements RuleInducerComponentsProvider {
		
		private RuleInducerComponents components;
		private int count;
		
		private DummyRuleInducerComponentsProvider(RuleInducerComponents components, int count) {
			this.components = components;
			this.count = count;
		}

		/* TODO (non-Javadoc)
		 * @see org.rulelearn.rules.RuleInducerComponentsProvider#provide(int)
		 */
		@Override
		public RuleInducerComponents provide(int i) {
			if ((i >= 0) && (i < count)) {
				return components;
			}
			else {
				throw new IndexOutOfBoundsException("Requested rule inducer components do not exist at index: " + i + ".");
			}
		}

		/* TODO (non-Javadoc)
		 * @see org.rulelearn.rules.RuleInducerComponentsProvider#getCount()
		 */
		@Override
		public int getCount() {
			return count;
		}
		
	}
	
	/**
	 * TODO
	 * @param componentsProvider
	 * @param approximatedSetProvider
	 * @param approximatedSetRuleDecisionsProvider
	 * 
	 * @throws NullPointerException TODO
	 */
	public VCDomLEM(RuleInducerComponentsProvider ruleInducerComponentsProvider, ApproximatedSetProvider approximatedSetProvider, ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider) {
		this.ruleInducerComponentsProvider = Precondition.notNull(ruleInducerComponentsProvider, "VC-DomLEM's components provider is null.");
		this.approximatedSetProvider = Precondition.notNull(approximatedSetProvider, "VC-DomLEM's approximated set provider is null.");
		this.approximatedSetRuleDecisionsProvider = Precondition.notNull(approximatedSetRuleDecisionsProvider, "VC-DomLEM's approximated set rule decisions provider is null.");
		
		if (ruleInducerComponentsProvider.getCount() != approximatedSetProvider.getCount()) {
			throw new InvalidValueException("Different number of provided VC-DOMLEM's components and approximated sets.");
		}
	}
	
	/**
	 * TODO
	 * @param componentsProvider
	 * @param approximatedSetProvider
	 * @param approximatedSetRuleDecisionsProvider
	 * 
	 * @throws NullPointerException TODO
	 */
	public VCDomLEM(RuleInducerComponents ruleInducerComponentsProvider, ApproximatedSetProvider approximatedSetProvider, ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider) {
		this.ruleInducerComponentsProvider = new DummyRuleInducerComponentsProvider(ruleInducerComponentsProvider, approximatedSetProvider.getCount());
		this.approximatedSetProvider = Precondition.notNull(approximatedSetProvider, "VC-DomLEM's approximated set provider is null.");
		this.approximatedSetRuleDecisionsProvider = Precondition.notNull(approximatedSetRuleDecisionsProvider, "VC-DomLEM's approximated set rule decisions provider is null.");
	}
		
	/**
	 * TODO Generates a minimal set of decision rules by VC-DomLEM algorithm. If certain rules are considered, rule conditions are generated using evaluations of objects from lower approximations.
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
	public RuleSet generateRules() {		
		List<RuleConditionsWithApproximatedSet> minimalRuleConditionsWithApproximatedSets = new ObjectArrayList<RuleConditionsWithApproximatedSet>(); //rule conditions for approximated sets considered so far
		List<RuleConditions> approximatedSetRuleConditions; //rule conditions for current approximated set
		List<RuleConditionsWithApproximatedSet> verifiedRuleConditionsWithApproximatedSet; //minimal rule conditions for current approximated set
		RuleConditionsWithApproximatedSet ruleConditionsWithApproximatedSet;
		
		int approximatedSetsCount = approximatedSetProvider.getCount(); //supplementary variable
		ApproximatedSet approximatedSet; //supplementary variable
		RuleInducerComponents ruleInducerComponents;
		
		for (int i = 0; i < approximatedSetsCount; i++) {
			ruleInducerComponents = ruleInducerComponentsProvider.provide(i);
			approximatedSet = approximatedSetProvider.getApproximatedSet(i);
			approximatedSetRuleConditions = calculateApproximatedSetRuleConditions(ruleInducerComponents, approximatedSet);
			
			verifiedRuleConditionsWithApproximatedSet = new ObjectArrayList<RuleConditionsWithApproximatedSet>();
			for (RuleConditions ruleConditions : approximatedSetRuleConditions) { //verify minimality of each rule conditions
				ruleConditionsWithApproximatedSet = new RuleConditionsWithApproximatedSet(ruleConditions, approximatedSet); 
				if (ruleInducerComponents.getRuleMinimalityChecker().check(minimalRuleConditionsWithApproximatedSets, ruleConditionsWithApproximatedSet)) {
					verifiedRuleConditionsWithApproximatedSet.add(ruleConditionsWithApproximatedSet);
				}
			}
			
			minimalRuleConditionsWithApproximatedSets.addAll(verifiedRuleConditionsWithApproximatedSet);
		}
		
		Rule[] rules = new Rule[minimalRuleConditionsWithApproximatedSets.size()];
		RuleCoverageInformation[] ruleCoverageInformationArray = new RuleCoverageInformation[minimalRuleConditionsWithApproximatedSets.size()];
		int ruleIndex = 0;
		
		for (RuleConditionsWithApproximatedSet minimalRuleConditionsWithApproximatedSet : minimalRuleConditionsWithApproximatedSets ) {
			rules[ruleIndex] = new Rule(minimalRuleConditionsWithApproximatedSet.getRuleConditions().getRuleType(), approximatedSetRuleDecisionsProvider.getRuleSemantics(minimalRuleConditionsWithApproximatedSet.getApproximatedSet()),
					minimalRuleConditionsWithApproximatedSet.getRuleConditions(),
					approximatedSetRuleDecisionsProvider.getRuleDecisions(minimalRuleConditionsWithApproximatedSet.getApproximatedSet()));
			ruleCoverageInformationArray[ruleIndex] = minimalRuleConditionsWithApproximatedSet.getRuleConditions().getRuleCoverageInformation();
			ruleIndex++;
		}
		
		return new RuleSetWithComputableCharacteristics(rules, ruleCoverageInformationArray, true); //TODO: second version of VCDomLEM returning just decision rules, without characteristics
	}
	
	/**
	 * TODO Generates a set of rule conditions for a single approximated set.
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
	private List<RuleConditions> calculateApproximatedSetRuleConditions(RuleInducerComponents ruleInducerComponents, ApproximatedSet approximatedSet) {
		List<RuleConditions> approximatedSetRuleConditions = new ObjectArrayList<RuleConditions>(); //the result
		
		IntSortedSet indicesOfApproximationObjects = null; //set of objects that need to be covered (each object by at least one rule conditions)
		RuleType ruleType = ruleInducerComponents.getRuleType();
		AllowedNegativeObjectsType allowedCoveredNegativeObjectsType = ruleInducerComponents.getAllowedNegativeObjectsType();
		
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
					ruleInducerComponents.getConditionGenerator(), ruleInducerComponents.getRuleInductionStoppingConditionChecker(), ruleInducerComponents.getConditionSeparator());
			ruleConditions = ruleConditionsBuilder.build(); //build rule conditions
			
			ruleConditions = ruleInducerComponents.getRuleConditionsPruner().prune(ruleConditions); //prune built rule conditions by removing redundant elementary conditions
			approximatedSetRuleConditions.add(ruleConditions);
			
			//remove objects covered by the new rule conditions
			//setB = setB \ ruleConditions.getIndicesOfCoveredObjects()
			IntSet setOfIndicesOfCoveredObjects = new IntOpenHashSet(ruleConditions.getIndicesOfCoveredObjects()); //translate list to hash set to accelerate subsequent removeAll method execution
			setB.removeAll(setOfIndicesOfCoveredObjects);
		}
	
		return ruleInducerComponents.getRuleConditionsSetPruner().prune(approximatedSetRuleConditions, indicesOfApproximationObjects); //remove redundant rules, but keep covered all objects from lower/upper approximation
	}
	
	/**
	 * TODO
	 * Gets parameters used during induction of decision rules by VC-DomLEMalgorithm.
	 *  
	 * @return parameters used during induction of decision rules
	 */
	public RuleInducerComponentsProvider getRuleInducerComponentsProvider() {
		return this.ruleInducerComponentsProvider;
	}

}
