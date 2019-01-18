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

package org.rulelearn.rules;

/**
 * Exception occurring when the semantics of rule is different than expected (i.e., it might be correct but is not concordant with the expected semantics).
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 *
 */
public class UnknownRuleSemanticsException extends RuntimeException {

	private static final long serialVersionUID = 5034172542445366932L;
	
	public UnknownRuleSemanticsException() {
        super();
    }

	/**
     * Creates an exception with a message.
     * 
     * @param message exception message
     */
    public UnknownRuleSemanticsException(String message) {
        super(message);
    }

    /**
     * Creates an exception without a message but with a pointer to a cause.
     * 
     * @param cause a throwable exception, which caused the {@link UnknownRuleSemanticsException}
     */
    public UnknownRuleSemanticsException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an exception with a message and a pointer to a cause.
     * 
     * @param message exception message
     * @param cause a throwable exception, which caused the {@link UnknownRuleSemanticsException}
     */
    public UnknownRuleSemanticsException(String message, Throwable cause) {
        super(message, cause);
    }

}
