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
 * Type of an attribute in information table.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public enum AttributeType {
	/**
	 * Type of a description attribute.
	 */
	DESCRIPTION,
	/**
	 * Type of a condition attribute.
	 */
	CONDITION,
	/**
	 * Type of a decision attribute.
	 */
	DECISION;
	
	/**
	 * Gets text representation of this instance.
	 * 
	 * @return text representation of this instance
	 */
	public String serialize() {
		switch(this) {
		case DESCRIPTION:
			return "description";
		case CONDITION:
			return "condition";
		case DECISION:
			return "decision";
		default:
			return null;
		}
	}
}
