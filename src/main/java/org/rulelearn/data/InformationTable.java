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
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.types.Field;

/**
 * Table storing data, i.e., fields corresponding to all considered objects and all specified attributes (condition and description ones, both active and non-active).
 * Each field is identified by object's index and attribute's index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTable {
	
	/**
	 * All attributes of this information table (regardless of their type and regardless of the fact if they are active or not).
	 */
	protected Attribute[] attributes;
	
	/**
	 * Mapper translating object's index to its unique id.
	 */
	protected Index2IdMapper mapper;
	
	/**
	 * Sub-table, corresponding to active condition attributes only.
	 * This sub-table is used in calculations.
	 */
	protected Table learningTable = null;
	/**
	 * Sub-table, corresponding to all attributes which are not active or not condition ones.
	 * This sub-table is not used in calculations. It stores values of such supplementary attributes
	 * mainly for the on-screen presentation of data and their write-back to file.
	 */
	protected Table supplementaryTable = null;
	
	/**
	 * Maps index of an attribute of this information table to index of an (active and condition)/other attribute. Index of other attribute among other attributes is multiplied by -1 and shifted by -1.
	 * Suppose that there are five active attributes of the following types:<br>
	 * {@link AttributeType#CONDITION}, {@link AttributeType#DESCRIPTION}, {@link AttributeType#CONDITION}, {@link AttributeType#CONDITION}, {@link AttributeType#DESCRIPTION}.
	 * Then, the map will be the following:<br>
	 * attributeMap = [0, -1, 1, 2, -2].<br>
	 * attributeMap[0] == 0 means that 0-indexed attribute among all attributes is 0-indexed attribute among active condition attributes.<br> 
	 * attributeMap[1] == -1 means that 1-indexed attribute among all attributes is 0-indexed attribute among other attributes.<br>
	 * attributeMap[2] == 1 means that 2-indexed attribute among all attributes is 1-indexed attribute among active condition attributes.<br>
	 * attributeMap[3] == 2 means that 3-indexed attribute among all attributes is 2-indexed attribute among active condition attributes.<br>
	 * attributeMap[4] == -2 means that 4-indexed attribute among all attributes is 1-indexed attribute among other attributes (-2 == (1 * -1) + (-1)).
	 */
	protected int[] attributeMap;
	
	/**
	 * Information table constructor.
	 * 
	 * @param attributes all attributes of an information table (condition and description ones, both active and non-active)
	 * @param fields list of fields of subsequent objects; each array contains subsequent fields of a single object of this information table;
	 *        it is assumed that each array is of the same length
	 *        (i.e., the number of fields of each object is the same)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public InformationTable(Attribute[] attributes, List<Field[]> fields) {
		this(attributes, fields, false);
	}
	
	/**
	 * Information table constructor.<br>
	 * <br>
	 * This constructor can be used in certain circumstances to accelerate object construction.
	 * 
	 * @param attributes all attributes of an information table (condition and description ones, both active and non-active)
	 * @param fields list of fields of subsequent objects; each array contains subsequent fields of a single object of this information table;
	 *        it is assumed that each array is of the same length
	 *        (i.e., the number of fields of each object is the same)
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given reference
	 *        to an array of attributes and references to arrays of fields present at the given list are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal use)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if the number of attributes and the number of fields corresponding to one object
	 *         (i.e., stored in a single array) do not match
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public InformationTable(Attribute[] attributes, List<Field[]> fields, boolean accelerateByReadOnlyParams) {
		if (fields.size() > 0 && attributes.length != fields.get(0).length) {
			throw new InvalidValueException("The number of attributes and the number of objects' fields in an information table do not match.");
		}
		
		int numberOfActiveConditionAttributes = 0;
		
		for (int i = 0; i < attributes.length; i++) { //scout first
			if (attributes[i].getType() == AttributeType.CONDITION &&
					attributes[i].isActive()) { //active condition attribute
				numberOfActiveConditionAttributes++;
			}
		}
		
		int numberOfOtherAttributes = attributes.length - numberOfActiveConditionAttributes;
		
		boolean hasActiveConditionAttributes = numberOfActiveConditionAttributes > 0;
		boolean hasOtherAttributes = numberOfOtherAttributes > 0;
		
		Attribute[] activeConditionAttributes = hasActiveConditionAttributes ? new Attribute[numberOfActiveConditionAttributes] : null;
		Attribute[] otherAttributes = hasOtherAttributes ? new Attribute[numberOfOtherAttributes] : null;
		
		int activeConditionAttributeIndex = 0;
		int otherAttributeIndex = 0;
		
		this.attributeMap = new int[attributes.length];
		
		for (int i = 0; i < attributes.length; i++) { //split attributes
			if (attributes[i].getType() == AttributeType.CONDITION &&
					attributes[i].isActive()) { //active condition attribute
				activeConditionAttributes[activeConditionAttributeIndex] = attributes[i];
				this.attributeMap[i] = activeConditionAttributeIndex;
				activeConditionAttributeIndex++;
			} else { //other attribute
				otherAttributes[otherAttributeIndex] = attributes[i];
				this.attributeMap[i] = this.encodeOtherAttributeIndex(otherAttributeIndex);
				otherAttributeIndex++;
			}
		}
		
		Field[][] activeConditionFieldsList = hasActiveConditionAttributes ? new Field[fields.size()][] : null;
		Field[][] otherFieldsList = hasOtherAttributes ? new Field[fields.size()][] : null;
		
		Field[] activeConditionFields = null;
		Field[] otherFields = null;
		
		int rowIndex = 0;
		
		//split fields into two tables
		for (Field[] values : fields) { //choose row (single object)
			if (hasActiveConditionAttributes) {
				activeConditionFields = new Field[numberOfActiveConditionAttributes];
				activeConditionAttributeIndex = 0;
			}
			if (hasOtherAttributes) {
				otherFields = new Field[numberOfOtherAttributes];
				otherAttributeIndex = 0;
			}
			
			for (int i = 0; i < attributes.length; i++) { //choose column (single attribute)
				if (attributes[i].getType() == AttributeType.CONDITION &&
						attributes[i].isActive()) { //active condition attribute
					activeConditionFields[activeConditionAttributeIndex++] = values[i];
				} else { //other attribute
					otherFields[otherAttributeIndex++] = values[i];
				} 
			}
			
			if (hasActiveConditionAttributes) {
				activeConditionFieldsList[rowIndex] = activeConditionFields;
			}
			if (hasOtherAttributes) {
				otherFieldsList[rowIndex] = otherFields;
			}
			
			rowIndex++;
		}
		
		this.attributes = accelerateByReadOnlyParams ? attributes : attributes.clone(); //remember all attributes
		//map each object (row of this information table) to a unique id, and remember that mapping
		this.mapper = new Index2IdMapper(UniqueIdGenerator.getInstance().getUniqueIds(fields.size()), true);
		
		this.learningTable = hasActiveConditionAttributes? new Table(activeConditionAttributes, activeConditionFieldsList, this.mapper, true) : null;
		this.supplementaryTable = hasOtherAttributes? new Table(otherAttributes, otherFieldsList, this.mapper, true) : null;
	}
	
	//	public InformationTable(String metadataPath, String dataPath) {

	//}
	
	/**
	 * Gets sub-table of this information table, corresponding to active condition attributes only.
	 * If there is no such attribute, then returns {@code null}.
	 * 
	 * @return sub-table of this information table, corresponding to active condition attributes only
	 */
	public Table getLearningTable() {
		return this.learningTable;
	}

	/**
	 * Gets sub-table of this information table, corresponding to all attributes which are not active or not condition ones.
	 * If there is no such attribute, then returns {@code null}.
	 * 
	 * @return sub-table of this information table, corresponding to all attributes which are not active or not condition ones.
	 */
	public Table getDescriptionTable() {
		return this.supplementaryTable;
	}

	/**
	 * Gets all attributes of this information table (regardless of their type and regardless of the fact if they are active or not).
	 *  
	 * @return array with all attributes of this information table
	 */
	public Attribute[] getAttributes() {
		return this.attributes.clone();
	}
	
	/**
	 * Gets all attributes of this information table (regardless of their type and regardless of the fact if they are active or not).<br>
	 * <br>
	 * This method can be used in certain circumstances to accelerate calculations.
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
	
	/**
	 * Gets mapper that maps indices of objects stored in this information table to their unique ids.
	 * 
	 * @return mapper that maps indices of objects stored in this information table to their unique ids
	 */
	public Index2IdMapper getIndex2IdMapper() {
		return this.mapper;
	}
	
	/**
	 * Gets field of this information table for the object and attribute identified by the given indices.
	 * 
	 * @param objectIndex index of an object (row of the table)
	 * @param attributeIndex index of an attribute (column of the table)
	 * @return field of this information table corresponding to given indices
	 * 
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object for which this table stores fields
	 * @throws IndexOutOfBoundsException if given attribute index does not correspond to any attribute for which this table stores fields
	 */
	public Field getField(int objectIndex, int attributeIndex) {
		if (this.attributeMap[attributeIndex] >= 0) { //active condition attribute
			return this.learningTable.getField(objectIndex, this.attributeMap[attributeIndex]);
		} else { //other attribute
			return this.supplementaryTable.getField(objectIndex, this.decodeOtherAttributeIndex(this.attributeMap[attributeIndex]));
		}
	}
	
	private int encodeOtherAttributeIndex(int index) {
		return -index - 1;
	}
	
	private int decodeOtherAttributeIndex(int encodedOtherAttributeIndex) {
		return -encodedOtherAttributeIndex - 1;
	}
	
}
