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
import org.rulelearn.core.UncomparableException;
import org.rulelearn.data.EvaluationAttribute;

/**
 * Class implementing a missing attribute value handled according to approach denoted by mv_{1.5}. This approach is described in:<br>
 * M. Szeląg, J. Błaszczyński, R. Słowiński, Rough Set Analysis of Classification Data with Missing Values.
 * [In]: L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017,
 * Proceedings, Part I. Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.<br>
 * The crucial definitions of this approach are as follows:<br>
 * <ul>
 * <li>subject y dominates referent x iff for each condition criterion q, y is at least as good as x, i.e.,<br>
 * q(y) is not worse than q(x), or q(y)=*;</li>
 * <li>subject y is dominated by referent x iff for each condition criterion q, y is at most as good as x, i.e.,<br>
 * q(y) is not better than q(x), or q(y)=*.</li>
 * </ul>
 * <br>
 * As an instance of this class does not have internal state, only one such instance is maintained. It can be obtained using the {@link #getInstance()} method.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UnknownSimpleFieldMV15 extends UnknownSimpleField {
	
	/**
	 * The only instance of this class.
	 */
	private static UnknownSimpleFieldMV15 instance = null;
	
	/**
	 * Sole constructor.
	 */
	public UnknownSimpleFieldMV15() {
	}
	
	/**
	 * Gets the only instance of this class.
	 * 
	 * @return the only instance of this class
	 */
	public static UnknownSimpleFieldMV15 getInstance() {
		if (instance == null) {
			instance = new UnknownSimpleFieldMV15();
		}
		return instance;
	}
	
	/**
	 * Checks if the given field is not {@code null} and if this field can be compared with that field (i.e., it is of type {@link SimpleField}).
	 * 
	 * @param otherField other field that this field is being compared to
	 * @return {@code true} if this field can be compared with the other field,<br>
	 *         {@code false} otherwise.
	 * @throws NullPointerException if the other field is {@code null}
	 */
	private boolean canBeComparedWith(Field otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return (otherField instanceof SimpleField);
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
		if (this.canBeComparedWith(otherField)) {
			return 0;
		} else {
			throw new ClassCastException("Other field cannot be compared to this uknown field.");
		}
	}

	/**
	 * Compares the other field to this field. Does the reverse comparison than {@link ComparableExt#compareToEx(Object)}.
	 * 
	 * @param otherField other field to be compared to this field
	 * @return zero, as any other non-null simple field is assumed to be equal to this field
	 * 
	 * @throws NullPointerException if the other field is {@code null}
	 * @throws UncomparableException if the other field is not {@code null} (so one cannot decide
	 *         the result of comparison of the other known simple field to this unknown simple field)
	 */
	@Override
	public int reverseCompareToEx(KnownSimpleField otherField) throws UncomparableException {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			throw new UncomparableException("Other field cannot be compared to this unknown field.");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Field> S selfClone() {
		//return (S)new UnknownSimpleFieldMV15();
		return (S)UnknownSimpleFieldMV15.getInstance();
	}

	@Override
	public TernaryLogicValue isAtLeastAsGoodAs(Field otherField) {
		if (this.canBeComparedWith(otherField)) {
			return TernaryLogicValue.TRUE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue isAtMostAsGoodAs(Field otherField) {
		if (this.canBeComparedWith(otherField)) {
			return TernaryLogicValue.TRUE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue isEqualTo(Field otherField) {
		if (this.canBeComparedWith(otherField)) {
			return TernaryLogicValue.TRUE;
		} else {
			return TernaryLogicValue.UNCOMPARABLE;
		}
	}

	@Override
	public TernaryLogicValue reverseIsAtLeastAsGoodAs(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return TernaryLogicValue.FALSE;
		}
	}

	@Override
	public TernaryLogicValue reverseIsAtMostAsGoodAs(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return TernaryLogicValue.FALSE;
		}
	}

	@Override
	public TernaryLogicValue reverseIsEqualTo(KnownSimpleField otherField) {
		if (otherField == null) {
			throw new NullPointerException("Field is null.");
		} else {
			return TernaryLogicValue.FALSE;
		}
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
	 * @return {@code false}
	 */
	@Override
	public boolean equalWhenReverseComparedToAnyEvaluation() {
		return false;
	}
	
	/**
	 * Factory for {@link UnknownSimpleFieldMV15} evaluations, employing abstract factory and singleton design patterns.
	 * Instead of constructing new objects, employs {@link UnknownSimpleFieldMV15#getInstance() the only instance of unknown simple field of type 1.5}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class UnknownSimpleFieldMV15Factory implements EvaluationFieldFactory {
		
		/**
		 * The only instance of this factory.
		 */
		private static UnknownSimpleFieldMV15Factory factory = null;

		/**
		 * Retrieves {@link UnknownSimpleFieldMV15#getInstance() the only instance of unknown simple field of type 1.5}.
		 * 
		 * @param value any text or {@code null} (not taken into account as missing value of the constructed type does not have state)
		 * @param attribute {@inheritDoc}
		 *  
		 * @return {@inheritDoc}
		 * 
		 * @throws NullPointerException {@inheritDoc}
		 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getMissingValueType() attribute's missing value}
		 *         is not {@link UnknownSimpleFieldMV15}
		 */
		@Override
		public EvaluationField create(String value, EvaluationAttribute attribute) {
			if (!(Precondition.notNull(attribute, "Attribute for construction of an unknown simple field of type 1.5 is null.").getMissingValueType() instanceof UnknownSimpleFieldMV15)) {
				throw new InvalidTypeException("Type of attribute's missing value is not unknown simple field of type 1.5.");
			}
			return UnknownSimpleFieldMV15.getInstance();
		}
		
		/**
		 * Retrieves the only instance of this factory (singleton).
		 * 
		 * @return the only instance of this factory
		 */
		public static UnknownSimpleFieldMV15Factory getInstance() {
			if (factory == null) {
				factory = new UnknownSimpleFieldMV15Factory();
			}
			return factory;
		}
		
	}
	
	/**
	 * Caching factory for {@link UnknownSimpleFieldMV15} evaluations, employing abstract factory and singleton design patterns.
	 * Instead of constructing new objects, employs {@link UnknownSimpleFieldMV15#getInstance() the only instance of unknown simple field of type 1.5}.
	 * Does not perform internal caching.
	 * 
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public static class UnknownSimpleFieldMV15CachingFactory implements EvaluationFieldCachingFactory {
		
		/**
		 * The only instance of this factory.
		 */
		private static UnknownSimpleFieldMV15CachingFactory factory = null;
		
		/**
		 * Retrieves the only instance of this factory (singleton).
		 * 
		 * @return the only instance of this factory
		 */
		public static UnknownSimpleFieldMV15CachingFactory getInstance() {
			if (factory == null) {
				factory = new UnknownSimpleFieldMV15CachingFactory();
			}
			return factory;
		}

		/**
		 * Retrieves {@link UnknownSimpleFieldMV15#getInstance() the only instance of unknown simple field of type 1.5}.
		 * 
		 * @param value any text or {@code null} (not taken into account as missing value of the constructed type does not have state)
		 * @param attribute {@inheritDoc}
		 *  
		 * @return {@inheritDoc}
		 * 
		 * @throws NullPointerException {@inheritDoc}
		 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getMissingValueType() attribute's missing value}
		 *         is not {@link UnknownSimpleFieldMV15}
		 */
		@Override
		public EvaluationField createWithPersistentCache(String value, EvaluationAttribute attribute) {
			if (!(Precondition.notNull(attribute, "Attribute for construction of an unknown simple field of type 1.5 is null.").getMissingValueType() instanceof UnknownSimpleFieldMV15)) {
				throw new InvalidTypeException("Type of attribute's missing value is not unknown simple field of type 1.5.");
			}
			return UnknownSimpleFieldMV15.getInstance();
		}
		
		/**
		 * Retrieves {@link UnknownSimpleFieldMV15#getInstance() the only instance of unknown simple field of type 1.5}.
		 * 
		 * @param value any text or {@code null} (not taken into account as missing value of the constructed type does not have state)
		 * @param attribute {@inheritDoc}
		 *  
		 * @return {@inheritDoc}
		 * 
		 * @throws NullPointerException {@inheritDoc}
		 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getMissingValueType() attribute's missing value}
		 *         is not {@link UnknownSimpleFieldMV15}
		 */
		@Override
		public EvaluationField createWithVolatileCache(String value, EvaluationAttribute attribute) {
			if (!(Precondition.notNull(attribute, "Attribute for construction of an unknown simple field of type 1.5 is null.").getMissingValueType() instanceof UnknownSimpleFieldMV15)) {
				throw new InvalidTypeException("Type of attribute's missing value is not unknown simple field of type 1.5.");
			}
			return UnknownSimpleFieldMV15.getInstance();
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
		 * Tests if {@link UnknownSimpleFieldMV15#getInstance() the only instance of unknown simple field of type 1.5}
		 * has already been initialized.
		 * 
		 * @return {@code 1} if {@link UnknownSimpleFieldMV15#getInstance() the only instance of unknown simple field of type 1.5}
		 *         has been already initialized, {@code 0} otherwise
		 */
		@Override
		public int getPersistentCacheSize() {
			return UnknownSimpleFieldMV15.instance != null ? 1 : 0;
		}
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationFieldFactory getDefaultFactory() {
		return UnknownSimpleFieldMV15Factory.getInstance();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EvaluationFieldCachingFactory getCachingFactory() {
		return UnknownSimpleFieldMV15CachingFactory.getInstance();
	}

	/**
	 * Gets evaluation representing {@link UnknownSimpleFieldMV15 unknown (empty) value of type 1.5}.
	 * This method should never be invoked under normal circumstances.
	 * 
	 * @param missingValueType type of evaluation attribute's missing value, as returned by {@link EvaluationAttribute#getMissingValueType()}
	 * @return evaluation representing {@link UnknownSimpleFieldMV15 unknown (empty) value of type 1.5}
	 * @throws InvalidTypeException if given parameter is not an instance of {@link UnknownSimpleFieldMV15}
	 */
	@Override
	public EvaluationField getUnknownEvaluation(UnknownSimpleField missingValueType) {
		if (missingValueType instanceof UnknownSimpleFieldMV15) {
			return missingValueType;
		} else {
			throw new InvalidTypeException("Missing value is not of type 1.5.");
		}
	}

}
