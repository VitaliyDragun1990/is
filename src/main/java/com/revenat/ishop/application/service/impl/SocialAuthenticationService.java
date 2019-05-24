package com.revenat.ishop.application.service.impl;

import java.io.IOException;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.infrastructure.exception.PersistenceException;
import com.revenat.ishop.infrastructure.exception.flow.FlowException;
import com.revenat.ishop.infrastructure.repository.AccountRepository;
import com.revenat.ishop.infrastructure.service.AvatarService;
import com.revenat.ishop.infrastructure.service.SocialAccount;
import com.revenat.ishop.infrastructure.service.SocialService;

/**
 * Default implementation of the {@link AuthenticationService} interface which
 * is responsible for authenticating users via theirs social network accounts.
 * 
 * @author Vitaly Dragun
 *
 */
class SocialAuthenticationService implements AuthenticationService {
	private final SocialService socialService;
	private final AvatarService avatarService;
	private final AccountRepository accountRepository;

	public SocialAuthenticationService(SocialService socialService, AvatarService avatarService,
			AccountRepository accountRepository) {
		this.socialService = socialService;
		this.avatarService = avatarService;
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
		ClientAccount account = getClientAccount(socialAccount);
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

	private ClientAccount getClientAccount(SocialAccount socialAccount) {
		Account account = accountRepository.getByEmail(socialAccount.getEmail());
		if (account == null) {
			account = createNewAccount(socialAccount);
		}
		return new ClientAccount(account.getId(), account.getName(),
				account.getEmail(), account.getAvatarUrl());
	}

	private Account createNewAccount(SocialAccount socialAccount) {
		String avatarUrl = null;
		
		try {
			avatarUrl = avatarService.downloadAvatar(socialAccount.getAvatarUrl());
			Account account = new Account(socialAccount.getName(), socialAccount.getEmail(), avatarUrl);
			accountRepository.save(account);
			return account;
		} catch (PersistenceException | IOException e) {
			avatarService.deleteAvatarIfExists(avatarUrl);
			throw new FlowException("Error while creating new user's account", e);
		}
	}
}
