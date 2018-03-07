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
 * Table storing data, i.e., fields representing evaluations of all considered objects and all specified attributes
 * (condition, decision and description ones, both active and non-active).
 * Each field is identified by object's index and attribute's index.
 * An information table is allowed to have zero or exactly one active decision attribute.
 * An object's evaluations on this attribute may, e.g., indicate decision class to which this object is assigned.
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
	 * This sub-table is used in calculations. Equals to {@code null} if there are no active condition attributes.
	 */
	protected Table activeConditionEvaluations = null;
	/**
	 * Sub-table, corresponding to all attributes which are either not active or description ones.
	 * This sub-table is not used in calculations. It stores values of such supplementary attributes
	 * mainly for the on-screen presentation of data and their write-back to file.
	 * Equals to {@code null} if there are no such supplementary attributes.
	 */
	protected Table notActiveOrDescriptionEvaluations = null;
	
	/**
	 * Array of decisions concerning subsequent objects; i-th entry stores decision concerning i-th object.
	 * This array stores evaluations of objects on the only active decision attribute.
	 * Can be {@code null}, e.g., if this information table stores evaluations of test objects (for which decisions are unknown).
	 */
	protected Field[] activeDecisionAttributeFields = null;
	
	/**
	 * Index of the only active decision attribute used in calculations. If there is no such attribute, equals to -1.
	 */
	protected int activeDecisionAttributeIndex = -1;
	
	/**
	 * Array of identifiers concerning subsequent objects; i-th entry stores identifier of i-th object.
	 * This array stores identifiers of objects on the only active identification attribute.
	 * Can be {@code null}, if there is no such attribute.
	 */
	protected Field[] activeIdentificationAttributeFields = null;
	
	/**
	 * Index of the only active identification attribute used to identify objects. If there is no such attribute, equals to -1.
	 */
	protected int activeIdentificationAttributeIndex = -1;
	
	/**
	 * Maps global index of an attribute of this information table to encoded local index of: an active condition attribute, the only active decision attribute,
	 * or a non-active/description attribute.
	 * Local index concerns one of the above three groups of attributes (e.g., if 3-rd global attribute is the first active condition attribute,
	 * then {@code attributeMap[2]} will contain encoded local index 0).<br>
	 * Encoding of a local index of an active condition attribute is done by adding 1.<br>
	 * Encoding of a local index of a non-active/description attribute is done by subtracting 1.<br>
	 * Local index 0 of the only active decision attribute is not encoded.<br>
	 * <br>
	 * Suppose there are seven attributes:<br>
	 * #1: (active, {@link AttributeType#CONDITION}), #2: (active, {@link AttributeType#DESCRIPTION}), #3: (non-active, {@link AttributeType#CONDITION}),
	 * #4: (active, {@link AttributeType#CONDITION}), #5: (active, {@link AttributeType#DECISION}), #6: (non-active, {@link AttributeType#DECISION}),
	 * #7: (non-active, {@link AttributeType#DESCRIPTION}).<br>
	 * Then, the map will be the following:<br>
	 * attributeMap = [1, -1, -2, 2, 0, -3, -4].
	 */
	protected int[] attributeMap;
	
	/**
	 * Protected constructor for internal use only. Sets all data fields of this information table.
	 * 
	 * @param attributes all attributes of an information table (condition, decision, and description ones, both active and non-active)
	 * @param mapper translator of object's index, which is meaningful in this information table only,
	 *        to unique object's id, which is meaningful in general
	 * @param activeConditionEvaluations sub-table corresponding to active condition attributes
	 * @param notActiveOrDescriptionEvaluations sub-table corresponding to non-active/description attributes
	 * @param activeDecisionAttributeFields list of decisions concerning subsequent objects
	 * @param activeDecisionAttributeIndex index of the only active decision attribute used in calculations
	 * @param attributeMap see {@link #attributeMap}
	 * @param accelerateByReadOnlyParams tells if construction of this information table should be accelerated by assuming that the given references
	 *        to arrays are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal use)
	 */
	protected InformationTable(Attribute[] attributes, Index2IdMapper mapper, Table activeConditionEvaluations, Table notActiveOrDescriptionEvaluations,
			Field[] activeDecisionAttributeFields, int activeDecisionAttributeIndex, int[] attributeMap, boolean accelerateByReadOnlyParams) {
		this.attributes = accelerateByReadOnlyParams ? attributes : attributes.clone();
		this.mapper = mapper;
		this.activeConditionEvaluations = activeConditionEvaluations;
		this.notActiveOrDescriptionEvaluations = notActiveOrDescriptionEvaluations;
		this.activeDecisionAttributeFields = accelerateByReadOnlyParams ? activeDecisionAttributeFields : activeDecisionAttributeFields.clone();
		this.activeDecisionAttributeIndex = activeDecisionAttributeIndex;
		this.attributeMap = accelerateByReadOnlyParams ? attributeMap : attributeMap.clone();
	}
	
	
	/**
	 * Information table constructor. Assumes that the type of fields in i-th column is compatible with the type of attribute at i-th position.
	 * 
	 * @param attributes all attributes of an information table (condition, decision, and description ones, both active and non-active)
	 * @param fields list of fields of subsequent objects; each array contains subsequent fields of a single object (row) in this information table;
	 *        it is assumed that each array is of the same length (i.e., the number of fields of each object is the same)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if there is more than one active decision attribute
	 */
	public InformationTable(Attribute[] attributes, List<Field[]> fields) {
		this(attributes, fields, false);
	}
	
	/**
	 * Information table constructor. Assumes that the type of fields in i-th column is compatible with the type of attribute at i-th position.<br>
	 * <br>
	 * This constructor can be used in certain circumstances to accelerate information table construction (by not cloning arrays).
	 * 
	 * @param attributes all attributes of an information table (condition, decision, and description ones, both active and non-active)
	 * @param fields list of fields of subsequent objects; each array contains subsequent fields of a single object (row) in this information table;
	 *        it is assumed that each array is of the same length (i.e., the number of fields of each object is the same)
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given reference
	 *        to an array of attributes and references to arrays of fields present at the given list are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal use)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if the number of attributes and the number of fields corresponding to one object
	 *         (i.e., stored in a single array) do not match
	 * @throws InvalidValueException if there is more than one active decision attribute
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public InformationTable(Attribute[] attributes, List<Field[]> fields, boolean accelerateByReadOnlyParams) {
		if (fields.size() > 0 && attributes.length != fields.get(0).length) {
			throw new InvalidValueException("The number of attributes and the number of objects' fields in an information table do not match.");
		}
		
		int numberOfActiveConditionAttributes = 0;
		int numberOfActiveDecisionAttributes = 0;
		
		for (int i = 0; i < attributes.length; i++) { //scout attributes first
			if (isActiveConditionAttribute(attributes[i])) {
				numberOfActiveConditionAttributes++;
			} else if (isActiveDecisionAttribute(attributes[i])) {
				numberOfActiveDecisionAttributes++;
				if (numberOfActiveDecisionAttributes > 1) {
					throw new InvalidValueException("The number of active decision attributes is greater than 1.");
				}
			}
		}
		
		int numberOfNotActiveOrDescriptionAttributes = attributes.length - numberOfActiveConditionAttributes - numberOfActiveDecisionAttributes;
		
		boolean hasActiveConditionAttributes = numberOfActiveConditionAttributes > 0;
		boolean hasActiveDecisionAttribute = numberOfActiveDecisionAttributes > 0;
		boolean hasNotActiveOrDescriptionAttributes = numberOfNotActiveOrDescriptionAttributes > 0;
		
		Attribute[] activeConditionAttributes = hasActiveConditionAttributes ? new Attribute[numberOfActiveConditionAttributes] : null;
		Attribute[] notActiveOrDescriptionAttributes = hasNotActiveOrDescriptionAttributes ? new Attribute[numberOfNotActiveOrDescriptionAttributes] : null;
		
		int activeConditionAttributeIndex = 0;
		int notActiveOrDescriptionAttributeIndex = 0;
		
		this.attributeMap = new int[attributes.length];
		
		//split attributes into two tables + identify active decision attribute (if any)
		for (int i = 0; i < attributes.length; i++) {
			if (isActiveConditionAttribute(attributes[i])) {
				activeConditionAttributes[activeConditionAttributeIndex] = attributes[i];
				this.attributeMap[i] = this.encodeActiveConditionAttributeIndex(activeConditionAttributeIndex);
				activeConditionAttributeIndex++;
			} else if (isActiveDecisionAttribute(attributes[i])) {
				this.activeDecisionAttributeIndex = i;
				this.attributeMap[i] = 0; //no encoding
			} else { //not active or description attribute
				notActiveOrDescriptionAttributes[notActiveOrDescriptionAttributeIndex] = attributes[i];
				this.attributeMap[i] = this.encodeNotActiveOrDescriptionAttributeIndex(notActiveOrDescriptionAttributeIndex);
				notActiveOrDescriptionAttributeIndex++;
			}
		}
		
		Field[][] activeConditionFieldsArray = hasActiveConditionAttributes ? new Field[fields.size()][] : null;
		Field[] activeDecisionAttributeFields = hasActiveDecisionAttribute ? new Field[fields.size()] : null;
		Field[][] notActiveOrDescriptionFieldsArray = hasNotActiveOrDescriptionAttributes ? new Field[fields.size()][] : null;
		
		Field[] activeConditionFields = null;
		Field activeDecisionField = null;
		Field[] notActiveOrDescriptionFields = null;
		
		int rowIndex = 0;
		
		//split fields into two tables + collect decisions (if any)
		for (Field[] evaluations : fields) { //choose row (single object)
			if (hasActiveConditionAttributes) {
				activeConditionFields = new Field[numberOfActiveConditionAttributes];
				activeConditionAttributeIndex = 0;
			}
			if (hasNotActiveOrDescriptionAttributes) {
				notActiveOrDescriptionFields = new Field[numberOfNotActiveOrDescriptionAttributes];
				notActiveOrDescriptionAttributeIndex = 0;
			}
			
			for (int i = 0; i < attributes.length; i++) { //choose column (single attribute)
				if (isActiveConditionAttribute(attributes[i])) {
					activeConditionFields[activeConditionAttributeIndex++] = evaluations[i];
				} else if (isActiveDecisionAttribute(attributes[i])) { //should be true at most once per row
					activeDecisionField = evaluations[i];
				} else { //not active or description attribute
					notActiveOrDescriptionFields[notActiveOrDescriptionAttributeIndex++] = evaluations[i];
				} 
			}
			
			if (hasActiveConditionAttributes) {
				activeConditionFieldsArray[rowIndex] = activeConditionFields;
			}
			if (hasActiveDecisionAttribute) {
				activeDecisionAttributeFields[rowIndex] = activeDecisionField;
			}
			if (hasNotActiveOrDescriptionAttributes) {
				notActiveOrDescriptionFieldsArray[rowIndex] = notActiveOrDescriptionFields;
			}
			
			rowIndex++;
		}
		
		this.attributes = accelerateByReadOnlyParams ? attributes : attributes.clone(); //remember all attributes
		//map each object (row of this information table) to a unique id, and remember that mapping
		this.mapper = new Index2IdMapper(UniqueIdGenerator.getInstance().getUniqueIds(fields.size()), true);
		
		this.activeConditionEvaluations = hasActiveConditionAttributes ? new Table(activeConditionAttributes, activeConditionFieldsArray, this.mapper, true) : null;
		this.activeDecisionAttributeFields = hasActiveDecisionAttribute ? activeDecisionAttributeFields : null;
		this.notActiveOrDescriptionEvaluations = hasNotActiveOrDescriptionAttributes ? new Table(notActiveOrDescriptionAttributes, notActiveOrDescriptionFieldsArray, this.mapper, true) : null;
	}
	
	/**
	 * Gets sub-table of this information table, corresponding to active condition attributes only.
	 * If there are no such attributes, then returns {@code null}.
	 * 
	 * @return sub-table of this information table, corresponding to active condition attributes only
	 */
	public Table getActiveConditionEvaluations() {
		return this.activeConditionEvaluations;
	}

	/**
	 * Gets sub-table of this information table, corresponding to all attributes which are either not active or description ones.
	 * If there are no such attributes, then returns {@code null}.
	 * 
	 * @return sub-table of this information table, corresponding to all attributes which are either not active or description ones
	 */
	public Table getNotActiveOrDescriptionEvaluations() {
		return this.notActiveOrDescriptionEvaluations;
	}
	
	/**
	 * Gets array of decisions concerning subsequent objects of this information table; i-th entry stores decision concerning i-th object.
	 * This array stores evaluations of objects on the only active decision attribute.
	 * Can be {@code null}, e.g., if this information table stores evaluations of test objects (for which decisions are unknown).
	 * 
	 * @return array of decisions concerning subsequent objects of this information table
	 */
	public Field[] getDecisions() {
		return this.getDecisions(false);
	}
	
	/**
	 * Gets array of decisions concerning subsequent objects of this information table; i-th entry stores decision concerning i-th object.
	 * This array stores evaluations of objects on the only active decision attribute.
	 * Can be {@code null}, e.g., if this information table stores evaluations of test objects (for which decisions are unknown).
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array of decisions concerning subsequent objects of this information table
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Field[] getDecisions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? this.activeDecisionAttributeFields : this.activeDecisionAttributeFields.clone();
	}
	
	/**
	 * Gets decision available for an object with given index.
	 * 
	 * @param objectIndex index of an object in this information table
	 * @return decision available for an object with given index, or {@code null} if there is no such decision
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object for which this table stores fields
	 */
	public Field getDecision(int objectIndex) {
		if (this.activeDecisionAttributeFields != null) {
			return this.activeDecisionAttributeFields[objectIndex];
		} else {
			return null;
		}
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
		if (this.attributeMap[attributeIndex] > 0) { //active condition attribute
			return this.activeConditionEvaluations.getField(objectIndex, this.decodeActiveConditionAttributeIndex(this.attributeMap[attributeIndex]));
		} else if (this.attributeMap[attributeIndex] == 0) { //decision attribute
			return this.activeDecisionAttributeFields[objectIndex];
		} else { //not active or description attribute
			return this.notActiveOrDescriptionEvaluations.getField(objectIndex, this.decodeNotActiveOrDescriptionAttributeIndex(this.attributeMap[attributeIndex]));
		}
	}
	
	private int encodeActiveConditionAttributeIndex(int index) {
		return index + 1;
	}
	
	private int decodeActiveConditionAttributeIndex(int encodedIndex) {
		return encodedIndex - 1;
	}
	
	private int encodeNotActiveOrDescriptionAttributeIndex(int index) {
		return -index - 1;
	}
	
	private int decodeNotActiveOrDescriptionAttributeIndex(int encodedIndex) {
		return -encodedIndex - 1;
	}
	
	/**
	 * Tells if given attribute is an active condition attribute.
	 * 
	 * @param attribute attribute to check
	 * @return {@code true} if given attribute is condition and active, {@code false otherwise}
	 */
	private boolean isActiveConditionAttribute(Attribute attribute) {
		if (attribute instanceof EvaluationAttribute)
			return ((EvaluationAttribute)attribute).getType() == AttributeType.CONDITION && attribute.isActive();
		else
			return false;
	}
	
	/**
	 * Tells if given attribute is an active decision attribute.
	 * 
	 * @param attribute attribute to check
	 * @return {@code true} if given attribute is decision and active, {@code false otherwise}
	 */
	private boolean isActiveDecisionAttribute(Attribute attribute) {
		if (attribute instanceof EvaluationAttribute)
			return ((EvaluationAttribute)attribute).getType() == AttributeType.DECISION && attribute.isActive();
		else
			return false;
	}
	
	/**
	 * Selects rows of this information table that correspond to objects with given indices.
	 * Returns new information table concerning a subset of objects (rows).
	 * 
	 * @param objectIndices indices of objects to select to new information table (indices can repeat)
	 * @return sub-table of this information table, containing only rows corresponding to objects whose index is in the given array
	 * 
	 * @throws NullPointerException if given array with object indices is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public InformationTable select(int[] objectIndices) {
		return select(objectIndices, false);
	}
	
	/**
	 * Selects rows of this information table that correspond to objects with given indices.
	 * Returns new information table concerning a subset of objects (rows).
	 * 
	 * @param objectIndices indices of objects to select to new information table (indices can repeat)
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only information table, or should return a safe information table (that can be
	 *        modified), at the cost of returning the result slower
	 * @return sub-table of this information table, containing only rows corresponding to objects whose index is in the given array
	 * 
	 * @throws NullPointerException if given array with object indices is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public InformationTable select(int[] objectIndices, boolean accelerateByReadOnlyResult) {
		Index2IdMapper newMapper = null;
		
		Table newActiveConditionEvaluations = null;
		if (this.activeConditionEvaluations != null) {
			newActiveConditionEvaluations = this.activeConditionEvaluations.select(objectIndices, accelerateByReadOnlyResult);
			newMapper = newActiveConditionEvaluations.getIndex2IdMapper(); //use already calculated mapper
		}
		
		Table newNotActiveOrDescriptionEvaluations = null;
		if (this.notActiveOrDescriptionEvaluations != null) {
			newNotActiveOrDescriptionEvaluations = this.notActiveOrDescriptionEvaluations.select(objectIndices, accelerateByReadOnlyResult);
			if (newMapper == null) {
				newMapper = newNotActiveOrDescriptionEvaluations.getIndex2IdMapper(); //use already calculated mapper
			}
		}
		
		Field[] newActiveDecisionAttributeFields = null;
		if (this.activeDecisionAttributeFields != null) {
			newActiveDecisionAttributeFields = new Field[objectIndices.length];
			for (int i = 0; i < objectIndices.length; i++) {
				newActiveDecisionAttributeFields[i] = this.activeDecisionAttributeFields[objectIndices[i]];
			}
		}
		
		return new InformationTable(this.attributes, newMapper, newActiveConditionEvaluations, newNotActiveOrDescriptionEvaluations,
				newActiveDecisionAttributeFields, this.activeDecisionAttributeIndex, this.attributeMap, accelerateByReadOnlyResult);
	}
	
	/**
	 * Gets number of objects stored in this information table.
	 * 
	 * @return number of objects stored in this information table
	 */
	public int getNumberOfObjects() {
		return this.activeConditionEvaluations.getNumberOfObjects();
	}
	
	/**
	 * Gets number of attributes stored in this information table.
	 * 
	 * @return number of attributes stored in this information table
	 */
	public int getNumberOfAttributes() {
		return this.attributes.length;
	}

	/**
	 * Gets index of the only active decision attribute used in calculations. If there is no such attribute, returns -1.
	 * 
	 * @return index of the only active decision attribute used in calculations, or -1 if there is no such attribute
	 */
	public int getActiveDecisionAttributeIndex() {
		return this.activeDecisionAttributeIndex;
	}
}
