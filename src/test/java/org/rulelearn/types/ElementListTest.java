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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.core.TernaryLogicValue;

import com.thoughtworks.xstream.XStream;

/**
 * Test for {@link ElementList}
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class ElementListTest {

	private ElementList list1;
	private ElementList list2;
	private ElementList list3;
	
	private String[] domain1 = {"1", "2", "3"};
	private String[] domain2 = {"1", "2", "30"};
	private String[] domain3 = {"1", "20", "3"};
	private String[] domain4 = {"1", "2", "3", "4"};
	
	private String algorithm1 = "ano";
	private String algorithm2 = "MD5";
	
	private void setUp01() {
		try {
			this.list1 = new ElementList(domain1);
			this.list2 = new ElementList(domain1);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void setUp02() {
		try {
			this.list1 = new ElementList(domain1);
			this.list2 = new ElementList(domain2);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void setUp03() {
		try {
			this.list1 = new ElementList(domain1);
			this.list3 = new ElementList(domain4);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void setUp04() {
		try {
			this.list1 = new ElementList(domain3);
			this.list2 = new ElementList(domain3, algorithm2);
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Tests construction of element list.
	 */
	@Test
	@SuppressWarnings("unused")
	public void testConstruction01() {
		try {
			ElementList list = new ElementList(domain2, algorithm1);
			fail("Construction of element list and calculation of hash with unknown algorithm should fail.");
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Tests construction of element list.
	 */
	@Test
	@SuppressWarnings("unused")
	public void testConstruction02() {
		try {
			ElementList list = new ElementList(null);
			fail("Construction of element list with null content should fail.");
		}
		catch (NullPointerException ex) {
			System.out.println(ex.getMessage());
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Tests construction and comparisons of element lists.
	 */
	@Test
	public void testComparison01() {
		this.setUp01();
		
		assertEquals(list1.hasEqualHash(list2), TernaryLogicValue.TRUE);
		assertEquals(list1.isEqualTo(list2), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests construction and comparisons of element lists.
	 */
	@Test
	public void testComparison02() {
		this.setUp02();
		
		assertEquals(list1.hasEqualHash(list2), TernaryLogicValue.FALSE);
		assertEquals(list1.isEqualTo(list2), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests construction and comparisons of element lists.
	 */
	@Test
	public void testComparison03() {
		this.setUp03();
		
		assertEquals(list1.hasEqualHash(list2), TernaryLogicValue.UNCOMPARABLE);
		assertEquals(list1.hasEqualHash(list3), TernaryLogicValue.FALSE);
		assertEquals(list1.isEqualTo(list2), TernaryLogicValue.UNCOMPARABLE);
		assertEquals(list1.isEqualTo(list3), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Tests construction and comparisons of element lists.
	 */
	@Test
	public void testComparison04() {
		this.setUp04();
		
		assertEquals(list1.hasEqualHash(list2), TernaryLogicValue.FALSE);
		assertEquals(list1.isEqualTo(list2), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Tests construction and size of element lists.
	 */
	@Test
	public void testSize01() {
		this.setUp03();
		
		assertEquals(list1.getSize(), 3);
		assertEquals(list3.getSize(), 4);
	}
	
	/**
	 * Tests construction and getting elements from element lists.
	 */
	@Test
	public void testGetters01() {
		this.setUp01();
		
		assertEquals(list1.getElement(0), "1");
		assertEquals(list1.getIndex("1"), 0);
		assertEquals(list2.getElement(1), "2");
		assertEquals(list2.getIndex("2"), 1);
		assertEquals(list2.getElement(2), "3");
		assertEquals(list2.getIndex("3"), 2);
	}
	
	/**
	 * Tests construction and getting elements from element lists.
	 */
	@Test
	public void testGetters02() {
		this.setUp01();
		
		assertNull(list1.getElement(100));
					
	}
	
	/**
	 * Tests construction and getting elements from element lists.
	 */
	@Test
	public void testGetters03() {
		this.setUp01();
		
		assertTrue(list1.getIndex("100") < 0);
		assertTrue(list1.getIndex(null) < 0);
	}
	
	/**
	 * Tests persistence of calculated hash code.
	 */
	@Test
	public void testHashCode() {
		this.setUp01();
		assertFalse(list1.hashCodeCalculated);
		int hashCode = list1.hashCode();
		assertTrue(list1.hashCodeCalculated);
		assertEquals(list1.hashCode, hashCode);
		assertEquals(list1.hashCode(), hashCode);
	}
	
	/**
	 * Test for {@link ElementList#serialize()} method.
	 */
	@Test
	public void testSerialize() {
		this.setUp01();
		assertEquals(list1.serialize(), "(1,2,3)");
	}
	
	/**
	 * Test for {@link ElementList} serialization with {@code XStream}.
	 */
	@Test
	public void testXStreamSerialize01() {
		this.setUp01();
		
		XStream xstream = new XStream();
		String xml1 = xstream.toXML(list1);
		String xml2 = xstream.toXML(list2);
		ElementList list1d = (ElementList)xstream.fromXML(xml1);
		ElementList list2d = (ElementList)xstream.fromXML(xml2);
		
		assertEquals(list1.hasEqualHash(list1d), TernaryLogicValue.TRUE);
		assertEquals(list1.isEqualTo(list1d), TernaryLogicValue.TRUE);
		assertEquals(list2.hasEqualHash(list2d), TernaryLogicValue.TRUE);
		assertEquals(list2.isEqualTo(list2d), TernaryLogicValue.TRUE);
		assertEquals(list1d.hasEqualHash(list2d), TernaryLogicValue.TRUE);
		assertEquals(list1d.isEqualTo(list2d), TernaryLogicValue.TRUE);
	}
	
	/**
	 * Test for {@link ElementList} serialization with {@code XStream}.
	 */
	@Test
	public void testCXStreamSerialize02() {
		this.setUp02();
		
		XStream xstream = new XStream();
		String xml1 = xstream.toXML(list1);
		String xml2 = xstream.toXML(list2);
		ElementList list1d = (ElementList)xstream.fromXML(xml1);
		ElementList list2d = (ElementList)xstream.fromXML(xml2);
		
		assertEquals(list1.hasEqualHash(list1d), TernaryLogicValue.TRUE);
		assertEquals(list1.isEqualTo(list1d), TernaryLogicValue.TRUE);
		assertEquals(list2.hasEqualHash(list2d), TernaryLogicValue.TRUE);
		assertEquals(list2.isEqualTo(list2d), TernaryLogicValue.TRUE);
		assertEquals(list1d.hasEqualHash(list2d), TernaryLogicValue.FALSE);
		assertEquals(list1d.isEqualTo(list2d), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link ElementList} serialization with {@code XStream}.
	 */
	@Test
	public void testXStreamSerialize03() {
		this.setUp03();
		
		XStream xstream = new XStream();
		String xml1 = xstream.toXML(list1);
		String xml2 = xstream.toXML(list2);
		String xml3 = xstream.toXML(list3);
		ElementList list1d = (ElementList)xstream.fromXML(xml1);
		ElementList list2d = (ElementList)xstream.fromXML(xml2);
		ElementList list3d = (ElementList)xstream.fromXML(xml3);
		
		assertEquals(list1.hasEqualHash(list1d), TernaryLogicValue.TRUE);
		assertEquals(list1.isEqualTo(list1d), TernaryLogicValue.TRUE);
		assertEquals(list3.hasEqualHash(list3d), TernaryLogicValue.TRUE);
		assertEquals(list3.isEqualTo(list3d), TernaryLogicValue.TRUE);
		assertEquals(list1d.hasEqualHash(list2d), TernaryLogicValue.UNCOMPARABLE);
		assertEquals(list1d.hasEqualHash(list3d), TernaryLogicValue.FALSE);
		assertEquals(list1d.isEqualTo(list2d), TernaryLogicValue.UNCOMPARABLE);
		assertEquals(list1d.isEqualTo(list3d), TernaryLogicValue.FALSE);
	}
	
	/**
	 * Test for {@link ElementList} serialization with {@code XStream}.
	 */
	@Test
	public void testXStreamSerialize04() {
		this.setUp04();
		
		XStream xstream = new XStream();
		String xml1 = xstream.toXML(list1);
		String xml2 = xstream.toXML(list2);
		ElementList list1d = (ElementList)xstream.fromXML(xml1);
		ElementList list2d = (ElementList)xstream.fromXML(xml2);
		
		assertEquals(list1.hasEqualHash(list1d), TernaryLogicValue.TRUE);
		assertEquals(list1.isEqualTo(list1d), TernaryLogicValue.TRUE);
		assertEquals(list2.hasEqualHash(list2d), TernaryLogicValue.TRUE);
		assertEquals(list2.isEqualTo(list2d), TernaryLogicValue.TRUE);
		assertEquals(list1d.hasEqualHash(list2d), TernaryLogicValue.FALSE);
		assertEquals(list1d.isEqualTo(list2d), TernaryLogicValue.TRUE);
	}
	
}
