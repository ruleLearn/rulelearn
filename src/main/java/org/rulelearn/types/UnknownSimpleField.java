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
import org.rulelearn.core.ComparisonResult;
import org.rulelearn.core.ReverseComparableExt;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Top level class for all missing (unknown) simple values in an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class UnknownSimpleField extends SimpleField implements ReverseComparableExt<KnownSimpleField> {

	/**
	 * Tells if the given other field is at least as good as this field.
	 * 
	 * @param otherField other field to be compared to this field
	 * @return {@link TernaryLogicValue#TRUE} if the other field is at least as good as this field,<br>
	 *         {@link TernaryLogicValue#FALSE} if the other field is not at least as good as this field,<br>
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other field prevents comparison
	 *         of the other field to this field.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	abstract public TernaryLogicValue reverseIsAtLeastAsGoodAs(KnownSimpleField otherField);
	
	/**
	 * Tells if the given other field is at most as good as this field.
	 * 
	 * @param otherField other field to be compared to this field
	 * @return {@link TernaryLogicValue#TRUE} if the other field is at most as good as this field,<br>
	 *         {@link TernaryLogicValue#FALSE} if the other field is not at most as good as this field,<br>
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other field prevents comparison
	 *         of the other field to this field.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	abstract public TernaryLogicValue reverseIsAtMostAsGoodAs(KnownSimpleField otherField);
	
	/**
	 * Tells if the given other field is equal to this field (has the same value).
	 * 
	 * @param otherField other field to be compared to this field
	 * @return {@link TernaryLogicValue#TRUE} if the other field is equal to this field,<br>
	 *         {@link TernaryLogicValue#FALSE} if the other field is not equal to this field,<br>
	 *         {@link TernaryLogicValue#UNCOMPARABLE} if type of the other field prevents comparison
	 *         of the other field to this field.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	abstract public TernaryLogicValue reverseIsEqualTo(KnownSimpleField otherField);
	
	/**
	 * Informs if this unknown value always turns out to be equal to any compared evaluation of an object from an information table.
	 * It tests if implemented method {@link ComparableExt#compareToEnum(Object)} returns {@link ComparisonResult#EQUAL}.
	 * 
	 * @return {@code true} if this unknown value is equal to any compared evaluation of an object from an information table
	 *         {@code false} otherwise
	 */
	abstract public boolean equalWhenComparedToAnyEvaluation();
	
	/**
	 * Informs if this unknown value always turns out to be equal to any reverse compared evaluation of an object from an information table.
	 * It tests if implemented method {@link ReverseComparableExt#reverseCompareToEnum(Object)} returns {@link ComparisonResult#EQUAL}.
	 * 
	 * @return {@code true} if this unknown value is equal to any reverse compared evaluation of an object from an information table
	 *         {@code false} otherwise
	 */
	abstract public boolean equalWhenReverseComparedToAnyEvaluation();
	
}
