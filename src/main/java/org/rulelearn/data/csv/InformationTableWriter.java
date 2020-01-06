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
	 * Constructs this writer. See {@link org.rulelearn.data.json.InformationTableWriter#InformationTableWriter()}.
	 */
	public InformationTableWriter() {
		jsonInformationTableWriter = new org.rulelearn.data.json.InformationTableWriter();
	}
	
	/**
	 * Constructs this writer. See {@link org.rulelearn.data.json.InformationTableWriter#InformationTableWriter(boolean)}.
	 * 
	 * @param setPrettyPrinting indicator of pretty printing in written JSON
	 */
	public InformationTableWriter(boolean setPrettyPrinting) {
		jsonInformationTableWriter = new org.rulelearn.data.json.InformationTableWriter(setPrettyPrinting);
	}
	
	/**
	 * Writes attributes from the information table passed as parameter to JSON using the writer passed as parameter.
	 * Calls {@link org.rulelearn.data.json.InformationTableWriter#writeAttributes(InformationTable, Writer)}.
	 * 
	 * @param informationTable information table with attributes to be written to JSON
	 * @param writer writer used to write attributes to JSON
	 * @throws IOException when the writer encounters a problem when writing JSON string with attributes
	 */
	public void writeAttributes(InformationTable informationTable, Writer writer) throws IOException {
		jsonInformationTableWriter.writeAttributes(informationTable, writer);
	}
	
	/**
	 * Writes objects from the information table passed as parameter to CSV using the writer passed as parameter.
	 * Uses comma (,) as a delimiter.
	 * Closes given writer in the end.
	 * 
	 * @param informationTable information table with objects to be written to CSV
	 * @param writer writer used to write objects to CSV
	 * @param delimiter delimiter for subsequent fields in a row
	 * 
	 * @throws IOException when the writer encounters a problem when writing CSV string with objects
	 */
	public void writeObjects(InformationTable informationTable, Writer writer, String delimiter) throws IOException {
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
			writer.write(stringBuilder.toString()); //write one line
		}
		
		writer.close();
	}
}
