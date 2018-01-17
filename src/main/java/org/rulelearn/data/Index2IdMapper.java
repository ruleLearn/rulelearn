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
 * Mapper from object's index to its unique id. Index corresponds to a single information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Index2IdMapper {
	
	/**
	 * Maps object's index to its id - objectIndex2Id[objectIndex] == objectId.
	 */
	protected int[] objectIndex2Id;
	
	/**
	 * Constructor of this mapper that memorizes mapping between object's index
	 * (array index) and unique object's id.
	 * 
	 * @param objectIndex2Id array mapping object's index to unique object's id - {@code objectIndex2Id[objectIndex] == objectId}
	 * @throws NullPointerException if given array is {@code null}
	 */
	public Index2IdMapper(int[] objectIndex2Id) {
		this.objectIndex2Id = objectIndex2Id.clone();
	}
	
	/**
	 * Gets unique id of an object (row) having a given index in an information table.
	 * 
	 * @param objectIndex index of an object in an information table
	 * @return unique id of an object in an information table
	 * @throws IndexOutOfBoundsException if given index is lower than zero or is not smaller than {@link #getSize()}
	 */
	public int getId(int objectIndex) {
		return objectIndex2Id[objectIndex];
	}
	
	/**
	 * Gets number of objects whose indices are mapped to unique identifiers.
	 * 
	 * @return number of objects whose indices are mapped to unique identifiers.
	 */
	public int getSize() {
		return this.objectIndex2Id.length;
	}

}
