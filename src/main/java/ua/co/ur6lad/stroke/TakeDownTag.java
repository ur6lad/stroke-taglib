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
import java.util.Map;

import javax.servlet.jsp.JspException;

/**
 * Take down data from the analytic bean.
 *
 * @author Vitaliy Berdinskikh
 */
public class TakeDownTag extends OmniTag {

	private static final String NAME = "analyticVariable";
	private static final String VALUE = "analyticValue";

	/**
	 * This method doesn't update bean but read it.
	 *
	 * @param omniBean an updated bean
	 * @return false
	 * @throws NullPointerException if a bean is null
	 */
	protected boolean updateBean(OmniBean omniBean) throws NullPointerException {
		StringWriter writer;

		if (null != getJspBody()) {
			Map<String, String> variables = OmniTools.beanToMap(omniBean);

			for (String name : variables.keySet()) {
				writer = new StringWriter();
				if (null == variables.get(name)) {
					logger().warn("Skip variable {}: an empty value", name);
				} else {
					try {
						getJspContext().setAttribute(NAME, name);
						getJspContext().setAttribute(VALUE, variables.get(name));
						getJspBody().invoke(writer);
						getJspContext().getOut().println(writer.toString());
					} catch (JspException pageException) {
						logger().warn(pageException.getMessage());
					} catch (IOException outputException) {
						logger().warn(outputException.getMessage());
					}
				}
			}
		} else {
			logger().warn("Missed tag's body");
		}

		return false;
	}

}