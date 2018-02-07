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

/**
 * Data available for the learning process, composed of an information table and preference information concerning the objects in the information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class LearningData {
	
	/**
	 * Information table used in the learning process.
	 */
	protected InformationTable informationTable;
	/**
	 * Preference information concerning objects present in the information table.
	 */
	protected PreferenceInformation preferenceInformation;
	
	/**
	 * Constructor storing an information table and preference information concerning objects in the information table.
	 * If there is no additional information, preference information may be {@code null}. 
	 * 
	 * @param informationTable information table storing evaluations of objects
	 * @param preferenceInformation preference information concerning the objects from the given information table
	 * 
	 * @throws NullPointerException if information table is {@code null}
	 */
	public LearningData(InformationTable informationTable, PreferenceInformation preferenceInformation) {
		if (informationTable == null) {
			throw new NullPointerException("Information table is null.");
		}
		
		this.informationTable = informationTable;
		this.preferenceInformation = preferenceInformation;
	}

	/**
	 * Gets the information table. 
	 * 
	 * @return the information table
	 */
	public InformationTable getInformationTable() {
		return this.informationTable;
	}

	/**
	 * Gets preference information concerning objects that this learning data concern.
	 * 
	 * @return preference information concerning objects that this learning data concern
	 */
	public PreferenceInformation getPreferenceInformation() {
		return this.preferenceInformation;
	}
	
	/**
	 * Selects a subset of this learning data that correspond to objects with given indices.
	 * Returns new learning data concerning a subset of objects.
	 * 
	 * @param objectIndices indices of objects to select to new learning data (indices can repeat)
	 * @return subset of this learning data, concerning only objects whose index is in the given array
	 * 
	 * @throws NullPointerException if given array with object indices is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public LearningData select(int[] objectIndices) {
		return select(objectIndices, false);
	}
	
	/**
	 * Selects a subset of this learning data that correspond to objects with given indices.
	 * Returns new learning data concerning a subset of objects.
	 * 
	 * @param objectIndices indices of objects to select to new learning data (indices can repeat)
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning read-only learning data, or should return safe learning data (that can be
	 *        modified), at the cost of returning the result slower
	 * @return subset of this learning data, concerning only objects whose index is in the given array
	 * 
	 * @throws NullPointerException if given array with object indices is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public LearningData select(int[] objectIndices, boolean accelerateByReadOnlyResult) {
		InformationTable newInformationTable = this.informationTable.select(objectIndices, accelerateByReadOnlyResult);
		PreferenceInformation newPreferenceInformation = this.preferenceInformation.select(objectIndices);
		
		return new LearningData(newInformationTable, newPreferenceInformation);
	}

}
