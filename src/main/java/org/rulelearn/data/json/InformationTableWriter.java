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
import java.io.Writer;

import org.rulelearn.data.Attribute;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.IdentificationAttribute;
import org.rulelearn.data.InformationTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Writes {@link Attribute attributes} and objects from an {@link InformationTable information table} to JSON.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableWriter {
	
	/** 
	 * Gson builder.
	 */
	GsonBuilder gsonBuilder;
	
	/**
	 * Gson.
	 */
	Gson gson;
	
	/**
	 * Tells if writers passed as parameters to {@link #writeAttributes(InformationTable, Writer)}
	 * and {@link #writeObjects(InformationTable, Writer)} methods
	 * should be automatically closed by these methods to force content flushing and release of blocked resources (like file handles).
	 * Initially set to {@code true}.
	 */
	boolean autoCloseWriters = true;
	
	/**
	 * Constructs this writer and initializes {@link GsonBuilder Gson builder}, as well as {@link Gson gson} itself.
	 * Turns on pretty printing in produced JSON files.
	 * Ensures that {@code autoCloseWriters} property is set to {@code true}.
	 */
	public InformationTableWriter() {
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(InformationTable.class, new InformationTableSerializer());
		gsonBuilder.registerTypeAdapter(IdentificationAttribute.class, new IdentificationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		gson = gsonBuilder.setPrettyPrinting().create();
		autoCloseWriters = true;
	}
	
	/**
	 * Constructs this writer and initializes {@link GsonBuilder Gson builder} (optionally setting pretty printing), 
	 * as well as {@link Gson gson instance} itself.
	 * Ensures that {@code autoCloseWriters} property is set to {@code true}.
	 * 
	 * @param setPrettyPrinting indicator of pretty printing in written JSON
	 */
	public InformationTableWriter(boolean setPrettyPrinting) {
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(InformationTable.class, new InformationTableSerializer());
		gsonBuilder.registerTypeAdapter(IdentificationAttribute.class, new IdentificationAttributeSerializer());
		gsonBuilder.registerTypeAdapter(EvaluationAttribute.class, new EvaluationAttributeSerializer());
		if (setPrettyPrinting) {
			gson = gsonBuilder.setPrettyPrinting().create();
		}
		else {
			gson = gsonBuilder.create();
		}
		autoCloseWriters = true;
	}
	
	/**
	 * Writes attributes from the information table passed as parameter to JSON using the writer passed as parameter.
	 * If {@code autoCloseWriters} property is {@code true}, closes given writer after all attributes are written.
	 * 
	 * @param informationTable information table with attributes to be written to JSON
	 * @param writer writer used to write attributes to JSON
	 * 
	 * @throws IOException when the writer encounters a problem when writing JSON string with attributes
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public void writeAttributes(InformationTable informationTable, Writer writer) throws IOException {
		notNull(informationTable, "Information table from which attributes are to be written to JSON is null.");
		notNull(writer, "Writer for attributes from information table to JSON is null.");
		
		writer.write(gson.toJson(informationTable.getAttributes()));
		
		if (autoCloseWriters) {
			writer.close(); //release resources
		}
	}
	
	/**
	 * Writes objects from information table passed as parameter to JSON using writer passed as parameter.
	 * If {@code autoCloseWriters} property is {@code true}, closes given writer after all objects are written.
	 * 
	 * @param informationTable information table with objects to be written to JSON
	 * @param writer writer used to write objects to JSON
	 * 
	 * @throws IOException when the writer encounters a problem when writing JSON string with objects
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public void writeObjects(InformationTable informationTable, Writer writer) throws IOException {
		notNull(informationTable, "Information table from which objects are to be written to JSON is null.");
		notNull(writer, "Writer for objects from information table to JSON is null.");
		
		writer.write(gson.toJson(informationTable));
		
		if (autoCloseWriters) {
			writer.close(); //release resources
		}
	}
	
	/**
	 * Tells if writers passed as parameters to {@link #writeAttributes(InformationTable, Writer)}
	 * and {@link #writeObjects(InformationTable, Writer)} methods
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
