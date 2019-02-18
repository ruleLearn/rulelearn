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

package org.rulelearn.core;

import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.KnownSimpleField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.SimpleField;
import org.rulelearn.types.UnknownSimpleField;

/**
 * Central tendency measures calculator for different field {@link SimpleField} types.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class CentralTendencyCalculator {
	
	/**
	 * Calculates mean (average) value of two fields of {@link SimpleField} type. Returns {@code null} when it is impossible to calculate mean value.
	 * 
	 * @param a first averaged field
	 * @param b second averaged field
	 * @return mean (average) value of two fields or {@code null} when it is impossible to calculate mean value
	 */
	public static SimpleField calculateMean(SimpleField a, SimpleField b) {
		SimpleField mean = null;
		
		if ((a instanceof KnownSimpleField) && (b instanceof KnownSimpleField)) {
			if ((a instanceof EnumerationField) && (b instanceof EnumerationField)) {
				mean = CentralTendencyCalculator.calculateMean((EnumerationField)a, (EnumerationField)b);
			}
			else if ((a instanceof IntegerField) && (b instanceof IntegerField)) {
				mean = CentralTendencyCalculator.calculateMean((IntegerField)a, (IntegerField)b);
			}
			else if ((a instanceof RealField) && (b instanceof RealField)) {
				mean = CentralTendencyCalculator.calculateMean((RealField)a, (RealField)b);
			}
		}
		else if ((a instanceof KnownSimpleField) && (b instanceof UnknownSimpleField)) {
			mean = CentralTendencyCalculator.calculateMean((KnownSimpleField)a, (UnknownSimpleField)b);
		}
		else if ((a instanceof UnknownSimpleField) && (b instanceof KnownSimpleField)) {
			mean = CentralTendencyCalculator.calculateMean((UnknownSimpleField)a, (KnownSimpleField)b);
		}
		else if ((a instanceof KnownSimpleField) && (b == null)) {
			mean = a;
		}
		else if ((a == null) && (b instanceof KnownSimpleField)) {
			mean = b;
		}
		
		return mean;
	}
	
	/**
	 * Calculates mean (average) value of two fields of {@link EvaluationField} type. Returns {@code null} when it is impossible to calculate mean value.
	 * 
	 * @param a first averaged field
	 * @param b second averaged field
	 * @return mean (average) value of two fields or {@code null} when it is impossible to calculate mean value
	 */
	public static EvaluationField calculateMean(EvaluationField a, EvaluationField b) {
		EvaluationField mean = null;
		
		if ((a instanceof KnownSimpleField) && (b instanceof KnownSimpleField)) {
			if ((a instanceof EnumerationField) && (b instanceof EnumerationField)) {
				mean = CentralTendencyCalculator.calculateMean((EnumerationField)a, (EnumerationField)b);
			}
			else if ((a instanceof IntegerField) && (b instanceof IntegerField)) {
				mean = CentralTendencyCalculator.calculateMean((IntegerField)a, (IntegerField)b);
			}
			else if ((a instanceof RealField) && (b instanceof RealField)) {
				mean = CentralTendencyCalculator.calculateMean((RealField)a, (RealField)b);
			}
		}
		else if ((a instanceof KnownSimpleField) && (b instanceof UnknownSimpleField)) {
			mean = CentralTendencyCalculator.calculateMean((KnownSimpleField)a, (UnknownSimpleField)b);
		}
		else if ((a instanceof UnknownSimpleField) && (b instanceof KnownSimpleField)) {
			mean = CentralTendencyCalculator.calculateMean((UnknownSimpleField)a, (KnownSimpleField)b);
		}
		else if ((a instanceof KnownSimpleField) && (b == null)) {
			mean = a;
		}
		else if ((a == null) && (b instanceof KnownSimpleField)) {
			mean = b;
		}
		
		return mean;
	}
	
	/**
	 * Calculates mean (average) value of a known value represented as {@link KnownSimpleField} and an unknown value represented as {@link UnknownSimpleField} field.
	 * 
	 * @param a known field
	 * @param b unknown field
	 * @return mean (average) value of known field and unknown field
	 */
	public static KnownSimpleField calculateMean(KnownSimpleField a, UnknownSimpleField b) {
		return a;
	}
	
	/**
	 * Calculates mean (average) value of an unknown value represented as {@link UnknownSimpleField} and a known value represented as {@link KnownSimpleField} field.
	 * 
	 * @param a unknown field
	 * @param b known field
	 * @return mean (average) value of unknown field and known field
	 */
	public static KnownSimpleField calculateMean(UnknownSimpleField a, KnownSimpleField b) {
		return b;
	}
	
	/**
	 * Calculates mean (average) value of two fields of {@link IntegerField} type. Returns {@code null} when it is impossible to calculate mean value.
	 * It is impossible to calculate mean value of two null values and values of fields which have different preference type {@link AttributePreferenceType}.
	 * 
	 * @param a first averaged field
	 * @param b second averaged field
	 * @return mean (average) value of two fields or {@code null} when it is impossible to calculate mean value
	 */
	public static IntegerField calculateMean(IntegerField a, IntegerField b) {
		IntegerField mean = null;
		if (a == null ) {
			if (b != null) {
				mean = b;
			}
		}
		else if (b == null) {
			mean = a;
		}
		else {
			if ((a.isEqualTo(b) == TernaryLogicValue.TRUE)) {
				mean = a;
			}
			else if (a.getPreferenceType() == b.getPreferenceType()) {
				mean = IntegerFieldFactory.getInstance().create((a.getValue()+b.getValue())/2, a.getPreferenceType());
			}
		}
		return mean;
	}
	
	/**
	 * Calculates mean (average) value of two fields of {@link RealField} type. Returns {@code null} when it is impossible to calculate mean value.
	 * It is impossible to calculate mean value of two null values and values of fields which have different preference type {@link AttributePreferenceType}.
	 * 
	 * @param a first averaged field
	 * @param b second averaged field
	 * @return mean (average) value of two fields or {@code null} when it is impossible to calculate mean value
	 */
	public static RealField calculateMean(RealField a, RealField b) {
		RealField mean = null;
		if (a == null ) {
			if (b != null) {
				mean = b;
			}
		}
		else if (b == null) {
			mean = a;
		}
		else {
			if (a.isEqualTo(b) == TernaryLogicValue.TRUE) {
				mean = a;
			}
			else if (a.getPreferenceType() == b.getPreferenceType()) {
				mean = RealFieldFactory.getInstance().create((a.getValue()+b.getValue())/2, a.getPreferenceType());
			}
		}
		return mean;
	}
	
	/**
	 * Calculates mean (average) value of two fields of {@link EnumerationField} type. Returns {@code null} when it is impossible to calculate mean value.
	 * It is impossible to calculate mean value of two null values and values of fields which have different preference type {@link AttributePreferenceType} or 
	 * different domains {@link ElementList}.
	 * 
	 * @param a first averaged field
	 * @param b second averaged field
	 * @return mean (average) value of two fields or {@code null} when it is impossible to calculate mean value
	 */
	public static EnumerationField calculateMean(EnumerationField a, EnumerationField b) {
		EnumerationField mean = null;
		if (a == null ) {
			if (b != null) {
				mean = b;
			}
		}
		else if (b == null) {
			mean = a;
		}
		else {
			if ((a.isEqualTo(b) == TernaryLogicValue.TRUE) && (a.hasEqualHashOfElementList(b) == TernaryLogicValue.TRUE)) {
				mean = a;
			}
			else if ((a.getPreferenceType() == b.getPreferenceType()) && (a.hasEqualHashOfElementList(b) == TernaryLogicValue.TRUE)) {
				mean = EnumerationFieldFactory.getInstance().create(a.getElementList(), (a.getValue()+b.getValue())/2, a.getPreferenceType());
			}
		}
		return mean;
	}
}
