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

package org.rulelearn.data.json;

import java.lang.reflect.Type;

import org.rulelearn.data.Attribute;
import org.rulelearn.data.InformationTable;
import org.rulelearn.types.Field;
import org.rulelearn.types.UnknownSimpleField;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serializer {@link com.google.gson.JsonSerializer} for information tables {@link org.rulelearn.data.InformationTable}. 
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class InformationTableSerializer implements JsonSerializer<InformationTable> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(InformationTable src, Type typeOfSrc, JsonSerializationContext context) {
		int numObjects = src.getNumberOfObjects(), numAttributes = src.getNumberOfAttributes();
		Attribute[] attributes = src.getAttributes();
		JsonArray jsonInformationTable = new JsonArray(src.getNumberOfObjects());

		for (int i = 0; i < numObjects; i++) {
			JsonObject jsonObjectDescription = new JsonObject();
			for (int j = 0; j < numAttributes; j++) {
				Field field = src.getField(i, j);
				if (!(field instanceof UnknownSimpleField)) {
					jsonObjectDescription.addProperty(attributes[j].getName(), field.toString());
				}
			}
			jsonInformationTable.add(jsonObjectDescription);
		}
		return jsonInformationTable;
	}

}
