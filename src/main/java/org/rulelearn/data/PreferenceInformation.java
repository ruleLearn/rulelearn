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
import org.rulelearn.types.Field;

/**
 * Preference information concerning learning objects, available in the considered decision problem (e.g., supplied by a decision maker as values of an active decision attribute).
 * This information may concern, e.g., decision class assignments or relations for pairs of learning objects. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class PreferenceInformation {
	
	/**
	 * List of preference information for subsequent objects; i-th entry stores preference information for i-th object.
	 */
	protected Field[] preferenceInformation;

	/**
	 * Gets preference information for an object (example, pair of examples) identified by the given index.
	 * 
	 * @param objectIndex index identifying an object
	 * @return preference information (e.g., decision class label, relation label) corresponding to the object with given index
	 * @throws IndexOutOfBoundsException if given object's index is out of range of indices of objects for which preference information has been stored
	 */
	public Field getPreferenceInformation(int objectIndex) {
		return preferenceInformation[objectIndex];
	}
	
	/**
	 * Constructor storing preference information for each object whose index corresponds to an entry in the given list.
	 *  
	 * @param preferenceInformation list of preference informations for subsequent objects;
	 *        i-th entry stores preference information for i-th object
	 * @throws NullPointerException if the given list is {@code null}
	 */
	public PreferenceInformation(List<Field> preferenceInformation) {
		int size = preferenceInformation.size();
		this.preferenceInformation = new Field[size];
		
		for (int i = 0; i < size; i++) {
			this.preferenceInformation[i] = preferenceInformation.get(i);
		}
	}
	
	/**
	 * Constructor storing preference information for each object whose index corresponds to an entry in the given array.
	 * 
	 * @param preferenceInformation array of preference informations for subsequent objects;
	 *        i-th entry stores preference information for i-th object
	 * @param accelerateByReadOnlyParam tells if construction of this object should be accelerated by assuming that the given reference
	 *        to an array of fields is not going to be used outside this class
	 *        to modify that array (and thus, this object does not need to clone the array for internal use)
	 *        
	 * @throws NullPointerException if the given array is {@code null}
	 */
	public PreferenceInformation(Field[] preferenceInformation, boolean accelerateByReadOnlyParam) {
		if (preferenceInformation == null) {
			throw new NullPointerException("Preference information is null.");
		}
		
		this.preferenceInformation = accelerateByReadOnlyParam ? preferenceInformation : preferenceInformation.clone();
	}
	
	/**
	 * Gets number of learning objects for which there is stored preference information.
	 * 
	 * @return number of learning objects for which there is stored preference information
	 */
	public int getNumberOfObjects() {
		return this.preferenceInformation.length;
	}
	
	/**
	 * Selects a subset of this preference information that correspond to objects with given indices.
	 * Returns new preference information concerning a subset of objects.
	 * 
	 * @param objectIndices indices of objects to select to new preference information (indices can repeat)
	 * @return subset of this preference information, concerning only objects whose index is in the given array
	 * 
	 * @throws NullPointerException if the given array is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public PreferenceInformation select(int[] objectIndices) {
		Field[] newPreferenceInformation = new Field[objectIndices.length];
		
		for (int i = 0; i < objectIndices.length; i++) {
			newPreferenceInformation[i] = this.preferenceInformation[objectIndices[i]];
		}
		
		return new PreferenceInformation(newPreferenceInformation, true);
	}
	
}
