package com.revenat.ishop.application.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.infrastructure.exception.PersistenceException;
import com.revenat.ishop.infrastructure.exception.flow.FlowException;
import com.revenat.ishop.infrastructure.exception.security.AuthenticationException;
import com.revenat.ishop.infrastructure.repository.AccountRepository;
import com.revenat.ishop.infrastructure.service.AvatarService;
import com.revenat.ishop.infrastructure.service.SocialAccount;
import com.revenat.ishop.infrastructure.service.SocialService;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;

/**
 * Default implementation of the {@link AuthenticationService} interface which
 * is responsible for authenticating users via theirs social network accounts.
 * 
 * @author Vitaly Dragun
 *
 */
@Service
public class SocialAuthenticationService implements AuthenticationService {
	private SocialService socialService;
	private AvatarService avatarService;
	private AccountRepository accountRepository;
	private Transformer transformer;

	@Autowired
	public SocialAuthenticationService(SocialService socialService, AvatarService avatarService,
			AccountRepository accountRepository, Transformer transformer) {
		this.socialService = socialService;
		this.avatarService = avatarService;
		this.accountRepository = accountRepository;
		this.transformer = transformer;
	}

	@Override
	public boolean isAuthenticated(ClientSession session) {
		return session.getAccount() != null;
	}

	@Transactional(readOnly=false)
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
		if (isAuthenticated(session)) {
			return session.getAccount();
		} else {
			throw new AuthenticationException(" Can not get clientAccount: "
					+ "user with specified client session is not authenticated: " + session);
		}
	}
	
	@Override
	public void logout(ClientSession session) {
		session.setAccount(null);
	}

	private SocialAccount getUserSocialAccount(String authToken) {
		return socialService.getSocialAccount(authToken);
	}

	private ClientAccount getClientAccount(SocialAccount socialAccount) {
		Account account = accountRepository.findByEmail(socialAccount.getEmail());
		if (account == null) {
			account = createNewAccount(socialAccount);
		}
		return transformer.transform(account, ClientAccount.class);
	}

	private Account createNewAccount(SocialAccount socialAccount) {
		String avatarUrl = null;
		
		try {
			avatarUrl = avatarService.saveAvatar(socialAccount.getAvatarUrl());
			Account account = new Account(socialAccount.getName(), socialAccount.getEmail(), avatarUrl);
			return accountRepository.save(account);
		} catch (PersistenceException | IOException e) {
			avatarService.deleteAvatarIfExists(avatarUrl);
			throw new FlowException("Error while creating new user's account", e);
		}
	}
}
