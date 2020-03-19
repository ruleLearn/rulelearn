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
import java.util.Set;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.rules.VCDomLEM;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IdentificationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * Information table composed of fields storing identifiers/evaluations of all considered objects on all specified attributes, among which we distinguish:<br>
 * (1) identification attributes,<br>
 * (2) evaluation attributes: condition, decision and description ones,<br>
 * both active and non-active.<br>
 * Each field is identified by object's index and attribute's index.
 * An information table is allowed to have any number of active decision attributes.
 * An ordered set of object's evaluations on active decision attributes defines decision associated with that object.
 * An information table is allowed to have zero or exactly one active identification attribute whose values are object identifiers.<br>
 * <br>
 * This object can be also build using {@link InformationTableBuilder}.
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
	 * Mapper translating object's index into its globally unique id.
	 */
	protected Index2IdMapper mapper;
	
	/**
	 * Sub-table, corresponding to active condition evaluation attributes only.
	 * This sub-table is used in calculations. Equals to {@code null} if there are no active condition evaluation attributes.
	 */
	protected Table<EvaluationAttribute, EvaluationField> activeConditionAttributeFields = null;
	/**
	 * Sub-table, corresponding to all attributes which are either not active or description ones.
	 * This sub-table is not used in calculations. It stores values of such supplementary attributes
	 * mainly for the on-screen presentation of data and their write-back to file.
	 * Equals to {@code null} if there are no supplementary attributes.
	 */
	protected Table<Attribute, Field> notActiveOrDescriptionAttributeFields = null;
	
	/**
	 * Contains decisions associated with subsequent objects. Each decision is defined by an ordered set of object's evaluations on active decision attributes.
	 * Can be {@code null}, e.g., if this information table stores evaluations of test objects (for which decisions are unknown).
	 */
	protected Decision[] decisions = null;
	
	/**
	 * Contains identifiers of objects assigned to them by the only active identification attribute.
	 * Can be {@code null}, if there is no such attribute.
	 */
	protected IdentificationField[] activeIdentificationAttributeFields = null;
	
	/**
	 * Index of the only active identification attribute used to identify objects. If there is no such attribute, equals to -1.
	 */
	protected int activeIdentificationAttributeIndex = -1;
	
	/**
	 * Maps global index of an attribute of this information table to encoded local index of an active condition attribute (called AC-attribute)
	 * or to encoded local index of a non-active/description attribute (called NA/D-attribute).
	 * Encoding of a local index of an AC-attribute is done by adding 1 - see {@link #encodeActiveConditionAttributeIndex(int)}. 
	 * Encoding of a local index of a NA/D-attribute is done by taking negative value and then subtracting 1 - see {@link #encodeNotActiveOrDescriptionAttributeIndex(int)}.<br>
	 * <br>
	 * Suppose that the 3-rd global attribute (having global index 2) is the first AC-attribute (having local index 0).
	 * Then, {@code attributeMap[2]} encodes 0, so it will be equal to 0+1=1.<br>
	 * <br>
	 * Suppose that the 5-rd global attribute (having global index 4) is the second NA/D-attribute (having local index 1).
	 * Then, {@code attributeMap[4]} encodes 1, so it will be equal to (-1)-1=-2.<br>
	 * <br>
	 * This map also marks active decision attributes (called AD-attributes) and the only active identification attribute (called AI-attribute),
	 * if there are such attributes.<br>
	 * If attributeMap[i]==0, then the global attribute having global index i is an AD-attribute or an AI-attribute
	 * (one can verify which of the two cases is true by comparing index i with {@link #activeIdentificationAttributeIndex}).<br>
	 * <br>
	 * Suppose there are nine attributes:<br>
	 * - attr0: active {@link EvaluationAttribute} of type {@link AttributeType#CONDITION},<br>
	 * - attr1: active {@link EvaluationAttribute} of type {@link AttributeType#DESCRIPTION},<br>
	 * - attr2: non-active {@link EvaluationAttribute} of type {@link AttributeType#CONDITION},<br>
	 * - attr3: active {@link EvaluationAttribute} of type {@link AttributeType#CONDITION},<br>
	 * - attr4: active {@link EvaluationAttribute} of type {@link AttributeType#DECISION},<br>
	 * - attr5: non-active {@link EvaluationAttribute} of type {@link AttributeType#DECISION},<br>
	 * - attr6: non-active {@link EvaluationAttribute} of type {@link AttributeType#DESCRIPTION},<br>
	 * - attr7: active {@link IdentificationAttribute} with value type {@link TextIdentificationField},<br>
	 * - attr8: non-active {@link IdentificationAttribute} with value type {@link UUIDIdentificationField}.<br>
	 * <br>
	 * Then, the map should be the following:<br>
	 * attributeMap = [1, -1, -2, 2, 0, -3, -4, 0, -5].
	 */
	int[] attributeMap;
	
	/**
	 * Maps local index of an active condition attribute (called AC-attribute) to the global index of this attribute
	 * in the array of all attributes of this information table. Can be empty, but should not be {@code null}.
	 */
	Int2IntMap localActiveConditionAttributeIndex2GlobalAttributeIndexMap;
	
	/**
	 * Cached output of {@link #calculateOrderedUniqueFullyDeterminedDecisions()}.
	 */
	private Decision[] orderedUniqueFullyDeterminedDecisions = null;
	
	/**
	 * Cached output of {@link #calculateUniqueDecisions()}.
	 */
	private Decision[] uniqueDecisions = null;
	
	/**
	 * Suffix of any new {@link AttributePreferenceType#GAIN gain-type} evaluation attribute created by {@link #imposePreferenceOrders(boolean)} when cloning an existing
	 * evaluation attribute {@link AttributePreferenceType#NONE without preference type} and setting for each clone opposite preference order.
	 */
	public static final String attributeNameSuffixGain = "_g"; //constant
	/**
	 * Suffix of any new {@link AttributePreferenceType#COST cost-type} evaluation attribute created by {@link #imposePreferenceOrders(boolean)} when cloning an existing
	 * evaluation attribute {@link AttributePreferenceType#NONE without preference type} and setting for each clone opposite preference order.
	 */
	public static final String attributeNameSuffixCost = "_c"; //constant
	/**
	 * Suffix of any new binary evaluation attribute {@link AttributePreferenceType#NONE without preference type} created by {@link #imposePreferenceOrders(boolean)}
	 * when binarizing an existing evaluation attribute {@link AttributePreferenceType#NONE without preference type},
	 * with {@link EnumerationField} {@link EvaluationAttribute#getValueType() value type}, and with at least 3 values in its domain.
	 */
	public static final String attributeNameSuffixNone = ""; //constant
	
	/**
	 * Protected copy constructor for internal use only. Sets all data fields of this information table.
	 * 
	 * @param attributes all attributes of constructed information table (identification and evaluation (condition/decision/description) ones, both active and non-active)
	 * @param mapper translator of object's index, which is meaningful in this information table only,
	 *        to unique object's id, which is meaningful in general
	 * @param activeConditionAttributeFields sub-table corresponding to active condition attributes
	 * @param notActiveOrDescriptionAttributeFields sub-table corresponding to non-active/description attributes
	 * @param decisions list of decisions concerning subsequent objects
	 * @param activeIdentificationAttributeFields list of identifiers of subsequent objects
	 * @param activeIdentificationAttributeIndex index of the only active identification attribute
	 * @param attributeMap see {@link #attributeMap}
	 * @param localActiveConditionAttributeIndex2GlobalAttributeIndexMap see {@link #localActiveConditionAttributeIndex2GlobalAttributeIndexMap}
	 * @param accelerateByReadOnlyParams tells if construction of this information table should be accelerated by assuming that the given references
	 *        to arrays are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal read-only use)
	 */
	protected InformationTable(Attribute[] attributes, Index2IdMapper mapper, Table<EvaluationAttribute, EvaluationField> activeConditionAttributeFields, Table<Attribute, Field> notActiveOrDescriptionAttributeFields,
			Decision[] decisions, 
			IdentificationField[] activeIdentificationAttributeFields, int activeIdentificationAttributeIndex,
			int[] attributeMap, Int2IntMap localActiveConditionAttributeIndex2GlobalAttributeIndexMap, boolean accelerateByReadOnlyParams) {
		this.attributes = accelerateByReadOnlyParams ? attributes : attributes.clone();
		this.mapper = mapper;
		this.activeConditionAttributeFields = activeConditionAttributeFields;
		this.notActiveOrDescriptionAttributeFields = notActiveOrDescriptionAttributeFields;
		
		// decisions not always must be set in the information table (i.e., they may be null)
		this.decisions = accelerateByReadOnlyParams ? decisions : ( (decisions == null) ? null : decisions.clone() );
		/* depreciated
		 this.activeDecisionAttributeIndex = activeDecisionAttributeIndex;
		*/

		// active identification fields not always must be set in the information table (i.e., they may be null)
		this.activeIdentificationAttributeFields = accelerateByReadOnlyParams ? activeIdentificationAttributeFields : 
			( (activeIdentificationAttributeFields == null) ? null : activeIdentificationAttributeFields.clone() );
		this.activeIdentificationAttributeIndex = activeIdentificationAttributeIndex;
		
		this.attributeMap = accelerateByReadOnlyParams ? attributeMap : attributeMap.clone();
		this.localActiveConditionAttributeIndex2GlobalAttributeIndexMap = accelerateByReadOnlyParams ? localActiveConditionAttributeIndex2GlobalAttributeIndexMap :
			new Int2IntOpenHashMap(localActiveConditionAttributeIndex2GlobalAttributeIndexMap);
	}
	
	
	/**
	 * A wrapper-type constructor, passing arguments to {@link InformationTable#InformationTable(Attribute[], List, boolean)} with the boolean flag set to {@code false}. 
	 * 
	 * @param attributes see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @param listOfFields see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * 
	 * @throws NullPointerException see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @throws InvalidValueException see {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 */
	public InformationTable(Attribute[] attributes, List<Field[]> listOfFields) {
		this(attributes, listOfFields, false);
	}
	
	/**
	 * Information table constructor. Assumes that the type of fields in i-th column is compatible with the type of attribute at i-th position.<br>
	 * <br>
	 * This constructor can be used in certain circumstances to accelerate information table construction (by not cloning arrays).
	 * 
	 * @param attributes all attributes of constructed information table (identification and evaluation (condition/decision/description) ones, both active and non-active)
	 * @param listOfFields list of fields of subsequent objects; each array contains subsequent fields of a single object (row) in this information table;
	 *        it is assumed that each array is of the same length (i.e., the number of fields of each object is the same)
	 * @param accelerateByReadOnlyParams tells if construction of this object should be accelerated by assuming that the given reference
	 *        to an array of attributes and references to arrays of fields present at the given list are not going to be used outside this class
	 *        to modify that arrays (and thus, this object does not need to clone the arrays for internal read-only use)
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if the number of attributes and the number of fields corresponding to one object
	 *         (i.e., stored in a single array) do not match
	 * @throws InvalidValueException if there is more than one active identification attribute
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.INPUT)
	public InformationTable(Attribute[] attributes, List<Field[]> listOfFields, boolean accelerateByReadOnlyParams) {
		Precondition.notNull(attributes, "Cannot build information table for null attributes");
		Precondition.notNull(listOfFields, "Cannot build information table for null list of objects' fields.");
		
		if (listOfFields.size() > 0 && attributes.length != listOfFields.get(0).length) {
			throw new InvalidValueException("The number of attributes and the number of objects' fields in an information table do not match.");
		}
		
		int numberOfActiveConditionAttributes = 0;
		int numberOfActiveDecisionAttributes = 0;
		int numberOfActiveIdentificationAttributes = 0;
		
		for (int i = 0; i < attributes.length; i++) { //scout attributes first
			if (isActiveConditionAttribute(attributes[i])) {
				numberOfActiveConditionAttributes++;
			} else if (isActiveDecisionAttribute(attributes[i])) {
				numberOfActiveDecisionAttributes++;
			} else if (isActiveIdentificationAttribute(attributes[i])) {
				numberOfActiveIdentificationAttributes++;
				if (numberOfActiveIdentificationAttributes > 1) {
					throw new InvalidValueException("The number of active identification attributes is greater than 1.");
				}
			}
		}
		
		int numberOfNotActiveOrDescriptionAttributes = attributes.length - numberOfActiveConditionAttributes - numberOfActiveDecisionAttributes - numberOfActiveIdentificationAttributes;
		
		boolean hasActiveConditionAttributes = numberOfActiveConditionAttributes > 0;
		boolean hasActiveDecisionAttributes = numberOfActiveDecisionAttributes > 0;
		boolean hasActiveIdentificationAttribute = numberOfActiveIdentificationAttributes > 0;
		boolean hasNotActiveOrDescriptionAttributes = numberOfNotActiveOrDescriptionAttributes > 0;
		
		//needed to construct instances of Table class
		EvaluationAttribute[] activeConditionAttributes = hasActiveConditionAttributes ? new EvaluationAttribute[numberOfActiveConditionAttributes] : null;
		Attribute[] notActiveOrDescriptionAttributes = hasNotActiveOrDescriptionAttributes ? new Attribute[numberOfNotActiveOrDescriptionAttributes] : null;
		
		int activeConditionAttributeIndex = 0;
		int activeDecisionAttributeIndex = 0;
		int notActiveOrDescriptionAttributeIndex = 0;
		
		this.attributeMap = new int[attributes.length];
		this.localActiveConditionAttributeIndex2GlobalAttributeIndexMap = new Int2IntOpenHashMap();
		
		//split attributes into two tables + mark active decision attributes (if there are any) and active identification attribute (if there is such an attribute)
		for (int i = 0; i < attributes.length; i++) {
			if (isActiveConditionAttribute(attributes[i])) {
				activeConditionAttributes[activeConditionAttributeIndex] = (EvaluationAttribute)attributes[i];
				this.attributeMap[i] = this.encodeActiveConditionAttributeIndex(activeConditionAttributeIndex);
				this.localActiveConditionAttributeIndex2GlobalAttributeIndexMap.put(activeConditionAttributeIndex, i);
				activeConditionAttributeIndex++;
			} else if (isActiveDecisionAttribute(attributes[i])) {
//				this.activeDecisionAttributeIndex = i;
				this.attributeMap[i] = 0; //no encoding
			} else if (isActiveIdentificationAttribute(attributes[i])) {
				this.activeIdentificationAttributeIndex = i;
				this.attributeMap[i] = 0; //no encoding
			} else { //not active or description attribute
				notActiveOrDescriptionAttributes[notActiveOrDescriptionAttributeIndex] = attributes[i];
				this.attributeMap[i] = this.encodeNotActiveOrDescriptionAttributeIndex(notActiveOrDescriptionAttributeIndex);
				notActiveOrDescriptionAttributeIndex++;
			}
		}
		
		EvaluationField[][] activeConditionAttributesFieldsArray = hasActiveConditionAttributes ? new EvaluationField[listOfFields.size()][] : null;
		Decision[] decisions = hasActiveDecisionAttributes ? new Decision[listOfFields.size()] : null;
		IdentificationField[] activeIdentificationAttributeFields = hasActiveIdentificationAttribute ? new IdentificationField[listOfFields.size()] : null;
		Field[][] notActiveOrDescriptionFieldsArray = hasNotActiveOrDescriptionAttributes ? new Field[listOfFields.size()][] : null;
		
		EvaluationField[] activeConditionFields = null;
		EvaluationField[] activeDecisionFields = null;
		int[] activeDecisionAttributeIndices = null;
		IdentificationField activeIdentificationField = null;
		Field[] notActiveOrDescriptionFields = null;
		
		int rowIndex = 0;
		
		//split fields into two tables + collect decisions (if there are any) and identifiers (if there are any)
		for (Field[] fields : listOfFields) { //choose a row (single object)
			if (hasActiveConditionAttributes) {
				activeConditionFields = new EvaluationField[numberOfActiveConditionAttributes];
				activeConditionAttributeIndex = 0;
			}
			if (hasActiveDecisionAttributes) {
				activeDecisionFields = new EvaluationField[numberOfActiveDecisionAttributes];
				activeDecisionAttributeIndices = new int[numberOfActiveDecisionAttributes];
				activeDecisionAttributeIndex = 0;
			}
			if (hasNotActiveOrDescriptionAttributes) {
				notActiveOrDescriptionFields = new Field[numberOfNotActiveOrDescriptionAttributes];
				notActiveOrDescriptionAttributeIndex = 0;
			}
			
			for (int i = 0; i < attributes.length; i++) { //choose a column (single attribute)
				if (isActiveConditionAttribute(attributes[i])) {
					activeConditionFields[activeConditionAttributeIndex++] = (EvaluationField)fields[i];
				} else if (isActiveDecisionAttribute(attributes[i])) {
					activeDecisionFields[activeDecisionAttributeIndex] = (EvaluationField)fields[i];
					activeDecisionAttributeIndices[activeDecisionAttributeIndex] = i;
					activeDecisionAttributeIndex++;
				} else if (isActiveIdentificationAttribute(attributes[i])) { //should be true at most once per row
					activeIdentificationField = (IdentificationField)fields[i];
				} else { //not active or description attribute
					notActiveOrDescriptionFields[notActiveOrDescriptionAttributeIndex++] = fields[i];
				} 
			}
			
			if (hasActiveConditionAttributes) {
				activeConditionAttributesFieldsArray[rowIndex] = activeConditionFields;
			}
			if (hasActiveDecisionAttributes) {
				decisions[rowIndex] = DecisionFactory.INSTANCE.create(activeDecisionFields, activeDecisionAttributeIndices);
			}
			if (hasActiveIdentificationAttribute) {
				activeIdentificationAttributeFields[rowIndex] = activeIdentificationField;
			}
			if (hasNotActiveOrDescriptionAttributes) {
				notActiveOrDescriptionFieldsArray[rowIndex] = notActiveOrDescriptionFields;
			}
			
			rowIndex++;
		}
		
		this.attributes = accelerateByReadOnlyParams ? attributes : attributes.clone(); //remember all attributes
		//map each object (row of this information table) to a unique id, and remember that mapping
		this.mapper = new Index2IdMapper(UniqueIdGenerator.getInstance().getUniqueIds(listOfFields.size()), true);
		
		this.activeConditionAttributeFields = hasActiveConditionAttributes ? new Table<EvaluationAttribute, EvaluationField>(activeConditionAttributes, activeConditionAttributesFieldsArray, this.mapper, true) : null;
		this.decisions = hasActiveDecisionAttributes ? decisions : null;
		this.activeIdentificationAttributeFields = hasActiveIdentificationAttribute ? activeIdentificationAttributeFields : null;
		this.notActiveOrDescriptionAttributeFields = hasNotActiveOrDescriptionAttributes ? new Table<Attribute, Field>(notActiveOrDescriptionAttributes, notActiveOrDescriptionFieldsArray, this.mapper, true) : null;
	}
	
	/**
	 * A wrapper-type constructor, passing arguments to {@link InformationTable#InformationTable(InformationTable, boolean)} with the boolean flag set to {@code false}. 
	 * 
	 * @param informationTable see {@link InformationTable#InformationTable(InformationTable, boolean)}
	 * @throws NullPointerException see {@link InformationTable#InformationTable(InformationTable, boolean)}
	 */
	public InformationTable(InformationTable informationTable) {
		this(informationTable, false);
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param informationTable information table to be copied
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only information table, or should return a safe information table (that can be modified),
	 *        at the cost of returning the result slower
	 * @throws NullPointerException if the given information table is {@code null}
	 */
	public InformationTable(InformationTable informationTable, boolean accelerateByReadOnlyResult) {
		if (informationTable == null) {
			throw new NullPointerException("Cannot copy null information table.");
		}
		this.attributes = informationTable.getAttributes(accelerateByReadOnlyResult);
		this.mapper = informationTable.getIndex2IdMapper();
		this.activeConditionAttributeFields = informationTable.getActiveConditionAttributeFields();
		this.notActiveOrDescriptionAttributeFields = informationTable.getNotActiveOrDescriptionAttributeFields();
		this.decisions = informationTable.getDecisions(accelerateByReadOnlyResult);
		this.activeIdentificationAttributeFields = informationTable.getIdentifiers(accelerateByReadOnlyResult);
		this.activeIdentificationAttributeIndex = informationTable.getActiveIdentificationAttributeIndex();
		this.attributeMap = accelerateByReadOnlyResult ? informationTable.attributeMap : informationTable.attributeMap.clone();
		this.localActiveConditionAttributeIndex2GlobalAttributeIndexMap = accelerateByReadOnlyResult ? informationTable.localActiveConditionAttributeIndex2GlobalAttributeIndexMap :
			new Int2IntOpenHashMap(informationTable.localActiveConditionAttributeIndex2GlobalAttributeIndexMap);
	}
	
	/**
	 * Gets sub-table of this information table, corresponding to active condition evaluation attributes only.
	 * If there are no such attributes, then returns {@code null}.
	 * 
	 * @return sub-table of this information table, corresponding to active condition evaluation attributes only
	 */
	public Table<EvaluationAttribute, EvaluationField> getActiveConditionAttributeFields() {
		return this.activeConditionAttributeFields;
	}

	/**
	 * Gets sub-table of this information table, corresponding to all attributes which are either not active or description ones.
	 * If there are no such attributes, then returns {@code null}.
	 * 
	 * @return sub-table of this information table, corresponding to all attributes which are either not active or description ones
	 */
	public Table<Attribute, Field> getNotActiveOrDescriptionAttributeFields() {
		return this.notActiveOrDescriptionAttributeFields;
	}
	
	/**
	 * Gets array of decisions concerning subsequent objects of this information table; i-th entry stores decision concerning i-th object.
	 * Result can be {@code null}, e.g., if this information table stores evaluations of test objects (for which decisions are unknown).
	 * 
	 * @return array of decisions concerning subsequent objects of this information table,
	 *         or {@code null} if this information table does not store any decisions
	 */
	public Decision[] getDecisions() {
		return this.getDecisions(false);
	}
	
	/**
	 * Gets array of decisions concerning subsequent objects of this information table; i-th entry stores decision concerning i-th object.
	 * Result can be {@code null}, e.g., if this information table stores evaluations of test objects (for which decisions are unknown).
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array of decisions concerning subsequent objects of this information table,
	 *         or {@code null} if this information table does not store any decisions
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public Decision[] getDecisions(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? this.decisions : 
			(this.decisions != null ? this.decisions.clone() : null);
	}
	
	/**
	 * Gets decision available for an object with given index.
	 * 
	 * @param objectIndex index of an object in this information table
	 * @return decision available for an object with given index, or {@code null} if there is no such decision
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object for which this table stores fields
	 */
	public Decision getDecision(int objectIndex) {
		if (this.decisions != null) {
			return this.decisions[objectIndex];
		} else {
			return null;
		}
	}
	
	/**
	 * Gets array of all unique decisions assigned to objects of this information table.<br>
	 * <br>
	 * For any two decisions from the returned array, {@code decision1.equals(decision2)} should return {@code false}.
	 * If {@link #getDecisions()} returns {@code null}, then result of this method is {@code null}.
	 * This may be the case if the information table stores evaluations of test objects (for which decisions are unknown).
	 * 
	 * @return array of all unique decisions assigned to objects of this information table
	 */
	public Decision[] getUniqueDecisions() {
		if (uniqueDecisions == null) {
			uniqueDecisions = calculateUniqueDecisions(); //quickly returns null if decisions are indeed null
		}
		return uniqueDecisions;
	}
	
	/**
	 * Calculates array of all unique decisions assigned to objects of this information table. The decisions are arranged
	 * in the order in which they appear in the information table.<br>
	 * <br>
	 * For any two decisions from the returned array, {@code decision1.equals(decision2)} should return {@code false}.
	 * If {@link #getDecisions()} returns {@code null}, then result of this method is {@code null}.
	 * This may be the case if the information table stores evaluations of test objects (for which decisions are unknown).
	 * 
	 * @return array of all unique decisions assigned to objects of this information table
	 */
	Decision[] calculateUniqueDecisions() {
		Decision[] allDecisions = this.getDecisions(true);
		
		if (allDecisions == null || allDecisions.length < 1) {
			return allDecisions;
		}
		
		List<Decision> uniqueDecisionsList = new ObjectArrayList<>();
		Set<Decision> uniqueDecisionsSet = new ObjectOpenHashSet<>();
		
		uniqueDecisionsList.add(allDecisions[0]); //add first decision
		uniqueDecisionsSet.add(allDecisions[0]); //remember decision
		
		for (int i = 1; i < allDecisions.length; i++) {
			if (!uniqueDecisionsSet.contains(allDecisions[i])) {
				uniqueDecisionsList.add(allDecisions[i]); //add next decision
				uniqueDecisionsSet.add(allDecisions[i]); //remember decision
			}
		}
		
		return uniqueDecisionsList.toArray(new Decision[uniqueDecisionsList.size()]); //convert the list of decisions to an array of decisions
	}
	
	/**
	 * Gets ordered (from the worst to the best) array of all unique fully-determined decisions assigned to objects of this information table.
	 * A fully-determined {@link Decision decision} is a decision whose all contributing evaluations are non-missing (are instances of {@link KnownSimpleField}) -
	 * see {@link Decision#hasNoMissingEvaluation()}.<br>
	 * <br>
	 * For any two decisions from the returned array, {@code decision1.equals(decision2)} should return {@code false}.
	 * If {@link #getDecisions()} returns {@code null}, then result of this method is {@code null}.
	 * This may be the case if the information table stores evaluations of test objects (for which decisions are unknown).<br>
	 * <br>
	 * If returned array contains {@link CompositeDecision composite decisions}, it is possible that some of its elements are pairwise incomparable.
	 * In such case, their mutual order is not constrained and may be any.
	 * 
	 * @return array with unique fully-determined decisions ordered from the worst to the best,
	 *         or {@code null} if this information table does not store any decisions ({@link #getDecisions()} returns {@code null})
	 * @see Decision#hasNoMissingEvaluation()
	 */
	public Decision[] getOrderedUniqueFullyDeterminedDecisions() {
		if (orderedUniqueFullyDeterminedDecisions == null) {
			orderedUniqueFullyDeterminedDecisions = calculateOrderedUniqueFullyDeterminedDecisions(); //quickly returns null if decisions are indeed null
		}
		return orderedUniqueFullyDeterminedDecisions;
	}
	
	/**
	 * Calculates ordered (from the worst to the best) array of all unique fully-determined decisions assigned to objects of this information table.
	 * A fully-determined {@link Decision decision} is a decision whose all contributing evaluations are non-missing (are instances of {@link KnownSimpleField}) -
	 * see {@link Decision#hasNoMissingEvaluation()}.<br>
	 * <br>
	 * For any two decisions from the returned array, {@code decision1.equals(decision2)} should return {@code false}.
	 * If {@link #getDecisions()} returns {@code null}, then result of this method is {@code null}.
	 * This may be the case if the information table stores evaluations of test objects (for which decisions are unknown).
	 * 
	 * @return array with unique fully-determined decisions ordered from the worst to the best,
	 *         or {@code null} if this information table does not store any decisions ({@link #getDecisions()} returns {@code null})
	 * @see Decision#hasNoMissingEvaluation()
	 */
	Decision[] calculateOrderedUniqueFullyDeterminedDecisions() {
		Decision[] allDecisions = this.getDecisions(true);
		
		if (allDecisions == null || allDecisions.length < 1) {
			return allDecisions;
		}
		
		ArrayList<Decision> orderedUniqueFullyDeterminedDecisionsList = new ArrayList<Decision>();
		
		//auxiliary variables
		Decision candidateDecision;
		Decision alreadyPresentDecision;
		boolean iterate;
		int decisionIndex;
		
		//create sorted list of decisions:
		
		//extract first fully-determined decision (if there is any)
		int startingIndex = allDecisions.length; //make sure that if there is no fully-determined decision, so first such decision could not be found, then next such decisions would not be searched for
		for (int i = 0; i < allDecisions.length; i++) {
			//current decision is fully-determined
			if (allDecisions[i].hasNoMissingEvaluation()) {
				orderedUniqueFullyDeterminedDecisionsList.add(allDecisions[i]);
				startingIndex = i + 1;
				break; //first fully-determined decision found
			}
		}
		
		//iterate through objects and extract next unique fully-determined decisions, retaining respective order of comparable decisions
		for (int i = startingIndex; i < allDecisions.length; i++) {
			candidateDecision = allDecisions[i];
			
			//verify if candidate decision satisfies loop entry condition of being fully-determined
			if (candidateDecision.hasNoMissingEvaluation()) {
				iterate = true;
				decisionIndex = 0;
				
				while (iterate) {
					alreadyPresentDecision = orderedUniqueFullyDeterminedDecisionsList.get(decisionIndex);
					//candidate decision is equal (identical) to compared decision from the list
					if (candidateDecision.equals(alreadyPresentDecision)) {
						//ignore candidate decision since it is already present in the list of decisions
						iterate = false;
					}
					//candidate decision is different than compared decision from the list
					else {
						//candidate decision is worse than compared decision from the list
						if (candidateDecision.isAtMostAsGoodAs(alreadyPresentDecision) == TernaryLogicValue.TRUE) {
							//insert candidate decision into appropriate position and shift following elements forward
							orderedUniqueFullyDeterminedDecisionsList.add(decisionIndex, candidateDecision);
							iterate = false;
						}
						//candidate decision is better than compared decision from the list
						//or is incomparable with the compared decision from the list
						else {
							//there is no next decision on the list
							if (decisionIndex == orderedUniqueFullyDeterminedDecisionsList.size() - 1) {
								//append candidate decision to the end of the list
								orderedUniqueFullyDeterminedDecisionsList.add(candidateDecision);
								iterate = false;
							}
							//there is next decision on the list
							else {
								decisionIndex++; //go to next decision from the list
							} //else
						} //else
					} //else
				} //while
			} //if
		} //for
		
		//create returned array of decisions
		int decisionsCount = orderedUniqueFullyDeterminedDecisionsList.size();
		Decision[] orderedUniqueFullyDeterminedDecisions = new Decision[decisionsCount];
		
		for (int i = 0; i < decisionsCount; i++)
			orderedUniqueFullyDeterminedDecisions[i] = orderedUniqueFullyDeterminedDecisionsList.get(i);
		
		return orderedUniqueFullyDeterminedDecisions;
	}
	
	/**
	 * Gets identifiers of objects assigned to them by the only active identification attribute.
	 * Can be {@code null}, if there is no active identification attribute.
	 * 
	 * @return array of identifiers of subsequent objects of this information table
	 */
	public IdentificationField[] getIdentifiers() {
		return this.getIdentifiers(false);
	}
	
	/**
	 * Gets identifiers of objects assigned to them by the only active identification attribute.
	 * Can be {@code null}, if there is no active identification attribute.
	 * 
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only array, or should return a safe array (that can be
	 *        modified outside this object), at the cost of returning the result slower
	 * @return array of identifiers of subsequent objects of this information table
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public IdentificationField[] getIdentifiers(boolean accelerateByReadOnlyResult) {
		return accelerateByReadOnlyResult ? this.activeIdentificationAttributeFields :
			(this.activeIdentificationAttributeFields != null ? this.activeIdentificationAttributeFields.clone() : null);
	}
	
	/**
	 * Gets identifier available for an object with given index.
	 * 
	 * @param objectIndex index of an object in this information table
	 * @return identifier available for an object with given index, or {@code null} if there is no such identifier
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object for which this table stores fields
	 */
	public IdentificationField getIdentifier(int objectIndex) {
		if (this.activeIdentificationAttributeFields != null) {
			return this.activeIdentificationAttributeFields[objectIndex];
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
	 * Gets attribute with given index.
	 * 
	 * @param attributeIndex index of an attribute of this information table
	 * @return the attribute of this information table having given index
	 * 
	 * @throws IndexOutOfBoundsException if given attribute index does not correspond to any attribute for which this table stores fields 
	 */
	public Attribute getAttribute(int attributeIndex) {
		return this.attributes[attributeIndex];
	}
	
	/**
	 * Gets mapper that maps indices of objects stored in this information table to their globally unique ids.
	 * 
	 * @return mapper that maps indices of objects stored in this information table to their globally unique ids
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
			return this.activeConditionAttributeFields.getField(objectIndex, this.decodeActiveConditionAttributeIndex(this.attributeMap[attributeIndex]));
		} else if (this.attributeMap[attributeIndex] == 0) { //active decision/identification attribute
			if (attributeIndex == this.activeIdentificationAttributeIndex) { //active identification attribute
				return this.activeIdentificationAttributeFields[objectIndex];
			} else { //active decision attribute
				return this.decisions[objectIndex].getEvaluation(attributeIndex);
			}
		} else { //not active or description attribute
			return this.notActiveOrDescriptionAttributeFields.getField(objectIndex, this.decodeNotActiveOrDescriptionAttributeIndex(this.attributeMap[attributeIndex]));
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
	 * Tells if given attribute is an active condition {@link EvaluationAttribute evaluation attribute}.
	 * 
	 * @param attribute attribute to check
	 * @return {@code true} if given attribute is an active condition evaluation attribute, {@code false otherwise}
	 */
	private boolean isActiveConditionAttribute(Attribute attribute) {
		if (attribute instanceof EvaluationAttribute) {
			return ((EvaluationAttribute)attribute).getType() == AttributeType.CONDITION && attribute.isActive();
		}
		else {
			return false;
		}
	}
	
	/**
	 * Tells if given attribute is an active decision {@link EvaluationAttribute evaluation attribute}.
	 * 
	 * @param attribute attribute to check
	 * @return {@code true} if given attribute is an active decision evaluation attribute, {@code false otherwise}
	 */
	private boolean isActiveDecisionAttribute(Attribute attribute) {
		if (attribute instanceof EvaluationAttribute) {
			return ((EvaluationAttribute)attribute).getType() == AttributeType.DECISION && attribute.isActive();
		}
		else {
			return false;
		}
	}
	
	/**
	 * Tells if given attribute is an active {@link IdentificationAttribute identification attribute}.
	 * 
	 * @param attribute attribute to check
	 * @return {@code true} if given attribute is an active identification attribute, {@code false otherwise}
	 */
	private boolean isActiveIdentificationAttribute(Attribute attribute) {
		if (attribute instanceof IdentificationAttribute) {
			return attribute.isActive();
		}
		else {
			return false;
		}
	}
	
	/**
	 * Selects rows of this information table that correspond to objects with given indices.
	 * Returns new information table concerning a subset of objects (rows).
	 * 
	 * @param objectIndices indices of objects to select to the resulting information table (indices can repeat)
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
	 * @param objectIndices indices of objects to select to the resulting information table (indices can repeat)
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only information table, or should return a safe information table (that can be modified),
	 *        at the cost of returning the result slower
	 * @return sub-table of this information table, containing only rows corresponding to objects whose index is in the given array
	 * 
	 * @throws NullPointerException if given array with object indices is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public InformationTable select(int[] objectIndices, boolean accelerateByReadOnlyResult) {
		Index2IdMapper newMapper = null;
		
		Table<EvaluationAttribute, EvaluationField> newActiveConditionAttributeFields = null;
		if (this.activeConditionAttributeFields != null) {
			newActiveConditionAttributeFields = this.activeConditionAttributeFields.select(objectIndices, accelerateByReadOnlyResult);
			newMapper = newActiveConditionAttributeFields.getIndex2IdMapper(); //use already calculated mapper
		}
		
		Table<Attribute, Field> newNotActiveOrDescriptionAttributeFields = null;
		if (this.notActiveOrDescriptionAttributeFields != null) {
			newNotActiveOrDescriptionAttributeFields = this.notActiveOrDescriptionAttributeFields.select(objectIndices, accelerateByReadOnlyResult);
			if (newMapper == null) {
				newMapper = newNotActiveOrDescriptionAttributeFields.getIndex2IdMapper(); //use already calculated mapper
			}
		}
		
		Decision[] newDecisions = null;
		if (this.decisions != null) {
			newDecisions = new Decision[objectIndices.length];
			for (int i = 0; i < objectIndices.length; i++) {
				newDecisions[i] = this.decisions[objectIndices[i]];
			}
		}
		
		IdentificationField[] newActiveIdentificationAttributeFields = null;
		if (this.activeIdentificationAttributeFields != null) {
			newActiveIdentificationAttributeFields = new IdentificationField[objectIndices.length];
			for (int i = 0; i < objectIndices.length; i++) {
				newActiveIdentificationAttributeFields[i] = this.activeIdentificationAttributeFields[objectIndices[i]];
			}
		}
		
		return new InformationTable(this.attributes, newMapper, newActiveConditionAttributeFields, newNotActiveOrDescriptionAttributeFields,
				newDecisions, newActiveIdentificationAttributeFields, this.activeIdentificationAttributeIndex, this.attributeMap,
				this.localActiveConditionAttributeIndex2GlobalAttributeIndexMap, accelerateByReadOnlyResult);
	}
	
	/**
	 * Discards rows of this information table that correspond to objects with given indices.
	 * Returns new information table concerning subset of remaining objects (rows).
	 * 
	 * @param objectIndices indices of discarded objects that should not be present in the resulting information table (indices can repeat)
	 * @return sub-table of this information table, containing only rows corresponding to objects whose index is not in the given array
	 * 
	 * @throws NullPointerException if given array with object indices is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public InformationTable discard(int[] objectIndices) {
		return discard(objectIndices, false);
	}
	
	/**
	 * Discards rows of this information table that correspond to objects with given indices.
	 * Returns new information table concerning subset of remaining objects (rows).
	 * 
	 * @param objectIndices indices of discarded objects that should not be present in the resulting information table (indices can repeat)
	 * @param accelerateByReadOnlyResult tells if this method should return the result faster,
	 *        at the cost of returning a read-only information table, or should return a safe information table (that can be modified),
	 *        at the cost of returning the result slower
	 * @return sub-table of this information table, containing only rows corresponding to objects whose index is not in the given array
	 * 
	 * @throws NullPointerException if given array with object indices is {@code null}
	 * @throws IndexOutOfBoundsException if any of the given indices does not match the number of considered objects
	 */
	public InformationTable discard(int[] objectIndices, boolean accelerateByReadOnlyResult) {
		int size = getNumberOfObjects();
		IntOpenHashSet discardedIndices = new IntOpenHashSet();
		
		for (int i : objectIndices) {
			if (i >= 0 && i < size) {
				discardedIndices.add(i); //eliminates duplicates of discarded indices
			} else {
				throw new IndexOutOfBoundsException("Discarded object's index is out of information table range.");
			}
		}
		
		int selectedIndices[] = new int[size - discardedIndices.size()];
		int index = 0;
		for (int i = 0; i < size; i++) {
			if (!discardedIndices.contains(i)) {
				selectedIndices[index++] = i;
			}
		}
		
		return select(selectedIndices, accelerateByReadOnlyResult);
	}
	
	/**
	 * Verifies attributes of this information table for their eligibility to preference order imposition or binarization.
	 * If there are no such attributes, then just returns this object.
	 * Otherwise, pursues a transformation of this object and returns a new information table (for a new array of attributes).<br>
	 * <br>
	 * An attribute is eligible to preference order imposition if it (jointly) fulfills the following conditions:
	 * <ul>
	 * <li>is active,</li>
	 * <li>its {@link EvaluationAttribute#getType() type} equals {@link AttributeType#CONDITION},</li>
	 * <li>is an instance of {@link EvaluationAttribute},</li>
	 * <li>its {@link EvaluationAttribute#getPreferenceType() preference type} equals {@link AttributePreferenceType#NONE},</li>
	 * <li>has {@link EvaluationAttribute#getValueType() value type} other than {@link EnumerationField}.</li>
	 * </ul>
	 * An attribute is eligible to binarization if it (jointly) fulfills the following conditions:
	 * <ul>
	 * <li>is active,</li>
	 * <li>its {@link EvaluationAttribute#getType() type} equals {@link AttributeType#CONDITION},</li>
	 * <li>is an instance of {@link EvaluationAttribute},</li>
	 * <li>its {@link EvaluationAttribute#getPreferenceType() preference type} equals {@link AttributePreferenceType#NONE},</li>
	 * <li>its {@link EvaluationAttribute#getValueType() value type} equals {@link EnumerationField},
	 * <li>parameter {@code transformNominalAttributesWith3PlusValues} is {@code true},</li>
	 * <li>there are more than two values in {@link EnumerationField#getElementList() attributes' domain}.</li>
	 * </ul>
	 * Each eligible attribute is replaced with two, or sometimes more, new evaluation attributes, depending on its {@link EvaluationAttribute#getValueType() value type}.<br>
	 * <br>
	 * An attribute eligible to preference order imposition is replaced by two new attributes - one with assumed
	 * {@link AttributePreferenceType#GAIN gain-type preference}, and another with assumed {@link AttributePreferenceType#COST cost-type preference}.
	 * For example, attribute "age" with unknown preference order and {@link IntegerField} value type will result in two new evaluation attributes.
	 * The first one will have {@link IntegerField} value type, {@link AttributePreferenceType#GAIN} preference type,
	 * and name "age&lt;{@link InformationTable#attributeNameSuffixGain}&gt;".
	 * The second one will have {@link IntegerField} value type, {@link AttributePreferenceType#COST} preference type,
	 * and name "age&lt;{@link InformationTable#attributeNameSuffixCost}&gt;".
	 * See also {@link InformationTable#attributeNameSuffixGain} and {@link InformationTable#attributeNameSuffixCost}.<br>
	 * <br>
	 * An attribute eligible to binarization undergoes a binarization procedure,
	 * where each domain value yields a new binary attribute (having 0/1 evaluations) with {@link IntegerField} {@link EvaluationAttribute#getValueType() value type}.
	 * For example, attribute "color" with unknown preference order, {@link EnumerationField} value type, and three values in its domain: "red", "green", "blue",
	 * will result in three new binary (0/1) attributes, with {@link IntegerField} value type. These attributes will have the following names:
	 * "color_red&lt;{@link InformationTable#attributeNameSuffixNone}&gt;", "color_green&lt;{@link InformationTable#attributeNameSuffixNone}&gt;",
	 * "color_blue&lt;{@link InformationTable#attributeNameSuffixNone}&gt;". See also {@link InformationTable#attributeNameSuffixNone}.<br>
	 * <br>
	 * This method does not modify any other attributes than the eligible ones defined above. In particular, it does not modify decision attribute(s).
	 * 
	 * @param binarizeNominalAttributesWith3PlusValues tells if active {@link AttributeType#CONDITION condition} nominal {@link EvaluationAttribute evaluation attributes},
	 *        (i.e., attributes with {@link EnumerationField} {@link EvaluationAttribute#getValueType() value type}
	 *        and {@link AttributePreferenceType#NONE without preference type}) having 3+ values in their {@link EnumerationField#getElementList() domain},
	 *        should be binarized
	 * 
	 * @return this information table, if there are no attributes eligible to preference order imposition or binarization,
	 *         or a new information table (with new array of attributes)
	 *         resulting from imposition of preference order or binarization for each eligible attribute
	 */
	public InformationTable imposePreferenceOrders(boolean binarizeNominalAttributesWith3PlusValues) {
		PreferenceOrdersImpositionHelper helper = new PreferenceOrdersImpositionHelper(binarizeNominalAttributesWith3PlusValues);
		
		if (helper.attributesCountAfterImposition == getNumberOfAttributes()) { //this information table does not need to be transformed
			return this; //just return this information table!
		} else {
			EvaluationAttribute evaluationAttribute;
			ElementList elementList;
			int oldAttributesCount = getNumberOfAttributes();
			int elementListSize;

			for (helper.oldAttributeIndex = 0; helper.oldAttributeIndex < oldAttributesCount; helper.oldAttributeIndex++) {
				if (isActiveConditionAttribute(attributes[helper.oldAttributeIndex])) { //active condition evaluation attribute
					evaluationAttribute = (EvaluationAttribute)attributes[helper.oldAttributeIndex];
					if (evaluationAttribute.getPreferenceType() == AttributePreferenceType.NONE) { //processing potentially necessary
						if (evaluationAttribute.getValueType() instanceof EnumerationField) { //nominal attribute that potentially should be binarized if has more than two values
							elementList = ((EnumerationField)evaluationAttribute.getValueType()).getElementList();
							
							if ((elementListSize = elementList.getSize()) > 2 && binarizeNominalAttributesWith3PlusValues) { //0/1 binarization necessary
								for (int elementListIndex = 0; elementListIndex < elementListSize; elementListIndex++) {
									//helper.create01AttributeAndCalculateItsFields(elementListIndex, AttributePreferenceType.GAIN);
									//helper.create01AttributeAndCalculateItsFields(elementListIndex, AttributePreferenceType.COST);
									//just binarize the attribute to enable meaningful equality conditions
									helper.create01AttributeAndCalculateItsFields(elementListIndex, AttributePreferenceType.NONE);
								}
							} else {
								//transformNominalAttributesWith3PlusValues == false - leave the enumeration attribute "as is"
								//or
								//just two values (already binary attribute) - leave the enumeration attribute "as is" (duplication + preference order imposition would
								//not introduce added value over this what can be achieved by just working with this nominal attribute and using equality conditions in rules)
								//helper.cloneNoneAttribute2GainAndCostAttributesAndSetTheirFields();
								helper.copyAttributeAndItsFields();
							}
							//follow with duplication + preference order imposition
						} else { //numerical (ordinal) attribute - only duplication + preference order imposition necessary
							helper.cloneNoneAttribute2GainAndCostAttributesAndSetTheirFields();
						}
					} else { //leave attribute "as is"
						helper.copyAttributeAndItsFields();
					}
				} else { //leave attribute "as is"
					helper.copyAttributeAndItsFields();
				}
			}
			
			InformationTable newInformationTable = new InformationTable(helper.newAttributes, helper.newListOfFields, true);
			newInformationTable.mapper = this.mapper; //preserve globally unique ids of objects (as the objects did not change, only their representation!) //TODO: OK?
			newInformationTable.orderedUniqueFullyDeterminedDecisions = this.orderedUniqueFullyDeterminedDecisions; //use if already calculated
			newInformationTable.uniqueDecisions = this.uniqueDecisions; //use if already calculated
			
			return newInformationTable;			
		}
	}
	
	/**
	 * Helper class for {@link InformationTable#imposePreferenceOrders()} method.
	 * Groups supplementary methods invoked by {@link InformationTable#imposePreferenceOrders()},
	 * and stores as properties variables shared by these supplementary methods.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class PreferenceOrdersImpositionHelper {
		
		/**
		 * Tells if active {@link AttributeType#CONDITION condition} nominal {@link EvaluationAttribute evaluation attributes},
		 * (i.e., attributes with {@link EnumerationField} {@link EvaluationAttribute#getValueType() value type}
		 * and {@link AttributePreferenceType#NONE without preference type}) having 3+ values in their {@link EnumerationField#getElementList() domain},
		 * should be binarized.
		 * It is worth noting that nominal attributes with less than three values in their domain are not processed (leaved "as is").
		 */
		boolean binarizeNominalAttributesWith3PlusValues;
		
		/**
		 * Number of attributes after imposing preference order or binarization to eligible active condition evaluation attributes.
		 * Calculated before imposing anything.
		 */
		int attributesCountAfterImposition;
		
		IntegerField binaryField0Gain; //singleton
		IntegerField binaryField0Cost; //singleton
		IntegerField binaryField0None; //singleton
		IntegerField binaryField1Gain; //singleton
		IntegerField binaryField1Cost; //singleton
		IntegerField binaryField1None; //singleton
		
		Attribute[] oldAttributes; //attributes from the information table; constant
		Attribute[] newAttributes; //attributes resulting from the transformation of the information table; to be used when constructing a new information table

		int oldAttributeIndex; //where to take old attribute from; this index is set externally, by the information table, not by the methods of this helper
		int newAttributeIndex; //where to put a new attribute to

		int numberOfObjects; //constant
		List<Field[]> newListOfFields; //to be used when constructing a new information table
		
		/**
		 * Constructs this object. First sets {@link #binarizeNominalAttributesWith3PlusValues} property according to the given parameter,
		 * and then calculates {@link #attributesCountAfterImposition}.
		 * This calculation is necessary to tell if any attributes of the information table are eligible to imposing preference order or binarization.<br>
		 * <br>
		 * If calculated value of {@link #attributesCountAfterImposition} is greater than
		 * {@link InformationTable#getNumberOfAttributes() the number of attributes of the information table},
		 * invokes {@link #init()} to initialize all other properties of this helper.
		 * 
		 * @param binarizeNominalAttributesWith3PlusValues tells if active {@link AttributeType#CONDITION condition} nominal {@link EvaluationAttribute evaluation attributes},
		 *        (i.e., attributes with {@link EnumerationField} {@link EvaluationAttribute#getValueType() value type}
		 *        and {@link AttributePreferenceType#NONE without preference type}) having 3+ values in their {@link EnumerationField#getElementList() domain},
		 *        should be binarized
		 */
		private PreferenceOrdersImpositionHelper(boolean binarizeNominalAttributesWith3PlusValues) {
			this.binarizeNominalAttributesWith3PlusValues = binarizeNominalAttributesWith3PlusValues;
			this.attributesCountAfterImposition = countAttributesAfterImposition();
			
			if (attributesCountAfterImposition > getNumberOfAttributes()) {
				init();
			}
		}
		
		/**
		 * Counts number of attributes in an information table that would result from imposing preference order to eligible attributes of the information table
		 * and from binarizing eligible attributes of the information table.
		 * 
		 * @return number of attributes in an information table that would result from imposing preference order to eligible attributes of the information table
		 *         and from binarizing eligible attributes of the information table
		 */
		private int countAttributesAfterImposition() {
			EvaluationAttribute evaluationAttribute;
			int elementListSize;
			
			int numberOfAttributes = getNumberOfAttributes();
			int attributesCountAfterImposition = 0; //number of all attributes if preference order / binarization was imposed for eligible attributes 
			
			for (int i = 0; i < numberOfAttributes; i++) {
				if (isActiveConditionAttribute(attributes[i])) {
					evaluationAttribute = (EvaluationAttribute)attributes[i];
					if (evaluationAttribute.getPreferenceType() == AttributePreferenceType.NONE) { //processing potentially necessary
						if (evaluationAttribute.getValueType() instanceof EnumerationField) { //nominal attribute that potentially should be binarized if has more than two values
							elementListSize = ((EnumerationField)evaluationAttribute.getValueType()).getElementList().getSize();
							
							if (elementListSize > 2 && binarizeNominalAttributesWith3PlusValues) { //0/1 binarization necessary
								attributesCountAfterImposition += elementListSize;
							} else {
								//transformNominalAttributesWith3PlusValues == false - leave the enumeration attribute "as is"
								//or
								//just two values (already binary attribute) - leave the enumeration attribute "as is" (duplication + preference order imposition would
								//not introduce added value over this what can be achieved by just working with this nominal attribute and using equality conditions in rules)
								attributesCountAfterImposition++;
							}
							//follow with duplication + preference order imposition
						} else { //only duplication + preference order imposition necessary
							attributesCountAfterImposition += 2;
						}
					} else { //leave attribute "as is"
						attributesCountAfterImposition++;
					}
				} else { //leave attribute "as is"
					attributesCountAfterImposition++;
				}
			}
			
			return attributesCountAfterImposition;
		}
		
		/**
		 * Initializes properties of this object, except for {@link #binarizeNominalAttributesWith3PlusValues} and
		 * {@link #attributesCountAfterImposition}, which are set already in class constructor.
		 */
		private void init() {
			binaryField0Gain = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN); //singleton
			binaryField0Cost = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST); //singleton
			binaryField0None = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE); //singleton
			binaryField1Gain = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN); //singleton
			binaryField1Cost = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST); //singleton
			binaryField1None = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE); //singleton
			
			oldAttributes = attributes; //constant (variable name changed for convenience)
			newAttributes = new Attribute[attributesCountAfterImposition];
			
			oldAttributeIndex = 0;
			newAttributeIndex = 0;
			
			numberOfObjects = getNumberOfObjects();  //constant
			newListOfFields = new ObjectArrayList<Field[]>();
			
			//initialized storage for fields
			for (int i = 0; i < numberOfObjects; i++) {
				newListOfFields.add(new Field[attributesCountAfterImposition]);
			}
		}
		
		//gets suffix of the name of a new attribute having given preference type
		private String getNameSuffix(AttributePreferenceType newPreferenceType) {
			switch(newPreferenceType) {
			case GAIN:
				return attributeNameSuffixGain;
			case COST:
				return attributeNameSuffixCost;
			case NONE:
				return attributeNameSuffixNone;
			default:
				throw new InvalidValueException("Name suffix is only defined for GAIN or COST attribute preference type.");
			}
		}
		
		//gets already created field (corresponding to value 0 or 1) of a binary attribute with given preference type
		private IntegerField getBinaryField(int binaryValue, AttributePreferenceType newPreferenceType) {
			switch (binaryValue) {
			case 0:
				switch (newPreferenceType) {
				case GAIN:
					return binaryField0Gain;
				case COST:
					return binaryField0Cost;
				case NONE:
					return binaryField0None;
				default:
					throw new InvalidValueException("Cannot get binary field 0 for null attribute's preference type.");
				}
			case 1:
				switch (newPreferenceType) {
				case GAIN:
					return binaryField1Gain;
				case COST:
					return binaryField1Cost;
				case NONE:
					return binaryField1None;
				default:
					throw new InvalidValueException("Cannot get binary field 1 for null attribute's preference type.");
				}
			default:
				throw new InvalidValueException("Binary value should be 0 or 1.");
			}
		}
		
		//copies an attribute and respective fields (1 column) from old to new information table
		private void copyAttributeAndItsFields() {
			newAttributes[newAttributeIndex] = oldAttributes[oldAttributeIndex]; //copy attribute
			
			for (int objectIndex = 0; objectIndex < numberOfObjects; objectIndex++) { //copy fields between columns of old and new information table
				newListOfFields.get(objectIndex)[newAttributeIndex] = getField(objectIndex, oldAttributeIndex);
			}
			
			newAttributeIndex++;
		}
		
		//builds a "similar" attribute - differing only by name and preference type
		private EvaluationAttribute cloneAttributeUsingNewPreferenceType(AttributePreferenceType newPreferenceType) {
			EvaluationAttribute duplicatedOldAttribute = (EvaluationAttribute)oldAttributes[oldAttributeIndex];
			return new EvaluationAttribute(
					duplicatedOldAttribute.getName() + getNameSuffix(newPreferenceType), //add name suffix
					duplicatedOldAttribute.isActive(),
					duplicatedOldAttribute.getType(),
					duplicatedOldAttribute.getValueType().clone(newPreferenceType), //clone value type but with preference type modification 
					duplicatedOldAttribute.getMissingValueType(),
					newPreferenceType);
		}
		
		//transforms a NONE evaluation attribute to two (GAIN and COST) new attributes and sets respective fields (1 column -> 2 columns)
		private void cloneNoneAttribute2GainAndCostAttributesAndSetTheirFields() {
			AttributePreferenceType attributePreferenceType;
			for (int i = 0; i < 2; i++) { //two iterations, each with different attribute preference type
				attributePreferenceType = (i == 0 ? AttributePreferenceType.GAIN : AttributePreferenceType.COST);
				newAttributes[newAttributeIndex] = cloneAttributeUsingNewPreferenceType(attributePreferenceType); //create new attribute
				
				for (int objectIndex = 0; objectIndex < numberOfObjects; objectIndex++) { //copy fields between columns of old and new information table, changing just attribute's preference type
					newListOfFields.get(objectIndex)[newAttributeIndex] = ((EvaluationField)getField(objectIndex, oldAttributeIndex)).clone(attributePreferenceType); //TODO: use cloning with caching factory to spare some memory
				}
				
				newAttributeIndex++;
			}
		}
		
		//creates new binary IntegerField attribute corresponding to particular element in the domain of an old enumeration attribute
		private EvaluationAttribute create01Attribute(int elementListIndex, AttributePreferenceType newPreferenceType) { //current old attribute has enumeration field value type
			EvaluationAttribute binarizedOldAttribute = (EvaluationAttribute)oldAttributes[oldAttributeIndex];
			String domainValue = ((EnumerationField)binarizedOldAttribute.getValueType()).getElementList().getElement(elementListIndex);
			
			return new EvaluationAttribute(
					(new StringBuilder()).append(binarizedOldAttribute.getName()).append("_").append(domainValue).append(getNameSuffix(newPreferenceType)).toString(),
					binarizedOldAttribute.isActive(),
					binarizedOldAttribute.getType(),
					IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, newPreferenceType), //binary attribute is created with IntegerField value type
					binarizedOldAttribute.getMissingValueType(),
					newPreferenceType);
		}
		
		//creates new binary IntegerField attribute corresponding to a particular element in the domain of an old enumeration attribute
		//and calculates new fields corresponding to that new binary attribute
		private void create01AttributeAndCalculateItsFields(int elementListIndex, AttributePreferenceType newPreferenceType) {
			newAttributes[newAttributeIndex] = create01Attribute(elementListIndex, newPreferenceType); //create new binary attribute
			
			for (int objectIndex = 0; objectIndex < numberOfObjects; objectIndex++) { //calculate fields in the new column
				newListOfFields.get(objectIndex)[newAttributeIndex] = (((EnumerationField)getField(objectIndex, oldAttributeIndex)).getValue() == elementListIndex) ?
						getBinaryField(1, newPreferenceType) : getBinaryField(0, newPreferenceType);
			}
			
			newAttributeIndex++;
		}
		
	}
	
	/**
	 * Gets number of objects stored in this information table.
	 * 
	 * @return number of objects stored in this information table
	 */
	public int getNumberOfObjects() {
		if (this.activeConditionAttributeFields != null) {
			return this.activeConditionAttributeFields.getNumberOfObjects();
		} else {
			if (this.notActiveOrDescriptionAttributeFields != null) {
				return this.notActiveOrDescriptionAttributeFields.getNumberOfObjects();
			} else {
				if (this.decisions != null) {
					return this.decisions.length;
				} else {
					if (this.activeIdentificationAttributeFields != null) {
						return this.activeIdentificationAttributeFields.length;
					} else {
						return 0;
					}
				}
			}
		}
	}
	
	/**
	 * Gets number of attributes stored in this information table.
	 * 
	 * @return number of attributes stored in this information table
	 */
	public int getNumberOfAttributes() {
		return this.attributes.length;
	}

