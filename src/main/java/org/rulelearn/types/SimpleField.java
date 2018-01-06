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

import org.rulelearn.core.ReverseComparable;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Top level class for all simple fields (i.e., representing simple values) in an information table.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class SimpleField extends Field implements Comparable<SimpleField>, ReverseComparable<SimpleField> {
	
	/**
	 * Compares this field with the other field. Note that this implementing method will not throw {@link org.rulelearn.core.UncomparableException}
	 * as every two simple fields of the same type are comparable.
	 * 
	 * @param otherField other field to be compared with this field
	 * 
	 * @return negative number when this field is smaller than the other field,<br>
	 *         zero if both fields are equal,<br>
	 *         positive number when this field is greater than the other field
	 * 
	 * @throws ClassCastException if the other field is not of type {@link SimpleField}
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int compareToEx(Field otherField) {
		return compareTo((SimpleField)otherField);
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
	 * Tells if this field has a value (the value is known, not missing).
	 * 
	 * @return {@code true} if this field has some value, {@code false} if value of this field is unknown (missing)
	 */
	abstract public boolean hasValue();

}
