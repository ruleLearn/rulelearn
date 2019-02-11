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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link VCDomLEMparameters}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class VCDomLEMParametersTest {

	/**
	 * Test for method {@link VCDomLEMParameters.VCDomLEMParametersBuilder#build()}.
	 */
	@Test
	void testDefaultBuilder() {
		VCDomLEMParameters parameters = new VCDomLEMParameters.VCDomLEMParametersBuilder().build();
		assertEquals(0.0, parameters.getConsistencyThreshold());
		assertNull(parameters.getConditionSeparator());
	}
	
	/**
	 * Test for methods {@link VCDomLEMParameters.VCDomLEMParametersBuilder#build()} and {@link VCDomLEMParameters#setConsistencyThreshold(double)}.
	 */
	@Test
	void testBuilderAndSetter() {
		VCDomLEMParameters parameters = new VCDomLEMParameters.VCDomLEMParametersBuilder().consistencyThreshold(0.9).build();
		assertEquals(0.9, parameters.getConsistencyThreshold());
		parameters.setConsistencyThreshold(0.0);
		assertEquals(0.0, parameters.getConsistencyThreshold());
	}

}
