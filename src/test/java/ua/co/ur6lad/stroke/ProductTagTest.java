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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class ProductTagTest {

	private static final String JSP_TEXT = "ABCDEF12345";
	private static final String JSP_PAGE_ERROR_MESSAGE = "JSP page error";
	private static final String OUTPUT_ERROR_MESSAGE = "Output page error";
	private static final String TEST_CATEGORY = "Test category";
	private static final String TEST_PRODUCT = "Test product";
	private static final String TEST_QUANTITY = "Test quantity";
	private static final String TEST_TOTAL_COST = "Test total cost";
	private static final int TWO = 2;

	@Mock
	private OmniBean alienBean;
	@Mock
	private AnalyticBean analyticBean;
	@Mock
	@SuppressWarnings("rawtypes")
	private Appender appender;
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
	@Mock
	private JspFragment jspBody;
	@Mock
	private JspContext jspContext;
	@Mock
	private JspWriter jspWriter;
	@Mock
	private ProductBean productBean;

	private ProductTag tag;

	/* updateBean() */

	@Test
	public void analyticBeanFromRequest() throws JspException, IOException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setCategory(TEST_CATEGORY);
		tag.setJspBody(jspBody);
		tag.setJspContext(jspContext);
		tag.setProduct(TEST_PRODUCT);
		tag.setQuantity(TEST_QUANTITY);
		tag.setTotalCost(TEST_TOTAL_COST);

		result = tag.updateBean(productBean);

		assertTrue("Happy path", result);
		verify(jspContext).getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE));
		verify(jspBody).invoke(isA(Writer.class));
		verify(jspContext).getOut();
		verify(jspWriter).println(eq(JSP_TEXT));

		assertTrue("Bean", ProductBean.class.isInstance(tag.bean()));
		verify(analyticBean).addProduct(eq(productBean));
		verify(productBean).setCategory(TEST_CATEGORY);
		verify(productBean).setProduct(TEST_PRODUCT);
		verify(productBean).setQuantity(TEST_QUANTITY);
		verify(productBean).setTotalCost(TEST_TOTAL_COST);
	}

	@Test
	public void emptyBody() throws JspException, IOException {
		boolean result;

		tag.setCategory(TEST_CATEGORY);
		tag.setJspContext(jspContext);
		tag.setProduct(TEST_PRODUCT);
		tag.setQuantity(TEST_QUANTITY);
		tag.setTotalCost(TEST_TOTAL_COST);

		result = tag.updateBean(productBean);

		assertTrue("Happy path", result);
		verify(jspContext).getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE));
		verify(jspContext, never()).getOut();
		verify(jspWriter, never()).println(isA(String.class));

		assertTrue("Bean", ProductBean.class.isInstance(tag.bean()));
		verify(analyticBean).addProduct(eq(productBean));
		verify(productBean).setCategory(TEST_CATEGORY);
		verify(productBean).setProduct(TEST_PRODUCT);
		verify(productBean).setQuantity(TEST_QUANTITY);
		verify(productBean).setTotalCost(TEST_TOTAL_COST);
	}

	@Test
	public void explicitAnalyticBean() throws JspException, IOException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setBean(analyticBean);
		tag.setCategory(TEST_CATEGORY);
		tag.setJspBody(jspBody);
		tag.setJspContext(jspContext);
		tag.setProduct(TEST_PRODUCT);
		tag.setQuantity(TEST_QUANTITY);
		tag.setTotalCost(TEST_TOTAL_COST);

		result = tag.updateBean(productBean);

		assertTrue("Happy path", result);
		verify(jspContext, never()).getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), anyInt());
		verify(jspBody).invoke(isA(Writer.class));
		verify(jspContext).getOut();
		verify(jspWriter).println(eq(JSP_TEXT));

		assertTrue("Bean", ProductBean.class.isInstance(tag.bean()));
		verify(analyticBean).addProduct(eq(productBean));
		verify(productBean).setCategory(TEST_CATEGORY);
		verify(productBean).setProduct(TEST_PRODUCT);
		verify(productBean).setQuantity(TEST_QUANTITY);
		verify(productBean).setTotalCost(TEST_TOTAL_COST);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void notAnalyticBean() throws JspException, IOException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		when(jspContext.getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE))).thenReturn(alienBean);
		tag.setJspBody(jspBody);
		tag.setJspContext(jspContext);
		tag.setProduct(TEST_PRODUCT);

		result = tag.updateBean(productBean);

		assertFalse("Not analytic bean", result);
		verify(jspContext).getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE));
		verify(jspBody).invoke(isA(Writer.class));
		verify(jspContext).getOut();
		verify(jspWriter).println(eq(JSP_TEXT));

		assertTrue("Bean", ProductBean.class.isInstance(tag.bean()));
		verify(analyticBean, never()).addProduct(isA(ProductBean.class));
		verify(productBean).setProduct(TEST_PRODUCT);

		verify(appender, times(TWO)).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
			int cutoff;

			assertEquals("Logging level", Level.WARN, loggingEvent.getLevel());
			cutoff = loggingEvent.getFormattedMessage().indexOf(alienBean.getClass().getName());
			assertEquals("Logging message", "Could not get the analytic bean: super.bean() returns class ", loggingEvent.getFormattedMessage().substring(0, cutoff));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void outputError() throws JspException, IOException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		doThrow(new IOException(OUTPUT_ERROR_MESSAGE)).when(jspWriter).println(isA(String.class));
		tag.setJspBody(jspBody);
		tag.setJspContext(jspContext);
		tag.setProduct(TEST_PRODUCT);

		result = tag.updateBean(productBean);

		assertFalse("Output error", result);
		verify(jspBody).invoke(isA(Writer.class));
		verify(jspContext).getOut();
		verify(jspWriter).println(eq(JSP_TEXT));

		assertTrue("Bean", ProductBean.class.isInstance(tag.bean()));
		verify(analyticBean, never()).addProduct(isA(ProductBean.class));

		verify(appender, times(TWO)).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals(Level.WARN, loggingEvent.getLevel());
			assertEquals(OUTPUT_ERROR_MESSAGE, loggingEvent.getFormattedMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void pageError() throws JspException, IOException {
		boolean result;

		doThrow(new JspException(JSP_PAGE_ERROR_MESSAGE)).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setJspContext(jspContext);

		result = tag.updateBean(productBean);

		assertFalse("Page error", result);
		verify(jspBody).invoke(isA(Writer.class));
		verify(jspContext, never()).getOut();
		verify(jspWriter, never()).println(eq(JSP_TEXT));

		assertTrue("Bean", ProductBean.class.isInstance(tag.bean()));
		verify(analyticBean, never()).addProduct(isA(ProductBean.class));

		verify(appender, times(TWO)).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals(Level.WARN, loggingEvent.getLevel());
			assertEquals(JSP_PAGE_ERROR_MESSAGE, loggingEvent.getFormattedMessage());
		}
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		Logger logger;

		tag = new ProductTag();
		logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(appender);

		when(jspContext.getAttribute(eq(OmniTools.ANALYTIC_BEAN_NAME), eq(PageContext.REQUEST_SCOPE))).thenReturn(analyticBean);
		when(jspContext.getOut()).thenReturn(jspWriter);
	}

	private static class FakeJspBodyAnswerer implements Answer<Void> {
		public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
			Writer writer = (Writer) invocationOnMock.getArguments()[0];
			writer.write(JSP_TEXT);
			return null;
		}
	}

}