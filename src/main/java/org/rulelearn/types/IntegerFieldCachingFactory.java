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
import org.rulelearn.core.Precondition;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Caching factory for {@link IntegerField} evaluations, employing abstract factory and singleton design patterns.
 * Avoids creating the same object more than once. It caches already constructed objects.
 * In case of a request to construct an integer field, it first checks whether a field for the given integer value
 * and preference type is already present in the cache. If this is the case, respective {@link IntegerField} object is returned
 * from that cache. Otherwise, constructs new object, caches it for future reference, and returns that object.<br>
 * <br>
 * This factory employs two types of caches: persistent and volatile one. Cache of the first type can be only extended with new objects (it is never cleared).
 * Cache of the second type is volatile, in the sense that it can be cleared any time using call to {@link #clearVolatileCache()} method.
 * Volatile cache can be useful for building an information table or reading decision rules. In such case, one can clear the volatile cache after reading the entire table
 * or after reading all rules to prevent high memory consumption by cached fields.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class IntegerFieldCachingFactory implements EvaluationFieldCachingFactory {
	
	//Maps constituting internal persistent cache
	Int2ObjectOpenHashMap<IntegerField> persistentGainCache;
	Int2ObjectOpenHashMap<IntegerField> persistentCostCache;
	Int2ObjectOpenHashMap<IntegerField> persistentNoneCache;
	
	//Maps constituting internal volatile cache
	Int2ObjectOpenHashMap<IntegerField> volatileGainCache;
	Int2ObjectOpenHashMap<IntegerField> volatileCostCache;
	Int2ObjectOpenHashMap<IntegerField> volatileNoneCache;
	
	/**
	 * Provider of the only instance of this factory.
	 */
	static ThreadLocal<IntegerFieldCachingFactory> factoryProvider = null;
	
	/**
	 * Retrieves the only instance of this factory (singleton). The returned instance is thread-local.
	 * 
	 * @return the only instance of this factory, local to the current thread
	 */
	public static IntegerFieldCachingFactory getInstance() {
		if (factoryProvider == null) {
			factoryProvider = new ThreadLocal<IntegerFieldCachingFactory>() {
				@Override
                protected IntegerFieldCachingFactory initialValue() {
                    return new IntegerFieldCachingFactory();
                }
			};
		}
		return factoryProvider.get();
	}
	
	/**
	 * Constructor preventing object creation.
	 */
	private IntegerFieldCachingFactory() {
		initializePersistentCache();
		initializeVolatileCache();
	}
	
	/**
	 * Factory method for constructing an instance of {@link IntegerField}.
	 * If available, returns integer field from internal cache. Otherwise,
	 * constructs new field.
	 * 
	 * @param value value of the created field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * @param usePersistentCache tells if persistent (i.e., non-clearable using API of this object) cache should be used;
	 *        if {@code true}, then persistent cache is searched for requested field;
	 *        if {@code false}, then volatile cache is searched for requested field;
	 *        in any case, if an integer field for requested value and preference type is already in considered cache,
	 *        it is returned from that cache; otherwise, a new instance of integer field is constructed,
	 *        put in the considered cache, and returned
	 * 
	 * @return constructed field
	 * 
	 * @throws NullPointerException if given attribute's preference type is {@code null}
	 */
	public IntegerField create(int value, AttributePreferenceType preferenceType, boolean usePersistentCache) {
		IntegerField field;
		Int2ObjectOpenHashMap<IntegerField> cache = null;
		
		switch (Precondition.notNull(preferenceType, "Attribute's preference type is null.")) {
		case GAIN:
			cache = usePersistentCache ? persistentGainCache : volatileGainCache;
			break;
		case COST:
			cache = usePersistentCache ? persistentCostCache : volatileCostCache;
			break;
		case NONE:
			cache = usePersistentCache ? persistentNoneCache : volatileNoneCache;
			break;
		default:
			throw new NullPointerException("Attribute preference type is null.");
		}
		
		if (cache.containsKey(value)) {
			return cache.get(value);
		} else {
			field = IntegerFieldFactory.getInstance().create(value, preferenceType);
			cache.put(value, field);
			return field;
		}
		
	}
	
	/**
	 * Initializes the persistent cache.
	 */
	private void initializePersistentCache() {
		persistentGainCache = new Int2ObjectOpenHashMap<>();
		persistentCostCache = new Int2ObjectOpenHashMap<>();
		persistentNoneCache = new Int2ObjectOpenHashMap<>();
	}
	
	/**
	 * Initializes the volatile cache.
	 */
	private void initializeVolatileCache() {
		volatileGainCache = new Int2ObjectOpenHashMap<>();
		volatileCostCache = new Int2ObjectOpenHashMap<>();
		volatileNoneCache = new Int2ObjectOpenHashMap<>();
	}
	
	/**
	 * Constructs integer field from its textual representation.
	 * If available, returns integer field from internal persistent cache.
	 * Otherwise, constructs new field, stores it in the persistent cache and returns it.
	 * 
	 * @param value textual representation of an integer field
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException if given value cannot be parsed as an integer number
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getValueType() attribute's value}
	 *         is not {@link IntegerField}
	 */
	@Override
	public IntegerField createWithPersistentCache(String value, EvaluationAttribute attribute) {
		Precondition.notNull(attribute, "Attribute used to construct integer field is null.");
		if (!(attribute.getValueType() instanceof IntegerField)) {
			throw new InvalidTypeException("Attribute's value type is not an instance of integer field.");
		}
		
		try {
			return create(Integer.parseInt(value), attribute.getPreferenceType(), true);
		}
		catch (NumberFormatException exception) {
			throw new FieldParseException(new StringBuilder("Incorrect value ").append(value)
					.append(" of integer attribute ").append(attribute.getName()).append(". ").append(exception.getMessage()).toString());
		}
	}
	
	/**
	 * Constructs integer field from its textual representation.
	 * If available, returns integer field from internal volatile cache.
	 * Otherwise, constructs new field, stores it in the volatile cache and returns it.
	 * 
	 * @param value textual representation of an integer field
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException if given value cannot be parsed as an integer number
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getValueType() attribute's value}
	 *         is not {@link IntegerField}
	 */
	@Override
	public IntegerField createWithVolatileCache(String value, EvaluationAttribute attribute) {
		Precondition.notNull(attribute, "Attribute used to construct integer field is null.");
		if (!(attribute.getValueType() instanceof IntegerField)) {
			throw new InvalidTypeException("Attribute's value type is not an instance of integer field.");
		}
		
		try {
			return create(Integer.parseInt(value), attribute.getPreferenceType(), false);
		}
		catch (NumberFormatException exception) {
			throw new FieldParseException(new StringBuilder("Incorrect value ").append(value)
					.append(" of integer attribute ").append(attribute.getName()).append(". ").append(exception.getMessage()).toString());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int clearVolatileCache() {
		int size = volatileNoneCache.size() + volatileCostCache.size() + volatileGainCache.size();
		if (size > 0) {
			initializeVolatileCache();
		}
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVolatileCacheSize() {
		return volatileNoneCache.size() + volatileCostCache.size() + volatileGainCache.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPersistentCacheSize() {
		return persistentNoneCache.size() + persistentCostCache.size() + persistentGainCache.size();
	}
	
}
