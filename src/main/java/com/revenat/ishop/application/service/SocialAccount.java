package com.revenat.ishop.application.service;

/**
 * This component represents abstraction over user's account from some kind of social network.
 * 
 * @author Vitaly Dragun
 *
 */
public class SocialAccount {
	private final String name;
	private final String email;

	public SocialAccount(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
}
