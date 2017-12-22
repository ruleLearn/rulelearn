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

package org.ruleLearn.types;

import org.ruleLearn.data.AttributePreferenceType;

/**
 * Factory for {@link IntegerField}, employing abstract factory design pattern.
 * 
 * @author Jurek Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
 * @author Marcin Szeląg <marcin.szelag@cs.put.poznan.pl>
 */
public class IntegerFieldFactory {
	
	/**
	 * The only instance of this factory.
	 */
	protected static IntegerFieldFactory fieldFactory = null;
	
	/**
	 * Retrieves the only instance of this factory (singleton).
	 * 
	 * @return the only instance of this factory
	 */
	public static IntegerFieldFactory getInstance() {
		if (fieldFactory == null) {
			fieldFactory = new IntegerFieldFactory();
		}
		return fieldFactory;
	}
	
	/**
	 * Object creation preventing constructor.
	 */
	private IntegerFieldFactory() {
		
	}
	
	/**
	 * Factory method for creating an instance of {@link IntegerField}
	 * 
	 * @param value value of the created field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return created field
	 */
	public IntegerField create(int value, AttributePreferenceType preferenceType) {
		switch (preferenceType) {
			case COST: return new CostIntegerField(value);
			case GAIN: return new GainIntegerField(value);
			case NONE: return new NoneIntegerField(value);
			
			default: return new NoneIntegerField(value);
		}
	}
	
	/**
	 * Field with integer value, for an attribute without preference type.
	 * 
	 * @author Marcin Szeląg
	 */
	private class NoneIntegerField extends IntegerField {
		public NoneIntegerField(int value) {
			super(value);
		}
		
		/**
		 * Tells if this field is equal to the given field
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@Link FieldComparisonResult}
		 */
		protected FieldComparisonResult isEqualTo(Field otherField) {
			try {
				return (this.value == ((NoneIntegerField)otherField).value ? 
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			} catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}
		
		@Override
		public FieldComparisonResult isAtLeastAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}

		@Override
		public FieldComparisonResult isAtMostAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}
		
	}
	
	/**
	 * Field with integer value, for an attribute with gain-type preference.
	 * 
	 * @author Marcin Szeląg
	 */
	private class GainIntegerField extends IntegerField {
		public GainIntegerField(int value) {
			super(value);
		}
		
		@Override
		public FieldComparisonResult isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.value >= ((GainIntegerField)otherField).value ? 
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}


		@Override
		public FieldComparisonResult isAtMostAsGoodAs(Field otherField) {
			try {
				return (this.value <= ((GainIntegerField)otherField).value ? 
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}
	}
	
	/**
	 * Field with integer value, for an attribute with cost-type preference.
	 * 
	 * @author Marcin Szeląg
	 */
	private class CostIntegerField extends IntegerField {
		public CostIntegerField(int value) {
			super(value);
		}
		
		@Override
		public FieldComparisonResult isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.value <= ((CostIntegerField)otherField).value ?
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}


		@Override
		public FieldComparisonResult isAtMostAsGoodAs(Field otherField) {
			try {
				return (this.value >= ((CostIntegerField)otherField).value ?
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}
	}
	
}
