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

import org.rulelearn.core.TernaryLogicValue;

/**
 * Field representing integer number value.
 * Should be instantiated using {@link IntegerFieldFactory#create(int, AttributePreferenceType)}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class IntegerField extends SimpleField {
	/**
	 * Value of this field.
	 */
	protected int value = 0;
	
	/**
	 * Constructor preventing object creation.
	 */
	protected IntegerField() {}
	
	/**
	 * Constructor setting value of this field.
	 * 
	 * @param value value of created field
	 */
	protected IntegerField(int value) {
		this.value = value;
	}
	
	/**
	 * Gets value of this field.
	 * 
	 * @return value of this field
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param otherField {@inheritDoc}
	 */
	@Override
	public TernaryLogicValue isDifferentThan(Field otherField) {
		switch (this.isEqualTo(otherField)) {
			case TRUE: return TernaryLogicValue.FALSE;
			case FALSE: return TernaryLogicValue.TRUE;
			case UNCOMPARABLE: return TernaryLogicValue.UNCOMPARABLE;
			default: return TernaryLogicValue.UNCOMPARABLE;
		}
	}
	
	/**
	 * Compares this field with the other field.
	 * 
	 * @param otherField other field to be compared with this field
	 * 
	 * @return negative number when this field is smaller than the other field,<br>
	 *         zero if both fields are equal,<br>
	 *         positive number when this field is greater than the other field
	 * 
	 * @throws ClassCastException if the other field is not of type {@link IntegerField}
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int compareTo(SimpleField otherField) {
		IntegerField other = (IntegerField)otherField;
		if (this.value > other.value) {
			return 1;
		} else if (this.value < other.value) {
			return -1;
		} else {
			return 0;
		}
	}
}
