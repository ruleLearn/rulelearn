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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;
import org.rulelearn.data.csv.ObjectBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Parser of objects stored in JSON format using GSON streams. Parses objects from JSON into {@link InformationTable an information table}.
 * 
 * Content is read using {@link InputStreamReader} and JSON structure is parsed object by object using GSON {@link JsonParser}.
 * {@link InformationTable An information table} is constructed using {@link InformationTableBuilder} by adding subsequently parsed objects. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class ObjectStreamParser {
	
	/**
	 * All attributes which describe objects.
	 */
	protected Attribute [] attributes = null;
	
	/**
	 * Encoding of text data in JSON.
	 */
	protected String encoding = ObjectBuilder.DEFAULT_ENCODING;
	
	/**
	 * String representation of a missing value in JSON.
	 */
	protected String missingValueString = ObjectBuilder.DEFAULT_MISSING_VALUE_STRING;
	
	/**
	 * 
	 * Builder class for {@link ObjectStreamParser}. 
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
		 * Encoding of text data in JSON.
		 */
		protected String encoding = ObjectBuilder.DEFAULT_ENCODING;
		
		/**
		 * String representation of a missing value in JSON.
		 */
		protected String missingValueString = ObjectBuilder.DEFAULT_MISSING_VALUE_STRING;
		
		/**
		 * Constructs object parser builder, and sets attributes.
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
		 * Sets representation of missing value in parsed JSON.
		 * 
		 * @param value string representation of missing value 
		 * @throws NullPointerException if representation of missing value has not been set
		 * @return this builder
		 */
		public Builder missingValueString (String value) {
			notNull(value, "String representing missing values is null.");
			this.missingValueString = value;
			return this;
		}
		
		/**
		 * Builds a new object parser {@link ObjectParser}.
		 * 
		 * @return a new object parser
		 */
		public ObjectStreamParser build () {
			return new ObjectStreamParser(this);
		}
	}
	
	/**
	 * Constructor initializing all values according to what has been set in builder passed as parameter.
	 * 
	 * @param builder builder of object parser
	 */
	private ObjectStreamParser(Builder builder) {
		this.attributes = builder.attributes;
		this.encoding = builder.encoding;
		this.missingValueString = builder.missingValueString;
	}
	
	/**
	 * Parses content from reader {@link Reader} and constructs an information table {@link InformationTable} with parsed objects.
	 *  
	 * @param reader a reader with content to be parsed
	 * @return information table {@link InformationTable} with parsed objects
	 * @throws IOException when something goes wrong with {@link Reader}
	 * @throws JsonParseException when malformed JSON read
	 */
	public InformationTable parseObjects (InputStreamReader reader) throws IOException {
		notNull(reader, "Reader with content to be parsed is null.");
		InformationTable informationTable = null;
		
		try (JsonReader jsonReader = new JsonReader(reader)) {
			JsonParser jsonParser = new JsonParser();
			// separator passed to InforamtionTableBuilder is irrelevant here
			InformationTableBuilder informationTableBuilder  = new InformationTableBuilder(this.attributes, new String [] {this.missingValueString});
			jsonReader.beginArray();
			while(jsonReader.hasNext()) {
				JsonElement json = jsonParser.parse(jsonReader);
				if ((json != null) && (!json.isJsonNull())) {
					informationTableBuilder.addObject(parseObject(json));
				}
			}
			jsonReader.endArray();
			informationTable = informationTableBuilder.build();
		}
		
		
		return informationTable;
	}
	/*public InformationTable parseObjects (InputStreamReader reader) throws IOException {
		notNull(reader, "Reader with content to be parsed is null.");
		InformationTable informationTable = null;
		Gson gson = new Gson();
		TypeAdapter<JsonElement> jsonElementTypeAdapter = gson.getAdapter(JsonElement.class);
		try (JsonReader jsonReader = new JsonReader(reader)) {
			// separator passed to InforamtionTableBuilder is irrelevant here
			InformationTableBuilder informationTableBuilder  = new InformationTableBuilder(this.attributes, new String [] {this.missingValueString});
			jsonReader.beginArray();
			while(jsonReader.hasNext()) {
				JsonElement json = jsonElementTypeAdapter.read(jsonReader);
				if ((json != null) && (!json.isJsonNull())) {
					informationTableBuilder.addObject(parseObject(json));
				}
			}
			jsonReader.endArray();
			informationTable = informationTableBuilder.build();
		}
		
		
		return informationTable;
	}*/
	
	
	/**
	 * Parses description of one object from the supplied JSON structure and returns it as a {@link String} array.
	 * 
	 * @param json a JSON structure representing objects
	 * @return a list of {@link String} arrays representing description of all objects in the file on all attributes
	 */
	protected String [] parseObject (JsonElement json) {
		notNull(json, "JSON strucure with objects to be parsed is null.");
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
					object[i] = this.missingValueString;
				}
			}
		}
		
		return object;
	}
}
