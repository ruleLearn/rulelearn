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

import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Contract to make calculations for simple fields {@link EvaluationField} and inherent fields.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface EvaluationFieldCalculator {

	/**
	 * Make calculations for integer fields {@link IntegerField}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return result of calculations
	 */
	public EvaluationField calculate (IntegerField firstField, EvaluationField secondField);
	
	/**
	 * Make calculations for real fields {@link RealField}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return result of calculations
	 */
	public EvaluationField calculate (RealField firstField, EvaluationField secondField);
	
	/**
	 * Make calculations for enumeration fields {@link EnumerationField}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return result of calculations
	 */
	public EvaluationField calculate (EnumerationField firstField, EvaluationField secondField);
	
	/**
	 * Make calculations for enumeration fields {@link EvaluationField}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return result of calculations
	 */
	public EvaluationField calculate(PairField<? extends SimpleField> firstField, EvaluationField secondField);
	
	/**
	 * Make calculations for unknown fields representing missing attribute values handled according to approach denoted as mv_{1.5} {@link UnknownSimpleFieldMV15}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return result of calculations
	 */
	public EvaluationField calculate (UnknownSimpleFieldMV15 firstField, EvaluationField secondField);
	
	/**
	 * Make calculations for unknown fields representing missing attribute values handled according to approach denoted as mv_{2} {@link UnknownSimpleFieldMV2}.
	 * 
	 * @param firstField first filed to make calculations 
	 * @param secondField second filed to make calculations
	 * @return result of calculations
	 */
	public EvaluationField calculate (UnknownSimpleFieldMV2 firstField, EvaluationField secondField);
}
