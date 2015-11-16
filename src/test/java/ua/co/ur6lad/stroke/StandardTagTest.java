package ua.co.ur6lad.stroke;

/*
 * Copyright 2015 Vitaliy Berdinskikh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	   http://www.apache.org/licenses/LICENSE-2.0
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
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
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
public class StandardTagTest {

	private static final OmniVariable.Standard.Name COMPLEX_VARIABLE_NAME = OmniVariable.Standard.Name.Traffic;
	private static final OmniVariable.Standard.Name READONLY_VARIABLE_NAME = OmniVariable.Standard.Name.CookiesEnabled;
	private static final OmniVariable.Standard.Name VARIABLE_NAME = OmniVariable.Standard.Name.CharSet;
	private static final String VARIABLE_VALUE = "variable value";
	private static final String WRONG_EVENT= "abcde123";

	@Mock
	private AnalyticBean analyticBean;
	@Mock
	@SuppressWarnings("rawtypes")
	private Appender appender;
	@Mock
	private OmniBean bean;
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
	@Mock
	private JspFragment jspBody;

	private OmniVariableTag.Standard tag;

	@SuppressWarnings("unchecked")
	@Test
	public void complexVariable() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setName(COMPLEX_VARIABLE_NAME.toString());

		result = tag.updateBean(analyticBean);

		assertFalse("Complex variable", result);
		verify(analyticBean, never()).addVariable(isA(OmniVariable.Standard.class));
		verify(appender).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals("Logging level", Level.WARN, loggingEvent.getLevel());
			assertEquals("Logging message", "Could not set the complex variable prop", loggingEvent.getFormattedMessage());
		}
	}

	@Test
	public void happyPath_name() throws JspException, IOException, IllegalArgumentException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setName(VARIABLE_NAME.toString());

		result = tag.updateBean(bean);

		assertTrue("Happy path, name", result);
		verify(bean).addVariable(eq(new OmniVariable.Standard(VARIABLE_NAME)));
		verify(bean, never()).addVariable(isA(OmniVariable.Standard.class), isA(String.class));
	}

	@Test
	public void happyPath_nameAndValue() throws JspException, IOException, IllegalArgumentException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setName(VARIABLE_NAME.toString());

		result = tag.updateBean(bean);

		assertTrue("Happy path, name and value", result);
		verify(bean, never()).addVariable(isA(OmniVariable.Standard.class));
		verify(bean).addVariable(eq(new OmniVariable.Standard(VARIABLE_NAME)), eq(VARIABLE_VALUE));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void readonlyVariable() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setName(READONLY_VARIABLE_NAME.toString());

		result = tag.updateBean(analyticBean);

		assertFalse("Readonly variable", result);
		verify(analyticBean, never()).addVariable(isA(OmniVariable.Standard.class));
		verify(appender).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals("Logging level", Level.WARN, loggingEvent.getLevel());
			assertEquals("Logging message", "Read only variable cookiesEnabled", loggingEvent.getFormattedMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void wrongVariableName() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setName(WRONG_EVENT);

		result = tag.updateBean(analyticBean);

		assertFalse("Wrong variable name", result);
		verify(analyticBean, never()).addVariable(isA(OmniVariable.Standard.class));
		verify(appender).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals("Logging level", Level.WARN, loggingEvent.getLevel());
			assertEquals("Logging message", "Could not find name abcde123", loggingEvent.getFormattedMessage());
		}
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		Logger logger;
		
		tag = new OmniVariableTag.Standard();
		logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(appender);
	}

	private static class FakeJspBodyAnswerer implements Answer<Void> {
		public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
			Writer writer = (Writer) invocationOnMock.getArguments()[0];
			writer.write(VARIABLE_VALUE);
			return null;
		}
	}

}