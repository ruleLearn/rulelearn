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

package org.rulelearn.rules;

import java.util.function.Function;

import org.rulelearn.core.InvalidValueException;
import org.rulelearn.core.Precondition;
import org.rulelearn.core.UnknownValueException;

/**
 * Rule characteristics filter used to filter using a single rule quality measure, e.g., confidence or coverage factor. 
 *
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class RuleCharacteristicsFilter implements RuleFilter {
	
	/**
	 * Field intended to store reference to a single method of {@link RuleCharacteristics} that returns a {@link Number}, e.g., an {@code int} or a {@code double} value.
	 * This field can be assigned method references like {@code RuleCharacteristics::getSupport}.
	 */
	Function<RuleCharacteristics, Number> calculationMethod;
	
	/**
	 * {@link RuleCharacteristic#getName() Name of rule characteristic} employed by this filter.
	 */
	String ruleCharacteristicName = null;
	
	/**
	 * Relation used to compare return value of the calculation method with a threshold.
	 */
	Relation relation;
	
	/**
	 * Threshold for the return value of chosen calculation method.
	 */
	Number threshold;
	
	/**
	 * Constructs this rule characteristics filter.
	 * 
	 * @param calculationMethod reference to one of the methods of {@link RuleCharacteristics},
	 *        e.g., {@code RuleCharacteristics::getSupport} or {@code RuleCharacteristics::getConfidence}
	 * @param ruleCharacteristicName name of the rule characteristic calculated by the calculation method (it cannot be deduced from lambda passed as the first parameter);
	 *        see {@link RuleCharacteristic#getName()}
	 * @param relation relation used to compare value of chosen method with a threshold
	 * @param threshold threshold for value of chosen method
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleCharacteristicsFilter(Function<RuleCharacteristics, Number> calculationMethod, String ruleCharacteristicName, Relation relation, Number threshold) {
		this.calculationMethod = Precondition.notNull(calculationMethod, "Calculation method for rule characteristics filter is null.");
		this.ruleCharacteristicName = Precondition.notNull(ruleCharacteristicName, "Rule characteristic name is null.");
		this.relation = Precondition.notNull(relation, "Relation for rule characteristics filter is null.");
		this.threshold = Precondition.notNull(threshold, "Threshold for rule characteristics filter is null.");
	}
	
	/**
	 * Constructs this rule characteristics filter.
	 * 
	 * @param ruleCharacteristic rule characteristic employed in calculations, determining calculation method - see {@link RuleCharacteristic#getCalculationMethod}.
	 * @param relation relation used to compare value of chosen method with a threshold
	 * @param threshold threshold for value of chosen method
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 */
	public RuleCharacteristicsFilter(RuleCharacteristic ruleCharacteristic, Relation relation, Number threshold) {
		this(Precondition.notNull(ruleCharacteristic, "Rule characteristic is null.").getCalculationMethod(), ruleCharacteristic.toString(), relation, threshold);
		this.ruleCharacteristicName = ruleCharacteristic.getName(); //store rule characteristic name!
	}
	
	/**
	 * Constructs and returns rule characteristics filter using textual representation of parameters.
	 * 
	 * @param ruleCharacteristic textual representation of rule characteristic, handled by {@link RuleCharacteristic#of(String)}
	 * @param relation textual representation of relation, handled by {@link Relation#of(String)}
	 * @param threshold textual representation of threshold, handled by {@link Double#valueOf(String)}
	 * 
	 * @return constructed rule characteristics filter
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InvalidValueException if textual representation of rule characteristic is incorrect (for correct values see public static fields of {@link RuleCharacteristic} class)
	 * @throws InvalidValueException if textual representation of relation is incorrect (for correct values see public static fields of {@link Relation} class)
	 * @throws NumberFormatException if given threshold cannot be parsed as a {@code double} value
	 */
	public static RuleCharacteristicsFilter of(String ruleCharacteristic, String relation, String threshold) {
		Precondition.notNull(ruleCharacteristic, "Textual representation of rule characteristic is null.");
		Precondition.notNull(relation, "Textual representation of relation is null.");
		Precondition.notNull(threshold, "Textual representation of threshold is null.");
		
		Number number= Double.valueOf(threshold); //may throw NumberFormatException
		if (Double.valueOf(number.intValue()).equals(number)) { //number is in fact an int
			number = Integer.valueOf(number.intValue());
		}
		
		return new RuleCharacteristicsFilter(RuleCharacteristic.of(ruleCharacteristic), Relation.of(relation), number);
	}
	
	/**
	 * Constructs and returns rule characteristics filter using textual representation of the filter.
	 * 
	 * @param ruleCharacteristicFilter textual representation of the filter
	 * @return constructed rule characteristics filter
	 * 
	 * @throws InvalidValueException if given textual representation of filter does not contain any relation sign - see public static fields of {@link Relation} enum.
	 */
	public static RuleCharacteristicsFilter of(String ruleCharacteristicFilter) {
		Precondition.notNull(ruleCharacteristicFilter, "Textual representation of rule characteristics filter is null.");
		
		String[] splitResult;
		if (ruleCharacteristicFilter.contains(Relation.ge)) {
			splitResult = ruleCharacteristicFilter.split(Relation.ge);
			return of(splitResult[0], Relation.ge, splitResult[1]);
		}
		if (ruleCharacteristicFilter.contains(Relation.le)) {
			splitResult = ruleCharacteristicFilter.split(Relation.le);
			return of(splitResult[0], Relation.le, splitResult[1]);
		}
		if (ruleCharacteristicFilter.contains(Relation.gt)) {
			splitResult = ruleCharacteristicFilter.split(Relation.gt);
			return of(splitResult[0], Relation.gt, splitResult[1]);
		}
		if (ruleCharacteristicFilter.contains(Relation.lt)) {
			splitResult = ruleCharacteristicFilter.split(Relation.lt);
			return of(splitResult[0], Relation.lt, splitResult[1]);
		}
		if (ruleCharacteristicFilter.contains(Relation.eq)) {
			splitResult = ruleCharacteristicFilter.split(Relation.eq);
			return of(splitResult[0], Relation.eq, splitResult[1]);
		}
		throw new InvalidValueException("Could not construct rule characteristics filter due to lack of relation.");
	}
	
	/**
	 * Gets calculation method. It is intended to store reference to a single method of {@link RuleCharacteristics} that returns a {@link Number}, e.g., an {@code int} or a {@code double} value.
	 * This field can be assigned method references like {@code RuleCharacteristics::getSupport}.
	 * 
	 * @return the calculation method
	 */
	public Function<RuleCharacteristics, Number> getCalculationMethod() {
		return calculationMethod;
	}

	/**
	 * Gets relation used to compare return value of the calculation method with a threshold.
	 * 
	 * @return relation used to compare return value of the calculation method with a threshold
	 */
	public Relation getRelation() {
		return relation;
	}

	/**
	 * Gets threshold for the return value of chosen calculation method.
	 * 
	 * @return threshold for the return value of chosen calculation method
	 */
	public Number getThreshold() {
		return threshold;
	}

	/**
	 * Tells if a rule having given characteristics, is accepted by this filter. Tests only rule characteristics.
	 * 
	 * @param rule rule to test; can be {@code null} - rule is not taken into account
	 * @param ruleCharacteristics rule characteristics to be tested against this filter
	 * 
	 * @return {@code true} if given rule characteristics are acceptable,
	 *         {@code false} if given rule characteristics do not satisfy this filter
	 *         
	 * @throws UnknownValueException if calculation method for which this filter was constructed throws an {@link UnknownValueException}
	 * @throws NullPointerException if given rule characteristics are {@code null}
	 */
	@Override
	public boolean accepts(Rule rule, RuleCharacteristics ruleCharacteristics) {
		Precondition.notNull(ruleCharacteristics, "Rule characteristics are null.");
		
		try {
			Number result = calculationMethod.apply(ruleCharacteristics);
					
			switch (relation) {
			case GT:
				return result.doubleValue() > threshold.doubleValue();
			case GE:
				return result.doubleValue() >= threshold.doubleValue();
			case EQ:
				return result.doubleValue() == threshold.doubleValue();
			case LE:
				return result.doubleValue() <= threshold.doubleValue();
			case LT:
				return result.doubleValue() < threshold.doubleValue();
			default:
				throw new InvalidValueException("Unsupported value of relation in rule characteristics filter."); //this should not happen
			}
		} catch (UnknownValueException exception) { //value of tested quality measure is not stored in rule characteristics
			throw exception;
		}
	}
	
	/**
	 * Gets textual representation of this rule characteristics filter.
	 */
	@Override
	public String toString() {
		StringBuilder resultBuilder = (new StringBuilder())
				.append(ruleCharacteristicName)
				.append(relation)
				.append(threshold);
		
		return resultBuilder.toString();
	}
}
