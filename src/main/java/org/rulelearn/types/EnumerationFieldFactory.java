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
 * EnumerationFieldFactory
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class EnumerationFieldFactory {
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
	 * Factory method for creating an instance of {@link EnumerationField}
	 * 
	 * @param set element set of the created field
	 * @param index position in the element set of enumeration which represents value of the field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return created field
	 */
	public EnumerationField create(ElementSet set, int index, AttributePreferenceType preferenceType) {
		switch (preferenceType) {
			case NONE: return new NoneEnumerationField(set, index);
			case GAIN: return new GainEnumerationField(set, index);
			case COST: return new CostEnumerationField(set, index);
			default: return new NoneEnumerationField(set, index);
		}
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link EnumerationField}
	 * 
	 * @param field to be cloned
	 * 
	 * @return created field
	 */
	public EnumerationField clone (NoneEnumerationField field) {
		return new NoneEnumerationField(field.getElementSet(), field.getIndex());
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link EnumerationField}
	 * 
	 * @param field to be cloned
	 * 
	 * @return created field
	 */
	public EnumerationField clone (GainEnumerationField field) {
		return new GainEnumerationField(field.getElementSet(), field.getIndex());
	}
	
	/**
	 * Factory method for cloning/duplicating an instance of {@link EnumerationField}
	 * 
	 * @param field to be cloned
	 * 
	 * @return created field
	 */
	public EnumerationField clone (CostEnumerationField field) {
		return new CostEnumerationField(field.getElementSet(), field.getIndex());
	}
	
	/**
	 * Field representing an enumeration value, for an attribute without preference type.
	 * 
	 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
	 * @author Marcin Szeląg
	 */
	private class NoneEnumerationField extends EnumerationField {
		public NoneEnumerationField(ElementSet set, int index){
			super(set, index);
		}
		
		/**
		 * Tells if this field is equal to the given field.
		 * 
		 * This method does not compare element sets. If necessary element sets should be compared by
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue}
		 */
		public TernaryLogicValue isEqualTo(Field otherField) {
			try {
				return (this.getIndex() == ((EnumerationField)otherField).getIndex() ? 
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			} catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}
		
		/**
		 * Tells if this field is at least as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}

		/**
		 * Tells if this field is at most as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
			return this.isEqualTo(otherField);
		}
	}
	
	/**
	 * Field representing an enumeration value, for an attribute with gain-type preference.
	 * 
	 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
	 * @author Marcin Szeląg
	 */
	private class GainEnumerationField extends EnumerationField {
		public GainEnumerationField(ElementSet set, int index){
			super(set, index);
		}
		
		/**
		 * Tells if this field is at least as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.getIndex() >= ((GainEnumerationField)otherField).getIndex() ?
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
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
			try {
				return (this.getIndex() <= ((GainEnumerationField)otherField).getIndex() ?
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}

		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			try {
				return (this.getIndex() == ((EnumerationField)otherField).getIndex() ? 
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			} catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}

		@Override
		public TernaryLogicValue isDifferentThan(Field otherField) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/**
	 * Field representing an enumeration value, for an attribute with cost-type preference.
	 * 
	 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
	 * @author Marcin Szeląg
	 */
	private class CostEnumerationField extends EnumerationField {
		public CostEnumerationField(ElementSet set, int index){
			super(set, index);
		}
		
		/**
		 * Tells if this field is at least as good as the given field.
		 * 
		 * @param otherField other field that this field is being compared to
		 * @return see {@link TernaryLogicValue} 
		 */
		@Override
		public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
			try {
				return (this.getIndex() <= ((CostEnumerationField)otherField).getIndex() ?
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
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
			try {
				return (this.getIndex() >= ((CostEnumerationField)otherField).getIndex() ?
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			}
			catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}
		
		@Override
		public TernaryLogicValue isEqualTo(Field otherField) {
			try {
				return (this.getIndex() == ((EnumerationField)otherField).getIndex() ?
						TernaryLogicValue.TRUE : TernaryLogicValue.FALSE);
			} catch (ClassCastException exception) {
				return TernaryLogicValue.UNCOMPARABLE;
			}
		}
	}
}
