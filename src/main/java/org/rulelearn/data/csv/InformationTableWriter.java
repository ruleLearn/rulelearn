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

import java.io.IOException;
import java.io.Writer;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;

/**
 * Writes {@link Attribute attributes} and objects from an {@link InformationTable information table} to a pair of text files.
 * Attributes are stored in JSON format, and objects are stored in CSV format.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableWriter {
	
	/**
	 * Internal writer used to write attributes to a JSON file.
	 */
	org.rulelearn.data.json.InformationTableWriter jsonInformationTableWriter;
	
	/**
	 * Tells if writers passed as parameters to {@link #writeAttributes(InformationTable, Writer)}
	 * and {@link #writeObjects(InformationTable, Writer, String)} methods
	 * should be automatically closed by these methods to force content flushing and release of blocked resources (like file handles).
	 * Initially set to {@code true}.
	 */
	boolean autoCloseWriters = true;
	
	/**
	 * Constructs this writer. See {@link org.rulelearn.data.json.InformationTableWriter#InformationTableWriter()}.
	 * Ensures that {@code autoCloseWriters} property is set to {@code true}.
	 */
	public InformationTableWriter() {
		jsonInformationTableWriter = new org.rulelearn.data.json.InformationTableWriter();
		autoCloseWriters = true;
	}
	
	/**
	 * Constructs this writer. See {@link org.rulelearn.data.json.InformationTableWriter#InformationTableWriter(boolean)}.
	 * Ensures that {@code autoCloseWriters} property is set to {@code true}.
	 * 
	 * @param setPrettyPrinting indicator of pretty printing in written JSON
	 */
	public InformationTableWriter(boolean setPrettyPrinting) {
		jsonInformationTableWriter = new org.rulelearn.data.json.InformationTableWriter(setPrettyPrinting);
		autoCloseWriters = true;
	}
	
	/**
	 * Writes attributes from the information table passed as parameter to JSON using the writer passed as parameter.
	 * Calls {@link org.rulelearn.data.json.InformationTableWriter#writeAttributes(InformationTable, Writer)}.
	 * If {@code autoCloseWriters} property is {@code true}, closes given writer after all attributes are written.
	 * 
	 * @param informationTable information table with attributes to be written to JSON
	 * @param writer writer used to write attributes to JSON
	 * 
	 * @throws IOException when the writer encounters a problem when writing JSON string with attributes
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public void writeAttributes(InformationTable informationTable, Writer writer) throws IOException {
		jsonInformationTableWriter.writeAttributes(informationTable, writer);
		
		if (autoCloseWriters) {
			writer.close(); //release resources
		}
	}
	
	/**
	 * Writes objects from the information table passed as parameter to CSV using the writer passed as parameter.
	 * Uses given delimiter.
	 * If {@code autoCloseWriters} property is {@code true}, closes given writer after all objects are written.
	 * 
	 * @param informationTable information table with objects to be written to CSV
	 * @param writer writer used to write objects to CSV
	 * @param delimiter delimiter for subsequent fields in a row
	 * 
	 * @throws IOException when the writer encounters a problem when writing CSV string with objects
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if given {@code delimiter} is an empty string
	 */
	public void writeObjects(InformationTable informationTable, Writer writer, String delimiter) throws IOException {
		Precondition.notNull(informationTable, "Cannot write to CSV objects of a null information table.");
		Precondition.notNull(writer, "Writer for objects of an information table in CSV format is null.");
		Precondition.notNull(delimiter, "Delimiter for evaluations of objects of an information table in CSV format is null.");
		
		if (delimiter.isEmpty()) {
			throw new InvalidValueException("Delimiter for evaluations of objects of an information table in CSV format is an empty string.");
		}
		
		int numberOfAttrs = informationTable.getNumberOfAttributes();
		int numberOfObjs = informationTable.getNumberOfObjects();
		
		StringBuilder stringBuilder;
		
		for (int i = 0; i < numberOfObjs; i++) {
			stringBuilder = new StringBuilder();
			for (int j = 0; j < numberOfAttrs; j++) {
				stringBuilder.append(informationTable.getField(i, j));
				if (j < numberOfAttrs - 1) { //not the last  attribute
					stringBuilder.append(delimiter);
				} else { //last attribute
					stringBuilder.append("\n");
				}
			}
			writer.write(stringBuilder.toString()); //write one line of text
		}
		
		if (autoCloseWriters) {
			writer.close(); //release resources
		}
	}

	/**
	 * Tells if writers passed as parameters to {@link #writeAttributes(InformationTable, Writer)}
	 * and {@link #writeObjects(InformationTable, Writer, String)} methods
	 * should be automatically closed by these methods to force content flushing and release of blocked resources (like file handles).

	 * @return value of {@code autoCloseWriters} property determining if writers
	 *         used to write attributes and objects should be automatically closed
	 */
	public boolean isAutoCloseWriters() {
		return autoCloseWriters;
	}

	/**
	 * Sets value of {@code autoCloseWriters} property determining if writers
	 * used to write attributes and objects should be automatically closed.
	 *  
	 * @param autoCloseWriters new value of {@code autoCloseWriters} property
	 */
	public void setAutoCloseWriters(boolean autoCloseWriters) {
		this.autoCloseWriters = autoCloseWriters;
	}
}
