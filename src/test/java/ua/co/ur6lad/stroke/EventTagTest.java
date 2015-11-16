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
public class EventTagTest {

	private static final int ONE = 1;
	private static final OmniVariable.Event.Name VARIABLE_NAME = OmniVariable.Event.Name.Checkout;
	private static final int VARIABLE_NUMBER = 15;
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

	private OmniVariableTag.Event tag;

	@SuppressWarnings("unchecked")
	@Test
	public void emptyNameAndNumber() throws JspException, IOException, UnsupportedVariableException {
		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);

		tag.updateBean(bean);

		verify(bean, never()).addVariable(isA(OmniVariable.class), isA(String.class));
		verify(appender).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals("Logging level", Level.WARN, loggingEvent.getLevel());
			assertEquals("Logging message", "Both attributes name and number are missed", loggingEvent.getFormattedMessage());
		}
	}

	@Test
	public void happyPath_analyticBean() throws JspException, IOException {
		OmniVariable.Event expectedEvent = new OmniVariable.Event(VARIABLE_NAME);

		bean = new AnalyticBean();
		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setName(VARIABLE_NAME.toString());

		tag.updateBean(bean);

		assertEquals("One event", ONE, bean.getVariables().keySet().size());
		assertTrue("Expected event", bean.getVariables().keySet().contains(expectedEvent));
	}

	@Test
	public void happyPath_name() throws JspException, IOException, IllegalArgumentException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setName(VARIABLE_NAME.toString());

		result = tag.updateBean(bean);

		assertTrue("Result", result);
		verify(bean).addVariable(eq(new OmniVariable.Event(VARIABLE_NAME)), eq(VARIABLE_VALUE));
	}

	@Test
	public void happyPath_number() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setNumber(VARIABLE_NUMBER);

		result = tag.updateBean(bean);

		assertTrue("Result", result);
		verify(bean).addVariable(eq(new OmniVariable.Event(VARIABLE_NUMBER)), eq(VARIABLE_VALUE));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void wrongEventName() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setName(WRONG_EVENT);

		result = tag.updateBean(analyticBean);

		assertFalse("Result", result);
		verify(analyticBean, never()).addVariable(isA(OmniVariable.Event.class));
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
		
		tag = new OmniVariableTag.Event();
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