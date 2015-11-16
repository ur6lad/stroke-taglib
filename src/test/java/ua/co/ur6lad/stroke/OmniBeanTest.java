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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OmniBeanTest {

	private static final int NO_ITEMS = 0;
	private static final int ONE_ITEM = 1;
	private static final int VARIABLE_ID = 42;
	private static final String VARIABLE_VALUE = "variable value";

	private OmniBean bean;

	@Test
	public void addConversionVariableWithValue() throws UnsupportedVariableException {
		final OmniVariable.Conversion conversionVariable = new OmniVariable.Conversion(VARIABLE_ID);

		bean.addVariable(conversionVariable, VARIABLE_VALUE);

		assertEquals("Add one variable to the map", ONE_ITEM, bean.getVariables().size());
		assertTrue("Conversion variable", bean.getVariables().containsKey(conversionVariable));
		assertEquals("Conversion variable\'s value", VARIABLE_VALUE, bean.getVariables().get(conversionVariable));
	}

	@Test(expected=UnsupportedVariableException.class)
	public void addConversionVariableWithoutValue() throws UnsupportedVariableException {
		final OmniVariable.Conversion conversionVariable = new OmniVariable.Conversion(VARIABLE_ID);

		bean.addVariable(conversionVariable);
	}

	@Test
	public void addEventWithValue() throws UnsupportedVariableException {
		final OmniVariable.Event eventVariable = new OmniVariable.Event(VARIABLE_ID);

		bean.addVariable(eventVariable, VARIABLE_VALUE);

		assertEquals("Add one variable to the map", ONE_ITEM, bean.getVariables().size());
		assertTrue("Event variable", bean.getVariables().containsKey(eventVariable));
		assertEquals("Event variable\'s value", VARIABLE_VALUE, bean.getVariables().get(eventVariable));
	}

	@Test
	public void addEventWithoutValue() throws UnsupportedVariableException {
		final OmniVariable.Event eventVariable = new OmniVariable.Event(VARIABLE_ID);

		bean.addVariable(eventVariable);

		assertEquals("Add one variable to the set", ONE_ITEM, bean.getVariables().size());
		assertTrue("Event variable", bean.getVariables().keySet().contains(eventVariable));
	}

	@Test
	public void emptyMap() {
		assertEquals("Map of variables is empty", NO_ITEMS, bean.getVariables().size());
	}

	@Before
	public void setUp() throws Exception {
		bean = new OmniBean();
	}

}