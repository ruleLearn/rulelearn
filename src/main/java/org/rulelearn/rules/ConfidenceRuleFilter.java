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

import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.UnknownValueException;

/**
 * Filter for decision rules that accepts only rules with confidence &gt; or &gt;= given threshold.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ConfidenceRuleFilter implements RuleFilter {
	
	/**
	 * Threshold for rule's confidence.
	 */
	double confidenceThreshold;
	/**
	 * Tells if strict comparison (confidence &gt; threshold) or weak comparison (confidence &gt=; threshold)
	 * should be performed when this filter is applied to a decision rule.
	 */
	boolean strictComparison;
	
	/**
	 * Constructs this confidence filter.
	 * 
	 * @param confidenceThreshold threshold for rule's confidence; should belong to [0, 1] interval
	 * @param strictComparison tells if strict comparison (confidence &gt; threshold) or weak comparison (confidence &gt;= threshold)
	 *        should be performed when this filter is applied to a decision rule
	 * 
	 * @throws InvalidTypeException if given threshold is not within [0, 1] interval
	 */
	public ConfidenceRuleFilter(double confidenceThreshold, boolean strictComparison) {
		Precondition.within01Interval(confidenceThreshold, "Confidence threshold does not belong to [0,1] interval.");
		
		this.confidenceThreshold = confidenceThreshold;
		this.strictComparison = strictComparison;
	}

	/**
	 * Tells if a rule having given characteristics, is accepted by this filter.
	 * Checks only given rule characteristics (rule can be set to {@code null}).
	 * 
	 * @param rule rule to test (can be {@code null} - this parameter is not used)
	 * @param ruleCharacteristics characteristics of the tested rule
	 * 
	 * @return {@code true} if given rule is acceptable,
	 *         {@code false} if given rule does not satisfy this filter
	 * 
	 * @throws UnknownValueException if given characteristics do not allow to get rule's confidence
	 * @throws NullPointerException if given characteristics are {@code null}
	 */
	@Override
	public boolean accepts(Rule rule, RuleCharacteristics ruleCharacteristics) {
		Precondition.notNull(ruleCharacteristics, "Rule characteristics are null.");
		
		try {
			if (strictComparison) {
				return (ruleCharacteristics.getConfidence() > confidenceThreshold);
			} else {
				return (ruleCharacteristics.getConfidence() >= confidenceThreshold);
			}
		} catch (UnknownValueException exception) {
			throw new UnknownValueException("Rule's confidence is unknown.");
		}
	}
	
	/**
	 * Gets confidence threshold.
	 * 
	 * @return confidence threshold
	 */
	public double getConfidenceThreshold() {
		return confidenceThreshold;
	}
	
	/**
	 * Gets flag indicating if strict comparison (confidence &gt; threshold) is employed.
	 * 
	 * @return {@code true} if strict comparison (confidence &gt; threshold) is employed,
	 *         {@code false} if weak comparison (confidence &gt;= threshold) is employed
	 */
	public boolean getStrictComparison() {
		return strictComparison;
	}

}
