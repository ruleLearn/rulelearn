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
import org.rulelearn.approximations.Union;

/**
 * Provider of rule decisions {@link Rule.RuleDecisions} that can be put on the right-hand side of a certain/possible decision rule
 * built to describe objects from a union of decisions classes {@link Union}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class UnionRuleDecisionsProvider implements ApproximatedSetRuleDecisionsProvider {

	/**
	 * Gets semantics of decision rule generated for given union.
	 * 
	 * @param union union for which decision rule is built
	 * @return semantics of decision rule generated for given union
	 * 
	 * @throws ClassCastException if given approximated set is not of type {@link Union}
	 */
	@Override
	public RuleSemantics getRuleSemantics(ApproximatedSet union) {
		if (!(union instanceof Union)) {
			throw new ClassCastException("Cannot cast ApproximatedSet to Union.");
		}
		
		Union aUnion = (Union)union;
		
		if (aUnion.getUnionType() == Union.UnionType.AT_LEAST) {
			return RuleSemantics.AT_LEAST;
		} else {
			return RuleSemantics.AT_MOST;
		}
	}

}
