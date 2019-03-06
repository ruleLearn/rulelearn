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

package org.rulelearn.approximations;

import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.approximations.Union.UnionType;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTableWithDecisionDistributions;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Union of ordered decision classes, i.e., set of objects whose decision is not worse or not better than given limiting decision.
 * Objects from the information table such that:<br>
 * - upward union's limiting decision is at most as good as their decision, or<br>
 * - downward union's limiting decision is at least as good as their decision,<br>
 * are considered to belong to this union (they are called positive objects).<br>
 * Objects from the information table such that:<br>
 * - upward union's limiting decision is better than their decision, or<br>
 * - downward union's limiting decision is worse than their decision,<br>
 * are considered to belong to the complement of this union (they are called negative objects).<br>
 * Remaining objects from the information table are called neutral objects. Their decision is uncomparable with this union's decision.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
//E.g., if there are five decision classes: 1, 2, 3, 4, 5, and this union concerns classes 3-5 (&gt;=3),
//* then the complementary union concerns classes 1-2 (&lt;=2).
public class UnionWithSingleLimitingDecision extends Union {
	
	/**
	 * Limiting decision, determining which objects from the information table belong to this union.
	 */
	protected Decision limitingDecision;
	
	/**
	 * Indicates if objects having decision equal to the limiting decision of this union should be included in this union.
	 * Value of this field affects calculation of objects belonging to this union.
	 * Defaults to {@code true}.
	 */
	protected boolean includeLimitingDecision = true;
	
	/**
	 * Tells if given decision is positive with respect to the union of ordered decision classes that would be constructed for given parameters.
	 * See {@link #isDecisionPositive(Decision)}.
	 * 
	 * @param decision decision to verify for being positive with the union of ordered decision classes that would be constructed for given parameters
	 * @param unionType see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param limitingDecision see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param informationTable see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * 
	 * @return {@code true} if given decision is positive with the union of ordered decision classes that would be constructed for given parameters,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidTypeException see {@link #validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}
	 * @throws InvalidValueException see {@link #validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}
	 */
	public static boolean isDecisionPositive(Decision decision, UnionType unionType, Decision limitingDecision, InformationTableWithDecisionDistributions informationTable) {
		Union minimalUnion = new UnionWithSingleLimitingDecision(unionType, limitingDecision, informationTable); //construct a minimal union, sufficient to call isDecisionPositive method
		return minimalUnion.isDecisionPositive(decision);
	}

	/**
	 * Constructs union of ordered decision classes of given type (at least or at most), using given limiting decision (concerning the least or the most preferred decision class). Calculates objects
	 * belonging to this union and neutral objects. Stores given information table and given rough set calculator.
	 * 
	 * @param unionType type of this union; see {@link UnionType}
	 * @param limitingDecision decision that serves as a limit for this union; e.g., decision "3" is a limit for union "at least 3" and "at most 3" 
	 * @param informationTable information table with considered objects, some of which belong to this union
	 * @param roughSetCalculator dominance-based rough set calculator used to calculate approximations and boundary of this union
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidTypeException if any of the attributes contributing to given limiting decision is not an evaluation attribute
	 * @throws InvalidValueException if any of the attributes contributing to given limiting decision is not an active decision attribute
	 * @throws InvalidValueException if none of the attributes contributing to given limiting decision is ordinal (i.e., has gain- or cost-type preference)
	 */
	public UnionWithSingleLimitingDecision(UnionType unionType, Decision limitingDecision, InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator) {
		super(unionType, informationTable, roughSetCalculator);
		this.limitingDecision = notNull(limitingDecision, "Limiting decision for constructed union is null.");
		validateLimitingDecision(limitingDecision, informationTable);
		
		this.findObjects();
	}
	
