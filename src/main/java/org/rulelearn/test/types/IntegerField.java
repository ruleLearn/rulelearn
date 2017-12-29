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

package org.rulelearn.test.types;

import org.rulelearn.types.Field;
import org.rulelearn.types.TernaryLogicValue;
import org.rulelearn.types.SimpleField;

/**
 * IntegerField
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
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
	 * @return see {@link TernaryLogicValue}
	 */
	public TernaryLogicValue isEqualTo(Field otherField) {
		try {
			return (this.value == ((IntegerField)otherField).value ? 
					TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
		} catch (ClassCastException exception) {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}
	
	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
		return this.isEqualTo(otherField);
	}

	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
		return this.isEqualTo(otherField);
	}

	@Override
	public TernaryLogicValue isDifferentThan(Field otherField) {
		switch (this.isEqualTo(otherField)) {
			case TRUE: return TernaryLogicValue.FALSE;
			case FALSE: return TernaryLogicValue.TRUE;
			case UNCOMPARABLE: return TernaryLogicValue.UNCOMPARABLE;
			default: return TernaryLogicValue.UNCOMPARABLE;
		}
	}

}
