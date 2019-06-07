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
import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.exception.ResourceNotFoundException;
import com.revenat.ishop.application.exception.security.AccessDeniedException;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.application.transform.transformer.impl.BasicFieldProvider;
import com.revenat.ishop.application.transform.transformer.impl.SimpleDTOTransformer;
import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.domain.exception.flow.InvalidParameterException;
import com.revenat.ishop.domain.exception.flow.ValidationException;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OrderManagerTest {
	private static final String CLIENT_NAME = "Jack";
	private static final String CLIENT_EMAIL = "jack@test.com";
	private static final long ORDER_ID = 1L;
	private static final Locale CLIENT_LOCALE = Locale.getDefault();

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
		orderManager = new OrderManagerImpl(authService, orderService, feedbackService, new SimpleDTOTransformer(new BasicFieldProvider()));
//		Account clientAccount = new Account(CLIENT_NAME, CLIENT_EMAIL);
		ClientAccount clientAccount = new ClientAccount(1, CLIENT_NAME, CLIENT_EMAIL, "");
		clientAccount.setId(1);
		when(authService.getAuthenticatedUserAccount(Mockito.any(ClientSession.class))).thenReturn(clientAccount);
		OrderDTO order = new OrderDTO();
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

//	@Test
	public void shouldReturnIdOfCreatedOrder() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();

		long placeOrder = orderManager.placeOrder(session);

		assertThat(placeOrder, greaterThan(0L));
	}

//	@Test
	public void shouldClearShoppingCartAfterOrderWasPlaced() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();

		orderManager.placeOrder(session);

		assertTrue(session.getShoppingCart().isEmpty());
	}
	
//	@Test
	public void shouldSendNotificationAfterOrderWasPlaced() throws Exception {
		ClientSession session = createSessionWithShoppingCartWithProduct();
		ArgumentCaptor<OrderDTO> captor = ArgumentCaptor.forClass(OrderDTO.class);

		orderManager.placeOrder(session);

		verify(feedbackService).sendNewOrderNotification(CLIENT_EMAIL, CLIENT_LOCALE, captor.capture());
		OrderDTO savedOrder = captor.getValue();
		assertThat(savedOrder.getId(), equalTo(ORDER_ID));
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

		OrderDTO order = orderManager.getById(orderId, session);

		assertThat(order.getId(), equalTo(orderId));
	}

	@Test
	public void shouldAllowToFindOrdersByClient() throws Exception {
		int accountId = 5;
		returnAccount(createAccountWithId(accountId));
		List<OrderDTO> orders = Arrays.asList(createOrderWithIdAndAccountId(1, accountId),
				createOrderWithIdAndAccountId(2, accountId));
		returnOrdersByAccountId(orders, accountId);
		ClientSession session = createSessionWithShoppingCartWithProduct();

		int anyPage = 1;
		int anyLimit = 10;
		List<OrderDTO> clientOrders = orderManager.findByClient(session, anyPage, anyLimit);

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

	private void returnOrdersByAccountId(List<OrderDTO> orders, int accountId) {
		when(orderService.findByAccountId(Mockito.eq(accountId), Mockito.anyInt(), Mockito.anyInt())).thenReturn(orders);
	}

	private void returnOrder(OrderDTO order) {
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

	private OrderDTO createOrderWithIdAndAccountId(long id, int accountId) {
		OrderDTO order = new OrderDTO();
		order.setId(id);
		order.setAccountId(accountId);
		return order;
	}

	private ClientSession createSessionWithShoppingCartWithProduct() {
		ProductDTO p = new ProductDTO();
		p.setId(1);
		ClientSession session = new ClientSession();
		session.getShoppingCart().addProduct(p, 1);
		session.setClientLocale(CLIENT_LOCALE);
		return session;
	}

	private ClientSession createSessionWithEmptyShoppingCart() {
		ClientSession session = new ClientSession();
		return session;
	}

}
