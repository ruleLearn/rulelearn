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

import org.rulelearn.core.InvalidValueException;

/**
 * Type of simple binary relation between two numbers.
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public enum Relation {
	/**
	 * Relation &gt;.
	 */
	GT, //>
	/**
	 * Relation &gt;=.
	 */
	GE, //>=
	/**
	 * Relation =.
	 */
	EQ, //=
	/**
	 * Relation &lt;=.
	 */
	LE, //<=
	/**
	 * Relation &lt;.
	 */
	LT; //<
	
	/**
	 * Textual representation of {@link #GT} value.
	 */
	public static final String gt = ">";
	/**
	 * Textual representation of {@link #GE} value.
	 */
	public static final String ge = ">=";
	/**
	 * Textual representation of {@link #EQ} value.
	 */
	public static final String eq = "=";
	/**
	 * Textual representation of {@link #LE} value.
	 */
	public static final String le = "<=";
	/**
	 * Textual representation of {@link #LT} value.
	 */
	public static final String lt = "<";
	
	/**
	 * Gets textual representation of this relation.
	 * 
	 * @return textual representation of this relation
	 */
	@Override
	public String toString() {
		switch(this) {
		case GT:
			return gt;
		case GE:
			return ge;
		case EQ:
			return eq;
		case LE:
			return le;
		case LT:
			return lt;
		default:
			throw new InvalidValueException("Not supported relation."); //this should not happen
		}
	}
	
	/**
	 * Constructs this relation using given textual representation.
	 * 
	 * @param relation textual representation of relation; should be equal to one of the public static fields of this enum 
	 * @return constructed relation
	 * 
	 * @throws InvalidValueException if given text does not correspond to any supported relation
	 */
	public static Relation of(String relation) {
		if (relation.equals(gt)) {return GT;}
		if (relation.equals(ge)) {return GE;}
		if (relation.equals(eq)) {return EQ;}
		if (relation.equals(le)) {return LE;}
		if (relation.equals(lt)) {return LT;}
		throw new InvalidValueException("Not supported textual representation of relation.");
	}
	
}
