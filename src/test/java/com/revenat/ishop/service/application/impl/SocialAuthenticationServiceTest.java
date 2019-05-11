package com.revenat.ishop.service.application.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.ishop.entity.Account;
import com.revenat.ishop.model.ClientAccount;
import com.revenat.ishop.model.ClientSession;
import com.revenat.ishop.model.SocialAccount;
import com.revenat.ishop.repository.AccountRepository;
import com.revenat.ishop.service.application.AuthenticationService.Credentials;
import com.revenat.ishop.service.application.SocialService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SocialAuthenticationServiceTest {
	private static final String AUTHENTICATION_URL = "auth_url";
	private static final String AUTHENTICATION_TOKEN = "auth_token";
	private static final String CLIENT_NAME = "Jack";
	private static final String CLIENT_EMAIL = "jack@test.com";
	

	@Mock
	private SocialService socialService;
	@Mock
	private AccountRepository accountRepository;

	private SocialAuthenticationService service;

	@Before
	public void setUp() {
		service = new SocialAuthenticationService(socialService, accountRepository);
		when(socialService.getSocialAccount(AUTHENTICATION_TOKEN)).thenReturn(new SocialAccount(CLIENT_NAME, CLIENT_EMAIL));
	}

	@Test
	public void shouldTellClientIsAuthenticatedIfAccountPropertyNotNull() throws Exception {
		ClientSession session = new ClientSession();
		session.setAccount(new Account());

		boolean isAuthenticated = service.isAuthenticated(session);

		assertTrue("If account property present, client should be authenticated", isAuthenticated);
	}

	@Test
	public void shouldTellClientIsNotAuthenticatedIfAccountPropertyIsNull() throws Exception {
		ClientSession session = new ClientSession();
		
		boolean isAuthenticated = service.isAuthenticated(session);

		assertFalse("If account property null, client should not be authenticated", isAuthenticated);
	}
	
	@Test
	public void shouldAllowToGetAuthenticationUrl() throws Exception {
		when(socialService.getAuthorizeUrl()).thenReturn(AUTHENTICATION_URL);
		
		String authUrl = service.getAuthenticationUrl();
		
		assertThat(authUrl ,equalTo(AUTHENTICATION_URL));
	}
	
	@Test
	public void shouldAllowToAuthenticateClientViaCredentials() throws Exception {
		Credentials credentials = CredentialsFactory.fromAuthToken(AUTHENTICATION_TOKEN);
		ClientSession session = new ClientSession();
		
		service.authenticate(credentials, session);
		
		assertTrue(service.isAuthenticated(session));
	}
	
	@Test
	public void shouldCreateNewAccountIfClientAuthenticatesForTheFirstTime() throws Exception {
		Credentials credentials = CredentialsFactory.fromAuthToken(AUTHENTICATION_TOKEN);
		ClientSession session = new ClientSession();
		
		service.authenticate(credentials, session);
		
		verify(accountRepository).save(Mockito.any(Account.class));
	}
	
	@Test
	public void shouldNowCreateAccountIfClientHasAlreadyAuthenticatedBefore() throws Exception {
		Account account = new Account(CLIENT_NAME, CLIENT_EMAIL);
		Credentials credentials = CredentialsFactory.fromAuthToken(AUTHENTICATION_TOKEN);
		ClientSession session = new ClientSession();
		when(accountRepository.getByEmail(CLIENT_EMAIL)).thenReturn(account);
		
		service.authenticate(credentials, session);
		
		verify(accountRepository, never()).save(Mockito.any(Account.class));
	}
	
	@Test
	public void shouldAllowToLogoutAuthenticatedUser() throws Exception {
		Credentials credentials = CredentialsFactory.fromAuthToken(AUTHENTICATION_TOKEN);
		ClientSession session = new ClientSession();
		service.authenticate(credentials, session);
		
		service.logout(session);
		
		assertFalse("Should not be authenticated after log-out process", service.isAuthenticated(session));
	}
	
	@Test
	public void shouldAllowToGetAccountOfAuthenticatedClient() throws Exception {
		Credentials credentials = CredentialsFactory.fromAuthToken(AUTHENTICATION_TOKEN);
		ClientSession session = new ClientSession();
		service.authenticate(credentials, session);
		
		ClientAccount account = service.getAuthenticatedUserAccount(session);
		
		assertNotNull(account);
	}
	
	@Test
	public void shouldReturnNullIfAskForAccountOfUnauthenticatedClient() throws Exception {
		ClientSession session = new ClientSession();
		
		
		ClientAccount account = service.getAuthenticatedUserAccount(session);
		
		assertNull(account);
	}
}
