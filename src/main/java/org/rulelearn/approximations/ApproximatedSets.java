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

package org.rulelearn.approximations;

import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;

/**
 * Container for all approximated sets that can be defined with respect to an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class ApproximatedSets {

	/**
	 * Information table for which all approximated sets stored in this container are defined.
	 */
	InformationTable informationTable = null;
	
	/**
	 * Rough set calculator used to calculate approximations of all approximated sets stored in this container.
	 */
	RoughSetCalculator<? extends ApproximatedSet> roughSetCalculator = null;

	/**
	 * Gets information table for which all approximated sets stored in this container are defined.
	 * 
	 * @return information table for which all approximated sets stored in this container are defined
	 */
	public InformationTable getInformationTable() {
		return this.informationTable;
	}
	
	/**
	 * Gets rough set calculator used to calculate approximations of all approximated sets stored in this container.
	 * 
	 * @return rough set calculator used to calculate approximations of all approximated sets stored in this container
	 */
	public RoughSetCalculator<? extends ApproximatedSet> getRoughSetCalculator() {
		return roughSetCalculator;
	}

	/**
	 * Gets quality of approximation of all approximated sets which can be defined for information table.
	 * 
	 * @return quality of approximation of all approximated sets which can be defined for information table
	 */
	public abstract double getQualityOfApproximation();

	/**
	 * Constructs this container of approximated sets that can be defined with respect to an information table.
	 * 
	 * @param informationTable information table for which all approximated sets stored in this container are defined
	 * @param roughSetCalculator rough set calculator used to calculate approximations of all approximated sets stored in this container
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public ApproximatedSets(InformationTable informationTable, RoughSetCalculator<? extends ApproximatedSet> roughSetCalculator) {
		super();
		this.informationTable = Precondition.notNull(informationTable, "Information table determining approximated sets is null.");
		this.roughSetCalculator = Precondition.notNull(roughSetCalculator, "Rough set calculator determining approximated sets is null.");
	}
	
	 
	
}
