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

/**
 * Type of negative objects allowed to be covered by {@link RuleConditions rule conditions}, apart from positive objects, and apart from neutral objects,
 * determining the way of construction of the rule conditions.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public enum AllowedNegativeObjectsType {
	/**
	 * Allowed negative objects type used when rule conditions {@link RuleConditions}, apart from covering positive objects,
	 * are also allowed to cover negative objects from the positive region of considered approximated set.
	 * This type concerns certain decision rules only.
	 */
	POSITIVE_REGION,
	/**
	 * Allowed negative objects type used when rule conditions {@link RuleConditions}, apart from covering positive objects,
	 * are also allowed to cover negative objects from the positive and boundary regions of considered approximated set.
	 * This type concerns certain decision rules only.
	 */
	POSITIVE_AND_BOUNDARY_REGIONS,
	/**
	 * Allowed negative objects type used when rule conditions {@link RuleConditions}, apart from covering positive objects,
	 * are also allowed to cover any negative object from considered learning information table.
	 * This type concerns certain decision rules only.
	 */
	ANY_REGION,
	/**
	 * Allowed negative objects type used when rule conditions {@link RuleConditions}, apart from covering positive objects,
	 * are also allowed to cover negative objects from considered (upper) approximation.
	 * This type concerns possible decision rules only.
	 */
	APPROXIMATION
}