	/**
	 * Constructs union of ordered decision classes of given type (at least or at most), using given limiting decision (concerning the least or the most preferred decision class). Calculates objects
	 * belonging to this union and neutral objects. Stores given information table and given rough set calculator. Takes into account the flag concerning inclusion of objects
	 * having decision equal to the limiting decision of this union. 
	 * 
	 * @param unionType see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param limitingDecision see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param informationTable see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param roughSetCalculator see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param includeLimitingDecision tells if objects having decision equal to the limiting decision of this union should be included in this union
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidTypeException see {@link #validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}
	 * @throws InvalidValueException see {@link #validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}
	 */
	UnionWithSingleLimitingDecision(UnionType unionType, Decision limitingDecision, InformationTableWithDecisionDistributions informationTable, DominanceBasedRoughSetCalculator roughSetCalculator, boolean includeLimitingDecision) {
		super(unionType, informationTable, roughSetCalculator);
		this.limitingDecision = notNull(limitingDecision, "Limiting decision for constructed union is null.");
		validateLimitingDecision(limitingDecision, informationTable);
		
		this.includeLimitingDecision = includeLimitingDecision; //set flag concerning inclusion of limiting decision
		
		this.findObjects();
	}
	
	/**
	 * Constructs union of ordered decision classes of given type (at least or at most), using given limiting decision
	 * (concerning the least or the most preferred decision class).
	 * Stores given information table.<br>
	 * <br>
	 * This is a minimal constructor that can be used to quickly construct a lightweight union,
	 * providing limited functionality. In particular, this constructor does not calculate objects belonging to this union (nor neutral objects),
	 * which is a time consuming process. Moreover, it does not set rough set calculator.
	 * 
	 * @param unionType see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param limitingDecision see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * @param informationTable see {@link #Union(UnionType, Decision, InformationTableWithDecisionDistributions, DominanceBasedRoughSetCalculator)}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidTypeException see {@link #validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}
	 * @throws InvalidValueException see {@link #validateLimitingDecision(Decision, InformationTableWithDecisionDistributions)}
	 */
	private UnionWithSingleLimitingDecision(UnionType unionType, Decision limitingDecision, InformationTableWithDecisionDistributions informationTable) {
		super(unionType, informationTable);
		
		notNull(limitingDecision, "Limiting decision for constructed union is null.");
		validateLimitingDecision(limitingDecision, informationTable);
		this.limitingDecision = limitingDecision;
	}
	
	/**
	 * Validates given limiting decision, taking into account given information table. Checks if each attribute contributing to the given decision
	 * is an evaluation attribute, is active, and of type {@link AttributeType#DECISION}. Moreover, at least one of the attributes has to have
	 * preference type different than {@link AttributePreferenceType#NONE}.
	 * 
	 * @param limitingDecision decision that serves as a limit for this union; e.g., decision "3" is a limit for union "at least 3" and "at most 3"
	 * @param informationTable information table with considered objects, some of which belong to this union
	 * @return {@code true} if given decision is valid in the context of the given information table, throws exception otherwise
	 * 
	 * @throws InvalidTypeException if any of the attributes contributing to given limiting decision is not an evaluation attribute
	 * @throws InvalidValueException if any of the attributes contributing to given limiting decision is not an active decision attribute
	 * @throws InvalidValueException if none of the attributes contributing to given limiting decision is ordinal (i.e., has gain- or cost-type preference)
	 */
	protected boolean validateLimitingDecision(Decision limitingDecision, InformationTableWithDecisionDistributions informationTable) {
		IntSet attributeIndices = limitingDecision.getAttributeIndices();
		IntIterator attributeIndicesIterator  = attributeIndices.iterator();
		int attributeIndex;
		Attribute attribute;
		EvaluationAttribute evaluationAttribute;
		
		boolean activeDecisionCriterionFound = false;
		
		//check attributes contributing to the limiting decision
		while (attributeIndicesIterator.hasNext()) {
			attributeIndex = attributeIndicesIterator.nextInt();
			attribute = informationTable.getAttribute(attributeIndex);
			
			if (attribute instanceof EvaluationAttribute) {
				evaluationAttribute = (EvaluationAttribute) attribute;
				if (evaluationAttribute.isActive() && evaluationAttribute.getType() == AttributeType.DECISION) { //active decision attribute
					if (evaluationAttribute.getPreferenceType() != AttributePreferenceType.NONE) { //gain/cost-type attribute
						activeDecisionCriterionFound = true; //mark finding, but do not break the loop, as it should be also checked if next attributes are correct
					}
				} else {
					throw new InvalidValueException("Attribute no. "+attributeIndex+" contributing to union's limiting decision is not an active decision attribute.");
				}
			} else {
				throw new InvalidTypeException("Attribute no. "+attributeIndex+" contributing to union's limiting decision is not an evaluation attribute.");
			}
		} //while
		
		if (!activeDecisionCriterionFound) {
			throw new InvalidValueException("Cannot create union of ordered decision classes - none of the attributes contributing to union's limiting decision is ordinal.");
		}
		
		return true;
	}
	
