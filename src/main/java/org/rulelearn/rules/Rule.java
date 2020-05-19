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

import static org.rulelearn.core.Precondition.nonEmpty;
import static org.rulelearn.core.Precondition.notNull;

import java.util.List;

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.data.Decision;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

/**
 * Decision rule composed of elementary conditions on the left-hand-side (LHS), connected by "and" connective, and elementary decisions on the right-hand-side (RHS)
 * represented by {@link Rule.RuleDecisions} object.<br>
 * <br>
 * The rule is immutable, i.e., it has all conditions and decisions fixed in constructor.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Rule {
	
	/**
	 * Abstract rule's RHS.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	abstract static class RuleDecisions {
	    /**
	     * Checks if an object from the information table, defined by its index, fulfills rule's RHS.
	     * 
	     * @param objectIndex index of an object in the given information table
	     * @param informationTable information table containing the object to check
	     * 
	     * @return {@code true} if considered object satisfies rule's RHS,
	     *         {@code false} otherwise
	     * 
	     * @throws IndexOutOfBoundsException if given object index does not correspond to any object in the given information table
	     * @throws NullPointerException if given information table is {@code null}
	     */
	    public abstract boolean satisfiedBy(int objectIndex, InformationTable informationTable);
	}
	
	/**
	 * Rule's RHS composed of a single elementary decision.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class ElementaryDecision extends RuleDecisions {
		Condition<? extends EvaluationField> elementaryDecision;
		
		/**
		 * Constructs rule's RHS composed of a single elementary decision.
		 * 
		 * @param elementaryDecision elementary decision representing rule's RHS
		 * @throws NullPointerException if given object is {@code null}
		 */
		public ElementaryDecision(Condition<? extends EvaluationField> elementaryDecision) {
			this.elementaryDecision = Precondition.notNull(elementaryDecision, "Elementary decision for rule's RHS is null.");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws IndexOutOfBoundsException {@inheritDoc}
	     * @throws NullPointerException {@inheritDoc}
		 */
		@Override
		public boolean satisfiedBy(int objectIndex, InformationTable informationTable) {
			return this.elementaryDecision.satisfiedBy(objectIndex, informationTable);
		}
		
		/**
		 * Gets underlying elementary decision.
		 * 
		 * @return underlying elementary decision
		 */
		@SuppressWarnings("unchecked")
		public Condition<EvaluationField> getElementaryDecision() {
			return (Condition<EvaluationField>)elementaryDecision; //this cast is safe
		}
	}
	
	/**
	 * Compound rule's RHS composed of AND-connected objects of type {@link Rule.RuleDecisions}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class ANDConnectedRuleDecisions extends RuleDecisions {
		ObjectList<RuleDecisions> andConnectedRuleDecisions;
		
		/**
		 * Constructs compound rule's RHS composed of AND-connected rule decisions {@link Rule.RuleDecisions}.
		 * 
		 * @param andConnectedRuleDecisions list of rule decisions that should be connected with AND connective to form rule's RHS
		 * @throws NullPointerException if given list is {@code null}
		 * @throws InvalidSizeException if given list is empty
		 */
		public ANDConnectedRuleDecisions(List<RuleDecisions> andConnectedRuleDecisions) {
			Precondition.nonEmpty(Precondition.notNull(andConnectedRuleDecisions, "List with AND-connected rule decisions is null."), "List with AND-connected rule decisions is empty.");
			this.andConnectedRuleDecisions = new ObjectArrayList<RuleDecisions>();
			for (RuleDecisions ruleDecisions : andConnectedRuleDecisions) {
				this.andConnectedRuleDecisions.add(Precondition.notNull(ruleDecisions, "Some rule decisions on a list of AND-connected rule decisions are null."));
			}
		}
		
		/**
		 * Constructs compound rule's RHS composed of AND-connected rule decisions {@link Rule.RuleDecisions}.
		 * 
		 * @param andConnectedRuleDecisions vararg (array) of rule decisions that should be connected with AND connective to form rule's RHS
		 * @throws NullPointerException if given array is {@code null}
		 * @throws InvalidSizeException if given array is empty
		 */
		public ANDConnectedRuleDecisions(RuleDecisions... andConnectedRuleDecisions) {
			Precondition.nonEmpty(Precondition.notNull(andConnectedRuleDecisions, "Array with AND-connected rule decisions is null."), "Array with AND-connected rule decisions is empty.");
			this.andConnectedRuleDecisions = new ObjectArrayList<RuleDecisions>();
			for (RuleDecisions ruleDecisions : andConnectedRuleDecisions) {
				this.andConnectedRuleDecisions.add(Precondition.notNull(ruleDecisions, "Some rule decisions on a list of AND-connected rule decisions are null."));
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws IndexOutOfBoundsException {@inheritDoc}
	     * @throws NullPointerException {@inheritDoc}
		 */
		public boolean satisfiedBy(int objectIndex, InformationTable informationTable) {
			for (RuleDecisions ruleDecisions : andConnectedRuleDecisions) {
				if (!ruleDecisions.satisfiedBy(objectIndex, informationTable)) {
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Gets underlying list of rule decisions.
		 * 
		 * @return underlying list of rule decisions
		 */
		public List<RuleDecisions> getRuleDecisions() {
			return ObjectLists.unmodifiable(andConnectedRuleDecisions);
		}
	}
	
	/**
	 * Compound rule's RHS composed of OR-connected objects of type {@link Rule.RuleDecisions}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class ORConnectedRuleDecisions extends RuleDecisions {
		ObjectList<RuleDecisions> orConnectedRuleDecisions;
		
		/**
		 * Constructs compound rule's RHS composed of OR-connected rule decisions {@link Rule.RuleDecisions}.
		 * 
		 * @param orConnectedRuleDecisions list of rule decisions that should be connected with OR connective to form rule's RHS
		 * @throws NullPointerException if given list is {@code null}
		 * @throws InvalidSizeException if given list is empty
		 */
		public ORConnectedRuleDecisions(List<RuleDecisions> orConnectedRuleDecisions) {
			Precondition.nonEmpty(Precondition.notNull(orConnectedRuleDecisions, "List with OR-connected rule decisions is null."), "List with OR-connected rule decisions is empty.");
			this.orConnectedRuleDecisions = new ObjectArrayList<RuleDecisions>();
			for (RuleDecisions ruleDecisions : orConnectedRuleDecisions) {
				this.orConnectedRuleDecisions.add(Precondition.notNull(ruleDecisions, "Some rule decisions on a list of OR-connected rule decisions are null."));
			}
		}
		
		/**
		 * Constructs compound rule's RHS composed of OR-connected rule decisions {@link Rule.RuleDecisions}.
		 * 
		 * @param orConnectedRuleDecisions vararg (array) of rule decisions that should be connected with OR connective to form rule's RHS
		 * @throws NullPointerException if given array is {@code null}
		 * @throws InvalidSizeException if given array is empty
		 */
		public ORConnectedRuleDecisions(RuleDecisions... orConnectedRuleDecisions) {
			Precondition.nonEmpty(Precondition.notNull(orConnectedRuleDecisions, "Array with OR-connected rule decisions is null."), "Array with OR-connected rule decisions is empty.");
			this.orConnectedRuleDecisions = new ObjectArrayList<RuleDecisions>();
			for (RuleDecisions ruleDecisions : orConnectedRuleDecisions) {
				this.orConnectedRuleDecisions.add(Precondition.notNull(ruleDecisions, "Some rule decisions on a list of OR-connected rule decisions are null."));
			}
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @throws IndexOutOfBoundsException {@inheritDoc}
	     * @throws NullPointerException {@inheritDoc}
		 */
		public boolean satisfiedBy(int objectIndex, InformationTable informationTable) {
			for (RuleDecisions ruleDecisions : orConnectedRuleDecisions) {
				if (ruleDecisions.satisfiedBy(objectIndex, informationTable)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Gets underlying list of rule decisions.
		 * 
		 * @return underlying list of rule decisions
		 */
		public List<RuleDecisions> getRuleDecisions() {
			return ObjectLists.unmodifiable(orConnectedRuleDecisions);
		}
	}
	
	//TODO: handle connectives of elementary decisions in more general way
	
	/**
	 * Type of connective between conditions from a considered list.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static enum ConditionConnectiveType {
		/**
		 * Connective indicating that conditions from a considered list should be satisfied jointly.
		 */
		OR,
		/**
		 * Connective indicating that at least one condition from a considered list should be satisfied.
		 */
		AND
	}
	
	/**
	 * Type of this rule. See {@link RuleType}.
	 */
	protected RuleType type; //TODO: is this information stored at the single rule level or at the rule set level?
	
	/**
	 * Semantics of this rule. See {@link RuleSemantics}.
	 */
	protected RuleSemantics semantics; //TODO: is this information stored at the single rule level or at the rule set level?
	
	/**
     * Array with conditions building condition part of this rule, stored in order in which they were added to this rule.
     * If there is more than one condition, they are treated as connected by the AND operator.
     */
    protected Condition<? extends EvaluationField>[] conditions = null;
	
	/**
     * Array with elementary decisions building decision part of this rule.
     * Conditions in each row (for fixed first array index) are considered to be connected with AND connective.
     * Rows are considered to be connected using OR connective.
     */
    protected Condition<? extends EvaluationField>[][] decisions = null; //TODO: rewrite to use ruleDecisions object
    
    /**
     * Elementary decisions building decision part of this rule, possibly connected by logical connectives (AND, OR).
     * For example, rule decisions may look like this: (dec1a OR dec1b) AND (dec2a OR dec2b).
     * Such combination can be obtained this way:<br>
     * new {@link Rule.ANDConnectedRuleDecisions} (<br>
     * &nbsp;&nbsp;new {@link Rule.ORConnectedRuleDecisions} (new {@link Rule.ElementaryDecision}(dec1a), new {@link Rule.ElementaryDecision}(dec1b)),<br>
     * &nbsp;&nbsp;new {@link Rule.ORConnectedRuleDecisions} (new {@link Rule.ElementaryDecision}(dec2a), new {@link Rule.ElementaryDecision}(dec2b))<br>
     * ).
     */
    protected RuleDecisions ruleDecisions = null; //TODO: use this field!
    
    /**
     * Value appended to the beginning of a rule while transforming the rule to text form.
     * May be changed to another value (for example "if").
     */
    public static String ruleStarter = "";
    
    /**
     * Connective used when concatenating rule's conditions and decisions while transforming the rule to text form.
     * May be changed to another value (for example "and").
     */
    public static String andConnective = "&";
    
    /**
     * Connective used when concatenating rule's decisions while transforming the rule to text form.
     * May be changed to another value (for example "||").
     */
    public static String orConnective = "OR";
    
    /**
     * Delimiter inserted between condition and decision parts of the rule while transforming rule to text form.
     * May be changed to another value (for example "then").
     */
    public static String conditionsAndDecisionsDelimiter = "=>";
    
    /**
     * Extracts elementary decisions from the given list of lists of elementary decisions, validates everything, and saves decisions as {@link #decisions}.
     * 
     * @param decisions elementary decisions to be saved in this rule
     * 
     * @throws NullPointerException if any list with elementary decisions is {@code null}, or any elementary decision is {@code null}
     * @throws InvalidSizeException if any list with elementary decisions is empty
     */
    private void saveDecisions(List<? extends List<? extends Condition<? extends EvaluationField>>> decisions) { //TODO: save decisions in more general way
    	nonEmpty(notNull(decisions, "Rule's decisions are null."), "Rule's decisions are empty.");
    	
    	int numberOfAlternativeDecisions = decisions.size();
    	this.decisions = new Condition<?>[numberOfAlternativeDecisions][];
    	for (int i = 0; i < numberOfAlternativeDecisions; i++) {
    		nonEmpty(notNull(decisions.get(i), "Rule's decisions are null."), "Rule's decisions are empty.");
    		this.decisions[i] = new Condition<?>[decisions.get(i).size()];
    		for (int j = 0; j < this.decisions[i].length; j++) {
    			this.decisions[i][j] = notNull(decisions.get(i).get(j), "Decision is null.");
    		}
    	}
    }
    
    /**
     * Extracts elementary decisions from the given list of elementary decisions, validates everything, and saves decisions as {@link #decisions}.
     * 
     * @param decisions elementary decisions to be saved in this rule
     * @param decisionConnectiveType type of connective between decisions from the given list
     * 
     * @throws NullPointerException if any of the parameters is {@code null}, or any elementary decision from the given list is {@code null}
     * @throws InvalidSizeException if the given list with elementary decisions is empty
     */
    private void saveDecisions(List<? extends Condition<? extends EvaluationField>> decisions, ConditionConnectiveType decisionConnectiveType) { //TODO: save decisions in more general way
    	nonEmpty(notNull(decisions, "Rule's decisions are null."), "Rule's decisions are empty.");
    	
    	switch (notNull(decisionConnectiveType, "Decision connective type is null.")) {
    	case AND:
    		int andDecisionsCount = decisions.size();
    		this.decisions = new Condition<?>[1][];
    		this.decisions[0] = new Condition<?>[andDecisionsCount];
    		
        	for (int i = 0; i < andDecisionsCount; i++) {
        		this.decisions[0][i] = notNull(decisions.get(i), "Decision is null.");
        	}
    		break;
    	case OR:
    		int orDecisionsCount = decisions.size();
    		this.decisions = new Condition<?>[orDecisionsCount][];
    		for (int i = 0; i < orDecisionsCount; i++) {
    			this.decisions[i] = new Condition<?>[1];
    			this.decisions[i][0] = notNull(decisions.get(i), "Decision is null.");
    		}
    		break;
    	}
    }
    
    /**
     * Most general constructor that enables to built decision rule with multiple elementary decisions.
     * 
     * @param type type of constructed rule; see {@link RuleType}
     * @param semantics semantics of constructed rule; see {@link RuleSemantics}
     * @param conditions list with elementary conditions building condition (LHS) part of this rule
     * @param decisions list with lists of elementary decisions building decision (RHS) part of this rule; it is assumed that the items from the given list are connected with OR connective,
     *        while the items on each "inner" list are connected with AND connective; for instance, if decisions are {@code ((d_1(x) >= 5, d_2(x) <= 2), (d_1(x) <= 8, d_2(x) >= 1))},
     *        then the RHS of this rule is read as: {@code then (d_1(x) >= 5 AND d_2(x) <= 2) OR (d_1(x) <= 8 AND d_2(x) >= 1)}
     * 
     * @throws NullPointerException if any of the parameters is {@code null}, or any list with elementary decisions is {@code null}, or any elementary decision is {@code null}
     * @throws InvalidSizeException if any list with elementary decisions is empty
     */
    public Rule(RuleType type, RuleSemantics semantics, List<? extends Condition<? extends EvaluationField>> conditions, List<? extends List<? extends Condition<? extends EvaluationField>>> decisions) {
    	this.type = notNull(type, "Rule's type is null.");
    	this.semantics = notNull(semantics, "Rule's semantics is null.");
    	this.conditions = notNull(conditions, "Rule's conditions are null.").toArray(new Condition<?>[conditions.size()]);
    	//see https://www.oreilly.com/library/view/learning-java-4th/9781449372477/ch08s10.html
    	//see https://medium.com/omnius/wildcards-in-java-generics-part-1-3-dd2ce5b0e59a
    	//see https://youtu.be/V1vQf4qyMXg?t=22m24s
    	//see http://www.angelikalanger.com/GenericsFAQ/FAQSections/ParameterizedTypes.html#FAQ006
    	//see https://www.simplexacode.ch/en/blog/2018/08/the-problem-with-creating-generic-arrays/
    	//https://softwareengineering.stackexchange.com/questions/257257/suppresswarnings-in-generic-array-declaration
    	
    	saveDecisions(decisions);
    }
    
    /**
     * Constructor that enables to built decision rule with multiple elementary decisions connected with the given connective.
     * 
     * @param type type of constructed rule; see {@link RuleType}
     * @param semantics semantics of constructed rule; see {@link RuleSemantics}
     * @param conditions list with elementary conditions building condition (LHS) part of this rule
     * @param decisions list with elementary decisions building decision (RHS) part of this rule; the items from the given list are connected with OR connective or with AND connective,
     *        depending on the given decision connective type
     * @param decisionConnectiveType type of connective between decisions from the given list
     * 
     * @throws NullPointerException if any of the parameters is {@code null}, or any elementary decision from the given list is {@code null}
     * @throws InvalidSizeException if the given list with elementary decisions is empty
     */
    public Rule(RuleType type, RuleSemantics semantics, List<? extends Condition<? extends EvaluationField>> conditions, List<? extends Condition<? extends EvaluationField>> decisions,
    		ConditionConnectiveType decisionConnectiveType) {
    	this.type = notNull(type, "Rule's type is null.");
    	this.semantics = notNull(semantics, "Rule's semantics is null.");
    	this.conditions = notNull(conditions, "Rule's conditions are null.").toArray(new Condition<?>[conditions.size()]);

    	saveDecisions(decisions, decisionConnectiveType);
    }
    
    /**
     * Constructor that enables to built decision rule with single elementary decision.
     * 
     * @param type type of constructed rule; see {@link RuleType}
     * @param conditions list with conditions building condition (LHS) part of this rule
     * @param decision decision for the decision (RHS) part of this rule
     * 
     * @throws NullPointerException if any of the parameters is {@code null}
     * @throws InvalidValueException if semantics of this rule cannot be established using the given decision - see {@link Condition#getRuleSemantics()}
     */
    public Rule(RuleType type, List<? extends Condition<? extends EvaluationField>> conditions, Condition<? extends EvaluationField> decision) {
    	this.type = notNull(type, "Rule's type is null.");
    	
    	notNull(decision, "Rule's decision is null."); //validate decision
    	
    	this.semantics = decision.getRuleSemantics(); //may throw exception
    	this.conditions = notNull(conditions, "Rule's conditions are null.").toArray(new Condition<?>[conditions.size()]);
    	
    	this.decisions = new Condition<?>[1][1];
    	this.decisions[0][0] = notNull(decision, "Rule's decision is null.");
    }

    /**
     * Constructor that enables to build decision rule using (induced) rule conditions.
     * 
     * @param type type of constructed rule; see {@link RuleType}
     * @param semantics semantics of constructed rule; see {@link RuleSemantics}
     * @param ruleConditions rule conditions to be set on the LHS of this rule
     * @param decisions list with lists of elementary decisions building decision (RHS) part of this rule; it is assumed that the items from the given list are connected with OR connective,
     *        while the items on each "inner" list are connected with AND connective; for instance, if decisions are {@code ((d_1(x) >= 5, d_2(x) <= 2), (d_1(x) <= 8, d_2(x) >= 1))},
     *        then the RHS of this rule is read as: {@code then (d_1(x) >= 5 AND d_2(x) <= 2) OR (d_1(x) <= 8 AND d_2(x) >= 1)}
     * 
     * @throws NullPointerException if any of the parameters is {@code null}, or any list with elementary decisions is {@code null}, or any elementary decision is {@code null}
     * @throws InvalidSizeException if any list with elementary decisions is empty
     */
    public Rule(RuleType type, RuleSemantics semantics, RuleConditions ruleConditions, List<? extends List<? extends Condition<? extends EvaluationField>>> decisions) {
    	this.type = notNull(type, "Rule's type is null.");
    	this.semantics = notNull(semantics, "Rule's semantics is null.");
    	
    	this.conditions = notNull(ruleConditions, "Rule conditions are null.").getConditions().toArray(new Condition<?>[ruleConditions.size()]);
    	
    	saveDecisions(decisions);
    }

    /**
     * Constructor that enables to build decision rule using (induced) rule conditions.
     * 
     * @param type type of constructed rule; see {@link RuleType}
     * @param semantics semantics of constructed rule; see {@link RuleSemantics}
     * @param ruleConditions rule conditions to be set on the LHS of this rule
     * @param decisions list with elementary decisions building decision (RHS) part of this rule; the items from the given list are connected with OR connective or with AND connective,
     *        depending on the given decision connective type
     * @param decisionConnectiveType type of connective between decisions from the given list
     * 
     * @throws NullPointerException if any of the parameters is {@code null}, or any elementary decision from the given list is {@code null}
     * @throws InvalidSizeException if the given list with elementary decisions is empty
     */
    public Rule(RuleType type, RuleSemantics semantics, RuleConditions ruleConditions, List<? extends Condition<? extends EvaluationField>> decisions, ConditionConnectiveType decisionConnectiveType) {
    	this.type = notNull(type, "Rule's type is null.");
    	this.semantics = notNull(semantics, "Rule's semantics is null.");
    	
    	this.conditions = notNull(ruleConditions, "Rule conditions are null.").getConditions().toArray(new Condition<?>[ruleConditions.size()]);
    	
    	saveDecisions(decisions, decisionConnectiveType);
    }
    
    /**
     * Constructor that enables to build decision rule using (induced) rule conditions {@link RuleConditions} and (given) rule decisions {@link Rule.RuleDecisions}.
     * 
     * @param type type of constructed rule; see {@link RuleType}
     * @param semantics semantics of constructed rule; see {@link RuleSemantics}
     * @param ruleConditions rule conditions to be set on the LHS of this rule
     * @param ruleDecisions rule decisions to be set on the RHS of this rule
     * 
     * @throws NullPointerException if any of the parameters is {@code null}
     * @throws InvalidTypeException when {@code ruleDecisions} have too complex inner structure, that cannot be translated to the structure returned by {@link #getDecisions()}.
     */
    public Rule(RuleType type, RuleSemantics semantics, RuleConditions ruleConditions, RuleDecisions ruleDecisions) {
    	this.type = notNull(type, "Rule's type is null.");
    	this.semantics = notNull(semantics, "Rule's semantics is null.");
    	
    	this.conditions = notNull(ruleConditions, "Rule conditions are null.").getConditions().toArray(new Condition<?>[ruleConditions.size()]);
    	//this.ruleDecisions = notNull(ruleDecisions, "Rule decisions are null."); //TODO: uncomment once ruleDecisions field is used!
    	
    	//TODO: remove the following code once ruleDecisions field is used!
    	notNull(ruleDecisions, "Rule decisions are null.");
    	if (ruleDecisions instanceof ElementaryDecision) {
    		this.decisions = new Condition<?>[1][1];
    		this.decisions[0][0] = ((ElementaryDecision)ruleDecisions).getElementaryDecision();
    	} else if (ruleDecisions instanceof ANDConnectedRuleDecisions) {
    		List<RuleDecisions> andConnectedRuleDecisions = ((ANDConnectedRuleDecisions)ruleDecisions).getRuleDecisions();
    		
    		if (andConnectedRuleDecisions.get(0) instanceof ElementaryDecision) {
    			int andDecisionsCount = andConnectedRuleDecisions.size();
        		this.decisions = new Condition<?>[1][];
        		this.decisions[0] = new Condition<?>[andDecisionsCount];
        		
            	for (int j = 0; j < andDecisionsCount; j++) {
            		this.decisions[0][j] = notNull(((ElementaryDecision)andConnectedRuleDecisions.get(j)).getElementaryDecision(), "Decision is null.");
            	}
    		} else {
    			throw new InvalidTypeException("Unspupported type of elements on a list of AND-connected rule decisions. Only elementary decisions are handled.");
    		}
    	} else if (ruleDecisions instanceof ORConnectedRuleDecisions) {
    		List<RuleDecisions> orConnectedRuleDecisions = ((ORConnectedRuleDecisions)ruleDecisions).getRuleDecisions();
    		int orDecisionsCount = orConnectedRuleDecisions.size();
    		
    		if (orConnectedRuleDecisions.get(0) instanceof ElementaryDecision) {
        		this.decisions = new Condition<?>[orDecisionsCount][];
        		for (int i = 0; i < orDecisionsCount; i++) {
        			this.decisions[i] = new Condition<?>[1];
        			this.decisions[i][0] = notNull(((ElementaryDecision)orConnectedRuleDecisions.get(i)).getElementaryDecision(), "Decision is null.");
        		}
    		} else if (orConnectedRuleDecisions.get(0) instanceof ANDConnectedRuleDecisions) {
    			if (((ANDConnectedRuleDecisions)orConnectedRuleDecisions.get(0)).getRuleDecisions().get(0) instanceof ElementaryDecision) {
	    			this.decisions = new Condition<?>[orDecisionsCount][];
	    			for (int i = 0; i < orDecisionsCount; i++) {
	    				List<RuleDecisions> andConnectedRuleDecisions = ((ANDConnectedRuleDecisions)orConnectedRuleDecisions.get(i)).getRuleDecisions();
	    				int andDecisionsCount = andConnectedRuleDecisions.size();
	    				this.decisions[i] = new Condition<?>[andDecisionsCount];
	    				for (int j = 0; j < andDecisionsCount; j++) {
	                		this.decisions[i][j] = notNull(((ElementaryDecision)andConnectedRuleDecisions.get(j)).getElementaryDecision(), "Decision is null.");
	                	}
	    			}
    			} else {
    				throw new InvalidTypeException("Unspupported type of elements on a list of AND-connected rule decisions. Only elementary decisions are handled.");
    			}
    		} else {
    			throw new InvalidTypeException("Unspupported type of elements on a list of OR-connected rule decisions. Only elementary decisions and AND-connected rule decisions are handled.");
    		}
    	}
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
	 * Gets an array with conditions of this rule (in order in which they were added to this rule).<br>
	 * <br>
	 * This rule stores conditions, due to efficiency, in an array (constructed in class constructor) this way:<br>
	 * {@code Condition<? extends EvaluationField>[] conditions = new Condition<?>[numberOfConditions]}.<br>
	 * Because {@link Condition} is a parameterized generic type with the type parameter upper-bounded to {@link EvaluationField},
	 * we can safely consider each condition being of type {@code Condition<EvaluationField>}.
	 * Therefore, to avoid wildcard in the return type, we cast the returned array of conditions to
	 * {@code Condition<EvaluationField>[]}.
	 * 
	 * @return the conditions array with conditions of this rule (in order in which they were added to this rule).
	 */
	public Condition<EvaluationField>[] getConditions() {
		return this.getConditions(false);
	}
	
	/**
	 * Gets array with conditions of this rule (in order in which they were added to this rule).<br>
	 * <br>
	 * This rule stores conditions, due to efficiency, in an array (constructed in class constructor) this way:<br>
	 * {@code Condition<? extends EvaluationField>[] conditions = new Condition<?>[numberOfConditions]}.<br>
	 * Because {@link Condition} is a parameterized generic type with the type parameter upper-bounded to {@link EvaluationField},
	 * we can safely consider each condition being of type {@code Condition<EvaluationField>}.
	 * Therefore, to avoid wildcard in the return type, we cast the returned array of conditions to
	 * {@code Condition<EvaluationField>[]}.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return the conditions array with conditions of this rule (in order in which they were added to this rule).
	 */
	@SuppressWarnings("unchecked")
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Condition<EvaluationField>[] getConditions(boolean accelerateByReadOnlyResult) {
		return (Condition<EvaluationField>[]) (accelerateByReadOnlyResult ? conditions : conditions.clone()); //this cast is safe
	}	

	/**
	 * Gets array with decisions of this rule.
	 * Conditions in each row (for fixed first array index) are considered to be connected with AND connective.
     * Rows are considered to be connected using OR connective.
	 * 
	 * @return array with decisions of this rule
	 */
	public Condition<EvaluationField>[][] getDecisions() {
		return this.getDecisions(false);
	}
	
	/**
	 * Gets array with decisions of this rule.
	 * Conditions in each row (for fixed first array index) are considered to be connected with AND connective.
     * Rows are considered to be connected using OR connective.<br>
	 * <br>
	 * This method can be used in certain circumstances to accelerate calculations.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array with decisions of this rule
	 */
	@SuppressWarnings("unchecked")
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Condition<EvaluationField>[][] getDecisions(boolean accelerateByReadOnlyResult) {
		if (accelerateByReadOnlyResult) {
			return (Condition<EvaluationField>[][])this.decisions; //this cast is safe
		} else {
			Condition<?>[][] returnedDecisions = this.decisions.clone();
			for (int i = 0; i < this.decisions.length; i++) {
				returnedDecisions[i] = this.decisions[i].clone();
			}
			return (Condition<EvaluationField>[][])returnedDecisions; //this cast is safe
		}
	}
	
	/**
	 * Gets the first (and possibly the only) decision suggested by this rule.
	 * 
	 * @return the first (and possibly the only) decision suggested by this rule
	 */
	@SuppressWarnings("unchecked")
	public Condition<EvaluationField> getDecision() {
		return (Condition<EvaluationField>)this.decisions[0][0]; //this cast is safe
	}
	
	/**
	 * Gets text representation of this rule.
	 * 
	 * @return text representation of this rule
	 */
	public String toString() {
		return toString(false);
	}
	
	/**
	 * Gets plain text representation of this rule, taking into account all of its conditions and decisions.<br>
	 * <br>
	 * This text representation is used in {@link org.rulelearn.rules.RuleSet#serialize(String)} method, which, in turn, is used in
	 * {@link org.rulelearn.rules.RuleSet#getHash()} method to produce hash of a rule set consistent among multiple program runs.<br>
	 * <br>
	 * Proceed with caution! Changing plain text representation returned by this method may invalidate already calculated hashes, being stored in unit tests and RuleML files.
	 * 
	 * @return plain text representation of this rule, taking into account all of its conditions and decisions
	 * 
	 * @see org.rulelearn.rules.RuleSet#serialize(String)
	 * @see org.rulelearn.rules.RuleSet#getHash()
	 * @see #toString()
	 */
	public String serialize() {
		return toString();
	}
	
	/**
	 * Gets text representation of this rule.
	 * 
	 * @param sortConditions tells if conditions should be sorted according to attribute order,
	 *        or leaved in their "natural" order (i.e., in the order in which they have been added to this rule)
	 * @return text representation of this rule
	 */
	public String toString(boolean sortConditions) {
		StringBuilder sB = new StringBuilder();
		if (sortConditions) {
			sB = appendRuleStarter(sB);
			sB = appendSortedConditions(sB);
			sB = appendDecisions(sB);
		} else {
			sB = appendRuleStarter(sB);
			sB = appendConditions(sB);
			sB = appendDecisions(sB);
		}
		return sB.toString();
	}	
	
	/**
	 * Appends condition part to given string builder. Particular conditions are appended in the same order in which they have been added to this rule.
	 * 
	 * @param sB string builder used to build this rule
	 * @return string builder with appended condition part
	 */
	StringBuilder appendConditions(StringBuilder sB) {
		//append conditions
		for (int i = 0; i < this.conditions.length; i++) {
			sB.append("(").append(this.conditions[i].toString()).append(")");

			if (i < this.conditions.length - 1) { //not last condition
				sB.append(" ").append(andConnective).append(" ");
			}
		}
		
		return sB;
	}
	
	/**
	 * Appends condition part to given string builder, composed of conditions sorted according to ascending attribute order.
	 * 
	 * @param sB string builder used to build this rule
	 * @return string builder with appended condition part
	 */
	StringBuilder appendSortedConditions(StringBuilder sB) {
		//append conditions
		Condition<? extends EvaluationField>[] clonedConditions = this.conditions.clone();
		
		java.util.Arrays.sort(clonedConditions, (condition1, condition2) -> {
			int i = condition1.getAttributeWithContext().getAttributeIndex();
			int j = condition2.getAttributeWithContext().getAttributeIndex();
			if (i < j) {
				return -1;
			} else {
				return (i == j) ? 0 : 1;
			}
		}); //sort conditions in ascending attribute order
		
		for (int i = 0; i < clonedConditions.length; i++) {
			sB.append("(").append(clonedConditions[i].toString()).append(")");

			if (i < clonedConditions.length - 1) { //not last condition
				sB.append(" ").append(andConnective).append(" ");
			}
		}
		
		return sB;
	}
	
	/**
	 * Appends rule starter {@link #ruleStarter} to given string builder.
	 * 
	 * @param sB string builder used to build this rule
	 * @return string builder with appended rule starter
	 */
	StringBuilder appendRuleStarter(StringBuilder sB) {
		//start rule
		if (ruleStarter != null && !ruleStarter.equals("")) {
			sB.append(ruleStarter).append(" ");
		}
		
		return sB;
	}
	
	/**
	 * Appends {@link #conditionsAndDecisionsDelimiter} and decision part to given string builder.
	 * 
	 * @param sB string builder used to build this rule
	 * @return string builder with appended decision part
	 */
	StringBuilder appendDecisions(StringBuilder sB) { //TODO: take into account more general configuration of decisions
		String ruleTypeIndicator;
		if (this.type == RuleType.CERTAIN) {
			ruleTypeIndicator = "";
			//ruleTypeIndicator = "[c]";
		} else if (this.type == RuleType.POSSIBLE) {
			ruleTypeIndicator = "[p]";
		} else {
			//ruleTypeIndicator = "[a]";
			ruleTypeIndicator = "";
		}
		
		//insert delimiter between conditions and decisions
		sB.append(" ").append(conditionsAndDecisionsDelimiter).append(ruleTypeIndicator).append(" ");
		
		//append decisions
		for (int i = 0; i < this.decisions.length; i++) {
			if (this.decisions.length > 1) { //more than one alternative
				sB.append("(");
			}
			
			for (int j = 0; j < this.decisions[i].length; j++) {
				sB.append("(").append(this.decisions[i][j]).append(")");
				
				if (j < this.decisions[i].length - 1) { // not last AND-connected decision
					sB.append(" ").append(andConnective).append(" ");
				}
			}
			
			if (this.decisions.length > 1) { //more than one alternative
				sB.append(")");
			}

			if (i < this.decisions.length - 1) { // not last OR-connected list of decisions
				sB.append(" ").append(orConnective).append(" ");
			}
		}
		
		return sB;
	}

	/**
	 * Verifies if this rule covers an object in an information table.
	 *  
	 * @param objectIndex index of an object in the given information table
	 * @param informationTable information table containing object with given index
	 * @return {@code true} if this rule covers considered object,
	 *         {@code false} otherwise
	 * 
	 * @throws IndexOutOfBoundsException see {@link Condition#satisfiedBy(int, InformationTable)}
	 * @throws NullPointerException see {@link Condition#satisfiedBy(int, InformationTable)}
	 */
	public boolean covers(int objectIndex, InformationTable informationTable) {
		for (int i = 0; i < this.conditions.length; i++) {
			if (!this.conditions[i].satisfiedBy(objectIndex, informationTable)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifies if decision part of this rule is satisfied by the object at given index in the given information table.
	 * In general, a rule can have several alternative compound decisions. Each of them can be composed of several elementary decisions.
	 * This method returns {@code true} if at least one compound decision is satisfied, which, in turn, means, that each elementary decision
	 * contributing to that compound decision, is satisfied. Suppose the following decision part of this rule:<br>
	 * ((dec &gt;= 5) &amp; (dec2 &lt;= 3)) OR ((dec &lt;= 3) &amp; (dec2 &gt;= 5)).<br>
	 * Then, this method returns {@code true} if considered object satisfies, e.g., both (dec &lt;= 3) and (dec2 &gt;= 5).
	 * 
	 * @param objectIndex index of an object in the given information table
	 * @param informationTable information table containing object with given index
	 * @return {@code true} if decision part of this rule is verified by the object at given index in the given information table,
	 *         {@code false} otherwise
	 * 
	 * @throws IndexOutOfBoundsException see {@link Condition#satisfiedBy(int, InformationTable)}
	 * @throws NullPointerException see {@link Condition#satisfiedBy(int, InformationTable)}
	 */
	public boolean decisionsMatchedBy(int objectIndex, InformationTable informationTable) { //TODO: take into account more general configuration of decisions
		boolean conjunctionSatisfied;
		
		//check if at least one alternative is satisfied by the considered object
		for (int i = 0; i < this.decisions.length; i++) {
			conjunctionSatisfied = true;
			for (int j = 0; j < this.decisions[i].length; j++) {
				if (!this.decisions[i][j].satisfiedBy(objectIndex, informationTable)) {
					conjunctionSatisfied = false;
					break; //go the the next alternative
				}
			}
			if (conjunctionSatisfied) {
				return true;
			}
		}
		return false; //no alternative is satisfied by the considered object
	}
	
	/**
	 * Verifies if decision part of this rule is satisfied by an object from information table having given evaluations on decision attributes
	 * considered in this rule.
	 * 
	 * @param evaluations list of {@link EvaluationField evaluations} of an object from information table on subsequent decision attributes 
	 * @return {@code true} if decision part of this rule is verified by the object having given evaluations on subsequent decision attributes,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given list or any of its elements is {@code null}
	 * @throws InvalidSizeException if number of supplied evaluations is smaller than number of AND-connected conditions in decision part of this rule
	 */
	public boolean decisionsMatchedBy(List<? extends EvaluationField> evaluations) { //TODO: take into account more general configuration of decisions
		notNull(evaluations, "Evaluations to be matched against rule's decisions are null.");
		if  (evaluations.size() != decisions[0].length) {
			throw new InvalidSizeException("Number of supplied evaluations is smaller than number of AND-connected rule's decisions.");
		}
		
		boolean conjunctionSatisfied;
		
		Condition<EvaluationField>[][] decisions = getDecisions(true);
		
		//check if at least one alternative is satisfied by the considered object
		for (int i = 0; i < decisions.length; i++) {
			conjunctionSatisfied = true;
			for (int j = 0; j < decisions[i].length; j++) {
				if (!decisions[i][j].satisfiedBy(evaluations.get(j))) {
					conjunctionSatisfied = false;
					break; //go the the next alternative
				}
			}
			if (conjunctionSatisfied) {
				return true;
			}
		}
		return false; //no alternative is satisfied by the considered object
	}
	
	/**
	 * Verifies if decision part of this rule is satisfied by an object from information table having given decision.
	 * 
	 * @param decision decision of an object from an information table 
	 * @return {@code true} if decision part of this rule is verified by the object having given decision,
	 *         {@code false} otherwise
	 * 
	 * @throws NullPointerException if given decision is {@code null}
	 * @throws NullPointerException if given decision does not have an evaluation for some decision attribute considered in this rule
	 */
	public boolean decisionsMatchedBy(Decision decision) { //TODO: take into account more general configuration of decisions
		notNull(decision, "Decision to be matched against rule's decisions is null.");
		
		boolean conjunctionSatisfied;
		
		Condition<EvaluationField>[][] decisions = getDecisions(true);
		EvaluationField evaluation;
		int decisionAttributeIndex;
		
		//check if at least one alternative is satisfied by the considered object
		for (int i = 0; i < decisions.length; i++) {
			conjunctionSatisfied = true;
			for (int j = 0; j < decisions[i].length; j++) {
				decisionAttributeIndex = decisions[i][j].getAttributeWithContext().getAttributeIndex();
				evaluation = decision.getEvaluation(decisionAttributeIndex); //no evaluation for current decision attribute
				if (evaluation != null) {
					if (!decisions[i][j].satisfiedBy(evaluation)) {
						conjunctionSatisfied = false;
						break; //go the the next alternative
					}
				} else {
					throw new NullPointerException("Object's decision does not have an evaluation for decision attribute no. " + decisionAttributeIndex + " considered in decision rule.");
				}
			}
			if (conjunctionSatisfied) {
				return true;
			}
		}
		return false; //no alternative is satisfied by the considered object
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
