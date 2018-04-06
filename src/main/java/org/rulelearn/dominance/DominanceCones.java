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

package org.rulelearn.dominance;

import java.util.Set;

/**
 * DominanceRelation
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class DominanceCones {
	/**
	 * Set of positive cones w.r.t. (straight) dominance relation D (x D y <=> x dominates y), one for each object x from an information table.
	 * Formally, D^+(x) = {y \in U : y D x}.
	 */
	protected Set<Integer>[] positiveDCone;
	/**
	 * Set of negative cones w.r.t. (straight) dominance relation D (x D y <=> x dominates y), one for each object x from an information table.
	 * Formally, D^-(x) = {y \in U : x D y}.
	 */
	protected Set<Integer>[] negativeDCone;
	/**
	 * Set of positive cones w.r.t. (inverse) dominance relation InvD (x InvD y <=> x is dominated by y), one for each object x from an information table.
	 * Formally, InvD^+(x) = {y \in U : x InvD y}.
	 */
	protected Set<Integer>[] positiveInvDCone;
	/**
	 * Set of negative cones w.r.t. (inverse) dominance relation InvD (x InvD y <=> x is dominated by y), one for each object x from an information table.
	 * Formally, InvD^-(x) = {y \in U : y InvD x}.
	 */
	protected Set<Integer>[] negativeInvDCone;
	
}
