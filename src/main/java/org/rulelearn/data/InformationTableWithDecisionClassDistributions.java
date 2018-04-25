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

package org.rulelearn.data;

import java.util.List;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.dominance.DominanceConesDecisionDistributions;
import org.rulelearn.types.Field;

/**
 * TODO: write javadoc
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class InformationTableWithDecisionClassDistributions extends InformationTable {
	
	/**
	 * Distribution of decisions found in this information table among different dominance cones defined for objects from this information table.
	 */
	protected DominanceConesDecisionDistributions dominanceConesDecisionDistributions;
	
	/**
	 * Distribution of decisions associated with objects of this information table.
	 */
	protected DecisionDistribution decisionClassDistribution;
	
	/**
	 * TODO: write javadoc
	 * 
	 * @param attributes
	 * @param listOfFields
	 */
	public InformationTableWithDecisionClassDistributions(Attribute[] attributes, List<Field[]> listOfFields) {
		this(attributes, listOfFields, false);
	}
	
	/**
	 * TODO: write javadoc
	 * 
	 * @param attributes
	 * @param listOfFields
	 * @param accelerateByReadOnlyParams
	 * 
	 * @throws InvalidValueException if given information table does not have any active decision attribute
	 */
	public InformationTableWithDecisionClassDistributions(Attribute[] attributes, List<Field[]> listOfFields, boolean accelerateByReadOnlyParams) {
		super(attributes, listOfFields, accelerateByReadOnlyParams);
		
		if (this.getDecisions(true) == null) {
			throw new InvalidValueException("Information table for which decision distributions should be calculated does not have any active decision attribute.");
		}
		
		this.dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(this);
		this.decisionClassDistribution = new DecisionDistribution(this);
	}

	/**
	 * Gets distribution of decisions found in this information table among different dominance cones defined for objects from this information table.
	 * 
	 * @return distribution of decisions found in this information table among different dominance cones defined for objects from this information table
	 */
	public DominanceConesDecisionDistributions getDominanceConesDecisionDistributions() {
		return this.dominanceConesDecisionDistributions;
	}

	/**
	 * Gets distribution of decisions associated with objects of this information table
	 * 
	 * @return distribution of decisions associated with objects of this information table
	 */
	public DecisionDistribution getDecisionDistribution() {
		return this.decisionClassDistribution;
	}
	
	
	
}
