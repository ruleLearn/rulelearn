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

import org.rulelearn.core.TernaryLogicValue;

/**
 * Field of an information table used to store a text identifier assigned to an object.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class TextIdentificationField extends IdentificationField {
	
	/** 
	 * Default value for this type of a field.
	 */
	public final static String DEFAULT_VALUE = ""; //TODO: is this correct?
	
	/**
	 * Value of this field.
	 */
	protected String value;
	
	/**
	 * Constructor setting text value of this identification field.
	 * 
	 * @param value text value to set
	 */
	public TextIdentificationField(String value) {
		this.value = value;
	}

	@Override
	public TernaryLogicValue isEqualTo(Field otherField) {
		if (otherField instanceof TextIdentificationField) {
			return this.value.equals(((TextIdentificationField)otherField).value) ? TernaryLogicValue.TRUE : TernaryLogicValue.FALSE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this) {
			if (otherObject instanceof TextIdentificationField) {
				return this.value.equals(((TextIdentificationField)otherObject).value);
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public int hashCode () {
		return Objects.hash(this.getClass(), this.value);
	}

}
