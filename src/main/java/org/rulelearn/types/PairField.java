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

import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.core.UncomparableException;

/**
 * Field composed of two simple fields of the same sub-type of {@link SimpleField}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 * 
 * @param <T> class of the first and second simple field in the pair
 */
public class PairField<T extends SimpleField> extends CompositeField {
	
	/**
	 * The first value in this pair.
	 */
	protected T firstValue;
	/**
	 * The second value in this pair.
	 */
	protected T secondValue;
	
	/**
	 * Constructor setting both values.
	 * 
	 * @param firstValue first value of this pair
	 * @param secondValue second value of this pair
	 * 
	 * @throws NullPointerException if any of the given values is {@code null}
	 * @throws InvalidTypeException if the first and the second value are of different types
	 */
	public PairField(T firstValue, T secondValue) {
		if (firstValue == null) {
			throw new NullPointerException("The first value in the pair is null.");
		}
		if (secondValue == null) {
			throw new NullPointerException("The second value in the pair is null.");
		}
		
		if (!firstValue.getClass().equals(secondValue.getClass())) {
			throw new InvalidTypeException("Types of fields in a pair have to be the same.");
		}
		
		this.firstValue = firstValue;
		this.secondValue = secondValue;
	}

	@Override
	public int compareToEx(Field otherField) throws UncomparableException {
		if (otherField instanceof PairField<?>) {
			int firstCompareExResult;
			int secondCompareExResult;
			
			try {
				firstCompareExResult = this.firstValue.compareToEx(((PairField<?>)otherField).firstValue);
				secondCompareExResult = this.secondValue.compareToEx(((PairField<?>)otherField).secondValue);
			} catch (UncomparableException exception) { //first or second values in pairs cannot be compared
				throw new UncomparableException("This pair field cannot be compared with the other pair field.");
			}
				
			if (firstCompareExResult == 0 && secondCompareExResult == 0) {
				return 0;
			} else if (firstCompareExResult >= 0 &&
					secondCompareExResult <= 0) {
				return 1;
			} else if (firstCompareExResult <= 0 &&
					secondCompareExResult >= 0) {
				return -1;
			} else {
				throw new UncomparableException("This pair field cannot be compared with the other pair field.");
			}
		} else {
			throw new ClassCastException("This pair field cannot be compared with the other field.");
		}
		
	}

	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
		if (otherField instanceof PairField<?>) {
			if (this.firstValue.isAtLeastAsGoodAs(((PairField<?>)otherField).firstValue) ==  TernaryLogicValue.TRUE &&
					this.secondValue.isAtMostAsGoodAs(((PairField<?>)otherField).secondValue) ==  TernaryLogicValue.TRUE) {
				return TernaryLogicValue.TRUE;
			} else {
				return TernaryLogicValue.FALSE;
			}
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
		if (otherField instanceof PairField<?>) {
			if (this.firstValue.isAtMostAsGoodAs(((PairField<?>)otherField).firstValue) ==  TernaryLogicValue.TRUE &&
					this.secondValue.isAtLeastAsGoodAs(((PairField<?>)otherField).secondValue) ==  TernaryLogicValue.TRUE) {
				return TernaryLogicValue.TRUE;
			} else {
				return TernaryLogicValue.FALSE;
			}
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue isEqualTo(Field otherField) {
		if (otherField instanceof PairField<?>) {
			if (this.firstValue.isEqualTo(((PairField<?>)otherField).firstValue) ==  TernaryLogicValue.TRUE &&
					this.secondValue.isEqualTo(((PairField<?>)otherField).secondValue) ==  TernaryLogicValue.TRUE) {
				return TernaryLogicValue.TRUE;
			} else {
				return TernaryLogicValue.FALSE;
			}
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
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
				PairField<?> otherField = ((PairField<?>)otherObject);
				return ((this.firstValue.equals(otherField.firstValue)) 
						&& this.secondValue.equals(otherField.secondValue));
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
		return Objects.hash(firstValue.getClass(), firstValue, secondValue);
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc} 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <S extends Field> S selfClone() {
		return (S)new PairField<T>(firstValue, secondValue);
	}

	/**
	 * Gets the first value in this pair.
	 * 
	 * @return the first value in this pair.
	 */
	public T getFirstValue() {
		return firstValue;
	}

	/**
	 * Gets the second value in this pair.
	 * 
	 * @return the second value in this pair.
	 */
	public T getSecondValue() {
		return secondValue;
	}

}
