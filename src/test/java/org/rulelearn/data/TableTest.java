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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.rulelearn.types.Field;
import org.rulelearn.types.IntegerFieldFactory;
import org.rulelearn.types.UnknownSimpleFieldMV15;
import org.rulelearn.types.UnknownSimpleFieldMV2;

/**
 * Tests for {@link Table}.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
class TableTest {
	
	private int[][] fieldValues = {
			{ 3,  0,   1},
			{ 2, -5,  -3},
			{ 4,  7, -15},
			{-1,  6,  14}
	};
	
	private AttributePreferenceType[] attributePreferenceTypes = {AttributePreferenceType.GAIN, AttributePreferenceType.COST, AttributePreferenceType.GAIN};
	
	private Attribute[] getAttributes() {
		return new Attribute[] {
			new EvaluationAttribute("a0", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, attributePreferenceTypes[0]), new UnknownSimpleFieldMV2(), attributePreferenceTypes[0]),
			new EvaluationAttribute("a1", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, attributePreferenceTypes[1]), new UnknownSimpleFieldMV15(), attributePreferenceTypes[1]),
			new EvaluationAttribute("a2", true, AttributeType.CONDITION, IntegerFieldFactory.getInstance().create(0, attributePreferenceTypes[2]), new UnknownSimpleFieldMV2(), attributePreferenceTypes[2])
		}; 
	}
	
	private Field[][] getFields(int[] objectIndices) {
		Field[][] fields = new Field[objectIndices.length][];
		
		for (int i = 0; i < objectIndices.length; i++) {
			fields[i] = new Field[attributePreferenceTypes.length];
			for (int j = 0; j < fields[i].length; j++) {
				fields[i][j] = IntegerFieldFactory.getInstance().create(fieldValues[objectIndices[i]][j], attributePreferenceTypes[j]);
			}
		}
		
		return fields;
	}
	
	private Table getTable(boolean accelerateByReadOnlyParams) {
		Field[][] fields = this.getFields(new int[]{0, 1, 2, 3});
		Index2IdMapper mapper = new Index2IdMapper(UniqueIdGenerator.getInstance().getUniqueIds(fields.length));
		
		return new Table(this.getAttributes(), fields, mapper, accelerateByReadOnlyParams);
	}

	/**
	 * Test for {@link Table#getField(int, int)} method. Tests 4 x 3 table (4 objects, 3 attributes).
	 */
	@Test
	public void testGetField() {
		Table table = getTable(false);
		
		assertEquals(table.getField(0, 0), IntegerFieldFactory.getInstance().create(fieldValues[0][0], attributePreferenceTypes[0]));
		assertEquals(table.getField(0, 1), IntegerFieldFactory.getInstance().create(fieldValues[0][1], attributePreferenceTypes[1]));
		assertEquals(table.getField(0, 2), IntegerFieldFactory.getInstance().create(fieldValues[0][2], attributePreferenceTypes[2]));
		
		assertEquals(table.getField(1, 0), IntegerFieldFactory.getInstance().create(fieldValues[1][0], attributePreferenceTypes[0]));
		assertEquals(table.getField(1, 1), IntegerFieldFactory.getInstance().create(fieldValues[1][1], attributePreferenceTypes[1]));
		assertEquals(table.getField(1, 2), IntegerFieldFactory.getInstance().create(fieldValues[1][2], attributePreferenceTypes[2]));
		
		assertEquals(table.getField(2, 0), IntegerFieldFactory.getInstance().create(fieldValues[2][0], attributePreferenceTypes[0]));
		assertEquals(table.getField(2, 1), IntegerFieldFactory.getInstance().create(fieldValues[2][1], attributePreferenceTypes[1]));
		assertEquals(table.getField(2, 2), IntegerFieldFactory.getInstance().create(fieldValues[2][2], attributePreferenceTypes[2]));
		
		assertEquals(table.getField(3, 0), IntegerFieldFactory.getInstance().create(fieldValues[3][0], attributePreferenceTypes[0]));
		assertEquals(table.getField(3, 1), IntegerFieldFactory.getInstance().create(fieldValues[3][1], attributePreferenceTypes[1]));
		assertEquals(table.getField(3, 2), IntegerFieldFactory.getInstance().create(fieldValues[3][2], attributePreferenceTypes[2]));
	}

	/**
	 * Test for {@link Table#getFields(int)} method. Tests 4 x 3 table (4 objects, 3 attributes).
	 */
	@Test
	public void testGetFields_01() {
		Table table = getTable(true);
		
		for (int i = 0; i < fieldValues.length; i++) {
			Field[] fields = table.getFields(i);
			for (int j = 0; j < fields.length; j++) {
				assertEquals(fields[j], IntegerFieldFactory.getInstance().create(fieldValues[i][j], attributePreferenceTypes[j]));
			}
		}
	}

	/**
	 * Test for {@link Table#getFields(int, boolean)} method. Tests 4 x 3 table (4 objects, 3 attributes).
	 */
	@Test
	public void testGetFields_02() {
		Table table = getTable(true);
		
		for (int i = 0; i < fieldValues.length; i++) {
			Field[] fields = table.getFields(i, true);
			for (int j = 0; j < fields.length; j++) {
				assertEquals(fields[j], IntegerFieldFactory.getInstance().create(fieldValues[i][j], attributePreferenceTypes[j]));
			}
		}
	}

	/**
	 * Test for {@link Table#select(int[])} method}.
	 */
	@Test
	public void testSelect_01() {
		Table table = getTable(false);
		
		int[] objectIndices = new int[]{0, 2};
		Table newTable = table.select(objectIndices);
		
		assertEquals(newTable.getNumberOfObjects(), objectIndices.length);
		assertEquals(newTable.getNumberOfAttributes(), table.getNumberOfAttributes());

		//check fields of the new table
		Field[][] expectedFields = this.getFields(objectIndices);
		for (int i = 0; i < expectedFields.length; i++) {
			for (int j = 0; j < expectedFields[i].length; j++) {
				assertEquals(expectedFields[i][j], newTable.getField(i, j));
			}
		}
		
		//check attributes of the new table
		Attribute[] expectedAttributes = this.getAttributes();
		Attribute[] attributes = newTable.getAttributes();
		assertEquals(attributes.length, expectedAttributes.length);
		for (int i = 0; i < attributes.length; i++) {
			assertEquals(attributes[i], expectedAttributes[i]);
		}
		
		//check mapper of the new table
		Index2IdMapper newMapper = newTable.getIndex2IdMapper();
		for (int i = 0; i < objectIndices.length; i++) {
			assertEquals(newMapper.getId(i), table.getIndex2IdMapper().getId(objectIndices[i]));
		}
	}
	
	/**
	 * Test for {@link Table#select(int[], boolean)} method}.
	 */
	@Test
	public void testSelect_02() {
		Table table = getTable(false);
		
		int[] objectIndices = new int[]{1, 2, 3};
		Table newTable = table.select(objectIndices, true);
		
		assertEquals(newTable.getNumberOfObjects(), objectIndices.length);
		assertEquals(newTable.getNumberOfAttributes(), table.getNumberOfAttributes());

		//check fields of the new table
		Field[][] expectedFields = this.getFields(objectIndices);
		for (int i = 0; i < expectedFields.length; i++) {
			for (int j = 0; j < expectedFields[i].length; j++) {
				assertEquals(expectedFields[i][j], newTable.getField(i, j));
			}
		}
		
		//check attributes of the new table
		Attribute[] expectedAttributes = this.getAttributes();
		Attribute[] attributes = newTable.getAttributes();
		assertEquals(attributes.length, expectedAttributes.length);
		for (int i = 0; i < attributes.length; i++) {
			assertEquals(attributes[i], expectedAttributes[i]);
		}
		
		//check mapper of the new table
		Index2IdMapper newMapper = newTable.getIndex2IdMapper();
		for (int i = 0; i < objectIndices.length; i++) {
			assertEquals(newMapper.getId(i), table.getIndex2IdMapper().getId(objectIndices[i]));
		}
	}

	/**
	 * Test for {@link Table#getNumberOfObjects()} method}.
	 */
	@Test
	public void testGetNumberOfObjects() {
		Table table = getTable(true);
		assertEquals(table.getNumberOfObjects(), fieldValues.length);
	}

	/**
	 * Test for {@link Table#getNumberOfAttributes()} method}.
	 */
	@Test
	public void testGetNumberOfAttributes() {
		Table table = getTable(true);
		assertEquals(table.getNumberOfAttributes(), attributePreferenceTypes.length);
	}

	/**
	 * Test for {@link Table#getAttributes()} method}.
	 */
	@Test
	public void testGetAttributes_01() {
		Table table = getTable(true);
		Attribute[] expectedAttributes = this.getAttributes();
		Attribute[] attributes = table.getAttributes();
		
		assertEquals(attributes.length, expectedAttributes.length);
		
		for (int i = 0; i < attributes.length; i++) {
			assertEquals(attributes[i], expectedAttributes[i]);
		}
	}

	/**
	 * Test for {@link Table#getAttributes(boolean)} method}.
	 */
	@Test
	public void testGetAttributes_02() {
		Table table = getTable(false);
		Attribute[] expectedAttributes = this.getAttributes();
		Attribute[] attributes = table.getAttributes(true);
		
		assertEquals(attributes.length, expectedAttributes.length);
		
		for (int i = 0; i < attributes.length; i++) {
			assertEquals(attributes[i], expectedAttributes[i]);
		}
	}

	/**
	 * Test for {@link Table#getIndex2IdMapper()} method}.
	 */
	@Test
	public void testGetIndex2IdMapper() {
		Table table = getTable(false);
		Index2IdMapper mapper = table.getIndex2IdMapper();
		
		assertNotEquals(mapper.getId(0), mapper.getId(1));
		assertNotEquals(mapper.getId(1), mapper.getId(2));
		assertNotEquals(mapper.getId(2), mapper.getId(3));
	}

}
