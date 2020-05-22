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

package org.rulelearn.data.arff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.Attribute;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.AttributeType;
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.data.InformationTableBuilder;
import org.rulelearn.data.ObjectParseException;
import org.rulelearn.data.json.InformationTableWriter;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Reader of information present in WEKA's *.arff files.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ArffReader {
	
	/**
	 * Attribute represented only by name and value type. These informations are useful to construct a {@link Attribute regular attribute}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class MinimalAttribute {
		private String name;
		private EvaluationField valueType;
		
		private MinimalAttribute(String name, EvaluationField valueType) {
			this.name = name;
			this.valueType = valueType;
		}
	}
	
	/**
	 * Reads attributes and objects from a text file in WEKA's ARFF format and returns corresponding {@link InformationTable information table}.
	 * The last attribute will be the decision one, with preference type set to {@link AttributePreferenceType#NONE}.<br>
	 * <br>
	 * Warning! This method does not handle quoted attribute names (containing spaces). However, it handles quoted attribute values.<br>
	 * Warning! This method does not handle sparse data notation (of the type: {1 X, 3 Y, 4 "class A"}).
	 * 
	 * @param arffFilePath path to a file (expected to be in ARFF format)
	 * @return information table containing data stored in the processed ARFF file
	 * 
	 * @throws NullPointerException if given ARFF file path is {@code null}
	 * @throws FileNotFoundException if {@code arffFilePath} does not point to an existing disk file
	 * @throws InvalidValueException if type of any attribute found in the processed ARFF file is neither nominal (like {a,b,c,}) nor numeric
	 * @throws UnsupportedOperationException if any attribute present in ARFF file is of nominal type,
	 *         and {@link ElementList#ElementList(String[]) constructor of enumeration's field element list} throws {@link NoSuchAlgorithmException}
	 * @throws UnsupportedOperationException if any line in the @data section section in the ARFF file starts with a curly brace (sparse notation)
	 */
	public InformationTable read(String arffFilePath) throws FileNotFoundException {
		return read(arffFilePath, AttributePreferenceType.NONE);
	}
	
	/**
	 * Reads attributes and objects from a text file in WEKA's ARFF format and returns corresponding {@link InformationTable information table}.
	 * The last attribute will be the decision one, with preference type set to {@code decisionAttributePreferenceType}.<br>
	 * <br>
	 * Warning! This method does not handle quoted attribute names (containing spaces). However, it handles quoted attribute values.<br>
	 * Warning! This method does not handle sparse data notation (of the type: {1 X, 3 Y, 4 "class A"}).
	 * 
	 * @param arffFilePath path to a file (expected to be in ARFF format)
	 * @param decisionAttributePreferenceType attribute preference type to be set for the decision attribute read from the ARFF file.
	 *        The option of setting value other than the default value {@link AttributePreferenceType#NONE} is useful, e.g., when reading ordinal classification problems.
	 * 
	 * @return information table containing data stored in the processed ARFF file
	 * 
	 * @throws NullPointerException if given ARFF file path is {@code null}
	 * @throws NullPointerException if given decision attribute preference type is {@code null}
	 * @throws FileNotFoundException if {@code arffFilePath} does not point to an existing disk file
	 * @throws InvalidValueException if type of any attribute found in the processed ARFF file is neither nominal (like {a,b,c,}) nor numeric
	 * @throws UnsupportedOperationException if any attribute present in ARFF file is of nominal type,
	 *         and {@link ElementList#ElementList(String[]) constructor of enumeration's field element list} throws {@link NoSuchAlgorithmException}
	 * @throws UnsupportedOperationException if any line in the @data section section in the ARFF file starts with a curly brace (sparse notation)
	 * @throws ObjectParseException if at least one of the objects can't be parsed from the ARFF file
	 */
	public InformationTable read(String arffFilePath, AttributePreferenceType decisionAttributePreferenceType) throws FileNotFoundException {
		Precondition.notNull(arffFilePath, "ARFF file path is null.");
		Precondition.notNull(decisionAttributePreferenceType, "Decision attribute preference type is null.");
		
		List<MinimalAttribute> minimalAttributes = new ObjectArrayList<MinimalAttribute>();
		File arffFile = new File(arffFilePath);
		String line;
		String[] tokens;
		String attributeName;
		String attributeDomain;
		String[] elements;
		ElementList elementList;
		EvaluationField valueType;
		AttributePreferenceType conditionAttributePreferenceType = AttributePreferenceType.NONE;
		
		String dataSeparator = "\\s*,\\s*";
		
		InformationTable informationTable = null;
		
		try (Scanner fileScanner = new Scanner(arffFile)) { //ensure scanner releases resources (file handle) if an exception is thrown
			//read attributes
			while (fileScanner.hasNextLine()) {
				line = fileScanner.nextLine().trim();
	
				if (line.startsWith("@relation")) {
					continue; //skip line
				}
				if (line.startsWith("@data")) { //beginning of the data section
					break; //stop reading attributes, and proceed to reading  data
				}
	
				if (line.startsWith("@attribute")) {
					tokens = line.split("\\s+"); //split on white spaces
					//tokens[0] contains "@attribute" - skipped
					attributeName = tokens[1];
	
					if (tokens[2].startsWith("{")) { //enum domain (nominal attribute)
						int openingBraceIndex = line.indexOf("{");
						int closingBraceIndex = line.indexOf("}");
						attributeDomain = line.substring(openingBraceIndex + 1, closingBraceIndex).trim(); //remove braces, spaces after opening brace, and spaces before closing brace
						elements = attributeDomain.split(dataSeparator);
						unquoteElements(elements);
						try {
							elementList = new ElementList(elements);
						} catch (NoSuchAlgorithmException exception) {
							throw new UnsupportedOperationException(exception.getMessage()); //this should not happen
						}
						valueType = EnumerationFieldFactory.getInstance().create(elementList, EnumerationField.DEFAULT_VALUE, conditionAttributePreferenceType);
						minimalAttributes.add(new MinimalAttribute(attributeName, valueType));
					} else {
						if (tokens[2].trim().equals("numeric")) { //numeric (integer or real) attribute
							valueType = RealFieldFactory.getInstance().create(RealField.DEFAULT_VALUE, conditionAttributePreferenceType);
							minimalAttributes.add(new MinimalAttribute(attributeName, valueType));
						} else { //string or date [<date-format>]
							throw new InvalidValueException("Unsupported attribute type in ARFF file.");
						}
					}
				}
			}
			
			//construct regular attributes based on minimal ones
			Attribute[] rlAttributes = new EvaluationAttribute[minimalAttributes.size()];
			int attrIndex = 0;
			
			while (attrIndex < rlAttributes.length - 1) { //all but last attribute are condition ones
				rlAttributes[attrIndex] = new EvaluationAttribute(
						minimalAttributes.get(attrIndex).name, true, AttributeType.CONDITION,
						minimalAttributes.get(attrIndex).valueType, UnknownSimpleFieldMV2.getInstance(), conditionAttributePreferenceType);
				attrIndex++;
			}
			
			rlAttributes[attrIndex] = new EvaluationAttribute( //last attribute is a decision one
					minimalAttributes.get(attrIndex).name, true, AttributeType.DECISION,
					minimalAttributes.get(attrIndex).valueType, UnknownSimpleFieldMV2.getInstance(), decisionAttributePreferenceType); //set requested decision attribute preference type
			
			InformationTableBuilder informationTableBuilder = new InformationTableBuilder(rlAttributes, ",", new String[] {"?"}); //use WEKA's separator (comma) and missing value string (question mark)
			
			//read and parse data rows till the end of ARFF file
			while (fileScanner.hasNextLine()) { //next line can contain object's evaluations separated by commas
				line = fileScanner.nextLine().trim();
				if (line.length() > 0) { //TODO: use more sophisticated check?
					if (line.startsWith("{")) { //sparse representation of data
						throw new UnsupportedOperationException("Sparse data in ARFF file are not supported.");
					}
					elements = line.split(dataSeparator);
					unquoteElements(elements);
					
					try {
						informationTableBuilder.addObject(elements); //uses volatile caches
					} catch (ObjectParseException exception) {
						throw new ObjectParseException(new StringBuilder("Error while parsing object from ARFF. ").append(exception.toString()).toString()); //if exception was thrown, re-throw it
					}
				}
			}
			
			informationTableBuilder.clearVolatileCaches();
			informationTable = informationTableBuilder.build();
			
		} catch (FileNotFoundException exception) {
			throw new FileNotFoundException("Could not find file: "+arffFilePath);
		}
		
		return informationTable; //return information table built for attributes and objects read from the ARFF file
	}
	
	private void unquoteElements(String[] elements) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].startsWith("\"") && elements[i].endsWith("\"")) {
				elements[i] = elements[i].substring(1, elements[i].length() - 1); //remove double quotes
			} else {
				if (elements[i].startsWith("'") && elements[i].endsWith("'")) {
					elements[i] = elements[i].substring(1, elements[i].length() - 1); //remove single quotes
				}
			}
		} //for
	}
	
	/**
	 * Reads attributes from the given ARFF file and writes them to the given JSON file.
	 * 
	 * @param arffFilePath path to a file (expected to be in ARFF format)
	 * @param decisionAttributePreferenceType attribute preference type to be set for the decision attribute read from the ARFF file.
	 *        The option of setting value other than the default value {@link AttributePreferenceType#NONE} is useful, e.g., when reading ordinal classification problems.
	 * @param jsonAttributesPath path to resulting JSON file where attributes will be stored
	 * @param prettyPrinting indicator of pretty printing in written JSON
	 * 
	 * @throws NullPointerException see {@link #read(String, AttributePreferenceType)}
	 * @throws FileNotFoundException see {@link #read(String, AttributePreferenceType)}
	 * @throws InvalidValueException see {@link #read(String, AttributePreferenceType)}
	 * @throws UnsupportedOperationException see {@link #read(String, AttributePreferenceType)}
	 * @throws IOException see {@link InformationTableWriter#writeAttributes(InformationTable, java.io.Writer)}
	 */
	public void arff2JsonAttributes(String arffFilePath, AttributePreferenceType decisionAttributePreferenceType, String jsonAttributesPath, boolean prettyPrinting)
			throws FileNotFoundException, IOException {
		InformationTable informationTable = read(arffFilePath, decisionAttributePreferenceType);
		InformationTableWriter informationTableWriter = new InformationTableWriter(prettyPrinting);
		
		try (FileWriter fileWriter = new FileWriter(jsonAttributesPath)) {
			informationTableWriter.writeAttributes(informationTable, fileWriter);
		}
		catch (IOException exception) {
			throw exception;
		}
	}

}
