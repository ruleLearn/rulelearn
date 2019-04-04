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
 * Contract of a provider of {@link RuleInducerComponents rule inducer components}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface RuleInducerComponentsProvider {
		
	/**
	 * Provides i-th {@link RuleInducerComponents rule inducer components}.
	 * 
	 * @param i index of rule inducer components to be provided
	 * @return {@link RuleInducerComponents rule inducer components}
	 */
	public RuleInducerComponents provide(int i);
	
	/**
	 * Gets number of {@link RuleInducerComponents rule inducer components} that this provider has to offer.
	 * 
	 * @return number of {@link RuleInducerComponents rule inducer components} offered by this provider
	 */
	public int getCount();
}
