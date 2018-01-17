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

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.rulelearn.types.ElementList;
import org.rulelearn.types.EnumerationField;
import org.rulelearn.types.EnumerationFieldFactory;
import org.rulelearn.types.IntegerField;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.PairField;
import org.rulelearn.types.RealField;
import org.rulelearn.types.RealFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV2;

import com.google.gson.Gson;

/**
 * Test for {@link Attribute}
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class AttributeTest {

	/**
	 * Tests construction and serialization to JSON
	 */
	@Test
	public void testConstruction01() {
		Attribute[] attributes = new Attribute[5];
		
		//TODO Should be default value taken from a config class
		Attribute attribute = new Attribute("a1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
		attributes[0] = attribute;
		attribute = new Attribute("c1", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN),
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[1] = attribute;
		attribute = new Attribute("c2", true, AttributeType.CONDITION, RealFieldFactory.getInstance().create(0, AttributePreferenceType.COST),
				new UnknownSimpleFieldMV2(), AttributePreferenceType.COST);
		attributes[2] = attribute;
		
		try {
			String [] values = {"0", "1", "2"}; 
			attribute = new Attribute("d1", true, AttributeType.DESCRIPTION, EnumerationFieldFactory.getInstance().create(new ElementList(values), 0, AttributePreferenceType.NONE), 
					new UnknownSimpleFieldMV2(), AttributePreferenceType.NONE);
			attributes[3] = attribute;
			
			String [] labels = {"a", "b", "c"};
			attribute = new Attribute("d", true, AttributeType.DECISION, EnumerationFieldFactory.getInstance().create(new ElementList(labels), 0, AttributePreferenceType.GAIN),
					new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
			attributes[4] = attribute;
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(attributes);
		System.out.println(json);
		
	}
	
	/**
	 * Tests construction and serialization to JSON
	 */
	@Test
	public void testConstruction02() {
		Attribute[] attributes = new Attribute[3];
		//TODO Should be default value taken from a config class
		Attribute attribute = new Attribute("c1", true, AttributeType.CONDITION, 
				new PairField<IntegerField>(IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN)), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[0] = attribute;
		attribute = new Attribute("c2", true, AttributeType.CONDITION, 
				new PairField<RealField>(RealFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN), RealFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN)), 
				new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
		attributes[1] = attribute;
		
		try {
			String [] values = {"0", "1", "2"}; 
			attribute = new Attribute("d1", true, AttributeType.CONDITION, 
					new PairField<EnumerationField>(EnumerationFieldFactory.getInstance().create(new ElementList(values), 0, AttributePreferenceType.GAIN), 
							EnumerationFieldFactory.getInstance().create(new ElementList(values), 1, AttributePreferenceType.GAIN)), 
					new UnknownSimpleFieldMV2(), AttributePreferenceType.GAIN);
			attributes[2] = attribute;
		}
		catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(attributes);
		System.out.println(json);
		
	}
	
	@Test
	public void testReConstruction01() {
		Attribute attribute = null;
		
	}
	
}
