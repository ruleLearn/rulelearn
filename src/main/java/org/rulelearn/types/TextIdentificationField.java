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
import java.util.Random;

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
	public static final String DEFAULT_VALUE = "rLId-0000";
	
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
	
	/**
	 * Gets the value of this field.
	 * 
	 * @return the value of this field
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Gets random text identifier (name).
	 * 
	 * @param idLength requested length of the generated text identifier
	 * @return random name having the requested length
	 */
	public static String getRandomValue(int idLength) {
		Random r = new Random();
		char[] idChars = new char[idLength];
	    String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
	    
	    for (int i = 0; i < idLength; i++) {
	    	idChars[i] = alphabet.charAt(r.nextInt(alphabet.length()));
	    } // takes idLength random characters from alphabet
	    
	    StringBuilder sB = new StringBuilder();
	    
	    return sB.append("rLId-").append(idChars).toString();
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

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Field> S selfClone() {
		return (S)new TextIdentificationField(this.value);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.value;
	}
	
	/**
	 * Gets text representation of type of this field (i.e., {@link TextIdentificationField}).
	 * 
	 * @return text representation of type of this field
	 */
	@Override
	public String getTypeDescriptor() {
		return "textId";
	}

}
