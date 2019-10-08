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

import static org.rulelearn.core.Precondition.notNull;

import java.util.List;

import org.rulelearn.data.Attribute;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Builds objects {@link String} representation from {@link JsonElement GSON JSON representation}.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class ObjectBuilder {
	
	/** 
	 * Default value for this type of a field.
	 */
	public final static String DEFAULT_ENCODING = "UTF-8";
	
	/** 
	 * Default string representation of a missing value.
	 */
	public final static String DEFAULT_MISSING_VALUE_STRING = "?";
	
	/**
	 * All attributes which describe objects.
	 */
	protected Attribute [] attributes = null;
	
	/**
	 * Encoding of text data in JSON file.
	 */
	protected String encoding = ObjectBuilder.DEFAULT_ENCODING; 
	
	/**
	 * Builder class for {@link ObjectBuilder}. 
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class Builder {
		
		/**
		 * All attributes which describe objects.
		 */
		protected Attribute [] attributes = null;
		
		/**
		 * Encoding of text data in CSV files.
		 */
		protected String encoding = ObjectBuilder.DEFAULT_ENCODING;
						
		/**
		 * Constructor initializing attributes describing objects from parsed JSON.
		 * 
		 * @param attributes array of attributes {@link Attribute} which describe parsed objects
		 * @throws NullPointerException if all or some of the attributes describing data to be loaded have not been set
		 */
		public Builder (Attribute [] attributes) {
			if (attributes != null) {
				for (Attribute attribute : attributes) {
					if (attribute == null) throw new NullPointerException("At least one attribute is not set.");
				}
				this.attributes = attributes;
			}
			else {
				throw new NullPointerException("Attributes are not set.");
			}
		}
		
		/**
		 * Sets encoding of parsed JSON.
		 * 
		 * @param value string representation of encoding 
		 * @throws NullPointerException if encoding has not been set
		 * @return this builder
		 */
		public Builder encoding (String value) {
			notNull(value, "String representing encoding is null.");
			this.encoding = value;
			return this;
		}
		
		/**
		 * Builds a new object builder {@link ObjectBuilder}.
		 * 
		 * @return a new object builder
		 */
		public ObjectBuilder build () {
			return new ObjectBuilder(this);
		}
	}
	
	/**
	 * Constructor initializing all values according to what has been set in builder passed as parameter.
	 * 
	 * @param builder builder of object parser
	 */
	private ObjectBuilder(Builder builder) {
		this.attributes = builder.attributes;
		this.encoding = builder.encoding;
	}
	
	/**
	 * Parses description of all objects from the supplied JSON structure and returns them as a list of {@link String} arrays.
	 * 
	 * @param json a JSON structure representing objects
	 * @return a list of {@link String} arrays representing description of all objects in the file on all attributes
	 */
	public List<String[]> getObjects(JsonElement json) {
		ObjectArrayList<String []> objectList = new ObjectArrayList<String []>();
	
		if ((json != null) && (!json.isJsonNull())) {
			if (json.isJsonArray()) {
				for (int i = 0; i < ((JsonArray)json).size(); i++) {
					objectList.add(getObject(((JsonArray)json).get(i).getAsJsonObject()));
				}
			}
			else {
				objectList.add(getObject(json));
			}
		}
		return objectList;
	}
	
	/**
	 * Parses description of one object from the supplied JSON structure and returns it as a {@link String} array.
	 * 
	 * @param json a JSON structure representing objects
	 * @return a list of {@link String} arrays representing description of all objects in the file on all attributes
	 */
	protected String [] getObject (JsonElement json) {
		String [] object = null;
		
		if (attributes != null) {
			object = new String [attributes.length];
			JsonElement element = null;
			
			for (int i = 0; i < attributes.length; i++) {
				element = json.getAsJsonObject().get(attributes[i].getName());
				if ((element != null) && (element.isJsonPrimitive())) {
					object[i] = element.getAsString();
				}
				else {
					object[i] = ObjectBuilder.DEFAULT_MISSING_VALUE_STRING;
				}
			}
		}
		return object;
	}
}
