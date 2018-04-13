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

package org.rulelearn.dominance;

import org.rulelearn.data.FieldDistribution;
import org.rulelearn.data.InformationTable;
import static org.rulelearn.core.Precondition.notNull;

/**
 * Class for calculation and storage of decision class distributions in dominance cones originating in objects of an information table.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DominanceConesDecisionClassDistributions {
	/**
	 * Decision class distributions of positive dominance cones w.r.t. (straight) dominance relation D (y D x &lt;=&gt; y dominates x), one for each object x from an information table.
	 * Formally, D^+(x) = {y \in U : y D x}. 
	 */
	protected FieldDistribution[] positiveDConesDecisionClassDistributions;
	/**
	 * Decision class distributions of negative dominance cones w.r.t. (straight) dominance relation D (x D y &lt;=&gt; x dominates y), one for each object x from an information table.
	 * Formally, D^-(x) = {y \in U : x D y}.
	 */
	protected FieldDistribution[] negativeDConesDecisionClassDistributions; //used to calc lower appx of union "at most"
	/**
	 * Decision class distributions of positive dominance cones w.r.t. (inverse) dominance relation InvD (x InvD y &lt;=&gt; x is dominated by y), one for each object x from an information table.
	 * Formally, InvD^+(x) = {y \in U : x InvD y}.
	 */
	protected FieldDistribution[] positiveInvDConesDecisionClassDistributions; //used to calc lower appx of union "at least"
	/**
	 * Decision class distributions of negative dominance cones w.r.t. (inverse) dominance relation InvD (y InvD x &lt;=&gt; y is dominated by x), one for each object x from an information table.
	 * Formally, InvD^-(x) = {y \in U : y InvD x}.
	 */
	protected FieldDistribution[] negativeInvDConesDecisionClassDistributions;
	
	/**
	 * Number of objects for which dominance cones are processed in this object.
	 */
	protected int numberOfObjects;
	
	/**
	 * Constructs this object by calculating distribution of decision classes in each dominance cone of every object.
	 * 
	 * @param informationTable information table containing objects for which dominance cones should be processed
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public DominanceConesDecisionClassDistributions(InformationTable informationTable) {
		notNull(informationTable, "Information table for calculation of dominance cones is null.");
		this.numberOfObjects = informationTable.getNumberOfObjects();
		
		this.positiveDConesDecisionClassDistributions = new FieldDistribution[this.numberOfObjects];
		this.negativeDConesDecisionClassDistributions = new FieldDistribution[this.numberOfObjects];
		this.positiveInvDConesDecisionClassDistributions = new FieldDistribution[this.numberOfObjects];
		this.negativeInvDConesDecisionClassDistributions = new FieldDistribution[this.numberOfObjects];
		
		this.calculatePositiveDConesDecisionClassDistributions(informationTable);
		this.calculateNegativeDConesDecisionClassDistributions(informationTable);
		this.calculatePositiveInvDConesDecisionClassDistributions(informationTable);
		this.calculateNegativeInvDConesDecisionClassDistributions(informationTable);
	}
	
	/**
	 * Gets number of objects for which decision class distributions in dominance cones were calculated and stored.
	 * 
	 * @return number of objects for which decision class distributions in dominance cones were calculated and stored
	 */
	public int getNumberOfObjects() {
		return this.numberOfObjects;
	}
	
	/**
	 * Calculates decision class distributions in positive dominance cones w.r.t. (straight) dominance relation D.
	 * 
	 * @param informationTable information table for which decision class distributions in positive dominance cones w.r.t. (straight) dominance relation D should be calculated
	 */
	protected void calculatePositiveDConesDecisionClassDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.positiveDConesDecisionClassDistributions[x] = new FieldDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.dominates(y, x, informationTable)) {// y D x
					this.positiveDConesDecisionClassDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Calculates decision class distributions in negative dominance cones w.r.t. (straight) dominance relation D.
	 * 
	 * @param informationTable information table for which decision class distributions in negative dominance cones w.r.t. (straight) dominance relation D should be calculated
	 */
	protected void calculateNegativeDConesDecisionClassDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.negativeDConesDecisionClassDistributions[x] = new FieldDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.dominates(x, y, informationTable)) {// x D y
					this.negativeDConesDecisionClassDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Calculates decision class distributions in positive dominance cones w.r.t. (inverse) dominance relation InvD.
	 * 
	 * @param informationTable information table for which decision class distributions in positive dominance cones w.r.t. (inverse) dominance relation InvD should be calculated
	 */
	protected void calculatePositiveInvDConesDecisionClassDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.positiveInvDConesDecisionClassDistributions[x] = new FieldDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.isDominatedBy(x, y, informationTable)) {// x InvD y
					this.positiveInvDConesDecisionClassDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Calculates decision class distributions in negative dominance cones w.r.t. (inverse) dominance relation InvD.
	 * 
	 * @param informationTable information table for which decision class distributions in negative dominance cones w.r.t. (inverse) dominance relation InvD should be calculated
	 */
	protected void calculateNegativeInvDConesDecisionClassDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.negativeInvDConesDecisionClassDistributions[x] = new FieldDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.isDominatedBy(y, x, informationTable)) {// y InvD x
					this.negativeInvDConesDecisionClassDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Gets decision class distribution in positive dominance cone w.r.t. (straight) dominance relation D (y D x &lt;=&gt; y dominates x),
	 * originating in object x addresses by the given index. Formally, D^+(x) = {y \in U : y D x}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision class distribution in positive dominance cone w.r.t. (straight) dominance relation D, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 */
	public FieldDistribution getPositiveDConeDecisionClassDistribution(int objectIndex) {
		return this.positiveDConesDecisionClassDistributions[objectIndex];
	}
	
	/**
	 * Gets decision class distribution in negative dominance cone w.r.t. (straight) dominance relation D (x D y &lt;=&gt; x dominates y),
	 * originating in object x addresses by the given index. Formally, D^-(x) = {y \in U : x D y}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision class distribution in negative dominance cone w.r.t. (straight) dominance relation D, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 */
	public FieldDistribution getNegativeDConeDecisionClassDistribution(int objectIndex) {
		return this.negativeDConesDecisionClassDistributions[objectIndex];
	}
	
	/**
	 * Gets decision class distribution in positive dominance cone w.r.t. (inverse) dominance relation InvD (x InvD y &lt;=&gt; x is dominated by y),
	 * originating in object x addresses by the given index. Formally, InvD^+(x) = {y \in U : x InvD y}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision class distribution in positive dominance cone w.r.t. (inverse) dominance relation InvD, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 */
	public FieldDistribution getPositiveInvDConeDecisionClassDistribution(int objectIndex) {
		return this.positiveInvDConesDecisionClassDistributions[objectIndex];
	}
	
	/**
	 * Gets decision class distribution in negative dominance cone w.r.t. (inverse) dominance relation InvD (y InvD x &lt;=&gt; y is dominated by x),
	 * originating in object x addresses by the given index. Formally, InvD^-(x) = {y \in U : y InvD x}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision class distribution in negative dominance cone w.r.t. (inverse) dominance relation InvD, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 */
	public FieldDistribution getNegativeInvDConeDecisionClassDistribution(int objectIndex) {
		return this.negativeInvDConesDecisionClassDistributions[objectIndex];
	}
	
}
