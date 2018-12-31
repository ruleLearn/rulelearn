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

import org.rulelearn.approximations.ApproximatedSet;

/**
 * Contract for provider of rule decisions {@link Rule.RuleDecisions} that can be put on the right-hand side of a certain/possible decision rule
 * built to describe objects from an approximated set.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ApproximatedSetRuleDecisionsProvider {

	/**
	 * Gets rule decisions that can be put on the right-hand side of a certain/possible decision rule describing objects from the given approximated set.
	 * 
	 * @param approximatedSet approximated set for which certain/possible decision rule is built and thus, rule decisions need to be obtained
	 * @return rule decisions that can be put on the right-hand side of a certain/possible decision rule describing objects from the given approximated set
	 */
	public Rule.RuleDecisions getRuleDecisions(ApproximatedSet approximatedSet);
	
}
