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
import org.rulelearn.data.AttributePreferenceType;

/**
 * Field representing a real number value.
 * Should be instantiated using {@link RealFieldFactory#create(double, org.rulelearn.data.AttributePreferenceType)}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public abstract class RealField extends KnownSimpleField {
	/** 
	 * Default value for this type of a field.
	 */
	public final static double DEFAULT_VALUE = 0.0;
	
	/**
	 * Value of this field.
	 */
	protected double value = RealField.DEFAULT_VALUE;
	
	/**
	 * Constructor preventing object creation.
	 */
	protected RealField() {}
	
	/**
	 * Constructor setting value of this field.
	 * 
	 * @param value value of created field
	 */
	protected RealField(double value) {
		this.value = value;
	}
	
	/**
	 * Gets value of this field.
	 * 
	 * @return value of this field
	 */
	public double getValue() {
		return this.value;
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
	 * @throws ClassCastException if the other simple field has a value (i.e., it is not an instance of {@link UnknownSimpleField}) and is not of type {@link RealField}
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int compareTo(KnownSimpleField otherField) {
		RealField other = (RealField)otherField;
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
				final RealField other = (RealField) otherObject;
				return (this.value == other.value);
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
		return Objects.hash(this.getClass(), value);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public String toString() {
		return String.valueOf(this.value);
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
	public RealFieldFactory getDefaultFactory() {
		return RealFieldFactory.getInstance();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public RealFieldCachingFactory getCachingFactory() {
		return RealFieldCachingFactory.getInstance();
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
	public RealField clone(AttributePreferenceType attributePreferenceType) {
		return RealFieldFactory.getInstance().create(value, attributePreferenceType);
	}
	
}
