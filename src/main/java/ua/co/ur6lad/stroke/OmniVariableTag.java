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

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;

/**
 * Base class for all variable tags.
 *
 * @author Vitaliy Berdinskikh
 */
public abstract class OmniVariableTag extends OmniTag {

	protected String name;
	protected Integer number;

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Update bean: add a variable to bean.
	 *
	 * @param bean an updated bean
	 * @param variable an added variable
	 * @return true if a bean was updated with new variable
	 * @throws JspException if an error occurred while processing this tag.
	 * @throws IOException if there was an error writing to the output stream
	 * @throws NullPointerException if a bean is null
	 */
	protected boolean updateBean(OmniBean bean, OmniVariable variable) throws JspException, IOException, NullPointerException {
		final StringWriter writer = new StringWriter();
		boolean result = false;
		String value;

		if (null != getJspBody()) {
			getJspBody().invoke(writer);
		}
		value = writer.toString().trim();
		try {
			if (value.isEmpty()) {
				bean.addVariable(variable);
			} else {
				bean.addVariable(variable, value);
			}
			result = true;
		} catch (UnsupportedVariableException unsupportedVariable) {
			logger().warn("Bean {} does not support variable {}: {}", bean, variable, unsupportedVariable.getMessage());
		}

		return result;
	}

	/**
	 * Add a conversion variable to the bean.
	 *
	 * @author Vitaliy Berdinskikh
	 * @see OmniVariable.Conversion
	 */
	public static class Conversion extends OmniVariableTag {

		/**
		 * Update bean: add a conversion variable to bean.
		 *
		 * @param omniBean an updated bean
		 * @return true if a bean was updated with new variable
		 * @throws NullPointerException if a bean is null or if a number is missed
		 */
		@Override
		protected boolean updateBean(OmniBean omniBean) throws JspException, IOException, NullPointerException {
			return updateBean(omniBean, new OmniVariable.Conversion(number));
		}

	}

	/**
	 * Add an event to the bean.
	 *
	 * @author Vitaliy Berdinskikh
	 * @see OmniVariable.Event
	 */
	public static class Event extends OmniVariableTag {

		/**
		 * Update bean: add an event variable to bean.
		 *
		 * @param omniBean an updated bean
		 * @return true if a bean was updated with new variable
		 * @throws NullPointerException if a bean is null
		 */
		@Override
		protected boolean updateBean(OmniBean omniBean) throws JspException, IOException, NullPointerException {
			OmniVariable.Event event = null;
			boolean result = false;

			if (null != name) {
				try {
					event = new OmniVariable.Event(name);
				} catch (IllegalArgumentException illegalName) {
					logger().warn("Could not find name {}", name);
				}
			} else if (null != number) {
				event = new OmniVariable.Event(number);
			} else {
				logger().warn("Both attributes name and number are missed");
			}

			if (null != event) {
				result = updateBean(omniBean, event);
			}

			return result;
		}

	}

	/**
	 * Add a hierarchy variable to the bean.
	 *
	 * @author Vitaliy Berdinskikh
	 * @see OmniVariable.Hierarchy
	 */
	public static class Hierarchy extends OmniVariableTag {

		/**
		 * Update bean: add a hierarchy variable to bean.
		 *
		 * @param omniBean an updated bean
		 * @return true if a bean was updated with new variable
		 * @throws NullPointerException if a bean is null or if a number is missed
		 */
		@Override
		protected boolean updateBean(OmniBean omniBean) throws JspException, IOException, NullPointerException {
			return updateBean(omniBean, new OmniVariable.Hierarchy(number));
		}

	}

	/**
	 * Add a list variable to the bean.
	 *
	 * @author Vitaliy Berdinskikh
	 * @see OmniVariable.List
	 */
	public static class List extends OmniVariableTag {

		/**
		 * Update bean: add a list variable to bean.
		 *
		 * @param omniBean an updated bean
		 * @return true if a bean was updated with new variable
		 * @throws NullPointerException if a bean is null or if a number is missed
		 */
		@Override
		protected boolean updateBean(OmniBean omniBean) throws JspException, IOException, NullPointerException {
			return updateBean(omniBean, new OmniVariable.List(number));
		}

	}

	/**
	 * Add a standard variable to the bean.
	 *
	 * @author Vitaliy Berdinskikh
	 * @see OmniVariable.Standard
	 */
	public static class Standard extends OmniVariableTag {

		/**
		 * Update bean: add a standard variable to bean.
		 *
		 * @param omniBean an updated bean
		 * @return true if a bean was updated with new variable
		 * @throws NullPointerException if a bean is null
		 */
		@Override
		protected boolean updateBean(OmniBean omniBean) throws JspException, IOException, NullPointerException {
			OmniVariable.Standard variable = null;
			boolean result = false;

			try {
				variable = new OmniVariable.Standard(name);
			} catch (IllegalArgumentException illegalName) {
				logger().warn("Could not find name {}", name);
			}

			if (null != variable) {
				switch (variable.getName().getType()) {
				case Complex:
					logger().warn("Could not set the complex variable {}", name);
					break;
				case ReadOnly:
					logger().warn("Read only variable {}", name);
					break;
				default:
					updateBean(omniBean, variable);
					result = true;
				}
			}

			return result;
		}

	}

	/**
	 * Add a traffic variable to the bean.
	 *
	 * @author Vitaliy Berdinskikh
	 * @see OmniVariable.Traffic
	 */
	public static class Traffic extends OmniVariableTag {

		/**
		 * Update bean: add a traffic variable to bean.
		 *
		 * @param omniBean an updated bean
		 * @return true if a bean was updated with new variable
		 * @throws NullPointerException if a bean is null or if a number is missed
		 */
		@Override
		protected boolean updateBean(OmniBean omniBean) throws JspException, IOException, NullPointerException {
			return updateBean(omniBean, new OmniVariable.Traffic(number));
		}

	}

}