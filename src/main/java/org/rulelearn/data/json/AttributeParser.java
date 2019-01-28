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
import java.io.Reader;

import org.rulelearn.data.Attribute;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * Parser of attributes {@link Attribute} stored in JSON format.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class AttributeParser {
	/** 
	 * Default encoding of text in JSON.
	 */
	public final static String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * Encoding of text data in JSON.
	 */
	protected String encoding = AttributeParser.DEFAULT_ENCODING;
	
	/**
	 * Default constructor.
	 */
	public AttributeParser () {	
	}
	
	/**
	 * Constructor setting encoding.
	 * 
	 * @param encoding encoding of JSON files
	 */
	public AttributeParser (String encoding) {
		notNull(encoding, "String representing encoding is null.");
		this.encoding  = encoding;
	}
	
	/**
	 * Parses content from reader {@link Reader} and constructs an array with attributes {@link Attribute}.
	 * 
	 * @param reader a reader with content to be parsed
	 * @return array with attributes {@link Attribute}
	 * @throws IOException when something goes wrong with {@link JsonReader}
	 * @throws NullPointerException when the provided reader is null
	 */
	public Attribute[] parseAttributes (Reader reader) throws IOException {
		notNull(reader, "Reader of the JSON file is null.");
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.create();
		
		try (JsonReader jsonReader = new JsonReader(reader)) {
			return gson.fromJson(jsonReader, Attribute[].class);
		}
	}
	
}
