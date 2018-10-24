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

import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeWithContext;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.rules.Condition;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleCharacteristics;
import org.rulelearn.rules.RuleSemantics;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.RuleSetWithCharacteristics;
import org.rulelearn.rules.RuleType;
import org.rulelearn.rules.SimpleConditionAtLeast;
import org.rulelearn.rules.SimpleConditionAtMost;
import org.rulelearn.rules.SimpleConditionEqual;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.SimpleField;
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

/**
 * Parser of decision rules stored in RuleML format. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class RuleParser {

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
	 * Constructs rule parser, sets attributes and initializes parser.
	 * 
	 * @param attributes array of attributes {@link Attribute} which may be present in elementary conditions, that are forming decision rules
	 */
	public RuleParser (Attribute [] attributes) {
		this.attributes = attributes;
		this.initializeParser();
	}
	
	/**
	 * Constructs rule parser, sets attributes, encoding and initializes parser.
	 * 
	 * @param attributes array of attributes {@link Attribute} which may be present in elementary conditions, that are forming decision rules
	 * @param encoding encoding of text data in RuleML
	 */
	public RuleParser (Attribute [] attributes, String encoding) {
		this.attributes = attributes;
		this.encoding = encoding;
		this.initializeParser();
	}
	
	/**
	 * Initializes rule parser.
	 */
	protected void initializeParser () {
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
	}
	
	/**
	 * Gets DOM interface which represent RuleML document. 
	 * 
	 * @param inputStream stream with RuleML
	 * @return interface representing RuleML document
	 */
	protected Document getRuleMLDocument (InputStream inputStream)  {
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
	 * Parses all rules from RuleML source and returns a map {@link Object2ObjectRBTreeMap} with each rule sets from RuleML placed on different index. 
	 * 
	 * @param inputStream stream with RuleML
	 * @return map {@link Object2ObjectRBTreeMap} with each rule sets from RuleML placed on different index
	 */
	public Map<Integer, RuleSet> parseRules (InputStream inputStream)  {
		Map<Integer, RuleSet> ruleSets = null;
		
		notNull(this.attributesWithContext, "Attributes were not specified in RuleML parser.");
		notNull(this.attributeNamesMap, "Attributes were not specified in RuleML parser.");
		Document ruleMLDocument =  getRuleMLDocument(inputStream);
		// RuleML has been parsed
		if (ruleMLDocument != null) {
			Element root = ruleMLDocument.getDocumentElement();
            int lowestAvailableIndex = 0;
            ruleSets = new Object2ObjectRBTreeMap<Integer, RuleSet> ();
            // iterate through sets of rules
            for (Node act : new NodeListWrapper(root.getElementsByTagName("act"))) {
                if (act.getNodeType() == Node.ELEMENT_NODE) {
                    int index;
                    try {
                        index = Integer.parseInt(act.getAttributes().getNamedItem("index").getNodeValue());
                    } catch (NumberFormatException ex) {
                        index = lowestAvailableIndex;
                    }
                    if (index >= lowestAvailableIndex)
                        lowestAvailableIndex = index + 1;
                    //parse set of rules
                    List<Rule> rules = new ObjectArrayList<Rule> ();
                    for (Node actChild = ((Element)act).getFirstChild(); actChild != null; actChild = actChild.getNextSibling()) {
                        if (actChild.getNodeType() == Node.ELEMENT_NODE && "assert".equals(actChild.getNodeName())) {
                        		try {
                        			rules.add(parseRule((Element) actChild));
                        		}
                        		catch (RuleParseException ex) {
								System.out.println("Error while parsing RuleML. " + ex.toString());
							}
                        }
                    }
                    //add parsed set of rules to the map
                    ruleSets.put(index, new RuleSet(rules.toArray(new Rule[rules.size()])));
                }
            }
		}
		return ruleSets;
	}
	
	/**
	 * Parses all rules from RuleML source with characteristics and returns a map {@link Object2ObjectRBTreeMap} with each rule sets from RuleML placed on different index. 
	 * 
	 * @param inputStream stream with RuleML
	 * @return map {@link Object2ObjectRBTreeMap} with each rule sets with characteristics (may have not set values) from RuleML placed on different index
	 */
	public Map<Integer, RuleSetWithCharacteristics> parseRulesWithCharacteristics (InputStream inputStream)  {
		Map<Integer, RuleSetWithCharacteristics> ruleSets = null;
		
		notNull(this.attributesWithContext, "Attributes were not specified in RuleML parser.");
		notNull(this.attributeNamesMap, "Attributes were not specified in RuleML parser.");
		Document ruleMLDocument =  getRuleMLDocument(inputStream);
		// RuleML has been parsed
		if (ruleMLDocument != null) {
			Element root = ruleMLDocument.getDocumentElement();
            int lowestAvailableIndex = 0;
            ruleSets = new Object2ObjectRBTreeMap<Integer, RuleSetWithCharacteristics> ();
            // iterate through sets of rules
            for (Node act : new NodeListWrapper(root.getElementsByTagName("act"))) {
                if (act.getNodeType() == Node.ELEMENT_NODE) {
                    int index;
                    try {
                        index = Integer.parseInt(act.getAttributes().getNamedItem("index").getNodeValue());
                    } catch (NumberFormatException ex) {
                        index = lowestAvailableIndex;
                    }
                    if (index >= lowestAvailableIndex)
                        lowestAvailableIndex = index + 1;
                    //parse set of rules and rule characteristics
                    List<Rule> rules = new ObjectArrayList<Rule> ();
                    List<RuleCharacteristics> ruleCharacteristics = new ObjectArrayList<RuleCharacteristics> ();
                    for (Node actChild = ((Element)act).getFirstChild(); actChild != null; actChild = actChild.getNextSibling()) {
                        if (actChild.getNodeType() == Node.ELEMENT_NODE && "assert".equals(actChild.getNodeName())) {
                        		try {
                        			rules.add(parseRule((Element) actChild));
                        			ruleCharacteristics.add(parseRuleEvaluations((Element) actChild));
                        		}
                        		catch (RuleParseException ex) {
								System.out.println("Error while parsing RuleML. " + ex.toString());
							}
                        }
                    }
                    //add parsed sets of rules and rule characteristics to the map
                    ruleSets.put(index, new RuleSetWithCharacteristics(rules.toArray(new Rule[rules.size()]), 
                    													 ruleCharacteristics.toArray(new RuleCharacteristics[ruleCharacteristics.size()])));
                }
            }
		}
		return ruleSets;
	}
	
	/** 
	 * Parses a single rule from RuleML.
	 * 
	 * @param assertElement RuleML element representing a single rule
	 * @return a rule {@link Rule}
	 * @throws RuleParseException if any of the rules can't be parsed
	 */
	protected Rule parseRule (Element assertElement) throws RuleParseException {
		Rule rule = null;
		List<Condition<? extends EvaluationField>> conditions = null;
		List<List<Condition<? extends EvaluationField>>> decisions = null;
		RuleSemantics ruleSemantics = null;
		RuleType ruleType = null;
		
		for (Node assertClause = assertElement.getFirstChild(); assertClause != null; assertClause = assertClause.getNextSibling()) {
			if (assertClause.getNodeType() == Node.ELEMENT_NODE && "implies".equals(assertClause.getNodeName())) {
				for (Node impliesClause = assertClause.getFirstChild(); impliesClause != null; impliesClause = impliesClause.getNextSibling()) {
					if (impliesClause.getNodeType() == Node.ELEMENT_NODE) {
                        if ("if".equals(impliesClause.getNodeName())) {
                            if (conditions == null) {
                            	conditions = parseRuleConditionPart((Element) impliesClause);
                            } else {
                                throw new RuleParseException("More than one 'if' node inside an 'implies' node detected in RuleML.");
                            }
                        }
                        else if ("then".equals(impliesClause.getNodeName())) {
                            if (decisions == null) {
                            	decisions = parseRuleDecisionPart((Element) impliesClause);
                            } else {
                                throw new RuleParseException("More than one 'then' node in an 'implies' node detected in RuleML.");
                            }
                        }
                        else if ("ruleSemantics".equals(impliesClause.getNodeName())) {
                        		ruleSemantics = parseRuleSemantics((Element) impliesClause);
                        }
                        else if ("ruleType".equals(impliesClause.getNodeName())) {
                        		ruleType = parseRuleType((Element) impliesClause);
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
				// TODO for now the semantics of rule depends on type of first decision condition only
				Condition<? extends EvaluationField> firstDecisionCondition = decisions.get(0).get(0);
				AttributePreferenceType preferenceType = firstDecisionCondition.getAttributeWithContext().getAttributePreferenceType();
				if (preferenceType == AttributePreferenceType.NONE) {
					ruleSemantics = RuleSemantics.EQUAL;
				}
				else if (preferenceType == AttributePreferenceType.GAIN) {
					if (firstDecisionCondition instanceof SimpleConditionAtLeast) { // TODO works only for SimpleCondition
						ruleSemantics = RuleSemantics.AT_LEAST;
					}
					else {
						ruleSemantics = RuleSemantics.AT_MOST;
					}
				}
				else { //cost-type attribute
					if (firstDecisionCondition instanceof SimpleConditionAtLeast) { // TODO works only for SimpleCondition
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
	 * Parses rule evaluations from RuleML.
	 * 
	 * @param assertElement RuleML element representing a single rule
	 * @return rule characteristics {@link RuleCharacteristics}
	 * @throws RuleParseException if any of the rules can't bex parsed
	 */
	protected RuleCharacteristics parseRuleEvaluations (Element assertElement) throws RuleParseException {
		RuleCharacteristics ruleCharacteristics = null;
		
		for (Node assertClause = assertElement.getFirstChild(); assertClause != null; assertClause = assertClause.getNextSibling()) {
			if (assertClause.getNodeType() == Node.ELEMENT_NODE && "implies".equals(assertClause.getNodeName())) {
				for (Node impliesClause = assertClause.getFirstChild(); impliesClause != null; impliesClause = impliesClause.getNextSibling()) {
					if (impliesClause.getNodeType() == Node.ELEMENT_NODE) {
                        if ("evaluations".equals(impliesClause.getNodeName())) {
                            if (ruleCharacteristics == null) {
                            	ruleCharacteristics = parseEachRuleEvaluation((Element) impliesClause);
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
	 * @throws RuleParseException if any of the rules can't be parsed
	 */
	protected List<Condition<? extends EvaluationField>> parseRuleConditionPart (Element ifElement) throws RuleParseException {
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
	 * @throws RuleParseException if any of the rules can't be parsed 
	 */
	protected List<List<Condition<? extends EvaluationField>>> parseRuleDecisionPart (Element thenElement) throws RuleParseException {
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
	 * @throws RuleParseException if any of the rules can't be parsed
	 */
	protected Condition<? extends EvaluationField> parseRuleCondition (Element atomElement) throws RuleParseException {
		Condition<? extends EvaluationField> condition = null;
        NodeList relationList = atomElement.getElementsByTagName("rel");
        if (relationList.getLength() > 1) {
            throw new RuleParseException("More than one relation ('rel' node) inside an 'atom' node detected in RuleML.");
        }
        if (relationList.getLength() < 1) {
            throw new RuleParseException("No 'rel' node was detected inside an 'atom' node in RuleML.");
        }
        String relation = relationList.item(0).getTextContent();
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
		        	if ("le".equals(relation.toLowerCase())) {
		        		condition = new SimpleConditionAtMost((EvaluationAttributeWithContext)this.attributesWithContext[attributeIndex], 
		        						((SimpleField)parseEvaluation(value, (EvaluationAttribute)this.attributes[attributeIndex])));
		        	}
		        	else if ("ge".equals(relation.toLowerCase())) {
		        		condition = new SimpleConditionAtLeast((EvaluationAttributeWithContext)this.attributesWithContext[attributeIndex], 
		        						((SimpleField)parseEvaluation(value, (EvaluationAttribute)this.attributes[attributeIndex])));
		        	}
		        	else {
		        		condition = new SimpleConditionEqual((EvaluationAttributeWithContext)this.attributesWithContext[attributeIndex], 
		        						((SimpleField)parseEvaluation(value, (EvaluationAttribute)this.attributes[attributeIndex])));
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
	 * Parses a string {@link String} representation of evaluation to evaluation field {@link EvaluationField}.
	 * 
	 * @param evaluation string representation of evaluation
	 * @param attribute attribute, which evaluation is parsed
	 * @return evaluation field {@link EvaluationField}
	 */
	// TODO should be propagated somewhere else and used also in InformationTableBuilder
	protected EvaluationField parseEvaluation (String evaluation, EvaluationAttribute attribute) {
		EvaluationField field = null;
		if (attribute.getValueType() instanceof SimpleField) {
			Field valueType = attribute.getValueType();
			if (valueType instanceof IntegerField) {
				try {
					field = IntegerFieldFactory.getInstance().create(Integer.parseInt(evaluation), attribute.getPreferenceType());
				}
				catch (NumberFormatException ex) {
					// just assign a reference (no new copy of missing value field is made)
					field = attribute.getMissingValueType();
					throw new NumberFormatException(ex.getMessage());
				}
			}
			else if (valueType instanceof RealField) {
				try {
					field = RealFieldFactory.getInstance().create(Double.parseDouble(evaluation), attribute.getPreferenceType());
				}
				catch (NumberFormatException ex) {
					// just assign a reference (no new copy of missing value field is made)
					field = attribute.getMissingValueType();
					throw new NumberFormatException(ex.getMessage());
				}
			}
			else if (valueType instanceof EnumerationField) {
				// TODO some optimization is needed here (e.g., construction of a table with element lists)
				int index = ((EnumerationField)valueType).getElementList().getIndex(evaluation);
				if (index != ElementList.DEFAULT_INDEX) {
					field = EnumerationFieldFactory.getInstance().create(((EnumerationField)valueType).getElementList(), index, attribute.getPreferenceType());
				}
				else {
					field = attribute.getMissingValueType();
					throw new IndexOutOfBoundsException(new StringBuilder("Incorrect value of enumeration: ").append(evaluation).append(" was replaced by a missing value.").toString());
				}
			}
			else {
				field = attribute.getMissingValueType();
			}
		}
		return field;
	}
	
	/**
	 * Parses each evaluation characterizing a rule from RuleML.
	 * 
	 * @param evaluationsElement RuleML element representing evaluations of a rule
	 * @return rule characteristics {@link RuleCharacteristics} of a rule
	 * @throws RuleParseException if any of evaluations of rules can't be parsed
	 */
	protected RuleCharacteristics parseEachRuleEvaluation (Element evaluationsElement) throws RuleParseException {
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
	protected RuleSemantics parseRuleSemantics (Element ruleSemanticsElement) {
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
	 * @param ruleTypeElement RuleML element representing rule semantics
	 * @return rule type {@link RuleType}
	 */
	protected RuleType parseRuleType (Element ruleTypeElement) {
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
}
