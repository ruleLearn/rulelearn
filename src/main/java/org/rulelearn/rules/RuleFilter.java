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

/**
 * Filter for decision rules. Can be used to reduce number of rules by dropping rules that do not satisfy certain condition(s).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleFilter {

	/**
	 * Tells if given rule, having given characteristics, is accepted by this filter. Checks either the rule, or its characteristics, or both.
	 * Not used parameters can be set to {@code null}.
	 * 
	 * @param rule rule to test
	 * @param ruleCharacteristics characteristics of the tested rule
	 * 
	 * @return {@code true} if given rule is acceptable,
	 *         {@code false} if given rule does not satisfy this filter
	 */
	public boolean accepts(Rule rule, RuleCharacteristics ruleCharacteristics);
}
