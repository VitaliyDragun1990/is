package com.revenat.ishop.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

public class TestExample {
	
	@Before
	public void setUp() {
	}
	
//	@Test
	public void test1() throws Exception {
		try (final WebClient webClient = new WebClient()) {
			final HtmlPage page = webClient.getPage("http://localhost:8080/products");
			assertThat(page.getTitleText(), equalTo("IShop"));
			
			HtmlAnchor buyBtn = page.querySelector("div#product1 a");
			buyBtn.click();
			
			HtmlAnchor addToCartBtn = page.querySelector("a#addToCart");
			addToCartBtn.click();
			
			HtmlSpan cartTotalCount = page.querySelector("span.cart-total-count");
			String textContent = cartTotalCount.getTextContent();
			assertThat(textContent, equalTo("1"));
		}
	}
}
