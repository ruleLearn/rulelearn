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

import java.util.List;
import java.util.UUID;

import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IdentificationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.TextIdentificationField;
import org.rulelearn.types.UUIDIdentificationField;

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
		
		this.attributes = attributes;
		
		// check attributes and initialize fields
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				if (attribute == null) throw new NullPointerException("At least one attribute is not set");
			}
			this.fields = new ObjectArrayList<Field []>();
		}
		else {
			throw new NullPointerException("Attributes are not set");
		}
	}
	
	/**
	 * Constructor initializing this information table builder and setting attributes.
	 * 
	 * @param attributes table with attributes
	 * @param separator separator of object's evaluations
	 * @throws NullPointerException if all or some of attributes of the constructed information table have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes, String separator) {
		this(attributes);
		this.separator = separator;
	}
	
	/**
	 * Constructor initializing this information table builder, setting attributes, and missing values strings (i.e., array of strings representing missing values).
	 * 
	 * @param attributes table with attributes
	 * @param separator separator of object's evaluations
	 * @param missingValuesStrings array of string representations of missing values
	 * @throws NullPointerException if all or some of attributes of the constructed information table have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes, String separator, String [] missingValuesStrings) {
		this(attributes, separator);
		this.missingValueStrings = missingValuesStrings;
	}
	
	/**
	 * Adds one object to this builder. 
	 * Given string is considered to contain subsequent identifiers/evaluations of a single object, separated by the {@link InformationTableBuilder#separator}.
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
		Field[] object = new Field[objectDescriptions.length];
		if (objectDescriptions.length > attributes.length)
			throw new IndexOutOfBoundsException("Object has more evaluations than the number of attributes declared.");
		
		for (int i = 0; i < objectDescriptions.length; i++) {
			if (attributes[i] instanceof EvaluationAttribute) {
				object[i] = parseEvaluation(objectDescriptions[i], (EvaluationAttribute)attributes[i]);
			}
			else if (attributes[i] instanceof IdentificationAttribute) {
				object[i] = parseIdentification(objectDescriptions[i], (IdentificationAttribute)attributes[i]);
			}
		}
		
		this.fields.add(object);
	}
	
	/**
	 * Parses one object's evaluation and transforms it into a field {@link EvaluationField}.
	 * 
	 * @param evaluation text-encoded object's evaluation
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed field
	 * @throws NumberFormatException if evaluation of a numeric attribute can't be parsed
	 * @throws IndexOutOfBoundsException if evaluation of an enumeration attribute can't be parsed  
	 */
	protected EvaluationField parseEvaluation(String evaluation, EvaluationAttribute attribute) throws NumberFormatException, IndexOutOfBoundsException {
		EvaluationField field = null;
		boolean missingValue = false;
	
		
		// get rid of white spaces
		evaluation = trimConversion.execute(evaluation);
		// check whether it is a missing value
		if (missingValueStrings != null) {
			for (String missingValueString : this.missingValueStrings) {
				if ((missingValueString != null) && (missingValueString.equalsIgnoreCase(evaluation))) {
					missingValue = true;
					break;
				}
			}
		}
		
		if (!missingValue) {
			EvaluationField valueType = attribute.getValueType(); 
			if (valueType instanceof IntegerField) {
				try {
					field = IntegerFieldFactory.getInstance().create(Integer.parseInt(evaluation), attribute.preferenceType);
				}
				catch (NumberFormatException ex) {
					// just assign a reference (no new copy of missing value field is made)
					field = attribute.getMissingValueType();
					throw new NumberFormatException(ex.getMessage());
				}
			}
			else if (valueType instanceof RealField) {
				try {
					field = RealFieldFactory.getInstance().create(Double.parseDouble(evaluation), attribute.preferenceType);
				}
				catch (NumberFormatException ex) {
					// just assign a reference (no new copy of missing value field is made)
					field = attribute.getMissingValueType();
					throw new NumberFormatException(ex.getMessage());
				}
			}
			else if (valueType instanceof EnumerationField) {
				// TODO some optimization is needed here (e.g., construction of a table with element lists)
				int index = ((EnumerationField)valueType).getElementList().getIndex(evaluation);
				if (index != ElementList.DEFAULT_INDEX) {
					field = EnumerationFieldFactory.getInstance().create(((EnumerationField)valueType).getElementList(), index, attribute.preferenceType);
				}
				else {
					field = attribute.getMissingValueType();
					throw new IndexOutOfBoundsException(new StringBuilder("Incorrect value of enumeration: ").append(evaluation).append(" was replaced by a missing value.").toString());
				}
					
			}
			else {
				// just assign a reference (no new copy of missing value field is made) 
				field = attribute.getMissingValueType();
			}
		}
		else {
			// just assign a reference (no new copy of missing value field is made) 
			field = attribute.getMissingValueType();
		}
		
		return field;
	}
	
	/**
	 * Parses one object's identification and transforms it into a field {@link IdentificationField}.
	 * 
	 * @param identification text-encoded object's identification
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed field
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
			field = (missingValue ?
				new UUIDIdentificationField(UUIDIdentificationField.DEFAULT_VALUE) : new UUIDIdentificationField(UUID.fromString(identification))); 			
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
	 * Gets missing value strings.
	 * 
	 * @return the missingValueStrings
	 */
	public String[] getMissingValueStrings() {
		return missingValueStrings;
	}

	/**
	 * Sets missing value strings.
	 * 
	 * @param missingValueStrings the missingValueStrings to set
	 */
	public void setMissingValueStrings(String[] missingValueStrings) {
		this.missingValueStrings = missingValueStrings;
	}
	
}
