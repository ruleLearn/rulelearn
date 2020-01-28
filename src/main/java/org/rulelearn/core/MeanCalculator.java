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

package org.rulelearn.core;

import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.UnknownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Central tendency (mean) calculator for different {@link EvaluationField evaluation fields} of the same type.
 * Contributes to realization of visitor design pattern.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class MeanCalculator implements EvaluationFieldCalculator {

	/**
	 * Calculates mean for {@link IntegerField integer fields}. 
	 * The {@link AttributePreferenceType preference type} of returned field is the same as the preference type of the first field.
	 * 
	 * @param firstField first field to make calculations 
	 * @param secondField second field to make calculations
	 * @return mean of arguments, or second field if it is an instance of {@link UnknownSimpleField}
	 * @throws ClassCastException if it is impossible to cast second field to {@link IntegerField}
	 * @throws NullPointerException if at least one of fields is {@code null}
	 */
	@Override
	public EvaluationField calculate(IntegerField firstField, EvaluationField secondField) {
		EvaluationField mean = null;
		if ((firstField == null) || (secondField == null)) {
			throw new NullPointerException("At least one of fields is null.");
		}
		else if (secondField instanceof UnknownSimpleField) {
			mean = secondField;
		}
		else {
			if ((firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE)) {
				mean = firstField;
			}
			else {
				mean = IntegerFieldFactory.getInstance().create((firstField.getValue() + ((IntegerField)secondField).getValue())/2, firstField.getPreferenceType());
			}
		}
		return mean;
	}

	/**
	 * Calculates mean for {@link RealField real fields}.
	 * The {@link AttributePreferenceType preference type} of returned field is the same as the preference type of the first field.
	 * 
	 * @param firstField first field to make calculations 
	 * @param secondField second field to make calculations
	 * @return mean of arguments, or second field if it is an instance of {@link UnknownSimpleField}
	 * @throws ClassCastException if it is impossible to cast second field to {@link RealField}
	 * @throws NullPointerException if at least one of fields is {@code null}
	 */
	@Override
	public EvaluationField calculate(RealField firstField, EvaluationField secondField) {
		EvaluationField mean = null;
		if ((firstField == null) || (secondField == null)) {
			throw new NullPointerException("At least one of fields is null.");
		}
		else if (secondField instanceof UnknownSimpleField) {
			mean = secondField;
		}
		else {
			if ((firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE)) {
				mean = firstField;
			}
			else {
				mean = RealFieldFactory.getInstance().create((firstField.getValue() + ((RealField)secondField).getValue())/2, firstField.getPreferenceType());
			}
		}
		return mean;
	}

	/**
	 * Calculates mean for {@link EnumerationField enumeration fields}. 
	 * The {@link AttributePreferenceType preference type} of returned field is the same as the preference type of the first field.
	 * 
	 * @param firstField first field to make calculations 
	 * @param secondField second field to make calculations
	 * @return mean of arguments, or second field if it is an instance of {@link UnknownSimpleField}
	 * @throws ClassCastException if it is impossible to cast second field to {@link EnumerationField}
	 * @throws NullPointerException if at least one of fields is {@code null}
	 * @throws InvalidValueException if fields have different element lists {@link ElementList}
	 */
	@Override
	public EvaluationField calculate(EnumerationField firstField, EvaluationField secondField) {
		EvaluationField mean = null;
		if ((firstField == null) || (secondField == null)) {
			throw new NullPointerException("At least one of fields is null.");
		}
		else if (secondField instanceof UnknownSimpleField) {
			mean = secondField;
		}
		else {
			if (firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE) {
				if (firstField.hasEqualHashOfElementList((EnumerationField)secondField) == TernaryLogicValue.TRUE) {
					mean = firstField;
				}
				else {
					throw new InvalidValueException("Fields have different element lists.");
				}
			}
			else if (firstField.hasEqualHashOfElementList((EnumerationField)secondField) == TernaryLogicValue.TRUE) {
				//calculate mean value
				mean = EnumerationFieldFactory.getInstance().create(firstField.getElementList(),
						(firstField.getValue() + ((EnumerationField)secondField).getValue())/2,
						firstField.getPreferenceType());
			}
			else {
				throw new InvalidValueException("Fields have different element lists.");
			}
		}
		return mean;
	}
	
	/**
	 * Calculates mean for {@link PairField pair fields}. 
	 * The {@link AttributePreferenceType preference type} of returned field is the same as the preference type of the first field.
	 * Currently throws {@link UnsupportedOperationException}.
	 * 
	 * @param firstField first field to make calculations 
	 * @param secondField second field to make calculations
	 * @return mean of arguments, or second field if it is an instance of {@link UnknownSimpleField}
	 * @throws ClassCastException if it is impossible to cast second field to {@link PairField}
	 */
	@Override
	public EvaluationField calculate(PairField<? extends SimpleField> firstField, EvaluationField secondField) {
		EvaluationField mean = null;
		if ((firstField == null) || (secondField == null)) {
			throw new NullPointerException("At least one of fields is null.");
		}
		else if (secondField instanceof UnknownSimpleField) {
			mean = secondField;
		} else {
			// TODO implementation
			throw new UnsupportedOperationException("Not implemented yet!");
		}
		
		return mean;
	}

	/**
	 * Calculates mean as equal to the first field passed as parameter. 
	 * 
	 * @param firstField first field to make calculations 
	 * @param secondField second field to make calculations
	 * @return first field
	 */
	@Override
	public EvaluationField calculate(UnknownSimpleFieldMV15 firstField, EvaluationField secondField) {
		return firstField;
	}

	/**
	 * Calculates mean as equal to the first field passed as parameter.
	 * 
	 * @param firstField first field to make calculations 
	 * @param secondField second field to make calculations
	 * @return first field
	 */
	@Override
	public EvaluationField calculate(UnknownSimpleFieldMV2 firstField, EvaluationField secondField) {
		return firstField;
	}

}
