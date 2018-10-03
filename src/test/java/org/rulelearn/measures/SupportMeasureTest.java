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

package org.rulelearn.measures;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rulelearn.data.InformationTable;
import org.rulelearn.rules.Rule;

/**
 * Tests for {@link SupportMeasure}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class SupportMeasureTest {

	@Mock
	private InformationTable informationTableMock;
	
	@Mock 
	private Rule ruleMock;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		when(this.informationTableMock.getNumberOfObjects()).thenReturn(5);
		when(this.ruleMock.supportedBy(0, this.informationTableMock)).thenReturn(true);
		when(this.ruleMock.supportedBy(1, this.informationTableMock)).thenReturn(true);
		when(this.ruleMock.supportedBy(2, this.informationTableMock)).thenReturn(true);
		when(this.ruleMock.supportedBy(3, this.informationTableMock)).thenReturn(true);
		when(this.ruleMock.supportedBy(4, this.informationTableMock)).thenReturn(false);
	}
	
	/**
	 * Test for method {@link SupportMeasure#evaluate(Rule, InformationTable)}.
	 */
	@Test
	void testEvalueate() {
		SupportMeasure support = new SupportMeasure();
		assertEquals(4, support.evaluate(this.ruleMock, this.informationTableMock));
	}

}
