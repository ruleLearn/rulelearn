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

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Coverage information concerning a single {@link Rule decision rule}, like: set of objects positive with respect to the rule,
 * set of objects neutral with respect to the rule, and other information offered by the super-type {@link BasicRuleCoverageInformation}.
 * If considered rule happens to cover some neutral objects, it should have no influence on any rule's characteristics.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleCoverageInformation extends BasicRuleCoverageInformation {
	//rule coverage info
	//rule coverage report
	//rule coverage summary
	//rule description
	
	/**
	 * Indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part) of considered decision rule.
	 * In case of a certain/possible rule, these are the objects from considered approximated set.
	 */
	IntSet indicesOfPositiveObjects = null;
	
	/**
	 * Indices of all neutral objects from rule's learning information (decision) table. Can be an empty set.
	 * Neutral objects are such objects that their decision is neither positive nor negative with respect to the approximated set used to induce the rule.
	 */
	IntSet indicesOfNeutralObjects = null;
	
	/**
	 * Constructs this rule coverage info.
	 * 
	 * @param ruleConditions rule conditions supplying useful information: {@link RuleConditions#getIndicesOfCoveredObjects() indices of covered objects},
	 *        {@link RuleConditions#getLearningInformationTable() learning information table}, {@link RuleConditions#getIndicesOfPositiveObjects() indices of positive objects},
	 *        and {@link RuleConditions#getIndicesOfNeutralObjects() indices of neutral objects}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	public RuleCoverageInformation(RuleConditions ruleConditions) {
		super(ruleConditions);
		
		indicesOfPositiveObjects = ruleConditions.getIndicesOfPositiveObjects();
		indicesOfNeutralObjects = ruleConditions.getIndicesOfNeutralObjects();
	}
	
	/**
	 * Gets indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part)
	 * of considered decision rule. In case of a certain/possible rule, these are the objects from considered approximated set.
	 * 
	 * @return indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part)
	 *         of considered decision rule
	 */
	public IntSet getIndicesOfPositiveObjects() {
		return indicesOfPositiveObjects;
	}

	/**
	 * Gets indices of all objects from rule's learning information (decision) table that are neutral with respect to the rule, i.e., objects such that their decision
	 * is neither positive nor negative with respect to the approximated set used to induce the rule.
	 * 
	 * @return indices of all objects from rule's learning information (decision) table that are neutral with respect to the rule
	 */
	public IntSet getIndicesOfNeutralObjects() {
		return indicesOfNeutralObjects;
	}

}
