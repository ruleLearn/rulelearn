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

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.approximations.Union;
import org.rulelearn.approximations.Unions;
import org.rulelearn.core.Precondition;

/**
 * Provides upward/downward unions of ordered decision classes {@link Union} from a set of unions (union container) {@link Unions}.
 * Type of provided unions can be set in class constructor and also modified later using {@link #setProvidedUnionType(Union.UnionType)}.
 * Should it be equal to {@code Union.UnionType#AT_LEAST},
 * a call to {@link #getApproximatedSet(int)} returns i-th upward union from the union container.<br>
 * Should it be equal to {@code Union.UnionType#AT_MOST},
 * a call to {@link #getApproximatedSet(int)} returns i-th downward union from the union container.<br>
 * <br>
 * Unions of each type are sorted from the most specific to the least specific, just like in the underlying union container.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnionProvider implements ApproximatedSetProvider {
	
	/**
	 * Container for all upward and downward unions that can be defined with respect to an information table.
	 * It stores both upward and downward unions of ordered classes.
	 */
	Unions unions;
	
	/**
	 * Type of unions returned by {@link UnionProvider#getApproximatedSet(int)}.
	 */
	Union.UnionType providedUnionType;
	
	/**
	 * Constructs this provider.
	 * 
	 * @param providedUnionType type of unions provided from given union container by this provider by means of {@link #getApproximatedSet(int)};
	 *        should it be equal to {@code Union.UnionType#AT_LEAST},
	 *        next call to {@link #getApproximatedSet(int)} returns i-th upward union;
	 *        should it be equal to {@code Union.UnionType#AT_MOST},
	 *        next call to {@link #getApproximatedSet(int)} returns i-th downward union
	 * @param unions union container {@link Unions}
	 */
	public UnionProvider(Union.UnionType providedUnionType, Unions unions) {
		Precondition.notNull(providedUnionType, "Union type for union provider is null.");
		Precondition.notNull(unions, "Union container for union provider is null.");
		
		this.unions = unions;
		this.providedUnionType = providedUnionType;
	}
	
	/**
	 * Sets type of subsequently provided unions.
	 * 
	 * @param providedUnionType type of union provided by this provider upon future calls to {@link #getApproximatedSet(int)}
	 */
	public void setProvidedUnionType(Union.UnionType providedUnionType) {
		Precondition.notNull(providedUnionType, "Union type for union provider is null.");
		this.providedUnionType = providedUnionType;
	}
	
	/**
	 * Gets type of provided unions.
	 * 
	 * @return type of provided unions
	 */
	public Union.UnionType getProvidedUnionType() {
		return providedUnionType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		switch (providedUnionType) {
		case AT_LEAST:
			return unions.getUpwardUnions(true).length;
		case AT_MOST:
			return unions.getDownwardUnions(true).length;
		default:
			return -1; //this should not happen
		}
	}

	/**
	 * Gets i-th upward/downward union, depending on the union type, as returned by {@link #getProvidedUnionType()}.
	 * 
	 * @param i index of requested union
	 * @return i-th union of considered union type, as returned by {@link #getProvidedUnionType()}
	 * 
	 * @throws IndexOutOfBoundsException if given index is less than zero or
	 *         greater or equal to the number of available unions of considered type
	 */
	@Override
	public ApproximatedSet getApproximatedSet(int i) {
		switch (providedUnionType) {
		case AT_LEAST:
			return unions.getUpwardUnions(true)[i];
		case AT_MOST:
			return unions.getDownwardUnions(true)[i];
		default:
			return null; //this should not happen
		}
	}

}
