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

import org.rulelearn.core.InvalidValueException;

/**
 * Structure embracing an attribute representing evaluations {@link EvaluationAttribute} and its contextual information (like attribute's index in the array of all attributes).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EvaluationAttributeWithContext extends AttributeWithContext<EvaluationAttribute> {

	/**
	 * Constructor initializing all fields.
	 * 
	 * @param attribute evaluation attribute of an information table for which this object is created
	 * @param attributeIndex index of the attribute in the array of all attributes of an information table
	 * 
	 * @throws NullPointerException see {@link AttributeWithContext#AttributeWithContext(Attribute, int)}
	 * @throws InvalidValueException see {@link AttributeWithContext#AttributeWithContext(Attribute, int)}
	 */
	public EvaluationAttributeWithContext(EvaluationAttribute attribute, int attributeIndex) {
		super(attribute, attributeIndex);
	}
	
	/**
	 * Gets preference type {@link AttributePreferenceType} of the attribute.
	 * 
	 * @return preference type of the evaluation attribute
	 */
	public AttributePreferenceType getAttributePreferenceType() {
		return ((EvaluationAttribute)this.attribute).getPreferenceType();
	}

}
