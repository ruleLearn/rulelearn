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
 * Semantics a decision rule, reflecting the type of conditions and decision present in this rule.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public enum RuleSemantics {
	
	/**
	 * Semantics of a rule describing minimal conditions (lower profile) that need to be satisfied by a decision object to conclude that this object belongs to the set determined by rule's decision part.
	 */
	AT_LEAST,
	
	/**
	 * Semantics of a rule describing maximal conditions (upper profile) that must not be exceeded by a decision object to conclude that this object belongs to the set determined by rule's decision part.
	 */
	AT_MOST,
	
	/**
	 * Semantics of a rule describing exact conditions (profile) that need to be satisfied by a decision object to conclude that this object belongs to the set determined by rule's decision part.
	 */
	EQUAL

}
