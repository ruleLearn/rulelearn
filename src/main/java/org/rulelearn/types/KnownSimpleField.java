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

import org.rulelearn.core.UncomparableException;

/**
 * Top level class for all non-missing simple values in an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class KnownSimpleField extends SimpleField implements Comparable<KnownSimpleField> {

	/**
	 * Compares this field with the other field. Expects the other field to be either a known simple field
	 * or an unknown simple field (missing value).
	 * 
	 * @param otherField other field that this field is compared to
	 * 
	 * @return negative value if this field is smaller than the other field,<br>
	 *         zero if both fields are equal,<br>
	 *         positive number if this field is greater than the other field
	 * 
	 * @throws ClassCastException if type of the other field is neither {@link KnownSimpleField} nor {@link UnknownSimpleField}
	 * @throws NullPointerException if the other field is {@code null}
	 * @throws UncomparableException if the other field represents an unknown value (i.e., is of type {@link UnknownSimpleField},
	 *         and value of this known field cannot be compared with that unknown value
	 */
	@Override
	public int compareToEx(EvaluationField otherField) throws UncomparableException {
		if (otherField instanceof UnknownSimpleField) {
			return ((UnknownSimpleField)otherField).reverseCompareToEx(this);
		} else {
			return this.compareTo((KnownSimpleField)otherField);
		}
	}

}
