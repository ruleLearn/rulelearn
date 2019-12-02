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

import java.util.List;
import java.util.Objects;

import org.rulelearn.core.FieldParseException;
import org.rulelearn.core.InvalidTypeException;
import org.rulelearn.core.Precondition;
import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.data.EvaluationAttribute;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Caching factory for {@link EnumerationField} evaluations, employing abstract factory and singleton design patterns.
 * Avoids creating the same object more than once. It caches already constructed objects.
 * In case of a request to construct an enumeration field, it first checks whether a field for the given enumeration value
 * and preference type is already present in the cache. If this is the case, respective {@link EnumerationField} object is returned
 * from that cache. Otherwise, constructs new object, caches it for future reference, and returns that object.<br>
 * <br>
 * This factory employs two types of caches: persistent and volatile one. Cache of the first type can be only extended with new objects (it is never cleared).
 * Cache of the second type is volatile, in the sense that it can be cleared any time using call to {@link #clearVolatileCache()} method.
 * Volatile cache can be useful for building an information table. In such case, one can clear the cache after reading the entire table
 * to prevent high memory consumption by cached fields.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class EnumerationFieldCachingFactory implements EvaluationFieldCachingFactory {
	
	//Maps constituting internal persistent cache, mapping calculated hash of enumeration field to list of enumeration fields having that hash
	Int2ObjectOpenHashMap<List<EnumerationField>> persistentGainCache;
	Int2ObjectOpenHashMap<List<EnumerationField>> persistentCostCache;
	Int2ObjectOpenHashMap<List<EnumerationField>> persistentNoneCache;
	
	//Maps constituting internal volatile cache, mapping calculated hash of enumeration field to list of enumeration fields having that hash
	Int2ObjectOpenHashMap<List<EnumerationField>> volatileGainCache;
	Int2ObjectOpenHashMap<List<EnumerationField>> volatileCostCache;
	Int2ObjectOpenHashMap<List<EnumerationField>> volatileNoneCache;
	
	/**
	 * Provider of the only instance of this factory, local to current thread.
	 */
	static ThreadLocal<EnumerationFieldCachingFactory> factoryProvider = null;
	
	/**
	 * Retrieves the only instance of this factory (singleton). The returned instance is thread-local.
	 * 
	 * @return the only instance of this factory, local to the current thread
	 */
	public static EnumerationFieldCachingFactory getInstance() {
		if (factoryProvider == null) {
			factoryProvider = new ThreadLocal<EnumerationFieldCachingFactory>() {
				@Override
                protected EnumerationFieldCachingFactory initialValue() {
                    return new EnumerationFieldCachingFactory();
                }
			};
		}
		return factoryProvider.get();
	}
	
	/**
	 * Constructor preventing object creation.
	 */
	private EnumerationFieldCachingFactory() {
		initializePersistentCache();
		initializeVolatileCache();
	}
	
	/**
	 * Calculates hash for constructed enumeration field, based on factory parameters.
	 * Returned value can be different than the one obtained by {@link EnumerationField#hashCode()}.  
	 * 
	 * @param list element list of the constructed field
	 * @param index position in the element list of enumeration which represents value of the field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * 
	 * @return hash for constructed enumeration field
	 */
	private int hashEnumerationField(ElementList list, int index, AttributePreferenceType preferenceType) {
		return Objects.hash(preferenceType, index, list);
	}
	
	/**
	 * Factory method for constructing an instance of {@link EnumerationField}.
	 * If available, returns enumeration field from internal cache. Otherwise,
	 * constructs new field.
	 * 
	 * @param list element list of the constructed field
	 * @param index position in the element list of enumeration which represents value of the field
	 * @param preferenceType preference type of the attribute that the field value refers to
	 * @param usePersistentCache tells if persistent (i.e., non-clearable using API of this object) cache should be used;
	 *        if {@code true}, then persistent cache is searched for requested field;
	 *        if {@code false}, then volatile cache is searched for requested field;
	 *        in any case, if an enumeration field for requested value and preference type is already in considered cache,
	 *        it is returned from that cache; otherwise, a new instance of enumeration field is constructed,
	 *        put in the considered cache, and returned
	 * 
	 * @return constructed field
	 * 
	 * @throws NullPointerException if given list or attribute's preference type is {@code null}
	 * @throws IndexOutOfBoundsException if given index is incorrect (i.e., does not match any position of given element list)
	 */
	public EnumerationField create(ElementList list, int index, AttributePreferenceType preferenceType, boolean usePersistentCache) {
		EnumerationField field = null;
		Int2ObjectOpenHashMap<List<EnumerationField>> cache = null;
		
		Precondition.notNull(list, "Element list of constructed enumeration attribute is null.");
		
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
		
		int key = hashEnumerationField(list, index, preferenceType);
		boolean found = false;
		List<EnumerationField> listOfEnumerationFields = null;
		
		if (cache.containsKey(key)) { //there is a list of enumeration fields having calculated hash code
			listOfEnumerationFields = cache.get(key);
			
			//check list
			for (EnumerationField testedField : listOfEnumerationFields) {
				if (testedField.getValue() == index &&
						testedField.getElementList().equals(list)) {
					field = testedField;
					found = true;
					break;
				}
			}
		}
		
		if (!found) {
			field = EnumerationFieldFactory.getInstance().create(list, index, preferenceType);
			List<EnumerationField> fields = (listOfEnumerationFields == null ? new ObjectArrayList<EnumerationField>() : listOfEnumerationFields);
			fields.add(field);
			cache.put(key, fields); //consistently use calculated key, not the hash code returned by the constructed enumeration field!
		}
		
		return field;
		
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
	 * Constructs enumeration field from its textual representation.
	 * If available, returns enumeration field from internal persistent cache.
	 * Otherwise, constructs new field, stores it in the persistent cache and returns it.
	 * 
	 * @param value textual representation of an enumeration field
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException if given value cannot be parsed as an enumeration value (is not present in given attribute's domain)
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getValueType() attribute's value}
	 *         is not {@link EnumerationField}
	 */
	@Override
	public EnumerationField createWithPersistentCache(String value, EvaluationAttribute attribute) {
		Precondition.notNull(attribute, "Attribute used to construct enumeration field is null.");
		if (!(attribute.getValueType() instanceof EnumerationField)) {
			throw new InvalidTypeException("Attribute's value type is not an instance of enumeration field.");
		}
		
		//TODO is some optimization is needed here (e.g., construction of a table with element lists)?
		int index = ((EnumerationField)attribute.getValueType()).getElementList().getIndex(value);
		if (index != ElementList.DEFAULT_INDEX) {
			return create(((EnumerationField)attribute.getValueType()).getElementList(), index, attribute.getPreferenceType(), true);
		}
		else {
			throw new FieldParseException(new StringBuilder("Incorrect value of enumeration attribute: ").append(value).toString());
		}
	}
	
	/**
	 * Constructs enumeration field from its textual representation.
	 * If available, returns enumeration field from internal volatile cache.
	 * Otherwise, constructs new field, stores it in the volatile cache and returns it.
	 * 
	 * @param value textual representation of an enumeration field
	 * @param attribute {@inheritDoc}
	 *  
	 * @return {@inheritDoc}
	 * 
	 * @throws FieldParseException if given value cannot be parsed as an enumeration value (is not present in given attribute's domain)
	 * @throws NullPointerException {@inheritDoc}
	 * @throws InvalidTypeException if type of {@link EvaluationAttribute#getValueType() attribute's value}
	 *         is not {@link EnumerationField}
	 */
	@Override
	public EnumerationField createWithVolatileCache(String value, EvaluationAttribute attribute) {
		Precondition.notNull(attribute, "Attribute used to construct enumeration field is null.");
		if (!(attribute.getValueType() instanceof EnumerationField)) {
			throw new InvalidTypeException("Attribute's value type is not an instance of enumeration field.");
		}
		
		//TODO is some optimization is needed here (e.g., construction of a table with element lists)?
		int index = ((EnumerationField)attribute.getValueType()).getElementList().getIndex(value);
		if (index != ElementList.DEFAULT_INDEX) {
			return create(((EnumerationField)attribute.getValueType()).getElementList(), index, attribute.getPreferenceType(), false);
		}
		else {
			throw new FieldParseException(new StringBuilder("Incorrect value of enumeration attribute: ").append(value).toString());
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
