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
import org.rulelearn.types.Field;

/**
 * Top level class for an attribute.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class Attribute {
	/**
	 * Name of an attribute.
	 */
	protected String name = "";
	
	/**
	 * State of an attribute indicating whether it is used in calculations. 
	 */
	protected boolean active = true;
	
	
	public Attribute (String name, boolean active) {
		this.name = name;
		this.active = active;
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
	
	@Override
	public String toString () {
		StringBuilder builder = new StringBuilder ();
		
		builder.append(this.active ? "+ ": "- ");
		builder.append(this.name);
		
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
				final Attribute other = (Attribute) otherObject;
				if (this.name.compareTo(other.name) == 0) {
	    	  			if (this.active == other.active) {
		    	  			return true;
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
		return Objects.hash(name, active);
	}
	
	/**
	 * Gets value type {@link Field} of this attribute.
	 * 
	 * @return the attribute value type
	 */
	public abstract Field getValueType();
}