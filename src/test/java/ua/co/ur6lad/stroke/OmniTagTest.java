package ua.co.ur6lad.stroke;

/*
 * Copyright 2015 Vitaliy Berdinskikh
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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OmniTagTest {

	private static final int TWO = 2;

	// QUESTION: which bean should be used?
	@Mock
	private AnalyticBean analyticBean;
	@Mock
	private JspFragment jspBody;
	@Mock
	private JspContext jspContext;
	@Mock
	private OmniTag ancestorOmniTag;
	@Spy
	private OmniTag tag;

	/* Get a bean */

	@Test
	public void attribute() throws JspException, IOException {
		OmniBean bean;

		tag.setBean(analyticBean);

		bean = tag.bean();

		assertNotNull("Analytic bean, from the attribute", bean);
		verify(ancestorOmniTag, never()).bean();
		verify(jspContext, never()).getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE));
	}

	@Test
	public void request() throws JspException, IOException {
		OmniBean bean = null;

		bean = tag.bean();

		assertNotNull("Analytic bean, from the request", bean);
		verify(jspContext).getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE));
	}

	@Test
	public void initializeBean() throws JspException, IOException {
		OmniBean bean = null;

		when(jspContext.getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE))).thenReturn(null).thenReturn(analyticBean);

		bean = tag.bean();

		assertNotNull("Analytic bean, from request", bean);
		verify(jspContext).setAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), isA(OmniBean.class), eq(PageContext.REQUEST_SCOPE));
		verify(jspContext, times(TWO)).getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE));
	}

	/* updateBean() */

	@Test
	public void updateBean() throws JspException, IOException {
		tag.setBean(analyticBean);

		tag.doTag();

		verify(tag).updateBean(eq(analyticBean));
	}

	@Before
	public void setUp() {
		when(jspContext.getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE))).thenReturn(analyticBean);
		tag.setJspContext(jspContext);
	}

}
