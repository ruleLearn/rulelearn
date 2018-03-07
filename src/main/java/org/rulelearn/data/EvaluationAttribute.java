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

import java.util.Objects;

import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.UnknownSimpleField;

/**
 * Attribute which describes evaluations stored in an information table {@link InformationTable}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EvaluationAttribute extends Attribute {
	/**
	 * Type {@link AttributeType} of an attribute.
	 */
	protected AttributeType type;
	
	/**
	 * Type of value {@link EvaluationField} of an attribute.
	 */
	protected EvaluationField valueType;
	
	/**
	 * Type of missing value {@link UnknownSimpleField} of an attribute. 
	 */
	protected UnknownSimpleField missingValueType;
	
	/**
	 * Preference type {@link AttributePreferenceType} of an attribute. 
	 */
	protected AttributePreferenceType preferenceType;
	
	public EvaluationAttribute (String name, boolean active, AttributeType type, EvaluationField valueType, UnknownSimpleField missingValueType, AttributePreferenceType preferenceType) {
		super(name, active);
		this.type = type;
		this.valueType = valueType;
		this.missingValueType = missingValueType;
		this.preferenceType = preferenceType;
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
	 * Gets value type of this attribute.
	 * 
	 * @return the attribute value type
	 */
	public EvaluationField getValueType() {
		return valueType;
	}

	/**
	 * Gets missing value type {@link UnknownSimpleField} of this attribute.
	 * 
	 * @return the attribute missing value type
	 */
	public UnknownSimpleField getMissingValueType() {
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
	
	@Override
	public String toString () {
		StringBuilder builder = new StringBuilder ();
		
		builder.append(this.active ? "+ ": "- ");
		builder.append(this.name).append(": ");
		builder.append(this.type).append(", ");
		builder.append(this.preferenceType).append(", ");
		builder.append(this.valueType).append(", ");
		builder.append(this.missingValueType);
		
		return builder.toString();
	}
	
	/**
	 * Tells if this attribute object is equal to the other object.
	 * 
	 * @param otherObject other object that this object should be compared with
	 * @return {@code true} if this object is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this) {
			if (otherObject != null && getClass().equals(otherObject.getClass())) {
				final EvaluationAttribute other = (EvaluationAttribute) otherObject;
				if (this.name.compareTo(other.name) == 0) {
					if (this.preferenceType == other.preferenceType) {
	    	  				if (this.type == other.type) {
		    	  				if (this.active == other.active) {
		    	  					if (this.valueType.equals(other.valueType)) {
		    	  						return this.missingValueType.equals(other.missingValueType);
		    	  					}
		    	  					else return false;
		    	  				}
		    	  				else return false;
	    	  				}
	    	  				else return false;
	    	  			}
	    	  			else return false;
	    	  		}
				else return false;
			}
			else return false;
		}
		else return true;
	} 
	
	/**
     * Gets hash code of this attribute.
     *
     * @return hash code of this field
     */
	@Override
	public int hashCode () {
		return Objects.hash(name, active, type, valueType, missingValueType, preferenceType);
	}

}
