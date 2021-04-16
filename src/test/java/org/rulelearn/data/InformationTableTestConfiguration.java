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
import java.util.UUID;

import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IdentificationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;
import org.rulelearn.types.UnknownSimpleField;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Configuration used to create information table for testing purposes.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableTestConfiguration {
	protected Attribute[] attributes;
	//protected int activeDecisionAttributeIndex = -1;
	protected int activeIdentificationAttributeIndex = -1;
	protected List<Integer> listOfActiveDecisionAttributeIndices;
	protected List<Field[]> listOfFields;
	protected int[] allObjectIndices = null;
	
	/**
	 * Parses a textual-form value (field) concerning an evaluation attribute.
	 * 
	 * @param attribute attribute whose value should be parsed
	 * @param fieldAsText textual-form of attribute's value (field)
	 * @return parsed value (field)
	 * @throws NumberFormatException if given text should be parsed into an int/double value, but does not represent a valid number
	 * @throws InvalidTypeException if value type of given attribute is none of {@link IntegerField}, {@link RealField}, {@link EnumerationField}
	 */
	protected Field parseEvaluationAttributeField(EvaluationAttribute attribute, String fieldAsText) {
		EvaluationField valueType = attribute.getValueType();
		UnknownSimpleField missingValueType = attribute.getMissingValueType();
		String missingValueString = missingValueType.toString();
		
		if (fieldAsText.trim().equals(missingValueString)) { //handle missing value on evaluation attribute
			return missingValueType;
		}
		
		if (valueType instanceof IntegerField) {
			return IntegerFieldFactory.getInstance().create(Integer.parseInt(fieldAsText), attribute.getPreferenceType());
		} else if (valueType instanceof RealField) {
			return RealFieldFactory.getInstance().create(Double.parseDouble(fieldAsText), attribute.getPreferenceType());
		} else if (valueType instanceof EnumerationField) {
			return EnumerationFieldFactory.getInstance().create(((EnumerationField)valueType).getElementList(), ((EnumerationField)valueType).getElementList().getIndex(fieldAsText), attribute.getPreferenceType());
		} else {
			throw new InvalidTypeException("Encountered unsupported type of evaluation field.");
		}
	}
	
	/**
	 * Parses a textual-form value (field) concerning an identification attribute.
	 * 
	 * @param attribute attribute whose value should be parsed
	 * @param fieldAsText textual-form of attribute's value (field)
	 * @return parsed value (field)
	 * @throws InvalidTypeException if value type of given attribute is none of {@link TextIdentificationField}, {@link UUIDIdentificationField}
	 */
	protected Field parseIdentificationAttributeField(IdentificationAttribute attribute, String fieldAsText) {
		IdentificationField idType = attribute.getValueType();
		
		if (idType instanceof TextIdentificationField) {
			return new TextIdentificationField(fieldAsText);
		} else if (idType instanceof UUIDIdentificationField) {
			return new UUIDIdentificationField(UUID.fromString(fieldAsText));
		} else {
			throw new InvalidTypeException("Encountered unsupported type of identification field.");
		}
	}
	
	/**
	 * Converts (parses) given matrix of fields to a list of fields of subsequent objects.
	 * 
	 * @param attributes attributes of the objects
	 * @param matrixOfFieldsAsText matrix of fields in textual form
	 * @return list of fields of subsequent objects
	 * 
	 * @throws InvalidTypeException when type of some attribute is none of {@link EvaluationAttribute}, {@link IdentificationAttribute}
	 */
	protected List<Field[]> convertFields(Attribute[] attributes, String[][] matrixOfFieldsAsText) {
		List<Field[]> listOfFields = new ObjectArrayList<Field[]>();
		Field[] fields;
		
		for (int i = 0; i < matrixOfFieldsAsText.length; i++) {
			fields = new Field[matrixOfFieldsAsText[i].length];
			
			for (int j = 0; j < matrixOfFieldsAsText[i].length; j++) {
				if (attributes[j] instanceof EvaluationAttribute) {
					fields[j] = this.parseEvaluationAttributeField((EvaluationAttribute)attributes[j], matrixOfFieldsAsText[i][j]);
				} else if (attributes[j] instanceof IdentificationAttribute) {
					fields[j] = this.parseIdentificationAttributeField((IdentificationAttribute)attributes[j], matrixOfFieldsAsText[i][j]);
				} else {
					throw new InvalidTypeException("Encountered unsupported type of attribute.");
				}
			}
			
			listOfFields.add(fields);
		}
		
		return listOfFields;
	}
	
//	/**
//	 * Sets index of the only active decision attribute (if there is such attribute). Throws {@link InvalidValueException} if there is more than one such attribute.
//	 * Assumes that {@link #attributes} have been already set.
//	 * 
//	 * @throws InvalidValueException if there is more than one active decision attribute
//	 */
//	protected void setDecisionAttributeIndex() {
//		//calculate index of the active decision attribute, and verify if there is only one such attribute
//		this.activeDecisionAttributeIndex = -1;
//		for (int i = 0; i < this.attributes.length; i++) {
//			if (this.attributes[i] instanceof EvaluationAttribute) {
//				if (this.attributes[i].isActive() && ((EvaluationAttribute)this.attributes[i]).getType() == AttributeType.DECISION) {
//					if (this.activeDecisionAttributeIndex >= 0) {
//						throw new InvalidValueException("More than 1 active decision attribute detected.");
//					} else {
//						this.activeDecisionAttributeIndex = i;
//					}
//				}
//			}
//		}
//	}
	
	/**
	 * Sets list of indices of active decision attributes.
	 * Assumes that {@link #attributes} have been already set.
	 */
	protected void setActiveDecisionAttributeIndices() {
		this.listOfActiveDecisionAttributeIndices = new ArrayList<Integer>();
		
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] instanceof EvaluationAttribute) {
				if (((EvaluationAttribute)attributes[i]).getType() == AttributeType.DECISION && attributes[i].isActive()) {
					this.listOfActiveDecisionAttributeIndices.add(Integer.valueOf(i));
				}
			}
		}
	}
	
	/**
	 * Sets index of the only active identification attribute (if there is such attribute). Throws {@link InvalidValueException} if there is more than one such attribute.
	 * Assumes that {@link #attributes} have been already set.
	 * 
	 * @throws InvalidValueException if there is more than one active identification attribute
	 */
	protected void setIdentificationAttributeIndex() {
		//calculate index of the active identification attribute, and verify if there is only one such attribute
		this.activeIdentificationAttributeIndex = -1;
		for (int i = 0; i < this.attributes.length; i++) {
			if (this.attributes[i] instanceof IdentificationAttribute) {
				if (this.attributes[i].isActive()) {
					if (this.activeIdentificationAttributeIndex >= 0) {
						throw new InvalidValueException("More than 1 active identification attribute detected.");
					} else {
						this.activeIdentificationAttributeIndex = i;
					}
				}
			}
		}
	}
	
	/**
	 * Constructs this test configuration.
	 * 
	 * @param attributes attributes of an information table
	 * @param fieldsAsText fields of an information table, in textual form
	 * 
	 * @throws NullPointerException if attributes are {@code null}
	 * @throws InvalidValueException if more than one active decision attribute is detected
	 */
	public InformationTableTestConfiguration(Attribute[] attributes, String[][] fieldsAsText) {
		this.attributes = attributes.clone();
//		this.setDecisionAttributeIndex();
		this.setActiveDecisionAttributeIndices();
		this.setIdentificationAttributeIndex();
		this.listOfFields = convertFields(attributes, fieldsAsText);
	}
	
	/**
	 * Gets list with indices of all objects considered in this configuration.
	 * 
	 * @return list with indices of all objects considered in this configuration
	 */
	protected int[] getAllObjectIndices() {
		if (this.allObjectIndices == null) {
			this.allObjectIndices = new int[this.getNumberOfObjects()];
			
			for (int i = 0; i < this.allObjectIndices.length; i++) {
				this.allObjectIndices[i] = i;
			}
		}
		return this.allObjectIndices;
	}
	
	/**
	 * Gets the attributes. On each method call, a new array with clones of the attributes stored in this configuration is returned.
	 * 
	 * @return the attributes of this configuration (new array with clones of the attributes stored in this configuration)
	 * @throws InvalidTypeException if some of the attributes in neither {@link EvaluationAttribute} nor {@link IdentificationAttribute}
	 */
	public Attribute[] getAttributes() {
		Attribute[] clonedAttributes = new Attribute[this.attributes.length];
		
		EvaluationAttribute evalAttribute;
		IdentificationAttribute idAttribute;
		
		//clone each attribute into the newly allocated array
		for (int i = 0; i < this.attributes.length; i++) {
			if (this.attributes[i] instanceof EvaluationAttribute) {
				evalAttribute = (EvaluationAttribute)this.attributes[i];
				clonedAttributes[i] = new EvaluationAttribute (
						evalAttribute.getName(), evalAttribute.isActive(),
						evalAttribute.getType(), evalAttribute.getValueType(), evalAttribute.getMissingValueType(), evalAttribute.getPreferenceType()
				);
			} else if (this.attributes[i] instanceof IdentificationAttribute) {
				idAttribute = (IdentificationAttribute)this.attributes[i];
				clonedAttributes[i] = new IdentificationAttribute (
						idAttribute.getName(), idAttribute.isActive(),
						idAttribute.getValueType()
				);
			} else {
				throw new InvalidTypeException("Unsupported attribute type.");
			}
		}
		
		return clonedAttributes;
	}
	
	/**
	 * Gets a list of all rows, each corresponding to fields of a single object.
	 * On each method call, a new list composed of new arrays composed of new fields is returned.<br>
	 * <br>
	 * Wraps {@link #getListOfFields(int[])} by first calculating array with ordered indices of all objects (rows),
	 * and then invoking wrapped method using this array as an input parameter.
	 * 
	 * @return see {@link #getListOfFields(int[])}
	 * 
	 * @throws InvalidTypeException see {@link #getListOfFields(int[])}
	 */
	public List<Field[]> getListOfFields() {
		return this.getListOfFields(this.getAllObjectIndices());
	}
	
	/**
	 * Gets a list of rows, each corresponding to fields of a single object.
	 * On each method call, a new list composed of new arrays composed of new fields is returned.
	 * 
	 * @param objectIndices list of indices of selected rows (only fields from these rows should be returned)
	 * @return the list of rows, each corresponding to fields of a single object
	 * 
	 * @throws InvalidTypeException if unsupported sub-type of {@link Attribute} is encountered
	 * @throws InvalidTypeException if unsupported sub-type of {@link EvaluationField} is encountered
	 * @throws InvalidTypeException if unsupported sub-type of {@link IdentificationField} is encountered 
	 * @throws NullPointerException if objects' indices are {@code null}
	 * @throws IndexOutOfBoundsException if any of the given object indices is not in the interval [0, {@code this#getNumberOfObjects()})
	 */
	public List<Field[]> getListOfFields(int[] objectIndices) {
		if (objectIndices == null) {
			throw new NullPointerException("Objects' indices are null.");
		}
		
		List<Field[]> clonedListOfFields = new ObjectArrayList<Field[]>();
		Field[] selectedRow;
		Field[] clonedSelectedRow;
		
		for (int i = 0; i < objectIndices.length; i++) { //go through selected objects (rows)
			selectedRow = this.listOfFields.get(objectIndices[i]);
			clonedSelectedRow = new Field[selectedRow.length];
			
			for (int j = 0; j < selectedRow.length; j++) {
				clonedSelectedRow[j] = selectedRow[j].selfClone();
			}
			
			clonedListOfFields.add(clonedSelectedRow);
		}
		
		return clonedListOfFields;
	}
	
	/**
	 * Gets decisions of objects (rows) with given indices.<br>
	 * <br>
	 * Wraps {@link #getDecisions(int[])} by first calculating array with ordered indices of all objects (rows),
	 * and then invoking wrapped method using this array as an input parameter.
	 * 
	 * @return see {@link #getDecisions(int[])}
	 */
	public Decision[] getDecisions() {
		return this.getDecisions(this.getAllObjectIndices());
	}
	
	/**
	 * Gets decisions of objects (rows) with given indices.
	 * 
	 * @param objectIndices list of indices of selected rows (only decisions concerning these objects (rows) should be returned)
	 * @return decisions concerning objects (rows) with given indices or {@code null} if there is no active decision attribute
	 * 
	 * @throws NullPointerException if objects' indices are {@code null}
	 * @throws IndexOutOfBoundsException if any of the given object indices is not in the interval [0, {@code this#getNumberOfObjects()})
	 */
	public Decision[] getDecisions(int[] objectIndices) {
		if (objectIndices == null) {
			throw new NullPointerException("Objects' indices are null.");
		}
		if (this.listOfActiveDecisionAttributeIndices.size() < 1) { //there are no active decision attributes
			return null;
		}
		
		Decision[] decisions = new Decision[objectIndices.length]; //returned array with decisions for subsequent objects
		
		int numberOfActiveDecisionAttributes = this.listOfActiveDecisionAttributeIndices.size();
		
		EvaluationField[] evaluations = new EvaluationField[numberOfActiveDecisionAttributes]; //content of this array will be overwritten for each object
		int[] attributeIndices = new int[numberOfActiveDecisionAttributes]; //content of this array will be overwritten for each object
		
		for (int i = 0; i < objectIndices.length; i++) {
			for (int j = 0; j < numberOfActiveDecisionAttributes; j++) {
				evaluations[j] = this.listOfFields.get(objectIndices[i])[this.listOfActiveDecisionAttributeIndices.get(j).intValue()].selfClone();
				attributeIndices[j] = this.listOfActiveDecisionAttributeIndices.get(j).intValue();
			}
			decisions[i] = DecisionFactory.INSTANCE.create(evaluations, attributeIndices);
		}
		
		return decisions;
	}
	
	/**
	 * Gets identifiers (active identification attribute values) of objects (rows) with given indices.<br>
	 * <br>
	 * Wraps {@link #getIdentifiers(int[])} by first calculating array with ordered indices of all objects (rows),
	 * and then invoking wrapped method using this array as an input parameter.
	 * 
	 * @return see {@link #getIdentifiers(int[])}
	 */
	public IdentificationField[] getIdentifiers() {
		return this.getIdentifiers(this.getAllObjectIndices());
	}
	
	/**
	 * Gets identifiers (active identification attribute values) of objects (rows) with given indices.
	 * 
	 * @param objectIndices list of indices of selected rows (only identifiers concerning these objects (rows) should be returned)
	 * @return identifiers of objects (rows) with given indices or {@code null} if there is no active identification attribute
	 * 
	 * @throws NullPointerException if objects' indices are {@code null}
	 * @throws IndexOutOfBoundsException if any of the given object indices is not in the interval [0, {@code this#getNumberOfObjects()})
	 */
	public IdentificationField[] getIdentifiers(int[] objectIndices) {
		if (objectIndices == null) {
			throw new NullPointerException("Objects' indices are null.");
		}
		if (this.activeIdentificationAttributeIndex < 0) {
			return null;
		}
		
		IdentificationField[] identifiers = new IdentificationField[objectIndices.length];
		
		for (int i = 0; i < objectIndices.length; i++) {
			identifiers[i] = this.listOfFields.get(objectIndices[i])[this.activeIdentificationAttributeIndex].selfClone();
		}
		
		return identifiers;
	}
	
	/**
	 * Gets number of attributes.
	 * 
	 * @return number of attributes
	 */
	public int getNumberOfAttributes() {
		return this.attributes.length;
	}
	
	/**
	 * Gets number of objects.
	 * 
	 * @return number of objects
	 */
	public int getNumberOfObjects() {
		return this.listOfFields.size();
	}
	
