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
 * Contract of a provider of {@link RuleConditionsPruner rule conditions pruners}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleConditionsPrunerProvider {

	/**
	 * Gets number of rule conditions pruners that this provider has to offer.
	 * 
	 * @return number of rule conditions pruners offered by this provider
	 */
	public int getCount();
	
	/**
	 * Gets i-th rule conditions pruner.
	 * 
	 * @param i index of requested rule conditions pruners
	 * @return i-th rule conditions pruners
	 * 
	 * @throws IndexOutOfBoundsException if given index is less than zero or
	 *         greater or equal to the number of available rule conditions pruners
	 */
	public RuleConditionsPruner getRuleConditionsPruner(int i);
	
}
