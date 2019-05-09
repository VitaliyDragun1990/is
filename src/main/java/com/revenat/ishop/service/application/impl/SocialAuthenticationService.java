package com.revenat.ishop.service.application.impl;

import com.revenat.ishop.entity.Account;
import com.revenat.ishop.model.ClientAccount;
import com.revenat.ishop.model.ClientSession;
import com.revenat.ishop.model.SocialAccount;
import com.revenat.ishop.repository.AccountRepository;
import com.revenat.ishop.service.application.AuthenticationService;
import com.revenat.ishop.service.application.SocialService;

/**
 * Default implementation of the {@link AuthenticationService} interface which
 * is responsible for authenticating users via theirs social network accounts.
 * 
 * @author Vitaly Dragun
 *
 */
public class SocialAuthenticationService implements AuthenticationService {
	private final SocialService socialService;
	private final AccountRepository accountRepository;

	public SocialAuthenticationService(SocialService socialService, AccountRepository accountRepository) {
		this.socialService = socialService;
		this.accountRepository = accountRepository;
	}

	@Override
	public boolean isAuthenticated(ClientSession session) {
		return session.getAccount() != null;
	}

	@Override
	public void authenticate(Credentials credentials, ClientSession session) {
		String authToken = ((AuthenticationTokenCredentials) credentials).getAuthToken();
		SocialAccount socialAccount = getUserSocialAccount(authToken);
		Account account = getUserAccount(socialAccount);
		session.setAccount(account);
	}

	@Override
	public String getAuthenticationUrl() {
		return socialService.getAuthorizeUrl();
	}

	@Override
	public ClientAccount getAuthenticatedUserAccount(ClientSession session) {
		return session.getAccount();
	}
	
	@Override
	public void logout(ClientSession session) {
		session.setAccount(null);
	}

	private SocialAccount getUserSocialAccount(String authToken) {
		return socialService.getSocialAccount(authToken);
	}

	private Account getUserAccount(SocialAccount socialAccount) {
		Account account = accountRepository.getByEmail(socialAccount.getEmail());
		if (account == null) {
			account = new Account(socialAccount.getName(), socialAccount.getEmail());
			accountRepository.save(account);
		}
		return account;
	}
}
