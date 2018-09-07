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
import org.rulelearn.data.InformationTable;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Builder of {@link RuleConditions} objects.
 * TODO: write javadoc
 * TODO: add missing parameters
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleConditionsBuilder {
	
	IntSet consideredObjects;
	InformationTable learningInformationTable;
	IntSet indicesOfPositiveObjects;
	
	public RuleConditionsBuilder(IntSet consideredObjects, InformationTable learningInformationTable, IntSet indicesOfPositiveObjects) {
		this.consideredObjects = Precondition.notNull(consideredObjects, "Considered objects are null.");
		this.learningInformationTable = Precondition.notNull(learningInformationTable, "Learning information table is null.");
		this.indicesOfPositiveObjects = Precondition.notNull(indicesOfPositiveObjects, "Indices of positive objects are null.");
	}
	
	public RuleConditions build() {
		RuleConditions ruleConditions = new RuleConditions(learningInformationTable, indicesOfPositiveObjects);
		
		//TODO: implement
		
		return ruleConditions;
	}
	
}
