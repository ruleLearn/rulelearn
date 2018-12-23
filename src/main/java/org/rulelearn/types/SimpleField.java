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

import org.rulelearn.core.MeanCalculator;


/**
 * Top level class for all (known and unknown) simple evaluations in an information table.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class SimpleField extends EvaluationField {
	
	/**
	 * Calculates mean value of this field's value and other field's value using the provided calculator {@link MeanCalculator}.<br>
	 * <br>
	 * Should be implemented as<br>
	 * 
	 * {@code return calculator.calculate(this, otherValue);}
	 * 
	 * @param calculator mean calculator class {@link MeanCalculator} 
	 * @param otherValue other field
	 * @return mean calculated by calculator
	 */
	public abstract SimpleField getMean(MeanCalculator calculator, SimpleField otherValue);
}
