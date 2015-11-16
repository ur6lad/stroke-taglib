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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class AnalyticBeanTest {

	private static final int EVENT_1 = 27;
	private static final OmniVariable.Event.Name EVENT_2 = OmniVariable.Event.Name.Checkout;
	private static final int NO_ITEMS = 0;
	private static final int ONE_ITEM = 1;
	private static final String PRODUCT_ID = "12345687";
	private static final int PRODUCT_EVENT_1 = 13;
	private static final String PRODUCT_EVENT_VALUE_1 = "fge";
	private static final int TWO = 2;
	private static final int VARIABLE_A = 43;
	private static final int VARIABLE_B = 3;
	private static final String VARIABLE_VALUE_A = "A";
	private static final String VARIABLE_VALUE_B = "B";
	private static final String UNSUPPORTED_EVENT_MESSAGE = "Product\'s event is unsupported in the analytic bean: alienEvent";
	private static final String WRONG_EVENT = "alienEvent";

	@Mock
	@SuppressWarnings("rawtypes")
	private Appender appender;
	@Captor
	private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	@Spy
	private AnalyticBean bean;

	private OmniVariable.Conversion conversionVariableA;
	private OmniVariable.Conversion conversionVariableB;
	private OmniVariable.Event eventA;
	private OmniVariable.Event eventB;
	private OmniVariable.Hierarchy hierarchyVariableA;
	private OmniVariable.Hierarchy hierarchyVariableB;
	private OmniVariable.List listVariableA;
	private OmniVariable.List listVariableB;
	private ProductBean product;
	private OmniVariable.Event productEvent;
	private ProductBean simpleProduct;
	private OmniVariable.Traffic trafficVariableA;
	private OmniVariable.Traffic trafficVariableB;

	/* Product */

	@Test
	public void addProduct() {
		bean.addProduct(product);

		assertNotNull("Set exists", bean.getProducts());
		assertEquals("Set contains one product", ONE_ITEM, bean.getProducts().size());
		assertTrue("Product was added", bean.getProducts().contains(product));
		assertTrue("Product event was copied", bean.getEvents().keySet().contains(productEvent));
	}

	@Test(expected=NullPointerException.class)
	public void nullProduct() {
		product = null;

		bean.addProduct(product);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void wrongEvent() throws UnsupportedVariableException {
		doThrow(new UnsupportedVariableException(WRONG_EVENT)).when(bean).addVariable(isA(OmniVariable.Event.class));

		bean.addProduct(product);

		assertNotNull("Set exists", bean.getProducts());
		assertEquals("Set contains one product", ONE_ITEM, bean.getProducts().size());
		assertTrue("Product was added", bean.getProducts().contains(product));
		assertFalse("Product event wasn't copied", bean.getEvents().keySet().contains(productEvent));
		verify(appender, times(TWO)).doAppend(captorLoggingEvent.capture());
		{
			final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

			assertEquals(Level.WARN, loggingEvent.getLevel());
			assertEquals(UNSUPPORTED_EVENT_MESSAGE, loggingEvent.getFormattedMessage());
		}
	}

	/* getProducts */

	@Test
	public void emptyProductSet() {
		assertNotNull("Set exists", bean.getProducts());
		assertEquals("Set of products is empty", NO_ITEMS, bean.getProducts().size());
	}

	/* Get variable collections by type */

	@Test
	public void getConversionVariables() throws UnsupportedVariableException {
		Map<OmniVariable, String> result;

		assumeThat("Map of conversion variables is empty", NO_ITEMS, equalTo(bean.getConversionVariables().size()));

		addMoreVariables();

		result = bean.getConversionVariables();

		assertNotNull("Map exists", result);
		assertEquals("Conversion variable A is found", VARIABLE_VALUE_A, result.remove(conversionVariableA));
		assertEquals("Conversion variable B is found", VARIABLE_VALUE_B, result.remove(conversionVariableB));
		assertTrue("All conversion variables are found", result.isEmpty());
	}

	@Test
	public void getEvents() throws UnsupportedVariableException {
		Set<OmniVariable> expectedVariables;

		assumeThat("Map of events is empty", NO_ITEMS, equalTo(bean.getEvents().size()));
		assumeThat("Set of products is empty", NO_ITEMS, equalTo(bean.getProducts().size()));

		addMoreVariables();
		bean.addProduct(product);

		assertNotNull("Map exists", bean.getEvents());
		expectedVariables = bean.getEvents().keySet();
		assertTrue("Event A is found", expectedVariables.remove(eventA));
		assertTrue("Event B is found", expectedVariables.remove(eventB));
		assertTrue("Product event is found", expectedVariables.remove(productEvent));
		assertTrue("All events are found", expectedVariables.isEmpty());
	}

	@Test
	public void getHierarchyVariables() throws UnsupportedVariableException {
		Map<OmniVariable, String> result;

		assumeThat("Map of hierarchy variables is empty", NO_ITEMS, equalTo(bean.getHierarchyVariables().size()));

		addMoreVariables();

		result = bean.getHierarchyVariables();

		assertNotNull("Map exists", result);
		assertEquals("Hierarchy variable A is found", VARIABLE_VALUE_A, result.remove(hierarchyVariableA));
		assertEquals("Hierarchy variable B is found", VARIABLE_VALUE_B, result.remove(hierarchyVariableB));
		assertTrue("All hierarchy variables are found", result.isEmpty());
	}

	@Test
	public void getListVariables() throws UnsupportedVariableException {
		Map<OmniVariable, String> result;

		assumeThat("Map of hierarchy variables is empty", NO_ITEMS, equalTo(bean.getListVariables().size()));

		addMoreVariables();

		result = bean.getListVariables();

		assertNotNull("Map exists", result);
		assertEquals("List variable A is found", VARIABLE_VALUE_A, result.remove(listVariableA));
		assertEquals("List variable B is found", VARIABLE_VALUE_B, result.remove(listVariableB));
		assertTrue("All list variables are found", result.isEmpty());
	}

	@Test
	public void getTrafficVariables() throws UnsupportedVariableException {
		Map<OmniVariable, String> result;

		assumeThat("Map of traffic variables is empty", NO_ITEMS, equalTo(bean.getTrafficVariables().size()));

		addMoreVariables();

		result = bean.getTrafficVariables();

		assertNotNull("Map exists", result);
		assertEquals("Traffic variable A is found", VARIABLE_VALUE_A, result.remove(trafficVariableA));
		assertEquals("Traffic variable B is found", VARIABLE_VALUE_B, result.remove(trafficVariableB));
		assertTrue("All traffic variables are found", result.isEmpty());
	}

	/* toString() */

	@Test
	public void beanToString() throws UnsupportedVariableException {
		String testString;
		assumeThat("Set of products is empty", NO_ITEMS, equalTo(bean.getProducts().size()));

		addMoreVariables();
		bean.addProduct(product);

		testString = bean.toString();

		assertNotNull("String exists", testString);
		assertEquals("String", "eVar3='B',eVar43='A',events='scCheckout,event13,event27',hier3='B',hier43='A',list3='B',list43='A',products=';12345687;;;event13=fge;',prop3='B',prop43='A'", testString);
	}

	@Test
	public void emptyBean() throws UnsupportedVariableException {
		String testString;
		assumeThat("Set of products is empty", NO_ITEMS, equalTo(bean.getProducts().size()));

		testString = bean.toString();
		assertNotNull("String exists", testString);
		assertTrue("String is empty", testString.isEmpty());
	}

	@Test
	public void productsOnly() throws UnsupportedVariableException {
		String testString;
		assumeThat("Set of products is empty", NO_ITEMS, equalTo(bean.getProducts().size()));

		bean.addProduct(simpleProduct);

		testString = bean.toString();

		assertNotNull("String exists", testString);
		assertEquals("String", "products=';12345687;;;;'", testString);
	}

	@Test
	public void withoutEvents() throws UnsupportedVariableException {
		String testString;
		assumeThat("Set of products is empty", NO_ITEMS, equalTo(bean.getProducts().size()));

		conversionVariableA = new OmniVariable.Conversion(VARIABLE_A);
		conversionVariableB = new OmniVariable.Conversion(VARIABLE_B);
		hierarchyVariableA = new OmniVariable.Hierarchy(VARIABLE_A);
		hierarchyVariableB = new OmniVariable.Hierarchy(VARIABLE_B);
		trafficVariableA = new OmniVariable.Traffic(VARIABLE_A);
		trafficVariableB = new OmniVariable.Traffic(VARIABLE_B);
		bean.addVariable(conversionVariableA, VARIABLE_VALUE_A);
		bean.addVariable(conversionVariableB, VARIABLE_VALUE_B);
		bean.addVariable(hierarchyVariableA, VARIABLE_VALUE_A);
		bean.addVariable(hierarchyVariableB, VARIABLE_VALUE_B);
		bean.addVariable(trafficVariableA, VARIABLE_VALUE_A);
		bean.addVariable(trafficVariableB, VARIABLE_VALUE_B);
		bean.addProduct(simpleProduct);

		testString = bean.toString();

		assertNotNull("String exists", testString);
		assertEquals("String", "eVar3='B',eVar43='A',hier3='B',hier43='A',products=';12345687;;;;',prop3='B',prop43='A'", testString);
	}

	@Test
	public void withoutProducts() throws UnsupportedVariableException {
		String testString;
		assumeThat("Set of products is empty", NO_ITEMS, equalTo(bean.getProducts().size()));

		addMoreVariables();

		testString = bean.toString();

		assertNotNull("String exists", testString);
		assertEquals("String", "eVar3='B',eVar43='A',events='scCheckout,event27',hier3='B',hier43='A',list3='B',list43='A',prop3='B',prop43='A'", testString);
	}

	@Test
	public void withoutVariables() throws UnsupportedVariableException {
		String testString;
		assumeThat("Set of products is empty", NO_ITEMS, equalTo(bean.getProducts().size()));

		eventA = new OmniVariable.Event(EVENT_1);
		eventB = new OmniVariable.Event(EVENT_2);
		bean.addVariable(eventA);
		bean.addVariable(eventB);
		bean.addProduct(product);

		testString = bean.toString();

		assertNotNull("String exists", testString);
		assertEquals("String", "events='scCheckout,event13,event27',products=';12345687;;;event13=fge;'", testString);
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		Logger logger;

		logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(appender);
		productEvent = new OmniVariable.Event(PRODUCT_EVENT_1);
		product = new ProductBean();
		product.setProduct(PRODUCT_ID);
		product.addVariable(productEvent, PRODUCT_EVENT_VALUE_1);
		simpleProduct = new ProductBean();
		simpleProduct.setProduct(PRODUCT_ID);
	}

	private void addMoreVariables() throws UnsupportedVariableException {
		conversionVariableA = new OmniVariable.Conversion(VARIABLE_A);
		conversionVariableB = new OmniVariable.Conversion(VARIABLE_B);
		eventA = new OmniVariable.Event(EVENT_1);
		eventB = new OmniVariable.Event(EVENT_2);
		hierarchyVariableA = new OmniVariable.Hierarchy(VARIABLE_A);
		hierarchyVariableB = new OmniVariable.Hierarchy(VARIABLE_B);
		listVariableA = new OmniVariable.List(VARIABLE_A);
		listVariableB = new OmniVariable.List(VARIABLE_B);
		trafficVariableA = new OmniVariable.Traffic(VARIABLE_A);
		trafficVariableB = new OmniVariable.Traffic(VARIABLE_B);
		bean.addVariable(conversionVariableA, VARIABLE_VALUE_A);
		bean.addVariable(conversionVariableB, VARIABLE_VALUE_B);
		bean.addVariable(eventA);
		bean.addVariable(eventB);
		bean.addVariable(hierarchyVariableA, VARIABLE_VALUE_A);
		bean.addVariable(hierarchyVariableB, VARIABLE_VALUE_B);
		bean.addVariable(listVariableA, VARIABLE_VALUE_A);
		bean.addVariable(listVariableB, VARIABLE_VALUE_B);
		bean.addVariable(trafficVariableA, VARIABLE_VALUE_A);
		bean.addVariable(trafficVariableB, VARIABLE_VALUE_B);
	}

}