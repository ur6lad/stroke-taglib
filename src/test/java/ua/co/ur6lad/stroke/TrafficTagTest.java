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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class TrafficTagTest {

	private static final int VARIABLE_NUMBER = 15;
	private static final String VARIABLE_VALUE = "variable value";

	@Mock
	private OmniBean bean;
	@Mock
	private JspFragment jspBody;

	private OmniVariableTag.Traffic tag;

	@Test
	public void happyPath() throws JspException, IOException, UnsupportedVariableException {
		boolean result;

		doAnswer(new FakeJspBodyAnswerer()).when(jspBody).invoke(isA(Writer.class));
		tag.setJspBody(jspBody);
		tag.setNumber(VARIABLE_NUMBER);

		result = tag.updateBean(bean);

		assertTrue("Result", result);
		verify(bean).addVariable(eq(new OmniVariable.Traffic(VARIABLE_NUMBER)), eq(VARIABLE_VALUE));
	}

	@Before
	public void setUp() {
		tag = new OmniVariableTag.Traffic();
	}

	private static class FakeJspBodyAnswerer implements Answer<Void> {
		public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
			Writer writer = (Writer) invocationOnMock.getArguments()[0];
			writer.write(VARIABLE_VALUE);
			return null;
		}
	}

}