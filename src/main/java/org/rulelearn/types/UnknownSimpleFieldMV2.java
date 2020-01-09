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

import java.util.Objects;

import org.rulelearn.core.ComparableExt;
import org.rulelearn.core.EvaluationFieldCalculator;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.TernaryLogicValue;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;

/**
 * Class implementing a missing attribute value handled according to approach denoted by mv_2. This approach is described in:<br>
 * M. Szeląg, J. Błaszczyński, R. Słowiński, Rough Set Analysis of Classification Data with Missing Values.
 * [In]: L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017,
 * Proceedings, Part I. Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.<br>
 * The crucial definitions of this approach are as follows:<br>
 * <ul>
 * <li>subject y dominates referent x iff for each condition criterion q, y is at least as good as x, i.e.,<br>
 * q(y) is not worse than q(x), or q(y)=*, or q(x)=*;</li>
 * <li>subject y is dominated by referent x iff for each condition criterion q, y is at most as good as x, i.e.,<br>
 * q(y) is not better than q(x), or q(y)=*, or q(x)=*.</li>
 * </ul>
 * <br>
 * As an instance of this class does not have internal state, only one such instance is maintained. It can be obtained using the {@link #getInstance()} method.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnknownSimpleFieldMV2 extends UnknownSimpleField {
	
	/**
	 * The only instance of this class.
	 */
	private static UnknownSimpleFieldMV2 instance = null;
	
	/**
	 * Sole constructor.
	 */
	public UnknownSimpleFieldMV2() {
	}
	
	/**
	 * Gets the only instance of this class.
	 * 
	 * @return the only instance of this class
	 */
	public static UnknownSimpleFieldMV2 getInstance() {
		if (instance == null) {
			instance = new UnknownSimpleFieldMV2();
		}
		return instance;
	}
	
	/**
	 * Checks if the given field is not {@code null} and if this field can be compared with that field (i.e., it is of type {@link SimpleField}).
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@link TernaryLogicValue#TRUE} if this field can be compared with the other field,<br>
	 *         {@link TernaryLogicValue#FALSE} otherwise.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	private TernaryLogicValue canBeComparedWith(Field otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return (otherField instanceof SimpleField) ? TernaryLogicValue.TRUE : TernaryLogicValue.UNCOMPARABLE;
		}
	}

	/**
	 * Compares this field with the other field.
	 * 
	 * @param otherField other field to which this field is being compared to
	 * @return zero, if the other field is an instance of {@link SimpleField}
	 * 
	 * @throws ClassCastException if the other field is not an instance of {@link SimpleField}
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int compareToEx(EvaluationField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Other field is null.");
		} else if (otherField instanceof SimpleField) {
			return 0;
		} else {
			throw new ClassCastException("Other field is not a simple field.");
		}
	}
	
	/**
	 * Compares the other field to this field. Does the reverse comparison than {@link ComparableExt#compareToEx(Object)}.
	 * 
	 * @param otherField other field to be compared to this field
	 * @return zero, if the other field is an instance of {@link SimpleField}
	 * 
	 * @throws NullPointerException if the other field is {@code null}
	 */
	@Override
	public int reverseCompareToEx(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Other field is null.");
		} else {
			return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Field> S selfClone() {
		//return (S)new UnknownSimpleFieldMV2();
		return (S)UnknownSimpleFieldMV2.getInstance();
	}

	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue isEqualTo(Field otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue reverseIsAtLeastAsGoodAs(KnownSimpleField otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue reverseIsAtMostAsGoodAs(KnownSimpleField otherField) {
		return this.canBeComparedWith(otherField);
	}

	@Override
	public TernaryLogicValue reverseIsEqualTo(KnownSimpleField otherField) {
		return this.canBeComparedWith(otherField);
	}
	
	/**
	 * Tells if this field object is equal to the other object.
	 * 
	 * @param otherObject other object that this object should be compared with
	 * @return {@code true} if this object is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this) {
			return ((otherObject != null) && (this.getClass().equals(otherObject.getClass())));
		} else {
			return true;
		}
	}
	
	/**
     * Gets hash code of this field.
     *
     * @return hash code of this field
     */
	@Override
	public int hashCode () {
		return Objects.hash(this.getClass());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "?";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationField calculate(EvaluationFieldCalculator calculator, EvaluationField otherField) {
		return calculator.calculate(this, otherField);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 */
	@Override
	public boolean equalWhenComparedToAnyEvaluation() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 */
	@Override
	public boolean equalWhenReverseComparedToAnyEvaluation() {
		return true;
	}
	
	/**
	 * Factory for {@link UnknownSimpleFieldMV2} evaluations, employing abstract factory and singleton design patterns.
	 * Instead of constructing new objects, employs {@link UnknownSimpleFieldMV2#getInstance() the only instance of unknown simple field of type 2}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class UnknownSimpleFieldMV2Factory implements EvaluationFieldFactory {
		
		/**
		 * The only instance of this factory.
		 */
		private static UnknownSimpleFieldMV2Factory factory = null;

		/**
		 * Retrieves {@link UnknownSimpleFieldMV2#getInstance() the only instance of unknown simple field of type 2}.
		 * 
		 * @param value any text or {@code null} (not taken into account as missing value of the constructed type does not have state)
		 * @param attribute {@inheritDoc}
		 *  
		 * @return {@inheritDoc}
		 * 
		 * @throws NullPointerException {@inheritDoc}
		 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getMissingValueType() attribute's missing value}
		 *         is not {@link UnknownSimpleFieldMV2}
		 */
		@Override
		public EvaluationField create(String value, EvaluationAttribute attribute) {
			if (!(Precondition.notNull(attribute, "Attribute for construction of an unknown simple field of type 2 is null.").getMissingValueType() instanceof UnknownSimpleFieldMV2)) {
				throw new InvalidTypeException("Type of attribute's missing value is not unknown simple field of type 2.");
			}
			return UnknownSimpleFieldMV2.getInstance();
		}
		
		/**
		 * Retrieves the only instance of this factory (singleton).
		 * 
		 * @return the only instance of this factory
		 */
		public static UnknownSimpleFieldMV2Factory getInstance() {
			if (factory == null) {
				factory = new UnknownSimpleFieldMV2Factory();
			}
			return factory;
		}
		
	}
	
	/**
	 * Caching factory for {@link UnknownSimpleFieldMV2} evaluations, employing abstract factory and singleton design patterns.
	 * Instead of constructing new objects, employs {@link UnknownSimpleFieldMV2#getInstance() the only instance of unknown simple field of type 2}.
	 * Does not perform internal caching.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class UnknownSimpleFieldMV2CachingFactory implements EvaluationFieldCachingFactory {
		
		/**
		 * The only instance of this factory.
		 */
		private static UnknownSimpleFieldMV2CachingFactory factory = null;
		
		/**
		 * Retrieves the only instance of this factory (singleton).
		 * 
		 * @return the only instance of this factory
		 */
		public static UnknownSimpleFieldMV2CachingFactory getInstance() {
			if (factory == null) {
				factory = new UnknownSimpleFieldMV2CachingFactory();
			}
			return factory;
		}

		/**
		 * Retrieves {@link UnknownSimpleFieldMV2#getInstance() the only instance of unknown simple field of type 2}.
		 * 
		 * @param value any text or {@code null} (not taken into account as missing value of the constructed type does not have state)
		 * @param attribute {@inheritDoc}
		 *  
		 * @return {@inheritDoc}
		 * 
		 * @throws NullPointerException {@inheritDoc}
		 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getMissingValueType() attribute's missing value}
		 *         is not {@link UnknownSimpleFieldMV2}
		 */
		@Override
		public EvaluationField createWithPersistentCache(String value, EvaluationAttribute attribute) {
			if (!(Precondition.notNull(attribute, "Attribute for construction of an unknown simple field of type 2 is null.").getMissingValueType() instanceof UnknownSimpleFieldMV2)) {
				throw new InvalidTypeException("Type of attribute's missing value is not unknown simple field of type 2.");
			}
			return UnknownSimpleFieldMV2.getInstance();
		}
		
		/**
		 * Retrieves {@link UnknownSimpleFieldMV2#getInstance() the only instance of unknown simple field of type 2}.
		 * 
		 * @param value any text or {@code null} (not taken into account as missing value of the constructed type does not have state)
		 * @param attribute {@inheritDoc}
		 *  
		 * @return {@inheritDoc}
		 * 
		 * @throws NullPointerException {@inheritDoc}
		 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getMissingValueType() attribute's missing value}
		 *         is not {@link UnknownSimpleFieldMV2}
		 */
		@Override
		public EvaluationField createWithVolatileCache(String value, EvaluationAttribute attribute) {
			if (!(Precondition.notNull(attribute, "Attribute for construction of an unknown simple field of type 2 is null.").getMissingValueType() instanceof UnknownSimpleFieldMV2)) {
				throw new InvalidTypeException("Type of attribute's missing value is not unknown simple field of type 2.");
			}
			return UnknownSimpleFieldMV2.getInstance();
		}

		/**
		 * No operation method, as this factory does not use volatile cache.
		 * 
		 * @return 0
		 */
		@Override
		public int clearVolatileCache() {
			return 0;
		}

		/**
		 * Constant return value method, as this factory does not use volatile cache.
		 * 
		 * @return 0
		 */
		@Override
		public int getVolatileCacheSize() {
			return 0;
		}

		/**
		 * Tests if {@link UnknownSimpleFieldMV2#getInstance() the only instance of unknown simple field of type 2}
		 * has already been initialized.
		 * 
		 * @return {@code 1} if {@link UnknownSimpleFieldMV2#getInstance() the only instance of unknown simple field of type 2}
		 *         has been already initialized, {@code 0} otherwise
		 */
		@Override
		public int getPersistentCacheSize() {
			return UnknownSimpleFieldMV2.instance != null ? 1 : 0;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationFieldFactory getDefaultFactory() {
		return UnknownSimpleFieldMV2Factory.getInstance();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationFieldCachingFactory getCachingFactory() {
		return UnknownSimpleFieldMV2CachingFactory.getInstance();
	}

	/**
	 * Gets evaluation representing {@link UnknownSimpleFieldMV2 unknown (empty) value of type 2}.
	 * This method should never be invoked under normal circumstances.
	 * 
	 * @param missingValueType type of evaluation attribute's missing value, as returned by {@link EvaluationAttribute#getMissingValueType()}
	 * @return evaluation representing {@link UnknownSimpleFieldMV2 unknown (empty) value of type 2}
	 * @throws InvalidTypeException if given parameter is not an instance of {@link UnknownSimpleFieldMV2}
	 */
	@Override
	public EvaluationField getUnknownEvaluation(UnknownSimpleField missingValueType) {
		if (missingValueType instanceof UnknownSimpleFieldMV2) {
			return missingValueType;
		} else {
			throw new InvalidTypeException("Missing value is not of type 2.");
		}
	}
	
	/**
	 * Returns the only instance of this class - see {@link #getInstance()}.
	 * 
	 * @param attributePreferenceType {@link AttributePreferenceType attribute preference type} that should be taken into account
	 *        when cloning this evaluation field
	 * @return the only instance of this class
	 */
	public UnknownSimpleFieldMV2 clone(AttributePreferenceType attributePreferenceType) {
		return getInstance();
	}

}
