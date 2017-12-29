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

package org.rulelearn.types;

import org.rulelearn.data.AttributePreferenceType;

/**
 * Factory for {@link RealField}, employing abstract factory design pattern.
 *
 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
 * @author Marcin Szeląg <marcin.szelag@cs.put.poznan.pl>
 *
 */
public class RealFieldFactory {
	/**
	 * The only instance of this factory.
	 */
	protected static RealFieldFactory fieldFactory = null;
	
	/**
	 * Retrieves the only instance of this factory (singleton).
	 * 
	 * @return the only instance of this factory
	 */
	public static RealFieldFactory getInstance() {
		if (fieldFactory == null) {
			fieldFactory = new RealFieldFactory();
		}
		return fieldFactory;
	}
	
	/**
	 * Constructor preventing object creation.
	 */
	private RealFieldFactory() {	}
	
	/**
	 * Factory method for creating an instance of {@link RealField}
	 * 
	 * @param value value of the created field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return created field
	 */
	public RealField create(double value, AttributePreferenceType preferenceType) {
		switch (preferenceType) {
			case NONE: return new NoneRealField(value);
			case GAIN: return new GainRealField(value);
			case COST: return new CostRealField(value);
			default: return new NoneRealField(value);
		}
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link RealField}
	 * 
	 * @param field to be cloned
	 * 
	 * @return created field
	 */
	public RealField clone (NoneRealField field) {
		return new NoneRealField(field.value);
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link RealField}
	 * 
	 * @param field to be cloned
	 * 
	 * @return created field
	 */
	public RealField clone (GainRealField field) {
		return new GainRealField(field.value);
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link RealField}
	 * 
	 * @param field to be cloned
	 * 
	 * @return created field
	 */
	public RealField clone (CostRealField field) {
		return new CostRealField(field.value);
	}
	
	/**
	 * Field representing a real number value, for an attribute without preference type.
	 * 
	 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
	 * @author Marcin Szeląg
	 */
	private class NoneRealField extends RealField {
		public NoneRealField(double value) {
			super(value);
		}
		
		/**
		 * Tells if this field is equal to the given field
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@Link FieldComparisonResult}
		 */
		public FieldComparisonResult isEqualTo(Field otherField) {
			try {
				return (this.value == ((NoneRealField)otherField).value ? 
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
	 * Field representing a real number value, for an attribute with gain-type preference.
	 * 
	 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
	 * @author Marcin Szeląg
	 */
	private class GainRealField extends RealField {
		public GainRealField(double value) {
			super(value);
		}
		
		@Override
		public FieldComparisonResult isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.value >= ((GainRealField)otherField).value ? 
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}

		@Override
		public FieldComparisonResult isAtMostAsGoodAs(Field otherField) {
			try {
				return (this.value <= ((GainRealField)otherField).value ? 
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}

		@Override
		public FieldComparisonResult isEqualTo(Field otherField) {
			try {
				return (this.value == ((GainRealField)otherField).value ? 
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			} catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}

		@Override
		public FieldComparisonResult isDifferentThan(Field otherField) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/**
	 * Field representing a real number value, for an attribute with cost-type preference.
	 * 
	 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
	 * @author Marcin Szeląg
	 */
	private class CostRealField extends RealField {
		public CostRealField(double value) {
			super(value);
		}
		
		@Override
		public FieldComparisonResult isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.value <= ((CostRealField)otherField).value ?
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}


		@Override
		public FieldComparisonResult isAtMostAsGoodAs(Field otherField) {
			try {
				return (this.value >= ((CostRealField)otherField).value ?
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			}
			catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}
		
		@Override
		public FieldComparisonResult isEqualTo(Field otherField) {
			try {
				return (this.value == ((CostRealField)otherField).value ? 
						FieldComparisonResult.TRUE : FieldComparisonResult.FALSE);
			} catch (ClassCastException exception) {
				return FieldComparisonResult.UNCOMPARABLE;
			}
		}
	}	
}
