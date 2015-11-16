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

import org.junit.Test;

public class OmniVariableTest {

	private static final String ABC = "ABC";
	private static final int EQUAL = 0;
	private static final int GREATER_THAN = 1;
	private static final int LESS_THAN = -1;
	private static final int NUMBER_1 = 556;
	private static final int NUMBER_2 = 7910;
	private static final String XYZ = "XYZ";

	OmniVariable variableA;
	OmniVariable variableB;
	OmniVariable variableC;

	/* Compare */

	@Test
	public void compare_differentTypesAndNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_2);

		assertTrue("Compare different types and numbers, variable A", LESS_THAN >= variableA.compareTo(variableB));
		assertTrue("Compare different types and numbers, variable B", GREATER_THAN <= variableB.compareTo(variableA));
	}

	@Test
	public void compare_differentTypesButEqualNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_1);

		assertEquals("Compare different types but equal numbers, variable A", EQUAL, variableA.compareTo(variableB));
		assertEquals("Compare different types but equal numbers, variable B", EQUAL, variableB.compareTo(variableA));
	}

	@Test
	public void compare_equalTypesButDifferentNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new A(NUMBER_2);

		assertTrue("Compare equal types but different numbers, variable A", LESS_THAN >= variableA.compareTo(variableB));
		assertTrue("Compare equal types but different numbers, variable B", GREATER_THAN <= variableB.compareTo(variableA));
	}

	@Test
	public void compare_equalTypesAndNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new A(NUMBER_1);

		assertEquals("Compare equal types and numbers, variable A", EQUAL, variableA.compareTo(variableB));
		assertEquals("Compare equal types and numbers, variable B", EQUAL, variableB.compareTo(variableA));
	}

	@Test
	public void compare_null() {
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_2);
		variableC = null;

		assertTrue("Compare with a variable with null number, variable A", LESS_THAN >= variableA.compareTo(variableC));
		assertTrue("Compare with a variable with null number, variable B", LESS_THAN >= variableB.compareTo(variableC));
	}

	/* Equals */

	@Test
	public void equals_notVariable() {
		String obj = "object";
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_2);


		assertFalse("Not equal objects, variable A", variableA.equals(obj));
		assertFalse("Not equal objects, variable B", variableB.equals(obj));
	}


	@Test
	public void equals_differentTypesAndNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_2);

		assertFalse("Different types and numbers, variable A", variableA.equals(variableB));
		assertFalse("Different types and numbers, variable B", variableB.equals(variableA));
	}

	@Test
	public void equals_differentTypesButEqualNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_1);

		assertFalse("Different types but equal numbers, variable A", variableA.equals(variableB));
		assertFalse("Different types but equal numbers, variable B", variableB.equals(variableA));
	}

	@Test
	public void equals_equalTypesButDifferentNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new A(NUMBER_2);

		assertFalse("Equal types but different numbers, variable A", variableA.equals(variableB));
		assertFalse("Equal types but different numbers, variable B", variableB.equals(variableA));
	}

	@Test
	public void equals_equalTypesAndNumbers() {
		variableA = new A(NUMBER_1);
		variableB = new A(NUMBER_1);

		assertTrue("Equal types and numbers, variable A", variableA.equals(variableB));
		assertTrue("Equal types and numbers, variable B", variableB.equals(variableA));
	}

	@Test
	public void equals_null() {
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_2);
		variableC = null;

		assertFalse("Not equal number variables, variable A", variableA.equals(variableC));
		assertFalse("Not equal number variables, variable B", variableB.equals(variableC));
	}

	/* hashCode() */

	@Test
	public void hashCodeAndNumber() {
		variableA = new A(NUMBER_1);

		assertEquals("Hashcode equals number's one", Integer.valueOf(NUMBER_1).hashCode(), variableA.hashCode());
	}

	/* toString() */

	@Test
	public void asString() {
		variableA = new A(NUMBER_1);
		variableB = new B(NUMBER_2);

		assertEquals("To string, variable A", ABC + NUMBER_1, variableA.toString());
		assertEquals("To string, variable B", XYZ + NUMBER_2, variableB.toString());
	}

	public class A extends OmniVariable {

		public A(int number) {
			super(number);
		}

		public String getPrefix() {
			return ABC;
		}

	}

	public class B extends OmniVariable {

		public B(int number) {
			super(number);
		}

		public String getPrefix() {
			return XYZ;
		}

	}

}