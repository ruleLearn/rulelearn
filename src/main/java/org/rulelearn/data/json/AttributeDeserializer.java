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
import java.security.NoSuchAlgorithmException;

import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleField;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Deserializer {@link com.google.gson.JsonDeserializer} for Attribute {@link org.rulelearn.data.Attribute}
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class AttributeDeserializer implements JsonDeserializer<Attribute> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public Attribute deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String name = json.getAsJsonObject().get("name").getAsString();
		// in case not provided set inactive
		boolean active = false;
		// in case not provided set description
		AttributeType type = AttributeType.DESCRIPTION;
		Field valueType = null;
		UnknownSimpleField missingValueType = null;
		// in case not provided set none
		AttributePreferenceType preferenceType = AttributePreferenceType.NONE;
		
		JsonElement element = json.getAsJsonObject().get("active");
		if (element != null)
			active = element.getAsBoolean();
		String value = "";
		element = json.getAsJsonObject().get("preferenceType");
		if (element != null) {
			value = element.getAsString().toLowerCase();
			if (value.compareTo("cost") == 0)
				preferenceType = AttributePreferenceType.COST;
			else if (value.compareTo("gain") == 0)
				preferenceType = AttributePreferenceType.GAIN;
		}
		element = json.getAsJsonObject().get("type");
		if (element != null) {
			value = element.getAsString().toLowerCase();
			if (value.compareTo("condition") == 0)
				type = AttributeType.CONDITION;
			else if (value.compareTo("decision") == 0)
				type = AttributeType.DECISION;
		}
		
		boolean pair = false;
		String pairValue;
		element = json.getAsJsonObject().get("valueType");
		if (element != null) {
			// check if it is a pair type
			if (element.isJsonArray()) {
				JsonArray jsonArray = element.getAsJsonArray();
				if (jsonArray.size() == 2) {
					for (int i = 0; i < jsonArray.size(); i++) {
						pairValue = jsonArray.get(i).getAsString().toLowerCase();
						if (pairValue.compareTo("pair") == 0)
							pair = true;
						else // store for further investigation
							value = pairValue;
					}
				}
				else 
					throw new JsonParseException("Incorrect valueType specification.");
			}
			else {
				value = element.getAsString().toLowerCase(); 
			}
			
			// set valueType
			//TODO Default values should be taken from a default configuration class
			if (value.compareTo("integer") == 0) {
				if (	pair)
					valueType = new PairField<IntegerField>(IntegerFieldFactory.getInstance().create(0, preferenceType), IntegerFieldFactory.getInstance().create(0, preferenceType));
				else
					valueType = IntegerFieldFactory.getInstance().create(0, preferenceType);
			}
			else if (value.compareTo("real") == 0) {
				if (	pair)
					valueType = new PairField<RealField>(RealFieldFactory.getInstance().create(0.0, preferenceType), RealFieldFactory.getInstance().create(0.0, preferenceType));
				else
					valueType = RealFieldFactory.getInstance().create(0.0, preferenceType);
			}
			else if (value.compareTo("enumeration") == 0) {
				element = json.getAsJsonObject().get("domain");
				if (element != null) {
					if (element.isJsonArray()) {
						JsonArray jsonArray = element.getAsJsonArray();
						String [] values = new String [jsonArray.size()];
						for (int i = 0; i < jsonArray.size(); i++)
							values[i] = jsonArray.get(i).getAsString();
						
						ElementList domain = null;
						try {
							element = json.getAsJsonObject().get("algorithm");
							if (element != null) { 
								 domain = new ElementList(values, element.getAsString());
							}
							else
								domain = new ElementList(values);
						}
						catch (NoSuchAlgorithmException ex) {
							throw new JsonParseException("Incorrect domain hashing algorithm specified enumeration type attribute.");
						}
						
						if (	pair)
							valueType = new PairField<EnumerationField>(EnumerationFieldFactory.getInstance().create(domain, 0, preferenceType), 
																		EnumerationFieldFactory.getInstance().create(domain, 0, preferenceType));
						else
							valueType = EnumerationFieldFactory.getInstance().create(domain, 0, preferenceType);
					}
					else
						throw new JsonParseException("Incorrect domain specified for enumeration type attribute.");
				}
				else
					throw new JsonParseException("No domain specified for enumeration type attribute.");
			}
			else 
				throw new JsonParseException("Incorrect valueType specification.");
		}
		else 
			throw new JsonParseException("Incorrect valueType specification.");
		
		element = json.getAsJsonObject().get("missingValueType");
		if (element != null) {
			value = element.getAsString().toLowerCase();
			if (value.compareTo("mv1.5") == 0)
				missingValueType = new UnknownSimpleFieldMV15();
			else // in case it is not provided set mv2
				missingValueType = new UnknownSimpleFieldMV2();
		}
		else // in case it is not provided set mv2
			missingValueType = new UnknownSimpleFieldMV2();
		
		return new Attribute(name, active, type, valueType, missingValueType, preferenceType);
	}

}
