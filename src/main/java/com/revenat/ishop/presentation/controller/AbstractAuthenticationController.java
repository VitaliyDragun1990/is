package com.revenat.ishop.presentation.controller;

import com.revenat.ishop.application.service.AuthenticationService;

public abstract class AbstractAuthenticationController extends AbstractController {
	private static final long serialVersionUID = -7734947436884433851L;
	
	protected final AuthenticationService authService;

	public AbstractAuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}
}
