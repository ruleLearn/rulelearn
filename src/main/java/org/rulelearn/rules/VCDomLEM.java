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

import it.unimi.dsi.fastutil.doubles.DoubleList;

/**
 * VC-DomLEM rule inductions algorithm described in:
 * J. Błaszczyński, R. Słowiński, M. Szeląg, Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches.
 * Information Sciences, 181, 2011, pp. 987-1002.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public class VCDomLEM {
	
	/**
	 * Parameters used for rule induction.
	 */
	VCDomLEMParameters vcDomLEMParameters;
	
	public VCDomLEM(VCDomLEMParameters vcDomLEMParameters) {
		this.vcDomLEMParameters = vcDomLEMParameters; //TODO: clone parameters 
	}
	
	public RuleSet generateRules(ApproximatedSetProvider approximatedSetProvider, ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider) {
		return null;
	}
	
	public RuleSet generateRules(ApproximatedSetProvider approximatedSetProvider, ApproximatedSetRuleDecisionsProvider approximatedSetRuleDecisionsProvider,
			DoubleList consistencyThresholds) {
		return null;
	}
	
}
