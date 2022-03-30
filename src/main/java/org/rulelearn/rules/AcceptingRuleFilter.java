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
 * Rule filter that just accepts any rule (no-operation filter, passing all rules through).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class AcceptingRuleFilter implements RuleFilter {

	/**
	 * Just accepts the rule having given characteristics.
	 * Parameter are not checked (can be set to {@code null}).
	 * 
	 * @param rule rule to test (can be {@code null} - this parameter is not used)
	 * @param ruleCharacteristics characteristics of the tested rule (can be {@code null} - this parameter is not used)
	 * 
	 * @return {@code true}
	 */
	public boolean accepts(Rule rule, RuleCharacteristics ruleCharacteristics) {
		return true; //minimize execution time
	}

}
