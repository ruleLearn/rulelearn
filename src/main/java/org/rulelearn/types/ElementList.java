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

import org.rulelearn.core.TernaryLogicValue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	protected transient byte [] hash = null;
	
	/**
	 * Creates an element list and sets element list according to an array of {@link String} elements and creates a hash value using {@link DEFAULT_HASH_ALGORITHM} algorithm. 
	 * 
	 * @param elements array of {@link String} elements
	 * @throws NullPointerException when elements is null
	 * @throws NoSuchAlgorithmException when algorithm {@link DEFAULT_HASH_ALGORITHM} is not on the list of algorithms provided in {@link MessageDigest}.
	 */
	public ElementList (String [] elements) throws NullPointerException, NoSuchAlgorithmException {
		this(elements, DEFAULT_HASH_ALGORITHM);
	}
	
	/**
	 * Creates an element list and sets element list according to an array of {@link String} elements and creates a hash value using algorithm. 
	 * 
	 * @param elements array of {@link String} elements
	 * @param algorithm algorithm, from the list provided in {@link MessageDigest}, used to calculate hash
	 * @throws NullPointerException when elements is null
	 * @throws NoSuchAlgorithmException when algorithm is not on the list of algorithms provided in {@link MessageDigest}.
	 */
	public ElementList (String [] elements, String algorithm) throws NullPointerException, NoSuchAlgorithmException {
		if (elements != null) {
			int size = elements.length;
			this.elements = new String [size];
			int [] indices = new int [size];
			for (int i=0; i < elements.length; i++) {
				this.elements[i] = new String(elements[i]);
				indices[i] = i;
			}
			this.map = new Object2IntOpenHashMap<String>(this.elements, indices);
			// TODO set default value for the project
			this.map.defaultReturnValue(Integer.MIN_VALUE);
			
			// calculate hash code
			this.algorithm = algorithm;
			MessageDigest m = MessageDigest.getInstance(algorithm);
			for (int i = 0; i < this.elements.length; i++)
				m.update(this.elements[i].getBytes());
			this.hash = m.digest();
		}
		else {
			throw new NullPointerException("Array of elements cannot be null");
		}
	}
	
	/**
	 * Gets element according to the index.
	 * 
	 * @param index position of the element
	 * @throws ArrayIndexOutOfBoundsException when index is incorrect
	 * @return {@link String} element
	 */
	public String getElement (int index) throws ArrayIndexOutOfBoundsException {
		if ((index >= 0) && (index < elements.length)) {
			return elements[index];
		}
		// TODO exception? default value for the project
		else {
			//return null;
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}
	
	/**
	 * Gets index of element on the list according to its value. If element is not present in the list returns negative value. 
	 * 
	 * @param value value of the element
	 * @return index (position) of the element
	 */
	public int getIndex (String value) {
		if (value != null) {
			return map.getInt(value);
		}
		// TODO exception?
		else {
			return Integer.MIN_VALUE;
		}
	}
	
	/**
	 * Gets elements.
	 * TODO - add javadoc comment concerning returning reference
	 * 
	 * @return array of {@link String} elements
	 */
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
}