	/**
	 * Gets limiting decision of this union, determining which objects from the information table belong to this union.
	 * 
	 * @return limiting decision of this union, determining which objects from the information table belong to this union
	 */
	public Decision getLimitingDecision() {
		return limitingDecision;
	}
	
	/**
	 * Calculates complementary union of decision classes that complements this union w.r.t. set of all objects U.
	 * Calculated union has the same limiting decision, but does not include objects with that decision. Moreover, it has opposite union type.
	 * 
	 * @return complementary union of decision classes
	 */
	protected UnionWithSingleLimitingDecision calculateComplementaryUnion() {
		UnionType complementaryUnionType = null;
		
		switch (this.unionType) {
		case AT_LEAST:
			complementaryUnionType = UnionType.AT_MOST;
			break;
		case AT_MOST:
			complementaryUnionType = UnionType.AT_LEAST;
			break;
		}
		
		return new UnionWithSingleLimitingDecision(complementaryUnionType, this.limitingDecision, this.getInformationTable(), this.getRoughSetCalculator(), false);
	}
	
	/**
	 * Tests if this union is concordant with given decision. In case of an upward union, returns:<br>
	 * - {@link TernaryLogicValue#TRUE} if limiting decision of this union is at most as good as the given decision
	 *   (strictly worse if {@link #includeLimitingDecision == false),<br>
	 * - {@link TernaryLogicValue#FALSE} if limiting decision of this union	is strictly better than the given decision
	 *   (strictly better or equal if {@link #includeLimitingDecision == false),<br>
	 * - {@link TernaryLogicValue#UNCOMPARABLE} otherwise.<br> 
	 * In case of a downward union, returns:<br>
	 * - {@link TernaryLogicValue#TRUE} if limiting decision of this union is at least as good as the given decision
	 *   (strictly better if {@link #includeLimitingDecision == false),<br>
	 * - {@link TernaryLogicValue#FALSE} if limiting decision of this union	is strictly worse than the given decision,
	 *   (strictly worse or equal if {@link #includeLimitingDecision == false)<br>
	 * - {@link TernaryLogicValue#UNCOMPARABLE} otherwise.
	 * 
	 * @param decision decision that limiting decision of this union should be compared with
	 * @return {@link TernaryLogicValue#TRUE} if this unions' limiting decision is concordant with given decision,
	 *         {@link TernaryLogicValue#FALSE} if this unions' limiting decision is not concordant with given decision,
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if this unions' limiting decision is uncomparable with given decision
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 */
	@Override
	protected TernaryLogicValue isConcordantWithDecision(Decision decision) {
		notNull(decision, "Decision tested for concordance with union is null.");
		
		if (this.includeLimitingDecision) { //"normal" union
			switch (this.unionType) {
			case AT_LEAST:
				if (this.limitingDecision.isAtMostAsGoodAs(decision) == TernaryLogicValue.TRUE) {
					return TernaryLogicValue.TRUE;
				} else {
					//limiting decision is strictly better (as equality was eliminated above)
					if (this.limitingDecision.isAtLeastAsGoodAs(decision) == TernaryLogicValue.TRUE) {
						return TernaryLogicValue.FALSE;
					} else { //union's limiting decision is uncomparable with given decision 
						return TernaryLogicValue.UNCOMPARABLE;
					}
				}
			case AT_MOST:
				if (this.limitingDecision.isAtLeastAsGoodAs(decision) == TernaryLogicValue.TRUE) {
					return TernaryLogicValue.TRUE;
				} else {
					//limiting decision is strictly worse (as equality was eliminated above)
					if (this.limitingDecision.isAtMostAsGoodAs(decision) == TernaryLogicValue.TRUE) {
						return TernaryLogicValue.FALSE;
					} else { //union's limiting decision is uncomparable with given decision 
						return TernaryLogicValue.UNCOMPARABLE;
					}
				}
			default:
				throw new InvalidValueException("Unexpected union type."); //this should not happen
			}
		} else { //"strict union"
			switch (this.unionType) {
			case AT_LEAST:
				if (this.limitingDecision.isAtLeastAsGoodAs(decision) == TernaryLogicValue.TRUE) { //includes equality of decisions
					return TernaryLogicValue.FALSE;
				} else {
					//limiting decision is strictly worse (as equality was eliminated above)
					if (this.limitingDecision.isAtMostAsGoodAs(decision) == TernaryLogicValue.TRUE) {
						return TernaryLogicValue.TRUE;
					} else { //union's limiting decision is uncomparable with given decision 
						return TernaryLogicValue.UNCOMPARABLE;
					}
				}
			case AT_MOST:
				if (this.limitingDecision.isAtMostAsGoodAs(decision) == TernaryLogicValue.TRUE) { //includes equality of decisions
					return TernaryLogicValue.FALSE;
				} else {
					//limiting decision is strictly better (as equality was eliminated above)
					if (this.limitingDecision.isAtLeastAsGoodAs(decision) == TernaryLogicValue.TRUE) {
						return TernaryLogicValue.TRUE;
					} else { //union's limiting decision is uncomparable with given decision 
						return TernaryLogicValue.UNCOMPARABLE;
					}
				}
			default:
				throw new InvalidValueException("Unexpected union type."); //this should not happen
			}
		}
	}
	
