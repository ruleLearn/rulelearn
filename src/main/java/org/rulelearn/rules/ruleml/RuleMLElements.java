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
    static final String Assert = "assert";
    static final String Implies = "implies";
    static final String If = "if";
    static final String Then = "then";
    static final String And = "and";
    static final String Or = "or";
    static final String Atom = "atom";
    static final String Operation = "op";
    static final String Relation = "rel";
    static final String AtLeast = "ge";
    static final String Greater = "g";
    static final String AtMost = "le";
    static final String Less = "l";
    static final String Equals = "eq";
    static final String NotEquals = "neq";
    static final String Constant = "ind";
    static final String Variable = "var";	
    static final String Evaluations = "evaluations";
    static final String Evaluation = "evaluation";
    static final String PositiveCovered = "positiveCovered";
    static final String NegativeCovered = "negativeCovered";
    static final String Case = "case";
    static final String RuleSemantics = "ruleSemantics";
    static final String RuleType = "ruleType";
    static final String Certain = "certain";
    static final String Possible = "possible";
    static final String Approximate = "approximate";
    
    /** 
     * Declarations of character sequences used for white spaces.
     */
    static final String NewLine = System.lineSeparator();
    static final String UnixNewLine = "\n";
    static final String Tab = "\t";
    
    /**
     * Constructs RuleML header tag, which should be placed at the beginning of any RuleML document.
     * @return RuleML header tag
     */
    public static String getHeader () {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<?xml version=\"1.01\" encoding=\"UTF-8\"?>\n<?xml-model href=\"http://deliberation.ruleml.org/1.01/relaxng/datalogplus_min_relaxed.rnc\"?>")
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
     * @param ruleSetIndex index of the set of rules
     * 
     * @return RuleML beginning of a rule set tag
     */
    public static String getBeginningOfRuleSet (int ruleSetIndex) {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<act index=\"").append(ruleSetIndex).append("\">").append(getNewLine());
    		return buffer.toString();
    }
    
    /**
     * Constructs RuleML beginning of a rule set tag, which should be placed at the beginning of each set of rules.
     * @param ruleSetIndex index of the set of rules
     * 
     * @return RuleML beginning of a rule set tag
     */
    public static String getBeginningOfRuleSet (UUID ruleSetIndex) {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("<act index=\"").append(ruleSetIndex.toString()).append("\">").append(getNewLine());
    		return buffer.toString();
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
	    	buffer.append(getBeginnigInlineTag(Case)).append(id).append(getEndTag(Case));
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
	    	buffer.append(getBeginnigInlineTag(Case)).append(id.toString()).append(getEndTag(Case));
	    	return buffer.toString();
    }

	/**
	 * @return the assert keyword
	 */
	public static String getAssertKeyword() {
		return Assert;
	}

	/**
	 * @return the implies keyword
	 */
	public static String getImpliesKeyword() {
		return Implies;
	}

	/**
	 * @return the if
	 */
	public static String getIfKeyword() {
		return If;
	}

	/**
	 * @return the and keyword
	 */
	public static String getAndKeyword() {
		return And;
	}
	
	/**
	 * @return the or keyword
	 */
	public static String getOrKeyword() {
		return Or;
	}

	/**
	 * @return the atom keyword
	 */
	public static String getAtomKeyword() {
		return Atom;
	}

	/**
	 * @return the operation keyword
	 */
	public static String getOperationKeyword() {
		return Operation;
	}

	/**
	 * @return the relation keyword
	 */
	public static String getRelationKeyword() {
		return Relation;
	}

	/**
	 * @return the constant keyword
	 */
	public static String getConstantKeyword() {
		return Constant;
	}

	/**
	 * @return the variable keyword
	 */
	public static String getVariableKeyword() {
		return Variable;
	}

	/**
	 * @return the then keyword
	 */
	public static String getThenKeyword() {
		return Then;
	}

	/**
	 * @return the evaluation keyword
	 */
	public static String getEvaluationKeyword() {
		return Evaluation;
	}

	/**
	 * @return the evaluations keyword
	 */
	public static String getEvaluationsKeyword() {
		return Evaluations;
	}

	/**
	 * @return the positive covered keyword
	 */
	public static String getPositiveCoveredKeyword() {
		return PositiveCovered;
	}

	/**
	 * @return the negative covered keyword
	 */
	public static String getNegativeCoveredKeyword() {
		return NegativeCovered;
	}

	/**
	 * @return the case keyword
	 */
	public static String getCaseKeyword() {
		return Case;
	}

	/**
	 * @return the at least keyword
	 */
	public static String getAtLeastKeyword() {
		return AtLeast;
	}

	/**
	 * @return the greater keyword
	 */
	public static String getGreaterKeyword() {
		return Greater;
	}

	/**
	 * @return the at most keyword
	 */
	public static String getAtMostKeyword() {
		return AtMost;
	}

	/**
	 * @return the less keyword
	 */
	public static String getLessKeyword() {
		return Less;
	}

	/**
	 * @return the equals keyword
	 */
	public static String getEqualsKeyword() {
		return Equals;
	}

	/**
	 * @return the not equals keyword
	 */
	public static String getNotEqualsKeyword() {
		return NotEquals;
	}
	
	/**
	 * @return the rule semantics keyword
	 */
	public static String getRuleSemanticsKeyword() {
		return RuleSemantics;
	}
	
	/**
	 * @return the rule type keyword
	 */
	public static String getRuleTypeKeyword() {
		return RuleType;
	}
	
	/**
	 * @return the certain keyword
	 */
	public static String getCertainKeyword() {
		return Certain;
	}
	
	/**
	 * @return the possible keyword
	 */
	public static String getPossibleKeyword() {
		return Possible;
	}
	
	/**
	 * @return the approximate keyword
	 */
	public static String getApproximateKeyword() {
		return Approximate;
	}

	/**
	 * @return the new line keyword
	 */
	public static String getNewLine() {
		return UnixNewLine;
	}

	/**
	 * @return the tab keyword
	 */
	public static String getTab() {
		return Tab;
	}   
    
}