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
import static org.rulelearn.core.Precondition.notNullWithContents;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.arff.ArffReader;
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IdentificationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.univocity.parsers.conversions.TrimConversion;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

/**
 * Builder for {@link InformationTable}. Allows building of an information table by setting attributes first, and then iteratively adding objects (rows).
 * This builder optimizes memory footprint of the constructed information table by employing a volatile cache of objects' evaluations. For example,
 * if the same integer number is an evaluation of two different objects, even with respect to two different attributes (but both having {@link IntegerField}
 * {@link EvaluationAttribute#getValueType() value type} and the same {@link AttributePreferenceType preference type}),
 * then a single instance of {@link IntegerField} will be used to store that evaluation. This strategy employs the fact that particular
 * {@link EvaluationField evaluation fields} stored in the information table are read-only.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableBuilder {
	
	/** 
	 * Default string representations of a missing value.
	 */
	protected final static String[] DEFAULT_MISSING_VALUE_STRINGS = {"?", "*", "NA"}; //SIC! null is not allowed on this list and the list itself cannot be null
	
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
	protected String[] missingValueStrings = null;
	
	/**
	 * Separator of values in string representation of an object of an information table.
	 */
	protected String separator = null;
	
	/**
	 * Converter which removes leading and trailing white spaces from an input {@link String}.
	 */
	TrimConversion trimConversion = null;
	
	/**
	 * Parser of evaluations given in textual form, converting them to {@link EvaluationField evaluation fields} based on information about an attribute.
	 */
	EvaluationParser evaluationParser = null;
	
	/**
	 * Build log of this information table builder used for logging effects of calls to methods attempting to add (parse) an object,
	 * like {@link InformationTableBuilder#addObject(String[])}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class BuildLog {
		
		int readObjectsCount;
		int successfullyParsedObjectsCount;
		ObjectList<String>failureMessages;
		
		private BuildLog() {
			readObjectsCount = 0;
			successfullyParsedObjectsCount = 0;
			failureMessages = new ObjectArrayList<String>();
		}
		
		void logSuccess() {
			readObjectsCount++;
			successfullyParsedObjectsCount++;
		}
		
		void logFailure(String failureMessage) {
			readObjectsCount++;
			failureMessages.add(failureMessage);
		}
		
		/**
		 * Gets the number of objects read from an input, i.e.,
		 * the number of objects that were attempted to parse from textual
		 * representation into in-memory representation, strictly corresponding to
		 * considered set of attributes.
		 * 
		 * @return the number of objects read from an input (that were later either successfully or unsuccessfully parsed)
		 */
		public int getReadObjectsCount() {
			return readObjectsCount;
		}
		
		/**
		 * Gets the number of objects successfully parsed from an input, i.e.,
		 * the number of objects that were successfully parsed from textual
		 * representation into in-memory representation, strictly corresponding to
		 * considered set of attributes.
		 * 
		 * @return the number of objects read from an input and then successfully parsed
		 */
		public int getSuccessfullyParsedObjectsCount() {
			return successfullyParsedObjectsCount;
		}
		
		/**
		 * Gets unmodifiable list with messages for all parse errors.
		 * 
		 * @return unmodifiable list with messages for all parse errors
		 */
		public List<String> getFailureMessages() {
			return ObjectLists.unmodifiable(failureMessages);
		}
		
		/**
		 * Tells if all objects parsed from text representation have been successfully parsed into their in-memory representation (array of {@link Field fields}.
		 * 
		 * @return {@code true} if all objects parsed from text representation have been successfully parsed into their in-memory representation,
		 *         {@code false} otherwise
		 */
		public boolean allObjectsSuccessfullyParsed() {
			return readObjectsCount == successfullyParsedObjectsCount;
		}
	}
	
	/**
	 * Build log of this information table builder used for logging effects of calls to methods attempting to add an object, like {@link InformationTableBuilder#addObject(String[])}.
	 */
	BuildLog buildLog;
	
	/**
	 * Tells if {@link ObjectParseException} exception should be thrown when any object cannot be successfully parsed from text representation (as some of its values cannot be parsed).
	 * If {@code false}, then the exception is not thrown but a message is logged in the system console and the list of parsed objects does not change.
	 * Regardless of this setting, {@link BuildLog build log} is maintained and updated after each attempt to parse an object.<br>
	 * <br>
	 * Initialized with {@code true}. Once modified, affects only subsequent attempts to parse an object.
	 */
	public boolean exceptionOnObjectParseError = true;

	/**
	 * Default constructor initializing this information table builder.
	 */
	InformationTableBuilder() {
		this.separator = InformationTableBuilder.DEFAULT_SEPARATOR_STRING;
		this.missingValueStrings = InformationTableBuilder.DEFAULT_MISSING_VALUE_STRINGS;
		this.trimConversion = new TrimConversion();
		this.evaluationParser = new EvaluationParser(InformationTableBuilder.DEFAULT_MISSING_VALUE_STRINGS, EvaluationParser.CachingType.VOLATILE); //use caching factory, and force volatile cache
		this.buildLog = new BuildLog();
		exceptionOnObjectParseError = true;
	}
	
	/**
	 * Constructor initializing this information table builder and setting attributes.
	 * 
	 * @param attributes table with attributes
	 * @throws NullPointerException if all or some of the attributes of the constructed information table have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes) { //TODO: use accelerateByReadOnlyParams
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
	public InformationTableBuilder(Attribute[] attributes, String separator) { //TODO: use accelerateByReadOnlyParams
		this(attributes);
		notNull(separator, "Separator is null.");
		this.separator = separator;
	}
	
	/**
	 * Constructor initializing this information table builder, setting attributes, and missing values strings (i.e., array of strings representing missing values).
	 * 
	 * @param attributes table with attributes
	 * @param missingValueStrings array of string representations of missing values
	 * 
	 * @throws NullPointerException if all or some of attributes of the constructed information table, and/or strings representing missing values have not been set 
	 */
	public InformationTableBuilder(Attribute[] attributes, String[] missingValueStrings) { //TODO: use accelerateByReadOnlyParams
		this(attributes);
		//asserts all missing value representations are not null
		this.missingValueStrings = notNullWithContents(missingValueStrings, "Missing value strings array is null.", "Missing value string is null at index %i.");
		this.evaluationParser.setMissingValueStrings(missingValueStrings); //update missing value strings
	}
	
	/**
	 * Constructor initializing this information table builder, setting attributes, separator, and missing values strings (i.e., array of strings representing missing values).
	 * 
	 * @param attributes table with attributes
	 * @param separator separator of object's evaluations
	 * @param missingValueStrings array of string representations of missing values
	 * @throws NullPointerException if all or some of attributes of the constructed information table, and/or separator, and/or strings representing missing values have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes, String separator, String[] missingValueStrings) { //TODO: use accelerateByReadOnlyParams
		this(attributes, separator);
		//asserts all missing value representations are not null
		this.missingValueStrings = notNullWithContents(missingValueStrings, "Missing value strings array is null.", "Missing value string is null at index %i.");
		this.evaluationParser.setMissingValueStrings(missingValueStrings); //update missing value strings
	}
	
	/**
	 * Adds one object to this builder, parsing its subsequent values from text. 
	 * Given string is considered to contain subsequent identifiers/evaluations of a single object, separated by the given separator.
	 * 
	 * @param objectDescriptions string with object's identifiers/evaluations, separated by the separator declared when constructing this builder
	 * 
	 * @see #addObject(String[])
	 * 
	 * @throws NullPointerException if given parameter is {@code null}
	 * @throws IndexOutOfBoundsException if object has different number of descriptions than the number of attributes declared
	 * @throws ObjectParseException if {@link #exceptionOnObjectParseError} is {@code true} and it is impossible to construct an object
	 *         (array of {@link Field fields}) from given object descriptions,
	 *         as at least one textual value cannot be parsed as a correct value of the respective {@link Attribute attribute}
	 */
	public void addObject(String objectDescriptions) {
		Precondition.notNull(objectDescriptions, "Object descriptions are null.");
		if (this.separator != null) {
			addObject(objectDescriptions.split(this.separator));
		} else {
			throw new NullPointerException("Separator used to parse object descriptions is null."); //this should not happen as the separator is validated in constructors
		}
	}
	
	/**
	 * Adds one object to this builder, parsing its subsequent values from text. 
	 * Given string is considered to contain subsequent identifiers/evaluations of a single object, separated by the given separator.
	 * 
	 * @param objectDescriptions string with object's identifiers/evaluations, separated by the given separator
	 * @param separator separator of object's evaluations
	 * 
	 * @see #addObject(String[])
	 * 
	 * @throws IndexOutOfBoundsException if object has different number of descriptions than the number of attributes declared
	 * @throws ObjectParseException if {@link #exceptionOnObjectParseError} is {@code true} and it is impossible to construct an object
	 *         (array of {@link Field fields}) from given object descriptions,
	 *         as at least one textual value cannot be parsed as a correct value of the respective {@link Attribute attribute}
	 * @throws NullPointerException if any of the given parameters are {@code null}
	 */
	public void addObject(String objectDescriptions, String separator) {
		Precondition.notNull(objectDescriptions, "Object descriptions are null.");
		if (separator != null) {
			addObject(objectDescriptions.split(separator));
		} else {
			throw new NullPointerException("Separator used to parse object descriptions is null.");
		}
	}
	
	/**
	 * Adds one object to this builder, parsing its subsequent values from text.
	 * Given array is considered to contain subsequent identifiers/evaluations of a single object.
	 * 
	 * @param objectDescriptions single object's identifiers/evaluations
	 * 
	 * @throws NullPointerException if given parameter is {@code null}
	 * @throws IndexOutOfBoundsException if object has different number of descriptions than the number of attributes declared
	 * @throws ObjectParseException if {@link #exceptionOnObjectParseError} is {@code true} and it is impossible to construct an object
	 *         (array of {@link Field fields}) from given array of textual values,
	 *         as at least one textual value cannot be parsed as a correct value of the respective {@link Attribute attribute}
	 */
	public void addObject(String[] objectDescriptions) {
		Precondition.notNull(objectDescriptions, "Object descriptions are null.");
		if (objectDescriptions.length != attributes.length) {
			throw new IndexOutOfBoundsException("Object has different number of descriptions than the number of attributes declared.");
		}
		
		boolean allValuesParsedSuccessfully = true;
		
		Field[] object = new Field[this.attributes.length];
		for (int i = 0; i < this.attributes.length; i++) {
			if (this.attributes[i] instanceof EvaluationAttribute) {
				try {
					object[i] = parseEvaluation(objectDescriptions[i], (EvaluationAttribute)this.attributes[i]);
				} catch (FieldParseException exception) {
					String failureMessage = "Error while parsing object's value. " + exception.toString();
					buildLog.logFailure(failureMessage);
					if (exceptionOnObjectParseError) {
						throw new ObjectParseException(failureMessage);
					} else {
						System.out.println(failureMessage);
						allValuesParsedSuccessfully = false;
						break; //skip subsequent attributes
					}
				}
			}
			else if (this.attributes[i] instanceof IdentificationAttribute) {
				try {
					object[i] = parseIdentification(objectDescriptions[i], (IdentificationAttribute)this.attributes[i]);
				} catch (FieldParseException exception) {
					String failureMessage = "Error while parsing object's value. " + exception.toString();
					buildLog.logFailure(failureMessage);
					if (exceptionOnObjectParseError) {
						throw new ObjectParseException(failureMessage);
					} else {
						System.out.println(failureMessage);
						allValuesParsedSuccessfully = false;
						break; //skip subsequent attributes
					}
				}
			}
		}
		
		if (allValuesParsedSuccessfully) {
			this.fields.add(object);
			buildLog.logSuccess();
		}
	}
	
	/**
	 * Parses one object's evaluation and transforms it into an {@link EvaluationField evaluation field}.
	 * See {@link EvaluationParser#parseEvaluation(String, EvaluationAttribute)}.
	 * 
	 * @param evaluation text-encoded object's evaluation
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed field
	 * 
	 * @throws FieldParseException if given string cannot be parsed as a value of the given attribute
	 */
	protected EvaluationField parseEvaluation(String evaluation, EvaluationAttribute attribute) {
		return this.evaluationParser.parseEvaluation(evaluation, attribute);
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
				throw new FieldParseException(new StringBuilder("Cannot parse text ").append(identification)
						.append(" as an UUID for attribute ").append(attribute.getName()).append(".").toString());
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
	 * 
	 * @throws NullPointerException if path to JSON file and/or path to CSV file have not been set
	 * @throws IOException when there is problem with handling JSON file and/or CSV file
	 * @throws FileNotFoundException when JSON file and/or CSV file cannot be found
	 * @throws UnsupportedEncodingException when encoding of CSV file is not supported
	 * @throws ObjectParseException if at least one of the objects can't be parsed from the CSV file
	 */
	public static InformationTable safelyBuildFromCSVFile(String pathToJSONAttributeFile, String pathToCSVObjectFile, boolean header, char separator) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		notNull(pathToJSONAttributeFile, "Path to JSON file with attributes is null.");
		notNull(pathToCSVObjectFile, "Path to CSV file with objects is null.");
		
		Attribute[] attributes = null;
		List<String[]> objects = null;
		InformationTableBuilder informationTableBuilder = null;
		InformationTable informationTable = null;
		
		//load attributes
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try (JsonReader jsonReader = new JsonReader(new FileReader(pathToJSONAttributeFile))) {
			attributes = gson.fromJson(jsonReader, Attribute[].class);
			
			//construct information table builder
			if (attributes != null) {
				//TODO: the code below is the same as in ObjectParser.parseObjects(Reader) ...
				
				//load objects 
				org.rulelearn.data.csv.ObjectBuilder ob = new org.rulelearn.data.csv.ObjectBuilder.Builder().attributes(attributes).header(header).separator(separator).build();
				objects = ob.getObjects(pathToCSVObjectFile);
				informationTableBuilder = new InformationTableBuilder(attributes, new String[] {org.rulelearn.data.csv.ObjectBuilder.DEFAULT_MISSING_VALUE_STRING});
				
				if (objects != null) {
					for (int i = 0; i < objects.size(); i++) {
						try {
							informationTableBuilder.addObject(objects.get(i)); //uses volatile caches
						} catch (ObjectParseException exception) {
							throw new ObjectParseException(new StringBuilder("Error while parsing object no. ").append(i+1).append(" from CSV. ").append(exception.toString()).toString()); //if exception was thrown, re-throw it
						}
					}
				}
			}
		}
		
		//build information table
		if (informationTableBuilder != null) {
			//clear volatile caches of all used evaluation field caching factories
			informationTableBuilder.clearVolatileCaches();
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
	 * 
	 * @return constructed information table
	 * 
	 * @throws NullPointerException if path to any JSON file has not been set
	 * @throws IOException when there is problem with handling JSON file
	 * @throws FileNotFoundException when JSON file cannot be found
	 * @throws ObjectParseException if at least one of the objects can't be parsed from the JSON file
	 */
	public static InformationTable safelyBuildFromJSONFile(String pathToJSONAttributeFile, String pathToJSONObjectFile) throws IOException, FileNotFoundException {
		notNull(pathToJSONAttributeFile, "Path to JSON file with attributes is null.");
		notNull(pathToJSONObjectFile, "Path to JSON file with objects is null.");
		
		Attribute [] attributes = null;
		List<String[]> objects = null;
		InformationTableBuilder informationTableBuilder = null;
		InformationTable informationTable = null;
		
		// load attributes
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try (JsonReader jsonAttributesReader = new JsonReader(new FileReader(pathToJSONAttributeFile))) {
			attributes = gson.fromJson(jsonAttributesReader, Attribute[].class);
			// load objects
			JsonElement json = null;
			try (JsonReader jsonObjectsReader = new JsonReader(new FileReader(pathToJSONObjectFile))) {
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
						try {
							informationTableBuilder.addObject(objects.get(i)); //uses volatile cache
						} catch (ObjectParseException exception) {
							throw new ObjectParseException(new StringBuilder("Error while parsing object no. ").append(i+1).append(" from JSON. ").append(exception.toString()).toString()); //if exception was thrown, re-throw it
						}
					}
				}
			}
		}
		
		// build information table
		if (informationTableBuilder != null) {
			//clear volatile caches of all used evaluation field caching factories
			informationTableBuilder.clearVolatileCaches();
			informationTable = informationTableBuilder.build();
		}

		return informationTable;
	}
	
	/**
	 * Reads attributes and objects from a text file in WEKA's ARFF format and returns corresponding {@link InformationTable information table}.
	 * The last attribute will be the decision one, with preference type set to {@code decisionAttributePreferenceType}.<br>
	 * <br>
	 * Warning! This method may have limitations as to supported ARFF syntax - please refer to {@link ArffReader#read(String, AttributePreferenceType)} for details.
	 * 
	 * @param arffFilePath path to a file (expected to be in ARFF format)
	 * @param decisionAttributePreferenceType attribute preference type to be set for the decision attribute read from the ARFF file.
	 *        The option of setting value other than the default value {@link AttributePreferenceType#NONE} is useful, e.g., when reading ordinal classification problems.
	 *
	 * @return information table containing data stored in the processed ARFF file
	 * 
	 * @throws NullPointerException see {@link ArffReader#read(String, AttributePreferenceType)}
	 * @throws FileNotFoundException see {@link ArffReader#read(String, AttributePreferenceType)}
	 * @throws InvalidValueException see {@link ArffReader#read(String, AttributePreferenceType)}
	 * @throws UnsupportedOperationException see {@link ArffReader#read(String, AttributePreferenceType)}
	 * @throws ObjectParseException see {@link ArffReader#read(String, AttributePreferenceType)}
	 */
	public static InformationTable safelyBuildFromARFFFile(String arffFilePath, AttributePreferenceType decisionAttributePreferenceType) throws IOException, FileNotFoundException {
		return (new ArffReader()).read(arffFilePath, decisionAttributePreferenceType);
	}
	
	/**
	 * Clears volatile caches of all evaluation field caching factories used by this builder
	 * when parsing strings representing objects' evaluations into {@link EvaluationField evaluation fields}.
	 */
	public void clearVolatileCaches() {
		//clear volatile caches of all used evaluation field caching factories
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] instanceof EvaluationAttribute) {
				((EvaluationAttribute)attributes[i]).getValueType().getCachingFactory().clearVolatileCache(); //clears volatile cache of the caching factory corresponding to current evaluation attribute
			}
		}
	}
	
	/**
	 * Gets missing value strings (cloned array).
	 * 
	 * @return the missingValueStrings
	 */
	public String[] getMissingValueStrings() {
		return this.missingValueStrings.clone();
	}
	
	/**
	 * Gets separator string.
	 * 
	 * @return the separator
	 */
	public String getSeparator() {
		return this.separator;
	}

	/**
	 * Gets build log of this information table builder used for logging effects of calls to methods attempting to add an object, like {@link InformationTableBuilder#addObject(String[])}.
	 * 
	 * @return build log of this information table
	 */
	public BuildLog getBuildLog() {
		return buildLog;
	}
	
}
