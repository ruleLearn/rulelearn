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
	
	public AttributeParser () {	
	}
	
	/**
	 * Parses content from reader {@link Reader} and constructs an array with attributes {@link Attribute}. 
	 * @param reader a reader with content to be parsed
	 * @return array with attributes {@link Attribute}
	 */
	public Attribute[] parseAttributes (Reader reader) {
		Attribute[] attributes = null;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.create();
		
		JsonReader jsonReader = new JsonReader(reader);
		if (jsonReader != null) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
		}
		
		return attributes;
	}
	
}
