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

package org.rulelearn.rules.ruleml;

import java.util.UUID;

import org.rulelearn.rules.Condition;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleCharacteristics;
import org.rulelearn.rules.RuleSemantics;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.UnknownRuleSemanticsException;
import org.rulelearn.types.EvaluationField;

/**
 * Builds a RuleML document concordant (partially) with: <a href="http://wiki.ruleml.org/index.php/Specification_of_Deliberation_RuleML_1.01">Deliberation RuleML specification</a>.
 *
 * Note that definitions proposed here extend standard specification to allow incorporate into a RuleML document information specific for decision rules (e.g. {@link RuleCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class RuleMLBuilder {
	
	/**
	 * Construct a RuleML document string representing all decision rules from the rule set passed as a parameter. The document is constructed by 
	 * joining string RuleML representation of each rule from the set preceded by RuleML header defined as {@link RuleMLElements#getHeader()}, and 
	 * followed by RuleML footer defined as {@link RuleMLElements#getFooter()}.
	 * 
	 * @param ruleSet a set of rules {@link RuleSet} to be represented as a RuleML document
	 * @param ruleSetIndex index of the set of rules to be specified in the document (i.e., identifier of a rule set represented as a RuleML document)
	 * @return RuleML document string representing all rules  
	 */
	public String toRuleMLString(RuleSet ruleSet, int ruleSetIndex) {
		StringBuilder result = new StringBuilder();
		result.append(RuleMLElements.getHeader());
		result.append(RuleMLElements.getBeginningOfRuleSet(ruleSetIndex));
		result.append(toRuleMLString(ruleSet));
		result.append(RuleMLElements.getEndOfRuleSet());
		result.append(RuleMLElements.getFooter());
		return result.toString();
	}
	
	/**
	 * Construct a RuleML document string representing all decision rules from the rule set passed as a parameter. The document is constructed by 
	 * joining string RuleML representation of each rule from the set preceded by RuleML header defined as {@link RuleMLElements#getHeader()}, and 
	 * followed by RuleML footer defined as {@link RuleMLElements#getFooter()}.
	 * 
	 * @param ruleSet a set of rules {@link RuleSet} to be represented as a RuleML document
	 * @param ruleSetIndex index of the set of rules to be specified in the document (i.e., identifier of a rule set represented as a RuleML document)
	 * @return RuleML document string representing all rules  
	 */
	public String toRuleMLString(RuleSet ruleSet, UUID ruleSetIndex) {
		StringBuilder result = new StringBuilder();
		result.append(RuleMLElements.getHeader());
		result.append(RuleMLElements.getBeginningOfRuleSet(ruleSetIndex));
		result.append(toRuleMLString(ruleSet));
		result.append(RuleMLElements.getEndOfRuleSet());
		result.append(RuleMLElements.getFooter());
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing all decision rules from the rule set passed as a parameter. The RuleML string is constructed by 
	 * joining string RuleML representation of each rule from the set.
	 *  
	 * @param ruleSet a set of rules {@link RuleSet} to be represented as a RuleML document
	 * @return RuleML representation of rules
	 */
	String toRuleMLString(RuleSet ruleSet) {
		StringBuilder result = new StringBuilder();
		
		boolean withCharacteristics = false;
		if (ruleSet instanceof RuleSetWithCharacteristics) {
			withCharacteristics = true;
		}
		
		Rule rule = null;
		RuleCharacteristics ruleCharacteristics = null; 
		for (int i = 0; i < ruleSet.size(); i++) {
			rule = ruleSet.getRule(i);
			if (rule != null) {
				if (!withCharacteristics) {
					result.append(toRuleMLString(rule));
				}
				else {
					ruleCharacteristics = ((RuleSetWithCharacteristics)ruleSet).getRuleCharacteristics(i);
					result.append(toRuleMLString(rule, ruleCharacteristics));
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing a decision rule passed as a parameter. The RuleML string is constructed by 
	 * joining string RuleML representation of each elementary condition in the condition part of the rule, and decision part of the rule.
	 *  
	 * @param rule a rule {@link Rule} to be represented as a RuleML string
	 * @return RuleML representation of the rule
	 */
	public String toRuleMLString(Rule rule) {
		StringBuilder result = new StringBuilder();
		
		result.append(RuleMLElements.getBeginningTag(RuleMLElements.getAssertKeyword())).append(RuleMLElements.getTab()).append(
	    		RuleMLElements.getBeginningTag(RuleMLElements.getImpliesKeyword()));
		//conditions part
	    result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.getIfKeyword()));
	    Condition<? extends EvaluationField>[] conditions = rule.getConditions();
		if (conditions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAndKeyword()));
		}
		for (int i = 0; i < conditions.length; i++) {
			result.append(toRuleMLString(conditions[i], 4));
		}
		if (conditions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getEndTag(RuleMLElements.getAndKeyword()));
		}
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getEndTag(RuleMLElements.getIfKeyword()));
	
		// decision part
		// TODO multiple decisions (with different operator than and)
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.Then));
		Condition<? extends EvaluationField>[] decisions = rule.getDecisions();
		if (decisions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAndKeyword()));
		}
		for (int i = 0; i < decisions.length; i++) { 
			result.append(toRuleMLString(decisions[i], 4));
		}
		if (decisions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getEndTag(RuleMLElements.getAndKeyword()));
		}
	    	result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getEndTag(RuleMLElements.Then));
	    		
	    	// end rule
	    	result.append(RuleMLElements.getTab()).append(RuleMLElements.getEndTag(RuleMLElements.Implies)).append(RuleMLElements.getEndTag(RuleMLElements.Assert));
	    	
	    	return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing a decision rule passed as a parameter. The RuleML string is constructed by 
	 * joining string RuleML representation of each elementary condition in the condition part of the rule, and decision part of the rule. 
	 * Then a RuleML string representing characteristics of the rule (passed also as a parameter) is added.
	 *  
	 * @param rule a rule {@link Rule} to be represented as a RuleML string
	 * @param ruleCharacteristics rule characteristics {@link RuleCharacteristics} of the rule to be represented as a RuleML string and added 
	 * 		to RuleML representation of the rule 
	 * @return RuleML representation of the rule
	 */
	public String toRuleMLString(Rule rule, RuleCharacteristics ruleCharacteristics) {
		StringBuilder result = new StringBuilder();

		// rule beginning
		result.append(RuleMLElements.getBeginningTag(RuleMLElements.getAssertKeyword())).append(RuleMLElements.getTab()).append(
				RuleMLElements.getBeginningTag(RuleMLElements.getImpliesKeyword()));
	    
	    //conditions part
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.getIfKeyword()));
		Condition<? extends EvaluationField>[] conditions = rule.getConditions();
		if (conditions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAndKeyword()));
		}
		for (int i = 0; i < conditions.length; i++) {
			result.append(toRuleMLString(conditions[i], 4));
		}
		if (conditions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getEndTag(RuleMLElements.getAndKeyword()));
		}
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getEndTag(RuleMLElements.getIfKeyword()));
	
		// decision part
		// TODO multiple decisions (with different operator than and)
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.Then));
		Condition<? extends EvaluationField>[] decisions = rule.getDecisions();
		if (decisions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAndKeyword()));
		}
		for (int i = 0; i < decisions.length; i++) { 
			result.append(toRuleMLString(decisions[i], 4));
		}
		if (decisions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getEndTag(RuleMLElements.getAndKeyword()));
		}
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getEndTag(RuleMLElements.Then));
		
		// rule characteristics
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.getEvaluationsKeyword()));
		result.append(toRuleMLString(ruleCharacteristics, 3));
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getEndTag(RuleMLElements.getEvaluationsKeyword()));
		
		// end rule
		result.append(RuleMLElements.getTab()).append(RuleMLElements.getEndTag(RuleMLElements.Implies)).append(RuleMLElements.getEndTag(RuleMLElements.Assert));
	    	
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing an elementary condition passed as a parameter. The RuleML string is constructed by 
	 * joining string RuleML representation of relation, limiting value, and attribute name.
	 * 
	 * @param condition an elementary condition to be represented as a RuleML string
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the elementary condition
	 */
	String toRuleMLString(Condition<? extends EvaluationField> condition, int indenture) {
		StringBuilder result = new StringBuilder(); 
		
		// beginning of elementary condition
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAtomKeyword()));
        RuleSemantics semantics = condition.getRuleSemantics();
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getBeginningTag(RuleMLElements.getOperationKeyword()));
        
        // relation
        if (semantics == RuleSemantics.AT_LEAST) {
        		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture+1)).append(
        			RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRelationKeyword())).append(RuleMLElements.getAtLeastKeyword()).append(
        					RuleMLElements.getEndTag(RuleMLElements.getRelationKeyword()));
        }
        else if (semantics == RuleSemantics.AT_MOST) {
        		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture+1)).append(
    				RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRelationKeyword())).append(RuleMLElements.getAtMostKeyword()).append(
    						RuleMLElements.getEndTag(RuleMLElements.getRelationKeyword()));
        }
        else if (semantics == RuleSemantics.EQUAL) {
        	result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture+1)).append(
    				RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRelationKeyword())).append(RuleMLElements.getEqualsKeyword()).append(
    						RuleMLElements.getEndTag(RuleMLElements.getRelationKeyword()));
        }
        else {
        		throw new UnknownRuleSemanticsException("Semantics of the processed condition " + condition + " is unknown.");
        }
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getEndTag(RuleMLElements.getOperationKeyword()));
        
        // evaluation
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
        		RuleMLElements.getBeginnigInlineTag(RuleMLElements.getConstantKeyword())).append(condition.getLimitingEvaluation().toString()).append(
        				RuleMLElements.getEndTag(RuleMLElements.getConstantKeyword()));
        // attribute name
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture+1)).append(
        		RuleMLElements.getBeginnigInlineTag(RuleMLElements.getVariableKeyword())).append(
        				condition.getAttributeWithContext().getAttributeName()).append(RuleMLElements.getEndTag(RuleMLElements.getVariableKeyword()));
        
        // end elementary condition
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getEndTag(RuleMLElements.getAtomKeyword()));
	    
	    return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing characteristics of a rule passed as a parameter. The RuleML string is constructed by 
	 * joining string RuleML representation of all specified characteristics of a rule.
	 *  
	 * @param ruleCharacteristics characteristics of a rule
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule characteristics 
	 */
	String toRuleMLString(RuleCharacteristics ruleCharacteristics, int indenture) {
		final StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("Support", ruleCharacteristics.getSupport()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("Strength", ruleCharacteristics.getStrength()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("Confidence", ruleCharacteristics.getConfidence()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("CoverageFactor", String.valueOf(ruleCharacteristics.getCoverageFactor())));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("Coverage", ruleCharacteristics.getCoverage()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("NegativeCoverage", ruleCharacteristics.getNegativeCoverage()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("InconsistencyMeasure", ruleCharacteristics.getEpsilon()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("EpsilonPrimMeasure", ruleCharacteristics.getEpsilonPrim()));		
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("f-ConfirmationMeasure", ruleCharacteristics.getFConfirmation()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("A-ConfirmationMeasure", ruleCharacteristics.getAConfirmation()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("Z-ConfirmationMeasure", ruleCharacteristics.getZConfirmation()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("l-ConfirmationMeasure", ruleCharacteristics.getLConfirmation()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("c1-ConfirmationMeasure", ruleCharacteristics.getC1Confirmation()));
		stringBuilder.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(
				RuleMLElements.getEvaluationTag("s-ConfirmationMeasure", ruleCharacteristics.getSConfirmation()));
				
		return stringBuilder.toString();
	}
	
}
