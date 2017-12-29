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

/**
 * Top level class for all fields (values) in an information table.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Field {
	
	/**
	 * Tells if this field is at least as good as the given field.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return see {@link FieldComparisonResult} 
	 */
	abstract public FieldComparisonResult isAtLeastAsGoodAs(Field otherField);
	
	/**
	 * Tells if this field is at most as good as the given field.
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return <code>true</code> if this field is at most as good as the other field 
	 */
	abstract public FieldComparisonResult isAtMostAsGoodAs(Field otherField);
	
	/**
	 * Tells if this field is equal to the given field (has the same value).
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return <code>true</code> if this field is equal to the other field 
	 */
	abstract public FieldComparisonResult isEqualTo(Field otherField);
	
	/**
	 * Tells if this field is not equal to the given field (has different value).
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return <code>true</code> if this field is not equal to the other field 
	 */
	abstract public FieldComparisonResult isDifferentThan(Field otherField);
}
