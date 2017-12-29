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

package org.ruleLearn.types;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * ElementSet
 *
 * @author Jerzy Błaszczyński <jurek.blaszczynski@cs.put.poznan.pl>
 * @author Marcin Szeląg <marcin.szelag@cs.put.poznan.pl>
 *
 */
public class ElementSet {
	/**
	 * Array of names of elements of an enumeration
	 */
	protected String [] elements = null;
	
	/**
	 * Map of names of elements of an enumeration
	 */
	protected Object2IntMap<String> map = null;
	
	/**
	 * Creates an element set and sets element names according to the table. 
	 * 
	 * @param elements string table with names of elements
	 * @throws NullPointerException when elements is null
	 */
	public ElementSet (String [] elements) {
		if (elements != null) {
			int size = elements.length;
			elements = new String [size];
			int [] indices = new int [size];
			for (int i=0; i < elements.length; i++) {
				this.elements[i] = new String(elements[i]);
				indices[i] = i;
			}
			map = new Object2IntOpenHashMap<String>(elements, indices);
			// TODO set default value for the project
			map.defaultReturnValue(Integer.MIN_VALUE);
		}
		else {
			throw new NullPointerException("Array of elements cannot be null");
		}
	}
	
	/**
	 * Gets name of element basing on its index
	 * 
	 * @param index position of the element name
	 * @return name of the element
	 */
	public String getElementName (int index) {
		if ((index >= 0) && (index < elements.length)) {
			return elements[index];
		}
		// TODO exception? default value for the project
		else {
			return null;
		}
	}
	
	/**
	 * Gets index of element basing on its name
	 * 
	 * @param name of the element
	 * @return index (position) of the element
	 */
	public int getIndex (String name) {
		if (name != null) {
			return map.getInt(name);
		}
		// TODO exception?
		else {
			return Integer.MIN_VALUE;
		}
	}
	
	public String [] getElements () {
		return elements;
	}
	
	//TODO comparator?
	public FieldComparisonResult isEqualTo(ElementSet otherSet) {
		if (otherSet != null) {
			String [] otherElements = otherSet.getElements();
			if (elements.length == otherElements.length) {
				int i = 0;
				while ((elements[i] != null) && (otherElements[i] != null) && (elements[i].compareTo(otherElements[i]) == 0))
					i++;
				if (i < elements.length-1)
					return FieldComparisonResult.FALSE;
				else
					return FieldComparisonResult.TRUE;
			}
			else {
				return FieldComparisonResult.FALSE;
			}
		}
		return FieldComparisonResult.UNCOMPARABLE;
	}
	
	//TODO approximate comparator (hash-based)?
	public FieldComparisonResult hasEqualHash(ElementSet otherSet) throws IOException, NoSuchAlgorithmException {
		if (otherSet != null) {
			String [] otherElements = otherSet.getElements();
			if (elements.length == otherElements.length) {
				// calculate hash code of the elements array
				MessageDigest m = MessageDigest.getInstance("SHA1");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    ObjectOutputStream oos = new ObjectOutputStream(baos);
			    oos.writeObject(elements);
			    oos.close();
				m.update(baos.toByteArray());
				byte [] der = m.digest();
				// calculate hash code of the otherElements array
				oos = new ObjectOutputStream(baos);
			    oos.writeObject(otherElements);
			    oos.close();
				m.update(baos.toByteArray());
				byte [] doer = m.digest();
				// compare hash codes
				if (MessageDigest.isEqual(der, doer))
					return FieldComparisonResult.TRUE;
				else
					return FieldComparisonResult.FALSE;
			}
		    else {
				return FieldComparisonResult.FALSE;
			}
		}
		return FieldComparisonResult.UNCOMPARABLE;
	}
}
