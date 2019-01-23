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
 * Build objects from CSV file.
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
	 * Default representation of a separator of fields in CSV file.
	 */
	public final static char DEFAULT_SEPARATOR = ',';
	
	/** 
	 * Default value for this type of a field.
	 */
	public final static String DEFAULT_MISSING_VALUE_STRING = "?";
	
	/**
	 * All attributes which describe objects.
	 */
	Attribute [] attributes = null;
	
	/**
	 * Encoding of text data in CSV file.
	 */
	String encoding = ObjectBuilder.DEFAULT_ENCODING; 
			
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
	 * Constructs object builder.
	 */
	public ObjectBuilder () {}
	
	/**
	 * Constructor initializing this object builder and setting attributes.
	 * 
	 * @param attributes table with attributes
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded have not been set
	 */
	public ObjectBuilder (Attribute [] attributes) {
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
	 * Constructor initializing this object builder and setting encoding of loaded CSV files.
	 * 
	 * @param encoding string representation of encoding
	 * @throws NullPointerException if all or some of the attributes of the constructed information table have not been set
	 */
	public ObjectBuilder (String encoding) {
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding; 
	}
	
	/**
	 * Constructs object builder, sets encoding and header option.
	 * 
	 * @param encoding encoding of text data in CSV file
	 * @param header indication of presence of a header in CSV file
	 * @throws NullPointerException if encoding have not been set
	 */
	public ObjectBuilder (String encoding, boolean header) {
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding;
		this.header = header;
	}
	
	/**
	 * Constructs object builder, sets encoding and header option.
	 * 
	 * @param encoding encoding of text data in CSV file
	 * @param header indication of presence of a header in CSV file
	 * @param separator representation of a separator of fields in CSV file
	 * @throws NullPointerException if encoding have not been set
	 */
	public ObjectBuilder (String encoding, boolean header, char separator) {
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding;
		this.header = header;
		this.separator = separator;
	}
	
	/**
	 * Constructor initializing this object builder and setting expected presence of header in loaded CSV files.
	 * 
	 * @param header tells whether a header is expected in loaded files 
	 */
	public ObjectBuilder (boolean header) {
		this.header = header; 
	}
	
	/**
	 * Constructor initializing this object builder and setting expected presence of header in loaded CSV files together with separator of fields.
	 * 
	 * @param header tells whether a header is expected in loaded files
	 * @param separator representation of a separator of fields in CSV file 
	 */
	public ObjectBuilder (boolean header, char separator) {
		this.header = header; 
		this.separator = separator;
	}
	
	/**
	 * Constructor initializing this object builder and setting attributes together with encoding of loaded CSV files.
	 * 
	 * @param attributes table with attributes
	 * @param encoding string representation of encoding
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded, and/or encoding have not been set
	 */
	public ObjectBuilder (Attribute [] attributes, String encoding) {
		notNull(attributes, "Attributes array is null.");
		notNull(encoding, "Encoding string is null.");
		this.attributes = attributes;
		this.encoding = encoding;
	}
	
	/**
	 * Constructor initializing this object builder and setting attributes together with expected presence of header in CSV files.
	 * 
	 * @param attributes table with attributes
	 * @param header tells whether a header is expected in loaded files
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded have not been set
	 */
	public ObjectBuilder (Attribute [] attributes, boolean header) {
		notNull(attributes, "Attributes array is null.");
		this.attributes = attributes;
		this.header = header;
	}
	
	/**
	 * Constructor initializing this object builder and setting attributes, encoding of loaded CSV files together with expected presence of header in CSV files.
	 * 
	 * @param attributes table with attributes
	 * @param encoding string representation of encoding
	 * @param header tells whether a header is expected in loaded files
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded, and/or encoding have not been set
	 */
	public ObjectBuilder (Attribute [] attributes, String encoding, boolean header) {
		notNull(attributes, "Attributes array is null.");
		notNull(encoding, "Encoding string is null.");
		this.attributes = attributes;
		this.encoding = encoding;
		this.header = header;
	}
	
	/**
	 * Constructor initializing this object builder and setting encoding of loaded CSV files together with expected presence of header in CSV files, and separator of fields.
	 * 
	 * @param attributes array of attributes {@link Attribute} which describe objects
	 * @param encoding encoding of text data in CSV file
	 * @param header indication of presence of a header in CSV file
	 * @param separator representation of a separator of fields in CSV file
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded, and/or encoding have not been set
	 */
	public ObjectBuilder (Attribute [] attributes, String encoding, boolean header, char separator) {
		notNull(attributes, "Attributes array is null.");
		notNull(encoding, "Encoding string is null.");
		this.attributes = attributes;
		this.encoding = encoding;
		this.header = header;
		this.separator = separator;
	}
	
	/**
	 * Constructor initializing this object builder and setting encoding of loaded CSV files together with separator of fields.
	 * 
	 * @param attributes array of attributes {@link Attribute} which describe objects
	 * @param encoding encoding of text data in CSV file
	 * @param separator representation of a separator of fields in CSV file
	 * @throws NullPointerException if all or some of the attributes describing data to be loaded, and/or encoding have not been set
	 */
	public ObjectBuilder (Attribute [] attributes, String encoding, char separator) {
		notNull(attributes, "Attributes array is null.");
		notNull(encoding, "Encoding string is null.");
		this.attributes = attributes;
		this.encoding = encoding;
		this.separator = separator;
	}
	
	/**
	 * Reads description of all objects from the supplied CSV reader and returns them as a list of {@link String} arrays.
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
