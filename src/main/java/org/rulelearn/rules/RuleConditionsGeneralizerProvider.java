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
 * Contract of a provider of {@link RuleConditionsGeneralizer rule conditions generalizers}.
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleConditionsGeneralizerProvider {
	
	/**
	 * Gets number of rule conditions generalizers that this provider has to offer.
	 * 
	 * @return number of rule conditions generalizers offered by this provider
	 */
	public int getCount();
	
	/**
	 * Gets i-th rule conditions generalizer.
	 * 
	 * @param i index of requested rule conditions generalizer
	 * @return i-th rule conditions generalizer
	 * 
	 * @throws IndexOutOfBoundsException if given index is less than zero or
	 *         greater or equal to the number of available rule conditions generalizers
	 */
	public RuleConditionsGeneralizer getRuleConditionsGeneralizer(int i);
}
