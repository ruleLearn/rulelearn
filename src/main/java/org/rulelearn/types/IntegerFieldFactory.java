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

import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;

/**
 * Factory for {@link IntegerField} evaluations, employing abstract factory and singleton design patterns.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class IntegerFieldFactory implements EvaluationFieldFactory {
	
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
	 * Factory method for constructing an instance of {@link IntegerField}.
	 * 
	 * @param value value of the constructed field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return constructed field
	 * 
	 * @throws NullPointerException if given attribute's preference type is {@code null}
	 */
	public IntegerField create(int value, AttributePreferenceType preferenceType) {
		switch (Precondition.notNull(preferenceType, "Attribute's preference type is null.")) {
			case NONE: return new NoneIntegerField(value);
			case GAIN: return new GainIntegerField(value);
			case COST: return new CostIntegerField(value); 
			default: return new NoneIntegerField(value);
		}
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link IntegerField}.
	 * 
	 * @param field field to be cloned
	 * @return cloned field
	 * 
	 * @throws NullPointerException if given field is {@code null}
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
		@SuppressWarnings("unchecked") //unfortunately the following implementation causes a warning by javac:
//		public NoneIntegerField selfClone() {
//			return new NoneIntegerField(this.value);
//		}
		public <S extends Field> S selfClone() {
			return (S)new NoneIntegerField(this.value);
		}
		
		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsEqualTo(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value == ((NoneIntegerField)otherField).value ? 
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				} catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * As for an attribute without preference type, one cannot decide whether one value is at least as good as some other value,
		 * this method has no semantic meaning. It is left for convenience sake only - calling this method gives the same result
		 * as calling method {@link #isEqualTo(Field)}.
		 * 
		 * @param otherField {@inheritDoc}
		 * @return {@inheritDoc}
		 * @throws NullPointerException {@inheritDoc}
		 */
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * As for an attribute without preference type, one cannot decide whether one value is at most as good as some other value,
		 * this method has no semantic meaning. It is left for convenience sake only - calling this method gives the same result
		 * as calling method {@link #isEqualTo(Field)}.
		 * 
		 * @param otherField {@inheritDoc}
		 * @return {@inheritDoc}
		 * @throws NullPointerException {@inheritDoc}
		 */
		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		public AttributePreferenceType getPreferenceType() {
			return AttributePreferenceType.NONE;
		}
		
		/**
		 * Gets text representation of type of this field (i.e., {@link NoneIntegerField}).
		 * 
		 * @return text representation of type of this field
		 */
		@Override
		public String getTypeDescriptor() {
			return "noneInt";
		}

	}
	
	/**
	 * Field representing an integer number value, for an attribute with gain-type preference.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class GainIntegerField extends IntegerField {
		
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param value value of created field
		 */
		public GainIntegerField(int value) {
			super(value);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <S extends Field> S selfClone() {
			return (S)new GainIntegerField(this.value);
		}
		
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtLeastAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value >= ((GainIntegerField)otherField).value ? 
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				}
				catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}

		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtMostAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value <= ((GainIntegerField)otherField).value ? 
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				}
				catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}

		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsEqualTo(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value == ((GainIntegerField)otherField).value ? 
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				} catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		public AttributePreferenceType getPreferenceType() {
			return AttributePreferenceType.GAIN;
		}
		
		/**
		 * Gets text representation of type of this field (i.e., {@link GainIntegerField}).
		 * 
		 * @return text representation of type of this field
		 */
		@Override
		public String getTypeDescriptor() {
			return "gainInt";
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
		public <S extends Field> S selfClone() {
			return (S)new CostIntegerField(this.value);
		}
		
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtLeastAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value <= ((CostIntegerField)otherField).value ?
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				}
				catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}


		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtMostAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value >= ((CostIntegerField)otherField).value ?
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				}
				catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}
		
		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsEqualTo(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value == ((CostIntegerField)otherField).value ? 
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				} catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public AttributePreferenceType getPreferenceType() {
			return AttributePreferenceType.COST;
		}
		
		/**
		 * Gets text representation of type of this field (i.e., {@link CostIntegerField}).
		 * 
		 * @return text representation of type of this field
		 */
		@Override
		public String getTypeDescriptor() {
			return "costInt";
		}
	}

	/**
	 * Constructs integer field from its textual representation.
	 * 
	 * @param value textual representation of an integer field
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException if given value cannot be parsed as an integer number
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidTypeException {@inheritDoc}
	 */
	@Override
	public IntegerField create(String value, EvaluationAttribute attribute) {
		Precondition.notNull(attribute, "Attribute used to construct integer field is null.");
		if (!(attribute.getValueType() instanceof IntegerField)) {
			throw new InvalidTypeException("Attribute's value type is not an instance of integer field.");
		}
		
		try {
			return create(Integer.parseInt(value), attribute.getPreferenceType());
		}
		catch (NumberFormatException exception) {
			throw new FieldParseException(new StringBuilder("Incorrect value ").append(value)
					.append(" of integer attribute ").append(attribute.getName()).append(". ").append(exception.getMessage()).toString());
		}
	}
}
