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

import static org.rulelearn.core.Precondition.notNull;

import java.util.UUID;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import org.rulelearn.core.UnknownValueException;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtLeastObjectVSThreshold;
import org.rulelearn.rules.ConditionAtLeastThresholdVSObject;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.ConditionAtMostObjectVSThreshold;
import org.rulelearn.rules.ConditionAtMostThresholdVSObject;
import org.rulelearn.rules.ConditionEqual;
import org.rulelearn.rules.ConditionEqualObjectVSThreshold;
import org.rulelearn.rules.ConditionEqualThresholdVSObject;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleCharacteristics;
import org.rulelearn.rules.RuleSemantics;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.RuleType;
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
		notNull(ruleSet, "Rule set to be transfomed into a RuleML document string is null.");
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
		notNull(ruleSet, "Rule set to be transfomed into a RuleML document string is null.");
		notNull(ruleSetIndex, "Rule set UUID is null.");
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
		return toRuleMLString(rule, null);
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
		notNull(rule, "Rule to be transfomed into a RuleML string is null.");
		StringBuilder result = new StringBuilder();

		// rule beginning
		result.append(RuleMLElements.getBeginningTag(RuleMLElements.getAssertKeyword())).append(RuleMLElements.getTab()).append(
				RuleMLElements.getBeginningTagWithParameter(RuleMLElements.getImpliesKeyword(), RuleMLElements.getTypeKeyword(), RuleMLElements.getKeywordForRuleType(rule.getType())));
	    
	    //conditions part
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.getIfKeyword()));
		Condition<EvaluationField>[] conditions = rule.getConditions();
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
	
		// decision part only 'and' elements inside 'or' element
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.getThenKeyword()));
		Condition<? extends EvaluationField>[][] decisions = rule.getDecisions();
		if (decisions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getBeginningTag(RuleMLElements.getOrKeyword()));
		}
		for (int i = 0; i < decisions.length; i++) { 
			if (decisions[i].length > 1) {
				result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 4)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAndKeyword()));
			}
			for (int j = 0; j < decisions[i].length; j++) {
				result.append(toRuleMLString(decisions[i][j], 5));
			}
			if (decisions[i].length > 1) {
				result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 4)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAndKeyword()));
			}
		}
		if (decisions.length > 1) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 3)).append(RuleMLElements.getEndTag(RuleMLElements.getOrKeyword()));
		}
	    	result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getEndTag(RuleMLElements.getThenKeyword()));
	    	
	    	// rule type and rule semantics
	    	result.append(toRuleMLString(rule.getSemantics(), 2));
	    	//result.append(toRuleMLString(rule.getType(), 2));
		
		// rule characteristics
	    	if (ruleCharacteristics != null) {
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getBeginningTag(RuleMLElements.getEvaluationsKeyword()));
			result.append(toRuleMLString(ruleCharacteristics, 3));
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), 2)).append(RuleMLElements.getEndTag(RuleMLElements.getEvaluationsKeyword()));
	    	}
		
		// end rule
		result.append(RuleMLElements.getTab()).append(RuleMLElements.getEndTag(RuleMLElements.getImpliesKeyword())).append(RuleMLElements.getEndTag(RuleMLElements.getAssertKeyword()));
	    	
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing an elementary condition {@link Condition} passed as a parameter. The RuleML string is constructed by 
	 * joining string RuleML representation of relation, limiting value, and attribute name.
	 * 
	 * @param condition an elementary condition to be represented as a RuleML string
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the elementary condition
	 * @throws UnknownRuleSemanticsException when type of processed condition is unknown
	 */
	String toRuleMLString(Condition<? extends EvaluationField> condition, int indenture) {
		notNull(condition, "Condition to be transfomed into a RuleML string is null.");
		StringBuilder result = new StringBuilder(); 
		
		// beginning of elementary condition
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getBeginningTag(RuleMLElements.getAtomKeyword()));
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getBeginningTag(RuleMLElements.getOperationKeyword()));
        
        // relation
        result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture+1));
        if (condition instanceof ConditionAtLeast<?>) {
        		if (condition instanceof ConditionAtLeastObjectVSThreshold<?>) {
        			result.append(RuleMLElements.getBeginnigInlineTagWithParameter(RuleMLElements.getRelationKeyword(), RuleMLElements.getTypeKeyword(), RuleMLElements.getObjectVSThresholdKeyword()));
        		}
        		else if (condition instanceof ConditionAtLeastThresholdVSObject<?>) {
        			result.append(RuleMLElements.getBeginnigInlineTagWithParameter(RuleMLElements.getRelationKeyword(), RuleMLElements.getTypeKeyword(), RuleMLElements.getThresholdVSObjectKeyword()));
        		}
        		else {
        			result.append(RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRelationKeyword()));
        		}
        		result.append(RuleMLElements.getAtLeastKeyword());
        }
        else if (condition instanceof ConditionAtMost<?>) {
	        	if (condition instanceof ConditionAtMostObjectVSThreshold<?>) {
	        		result.append(RuleMLElements.getBeginnigInlineTagWithParameter(RuleMLElements.getRelationKeyword(), RuleMLElements.getTypeKeyword(), RuleMLElements.getObjectVSThresholdKeyword()));
	    		}
	    		else if (condition instanceof ConditionAtMostThresholdVSObject<?>) {
	    			result.append(RuleMLElements.getBeginnigInlineTagWithParameter(RuleMLElements.getRelationKeyword(), RuleMLElements.getTypeKeyword(), RuleMLElements.getThresholdVSObjectKeyword()));
	    		}
	    		else {
	    			result.append(RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRelationKeyword()));
	    		}
        		result.append(RuleMLElements.getAtMostKeyword());
        }
        else if (condition instanceof ConditionEqual<?>) {
	        	if (condition instanceof ConditionEqualObjectVSThreshold<?>) {
	        		result.append(RuleMLElements.getBeginnigInlineTagWithParameter(RuleMLElements.getRelationKeyword(), RuleMLElements.getTypeKeyword(), RuleMLElements.getObjectVSThresholdKeyword()));
	    		}
	    		else if (condition instanceof ConditionEqualThresholdVSObject<?>) {
	    			result.append(RuleMLElements.getBeginnigInlineTagWithParameter(RuleMLElements.getRelationKeyword(), RuleMLElements.getTypeKeyword(), RuleMLElements.getThresholdVSObjectKeyword()));
	    		}
	    		else {
	    			result.append(RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRelationKeyword()));
	    		}
        		result.append(RuleMLElements.getEqualsKeyword());
        }
        else {
        		throw new UnknownRuleSemanticsException("Type of the processed condition " + condition + " is unknown.");
        }
        result.append(RuleMLElements.getEndTag(RuleMLElements.getRelationKeyword()));
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
	 * Construct a RuleML string representing characteristics {@link RuleCharacteristics} of a rule passed as a parameter. The RuleML string is constructed by 
	 * joining string RuleML representation of all specified characteristics of a rule.
	 *  
	 * @param ruleCharacteristics characteristics of a rule
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule characteristics 
	 */
	String toRuleMLString(RuleCharacteristics ruleCharacteristics, int indenture) {
		notNull(ruleCharacteristics, "Rule characteristics to be transfomed into a RuleML string are null.");
		final StringBuilder result = new StringBuilder();

		if (ruleCharacteristics.isSupportSet()) {
			result.append(evaluationToRuleMLString("Support", ruleCharacteristics.getSupport(), indenture));
		}
		if (ruleCharacteristics.isStrengthSet()) {
			result.append(evaluationToRuleMLString("Strength", ruleCharacteristics.getStrength(), indenture));
		}
		if (ruleCharacteristics.isConfidenceSet()) {
			result.append(evaluationToRuleMLString("Confidence", ruleCharacteristics.getConfidence(), indenture));
		}
		if (ruleCharacteristics.isCoverageFactorSet()) {
			result.append(evaluationToRuleMLString("CoverageFactor", ruleCharacteristics.getCoverageFactor(), indenture));
		}
		if (ruleCharacteristics.isCoverageSet()) {
			result.append(evaluationToRuleMLString("Coverage", ruleCharacteristics.getCoverage(), indenture));
		}
		if (ruleCharacteristics.isNegativeCoverageSet()) {
			result.append(evaluationToRuleMLString("NegativeCoverage", ruleCharacteristics.getNegativeCoverage(), indenture));
		}
		if (ruleCharacteristics.isEpsilonSet()) {
			// it was InconsistencyMeasure in previous version
			result.append(evaluationToRuleMLString("EpsilonMeasure", ruleCharacteristics.getEpsilon(), indenture));
		}
		if (ruleCharacteristics.isEpsilonPrimeSet()) {
			// it was EpsilonPrimMeasure in previous version
			result.append(evaluationToRuleMLString("EpsilonPrimeMeasure", ruleCharacteristics.getEpsilonPrime(), indenture));
		}
		if (ruleCharacteristics.isFConfirmationSet()) {
			result.append(evaluationToRuleMLString("f-ConfirmationMeasure", ruleCharacteristics.getFConfirmation(), indenture));
		}
		if (ruleCharacteristics.isAConfirmationSet()) {
			result.append(evaluationToRuleMLString("A-ConfirmationMeasure", ruleCharacteristics.getAConfirmation(), indenture));
		}
		if (ruleCharacteristics.isZConfirmationSet()) {
			result.append(evaluationToRuleMLString("Z-ConfirmationMeasure", ruleCharacteristics.getZConfirmation(), indenture));
		}
		if (ruleCharacteristics.isLConfirmationSet()) {
			result.append(evaluationToRuleMLString("l-ConfirmationMeasure", ruleCharacteristics.getLConfirmation(), indenture));
		}
		if (ruleCharacteristics.isC1ConfirmationSet()) {
			result.append(evaluationToRuleMLString("c1-ConfirmationMeasure", ruleCharacteristics.getC1Confirmation(), indenture));
		}
		if (ruleCharacteristics.isSConfirmationSet()) {
			result.append(evaluationToRuleMLString("s-ConfirmationMeasure", ruleCharacteristics.getSConfirmation(), indenture));
		}
				
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing evaluation of a rule. 
	 *  
	 * @param name name of evaluation
	 * @param value value of evaluation 
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule evaluation 
	 */
	String evaluationToRuleMLString(String name, int value, int indenture) {
		final StringBuilder result = new StringBuilder();
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getEvaluationTag(name, value));
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing evaluation of a rule. 
	 *  
	 * @param name name of evaluation
	 * @param value value of evaluation 
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule evaluation 
	 */
	String evaluationToRuleMLString(String name, double value, int indenture) {
		final StringBuilder result = new StringBuilder();
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getEvaluationTag(name, value));
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing evaluation of a rule. 
	 *  
	 * @param name name of evaluation
	 * @param valueSupplier supplier of integer value {@link IntSupplier} of evaluation 
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule evaluation 
	 */
	String evaluationToRuleMLString(String name, IntSupplier valueSupplier, int indenture) {
		final StringBuilder result = new StringBuilder();
		try {
			int value = valueSupplier.getAsInt();
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getEvaluationTag(name, value));
			return result.toString();
		}
		catch (UnknownValueException e) {
			return "";
		}
	}
	
	/**
	 * Construct a RuleML string representing evaluation of a rule. 
	 *  
	 * @param name name of evaluation
	 * @param valueSupplier supplier of integer value {@link IntSupplier} of evaluation 
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule evaluation 
	 */
	String evaluationToRuleMLString(String name, DoubleSupplier valueSupplier, int indenture) {
		final StringBuilder result = new StringBuilder();
		try {
			double value = valueSupplier.getAsDouble();
			result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getEvaluationTag(name, value));
			return result.toString();
		}
		catch (UnknownValueException e) {
			return "";
		}
	}
	
	/**
	 * Construct a RuleML string representing semantics {@link RuleSemantics} of a rule passed as a parameter. 
	 *  
	 * @param ruleSemantics semantics of a rule
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule semantics
	 * @throws UnknownRuleSemanticsException when rule semantics is unknown
	 */
	String toRuleMLString(RuleSemantics ruleSemantics, int indenture) {
		notNull(ruleSemantics, "Rule semantics to be transfomed into a RuleML string are null.");
		final StringBuilder result = new StringBuilder();
		
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRuleSemanticsKeyword()));
        if (ruleSemantics == RuleSemantics.AT_LEAST) {
        		result.append(RuleMLElements.getAtLeastKeyword());
        }
        else if (ruleSemantics == RuleSemantics.AT_MOST) {
        		result.append(RuleMLElements.getAtMostKeyword());
        }
        else if (ruleSemantics == RuleSemantics.EQUAL) {
        		result.append(RuleMLElements.getEqualsKeyword());
        }
        else {
        		throw new UnknownRuleSemanticsException("Rule semantics " + ruleSemantics + " is unknown.");
        }
        result.append(RuleMLElements.getEndTag(RuleMLElements.getRuleSemanticsKeyword()));
					
		return result.toString();
	}
	
	/**
	 * Construct a RuleML string representing type {@link RuleType} of a rule passed as a parameter. 
	 *  
	 * @param ruleType type of a rule
	 * @param indenture how many tabs should be added to pretty print the document
	 * @return RuleML representation of the rule type 
	 * @throws UnknownRuleSemanticsException when rule type is unknown
	 */
	String toRuleMLString(RuleType ruleType, int indenture) {
		notNull(ruleType, "Rule type to be transfomed into a RuleML string are null.");
		final StringBuilder result = new StringBuilder();
		
		result.append(RuleMLElements.getTagMultipied(RuleMLElements.getTab(), indenture)).append(RuleMLElements.getBeginnigInlineTag(RuleMLElements.getRuleTypeKeyword()));
        if (ruleType == RuleType.CERTAIN) {
        		result.append(RuleMLElements.getCertainKeyword());
        }
        else if (ruleType == RuleType.POSSIBLE) {
        		result.append(RuleMLElements.getPossibleKeyword());
        }
        else if (ruleType == RuleType.APPROXIMATE) {
        		result.append(RuleMLElements.getApproximateKeyword());
        }
        else {
        		throw new UnknownRuleSemanticsException("Rule type " + ruleType + " is unknown.");
        }
        result.append(RuleMLElements.getEndTag(RuleMLElements.getRuleTypeKeyword()));
					
		return result.toString();
	}
}
