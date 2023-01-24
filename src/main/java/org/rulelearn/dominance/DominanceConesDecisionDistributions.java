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

import org.rulelearn.data.DecisionDistribution;
import org.rulelearn.data.InformationTable;
import static org.rulelearn.core.Precondition.notNull;

/**
 * Class for calculation and storage of decision distributions of type {@link DecisionDistribution} in dominance cones
 * originating in objects of an information table. Four types of dominance cones are supported - with respect to (straight) dominance relation D
 * and with respect to (inverse) dominance relation InvD, as well as positive (+) and negative (-) ones. Formally:<br>
 * <ul>
 * <li>D^+(x) = {y \in U : y D x}, i.e., positive dominance cone of object x with respect to dominance relation D is composed of objects y such that y dominates x;</li>
 * <li>D^-(x) = {y \in U : x D y}, i.e., negative dominance cone of object x with respect to dominance relation D is composed of objects y such that x dominates y;</li>
 * <li>InvD^+(x) = {y \in U : x InvD y}, i.e., positive dominance cone of object x with respect to dominance relation InvD is composed of objects y such that x is dominated by y;</li>
 * <li>InvD^-(x) = {y \in U : y InvD x}, i.e., negative dominance cone of object x with respect to dominance relation InvD is composed of objects y such that y is dominated by x.</li>
 * </ul>
 * This class allows to get for each object x present in an information table a distribution (histogram) of decisions
 * of objects found in dominance cone (of any of the above four types) originating in x.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class DominanceConesDecisionDistributions {
	/**
	 * Decision distributions of positive dominance cones w.r.t. (straight) dominance relation D (y D x &lt;=&gt; y dominates x), one for each object x from an information table.
	 * Formally, D^+(x) = {y \in U : y D x}. 
	 */
	protected DecisionDistribution[] positiveDConesDecisionDistributions = null;
	/**
	 * Decision distributions of negative dominance cones w.r.t. (straight) dominance relation D (x D y &lt;=&gt; x dominates y), one for each object x from an information table.
	 * Formally, D^-(x) = {y \in U : x D y}.
	 */
	protected DecisionDistribution[] negativeDConesDecisionDistributions = null; //used to calc lower appx of union "at most"
	/**
	 * Decision distributions of positive dominance cones w.r.t. (inverse) dominance relation InvD (x InvD y &lt;=&gt; x is dominated by y), one for each object x from an information table.
	 * Formally, InvD^+(x) = {y \in U : x InvD y}.
	 */
	protected DecisionDistribution[] positiveInvDConesDecisionDistributions = null; //used to calc lower appx of union "at least"
	/**
	 * Decision distributions of negative dominance cones w.r.t. (inverse) dominance relation InvD (y InvD x &lt;=&gt; y is dominated by x), one for each object x from an information table.
	 * Formally, InvD^-(x) = {y \in U : y InvD x}.
	 */
	protected DecisionDistribution[] negativeInvDConesDecisionDistributions = null;
	
	/**
	 * Number of objects for which dominance cones are processed in this object.
	 */
	protected int numberOfObjects;
	
	/**
	 * Constructs this object by calculating distribution of decisions in each dominance cone of every object.
	 * 
	 * @param informationTable information table containing objects for which dominance cones should be processed
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public DominanceConesDecisionDistributions(InformationTable informationTable) {
		notNull(informationTable, "Information table for calculation of dominance cones is null.");
		this.numberOfObjects = informationTable.getNumberOfObjects();
		
		this.positiveDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
		this.negativeDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
		this.positiveInvDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
		this.negativeInvDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
		
		this.calculatePositiveDConesDecisionDistributions(informationTable);
		this.calculateNegativeDConesDecisionDistributions(informationTable);
		this.calculatePositiveInvDConesDecisionDistributions(informationTable);
		this.calculateNegativeInvDConesDecisionDistributions(informationTable);
	}
	
	/**
	 * Constructs this object by calculating distribution of decisions in each dominance cone of every object.
	 * 
	 * @param informationTable information table containing objects for which dominance cones should be processed
	 * @param onlyNecessaryDistributions tells if only necessary, i.e., {@code positiveInvDConeDecisionClassDistribution} and {@code negativeDConeDecisionClassDistribution} distributions are calculated
	 *        (to finish calculations faster),
	 *        or all distributions are calculated (e.g., to present them in a GUI)
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public DominanceConesDecisionDistributions(InformationTable informationTable, boolean onlyNecessaryDistributions) {
		notNull(informationTable, "Information table for calculation of dominance cones is null.");
		this.numberOfObjects = informationTable.getNumberOfObjects();
		
		this.positiveInvDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
		this.negativeDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
		
		this.calculatePositiveInvDConesDecisionDistributions(informationTable);
		this.calculateNegativeDConesDecisionDistributions(informationTable);
		
		if (!onlyNecessaryDistributions) {
			this.positiveDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
			this.negativeInvDConesDecisionDistributions = new DecisionDistribution[this.numberOfObjects];
			
			this.calculatePositiveDConesDecisionDistributions(informationTable);
			this.calculateNegativeInvDConesDecisionDistributions(informationTable);
		}
	}
	
	/**
	 * Gets number of objects for which decision distributions in dominance cones were calculated and stored.
	 * 
	 * @return number of objects for which decision distributions in dominance cones were calculated and stored
	 */
	public int getNumberOfObjects() {
		return this.numberOfObjects;
	}
	
	/**
	 * Calculates decision distributions in positive dominance cones w.r.t. (straight) dominance relation D.
	 * 
	 * @param informationTable information table for which decision distributions in positive dominance cones w.r.t. (straight) dominance relation D should be calculated
	 */
	protected void calculatePositiveDConesDecisionDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.positiveDConesDecisionDistributions[x] = new DecisionDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.dominates(y, x, informationTable)) {// y D x
					this.positiveDConesDecisionDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Calculates decision distributions in negative dominance cones w.r.t. (straight) dominance relation D.
	 * 
	 * @param informationTable information table for which decision distributions in negative dominance cones w.r.t. (straight) dominance relation D should be calculated
	 */
	protected void calculateNegativeDConesDecisionDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.negativeDConesDecisionDistributions[x] = new DecisionDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.dominates(x, y, informationTable)) {// x D y
					this.negativeDConesDecisionDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Calculates decision distributions in positive dominance cones w.r.t. (inverse) dominance relation InvD.
	 * 
	 * @param informationTable information table for which decision distributions in positive dominance cones w.r.t. (inverse) dominance relation InvD should be calculated
	 */
	protected void calculatePositiveInvDConesDecisionDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.positiveInvDConesDecisionDistributions[x] = new DecisionDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.isDominatedBy(x, y, informationTable)) {// x InvD y
					this.positiveInvDConesDecisionDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Calculates decision distributions in negative dominance cones w.r.t. (inverse) dominance relation InvD.
	 * 
	 * @param informationTable information table for which decision distributions in negative dominance cones w.r.t. (inverse) dominance relation InvD should be calculated
	 */
	protected void calculateNegativeInvDConesDecisionDistributions(InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		
		for (int x = 0; x < numberOfObjects; x++) { //object being in the origin of dominance cone
			this.negativeInvDConesDecisionDistributions[x] = new DecisionDistribution();
			
			for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
				if (DominanceChecker.isDominatedBy(y, x, informationTable)) {// y InvD x
					this.negativeInvDConesDecisionDistributions[x].increaseCount(informationTable.getDecision(y));
				}
			}
		}
	}
	
	/**
	 * Gets distribution (histogram) of decisions ({@link DecisionDistribution}) in positive dominance cone w.r.t. (straight) dominance relation D (y D x &lt;=&gt; y dominates x),
	 * originating in object x addresses by the given index. Formally, D^+(x) = {y \in U : y D x}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision distribution in positive dominance cone w.r.t. (straight) dominance relation D, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 * @throws NullPointerException if this object has been created using {@link #DominanceConesDecisionDistributions(InformationTable, boolean)} with second parameter set to {@code true}
	 */
	public DecisionDistribution getPositiveDConeDecisionClassDistribution(int objectIndex) {
		return this.positiveDConesDecisionDistributions[objectIndex];
	}
	
	/**
	 * Gets distribution (histogram) of decisions ({@link DecisionDistribution}) in negative dominance cone w.r.t. (straight) dominance relation D (x D y &lt;=&gt; x dominates y),
	 * originating in object x addresses by the given index. Formally, D^-(x) = {y \in U : x D y}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision distribution in negative dominance cone w.r.t. (straight) dominance relation D, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 */
	public DecisionDistribution getNegativeDConeDecisionClassDistribution(int objectIndex) {
		return this.negativeDConesDecisionDistributions[objectIndex];
	}
	
	/**
	 * Gets distribution (histogram) of decisions ({@link DecisionDistribution}) in positive dominance cone w.r.t. (inverse) dominance relation InvD (x InvD y &lt;=&gt; x is dominated by y),
	 * originating in object x addresses by the given index. Formally, InvD^+(x) = {y \in U : x InvD y}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision distribution in positive dominance cone w.r.t. (inverse) dominance relation InvD, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 */
	public DecisionDistribution getPositiveInvDConeDecisionClassDistribution(int objectIndex) {
		return this.positiveInvDConesDecisionDistributions[objectIndex];
	}
	
	/**
	 * Gets distribution (histogram) of decisions ({@link DecisionDistribution}) in negative dominance cone w.r.t. (inverse) dominance relation InvD (y InvD x &lt;=&gt; y is dominated by x),
	 * originating in object x addresses by the given index. Formally, InvD^-(x) = {y \in U : y InvD x}.
	 * 
	 * @param objectIndex index of an object x from an information table, considered to be the origin of dominance cone
	 * @return decision distribution in negative dominance cone w.r.t. (inverse) dominance relation InvD, originating in object x having given index
	 * 
	 * @throws IndexOutOfBoundsException if given object index is lower than zero or exceeds {code{@link #getNumberOfObjects()}-1}
	 * @throws NullPointerException if this object has been created using {@link #DominanceConesDecisionDistributions(InformationTable, boolean)} with second parameter set to {@code true}
	 */
	public DecisionDistribution getNegativeInvDConeDecisionClassDistribution(int objectIndex) {
		return this.negativeInvDConesDecisionDistributions[objectIndex];
	}
	
}
