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

package org.rulelearn.classification;

import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.Decision;

/**
 * Result of classification of an object from an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class ClassificationResult {
	/**
	 * Verifies if this classification result is consistent with the given decision.
	 * 
	 * @param decision decision class of an object in an information table
	 * @return {@link TernaryLogicValue#TRUE} if this classification result is consistent with the given decision,
	 *         {@link TernaryLogicValue#FALSE} if this classification result is not consistent with the given decision,
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if this classification result is uncomparable with the given decision
	 *         (which may be the case if, e.g., this classification result is unknown)
	 * @throws NullPointerException if given decision is {@code null} 
	 */
	public abstract TernaryLogicValue isConsistentWith(Decision decision);
}
