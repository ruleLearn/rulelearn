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
import org.rulelearn.core.TernaryLogicValue;

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

}
