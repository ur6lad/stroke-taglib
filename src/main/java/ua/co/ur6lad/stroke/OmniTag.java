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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all analytic tags.
 *
 * @author Vitaliy Berdinskikh
 */
public abstract class OmniTag extends SimpleTagSupport {

	private OmniBean omniBean;
	private Logger logger;

	/**
	 * Update bean: add some variables etc.
	 *
	 * @param bean an updated bean
	 * @return true if a bean was updated with new values
	 * @throws JspException if an error occurred while processing this tag.
	 * @throws IOException if there was an error writing to the output stream
	 * @throws NullPointerException if the bean is null
	 */
	protected abstract boolean updateBean(OmniBean bean) throws JspException, IOException, NullPointerException;

	@Override
	public void doTag() throws JspException, IOException {
		boolean beanUpdated = updateBean(bean());
		logger().debug("Bean updated: {}", beanUpdated);
	}

	/**
	 * The bean to update. Usually it's the {@link AnalyticBean}.
	 *
	 * @param bean an updated bean
	 */
	public void setBean(OmniBean bean) throws NullPointerException {
		omniBean = bean;
	}

	/**
	 * Return bean that the tag will update. Create new one if needed.
	 *
	 * @return an updated bean.
	 */
	protected OmniBean bean() {
		synchronized(this) {
			if (null == omniBean) {
				logger().debug("Try to get a bean from the request");
				omniBean = (OmniBean) getJspContext().getAttribute(OmniTools.ANALYTIC_BEAN_NAME, PageContext.REQUEST_SCOPE);
				if (null == omniBean) {
					// QUESTION: Could I initialize the bean with some values: server etc.? See #1 issue
					logger().debug("Put new bean to the request");
					getJspContext().setAttribute(OmniTools.ANALYTIC_BEAN_NAME, new AnalyticBean(), PageContext.REQUEST_SCOPE);
					omniBean = (OmniBean) getJspContext().getAttribute(OmniTools.ANALYTIC_BEAN_NAME, PageContext.REQUEST_SCOPE);
				}
			}
		}

		return omniBean;
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