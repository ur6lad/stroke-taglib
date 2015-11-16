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

/**
 * The product bean. It has fields category, product, quantity, cost fields. It could have events and conversion variables, both should have value.
 *
 * @author Vitaliy Berdinskikh
 */
public class ProductBean extends OmniBean implements Comparable<ProductBean> {

	private static final String EMPTY_VARIABLE = "Product bean does not support the variable without value.";
	private static final String SEMICOLON = ";";

	private String category;
	private String product;
	private String quantity;
	private String totalCost;

	/**
	 * Add the variable with a value. Use to add events and conversion variables.
	 *
	 * The product bean doesn't support any variables without value.
	 */
	@Override
	public void addVariable(OmniVariable variable, String value) throws UnsupportedVariableException {
		if (null == value) {
			throw new UnsupportedVariableException(EMPTY_VARIABLE);
		}

		if (variable instanceof OmniVariable.Conversion || variable instanceof OmniVariable.Event) {
			super.addVariable(variable, value);
		} else {
			throw new UnsupportedVariableException(variable.toString());
		}
	}

	@Override
	public int compareTo(ProductBean that) {
		int result = 0;

		if (null != product && null != that && null != that.getProduct()) {
			result = product.compareTo(that.getProduct());
		}

		return result;
	}

	@Override
	public boolean equals(Object that) {
		if (that instanceof ProductBean) {
			ProductBean bean = (ProductBean) that;

			if (null != product) {
				return product.equals(bean.getProduct());
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (null != product) {
			return product.hashCode();
		} else {
			return super.hashCode();
		}
	}

	public Map<OmniVariable, String> getConversionVariables() {
		return OmniTools.filterMap(getVariables(), OmniVariable.Conversion.class);
	}

	public Map<OmniVariable, String> getEvents() {
		return OmniTools.filterMap(getVariables(), OmniVariable.Event.class);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(getCategory()).append(SEMICOLON);
		buffer.append(getProduct()).append(SEMICOLON);
		buffer.append(getQuantity()).append(SEMICOLON);
		buffer.append(getTotalCost()).append(SEMICOLON);
		buffer.append(OmniTools.mapToString(getEvents())).append(SEMICOLON);
		buffer.append(OmniTools.mapToString(getConversionVariables()));

		return buffer.toString();
	}

	public String getCategory() {
		return OmniTools.notNull(category);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProduct() {
		return OmniTools.notNull(product);
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getQuantity() {
		return OmniTools.notNull(quantity);
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getTotalCost() {
		return OmniTools.notNull(totalCost);
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

}