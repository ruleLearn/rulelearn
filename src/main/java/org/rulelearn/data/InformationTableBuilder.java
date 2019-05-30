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

package org.rulelearn.data;

import static org.rulelearn.core.Precondition.notNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.rulelearn.core.FieldParseException;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IdentificationField;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.univocity.parsers.conversions.TrimConversion;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Builder for {@link InformationTable}. Allows building of an information table by setting attributes first, and then iteratively adding objects (rows).
 * Attributes are set using a JSON string. Objects are set using CSV strings.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableBuilder {
	
	/** 
	 * Default string representations of missing value.
	 */
	protected final static String [] DEFAULT_MISSING_VALUE_STRINGS = {"?", "*", "NA"};
	
	/** 
	 * Default string representation of separator.
	 */
	protected final static String DEFAULT_SEPARATOR_STRING = ",";
	
	/**
	 * All attributes of the constructed information table.
	 */
	protected Attribute[] attributes = null;
	
	/**
	 * List of fields of subsequent objects of constructed information table; each array contains subsequent fields of a single object (row) in the information table.
	 */
	protected List<Field[]> fields = null;
	
	/**
	 * Array of string representations of missing values.
	 */
	protected String [] missingValueStrings = null;
	
	/**
	 * Separator of values in string representation of an object of an information table.
	 */
	protected String separator = null;
	
	/**
	 * Converter which removes leading and trailing white spaces from an input {@link String}.
	 */
	TrimConversion trimConversion = null;
	
	/**
	 * Default constructor initializing this information table builder.
	 */
	protected InformationTableBuilder() {
		this.separator = InformationTableBuilder.DEFAULT_SEPARATOR_STRING;
		this.missingValueStrings = InformationTableBuilder.DEFAULT_MISSING_VALUE_STRINGS;
		this.trimConversion = new TrimConversion();
	}
	
	/**
	 * Constructor initializing this information table builder and setting attributes.
	 * 
	 * @param attributes table with attributes
	 * @throws NullPointerException if all or some of the attributes of the constructed information table have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes) {
		this();
		// check attributes and initialize fields
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				if (attribute == null) throw new NullPointerException("At least one attribute is not set.");
			}
			this.attributes = attributes;
			this.fields = new ObjectArrayList<Field []>();
		}
		else {
			throw new NullPointerException("Attributes are not set.");
		}
	}
	
	/**
	 * Constructor initializing this information table builder and setting attributes.
	 * 
	 * @param attributes table with attributes
	 * @param separator separator of object's evaluations
	 * @throws NullPointerException if all or some of attributes of the constructed information table, and/or separator have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes, String separator) {
		this(attributes);
		notNull(separator, "Separator is null.");
		this.separator = separator;
	}
	
	/**
	 * Constructor initializing this information table builder, setting attributes, and missing values strings (i.e., array of strings representing missing values).
	 * 
	 * @param attributes table with attributes
	 * @param missingValueStrings array of string representations of missing values
	 * @throws NullPointerException if all or some of attributes of the constructed information table, and/or strings representing missing values have not been set 
	 */
	public InformationTableBuilder(Attribute[] attributes, String[] missingValueStrings) {
		this(attributes);
		notNull(missingValueStrings, "Missing value strings array is null.");
		this.missingValueStrings = missingValueStrings;
	}
	
	/**
	 * Constructor initializing this information table builder, setting attributes, separator, and missing values strings (i.e., array of strings representing missing values).
	 * 
	 * @param attributes table with attributes
	 * @param separator separator of object's evaluations
	 * @param missingValueStrings array of string representations of missing values
	 * @throws NullPointerException if all or some of attributes of the constructed information table, and/or sparator, and/or strings representing missing values have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes, String separator, String[] missingValueStrings) {
		this(attributes, separator);
		notNull(missingValueStrings, "Missing value strings array is null.");
		this.missingValueStrings = missingValueStrings;
	}
	
	/**
	 * Adds one object to this builder. 
	 * Given string is considered to contain subsequent identifiers/evaluations of a single object, separated by the given separator.
	 * 
	 * @param objectDescriptions string with object's identifiers/evaluations, separated by the given separator
	 */
	public void addObject(String objectDescriptions) {
		if (this.separator != null)
			addObject(objectDescriptions.split(this.separator));
	}
	
	/**
	 * Adds one object to this builder. 
	 * Given string is considered to contain subsequent identifiers/evaluations of a single object, separated by the given separator.
	 * 
	 * @param objectDescriptions string with object's identifiers/evaluations, separated by the given separator
	 * @param separator separator of object's evaluations
	 */
	public void addObject(String objectDescriptions, String separator) {
		if (separator != null) {
			addObject(objectDescriptions.split(separator));
		}
	}
	
	/**
	 * Adds one object to this builder. 
	 * Given array is considered to contain subsequent identifiers/evaluations of a single object.
	 * 
	 * @param objectDescriptions single object's identifiers/evaluations
	 * 
	 * @throws IndexOutOfBoundsException if given attribute index does not correspond to any attribute of the constructed information table
	 */
	public void addObject(String[] objectDescriptions) {
		if (objectDescriptions.length != attributes.length)
			throw new IndexOutOfBoundsException("Object has different number of evaluations than the number of attributes declared.");
		
		Field[] object = new Field[this.attributes.length];
		for (int i = 0; i < this.attributes.length; i++) {
			if (this.attributes[i] instanceof EvaluationAttribute) {
				object[i] = parseEvaluation(objectDescriptions[i], (EvaluationAttribute)this.attributes[i]);
			}
			else if (this.attributes[i] instanceof IdentificationAttribute) {
				object[i] = parseIdentification(objectDescriptions[i], (IdentificationAttribute)this.attributes[i]);
			}
		}
		
		this.fields.add(object);
	}
	
	/**
	 * Parses one object's evaluation and transforms it into an {@link EvaluationField evaluation field}.
	 * 
	 * @param evaluation text-encoded object's evaluation
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed field
	 * 
	 * @throws FieldParseException if given string cannot be parsed as a value of the given attribute
	 * @throws IndexOutOfBoundsException if evaluation of an enumeration attribute can't be parsed  
	 */
	protected EvaluationField parseEvaluation(String evaluation, EvaluationAttribute attribute) throws NumberFormatException, IndexOutOfBoundsException {
		EvaluationField field = null;
		boolean missingValue = false;
	
		// get rid of white spaces
		evaluation = trimConversion.execute(evaluation);
		
		//TODO: recognize text representation of unknown values of all types of evaluation fields (see EvaluationField.getUnknownEvaluation method),
		//especially for composite fields; e.g., for a pair field, one could expect something like "(?,?)"
		
		// check whether it is a missing value
		if (attribute.getValueType() instanceof SimpleField && missingValueStrings != null) { //single missing value is of interest only for a simple field
			for (String missingValueString : this.missingValueStrings) {
				if ((missingValueString != null) && (missingValueString.equalsIgnoreCase(evaluation))) {
					missingValue = true;
					break;
				}
			}
		}
		
		if (!missingValue) { //simple field without missing value or not a simple field
			try {
				field = attribute.getValueType().getCachingFactory().createWithVolatileCache(evaluation, attribute); //MSz
			}
			catch (FieldParseException exception) {
				throw exception;
			}
			
//			if (valueType instanceof IntegerField) {
//				try {
//					field = IntegerFieldFactory.getInstance().create(Integer.parseInt(evaluation), attribute.getPreferenceType());
//				}
//				catch (NumberFormatException ex) {
//					// just assign a reference (no new copy of missing value field is made)
//					field = attribute.getMissingValueType();
//					throw new NumberFormatException(ex.getMessage());
//				}
//			}
//			else if (valueType instanceof RealField) {
//				try {
//					field = RealFieldFactory.getInstance().create(Double.parseDouble(evaluation), attribute.getPreferenceType());
//				}
//				catch (NumberFormatException ex) {
//					// just assign a reference (no new copy of missing value field is made)
//					field = attribute.getMissingValueType();
//					throw new NumberFormatException(ex.getMessage());
//				}
//			}
//			else if (valueType instanceof EnumerationField) {
//				int index = ((EnumerationField)valueType).getElementList().getIndex(evaluation);
//				if (index != ElementList.DEFAULT_INDEX) {
//					field = EnumerationFieldFactory.getInstance().create(((EnumerationField)valueType).getElementList(), index, attribute.getPreferenceType());
//				}
//				else {
//					field = attribute.getMissingValueType();
//					throw new IndexOutOfBoundsException(new StringBuilder("Incorrect value of enumeration: ").append(evaluation).append(" was replaced by a missing value.").toString());
//				}
//					
//			}
//			else {
//				// just assign a reference (no new copy of missing value field is made) 
//				field = attribute.getMissingValueType();
//			}
		}
		else {
			//field = attribute.getMissingValueType(); //does not work for a pair field...
			field = attribute.getValueType().getUnknownEvaluation(attribute.getMissingValueType());
		}
		
		return field;
	}
	
	/**
	 * Parses one object's identification and transforms it into an {@link IdentificationField identification field}.
	 * 
	 * @param identification text-encoded object's identification
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed field
	 * 
	 * @throws FieldParseException if given string cannot be parsed as a value of given identification attribute;
	 *         this exception concerns only attributes with {@link UUIDIdentificationField} value type 
	 */
	protected IdentificationField parseIdentification(String identification, IdentificationAttribute attribute) {
		IdentificationField field = null;
		boolean missingValue = false;

		// get rid of white spaces
		identification = trimConversion.execute(identification);
		
		// check whether it is a missing value
		if (missingValueStrings != null) {
			for (String missingValueString : this.missingValueStrings) {
				if ((missingValueString != null) && (missingValueString.equalsIgnoreCase(identification))) {
					missingValue = true;
					break;
				}
			}
		}
		
		// assign according to value type
		if (attribute.getValueType() instanceof UUIDIdentificationField) {
			try {
				field = (missingValue ?
						new UUIDIdentificationField(UUIDIdentificationField.DEFAULT_VALUE) : new UUIDIdentificationField(UUID.fromString(identification)));
			} catch (IllegalArgumentException exception) {
				throw new FieldParseException("Cannot parse given string as an UUID.");
			}
		}
		else {
			field = (missingValue ?
					new TextIdentificationField(TextIdentificationField.DEFAULT_VALUE) : new TextIdentificationField(identification));
		}
		
		return field;
	}
	
	/**
	 * Builds information table.
	 * 
	 * @return constructed information table
	 */
	public InformationTable build() {
		return new InformationTable(attributes, fields);
	}
	
	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored in CSV format.
	 * Internally it uses {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean, char)}.
	 * 
	 * No header in parsed CSV file, and default separator {@link org.rulelearn.data.csv.ObjectBuilder#DEFAULT_SEPARATOR} is assumed.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToCSVObjectFile a path to the CSV file with objects
	 * 
	 * @return constructed information table or {@code null} value provided that it was not possible to construct the table
	 * @throws NullPointerException if path to JSON file and/or path to CSV file have not been set
	 * @throws IOException when there is problem with handling JSON file and/or CSV file
	 * @throws FileNotFoundException when JSON file and/or CSV file cannot be found
	 * @throws UnsupportedEncodingException when encoding of CSV file is not supported
	 */
	public static InformationTable buildFromCSVFile(String pathToJSONAttributeFile, String pathToCSVObjectFile) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		return safelyBuildFromCSVFile(pathToJSONAttributeFile, pathToCSVObjectFile, false, org.rulelearn.data.csv.ObjectBuilder.DEFAULT_SEPARATOR);
	}
	
	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored in CSV format.
	 * Internally it uses {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean, char)}.
	 * 
	 * No header in parsed CSV file is assumed.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToCSVObjectFile a path to the CSV file with objects
	 * @param separator representation of a separator of fields in CSV file
	 * 
	 * @return constructed information table or {@code null} value provided that it was not possible to construct the table
	 * @throws NullPointerException if path to JSON file and/or path to CSV file have not been set
	 * @throws IOException when there is problem with handling JSON file and/or CSV file
	 * @throws FileNotFoundException when JSON file and/or CSV file cannot be found
	 * @throws UnsupportedEncodingException when encoding of CSV file is not supported
	 */
	public static InformationTable buildFromCSVFile(String pathToJSONAttributeFile, String pathToCSVObjectFile, char separator) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		return safelyBuildFromCSVFile(pathToJSONAttributeFile, pathToCSVObjectFile, false, separator);
	}
	
	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored in CSV format.
	 * Internally it uses {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean, char)}.
	 * 
	 * Default separator {@link org.rulelearn.data.csv.ObjectBuilder#DEFAULT_SEPARATOR} is assumed.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToCSVObjectFile a path to the CSV file with objects
	 * @param header indicates whether header is present in CSV file
	 * 
	 * @return constructed information table or {@code null} value provided that it was not possible to construct the table
	 * @throws NullPointerException if path to JSON file and/or path to CSV file have not been set
	 * @throws IOException when there is problem with handling JSON file and/or CSV file
	 * @throws FileNotFoundException when JSON file and/or CSV file cannot be found
	 * @throws UnsupportedEncodingException when encoding of CSV file is not supported
	 */
	public static InformationTable buildFromCSVFile(String pathToJSONAttributeFile, String pathToCSVObjectFile, boolean header) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		return safelyBuildFromCSVFile(pathToJSONAttributeFile, pathToCSVObjectFile, header, org.rulelearn.data.csv.ObjectBuilder.DEFAULT_SEPARATOR);
	}
	
	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored in CSV format.
	 * Internally it uses {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean, char)}.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToCSVObjectFile a path to the CSV file with objects
	 * @param header indicates whether header is present in CSV file
	 * @param separator representation of a separator of fields in CSV file
	 * 
	 * @return constructed information table or {@code null} value provided that it was not possible to construct the table
	 * @throws NullPointerException if path to JSON file and/or path to CSV file have not been set
	 * @throws IOException when there is problem with handling JSON file and/or CSV file
	 * @throws FileNotFoundException when JSON file and/or CSV file cannot be found
	 * @throws UnsupportedEncodingException when encoding of CSV file is not supported
	 */
	public static InformationTable buildFromCSVFile(String pathToJSONAttributeFile, String pathToCSVObjectFile, boolean header, char separator) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		return safelyBuildFromCSVFile(pathToJSONAttributeFile, pathToCSVObjectFile, header, separator);
	}
	
	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored in CSV format.
	 * Internally it uses {@link InformationTableBuilder#safelyBuildFromCSVFile(String, String, boolean, char)}.
	 * 
	 * Default separator {@link org.rulelearn.data.csv.ObjectBuilder#DEFAULT_SEPARATOR} is assumed.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToCSVObjectFile a path to the CSV file with objects
	 * @param header indicates whether header is present in CSV file
	 * 
	 * @return constructed information table or {@code null} value provided that it was not possible to construct the table
	 * @throws NullPointerException if path to JSON file and/or path to CSV file have not been set
	 * @throws IOException when there is problem with handling JSON file and/or CSV file
	 * @throws FileNotFoundException when JSON file and/or CSV file cannot be found
	 * @throws UnsupportedEncodingException when encoding of CSV file is not supported
	 */
	public static InformationTable safelyBuildFromCSVFile(String pathToJSONAttributeFile, String pathToCSVObjectFile, boolean header) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		return safelyBuildFromCSVFile(pathToJSONAttributeFile, pathToCSVObjectFile, header, org.rulelearn.data.csv.ObjectBuilder.DEFAULT_SEPARATOR);
	}
	
	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored in CSV format.
	 * Internally it uses attribute deserializer {@link AttributeDeserializer} to load attributes and object builder {@link org.rulelearn.data.csv.ObjectBuilder} to load objects.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToCSVObjectFile a path to the CSV file with objects
	 * @param header indicates whether header is present in CSV file
	 * @param separator representation of a separator of fields in CSV file
	 * 
	 * @return constructed information table or {@code null} value provided that it was not possible to construct the table 
	 * @throws NullPointerException if path to JSON file and/or path to CSV file have not been set
	 * @throws IOException when there is problem with handling JSON file and/or CSV file
	 * @throws FileNotFoundException when JSON file and/or CSV file cannot be found
	 * @throws UnsupportedEncodingException when encoding of CSV file is not supported
	 */
	public static InformationTable safelyBuildFromCSVFile(String pathToJSONAttributeFile, String pathToCSVObjectFile, boolean header, char separator) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		notNull(pathToJSONAttributeFile, "Path to JSON file with attributes is null.");
		notNull(pathToCSVObjectFile, "Path to CSV file with objects is null.");
		
		Attribute [] attributes = null;
		List<String []> objects = null;
		InformationTableBuilder informationTableBuilder = null;
		InformationTable informationTable = null;
		
		// load attributes
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try (JsonReader jsonReader = new JsonReader(new FileReader(pathToJSONAttributeFile))) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
			
			// construct information table builder
			if (attributes != null) {
				// load objects 
				org.rulelearn.data.csv.ObjectBuilder ob = new org.rulelearn.data.csv.ObjectBuilder.Builder().attributes(attributes).header(header).separator(separator).build();
				objects = ob.getObjects(pathToCSVObjectFile);
				informationTableBuilder = new InformationTableBuilder(attributes, new String[] {org.rulelearn.data.csv.ObjectBuilder.DEFAULT_MISSING_VALUE_STRING});
				if (objects != null) {
					for (int i = 0; i < objects.size(); i++) {
						informationTableBuilder.addObject(objects.get(i)); //uses volatile caches
					}
					//clear volatile caches of all used evaluation field caching factories
					for (int i = 0; i < informationTableBuilder.attributes.length; i++) {
						if (informationTableBuilder.attributes[i] instanceof EvaluationAttribute) {
							((EvaluationAttribute)informationTableBuilder.attributes[i]).getValueType().getCachingFactory().clearVolatileCache();
						}
					}
				}
			}
		}
		
		// build information table
		if (informationTableBuilder != null) {
			informationTable = informationTableBuilder.build();
		}
		
		return informationTable;
	}

	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored also in JSON format.
	 * Internally it uses {@link InformationTableBuilder#safelyBuildFromJSONFile(String, String)}.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToJSONObjectFile a path to the JSON file with objects
	 * @throws NullPointerException if path to any JSON file has not been set
	 * @throws IOException when there is problem with handling JSON file
	 * @throws FileNotFoundException when JSON file cannot be found
	 * 
	 * @return constructed information table
	 */
	public static InformationTable buildFromJSONFile(String pathToJSONAttributeFile, String pathToJSONObjectFile) throws IOException, FileNotFoundException {
		return safelyBuildFromJSONFile(pathToJSONAttributeFile, pathToJSONObjectFile);
	}
	
	/**
	 * Builds information table on the base of file with JSON specification of attributes {@link Attribute} and file with objects stored also in JSON format.
	 * Internally it uses attribute deserializer {@link AttributeDeserializer} to load attributes and object builder {@link org.rulelearn.data.json.ObjectBuilder} to load objects.
	 * 
	 * @param pathToJSONAttributeFile a path to JSON file with attributes
	 * @param pathToJSONObjectFile a path to the JSON file with objects
	 * @return constructed information table
	 * @throws NullPointerException if path to any JSON file has not been set
	 * @throws IOException when there is problem with handling JSON file
	 * @throws FileNotFoundException when JSON file cannot be found
	 */
	public static InformationTable safelyBuildFromJSONFile(String pathToJSONAttributeFile, String pathToJSONObjectFile) throws IOException, FileNotFoundException {
		notNull(pathToJSONAttributeFile, "Path to JSON file with attributes is null.");
		notNull(pathToJSONObjectFile, "Path to JSON file with objects is null.");
		
		Attribute [] attributes = null;
		List<String []> objects = null;
		InformationTableBuilder informationTableBuilder = null;
		InformationTable informationTable = null;
		
		// load attributes
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try(JsonReader jsonAttributesReader = new JsonReader(new FileReader(pathToJSONAttributeFile))) {
			attributes = gson.fromJson(jsonAttributesReader, Attribute[].class);
			// load objects
			JsonElement json = null;
			try(JsonReader jsonObjectsReader = new JsonReader(new FileReader(pathToJSONObjectFile))) {
				JsonParser jsonParser = new JsonParser();
				json = jsonParser.parse(jsonObjectsReader);
			}
			org.rulelearn.data.json.ObjectBuilder ob = new org.rulelearn.data.json.ObjectBuilder.Builder(attributes).build();
			objects = ob.getObjects(json);
			
			// construct information table builder
			if (attributes != null) {
				informationTableBuilder = new InformationTableBuilder(attributes, new String[] {org.rulelearn.data.json.ObjectBuilder.DEFAULT_MISSING_VALUE_STRING});
				if (objects != null) {
					for (int i = 0; i < objects.size(); i++) {
						informationTableBuilder.addObject(objects.get(i)); //uses volatile cache
					}
					//clear volatile caches of all used evaluation field caching factories
					for (int i = 0; i < informationTableBuilder.attributes.length; i++) {
						if (informationTableBuilder.attributes[i] instanceof EvaluationAttribute) {
							((EvaluationAttribute)informationTableBuilder.attributes[i]).getValueType().getCachingFactory().clearVolatileCache();
						}
					}
				}
			}
		}
		
		// build information table
		if (informationTableBuilder != null) {
			informationTable = informationTableBuilder.build();
		}

		return informationTable;
	}
	
	/**
	 * Gets missing value strings.
	 * 
	 * @return the missingValueStrings
	 */
	public String[] getMissingValueStrings() {
		return this.missingValueStrings;
	}
	
	/**
	 * Gets separator string.
	 * 
	 * @return the separator
	 */
	public String getSeparatorStrings() {
		return this.separator;
	}
	
}
