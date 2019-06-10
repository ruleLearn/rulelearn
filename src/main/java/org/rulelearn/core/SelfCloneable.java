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

package org.rulelearn.core;

/**
 * Contract for objects that can clone themselves.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 * @param <T> class of cloned objects
 */
public interface SelfCloneable<T> {
	
	/**
	 * Clones this object. Type (class) of the returned clone is defined by the implementing class.
	 * It should be exactly that class.<br>
	 * <br>
	 * This method should be used with caution as internally it casts cloned object to requested return type {@code S}
	 * (determined by the declared type of the variable that the result is assigned to).
	 * This works if {@code S} is the same type as the runtime type of cloned object, or if it is its supertype.
	 * Otherwise, {@link ClassCastException} will be thrown. Therefore, it is recommended that the type of the variable
	 * that the result is assigned to is either the same type or a supertype of the DECLARED type of cloned object. 
	 * 
	 * @return clone of this object
	 * @param <S> subclass of cloned object
	 * 
	 * @throws ClassCastException if type {@code S} is not equal to the runtime type of cloned object or is not a supertype of the runtime type of cloned object
	 */
	public <S extends T> S selfClone();
}
