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
import org.rulelearn.data.FieldDistribution;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.UnknownSimpleField;

/**
 * Modal value (the most frequent value, also called {@code mode}) calculator for different pairs of {@link EvaluationField evaluation fields} of the same type,
 * taken from the same column of an information table (for the same attribute).
 * Contributes to realization of visitor design pattern.<br>
 * <br>
 * In the current implementation, calculates mode only for {@link EnumerationField enumeration fields}, while for other types of fields calculates mean value,
 * just like {@link MeanCalculator}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ModeCalculator extends MeanCalculator { //TODO override more methods - calculate mode also for integer and real fields, etc.
	
	/**
	 * Distribution of {@link EvaluationField evaluation fields} in a subset of domain (value set) of considered evaluation attribute.
	 */
	FieldDistribution fieldDistribution;
	
	/**
	 * Constructs this calculator.
	 * 
	 * @param fieldDistribution distribution of {@link EvaluationField evaluation fields} in a subset of domain (value set) of considered evaluation attribute
	 * @throws NullPointerException if given field distribution is {@code null}
	 */
	public ModeCalculator(FieldDistribution fieldDistribution) {
		super();
		this.fieldDistribution = Precondition.notNull(fieldDistribution, "Field distribution for mode calculator is null.");
	}

	/**
	 * Calculates modal value of the given two {@link EnumerationField enumeration fields} (in any order). 
	 * The {@link AttributePreferenceType preference type} of returned field is the same as the preference type of the first field.
	 * 
	 * @param firstField first field to make calculations
	 * @param secondEvaluationField second field to make calculations
	 * @return modal value of the arguments or {@link UnknownSimpleField} if second field is unknown
	 * 
	 * @throws ClassCastException when it is impossible to cast second field to {@link EnumerationField}
	 * @throws NullPointerException when at least one of the given fields is {@code null}
	 * @throws InvalidValueException when given fields have different {@link ElementList element lists}
	 * @throws ValueNotFoundException if any of the enumeration fields for calculating median value is not found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 */
	@Override
	public EvaluationField calculate(EnumerationField firstField, EvaluationField secondEvaluationField) {
		EvaluationField mode;
		EnumerationField secondField;
		
		if ((firstField == null) || (secondEvaluationField == null)) {
			throw new NullPointerException("At least one of enumeration fields for calculating median value is null.");
		}
		else if (secondEvaluationField instanceof UnknownSimpleField) {
			mode = secondEvaluationField;
		}
		else {
			secondField = (EnumerationField)secondEvaluationField;
			
			if (firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE) {
				if (firstField.hasEqualHashOfElementList(secondField) == TernaryLogicValue.TRUE) {
					mode = firstField;
				}
				else {
					throw new InvalidValueException("Enumeration fields have different element lists.");
				}
			}
			else if (firstField.hasEqualHashOfElementList(secondField) == TernaryLogicValue.TRUE) {
				int firstFieldCount = fieldDistribution.getCount(firstField);
				int secondFieldCount = fieldDistribution.getCount(firstField);
				
				if (firstFieldCount > secondFieldCount) {
					return firstField;
				} else {
					if (firstFieldCount < secondFieldCount) {
						return secondField;
					} else { //firstFieldCount == secondFieldCount
						return firstField; //TODO: draw one of the two fields, make result deterministic using a seed
					}
				}
			} //else if
			else {
				throw new InvalidValueException("Enumeration fields have different element lists.");
			}
		}
		return mode;
	}

	/**
	 * Gets employed distribution of {@link EvaluationField evaluation fields} in a subset of domain (value set) of considered evaluation attribute.
	 *  
	 * @return the distribution of {@link EvaluationField evaluation fields} in a subset of domain (value set) of considered evaluation attribute
	 */
	public FieldDistribution getFieldDistribution() {
		return fieldDistribution;
	}
}
