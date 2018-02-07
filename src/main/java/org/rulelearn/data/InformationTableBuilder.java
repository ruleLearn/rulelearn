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

import java.util.List;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.types.Field;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Builder for {@link InformationTable}. Allows building of an information table by setting attributes first, and then iteratively adding objects (rows).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableBuilder {
	
	Attribute[] attributes = null;
	List<Field[]> fields = null;

	/**
	 * Sole constructor.
	 */
	public InformationTableBuilder() {

	}
	
	/**
	 * Sets information table attributes.
	 * 
	 * @param jsonAttributes JSON string encoding information about all attributes
	 */
	public void setAttributes(String jsonAttributes) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		//gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		Gson gson = gsonBuilder.create();
		
		this.attributes = gson.fromJson(jsonAttributes, Attribute[].class);
	}
	
	/**
	 * Adds one object to this builder.
	 * 
	 * @param objectFields string with object's evaluations, separated by given separator
	 * @param separator separator of object's evaluations
	 */
	public void addObject(String objectFields, String separator) {
		addObject(objectFields.split(separator));
	}
	
	/**
	 * Adds one object to this builder.
	 * 
	 * @param objectFields object's evaluations
	 */
	public void addObject(String[] objectFields) {
		Field[] object = new Field[objectFields.length];
		
		for (int i = 0; i < objectFields.length; i++) {
			object[i] = parseField(objectFields[i].trim(), i);
		}
		
		this.fields.add(object);
	}
	
	/**
	 * Parses one object's evaluation (field).
	 * 
	 * @param field text-encoded object's evaluation
	 * @param attributeIndex index of the attribute whose value should be parsed
	 * 
	 * @return constructed field
	 */
	protected Field parseField(String field, int attributeIndex) {
//		switch(attributes[attributeIndex].valueType) {
//		case 
//		}
		//TODO: implement
		return null;
	}
	
	/**
	 * Builds information table.
	 * 
	 * @return constructed information table
	 */
	public InformationTable build() {
		return new InformationTable(attributes, fields);
	}
	
}
