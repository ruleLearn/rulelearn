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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.rulelearn.data.Attribute;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * Extracts from a block of text (mainly CSV file) a list of objects, each represented as an array of evaluations,
 * where each evaluation is a {@link String string}.
 * Reads all rows of considered text (from a CSV file or from a given {@link Reader reader}),
 * and in each row identifies subsequent fields (cells),
 * taking into account pre-configured field separator. Operates purely on the level of {@link String strings},
 * without interpreting their meaning.
 * As the result of text processing, returns a list of arrays of {@link String strings},
 * where each list item corresponds to a row of cells,
 * and each array item corresponds to a field (cell) in that row.
 * Can handle CSV files with and without headers.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ObjectBuilder {
	
	/** 
	 * Default value for this type of a field.
	 */
	public final static String DEFAULT_ENCODING = "UTF-8";
	
	/** 
	 * Default representation of a separator of fields in CSV files.
	 */
	public final static char DEFAULT_SEPARATOR = ',';
	
	/** 
	 * Default value for this type of a field.
	 */
	public final static String DEFAULT_MISSING_VALUE_STRING = "?";
	
	/**
	 * All attributes which describe objects.
	 */
	Attribute[] attributes = null;
	
	/**
	 * Encoding of text data in CSV files.
	 */
	String encoding = ObjectBuilder.DEFAULT_ENCODING; 
			
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
	 * Builder class for {@link ObjectBuilder}. 
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
		 * Sets attributes describing objects from parsed CSV files.
		 * 
		 * @param attributes array of attributes {@link Attribute} which describe parsed objects
		 * @throws NullPointerException if all or some of the attributes describing data to be loaded have not been set
		 * @return this builder
		 */
		public Builder attributes(Attribute[] attributes) {
			if (attributes != null) {
				for (Attribute attribute : attributes) {
					if (attribute == null) throw new NullPointerException("At least one attribute is not set.");
				}
				this.attributes = attributes;
			}
			else {
				throw new NullPointerException("Attributes are not set.");
			}
			return this;
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
		 * @param value string representation of encoding
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
		public ObjectBuilder build() {
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
		this.header = builder.header;
		this.separator = builder.separator;
		this.missingValueString = builder.missingValueString;
	}
	
	/**
	 * Reads description of all objects from the supplied (CSV) reader and returns them as a list of {@link String} arrays.
	 * 
	 * @param reader a reader of the CSV file
	 * @return a list of {@link String} arrays representing description of all objects in the file on all attributes
	 */
	public List<String[]> getObjects(Reader reader) {
		notNull(reader, "Reader of the CSV file is null.");
		
		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.setLineSeparatorDetectionEnabled(true);
		parserSettings.setHeaderExtractionEnabled(this.header);
		parserSettings.setIgnoreLeadingWhitespaces(true);
		parserSettings.setIgnoreTrailingWhitespaces(true);
		CsvFormat format = new CsvFormat();
		format.setDelimiter(this.separator);
		parserSettings.setFormat(format);
		RowListProcessor rowProcessor = new RowListProcessor();
		parserSettings.setProcessor(rowProcessor);
		if (this.attributes != null) {
			parserSettings.setMaxColumns(this.attributes.length);
		}
		CsvParser parser = new CsvParser(parserSettings);
		
		parser.parse(reader);
		
		String[] attributeNames = null;
		if (this.header) {
			attributeNames = rowProcessor.getHeaders();
		}
		if ((attributeNames != null) && (this.attributes != null)) {
			// TODO check whether attribute names are valid
		}
		
		// set maximal number of object fields
		
		List<String[]> objects = rowProcessor.getRows();
		return objects;
	}
	
	/**
	 * Reads description of all objects from the supplied CSV file and returns them as a list of {@link String} arrays.
	 * 
	 * @param pathToCSVFile a path to the CSV file
	 * @return a list of {@link String} arrays representing description of all objects in the file on all attributes or {@code null} when something goes wrong
	 * @throws IOException when something goes wrong with {@link InputStreamReader}
	 * @throws FileNotFoundException in case the supplied file does not exist
	 * @throws UnsupportedEncodingException in case the encoding specified is not correct 
	 */
	public List<String[]> getObjects(String pathToCSVFile) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		notNull(pathToCSVFile, "String representing path to CSV file is null.");
		
		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.setLineSeparatorDetectionEnabled(true);
		parserSettings.setHeaderExtractionEnabled(this.header);
		parserSettings.setIgnoreLeadingWhitespaces(true);
		parserSettings.setIgnoreTrailingWhitespaces(true);
		CsvFormat format = new CsvFormat();
		format.setDelimiter(this.separator);
		parserSettings.setFormat(format);
		RowListProcessor rowProcessor = new RowListProcessor();
		parserSettings.setProcessor(rowProcessor);
		if (this.attributes != null) {
			parserSettings.setMaxColumns(this.attributes.length);
		}
		CsvParser parser = new CsvParser(parserSettings);
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(pathToCSVFile), this.encoding)) {
			parser.parse(reader);
		}
		
		String[] attributeNames = null;
		if (this.header) {
			attributeNames = rowProcessor.getHeaders();
		}
		if ((attributeNames != null) && (attributes != null)) {
			// TODO check whether attribute names are valid 
		}
		
		List<String[]> objects = rowProcessor.getRows();
		return objects;
	}

}
