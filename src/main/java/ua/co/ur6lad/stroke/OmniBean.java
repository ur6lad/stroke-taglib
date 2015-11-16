package ua.co.ur6lad.stroke;

/*
 * Copyright 2015 Vitaliy Berdinskikh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for the analytic and product beans.
 *
 * @author Vitaliy Berdinskikh
 */
public class OmniBean {

	private Logger logger;
	private HashMap<OmniVariable, String> variables;

	public OmniBean() {
		variables = new HashMap<OmniVariable, String>();
	}

	/**
	 * Add the variable. Use to add the event variables.
	 *
	 * See {@link OmniVariable.Event}
	 *
	 * @param variable the analytic variable
	 * @throws UnsupportedVariableException if the type of variable is unsupported
	 * @see OmniVariable
	 */
	public void addVariable(OmniVariable variable) throws UnsupportedVariableException {
		addVariable(variable, null);
	}

	/**
	 * Add the variable with a value. Use to add conversion variables, traffic variables etc.
	 *
	 * See {@link OmniVariable.Conversion}, {@link OmniVariable.Event}, {@link OmniVariable.Hierarchy}, {@link OmniVariable.List},
	 *		{@link OmniVariable.Standard}, {@link OmniVariable.Traffic}.
	 *
	 * @param variable the analytic variable
	 * @param value the variable's value
	 * @throws UnsupportedVariableException if the type of variable is unsupported
	 * @see OmniVariable
	 */
	public void addVariable(OmniVariable variable, String value) throws UnsupportedVariableException {
		if (null == value && !OmniVariable.Event.class.isInstance(variable)) {
			throw new UnsupportedVariableException("Only events could have no value");
		}
		variables.put(variable, value);
		logger().debug("add variable: {} = {}", variable, value);
	}

	/**
	 * Get the map of variables.
	 *
	 * @return copy of map of variables
	 * @see OmniBean#addVariable(OmniVariable, String)
	 * @see OmniVariable
	 */
	@SuppressWarnings("unchecked")
	public Map<OmniVariable, String> getVariables() {
		return (Map<OmniVariable, String>) variables.clone();
	}

	/**
	 * Get a logger. If a logger is null create new one.
	 *
	 * @return the logger
	 * @see org.slf4j.Logger
	 */
	protected Logger logger() {
		if (null == logger) {
			logger = LoggerFactory.getLogger(this.getClass());
		}

		return logger;
	}

}