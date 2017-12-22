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

package org.ruleLearn.test.types;

import org.ruleLearn.types.Field;
import org.ruleLearn.types.FieldComparisonResult;
import org.ruleLearn.types.SimpleField;

/**
 * IntegerField
 *
 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
 * @author Marcin Szeląg <marcin.szelag@cs.put.poznan.pl>
 *
 */
public class IntegerField extends SimpleField {
	
	/**
	 * Value of this field.
	 */
	protected int value = 0;
	
	/**
	 * Object creation preventing constructor.
	 */
	public IntegerField() {}
	
	public IntegerField(int value) {
		this.value = value;
	}

	/**
	 * Tells if this field is equal to the given field
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return see {@Link FieldComparisonResult}
	 */
	protected FieldComparisonResult isEqualTo(Field otherField) {
		try {
			return (this.value == ((IntegerField)otherField).value ? 
					FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
		} catch (ClassCastException exception) {
			return FieldComparisonResult.UNCOMPARABLE;
		}
	}
	
	@Override
	public FieldComparisonResult isAtLeastAsGoodAs(Field otherField) {
		return this.isEqualTo(otherField);
	}

	@Override
	public FieldComparisonResult isAtMostAsGoodAs(Field otherField) {
		return this.isEqualTo(otherField);
	}

}
