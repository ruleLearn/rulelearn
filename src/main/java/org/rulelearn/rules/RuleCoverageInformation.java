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

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Coverage information concerning a single decision rule {@link Rule}, like set of objects positive with respect to the rule,
 * set of objects neutral with respect to the rule, set of objects covered by the rule, and number of all objects
 * in the information table that was used to induce the rule. If considered rule happens to cover some neutral objects,
 * it should have no influence on any rule's characteristics.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleCoverageInformation {
	//rule coverage info
	//rule coverage report
	//rule coverage summary
	//rule description
	
	/**
	 * Indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part) of considered decision rule.
	 * In case of a certain/possible rule, these are the objects from considered approximated set.
	 */
	IntSet indicesOfPositiveObjects;
	
	/**
	 * Indices of all neutral objects from rule's learning information (decision) table. Can be an empty set.
	 * Neutral objects are such that their decision is uncomparable with the limiting decision obtained by {@link ApproximatedSet#getLimitingDecision()}
	 * for the approximated set used to induce the rule.
	 */
	IntSet indicesOfNeutralObjects;
	
	/**
	 * Indices of objects from rule's learning information table that are covered by the rule.
	 */
	IntList indicesOfCoveredObjects;
	
	/**
	 * Number of all objects in rule's learning information table.
	 */
	int allObjectsCount;
	
	/**
	 * Constructs this rule coverage info.
	 * 
	 * @param indicesOfPositiveObjects indices of all objects from rule's learning information (decision) table that satisfy right-hand side (RHS, decision part)
	 *        of considered decision rule; in case of a certain/possible rule, these are the objects from considered approximated set
	 * @param indicesOfNeutralObjects indices of all neutral objects from rule's learning information (decision) table, i.e., objects such that their decision
	 *        is uncomparable with the limiting decision obtained by {@link ApproximatedSet#getLimitingDecision()}
	 *        for the approximated set used to induce the rule
	 * @param indicesOfCoveredObjects indices of all objects from rule's learning information table that are covered by the rule
	 * @param allObjectsCount number of all objects in rule's learning information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if given number of all objects is less than zero
	 */
	public RuleCoverageInformation(IntSet indicesOfPositiveObjects, IntSet indicesOfNeutralObjects, IntList indicesOfCoveredObjects, int allObjectsCount) {
		super();
		this.indicesOfPositiveObjects = Precondition.notNull(indicesOfPositiveObjects, "Positive objects are null.");
		this.indicesOfNeutralObjects = Precondition.notNull(indicesOfNeutralObjects, "Neutral objects are null.");
		this.indicesOfCoveredObjects = Precondition.notNull(indicesOfCoveredObjects, "Covered objects are null.");
		this.allObjectsCount = Precondition.nonNegative(allObjectsCount, "Number of objects is less than zero.");
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
	 * is uncomparable with the limiting decision obtained by {@link ApproximatedSet#getLimitingDecision()} for the approximated set used to induce the rule.
	 * 
	 * @return indices of all objects from rule's learning information (decision) table that are neutral with respect to the rule
	 */
	public IntSet getIndicesOfNeutralObjects() {
		return indicesOfNeutralObjects;
	}

	/**
	 * Gets indices of all objects from rule's learning information table that are covered by the rule.
	 * 
	 * @return indices of all objects from rule's learning information table that are covered by the rule
	 */
	public IntList getIndicesOfCoveredObjects() {
		return indicesOfCoveredObjects;
	}

	/**
	 * Gets number of all objects in rule's learning information table.
	 * 
	 * @return number of all objects in rule's learning information table
	 */
	public int getAllObjectsCount() {
		return allObjectsCount;
	}
}
