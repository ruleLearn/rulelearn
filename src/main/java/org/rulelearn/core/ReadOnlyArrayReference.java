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
import java.lang.annotation.*;

/**
 * Annotation used for methods that:<br>
 * (1) return (or can return for some parameter values) a reference to an array whose modification (i.e., change of any array's entry)
 * is not safe from the view point of the inner consistency of the encompassing class; such references are generally obtained faster,
 * but are intended only to access (read) elements of an array, and not to remove or replace any of array's elements;
 * if such modifications are required, first the returned array should be cloned to obtain a copy of that array,
 * and all the modifications should be performed on the copy;<br>
 * (2) assume (a priori or for some parameter values) that at least one of the input parameters passed to the method is an array reference
 * that is not going to be used to modify that array outside the method once the method returns;<br>
 * (3) assume (a priori or for some parameter values) that at least one of the input parameters passed to the method is an object that contains
 * a reference to an array (or references to arrays) that is (that are) not going to be used to modify that array (that arrays)
 * outside the method once the method returns.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
@Documented
@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ReadOnlyArrayReference {
	
	/**
	 * Tells where a read only array reference is located at method's signature
	 * (at method's input, at method's output, or at method's input and output simultaneously).
	 * 
	 * @return value encoding location(s) in method signature of read only array references (i.e., references that should not be used
	 *         to change any array's entry after the method returns); see {@link ReadOnlyArrayReferenceLocation}
	 */
	ReadOnlyArrayReferenceLocation at();
}
