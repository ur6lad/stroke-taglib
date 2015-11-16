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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductBeanTest {

	private static final String ANOTHER_PRODUCT_ID = "product99";
	private static final String CATEGORY_ID = "category1";
	private static final int EQUAL = 0;
	private static final int EVENT_1 = 13;
	private static final int EVENT_2 = 27;
	private static final int EVENT_3 = -1;
	private static final String EVENT_VALUE_1 = "fge";
	private static final String EVENT_VALUE_2 = "opqrst";
	private static final String EVENT_VALUE_3 = "xyz";
	private static final int KEY_AND_VALUE = 2;
	private static final int LESS_THAN = -1;
	private static final String PAGE_NAME = "pageName";
	private static final String PRODUCT_ID = "product2";
	private static final int PRODUCT_STRING_SIZE = 6;
	private static final String QUANTITY = "quantity3";
	private static final String TOTAL_COST = "totalCost4";
	private static final int TWO = 2;
	private static final int VARIABLE_1 = 1;
	private static final int VARIABLE_2 = 0;
	private static final String VARIABLE_VALUE_1 = "last";
	private static final String VARIABLE_VALUE_2 = "zero";

	@Mock
	private ProductBean anotherBean;

	private ProductBean bean;

	/* Equals */

	@Test
	public void alienObject() {
		bean.setProduct(PRODUCT_ID);

		assertFalse("Different beans", bean.equals(VARIABLE_VALUE_1));
	}

	@Test
	public void beanWithNullProduct() {
		bean.setProduct(PRODUCT_ID);

		assertFalse("Bean with null product", bean.equals(anotherBean));
	}

	@Test
	public void differentBeans() {
		bean.setProduct(PRODUCT_ID);
		when(anotherBean.getProduct()).thenReturn(ANOTHER_PRODUCT_ID);

		assertFalse("Different beans", bean.equals(anotherBean));
		verify(anotherBean).getProduct();
	}

	@Test
	public void equalBeans() {
		bean.setProduct(PRODUCT_ID);
		when(anotherBean.getProduct()).thenReturn(PRODUCT_ID);

		assertTrue("Equal beans", bean.equals(anotherBean));
		verify(anotherBean).getProduct();
	}

	@Test
	public void nullBean() {
		ProductBean nullBean = null;

		bean.setProduct(PRODUCT_ID);

		assertFalse("Null bean", bean.equals(nullBean));
	}

	@Test
	public void whenProductIsNull() {
		when(anotherBean.getProduct()).thenReturn(ANOTHER_PRODUCT_ID);

		assertFalse("Bean with null product", bean.equals(anotherBean));
	}

	/* toString() */

	@Test
	public void beanToString() throws UnsupportedVariableException {
		List<String> variables;
		String[] splittedTestString, variable;
		String testString;
		Map<String, String> expectedEvents = new LinkedHashMap<String, String>();
		Map<String, String> expectedVariables = new LinkedHashMap<String, String>();

		expectedEvents.put("event" + EVENT_1, EVENT_VALUE_1);
		expectedEvents.put("event" + EVENT_3, EVENT_VALUE_3);
		expectedEvents.put("event" + EVENT_2, EVENT_VALUE_2);
		expectedVariables.put("eVar" + VARIABLE_1, VARIABLE_VALUE_1);
		expectedVariables.put("eVar" + VARIABLE_2, VARIABLE_VALUE_2);
		bean.setCategory(CATEGORY_ID);
		bean.addVariable(new OmniVariable.Conversion(VARIABLE_1), VARIABLE_VALUE_1);
		bean.addVariable(new OmniVariable.Conversion(VARIABLE_2), VARIABLE_VALUE_2);
		bean.addVariable(new OmniVariable.Event(EVENT_1), EVENT_VALUE_1);
		bean.addVariable(new OmniVariable.Event(EVENT_2), EVENT_VALUE_2);
		bean.addVariable(new OmniVariable.Event(EVENT_3), EVENT_VALUE_3);
		bean.setProduct(PRODUCT_ID);
		bean.setQuantity(QUANTITY);
		bean.setTotalCost(TOTAL_COST);

		testString = bean.toString();

		assertNotNull("String exists", testString);

		splittedTestString = testString.split(";");
		assertEquals("Product string parts", PRODUCT_STRING_SIZE, splittedTestString.length);
		assertEquals("Category", CATEGORY_ID, splittedTestString[0]);
		assertEquals("Product", PRODUCT_ID, splittedTestString[1]);
		assertEquals("Quantity", QUANTITY, splittedTestString[2]);
		assertEquals("Total cost", TOTAL_COST, splittedTestString[3]);

		variables = Arrays.asList(splittedTestString[4].split("\\|"));
		for (String eventString : variables) {
			variable = eventString.split("=");
			assertEquals("Event: key and value", KEY_AND_VALUE, variable.length);
			assertEquals("Event", expectedEvents.get(variable[0]), variable[1]);
			expectedEvents.remove(variable[0]);
		}
		assertTrue("All events are found", expectedEvents.isEmpty());

		variables = Arrays.asList(splittedTestString[5].split("\\|"));
		for (String variableString : variables) {
			variable = variableString.split("=");
			assertEquals("Conversion variable: key and value", KEY_AND_VALUE, variable.length);
			assertEquals("Conversion variable", expectedVariables.get(variable[0]), variable[1]);
			expectedVariables.remove(variable[0]);
		}
		assertTrue("All conversion variables are found", expectedVariables.isEmpty());
	}

	@Test
	public void emptyBeanToString() {
		String testString = null;

		testString = bean.toString();

		assertNotNull("String exists", testString);
		assertEquals("Product string", ";;;;;", testString);
	}

	/* hashCode() */

	@Test
	public void hashCodeAndEmptyProduct() {
		assertNotNull("Hashcode of the empty bean", bean.hashCode());
	}

	@Test
	public void hashCodeAndProduct() {
		bean.setProduct(PRODUCT_ID);

		assertEquals("Hashcode equals number's one", PRODUCT_ID.hashCode(), bean.hashCode());
	}

	/* Compare */

	@Test
	public void compareBeanWithNullProduct() {
		bean.setProduct(PRODUCT_ID);

		assertEquals("Compare with a bean with null product", EQUAL, bean.compareTo(anotherBean));
	}

	@Test
	public void compareDifferentBeans() {
		bean.setProduct(PRODUCT_ID);
		when(anotherBean.getProduct()).thenReturn(ANOTHER_PRODUCT_ID);

		assertTrue("Different beans", LESS_THAN >= bean.compareTo(anotherBean));
		verify(anotherBean, times(TWO)).getProduct();
	}

	@Test
	public void compareEqualBeans() {
		bean.setProduct(PRODUCT_ID);
		when(anotherBean.getProduct()).thenReturn(PRODUCT_ID);

		assertEquals("Compare equal beans", EQUAL, bean.compareTo(anotherBean));
		verify(anotherBean, times(TWO)).getProduct();
	}

	@Test
	public void compareNullBean() {
		ProductBean nullBean = null;

		bean.setProduct(PRODUCT_ID);

		assertEquals("Compare with the null bean", EQUAL, bean.compareTo(nullBean));
	}

	@Test
	public void compareWhenProductIsNull() {
		when(anotherBean.getProduct()).thenReturn(ANOTHER_PRODUCT_ID);

		assertEquals("Compare when the bean with null product", EQUAL, bean.compareTo(anotherBean));
	}

	/* Unsupported variable types */

	@Test(expected=UnsupportedVariableException.class)
	public void unsupportedHierarchy() throws UnsupportedVariableException {
		OmniVariable.Hierarchy variable = new OmniVariable.Hierarchy(VARIABLE_1);

		bean.addVariable(variable, VARIABLE_VALUE_1);
	}

	@Test(expected=UnsupportedVariableException.class)
	public void unsupportedStandardVariable() throws UnsupportedVariableException {
		OmniVariable.Standard variable = new OmniVariable.Standard(PAGE_NAME);

		bean.addVariable(variable, VARIABLE_VALUE_1);
	}

	@Test(expected=UnsupportedVariableException.class)
	public void unsupportedTrafficTraffic() throws UnsupportedVariableException {
		OmniVariable.Traffic variable = new OmniVariable.Traffic(VARIABLE_1);

		bean.addVariable(variable, VARIABLE_VALUE_1);
	}

	@Test(expected=UnsupportedVariableException.class)
	public void unsupportedVariableSet() throws UnsupportedVariableException {
		OmniVariable.Event variable = new OmniVariable.Event(VARIABLE_1);

		bean.addVariable(variable);
	}

	@Before
	public void setUp() throws Exception {
		bean = new ProductBean();
	}

}