//	/**
//	 * Gets index of the only active decision attribute.
//	 * 
//	 * @return index of the only active decision attribute or -1 if there is no such attribute
//	 */
//	public int getActiveDecisionAttributeIndex() {
//		return this.activeDecisionAttributeIndex;
//	}
	
	/**
	 * Gets index of the only active identification attribute.
	 * 
	 * @return index of the only active identification attribute or -1 if there is no such attribute
	 */
	public int getActiveIdentificationAttributeIndex() {
		return this.activeIdentificationAttributeIndex;
	}
	
	/**
	 * Gets information table constructed from this configuration.<br>
	 * <br>
	 * Wraps {@link #getInformationTable(int[], boolean)} by first calculating array with ordered indices of all objects (rows),
	 * and then invoking wrapped method using this array as an input parameter.
	 * 
	 * @param accelerateByReadOnlyParams parameter passed to {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @return information table constructed from this configuration.
	 * 
	 * @throws InvalidTypeException see {@link #getInformationTable(int[], boolean)}
	 */
	public InformationTable getInformationTable(boolean accelerateByReadOnlyParams) {
		return this.getInformationTable(this.getAllObjectIndices(), accelerateByReadOnlyParams);
	}
	
	/**
	 * Gets information table constructed from this configuration.
	 * 
	 * @param objectIndices indices of objects selected to the constructed information table
	 * @param accelerateByReadOnlyParams parameter passed to {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @return information table constructed from this configuration.
	 * 
	 * @throws NullPointerException if objects' indices are {@code null}
	 * @throws InvalidTypeException see {@link #getAttributes()}
	 * @throws InvalidTypeException see {@link #getListOfFields(int[])}
	 * @throws IndexOutOfBoundsException see {@link #getListOfFields(int[])}
	 */
	public InformationTable getInformationTable(int[] objectIndices, boolean accelerateByReadOnlyParams) {
		if (objectIndices == null) {
			throw new NullPointerException("Objects' indices are null.");
		}
		
		return new InformationTable(
				this.getAttributes(),
				this.getListOfFields(objectIndices),
				accelerateByReadOnlyParams
		);
	}
	
}
