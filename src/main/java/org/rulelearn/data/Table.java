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
 * Table storing data, i.e., fields corresponding to objects and attributes.
 * Each field is identified by object's index and attribute's index. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class Table {
	
	protected Attribute[] attributes;
	protected List<Field[]> fields;
	protected Index2IdMapper mapper;
	
	/**
	 * Constructs this table.
	 * 
	 * @param attributes attributes corresponding two columns of this table
	 * @param fields fields corresponding two rows of this table
	 * @param mapper translator of object's index, which is meaningful in this table only,
	 *        to unique object's id, which is meaningful in general
	 */
	public Table(Attribute[] attributes, List<Field[]> fields, Index2IdMapper mapper) {
		//TODO
		this.attributes = attributes.clone();
		this.fields = fields;
		this.mapper = mapper;
	}
	
	/**
	 * Gets field of this table for the object and attribute identified by the given indices
	 * 
	 * @param objectIndex index of an object (row of the table)
	 * @param attributeIndex index of an attribute (column of the table)
	 * @return field of this table corresponding to given indices
	 */
	public Field getField(int objectIndex, int attributeIndex) {
		return this.fields.get(objectIndex)[attributeIndex];
	}
		
	/**
	 * Gets fields of this table for the object identified by the given index
	 * 
	 * @param objectIndex index of an object (row of the table)
	 * @return fields of this table corresponding to given index
	 */
	public Field[] getFields(int objectIndex) {
		return this.fields.get(objectIndex);
	}
	
	/**
	 * Selects rows of this table that correspond to objects with given indices.
	 *  
	 * @param objectIndices indices of objects to select to new information table (indices can repeat)
	 * @return sub-table of this table, containing only rows corresponding to objects whose index is on the given list
	 */
	public Table select(int[] objectIndices) {
		//TODO
		return null;
	}
	
}
