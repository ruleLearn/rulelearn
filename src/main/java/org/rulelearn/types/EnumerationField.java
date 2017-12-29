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

/**
 * EnumerationField
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public abstract class EnumerationField extends SimpleField {
	/**
	 * Set of elements to which the value of this field, which is represented by index, belongs to.
	 */
	private ElementSet set;
	
	/**
	 * Position of this element in the element set.
	 */
	private int index = 0;
	
	/**
	 * Constructor preventing object creation.
	 */
	protected EnumerationField() {}
	
	/**
	 * Constructor setting value of this field.
	 * 
	 * @param set element set of the created field
	 * @param index position in the element set of enumeration which represents value of the field
	 */
	protected EnumerationField(ElementSet set, int index) {
		this.set = set;
		this.index = index;
	}
	
	@Override
	public FieldComparisonResult isDifferentThan(Field otherField) {
		switch (this.isEqualTo(otherField)) {
			case TRUE: return FieldComparisonResult.FALSE;
			case FALSE: return FieldComparisonResult.TRUE;
			case UNCOMPARABLE: return FieldComparisonResult.UNCOMPARABLE;
			default: return FieldComparisonResult.UNCOMPARABLE;
		}
	}
	
	public FieldComparisonResult hasEqualElementSet(EnumerationField otherField) {
		return set.isEqualTo(otherField.getElementSet());
	}

	/**
	 * Get the element set of enumeration
	 * 
	 * @return the element set
	 */
	public ElementSet getElementSet() {
		return set;
	}

	/**
	 * Get the position (index) in the element set of enumeration which represents value of the field
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Get element name.
	 * 
	 * @return string that is the name of this element 
	 */
	public String getElementName() {
		return set.getElementName(index);
	}

	/**
	 * Set the element set of enumeration 
	 * 
	 * @param set to be set
	 *
	 * TODO setters
	 *
	public void setElementSet(ElementSet set) {
		this.set = set;
		// TODO check if index is correct
		if (index > set.)
	}*/
	
	/**
	 * Set the position in the element set of enumeration which represents value of the field
	 * 
	 * @param index the index to set
	 *
	 * TODO setters
	 *
	public void setIndex(int index) {
		// TODO check if index is correct
		this.index = index;
	}*/
}
