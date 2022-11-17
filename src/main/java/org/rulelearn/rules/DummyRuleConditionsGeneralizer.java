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
 * Rule conditions generalizer that does nothing (does not modify rule conditions and returns 0 as the number of generalized elementary conditions).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DummyRuleConditionsGeneralizer implements RuleConditionsGeneralizer {

	/**
	 * {@inheritDoc}
	 * 
	 * @param ruleConditions {@inheritDoc}
	 * @return {@inheritDoc}
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */	
	@Override
	public int generalize(RuleConditions ruleConditions) {
		return 0; //nothing generalized
	}

}
