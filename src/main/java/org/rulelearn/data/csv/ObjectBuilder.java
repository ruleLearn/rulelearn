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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.rulelearn.core.Precondition.notNull;

import org.rulelearn.data.Attribute;

import com.univocity.parsers.common.processor.RowListProcessor;
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
	 * All attributes which describe objects.
	 */
	protected Attribute [] attributes = null;
	
	/**
	 * Encoding of text data in CSV file.
	 */
	protected String encoding = ObjectBuilder.DEFAULT_ENCODING; 
			
	/**
	 * Indication of header in CSV file.
	 */
	boolean header = false;
	
	public ObjectBuilder () {}
	
	public ObjectBuilder (Attribute [] attributes) {
		notNull(attributes, "Attributes array is null.");
		this.attributes = attributes;
	}
	
	public ObjectBuilder (String encoding) {
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding; 
	}
	
	public ObjectBuilder (boolean header) {
		this.header = header; 
	}
	
	public ObjectBuilder (Attribute [] attributes, String encoding) {
		notNull(attributes, "Attributes array is null.");
		this.attributes = attributes;
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding;
	}
	
	public ObjectBuilder (Attribute [] attributes, boolean header) {
		notNull(attributes, "Attributes array is null.");
		this.attributes = attributes;
		this.header = header;
	}
	
	public ObjectBuilder (Attribute [] attributes, String encoding, boolean header) {
		notNull(attributes, "Attributes array is null.");
		this.attributes = attributes;
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding;
		this.header = header;
	}
	
	public ObjectBuilder (String encoding, boolean header) {
		notNull(encoding, "Encoding string is null.");
		this.encoding = encoding;
		this.header = header;
	}
	
	/**
	 * Reads description of all objects from the supplied CSV file and returns them as a list of {@link String} arrays.
	 * 
	 * @param pathToCSVFile a path to the CSV file
	 * @return a list of {@link String} arrays representing description of all objects in the file on all attributes or {@code null} when something goes wrong
	 * @throws FileNotFoundException in case the supplied file does not exist
	 * @throws UnsupportedEncodingException in case the encoding specified is not correct 
	 */
	public List<String[]> getObjects(String pathToCSVFile) throws FileNotFoundException, UnsupportedEncodingException {
		notNull(pathToCSVFile, "String representing path to CSV file is null.");
		
		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.setLineSeparatorDetectionEnabled(true);
		parserSettings.setHeaderExtractionEnabled(this.header);
		
		RowListProcessor rowProcessor = new RowListProcessor();
		parserSettings.setProcessor(rowProcessor);

		CsvParser parser = new CsvParser(parserSettings);
		parser.parse(new InputStreamReader(new FileInputStream(pathToCSVFile), this.encoding));
		
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
