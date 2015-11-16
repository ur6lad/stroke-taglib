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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class OmniToolsTest {

	private static final String CATEGORY = "category";
	private static final String CATEGORY_VALUE = "radios";
	private static final String EMPTY = "";
	private static final int EIGHT = 8;
	private static final String EVENTS = "events";
	private static final String EVENT_VALUE = "blah";
	private static final int FIVE = 5;
	private static final int FOUR = 4;
	private static final int NEGATIVE_NUMBER = -2147483648;
	private static final String NEGATIVE_VALUE = "min";
	private static final int ONE = 1;
	private static final String HOMEPAGE = "homepage";
	private static final String PARENTHESES = "()";
	private static final int POSITIVE_NUMBER = 2147483647;
	private static final String POSITIVE_VALUE = "max";
	private static final int PREVIOUS_DAY = -1;
	private static final String PRODUCT = "product";
	private static final String PRODUCT_ID = "01010101";
	private static final String PRODUCTS = "products";
	private static final String QUANTITY = "quantity";
	private static final String QUANTITY_VALUE = "10";
	private static final OmniVariable.Event.Name TEST_EVENT = OmniVariable.Event.Name.Checkout;
	private static final int SIX = 6;
	private static final int THREE = 3;
	private static final String TOTAL_COST = "totalCost";
	private static final String TOTAL_COST_VALUE = "UAH 20";
	private static final int TWO = 2;
	private static final int ZERO = 0;
	private static final String ZERO_VALUE = "zero";

	private BarAnalyticBean analyticBean;
	private BarConversion barConversion;
	private BarEvent barEvent;
	private OmniBean commonBean;
	private OmniVariable doPlugins;
	private FooConversion fooConversion;
	private FooEvent fooEvent;
	private FooVariable negativeFooVariable;
	private OmniVariable pageName;
	private FooVariable positiveFooVariable;
	private FooProductBean productBean;
	private BarVariable yetAnotherBarVariable;
	private FooVariable zeroFooVariable;

	/* beanToMap() */

	@Test
	public void analyticBean_eventsAndNamedVariables() throws UnsupportedVariableException {
		Map<String, String> variables;

		analyticBean.addVariable(negativeFooVariable, NEGATIVE_VALUE);
		analyticBean.addVariable(positiveFooVariable, POSITIVE_VALUE);
		analyticBean.addVariable(pageName, HOMEPAGE);
		analyticBean.addVariable(doPlugins, PARENTHESES);
		analyticBean.addVariable(fooEvent);
		analyticBean.addVariable(barEvent, ZERO_VALUE);

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: size", FIVE, variables.size());
		assertEquals("Analytic variables: pageName exists",
					 "\'" + HOMEPAGE + '\'',
					 variables.get(OmniVariable.Standard.Name.PageName.toString()));
		assertEquals("Analytic variables: doPlugins exists",
					 PARENTHESES,
					 variables.get(OmniVariable.Standard.Name.DoPlugins.toString()));
		assertEquals("Analytic variables: negative variable exists",
					 "\'" + NEGATIVE_VALUE + '\'',
					 variables.get(negativeFooVariable.toString()));
		assertEquals("Analytic variables: positive variable exists",
					 "\'" + POSITIVE_VALUE + '\'',
					 variables.get(positiveFooVariable.toString()));
		assertEquals("Analytic variables: events",
					 "'" + barEvent + '=' + ZERO_VALUE + ',' + fooEvent + '\'',
					 variables.get(EVENTS));
	}

	@Test
	public void analyticBean_eventsAndProducts() throws UnsupportedVariableException {
		Map<String, String> variables;

		productBean.setProduct(PRODUCT_ID);
		productBean.addVariable(fooEvent, EVENT_VALUE);
		productBean.addVariable(barConversion, NEGATIVE_VALUE);
		productBean.addVariable(fooConversion, POSITIVE_VALUE);
		analyticBean.addProduct(productBean);
		analyticBean.addVariable(barEvent);

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: size", TWO, variables.size());
		assertEquals("Analytic variables: events",
					 "'" + barEvent + ',' + fooEvent + '\'',
					 variables.get(EVENTS));
		assertEquals("Analytic variables: products",
					 "';01010101;;;foo1=blah;barConv-2147483648=min|fooConv2147483647=max'",
					 variables.get(PRODUCTS));
	}

	@Test
	public void analyticBean_eventsAndProductsAndVariables() throws UnsupportedVariableException {
		Map<String, String> variables;

		productBean.setProduct(PRODUCT_ID);
		productBean.addVariable(fooEvent, EVENT_VALUE);
		productBean.addVariable(barConversion, NEGATIVE_VALUE);
		productBean.addVariable(fooConversion, POSITIVE_VALUE);
		analyticBean.addVariable(yetAnotherBarVariable, ZERO_VALUE);
		analyticBean.addProduct(productBean);
		analyticBean.addVariable(barEvent);

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: named variable exists",
					 "\'" + ZERO_VALUE + '\'',
					 variables.get(yetAnotherBarVariable.toString()));
		assertEquals("Analytic variables: events",
					 "'" + barEvent + ',' + fooEvent + '\'',
					 variables.get(EVENTS));
		assertTrue("Analytic variables: another empty variable exists",
				   variables.containsKey(yetAnotherBarVariable.toString()));
		assertEquals("Analytic variables: products",
					 "';01010101;;;foo1=blah;barConv-2147483648=min|fooConv2147483647=max'",
					 variables.get(PRODUCTS));
		assertEquals("Analytic variables: size", THREE, variables.size());
	}

	@Test
	public void analyticBean_eventsAndVariables() throws UnsupportedVariableException {
		Map<String, String> variables;

		analyticBean.addVariable(negativeFooVariable, NEGATIVE_VALUE);
		analyticBean.addVariable(positiveFooVariable, POSITIVE_VALUE);
		analyticBean.addVariable(fooEvent);
		analyticBean.addVariable(barEvent);

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: negative variable exists",
					 "\'" + NEGATIVE_VALUE + '\'',
					 variables.get(negativeFooVariable.toString()));
		assertEquals("Analytic variables: positive variable exists",
					 "\'" + POSITIVE_VALUE + '\'',
					 variables.get(positiveFooVariable.toString()));
		assertEquals("Analytic variables: events",
					 "'" + barEvent + ',' + fooEvent + '\'',
					 variables.get(EVENTS));
		assertEquals("Analytic variables: size", THREE, variables.size());
	}

	@Test
	public void analyticBean_null() {
		Map<String, String> variables;

		analyticBean = null;

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables, null bean", variables);
		assertEquals("Analytic variables: size", ZERO, variables.size());
	}

	@Test
	public void analyticBean_products() throws UnsupportedVariableException {
		Map<String, String> variables;

		productBean.setProduct(PRODUCT_ID);
		productBean.addVariable(barConversion, NEGATIVE_VALUE);
		productBean.addVariable(fooConversion, POSITIVE_VALUE);
		analyticBean.addProduct(productBean);

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: size", ONE, variables.size());
		assertEquals("Analytic variables: products",
					 "';01010101;;;;barConv-2147483648=min|fooConv2147483647=max'",
					 variables.get(PRODUCTS));
	}

	@Test
	public void analyticBean_variables() throws UnsupportedVariableException {
		Map<String, String> variables;

		analyticBean.addVariable(negativeFooVariable, NEGATIVE_VALUE);
		analyticBean.addVariable(positiveFooVariable, POSITIVE_VALUE);

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: size", TWO, variables.size());
		assertEquals("Analytic variables: negative variable exists",
					 "\'" + NEGATIVE_VALUE + '\'',
					 variables.get(negativeFooVariable.toString()));
		assertEquals("Analytic variables: positive variable exists",
					 "\'" + POSITIVE_VALUE + '\'',
					 variables.get(positiveFooVariable.toString()));
	}

	@Test
	public void analyticBean_withoutEvents() throws UnsupportedVariableException {
		Map<String, String> variables;

		analyticBean.addVariable(negativeFooVariable, NEGATIVE_VALUE);
		analyticBean.addVariable(positiveFooVariable, POSITIVE_VALUE);
		analyticBean.addVariable(pageName, HOMEPAGE);
		analyticBean.addVariable(doPlugins, PARENTHESES);

		variables = OmniTools.beanToMap(analyticBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: size", FOUR, variables.size());
		assertEquals("Analytic variables: pageName exists",
					 "\'" + HOMEPAGE + '\'',
					 variables.get(OmniVariable.Standard.Name.PageName.toString()));
		assertEquals("Analytic variables: doPlugins exists",
					 PARENTHESES,
					 variables.get(OmniVariable.Standard.Name.DoPlugins.toString()));
		assertEquals("Analytic variables: negative variable exists",
					 "\'" + NEGATIVE_VALUE + '\'',
					 variables.get(negativeFooVariable.toString()));
		assertEquals("Analytic variables: positive variable exists",
					 "\'" + POSITIVE_VALUE + '\'',
					 variables.get(positiveFooVariable.toString()));
		assertNull("Analytic variables: events don\'t exist", variables.get(EVENTS));
	}

	@Test
	public void commonBean() throws UnsupportedVariableException {
		Map<String, String> variables;

		commonBean.addVariable(negativeFooVariable, NEGATIVE_VALUE);
		commonBean.addVariable(positiveFooVariable, POSITIVE_VALUE);
		commonBean.addVariable(pageName, HOMEPAGE);
		commonBean.addVariable(doPlugins, PARENTHESES);
		commonBean.addVariable(fooEvent);
		commonBean.addVariable(barEvent);

		variables = OmniTools.beanToMap(commonBean);

		assertNotNull("Analytic variables", variables);
		assertEquals("Analytic variables: size", SIX, variables.size());
		assertEquals("Analytic variables: pageName exists",
					 "\'" + HOMEPAGE + '\'',
					 variables.get(OmniVariable.Standard.Name.PageName.toString()));
		assertEquals("Analytic variables: doPlugins exists",
					 PARENTHESES,
					 variables.get(OmniVariable.Standard.Name.DoPlugins.toString()));
		assertEquals("Analytic variables: negative variable exists",
					 "\'" + NEGATIVE_VALUE + '\'',
					 variables.get(negativeFooVariable.toString()));
		assertEquals("Analytic variables: positive variable exists",
					 "\'" + POSITIVE_VALUE + '\'',
					 variables.get(positiveFooVariable.toString()));
		assertNull("Analytic variables: events don\'t exist", variables.get(EVENTS));
	}

	@Test
	public void productBean() throws UnsupportedVariableException {
		Map<String, String> variables;

		productBean.setCategory(CATEGORY_VALUE);
		productBean.setProduct(PRODUCT_ID);
		productBean.setQuantity(QUANTITY_VALUE);
		productBean.setTotalCost(TOTAL_COST_VALUE);
		productBean.addVariable(fooEvent, EVENT_VALUE);
		productBean.addVariable(barEvent, ZERO_VALUE);
		productBean.addVariable(barConversion, NEGATIVE_VALUE);
		productBean.addVariable(fooConversion, POSITIVE_VALUE);

		variables = OmniTools.beanToMap(productBean);

		assertNotNull("Product variables", variables);
		assertEquals("Product variables: size", EIGHT, variables.size());
		assertEquals("Product variables: category", CATEGORY_VALUE, variables.get(CATEGORY));
		assertEquals("Product variables: product", PRODUCT_ID, variables.get(PRODUCT));
		assertEquals("Product variables: category", QUANTITY_VALUE, variables.get(QUANTITY));
		assertEquals("Product variables: category", TOTAL_COST_VALUE, variables.get(TOTAL_COST));
		assertEquals("Product variables: negative variable", NEGATIVE_VALUE, variables.get(barConversion.toString()));
		assertEquals("Product variables: positive variable", POSITIVE_VALUE, variables.get(fooConversion.toString()));
		assertEquals("Product variables: number event", EVENT_VALUE, variables.get(fooEvent.toString()));
		assertEquals("Product variables: name event", ZERO_VALUE, variables.get(barEvent.toString()));
	}

	/* Filter map */

	@Test
	public void filterNullMap() {
		Map<OmniVariable, String> sourceMap = null;
		Map<OmniVariable, String> filteredMap;

		filteredMap = OmniTools.filterMap(sourceMap, FooVariable.class);

		assertNotNull("Map exists", filteredMap);
		assertEquals("Filtered map: size", ZERO, filteredMap.size());
	}

	@Test
	public void filterMapFooVaribales() {
		Map<OmniVariable, String> sourceMap = new TreeMap<OmniVariable, String>();
		Map<OmniVariable, String> filteredMap;

		sourceMap.put(negativeFooVariable, NEGATIVE_VALUE);
		sourceMap.put(positiveFooVariable, POSITIVE_VALUE);
		sourceMap.put(yetAnotherBarVariable, ZERO_VALUE);

		filteredMap = OmniTools.filterMap(sourceMap, FooVariable.class);

		assertNotNull("Map exists", filteredMap);
		assertEquals("Filtered map: size", TWO, filteredMap.size());
		assertEquals("Filtered map: negative variable exists", NEGATIVE_VALUE, filteredMap.get(negativeFooVariable));
		assertEquals("Filtered map: positive variable exists", POSITIVE_VALUE, filteredMap.get(positiveFooVariable));
	}

	@Test
	public void filterMapBarVaribales() {
		Map<OmniVariable, String> sourceMap = new TreeMap<OmniVariable, String>();
		Map<OmniVariable, String> filteredMap;

		sourceMap.put(negativeFooVariable, NEGATIVE_VALUE);
		sourceMap.put(positiveFooVariable, POSITIVE_VALUE);

		filteredMap = OmniTools.filterMap(sourceMap, BarVariable.class);

		assertNotNull("Map exists", filteredMap);
		assertEquals("Filtered map: size", ZERO, filteredMap.size());
		assertEquals("Filtered map: yet another variable doesn't exist", null, filteredMap.get(yetAnotherBarVariable));
	}

	/* mapToString() */

	@Test
	public void separator() {
		char separator = '+';
		Map<OmniVariable, String> sourceMap = new TreeMap<OmniVariable, String>();
		String testString;

		sourceMap.put(negativeFooVariable, NEGATIVE_VALUE);
		sourceMap.put(positiveFooVariable, POSITIVE_VALUE);
		sourceMap.put(zeroFooVariable, ZERO_VALUE);

		testString = OmniTools.mapToString(sourceMap, separator);

		assertEquals("Map to string: value", "foo-blah-2147483648=min+foo-blah0=zero+foo-blah2147483647=max", testString);
	}


	@Test
	public void emptyMapToString() {
		Map<OmniVariable, String> sourceMap	 = new TreeMap<OmniVariable, String>();
		String testString;

		testString = OmniTools.mapToString(sourceMap);

		assertEquals("Empty value", EMPTY, testString);
	}

	@Test
	public void mapToString() {
		Map<OmniVariable, String> sourceMap = new TreeMap<OmniVariable, String>();
		String testString;

		sourceMap.put(negativeFooVariable, NEGATIVE_VALUE);
		sourceMap.put(positiveFooVariable, POSITIVE_VALUE);
		sourceMap.put(zeroFooVariable, ZERO_VALUE);

		testString = OmniTools.mapToString(sourceMap);

		assertEquals("Map to string: value", "foo-blah-2147483648=min|foo-blah0=zero|foo-blah2147483647=max", testString);
	}

	@Test
	public void mapWithNullsToString_first() {
		Map<OmniVariable, String> sourceMap = new LinkedHashMap<OmniVariable, String>();
		String testString;

		sourceMap.put(null, ZERO_VALUE);
		sourceMap.put(positiveFooVariable, POSITIVE_VALUE);
		sourceMap.put(negativeFooVariable, NEGATIVE_VALUE);

		testString = OmniTools.mapToString(sourceMap);

		assertEquals("Map to string: value", "foo-blah2147483647=max|foo-blah-2147483648=min", testString);
	}

	@Test
	public void mapWithNullsToString_next() {
		Map<OmniVariable, String> sourceMap = new LinkedHashMap<OmniVariable, String>();
		String testString;

		sourceMap.put(positiveFooVariable, POSITIVE_VALUE);
		sourceMap.put(null, ZERO_VALUE);
		sourceMap.put(negativeFooVariable, NEGATIVE_VALUE);

		testString = OmniTools.mapToString(sourceMap);

		assertEquals("Map to string: value", "foo-blah2147483647=max|foo-blah-2147483648=min", testString);
	}

	@Test
	public void mapWithNullsToString_last() {
		Map<OmniVariable, String> sourceMap = new LinkedHashMap<OmniVariable, String>();
		String testString;

		sourceMap.put(positiveFooVariable, POSITIVE_VALUE);
		sourceMap.put(negativeFooVariable, NEGATIVE_VALUE);
		sourceMap.put(null, ZERO_VALUE);

		testString = OmniTools.mapToString(sourceMap);

		assertEquals("Map to string: value", "foo-blah2147483647=max|foo-blah-2147483648=min", testString);
	}

	@Test
	public void nullMapToString() {
		Map<OmniVariable, String> sourceMap = null;
		String testString;

		testString = OmniTools.mapToString(sourceMap);

		assertEquals("Empty value", EMPTY, testString);
	}

	/* setToString() */

	@Test
	public void emptySetToString() {
		Set<OmniVariable> set = new TreeSet<OmniVariable>();
		String testString;

		testString = OmniTools.setToString(set);

		assertEquals("Set to string: empty value", EMPTY, testString);
	}

	@Test
	public void setToString() {
		Set<OmniVariable> set = new TreeSet<OmniVariable>();
		String testString;

		set.add(positiveFooVariable);
		set.add(zeroFooVariable);
		set.add(negativeFooVariable);
		testString = OmniTools.setToString(set);

		assertEquals("Set to string: value", "foo-blah-2147483648,foo-blah0,foo-blah2147483647", testString);
	}

	@Test
	public void setWithNullsToString_first() {
		Set<OmniVariable> set = new LinkedHashSet<OmniVariable>();
		String testString;

		set.add(null);
		set.add(positiveFooVariable);
		set.add(negativeFooVariable);
		testString = OmniTools.setToString(set);

		assertEquals("Set to string: value", "foo-blah2147483647,foo-blah-2147483648", testString);
	}

	@Test
	public void setWithNullsToString_next() {
		Set<OmniVariable> set = new LinkedHashSet<OmniVariable>();
		String testString;

		set.add(positiveFooVariable);
		set.add(null);
		set.add(negativeFooVariable);
		testString = OmniTools.setToString(set);

		assertEquals("Set to string: value", "foo-blah2147483647,foo-blah-2147483648", testString);
	}

	@Test
	public void setWithNullsToString_last() {
		Set<OmniVariable> set = new LinkedHashSet<OmniVariable>();
		String testString;

		set.add(positiveFooVariable);
		set.add(negativeFooVariable);
		set.add(null);
		testString = OmniTools.setToString(set);

		assertEquals("Set to string: value", "foo-blah2147483647,foo-blah-2147483648", testString);
	}

	/* notNull(), map */

	@Test
	public void notNullMap() {
		Calendar calendar = Calendar.getInstance();
		Map<Date, Integer> sourceMap = null, testMap = null;

		sourceMap = new TreeMap<Date, Integer>();
		sourceMap.put(calendar.getTime(), POSITIVE_NUMBER);
		calendar.add(Calendar.DAY_OF_YEAR, PREVIOUS_DAY);
		sourceMap.put(calendar.getTime(), NEGATIVE_NUMBER);

		testMap = OmniTools.notNull(sourceMap);

		assertNotNull("Map exists", testMap);
		assertEquals("Maps are equals", sourceMap, testMap);
		assertTrue("The same map", sourceMap == testMap);
	}

	@Test
	public void nullMap() {
		Map<Date, Integer> sourceMap = null, testMap = null;

		testMap = OmniTools.notNull(sourceMap);

		assertNotNull("Map is created", testMap);
		assertEquals("Map is empty", ZERO, testMap.size());
	}

	@Test
	public void notNullString() {
		String sourceSet = "source string", testSet = null;

		testSet = OmniTools.notNull(sourceSet);

		assertNotNull("String exists", testSet);
		assertEquals("Strings are equals", sourceSet, testSet);
		assertTrue("The same string", sourceSet == testSet);
	}

	@Test
	public void nullString() {
		String sourceString = null, testString = null;

		testString = OmniTools.notNull(sourceString);

		assertNotNull("Set is created", testString);
		assertEquals("Set is empty", ZERO, testString.length());
	}

	@Before
	public void setUp() throws Exception {
		analyticBean = new BarAnalyticBean();
		barConversion = new BarConversion(NEGATIVE_NUMBER);
		barEvent = new BarEvent(TEST_EVENT);
		commonBean = new OmniBean();
		doPlugins = new OmniVariable.Standard(OmniVariable.Standard.Name.DoPlugins);
		fooConversion = new FooConversion(POSITIVE_NUMBER);
		fooEvent = new FooEvent(ONE);
		negativeFooVariable = new FooVariable(NEGATIVE_NUMBER);
		pageName = new OmniVariable.Standard(OmniVariable.Standard.Name.PageName);
		positiveFooVariable = new FooVariable(POSITIVE_NUMBER);
		productBean = new FooProductBean();
		yetAnotherBarVariable = new BarVariable(ONE);
		zeroFooVariable = new FooVariable(ZERO);
	}

	static class BarAnalyticBean extends AnalyticBean {
		// do nothing
	}

	static class BarConversion extends OmniVariable.Conversion {

		private static final String BAR = "barConv";

		public BarConversion(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return BAR;
		}

	}

	static class BarEvent extends OmniVariable.Event {

		private static final String BAR = "bar";

		public BarEvent(int number) {
			super(number);
		}

		public BarEvent(Name name) {
			super(name);
		}

		@Override
		public String getPrefix() {
			return BAR;
		}

	}

	static class BarVariable extends OmniVariable {

		private static final String TEST_PREFIX = "bar-blah";

		public BarVariable(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return TEST_PREFIX;
		}

	}

	static class FooConversion extends OmniVariable.Conversion {

		private static final String FOO = "fooConv";

		public FooConversion(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return FOO;
		}

	}

	static class FooEvent extends OmniVariable.Event {

		private static final String FOO = "foo";

		public FooEvent(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return FOO;
		}

	}

	static class FooProductBean extends ProductBean {
		// do nothing
	}

	static class FooVariable extends OmniVariable {

		private static final String TEST_PREFIX = "foo-blah";

		public FooVariable(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return TEST_PREFIX;
		}

	}

}