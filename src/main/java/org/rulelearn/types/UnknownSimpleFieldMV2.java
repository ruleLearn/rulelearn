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

import java.util.Objects;

import org.rulelearn.core.ComparableExt;
import org.rulelearn.core.EvaluationFieldCalculator;
import org.rulelearn.core.TernaryLogicValue;

/**
 * Class implementing a missing attribute value handled according to approach denoted by mv_2. This approach is described in:<br>
 * M. Szeląg, J. Błaszczyński, R. Słowiński, Rough Set Analysis of Classification Data with Missing Values.
 * [In]: L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017,
 * Proceedings, Part I. Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.<br>
 * The crucial definitions of this approach are as follows:<br>
 * <ul>
 * <li>subject y dominates referent x iff for each condition criterion q, y is at least as good as x, i.e.,<br>
 * q(y) is not worse than q(x), or q(y)=*, or q(x)=*;</li>
 * <li>subject y is dominated by referent x iff for each condition criterion q, y is at most as good as x, i.e.,<br>
 * q(y) is not better than q(x), or q(y)=*, or q(x)=*.</li>
 * </ul>
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnknownSimpleFieldMV2 extends UnknownSimpleField {
	
	/**
	 * Checks if the given field is not {@code null} and if this field can be compared with that field (i.e., it is of type {@link SimpleField}).
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this field can be compared with the other field,<br>
	 *         {@link TernaryLogicValue#FALSE} otherwise.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	private TernaryLogicValue canBeComparedWith(Field otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return (otherField instanceof SimpleField) ? TernaryLogicValue.TRUE : TernaryLogicValue.UNCOMPARABLE;
		}
	}

	/**
	 * Compares this field with the other field.
	 * 
	 * @param otherField other field to which this field is being compared to
	 * @return zero, if the other field is an instance of {@link SimpleField}
	 * 
	 * @throws ClassCastException if the other field is not an instance of {@link SimpleField}
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int compareToEx(EvaluationField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Other field is null.");
		} else if (otherField instanceof SimpleField) {
			return 0;
		} else {
			throw new ClassCastException("Other field is not a simple field.");
		}
	}
	
	/**
	 * Compares the other field to this field. Does the reverse comparison than {@link ComparableExt#compareToEx(Object)}.
	 * 
	 * @param otherField other field to be compared to this field
	 * @return zero, if the other field is an instance of {@link SimpleField}
	 * 
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int reverseCompareToEx(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Other field is null.");
		} else {
			return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Field> S selfClone() {
		return (S)new UnknownSimpleFieldMV2();
	}

	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue isEqualTo(Field otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue reverseIsAtLeastAsGoodAs(KnownSimpleField otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue reverseIsAtMostAsGoodAs(KnownSimpleField otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue reverseIsEqualTo(KnownSimpleField otherField) {
		return this.canBeComparedWith(otherField);
	}
	
	/**
	 * Tells if this field object is equal to the other object.
	 * 
	 * @param otherObject other object that this object should be compared with
	 * @return {@code true} if this object is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this) {
			return ((otherObject != null) && (this.getClass().equals(otherObject.getClass())));
		} else {
			return true;
		}
	}
	
	/**
     * Gets hash code of this field.
     *
     * @return hash code of this field
     */
	@Override
	public int hashCode () {
		return Objects.hash(this.getClass());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "?";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationField calculate(EvaluationFieldCalculator calculator, EvaluationField otherField) {
		return calculator.calculate(this, otherField);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 */
	@Override
	public boolean equalWhenComparedToAnyEvaluation() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 */
	@Override
	public boolean equalWhenReverseComparedToAnyEvaluation() {
		return true;
	}
	
	@Override
	public EvaluationFieldFactory getDefaultFactory() {
		//TODO: implement
		return null;
	}

	@Override
	public EvaluationFieldCachingFactory getCachingFactory() {
		//TODO: implement
		return null;
	}

	@Override
	public EvaluationField getUnknownEvaluation(UnknownSimpleField missingValueType) {
		//TODO: implement
		return null;
	}

}
