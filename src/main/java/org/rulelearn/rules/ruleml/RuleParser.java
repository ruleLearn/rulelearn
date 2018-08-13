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
import org.rulelearn.rules.RuleSemantics;
import org.rulelearn.rules.RuleSet;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import static org.rulelearn.core.Precondition.nonEmpty;
import static org.rulelearn.core.Precondition.notNull;

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
	 * All attributes which may be present in conditions, which are forming decision rules.
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
	
	
	public RuleParser (Attribute [] attributes) {
		this.attributes = attributes;
		this.initializeParser();
	}
	
	public RuleParser (Attribute [] attributes, String encoding) {
		this.attributes = attributes;
		this.encoding = encoding;
		this.initializeParser();
	}
	
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
	 * Parses a single rule from RuleML.
	 * 
	 * @param assertElement RuleML element representing a single rule
	 * @return a rule {@link Rule}
	 * @throws RuleParseException if any of the rules can't be parsed
	 */
	protected Rule parseRule (Element assertElement) throws RuleParseException {
		Rule rule = null;
		List<Condition<? extends EvaluationField>> conditions = null;
		List<Condition<? extends EvaluationField>> decisions = null;
		
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
                        if ("then".equals(impliesClause.getNodeName())) {
                            if (decisions == null) {
                            	decisions = parseRuleDecisionPart((Element) impliesClause);
                            } else {
                                throw new RuleParseException("More than one 'then' node in an 'implies' node detected in RuleML.");
                            }
                        }
                        if ("evaluations".equals(impliesClause.getNodeName())) {
                        		// TODO parse evaluations
                            /*if (evaluations == null) {
                         
                            } else {
                                throw new RuleParserException("More than one 'evaluations' node detected in RuleML.");
                            }*/
                        }
	                }
	             }
	         }
	    }
		
		notNull(conditions, "No condition part specified for a rule in RuleML.");
		notNull(decisions, "No decision part specified for a rule in RuleML.");
		if (decisions.size() == 1) {
			Condition<? extends EvaluationField> decision = decisions.get(0);
			// TODO include types of rules in RuleML
			rule = new Rule(RuleType.CERTAIN, conditions, decision);
		}
		else if (decisions.size() > 1) {
			Condition<? extends EvaluationField> decision = decisions.get(0);
			// TODO check correctness
			// TODO include types of rules in RuleML
			if (decision.getAttributeWithContext().getAttributePreferenceType() == AttributePreferenceType.NONE) {
				rule = new Rule(RuleType.CERTAIN, RuleSemantics.EQUAL, ((EvaluationField)decisions.get(0).getLimitingEvaluation()), conditions, decisions);
			}
			else if (decision.getAttributeWithContext().getAttributePreferenceType() == AttributePreferenceType.GAIN) {
				if (decision instanceof SimpleConditionAtLeast) {
					rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_LEAST, ((EvaluationField)decisions.get(0).getLimitingEvaluation()), conditions, decisions);
				}
				else { // TODO (MSz): verify if this else is safe (what if equality decision)
					rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_MOST, ((EvaluationField)decisions.get(0).getLimitingEvaluation()), conditions, decisions);
				}
			}
			else {
				if (decision instanceof SimpleConditionAtLeast) {
					rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_MOST, ((EvaluationField)decisions.get(0).getLimitingEvaluation()), conditions, decisions);
				}
				else { // TODO (MSz): verify if this else is safe (what if equality decision)
					rule = new Rule(RuleType.CERTAIN, RuleSemantics.AT_LEAST, ((EvaluationField)decisions.get(0).getLimitingEvaluation()), conditions, decisions);
				}
			}
		}
		return rule;
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
	protected List<Condition<? extends EvaluationField>> parseRuleDecisionPart (Element thenElement) throws RuleParseException {
		List<Condition<? extends EvaluationField>> decisions = new ObjectArrayList<Condition<? extends EvaluationField>> ();
		for (Node thenClause = thenElement.getFirstChild(); thenClause != null; thenClause = thenClause.getNextSibling()) {
            if (thenClause.getNodeType() == Node.ELEMENT_NODE) {
                if ("or".equals(thenClause.getNodeName())) {
                    if (decisions.size() > 0) {
                        throw new RuleParseException("More than one 'or' node inside a 'then' node detected in RuleML");
                    }
                    for (Node orElement = thenClause.getFirstChild(); orElement != null; orElement = orElement.getNextSibling()) {
                       if (orElement.getNodeType() == Node.ELEMENT_NODE) {
                           if ("atom".equals(orElement.getNodeName())) {
                        	   		decisions.add(parseRuleCondition((Element) orElement));
                           }
                           else {throw new RuleParseException("Node other than 'atom' detected inside 'or' node in RuleML.");}
                       }
                    }
                } else if ("atom".equals(thenClause.getNodeName())) {
                    if (decisions.size() > 0) {
                        throw new RuleParseException("More than one condition node without disjunction operation inside a 'then' node detected in RuleML.");
                    }
                    decisions.add(parseRuleCondition((Element) thenClause));
                } else {
                    throw new RuleParseException("Node other than 'or' and 'atom' detected inside a 'than' node in RuleML.");
                }
            }
        }
		return decisions;
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
}
