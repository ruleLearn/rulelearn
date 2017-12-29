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

package org.rulelearn.test;

import java.text.DecimalFormat;

import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.test.types.CostIntegerField;
import org.rulelearn.test.types.GainIntegerField;
import org.rulelearn.test.types.IntegerField;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * FieldTest
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class FieldTest {

	/**
	 * Program entry point
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		
		int n = 100000;
		long bef, aft;
		DecimalFormat df = new DecimalFormat("0.###");
		String line = "";
		
		Field integerField1 = null;
		Field integerField2 = null;
		
		/*
		 * Test with factory 
		 */
		
		bef = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			integerField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.NONE);
			integerField2 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.NONE);
			
			integerField1.isAtLeastAsGoodAs(integerField2);
			integerField2.isAtMostAsGoodAs(integerField1);
			
			integerField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
			integerField2 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
			
			integerField1.isAtLeastAsGoodAs(integerField2);
			integerField2.isAtMostAsGoodAs(integerField1);
			
			integerField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
			integerField2 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
			
			integerField1.isAtMostAsGoodAs(integerField2);
			integerField2.isAtLeastAsGoodAs(integerField1);
		}
		aft = System.currentTimeMillis();
		
		line += "\t" + df.format((aft - bef) / 1000.0);
		
		/*
		 * Test without factory 
		 */
		
		bef = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			integerField1 = new IntegerField(1);
			integerField2 = new IntegerField(0);
			
			integerField1.isAtLeastAsGoodAs(integerField2);
			integerField2.isAtMostAsGoodAs(integerField1);
			
			integerField1 = new GainIntegerField(1);
			integerField2 = new GainIntegerField(0);
			
			integerField1.isAtMostAsGoodAs(integerField2);
			integerField2.isAtLeastAsGoodAs(integerField1);
			
			integerField1 = new CostIntegerField(1);
			integerField2 = new CostIntegerField(0);
			
			integerField1.isAtMostAsGoodAs(integerField2);
			integerField2.isAtLeastAsGoodAs(integerField1);
		}
		aft = System.currentTimeMillis();
		
		line += "\t" + df.format((aft - bef) / 1000.0);
		
		AttributePreferenceType attr[] = {AttributePreferenceType.NONE, AttributePreferenceType.GAIN, AttributePreferenceType.COST};
		
		/*
		 * Test with factory fancy 
		 */
		
		bef = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < attr.length; j++) {
				integerField1 = IntegerFieldFactory.getInstance().create(1, attr[j]);
				integerField2 = IntegerFieldFactory.getInstance().create(0, attr[j]);
				
				integerField1.isAtLeastAsGoodAs(integerField2);
				integerField2.isAtMostAsGoodAs(integerField1);
			}
			
		}
		aft = System.currentTimeMillis();
		
		line += "\n\t" + df.format((aft - bef) / 1000.0);
		
		
		/*
		 * Test without factory fancy
		 */
		
		bef = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < attr.length; j++) {
				switch (attr[j]) {
				case NONE:
					integerField1 = new IntegerField(1);
					integerField2 = new IntegerField(0);
					break;
				case GAIN:
					integerField1 = new GainIntegerField(1);
					integerField2 = new GainIntegerField(0);
					break;
				case COST:
					integerField1 = new CostIntegerField(1);
					integerField2 = new CostIntegerField(0);
					break;
				default:
					break;
				}
			
				integerField1.isAtLeastAsGoodAs(integerField2);
				integerField2.isAtMostAsGoodAs(integerField1);
			}
		}
		aft = System.currentTimeMillis();
		
		line += "\t" + df.format((aft - bef) / 1000.0);
		
		System.out.println("time for building and comparing fields construced by a factory and directly");
		System.out.println(line);
	}
	
}
