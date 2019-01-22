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

package org.rulelearn.data.csv;

import static org.rulelearn.core.Precondition.notNull;

import java.io.Reader;
import java.util.List;

import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;

/**
 * Parser of objects stored in CSV format.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class ObjectParser {
	/** 
	 * Default encoding.
	 */
	public final static String DEFAULT_ENCODING = "UTF-8";
	
	/** 
	 * Default string representation of a missing value.
	 */
	protected final static String MISSING_VALUE_STRING = "?";
	
	/**
	 * All attributes which describe objects.
	 */
	protected Attribute [] attributes = null;
	
	/**
	 * Encoding of text data in CSV.
	 */
	protected String encoding = ObjectParser.DEFAULT_ENCODING;
	
	/**
	 * String representation of a missing value in CSV.
	 */
	protected String missingValueString = ObjectParser.MISSING_VALUE_STRING;
	
	/**
	 * Constructs object parser, and sets attributes.
	 * 
	 * @param attributes array of attributes {@link Attribute} which describe parsed objects
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded have not been set
	 */
	public ObjectParser (Attribute [] attributes) {
		notNull(attributes, "Attributes array is null.");
		this.attributes = attributes;
	}
	
	/**
	 * Constructs object parser, sets attributes, and string representation of missing value.
	 * 
	 * @param attributes array of attributes {@link Attribute} which describe parsed objects
	 * @param missingValueString string representation of missing value in CSV
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded, and/or string representing missing values have not been set
	 */
	public ObjectParser (Attribute [] attributes, String missingValueString) {
		this(attributes);
		notNull(missingValueString, "String representing missing values is null.");
		this.missingValueString = missingValueString;
	}
	
	/**
	 * Constructs object parser, sets attributes, string representation of missing value, and encoding.
	 * 
	 * @param attributes array of attributes {@link Attribute} which describe parsed objects
	 * @param missingValueString string representation of missing value in CSV
	 * @param encoding encoding of text data in CSV
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded, and/or string representing missing values, and/or encoding string have not been set
	 */
	public ObjectParser (Attribute [] attributes, String missingValueString, String encoding) {
		this(attributes, missingValueString);
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding;
	}
	
	/**
	 * Parses content from reader {@link Reader} and constructs an information table {@link InformationTable} with parsed objects. 
	 * @param reader a reader with content to be parsed
	 * @param header indicative of whether header is present in the parsed CSV
	 * @param separator separator representation of a separator of fields in CSV
	 * @return information table {@link InformationTable} with parsed objects or {@code null} when the table cannot be constructed
	 * @throws NullPointerException when the provided reader is null
	 */
	public InformationTable parseObjects (Reader reader, boolean header, char separator) {
		InformationTable informationTable = null;
		
		notNull(reader, "Reader is null.");
		ObjectBuilder objectBuilder = new ObjectBuilder(this.attributes, this.encoding, header, separator);
		List<String []> objects = null;
		
		objects = objectBuilder.getObjects(reader);
		if (objects != null) {
			// separator passed to InforamtionTableBuilder is irrelevant here
			InformationTableBuilder informationTableBuilder  = new InformationTableBuilder(this.attributes, ",", 
													new String [] {this.missingValueString}); 
			for (int i = 0; i < objects.size(); i++) {
				informationTableBuilder.addObject(objects.get(i));
			}
			informationTable = informationTableBuilder.build();
		}
		
		return informationTable;
	}
}
