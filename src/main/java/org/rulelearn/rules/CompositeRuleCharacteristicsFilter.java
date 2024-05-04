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

import java.util.ArrayList;
import java.util.List;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.UnknownValueException;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Composite rule characteristics filter capable of filtering simultaneously by different rule characteristics (e.g., by rule's confidence and coverage factor).
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CompositeRuleCharacteristicsFilter implements RuleFilter {
	
	/**
	 * Separator of particular {@link RuleCharacteristicsFilter rule characteristics filters}, assumed when presenting this composite filter {@link #toString() as text}.
	 */
	public static final String separator = "&";
	
	/**
	 * List of basic rule characteristics filters.
	 */
	List<RuleCharacteristicsFilter> ruleCharacteristicsFilters;
	
	/**
	 * Constructs this filter using a list of basic rule characteristics filters.
	 * 
	 * @param ruleCharacteristicsFilters set of rule characteristics filters; can be an empty array if no rule quality measure is taken into account
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public CompositeRuleCharacteristicsFilter(List<RuleCharacteristicsFilter> ruleCharacteristicsFilters) {
		this.ruleCharacteristicsFilters = Precondition.notNull(ruleCharacteristicsFilters, "Basic rule characteristics filters of a composite filter are null.");
	}

	/**
	 * Gets list of basic rule characteristics filters.
	 * 
	 * @return list of basic rule characteristics filters (copy of the list stored in this filter)
	 */
	public List<RuleCharacteristicsFilter> getRuleCharacteristicsFilters() {
		return new ObjectArrayList<RuleCharacteristicsFilter>(ruleCharacteristicsFilters);
	}
	
	/**
	 * Tells if a rule having given characteristics, is accepted by this filter. Tests only rule characteristics.
	 * 
	 * @param rule rule to test; can be {@code null} - rule is not taken into account
	 * @param ruleCharacteristics rule characteristics to test
	 * 
	 * @return {@code true} if given rule characteristics are acceptable,
	 *         {@code false} if given rule characteristics do not satisfy this filter
	 * 
	 * @throws UnknownValueException if given rule characteristics do not store any of the tested values
	 * @throws NullPointerException if given rule characteristics are {@code null}
	 */
	@Override
	public boolean accepts(Rule rule, RuleCharacteristics ruleCharacteristics) {
		Precondition.notNull(ruleCharacteristics, "Rule characteristics are null.");
		
		for (RuleCharacteristicsFilter ruleCharacteristicsFilter : ruleCharacteristicsFilters) {
			if (!ruleCharacteristicsFilter.accepts(rule, ruleCharacteristics)) {
				return false;
			}
		}
		return true; //all basic filters accepted rule characteristics
	}
	
	/**
	 * Gets textual representation of this rule characteristics filter.
	 */
	@Override
	public String toString() {
		StringBuilder resultBuilder = new StringBuilder();
		int index = 0;
		
		for (RuleCharacteristicsFilter ruleCharacteristicsFilter : ruleCharacteristicsFilters) {
			resultBuilder.append(ruleCharacteristicsFilter.toString());
			if (index++ < ruleCharacteristicsFilters.size() - 1) { //not the last filter
				resultBuilder.append(separator);
			}
		}
		
		return resultBuilder.toString();
	}
	
	/**
	 * Constructs and returns composite rule characteristics filter using textual representation of filter.
	 * 
	 * @param compositeRuleCharacteristicsFilter textual representation of the filter;
	 *        if {@code null} or equal to empty string, then constructs composite filter with 0 basic filters (accepting everything)
	 * @return constructed composite rule characteristics filter
	 * 
	 * @throws InvalidValueException if textual representation of any basic filter (between separators) is not correct
	 * @throws NumberFormatException if threshold in textual representation of any basic filter (between separators) is not a valid number
	 */
	public static CompositeRuleCharacteristicsFilter of(String compositeRuleCharacteristicsFilter) {
		List<RuleCharacteristicsFilter> basicFilters = new ArrayList<RuleCharacteristicsFilter>();
		
		if (compositeRuleCharacteristicsFilter == null || compositeRuleCharacteristicsFilter.equals("")) {
			return new CompositeRuleCharacteristicsFilter(basicFilters); //go with empty list of basic filters
		}
		
		if (compositeRuleCharacteristicsFilter.contains(separator)) { //expects many basic filters
			String[] splitResult = compositeRuleCharacteristicsFilter.split(separator);
			for (String basicFilterText : splitResult) {
				basicFilters.add(RuleCharacteristicsFilter.of(basicFilterText));
			}
			return new CompositeRuleCharacteristicsFilter(basicFilters);
		} else { //expects single basic filter
			basicFilters.add(RuleCharacteristicsFilter.of(compositeRuleCharacteristicsFilter));
			return new CompositeRuleCharacteristicsFilter(basicFilters);
		}
	}

}
