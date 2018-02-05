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
	 * Maps index of an attribute of this information table to index of a learning (i.e., active and condition)/supplementary (i.e., other) attribute.
	 * Index of a supplementary attribute among supplementary attributes is multiplied by -1 and shifted by -1.
	 * Suppose that there are five active attributes of the following types:<br>
	 * {@link AttributeType#CONDITION}, {@link AttributeType#DESCRIPTION}, {@link AttributeType#CONDITION}, {@link AttributeType#CONDITION}, {@link AttributeType#DESCRIPTION}.
	 * Then, the map will be the following:<br>
	 * attributeMap = [0, -1, 1, 2, -2].<br>
	 * attributeMap[0] == 0 means that 0-indexed attribute among all attributes is 0-indexed attribute among learning attributes.<br> 
	 * attributeMap[1] == -1 means that 1-indexed attribute among all attributes is 0-indexed attribute among supplementary attributes.<br>
	 * attributeMap[2] == 1 means that 2-indexed attribute among all attributes is 1-indexed attribute among learning attributes.<br>
	 * attributeMap[3] == 2 means that 3-indexed attribute among all attributes is 2-indexed attribute among learning attributes.<br>
	 * attributeMap[4] == -2 means that 4-indexed attribute among all attributes is 1-indexed attribute among supplementary attributes (-2 == (1 * -1) + (-1)).
	 */
	protected int[] attributeMap;
	
	/**
	 * Information table constructor.
	 * 
	 * @param attributes all attributes of an information table (condition and description ones, both active and non-active)
	 * @param fields list of fields of subsequent objects; each array contains subsequent fields of a single object in this information table;
	 *        it is assumed that each array is of the same length (i.e., the number of fields of each object is the same)
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
	 * @param fields list of fields of subsequent objects; each array contains subsequent fields of a single object in this information table;
	 *        it is assumed that each array is of the same length (i.e., the number of fields of each object is the same)
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
		
		int numberOfLearningAttributes = 0;
		
		for (int i = 0; i < attributes.length; i++) { //scout first
			if (isLearningAttribute(attributes[i])) {
				numberOfLearningAttributes++;
			}
		}
		
		int numberOfSupplementaryAttributes = attributes.length - numberOfLearningAttributes;
		
		boolean hasLearningAttributes = numberOfLearningAttributes > 0;
		boolean hasSupplementaryAttributes = numberOfSupplementaryAttributes > 0;
		
		Attribute[] learningAttributes = hasLearningAttributes ? new Attribute[numberOfLearningAttributes] : null;
		Attribute[] supplementaryAttributes = hasSupplementaryAttributes ? new Attribute[numberOfSupplementaryAttributes] : null;
		
		int learningAttributeIndex = 0;
		int supplementaryAttributeIndex = 0;
		
		this.attributeMap = new int[attributes.length];
		
		//split attributes into two tables
		for (int i = 0; i < attributes.length; i++) {
			if (isLearningAttribute(attributes[i])) {
				learningAttributes[learningAttributeIndex] = attributes[i];
				this.attributeMap[i] = learningAttributeIndex;
				learningAttributeIndex++;
			} else { //supplementary attribute
				supplementaryAttributes[supplementaryAttributeIndex] = attributes[i];
				this.attributeMap[i] = this.encodeSupplementaryAttributeIndex(supplementaryAttributeIndex);
				supplementaryAttributeIndex++;
			}
		}
		
		Field[][] learningFieldsArray = hasLearningAttributes ? new Field[fields.size()][] : null;
		Field[][] supplementaryFieldsArray = hasSupplementaryAttributes ? new Field[fields.size()][] : null;
		
		Field[] learningFields = null;
		Field[] supplementaryFields = null;
		
		int rowIndex = 0;
		
		//split fields into two tables
		for (Field[] values : fields) { //choose row (single object)
			if (hasLearningAttributes) {
				learningFields = new Field[numberOfLearningAttributes];
				learningAttributeIndex = 0;
			}
			if (hasSupplementaryAttributes) {
				supplementaryFields = new Field[numberOfSupplementaryAttributes];
				supplementaryAttributeIndex = 0;
			}
			
			for (int i = 0; i < attributes.length; i++) { //choose column (single attribute)
				if (isLearningAttribute(attributes[i])) {
					learningFields[learningAttributeIndex++] = values[i];
				} else { //supplementary attribute
					supplementaryFields[supplementaryAttributeIndex++] = values[i];
				} 
			}
			
			if (hasLearningAttributes) {
				learningFieldsArray[rowIndex] = learningFields;
			}
			if (hasSupplementaryAttributes) {
				supplementaryFieldsArray[rowIndex] = supplementaryFields;
			}
			
			rowIndex++;
		}
		
		this.attributes = accelerateByReadOnlyParams ? attributes : attributes.clone(); //remember all attributes
		//map each object (row of this information table) to a unique id, and remember that mapping
		this.mapper = new Index2IdMapper(UniqueIdGenerator.getInstance().getUniqueIds(fields.size()), true);
		
		this.learningTable = hasLearningAttributes ? new Table(learningAttributes, learningFieldsArray, this.mapper, true) : null;
		this.supplementaryTable = hasSupplementaryAttributes ? new Table(supplementaryAttributes, supplementaryFieldsArray, this.mapper, true) : null;
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
	 * @return sub-table of this information table, corresponding to all attributes which are not active or not condition ones
	 */
	public Table getSupplementaryTable() {
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
		if (this.attributeMap[attributeIndex] >= 0) { //learning attribute
			return this.learningTable.getField(objectIndex, this.attributeMap[attributeIndex]);
		} else { //supplementary attribute
			return this.supplementaryTable.getField(objectIndex, this.decodeOtherAttributeIndex(this.attributeMap[attributeIndex]));
		}
	}
	
	private int encodeSupplementaryAttributeIndex(int index) {
		return -index - 1;
	}
	
	private int decodeOtherAttributeIndex(int encodedOtherAttributeIndex) {
		return -encodedOtherAttributeIndex - 1;
	}
	
	/**
	 * Tells if given attribute is a learning attribute, i.e., it is a condition and active attribute.
	 * 
	 * @param attribute attribute to check
	 * @return {@code true} if given attribute is condition and active, {@code false otherwise}
	 */
	protected boolean isLearningAttribute(Attribute attribute) {
		return attribute.getType() == AttributeType.CONDITION && attribute.isActive();
	}
	
	/**
	 * TODO: write documentation
	 * 
	 * @param objectIndices
	 * @return
	 */
	public InformationTable select(int[] objectIndices) {
		return select(objectIndices, false);
	}
	
	/**
	 * TODO: write documentation
	 * 
	 * @param objectIndices
	 * @param accelerateByReadOnlyResult
	 * @return
	 */
	public InformationTable select(int[] objectIndices, boolean accelerateByReadOnlyResult) {
		//TODO: implement
		return null;
	}
	
}
