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

import static org.rulelearn.core.Precondition.notNull;

import java.util.List;

import org.rulelearn.approximations.ApproximatedSet;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.CompositeField;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.SimpleField;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

/**
 * List (complex) of elementary conditions on the left-hand side (LHS) of a decision rule induced to cover objects from a single approximated set {@link ApproximatedSet}.
 * Each condition is identified by its position on the list.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleConditions {
	
	/**
	 * Elementary conditions, in order of their addition to rule's LHS.
	 */
	ObjectList<Condition<? extends EvaluationField>> conditions;
	
	/**
	 * Indices of all objects that satisfy right-hand side (RHS, decision part) of induced decision rule.
	 * In case of a certain/possible rule, these are the objects from considered approximated set.
	 */
	IntSet indicesOfPositiveObjects; //e.g., IntOpenHashSet from fastutil library
	
	/**
	 * Indices of objects from learning information (decision) table that belong to lower/upper approximation, or boundary of considered approximated set.<br>
	 * <br>
	 * This set is a subset of {@link #indicesOfObjectsThatCanBeCovered}.
	 */
	IntSet indicesOfApproximationObjects; //lower/upper approximation, or boundary of considered approximated set
	
	/**
	 * Indices of all objects from learning information (decision) table that can be covered by these rule conditions (allowed objects).<br>
	 * <br>
	 * This set is a superset of {@link #indicesOfApproximationObjects} and a superset of {@link #indicesOfNeutralObjects}.
	 */
	IntSet indicesOfObjectsThatCanBeCovered; //allowed objects, AO
	
	/**
	 * Indices of all neutral objects from learning information (decision) table that can be covered by these rule conditions. Can be an empty set.
	 * This set is a subset of {@link #indicesOfObjectsThatCanBeCovered}.
	 * Neutral objects are such objects that their decision is neither positive nor negative with respect to the considered approximated set.
	 */
	IntSet indicesOfNeutralObjects;
	
	/**
	 * Learning information (decision) table in context of which this complex of elementary conditions is evaluated.
	 */
	InformationTable learningInformationTable;
	
	/**
	 * Maps index of an attribute from learning information table to list of indices of conditions concerning this attribute that are stored in {@link #conditions}.
	 */
	Int2ObjectMap<IntList> attributeIndex2ConditionIndices; //TODO: think of other structure that does not require update of remaining condition indices once some condition index gets removed
	
	/**
	 * Indices of objects from learning information table covered by these rule conditions.
	 */
	IntList indicesOfCoveredObjects;
	
	/**
	 * Stores for each object index the number of conditions from these rule conditions that do not cover that object (i.e., "eliminate" that object from the set of covered objects).
	 * If {@code notCoveringConditionsCounts[objectIndex] = 2}, it means that these rule conditions contain two conditions that do not cover object indexed by {@code objectIndex}.
	 * If {@code notCoveringConditionsCounts[objectIndex] = 0}, it means that these rule conditions cover object indexed by {@code objectIndex}.
	 * This field is initialized with all zeros.
	 */
	int[] notCoveringConditionsCounts;

	/**
	 * Type of constructed decision rule. See {@link RuleType}.
	 */
	RuleType ruleType;
	
	/**
	 * Semantics of the constructed decision rule. See {@link RuleSemantics}.
	 */
	RuleSemantics ruleSemantics;
	
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
		 * Gets index of next object covered by the rule conditions, or {@code -1} if there is no such object.
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
	 * Initializes {@link #notCoveringConditionsCounts} by setting, for each object from the learning information table, count equal to zero.
	 * Assumes that {@link #learningInformationTable} is already set.
	 */
	private void initializeNotCoveringConditionsCounts() {
		int objectsCount = this.learningInformationTable.getNumberOfObjects();
		this.notCoveringConditionsCounts = new int[objectsCount];
		
		for (int objectIndex = 0; objectIndex < objectsCount; objectIndex++) {
			this.notCoveringConditionsCounts[objectIndex] = 0;
		}
	}
	
	/**
	 * Checks if these rule conditions cover the object from the learning information table having given index.
	 * 
	 * @param objectIndex index of an object in the learning information table
	 * @return {@code true} if these rule conditions cover the object with given index, {@code false} otherwise
	 * 
	 * @throws IndexOutOfBoundsException if given object index does not correspond to any object in the learning information table
	 */
	public boolean covers(int objectIndex) { //TODO: try to optimize response using indicesOfCoveredObjects (if changed to hash set)
		int conditionsCount = this.conditions.size();
		for (int i = 0; i < conditionsCount; i++) {
			if (!this.conditions.get(i).satisfiedBy(objectIndex, this.learningInformationTable)) {
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
	 * Constructor setting learning information table, rule semantics, and the sets of indices of:
	 * <ul>
	 *   <li>all objects that satisfy right-hand side (RHS, decision part) of induced decision rule
	 *       (in case of a certain/possible rule, these are the objects from considered approximated set),</li>
	 *   <li>objects from learning information (decision) table that are taken into account when generating elementary conditions considered for addition to induced decision rule,</li>
	 *   <li>all objects from the information table that can be covered by these rule conditions.</li>
	 * </ul>
	 * In case of inducing a certain decision rule, positive objects are those belonging to the lower approximation of considered approximated set.
	 * In case of inducing a possible decision rule, positive objects are those belonging to the upper approximation of the approximated set.
	 * In case of inducing an approximate decision rule, positive objects are those belonging to the boundary of the approximated set.<br>
	 * <br> 
	 * The set of neutral objects (as returned by {@link #getIndicesOfNeutralObjects()}, is initialized as an empty set {@link IntSets#EMPTY_SET}.
	 * Neutral objects are such objects that their decision is neither positive nor negative with respect to the considered approximated set.
	 * 
	 * @param learningInformationTable learning information table for which these rule conditions are constructed
	 * @param indicesOfPositiveObjects set of indices of positive objects from the given information table, i.e., all objects that satisfy right-hand side (RHS, decision part) of induced decision rule;
	 *        in case of a certain/possible rule, these are the objects from considered approximated set; this set should be unmodifiable
	 * @param indicesOfApproximationObjects indices of objects from learning information (decision) table that belong to lower/upper approximation, or boundary of considered approximated set;
	 *        it is expected that this set is a subset of {@code indicesOfObjectsThatCanBeCovered}; this set should be unmodifiable
	 * @param indicesOfObjectsThatCanBeCovered indices of objects from the given learning information (decision) table that can be covered by these rule conditions; this set should be unmodifiable
	 * @param ruleType type of constructed rule; see {@link RuleType}
	 * @param ruleSemantics semantics of the constructed decision rule; see {@link RuleSemantics}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleConditions(InformationTable learningInformationTable, IntSet indicesOfPositiveObjects, IntSet indicesOfApproximationObjects, IntSet indicesOfObjectsThatCanBeCovered,
			RuleType ruleType, RuleSemantics ruleSemantics) {
		this(learningInformationTable, indicesOfPositiveObjects, indicesOfApproximationObjects, indicesOfObjectsThatCanBeCovered, IntSets.EMPTY_SET, ruleType, ruleSemantics);
	}

	/**
	 * Constructor setting learning learning information table, rule semantics, and the sets of indices of:
	 * <ul>
	 *    <li>all objects that satisfy right-hand side (RHS, decision part) of induced decision rule
	 *        (in case of a certain/possible rule, these are the objects from considered approximated set),</li>
	 *   <li>objects from learning information (decision) table that are taken into account when generating elementary conditions considered for addition to induced decision rule,</li>
	 *   <li>all objects from the information table that can be covered by these rule conditions,</li>
	 *   <li>objects from the information table that are neutral with respect to these rule conditions.</li>
	 * </ul>
	 * In case of inducing a certain decision rule, positive objects are those belonging to the lower approximation of considered approximated set.
	 * In case of inducing a possible decision rule, positive objects are those belonging to the upper approximation of the approximated set.
	 * In case of inducing an approximate decision rule, positive objects are those belonging to the boundary of the approximated set.<br>
	 * <br>
	 * Neutral objects are such objects that their decision is neither positive nor negative with respect to the considered approximated set.
	 * 
	 * @param learningInformationTable learning information table for which these rule conditions are constructed
	 * @param indicesOfPositiveObjects set of indices of positive objects from the given information table, i.e., all objects that satisfy right-hand side (RHS, decision part) of induced decision rule;
	 *        in case of a certain/possible rule, these are the objects from considered approximated set; this set should be unmodifiable
	 * @param indicesOfApproximationObjects indices of objects from learning information (decision) table that belong to lower/upper approximation, or boundary of considered approximated set;
	 *        it is expected that this set is a subset of {@code indicesOfObjectsThatCanBeCovered}; this set should be unmodifiable
	 * @param indicesOfObjectsThatCanBeCovered indices of objects from the given learning information (decision) table that can be covered by these rule conditions; this set should be unmodifiable
	 * @param indicesOfNeutralObjects indices of neutral objects from the given learning information (decision) table; it is expected that this set is a subset of {@code indicesOfObjectsThatCanBeCovered};
	 *        this set should be unmodifiable
	 * @param ruleType type of constructed rule; see {@link RuleType}
	 * @param ruleSemantics semantics of the constructed decision rule; see {@link RuleSemantics}
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleConditions(InformationTable learningInformationTable, IntSet indicesOfPositiveObjects, IntSet indicesOfApproximationObjects, IntSet indicesOfObjectsThatCanBeCovered, IntSet indicesOfNeutralObjects,
			RuleType ruleType, RuleSemantics ruleSemantics) {
		this.learningInformationTable = notNull(learningInformationTable, "Information table is null.");
		this.indicesOfPositiveObjects = notNull(indicesOfPositiveObjects, "Set of indices of positive objects is null.");
		this.indicesOfApproximationObjects = notNull(indicesOfApproximationObjects, "Set of indices of approximation objects is null.");
		this.indicesOfObjectsThatCanBeCovered = notNull(indicesOfObjectsThatCanBeCovered, "Set of indices of objects that can be covered is null.");
		this.indicesOfNeutralObjects = notNull(indicesOfNeutralObjects, "Set of indices of neutral objects is null.");
		
		this.conditions = new ObjectArrayList<Condition<? extends EvaluationField>>();
		this.attributeIndex2ConditionIndices = new Int2ObjectOpenHashMap<IntList>();
		this.indicesOfCoveredObjects = new IntArrayList();
		
		int objectsCount = learningInformationTable.getNumberOfObjects();
		for (int objectIndex = 0; objectIndex < objectsCount; objectIndex++) {
			this.indicesOfCoveredObjects.add(objectIndex); //initially all objects are covered
		}
		
		initializeNotCoveringConditionsCounts();
		
		this.ruleType = notNull(ruleType, "Rule type is null.");
		this.ruleSemantics = notNull(ruleSemantics, "Rule semantics is null.");
	}
	
	/**
	 * Gets indices of all objects that satisfy right-hand side (RHS, decision part) of induced decision rule.
	 * In case of a certain/possible rule, these are the objects from considered approximated set.
	 * 
	 * @return indices of all objects that satisfy right-hand side (RHS, decision part) of induced decision rule
	 */
	public IntSet getIndicesOfPositiveObjects() {
		return this.indicesOfPositiveObjects;
	}
	
	/**
	 * Gets the set of indices of objects from learning information (decision) table that belong to lower/upper approximation, or boundary of considered approximated set.<br>
	 * <br>
	 * The returned set is a subset of set returned by {@link #getIndicesOfObjectsThatCanBeCovered()}.
	 * 
	 * @return the set of indices of objects from learning information (decision) table that belong to lower/upper approximation, or boundary of considered approximated set.
	 */
	public IntSet getIndicesOfApproximationObjects() {
		return this.indicesOfApproximationObjects;
	}
	
	/**
	 * Gets indices of all objects from the learning information (decision) table that can be covered by these rule conditions.
	 * 
	 * @return the set of indices of all objects from the learning information (decision) table that can be covered by these rule conditions
	 */
	public IntSet getIndicesOfObjectsThatCanBeCovered() {
		return this.indicesOfObjectsThatCanBeCovered;
	}
	
	/**
	 * Gets indices of all neutral objects from learning information (decision) table that can be covered by these rule conditions.
	 * Neutral objects are such objects that their decision is neither positive nor negative with respect to the considered approximated set.<br>
	 * <br>
	 * The returned set is a subset of set returned by {@link #getIndicesOfObjectsThatCanBeCovered()}.
	 * 
	 * @return indices of all neutral objects from learning information (decision) table that can be covered by these rule conditions
	 */
	public IntSet getIndicesOfNeutralObjects() {
		return this.indicesOfNeutralObjects;
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
	 * Adds given condition to this complex of rule's conditions.
	 * 
	 * @param condition new condition to add
	 * @return index of added condition
	 * 
	 * @throws NullPointerException if given condition is {@code null}
	 */
	public int addCondition(Condition<? extends EvaluationField> condition) {
		this.conditions.add(notNull(condition, "Condition is null."));
		int addedConditionIndex = this.conditions.size() - 1;
		
		int attributeIndex = condition.getAttributeWithContext().getAttributeIndex();
		
		if (!this.attributeIndex2ConditionIndices.containsKey(attributeIndex)) {
			this.attributeIndex2ConditionIndices.put(attributeIndex, new IntArrayList());
		}
		this.attributeIndex2ConditionIndices.get(attributeIndex).add(addedConditionIndex);
		
		updateCoveredObjectsWithCondition(this.indicesOfCoveredObjects, condition);
		updateNotCoveringConditionsCountsWithCondition(condition);
		
		return addedConditionIndex;
	}
	
	/**
	 * Adds given condition to this complex of rule's conditions. First, checks if given condition is decomposable, and if so, tries to split it into simpler conditions
	 * using given condition separator.
	 * 
	 * @param condition new condition to add
	 * @param conditionSeparator condition separator capable of splitting compound conditions (i.e., conditions with limiting evaluation of type {@link CompositeField})
	 *        into a list (array) of simple conditions (i.e., conditions with limiting evaluations of type {@link SimpleField}); see {@link ConditionSeparator}
	 * @return index of (last) added condition
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public int addCondition(Condition<? extends EvaluationField> condition, ConditionSeparator conditionSeparator) {
		if (notNull(condition, "Condition is null.").isDecomposable()) {
			Condition<? extends EvaluationField>[] conditions;
			try {
				conditions = notNull(conditionSeparator, "Condition separator is null.").separate(condition);
			} catch (InvalidTypeException exception) {
				conditions = new Condition<?>[] {condition};
			}
			
			int lastAddedConditionIndex = this.conditions.size() - 1;
			for (Condition<? extends EvaluationField> simpleCondition : conditions) {
				lastAddedConditionIndex = addCondition(simpleCondition);
			}
			return lastAddedConditionIndex;
		} else {
			return addCondition(condition);
		}
	}
	
	/**
	 * Gets indices of objects covered by these rules conditions.
	 * 
	 * @return indices of objects covered by these rules conditions;
	 *         this list is in general non-ordered if at least one condition has been removed from these rule conditions
	 */
	public IntList getIndicesOfCoveredObjects() {
		return IntLists.unmodifiable(this.indicesOfCoveredObjects);
	}
	
	/**
	 * Gets indices of objects covered by these rules conditions assuming addition of given condition.
	 * 
	 * @param condition condition that can be added to these rule conditions
	 * @return indices of objects covered by these rules conditions assuming addition of given condition
	 * 
	 * @throws NullPointerException if given condition is {@code null}
	 */
	public IntList getIndicesOfCoveredObjectsWithCondition(Condition<? extends EvaluationField> condition) {
		notNull(condition, "Condition is null.");
		
		IntList indicesOfCoveredObjects = new IntArrayList(this.indicesOfCoveredObjects); //copy list
		updateCoveredObjectsWithCondition(indicesOfCoveredObjects, condition);
		
		return indicesOfCoveredObjects;
	}
	
	/**
	 * Gets indices of objects covered by these rule conditions if condition with given index is dropped.
	 * 
	 * @param conditionIndex index of condition in these rule conditions that is considered to be removed
	 * @return indices of objects covered by these rule conditions if condition with given index is dropped
	 * 
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning number of stored conditions
	 */
	public IntList getIndicesOfCoveredObjectsWithoutCondition(int conditionIndex) {
		IntList indicesOfCoveredObjects = new IntArrayList(this.indicesOfCoveredObjects); //copy list
		updateCoveredObjectsWithoutCondition(indicesOfCoveredObjects, conditionIndex, false);
		
		return indicesOfCoveredObjects;
	}
	
	/**
	 * Updates given set of indices of objects covered by these rule conditions assuming addition of given condition.
	 * 
	 * @param indicesOfCoveredObjects indices of objects covered so far by these rule conditions
	 * @param condition condition that is going to or can be added to these rule conditions
	 */
	private void updateCoveredObjectsWithCondition(IntList indicesOfCoveredObjects, Condition<? extends EvaluationField> condition) {
		IntSet nonCoveredObjects = new IntOpenHashSet(indicesOfCoveredObjects.size()); //ensures faster execution of indicesOfCoveredObjects.removeAll(nonCoveredObjects); expected capacity ensures no need of rehashing)
		
		for (int objectIndex : indicesOfCoveredObjects) { //iterate over already covered objects to see if they remain covered or get "rejected" by the given condition
			if (!condition.satisfiedBy(objectIndex, this.learningInformationTable)) {
				nonCoveredObjects.add(objectIndex);
			}
		}
		
		indicesOfCoveredObjects.removeAll(nonCoveredObjects);
	}
	
	/**
	 * Updates counts of not covering conditions {@link #notCoveringConditionsCounts} in view of adding given condition.
	 * Assumes that {@link #learningInformationTable} and {@link #notCoveringConditionsCounts} are already set.
	 */
	private void updateNotCoveringConditionsCountsWithCondition(Condition<? extends EvaluationField> condition) {
		int objectsCount = this.learningInformationTable.getNumberOfObjects();
		
		for (int objectIndex = 0; objectIndex < objectsCount; objectIndex++) { //iterate over all objects to see which are not covered by the given condition
			if (!condition.satisfiedBy(objectIndex, this.learningInformationTable)) { //condition eliminates given object
				this.notCoveringConditionsCounts[objectIndex] = this.notCoveringConditionsCounts[objectIndex] + 1; //increase counter for considered object
			}
		}
	}
	
	/**
	 * Updates given set of indices of objects covered by these rule conditions, and optionally (depending on the flag) also array with counts of conditions not covering particular objects,<br>
	 * assuming removal of condition with given index.
	 * 
	 * @param indicesOfCoveredObjects indices of objects covered by these rule conditions;
	 *        this parameter is modified to reflect the situation
	 *        when condition with given index is dropped from these rule conditions
	 * @param conditionIndex index of condition considered to be removed from these rule conditions
	 * @param tells if {@link #notCoveringConditionsCounts} should be also updated, apart of the given set of indices of objects covered by these rule conditions
	 * 
	 * @throws NullPointerException if the given list is {@code null}
	 * @throws IndexOutOfBoundsException if given condition index is less than zero or too big concerning number of stored conditions
	 */
	private void updateCoveredObjectsWithoutCondition(IntList indicesOfCoveredObjects, int conditionIndex, boolean updateNotCoveringConditionsCounts) {
		Precondition.notNull(indicesOfCoveredObjects, "Indices of covered objects are null.");
		
		Condition<? extends EvaluationField> condition = this.getCondition(conditionIndex); //validates given index of condition
		int numberOfObjects = this.notCoveringConditionsCounts.length;
		
		if (updateNotCoveringConditionsCounts) {
			int count;
			//update this.notCoveringConditionsCounts and given indicesOfCoveredObjects
			for (int objectIndex = 0; objectIndex < numberOfObjects; objectIndex++) {
				count = this.notCoveringConditionsCounts[objectIndex];
				if (count > 0) { //something can change
					if (!condition.satisfiedBy(objectIndex, this.learningInformationTable)) { //dropped condition eliminated current object
						this.notCoveringConditionsCounts[objectIndex] = --count;
						if (count == 0) {
							indicesOfCoveredObjects.add(objectIndex);
						}
					}
				}
			}
		} else {
			//update only given indicesOfCoveredObjects
			for (int objectIndex = 0; objectIndex < numberOfObjects; objectIndex++) {
				if (this.notCoveringConditionsCounts[objectIndex] == 1) { //something can change
					if (!condition.satisfiedBy(objectIndex, this.learningInformationTable)) { //dropped condition eliminated current object
						indicesOfCoveredObjects.add(objectIndex);
					}
				}
			}
		}
		
	}
	
	/**
	 * Removes given condition index of (just) removed condition from the list mapped by given attribute index and decrements remaining condition indices greater than that index.
	 * 
	 * @param attributeIndex index of an attribute for which condition has just been removed
	 * @param removedConditionIndex index of a condition that has just been removed
	 */
	private void updateAttributeIndex2ConditionIndices(int attributeIndex, int removedConditionIndex) {
		IntList listOfConditionIndices = this.attributeIndex2ConditionIndices.get(attributeIndex); //should not be empty!
		listOfConditionIndices.rem(removedConditionIndex); //iterates through the list (it usually contains 1 element, at maximum 2 elements, so the cost is negligible)
		if (listOfConditionIndices.isEmpty()) {
			this.attributeIndex2ConditionIndices.remove(attributeIndex); //remove the mapping from attribute's index to a list of condition indices
		}
		
		int position;
		for (IntList listOfTestedConditionIndices : this.attributeIndex2ConditionIndices.values()) { //consider all lists of condition indices, no matter for which attribute they refer to
			position = 0;
			for (int conditionIndex : listOfTestedConditionIndices) {
				if (conditionIndex > removedConditionIndex) {
					listOfTestedConditionIndices.set(position, conditionIndex - 1); //replace condition index with the decremented one
				}
				position++;
			}
		}
	}
	
	/**
	 * Removes from the list of conditions the condition with given index.
	 * 
	 * @param conditionIndex index of a condition to remove from this list of conditions
	 * @throws IndexOutOfBoundsException if given index does not refer to any stored condition
	 */
	public void removeCondition(int conditionIndex) {
		Condition<? extends EvaluationField> conditionToRemove = getCondition(conditionIndex);
		int attributeIndex = conditionToRemove.getAttributeWithContext().getAttributeIndex();
		
		//first call method that gets considered condition ...
		this.updateCoveredObjectsWithoutCondition(this.indicesOfCoveredObjects, conditionIndex, true);
		//...and only then remove that condition
		this.conditions.remove(conditionIndex);
		
		//remove condition index from the map and decrement remaining indices greater than removed index!
		this.updateAttributeIndex2ConditionIndices(attributeIndex, conditionIndex);
	}
	
	/**
	 * Gets list of elementary conditions building this complex of elementary conditions.
	 * The order of conditions on the list reflects the order in which they have been added.
	 * 
	 * @return list of elementary conditions building this complex of elementary conditions,
	 *         in order of their addition
	 */
	public List<Condition<? extends EvaluationField>> getConditions() {
		return ObjectLists.unmodifiable(this.conditions);
	}
	
	/**
	 * Gets an elementary condition building this list of elementary conditions and indexed by the given value.
	 * 
	 * @param conditionIndex index of a condition on this list of conditions
	 * @return an elementary condition building this list of elementary conditions and indexed by the given value
	 * 
	 * @throws IndexOutOfBoundsException if given index is less than zero or too big concerning number of stored conditions
	 */
	public Condition<? extends EvaluationField> getCondition(int conditionIndex) {
		if (conditionIndex < 0 || conditionIndex >= this.conditions.size()) {
			throw new IndexOutOfBoundsException("Condition index is less than zero or too big concerning number of stored conditions.");
		}
		
		return this.conditions.get(conditionIndex);
	}
	
	/**
	 * Gets list of indices of conditions defined for the attribute with given index. If there is not such condition, then returns and empty list.
	 * 
	 * @param attributeIndex index of a condition on this list of conditions
	 * @return list of indices of conditions defined for the attribute with given index
	 */
	public IntList getConditionIndicesForAttribute(int attributeIndex) {
		return this.attributeIndex2ConditionIndices.containsKey(attributeIndex) ?
				IntLists.unmodifiable(this.attributeIndex2ConditionIndices.get(attributeIndex)) : IntLists.EMPTY_LIST;
	}
	
	/**
	 * Gets index of the first occurrence of the specified condition in this list of conditions.
	 *  
	 * @param condition condition whose index should be found
	 * @return index of the given condition, or -1 if given condition has not been found
	 * 
	 * @throws NullPointerException if given condition is {@code null}
	 */
	public int getConditionIndex(Condition<? extends EvaluationField> condition) {
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
	public boolean containsCondition(Condition<? extends EvaluationField> condition) {
		return this.conditions.contains(condition);
	}
	
	/**
	 * Tells if attribute with given index is already involved in at least one of elementary conditions.
	 * 
	 * @param attributeIndex index of an attribute
	 * @return {@code true} if the attribute with given index is involved in at least one of elementary conditions,
	 *         {@code false} otherwise
	 */
	public boolean containsConditionForAttribute(int attributeIndex) {
		return this.attributeIndex2ConditionIndices.containsKey(attributeIndex);
	}
	
	/**
	 * Gets coverage information concerning induced decision rule.
	 * 
	 * @return coverage information concerning induced decision rule
	 * @see RuleCoverageInformation
	 */
	public RuleCoverageInformation getRuleCoverageInformation() {
		return new RuleCoverageInformation(this.indicesOfPositiveObjects, this.indicesOfNeutralObjects, this.indicesOfCoveredObjects,
				this.learningInformationTable.getNumberOfObjects());
	}
	
	/**
	 * Gets type of the constructed decision rule.
	 * 
	 * @return type of the constructed decision rule
	 */
	public RuleType getRuleType() {
		return this.ruleType;
	}
	
	/**
	 * Gets semantics of the constructed decision rule.
	 * 
	 * @return semantics of the constructed decision rule
	 */
	public RuleSemantics getRuleSemantics() {
		return this.ruleSemantics;
	}
	
	//tests if given rule conditions are less (or equally) general than given prior rule conditions (and thus, respective rule is not more attractive w.r.t. the condition part)
	/**
	 * Tells if these rule conditions are less or equally general as the other rule conditions.
	 * Compares lists of conditions obtained by means of {@link #getConditions()}.
	 * 
	 * @param otherRuleConditions other rule conditions that these rule conditions should be compared with
	 * @return {@code true} if these rule conditions are less or equally general as the other rule conditions,
	 *         {@code false} otherwise
	 * @throws NullPointerException if the other conditions are {@code null}
	 */
	public boolean isLessOrEquallyGeneralAs(RuleConditions otherRuleConditions) {
		List<Condition<? extends EvaluationField>> otherConditionsList = notNull(otherRuleConditions, "Other rule conditions are null.").getConditions();
		
		int otherAttributeIndex;
		IntList conditionIndices;
		TernaryLogicValue isAtMostAsGeneralAs;
		int comparableConditionsCount;
		
		//no condition for some attribute == the most general condition (covering all objects)
		
		for (Condition<? extends EvaluationField> otherCondition : otherConditionsList) {
			otherAttributeIndex = otherCondition.getAttributeWithContext().getAttributeIndex();
			if ((conditionIndices = this.getConditionIndicesForAttribute(otherAttributeIndex)).size() > 0) { //these rule conditions also have condition(s) for the same attribute
				comparableConditionsCount = 0;
				for (int conditionIndex : conditionIndices) {
					isAtMostAsGeneralAs = this.getCondition(conditionIndex).isAtMostAsGeneralAs(otherCondition);
					
					switch (isAtMostAsGeneralAs) {
					case FALSE: //type of condition and its limiting evaluation match
						return false;
					case TRUE: //type of condition and its limiting evaluation match
						comparableConditionsCount++;
						break;
					case UNCOMPARABLE: //type of condition or its limiting evaluation do not match
						break;
					}
				}
				if (comparableConditionsCount == 0) {
					return false;
				}
			} else {
				return false;
			}
		}
			
		return true;
	}
	
	/**
	 * Gets text representation of these rule conditions.
	 * 
	 * @return text representation of these rule conditions, enumerating all elementary conditions in order of their addition
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Condition<? extends EvaluationField> condition : conditions) {
			sb.append(condition);
			if (i < conditions.size() - 1) {
				sb.append(" & ");
			}
			i++;
		}
		return sb.toString();
	}
	
}
