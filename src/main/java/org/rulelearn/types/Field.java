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

import org.rulelearn.core.SelfCloneable;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Top level class for all fields (values) in an information table.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Field implements SelfCloneable<Field> {
	
	/**
	 * Tells if this field is equal to the given field (has the same or equivalent value). Both this field and the other field can represent a missing value.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this field is equal to the other field,<br>
	 *         {@link TernaryLogicValue#FALSE} if this field is not equal to the other field,<br>
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other field prevents comparison
	 *         of this field and the other field.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	abstract public TernaryLogicValue isEqualTo(Field otherField);
	
	/**
	 * Tells if this field is not equal to the given field (has different value). Both this field and the other field can represent a missing value.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this field is not equal to the other field,<br>
	 *         {@link TernaryLogicValue#FALSE} if this field is equal to the other field,<br>
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other field prevents comparison
	 *         of this field and the other field.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	public TernaryLogicValue isDifferentThan(Field otherField) {
		//negate and return the result of isEqualTo method
		switch (this.isEqualTo(otherField)) {
			case TRUE: return TernaryLogicValue.FALSE;
			case FALSE: return TernaryLogicValue.TRUE;
			case UNCOMPARABLE: return TernaryLogicValue.UNCOMPARABLE;
			default: return TernaryLogicValue.UNCOMPARABLE;
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
	public abstract boolean equals(Object otherObject);
	
	/**
     * Gets hash code of this field.
     *
     * @return hash code of this field
     */
	@Override
    public abstract int hashCode(); 
	
	/**
	 * Gets text representation of this field.<br>
	 * <br>
	 * This text representation is used in {@link org.rulelearn.data.InformationTable#serialize(boolean)} method, which, in turn, is used in
	 * {@link org.rulelearn.data.InformationTable#getHash()} method to produce hash of information table consistent among multiple program runs.<br>
	 * <br>
	 * Proceed with caution! Changing implementation of this method in any subclass may invalidate already calculated hashes, being stored in unit tests and RuleML files.
	 * 
	 * @return text representation of this field
	 * 
	 * @see org.rulelearn.data.InformationTable#serialize(boolean)
	 * @see org.rulelearn.data.InformationTable#getHash()
	 */
	public abstract String toString();
	
	/**
	 * Gets plain text representation of type (and possibly domain) of this field.
	 * Resulting text should take into account both semantics of the actual Java type of the field and content of its domain, if domain values are enumerated.
	 * This method is useful, e.g., for comparing two {@link org.rulelearn.data.Attribute#getValueType() value types of an attribute}.
	 * Then, by comparing type descriptors of the two value types, it is possible to decide whether these value types are the same or different.<br>
	 * <br>
	 * IMPLEMENTATION NOTE: All implementing methods have to return a different value!
	 * 
	 * @return plain text representation of type (and possibly domain) of this field
	 */
	public abstract String getTypeDescriptor();
	
}
