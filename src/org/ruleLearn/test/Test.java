package org.ruleLearn.test;

import org.ruleLearn.data.AttributePreferenceType;
import org.ruleLearn.types.Field;
import org.ruleLearn.types.IntegerFieldFactory;

/**
 * Tests for ruleLearn preview.
 * 
 * @author Marcin Szeląg
 */
public class Test {

	/**
	 * Program entry point
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		Field integerField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.GAIN);
		Field integerField2 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.GAIN);
		
		System.out.println(integerField1.isAtLeastAsGoodAs(integerField2));
		System.out.println(integerField2.isAtMostAsGoodAs(integerField1));
		
		integerField1 = IntegerFieldFactory.getInstance().create(1, AttributePreferenceType.COST);
		integerField2 = IntegerFieldFactory.getInstance().create(0, AttributePreferenceType.COST);
		
		System.out.println(integerField1.isAtMostAsGoodAs(integerField2));
		System.out.println(integerField2.isAtLeastAsGoodAs(integerField1));
	}

}
