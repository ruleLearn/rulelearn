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

import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.data.EvaluationAttribute;

/**
 * Contract for a factory capable of memory efficient constructing of evaluation fields based on their textual representation and corresponding attribute.
 * It caches already constructed fields, so upon a request to return a field, if possible, it returns a field from internal cache
 * instead of constructing a new instance.<br>
 * <br>
 * This factory employs two types of caches: persistent and volatile one. Cache of the first type can be only extended with new objects (it is never cleared).
 * Cache of the second type is volatile, in the sense that it can be cleared any time using call to {@link #clearVolatileCache()} method.
 * Volatile cache can be useful for building an information table. In such case, one can clear the cache after reading the entire table
 * to prevent high memory consumption by cached fields.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public interface EvaluationFieldCachingFactory extends EvaluationFieldFactory {
	
	/**
	 * {@inheritDoc}
	 * Calls {@link #createWithVolatileCache(String, EvaluationAttribute)}.
	 * 
	 * @param value {@inheritDoc}
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidTypeException {@inheritDoc}
	 */
	default EvaluationField create(String value, EvaluationAttribute attribute) {
		return createWithVolatileCache(value, attribute);
	}
	
	/**
	 * Constructs a field from its textual representation.
	 * If available, returns field from internal persistent cache.
	 * Otherwise, constructs new field, stores it in the persistent cache and returns it.
	 * 
	 * @param value textual representation of a field
	 * @param attribute evaluation attribute for which a field should be constructed
	 *  
	 * @return constructed field
	 * 
	 * @throws FieldParseException if given value cannot be parsed as a value of the given attribute
	 * @throws NullPointerException if given evaluation attribute is {@code null}
	 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getValueType() attribute's value}
	 *         is incompatible with type of constructed evaluation field
	 */
	public EvaluationField createWithPersistentCache(String value, EvaluationAttribute attribute);
	
	/**
	 * Constructs a field from its textual representation.
	 * If available, returns field from internal volatile cache.
	 * Otherwise, constructs new field, stores it in the volatile cache and returns it.
	 * 
	 * @param value textual representation of a field
	 * @param attribute evaluation attribute for which a field should be constructed
	 *  
	 * @return constructed field
	 * 
	 * @throws FieldParseException if given value cannot be parsed as a value of the given attribute
	 * @throws NullPointerException if given evaluation attribute is {@code null}
	 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getValueType() attribute's value}
	 *         is incompatible with type of constructed evaluation field
	 */
	public EvaluationField createWithVolatileCache(String value, EvaluationAttribute attribute);
	
	/**
	 * Clears volatile cache.
	 * 
	 * @return number of entries in the volatile cache that have been removed from that cache
	 *         as the result of its clearing
	 */
	public int clearVolatileCache();
	
	/**
	 * Gets size of the volatile cache, i.e., number  of fields temporarily cached in this factory.
	 * 
	 * @return size of the volatile cache
	 */
	public int getVolatileCacheSize();
	
	/**
	 * Gets size of the persistent cache, i.e., number  of fields permanently cached in this factory.
	 * 
	 * @return size of the persistent cache
	 */
	public int getPersistentCacheSize();

}
