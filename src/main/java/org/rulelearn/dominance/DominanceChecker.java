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

import static org.rulelearn.core.Precondition.notNull;
import java.util.function.BiPredicate;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.Table;
import org.rulelearn.types.EvaluationField;

/**
 * Dominance checker capable of verifying if a given pair {@code (x,y)} of objects {@code x, y} from an information table
 * belongs to dominance relation (i.e., {@code x} dominates {@code y}) or inverse dominance relation
 * (i.e., {@code x} is dominated by {@code y}). 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public final class DominanceChecker {
	
	/**
	 * Checks if the first given object ({@code x}) is in relation with the second given object ({@code y}) with respect to active condition attributes
	 * of the given information table.
	 * Such relation holds if for each pair of corresponding evaluations (i.e., evaluations on the same attribute),
	 * {@code integralRelationTester} verifies that the first evaluation (contributing to the first object) is in a particular relation with the second evaluation (contributing to the second object).
	 * Two evaluations are in relation verified by {@code integralRelationTester} if:<br>
	 * <br>
	 * {@code integralRelationTester.test(firstEvaluation, secondEvaluation) == true}.
	 * 
	 * @param x index of the first object
	 * @param y index of the second object
	 * @param informationTable information table containing evaluations of the first and the second object
	 * @param integralRelationTester object implementing {@link BiPredicate#test(Object, Object)} method that compares two evaluations;
	 *        this method is used to compare every two corresponding evaluations (i.e., evaluations on the same attribute),
	 *        the first from the object indexed by {@code x}, and the second from the object indexed by {@code y}
	 * @return {@code true} if the first given object ({@code x}) is in relation with the second given object ({@code y}) with respect to
	 *         active condition attributes of the given information table, {@code false} otherwise
	 */
	protected static boolean isInRelationWith(int x, int y, InformationTable informationTable, BiPredicate<EvaluationField, EvaluationField> integralRelationTester) {
		notNull(informationTable, "Information table for checking dominance is null.");
		
		Table<EvaluationField> evaluations = informationTable.getActiveConditionAttributeFields();
		EvaluationField[] xEvaluations = evaluations.getFields(x);
		EvaluationField[] yEvaluations = evaluations.getFields(y);
		
		for (int i = 0; i < xEvaluations.length; i++) {
			if (!integralRelationTester.test(xEvaluations[i], yEvaluations[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the first given object ({@code x}) dominates the second given object ({@code y}) with respect to active condition attributes
	 * of the given information table.
	 * 
	 * @param x index of the first object
	 * @param y index of the second object
	 * @param informationTable information table containing evaluations of the first and the second object
	 * @return {@code true} if the first given object ({@code x}) dominates the second given object ({@code y}) with respect to
	 *         active condition attributes of the given information table, {@code false} otherwise
	 * 
	 * @throws NullPointerException if given information table is {@code null}
	 * @throws IndexOutOfBoundsException if index {@code x} or {@code y} does not correspond to any object from the given information table
	 */
	public static boolean dominates(int x, int y, InformationTable informationTable) {
		return isInRelationWith(x, y, informationTable,
				(evaluation1, evaluation2) -> evaluation1.isAtLeastAsGoodAs(evaluation2) == TernaryLogicValue.TRUE);
	}
	
	/**
	 * Checks if the first given object ({@code x}) is dominated by the second given object ({@code y}) with respect to active condition attributes
	 * of the given information table.
	 * 
	 * @param x index of the first object
	 * @param y index of the second object
	 * @param informationTable information table containing evaluations of the first and the second object
	 * @return {@code true} if the first given object ({@code x}) is dominated by the second given object ({@code y}) with respect to
	 *         active condition attributes of the given information table, {@code false} otherwise
	 * 
	 * @throws NullPointerException if given information table is {@code null}
	 * @throws IndexOutOfBoundsException if index {@code x} or {@code y} does not correspond to any object from the given information table
	 */
	public static boolean isDominatedBy(int x, int y, InformationTable informationTable) {
		return isInRelationWith(x, y, informationTable,
				(evaluation1, evaluation2) -> evaluation1.isAtMostAsGoodAs(evaluation2) == TernaryLogicValue.TRUE);
	}
}
