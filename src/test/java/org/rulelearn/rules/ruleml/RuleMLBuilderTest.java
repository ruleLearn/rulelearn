package org.rulelearn.rules.ruleml;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

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
import org.rulelearn.rules.RuleSemantics;
import org.rulelearn.rules.RuleSet;
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
	private SimpleConditionAtLeast conditionAtLeast;
	@Mock
	private SimpleConditionAtMost conditionAtMost;
	@Mock
	private SimpleConditionEqual conditionEqual;
	@Mock
	private SimpleConditionAtLeast decisionAtLeast;
	@Mock
	private SimpleConditionAtMost decisionAtMost;
	
	@Mock
	private Rule rule;
	
	private String conditionAtLeastRuleML;
	private String conditionAtMostRuleML;
	private String conditionEqualRuleML;
	private String decisionAtLeastRuleML;
	private String decisionAtMostRuleML;

	/**
	 * Set up for each test.
	 */
	@BeforeEach
	public void setUp () {
		this.ruleMLBuilder = new RuleMLBuilder();
		MockitoAnnotations.initMocks(this);
		
		// set mock for condition at least
		when(this.conditionAtLeast.getRuleSemantics()).thenReturn(RuleSemantics.AT_LEAST);
		when(this.conditionAtLeast.getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(3, AttributePreferenceType.GAIN));
		when(this.conditionAtLeast.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
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
		when(this.conditionAtMost.getRuleSemantics()).thenReturn(RuleSemantics.AT_MOST);
		when(this.conditionAtMost.getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(2, AttributePreferenceType.COST));
		when(this.conditionAtMost.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
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
		when(this.conditionEqual.getRuleSemantics()).thenReturn(RuleSemantics.EQUAL);
		when(this.conditionEqual.getLimitingEvaluation()).thenReturn(IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE));
		when(this.conditionEqual.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
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
		when(this.decisionAtLeast.getRuleSemantics()).thenReturn(RuleSemantics.AT_LEAST);
		try {
			when(this.decisionAtLeast.getLimitingEvaluation()).thenReturn(EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
					1, AttributePreferenceType.GAIN));
			when(this.decisionAtLeast.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
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
		when(this.decisionAtMost.getRuleSemantics()).thenReturn(RuleSemantics.AT_MOST);
		try {
			when(this.decisionAtMost.getLimitingEvaluation()).thenReturn(EnumerationFieldFactory.getInstance().create(new ElementList(labels), 
					0, AttributePreferenceType.COST));
			when(this.decisionAtMost.getAttributeWithContext()).thenReturn(new EvaluationAttributeWithContext(
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
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(org.rulelearn.rules.Condition, int)}.
	 */
	@Test
	void testRuleMLBuilderCondition() {
		//System.out.println(ruleMLBuilder.toRuleMLString(this.conditionAtLeast, 0));
		assertEquals(ruleMLBuilder.toRuleMLString(this.conditionAtLeast, 0), this.conditionAtLeastRuleML);
		assertEquals(ruleMLBuilder.toRuleMLString(this.conditionAtMost, 0), this.conditionAtMostRuleML);
		assertEquals(ruleMLBuilder.toRuleMLString(this.conditionEqual, 0), this.conditionEqualRuleML);
		//System.out.println(ruleMLBuilder.toRuleMLString(this.decisionAtLeast, 0));
		assertEquals(ruleMLBuilder.toRuleMLString(this.decisionAtLeast, 0), this.decisionAtLeastRuleML);
		assertEquals(ruleMLBuilder.toRuleMLString(this.decisionAtMost, 0), this.decisionAtMostRuleML);
	}
	
	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(org.rulelearn.rules.Rule)}.
	 */
	@Test
	void testRuleMLBuilderRule1() {
		Condition<? extends EvaluationField>[] conditions = new Condition<?>[] {this.conditionAtLeast};
		Condition<? extends EvaluationField>[] decision = new Condition<?>[] {this.decisionAtLeast};
		
		when(this.rule.getConditions()).thenReturn(conditions);
		when(this.rule.getDecisions()).thenReturn(decision);
		
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
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>ge</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>b</ind>\n" + 
				"\t\t\t\t\t<var>gd1</var>\n" + 
				"\t\t\t\t</atom>\n" + 
				"\t\t</then>\n" + 
				"\t</implies>\n" + 
				"</assert>\n";
		
		//System.out.println(ruleMLBuilder.toRuleMLString(this.rule));
		assertEquals(ruleMLBuilder.toRuleMLString(this.rule), ruleRuleML);
	}

	/**
	 * Test method for {@link org.rulelearn.rules.ruleml.RuleMLBuilder#toRuleMLString(org.rulelearn.rules.Rule)}.
	 */
	@Test
	void testRuleMLBuilderRule2() {
		Condition<? extends EvaluationField>[] conditions = new Condition<?>[] {this.conditionAtMost, this.conditionEqual};
		Condition<? extends EvaluationField>[] decision = new Condition<?>[] {this.decisionAtMost};
		
		when(this.rule.getConditions()).thenReturn(conditions);
		when(this.rule.getDecisions()).thenReturn(decision);
		
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
				"\t\t\t\t<atom>\n" + 
				"\t\t\t\t<op>\n" + 
				"\t\t\t\t\t<rel>le</rel>\n" + 
				"\t\t\t\t</op>\n" + 
				"\t\t\t\t<ind>a</ind>\n" + 
				"\t\t\t\t\t<var>cd1</var>\n" + 
				"\t\t\t\t</atom>\n" + 
				"\t\t</then>\n" + 
				"\t</implies>\n" + 
				"</assert>\n";
		
		//System.out.println(ruleMLBuilder.toRuleMLString(this.rule));
		assertEquals(ruleMLBuilder.toRuleMLString(this.rule), ruleRuleML);
	}
}
