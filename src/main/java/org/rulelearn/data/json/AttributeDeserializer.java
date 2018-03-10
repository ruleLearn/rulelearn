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
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IdentificationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;
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
	public Attribute deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		// in case not provided set active
		boolean active = true;
		String name = null;
		JsonElement element = null;
		
		Attribute attribute = null;
		
		element = json.getAsJsonObject().get("name");
		// check if name is not empty and set name
		if (element != null) {
			name = element.getAsString();
			if (name.trim().isEmpty())
				throw new JsonParseException("Attribute name is not specified.");
		}
		else
			throw new JsonParseException("Attribute name is not specified.");
		// set active
		element = json.getAsJsonObject().get("active");
		if (element != null)
			active = element.getAsBoolean();
		
		//check type of attribute
		element = json.getAsJsonObject().get("valueType");
		if (element != null) 
			attribute = deserializeEvaluationAttribute(active, name, json, typeOfT, context);
		else
			attribute = deserializeIdentificationAttribute(active, name, json, typeOfT, context);
		
		return attribute;
	}
	
	/**
	 * Deserializes {@link IdentificationAttribute} from JSON.
	 * 
	 * @param active attribute activity state
	 * @param name attribute name
	 * @param json JSON with definition of properties specific for {@link IdentificationAttribute}
	 * @param typeOfT see {@link java.lang.reflect.Type}
	 * @param context see {@link com.google.gson.JsonDeserializationContext}
	 * @return deserialized (constructed) instance of {@link IdentificationAttribute} 
	 * @throws JsonParseException in case identification type is not specified 
	 */
	protected IdentificationAttribute deserializeIdentificationAttribute(boolean active, String name, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		// in case not provided set text type
		IdentificationField valueType;
		String typeName = null;
		JsonElement element = null;
		
		// set identification value type
		element = json.getAsJsonObject().get("identifierType");
		if (element != null) {
			typeName = element.getAsString().toLowerCase();
			if (typeName.trim().isEmpty())
				throw new JsonParseException("Identification type is not specified.");
			
			if (typeName.compareTo("uuid") == 0) {
				valueType = new UUIDIdentificationField(UUIDIdentificationField.getRandomId());
			} else {
				valueType = new TextIdentificationField(TextIdentificationField.getRandomId(8));
			}
				
		}
		else {
			throw new JsonParseException("Identification type is not specified.");
		}
		
		return new IdentificationAttribute(name, active, valueType);
	}
	
	/**
	 * Deserializes {@link EvaluationAttribute} from JSON.
	 * 
	 * @param active attribute activity state
	 * @param name attribute name
	 * @param json JSON with definition of properties specific for {@link EvaluationAttribute}
	 * @param typeOfT see {@link java.lang.reflect.Type}
	 * @param context see {@link com.google.gson.JsonDeserializationContext}
	 * @return deserialized (constructed) instance of {@link EvaluationAttribute}
	 * @throws JsonParseException in case definition of evaluation attribute is not correct (i.e., no value type specified, incorrect domain specification for enumeration attribute)
	 */
	protected EvaluationAttribute deserializeEvaluationAttribute (boolean active, String name, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		// in case not provided set condition type
		AttributeType type = AttributeType.CONDITION;
		// in case not provided set none
		AttributePreferenceType preferenceType = AttributePreferenceType.NONE;
		EvaluationField valueType = null;
		UnknownSimpleField missingValueType = null;				
		JsonElement element = null;
		
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
			if (value.compareTo("decision") == 0)
				type = AttributeType.DECISION;
			else if (value.compareTo("description") == 0)
				type = AttributeType.DESCRIPTION;
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
			if (value.compareTo("integer") == 0) {
				if (	pair)
					valueType = new PairField<IntegerField>(IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType), 
															IntegerFieldFactory.getInstance().create(IntegerField.DEFAULT_VALUE, preferenceType));
				else
					valueType = IntegerFieldFactory.getInstance().create(0, preferenceType);
			}
			else if (value.compareTo("real") == 0) {
				if (	pair)
					valueType = new PairField<RealField>(RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType), 
														RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType));
				else
					valueType = RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, preferenceType);
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
							valueType = new PairField<EnumerationField>(EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, preferenceType), 
																		EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, preferenceType));
						else
							valueType = EnumerationFieldFactory.getInstance().create(domain, EnumerationField.DEFAULT_VALUE, preferenceType);
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
		
		return new EvaluationAttribute(name, active, type, valueType, missingValueType, preferenceType);
	}

}
