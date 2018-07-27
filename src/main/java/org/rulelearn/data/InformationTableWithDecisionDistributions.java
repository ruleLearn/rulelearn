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
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.dominance.DominanceConesDecisionDistributions;
import org.rulelearn.types.Field;

/**
 * Specialized information table, extending {@link InformationTable} by exposing additional information concerning:<br>
 * - distribution of decisions found in this information table among different dominance cones defined for objects from this information table - see {@link DominanceConesDecisionDistributions},<br>
 * - distribution of decisions associated with objects of this information table - see {@link DecisionDistribution}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableWithDecisionDistributions extends InformationTable {
	
	/**
	 * Distribution of decisions found in this information table among different dominance cones defined for objects from this information table.
	 */
	protected DominanceConesDecisionDistributions dominanceConesDecisionDistributions;
	
	/**
	 * Distribution of decisions associated with objects of this information table.
	 */
	protected DecisionDistribution decisionDistribution;
	
	/**
	 * A wrapper-type constructor, passing arguments to {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}
	 * with the boolean flag set to {@code false}. 
	 * 
	 * @param attributes see {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}
	 * @param listOfFields see {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}
	 * 
	 * @throws NullPointerException see {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}
	 * @throws InvalidValueException see {@link InformationTableWithDecisionDistributions#InformationTableWithDecisionDistributions(Attribute[], List, boolean)}
	 */
	public InformationTableWithDecisionDistributions(Attribute[] attributes, List<Field[]> listOfFields) {
		this(attributes, listOfFields, false);
	}
	
	/**
	 * Information table constructor. Invokes superclass constructor {@link InformationTable#InformationTable(Attribute[], List, boolean)} for basic construction.
	 * Then, checks if there is at least one active decision attribute (throwing an {@link InvalidValueException} exception if this is not the case).
	 * Finally, calculates:<br>
	 * - distribution of decisions found in this information table among different dominance cones defined for objects from this information table,<br>
	 * - distribution of decisions among objects of this information table.
	 * 
	 * @param attributes see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @param listOfFields see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @param accelerateByReadOnlyParams see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * 
	 * @throws NullPointerException see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @throws InvalidValueException see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @throws InvalidValueException if given information table does not have any active decision attribute
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public InformationTableWithDecisionDistributions(Attribute[] attributes, List<Field[]> listOfFields, boolean accelerateByReadOnlyParams) {
		super(attributes, listOfFields, accelerateByReadOnlyParams);
		
		if (this.getDecisions(true) == null) {
			throw new InvalidValueException("Information table for which decision distributions should be calculated does not have any active decision attribute.");
		}
		
		this.dominanceConesDecisionDistributions = new DominanceConesDecisionDistributions(this);
		this.decisionDistribution = new DecisionDistribution(this);
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
		return this.decisionDistribution;
	}
	
}
