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
 * Attributes are set using a JSON string. Objects are set using CSV strings.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableBuilder {
	
	/**
	 * All attributes of the constructed information table.
	 */
	protected Attribute[] attributes = null;
	/**
	 * List of fields of subsequent objects of constructed information table; each array contains subsequent fields of a single object (row) in the information table.
	 */
	protected List<Field[]> fields = null;

	/**
	 * Sole constructor initializing this builder.
	 */
	public InformationTableBuilder() {
		this.attributes = null;
		this.fields = null;
	}
	
	/**
	 * Sets attributes of the constructed information table. This should be done first, before adding any objects.
	 * 
	 * @param jsonAttributes JSON string encoding information about all attributes
	 */
	public void setAttributesFromJSON(String jsonAttributes) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		//gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		Gson gson = gsonBuilder.create();
		
		this.attributes = gson.fromJson(jsonAttributes, Attribute[].class);
	}
	
	/**
	 * Adds one object to this builder. Assumes that attributes have already been set.
	 * Given string is considered to contain subsequent evaluations of a single object, separated by the given separator.
	 * 
	 * @param objectFields string with object's evaluations, separated by the given separator
	 * @param separator separator of object's evaluations
	 */
	public void addObject(String objectFields, String separator) {
		addObject(objectFields.split(separator));
	}
	
	/**
	 * Adds one object to this builder. Assumes that attributes have already been set.
	 * Given array is considered to contain subsequent evaluations of a single object.
	 * 
	 * @param objectFields single object's evaluations
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
	 * 
	 * @throws NullPointerException if attributes of the constructed information table have not been already set
	 * @throws IndexOutOfBoundsException if given attribute index does not correspond to any attribute of the constructed information table
	 */
	protected Field parseField(String field, int attributeIndex) {
//		switch(attributes[attributeIndex].valueType) {
//		 
//		}
		//TODO: implement parsing of fields, taking into account also missing values
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
