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
import org.rulelearn.data.EvaluationAttribute;
import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.Precondition;

/**
 * Factory for {@link EnumerationField}, employing abstract factory and singleton design patterns.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class EnumerationFieldFactory implements EvaluationFieldFactory {
	/**
	 * The only instance of this factory.
	 */
	protected static EnumerationFieldFactory fieldFactory = null;
	
	/**
	 * Retrieves the only instance of this factory (singleton).
	 * 
	 * @return the only instance of this factory
	 */
	public static EnumerationFieldFactory getInstance() {
		if (fieldFactory == null) {
			fieldFactory = new EnumerationFieldFactory();
		}
		return fieldFactory;
	}
	
	/**
	 * Constructor preventing object creation.
	 */
	private EnumerationFieldFactory() {}
	
	/**
	 * Factory method for constructing an instance of {@link EnumerationField}.
	 * 
	 * @param list element list of the constructed field
	 * @param index position in the element list of enumeration which represents value of the field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return constructed field
	 * 
	 * @throws NullPointerException if given list or attribute's preference type is {@code null}
	 * @throws IndexOutOfBoundsException if given index is incorrect (i.e., does not match any position of given element list)
	 */
	public EnumerationField create(ElementList list, int index, AttributePreferenceType preferenceType) {
		switch (Precondition.notNull(preferenceType, "Attribute's preference type is null.")) {
			case NONE: return new NoneEnumerationField(list, index);
			case GAIN: return new GainEnumerationField(list, index);
			case COST: return new CostEnumerationField(list, index);
			default: return new NoneEnumerationField(list, index);
		}
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link EnumerationField}.
	 * 
	 * @param field field to be cloned
	 * @return cloned field
	 * 
	 * @throws NullPointerException if given field is {@code null}
	 */
	public EnumerationField clone (EnumerationField field) {
		return field.selfClone();
	}
	
	/**
	 * Field representing an enumeration value, for an attribute without preference type.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class NoneEnumerationField extends EnumerationField {
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param set element set of the created field
		 * @param index position in the element set of enumeration which represents value of the field
		 * 
		 * @throws IndexOutOfBoundsException if index is incorrect
		 * @throws NullPointerException if given list is {@code null}
		 */
		public NoneEnumerationField(ElementList list, int index) {
			super(list, index);
		}
		
		@Override
		@SuppressWarnings("unchecked") //unfortunately the following implementation causes a warning by javac
		public <S extends Field> S selfClone() {
			return (S)new NoneEnumerationField(this.list, this.value);
		}
		
		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsEqualTo(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value == ((NoneEnumerationField)otherField).value ? 
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
	 * Field representing an enumeration value, for an attribute with gain-type preference.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class GainEnumerationField extends EnumerationField {
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param set element set of the created field
		 * @param index position in the element set of enumeration which represents value of the field
		 * 
		 * @throws IndexOutOfBoundsException if index is incorrect
		 * @throws NullPointerException if given list is {@code null}
		 */
		public GainEnumerationField(ElementList list, int index){
			super(list, index);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <S extends Field> S selfClone() {
			return (S)new GainEnumerationField(this.list, this.value);
		}
		/**
		 * Tells if this field is at least as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtLeastAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value >= ((GainEnumerationField)otherField).value ?
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				}
				catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}

		/**
		 * Tells if this field is at most as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtMostAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value <= ((GainEnumerationField)otherField).value ?
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
					return (this.value == ((GainEnumerationField)otherField).value ? 
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
	 * Field representing an enumeration value, for an attribute with cost-type preference.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	private class CostEnumerationField extends EnumerationField {
		/**
		 * Constructor setting value of this field.
		 * 
		 * @param set element set of the created field
		 * @param index position in the element set of enumeration which represents value of the field
		 * 
		 * @throws IndexOutOfBoundsException if index is incorrect
		 * @throws NullPointerException if given list is {@code null}
		 */
		public CostEnumerationField(ElementList list, int index){
			super(list, index);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <S extends Field> S selfClone() {
			return (S)new CostEnumerationField(this.list, this.value);
		}
		
		/**
		 * Tells if this field is at least as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtLeastAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value <= ((CostEnumerationField)otherField).value ?
							TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
				}
				catch (ClassCastException exception) {
					return TernaryLogicValue.UNCOMPARABLE;
				}
			}
		}

		/**
		 * Tells if this field is at most as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			if (otherField instanceof UnknownSimpleField) {
				return ((UnknownSimpleField)otherField).reverseIsAtMostAsGoodAs(this); //missing value => delegate comparison to the other field
			} else {
				try {
					return (this.value >= ((CostEnumerationField)otherField).value ?
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
					return (this.value == ((CostEnumerationField)otherField).value ?
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
	 * Constructs enumeration field from its textual representation.
	 * 
	 * @param value textual representation of an enumeration field
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException if given value is not present in given attribute's domain
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidTypeException {@inheritDoc} 
	 */
	@Override
	public EnumerationField create(String value, EvaluationAttribute attribute) {
		Precondition.notNull(attribute, "Attribute used to construct enumeration field is null.");
		if (!(attribute.getValueType() instanceof EnumerationField)) {
			throw new InvalidTypeException("Attribute's value type is not an instance of enumeration field.");
		}
		
		// TODO some optimization is needed here (e.g., construction of a table with element lists)
		int index = ((EnumerationField)attribute.getValueType()).getElementList().getIndex(value);
		if (index != ElementList.DEFAULT_INDEX) {
			return create(((EnumerationField)attribute.getValueType()).getElementList(), index, attribute.getPreferenceType());
		}
		else {
			throw new FieldParseException(new StringBuilder("Incorrect value of enumeration attribute: ").append(value).toString());
		}
	}
	
}
