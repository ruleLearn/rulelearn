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

import org.rulelearn.core.InvalidSizeException;
import org.rulelearn.core.Precondition;

/**
 * Simple rule conditions pruner provider, constructed using and array of {@link RuleConditionsPruner rule conditions pruners},
 * and capable of providing any of these pruners, by its index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleRuleConditionsPrunerProvider implements RuleConditionsPrunerProvider {

	/**
	 * Provided {@link RuleConditionsPruner rule condition pruners}.
	 */
	RuleConditionsPruner[] ruleCondtionPruners;
	
	/**
	 * Constructor storing given array of {@link RuleConditionsPruner rule condition pruners}.
	 * 
	 * @param ruleCondtionPruners array of rule condition pruners to be stored in this provider
	 * 
	 * @throws NullPointerException if given array is {@code null}
	 * @throws NullPointerException if any of the elements of the given array is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 */
	public SimpleRuleConditionsPrunerProvider(RuleConditionsPruner[] ruleCondtionPruners) {
		this.ruleCondtionPruners = Precondition.nonEmpty(Precondition.notNullWithContents(ruleCondtionPruners,
				"Provided rule conditions pruners are null.",
				"Provided rule conditions pruner is null at index %i."), "Array with rule conditions pruners is empty.");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return ruleCondtionPruners.length;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param i {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public RuleConditionsPruner getRuleConditionsPruner(int i) {
		if ((i >= 0) && (i < ruleCondtionPruners.length)) {
			return ruleCondtionPruners[i];
		}
		else {
			throw new IndexOutOfBoundsException("Requested rule conditions pruner does not exist at index: " + i + ".");
		}
	}
	
}
