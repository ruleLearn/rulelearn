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

import static org.rulelearn.core.Precondition.nonEmpty;
import static org.rulelearn.core.Precondition.notNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeWithContext;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.EvaluationParser;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtLeastObjectVSThreshold;
import org.rulelearn.rules.ConditionAtLeastThresholdVSObject;
import org.rulelearn.rules.ConditionAtMostObjectVSThreshold;
import org.rulelearn.rules.ConditionAtMostThresholdVSObject;
import org.rulelearn.rules.ConditionEqualObjectVSThreshold;
import org.rulelearn.rules.ConditionEqualThresholdVSObject;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleCharacteristics;
import org.rulelearn.rules.RuleSemantics;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.RuleType;
import org.rulelearn.types.EvaluationField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

/**
 * Parser of decision rules stored in RuleML format. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleParser {
	
	/** 
	 * Default string representations of a missing value.
	 */
	protected final static String[] DEFAULT_MISSING_VALUE_STRINGS = {"?", "*"}; //SIC! null is not allowed on this list and the list itself cannot be null

	/** 
	 * Default value for this type of a field.
	 */
	public final static String DEFAULT_ENCODING = "UTF-8";
	
	/** 
	 * Default index which must be returned to denote that a collection does not contain specified element.
	 */
	protected final static Integer DEFAULT_INDEX = -1;
	
	/**
	 * Default type of rule.
	 */
	final static RuleType DEFAULT_RULE_TYPE = RuleType.CERTAIN;
	
	/**
	 * All attributes {@link Attribute} which may be present in elementary conditions, that are forming decision rules.
	 */
	protected Attribute [] attributes = null;
	
	/**
	 * Encoding of text data in RuleML.
	 */
	protected String encoding = RuleParser.DEFAULT_ENCODING;	
	
	/**
	 * Attributes with context {@link AttributeWithContext} which are used to construct conditions. 
	 */
	protected AttributeWithContext<? extends Attribute> [] attributesWithContext = null;
	
	/**
	 * Map used to access attributes by name.
	 */
	protected Object2IntMap<String> attributeNamesMap = null;
	
	/**
	 * Parser of evaluations given in textual form, converting them to {@link EvaluationField evaluation fields} based on information about an attribute.
	 * Initialized in class constructor with {@link EvaluationParser.CachingType#VOLATILE volatile caching type}.
	 */
	EvaluationParser evaluationParser = null;
	
	/**
	 * Parse log of this rule parser used for logging effects of calls to methods attempting to parse a rule,
	 * like {@link RuleParser#parseRule(Element)} or {@link RuleParser#parseRuleEvaluations(Element)}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class ParseLog {
		
		int readRulesCount;
		int successfullyParsedRulesCount; //a rule is considered to be successfully parsed if both the rule itself and its characteristics (if attempted) were parsed successfully
		ObjectList<String>failureMessages;
		
		private ParseLog() {
			readRulesCount = 0;
			successfullyParsedRulesCount = 0;
			failureMessages = new ObjectArrayList<String>();
		}
		
		void logSuccess() {
			readRulesCount++;
			successfullyParsedRulesCount++;
		}
		
		void logFailure(String failureMessage) {
			readRulesCount++;
			failureMessages.add(failureMessage);
		}
		
		void extendFailure(String failureMessage) { //used if rule was successfully parsed, but rule characteristics was not (only logs message)
			failureMessages.add(failureMessage);
		}
		
		/**
		 * Gets the number of rules read from an input, i.e.,
		 * the number of rules that were attempted to parse from textual
		 * representation into in-memory representation, strictly corresponding to
		 * considered set of attributes.
		 * 
		 * @return the number of rules read from an input (that were later either successfully or unsuccessfully parsed)
		 */
		public int getReadRulesCount() {
			return readRulesCount;
		}
		
		/**
		 * Gets the number of rules successfully parsed from an input, i.e.,
		 * the number of rules that were successfully parsed from textual
		 * representation into in-memory representation, strictly corresponding to
		 * considered set of attributes.
		 * 
		 * @return the number of rules read from an input and then successfully parsed
		 */
		public int getSuccessfullyParsedRulesCount() {
			return successfullyParsedRulesCount;
		}
		
		/**
		 * Gets unmodifiable list with messages for all parse errors.
		 * 
		 * @return unmodifiable list with messages for all parse errors
		 */
		public List<String> getFailureMessages() {
			return ObjectLists.unmodifiable(failureMessages);
		}
		
		/**
		 * Tells if all rules parsed from text representation have been successfully parsed into their in-memory representation.
		 * 
		 * @return {@code true} if all rules parsed from text representation have been successfully parsed into their in-memory representation,
		 *         {@code false} otherwise
		 */
		public boolean allRulesSuccessfullyParsed() {
			return readRulesCount == successfullyParsedRulesCount;
		}
	}
	
	/**
	 * Parse log of this rule parser used for logging effects of calls to methods attempting to parse a rule,
	 * like {@link RuleParser#parseRule(Element)} or {@link RuleParser#parseRuleEvaluations(Element)}.
	 */
	ParseLog parseLog;
	
	/**
	 * Tells if {@link RuleParseException} exception should be thrown when any rule (along with its all characteristics, if requested) cannot be successfully parsed from text representation.
	 * If {@code false}, then the exception is not thrown but a message is logged in the system console and the list of parsed rules does not change.
	 * Regardless of this setting, a {@link ParseLog parse log} is maintained and updated after each attempt to parse a rule.<br>
	 * <br>
	 * Initialized with {@code true}. Once modified, affects only subsequent attempts to parse a rule.
	 */
	public boolean exceptionOnRuleParseError = true;
	
	/**
	 * Constructs rule parser, sets attributes and initializes parser.
	 * At present, it is assumed that missing values in rules' conditions, if present, have to be represented either by "?" or "*".
	 * 
	 * @param attributes array of attributes {@link Attribute} which may be present in elementary conditions, that are forming decision rules
	 */
	public RuleParser(Attribute [] attributes) { //TODO: add constructor with missingValueStrings, and update method description
		this.attributes = attributes;
		this.initializeParser();
	}
	
	/**
	 * Constructs rule parser, sets attributes, encoding and initializes parser.
	 * At present, it is assumed that missing values in rules' conditions, if present, have to be represented either by "?" or "*".
	 * 
	 * @param attributes array of attributes {@link Attribute} which may be present in elementary conditions, that are forming decision rules
	 * @param encoding encoding of text data in RuleML
	 */
	public RuleParser(Attribute [] attributes, String encoding) { //TODO: add constructor with missingValueStrings, and update method description
		this.attributes = attributes;
		this.encoding = encoding;
		this.initializeParser();
	}
	
	/**
	 * Initializes rule parser.
	 */
	protected void initializeParser() {
		notNull(this.attributes, "Null set of attributes was passed to RuleML parser.");
		nonEmpty(this.attributes, "Empty set of attributes was passed to RuleML parser.");
		
		int size = attributes.length;
		this.attributesWithContext = new AttributeWithContext<?>[size];
		this.attributeNamesMap = new Object2IntOpenHashMap<String> ();
		this.attributeNamesMap.defaultReturnValue(RuleParser.DEFAULT_INDEX);
		for (int i = 0; i < size; i++) {
			if (this.attributes[i] instanceof EvaluationAttribute) {
				this.attributesWithContext[i] = new EvaluationAttributeWithContext((EvaluationAttribute)this.attributes[i], i);
			}
			else {
				this.attributesWithContext[i] = new AttributeWithContext<Attribute>(this.attributes[i], i);
			}
			this.attributeNamesMap.put(this.attributes[i].getName(), i);
		}
		
		//initialize evaluation parser used to parse thresholds in rules' conditions
		//TODO: allow different missing value strings
		this.evaluationParser = new EvaluationParser(DEFAULT_MISSING_VALUE_STRINGS, EvaluationParser.CachingType.VOLATILE); //use caching factory, and force volatile cache
		this.parseLog = new ParseLog();
	}
	
	/**
	 * Gets DOM interface which represent RuleML document. 
	 * 
	 * @param inputStream stream with RuleML
	 * @return interface representing RuleML document
	 */
	protected Document getRuleMLDocument(InputStream inputStream)  {
		Document document = null;
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(inputStream);
		} catch (ParserConfigurationException ex) {
			System.out.println("Error in configuration of RuleML parser. " + ex.toString());
		}
		catch (SAXException ex) {
			System.out.println("Incorrect structure of RuleML. " + ex.toString());
		}
		catch (IOException ex) {
			System.out.println("Error while reading RuleML. " + ex.toString());
		}
		
		return document;
	}
	
	/**
	 * Parses all rules from RuleML source and returns a map {@link Object2ObjectRBTreeMap} with each rule set from RuleML placed on different index. 
	 * 
	 * @param inputStream stream with RuleML
	 * @return map {@link Object2ObjectRBTreeMap} with each rule sets from RuleML placed on different index
	 */
	public Map<Integer, RuleSet> parseRules(InputStream inputStream)  {
		Map<Integer, RuleSet> ruleSets = null;
		
		notNull(this.attributesWithContext, "Attributes were not specified in RuleML parser.");
		notNull(this.attributeNamesMap, "Attributes were not specified in RuleML parser.");
		Document ruleMLDocument =  getRuleMLDocument(inputStream);
		
		// RuleML has been parsed
		if (ruleMLDocument != null) {
			Element root = ruleMLDocument.getDocumentElement();
            int lowestAvailableIndex = 0;
            ruleSets = new Object2ObjectRBTreeMap<Integer, RuleSet>();
            String learningDataHash;
            
            // iterate through sets of rules
            for (Node act : new NodeListWrapper(root.getElementsByTagName("act"))) {
                if (act.getNodeType() == Node.ELEMENT_NODE) {
                    int index;
                    try {
                        index = Integer.parseInt(act.getAttributes().getNamedItem("index").getNodeValue());
                    } catch (NumberFormatException ex) {
                        index = lowestAvailableIndex;
                    }
                    if (index >= lowestAvailableIndex) {
                        lowestAvailableIndex = index + 1;
                    }
                    
                    //try to parse learning data hash
                    learningDataHash = null;
                    if (act.getAttributes().getNamedItem("learningDataHash") != null) {
                    	learningDataHash = act.getAttributes().getNamedItem("learningDataHash").getNodeValue();
                    }
                    
                    //parse set of rules
                    List<Rule> rulesList = new ObjectArrayList<Rule>();
                    int counter = 0;
                    
                    for (Node actChild = ((Element)act).getFirstChild(); actChild != null; actChild = actChild.getNextSibling()) {
                        if (actChild.getNodeType() == Node.ELEMENT_NODE && "assert".equals(actChild.getNodeName())) {
                        	counter++; //next rule expected
                    		try {
                    			rulesList.add(parseRule((Element) actChild)); //parse rule from <assert>...</assert> block
                    		}
                    		catch (RuleParseException ex) {
                    			String failureMessage = new StringBuilder("Error while parsing decision rule no. ").append(counter).append(" from RuleML. ").append(ex).toString();
                    			parseLog.logFailure(failureMessage);
                    			if (exceptionOnRuleParseError) {
                    				throw new RuleParseException(failureMessage);
                    			} else {
                    				System.out.println(failureMessage);
                    			}
                    		}
                        }
                    }
                    //add parsed set of rules to the map
                    ruleSets.put(index, new RuleSet(rulesList.toArray(new Rule[rulesList.size()])));
                    
                    //remember learning data hash, if it was parsed from file
                    if (learningDataHash != null) {
                    	ruleSets.get(index).setLearningInformationTableHash(learningDataHash);
                    }
                }
            }
		}
		clearVolatileCaches();
		return ruleSets;
	}
	
	/**
	 * Parses all rules from RuleML source with characteristics and returns a map {@link Object2ObjectRBTreeMap} with each rule sets from RuleML placed on different index. 
	 * 
	 * @param inputStream stream with RuleML
	 * @return map {@link Object2ObjectRBTreeMap} with each rule sets with characteristics (may have not set values) from RuleML placed on different index
	 */
	public Map<Integer, RuleSetWithCharacteristics> parseRulesWithCharacteristics(InputStream inputStream)  {
		Map<Integer, RuleSetWithCharacteristics> ruleSets = null;
		
		notNull(this.attributesWithContext, "Attributes were not specified in RuleML parser.");
		notNull(this.attributeNamesMap, "Attributes were not specified in RuleML parser.");
		Document ruleMLDocument =  getRuleMLDocument(inputStream);
		
		// RuleML has been parsed
		if (ruleMLDocument != null) {
			Element root = ruleMLDocument.getDocumentElement();
            int lowestAvailableIndex = 0;
            ruleSets = new Object2ObjectRBTreeMap<Integer, RuleSetWithCharacteristics> ();
            String learningDataHash;
            
            // iterate through sets of rules
            for (Node act : new NodeListWrapper(root.getElementsByTagName("act"))) {
                if (act.getNodeType() == Node.ELEMENT_NODE) {
                    int index;
                    try {
                        index = Integer.parseInt(act.getAttributes().getNamedItem("index").getNodeValue());
                    } catch (NumberFormatException ex) {
                        index = lowestAvailableIndex;
                    }
                    if (index >= lowestAvailableIndex) {
                        lowestAvailableIndex = index + 1;
                    }
                    
                    //try to parse learning data hash
                    learningDataHash = null;
                    if (act.getAttributes().getNamedItem("learningDataHash") != null) {
                    	learningDataHash = act.getAttributes().getNamedItem("learningDataHash").getNodeValue();
                    }
                    
                    //parse set of rules and rule characteristics
                    List<Rule> rulesList = new ObjectArrayList<Rule>();
                    List<RuleCharacteristics> ruleCharacteristicsList = new ObjectArrayList<RuleCharacteristics>();
                    int counter = 0;
                    boolean ruleSuccessfullyParsed = false;
                    boolean ruleCharacteristicsSuccessfullyParsed = false;
                    Rule rule = null;
                    RuleCharacteristics ruleCharacteristics = null;
                    
                    for (Node actChild = ((Element)act).getFirstChild(); actChild != null; actChild = actChild.getNextSibling()) {
                        if (actChild.getNodeType() == Node.ELEMENT_NODE && "assert".equals(actChild.getNodeName())) {
                        	counter++; //next rule expected
                        	
                    		try {
                    			rule = parseRule((Element)actChild); //parse rule from <assert>...</assert> block
                    			ruleSuccessfullyParsed = true;
                    		} catch (RuleParseException ex) {
                    			String failureMessage = new StringBuilder("Error while parsing decision rule no. ").append(counter).append(" from RuleML. ").append(ex).toString();
                    			parseLog.logFailure(failureMessage);
                    			
                    			if (exceptionOnRuleParseError) {
                    				throw new RuleParseException(failureMessage);
                    			} else {
                    				System.out.println(failureMessage);
                    			}
                    		}
                    		
                    		try {
                    			ruleCharacteristics = parseRuleEvaluations((Element)actChild); //parse rule characteristics from <assert>...</assert> block
                    			ruleCharacteristicsSuccessfullyParsed = true;
                    		} catch (RuleParseException ex) {
                    			String failureMessage = new StringBuilder("Error while parsing characteristics of decision rule no. ").append(counter).append(" from RuleML. ").append(ex).toString();
                    			
                    			if (ruleSuccessfullyParsed) {
                    				parseLog.logFailure(failureMessage); //if only characteristics could not be parsed, it still counts as a failure when parsing the rule
                    			} else {
                    				parseLog.extendFailure(failureMessage); //add just message explaining why also characteristics could not be parsed
                    			}
                    			
                    			if (exceptionOnRuleParseError) {
                    				throw new RuleParseException(failureMessage);
                    			} else {
                    				System.out.println(failureMessage);
                    			}
                    		}
                    		
                    		if (ruleSuccessfullyParsed && ruleCharacteristicsSuccessfullyParsed) {
                    			rulesList.add(rule);
                    			ruleCharacteristicsList.add(ruleCharacteristics);
                    		}
                    		
                        }
                    }
                    //add parsed sets of rules and rule characteristics to the map
                    ruleSets.put(index, new RuleSetWithCharacteristics(rulesList.toArray(new Rule[rulesList.size()]),
                    		ruleCharacteristicsList.toArray(new RuleCharacteristics[ruleCharacteristicsList.size()])));
                    
                    //remember learning data hash, if it was parsed from file
                    if (learningDataHash != null) {
                    	ruleSets.get(index).setLearningInformationTableHash(learningDataHash);
                    }
                }
            }
		}
		clearVolatileCaches();
		return ruleSets;
	}
	
	/** 
	 * Parses a single rule from RuleML.
	 * 
	 * @param assertElement RuleML element representing a single rule
	 * @return parsed {@link Rule decision rule}
	 * 
	 * @throws RuleParseException if rule can't be parsed
	 */
	protected Rule parseRule(Element assertElement) {
		Rule rule = null;
		List<Condition<? extends EvaluationField>> conditions = null;
		List<List<Condition<? extends EvaluationField>>> decisions = null;
		RuleSemantics ruleSemantics = null;
		RuleType ruleType = null;
		
		for (Node impliesNode = assertElement.getFirstChild(); impliesNode != null; impliesNode = impliesNode.getNextSibling()) { //should be one loop iteration
			if (impliesNode.getNodeType() == Node.ELEMENT_NODE && "implies".equals(impliesNode.getNodeName())) { //we are within <implies>...</implies> node
				
				if (impliesNode.hasAttributes()) { //rule type explicitly given
            		NamedNodeMap attributes = impliesNode.getAttributes();
        			// we only consider one attribute: type
        			String type;
        			if (attributes.getLength() == 1) {
        				if ("type".equals(attributes.item(0).getNodeName())) {
            				type = attributes.item(0).getNodeValue();
            				if (type.equalsIgnoreCase("certain")) {
            					ruleType = RuleType.CERTAIN;
            				}
            				else if (type.equalsIgnoreCase("possible")) {
            					ruleType = RuleType.POSSIBLE;
            				}
            				else if (type.equalsIgnoreCase("approximate")) {
            					ruleType = RuleType.APPROXIMATE;
            				}
        				}
        			}
            	}
				
				for (Node impliesChild = impliesNode.getFirstChild(); impliesChild != null; impliesChild = impliesChild.getNextSibling()) {
					if (impliesChild.getNodeType() == Node.ELEMENT_NODE) {
                        if ("if".equals(impliesChild.getNodeName())) {
                            if (conditions == null) {
                            	conditions = parseRuleConditionPart((Element) impliesChild);
                            } else {
                                throw new RuleParseException("More than one 'if' node inside an 'implies' node detected in RuleML.");
                            }
                        }
                        else if ("then".equals(impliesChild.getNodeName())) {
                            if (decisions == null) {
                            	decisions = parseRuleDecisionPart((Element) impliesChild);
                            } else {
                                throw new RuleParseException("More than one 'then' node in an 'implies' node detected in RuleML.");
                            }
                        }
                        else if ("ruleSemantics".equals(impliesChild.getNodeName())) {
                        		ruleSemantics = parseRuleSemantics((Element) impliesChild);
                        }
                        else if ("ruleType".equals(impliesChild.getNodeName())) {
                        	if (ruleType == null) { // rule type not set yet
                        		ruleType = parseRuleType((Element) impliesChild);
                        	} else { // rule type already parsed
                        		RuleType ruleType2 = null;
                        		ruleType2 = parseRuleType((Element) impliesChild);
                        		if (ruleType != ruleType2) {
                        			throw new RuleParseException("Two different types set for a decision rule in RuleML.");
                        		}
                        	}
                        }
	                }
	             }
	         }
	    }
		
		notNull(conditions, "No condition part specified for a rule in RuleML.");
		notNull(decisions, "No decision part specified for a rule in RuleML.");
		
		if (decisions.size() >= 1) {
			if (ruleType == null) { // rule type not set
				ruleType = DEFAULT_RULE_TYPE;
			}
			if (ruleSemantics == null) { // rule semantics not set 
				// TODO for now the semantics of rule depends on the type of the first decision condition only
				Condition<? extends EvaluationField> firstDecisionCondition = decisions.get(0).get(0);
				AttributePreferenceType preferenceType = firstDecisionCondition.getAttributeWithContext().getAttributePreferenceType();
				if (preferenceType == AttributePreferenceType.NONE) {
					ruleSemantics = RuleSemantics.EQUAL;
				}
				else if (preferenceType == AttributePreferenceType.GAIN) {
					if (firstDecisionCondition instanceof ConditionAtLeast<?>) { 
						ruleSemantics = RuleSemantics.AT_LEAST;
					}
					else {
						ruleSemantics = RuleSemantics.AT_MOST;
					}
				}
				else { //cost-type attribute
					if (firstDecisionCondition instanceof ConditionAtLeast<?>) { 
						ruleSemantics = RuleSemantics.AT_MOST;
					}
					else {
						ruleSemantics = RuleSemantics.AT_LEAST;
					}
				}
			}
			rule = new Rule(ruleType, ruleSemantics, conditions, decisions);
		}
		
		return rule;
	}
	
	/** 
	 * Parses rule evaluations (characteristics) from RuleML.
	 * 
	 * @param assertElement RuleML element representing a single rule
	 * @return rule characteristics {@link RuleCharacteristics}
	 * 
	 * @throws RuleParseException if at least one of rule's evaluations (characteristics) can't be parsed
	 */
	protected RuleCharacteristics parseRuleEvaluations(Element assertElement) {
		RuleCharacteristics ruleCharacteristics = null;
		
		for (Node assertClause = assertElement.getFirstChild(); assertClause != null; assertClause = assertClause.getNextSibling()) {
			if (assertClause.getNodeType() == Node.ELEMENT_NODE && "implies".equals(assertClause.getNodeName())) {
				for (Node impliesClause = assertClause.getFirstChild(); impliesClause != null; impliesClause = impliesClause.getNextSibling()) {
					if (impliesClause.getNodeType() == Node.ELEMENT_NODE) {
                        if ("evaluations".equals(impliesClause.getNodeName())) {
                            if (ruleCharacteristics == null) {
                            	try {
                            		ruleCharacteristics = parseEachRuleEvaluation((Element) impliesClause);
                            	} catch (NumberFormatException|InvalidValueException exception) {
                            		throw new RuleParseException("Can't parse at least one of rule's characteristics from RuleML.");
                            	}
                            } else {
                                throw new RuleParseException("More than one 'evaluations' node detected in RuleML.");
                            }
                        }
					}
				}
			}
		}
		return ruleCharacteristics;
	}
	
	/**
	 * Parses the condition part of a rule from RuleML.
	 * 
	 * @param ifElement RuleML element representing the condition part of a rule
	 * @return a list {@link ObjectArrayList} of conditions {@link Condition}
	 * 
	 * @throws RuleParseException if rule's condition part can't be parsed
	 */
	protected List<Condition<? extends EvaluationField>> parseRuleConditionPart(Element ifElement) {
		List<Condition<? extends EvaluationField>> conditions = new ObjectArrayList<Condition<? extends EvaluationField>> ();
		for (Node ifClause = ifElement.getFirstChild(); ifClause != null; ifClause = ifClause.getNextSibling()) {
            if (ifClause.getNodeType() == Node.ELEMENT_NODE) {
                if ("and".equals(ifClause.getNodeName())) {
                    if (conditions.size() > 0) {
                        throw new RuleParseException("More than one 'and' node inside an 'if' node detected in RuleML.");
                    }
                    else {
	                    for (Node andElement = ifClause.getFirstChild(); andElement != null; andElement = andElement.getNextSibling()) {
	                        if (andElement.getNodeType() == Node.ELEMENT_NODE) {
	                        		if ("atom".equals(andElement.getNodeName())) {
	                                   conditions.add(parseRuleCondition((Element) andElement));
	                            }
	                        		else {throw new RuleParseException("Node other than 'atom' detected inside 'and' node in RuleML.");}
	                        }
	                    }
                    }
                } else if ("atom".equals(ifClause.getNodeName())) {
                    if (conditions.size() > 0) {
                        throw new RuleParseException("More than one condition node without conjunction operation inside an 'if' node detected in RuleML.");
                    }
                    conditions.add(parseRuleCondition((Element) ifClause));
                } else {
                    throw new RuleParseException("Node other than 'and' and 'atom' detected inside an 'if' node in RuleML.");
                }
            }
        }
		return conditions;
	}
	
	/**
	 * Parses the decision part of a rule from RuleML.
	 * 
	 * @param thenElement RuleML element representing the decision part of a rule
	 * @return a list {@link ObjectArrayList} of conditions {@link Condition}
	 * 
	 * @throws RuleParseException if rule's decision part can't be parsed 
	 */
	protected List<List<Condition<? extends EvaluationField>>> parseRuleDecisionPart(Element thenElement) {
		List<List<Condition<? extends EvaluationField>>> decisionsOR = new ObjectArrayList<List<Condition<? extends EvaluationField>>> ();
		List<Condition<? extends EvaluationField>> decisionsAND = null;
		for (Node thenClause = thenElement.getFirstChild(); thenClause != null; thenClause = thenClause.getNextSibling()) {
			if (thenClause.getNodeType() == Node.ELEMENT_NODE) {
				if ("or".equals(thenClause.getNodeName())) {
					for (Node orElement = thenClause.getFirstChild(); orElement != null; orElement = orElement.getNextSibling()) {
						if (orElement.getNodeType() == Node.ELEMENT_NODE) {
							if ("and".equals(orElement.getNodeName())) {
								decisionsAND = new ObjectArrayList<Condition<? extends EvaluationField>> ();
								for (Node andElement = orElement.getFirstChild(); andElement != null; andElement = andElement.getNextSibling()) {
									if (andElement.getNodeType() == Node.ELEMENT_NODE) {
										if ("atom".equals(andElement.getNodeName())) {
											decisionsAND.add(parseRuleCondition((Element) andElement));
										}
									}
									else {throw new RuleParseException("Node other than 'atom' detected inside 'and' node in RuleML.");}
								}
								decisionsOR.add(decisionsAND);
							}
							else if ("atom".equals(orElement.getNodeName())) {
								decisionsAND.add(parseRuleCondition((Element) orElement));
								decisionsOR.add(decisionsAND);
							}
							else {throw new RuleParseException("Node other than 'and', and 'atom' detected inside 'or' node in RuleML.");}
                       }
                    }
                } 
                else if ("and".equals(thenClause.getNodeName())) {
                		decisionsAND = new ObjectArrayList<Condition<? extends EvaluationField>> ();
					for (Node andElement = thenClause.getFirstChild(); andElement != null; andElement = andElement.getNextSibling()) {
						if (andElement.getNodeType() == Node.ELEMENT_NODE) {
							if ("atom".equals(andElement.getNodeName())) {
								decisionsAND.add(parseRuleCondition((Element) andElement));
							}
						}
						else {throw new RuleParseException("Node other than 'atom' detected inside 'and' node in RuleML.");}
					}
					decisionsOR.add(decisionsAND);
				}
                else if ("atom".equals(thenClause.getNodeName())) {
                		decisionsAND = new ObjectArrayList<Condition<? extends EvaluationField>> ();
                		decisionsAND.add(parseRuleCondition((Element) thenClause));
                		decisionsOR.add(decisionsAND);
                	} 
                else {
                    throw new RuleParseException("Node other than 'or', 'and', and 'atom' detected inside a 'than' node in RuleML.");
                }
            }
        }
		return decisionsOR;
	}
	
	/**
	 * Parses a single condition of a rule from RuleML.
	 * 
	 * @param atomElement RuleML element representing a single condition
	 * @return a condition {@link Condition}
	 * 
	 * @throws RuleParseException if rule's condition can't be parsed
	 */
	protected Condition<? extends EvaluationField> parseRuleCondition(Element atomElement) {
		Condition<? extends EvaluationField> condition = null;
		boolean relationThresholdVSObject = true; // by default relation is of type threshold versus object
        NodeList relationList = atomElement.getElementsByTagName("rel");
        if (relationList.getLength() > 1) {
            throw new RuleParseException("More than one relation ('rel' node) inside an 'atom' node detected in RuleML.");
        }
        if (relationList.getLength() < 1) {
            throw new RuleParseException("No 'rel' node was detected inside an 'atom' node in RuleML.");
        }
        String relation = relationList.item(0).getTextContent();
        // check type of relation
        if (relationList.item(0).hasAttributes()) {
    		NamedNodeMap attributes = relationList.item(0).getAttributes();
			// we only consider one attribute: type
			String type;
			if (attributes.getLength() == 1) {
				if ("type".equals(attributes.item(0).getNodeName())) {
    				type = attributes.item(0).getNodeValue();
    				if (type.equalsIgnoreCase(RuleMLElements.OBJECT_VS_THRESHOLD)) {
    					relationThresholdVSObject = false;
    				}
				}
			}
    	}
        
        NodeList attributeList = atomElement.getElementsByTagName("var");
        if (attributeList.getLength() > 1) {
            throw new RuleParseException("More than one attribute name ('var' node) was detected inside an 'atom' node in RuleML.");
        }
        if (attributeList.getLength() < 1) {
            throw new RuleParseException("No 'var' node was detected inside an 'atom' node in RuleML.");
        }
        String attributeName = attributeList.item(0).getTextContent();
        NodeList valueList = atomElement.getElementsByTagName("ind");
        if (valueList.getLength() > 1) {
            throw new RuleParseException("More than one value ('ind' node) was detected inside an 'atom' node in RuleML.");
        }
        if (valueList.getLength() < 1) {
            throw new RuleParseException("No 'ind' node was detected inside an 'atom' node in RuleML.");
        }
        String value = valueList.item(0).getTextContent();
        
        int attributeIndex = this.attributeNamesMap.getInt(attributeName);
        if (attributeIndex != RuleParser.DEFAULT_INDEX) {
        		if ((this.attributes[attributeIndex] instanceof EvaluationAttribute) && (this.attributesWithContext[attributeIndex] instanceof EvaluationAttributeWithContext)) {
        			try {
        				condition = constructCondition(relation, value, relationThresholdVSObject, (EvaluationAttributeWithContext)attributesWithContext[attributeIndex]);
        			} catch (FieldParseException exception) {
        				throw new RuleParseException("Could not read an evaluation in RuleML decision rule's condition: " + exception.getMessage());
        			}
        		}
		    else {
		    		throw new RuleParseException("Attribute used in a RuleML decision rule is not an evaluation attribute.");
		    }
        }
        else {
        	    throw new RuleParseException("Attribute name ('var' node) in RuleML is not expected by RuleML parser.");
        }
	    return condition;
    }
	
	/**
	 * Constructs a condition {@link Condition} for a given relation, value, relation type and attribute (with context) {@link AttributeWithContext}.
	 * 
	 * @param relation string representation of relation
	 * @param value string representation of value
	 * @param relationThresholdVSObject type of relation (relation object versus value when false or value versus object when true)
	 * @param attributeWithContext attribute for which condition is constructed
	 * @return constructed condition 
	 * 
	 * @throws FieldParseException if given String value cannot be parsed as a value of the given attribute
	 */
	protected Condition<EvaluationField> constructCondition(String relation, String value, boolean relationThresholdVSObject, EvaluationAttributeWithContext attributeWithContext) {
		Condition<EvaluationField> condition = null;
		if (!relationThresholdVSObject) { // standard object versus value (threshold) relation
			if ("le".equals(relation.toLowerCase())) {
				condition = new ConditionAtMostObjectVSThreshold<EvaluationField>(attributeWithContext, constructEvaluation(value, attributeWithContext.getAttribute()));
        	}
        	else if ("ge".equals(relation.toLowerCase())) {
        		condition = new ConditionAtLeastObjectVSThreshold<EvaluationField>(attributeWithContext, constructEvaluation(value, attributeWithContext.getAttribute()));
        	}
        	else { //equality condition
        		condition = new ConditionEqualObjectVSThreshold<EvaluationField>(attributeWithContext, constructEvaluation(value, attributeWithContext.getAttribute()));
        	}
		}
		else { // value (threshold) versus object relation
			if ("le".equals(relation.toLowerCase())) {
				condition = new ConditionAtMostThresholdVSObject<EvaluationField>(attributeWithContext, constructEvaluation(value, attributeWithContext.getAttribute()));
        	}
        	else if ("ge".equals(relation.toLowerCase())) {
        		condition = new ConditionAtLeastThresholdVSObject<EvaluationField>(attributeWithContext, constructEvaluation(value, attributeWithContext.getAttribute()));
        	}
        	else { //equality condition
        		condition = new ConditionEqualThresholdVSObject<EvaluationField>(attributeWithContext, constructEvaluation(value, attributeWithContext.getAttribute()));
        	}
		}
		return condition;
	}

	/**
	 * Constructs an evaluation field {@link EvaluationField} basing on a string {@link String} representation of evaluation.
	 * 
	 * @param evaluation string representation of evaluation
	 * @param attribute attribute, which evaluation is parsed
	 * 
	 * @return evaluation field {@link EvaluationField}
	 * @throws FieldParseException if given string cannot be parsed as a value of the given attribute
	 */
	protected EvaluationField constructEvaluation(String evaluation, EvaluationAttribute attribute) {
		return this.evaluationParser.parseEvaluation(evaluation, attribute);
	}
	
	/**
	 * Parses each evaluation characterizing a rule from RuleML.
	 * 
	 * @param evaluationsElement RuleML element representing evaluations of a rule
	 * @return rule characteristics {@link RuleCharacteristics} of a rule
	 * 
	 * @throws NumberFormatException if any integer or double evaluation can't be parsed from text
	 * @throws InvalidValueException if any parsed integer or double evaluation can't be set in rule characteristics
	 */
	protected RuleCharacteristics parseEachRuleEvaluation(Element evaluationsElement) {
		RuleCharacteristics ruleCharacteristics = new RuleCharacteristics();
		for (Node evaluationElement = evaluationsElement.getFirstChild(); evaluationElement != null; evaluationElement = evaluationElement.getNextSibling()) {
            if (evaluationElement.getNodeType() == Node.ELEMENT_NODE) {
                if ("evaluation".equals(evaluationElement.getNodeName())) {
            		if (evaluationElement.hasAttributes()) {
            			NamedNodeMap attributes = evaluationElement.getAttributes();
            			// we only consider two attributes: measure and value
            			String measure, value;
            			if (attributes.getLength() == 2) {
            				if (("measure".equals(attributes.item(0).getNodeName())) && ("value".equals(attributes.item(1).getNodeName()))) {
                				measure = attributes.item(0).getNodeValue();
                				value = attributes.item(1).getNodeValue();
                				if ("Support".equals(measure)) {
                					ruleCharacteristics.setSupport(Integer.parseInt(value));
                				}
                				else if ("Strength".equals(measure)) {
                					ruleCharacteristics.setStrength(Double.parseDouble(value));
                				}
                				else if ("Confidence".equals(measure)) {
                					ruleCharacteristics.setConfidence(Double.parseDouble(value));
                				}
                				else if ("CoverageFactor".equals(measure)) {
                					ruleCharacteristics.setCoverageFactor(Double.parseDouble(value));
                				}
                				else if ("Coverage".equals(measure)) {
                					ruleCharacteristics.setCoverage(Integer.parseInt(value));
                				}
                				else if ("NegativeCoverage".equals(measure)) {
                					ruleCharacteristics.setNegativeCoverage(Integer.parseInt(value));
                				}
                				else if ("EpsilonMeasure".equals(measure)) {
                					ruleCharacteristics.setEpsilon(Double.parseDouble(value));
                				}
                				else if ("InconsistencyMeasure".equals(measure)) {
                					ruleCharacteristics.setEpsilon(Double.parseDouble(value));
                				}
                				else if ("EpsilonPrimMeasure".equals(measure)) {
                					ruleCharacteristics.setEpsilonPrime(Double.parseDouble(value));
                				}
                				else if ("EpsilonPrimeMeasure".equals(measure)) {
                					ruleCharacteristics.setEpsilonPrime(Double.parseDouble(value));
                				}
                				else if ("f-ConfirmationMeasure".equals(measure)) {
                					ruleCharacteristics.setFConfirmation(Double.parseDouble(value));
                				}
                				else if ("A-ConfirmationMeasure".equals(measure)) {
                					ruleCharacteristics.setAConfirmation(Double.parseDouble(value));
                				}
                				else if ("Z-ConfirmationMeasure".equals(measure)) {
                					ruleCharacteristics.setZConfirmation(Double.parseDouble(value));
                				}
                				else if ("l-ConfirmationMeasure".equals(measure)) {
                					ruleCharacteristics.setLConfirmation(Double.parseDouble(value));
                				}
                				else if ("c1-ConfirmationMeasure".equals(measure)) {
                					ruleCharacteristics.setC1Confirmation(Double.parseDouble(value));
                				}
                				else if ("s-ConfirmationMeasure".equals(measure)) {
                					ruleCharacteristics.setSConfirmation(Double.parseDouble(value));
                				}
            				}
            			}
            		}
                }
            }
		}
		return ruleCharacteristics;
	}
	
	/**
	 * Parses semantics of a rule from RuleML.
	 * 
	 * @param ruleSemanticsElement RuleML element representing rule semantics
	 * @return rule semantics {@link RuleSemantics}
	 */
	protected RuleSemantics parseRuleSemantics(Element ruleSemanticsElement) {
		RuleSemantics ruleSemantics = null;
        
		Node valueNode = ruleSemanticsElement.getFirstChild();
		if (valueNode != null) {
		        String value = valueNode.getNodeValue();
	        if ("le".equals(value.toLowerCase())) {
	        		ruleSemantics = RuleSemantics.AT_MOST;
	        	}
	        	else if ("ge".equals(value.toLowerCase())) {
	        		ruleSemantics = RuleSemantics.AT_LEAST;
	        	}
	        	else if ("eq".equals(value.toLowerCase())) {
	        		ruleSemantics = RuleSemantics.EQUAL;
	        }
		}
		
	    return ruleSemantics;
    }
	
	/**
	 * Parses type of a rule from RuleML.
	 * 
	 * @param ruleTypeElement RuleML element representing rule type
	 * @return rule type {@link RuleType}
	 */
	protected RuleType parseRuleType(Element ruleTypeElement) {
		RuleType ruleType = null;
		
		Node valueNode = ruleTypeElement.getFirstChild();
		if (valueNode != null) {   
	        String value = valueNode.getTextContent();
	        if ("certain".equals(value.toLowerCase())) {
	        		ruleType	 = RuleType.CERTAIN;
	        	}
	        	else if ("possible".equals(value.toLowerCase())) {
	        		ruleType	 = RuleType.POSSIBLE;
	        	}
	        	else if ("approximate".equals(value.toLowerCase())) {
	        		ruleType	 = RuleType.APPROXIMATE;
	        }
        }
		
	    return ruleType;
    }
	
	/**
	 * Clears volatile caches of all evaluation field caching factories.
	 *  
	 * @param attributes attributes of the information table for which parsed rules have been induced
	 */
	void clearVolatileCaches() {
		//clear volatile caches of all evaluation field caching factories
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] instanceof EvaluationAttribute) {
				((EvaluationAttribute)attributes[i]).getValueType().getCachingFactory().clearVolatileCache(); //clears volatile cache of the caching factory corresponding to current evaluation attribute
			}
		}
	}
	
	/**
	 * Gets parse log of this rule parser used for logging effects of calls to methods attempting to parse a rule, or a rule with its characteristics.
	 * 
	 * @return parse log of this rule parser
	 */
	public ParseLog getParseLog() {
		return parseLog;
	}
	
}
