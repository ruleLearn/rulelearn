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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.rulelearn.core.Precondition.notNull;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.SimpleDecision;
import org.rulelearn.rules.ConditionAtLeast;
import org.rulelearn.rules.ConditionAtMost;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleSet;
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
		MockitoAnnotations.initMocks(this);
		
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
	 * Test for {@link SimpleRuleClassifier#classify(int, InformationTable)} when decision attribute is of gain type.
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
	 * Tests parsing RuleML file and using rules to classify objects loaded from JSON.
	 */
	/*@Test
	void testLoading() {
		Attribute [] attributes = null;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		gsonBuilder.registerTypeAdapter(IdentificationAttribute.class, new IdentificationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		Gson gson = gsonBuilder.create();
		
		JsonReader jsonReader = null;
		try {
			jsonReader = new JsonReader(new FileReader("src/test/resources/data/csv/prioritisation.json"));
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		}
		
		Map<Integer, RuleSet> rules = null;
		if (jsonReader != null) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
			try {
				jsonReader.close();
				jsonReader = null;
			}
			catch (IOException ex){
				System.out.println(ex.toString());
			}
			
			RuleParser ruleParser = new RuleParser(attributes);
			try {
				rules = ruleParser.parseRules(new FileInputStream("src/test/resources/data/ruleml/prioritisation2.rules.xml"));
			}
			catch (FileNotFoundException ex) {
				System.out.println(ex.toString());
			}
			if ((rules != null) & (rules.size() > 0)) {
				JsonElement json = null;
				try {
					jsonReader = new JsonReader(new FileReader("src/test/resources/data/json/examples.json"));
				}
				catch (FileNotFoundException ex) {
					System.out.println(ex.toString());
				}
				if (jsonReader != null) {
					JsonParser jsonParser = new JsonParser();
					json = jsonParser.parse(jsonReader);
				}
				else {
					fail("Unable to load JSON test file with definition of objects");
				}
				try {
					jsonReader.close();
				}
				catch (IOException ex){
					System.out.println(ex.toString());
				}
				
				ObjectBuilder ob = new ObjectBuilder(attributes);
				List<String []> objects = null;
				objects = ob.getObjects(json);
				
				InformationTableBuilder iTB = new InformationTableBuilder(attributes, ",", new String[] {"?"});
				for (int i = 0; i < objects.size(); i++) {
					iTB.addObject(objects.get(i));
				}
				InformationTable iT = iTB.build();
				
				SimpleRuleClassifier classifier = new SimpleRuleClassifier(rules.get(1), new SimpleClassificationResult(
						new SimpleDecision(EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 2, 
																AttributePreferenceType.COST), 10)));
				//System.out.println(classifier.classify(0, iT).getSuggestedDecision().getEvaluation());
				assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 1, 
						AttributePreferenceType.COST), 10), classifier.classify(0, iT).getSuggestedDecision());
				//System.out.println(classifier.classify(1, iT).getSuggestedDecision().getEvaluation());
				assertEquals(new SimpleDecision(EnumerationFieldFactory.getInstance().create(((EnumerationField)attributes[10].getValueType()).getElementList(), 2, 
						AttributePreferenceType.COST), 10), classifier.classify(1, iT).getSuggestedDecision());
			}
			else {
				fail("Unable to load RuleML file.");
			}
		}
		else {
			fail("Unable to load JSON file with attributes.");
		}
	}*/
	
}