	/**
	 * Tells if this union includes objects whose decision is equal to the limiting decision.
	 * 
	 * @return {@code true} if this union includes objects whose decision is equal to the limiting decision,
	 *         {@code false} otherwise
	 */
	public boolean isIncludeLimitingDecision() {
		return includeLimitingDecision;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastException if given approximated set is not of type {@link Union}
	 */
	@Override
	public boolean includes(ApproximatedSet approximatedSet) {
		UnionWithSingleLimitingDecision otherUnion = (UnionWithSingleLimitingDecision)approximatedSet;
		
		if (unionType == otherUnion.getUnionType()) { //both unions are upward (or downward)
			TernaryLogicValue isConcordant = isConcordantWithDecision(otherUnion.getLimitingDecision());
			
			if (otherUnion.isIncludeLimitingDecision()) { //checked limiting decision of the other union is concordant also with the other union
				return isConcordant == TernaryLogicValue.TRUE;
			} else {
				if (isConcordant == TernaryLogicValue.TRUE) {
					return true;
				} else {
					if (isConcordant == TernaryLogicValue.FALSE &&
							this.includeLimitingDecision == false &&
							this.limitingDecision.equals(otherUnion.getLimitingDecision())) { //correct negative answer in a particular case 
						return true;
					} else {
						return false;
					}
				}
			} //else
		} else {
			return false;
		}

	}
	
}
