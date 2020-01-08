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

package org.rulelearn.types;

import java.util.Objects;

import org.rulelearn.core.EvaluationFieldCalculator;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Field representing enumeration value.
 * Should be instantiated using {@link EnumerationFieldFactory#create(ElementList, int, org.rulelearn.data.AttributePreferenceType)}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public abstract class EnumerationField extends KnownSimpleField {
	/** 
	 * Default value for this type of a field.
	 */
	public final static int DEFAULT_VALUE = 0;
	
	/**
	 * List of elements representing enumeration.
	 */
	protected ElementList list;
	
	/**
	 * Position on element list which is equivalent to value of this field.
	 */
	protected int value = EnumerationField.DEFAULT_VALUE;
	
	/**
	 * Constructor preventing object creation.
	 */
	protected EnumerationField() {}
	
	/**
	 * Constructor setting value of this field.
	 * 
	 * @param list element list of the created field
	 * @param index position in the element list of enumeration which represents value of the field
	 * 
	 * @throws IndexOutOfBoundsException if index is incorrect
	 * @throws NullPointerException if given list is {@code null}
	 */
	protected EnumerationField(ElementList list, int index) throws IndexOutOfBoundsException {
		if (list != null) {
			this.list = list;
			if ((index >= 0) && (index < this.list.getSize())) {
				this.value = index;
			}
			else {
				throw new IndexOutOfBoundsException();
			}
		}
		else {
			throw new NullPointerException("List of enumeration values is null.");
		}
	}
	
	/**
	 * Compares this field with the other field. Takes into account only the value of this field,
	 * ignoring its preference type.
	 * 
	 * @param otherField other field that this field is going to be compared to
	 * 
	 * @return negative number when this field is smaller than the other field,<br>
	 *         zero if both fields are equal,<br>
	 *         positive number when this field is greater than the other field
	 * 
	 * @throws ClassCastException if the other simple field has a value (i.e., it is not an instance of {@link UnknownSimpleField}) and is not of type {@link EnumerationField}
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int compareTo(KnownSimpleField otherField) {
		EnumerationField other = (EnumerationField)otherField;
		if (this.value > other.value) {
			return 1;
		} else if (this.value < other.value) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Tells if this field object is equal to the other object.
	 * 
	 * @param otherObject other object that this object should be compared with
	 * @return {@code true} if this object is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this) {
			if (otherObject != null && this.getClass().equals(otherObject.getClass())) {
				final EnumerationField otherField = (EnumerationField) otherObject;
				return ((this.value == otherField.value) &&
						(this.getElementList().equals(otherField.getElementList())));
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	/**
     * Gets hash code of this field.
     *
     * @return hash code of this field
     */
	@Override
	public int hashCode () {
		return Objects.hash(this.getClass(), value, this.getElementList());
	}
	
	/**
	 * Checks whether element list of this field has equal hash to element list of the given other field.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return result of comparison; see {@link TernaryLogicValue}
	 */
	public TernaryLogicValue hasEqualHashOfElementList(EnumerationField otherField) {
		return list.hasEqualHash(otherField.getElementList());
	}
	
	/**
	 * Checks whether element list of this field is equal to element list of the given other field.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return result of comparison; see {@link TernaryLogicValue}
	 */
	public TernaryLogicValue hasEqualElementList(EnumerationField otherField) {
		return list.isEqualTo(otherField.getElementList());
	}

	/**
	 * Gets the element list of enumeration.
	 * 
	 * @return the element list {@link ElementList}
	 */
	public ElementList getElementList() {
		return list;
	}

	/**
	 * Gets the position (index) on the element list of enumeration which represents value of the field.
	 * 
	 * @return index of element on list
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Gets element.
	 * 
	 * @return {@link String} element 
	 */
	public String getElement() {
		return list.getElement(value);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public String toString() {
		return String.valueOf(this.list.getElement(this.value));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationField calculate(EvaluationFieldCalculator calculator, EvaluationField otherField) {
		return calculator.calculate(this, otherField);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EnumerationFieldFactory getDefaultFactory() {
		return EnumerationFieldFactory.getInstance();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EnumerationFieldCachingFactory getCachingFactory() {
		return EnumerationFieldCachingFactory.getInstance();
	}
	
	/**
	 * {@inheritDoc}
	 * Just returns the given parameter.
	 * 
	 * @param missingValueType {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	public UnknownSimpleField getUnknownEvaluation(UnknownSimpleField missingValueType) {
		return missingValueType;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException if given attribute's preference type is {@code null}
	 */
	@Override
	public EnumerationField clone(AttributePreferenceType attributePreferenceType) {
		return EnumerationFieldFactory.getInstance().create(list, value, attributePreferenceType);
	}
	
}
