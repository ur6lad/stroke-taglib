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

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;

/**
 * Add a product to the analytic bean.
 *
 * @author Vitaliy Berdinskikh
 */
public class ProductTag extends OmniTag {

	private static final String PRODUCT_VARIABLE = "product";

	private String category;
	private String product;
	private ProductBean productBean = new ProductBean();
	private String quantity;
	private String totalCost;

	public void setCategory(String category) {
		this.category = category;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * Return the product bean.
	 *
	 * @return the product bean.
	 */
	@Override
	protected OmniBean bean() {
		return productBean;
	}

	/**
	 * Update bean: copy attributes' values to bean.
	 *
	 * @param omniBean an updated bean
	 * @return true if a bean was updated with new values
	 * @throws NullPointerException if a bean is null
	 */
	protected boolean updateBean(OmniBean omniBean) throws NullPointerException {
		ProductBean productBean = (ProductBean) omniBean;
		boolean result = false;
		final StringWriter writer = new StringWriter();

		productBean.setCategory(category);
		productBean.setProduct(product);
		productBean.setQuantity(quantity);
		productBean.setTotalCost(totalCost);
		try {
			OmniBean analyticBean = super.bean();

			if (null != getJspBody()) {
				getJspContext().setAttribute(PRODUCT_VARIABLE, productBean);
				getJspBody().invoke(writer);
				getJspContext().getOut().println(writer.toString());
			}

			/*
			 * As we rewrite setBean() the super.bean() always returns the analytic bean from parent tag or request.
			 *
			 * But we need check the case
			 *	   <s:product product="1"><s:product product="2"/></s:product>
			 */
			if (analyticBean instanceof AnalyticBean) {
				((AnalyticBean) analyticBean).addProduct(productBean);
				result = true;
			} else {
				logger().warn("Could not get the analytic bean: super.bean() returns {}", analyticBean.getClass());
			}

		} catch (JspException pageException) {
			logger().warn(pageException.getMessage(), pageException);
		} catch (IOException outputException) {
			logger().warn(outputException.getMessage(), outputException);
		}

		return result;
	}

}