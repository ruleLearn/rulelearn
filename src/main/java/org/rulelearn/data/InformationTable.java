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

import java.util.List;
import org.rulelearn.types.Field;

/**
 * Table storing data, i.e., fields corresponding to objects and attributes (both condition and description ones).
 * Each field is identified by object's index and attribute's index.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTable {
	
	/**
	 * All attributes of an information table.
	 */
	protected Attribute[] attributes;
	
	/**
	 * Mapper translating object's index to its unique id.
	 */
	protected Index2IdMapper mapper;
	
	/**
	 * Sub-table, corresponding to condition attributes only.
	 */
	protected Table conditionTable;
	/**
	 * Sub-table, corresponding to description attributes only.
	 */
	protected Table descriptionTable;
	
	/**
	 * Information table constructor.
	 * 
	 * @param attributes all attributes of an information table
	 * @param fields list of field vectors; each vector contains condition and description field values
	 *        of a single object of this information table
	 * @param mapper translating object's index to it's unique id
	 */
	public InformationTable(Attribute[] attributes,  List<Field[]> fields, Index2IdMapper mapper) {
		//TODO
	}
	
//	public InformationTable(String metadataPath, String dataPath) {

//}
	
}
