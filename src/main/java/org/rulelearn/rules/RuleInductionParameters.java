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
 * Stores and provides all parameters associated with induction of rules.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class RuleInductionParameters {
		
	private final ConditionGenerator conditionGenerator;

	private final RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker;
		
	private final ConditionSeparator conditionSeparator;
	
	private final RuleConditionsPruner ruleConditionsPruner;
	
	private final RuleConditionsSetPruner ruleConditionsSetPruner;
	
	private final RuleMinimalityChecker ruleMinimalityChecker;
	
	private final RuleType ruleType;
	
	private final AllowedObjectsType allowedObjectsType;
	
	public interface Builder {
		
		public Builder conditionGenerator(ConditionGenerator conditionGenerator);
		
		public Builder ruleInductionStoppingConditionChecker(RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker);
		
		public Builder conditionSeparator(ConditionSeparator conditionSeparator);
		
		public Builder ruleConditionsPruner(RuleConditionsPruner ruleConditionsPruner);
		
		public Builder ruleConditionsSetPruner(RuleConditionsSetPruner ruleConditionsSetPruner);
		
		public Builder ruleMinimalityChecker(RuleMinimalityChecker ruleMinimalityChecker); 
		
		public Builder ruleType(RuleType ruleType);
		
		public ConditionGenerator conditionGenerator();
		
		public RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker();
		
		public ConditionSeparator conditionSeparator();
		
		public RuleConditionsPruner ruleConditionsPruner();
		
		public RuleConditionsSetPruner ruleConditionsSetPruner();
		
		public RuleMinimalityChecker ruleMinimalityChecker(); 
		
		public RuleType ruleType();
		
		public AllowedObjectsType allowedObjectsType ();
		
		public RuleInductionParameters build();
		
	}
	
	protected RuleInductionParameters(Builder builder) {
		this.conditionGenerator = builder.conditionGenerator();
		this.ruleInductionStoppingConditionChecker = builder.ruleInductionStoppingConditionChecker();
		this.conditionSeparator = builder.conditionSeparator();
		this.ruleConditionsPruner = builder.ruleConditionsPruner();
		this.ruleConditionsSetPruner = builder.ruleConditionsSetPruner();
		this.ruleMinimalityChecker = builder.ruleMinimalityChecker();
		this.ruleType = builder.ruleType();
		this.allowedObjectsType = builder.allowedObjectsType();
	}

	/**
	 * @return the conditionGenerator
	 */
	public ConditionGenerator getConditionGenerator() {
		return conditionGenerator;
	}

	/**
	 * @return the ruleInductionStoppingConditionChecker
	 */
	public RuleInductionStoppingConditionChecker getRuleInductionStoppingConditionChecker() {
		return ruleInductionStoppingConditionChecker;
	}

	/**
	 * @return the conditionSeparator
	 */
	public ConditionSeparator getConditionSeparator() {
		return conditionSeparator;
	}

	/**
	 * @return the ruleConditionsPruner
	 */
	public RuleConditionsPruner getRuleConditionsPruner() {
		return ruleConditionsPruner;
	}

	/**
	 * @return the ruleConditionsSetPruner
	 */
	public RuleConditionsSetPruner getRuleConditionsSetPruner() {
		return ruleConditionsSetPruner;
	}

	/**
	 * @return the ruleMinimalityChecker
	 */
	public RuleMinimalityChecker getRuleMinimalityChecker() {
		return ruleMinimalityChecker;
	}

	/**
	 * @return the ruleType
	 */
	public RuleType getRuleType() {
		return ruleType;
	}
	
	/**
	 * @return the allowedObjectsType
	 */
	public AllowedObjectsType getAllowedObjectsType() {
		return allowedObjectsType;
	}
	
}
