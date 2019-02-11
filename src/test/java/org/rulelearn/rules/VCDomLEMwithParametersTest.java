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

package org.rulelearn.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rulelearn.approximations.ClassicalDominanceBasedRoughSetCalculator;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Unions;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTableTestConfiguration;
import org.rulelearn.data.InformationTableWithDecisionDistributions;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Integration tests for VCDomLEM algorithm parameterized with {@link VCDomLEMParameters}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
@Tag("integration")
class VCDomLEMWithParametersTest {

	/**
	 * Tests upward unions and certain rules.
	 */
	@Test
	public void testUpwardUnionCertain() {
		VCDomLEMParameters vcDomLEMParameters = (new VCDomLEMParameters.VCDomLEMParametersBuilder()).build();
		
		InformationTableTestConfiguration informationTableTestConfiguration = new InformationTableTestConfiguration (
				new Attribute[] {
						new IdentificationAttribute("bus", true, new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE)),
						new EvaluationAttribute("symptom1", true, AttributeType.CONDITION,
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN),
						new EvaluationAttribute("symptom2", true, AttributeType.CONDITION,
								RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN),
						new EvaluationAttribute("state", true, AttributeType.DECISION,
								IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, AttributePreferenceType.GAIN), new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN)
					},
				new String[][] {
						{ "a", "40",   "17.8", "2"},
						{ "b", "35",   "30",   "2"},
						{ "c", "32.5", "39",   "2"},
						{ "d", "31",   "35",   "2"},
						{ "e", "27.5", "17.5", "2"},
						{ "f", "24",   "17.5", "2"},
						{ "g", "22.5", "20",   "2"},
						{ "h", "30.8", "19",   "1"},
						{ "i", "27",   "25",   "1"},
						{ "j", "21",   "9.5",  "1"},
						{ "k", "18",   "12.5", "1"},
						{ "l", "10.5", "25.5", "1"},
						{ "m", "9.75", "17",   "1"},
						{ "n", "17.5", "5",    "0"},
						{ "o", "11",   "2",    "0"},
						{ "p", "10",   "9",    "0"},
						{ "q", "5",    "13",   "0"}
				});
		
		InformationTableWithDecisionDistributions informationTable = new InformationTableWithDecisionDistributions(
				informationTableTestConfiguration.getAttributes(),
				informationTableTestConfiguration.getListOfFields(),
				true); //TODO: use copy constructor from develop branch
		
		ApproximatedSetProvider approximatedSetProvider = new UnionProvider(Union.UnionType.AT_LEAST, new Unions(informationTable, new ClassicalDominanceBasedRoughSetCalculator()));
		ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider = new UnionRuleDecisionsProvider();
		
		RuleSet ruleSet = (new VCDomLEM(vcDomLEMParameters)).generateRules(approximatedSetProvider, approximatedSetRuleDecisionsProvider);
		
		assertEquals(ruleSet.size(), 3);
		
		for (int i = 0; i < ruleSet.size(); i++) {
			System.out.println(ruleSet.getRule(i));
		}
		
		//expected output:
		//(symptom1 >= 31.0) => (state >= 2)
		//(symptom1 >= 18.0) => (state >= 1)
		//(symptom2 >= 17.0) => (state >= 1)
	}
	
}
