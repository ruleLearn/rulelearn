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

import it.unimi.dsi.fastutil.ints.IntSortedSet;

/**
 * Class implementing classical Dominance-based Rough Set Approach.
 * TODO: write javadoc
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class ClassicalDominanceBasedRoughSetCalculator implements DominanceBasedRoughSetCalculator {

	/* (non-Javadoc)
	 * @see org.rulelearn.approximations.RoughSetCalculator#getUpperApproximation(org.rulelearn.approximations.ApproximatedSet)
	 */
	@Override
	public IntSortedSet calculateUpperApproximation(Union set) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rulelearn.approximations.RoughSetCalculator#getBoundary(org.rulelearn.approximations.ApproximatedSet)
	 */
	@Override
	public IntSortedSet calculateBoundary(Union set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositiveRegion calculateLowerApproximation(Union set) {
		// TODO Auto-generated method stub
		return null;
	}

}
