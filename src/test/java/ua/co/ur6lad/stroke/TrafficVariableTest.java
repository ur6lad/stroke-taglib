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
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class TrafficVariableTest {

	private static final int VARIABLE_NUMBER = 15;

	private OmniVariable.Traffic variable;

	@Test
	public void variableToString() {
		String testString = null;

		testString = variable.toString();

		assertNotNull("String exists", testString);
		assertEquals("Traffic variable: to string", "prop15", testString);
	}

	@Before
	public void setUp() throws Exception {
		variable = new OmniVariable.Traffic(VARIABLE_NUMBER);
	}

}