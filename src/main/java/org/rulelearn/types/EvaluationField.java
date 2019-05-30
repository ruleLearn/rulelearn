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

import org.rulelearn.core.ComparableExt;
import org.rulelearn.core.EvaluationFieldCalculator;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.EvaluationAttribute;

/**
 * Field of an information table used to store an evaluation assigned to an object by the so-called information function.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class EvaluationField extends Field implements ComparableExt<EvaluationField> {
	
	/**
	 * Tells if this field is at least as good as the given field. Both this field and the other field can represent a missing value.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this field is at least as good as the other field,<br>
	 *         {@link TernaryLogicValue#FALSE} if this field is not at least as good as the other field,<br>
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other field prevents comparison
	 *         of this field and the other field.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	abstract public TernaryLogicValue isAtLeastAsGoodAs(Field otherField);
	
	/**
	 * Tells if this field is at most as good as the given field. Both this field and the other field can represent a missing value.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this field is at least as good as the other field,<br>
	 *         {@link TernaryLogicValue#FALSE} if this field is not at least as good as the other field,<br>
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other field prevents comparison
	 *         of this field and the other field.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	abstract public TernaryLogicValue isAtMostAsGoodAs(Field otherField);

	/**
	 * Calculates a value (represented by a new field) according to the value of this field and other field's value using the provided calculator {@link EvaluationFieldCalculator}.
	 * <br>
	 * Should be implemented as<br>
	 * 
	 * {@code return calculator.calculate(this, otherField);}
	 * 
	 * @param calculator calculator class {@link EvaluationFieldCalculator} 
	 * @param otherField other field
	 * @return a value calculated by the calculator; this value is represented as a new field
	 */
	public abstract EvaluationField calculate(EvaluationFieldCalculator calculator, EvaluationField otherField);
	
	/**
	 * Gets default evaluation field factory used to create this type of evaluation field.
	 * 
	 * @return default evaluation field factory used to create this type of evaluation field
	 */
	public abstract EvaluationFieldFactory getDefaultFactory();
	
	/**
	 * Gets evaluation field caching factory used to create this type of evaluation field.
	 * 
	 * @return evaluation field caching factory used to create this type of evaluation field
	 */
	public abstract EvaluationFieldCachingFactory getCachingFactory();
	
	/**
	 * Gets evaluation representing unknown (empty) value of this field.
	 * 
	 * @param missingValueType type of missing value for the evaluation attribute for which this field is defined, as returned by
	 *        {@link EvaluationAttribute#getMissingValueType()}
	 * @return evaluation representing unknown (empty) value of this field
	 */
	public abstract EvaluationField getUnknownEvaluation(UnknownSimpleField missingValueType);
	
}
