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

package org.rulelearn.measures;

import org.rulelearn.data.InformationTable;
import org.rulelearn.rules.Rule;
import org.rulelearn.rules.RuleEvaluator;

/**
 * Rule support measure reflecting the amount of objects which support a rule (i.e., objects which match elementary conditions on the LHS, and decisions on the RHS).  
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SupportMeasure implements GainTypeMeasure, RuleEvaluator {

	/**
	 * {@inheritDoc}
	 * 
	 * @param rule {@inheritDoc}
	 * @param informationTable {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public double evaluate(Rule rule, InformationTable informationTable) {
		int counter = 0;
		int numberOfObjects = informationTable.getNumberOfObjects(); 
		for (int i = 0; i < numberOfObjects; i++) {
			if (rule.supportedBy(i, informationTable)) {
				counter++;
			}
		}
		return counter;
	}
	
}