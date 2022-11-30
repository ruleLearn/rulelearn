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
 * Simple rule conditions generalizer provider, constructed using and array of {@link RuleConditionsGeneralizer rule conditions generalizers},
 * and capable of providing any of these generalizers, by its index.
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleRuleConditionsGeneralizerProvider implements RuleConditionsGeneralizerProvider {

	/**
	 * Provided {@link RuleConditionsGeneralizer rule condition generalizers}.
	 */
	RuleConditionsGeneralizer[] ruleCondtionGeneralizers;
	
	/**
	 * Constructor storing given array of {@link RuleConditionsGeneralizer rule condition generalizers}.
	 * 
	 * @param ruleCondtionGeneralizers array of rule condition generalizers to be stored in this provider
	 * 
	 * @throws NullPointerException if given array is {@code null}
	 * @throws NullPointerException if any of the elements of the given array is {@code null}
	 * @throws InvalidSizeException if given array is empty
	 */
	public SimpleRuleConditionsGeneralizerProvider(RuleConditionsGeneralizer[] ruleCondtionGeneralizers) {
		this.ruleCondtionGeneralizers = Precondition.nonEmpty(Precondition.notNullWithContents(ruleCondtionGeneralizers,
				"Provided rule conditions generalizers are null.",
				"Provided rule conditions generalizer is null at index %i."), "Array with rule conditions generalizers is empty.");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return ruleCondtionGeneralizers.length;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param i {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public RuleConditionsGeneralizer getRuleConditionsGeneralizer(int i) {
		if ((i >= 0) && (i < ruleCondtionGeneralizers.length)) {
			return ruleCondtionGeneralizers[i];
		}
		else {
			throw new IndexOutOfBoundsException("Requested rule conditions generalizer does not exist at index: " + i + ".");
		}
	}
	
}
