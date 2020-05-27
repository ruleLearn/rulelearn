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

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * Mapper from object's index to its unique id, and vice versa. Each object's index corresponds to a single information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Index2IdMapper {
	
	/**
	 * Maps object's index to its id - objectIndex2Id[objectIndex] == objectId.
	 */
	int[] objectIndex2Id = null;
	
	/**
	 * Maps object's id to its index - id2ObjectIndexMap.get(objectId) == objectIndex
	 */
	Int2IntMap id2ObjectIndexMap = null;
	
	/**
	 * Constructor of this mapper that memorizes mapping between object's index
	 * (equal to array index) and unique object's id.
	 * 
	 * @param objectIndex2Id array mapping object's index to unique object's id - {@code objectIndex2Id[objectIndex] == objectId}
	 * @throws NullPointerException if given array is {@code null}
	 */
	public Index2IdMapper(int[] objectIndex2Id) {
		this(objectIndex2Id, false);
	}
	
	/**
	 * Constructor of this mapper that memorizes mapping between object's index
	 * (equal to array index) and unique object's id.<br>
	 * <br>
	 * This constructor can be used in certain circumstances to accelerate object construction.
	 * 
	 * @param objectIndex2Id array mapping object's index to unique object's id - {@code objectIndex2Id[objectIndex] == objectId}
	 * @param accelerateByReadOnlyParam tells if construction of this object should be accelerated by assuming that the given reference
	 *        to an array of unique identifiers is not going to be used outside this class to modify that array
	 *        (and thus, this object does not need to clone the array for internal read-only use)
	 * @throws NullPointerException if given array is {@code null}
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public Index2IdMapper(int[] objectIndex2Id, boolean accelerateByReadOnlyParam) {
		this.objectIndex2Id = accelerateByReadOnlyParam ? objectIndex2Id : objectIndex2Id.clone();
		this.id2ObjectIndexMap = new Int2IntOpenHashMap(objectIndex2Id.length);
		for (int i = 0; i < objectIndex2Id.length; i++) {
			this.id2ObjectIndexMap.put(objectIndex2Id[i], i);
		}
	}
	
	/**
	 * Gets unique id of an object (row) having a given index in an information table.
	 * 
	 * @param objectIndex index of an object in an information table
	 * @return unique id of an object in an information table
	 * @throws IndexOutOfBoundsException if given index is lower than zero or is not smaller than {@link #getNumberOfObjects()}
	 */
	public int getId(int objectIndex) {
		return objectIndex2Id[objectIndex];
	}
	
	/**
	 * Gets index of an object from an information table having given unique id.
	 * 
	 * @param id unique id of an object in an information table
	 * @return index of an object from an information table having given unique id, or -1 if there is no mapping for the given id
	 */
	public int getIndex(int id) {
		if (id2ObjectIndexMap.containsKey(id)) {
			return id2ObjectIndexMap.get(id);
		} else {
			return -1;
		}
	}
	
	/**
	 * Gets number of objects whose indices are mapped to unique identifiers.
	 * 
	 * @return number of objects whose indices are mapped to unique identifiers.
	 */
	public int getNumberOfObjects() {
		return this.objectIndex2Id.length;
	}

}
