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

import org.rulelearn.data.InformationTable;
import org.rulelearn.measures.Measure;

/**
 * Evaluates a {@link Rule} in the context of an {@link InformationTable}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleEvaluator extends Measure {
	
	/**
	 * Evaluates given decision rule in the context of given information table.
	 * 
	 * @param rule decision rule being evaluated
	 * @param informationTable information table used to evaluate given decision rule
	 * 
	 * @return evaluation of the given rule in the context of the given information table
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public double evaluate(Rule rule, InformationTable informationTable);
	
}
