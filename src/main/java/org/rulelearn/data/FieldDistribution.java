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

import static org.rulelearn.core.Precondition.nonNegative;
import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.types.Field;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Distribution of fields among considered objects (in a single column of an information table).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class FieldDistribution {

	/**
	 * Maps field to number of objects having this field.
	 */
	protected Object2IntMap<Field> field2CountMap;
	
	/**
	 * Sole constructor. Initializes an empty distribution.
	 */
	public FieldDistribution() {
		this.field2CountMap = new Object2IntOpenHashMap<Field>();
	}
	
	/**
	 * Constructs this distribution based on the given information table.
	 * 
	 * @param informationTable information table for which field distribution in a single column should be constructed
	 * @param attributeIndex index of the considered attribute of the given information table
	 * 
	 * @throws NullPointerException if given information table is {@code null}
	 */
	public FieldDistribution(InformationTable informationTable, int attributeIndex) {
		notNull(informationTable, "Information table for calculation of distribution of fields is null.");
		this.field2CountMap = new Object2IntOpenHashMap<Field>();
		
		//manually browse fields from a single column of the information table
		int numberOfFields = informationTable.getNumberOfObjects();
		for (int i = 0; i < numberOfFields; i++) {
			this.increaseCount(informationTable.getField(i, attributeIndex));
		}

	}
	
	/**
	 * Gets number of objects having given field.
	 * 
	 * @param field field from the considered column of an information table; should not be {@code null}
	 * @return number of objects having given field
	 */
	public int getCount(Field field) {
		return this.field2CountMap.containsKey(field) ? this.field2CountMap.getInt(field) : 0;
	}
	
	/**
	 * Increases by one the number of objects having given field.
	 * 
	 * @param field field from the considered column of an information table; should not be {@code null}
	 */
	public void increaseCount(Field field) {
		int count = this.field2CountMap.containsKey(field) ? this.field2CountMap.getInt(field) : 0;
		this.field2CountMap.put(field, ++count);
	}
	
	/**
	 * Increases by given increase the number of objects having given field.
	 * 
	 * @param field field from the considered column of an information table; should not be {@code null}
	 * @param increase increase of the number of objects having given field; should not be negative
	 * 
	 * @throws InvalidValueException if given increase is smaller than zero
	 */
	public void increaseCount(Field field, int increase) {
		int count = this.field2CountMap.containsKey(field) ? this.field2CountMap.getInt(field) : 0;
		this.field2CountMap.put(field, count + nonNegative(increase, "Increase should not be null."));
	}
	
	/**
	 * Gets number of different fields in this distribution.
	 * 
	 * @return number of different fields in this distribution
	 */
	public int getDifferentFieldsCount() {
		return this.field2CountMap.size();
	}
	
}
