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

import java.util.List;
import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.Field;
import static org.rulelearn.core.Precondition.notNull;
import static org.rulelearn.core.Precondition.nonEmpty;

/**
 * Decision rule composed of elementary conditions on the LHS, connected by "and", and decisions on the RHS, connected by "or".
 * The rule is immutable, i.e., it has all conditions and decisions fixed in constructor.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Rule {
	
	/**
	 * Type of this rule. See {@link RuleType}.
	 */
	protected RuleType type;
	
	/**
	 * Semantics of this rule. See {@link RuleSemantics}.
	 */
	protected RuleSemantics semantics;
	
	/**
	 * Value of decision attribute inherent for this rule.
	 * Value v of a decision attribute d is inherent for this rule if:<br>
     * - positive examples for which this rule was induced come from lower/upper approximation or boundary of a set of objects x such that d(x) is equal to v,<br>
     * - positive examples for which this rule was induced come from lower/upper approximation or boundary of a set of objects x such that d(x) is at least as good as v,<br>
     * - positive examples for which this rule was induced come from lower/upper approximation or boundary of a set of objects x such that d(x) is at most as good as v.
	 */
	protected Field inherentDecision;
	
	/**
     * Array with conditions building condition part of this rule, stored in order in which they were added to this rule.
     * If there is more than one condition, they are treated as connected by the AND operator.
     */
    protected Condition[] conditions = null;
	
	/**
     * Array with conditions building decision part of this rule.
     * If there is more than one decision, they are treated as connected by the OR operator.
     */
    protected Condition[] decisions = null;
    
    /**
     * Value appended to the beginning of a rule while transforming the rule to text form.
     * May be changed to another value (for example "if").
     */
    public static String ruleStarter = "";
    
    /**
     * Conjunction used when concatenating rule's conditions while transforming the rule to text form.
     * May be changed to another value (for example "and").
     */
    public static String andConjunction = "&";
    
    /**
     * Conjunction used when concatenating rule's decisions while transforming the rule to text form.
     * May be changed to another value (for example "or").
     */
    public static String orConjunction = "OR";
    
    /**
     * Delimiter inserted between condition and decision parts of the rule while transforming rule to text form.
     * May be changed to another value (for example "then").
     */
    public static String conditionsAndDecisionsDelimiter = "=>";
    
    /**
     * Constructor initializing all fields. Each argument should be set (not {@code null}).
     * 
     * @param type type of constructed rule; see {@link RuleType}
     * @param semantics semantics of constructed rule; see {@link RuleSemantics}
     * @param inherentDecision value of decision attribute inherent for this rule; see {@link #inherentDecision}
     * @param conditions list with conditions building condition (LHS) part of this rule
     * @param decisions list with decisions building decision (RHS) part of this rule
     * 
     * @throws NullPointerException if any of the parameters is {@code null}
     * @throws InvalidSizeException if the list with decisions is empty
     */
    public Rule(RuleType type, RuleSemantics semantics, Field inherentDecision, List<Condition> conditions, List<Condition> decisions) {
    	this.type = notNull(type, "Rule's type is null.");
    	this.semantics = notNull(semantics, "Rule's semantics is null.");
    	this.inherentDecision = notNull(inherentDecision, "Rule's inherent decision is null.");
    	this.conditions = notNull(conditions, "Rule's conditions are null.").toArray(new Condition[0]);
    	this.decisions = nonEmpty(notNull(decisions, "Rule's decisions are null."), "Rule decisions are empty.").toArray(new Condition[0]);
    }

	/**
	 * Gets type of this rule. See {@link RuleType}.
	 * 
	 * @return rule's type
	 */
	public RuleType getType() {
		return type;
	}

	/**
	 * Gets semantics of this rule. See {@link RuleSemantics}.
	 * 
	 * @return rule's semantics
	 */
	public RuleSemantics getSemantics() {
		return semantics;
	}

	/**
	 * Gets inherent decision of this rule. See {@link #inherentDecision}.
	 * 
	 * @return rule's inherent decision
	 */
	public Field getInherentDecision() {
		return inherentDecision;
	}

	/**
	 * Gets array with conditions of this rule (in order in which they were added to this rule).
	 * 
	 * @return the conditions array with conditions of this rule (in order in which they were added to this rule).
	 */
	public Condition[] getConditions() {
		return this.getConditions(false);
	}
	
	/**
	 * Gets array with conditions of this rule (in order in which they were added to this rule).
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return the conditions array with conditions of this rule (in order in which they were added to this rule).
	 * 
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Condition[] getConditions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? conditions : conditions.clone();
	}	

	/**
	 * Gets array with decisions of this rule.
	 * 
	 * @return array with decisions of this rule
	 */
	public Condition[] getDecisions() {
		return this.getDecisions(false);
	}
	
	/**
	 * Gets array with decisions of this rule.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array with decisions of this rule
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Condition[] getDecisions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? decisions : decisions.clone();
	}
	
	/**
	 * Gets the first (and possibly the only) decision suggested by this rule.
	 * 
	 * @return the first (and possibly the only) decision suggested by this rule
	 */
	public Condition getDecision() {
		return this.decisions[0];
	}
	
	/**
	 * Gets text representation of this rule.
	 * 
	 * @return text representation of this rule
	 */
	public String toString() {
		StringBuilder sB = new StringBuilder();
		
		//start rule
		if (ruleStarter != null && !ruleStarter.equals("")) {
			sB.append(ruleStarter).append(" ");
		}

		//append conditions
		for (int i = 0; i < this.conditions.length; i++) {
			sB.append(this.conditions[i].toString());
			
			if (i < this.conditions.length - 1) { //not last condition
				sB.append(" ").append(andConjunction).append(" ");
			}
		}
		
		//insert delimiter between conditions and decisions
		sB.append(" ").append(conditionsAndDecisionsDelimiter).append(" "); //TODO: append (c) or (p) after the delimiter?
		
		//append decisions
		for (int i = 0; i < this.decisions.length; i++) {
			sB.append(this.decisions[i].toString());

			if (i < this.decisions.length - 1) { // not last decision
				sB.append(" ").append(orConjunction).append(" ");
			}
		}
		
		return sB.toString();
	}

	/**
	 * Verifies if this rule covers an object in an information table.
	 *  
	 * @param objectIndex index of an object in the given information table
	 * @param informationTable information table containing object with given index
	 * @return {@code true} if this rule covers considered object,
	 *         {@code false} otherwise
	 * 
	 * @throws IndexOutOfBoundsException see {@link Condition#fulfilledBy(int, InformationTable)}
	 * @throws NullPointerException see {@link Condition#fulfilledBy(int, InformationTable)}
	 */
	public boolean covers(int objectIndex, InformationTable informationTable) {
		for (int i = 0; i < this.conditions.length; i++) {
			if (!this.conditions[i].fulfilledBy(objectIndex, informationTable)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifies if (at least one) decision of this rule is verified by an object in an information table.
	 * 
	 * @param objectIndex index of an object in the given information table
	 * @param informationTable information table containing object with given index
	 * @return {@code true} if at least one decision of this rule is verified by the considered object,
	 *         {@code false} otherwise
	 * 
	 * @throws IndexOutOfBoundsException see {@link Condition#fulfilledBy(int, InformationTable)}
	 * @throws NullPointerException see {@link Condition#fulfilledBy(int, InformationTable)}
	 */
	public boolean decisionsMatchedBy(int objectIndex, InformationTable informationTable) {
		//check if at least one rule's decision is verified by the considered object
		for (int i = 0; i < this.decisions.length; i++) {
			if (this.decisions[i].fulfilledBy(objectIndex, informationTable)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifies if this rule is supported by an object from an information table.
	 * 
	 * @param objectIndex index of an object in the given information table
	 * @param informationTable information table containing object with given index
	 * @return {@code true} if this rule covers considered object and the object also matches decisions of this rule,
	 *         {@code false} otherwise
	 * 
	 * @throws IndexOutOfBoundsException see {@link #covers(int, InformationTable)}
	 * @throws NullPointerException see {@link #covers(int, InformationTable)}
	 */
	public boolean supportedBy(int objectIndex, InformationTable informationTable) {
		return this.covers(objectIndex, informationTable) && this.decisionsMatchedBy(objectIndex, informationTable);
	}

}
