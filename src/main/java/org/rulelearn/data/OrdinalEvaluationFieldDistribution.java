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

package org.rulelearn.data;

import org.rulelearn.core.ComparableExt;
import org.rulelearn.core.Precondition;
import org.rulelearn.types.EvaluationField;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Distribution of {@link EvaluationField evaluation fields} in the value set of a single ordinal evaluation attribute,
 * i.e., evaluation attribute whose values can be linearly ordered from the smallest to the greatest (as decided by {@link ComparableExt#compareToEx(Object)}.
 * The values in this distribution come from a single column of an information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class OrdinalEvaluationFieldDistribution extends FieldDistribution {
	
	/**
	 * Array of different evaluation fields belonging to the domain (value set) of an ordinal evaluation attribute.
	 * The elements are ordered from the smallest to the greatest (as decided by {@link EvaluationField#compareToEx(EvaluationField)}.
	 */
	EvaluationField[] orderOfFields;
	
	/**
	 * Maps evaluation field to its index in attribute's {@link #orderOfFields ordered domain}.
	 */
	Object2IntMap<EvaluationField> field2DomainIndexMap;
	
	/**
	 * Constructs this distribution.
	 * 
	 * @param orderOfFields array with evaluation fields ordered from the smallest to the greatest (as decided by {@link ComparableExt#compareToEx(Object)};
	 *        all elements have to be different
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public OrdinalEvaluationFieldDistribution(EvaluationField[] orderOfFields) { //TODO: prepare two versions of class constructor, one with optional array cloning
		super();
		Precondition.notNullWithContents(orderOfFields, "Array representing order of evaluation fields should not be null.",
				"Null evaluation field at index %i.");
		this.orderOfFields = orderOfFields.clone();
		this.field2DomainIndexMap = new Object2IntOpenHashMap<EvaluationField>(orderOfFields.length);
		
		for (int i = 0; i < this.orderOfFields.length; i++) {
			this.field2DomainIndexMap.put(this.orderOfFields[i], i);
		}
	}
	
	/**
	 * Gets {@link EvaluationField evaluation field} corresponding to given index in ordered attribute's domain (value set).
	 * 
	 * @param domainIndex index of an element in ordered attribute's domain (value set)
	 * @return {@link EvaluationField evaluation field} corresponding to given index in ordered attribute's domain (value set)
	 * 
	 * @throws IndexOutOfBoundsException if given index does not match any element in the ordered domain
	 */
	public EvaluationField getEvaluationField(int domainIndex) {
		return orderOfFields[domainIndex];
	}
	
	/**
	 * Gets index of the given evaluation field in the ordered domain (value set) of an evaluation attribute.
	 * 
	 * @param evaluationField value in attribute's domain (value set)
	 * @return index of the given evaluation field in the ordered domain (value set) of an evaluation attribute
	 *         or -1 if given evaluation field does not belong to that domain
	 * @throws NullPointerException if given evaluation field is {@code null}
	 */
	public int getDomainIndex(EvaluationField evaluationField) {
		Precondition.notNull(evaluationField, "Evaluation field is null.");
		
		if (field2DomainIndexMap.containsKey(evaluationField)) {
			return field2DomainIndexMap.getInt(evaluationField);
		} else {
			return -1;
		}
	}
	
	/**
	 * Gets median evaluation in this distribution.
	 * 
	 * @return median evaluation in this distribution
	 */
	public EvaluationField getMedian() {
		EvaluationField median;
		
		int[] cumulativeSums = new int[orderOfFields.length];
		int cumulativeSum = 0;

		for (int i = 0; i < cumulativeSums.length; i++) {
			cumulativeSum += getCount(getEvaluationField(i));
			cumulativeSums[i] = cumulativeSum;
		}

		int roundedHalfOfCumulativeSum = Math.round(cumulativeSum / 2); //rounds 0.5 upwards
		median = null;

		for (int i = 0; i < cumulativeSums.length; i++) {
			//TODO: for cumulativeSums[i] == (cumulativeSum / 2) take median randomly
			if (cumulativeSums[i] >= roundedHalfOfCumulativeSum) { //median value found
				median = getEvaluationField(i);
			}
		}
		
		return median;
	}
	
}
