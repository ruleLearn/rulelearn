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
import org.rulelearn.data.json.AttributeDeserializer;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
	 * Separator of values in string representation of information.
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
	 * @throws NullPointerException if all or some of attributes of the constructed information table have not been set
	 */
	public InformationTableBuilder(Attribute[] attributes) {
		this();
		
		this.attributes = attributes;
		
		// check attributes and initialize fields
		if (attributes != null) {
			for (Attribute attribute : attributes)
				if (attribute == null) throw new NullPointerException("At least one attribute is not set");
			this.fields = new ObjectArrayList<Field []>();
		}
		else
			throw new NullPointerException("Attributes are not set");
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
	 * Constructor initializing this information table builder and setting attributes. It requires definition of attributes in JSON.
	 * 
	 * @param jsonAttributes JSON string encoding information about all attributes
	 * @throws NullPointerException if all or some of attributes of the constructed information table have not been set
	 */
	public InformationTableBuilder(String jsonAttributes) {
		this();
		
		// parse attributes
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute.class, new AttributeDeserializer());
		Gson gson = gsonBuilder.create();
		this.attributes = gson.fromJson(jsonAttributes, Attribute[].class);
		
		// check attributes and initialize fields
		if (attributes != null) {
			for (Attribute attribute : attributes)
				if (attribute == null) throw new NullPointerException("At least one attribute is not set");
			this.fields = new ObjectArrayList<Field []>();
		}
		else
			throw new NullPointerException("Attributes are not set");
	}
	
	/**
	 * Constructor initializing this information table builder and setting attributes. It requires definition of attributes in JSON.
	 * 
	 * @param jsonAttributes JSON string encoding information about all attributes
	 * @param separator separator of object's evaluations
	 * @throws NullPointerException if all or some of attributes of the constructed information table have not been set
	 */
	public InformationTableBuilder(String jsonAttributes, String separator) {
		this(jsonAttributes);
		this.separator = separator;
	}
	
	/**
	 * Adds one object to this builder. 
	 * Given string is considered to contain subsequent evaluations of a single object, separated by the {@link InformationTableBuilder#separator}.
	 * 
	 * @param objectEvaluations string with object's evaluations, separated by the given separator
	 */
	public void addObject(String objectEvaluations) {
		if (this.separator != null)
			addObject(objectEvaluations.split(this.separator));
	}
	
	/**
	 * Adds one object to this builder. 
	 * Given string is considered to contain subsequent evaluations of a single object, separated by the given separator.
	 * 
	 * @param objectEvaluations string with object's evaluations, separated by the given separator
	 * @param separator separator of object's evaluations
	 */
	public void addObject(String objectEvaluations, String separator) {
		if (separator != null)
			addObject(objectEvaluations.split(separator));
	}
	
	/**
	 * Adds one object to this builder. 
	 * Given array is considered to contain subsequent evaluations of a single object.
	 * 
	 * @param objectEvaluations single object's evaluations
	 * 
	 * @throws IndexOutOfBoundsException if given attribute index does not correspond to any attribute of the constructed information table
	 */
	public void addObject(String[] objectEvaluations) {
		Field[] object = new Field[objectEvaluations.length];
		if (objectEvaluations.length > attributes.length)
			throw new IndexOutOfBoundsException("Object has more evaluations than the number of attributes declared.");
		
		for (int i = 0; i < objectEvaluations.length; i++) {
			object[i] = parseEvaluation(objectEvaluations[i], attributes[i]);
		}
		
		this.fields.add(object);
	}
	
	/**
	 * Parses one object's evaluation and transforms it into a field {@link Field}.
	 * 
	 * @param evaluation text-encoded object's evaluation
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed field
	 * @throws NumberFormatException if evaluation of an numeric attribute can't be parsed
	 * @throws IndexOutOfBoundsException if evaluation of an enumeration attribute can't be parsed  
	 */
	protected Field parseEvaluation(String evaluation, Attribute attribute) throws NumberFormatException, IndexOutOfBoundsException {
		Field field = null;
		boolean missingValue = false;
	
		
		// get rid of white spaces
		evaluation = trimConversion.execute(evaluation);
		// check whether it is a missing value
		for (String missingValueString : this.missingValueStrings) {
			if (missingValueString.equalsIgnoreCase(evaluation)) {
				missingValue = true;
				break;
			}
		}
		
		if (!missingValue) {
			Field valueType = attribute.getValueType(); 
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
				if (index != ElementList.DEFAULT_INDEX)
					field = EnumerationFieldFactory.getInstance().create(((EnumerationField)valueType).getElementList(), index, attribute.preferenceType);
				else {
					field = attribute.getMissingValueType();
					throw new IndexOutOfBoundsException(new String("Incorrect value of enumeration: ").concat(evaluation));
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
	 * Builds information table.
	 * 
	 * @return constructed information table
	 */
	public InformationTable build() {
		return new InformationTable(attributes, fields);
	}

	/**
	 * @return the missingValueStrings
	 */
	public String[] getMissingValueStrings() {
		return missingValueStrings;
	}

	/**
	 * @param missingValueStrings the missingValueStrings to set
	 */
	public void setMissingValueStrings(String[] missingValueStrings) {
		this.missingValueStrings = missingValueStrings;
	}
	
}
