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

package org.rulelearn.rules.ruleml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RuleMLElements} class.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class RuleMLElementsTest {

	/**
	 * Test method for {@link RuleMLElements#getBeginningOfRuleSet(int, String)}.
	 * Tests {@code null) learning data hash.
	 */
	@Test
	void testGetBeginningOfRuleSet01() {
		String beginningOfRuleSet = RuleMLElements.getBeginningOfRuleSet(10, null);
		System.out.println(beginningOfRuleSet);
		assertEquals(beginningOfRuleSet, "<act index=\"10\">" + RuleMLElements.getNewLine());
	}
	
	/**
	 * Test method for {@link RuleMLElements#getBeginningOfRuleSet(int, String)}.
	 * Tests not {@code null) learning data hash.
	 */
	@Test
	void testGetBeginningOfRuleSet02() {
		String hash = "3689106708D2C045E59E2878DBADA2A523E71D65FDAD74F0F3300622C172931A";
		String beginningOfRuleSet = RuleMLElements.getBeginningOfRuleSet(111, hash);
		System.out.println(beginningOfRuleSet);
		assertEquals(beginningOfRuleSet, "<act index=\"111\" learningDataHash=\""+hash+"\">" + RuleMLElements.getNewLine());
	}
	
	/**
	 * Test method for {@link RuleMLElements#getBeginningOfRuleSet(UUID, String)}.
	 * Tests {@code null) learning data hash.
	 */
	@Test
	void testGetBeginningOfRuleSetUUID01() {
		String uuidString = "00000000-0000-0000-0000-000000000000";
		String beginningOfRuleSet = RuleMLElements.getBeginningOfRuleSet(UUID.fromString(uuidString), null);
		System.out.println(beginningOfRuleSet);
		assertEquals(beginningOfRuleSet, "<act index=\""+uuidString+"\">" + RuleMLElements.getNewLine());
	}
	
	/**
	 * Test method for {@link RuleMLElements#getBeginningOfRuleSet(UUID, String)}.
	 * Tests not {@code null) learning data hash.
	 */
	@Test
	void testGetBeginningOfRuleSetUUID02() {
		String uuidString = "00000000-0000-0000-0000-000000000000";
		String hash = "3689106708D2C045E59E2878DBADA2A523E71D65FDAD74F0F3300622C172931A";
		String beginningOfRuleSet = RuleMLElements.getBeginningOfRuleSet(UUID.fromString(uuidString), hash);
		System.out.println(beginningOfRuleSet);
		assertEquals(beginningOfRuleSet, "<act index=\""+uuidString+"\" learningDataHash=\""+hash+"\">" + RuleMLElements.getNewLine());
	}

}
