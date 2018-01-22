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
		this.preferenceInformation = (Field[])preferenceInformation.toArray();
	}
	
	/**
	 * Gets number of learning objects for which there is stored preference information.
	 * 
	 * @return number of learning objects for which there is stored preference information
	 */
	public int getNumberOfObjects() {
		return this.preferenceInformation.length;
	}
}
