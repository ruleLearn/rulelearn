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

import org.rulelearn.core.Precondition;

/**
 * TODO SimpleRuleConditionsPrunerProvider
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class SimpleRuleConditionsPrunerProvider implements RuleConditionsPrunerProvider {

	RuleConditionsPruner[] ruleCondtionPruners;
	
	/**
	 * TODO 
	 */
	public SimpleRuleConditionsPrunerProvider(RuleConditionsPruner[] ruleCondtionPruners) {
		this.ruleCondtionPruners = Precondition.notNullWithContents(ruleCondtionPruners, "Provided rule conditions pruners are null.", "Provided rule conditions pruner %i is null.");
	}
	
	/* TODO (non-Javadoc)
	 * @see org.rulelearn.rules.RuleConditionsPrunerProvider#getCount()
	 */
	@Override
	public int getCount() {
		return ruleCondtionPruners.length;
	}

	/* TODO (non-Javadoc)
	 * @see org.rulelearn.rules.RuleConditionsPrunerProvider#getRuleConditionsPruner(int)
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
