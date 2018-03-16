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

import org.rulelearn.data.InformationTable;
import org.rulelearn.types.Field;

/**
 * Condition of a decision rule. May be present both in the condition part and in the decision part of the rule.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Condition {
	
	/**
	 * Information about an attribute for which this condition has been created.
	 */
	protected AttributeInContext attributeInformation;

	/**
     * Checks if given evaluation fulfills this condition.
     * 
     * @param evaluation evaluation (field) to check
     * @return {@code true} if given evaluation fulfills this condition, {@code false} otherwise
     */
    public abstract boolean fulfilledBy(Field evaluation);
    
    /**
     * Checks if an object from the information table, defined by its index, fulfills this condition.
     * 
     * @param objectIndex index of an object in the given information table
     * @param informationTable information table containing the object to check (and possibly also other objects)
     * 
     * @return {@code true} if considered object fulfills this condition, {@code false} otherwise
     */
    public boolean fulfilledBy(int objectIndex, InformationTable informationTable) {
    	return this.fulfilledBy(informationTable.getField(objectIndex, this.attributeInformation.getAttributeIndex()));
    }
    
	/**
	 * Gets text representation of this condition
	 * 
	 * @return text representation of this condition
	 */
	public abstract String toString();

	/**
	 * Returns duplicate of this condition
	 * 
	 * @return duplicate of this condition
	 */
	public abstract Condition duplicate();
	
	/**
	 * Tells if this condition is equal to the other object.
	 * 
	 * @param otherObject other object that this object should be compared with
	 * @return {@code true} if this object is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public abstract boolean equals(Object otherObject);
	
	/**
     * Gets hash code of this condition.
     *
     * @return hash code of this condition
     */
	@Override
    public abstract int hashCode(); 
}
