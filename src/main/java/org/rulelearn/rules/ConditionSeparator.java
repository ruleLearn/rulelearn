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

package org.rulelearn.rules;

import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.types.CompositeField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.SimpleField;

/**
 * Separates conditions building a compound condition (i.e., condition with limiting evaluation of type {@link CompositeField})
 * into a list (array) of simple conditions (i.e., conditions with limiting evaluations of type {@link SimpleField}) that should be satisfied jointly.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface ConditionSeparator {
	
	/**
	 * Splits given "compound" condition (i.e., condition with limiting evaluation of type {@link CompositeField})
	 * into a list (array) of "simple" conditions (i.e., conditions with limiting evaluations of type {@link SimpleField}) that should be satisfied jointly.<br>
	 * <br>
	 * If actual type of "compound" condition's limiting evaluation is not handled by this separator,
	 * implementing class may choose to throw an {@link InvalidTypeException} exception or return a single-element array containing the original "compound" condition. 
	 * 
	 * @param compoundCondition "compound" condition that should be split into "simple" conditions
	 * @return list (array) of "simple" conditions, with limiting evaluations of type {@link SimpleField}
	 */
	public Condition<? extends EvaluationField>[] separate(Condition<? extends EvaluationField> compoundCondition);

}
