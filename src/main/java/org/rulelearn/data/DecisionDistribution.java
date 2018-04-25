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

import static org.rulelearn.core.Precondition.notNull;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Distribution of decisions in the set of considered objects (information table).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DecisionDistribution {
	/**
	 * Maps decision to number of objects having this decision.
	 */
	protected Object2IntMap<Decision> decision2CountMap;
	
	/**
	 * Sole constructor.
	 */
	public DecisionDistribution() {
		this.decision2CountMap = new Object2IntOpenHashMap<Decision>();
	}
	
	/**
	 * Constructs this distribution based on the given information table.
	 * 
	 * @param informationTable information table for which decision distribution should be constructed; this table should contain decisions for subsequent objects
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public DecisionDistribution(InformationTable informationTable) {
		notNull(informationTable, "Information table for calculation of distribution of decisions is null.");
		this.decision2CountMap = new Object2IntOpenHashMap<Decision>();
		
		//TODO: implement
		/*
		if (attributeIndex == informationTable.getActiveDecisionAttributeIndex()) {
			Field[] fields = informationTable.getDecisions();
			
			if (fields != null) { //it is possible to calculate distribution of decision classes
				for (int i = 0; i < fields.length; i++) {
					this.increaseCount(fields[i]);
				}
			}
		} else {
			//manually browse fields from a single column of the information table
			int numberOfFields = informationTable.getNumberOfObjects();
			for (int i = 0; i < numberOfFields; i++) {
				this.increaseCount(informationTable.getField(i, attributeIndex));
			}
		}*/

	}
	
	/**
	 * Gets number of objects having given decision.
	 * 
	 * @param decision decision of interest; should not be {@code null}
	 * @return number of objects having given decision
	 */
	public int getCount(Decision decision) {
		return this.decision2CountMap.containsKey(decision) ? this.decision2CountMap.getInt(decision) : 0;
	}
	
	/**
	 * Increases by one the number of objects having given decision.
	 * 
	 * @param decision decision of interest; should not be {@code null}
	 */
	public void increaseCount(Decision decision) {
		int count = this.decision2CountMap.containsKey(decision) ? this.decision2CountMap.getInt(decision) : 0;
		this.decision2CountMap.put(decision, ++count);
	}

}
