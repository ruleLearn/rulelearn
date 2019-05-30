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
 * Factory for {@link RealField}, employing abstract factory design pattern.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class RealFieldFactory implements EvaluationFieldFactory {
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
	 * Factory method for constructing an instance of {@link RealField}.
	 * 
	 * @param value value of the constructed field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return constructed field
	 * 
	 * @throws NullPointerException if given attribute's preference type is {@code null}
	 */
	public RealField create(double value, AttributePreferenceType preferenceType) {
		switch (Precondition.notNull(preferenceType, "Attribute's preference type is null.")) {
			case NONE: return new NoneRealField(value);
			case GAIN: return new GainRealField(value);
			case COST: return new CostRealField(value);
			default: return new NoneRealField(value);
		}
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link RealField}
	 * 
	 * @param field field to be cloned
	 * @return cloned field
	 * 
	 * @throws NullPointerException if given field is {@code null}
	 */
	public RealField clone (RealField field) {
		return field.selfClone();
	}
	
	/**
	 * Field representing a real number value, for an attribute without preference type.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class NoneRealField extends RealField {
		
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param value value of created field
		 */
		public NoneRealField(double value) {
			super(value);
		}
		
		@Override
		@SuppressWarnings("unchecked") //unfortunately the following implementation causes a warning by javac
//		public NoneRealField selfClone() {
//			return new NoneRealField(this.value);
//		}
		public <S extends Field> S selfClone() {
			return (S)new NoneRealField(this.value);
		}
		
		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsEqualTo(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value == ((NoneRealField)otherField).value ? 
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
		@Override
		public AttributePreferenceType getPreferenceType() {
			return AttributePreferenceType.NONE;
		}
	}
	
	/**
	 * Field representing a real number value, for an attribute with gain-type preference.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class GainRealField extends RealField {
		
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param value value of created field
		 */
		public GainRealField(double value) {
			super(value);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <S extends Field> S selfClone() {
			return (S)new GainRealField(this.value);
		}
		
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtLeastAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value >= ((GainRealField)otherField).value ? 
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
					return (this.value <= ((GainRealField)otherField).value ? 
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
					return (this.value == ((GainRealField)otherField).value ? 
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
			return AttributePreferenceType.GAIN;
		}

	}
	
	/**
	 * Field representing a real number value, for an attribute with cost-type preference.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class CostRealField extends RealField {
		
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param value value of created field
		 */
		public CostRealField(double value) {
			super(value);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <S extends Field> S selfClone() {
			return (S)new CostRealField(this.value);
		}
		
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtLeastAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value <= ((CostRealField)otherField).value ?
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
					return (this.value >= ((CostRealField)otherField).value ?
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
					return (this.value == ((CostRealField)otherField).value ? 
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

	}

	/**
	 * Constructs real field from its textual representation.
	 * 
	 * @param value textual representation of a real field
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException if given value cannot be parsed as a real number
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidTypeException {@inheritDoc}
	 */
	@Override
	public RealField create(String value, EvaluationAttribute attribute) {
		Precondition.notNull(attribute, "Attribute used to construct real field is null.");
		if (!(attribute.getValueType() instanceof RealField)) {
			throw new InvalidTypeException("Attribute's value type is not an instance of real field.");
		}
		
		try {
			return create(Double.parseDouble(value), attribute.getPreferenceType());
		}
		catch (NumberFormatException exception) {
			throw new FieldParseException(exception.getMessage());
		}
	}
}
