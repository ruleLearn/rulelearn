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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link VCDomLEMParameters}.
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
		VCDomLEMParameters parameters = (new VCDomLEMParameters.VCDomLEMParametersBuilder()).build();
		assertEquals(Arrays.asList(), parameters.getConsistencyThresholds());
		assertNull(parameters.getConditionSeparator());
	}
	
	/**
	 * Test for method {@link VCDomLEMParameters.VCDomLEMParametersBuilder#build()}.
	 */
	@Test
	void testBuilderAndBuilderSetter() {
		// construct builder
		VCDomLEMParameters.VCDomLEMParametersBuilder parametersBuilder = new VCDomLEMParameters.VCDomLEMParametersBuilder().consistencyThreshold(VCDomLEMParameters.DEFAULT_CONSISTENCY_TRESHOLD);
		// build parameters and check them
		VCDomLEMParameters parameters = parametersBuilder.build();
		assertEquals(Arrays.asList(VCDomLEMParameters.DEFAULT_CONSISTENCY_TRESHOLD), parameters.getConsistencyThresholds());
		
		List<Double> thresholds = Arrays.asList(VCDomLEMParameters.DEFAULT_CONSISTENCY_TRESHOLD, 0.1);
		// add single threshold to builder and build parameters
		parameters = parametersBuilder.consistencyThreshold(0.1).build();
		assertEquals(thresholds, parameters.getConsistencyThresholds());
		// clear and set thresholds in builder and build parameters
		parameters = parametersBuilder.clearConsistencyThresholds().consistencyThresholds(thresholds).build();
		assertEquals(thresholds, parameters.getConsistencyThresholds());
	}
}
