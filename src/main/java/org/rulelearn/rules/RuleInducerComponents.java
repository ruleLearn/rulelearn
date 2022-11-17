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
 * Stores and provides components and parameters associated with induction of decision rules with sequential covering algorithms.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 */
public abstract class RuleInducerComponents {
		
	private final ConditionGenerator conditionGenerator;

	private final RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker;
		
	private final ConditionSeparator conditionSeparator;
	
	private final RuleConditionsPruner ruleConditionsPruner;
	
	private final RuleConditionsGeneralizer ruleConditionsGeneralizer;
	
	private final RuleConditionsSetPruner ruleConditionsSetPruner;
	
	private final RuleMinimalityChecker ruleMinimalityChecker;
	
	private final RuleType ruleType;
	
	private final AllowedNegativeObjectsType allowedNegativeObjectsType;
	
	/**
	 * Contract of a builder of {@link RuleInducerComponents rule inducer components}.
	 *
	 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
	 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
	 */
	public interface Builder {
		
		/**
		 * Sets condition generator {@link ConditionGenerator} used in rule induction process.
		 * 
		 * @param conditionGenerator a condition generator
		 * @return this builder
		 */
		public Builder conditionGenerator(ConditionGenerator conditionGenerator);
		
		/**
		 * Sets checker used to determine occurrence of stopping condition of rule induction process {@link RuleInductionStoppingConditionChecker}.
		 * 
		 * @param ruleInductionStoppingConditionChecker a checker of stopping condition of rule induction process
		 * @return this builder
		 */
		public Builder ruleInductionStoppingConditionChecker(RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker);
		
		/**
		 * Sets the condition separator {@link ConditionSeparator} used to transform conditions constructed on composite fields into conditions expressed in terms of simple fields. 
		 * 
		 * @param conditionSeparator a condition separator
		 * @return this builder
		 */
		public Builder conditionSeparator(ConditionSeparator conditionSeparator);
		
		/**
		 * Sets pruner of rule conditions {@link RuleConditionsPruner} used to remove redundant conditions from rule conditions.
		 * 
		 * @param ruleConditionsPruner a rule conditions pruner
		 * @return this builder
		 */
		public Builder ruleConditionsPruner(RuleConditionsPruner ruleConditionsPruner);
		
		/**
		 * Sets {@link RuleConditionsGeneralizer generalizer of rule conditions} used to generalize conditions from rule conditions.
		 * 
		 * @param ruleConditionsGeneralizer a rule conditions generalizer
		 * @return this builder
		 */
		public Builder ruleConditionsGeneralizer(RuleConditionsGeneralizer ruleConditionsGeneralizer);
		
		/**
		 * Sets pruner of rule conditions set {@link RuleConditionsSetPruner} used to remove redundant rules from rule conditions set.
		 * 
		 * @param ruleConditionsSetPruner a rule conditions set pruner
		 * @return this builder
		 */
		public Builder ruleConditionsSetPruner(RuleConditionsSetPruner ruleConditionsSetPruner);
		
		/**
		 * Sets the rule minimality checker {@link RuleMinimalityChecker} used to induce rules.
		 * 
		 * @param ruleMinimalityChecker a rule minimality checker
		 * @return this builder
		 */
		public Builder ruleMinimalityChecker(RuleMinimalityChecker ruleMinimalityChecker); 
		
		/**
		 * Sets type of decision rule, i.e., type of rules to be induced.
		 * 
		 * @param ruleType type of decision rule
		 * @return this builder
		 */
		public Builder ruleType(RuleType ruleType);
		
		//public allowedNegativeObjectsType(AllowedNegativeObjectsType); //this method is defined in subclasses, as the type of allowed negative objects can be changed for certain rules only
		
		/**
		 * Gets condition generator {@link ConditionGenerator} used in rule induction process.
		 * 
		 * @return the condition generator
		 */
		public ConditionGenerator conditionGenerator();
		
		/**
		 * Gets checker used to determine occurrence of stopping condition of rule induction process {@link RuleInductionStoppingConditionChecker}.
		 * 
		 * @return checker of stopping condition of rule induction process
		 */
		public RuleInductionStoppingConditionChecker ruleInductionStoppingConditionChecker();
		
		/**
		 * Gets the condition separator {@link ConditionSeparator} used to transform conditions constructed on composite fields into conditions expressed in terms of simple fields. 
		 * 
		 * @return the condition separator
		 */
		public ConditionSeparator conditionSeparator();
		
		/**
		 * Gets pruner of rule conditions {@link RuleConditionsPruner} used to remove redundant conditions from rule conditions.
		 * 
		 * @return the rule conditions pruner
		 */
		public RuleConditionsPruner ruleConditionsPruner();
		
		
		/**
		 * Gets {@link RuleConditionsGeneralizer generalizer of rule conditions} used to generalize conditions from rule conditions.
		 * 
		 * @return the rule conditions generalizer
		 */
		public RuleConditionsGeneralizer ruleConditionsGeneralizer();
		
