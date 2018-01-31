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

import org.rulelearn.core.InvalidValueException;

/**
 * Generator of unique identifiers.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class UniqueIdGenerator {
	
	/**
	 * Next id to be returned by this generator.
	 */
	private int nextUniqueId;
	
	/**
	 * The only instance of this generator.
	 */
	protected static UniqueIdGenerator generator = null;
	
	/**
	 * Sole constructor initializing this generator.
	 */
	private UniqueIdGenerator() {
		this.nextUniqueId = 0;
	}
	
	/**
	 * Retrieves the only instance of this generator (singleton).
	 * 
	 * @return the only instance of this generator
	 */
	public static UniqueIdGenerator getInstance() {
		if (generator == null) {
			generator = new UniqueIdGenerator();
		}
		return generator;
	}
	
	/**
	 * Gets unique identifier (not returned by this generator before).
	 * 
	 * @return unique identifier (not returned by this generator before)
	 */
	public int getUniqueId() {
		return nextUniqueId++;
	}
	
	/**
	 * Gets an array of unique identifiers.
	 * 
	 * @param numberOfIds number of unique identifiers to generate
	 * @return array of unique identifiers
	 * 
	 * @throws InvalidValueException when requested number of unique identifiers to be generated is less than zero
	 */
	public int[] getUniqueIds(int numberOfIds) {
		if (numberOfIds < 0) {
			throw new InvalidValueException("Number of generated unique identifiers cannot be less than zero.");
		}
		int[] ids = new int[numberOfIds];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = nextUniqueId++;
		}
		return ids;
	}

}
