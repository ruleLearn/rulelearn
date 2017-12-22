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

package org.ruleLearn.types;

/**
 * Result of comparing two fields preference type of the attribute which values are represented by fields are taken into account. 
 * 
 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
 * @author Marcin Szeląg <marcin.szelag@cs.put.poznan.pl>
 */
public enum FieldComparisonResult {
	/**
	 * Relation in question is verified between two fields.
	 */
	TRUE,
	/**
	 * Relation in question is not verified between two fields.
	 */
	FALSE,
	/**
	 * Two fields are uncomparable (i.e., not-comparable).
	 */
	UNCOMPARABLE
}