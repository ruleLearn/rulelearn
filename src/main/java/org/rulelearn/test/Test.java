package org.rulelearn.test;

import org.rulelearn.data.AttributePreferenceType;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerFieldFactory;

/**
 * Tests for ruleLearn preview.
 * 
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
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
