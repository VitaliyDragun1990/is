package com.revenat.ishop.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CookieUtilsTest {
	private static final String COOKIE_NAME = "cookieName";
	private static final String COOKIE_VALUE = "cookieValue";
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Captor
	private ArgumentCaptor<Cookie> captor;

	@Test
	public void shouldFindCookieWithSpecifiedName() throws Exception {
		Cookie cookie = createCookie(COOKIE_NAME, COOKIE_VALUE);
		when(request.getCookies()).thenReturn(new Cookie[] {cookie});
		
		Cookie result = CookieUtils.findCookie(request, COOKIE_NAME);
		
		assertThat(result.getName(), equalTo(COOKIE_NAME));
		assertThat(result.getValue(), equalTo(COOKIE_VALUE));
	}
	
	@Test
	public void shouldReturnNullIfCanNotFindCookieWithSpecifiedName() throws Exception {
		when(request.getCookies()).thenReturn(new Cookie[0]);
		
		Cookie result = CookieUtils.findCookie(request, COOKIE_NAME);
		
		assertNull("result should be null if no cookie was found", result);
	}
	
	@Test
	public void shouldReturnNullIfCookieWithSpecifiedNameContainsEmptyValue() throws Exception {
		Cookie cookie = createCookie(COOKIE_NAME, "");
		when(request.getCookies()).thenReturn(new Cookie[] {cookie});
		
		Cookie result = CookieUtils.findCookie(request, COOKIE_NAME);
		
		assertNull("result should be null if cookie contains empty value", result);
	}
	
	@Test
	public void shouldReturnNullIfCookieWithSpecifiedNameContainsNullValue() throws Exception {
		Cookie cookie = createCookie(COOKIE_NAME, null);
		when(request.getCookies()).thenReturn(new Cookie[] {cookie});
		
		Cookie result = CookieUtils.findCookie(request, COOKIE_NAME);
		
		assertNull("result should be null if cookie contains null value", result);
	}
	
	@Test
	public void shouldAllowToSetCookie() throws Exception {
		CookieUtils.setCookie(COOKIE_NAME, COOKIE_VALUE, 100, response);
		
		verify(response).addCookie(captor.capture());
		Cookie setCookie = captor.getValue();
		assertThat(setCookie.getName(), equalTo(COOKIE_NAME));
		assertThat(setCookie.getValue(), equalTo(COOKIE_VALUE));
		assertThat(setCookie.getMaxAge(), equalTo(100));
	}
	
	private static Cookie createCookie(String name, String value) {
		return new Cookie(name, value);
	}

}
