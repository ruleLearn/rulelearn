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
import org.rulelearn.types.Field;

/**
 * Converts a list of text-encoded objects returned by {@link org.rulelearn.data.csv.ObjectBuilder}
 * to a corresponding {@link InformationTable information table}.
 * In this process, each object built by {@link org.rulelearn.data.csv.ObjectBuilder}, represented as an array of {@link String strings},
 * is converted to an array of {@link Field fields}. In this process,
 * {@link InformationTableBuilder information table builder} is used - see {@link InformationTableBuilder#addObject(String[])}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ObjectParser {
	
	/**
	 * All attributes which describe objects.
	 */
	protected Attribute[] attributes = null;
	
	/**
	 * Encoding of text data in CSV files.
	 */
	protected String encoding = ObjectBuilder.DEFAULT_ENCODING;

	/**
	 * Indication of presence of a header in CSV files.
	 */
	boolean header = false;
	
	/**
	 * Representation of a separator of fields in CSV files.
	 */
	char separator = ObjectBuilder.DEFAULT_SEPARATOR;
	
	/**
	 * String representation of a missing value in CSV files.
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
		protected Attribute[] attributes = null;
		
		/**
		 * Encoding of text data in CSV files.
		 */
		protected String encoding = ObjectBuilder.DEFAULT_ENCODING;

		/**
		 * Indication of presence of a header in CSV files.
		 */
		boolean header = false;
		
		/**
		 * Representation of a separator of fields in CSV files.
		 */
		char separator = ObjectBuilder.DEFAULT_SEPARATOR;
		
		/**
		 * String representation of a missing value in CSV files.
		 */
		protected String missingValueString = ObjectBuilder.DEFAULT_MISSING_VALUE_STRING;
		
		/**
		 * Constructor initializing attributes.
		 * 
		 * @param attributes array of attributes {@link Attribute} which describe parsed objects
		 * @throws NullPointerException if all or some of the attributes describing data to be loaded have not been set
		 */
		public Builder(Attribute[] attributes) {
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
		 * Sets encoding of parsed CSV files.
		 * 
		 * @param value string representation of encoding 
		 * @throws NullPointerException if encoding has not been set
		 * @return this builder
		 */
		public Builder encoding(String value) {
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
		public Builder header(boolean value) {
			this.header = value;
			return this;
		}
		
		/**
		 * Sets separator of fields in parsed CSV files.
		 * 
		 * @param value string representation of separator
		 * @return this builder 
		 */
		public Builder separator(char value) {
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
		public Builder missingValueString(String value) {
			notNull(value, "String representing missing values is null.");
			this.missingValueString = value;
			return this;
		}
		
		/**
		 * Builds a new object parser {@link ObjectParser}.
		 * 
		 * @return a new object parser
		 */
		public ObjectParser build() {
			return new ObjectParser(this);
		}
	}
	
	/**
	 * Constructor initializing all values according to what has been set in builder passed as parameter.
	 * 
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
	 * @return information table {@link InformationTable} with parsed objects or {@code null} when the table cannot be constructed
	 * @throws NullPointerException when the provided reader is {@code null}
	 */
	public InformationTable parseObjects(Reader reader) {
		notNull(reader, "Reader is null.");
		
		//TODO: the code below is the same as in InformationTableBuilder.safelyBuildFromCSVFile(String, String, boolean, char) ...
		
		InformationTable informationTable = null;
		List<String[]> objects = null;
		ObjectBuilder objectBuilder = new ObjectBuilder.Builder().attributes(this.attributes).encoding(this.encoding).header(this.header).separator(this.separator).build();
		
		if (objectBuilder != null) {
			objects = objectBuilder.getObjects(reader);
			if (objects != null) {
				// separator passed to InformationTableBuilder is irrelevant here
				InformationTableBuilder informationTableBuilder = new InformationTableBuilder(this.attributes, ",", new String[]{this.missingValueString}); 
				for (int i = 0; i < objects.size(); i++) {
					informationTableBuilder.addObject(objects.get(i));
				}
				
				informationTableBuilder.clearVolatileCaches();
				informationTable = informationTableBuilder.build();
			}
		}
		return informationTable;
	}
}
