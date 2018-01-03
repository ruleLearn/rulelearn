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

import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;

/**
 * Factory for {@link IntegerField}, employing abstract factory and singleton design patterns.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
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
	 * Constructor preventing object creation.
	 */
	private IntegerFieldFactory() {}
	
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
			case NONE: return new NoneIntegerField(value);
			case GAIN: return new GainIntegerField(value);
			case COST: return new CostIntegerField(value); 
			default: return new NoneIntegerField(value);
		}
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link IntegerField}.
	 * 
	 * @param field to be cloned
	 * @return created field
	 */
	public IntegerField clone(IntegerField field) {
		return field.selfClone();
	}
	
	/**
	 * Field representing an integer number value, for an attribute without preference type.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class NoneIntegerField extends IntegerField {
		
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param value value of created field
		 */
		public NoneIntegerField(int value) {
			super(value);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public NoneIntegerField selfClone() {
			return new NoneIntegerField(this.value);
		}
//		public <S extends Field> S selfClone() {
//			return (S)new NoneIntegerField(this.value);
//		}
		
		/**
		 * Tells if this field is equal to the given field
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@Link TernaryLogicValue}
		 */
		public TernaryLogicValue isEqualTo(Field otherField) {
			try {
				return (this.value == ((NoneIntegerField)otherField).value ? 
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			} catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}
		
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}

		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}
	}
	
	/**
	 * Field representing an integer number value, for an attribute with gain-type preference.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class GainIntegerField extends IntegerField {
		
		@Override
		@SuppressWarnings("unchecked")
		public GainIntegerField selfClone() {
			return new GainIntegerField(this.value);
		}
		
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param value value of created field
		 */
		public GainIntegerField(int value) {
			super(value);
		}
		
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.value >= ((GainIntegerField)otherField).value ? 
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}

		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			try {
				return (this.value <= ((GainIntegerField)otherField).value ? 
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}

		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			try {
				return (this.value == ((GainIntegerField)otherField).value ? 
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			} catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}

	}
	
	/**
	 * Field representing an integer number value, for an attribute with cost-type preference.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class CostIntegerField extends IntegerField {
		
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param value value of created field
		 */
		public CostIntegerField(int value) {
			super(value);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public CostIntegerField selfClone() {
			return new CostIntegerField(this.value);
		}
		
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.value <= ((CostIntegerField)otherField).value ?
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}


		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			try {
				return (this.value >= ((CostIntegerField)otherField).value ?
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}
		
		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			try {
				return (this.value == ((CostIntegerField)otherField).value ? 
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			} catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}
	}
}