//	/**
//	 * Gets index of the only active decision attribute used in calculations. If there is no such attribute, returns -1.
//	 * 
//	 * @return index of the only active decision attribute used in calculations, or -1 if there is no such attribute
//	 */
//	public int getActiveDecisionAttributeIndex() {
//		return this.activeDecisionAttributeIndex;
//	}
	
	/**
	 * Gets index of the only active identification attribute. If there is no such attribute, returns -1.
	 * 
	 * @return index of the only active identification attribute, or -1 if there is no such attribute
	 */
	public int getActiveIdentificationAttributeIndex() {
		return this.activeIdentificationAttributeIndex;
	}
	
	/**
	 * Takes given local index of an active condition attribute (i.e., index of such attribute in the table returned by {@link #getActiveConditionAttributeFields()},
	 * and translates it to the index that this attribute has in the array returned by {@link #getAttributes()}.<br>
	 * <br>
	 * Suppose there are nine attributes:<br>
	 * - attr0: active {@link EvaluationAttribute} of type {@link AttributeType#CONDITION},<br>
	 * - attr1: active {@link EvaluationAttribute} of type {@link AttributeType#DESCRIPTION},<br>
	 * - attr2: non-active {@link EvaluationAttribute} of type {@link AttributeType#CONDITION},<br>
	 * - attr3: active {@link EvaluationAttribute} of type {@link AttributeType#CONDITION},<br>
	 * - attr4: active {@link EvaluationAttribute} of type {@link AttributeType#DECISION},<br>
	 * - attr5: non-active {@link EvaluationAttribute} of type {@link AttributeType#DECISION},<br>
	 * - attr6: non-active {@link EvaluationAttribute} of type {@link AttributeType#DESCRIPTION},<br>
	 * - attr7: active {@link IdentificationAttribute} with value type {@link TextIdentificationField},<br>
	 * - attr8: non-active {@link IdentificationAttribute} with value type {@link UUIDIdentificationField}.<br>
	 * <br>
	 * Then:<br>
	 * - {@code translateActiveConditionAttributeIndex2GlobalAttributeIndex[0] == 0},<br>
	 * - {@code translateActiveConditionAttributeIndex2GlobalAttributeIndex[1] == 3},<br>
	 * and {@code translateActiveConditionAttributeIndex2GlobalAttributeIndex[i] == -1} for {@code i different than 0 and 1}.
	 * 
	 * @param localActiveConditionAttributeIndex index of an attribute in the table returned by {@link #getActiveConditionAttributeFields()}
	 * @return index of the considered attribute in the array of attributes returned by {@link #getAttributes()},
	 *         or -1 if given attribute index does not map to any global attribute index
	 */
	public int translateLocalActiveConditionAttributeIndex2GlobalAttributeIndex(int localActiveConditionAttributeIndex) {
		if (this.localActiveConditionAttributeIndex2GlobalAttributeIndexMap.containsKey(localActiveConditionAttributeIndex)) {
			return this.localActiveConditionAttributeIndex2GlobalAttributeIndexMap.get(localActiveConditionAttributeIndex);
		} else {
			return -1;
		}
	}
	
	/**
	 * Gets all fields of the object with given index.
	 * 
	 * @param objectIndex index of an object from this information table
	 * @return all fields of the object with given index
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object for which this table stores fields
	 */
	public Field[] getFields(int objectIndex) {
		int numberOfAttributes = getNumberOfAttributes();
		Field[] fields = new Field[numberOfAttributes];
		
		for (int i = 0; i < numberOfAttributes; i++) {
			if (attributeMap[i] > 0) { //active condition evaluation attribute
				fields[i] = activeConditionAttributeFields.getField(objectIndex, decodeActiveConditionAttributeIndex(attributeMap[i]));
			} else {
				if (attributeMap[i] < 0) { //non-active or description attribute
					fields[i] = notActiveOrDescriptionAttributeFields.getField(objectIndex, decodeNotActiveOrDescriptionAttributeIndex(attributeMap[i]));
				} else { //attributeMap[i] == 0
					if (i == activeIdentificationAttributeIndex) { //the only active identification (condition) attribute
						fields[i] = activeIdentificationAttributeFields[objectIndex];
					} else { //active decision evaluation attribute
						fields[i] = decisions[objectIndex].getEvaluation(i);
					}
				}
			}
		}
		
		return fields;
	}
	
	/**
	 * Tells if it is safe to use this information table to induce possible decision rules, considered in rough set approaches.
	 * The result is {@code false} if learning data contain missing attribute values that can lead to non-transitivity of dominance/indiscernibility relation,
	 * for example of type {@link UnknownSimpleFieldMV2}. Otherwise the result is {@code true}.<br>
	 * <br>
	 * See the disclaimer in {@link VCDomLEM} class. 
	 * 
	 * @return {@code false} if learning data contain missing attribute values that can lead to non-transitivity of dominance/indiscernibility relation,
	 *         for example of type {@link UnknownSimpleFieldMV2},
	 *         {@code true} otherwise
	 * @see VCDomLEM
	 */
	public boolean isSuitableForInductionOfPossibleRules() {
		EvaluationAttribute[] attributes = activeConditionAttributeFields.getAttributes(true);
		int numObj = activeConditionAttributeFields.getNumberOfObjects();
		int numAttr = activeConditionAttributeFields.getNumberOfAttributes();
		
		for (int j = 0; j < numAttr; j++) {
			//TODO: make the check more generic (i.e., check property of missing value type rather than particular sub-type)
			if (attributes[j].getMissingValueType() instanceof UnknownSimpleFieldMV2) { //there is a possibility of problematic MV for current active condition evaluation attribute
				for (int i = 0; i < numObj; i++) {
					if (activeConditionAttributeFields.getField(i, j) instanceof UnknownSimpleFieldMV2) { //problematic MV does appear
						return false;
					}
				}
			}
			
		}
		
		return true;
	}

}
