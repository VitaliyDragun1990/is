package com.revenat.ishop.service.application.impl;

import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.entity.Account;
import com.revenat.ishop.model.CurrentAccount;
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
	public boolean isAuthenticated(HttpSession userSession) {
		return getCurrentAccount(userSession) != null;
	}

	@Override
	public void authenticate(Credentials credentials, HttpSession userSession) {
		String authToken = ((AuthenticationTokenCredentials) credentials).getAuthToken();
		SocialAccount socialAccount = getUserSocialAccount(authToken);
		Account account = getUserAccount(socialAccount);
		setCurrentAccount(userSession, account);
	}

	@Override
	public String getAuthenticationUrl() {
		return socialService.getAuthorizeUrl();
	}

	@Override
	public CurrentAccount getAuthenticatedUserAccount(HttpSession userSession) {
		return getCurrentAccount(userSession);
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

	private CurrentAccount getCurrentAccount(HttpSession userSession) {
		return (CurrentAccount) userSession.getAttribute(Attribute.CURRENT_USER);
	}

	private void setCurrentAccount(HttpSession clientSession, CurrentAccount account) {
		clientSession.setAttribute(Attribute.CURRENT_USER, account);
	}
}
