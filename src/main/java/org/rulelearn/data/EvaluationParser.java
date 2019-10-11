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

import static org.rulelearn.core.Precondition.notNullWithContents;

import org.rulelearn.core.FieldParseException;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.SimpleField;

import com.univocity.parsers.conversions.TrimConversion;

/**
 * Parses object's evaluation given as string, using information about an attribute.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EvaluationParser {
	
	/** 
	 * Default string representations of a missing value.
	 */
	final static String[] DEFAULT_MISSING_VALUE_STRINGS = {"?", "*", "NA"}; //SIC! null is not allowed on this list and the list itself cannot be null
	
	/**
	 * Array of string representations of missing values.
	 */
	String[] missingValueStrings = null;
	
	/**
	 * Converter which removes leading and trailing white spaces from an input {@link String}.
	 */
	TrimConversion trimConversion = null;
	
	/**
	 * Tells if volatile caching factory should be used to translate string representation of an evaluation to its proper {@link EvaluationField} sub-type representation.
	 */
	boolean useVolatileCachingFactory;
	
	/**
	 * Sole constructor initializing fields with default values. In particular, the array of strings representing a missing value is initialized with
	 * {@link EvaluationParser#DEFAULT_MISSING_VALUE_STRINGS} and use of volatile caching factory is assumed when translating string representation of an evaluation
	 * to its proper {@link EvaluationField} sub-type representation.
	 */
	public EvaluationParser() {
		super();
		this.trimConversion = new TrimConversion();
		this.missingValueStrings = DEFAULT_MISSING_VALUE_STRINGS;
		this.useVolatileCachingFactory = true;
	}
	
	/**
	 * Sole constructor initializing fields with default values and setting some of them based on given parameters.
	 * 
	 * @param missingValueStrings array of string representations of missing values
	 * @param useVolatileCachingFactory tells if volatile caching factory should be used to translate string representation of an evaluation to its proper {@link EvaluationField} sub-type representation.
	 *        If {@code true}, then caching factories are used (see {@link EvaluationField#getCachingFactory()}),
	 *        and string evaluations are parsed to {@link EvaluationField evaluation fields} using volatile cache.
	 *        If {@code false}, then default factories are used (see {@link EvaluationField#getDefaultFactory()}).
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public EvaluationParser(String[] missingValueStrings, boolean useVolatileCachingFactory) {
		super();
		this.trimConversion = new TrimConversion();
		notNullWithContents(missingValueStrings, "Missing value strings array is null.", "Missing value string is null at index %i."); //asserts all missing value representations are not null
		this.missingValueStrings = missingValueStrings;
		this.useVolatileCachingFactory = useVolatileCachingFactory;
	}

	/**
	 * Parses one object's evaluation and transforms it into an {@link EvaluationField evaluation field}.
	 * 
	 * @param evaluation text-encoded object's evaluation
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed evaluation field
	 * 
	 * @throws FieldParseException if given string cannot be parsed as a value of the given attribute
	 */
	protected EvaluationField parseEvaluation(String evaluation, EvaluationAttribute attribute) {
		EvaluationField field = null;
		boolean missingSimpleField = false;
	
		// get rid of white spaces
		evaluation = trimConversion.execute(evaluation);
		
		// check whether given string represent a missing value of a simple field
		if (attribute.getValueType() instanceof SimpleField) { //single missing value is of interest only for a simple field
			for (String missingValueString : this.missingValueStrings) {
				if (missingValueString.equalsIgnoreCase(evaluation)) { //missingValueString cannot be null as this is verified in class constructor
					missingSimpleField = true;
					break;
				}
			}
		}
		
		if (!missingSimpleField) { //simple field without missing value or not a simple field
			try {
				if (useVolatileCachingFactory) {
					field = attribute.getValueType().getCachingFactory().createWithVolatileCache(evaluation, attribute);
				} else {
					field = attribute.getValueType().getDefaultFactory().create(evaluation, attribute);
				}
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
}
