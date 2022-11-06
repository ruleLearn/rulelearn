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
 * Contract of a generalizer used to generalize elementary conditions from {@link RuleConditions rule conditions}.
 * It is useful as sometimes elementary conditions added earlier can be generalized after additional conditions are added to rule conditions
 * (concerning different attributes, and "eliminating" some negative objects that earlier blocked more general conditions).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleConditionsGeneralizer {
	
	/**
	 * Generalizes given rule conditions, if possible, by making some conditions more general, and also returns modified rule conditions.
	 * Suppose the first condition (added earlier) is "f1 &gt;= 2" and second condition (added later) is "f2 &gt;= 2".
	 * Then, possibly one may generalize the first condition to "f1 &gt;= 1" because the second condition eliminated from coverage some negative objects
	 * having value of f1 between 1 (inclusive) and 2 (exclusive).
	 * 
	 * @param ruleConditions rule conditions that should be generalized; this object can be modified as a result of performed generalization
	 * @return generalized rule conditions (the same object, but possibly with more general conditions)
	 * 
	 * @throws NullPointerException if given rule conditions are {@code null}
	 */
	public RuleConditions generalize(RuleConditions ruleConditions);

}
