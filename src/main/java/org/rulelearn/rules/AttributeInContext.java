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

import org.rulelearn.core.Precondition;
import org.rulelearn.data.Attribute;

/**
 * Structure embracing an attribute and its contextual information (like attribute's index in the set of all attributes).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class AttributeInContext {
	/**
	 * Number (index) of the attribute in the array of all attributes.
	 */
	protected int attributeIndex;

	/**
	 * Reference to the attribute.
	 */
	protected Attribute attribute;
	
	/**
	 * Constructor initializing all fields.
	 * 
	 * @param attribute attribute of an information table for which this object is created
	 * @param attributeIndex index of the attribute in the array of all attributes of an information table
	 * 
	 * @throws NullPointerException if attribute does not conform to {@link Precondition#notNull(Object)}
	 * @throws InvalidValueException if attribute's index does not conform to {@link Precondition#isNonNegative(int)}
	 */
	public AttributeInContext(Attribute attribute, int attributeIndex) {
		this.attribute = Precondition.notNull(attribute, "Attribute to be stored in context is null.");
		this.attributeIndex = Precondition.isNonNegative(attributeIndex, "Index of an attribute to be stored in context is negative.");
	}
	
	/**
	 * Gets number (index) of the attribute.
	 * 
	 * @return number (index) of the attribute
	 */
	public int getAttributeIndex() {
		return this.attributeIndex;
	}
	
	/**
	 * Gets name of the attribute.
	 * 
	 * @return name of the attribute
	 */
	public String getAttributeName() {
		return this.attribute.getName();
	}
}
