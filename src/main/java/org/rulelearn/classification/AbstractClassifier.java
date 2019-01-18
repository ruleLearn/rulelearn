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

package org.rulelearn.classification;

import static org.rulelearn.core.Precondition.notNull;

/**
 * Abstract classifier, storing default classification result and implementing {@link Classifier} interface.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class AbstractClassifier implements Classifier {
	
	/**
	 * Default classification result, returned by this classifier if it is unable to calculate such a result.
	 */
	protected ClassificationResult defaultClassificationResult;
	
	/**
	 * Gets default classification result returned by this classifier if it is unable to calculate such a result.
	 * 
	 * @return default classification result returned by this classifier
	 */
	public ClassificationResult getDefaultClassificationResult() {
		return defaultClassificationResult;
	}

	/**
	 * Constructs this classifier.
	 * 
	 * @param defaultClassificationResult default classification result, to be returned by this classifier if it is unable to calculate such result
	 * @throws NullPointerException if given default classification result is {@code null}
	 */
	public AbstractClassifier(ClassificationResult defaultClassificationResult) {
		this.defaultClassificationResult = notNull(defaultClassificationResult, "Default classification result is null.");
	}
}
