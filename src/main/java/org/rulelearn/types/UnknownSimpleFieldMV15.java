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

import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.core.UncomparableException;
import org.rulelearn.types.SimpleField;

/**
 * Class implementing a missing attribute value handled according to approach denoted by mv_2. This approach is described in:<br>
 * M. Szeląg, J. Błaszczyński, R. Słowiński, Rough Set Analysis of Classification Data with Missing Values.
 * [In]: L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017,
 * Proceedings, Part I. Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.<br>
 * The crucial definitions of this approach are as follows:<br>
 * <ul>
 * <li>subject y dominates referent x iff for each condition criterion q, y is at least as good as x, i.e.,<br>
 * q(y) is not worse than q(x), or q(y)=*;</li>
 * <li>subject y is dominated by referent x iff for each condition criterion q, x is at least as good as y, i.e.,<br>
 * q(x) is not worse than q(y), or q(y)=*.</li>
 * </ul>
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnknownSimpleFieldMV15 extends UnknownSimpleField {
	
	/**
	 * Checks if the given field is not {@code null} and if this field can be compared with that field (i.e., it is of type {@link SimpleField}).
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this field can be compared with the other field,<br>
	 *         {@link TernaryLogicValue#FALSE} otherwise.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	private boolean canBeComparedWith(Field otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return (otherField instanceof SimpleField);
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
	public int compareToEx(Field otherField) {
		if (this.canBeComparedWith(otherField)) {
			return 0;
		} else {
			throw new ClassCastException("Other field cannot be compared to this uknown field.");
		}
	}

	/**
	 * Compares the other field to this field. Does the reverse comparison than {@link Comparable#compareTo(Object)}.
	 * 
	 * @param otherField other field to be compared to this field
	 * @return zero, as any other non-null simple field is assumed to be equal to this field
	 * 
	 * @throws NullPointerException if the other field is {@code null}
	 * @throws UncomparableException if the other field is not {@code null} (so one cannot decide
	 *         the result of comparison of the other known simple field to this unknown simple field)
	 */
	@Override
	public int reverseCompareToEx(KnownSimpleField otherField) throws UncomparableException {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			throw new UncomparableException("Other field cannot be compared to this unknown field.");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Field> S selfClone() {
		return (S)new UnknownSimpleFieldMV15();
	}

	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
		if (this.canBeComparedWith(otherField)) {
			return TernaryLogicValue.TRUE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
		if (this.canBeComparedWith(otherField)) {
			return TernaryLogicValue.TRUE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue isEqualTo(Field otherField) {
		if (this.canBeComparedWith(otherField)) {
			return TernaryLogicValue.TRUE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue reverseIsAtLeastAsGoodAs(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return TernaryLogicValue.FALSE;
		}
	}

	@Override
	public TernaryLogicValue reverseIsAtMostAsGoodAs(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return TernaryLogicValue.FALSE;
		}
	}

	@Override
	public TernaryLogicValue reverseIsEqualTo(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return TernaryLogicValue.FALSE;
		}
	}

}
