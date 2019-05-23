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

package org.rulelearn.types;

import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Integer field factory that tries to avoid creating the same object more than once. It caches already constructed objects.
 * In case of a request to construct an integer field, it first checks whether a field for the given integer value
 * and preference type is already present in the cache. If this is the case, respective {@link IntegerField} object is returned
 * from that cache. Otherwise, constructs new object and caches it for future reference. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class MemoryEfficientIntegerFieldFactory implements EvaluationFieldFactory<IntegerField> {
	
	Int2ObjectOpenHashMap<IntegerField> gainCache;
	Int2ObjectOpenHashMap<IntegerField> costCache;
	Int2ObjectOpenHashMap<IntegerField> noneCache;
	
	IntegerFieldFactory factory = IntegerFieldFactory.getInstance();
	
	/**
	 * Sole constructor initializing empty cache.
	 */
	public MemoryEfficientIntegerFieldFactory() {
		initialize();
	}
	
	/**
	 * Factory method for creating an instance of {@link IntegerField}.
	 * If available, returns integer field from internal cache. Otherwise,
	 * constructs new field.
	 * 
	 * @param value value of the created field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return created field
	 */
	public IntegerField create(int value, AttributePreferenceType preferenceType) {
		IntegerField field;
		switch (preferenceType) {
		case GAIN:
			if (gainCache.containsKey(value)) {
				return gainCache.get(value);
			} else {
				field = factory.create(value, preferenceType);
				gainCache.put(value, field);
				return field;
			}
		case COST:
			if (costCache.containsKey(value)) {
				return costCache.get(value);
			} else {
				field = factory.create(value, preferenceType);
				costCache.put(value, field);
				return field;
			}
		case NONE:
			if (noneCache.containsKey(value)) {
				return noneCache.get(value);
			} else {
				field = factory.create(value, preferenceType);
				noneCache.put(value, field);
				return field;
			}
		default:
			throw new NullPointerException("Preference type is null.");
		}
		
	}
	
	/**
	 * Initializes the cache.
	 */
	private void initialize() {
		gainCache = new Int2ObjectOpenHashMap<>();
		costCache = new Int2ObjectOpenHashMap<>();
		noneCache = new Int2ObjectOpenHashMap<>();
	}
	
	/**
	 * Constructs integer field from its textual representation.
	 * If available, returns integer field from internal cache. Otherwise,
	 * constructs new field.
	 * 
	 * @param value textual representation of an integer field
	 * @param attribute evaluation attribute for which a field should be constructed
	 *  
	 * @return constructed field
	 * 
	 * @throws NumberFormatException if given value cannot be parsed as an integer
	 * @throws NullPointerException if given evaluation attribute is {@code null}
	 */
	@Override
	public IntegerField create(String value, EvaluationAttribute attribute) {
		try {
			return create(Integer.parseInt(value), attribute.getPreferenceType());
		}
		catch (NumberFormatException exception) {
			throw exception;
		}
	}
}
