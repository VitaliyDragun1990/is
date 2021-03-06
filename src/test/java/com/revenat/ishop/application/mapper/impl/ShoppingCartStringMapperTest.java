package com.revenat.ishop.application.mapper.impl;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.application.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.application.service.impl.ShoppingCartServiceImpl;
import com.revenat.ishop.application.transform.transformer.impl.BasicFieldProvider;
import com.revenat.ishop.application.transform.transformer.impl.SimpleDTOTransformer;
import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.infrastructure.repository.ProductRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ShoppingCartStringMapperTest {
	
	@Mock
	private ProductRepository productRepository;

	private ShoppingCartStringMapper mapper;
	
	@Before
	public void setup() {
		mapper = new ShoppingCartStringMapper(new ShoppingCartServiceImpl(productRepository, new SimpleDTOTransformer(new BasicFieldProvider())));
		when(productRepository.findById(Mockito.any(Integer.class))).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				int productId = invocation.getArgument(0);
				Product p = new Product();
				p.setId(productId);
				Producer pr = new Producer();
				pr.setId(productId);
				Category ct = new Category();
				ct.setId(productId);
				p.setProducer(pr);
				p.setCategory(ct);
				return p;
			}
		});
	}
	
	@Test
	public void shouldAllowToMarshallShoppingCartWithProductsIntoCookieString() throws Exception {
		ProductDTO productA = new ProductDTO();
		productA.setId(1);
		ProductDTO productB = new ProductDTO();
		productB.setId(2);
		
		ShoppingCart cart = new ShoppingCart();
		cart.addProduct(productA, 5);
		cart.addProduct(productB, 3);
		
		String cookieString = mapper.marshall(cart);
		
		assertThat(cookieString, containsString("1-5"));
		assertThat(cookieString, containsString("2-3"));
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToMarshallNullShoppingCart() throws Exception {
		mapper.marshall(null);
	}
	
	@Test
	public void shouldAllowToUnmarshallShoppingCartFromCookieString() throws Exception {
		String cookieString = "1-5|25-10|18-5";
		
		ShoppingCart shoppingCart = mapper.unmarshall(cookieString);
		
		assertThat(shoppingCart.getTotalCount(), equalTo(20));
		assertContainsItemWithQuantity(shoppingCart, 1, 5);
		assertContainsItemWithQuantity(shoppingCart, 25, 10);
		assertContainsItemWithQuantity(shoppingCart, 18, 5);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToUnmarshallNullCookieString() throws Exception {
		mapper.unmarshall(null);
	}
	
	@Test
	public void shouldReturnEmptyShoppingCartIfCanNotUnmarshallCookieString() throws Exception {
		String invalidCookieString = "adaw|+|";
		
		ShoppingCart cart = mapper.unmarshall(invalidCookieString);
		
		assertThat(cart.getTotalCount(), equalTo(0));
	}
	
	@Test
	public void shouldDropInvalidCartItemSegmentIfExist() throws Exception {
		String cookieStringWithoutInvalidSegment = "1-5|25-";
		
		ShoppingCart shoppingCart = mapper.unmarshall(cookieStringWithoutInvalidSegment);
		
		assertThat(shoppingCart.getTotalCount(), equalTo(5));
		assertContainsItemWithQuantity(shoppingCart, 1, 5);
	}
	
	private static void assertContainsItemWithQuantity(ShoppingCart cart, int productId, int quantity) {
		Collection<ShoppingCartItem> items = cart.getItems();

		assertThat(items, hasItems(allOf(
				hasProperty("product", hasProperty("id", equalTo(productId))),
				hasProperty("quantity", equalTo(quantity))
				)));
	}
}
