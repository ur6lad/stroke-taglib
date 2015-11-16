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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Common methods for Stroke Taglib.
 *
 * @author Vitaliy Berdinskikh
 */
public class OmniTools {

	/**
	 * This string used as key to save the analytic bean to a request.
	 */
	public static final String ANALYTIC_BEAN_NAME = "SiteCatalyst.analyticBean";

	private static final char APOSTROPHE = '\'';
	private static final String CATEGORY = "category";
	private static final char COMMA = ',';
	private static final char EQUALS = '=';
	private static final String EVENTS = "events";
	private static final char PIPE = '|';
	private static final String PRODUCT = "product";
	private static final String PRODUCTS = "products";
	private static final String QUANTITY = "quantity";
	private static final String TOTAL_COST = "totalCost";

	/**
	 * Convert {@link AnalyticBean} or {@link ProductBean} to the map.
	 *
	 * @param bean the bean
	 * @return the map where variables are keys and variables' values are values.
	 * @see OmniTools#mapToString(Map)
	 */
	static public Map<String, String> beanToMap(OmniBean bean) {
		Map<String, String> variables = new TreeMap<String, String>();
		StringBuilder buffer = new StringBuilder();

		if (null == bean) {
			return variables;
		}

		Map<OmniVariable, String> variableMap = bean.getVariables();

		/*
		 * Particular actions:
		 *
		 * * add events and products if it's the analytic bean
		 * * add category, product, quantity and total cost if it's the product bean
		 */
		if (bean instanceof AnalyticBean) {
			AnalyticBean analyticBean = (AnalyticBean) bean;

			if (!analyticBean.getEvents().isEmpty()) {
				buffer.append(APOSTROPHE).append(mapToString(analyticBean.getEvents(), COMMA)).append(APOSTROPHE);
				variables.put(EVENTS, buffer.toString());
			}
			if (!analyticBean.getProducts().isEmpty()) {
				if (0 < buffer.length()) {
					buffer.delete(0, buffer.length());
				}
				buffer.append(APOSTROPHE).append(setToString(analyticBean.getProducts())).append(APOSTROPHE);
				variables.put(PRODUCTS, buffer.toString());
			}
			for (OmniVariable variable : variableMap.keySet()) {
				String value = null;

				if (variable instanceof OmniVariable.Event) {
					continue;
				}
				if (0 < buffer.length()) {
					buffer.delete(0, buffer.length());
				}
				if (variable instanceof OmniVariable.Standard
						&& !OmniVariable.Standard.Name.Type.Simple.equals(((OmniVariable.Standard) variable).getName().getType())) {
					buffer.append(variableMap.get(variable));
				} else {
					buffer.append(APOSTROPHE).append(variableMap.get(variable)).append(APOSTROPHE);
				}
				value = buffer.toString();
				variables.put(variable.toString(), value);
			}
		} else {
			for (OmniVariable variable : variableMap.keySet()) {
				String value = null;

				if (null != variableMap.get(variable)) {
					if (0 < buffer.length()) {
						buffer.delete(0, buffer.length());
					}
					if (bean instanceof ProductBean) {
						buffer.append(variableMap.get(variable));
					} else if (variable instanceof OmniVariable.Standard
							   && !OmniVariable.Standard.Name.Type.Simple.equals(((OmniVariable.Standard) variable).getName().getType())) {
						buffer.append(variableMap.get(variable));
					} else {
						buffer.append(APOSTROPHE).append(variableMap.get(variable)).append(APOSTROPHE);
					}
					value = buffer.toString();
				}
				variables.put(variable.toString(), value);
			}

		}
		if (bean instanceof ProductBean) {

			ProductBean productBean = (ProductBean) bean;

			variables.put(CATEGORY, productBean.getCategory());
			variables.put(PRODUCT, productBean.getProduct());
			variables.put(QUANTITY, productBean.getQuantity());
			variables.put(TOTAL_COST, productBean.getTotalCost());
		}

		return variables;
	}

	/**
	 * Select keys that are instance of the target class.
	 *
	 * @param map the source map
	 * @param clazz the target class
	 * @return the filtered map
	 */
	static public Map<OmniVariable, String> filterMap(Map<OmniVariable, String> map, Class<? extends OmniVariable> clazz) {
		Map<OmniVariable, String> result = new TreeMap<OmniVariable, String>();

		if (null != map) {
			for (OmniVariable key : map.keySet()) {
				if (clazz.isInstance(key)) {
					result.put(key, map.get(key));
				}
			}
		}

		return result;
	}

	/**
	 * Map's toString(): makes the string with key=value pairs and the pipe separator.
	 *
	 * @param <T> the type of keys
	 * @param map the map
	 * @return string with key=value pairs
	 */
	static public <T extends Object> String mapToString(Map<T, String> map) {
		return mapToString(map, PIPE);
	}

	/**
	 * Map's toString(): makes the string with key=value pairs and the target separator.
	 *
	 * @param <T> the type of keys
	 * @param map the map
	 * @param separator the target separator
	 * @return string with key=value pairs
	 */
	static public <T extends Object> String mapToString(Map<T, String> map, char separator) {
		StringBuilder buffer = new StringBuilder();
		T variable;

		for (Iterator<T> iterator = notNull(map).keySet().iterator(); iterator.hasNext();) {
			variable = iterator.next();

			if (null == variable) {
				if (iterator.hasNext()) {
					continue;
				} else {
					break;
				}
			}

			buffer.append(variable);
			if (null != map.get(variable)) {
				buffer.append(EQUALS).append(map.get(variable));
			}

			if (iterator.hasNext()) {
				buffer.append(separator);
			}
		}
		if (0 < buffer.length() && separator == buffer.charAt(buffer.length() - 1)) {
			buffer.deleteCharAt(buffer.length() - 1);
		}

		return buffer.toString();
	}

	/**
	 * Set's toString(): makes the string with key=value pairs and the comma separator.
	 *
	 * @param <T> the type of items
	 * @param set the set
	 * @return string with items
	 */
	static public <T extends Object> String setToString(Set<T> set) {
		StringBuilder buffer = new StringBuilder();

		if (!set.isEmpty()) {
			T item;
			Iterator<T> iterator = set.iterator();

			while(true) {
				item = iterator.next();

				if (null == item) {
					if (iterator.hasNext()) {
						continue;
					} else {
						break;
					}
				}

				buffer.append(item);

				if (iterator.hasNext()) {
					buffer.append(COMMA);
				} else {
					break;
				}
			}
		}
		if (0 < buffer.length() && COMMA == buffer.charAt(buffer.length() - 1)) {
			buffer.deleteCharAt(buffer.length() - 1);
		}

		return buffer.toString();
	}

	/**
	 * Create new map if needed or return source.
	 *
	 * @param <K> the type of keys
	 * @param <V> the type of values
	 * @param <M> the type of map
	 * @param map the source map
	 * @return not null map
	 */
	@SuppressWarnings("unchecked")
	static public <K, V, M extends Map<K, V>> M notNull(M map) {
		if (null == map) {
			map = (M) new HashMap<K, V>();
		}

		return map;
	}

	/**
	 * Create new string if needed or return source.
	 *
	 * @param string the source string
	 * @return not null string
	 */
	static public String notNull(String string) {
		if (null == string) {
			string = new String();
		}

		return string;
	}

}