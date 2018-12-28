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
 * Supplies upward/downward unions of ordered decision classes {@link Union} from a set of unions (union container) {@link Unions}.
 * Type of supplied unions can be set in class constructor and also modified later using {@link #setSuppliedUnionType(Union.UnionType)}.
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
public class UnionSupplier implements ApproximatedSetSupplier {
	
	/**
	 * Container for all upward and downward unions that can be defined with respect to an information table.
	 * It stores both upward and downward unions of ordered classes.
	 */
	Unions unions;
	
	/**
	 * Type of unions returned by {@link UnionSupplier#getApproximatedSet(int)}.
	 */
	Union.UnionType suppliedUnionType;
	
	/**
	 * Constructs this supplier.
	 * 
	 * @param suppliedUnionType type of unions supplied from given union container by this supplier by means of {@link #getApproximatedSet(int)};
	 *        should it be equal to {@code Union.UnionType#AT_LEAST},
	 *        next call to {@link #getApproximatedSet(int)} returns i-th upward union;
	 *        should it be equal to {@code Union.UnionType#AT_MOST},
	 *        next call to {@link #getApproximatedSet(int)} returns i-th downward union
	 * @param unions union container {@link Unions}
	 */
	public UnionSupplier(Union.UnionType suppliedUnionType, Unions unions) {
		Precondition.notNull(suppliedUnionType, "Union type for union supplier is null.");
		Precondition.notNull(unions, "Union container for union supplier is null.");
		
		this.unions = unions;
		this.suppliedUnionType = suppliedUnionType;
	}
	
	/**
	 * Sets type of subsequently supplied unions.
	 * 
	 * @param suppliedUnionType type of union supplied by this supplier upon future calls to {@link #getApproximatedSet(int)}
	 */
	public void setSuppliedUnionType(Union.UnionType suppliedUnionType) {
		Precondition.notNull(suppliedUnionType, "Union type for union supplier is null.");
		this.suppliedUnionType = suppliedUnionType;
	}
	
	/**
	 * Gets type of supplied unions.
	 * 
	 * @return type of supplied unions
	 */
	public Union.UnionType getSuppliedUnionType() {
		return suppliedUnionType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		switch (suppliedUnionType) {
		case AT_LEAST:
			return unions.getUpwardUnions(true).length;
		case AT_MOST:
			return unions.getDownwardUnions(true).length;
		default:
			return -1; //this should not happen
		}
	}

	/**
	 * Gets i-th upward/downward union, depending on the union type, as returned by {@link #getSuppliedUnionType()}.
	 * 
	 * @param i index of requested union
	 * @return i-th union of considered union type, as returned by {@link #getSuppliedUnionType()}
	 * 
	 * @throws IndexOutOfBoundsException if given index is less than zero or
	 *         greater or equal to the number of available unions of considered type
	 */
	@Override
	public ApproximatedSet getApproximatedSet(int i) {
		switch (suppliedUnionType) {
		case AT_LEAST:
			return unions.getUpwardUnions(true)[i];
		case AT_MOST:
			return unions.getDownwardUnions(true)[i];
		default:
			return null; //this should not happen
		}
	}

}
