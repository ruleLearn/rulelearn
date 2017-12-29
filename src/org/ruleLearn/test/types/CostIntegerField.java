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

/**
 * CostIntegerField
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class CostIntegerField extends IntegerField {
	public CostIntegerField(int value) {
		super(value);
	}
	
	@Override
	public FieldComparisonResult isAtLeastAsGoodAs(Field otherField) {
		try {
			return (this.value <= ((CostIntegerField)otherField).value ?
					FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
		}
		catch (ClassCastException exception) {
			return FieldComparisonResult.UNCOMPARABLE;
		}
	}


	@Override
	public FieldComparisonResult isAtMostAsGoodAs(Field otherField) {
		try {
			return (this.value >= ((CostIntegerField)otherField).value ?
					FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
		}
		catch (ClassCastException exception) {
			return FieldComparisonResult.UNCOMPARABLE;
		}
	}
}
