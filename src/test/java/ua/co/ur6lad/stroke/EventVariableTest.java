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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventVariableTest {

	private static final OmniVariable.Event.Name ANOTHER_EVENT_NAME = OmniVariable.Event.Name.Checkout;
	private static final int ANOTHER_EVENT_NUMBER = -11;
	private static final String ANOTHER_SERIAL = "qwerty1111";
	private static final int EQUAL = 0;
	private static final OmniVariable.Event.Name EVENT_NAME = OmniVariable.Event.Name.Purchase;
	private static final int EVENT_NUMBER = 10;
	private static final int GREATER_THAN = 1;
	private static final int LESS_THAN = -1;
	private static final String SERIAL = "abcde9876";
	private static final int TWO = 2;

	@Mock
	private OmniVariable.Event anotherEventVariable;

	private OmniVariable.Conversion anotherVariable;
	private OmniVariable.Event namedVariable;
	private OmniVariable.Event numberVariable;

	/* Compare */

	@Test
	public void compare_equalNames() {
		when(anotherEventVariable.getName()).thenReturn(EVENT_NAME);

		assertEquals("Compare equal named events", EQUAL, namedVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable, times(TWO)).getName();
	}

	@Test
	public void compare_equalNames_bothWithSerials() {
		namedVariable = new OmniVariable.Event(EVENT_NAME, SERIAL);
		when(anotherEventVariable.getName()).thenReturn(EVENT_NAME);
		when(anotherEventVariable.getSerial()).thenReturn(ANOTHER_SERIAL);

		assertTrue("Compare named events with serials", LESS_THAN >= namedVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable, times(TWO)).getName();
	}

	@Test
	public void compare_equalNumbers() {
		when(anotherEventVariable.getNumber()).thenReturn(EVENT_NUMBER);

		assertEquals("Compare equal number events", EQUAL, numberVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable).getNumber();
	}

	@Test
	public void compare_equalNumbers_bothWithSerials() {
		numberVariable = new OmniVariable.Event(EVENT_NUMBER, ANOTHER_SERIAL);
		when(anotherEventVariable.getNumber()).thenReturn(EVENT_NUMBER);
		when(anotherEventVariable.getSerial()).thenReturn(SERIAL);

		assertTrue("Compare number events with serials", GREATER_THAN <= numberVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable).getNumber();
	}

	@Test
	public void compare_equalNumbers_firstWithSerials() {
		namedVariable = new OmniVariable.Event(EVENT_NAME, SERIAL);
		when(anotherEventVariable.getName()).thenReturn(EVENT_NAME);

		assertTrue("Compare named events with serials", GREATER_THAN <= namedVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable, times(TWO)).getName();
	}

	@Test
	public void compare_equalNumbers_secondWithSerials() {
		when(anotherEventVariable.getName()).thenReturn(EVENT_NAME);
		when(anotherEventVariable.getSerial()).thenReturn(ANOTHER_SERIAL);

		assertTrue("Compare named events with serials", LESS_THAN >= namedVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable, times(TWO)).getName();
	}

	@Test
	public void compare_names() {
		when(anotherEventVariable.getName()).thenReturn(ANOTHER_EVENT_NAME);

		assertTrue("Compare named events", LESS_THAN >= namedVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable, times(TWO)).getName();
	}

	@Test
	public void compare_numbers() {
		when(anotherEventVariable.getNumber()).thenReturn(ANOTHER_EVENT_NUMBER);

		assertTrue("Compare number events", GREATER_THAN <= numberVariable.compareTo(anotherEventVariable));
		verify(anotherEventVariable).getNumber();
	}

	@Test
	public void compare_null() {
		anotherEventVariable = null;

		assertTrue("Compare the named event and null", LESS_THAN >= namedVariable.compareTo(anotherEventVariable));
	}

	/* Constructor */

	@Test
	public void namedEvent_stringName() {
		namedVariable = new OmniVariable.Event(EVENT_NAME.toString());

		assertEquals("Named event with serial", EVENT_NAME, namedVariable.getName());
	}

	@Test
	public void namedEvent_stringNameAndSerial() {
		namedVariable = new OmniVariable.Event(EVENT_NAME.toString(), SERIAL);

		assertEquals("Named event with serial", EVENT_NAME, namedVariable.getName());
		assertEquals("Named event with serial", SERIAL, namedVariable.getSerial());
	}

	@Test(expected=IllegalArgumentException.class)
	public void nullName() {
		OmniVariable.Event.Name nullName = null;

		namedVariable = new OmniVariable.Event(nullName);
	}

	/* Equals */

	@Test
	public void equals_nameAndNumber() {
		assertFalse("Name and number events", namedVariable.equals(numberVariable));
	}

	@Test
	public void equals_differentNames() {
		when(anotherEventVariable.getName()).thenReturn(ANOTHER_EVENT_NAME);

		assertFalse("Different names", namedVariable.equals(anotherEventVariable));
		verify(anotherEventVariable).getName();
	}

	@Test
	public void equals_differentNumbers() {
		when(anotherEventVariable.getNumber()).thenReturn(ANOTHER_EVENT_NUMBER);

		assertFalse("Different numbers", numberVariable.equals(anotherEventVariable));
		verify(anotherEventVariable).getNumber();
	}

	@Test
	public void equals_equalNames() {
		when(anotherEventVariable.getName()).thenReturn(EVENT_NAME);

		assertTrue("Equal events", namedVariable.equals(anotherEventVariable));
		verify(anotherEventVariable).getName();
	}

	@Test
	public void equals_equalNumbers() {
		when(anotherEventVariable.getNumber()).thenReturn(EVENT_NUMBER);

		assertTrue("Equal events", numberVariable.equals(anotherEventVariable));
		verify(anotherEventVariable).getNumber();
	}

	@Test
	public void equals_notEvent() {
		assertFalse("Event and conversion variables", namedVariable.equals(anotherVariable));
	}

	@Test
	public void equals_null() {
		anotherVariable = null;

		assertFalse("Named event: null event", namedVariable.equals(anotherVariable));
		assertFalse("Number event: null event", numberVariable.equals(anotherVariable));
	}

	/* hashCode() */

	@Test
	public void namedVariableHashCode() {
		assertEquals("Named event: hashcode", EVENT_NAME.toString().hashCode(), namedVariable.hashCode());
	}

	@Test
	public void numberVariableHashCode() {
		assertEquals("Number event: hashcode", Integer.valueOf(EVENT_NUMBER).hashCode(), numberVariable.hashCode());
	}

	/* prepareSerial() */

	@Test
	public void nullSerial() {
		String serial = null;

		assertNull("Null serial", namedVariable.prepareSerial(serial));
	}

	@Test
	public void emptySerial() {
		String serial = "";

		assertNull("Empty serial", namedVariable.prepareSerial(serial));
	}

	@Test
	public void spaceSerial() {
		String serial = "";

		assertNull("Space serial", namedVariable.prepareSerial(serial));
	}

	/* Name.forName() */

	@Test(expected=IllegalArgumentException.class)
	public void forName_nullName() {
		String name = null;

		OmniVariable.Event.Name.forName(name);
	}

	@Test(expected=IllegalArgumentException.class)
	public void forName_unknownName() {
		OmniVariable.Event.Name.forName(SERIAL);
	}

	/* toString() */

	@Test
	public void namedVariableToString() {
		String testString = null;

		testString = namedVariable.toString();

		assertNotNull("String exists", testString);
		assertEquals("Event string", EVENT_NAME.toString(), testString);
	}

	@Test
	public void numberVariableToString() {
		String testString = null;

		testString = numberVariable.toString();

		assertNotNull("String exists", testString);
		assertEquals("Event string", "event10", testString);
	}

	@Test
	public void namedVariableWithSerialToString() {
		String testString = null;

		namedVariable = new OmniVariable.Event(EVENT_NAME, SERIAL);

		testString = namedVariable.toString();

		assertNotNull("String exists", testString);
		assertEquals("Event string", "purchase:abcde9876", testString);
	}

	@Test
	public void numberVariableWithSerialToString() {
		String testString = null;

		numberVariable = new OmniVariable.Event(EVENT_NUMBER, SERIAL);

		testString = numberVariable.toString();

		assertNotNull("String exists", testString);
		assertEquals("Event string", "event10:abcde9876", testString);
	}

	@Before
	public void setUp() throws Exception {
		anotherVariable = new OmniVariable.Conversion(EVENT_NUMBER);
		namedVariable = new OmniVariable.Event(EVENT_NAME);
		numberVariable = new OmniVariable.Event(EVENT_NUMBER);
	}

}