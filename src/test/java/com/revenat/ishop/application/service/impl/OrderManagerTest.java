package com.revenat.ishop.application.service.impl;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.infrastructure.exception.ResourceNotFoundException;
import com.revenat.ishop.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.ishop.infrastructure.exception.flow.ValidationException;
import com.revenat.ishop.infrastructure.exception.security.AccessDeniedException;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OrderManagerTest {
	private static final String CLIENT_NAME = "Jack";
	private static final String CLIENT_EMAIL = "jack@test.com";
	private static final long ORDER_ID = 1L;

	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Mock
	private AuthenticationService authService;
	@Mock
	private OrderService orderService;
	@Mock
	private FeedbackService feedbackService;

	private OrderManager orderManager;

	@Before
	public void setUp() {
		orderManager = new OrderManagerImpl(authService, orderService, feedbackService);
//		Account clientAccount = new Account(CLIENT_NAME, CLIENT_EMAIL);
		ClientAccount clientAccount = new ClientAccount(1, CLIENT_NAME, CLIENT_EMAIL, "");
		clientAccount.setId(1);
		when(authService.getAuthenticatedUserAccount(Mockito.any(ClientSession.class))).thenReturn(clientAccount);
		Order order = new Order();
		order.setId(ORDER_ID);
		when(orderService.createOrder(Mockito.any(), Mockito.anyInt())).thenReturn(order);
	}

	@Test
	public void shouldNowAllowToPlaceOrderIfShoppingCartIsEmty() throws Exception {
		ClientSession session = createSessionWithEmptyShoppingCart();
		expected.expect(ValidationException.class);
		expected.expectMessage(containsString("provided shopping cart is null or empty"));

		orderManager.placeOrder(session);
	}

	@Test
	public void shouldNowAllowToPlaceOrderWithNullShoppingCart() throws Exception {
		ClientSession session = createSessionWithoutShoppingCart();
		expected.expect(ValidationException.class);
		expected.expectMessage(containsString("provided shopping cart is null or empty"));

		orderManager.placeOrder(session);
	}

	@Test
	public void shouldReturnIdOfCreatedOrder() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();

		long placeOrder = orderManager.placeOrder(session);

		assertThat(placeOrder, greaterThan(0L));
	}

	@Test
	public void shouldClearShoppingCartAfterOrderWasPlaced() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();

		orderManager.placeOrder(session);

		assertTrue(session.getShoppingCart().isEmpty());
	}
	
	@Test
	public void shouldSendNotificationAfterOrderWasPlaced() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();

		orderManager.placeOrder(session);

		verify(feedbackService).sendNewOrderNotification(CLIENT_EMAIL, ORDER_ID);
	}

	@Test
	public void shouldNowAllowToGetOrderByWrongId() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();
		long wrongId = 10L;
		expected.expect(ResourceNotFoundException.class);
		expected.expectMessage(containsString("Order with id: 10 not found"));

		orderManager.getById(wrongId, session);
	}

	@Test
	public void shouldNowAllowToGetOrderBelongingToOtherClient() throws Exception {
		long orderId = 3;
		returnAccount(createAccountWithId(5));
		returnOrder(createOrderWithIdAndAccountId(3, 10));
		ClientSession session = createSessionWithShoppingCartWithProduct();
		expected.expect(AccessDeniedException.class);
		expected.expectMessage(containsString("Account with id=5 is not the owner for the order with id=3"));

		orderManager.getById(orderId, session);
	}

	@Test
	public void shouldAllowToFindOrderById() throws Exception {
		long orderId = 1;
		int accountId = 5;
		returnAccount(createAccountWithId(accountId));
		returnOrder(createOrderWithIdAndAccountId(orderId, accountId));
		ClientSession session = createSessionWithShoppingCartWithProduct();

		Order order = orderManager.getById(orderId, session);

		assertThat(order.getId(), equalTo(orderId));
	}

	@Test
	public void shouldAllowToFindOrdersByClient() throws Exception {
		int accountId = 5;
		returnAccount(createAccountWithId(accountId));
		List<Order> orders = Arrays.asList(createOrderWithIdAndAccountId(1, accountId),
				createOrderWithIdAndAccountId(2, accountId));
		returnOrdersByAccountId(orders, accountId);
		ClientSession session = createSessionWithShoppingCartWithProduct();

		int anyPage = 1;
		int anyLimit = 10;
		List<Order> clientOrders = orderManager.findByClient(session, anyPage, anyLimit);

		assertThat(clientOrders, equalTo(orders));
	}

	@Test
	public void shouldNowAllowToFindOrdersForPageLessThanOne() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();
		int page = 0;
		int anyLimit = 10;
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("page number can not be less that 1"));
		
		orderManager.findByClient(session, page, anyLimit);
	}
	
	@Test
	public void shouldNowAllowToFindOrdersWithLimitLessThanOne() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();
		int anyPage = 1;
		int limit = 0;
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("limit can not be less that 1"));
		
		orderManager.findByClient(session, anyPage, limit);
	}

	@Test
	public void shouldAllowToCountOrdersBtClient() throws Exception {
		int accountId = 5;
		int expectedCount = 2;
		returnAccount(createAccountWithId(accountId));
		returnOrderCountByAccountId(expectedCount, accountId);
		ClientSession session = createSessionWithShoppingCartWithProduct();

		int orderCount = orderManager.coundByClient(session);

		assertThat(orderCount, equalTo(expectedCount));
	}

	private void returnOrderCountByAccountId(int orderCount, int accountId) {
		when(orderService.countByAccountId(Mockito.eq(accountId))).thenReturn(orderCount);
	}

	private void returnOrdersByAccountId(List<Order> orders, int accountId) {
		when(orderService.findByAccountId(Mockito.eq(accountId), Mockito.anyInt(), Mockito.anyInt())).thenReturn(orders);
	}

	private void returnOrder(Order order) {
		when(orderService.findById(Mockito.anyLong())).thenReturn(order);
	}

	private void returnAccount(Account account) {
		ClientAccount a = new ClientAccount(account.getId(), account.getName(), account.getEmail(), account.getAvatarUrl());
		when(authService.getAuthenticatedUserAccount(Mockito.any(ClientSession.class))).thenReturn(a);
	}

	private Account createAccountWithId(int id) {
		Account account = new Account();
		account.setId(id);
		return account;
	}

	private Order createOrderWithIdAndAccountId(long id, int accountId) {
		Order order = new Order();
		order.setId(id);
		order.setAccountId(accountId);
		return order;
	}

	private ClientSession createSessionWithShoppingCartWithProduct() {
		Product p = new Product();
		p.setId(1);
		ClientSession session = new ClientSession();
		session.getShoppingCart().addProduct(p, 1);
		return session;
	}

	private ClientSession createSessionWithoutShoppingCart() {
		ClientSession session = new ClientSession();
		session.setShoppingCart(null);
		return session;
	}

	private ClientSession createSessionWithEmptyShoppingCart() {
		ClientSession session = new ClientSession();
		return session;
	}

}
