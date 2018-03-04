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

/**
 * Attribute which is used to identify objects.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class IdentificationAttribute extends Attribute {

	protected IdentificationValueType identifyingValueType = IdentificationValueType.TEXT;
	
	public IdentificationAttribute (String name, boolean active, IdentificationValueType identifyingValueType) {
		super(name, active);
		this.identifyingValueType = identifyingValueType;
	}

	/**
	 * @return the identifyingValueType
	 */
	public IdentificationValueType getIdentifyingValueType() {
		return identifyingValueType;
	}
	
	@Override
	public String toString () {
		StringBuilder builder = new StringBuilder ();
		
		builder.append(this.active ? "+ ": "- ");
		builder.append(this.name).append(": ");
		builder.append(this.identifyingValueType);
		
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
				final IdentificationAttribute other = (IdentificationAttribute) otherObject;
				if (this.name.compareTo(other.name) == 0) {
	    	  			if (this.active == other.active) {
	    	  				if (this.identifyingValueType == other.identifyingValueType) {
	    	  					return true;
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
		return Objects.hash(name, active, identifyingValueType);
	}
}
