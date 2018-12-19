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
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.UnknownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Central tendency (mean) calculator for different field {@link SimpleField} types.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class MeanCalculator implements SimpleFieldCalculator {

	/**
	 * Calculates mean for integer fields {@link IntegerField}. Returns {@code null} when it is impossible to calculate mean value.
	 * It is impossible to calculate mean value of two null values and values of fields which have different preference type {@link AttributePreferenceType}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return mean of arguments or {@code null} if mean is not possible to calculate
	 * @throws ClassCastException when it is impossible to cast secondField to {@link IntegerField}
	 */
	@Override
	public SimpleField calculate(IntegerField firstField, SimpleField secondField) {
		IntegerField mean = null;
		if ((firstField == null)) {
			if (secondField != null) {
				mean = (IntegerField)secondField;
			}
		}
		else if ((secondField == null) || (secondField instanceof UnknownSimpleField)) {
			mean = firstField;
		}
		else {
			if ((firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE)) {
				mean = firstField;
			}
			else if (firstField.getPreferenceType() == ((IntegerField)secondField).getPreferenceType()) {
				mean = IntegerFieldFactory.getInstance().create((firstField.getValue() + ((IntegerField)secondField).getValue())/2, firstField.getPreferenceType());
			}
		}
		return mean;
	}

	/**
	 * Calculates mean for real fields {@link RealField}. Returns {@code null} when it is impossible to calculate mean value.
	 * It is impossible to calculate mean value of two null values and values of fields which have different preference type {@link AttributePreferenceType}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return mean of arguments or {@code null} if mean is not possible to calculate
	 * @throws ClassCastException when it is impossible to cast secondField to {@link RealField}
	 */
	@Override
	public SimpleField calculate(RealField firstField, SimpleField secondField) {
		RealField mean = null;
		if ((firstField == null)) {
			if (secondField != null) {
				mean = (RealField)secondField;
			}
		}
		else if ((secondField == null) || (secondField instanceof UnknownSimpleField)) {
			mean = firstField;
		}
		else {
			if ((firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE)) {
				mean = firstField;
			}
			else if (firstField.getPreferenceType() == ((RealField)secondField).getPreferenceType()) {
				mean = RealFieldFactory.getInstance().create((firstField.getValue() + ((RealField)secondField).getValue())/2, firstField.getPreferenceType());
			}
		}
		return mean;
	}

	/**
	 * Calculates mean for enumeration fields {@link EnumerationField}. Returns {@code null} when it is impossible to calculate mean value. 
	 * It is impossible to calculate mean value of two null values and values of fields which have different preference type {@link AttributePreferenceType} or 
	 * different domains {@link ElementList}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return mean of arguments or {@code null} if mean is not possible to calculate
	 * @throws ClassCastException when it is impossible to cast secondField to {@link RealField}
	 */
	@Override
	public SimpleField calculate(EnumerationField firstField, SimpleField secondField) {
		EnumerationField mean = null;
		if ((firstField == null)) {
			if (secondField != null) {
				mean = (EnumerationField)secondField;
			}
		}
		else if ((secondField == null) || (secondField instanceof UnknownSimpleField)) {
			mean = firstField;
		}
		else {
			if ((firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE) &&
				(firstField.hasEqualHashOfElementList((EnumerationField)secondField) == TernaryLogicValue.TRUE)) {
				mean = firstField;
			}
			else if ((firstField.getPreferenceType() == ((EnumerationField)secondField).getPreferenceType()) && 
					 (firstField.hasEqualHashOfElementList((EnumerationField)secondField) == TernaryLogicValue.TRUE)) {
				mean = EnumerationFieldFactory.getInstance().create(firstField.getElementList(), (firstField.getValue() + ((EnumerationField)secondField).getValue())/2, firstField.getPreferenceType());
			}
		}
		return mean;
	}

	/**
	 * Calculates mean for unknown fields representing missing attribute values handled according to approach denoted as mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * Returns {@code null} when it is impossible to calculate mean value.	
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return mean of arguments or {@code null} if mean is not possible to calculate
	 */
	@Override
	public SimpleField calculate(UnknownSimpleFieldMV15 firstField, SimpleField secondField) {
		SimpleField mean = null;
		if ((firstField == null)) {
			if (secondField != null) {
				mean = secondField;
			}
		}
		else if ((secondField == null) || (secondField instanceof UnknownSimpleField)) {
			mean = firstField;
		}
		else {
			mean = secondField;
		}
		return mean;
	}

	/**
	 * Calculates mean for unknown fields representing missing attribute values handled according to approach denoted as mv_{2} {@link UnknownSimpleFieldMV2}.
	 * Returns {@code null} when it is impossible to calculate mean value.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return mean of arguments or {@code null} if mean is not possible to calculate
	 */
	@Override
	public SimpleField calculate(UnknownSimpleFieldMV2 firstField, SimpleField secondField) {
		SimpleField mean = null;
		if ((firstField == null)) {
			if (secondField != null) {
				mean = secondField;
			}
		}
		else if ((secondField == null) || (secondField instanceof UnknownSimpleField)) {
			mean = firstField;
		}
		else {
			mean = secondField;
		}
		return mean;
	}

}
