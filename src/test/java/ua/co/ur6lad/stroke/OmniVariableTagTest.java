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
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class OmniVariableTagTest {

	private static final int VARIABLE_NUMBER = 15;
	private static final String VARIABLE_VALUE = "variable value";

	@Mock
	@SuppressWarnings("rawtypes")
	private Appender appender;
	@Mock
	private OmniBean bean;
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
	@Mock
	private JspFragment jspBody;
	@Spy
	private OmniVariableTag tag;
	@Mock
	private OmniVariable variable;

	@Test(expected=JspException.class)
	public void bodyThrowsJspException() throws JspException, IOException, UnsupportedVariableException {
		doThrow(new JspException("markup error")).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setNumber(VARIABLE_NUMBER);

		tag.updateBean(bean, variable);
	}

	@Test(expected=IOException.class)
	public void bodyThrowsIOException() throws JspException, IOException, UnsupportedVariableException {
		doThrow(new IOException("i/o error")).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setNumber(VARIABLE_NUMBER);

		tag.updateBean(bean, variable);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void catchUnsupportedVariableException() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		doThrow(new UnsupportedVariableException("null value")).when(bean).addVariable(isA(OmniVariable.class), eq(VARIABLE_VALUE));
		tag.setJspBody(jspBody);
		tag.setNumber(VARIABLE_NUMBER);

		result = tag.updateBean(bean, variable);

		assertFalse("Result", result);
		verify(bean, never()).addVariable(isA(OmniVariable.class));
		verify(bean).addVariable(isA(OmniVariable.class), isA(String.class));
		verify(appender).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals(Level.WARN, loggingEvent.getLevel());
			assertEquals("Bean bean does not support variable variable: null value", loggingEvent.getFormattedMessage());
		}
	}

	@Test
	public void emptyBody() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setNumber(VARIABLE_NUMBER);

		result = tag.updateBean(bean, variable);

		assertTrue("Result", result);
		verify(bean).addVariable(eq(variable));
	}

	@Test
	public void notEmptyBody() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setNumber(VARIABLE_NUMBER);

		result = tag.updateBean(bean, variable);

		assertTrue("Result", result);
		verify(bean).addVariable(eq(variable), eq(VARIABLE_VALUE));
	}

	@Test(expected=NullPointerException.class)
	public void nullBean() throws JspException, IOException {
		bean = null;
		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setNumber(VARIABLE_NUMBER);

		tag.updateBean(bean, variable);
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		Logger logger;

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