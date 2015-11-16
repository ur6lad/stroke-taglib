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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
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
public class TakeDownTagTest {

	private static final String JSP_TEXT = "ABCDEF12345";
	private static final String JSP_PAGE_ERROR_MESSAGE = "JSP page error";
	private static final String NULL_VALUE_MESSAGE = "Skip variable event9876: an empty value";
	private static final String OUTPUT_ERROR_MESSAGE = "Output page error";
	private static final String ANALYTIC_VALUE = "analyticValue";
	private static final String ANALYTIC_VARIABLE = "analyticVariable";
	private static final int CONVERSION_ID = 7575;
	private static final String CONVERSION_VALUE = "qwerty";
	private static final int EVENT_ID = 9876;
	private static final String HOMEPAGE = "homepage";
	private static final int TWO = 2;

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
	private OmniBean omniBean;

	private TakeDownTag tag;

	Map<OmniVariable, String> variableMap;

	@Test
	public void happyPath() throws JspException, IOException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setJspContext(jspContext);

		result = tag.updateBean(omniBean);

		assertFalse("Happy path", result);
		verify(jspBody, times(TWO)).invoke(isA(Writer.class));
		verify(jspContext, times(TWO)).getOut();
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "pageName");
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "eVar7575");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'qwerty'");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'homepage'");
		verify(jspWriter, times(TWO)).println(eq(JSP_TEXT));
	}

	@Test
	public void happyPath_emptyBody() throws JspException, IOException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspContext(jspContext);

		result = tag.updateBean(omniBean);

		assertFalse("Empty body", result);
		verify(jspBody, never()).invoke(isA(Writer.class));
		verify(jspContext, never()).getOut();
		verify(jspWriter, never()).println(isA(String.class));
		verify(jspContext, never()).setAttribute(isA(String.class), isA(String.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void nullValue() throws JspException, IOException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setJspContext(jspContext);
		variableMap.put(new OmniVariable.Event(EVENT_ID), null);

		result = tag.updateBean(omniBean);

		assertFalse("Null value", result);
		verify(jspBody, times(TWO)).invoke(isA(Writer.class));
		verify(jspContext, times(TWO)).getOut();
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "pageName");
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "eVar7575");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'homepage'");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'qwerty'");
		verify(jspWriter, times(TWO)).println(eq(JSP_TEXT));
		verify(appender).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals(Level.WARN, loggingEvent.getLevel());
			assertEquals(NULL_VALUE_MESSAGE, loggingEvent.getFormattedMessage());
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

		result = tag.updateBean(omniBean);

		assertFalse("Happy path", result);
		verify(jspBody, times(TWO)).invoke(isA(Writer.class));
		verify(jspContext, times(TWO)).getOut();
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "pageName");
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "eVar7575");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'homepage'");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'qwerty'");
		verify(jspWriter, times(TWO)).println(eq(JSP_TEXT));
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

		result = tag.updateBean(omniBean);

		assertFalse("Page error", result);
		verify(jspBody, times(TWO)).invoke(isA(Writer.class));
		verify(jspContext, never()).getOut();
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "pageName");
		verify(jspContext).setAttribute(ANALYTIC_VARIABLE, "eVar7575");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'homepage'");
		verify(jspContext).setAttribute(ANALYTIC_VALUE, "'qwerty'");
		verify(jspWriter, never()).println(eq(JSP_TEXT));
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

		logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(appender);
		tag = new TakeDownTag();
		variableMap = new HashMap<OmniVariable, String>();
		variableMap.put(new OmniVariable.Standard(OmniVariable.Standard.Name.PageName), HOMEPAGE);
		variableMap.put(new OmniVariable.Conversion(CONVERSION_ID), CONVERSION_VALUE);
		//variableMap.put(new OmniVariable.Event(EVENT_ID), null);

		when(jspContext.getOut()).thenReturn(jspWriter);
		when(omniBean.getVariables()).thenReturn(variableMap);
	}

	private static class FakeJspBodyAnswerer implements Answer<Void> {
		public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
			Writer writer = (Writer) invocationOnMock.getArguments()[0];
			writer.write(JSP_TEXT);
			return null;
		}
	}

}