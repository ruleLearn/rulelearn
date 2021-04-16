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

package org.rulelearn.classification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.rulelearn.core.Precondition.notNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.data.json.AttributeParser;
import org.rulelearn.data.json.ObjectParser;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleSet;
import org.rulelearn.rules.ruleml.RuleParser;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link SimpleRuleClassifier}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
class SimpleRuleClassifierTest {

	private SimpleRuleClassifier classifier;
	private EvaluationAttributeWithContext decisionAttribute;
	private ElementList domain;
	private EnumerationField value1, value2, value3, value5;
	
	@Mock
	private InformationTable informationTableMock;
	@Mock
	private RuleSet ruleSetMock;
	@Mock
	private Rule rule1Mock, rule2Mock, rule3Mock, rule4Mock, rule5Mock, rule6Mock; 
	@Mock
	private ConditionAtLeast<EnumerationField> decision1Mock, decision2Mock, decision3Mock;
	@Mock
	private ConditionAtMost<EnumerationField> decision4Mock, decision5Mock, decision6Mock;
	
	/**
	 * Setup all variables necessary for classification test with specified preference type of the decision attribute on which classification is being made.
	 *  
	 * @param decisionAttributePreferenceType preference type {@link AttributePreferenceType} of decision attribute
	 */
	private void setUpForTestWithGainTypeDecisionAttribute(AttributePreferenceType decisionAttributePreferenceType) {
		notNull(decisionAttributePreferenceType, "Preference type of the decision attribute is null.");
		MockitoAnnotations.openMocks(this);
		
		// set decision domain
		try {
			this.domain = new ElementList(new String [] {"0", "1", "2", "3", "4", "5"});
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		// set decision values
		this.value1 = EnumerationFieldFactory.getInstance().create(domain, 1, decisionAttributePreferenceType);
		this.value2 = EnumerationFieldFactory.getInstance().create(domain, 2, decisionAttributePreferenceType);
		this.value3 = EnumerationFieldFactory.getInstance().create(domain, 3, decisionAttributePreferenceType);
		this.value5 = EnumerationFieldFactory.getInstance().create(domain, 5, decisionAttributePreferenceType);
		// set decision attribute
		this.decisionAttribute = new EvaluationAttributeWithContext(new EvaluationAttribute("dec", true, AttributeType.DECISION,
				this.value1, new UnknownSimpleFieldMV2(), decisionAttributePreferenceType), 10);
		
		// mock information table
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(4);
		// mock coverage of rule 1
		when(this.rule1Mock.covers(0, this.informationTableMock)).thenReturn(true);
		when(this.rule1Mock.covers(1, this.informationTableMock)).thenReturn(false);
		when(this.rule1Mock.covers(2, this.informationTableMock)).thenReturn(true);
		when(this.rule1Mock.covers(3, this.informationTableMock)).thenReturn(false);
		// mock decision part of rule 1
		doReturn(this.decision1Mock).when(this.rule1Mock).getDecision();
		when(decision1Mock.getLimitingEvaluation()).thenReturn(this.value2);
		when(decision1Mock.getAttributeWithContext()).thenReturn(this.decisionAttribute);
		// mock coverage of rule 2
		when(this.rule2Mock.covers(0, this.informationTableMock)).thenReturn(true);
		when(this.rule2Mock.covers(1, this.informationTableMock)).thenReturn(false);
		when(this.rule2Mock.covers(2, this.informationTableMock)).thenReturn(false);
		when(this.rule2Mock.covers(3, this.informationTableMock)).thenReturn(true);
		// mock decision part of rule 2
		doReturn(this.decision2Mock).when(this.rule2Mock).getDecision();
		when(decision2Mock.getLimitingEvaluation()).thenReturn(value3);
		when(decision2Mock.getAttributeWithContext()).thenReturn(this.decisionAttribute);
		// mock coverage of rule 3
		when(this.rule3Mock.covers(0, this.informationTableMock)).thenReturn(true);
		when(this.rule3Mock.covers(1, this.informationTableMock)).thenReturn(false);
		when(this.rule3Mock.covers(2, this.informationTableMock)).thenReturn(false);
		when(this.rule3Mock.covers(3, this.informationTableMock)).thenReturn(false);
		// mock decision part of rule 3
		doReturn(this.decision3Mock).when(this.rule3Mock).getDecision();
		when(decision3Mock.getLimitingEvaluation()).thenReturn(value1);
		when(decision3Mock.getAttributeWithContext()).thenReturn(this.decisionAttribute);
		// mock coverage of rule 4
		when(this.rule4Mock.covers(0, this.informationTableMock)).thenReturn(false);
		when(this.rule4Mock.covers(1, this.informationTableMock)).thenReturn(true);
		when(this.rule4Mock.covers(2, this.informationTableMock)).thenReturn(false);
		when(this.rule4Mock.covers(3, this.informationTableMock)).thenReturn(false);
		// mock decision part of rule 4
		doReturn(this.decision4Mock).when(this.rule4Mock).getDecision();
		when(decision4Mock.getLimitingEvaluation()).thenReturn(value2);
		when(decision4Mock.getAttributeWithContext()).thenReturn(this.decisionAttribute);
		// mock coverage of rule 5
		when(this.rule5Mock.covers(0, this.informationTableMock)).thenReturn(false);
		when(this.rule5Mock.covers(1, this.informationTableMock)).thenReturn(true);
		when(this.rule5Mock.covers(2, this.informationTableMock)).thenReturn(true);
		when(this.rule5Mock.covers(3, this.informationTableMock)).thenReturn(false);
		// mock decision part of rule 5
		doReturn(this.decision5Mock).when(this.rule5Mock).getDecision();
		when(decision5Mock.getLimitingEvaluation()).thenReturn(value5);
		when(decision5Mock.getAttributeWithContext()).thenReturn(this.decisionAttribute);
		// mock coverage of rule 6
		when(this.rule6Mock.covers(0, this.informationTableMock)).thenReturn(false);
		when(this.rule6Mock.covers(1, this.informationTableMock)).thenReturn(true);
		when(this.rule6Mock.covers(2, this.informationTableMock)).thenReturn(false);
		when(this.rule6Mock.covers(3, this.informationTableMock)).thenReturn(true);
		// mock decision part of rule 6
		doReturn(this.decision6Mock).when(this.rule6Mock).getDecision();
		when(decision6Mock.getLimitingEvaluation()).thenReturn(value1);
		when(decision6Mock.getAttributeWithContext()).thenReturn(this.decisionAttribute);
		// mock rule set
		when(this.ruleSetMock.size()).thenReturn(6);
		when(this.ruleSetMock.getRule(0)).thenReturn(rule1Mock);
		when(this.ruleSetMock.getRule(1)).thenReturn(rule2Mock);
		when(this.ruleSetMock.getRule(2)).thenReturn(rule3Mock);
		when(this.ruleSetMock.getRule(3)).thenReturn(rule4Mock);
		when(this.ruleSetMock.getRule(4)).thenReturn(rule5Mock);
		when(this.ruleSetMock.getRule(5)).thenReturn(rule6Mock);
		
		// set classifier
		this.classifier = new SimpleRuleClassifier(this.ruleSetMock, new SimpleClassificationResult(
				new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 2, this.decisionAttribute.getAttributePreferenceType()), 
				this.decisionAttribute.getAttributeIndex())));
	}
	
	/**
	 * Test for {@link SimpleRuleClassifier#classify(int, InformationTable)} when decision attribute is of gain type.
	 */
	@Test
	void testClassifyWithGainTypeDecisionAttribute() {
		this.setUpForTestWithGainTypeDecisionAttribute(AttributePreferenceType.GAIN);
		// case 1 (rules 1, 2, 3 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 3, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(0, this.informationTableMock).getSuggestedDecision());
		// case 2 (rules 4, 5, 6 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 1, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(1, this.informationTableMock).getSuggestedDecision());
		// case 3 (rules 1 and 5 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 3, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(2, this.informationTableMock).getSuggestedDecision());
		// case 4 (rules 2 and 6 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 2, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(3, this.informationTableMock).getSuggestedDecision());
	}
	
	/**
	 * Test for {@link SimpleRuleClassifier#classify(int, InformationTable)} when decision attribute is of cost type.
	 */
	@Test
	void testClassifyWithCostTypeDecisionAttribute() {
		this.setUpForTestWithGainTypeDecisionAttribute(AttributePreferenceType.COST);
		// case 1 (rules 1, 2, 3 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 3, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(0, this.informationTableMock).getSuggestedDecision());
		// case 2 (rules 4, 5, 6 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 1, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(1, this.informationTableMock).getSuggestedDecision());
		// case 3 (rules 1 and 5 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 3, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(2, this.informationTableMock).getSuggestedDecision());
		// case 4 (rules 2 and 6 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 2, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				this.classifier.classify(3, this.informationTableMock).getSuggestedDecision());
	}
	
	/**
	 * Test for {@link SimpleRuleClassifier#classifyAll(InformationTable) when decision attribute is of gain type.
	 */
	@Test
	void testClassifyAllWithGainTypeDecisionAttribute() {
		this.setUpForTestWithGainTypeDecisionAttribute(AttributePreferenceType.GAIN);
		
		SimpleClassificationResult [] results = this.classifier.classifyAll(informationTableMock);
		// case 1 (rules 1, 2, 3 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 3, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				results[0].getSuggestedDecision());
		// case 2 (rules 4, 5, 6 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 1, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				results[1].getSuggestedDecision());
		// case 3 (rules 1 and 5 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 3, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				results[2].getSuggestedDecision());
		// case 4 (rules 2 and 6 are covering object)
		assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(this.domain, 2, this.decisionAttribute.getAttributePreferenceType()), this.decisionAttribute.getAttributeIndex()),
				results[3].getSuggestedDecision());
	}
	
	/**
	 * Tests parsing RuleML file and using rules to classify objects loaded from JSON.
	 */
	@Test
	void testLoading() {
		Attribute [] attributes = null;
		AttributeParser attributeParser = new AttributeParser();
		try (FileReader attributeReader = new FileReader("src/test/resources/data/csv/prioritisation.json")) {
			attributes = attributeParser.parseAttributes(attributeReader);
			if (attributes != null) {
				Map<Integer, RuleSet> rules = null;
				RuleParser ruleParser = new RuleParser(attributes);
				try (FileInputStream fileRulesStream = new FileInputStream("src/test/resources/data/ruleml/prioritisation2.rules.xml")) {
					rules = ruleParser.parseRules(fileRulesStream);
					if ((rules != null) & (rules.size() > 0)) {
						ObjectParser objectParser = new ObjectParser.Builder(attributes).build();
						InformationTable informationTable = null;
						try (FileReader objectReader = new FileReader("src/test/resources/data/json/examples.json")) {
							informationTable = objectParser.parseObjects(objectReader);
							SimpleRuleClassifier classifier = new SimpleRuleClassifier(rules.get(1), new SimpleClassificationResult(
										new SimpleDecision(EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 2, 
																			AttributePreferenceType.COST), 10)));
							//System.out.println(classifier.classify(0, iT).getSuggestedDecision().getEvaluation());
							assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 1, 
									AttributePreferenceType.COST), 10), classifier.classify(0, informationTable).getSuggestedDecision());
							//System.out.println(classifier.classify(1, iT).getSuggestedDecision().getEvaluation());
							assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 2, 
									AttributePreferenceType.COST), 10), classifier.classify(1, informationTable).getSuggestedDecision());
						}
					}
					else {
						fail("Unable to load RuleML file.");
					}
				}
			}
			else {
				fail("Unable to load JSON file with meta-data.");
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
}
