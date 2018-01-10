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

/**
 * Top level class for an attribute.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class Attribute {
	/**
	 * Name of an attribute.
	 */
	protected String name = "";
	
	/**
	 * State of an attribute indicating whether it is used in calculations. 
	 */
	protected boolean active = true;
	
	/**
	 * Type {@link AttributeType} of an attribute. 
	 */
	protected AttributeType type;
	
	/**
	 * Type of value {@link AttributeValueType} of an attribute. 
	 */
	protected AttributeValueType valueType;
	
	/**
	 * Type of missing value {@link AttributeMissingValueType} of an attribute. 
	 */
	protected AttributeMissingValueType missingValueType;
	
	/**
	 * Preference type {@link AttributePreferenceType} of an attribute. 
	 */
	protected AttributePreferenceType preferenceType;
	
	public Attribute (String name, boolean active, AttributeType type, AttributeValueType valueType, AttributeMissingValueType missingValueType, AttributePreferenceType preferenceType) {
		this.name = name;
		this.active = active;
		this.type = type;
		this.valueType = valueType;
		this.missingValueType = missingValueType;
		this.preferenceType = preferenceType;
	}

	/**
	 * Gets name of this attribute.
	 * 
	 * @return name of attribute
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets active state of this attribute.
	 * 
	 * @return the attribute active state
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Gets type {@link AttributeType} of this attribute.
	 * 
	 * @return the attribute type
	 */
	public AttributeType getType() {
		return type;
	}

	/**
	 * Gets value type {@link AttributeValueType} of this attribute.
	 * 
	 * @return the attribute value type
	 */
	public AttributeValueType getValueType() {
		return valueType;
	}

	/**
	 * Gets missing value type {@link AttributeMissingValueType} of this attribute.
	 * 
	 * @return the attribute missing value type
	 */
	public AttributeMissingValueType getMissingValueType() {
		return missingValueType;
	}

	/**
	 * Gets preference type {@link AttributePreferenceType} of this attribute.
	 * 
	 * @return the attribute preference type
	 */
	public AttributePreferenceType getPreferenceType() {
		return preferenceType;
	}
}
