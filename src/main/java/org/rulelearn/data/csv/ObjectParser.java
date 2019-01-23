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
	 * All attributes which describe objects.
	 */
	protected Attribute [] attributes = null;
	
	/**
	 * Encoding of text data in CSV.
	 */
	protected String encoding = ObjectBuilder.DEFAULT_ENCODING;

	/**
	 * Indication of presence of a header in CSV file.
	 */
	boolean header = false;
	
	/**
	 * Representation of a separator of fields in CSV file.
	 */
	char separator = ObjectBuilder.DEFAULT_SEPARATOR;
	
	/**
	 * String representation of a missing value in CSV.
	 */
	protected String missingValueString = ObjectBuilder.DEFAULT_MISSING_VALUE_STRING;
	
	/**
	 * 
	 * Builder class for {@link ObjectParser}. 
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
		 * Encoding of text data in CSV.
		 */
		protected String encoding = ObjectBuilder.DEFAULT_ENCODING;

		/**
		 * Indication of presence of a header in CSV file.
		 */
		boolean header = false;
		
		/**
		 * Representation of a separator of fields in CSV file.
		 */
		char separator = ObjectBuilder.DEFAULT_SEPARATOR;
		
		/**
		 * String representation of a missing value in CSV.
		 */
		protected String missingValueString = ObjectBuilder.DEFAULT_MISSING_VALUE_STRING;
		
		/**
		 * Constructs object parser builder, and sets attributes.
		 * 
		 * @param attributes array of attributes {@link Attribute} which describe parsed objects
		 * @throws NullPointerException if the attributes describing data to be loaded have not been set
		 */
		public Builder (Attribute [] attributes) {
			notNull(attributes, "Attributes array is null.");
			this.attributes = attributes;
		}
		
		/**
		 * Sets encoding of parsed CSV files.
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
		 * Sets reading of header in parsed CSV files.
		 * 
		 * @param value indication of header in parsed CSV file
		 * @return this builder 
		 */
		public Builder header (boolean value) {
			this.header = value;
			return this;
		}
		
		/**
		 * Sets separator of fields in parsed CSV files.
		 * 
		 * @param value string representation of encoding
		 * @return this builder 
		 */
		public Builder separator (char value) {
			this.separator = value;
			return this;
		}

		/**
		 * Sets representation of missing value in parsed CSV files.
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
		public ObjectParser build () {
			return new ObjectParser(this);
		}
	}
	
	/**
	 * Constructor initializing all values according to what has been set in builder passed as parameter.
	 * @param builder builder of object parser
	 */
	private ObjectParser(Builder builder) {
		this.attributes = builder.attributes;
		this.encoding = builder.encoding;
		this.header = builder.header;
		this.separator = builder.separator;
		this.missingValueString = builder.missingValueString;
	}
	
	
	/**
	 * Parses content from reader {@link Reader} and constructs an information table {@link InformationTable} with parsed objects. 
	 * 
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
