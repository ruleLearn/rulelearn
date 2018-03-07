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

package org.rulelearn.data.json;

import java.lang.reflect.Type;

import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.types.Field;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serializer {@link com.google.gson.JsonSerializer} for IdentificationAttribute {@link org.rulelearn.data.IdentificationAttribute} 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class IdentificationAttributeSerializer implements JsonSerializer<IdentificationAttribute> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(IdentificationAttribute src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		
		json.addProperty("name", src.getName());
		json.addProperty("active", src.isActive());
		
		Field valueType = src.getValueType();
		
		if (valueType instanceof TextIdentificationField) {
			json.addProperty("identifierType", "text");
		} else if (valueType instanceof UUIDIdentificationField) {
			json.addProperty("identifierType", "uuid");
		} else {
			json.addProperty("identifierType", "text"); //default
		}
		
		return json;
	}
	
}
