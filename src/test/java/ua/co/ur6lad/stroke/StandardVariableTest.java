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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StandardVariableTest {

	private static final OmniVariable.Standard.Name CHAR_SET = OmniVariable.Standard.Name.CharSet;
	private static final int EQUAL = 0;
	private static final String EVAR39 = "eVar39";
	private static final int LESS_THAN = -1;
	private static final String KNOWN_VARIABLE_NAME = "pageURL";
	private static final OmniVariable.Standard.Name PAGE_NAME = OmniVariable.Standard.Name.PageName;
	private static final int VARIABLE_NUMBER = 13;
	private static final String WRONG_NAME = "123";

	@Mock
	private OmniVariable.Standard anotherVariable;

	private OmniVariable.Standard variable;

	/* Compare */

	@Test
	public void compare_differentNames() {
		when(anotherVariable.getName()).thenReturn(PAGE_NAME);

		assertTrue("Compare different names", LESS_THAN >= variable.compareTo(anotherVariable));
		verify(anotherVariable).getName();
	}

	@Test
	public void compare_equalNames() {
		when(anotherVariable.getName()).thenReturn(CHAR_SET);

		assertEquals("Compare equal names", EQUAL, variable.compareTo(anotherVariable));
		verify(anotherVariable).getName();
	}

	@Test
	public void compare_null() {
		anotherVariable = null;

		assertTrue("Compare null", LESS_THAN >= variable.compareTo(anotherVariable));
	}

	/* Constructor */

	@Test
	public void create_complexVariable() {
		variable = new OmniVariable.Standard(EVAR39);

		assertEquals("Standard variable, string", OmniVariable.Standard.Name.Conversion, variable.getName());
	}

	@Test
	public void create_enum() {
		variable = new OmniVariable.Standard(PAGE_NAME);

		assertEquals("Standard variable, enum", OmniVariable.Standard.Name.PageName, variable.getName());
	}

	@Test
	public void create_emptyPrefix() {
		assertTrue("Empty prefix", variable.getPrefix().isEmpty());
	}

	@Test
	public void create_lowerCaseName() {
		variable = new OmniVariable.Standard(KNOWN_VARIABLE_NAME.toLowerCase());

		assertEquals("Standard variable, string", OmniVariable.Standard.Name.PageURL, variable.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void create_null() {
		OmniVariable.Standard.Name nullName = null;

		variable = new OmniVariable.Standard(nullName);
	}

	@Test
	public void create_string() {
		variable = new OmniVariable.Standard(CHAR_SET.toString());

		assertEquals("Standard variable, string", OmniVariable.Standard.Name.CharSet, variable.getName());
	}

	@Test
	public void create_upperCaseName() {
		variable = new OmniVariable.Standard(KNOWN_VARIABLE_NAME.toUpperCase());

		assertEquals("Standard variable, string", OmniVariable.Standard.Name.PageURL, variable.getName());
	}

	/* Equals */

	@Test
	public void equals_differentNames() {
		when(anotherVariable.getName()).thenReturn(PAGE_NAME);

		assertFalse("Different names", variable.equals(anotherVariable));
		verify(anotherVariable).getName();
	}

	@Test
	public void equals_equalNames() {
		when(anotherVariable.getName()).thenReturn(CHAR_SET);

		assertTrue("Equal names", variable.equals(anotherVariable));
		verify(anotherVariable).getName();
	}

	@Test
	public void equals_notVariable() {
		OmniVariable.Conversion anotherVariable = new OmniVariable.Conversion(VARIABLE_NUMBER);

		assertFalse("Equal names", variable.equals(anotherVariable));
	}

	/* hashCode() */

	@Test
	public void hashCodeEqualsHashCodeOfNameAsString() {
		assertEquals("Hashcode", CHAR_SET.toString().hashCode(), variable.hashCode());
	}

	/* Name.forName() */

	@Test(expected=IllegalArgumentException.class)
	public void forName_nullName() {
		String name = null;

		OmniVariable.Standard.Name.forName(name);
	}

	@Test(expected=IllegalArgumentException.class)
	public void forName_unknownName() {
		OmniVariable.Standard.Name.forName(WRONG_NAME);
	}

	/* toString() */

	@Test
	public void variableToString() {
		String testString = null;

		testString = variable.toString();

		assertNotNull("String exists", testString);
		assertEquals("Conversion variable: to string", "charSet", testString);
	}

	@Before
	public void setUp() throws Exception {
		variable = new OmniVariable.Standard(CHAR_SET);
	}

}