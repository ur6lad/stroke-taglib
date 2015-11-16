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

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The analytic bean.
 *
 * Put here all type of variables and products.
 *
 * @author Vitaliy Berdinskikh
 */
public class AnalyticBean extends OmniBean {

	private static final char COMMA = ',';

	private Set<ProductBean> products;

	public AnalyticBean() {
		super();
		products = new TreeSet<ProductBean>();
	}

	/**
	 * Add the product.
	 *
	 * See {@link ProductBean}.
	 *
	 * @param product the added product
	 * @see ProductBean
	 */
	public void addProduct(ProductBean product) {
		products.add(product);
		for (OmniVariable event : product.getEvents().keySet()) {
			try {
				addVariable(event);
			} catch (UnsupportedVariableException unsupportedVariable) {
				logger().warn("Product\'s event is unsupported in the analytic bean: {}", unsupportedVariable.getMessage());
			}
		}
	}

	public Map<OmniVariable, String> getConversionVariables() {
		return OmniTools.filterMap(getVariables(), OmniVariable.Conversion.class);
	}

	public Map<OmniVariable, String> getEvents() {
		return OmniTools.filterMap(getVariables(), OmniVariable.Event.class);
	}

	public Map<OmniVariable, String> getHierarchyVariables() {
		return OmniTools.filterMap(getVariables(), OmniVariable.Hierarchy.class);
	}

	public Map<OmniVariable, String> getListVariables() {
		return OmniTools.filterMap(getVariables(), OmniVariable.List.class);
	}

	public Set<ProductBean> getProducts() {
		Set<ProductBean> result = new TreeSet<ProductBean>();

		result.addAll(products);

		return result;
	}

	public Map<OmniVariable, String> getTrafficVariables() {
		return OmniTools.filterMap(getVariables(), OmniVariable.Traffic.class);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> stringMap = OmniTools.beanToMap(this);

		if (!stringMap.isEmpty()) {
			buffer.append(OmniTools.mapToString(stringMap, COMMA));
		}

		return buffer.toString();
	}

}