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

import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.Precondition;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.EvaluationFieldCachingFactory;
import org.rulelearn.types.SimpleField;

import com.univocity.parsers.conversions.TrimConversion;

/**
 * Parses object's evaluation given as string, using information about an attribute.
 * Ignores leading and trailing spaces.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EvaluationParser {
	
	/**
	 * Type of caching of {@link EvaluationField evaluation fields} constructed by evaluation parser from their textual representations. 
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static enum CachingType {
		/**
		 * Type of caching denoting lack of caching. If used, then {@link EvaluationField#getDefaultFactory() default (non-caching) factory} is used to convert
		 * an evaluation from textual representation to an instance of {@link EvaluationField}, based on the {@link EvaluationAttribute#getValueType()
		 * value type of considered evaluation attribute}.
		 */
		NONE,
		/**
		 * Type of caching denoting caching using volatile cache. If used, then {@link EvaluationField#getCachingFactory() caching factory} is used to convert
		 * an evaluation from textual representation to an instance of {@link EvaluationField}, based on the {@link EvaluationAttribute#getValueType()
		 * value type of considered evaluation attribute},
		 * storing resulting instance in caching factory's volatile cache.
		 */
		VOLATILE
	}
	
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
	 * Type of caching of {@link EvaluationField evaluation fields} constructed from their textual representations.
	 */
	CachingType cachingType;
	
	/**
	 * Sole constructor initializing fields with default values. In particular, the array of strings representing a missing value is initialized with
	 * {@link EvaluationParser#DEFAULT_MISSING_VALUE_STRINGS} and use of default (non-caching) factory is assumed to be used when converting
	 * textual representation of an evaluation to an instance of {@link EvaluationField}.
	 */
	public EvaluationParser() {
		super();
		this.trimConversion = new TrimConversion();
		this.missingValueStrings = DEFAULT_MISSING_VALUE_STRINGS;
		this.cachingType = CachingType.NONE;
	}
	
	/**
	 * Sole constructor initializing fields with default values and setting some of them based on given parameters.
	 * 
	 * @param missingValueStrings array of string representations of missing values
	 * @param cachingType requested type of caching of {@link EvaluationField evaluation fields} constructed by this parser from evaluations given in textual form
	 * 
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 * @throws NullPointerException if requested caching type is {@code null}
	 */
	public EvaluationParser(String[] missingValueStrings, CachingType cachingType) {
		super();
		this.trimConversion = new TrimConversion();
		//asserts all missing value representations are not null
		this.missingValueStrings = Precondition.notNullWithContents(missingValueStrings, "Missing value strings array is null.", "Missing value string is null at index %i.");
		this.cachingType = Precondition.notNull(cachingType, "Caching type is null.");
	}

	/**
	 * Parses an object's evaluation and transforms it into an {@link EvaluationField evaluation field}. Employs current information about string representations of missing values
	 * (as returned by {@link #getMissingValueStrings()} and caching type (as returned by {@link #getCachingType()}.
	 * Exact type of the resulting evaluation field is determined using {@link EvaluationAttribute#getValueType() value type} of the given attribute.
	 * If caching type if {@link CachingType#NONE}, then resulting evaluation field is constructed using default factory (see {@link EvaluationField#getDefaultFactory()}).
	 * If caching type if {@link CachingType#VOLATILE}, then resulting evaluation field is constructed using caching factory (see {@link EvaluationField#getCachingFactory()})
	 * and put in its volatile cache (see {@link EvaluationFieldCachingFactory#createWithVolatileCache(String, EvaluationAttribute)}).
	 * 
	 * @param evaluation text-encoded object's evaluation
	 * @param attribute whose value should be parsed
	 * 
	 * @return constructed evaluation field
	 * 
	 * @throws FieldParseException if given string cannot be parsed as a value of the given attribute
	 * @throws FieldParseException if given string is {@code null} or empty
	 */
	public EvaluationField parseEvaluation(String evaluation, EvaluationAttribute attribute) {
		EvaluationField field = null;
		boolean missingSimpleField = false;
		
		if (evaluation == null || evaluation.equals("")) {
			throw new FieldParseException("Cannot parse null or empty evaluation.");
		}
	
		// get rid of white spaces
		evaluation = this.trimConversion.execute(evaluation);
		
		// check whether given string represent a missing value of a simple field
		if (attribute.getValueType() instanceof SimpleField) { //single missing value is of interest only for a simple field
			for (String missingValueString : this.missingValueStrings) {
				if (missingValueString.equalsIgnoreCase(evaluation)) { //missingValueString cannot be null as this is verified in class constructor
					missingSimpleField = true;
					break;
				}
			}
		}
		
		if (!missingSimpleField) { //simple field without missing value, or not a simple field
			try {
				if (this.cachingType == CachingType.VOLATILE) {
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
			   //todo: some optimization is needed here (e.g., construction of a table with element lists)
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
	 * Gets array of string representations of missing values.
	 * 
	 * @return array of string representations of missing values (clone of the array stored in this parser)
	 */
	public String[] getMissingValueStrings() {
		return missingValueStrings.clone();
	}

	/**
	 * Sets array of string representations of missing values.
	 * 
	 * @param missingValueStrings array of string representations of missing values
	 * @throws NullPointerException if given array or any of its elements is {@code null}
	 */
	public void setMissingValueStrings(String[] missingValueStrings) {
		//asserts all missing value representations are not null
		this.missingValueStrings = Precondition.notNullWithContents(missingValueStrings, "Missing value strings array is null.", "Missing value string is null at index %i.");
	}

	/**
	 * Gets requested type of caching of {@link EvaluationField evaluation fields} constructed by this parser from evaluations given in textual form.
	 * 
	 * @return requested type of caching of {@link EvaluationField evaluation fields} constructed by this parser from evaluations given in textual form
	 */
	public CachingType getCachingType() {
		return cachingType;
	}

	/**
	 * Sets requested type of caching of {@link EvaluationField evaluation fields} constructed by this parser from evaluations given in textual form.
	 * 
	 * @param cachingType requested type of caching of {@link EvaluationField evaluation fields} constructed by this parser from evaluations given in textual form
	 * @throws NullPointerException if requested caching type is {@code null}
	 */
	public void setCachingType(CachingType cachingType) {
		this.cachingType = Precondition.notNull(cachingType, "Caching type is null.");
	}
}
