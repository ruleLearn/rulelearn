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

package org.rulelearn.wrappers;

import org.rulelearn.data.InformationTable;
import org.rulelearn.measures.ConsistencyMeasure;
import org.rulelearn.rules.RuleSet;

/**
 * Wraps a variable consistency rule induction algorithm.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface VariableConsistencyRuleInducerWrapper extends RuleInducerWrapper {

	/**
	 * Induces a set of rules, satisfying a threshold with respect to a consistency measure, covering objects from an information table.
	 * 
	 * @param informationTable an information table {@link InformationTable}
	 * @param consistencyThreshold threshold on a consistency measure {@link ConsistencyMeasure}
	 * 
	 * @return induced rules in {@link RuleSet}
	 */
	public RuleSet induceRules(InformationTable informationTable, double consistencyThreshold);
	
}
