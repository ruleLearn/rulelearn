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

import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.UnknownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serializer {@link com.google.gson.JsonSerializer} for EvaluationAttribute {@link org.rulelearn.data.EvaluationAttribute} 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class EvaluationAttributeSerializer implements JsonSerializer<EvaluationAttribute> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(EvaluationAttribute src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		
		json.addProperty("name", src.getName());
		json.addProperty("active", src.isActive());
		switch (src.getPreferenceType()) {
			case COST : json.addProperty("preferenceType", "cost"); break;
			case GAIN : json.addProperty("preferenceType", "gain"); break;
			default: json.addProperty("preferenceType", "none");
		}
		switch (src.getType()) {
			case CONDITION : json.addProperty("type", "condition"); break;
			case DECISION : json.addProperty("type", "decision"); break;
			default: json.addProperty("type", "description");
		}
		
		Field type;
		boolean pair = false;
		JsonArray jsonPairArray = new JsonArray(2);;
		if (src.getValueType() instanceof PairField<?>) {
			type = ((PairField<?>)src.getValueType()).getFirstValue();
			pair = true;
			jsonPairArray.add("pair");
		}
		else {
			type = src.getValueType();
		}
		
		if (type instanceof SimpleField) {
			if (type instanceof IntegerField) {
				if (pair) {
					jsonPairArray.add("integer");
					json.add("valueType", jsonPairArray);
				}
				else
					json.addProperty("valueType", "integer");
			}
			else if (type instanceof RealField) {
				if (pair) {
					jsonPairArray.add("real");
					json.add("valueType", jsonPairArray);
				}
				else
					json.addProperty("valueType", "real");
			}
			else if (type instanceof EnumerationField) {
				if (pair) {
					jsonPairArray.add("enumeration");
					json.add("valueType", jsonPairArray);
				}
				else 
					json.addProperty("valueType", "enumeration");
				
				ElementList domain = ((EnumerationField)type).getElementList();
				JsonArray jsonDomain = new JsonArray(domain.getSize());
				for (int i = 0; i < domain.getSize(); i++) {
					jsonDomain.add(domain.getElement(i));
				}
				json.add("domain", jsonDomain);
				json.addProperty("algorithm", domain.getAlgorithm());
			}
		}
		
		UnknownSimpleField missingType = src.getMissingValueType();
		if (missingType instanceof UnknownSimpleFieldMV15)
			json.addProperty("missingValueType", "mv1.5");
		else
			json.addProperty("missingValueType", "mv2");
		
		return json;
	}

}
