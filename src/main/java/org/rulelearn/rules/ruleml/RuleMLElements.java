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

import org.rulelearn.rules.RuleCharacteristics;
import org.rulelearn.rules.RuleType;
import org.rulelearn.rules.UnknownRuleSemanticsException;

/**
 * Definitions of tags and keywords which are used to construct RuleML documents according to: <a href="http://wiki.ruleml.org/index.php/Specification_of_Deliberation_RuleML_1.01">Deliberation RuleML specification</a>.
 * 
 * Note that definitions proposed here extend standard specification to allow incorporate into a RuleML document information specific for decision rules (e.g. {@link RuleCharacteristics}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class RuleMLElements {
	
	/**
     * Declarations of keywords used in RuleML documents.
     * 
     * Note that these definitions extend standard definition to allow incorporate into a RuleML document information specific for decision rules (e.g. {@link RuleCharacteristics}.
     */
    static final String ASSERT = "assert";
    static final String IMPLIES = "implies";
    static final String IF = "if";
    static final String THEN = "then";
    static final String AND = "and";
    static final String OR = "or";
    static final String ATOM = "atom";
    static final String OPERATION = "op";
    static final String RELATION = "rel";
    static final String AT_LEAST = "ge";
    static final String GREATER = "g";
    static final String AT_MOST = "le";
    static final String LESS = "l";
    static final String EQUALS = "eq";
    static final String NOT_EQUALS = "neq";
    static final String CONSTANT = "ind";
    static final String VARIABLE = "var";	
    static final String EVALUATIONS = "evaluations";
    static final String EVALUATION = "evaluation";
    static final String POSITIVE_COVERED = "positiveCovered";
    static final String NEGATIVE_COVERED = "negativeCovered";
    static final String CASE = "case";
    static final String RULE_SEMANTICS = "ruleSemantics";
    static final String RULE_TYPE = "ruleType";
    static final String CERTAIN = "certain";
    static final String POSSIBLE = "possible";
    static final String APPROXIMATE = "approximate";
    static final String TYPE = "type";
    static final String OBJECT_VS_THRESHOLD = "object-threshold";
    static final String THRESHOLD_VS_OBJECT = "threshold-object";
    
    /** 
     * Declarations of character sequences used for white spaces.
     */
    static final String NEW_LINE = System.lineSeparator();
    static final String UNIX_NEW_LINE = "\n";
    static final String TAB = "\t";
    
    /**
     * Constructs RuleML header tag, which should be placed at the beginning of any RuleML document.
     * @return RuleML header tag
     */
    public static String getHeader () {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?xml-model href=\"http://deliberation.ruleml.org/1.01/relaxng/datalogplus_min_relaxed.rnc\"?>")
    			.append(getNewLine()).append("<RuleML xmlns=\"http://ruleml.org/spec\">").append(getNewLine());
    		return buffer.toString();
    }
    
    /**
     * Constructs RuleML footer tag, which should be placed at the beginning of any RuleML document.
     * @return RuleML footer tag
     */
    public static String getFooter () {
    		return "</RuleML>"+getNewLine();
    }
    
    /**
     * Constructs RuleML beginning of a rule set tag, which should be placed at the beginning of each set of rules.
     * 
     * @param ruleSetIndex index of the set of rules
     * @param learningInformationTableHash hash of the learning information table (if such hash should be written to file), or {@code null}
     *        (if such hash has not been calculated and stored in rule set)
     * 
     * @return RuleML beginning of a rule set tag, containing also given hash of learning information table (if it is not {@code null})
     */
    public static String getBeginningOfRuleSet (int ruleSetIndex, String learningInformationTableHash) {
    		StringBuilder builder = new StringBuilder();
    		builder.append("<act index=\"").append(ruleSetIndex);
    		
    		if (learningInformationTableHash != null) {
    			builder.append("\" learningDataHash=\"");
    			builder.append(learningInformationTableHash);
    		}
    		
    		return builder.append("\">").append(getNewLine()).toString();
    }
    
    /**
     * Constructs RuleML beginning of a rule set tag, which should be placed at the beginning of each set of rules.
     * 
     * @param ruleSetIndex index of the set of rules
     * @param learningInformationTableHash hash of the learning information table (if such hash should be written to file), or {@code null}
     *        (if such hash has not been calculated and stored in rule set)
     * 
     * @return RuleML beginning of a rule set tag, containing also given hash of learning information table (if it is not {@code null}) 
     */
    public static String getBeginningOfRuleSet (UUID ruleSetIndex, String learningInformationTableHash) {
    		StringBuilder builder = new StringBuilder();
    		builder.append("<act index=\"").append(ruleSetIndex.toString());
    		
    		if (learningInformationTableHash != null) {
    			builder.append("\" learningDataHash=\"");
    			builder.append(learningInformationTableHash);
    		}
    		
    		return builder.append("\">").append(getNewLine()).toString();
    }
    
    /**
     * Constructs RuleML end of a rule set tag, which should be placed at the end of each set of rules.
     * 
     * @return RuleML end of a rule set tag
     */
    public static String getEndOfRuleSet () {
    		return "</act>"+getNewLine();
    }
    
    /**
     * Constructs beginning of a specified RuleML tag followed by indication of a new line.
     * @param ruleMLTag RuleML tag to be started
     * 
     * @return beginning of specified RuleML tag
     */
    public static String getBeginningTag (String ruleMLTag) {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<").append(ruleMLTag).append(">").append(getNewLine());
    		return buffer.toString();
    }
    
    /**
     * Constructs beginning of a specified RuleML tag followed by indication of a new line.
     * @param ruleMLTag RuleML tag to be started
     * @param parameter name of parameter to be added to the tag
     * @param parameterValue value of the parameter
     * 
     * @return beginning of specified RuleML tag
     */
    public static String getBeginningTagWithParameter (String ruleMLTag, String parameter, String parameterValue) {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<").append(ruleMLTag).append(" ").append(parameter).append("=\"").append(parameterValue).append("\">").append(getNewLine());
    		return buffer.toString();
    }
    
    /**
     * Constructs beginning of a specified RuleML tag in an in-line style (i.e., the whole tag is represented in one line of the RuleML document). 
     * @param ruleMLTag RuleML tag to be started
     * 
     * @return beginning of specified RuleML tag
     */
    public static String getBeginnigInlineTag (String ruleMLTag) {
    		StringBuffer buffer = new StringBuffer();
		buffer.append("<").append(ruleMLTag).append(">");
		return buffer.toString();
    }
    
    /**
     * Constructs beginning of a specified RuleML tag in an in-line style (i.e., the whole tag is represented in one line of the RuleML document). 
     * @param ruleMLTag RuleML tag to be started
     * @param parameter name of parameter to be added to the tag
     * @param parameterValue value of the parameter
     * 
     * @return beginning of specified RuleML tag
     */
    public static String getBeginnigInlineTagWithParameter (String ruleMLTag, String parameter, String parameterValue) {
    		StringBuffer buffer = new StringBuffer();
		buffer.append("<").append(ruleMLTag).append(" ").append(parameter).append("=\"").append(parameterValue).append("\">");
		return buffer.toString();
    }
    
    /**
     * Constructs a multiplication (i.e., a set of copies) of a specified RuleML tag.  
     * @param ruleMLTag RuleML tag to be multiplied
     * @param times number of times the specified RuleML tag has to be multiplied
     * 
     * @return multiplication of specified RuleML tag
     */
    public static String getTagMultipied (String ruleMLTag, int times) {
	    	StringBuffer buffer = new StringBuffer();
	    	for (int i = 0; i < times; i++)
	    		buffer.append(ruleMLTag);
	    	return buffer.toString();
    }
    
    /**
     * Constructs end of a specified RuleML tag followed by indication of a new line.
     * @param ruleMLTag RuleML tag to be ended
     * 
     * @return end of specified RuleML tag
     */
    public static String getEndTag (String ruleMLTag) {
    		StringBuffer buffer = new StringBuffer();
		buffer.append("</").append(ruleMLTag).append(">").append(getNewLine());
		return buffer.toString();
    }
    
    /**
     * Constructs an evaluation RuleML tag (i.e., tag representing a value of a measure describing a rule) followed by indication of a new line.
     * 
     * Note that this tag is not defined in RuleML standard (i.e., it is our extension of the RuleML standard).
     * 
     * @param measure measure to be represented as a RuleML tag
     * @param value value of the measure
     * 
     * @return RuleML evaluation tag
     */
    public static String getEvaluationTag (String measure, int value) {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<").append(getEvaluationKeyword()).append(" measure=\"").append(measure).append("\" value=\"").append(value).append("\"/>").append(getNewLine());
    		return buffer.toString();
    }
    
    /**
     * Constructs an evaluation RuleML tag (i.e., tag representing a value of a measure describing a rule) followed by indication of a new line.
     * 
     * Note that this tag is not defined in RuleML standard (i.e., it is our extension of the RuleML standard).
     * 
     * @param measure measure to be represented as a RuleML tag
     * @param value value of the measure
     * 
     * @return RuleML evaluation tag
     */
    public static String getEvaluationTag (String measure, double value) {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<").append(getEvaluationKeyword()).append(" measure=\"").append(measure).append("\" value=\"").append(value).append("\"/>").append(getNewLine());
    		return buffer.toString();
    }
    
    /**
     * Constructs an evaluation RuleML tag (i.e., tag representing a value of a measure describing a rule) followed by indication of a new line.
     * 
     * Note that this tag is not defined in RuleML standard (i.e., it is our extension of the RuleML standard).
     * 
     * @param measure measure to be represented as a RuleML tag
     * @param value value of the measure
     * 
     * @return RuleML evaluation tag
     */
    public static String getEvaluationTag (String measure, String value) {
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append("<").append(getEvaluationKeyword()).append(" measure=\"").append(measure).append("\" value=\"").append(value).append("\"/>").append(getNewLine());
	    	return buffer.toString();
    }
    
    /**
     * Constructs a case RuleML tag (i.e., tag representing a case, an example, which is covered by a rule).
     * 
     * Note that this tag is not defined in RuleML standard (i.e., it is our extension of the RuleML standard).
     * 
     * @param id identifier of the case 
     * 
     * @return RuleML evaluation tag
     */
    public static String getCaseTag (int id) {
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append(getBeginnigInlineTag(CASE)).append(id).append(getEndTag(CASE));
	    	return buffer.toString();
    }
    
    /**
     * Constructs a case RuleML tag (i.e., tag representing a case, an example, which is covered by a rule).
     * 
     * Note that this tag is not defined in RuleML standard (i.e., it is our extension of the RuleML standard).
     * 
     * @param id identifier of the case 
     * 
     * @return RuleML evaluation tag
     */
    public static String getCaseTag (UUID id) {
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append(getBeginnigInlineTag(CASE)).append(id.toString()).append(getEndTag(CASE));
	    	return buffer.toString();
    }

	/**
	 * Gets assert keyword.
	 *
	 * @return the assert keyword
	 */
	public static String getAssertKeyword() {
		return ASSERT;
	}

	/**
	 * Gets implies keyword.
	 *
	 * @return the implies keyword
	 */
	public static String getImpliesKeyword() {
		return IMPLIES;
	}

	/**
	 * Gets if keyword.
	 *
	 * @return the if keyword
	 */
	public static String getIfKeyword() {
		return IF;
	}

	/**
	 * Gets and keyword.
	 *
	 * @return the and keyword
	 */
	public static String getAndKeyword() {
		return AND;
	}
	
	/**
	 * Gets or keyword.
	 *
	 * @return the or keyword
	 */
	public static String getOrKeyword() {
		return OR;
	}

	/**
	 * Gets atom keyword.
	 *
	 * @return the atom keyword
	 */
	public static String getAtomKeyword() {
		return ATOM;
	}

	/**
	 * Gets operation keyword.
	 *
	 * @return the operation keyword
	 */
	public static String getOperationKeyword() {
		return OPERATION;
	}

	/**
	 * Gets relation keyword.
	 *
	 * @return the relation keyword
	 */
	public static String getRelationKeyword() {
		return RELATION;
	}

	/**
	 * Gets constant keyword.
	 *
	 * @return the constant keyword
	 */
	public static String getConstantKeyword() {
		return CONSTANT;
	}

	/**
	 * Gets variable keyword.
	 *
	 * @return the variable keyword
	 */
	public static String getVariableKeyword() {
		return VARIABLE;
	}

	/**
	 * Gets then keyword.
	 *
	 * @return the then keyword
	 */
	public static String getThenKeyword() {
		return THEN;
	}

	/**
	 * Gets evaluation keyword.
	 *
	 * @return the evaluation keyword
	 */
	public static String getEvaluationKeyword() {
		return EVALUATION;
	}

	/**
	 * Gets evaluations keyword.
	 *
	 * @return the evaluations keyword
	 */
	public static String getEvaluationsKeyword() {
		return EVALUATIONS;
	}

	/**
	 * Gets positive covered keyword.
	 *
	 * @return the positive covered keyword.
	 */
	public static String getPositiveCoveredKeyword() {
		return POSITIVE_COVERED;
	}

	/**
	 * Get negative covered keyword.
	 *
	 * @return the negative covered keyword
	 */
	public static String getNegativeCoveredKeyword() {
		return NEGATIVE_COVERED;
	}

	/**
	 * Gets case keyword.
	 *
	 * @return the case keyword
	 */
	public static String getCaseKeyword() {
		return CASE;
	}

	/**
	 * Gets at least keyword.
	 *
	 * @return the at least keyword
	 */
	public static String getAtLeastKeyword() {
		return AT_LEAST;
	}

	/**
	 * Gets greater keyword.
	 *
	 * @return the greater keyword
	 */
	public static String getGreaterKeyword() {
		return GREATER;
	}

	/**
	 * Gets at most keyword.
	 *
	 * @return the at most keyword
	 */
	public static String getAtMostKeyword() {
		return AT_MOST;
	}

	/**
	 * Gets less keyword.
	 *
	 * @return the less keyword
	 */
	public static String getLessKeyword() {
		return LESS;
	}

	/**
	 * Gets equals keyword.
	 *
	 * @return the equals keyword
	 */
	public static String getEqualsKeyword() {
		return EQUALS;
	}

	/**
	 * Gets not equals keyword.
	 *
	 * @return the not equals keyword
	 */
	public static String getNotEqualsKeyword() {
		return NOT_EQUALS;
	}
	
	/**
	 * Gets rule semantics keyword.
	 *
	 * @return the rule semantics keyword
	 */
	public static String getRuleSemanticsKeyword() {
		return RULE_SEMANTICS;
	}
	
	/**
	 * Gets rule type keyword.
	 *
	 * @return the rule type keyword
	 */
	public static String getRuleTypeKeyword() {
		return RULE_TYPE;
	}
	
	/**
	 * Gets keyword for given rule type.
	 *
	 * @param ruleType type of a rule
	 * @return keyword representing rule type value 
	 * @throws UnknownRuleSemanticsException when rule type is unknown
	 */
	public static String getKeywordForRuleType (RuleType ruleType) {
		if (ruleType == RuleType.CERTAIN) {
     		return RuleMLElements.getCertainKeyword();
	     }
	     else if (ruleType == RuleType.POSSIBLE) {
	     	return RuleMLElements.getPossibleKeyword();
	     }
	     else if (ruleType == RuleType.APPROXIMATE) {
	     	return RuleMLElements.getApproximateKeyword();
	     }
	     else {
	     		throw new UnknownRuleSemanticsException("Rule type " + ruleType + " is unknown.");
	     }
	}
	
	/**
	 * Gets type keyword.
	 *
	 * @return the type keyword
	 */
	public static String getTypeKeyword() {
		return TYPE;
	}
	
	/**
	 * Gets certain keyword.
	 *
	 * @return the certain keyword
	 */
	public static String getCertainKeyword() {
		return CERTAIN;
	}
	
	/**
	 * Gets possible keyword.
	 *
	 * @return the possible keyword
	 */
	public static String getPossibleKeyword() {
		return POSSIBLE;
	}
	
	/**
	 * Gets approximate keyword.
	 *
	 * @return the approximate keyword
	 */
	public static String getApproximateKeyword() {
		return APPROXIMATE;
	}
	
	/**
	 * Gets objects vs threshold keyword.
	 *
	 * @return the object versus threshold keyword
	 */
	public static String getObjectVSThresholdKeyword() {
		return OBJECT_VS_THRESHOLD;
	}
	
	/**
	 * Gets threshold vs objects keyword.
	 *
	 * @return the threshold versus object keyword
	 */
	public static String getThresholdVSObjectKeyword() {
		return THRESHOLD_VS_OBJECT;
	}

	/**
	 * Gets new line keyword.
	 *
	 * @return the new line keyword
	 */
	public static String getNewLine() {
		return UNIX_NEW_LINE;
	}

	/**
	 * Gets tab keyword.
	 *
	 * @return the tab keyword
	 */
	public static String getTab() {
		return TAB;
	}   
    
}