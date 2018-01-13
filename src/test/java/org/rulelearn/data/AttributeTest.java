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


import org.junit.jupiter.api.Test;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Test for {@link Attribute}
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class AttributeTest {

	/**
	 * Tests construction and serialization to JSON
	 */
	@Test
	public void testConstruction01() {
		Attribute attribute = new Attribute("a1", true, AttributeType.CONDITION, AttributeValueType.INTEGER, AttributeMissingValueType.MV2, AttributePreferenceType.NONE);
		
		Moshi moshi = new Moshi.Builder().build();
		JsonAdapter<Attribute> jsonAdapter = moshi.adapter(Attribute.class);

		String json = jsonAdapter.toJson(attribute);
		System.out.println(json);
	}
	
}