		/**
		 * Gets pruner of rule conditions set {@link RuleConditionsSetPruner} used to remove redundant rules from rule conditions set.
		 * 
		 * @return the rule conditions set pruner
		 */
		public RuleConditionsSetPruner ruleConditionsSetPruner();
		
		/**
		 * Gets the rule minimality checker {@link RuleMinimalityChecker} used to induce rules.
		 * 
		 * @return the rule minimality checker
		 */
		public RuleMinimalityChecker ruleMinimalityChecker(); 
		
		/**
		 * Gets type of decision rule, i.e., type of rules to be induced.
		 * 
		 * @return the type of decision rule
		 */
		public RuleType ruleType();
		
		/**
		 * Gets type of negative objects allowed to be covered {@link AllowedNegativeObjectsType} by rule conditions {@link RuleConditions}. 
		 * 
		 * @return the type of negative objects allowed to be covered
		 */
		public AllowedNegativeObjectsType allowedNegativeObjectsType();
		
		/**
		 * Builds rule inducer components {@link RuleInducerComponents}.
		 * 
		 * @return the rule inducer components
		 */
		public RuleInducerComponents build();
		
	}
	
	/**
	 * Constructor setting all rule inducer components according to provided builder {@link Builder}.
	 * 
	 * @param builder builder of rule induction components
	 */
	protected RuleInducerComponents(Builder builder) {
		this.conditionGenerator = builder.conditionGenerator();
		this.ruleInductionStoppingConditionChecker = builder.ruleInductionStoppingConditionChecker();
		this.conditionSeparator = builder.conditionSeparator();
		this.ruleConditionsPruner = builder.ruleConditionsPruner();
		this.ruleConditionsGeneralizer = builder.ruleConditionsGeneralizer();
		this.ruleConditionsSetPruner = builder.ruleConditionsSetPruner();
		this.ruleMinimalityChecker = builder.ruleMinimalityChecker();
		this.ruleType = builder.ruleType();
		this.allowedNegativeObjectsType = builder.allowedNegativeObjectsType();
	}

	/**
	 * Gets condition generator {@link ConditionGenerator} used in rule induction process.
	 * 
	 * @return the condition generator
	 */
	public ConditionGenerator getConditionGenerator() {
		return conditionGenerator;
	}

	/**
	 * Gets checker used to determine occurrence of stopping condition of rule induction process {@link RuleInductionStoppingConditionChecker}.
	 * 
	 * @return checker of stopping condition of rule induction process
	 */
	public RuleInductionStoppingConditionChecker getRuleInductionStoppingConditionChecker() {
		return ruleInductionStoppingConditionChecker;
	}

	/**
	 * Gets the condition separator {@link ConditionSeparator} used to transform conditions constructed on composite fields into conditions expressed in terms of simple fields. 
	 * 
	 * @return the condition separator
	 */
	public ConditionSeparator getConditionSeparator() {
		return conditionSeparator;
	}

	/**
	 * Gets pruner of rule conditions {@link RuleConditionsPruner} used to remove redundant conditions from rule conditions.
	 * 
	 * @return the rule conditions pruner
	 */
	public RuleConditionsPruner getRuleConditionsPruner() {
		return ruleConditionsPruner;
	}
	
	/**
	 * Gets {@link RuleConditionsGeneralizer generalizer of rule conditions} used to generalize conditions from rule conditions.
	 * 
	 * @return the rule conditions generalizer
	 */
	public RuleConditionsGeneralizer getRuleConditionsGeneralizer() {
		return ruleConditionsGeneralizer;
	}

	/**
	 * Gets pruner of rule conditions set {@link RuleConditionsSetPruner} used to remove redundant rules from rule conditions set.
	 * 
	 * @return the rule conditions set pruner
	 */
	public RuleConditionsSetPruner getRuleConditionsSetPruner() {
		return ruleConditionsSetPruner;
	}

	/**
	 * Gets the rule minimality checker {@link RuleMinimalityChecker} used to induce rules.
	 * 
	 * @return the rule minimality checker
	 */
	public RuleMinimalityChecker getRuleMinimalityChecker() {
		return ruleMinimalityChecker;
	}

	/**
	 * Gets type of decision rule, i.e., type of rules to be induced.
	 * 
	 * @return the type of decision rule
	 */
	public RuleType getRuleType() {
		return ruleType;
	}
	
	/**
	 * Gets type of negative objects allowed to be covered {@link AllowedNegativeObjectsType} by rule conditions {@link RuleConditions}.
	 * 
	 * @return the type of negative objects allowed to be covered
	 */
	public AllowedNegativeObjectsType getAllowedNegativeObjectsType() {
		return allowedNegativeObjectsType;
	}
	
	/**
	 * Gets {@link Builder builder} which may be useful to reconstruct the rule inducer components object.
	 * 
	 * @return {@link Builder builder} able to reconstruct the rule inducer components object
	 */
	public abstract Builder builder(); 
	
}
