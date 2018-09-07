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

import java.util.List;

import org.rulelearn.core.Precondition;
import org.rulelearn.data.InformationTable;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import static org.rulelearn.core.Precondition.notNull;

/**
 * List (complex) of elementary conditions on the LHS of a decision rule. Each condition is identified by its position on the list.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleConditions {
	
	/**
	 * Elementary conditions, in order of their addition to rule's LHS.
	 */
	protected List<Condition<?>> conditions;
	
	/**
	 * Indices of objects from learning information (decision) table that are considered to be positive objects for this complex of rule conditions.
	 * In case of inducing a certain decision rule, one should assume that positive objects are those belonging to the lower approximation of an approximated set,
	 * or that positive objects are those belonging to the approximated set.
	 * In case of inducing a possible decision rule, positive objects are those belonging to the upper approximation of the approximated set.
	 * The meaning of the notion "positive object" is such, that this complex of rule conditions should be appreciated for covering "positive objects" and/or for not covering the other objects ("non-positive" ones).
	 */
	protected IntSet indicesOfPositiveObjects; //e.g., IntOpenHashSet from fastutil library
	
	/**
	 * Learning information (decision) table in context of which this complex of elementary conditions is evaluated.
	 */
	protected InformationTable learningInformationTable = null;
	
	/**
	 * Indices of attributes from learning information table for which conditions are already stored in {@link #conditions}.
	 */
	protected Int2IntMap attributeIndices;
	
	/**
	 * Iterator among objects covered by the rule conditions.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public class CoveredObjectsIterator {
		
		/**
		 * Index of last found covered object.
		 */
		private int lastCoveredObjectIndex = -1;
	
		/**
		 * Gets index of next object covered by the rule conditions, or {@code -1} if there is no such object
		 * 
		 * @return index of next object covered by the rule conditions, or {@code -1} if there is no such object
		 */
		public int next() {
			int objectsCount = learningInformationTable.getNumberOfObjects();
			for (int i = lastCoveredObjectIndex + 1; i < objectsCount; i++) {
				if (covers(i)) {
					lastCoveredObjectIndex = i;
					return lastCoveredObjectIndex;
				}
			}
			return -1;
		}
	}
	
	/**
	 * Checks if these rule conditions cover the object from the learning information table having given index.
	 * 
	 * @param objectIndex index of an object in the learning information table
	 * @return {@code true} if these rule conditions cover the object with given index, {@code false} otherwise
	 */
	public boolean covers(int objectIndex) {
		int conditionsCount = this.conditions.size();
		for (int i = 0; i < conditionsCount; i++) {
			if (!this.conditions.get(i).satisfiedBy(objectIndex, learningInformationTable)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets an iterator among ordered set of indices of objects from learning information table that are covered by this rule conditions, as returned by {@link #covers(int)}.
	 * 
	 * @return an iterator among ordered set of indices of objects from learning information table that are covered by this rule conditions
	 */
	public CoveredObjectsIterator getCoveredObjectsIterator() {
		return new CoveredObjectsIterator();
	}

	/**
	 * Constructor setting learning information table and the set of indices of positive objects from this table.
	 * 
	 * @param learningInformationTable information table containing positive and negative objects
	 * @param indicesOfPositiveObjects set of indices of positive objects from the given information table
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleConditions(InformationTable learningInformationTable, IntSet indicesOfPositiveObjects) {
		this.learningInformationTable = notNull(learningInformationTable, "Information table is null.");
		this.indicesOfPositiveObjects = notNull(indicesOfPositiveObjects, "Set of indices of positive objects is null.");
		
		this.conditions = new ObjectArrayList<Condition<?>>();
		this.attributeIndices = new Int2IntOpenHashMap();
	}
	
	/**
	 * Gets the set of indices of positive objects
	 * 
	 * @return the set of indices of positive objects
	 */
	public IntSet getIndicesOfPositiveObjects() {
		return this.indicesOfPositiveObjects;
	}

	/**
	 * Gets the learning information table.
	 * 
	 * @return the learning information table
	 */
	public InformationTable getLearningInformationTable() {
		return this.learningInformationTable;
	}

	/**
	 * Tells if object with given index is positive for this set of rule conditions.
	 * 
	 * @param objectIndex index of an object in learning information table
	 * @return {@code true} if object with given index is positive for this set of rule conditions
	 *         {@code false} otherwise
	 */
	public boolean objectIsPositive(int objectIndex) {
		return this.indicesOfPositiveObjects.contains(objectIndex);
	}
	
	/**
	 * Adds given condition to this complex of rule's conditions.
	 * 
	 * @param condition new condition to add
	 * @return index of added condition
	 * 
	 * @throws NullPointerException if condition does not conform to {@link org.rulelearn.core.Precondition#notNull(Object, String)}
	 */
	public int addCondition(Condition<?> condition) {
		this.conditions.add(notNull(condition, "Condition is null."));
		int attributeIndex = condition.getAttributeWithContext().getAttributeIndex();
		int count = this.attributeIndices.containsKey(attributeIndex) ? this.attributeIndices.get(attributeIndex) : 0;
		this.attributeIndices.put(attributeIndex, count + 1);
		
		return this.conditions.size() - 1;
	}
	
	/**
	 * Removes from the list of conditions the condition with given index.
	 * 
	 * @param index index of a condition to remove from this list of conditions
	 * @throws IndexOutOfBoundsException if given index does not refer to any stored condition
	 */
	public void removeCondition(int index) {
		int attributeIndex = this.conditions.get(index).getAttributeWithContext().getAttributeIndex();
		this.conditions.remove(index);

		this.attributeIndices.put(attributeIndex, this.attributeIndices.get(attributeIndex) - 1); //decrease count
	}
	
	/**
	 * Gets list of elementary conditions building this complex of elementary conditions.
	 * The order of conditions on the list reflects the order in which they have been added.
	 * 
	 * @return list of elementary conditions building this complex of elementary conditions,
	 *         in order of their addition
	 */
	public List<Condition<?>> getConditions() {
		return this.conditions;
	}
	
	/**
	 * Gets an elementary condition building this list of elementary conditions and indexed by the given value.
	 * 
	 * @param index index of a condition on this list of conditions
	 * @return an elementary condition building this list of elementary conditions and indexed by the given value
	 */
	public Condition<?> getCondition(int index) {
		return this.conditions.get(index);
	}
	
	/**
	 * Gets index of the first occurrence of the specified condition in this list of conditions.
	 *  
	 * @param condition condition whose index should be found
	 * @return index of the given condition, or -1 if given condition has not been found
	 * 
	 * @throws NullPointerException if given condition is {@code null}
	 */
	public int getConditionIndex(Condition<?> condition) {
		return this.conditions.indexOf(Precondition.notNull(condition, "Condition is null."));
	}
	
	/**
	 * Gets number of conditions on this list of conditions.
	 * 
	 * @return number of conditions on this list of conditions
	 */
	public int size() {
		return this.conditions.size();
	}
	
	/**
	 * Checks if this list contains given condition.
	 * 
	 * @param condition condition whose presence on the list of conditions should be verified
	 * @return {@code true} if this list contains given condition, {@code false} otherwise
	 */
	public boolean containsCondition(Condition<?> condition) {
		return this.conditions.contains(condition);
	}
	
	/**
	 * Tells if attribute with given index is already involved in at least one of elementary conditions.
	 * 
	 * @param attributeIndex index of an attribute
	 * @return {@code true} if the attribute with given index is involved in at least one of elementary conditions,
	 *         {@code false} otherwise
	 */
	public boolean hasConditionForAttribute(int attributeIndex) {
		return (this.attributeIndices.containsKey(attributeIndex) && (this.attributeIndices.get(attributeIndex) > 0));
	}
	
}
