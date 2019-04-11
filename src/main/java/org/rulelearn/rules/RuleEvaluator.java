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

import org.rulelearn.measures.Measure;

/**
 * Contract of an evaluator of a {@link Rule decision rule} using {@link RuleCoverageInformation rule coverage information}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleEvaluator extends Measure {
	
	/**
	 * Evaluates given decision rule in the context of rule coverage information.
	 * 
	 * @param ruleCoverageInfo rule coverage information concerning considered decision rule
	 * 
	 * @return evaluation of the given rule in the context of the given rule coverage information
	 * @throws NullPointerException if given parameter is {@code null}
	 */
	public double evaluate(RuleCoverageInformation ruleCoverageInfo);
	
}
