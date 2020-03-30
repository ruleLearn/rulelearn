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

package org.rulelearn.dominance;

import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.Table;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.UnknownSimpleField;

import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Calculator of dominance cones, capable of calculating different types of dominance cones of an object found in an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public enum DominanceConeCalculator {
	
	/**
	 * The only instance of this calculator.
	 */
	INSTANCE;
	
	/**
	 * Tells if for any object from the given information table both positive dominance cones, as returned by methods {@link #calculatePositiveDCone(int, InformationTable)}
	 * and {@link #calculatePositiveInvDCone(int, InformationTable)}, are equal (which happens if the fact that object a dominates object b implies that object b is dominated by object a,
	 * and vice versa.
	 * 
	 * @param informationTable information table containing objects for which dominance cones are to be calculated
	 * @return {@code true} if the definition of active condition evaluation attributes of the given information table (and particularly of their {@link EvaluationAttribute#getMissingValueType()
	 *         missing value types} is such, that the fact that object a dominates object b is equivalent to the fact that object b is dominated by object a
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public boolean positiveDominanceConesEqual(InformationTable informationTable) {
		Table<EvaluationAttribute, EvaluationField> activeConditionAttributeFields = informationTable.getActiveConditionAttributeFields();
		
		EvaluationAttribute[] attributes = activeConditionAttributeFields.getAttributes(true);
		int numObj = activeConditionAttributeFields.getNumberOfObjects();
		int numAttr = activeConditionAttributeFields.getNumberOfAttributes();
		UnknownSimpleField missingValueType;
		
		for (int j = 0; j < numAttr; j++) {
			missingValueType = attributes[j].getMissingValueType();
			//missing value type can cause lack of symmetry (e.g. ? eq 8, but not 8 eq ?), like UnknownSimpleFieldMV15;
			//lack of symmetry implies that both D and invD cones are necessary
			if (!(missingValueType.equalWhenComparedToAnyEvaluation() && missingValueType.equalWhenReverseComparedToAnyEvaluation())) {
				for (int i = 0; i < numObj; i++) {
					if (activeConditionAttributeFields.getField(i, j) instanceof UnknownSimpleField) { //problematic MV does appear
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Tells if for any object from the given information table both negative dominance cones, as returned by methods {@link #calculateNegativeDCone(int, InformationTable)}
	 * and {@link #calculateNegativeInvDCone(int, InformationTable)}, are equal (which happens if the fact that object a dominates object b implies that object b is dominated by object a,
	 * and vice versa.
	 * 
	 * @param informationTable information table containing objects for which dominance cones are to be calculated
	 * @return {@code true} if the definition of active condition evaluation attributes of the given information table (and particularly of their {@link EvaluationAttribute#getMissingValueType()
	 *         missing value types} is such, that the fact that object a dominates object b is equivalent to the fact that object b is dominated by object a
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public boolean negativeDominanceConesEqual(InformationTable informationTable) {
		return positiveDominanceConesEqual(informationTable); //redirect - principle of checking equality of positive and negative dominance cones is the same
	}
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its positive dominance cone w.r.t. (straight) dominance relation D.
	 * Formally, D^+(x)={y \in U : y D x}, i.e., positive dominance cone of object x w.r.t. dominance relation D contains objects
	 * such that each of them dominates x.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the positive dominance cone of the object indexed by x, calculated w.r.t. (straight) dominance relation D
	 */
	public IntSortedSet calculatePositiveDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.dominates(y, x, informationTable)) {// y D x
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its negative dominance cone w.r.t. (straight) dominance relation D.
	 * Formally, D^-(x)={y \in U : x D y}, i.e., negative dominance cone of object x w.r.t. dominance relation D contains objects
	 * such that x dominates each of them.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the negative dominance cone of the object indexed by x, calculated w.r.t. (straight) dominance relation D
	 */
	public IntSortedSet calculateNegativeDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.dominates(x, y, informationTable)) {// x D y
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its positive dominance cone w.r.t. (inverse) dominance relation InvD.
	 * Formally, InvD^+(x)={y \in U : x InvD y}, i.e., positive dominance cone of object x w.r.t. (inverse) dominance relation InvD contains objects
	 * such that x is dominated by each of them.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the positive dominance cone of the object indexed by x, calculated w.r.t. (inverse) dominance relation InvD
	 */
	public IntSortedSet calculatePositiveInvDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.isDominatedBy(x, y, informationTable)) {// x InvD y
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}
	
	/**
	 * Calculates, for object having index x, set of indices of objects in its negative dominance cone w.r.t. (inverse) dominance relation InvD.
	 * Formally, InvD^-(x)={y \in U : y InvD x}, i.e., negative dominance cone of object x w.r.t. (inverse) dominance relation InvD contains objects
	 * such that each of them is dominated by x.
	 * 
	 * @param x index of an object from the information table
	 * @param informationTable information table containing object indexed by {@code x}
	 * @return set of indices of objects in the negative dominance cone of the object indexed by x, calculated w.r.t. (inverse) dominance relation InvD
	 */
	public IntSortedSet calculateNegativeInvDCone(int x, InformationTable informationTable) {
		int numberOfObjects = informationTable.getNumberOfObjects();
		IntSortedSet dominanceCone = new IntLinkedOpenHashSet();
		
		for (int y = 0; y < numberOfObjects; y++) { //object being candidate to dominance cone
			if (DominanceChecker.isDominatedBy(y, x, informationTable)) {// y InvD x
				dominanceCone.add(y);
			}
		}
		
		return dominanceCone;
	}

}
