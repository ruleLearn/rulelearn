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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import org.rulelearn.core.ReadOnlyArrayReference;
import org.rulelearn.core.ReadOnlyArrayReferenceLocation;
import org.rulelearn.core.TernaryLogicValue;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Immutable list of {@link String} elements.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class ElementList {
	/** 
	 * Default element which must be returned by {@link ElementList#getElement(int)} to denote that the element list does not contain element at the specified index.
	 */
	public final static String DEFAULT_ELEMENT = null;
	
	/** 
	 * Default index which must be returned by {@link ElementList#getIndex(String)} to denote that the element list does not contain specified element.
	 */
	public final static int DEFAULT_INDEX = -1;
	
	/**
	 * Array of {@link String} elements of an enumeration.
	 */
	protected String [] elements = null;
	
	/**
	 * Map of elements of an enumeration.
	 */
	protected transient Object2IntMap<String> map = null;
	
	/**
	 * Default algorithm used to calculate hash value of element list.
	 */
	protected static final String DEFAULT_HASH_ALGORITHM = "SHA-256";
	
	/** 
	 * Algorithm used to calculate hash value of element list.
	 */
	protected String algorithm = DEFAULT_HASH_ALGORITHM;
	
	/**
	 * Hash value used for quicker comparison of element lists.
	 */
	protected transient byte[] hash = null;
	
	/**
	 * Cached hash code.
	 */
	int hashCode;
	
	/**
	 * Tells if hash code have already been cached in {@link #hashCode} field.
	 */
	boolean hashCodeCalculated = false;
	
	/**
	 * Creates an element list and sets element list according to an array of {@link String} elements and creates a hash value using {@link #DEFAULT_HASH_ALGORITHM default hash algorithm}. 
	 * 
	 * @param elements array of {@link String} elements
	 * @throws NullPointerException when elements is null
	 * @throws NoSuchAlgorithmException when {@link #DEFAULT_HASH_ALGORITHM default hash algorithm} is not on the list of algorithms provided in {@link MessageDigest}.
	 */
	public ElementList (String [] elements) throws NoSuchAlgorithmException {
		this(elements, DEFAULT_HASH_ALGORITHM);
	}
	
	/**
	 * Creates an element list and sets element list according to an array of {@link String} elements and creates a hash value using algorithm. 
	 * 
	 * @param elements array of {@link String} elements
	 * @param algorithm algorithm, from the list provided in {@link MessageDigest}, used to calculate hash
	 * 
	 * @throws NullPointerException when elements or algorithm is {@code null}
	 * @throws NoSuchAlgorithmException when algorithm is not on the list of algorithms provided in {@link MessageDigest}
	 */
	public ElementList (String [] elements, String algorithm) throws NoSuchAlgorithmException {
		if (elements != null) {
			this.elements = new String[elements.length];
			for (int i = 0; i < elements.length; i++) {
				this.elements[i] = elements[i];
			}
			initializeMap();
			this.algorithm = algorithm;
			initializeHash();
		}
		else {
			throw new NullPointerException("Array of elements cannot be null.");
		}
	}
	
	/**
	 * Initializes {@link #map} property. Employs {@link #elements} property, so it has to be already set.
	 */
	void initializeMap() {
		int[] indices = new int [elements.length];
		for (int i = 0; i < elements.length; i++) {
			indices[i] = i;
		}
		map = new Object2IntOpenHashMap<String>(elements, indices);
		map.defaultReturnValue(ElementList.DEFAULT_INDEX);
	}
	
	/**
	 * Initializes {@link #hash} property. Employs {@link #algorithm} and {@link #elements} properties, so they have to be already set.
	 * 
	 * @throws NoSuchAlgorithmException when {@link #algorithm} is not on the list of algorithms provided in {@link MessageDigest}
	 */
	void initializeHash() throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance(algorithm);
		for (int i = 0; i < elements.length; i++) {
			m.update(elements[i].getBytes(StandardCharsets.UTF_8));
		}
		hash = m.digest();
	}
	
	/**
	 * Gets element according to the index. In case the element list does not contain element at the specified index {@link ElementList#DEFAULT_ELEMENT} is returned.
	 * 
	 * @param index position of the element
	 * @return {@link String} element
	 */
	public String getElement (int index)  {
		if ((index >= 0) && (index < elements.length)) {
			return elements[index];
		}
		else {
			return ElementList.DEFAULT_ELEMENT;
		}
	}
	
	/**
	 * Gets index of element on the list according to its value. In the case element is not present at the element list returns {@link ElementList#DEFAULT_INDEX}. 
	 * 
	 * @param value value of the element
	 * @return index (position) of the element
	 */
	public int getIndex (String value) {
		if (value != null) {
			return map.getInt(value);
		}
		else {
			return ElementList.DEFAULT_INDEX;
		}
	}
	
	/**
	 * Gets elements.
	 * 
	 * @return array of {@link String} elements
	 */
	@ReadOnlyArrayReference(at = ReadOnlyArrayReferenceLocation.OUTPUT)
	public String [] getElements () {
		return elements;
	}
	
	/**
	 * Gets size of element list.
	 * 
	 * @return size of element list
	 */
	public int getSize () {
		return elements.length;
	}
	
	
	/**
	 * Gets algorithm used to calculate hash value of element list.
	 * 
	 * @return {@link String} name of algorithm
	 */
	public String getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Tells if this element list object is equal to the other object.
	 * 
	 * @param otherObject other object that this object should be compared with
	 * @return {@code true} if this object is equal to the other object,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this) {
			if (otherObject != null && this.getClass().equals(otherObject.getClass())) {
				final ElementList otherList = (ElementList) otherObject;
				return ((this.hasEqualHash(otherList) == TernaryLogicValue.TRUE));
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	/**
     * Gets hash code of this element list.
     *
     * @return hash code of this field
     */
	@Override
	public int hashCode () {
		if (!hashCodeCalculated) {
			hashCode = Objects.hash(this.getClass(), this.elements);
			hashCodeCalculated = true;
		}
		
		return hashCode;
	}

	/**
	 * Gets hash of the element list.
	 * 
	 * @return array of bytes representing hash value
	 */
	public byte [] getHash () {
		return hash;
	}
	
	/**
	 * Tells if this element list is equal to the given other element list (has the same values on the same positions).
	 * 
	 * @param otherList other element list that this element list is being compared to
	 * @return see {@link TernaryLogicValue}
	 */
	public TernaryLogicValue isEqualTo(ElementList otherList) {
		if (otherList != null) {
			String [] otherElements = otherList.getElements();
			if (elements.length == otherElements.length) {
				int i = 0;
				int length = elements.length;
				while ( (i < length) && (elements[i] != null) && (otherElements[i] != null) && 
						(elements[i].compareTo(otherElements[i]) == 0))
					i++;
				if (i < elements.length)
					return TernaryLogicValue.FALSE;
				else
					return TernaryLogicValue.TRUE;
			}
			else {
				return TernaryLogicValue.FALSE;
			}
		}
		else 
			return TernaryLogicValue.UNCOMPARABLE;
	}
	
	/**
	 * Tells if this element list has equal hash value to the given other element list.
	 * 
	 * @param otherList other element list that this element list is being compared to
	 * @return see {@link TernaryLogicValue}
	 */
	public TernaryLogicValue hasEqualHash(ElementList otherList) {
		if (otherList != null) {
			byte [] otherHash = otherList.getHash();
			if (hash.length == otherHash.length) {
				int i = 0;
				int length = hash.length;
				while ((i < length) && (hash[i] == otherHash[i]))
					i++;
				if (i < length)
					return TernaryLogicValue.FALSE;
				else
					return TernaryLogicValue.TRUE;
			}
			else {
				return TernaryLogicValue.FALSE;
			}
		}
		else
			return TernaryLogicValue.UNCOMPARABLE;
	}
	
	/**
	 * Gets text representation of this element list (comma separated list of elements).
	 * 
	 * @return text representation of this element list
	 * @see #serialize()
	 */
	@Override
	public String toString() {
		return serialize();
	}
	
	/**
	 * Gets plain text representation of this element list.
	 * 
	 * @return plain text representation of this element list
	 */
	public String serialize() {
		StringBuilder builder = new StringBuilder("(");
		String separator = ",";
		
		for (int i = 0; i < elements.length; i++) {
			builder.append(elements[i]);
			if (i < elements.length - 1) {
				builder.append(separator);
			}
		}
		
		builder.append(")");
		
		return builder.toString();
	}
	
	
	/**
	 * Sets {@code map} and {@code hash} fields after deserialization provided that
	 * {@code algorithm} is set properly and {@code elements} is not {@code null}.
	 * 
	 * @return this object
	 */
	private Object readResolve() {
		if (elements != null) {
			initializeMap();
			
			if (algorithm == null) {
				algorithm = DEFAULT_HASH_ALGORITHM;
			}
			try {
				initializeHash();
			}
			catch (NoSuchAlgorithmException ex) {
				;
			}
		}
		
	    return this;
	}
	
}
