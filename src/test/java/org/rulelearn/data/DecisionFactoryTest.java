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

package org.rulelearn.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.rulelearn.types.EvaluationField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.RealFieldFactory;

/**
 * Tests for {@link DecisionFactory}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class DecisionFactoryTest {

	/**
	 * Test method for {@link org.rulelearn.data.DecisionFactory#create(org.rulelearn.types.EvaluationField[], int[])}.
	 * Test if a {@link SimpleDecision} decision is properly constructed.
	 */
	@Test
	void testCreate01() {
		EvaluationField[] evaluations = {RealFieldFactory.getInstance().create(2.5, AttributePreferenceType.GAIN)};
		int[] attributeIndices = {1};
		
		Decision decision = DecisionFactory.INSTANCE.create(evaluations, attributeIndices);
		
		assertTrue(decision instanceof SimpleDecision);
	}
	
	/**
	 * Test method for {@link org.rulelearn.data.DecisionFactory#create(org.rulelearn.types.EvaluationField[], int[])}.
	 * Test if a {@link CompositeDecision} decision is properly constructed.
	 */
	@Test
	void testCreate02() {
		EvaluationField[] evaluations = {
				RealFieldFactory.getInstance().create(2.5, AttributePreferenceType.GAIN),
				IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST)};
		int[] attributeIndices = {3, 7};
		
		Decision decision = DecisionFactory.INSTANCE.create(evaluations, attributeIndices);
		
		assertTrue(decision instanceof CompositeDecision);
	}

}
