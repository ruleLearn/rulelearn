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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
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
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link RuleMLBuilder}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleMLBuilderTest {
	
	RuleMLBuilder ruleMLBuilder = null;
	
	@Mock
    private RuleSet ruleSetMock;
	
	@Mock
	private SimpleConditionAtLeast conditionAtLeastMock;
	@Mock
	private SimpleConditionAtMost conditionAtMostMock;
	@Mock
	private SimpleConditionEqual conditionEqualMock;
	@Mock
	private SimpleConditionAtLeast decisionAtLeastMock;
	@Mock
	private SimpleConditionAtMost decisionAtMostMock;
	
	@Mock
	private Rule ruleAtLeastMock;
	@Mock
	private Rule ruleAtMostMock;
	
	@Mock
	private RuleCharacteristics ruleCharacteristicsMock;
	
	@Mock
	private RuleSetWithCharacteristics ruleSetWithCharacteristicsMock;  
	
	private String conditionAtLeastRuleML;
	private String conditionAtMostRuleML;
	private String conditionEqualRuleML;
	private String decisionAtLeastRuleML;
	private String decisionAtMostRuleML;
	
	private String ruleCharacteristicsRuleML;

	/**
	 * Set up for each test.
	 */
	@BeforeEach
	public void setUp () {
		this.ruleMLBuilder = new RuleMLBuilder();
		MockitoAnnotations.initMocks(this);
		
		// set mock for condition at least
		when(this.conditionAtLeastMock.getRuleSemantics()).thenReturn(RuleSemantics.AT_LEAST);
		when(this.conditionAtLeastMock.getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		when(this.conditionAtLeastMock.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
				new EvaluationAttribute(
						"gc1",
						true,
						AttributeType.CONDITION,
						IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN),
						new UnknownSimpleFieldMV2(),
						AttributePreferenceType.GAIN),
				1));
		this.conditionAtLeastRuleML = "<atom>\n" + 
				"<op>\n" + 
				"\t<rel>ge</rel>\n" + 
				"</op>\n" + 
				"<ind>3</ind>\n" + 
				"\t<var>gc1</var>\n" + 
				"</atom>\n";
		
		// set mock for condition at most
		when(this.conditionAtMostMock.getRuleSemantics()).thenReturn(RuleSemantics.AT_MOST);
		when(this.conditionAtMostMock.getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST));
		when(this.conditionAtMostMock.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
				new EvaluationAttribute(
						"cc1",
						true,
						AttributeType.CONDITION,
						RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.COST),
						new UnknownSimpleFieldMV2(),
						AttributePreferenceType.COST),
				1));
		this.conditionAtMostRuleML = "<atom>\n" + 
				"<op>\n" + 
				"\t<rel>le</rel>\n" + 
				"</op>\n" + 
				"<ind>2</ind>\n" + 
				"\t<var>cc1</var>\n" + 
				"</atom>\n";
		
		// set mock for condition equal
		when(this.conditionEqualMock.getRuleSemantics()).thenReturn(RuleSemantics.EQUAL);
		when(this.conditionEqualMock.getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE));
		when(this.conditionEqualMock.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
				new EvaluationAttribute(
						"a1",
						true,
						AttributeType.CONDITION,
						IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.NONE),
						new UnknownSimpleFieldMV2(),
						AttributePreferenceType.NONE),
				1));
		this.conditionEqualRuleML = "<atom>\n" + 
				"<op>\n" + 
				"\t<rel>eq</rel>\n" + 
				"</op>\n" + 
				"<ind>1</ind>\n" + 
				"\t<var>a1</var>\n" + 
				"</atom>\n";

		String [] labels = {"a", "b", "c"};
		
		// set mock for decision at least condition
		when(this.decisionAtLeastMock.getRuleSemantics()).thenReturn(RuleSemantics.AT_LEAST);
		try {
			when(this.decisionAtLeastMock.getLimitingEvaluation()).thenReturn(EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
					1, AttributePreferenceType.GAIN));
			when(this.decisionAtLeastMock.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
					new EvaluationAttribute(
							"gd1",
							true,
							AttributeType.DECISION,
							EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
									EnumerationField.DEFAULT_VALUE, AttributePreferenceType.GAIN),
							new UnknownSimpleFieldMV2(),
							AttributePreferenceType.GAIN),
					1));
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		this.decisionAtLeastRuleML = "<atom>\n" + 
				"<op>\n" + 
				"\t<rel>ge</rel>\n" + 
				"</op>\n" + 
				"<ind>b</ind>\n" + 
				"\t<var>gd1</var>\n" + 
				"</atom>\n";
		
		// set mock for decision at least condition
		when(this.decisionAtMostMock.getRuleSemantics()).thenReturn(RuleSemantics.AT_MOST);
		try {
			when(this.decisionAtMostMock.getLimitingEvaluation()).thenReturn(EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
					0, AttributePreferenceType.COST));
			when(this.decisionAtMostMock.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
					new EvaluationAttribute(
							"cd1",
							true,
							AttributeType.DECISION,
							EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
									EnumerationField.DEFAULT_VALUE, AttributePreferenceType.COST),
							new UnknownSimpleFieldMV2(),
							AttributePreferenceType.COST),
					1));
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		this.decisionAtMostRuleML = "<atom>\n" + 
				"<op>\n" + 
				"\t<rel>le</rel>\n" + 
				"</op>\n" + 
				"<ind>a</ind>\n" + 
				"\t<var>cd1</var>\n" + 
				"</atom>\n";
		
		// set mock for rule characteristics
		when(this.ruleCharacteristicsMock.getAConfirmation()).thenReturn(1.0);
		when(this.ruleCharacteristicsMock.getC1Confirmation()).thenReturn(-1.0);
		when(this.ruleCharacteristicsMock.getConfidence()).thenReturn(1.0);
		when(this.ruleCharacteristicsMock.getCoverage()).thenReturn(1);
		when(this.ruleCharacteristicsMock.getEpsilon()).thenReturn(-1.0);
		when(this.ruleCharacteristicsMock.getEpsilonPrime()).thenReturn(1.0);
		when(this.ruleCharacteristicsMock.getFConfirmation()).thenReturn(1.0);
		when(this.ruleCharacteristicsMock.getLConfirmation()).thenReturn(1.0);
		when(this.ruleCharacteristicsMock.getNegativeCoverage()).thenReturn(1);
		when(this.ruleCharacteristicsMock.getSConfirmation()).thenReturn(-1.0);
		when(this.ruleCharacteristicsMock.getStrength()).thenReturn(1.0);
		when(this.ruleCharacteristicsMock.getSupport()).thenReturn(1);
		when(this.ruleCharacteristicsMock.getZConfirmation()).thenReturn(-1.0);
		this.ruleCharacteristicsRuleML = "<evaluation measure=\"Support\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"Strength\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"Confidence\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"CoverageFactor\" value=\"0.0\"/>\n" + 
				"<evaluation measure=\"Coverage\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"NegativeCoverage\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"InconsistencyMeasure\" value=\"-1.0\"/>\n" + 
				"<evaluation measure=\"EpsilonPrimMeasure\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"f-ConfirmationMeasure\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"A-ConfirmationMeasure\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"Z-ConfirmationMeasure\" value=\"-1.0\"/>\n" + 
				"<evaluation measure=\"l-ConfirmationMeasure\" value=\"1.0\"/>\n" + 
				"<evaluation measure=\"c1-ConfirmationMeasure\" value=\"-1.0\"/>\n" + 
				"<evaluation measure=\"s-ConfirmationMeasure\" value=\"-1.0\"/>\n";	
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(org.rulelearn.rules.Condition, int)}.
	 */
	@Test
	void testRuleMLBuilderCondition() {
		//System.out.println(ruleMLBuilder.toRuleMLString(this.conditionAtLeast, 0));
		assertEquals(this.conditionAtLeastRuleML, ruleMLBuilder.toRuleMLString(this.conditionAtLeastMock, 0));
		assertEquals(this.conditionAtMostRuleML, ruleMLBuilder.toRuleMLString(this.conditionAtMostMock, 0));
		assertEquals(this.conditionEqualRuleML, ruleMLBuilder.toRuleMLString(this.conditionEqualMock, 0));
		//System.out.println(ruleMLBuilder.toRuleMLString(this.decisionAtLeast, 0));
		assertEquals(this.decisionAtLeastRuleML, ruleMLBuilder.toRuleMLString(this.decisionAtLeastMock, 0));
		assertEquals(this.decisionAtMostRuleML, ruleMLBuilder.toRuleMLString(this.decisionAtMostMock, 0));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(RuleCharacteristics, int)}.
	 */
	@Test
	void testRuleMLBuilderRuleCharacteristics() {
		//System.out.println(ruleMLBuilder.toRuleMLString(this.ruleCharacteristicsMock, 0));
		assertEquals(this.ruleCharacteristicsRuleML, ruleMLBuilder.toRuleMLString(this.ruleCharacteristicsMock, 0));
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(org.rulelearn.rules.Rule)}.
	 */
	@Test
	void testRuleMLBuilderRule1() {
		Condition<? extends EvaluationField>[] conditions = new Condition<?>[] {this.conditionAtLeastMock};
		Condition<? extends EvaluationField>[][] decisions = new Condition<?>[][] {{this.decisionAtLeastMock}};
		
		when(this.ruleAtLeastMock.getConditions()).thenReturn(conditions);
		when(this.ruleAtLeastMock.getDecisions()).thenReturn(decisions);
		when(this.ruleAtLeastMock.getSemantics()).thenReturn(RuleSemantics.AT_LEAST);
		when(this.ruleAtLeastMock.getType()).thenReturn(RuleType.CERTAIN);
		
		String ruleRuleML = "<assert>\n" + 
				"\t<implies>\n" + 
				"\t\t<if>\n" + 
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>ge</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>3</ind>\n" + 
				"\t\t\t\t\t<var>gc1</var>\n" + 
				"\t\t\t\t</atom>\n" + 
				"\t\t</if>\n" + 
				"\t\t<then>\n" + 
				"\t\t\t\t\t<atom>\n" + 
				"\t\t\t\t\t<op>\n" + 
				"\t\t\t\t\t\t<rel>ge</rel>\n" + 
				"\t\t\t\t\t</op>\n" + 
				"\t\t\t\t\t<ind>b</ind>\n" + 
				"\t\t\t\t\t\t<var>gd1</var>\n" + 
				"\t\t\t\t\t</atom>\n" + 
				"\t\t</then>\n" +
				"\t\t<ruleSemantics>ge</ruleSemantics>\n" +
				"\t\t<ruleType>certain</ruleType>\n" +
				"\t</implies>\n" + 
				"</assert>\n";
		
		//System.out.println(ruleMLBuilder.toRuleMLString(this.rule));
		assertEquals(ruleRuleML, ruleMLBuilder.toRuleMLString(this.ruleAtLeastMock));
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(org.rulelearn.rules.Rule)}.
	 */
	@Test
	void testRuleMLBuilderRule2() {
		Condition<? extends EvaluationField>[] conditions = new Condition<?>[] {this.conditionAtMostMock, this.conditionEqualMock};
		Condition<? extends EvaluationField>[][] decisions = new Condition<?>[][] {{this.decisionAtMostMock}};
		
		when(this.ruleAtMostMock.getConditions()).thenReturn(conditions);
		when(this.ruleAtMostMock.getDecisions()).thenReturn(decisions);
		when(this.ruleAtMostMock.getSemantics()).thenReturn(RuleSemantics.AT_MOST);
		when(this.ruleAtMostMock.getType()).thenReturn(RuleType.POSSIBLE);
		
		String ruleRuleML = "<assert>\n" + 
				"\t<implies>\n" + 
				"\t\t<if>\n" +
				"\t\t\t<and>\n" +
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>le</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>2</ind>\n" + 
				"\t\t\t\t\t<var>cc1</var>\n" + 
				"\t\t\t\t</atom>\n" +
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>eq</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>1</ind>\n" + 
				"\t\t\t\t\t<var>a1</var>\n" + 
				"\t\t\t\t</atom>\n" +
				"\t\t\t</and>\n" +
				"\t\t</if>\n" + 
				"\t\t<then>\n" + 
				"\t\t\t\t\t<atom>\n" + 
				"\t\t\t\t\t<op>\n" + 
				"\t\t\t\t\t\t<rel>le</rel>\n" + 
				"\t\t\t\t\t</op>\n" + 
				"\t\t\t\t\t<ind>a</ind>\n" + 
				"\t\t\t\t\t\t<var>cd1</var>\n" + 
				"\t\t\t\t\t</atom>\n" + 
				"\t\t</then>\n" +
				"\t\t<ruleSemantics>le</ruleSemantics>\n" +
				"\t\t<ruleType>possible</ruleType>\n" +
				"\t</implies>\n" + 
				"</assert>\n";
		
		//System.out.println(ruleMLBuilder.toRuleMLString(this.rule));
		assertEquals(ruleMLBuilder.toRuleMLString(this.ruleAtMostMock), ruleRuleML);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(RuleSet, int)}.
	 */
	@Test
	void testRuleMLBuilderRuleSet1() {
		// first rule
		Condition<? extends EvaluationField>[] conditions = new Condition<?>[] {this.conditionAtLeastMock};
		Condition<? extends EvaluationField>[][] decisions = new Condition<?>[][] {{this.decisionAtLeastMock}};
		
		when(this.ruleAtLeastMock.getConditions()).thenReturn(conditions);
		when(this.ruleAtLeastMock.getDecisions()).thenReturn(decisions);
		when(this.ruleAtLeastMock.getSemantics()).thenReturn(RuleSemantics.AT_LEAST);
		when(this.ruleAtLeastMock.getType()).thenReturn(RuleType.CERTAIN);
		
		// second rule
		conditions = new Condition<?>[] {this.conditionAtMostMock, this.conditionEqualMock};
		decisions = new Condition<?>[][] {{this.decisionAtMostMock}};
		
		when(this.ruleAtMostMock.getConditions()).thenReturn(conditions);
		when(this.ruleAtMostMock.getDecisions()).thenReturn(decisions);
		when(this.ruleAtMostMock.getSemantics()).thenReturn(RuleSemantics.AT_MOST);
		when(this.ruleAtMostMock.getType()).thenReturn(RuleType.CERTAIN);
		
		when(this.ruleSetMock.getRule(0)).thenReturn(this.ruleAtLeastMock);
		when(this.ruleSetMock.getRule(1)).thenReturn(this.ruleAtMostMock);
		when(this.ruleSetMock.size()).thenReturn(2);
		
		String ruleSetRuleML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<?xml-model href=\"http://deliberation.ruleml.org/1.01/relaxng/datalogplus_min_relaxed.rnc\"?>\n" + 
				"<RuleML xmlns=\"http://ruleml.org/spec\">\n" + 
				"<act index=\"1\">\n" + 
				"<assert>\n" + 
				"\t<implies>\n" + 
				"\t\t<if>\n" + 
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>ge</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>3</ind>\n" + 
				"\t\t\t\t\t<var>gc1</var>\n" + 
				"\t\t\t\t</atom>\n" + 
				"\t\t</if>\n" + 
				"\t\t<then>\n" + 
				"\t\t\t\t\t<atom>\n" + 
				"\t\t\t\t\t<op>\n" + 
				"\t\t\t\t\t\t<rel>ge</rel>\n" + 
				"\t\t\t\t\t</op>\n" + 
				"\t\t\t\t\t<ind>b</ind>\n" + 
				"\t\t\t\t\t\t<var>gd1</var>\n" + 
				"\t\t\t\t\t</atom>\n" + 
				"\t\t</then>\n" + 
				"\t\t<ruleSemantics>ge</ruleSemantics>\n" +
				"\t\t<ruleType>certain</ruleType>\n" +
				"\t</implies>\n" + 
				"</assert>\n" +
				"<assert>\n" + 
				"\t<implies>\n" + 
				"\t\t<if>\n" +
				"\t\t\t<and>\n" +
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>le</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>2</ind>\n" + 
				"\t\t\t\t\t<var>cc1</var>\n" + 
				"\t\t\t\t</atom>\n" +
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>eq</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>1</ind>\n" + 
				"\t\t\t\t\t<var>a1</var>\n" + 
				"\t\t\t\t</atom>\n" +
				"\t\t\t</and>\n" +
				"\t\t</if>\n" + 
				"\t\t<then>\n" + 
				"\t\t\t\t\t<atom>\n" + 
				"\t\t\t\t\t<op>\n" + 
				"\t\t\t\t\t\t<rel>le</rel>\n" + 
				"\t\t\t\t\t</op>\n" + 
				"\t\t\t\t\t<ind>a</ind>\n" + 
				"\t\t\t\t\t\t<var>cd1</var>\n" + 
				"\t\t\t\t\t</atom>\n" + 
				"\t\t</then>\n" +
				"\t\t<ruleSemantics>le</ruleSemantics>\n" +
				"\t\t<ruleType>certain</ruleType>\n" +
				"\t</implies>\n" + 
				"</assert>\n" +
				"</act>\n" + 
				"</RuleML>\n";
		
		//System.out.println(ruleMLBuilder.toRuleMLString(this.ruleSetMock, 1));
		assertEquals(ruleMLBuilder.toRuleMLString(this.ruleSetMock, 1), ruleSetRuleML);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(RuleSet, UUID)}.
	 */
	@Test
	void testRuleMLBuilderRuleSet2() {
		Condition<? extends EvaluationField>[] conditions = new Condition<?>[] {this.conditionAtLeastMock};
		Condition<? extends EvaluationField>[][] decisions = new Condition<?>[][] {{this.decisionAtLeastMock}};
		
		when(this.ruleAtLeastMock.getConditions()).thenReturn(conditions);
		when(this.ruleAtLeastMock.getDecisions()).thenReturn(decisions);
		when(this.ruleAtLeastMock.getSemantics()).thenReturn(RuleSemantics.AT_LEAST);
		when(this.ruleAtLeastMock.getType()).thenReturn(RuleType.APPROXIMATE);
				
		when(this.ruleSetWithCharacteristicsMock.getRule(0)).thenReturn(this.ruleAtLeastMock);
		when(this.ruleSetWithCharacteristicsMock.getRuleCharacteristics(0)).thenReturn(this.ruleCharacteristicsMock);
		when(this.ruleSetWithCharacteristicsMock.size()).thenReturn(1);
		
		String ruleSetRuleML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<?xml-model href=\"http://deliberation.ruleml.org/1.01/relaxng/datalogplus_min_relaxed.rnc\"?>\n" + 
				"<RuleML xmlns=\"http://ruleml.org/spec\">\n" + 
				"<act index=\"00000000-0000-0000-0000-000000000001\">\n" + 
				"<assert>\n" + 
				"\t<implies>\n" + 
				"\t\t<if>\n" + 
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>ge</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>3</ind>\n" + 
				"\t\t\t\t\t<var>gc1</var>\n" + 
				"\t\t\t\t</atom>\n" + 
				"\t\t</if>\n" + 
				"\t\t<then>\n" + 
				"\t\t\t\t\t<atom>\n" + 
				"\t\t\t\t\t<op>\n" + 
				"\t\t\t\t\t\t<rel>ge</rel>\n" + 
				"\t\t\t\t\t</op>\n" + 
				"\t\t\t\t\t<ind>b</ind>\n" + 
				"\t\t\t\t\t\t<var>gd1</var>\n" + 
				"\t\t\t\t\t</atom>\n" + 
				"\t\t</then>\n" + 
				"\t\t<ruleSemantics>ge</ruleSemantics>\n" +
				"\t\t<ruleType>approximate</ruleType>\n" +
				"\t\t<evaluations>\n" + 
				"\t\t\t<evaluation measure=\"Support\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"Strength\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"Confidence\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"CoverageFactor\" value=\"0.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"Coverage\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"NegativeCoverage\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"InconsistencyMeasure\" value=\"-1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"EpsilonPrimMeasure\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"f-ConfirmationMeasure\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"A-ConfirmationMeasure\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"Z-ConfirmationMeasure\" value=\"-1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"l-ConfirmationMeasure\" value=\"1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"c1-ConfirmationMeasure\" value=\"-1.0\"/>\n" + 
				"\t\t\t<evaluation measure=\"s-ConfirmationMeasure\" value=\"-1.0\"/>\n" + 
				"\t\t</evaluations>\n" +
				"\t</implies>\n" + 
				"</assert>\n" +
				"</act>\n" + 
				"</RuleML>\n";
		
		//System.out.println(ruleMLBuilder.toRuleMLString(this.ruleSetWithCharacteristicsMock, new UUID(0, 1)));
		assertEquals(ruleSetRuleML, ruleMLBuilder.toRuleMLString(this.ruleSetWithCharacteristicsMock, new UUID(0, 1)));
	}
		
}
