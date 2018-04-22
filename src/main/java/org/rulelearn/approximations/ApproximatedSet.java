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

package org.rulelearn.approximations;

import java.util.HashSet;

import org.rulelearn.data.InformationTable;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Top level class for all sets of objects that can be approximated using the rough set concept.
 * TODO: add javadoc
 * TODO: getPOSRegion, ...
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class ApproximatedSet {
	
	protected IntSortedSet lowerApproximation;
	protected IntSortedSet upperApproximation;
	protected IntSortedSet boundary;

	protected InformationTable informationTable;
	
	protected HashSet<Integer> inconsistentObjectsInPositiveRegion = null;
	
	/**
	 * Array with numbers of objects belonging to this set.
	 */
	protected int[] objects = null;
	
	//TODO: add has set storing objects of this approximated set?
	
	public ApproximatedSet(InformationTable informationTable) {
		this.informationTable = informationTable;
	}
}
