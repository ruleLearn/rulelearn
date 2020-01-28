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

import org.rulelearn.data.FieldDistribution;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.SimpleField;
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
public class ModeCalculator extends MeanCalculator {
	
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
	 * Chooses more frequent evaluations of the two.
	 * If both evaluations have the same number of occurrences in the distribution, then returns the first evaluation.
	 * 
	 * @param firstField first field
	 * @param secondField second field
	 * 
	 * @return more frequent evaluation field of the two
	 * @throws ValueNotFoundException if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 */
	private EvaluationField chooseMoreFrequentEvaluationField(EvaluationField firstField, EvaluationField secondField) {
		int firstFieldCount = fieldDistribution.getCount(firstField);
		int secondFieldCount = fieldDistribution.getCount(secondField);
		
		if (firstFieldCount == 0 && secondFieldCount == 0) {
			throw new ValueNotFoundException("None of the evaluations for calculating modal value have been found in the field distribution.");
		}
		
		if (firstFieldCount > secondFieldCount) {
			return firstField;
		} else {
			if (firstFieldCount < secondFieldCount) {
				return secondField;
			} else { //firstFieldCount == secondFieldCount
				return firstField; //TODO: draw one of the two fields, make result deterministic using a seed
			}
		}
	}
	
	/**
	 * Calculates modal value of the given two {@link IntegerField integer fields} (in any order). Uses {@link #getFieldDistribution() field distribution} set in constructor. 
	 * If both evaluations have the same number of occurrences in the distribution, then returns the first evaluation.
	 * 
	 * @param firstField first field to make calculations
	 * @param secondEvaluationField second field to make calculations
	 * @return modal value of the arguments, or second field if it is an instance of {@link UnknownSimpleField},
	 *         or {@code null} if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 * 
	 * @throws ClassCastException if it is impossible to cast second field to {@link IntegerField}
	 * @throws NullPointerException if at least one of the given fields is {@code null}
	 * @throws ValueNotFoundException if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 */
	@Override
	public EvaluationField calculate(IntegerField firstField, EvaluationField secondEvaluationField) {
		EvaluationField mode;
		IntegerField secondField;
		
		if ((firstField == null) || (secondEvaluationField == null)) {
			throw new NullPointerException("At least one of integer fields for calculating modal value is null.");
		}
		else if (secondEvaluationField instanceof UnknownSimpleField) {
			mode = secondEvaluationField;
		}
		else {
			secondField = (IntegerField)secondEvaluationField;
			
			if ((firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE)) {
				mode = firstField;
			}
			else {
				try {
					mode = chooseMoreFrequentEvaluationField(firstField, secondField);
				} catch (ValueNotFoundException exception) {
					return null;
				}
			}
		}
		return mode;
	}
	
	/**
	 * Calculates modal value of the given two {@link RealField real fields} (in any order). Uses {@link #getFieldDistribution() field distribution} set in constructor. 
	 * If both evaluations have the same number of occurrences in the distribution, then returns the first evaluation.
	 * 
	 * @param firstField first field to make calculations
	 * @param secondEvaluationField second field to make calculations
	 * @return modal value of the arguments, or second field if it is an instance of {@link UnknownSimpleField},
	 *         or {@code null} if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 * 
	 * @throws ClassCastException if it is impossible to cast second field to {@link RealField}
	 * @throws NullPointerException if at least one of the given fields is {@code null}
	 * @throws ValueNotFoundException if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 */
	@Override
	public EvaluationField calculate(RealField firstField, EvaluationField secondEvaluationField) {
		EvaluationField mode;
		RealField secondField;
		
		if ((firstField == null) || (secondEvaluationField == null)) {
			throw new NullPointerException("At least one of real fields for calculating modal value is null.");
		}
		else if (secondEvaluationField instanceof UnknownSimpleField) {
			mode = secondEvaluationField;
		}
		else {
			secondField = (RealField)secondEvaluationField;
			
			if ((firstField.isEqualTo(secondField) == TernaryLogicValue.TRUE)) {
				mode = firstField;
			}
			else {
				try {
					mode = chooseMoreFrequentEvaluationField(firstField, secondField);
				} catch (ValueNotFoundException exception) {
					return null;
				}
			}
		}
		return mode;
	}

	/**
	 * Calculates modal value of the given two {@link EnumerationField enumeration fields} (in any order). Uses {@link #getFieldDistribution() field distribution} set in constructor. 
	 * If both evaluations have the same number of occurrences in the distribution, then returns the first evaluation.
	 * 
	 * @param firstField first field to make calculations
	 * @param secondEvaluationField second field to make calculations
	 * @return modal value of the arguments, or second field if it is an instance of {@link UnknownSimpleField},
	 *         or {@code null} if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 * 
	 * @throws ClassCastException if it is impossible to cast second field to {@link EnumerationField}
	 * @throws NullPointerException if at least one of the given fields is {@code null}
	 * @throws InvalidValueException if given fields have different {@link ElementList element lists}
	 * @throws ValueNotFoundException if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 */
	@Override
	public EvaluationField calculate(EnumerationField firstField, EvaluationField secondEvaluationField) {
		EvaluationField mode;
		EnumerationField secondField;
		
		if ((firstField == null) || (secondEvaluationField == null)) {
			throw new NullPointerException("At least one of enumeration fields for calculating modal value is null.");
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
				try {
					mode = chooseMoreFrequentEvaluationField(firstField, secondField);
				} catch (ValueNotFoundException exception) {
					return null;
				}
			} //else if
			else {
				throw new InvalidValueException("Enumeration fields have different element lists.");
			}
		}
		return mode;
	}
	
	/**
	 * Calculates modal value of the given two {@link PairField pair fields} (in any order). Uses {@link #getFieldDistribution() field distribution} set in constructor. 
	 * If both evaluations have the same number of occurrences in the distribution, then returns the first evaluation.
	 * 
	 * @param firstField first field to make calculations
	 * @param secondEvaluationField second field to make calculations
	 * @return modal value of the arguments,
	 *         or {@code null} if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 * 
	 * @throws InvalidTypeException if second field is an instance of {@link UnknownSimpleField}
	 * @throws ClassCastException if second field is not an instance of {@link PairField}
	 * @throws NullPointerException if at least one of the given fields is {@code null}
	 * @throws ValueNotFoundException if both given evaluations for calculating modal value have not been found in the {@link #getFieldDistribution() field distribution}
	 *         set in {@link #ModeCalculator(FieldDistribution) class constructor}
	 */
	@Override
	public EvaluationField calculate(PairField<? extends SimpleField> firstField, EvaluationField secondEvaluationField) {
		EvaluationField mode;
		
		if ((firstField == null) || (secondEvaluationField == null)) {
			throw new NullPointerException("At least one of pair fields for calculating modal value is null.");
		}
		else if (secondEvaluationField instanceof UnknownSimpleField) {
			//mode = secondEvaluationField;
			throw new InvalidTypeException("Pair field should not be compared in mode calculator with an unknown field.");
		}
		else {
			if (secondEvaluationField instanceof PairField<?>) {
				if ((firstField.isEqualTo(secondEvaluationField) == TernaryLogicValue.TRUE)) {
					mode = firstField;
				}
				else {
					try {
						mode = chooseMoreFrequentEvaluationField(firstField, secondEvaluationField);
					} catch (ValueNotFoundException exception) {
						return null;
					}
				}
			} else {
				throw new ClassCastException("Second evaluation field for calculating modal value is not a pair field.");
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
