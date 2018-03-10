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
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Configuration used to create information table for testing purposes.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableTestConfiguration {
	protected Attribute[] attributes;
	protected int activeDecisionAttributeIndex;
	protected String[][] fieldsAsText;
	
	/**
	 * Constructs this test configuration.
	 * 
	 * @param attributes attributes of an information table
	 * @param fieldsAsText fields of an information table, in text form
	 * 
	 * @throws InvalidValueException if more than one active decision attribute is detected
	 * @throws NullPointerException if attributes are {@code null}
	 */
	public InformationTableTestConfiguration(Attribute[] attributes, String[][] fieldsAsText) {
		this.attributes = attributes.clone();
		
		//calculate index of the active decision attribute, and verify there is only one such attribute
		this.activeDecisionAttributeIndex = -1;
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] instanceof EvaluationAttribute) {
				if (attributes[i].isActive() && ((EvaluationAttribute)attributes[i]).getType() == AttributeType.DECISION) {
					if (this.activeDecisionAttributeIndex >= 0) {
						throw new InvalidValueException("More than 1 active decision attribute detected.");
					} else {
						this.activeDecisionAttributeIndex = i;
					}
				}
			}
		}
		
		this.fieldsAsText = fieldsAsText;
	}
	
	/**
	 * Gets the attributes. Each time a new array with clones of the attributes stored in this configuration is returned.
	 * 
	 * @return the attributes of this configuration (new array with clones of the attributes stored in this configuration)
	 * @throws InvalidTypeException if some of the attributes in neither {@link EvaluationAttribute} nor {@link IdentificationAttribute}
	 */
	public Attribute[] getAttributes() {
		Attribute[] freshAttributes = new Attribute[this.attributes.length];
		EvaluationAttribute evalAttribute;
		IdentificationAttribute idAttribute;
		
		//clone each attribute into the newly allocated array
		for (int i = 0; i < this.attributes.length; i++) {
			if (this.attributes[i] instanceof EvaluationAttribute) {
				evalAttribute = (EvaluationAttribute)this.attributes[i];
				freshAttributes[i] = new EvaluationAttribute (
						evalAttribute.getName(), evalAttribute.isActive(),
						evalAttribute.getType(), evalAttribute.getValueType(), evalAttribute.getMissingValueType(), evalAttribute.getPreferenceType()
				);
			} else if (this.attributes[i] instanceof IdentificationAttribute) {
				idAttribute = (IdentificationAttribute)this.attributes[i];
				freshAttributes[i] = new IdentificationAttribute (
						idAttribute.getName(), idAttribute.isActive(),
						idAttribute.getValueType()
				);
			} else {
				throw new InvalidTypeException("Unsupported attribute type.");
			}
		}
		
		return freshAttributes;
	}
	
	/**
	 * Gets a list of all rows, each corresponding to fields of a single object.
	 * Each time a new list composed of new arrays composed of new fields is returned.
	 * Wraps {@link #getListOfFields(int[])} by first calculating array with ordered indices of all objects (rows),
	 * and then invoking wrapped method using this array as an input parameter.
	 * 
	 * @return see {@link #getListOfFields(int[])}
	 * 
	 * @throws InvalidTypeException see {@link #getListOfFields(int[])}
	 * @throws InvalidTypeException see {@link #getListOfFields(int[])}
	 * @throws InvalidTypeException see {@link #getListOfFields(int[])}
	 */
	public List<Field[]> getListOfFields() {
		int[] objectIndices = new int[this.getNumberOfObjects()];
		
		for (int i = 0; i < objectIndices.length; i++) {
			objectIndices[i] = i; //indicate that all objects (rows) should be selected to the constructed information table
		}
		
		return this.getListOfFields(objectIndices);
	}
	
	/**
	 * Gets a list of rows, each corresponding to fields of a single object.
	 * Each time a new list composed of new arrays composed of new fields is returned.
	 * 
	 * @param objectIndices list of indices of selected rows (only fields from these rows should be returned)
	 * @return the list of rows, each corresponding to fields of a single object
	 * 
	 * @throws InvalidTypeException if unsupported sub-type of {@link Attribute} is encountered
	 * @throws InvalidTypeException if unsupported sub-type of {@link EvaluationField} is encountered
	 * @throws InvalidTypeException if unsupported sub-type of {@link IdentificationField} is encountered
	 * 
	 * @throws NullPointerException if objects' indices are {@code null}
	 */
	public List<Field[]> getListOfFields(int[] objectIndices) {
		if (objectIndices == null) {
			throw new NullPointerException("Objects' indices are null.");
		}
		
		List<Field[]> listOfFields = new ObjectArrayList<Field[]>();
		Field[] selectedRow;
		EvaluationAttribute evalAttribute;
		EvaluationField valueType;
		IdentificationAttribute idAttribute;
		IdentificationField idType;
		
		for (int i = 0; i < objectIndices.length; i++) {
			selectedRow = new Field[this.fieldsAsText[objectIndices[i]].length];
			
			for (int j = 0; j < this.fieldsAsText[objectIndices[i]].length; j++) {
				if (this.attributes[j] instanceof EvaluationAttribute) {
					evalAttribute = (EvaluationAttribute)this.attributes[j];
					valueType = evalAttribute.getValueType();
					
					if (valueType instanceof IntegerField) {
						selectedRow[j] = IntegerFieldFactory.getInstance().create(Integer.parseInt(this.fieldsAsText[objectIndices[i]][j]), evalAttribute.getPreferenceType());
					} else if (valueType instanceof RealField) {
						selectedRow[j] = RealFieldFactory.getInstance().create(Double.parseDouble(this.fieldsAsText[objectIndices[i]][j]), evalAttribute.getPreferenceType());
					} else if (valueType instanceof EnumerationField) {
						selectedRow[j] = EnumerationFieldFactory.getInstance().create(((EnumerationField)valueType).getElementList(), Integer.parseInt(this.fieldsAsText[objectIndices[i]][j]), evalAttribute.getPreferenceType());
					} else {
						throw new InvalidTypeException("Encountered unsupported type of evaluation field.");
					}
				} else if (this.attributes[j] instanceof IdentificationAttribute) {
					idAttribute = (IdentificationAttribute)this.attributes[j];
					idType = idAttribute.getValueType();
					
					if (idType instanceof TextIdentificationField) {
						selectedRow[j] = new TextIdentificationField(this.fieldsAsText[objectIndices[i]][j]);
					} else if (idType instanceof UUIDIdentificationField) {
						selectedRow[j] = new UUIDIdentificationField(UUID.fromString(this.fieldsAsText[objectIndices[i]][j]));
					} else {
						throw new InvalidTypeException("Encountered unsupported type of identification field.");
					}
				} else {
					throw new InvalidTypeException("Encountered unsupported type of attribute.");
				}
			}
			
			listOfFields.add(selectedRow);
		}
		
		return listOfFields;
	}
	
	/**
	 * Gets decisions (active decision attribute values) of objects (rows) with given indices.
	 * Wraps {@link #getDecisions(int[])} by first calculating array with ordered indices of all objects (rows),
	 * and then invoking wrapped method using this array as an input parameter.
	 * 
	 * @return see {@link #getDecisions(int[])}
	 * @throws InvalidTypeException see {@link #getDecisions(int[])}
	 */
	public EvaluationField[] getDecisions() {
		int[] objectIndices = new int[this.getNumberOfObjects()];
		for (int i = 0; i < objectIndices.length; i++) {
			objectIndices[i] = i; //indicate that decisions for all objects (rows) should be returned
		}
		
		return this.getDecisions(objectIndices);
	}
	
	/**
	 * Gets decisions (active decision attribute values) of objects (rows) with given indices.
	 * 
	 * @param objectIndices list of indices of selected rows (only decisions concerning these objects (rows) should be returned)
	 * @return decisions concerning objects (rows) with given indices
	 * 
	 * @throws NullPointerException if objects' indices are {@code null}
	 * @throws InvalidTypeException if unsupported sub-type of {@link EvaluationField} is encountered in the decision column
	 */
	public EvaluationField[] getDecisions(int[] objectIndices) {
		if (objectIndices == null) {
			throw new NullPointerException("Objects' indices are null.");
		}
		
		EvaluationField[] decisions = new EvaluationField[objectIndices.length];
		
		EvaluationAttribute evalAttribute = (EvaluationAttribute)this.attributes[this.activeDecisionAttributeIndex];
		EvaluationField valueType = evalAttribute.getValueType();
		
		for (int i = 0; i < objectIndices.length; i++) {
			if (valueType instanceof IntegerField) {
				decisions[i] = IntegerFieldFactory.getInstance().create(
						Integer.parseInt(this.fieldsAsText[objectIndices[i]][this.activeDecisionAttributeIndex]), evalAttribute.getPreferenceType());
			} else if (valueType instanceof RealField) {
				decisions[i] = RealFieldFactory.getInstance().create(
						Double.parseDouble(this.fieldsAsText[objectIndices[i]][this.activeDecisionAttributeIndex]), evalAttribute.getPreferenceType());
			} else if (valueType instanceof EnumerationField) {
				decisions[i] = EnumerationFieldFactory.getInstance().create(
						((EnumerationField)valueType).getElementList(),
						Integer.parseInt(this.fieldsAsText[objectIndices[i]][this.activeDecisionAttributeIndex]),
						evalAttribute.getPreferenceType());
			} else {
				throw new InvalidTypeException("Encountered unsupported type of evaluation field.");
			}
		}
		
		return decisions;
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
		return this.fieldsAsText.length;
	}
	
	/**
	 * Gets index of the only active decision attribute.
	 * 
	 * @return index of the only active decision attribute
	 */
	public int getActiveDecisionAttributeIndex() {
		return this.activeDecisionAttributeIndex;
	}
	
	/**
	 * Gets information table constructed from this configuration.
	 * Wraps {@link #getInformationTable(int[], boolean)} by first calculating array with ordered indices of all objects (rows),
	 * and then invoking wrapped method using this array as an input parameter.
	 * 
	 * @param accelerateByReadOnlyParams parameter passed to {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @return information table constructed from this configuration.
	 */
	public InformationTable getInformationTable(boolean accelerateByReadOnlyParams) {
		int[] objectIndices = new int[this.getNumberOfObjects()];
		for (int i = 0; i < objectIndices.length; i++) {
			objectIndices[i] = i; //indicate that all objects (rows) should be selected to the constructed information table
		}
		
		return this.getInformationTable(objectIndices, accelerateByReadOnlyParams);
	}
	
	/**
	 * Gets information table constructed from this configuration.
	 * 
	 * @param objectIndices indices of objects selected to the constructed information table
	 *        if {@code null} then all objects are selected (i.e., no object is skipped)
	 * @param accelerateByReadOnlyParams parameter passed to {@link InformationTable#InformationTable(Attribute[], List, boolean)}
	 * @return information table constructed from this configuration.
	 * @throws NullPointerException if objects' indices are {@code null}
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
