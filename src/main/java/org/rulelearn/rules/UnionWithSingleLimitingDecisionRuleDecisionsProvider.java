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

import java.util.List;

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.UnionWithSingleLimitingDecision;
import org.rulelearn.data.Decision;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.EvaluationAttributeWithContext;
import org.rulelearn.data.InformationTable;
import org.rulelearn.rules.Rule.RuleDecisions;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Provider of rule decisions {@link Rule.RuleDecisions} that can be put on the right-hand side of a certain/possible decision rule
 * built to describe objects from a union of decisions classes {@link UnionWithSingleLimitingDecision}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnionWithSingleLimitingDecisionRuleDecisionsProvider extends UnionRuleDecisionsProvider {

	/**
	 * Gets rule decisions that can be put on the right-hand side of a certain/possible decision rule describing objects from the given union.
	 * 
	 * @param union union for which certain/possible decision rule is built and thus, rule decisions need to be obtained
	 * @return rule decisions that can be put on the right-hand side of a certain/possible decision rule describing objects from the given union
	 * 
	 * @throws ClassCastException if given approximated set is not of type {@link UnionWithSingleLimitingDecision}
	 */
	@Override
	public RuleDecisions getRuleDecisions(ApproximatedSet union) {
		if (!(union instanceof UnionWithSingleLimitingDecision)) {
			throw new ClassCastException("Cannot cast ApproximatedSet to UnionWithSingleLimitingDecision.");
		}
		
		UnionWithSingleLimitingDecision aUnion = (UnionWithSingleLimitingDecision)union;
		
		Decision limitingDecision = aUnion.getLimitingDecision();
		InformationTable informationTable = aUnion.getInformationTable();
		Union.UnionType unionType = aUnion.getUnionType();
		
		IntSet attributeIndices = limitingDecision.getAttributeIndices();
		EvaluationField evaluation;
		EvaluationAttribute attribute;
		int attributeIndex;
		
		List<Condition<? extends EvaluationField>> elementaryDecisionsList = new ObjectArrayList<>();
		
		IntIterator attributeIndicesIterator = attributeIndices.iterator();
		
		while (attributeIndicesIterator.hasNext()) {
			attributeIndex = attributeIndicesIterator.nextInt();
			attribute = (EvaluationAttribute)informationTable.getAttribute(attributeIndex); //this cast should not throw an exception since type of attributes contributing to limiting decision is checked in union class constructor
			evaluation = limitingDecision.getEvaluation(attributeIndex);
			
			switch (unionType) {
			case AT_LEAST:
				switch (attribute.getPreferenceType()) {
				case GAIN:
					elementaryDecisionsList.add(new ConditionAtLeastThresholdVSObject<EvaluationField>(
							new EvaluationAttributeWithContext(attribute, attributeIndex), evaluation));
					break;
				case COST:
					elementaryDecisionsList.add(new ConditionAtMostThresholdVSObject<EvaluationField>(
							new EvaluationAttributeWithContext(attribute, attributeIndex), evaluation));
					break;
				case NONE:
					elementaryDecisionsList.add(new ConditionEqualThresholdVSObject<EvaluationField>(
							new EvaluationAttributeWithContext(attribute, attributeIndex), evaluation));
					break;
				}
				break;
			case AT_MOST:
				switch (attribute.getPreferenceType()) {
				case GAIN:
					elementaryDecisionsList.add(new ConditionAtMostThresholdVSObject<EvaluationField>(
							new EvaluationAttributeWithContext(attribute, attributeIndex), evaluation));
					break;
				case COST:
					elementaryDecisionsList.add(new ConditionAtLeastThresholdVSObject<EvaluationField>(
							new EvaluationAttributeWithContext(attribute, attributeIndex), evaluation));
					break;
				case NONE:
					elementaryDecisionsList.add(new ConditionEqualThresholdVSObject<EvaluationField>(
							new EvaluationAttributeWithContext(attribute, attributeIndex), evaluation));
					break;
				}
				break;
			}
		}
		
		//arrange elementary decisions in order of respective attributes
		elementaryDecisionsList.sort((x, y) -> {
			int i = x.getAttributeWithContext().getAttributeIndex();
			int j = y.getAttributeWithContext().getAttributeIndex();
			if (i < j) {
				return -1;
			} else {
				return (i == j) ? 0 : 1;
			}
		});
		
		if (elementaryDecisionsList.size() == 1) {
			return new Rule.ElementaryDecision(elementaryDecisionsList.get(0));
		} else {
			List<Rule.RuleDecisions> ruleDecisionsList = new ObjectArrayList<>();
			for (Condition<? extends EvaluationField> elementaryDecision : elementaryDecisionsList) {
				ruleDecisionsList.add(new Rule.ElementaryDecision(elementaryDecision));
			}
			return new Rule.ANDConnectedRuleDecisions(ruleDecisionsList);
		}
		
	}
	
}
