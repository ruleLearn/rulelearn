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

import java.util.ArrayList;
import java.util.List;

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.types.Field;

/**
 * Table storing data, i.e., fields corresponding to objects and attributes (both condition and description ones).
 * Each field is identified by object's index and attribute's index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTable {
	
	/**
	 * All attributes of an information table (condition and description ones together).
	 */
	protected Attribute[] attributes;
	
	/**
	 * Mapper translating object's index to its unique id.
	 */
	protected Index2IdMapper mapper;
	
	/**
	 * Sub-table, corresponding to condition attributes only.
	 */
	protected Table conditionTable;
	/**
	 * Sub-table, corresponding to description attributes only.
	 */
	protected Table descriptionTable;
	
	/**
	 * Information table constructor.
	 * 
	 * @param attributes all attributes of an information table (condition and description ones together)
	 * @param fields list of field vectors; each vector contains condition and description field values
	 *        of a single object of this information table
	 * @param mapper translating object's index to it's unique id
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public InformationTable(Attribute[] attributes, List<Field[]> fields, Index2IdMapper mapper) {
		this(attributes, fields, mapper, false);
	}
	
	/**
	 * Information table constructor.<br>
	 * <br>
	 * This constructor can be used in certain circumstances to accelerate object construction.
	 * 
	 * @param attributes all attributes of an information table (condition and description ones together)
	 * @param fields list of field vectors; each vector contains condition and description field values
	 *        of a single object of this information table
	 * @param mapper translating object's index to it's unique id
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given reference
	 *        to an array of attributes and references to arrays of fields present at the given list are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal use)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws IllegalArgumentException if given list of {@link Field} arrays is empty
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public InformationTable(Attribute[] attributes, List<Field[]> fields, Index2IdMapper mapper, boolean accelerateByReadOnlyParams) {
		if (fields.isEmpty()) {
			throw new IllegalArgumentException("Information table cannot be created with and empty list of values of objects.");
		}
		
		if (attributes.length != fields.get(0).length) {
			//TODO: use customized runtime exception subclass
			throw new RuntimeException("The number of attributes and object's fields in an information table do not match.");
		}
		
		if (mapper == null) {
			throw new NullPointerException("Mapper is null.");	
		}
		
		int numberOfConditionAttributes = 0;
		int numberOfDescriptionAttributes = 0;
		
		for (int i = 0; i < attributes.length; i++) { //scout
			if (attributes[i].getType() == AttributeType.CONDITION) {
				numberOfConditionAttributes++;
			} else if (attributes[i].getType() == AttributeType.DESCRIPTION) {
				numberOfDescriptionAttributes++;
			}
		}
		
		Attribute[] conditionAttributes = new Attribute[numberOfConditionAttributes];
		Attribute[] descriptionAttributes = new Attribute[numberOfDescriptionAttributes];
		int conditionAttributeIndex = 0;
		int descriptionAttributeIndex = 0;
		
		for (int i = 0; i < attributes.length; i++) { //split attributes
			if (attributes[i].getType() == AttributeType.CONDITION) {
				conditionAttributes[conditionAttributeIndex++] = attributes[i];
			} else if (attributes[i].getType() == AttributeType.DESCRIPTION) {
				descriptionAttributes[descriptionAttributeIndex++] = attributes[i];
			}
		}
		
		List<Field[]> conditionFieldsList = new ArrayList<>();
		List<Field[]> descritpionFieldsList = new ArrayList<>();
		
		Field[] conditionFields;
		Field[] descriptionFields;
		
		//split fields
		for (Field[] values : fields) { //choose row (single object)
			conditionFields = new Field[numberOfConditionAttributes];
			descriptionFields = new Field[numberOfDescriptionAttributes];
			conditionAttributeIndex = 0;
			descriptionAttributeIndex = 0;
			
			for (int i = 0; i < attributes.length; i++) { //choose column (single attribute)
				if (attributes[i].getType() == AttributeType.CONDITION) {
					conditionFields[conditionAttributeIndex++] = values[i];
				} else if (attributes[i].getType() == AttributeType.DESCRIPTION) {
					descriptionFields[descriptionAttributeIndex++] = values[i];
				} 
			}
			
			conditionFieldsList.add(conditionFields);
			descritpionFieldsList.add(descriptionFields);
		}
		
		this.attributes = accelerateByReadOnlyParams ? attributes : attributes.clone(); //remember all attributes (both condition and description ones)
		
		this.conditionTable = new Table(conditionAttributes, conditionFieldsList, mapper, true);
		this.descriptionTable = new Table(descriptionAttributes, descritpionFieldsList, mapper, true);
		
		this.mapper = mapper;
	}
	
//	public InformationTable(String metadataPath, String dataPath) {

//}
	
	/**
	 * Gets all attributes of this information table (condition and description ones together).
	 *  
	 * @return array with all attributes of this information table
	 */
	public Attribute[] getAttributes() {
		return this.attributes.clone();
	}
	
	/**
	 * Gets all attributes of this information table (condition and description ones together).
	 *  
	 * @return array with all attributes of this information table
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Attribute[] getAttributes(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? this.attributes : this.attributes.clone();
	}
	
}
