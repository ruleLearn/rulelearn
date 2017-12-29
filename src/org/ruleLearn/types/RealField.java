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

package org.ruleLearn.types;

/**
 * Field representing a real number value.
 * Should be instantiated using {@link ReakFieldFactory#create(int, AttributePreferenceType)}.
 *
 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
 * @author Marcin Szeląg <marcin.szelag@cs.put.poznan.pl>
 *
 */
public abstract class RealField extends SimpleField {

	/**
	 * Value of this field.
	 */
	protected double value = 0;
	
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

	@Override
	public FieldComparisonResult isDifferentThan(Field otherField) {
		switch (this.isEqualTo(otherField)) {
			case TRUE: return FieldComparisonResult.FALSE;
			case FALSE: return FieldComparisonResult.TRUE;
			case UNCOMPARABLE: return FieldComparisonResult.UNCOMPARABLE;
			default: return FieldComparisonResult.UNCOMPARABLE;
		}
	}
}
