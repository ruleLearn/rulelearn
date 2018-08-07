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

import java.util.Objects;
import java.util.Set;

//import org.rulelearn.approximations.Union;
//import org.rulelearn.core.TernaryLogicValue;

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
		
		int numberOfObjects = informationTable.getNumberOfObjects();
		for (int i = 0; i < numberOfObjects; i++) {
			this.increaseCount(informationTable.getDecision(i));
		}
	}
	
	/**
	 * Checks whether a given decision is present in the distribution (i.e., object/objects having a given value of decision are present in the distribution).
	 * 
	 * @param decision decision of interest; should not be {@code null}
	 * @return true if a given decision is present in the distribution
	 */
	public boolean isPresent(Decision decision) {
		return this.decision2CountMap.containsKey(decision);
	}
	
	/**
	 * Checks whether a given union is present in the distribution (i.e., object/objects having a value of decision concordant with the union are present in the distribution).
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return true if a value of decision concordant with the given union is present in the distribution
	 */
	/*public boolean isPresent(Union union) {
		boolean concordant = false;
		for (Decision decision : this.decision2CountMap.keySet()) {
			if (union.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {
				concordant = true;
				break;
			}
		}
		return concordant;
	}*/
	
	/**
	 * Gets all decisions, which are present in the distribution.
	 * 
	 * @return set {@link Set} of decisions
	 */
	public Set<Decision> getDecisions() {
		return this.decision2CountMap.keySet();
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
	 * Gets number of objects having the decision concordant with a given union.
	 * 
	 * @param union union of interest; should not be {@code null}
	 * @return number of objects having the decision value concordant with a given union 
	 */
	/*public int getCount(Union union) {
		int count = 0;
		for (Decision decision : this.decision2CountMap.keySet()) {
			if (union.isConcordantWithDecision(decision) == TernaryLogicValue.TRUE) {
				count += this.decision2CountMap.getInt(decision);
			}
		}
		return count;
	}*/
	
	/**
	 * Increases by one the number of objects having given decision.
	 * 
	 * @param decision decision of interest; should not be {@code null}
	 */
	public void increaseCount(Decision decision) {
		int count = this.decision2CountMap.containsKey(decision) ? this.decision2CountMap.getInt(decision) : 0;
		this.decision2CountMap.put(decision, ++count);
	}
	
	/**
	 * Gets hash code of this decision distribution.
	 * 
	 * @return calculated hash code of this decision distribution
	 */
	public int hashCode() {
		return Objects.hash(this.getClass(), this.decision2CountMap);
	}
	
	/**
	 * Tests if this decision distribution is equal to the given object. Returns {@code true} if the given object is also a decision distribution,
	 * and the two distributions concern the same decisions with the same cardinalities.
	 * 
	 * @param otherObject other object that this decision distribution should be compared with
	 * @return {@code true} if this decision distribution is equal to the given objects,
	 *         {@code false} otherwise
	 */
	public boolean equals(Object otherObject) {
		if (otherObject instanceof DecisionDistribution &&
			this.decision2CountMap.equals(((DecisionDistribution)otherObject).decision2CountMap)) {
			return true;
		} else {
			return false;
		}
	}

}
